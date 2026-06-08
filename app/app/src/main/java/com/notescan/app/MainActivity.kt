package com.notescan.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaRouter2
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.notescan.app.R
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log
import androidx.core.content.edit


class MainActivity : AppCompatActivity() {

//    private val pcIPAddress = "192.168.0.108"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var cameraImage: ImageView
    private lateinit var captureImgBtn: Button
    private lateinit var resultText: TextView
    private lateinit var copyTextBtn: Button
    private lateinit var sendTxtBtn: Button

    private lateinit var logOutBtn: Button

    private var currentPhotoPath: String? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraImage = findViewById(R.id.cameraImage)
        captureImgBtn = findViewById(R.id.captureImageBtn)
        resultText = findViewById(R.id.resultText)
        copyTextBtn = findViewById(R.id.copyTextBtn)
        sendTxtBtn = findViewById(R.id.sendTxtBtn)
        logOutBtn = findViewById(R.id.logOutBtn)

        sharedPreferences = getSharedPreferences("NetworkConfig", android.content.Context.MODE_PRIVATE)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    captureImage()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()

                }
            }



        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    currentPhotoPath?.let { path ->
                        val bitmap = BitmapFactory.decodeFile(path)
                        cameraImage.setImageBitmap(bitmap)
                        recognizeText(bitmap)
                    }
                }
            }


        captureImgBtn.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        logOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            // Redirect the user back to the SignInActivity
            val intent = Intent(this, SignInActivity::class.java)

            // CRITICAL SECURITY STEP: Clear the backstack!
            // This prevents the user from clicking the physical "Back" button on their phone to get back into the app after logging out.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }

    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmms", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun captureImage() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Error occured while creating the file", Toast.LENGTH_SHORT).show()
            null
        }
        photoFile?.also {
            val photoUri: Uri =
                FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", it)
            takePictureLauncher.launch(photoUri)
        }
    }

    private fun copyToClipboard(text: String) {
        var clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        var clip = ClipData.newPlainText("recognized text", text)
        clipboard?.setPrimaryClip(clip)
    }


    private fun recognizeText(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image).addOnSuccessListener { ocrText ->
            val extractedText = ocrText.text
            resultText.text = ocrText.text
            resultText.movementMethod = ScrollingMovementMethod()
            copyTextBtn.visibility = Button.VISIBLE
            sendTxtBtn.visibility = Button.VISIBLE
            copyTextBtn.setOnClickListener {
                val clipboard = ContextCompat.getSystemService(
                    this,
                    android.content.ClipboardManager::class.java
                )
                val clip = android.content.ClipData.newPlainText("recognized text", ocrText.text)
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
            }
            sendTxtBtn.setOnClickListener {
                val savedIp = sharedPreferences.getString("pc_ip", null)
                if(savedIp.isNullOrEmpty()){
                    showIpDialog(extractedText)
                }else{
                    sendTextToServer(extractedText, savedIp)
                }

            }

        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to recognize text: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun showIpDialog(textToSend: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Connect to PC")
        builder.setMessage("Enter the PC IP address (e.g., 192.168.1.5)")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        // Pre-fill with existing IP if available
        input.setText(sharedPreferences.getString("pc_ip", ""))
        builder.setView(input)

        builder.setPositiveButton("Save & Send") { _, _ ->
            val ip = input.text.toString().trim()
            if (ip.isNotEmpty()) {
                // Save the IP so we don't have to ask again
                sharedPreferences.edit { putString("pc_ip", ip) }
                sendTextToServer(textToSend, ip)
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
    private fun sendTextToServer(text: String, ip: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://$ip:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(NoteApiService::class.java)
        val request = NoteRequest(text)

        service.sendNote(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Text sent to server", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to send text to server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Connection Failed! Is the PC IP correct?", Toast.LENGTH_SHORT)
                    .show()
                showIpDialog(text)
            }
        })

    }
}
