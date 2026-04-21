package src.main.java.com.notescan.app

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaRouter2
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.IOError
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainActivity : AppCompatActivity() {

    private val pcIPAddress = "192.168.0.108"

    private lateinit var cameraImage: ImageView
    private lateinit var captureImgBtn: Button
    private lateinit var resultText: TextView
    private lateinit var copyTextBtn: Button

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


        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted ->
            if(isGranted){
                captureImage()
            }else{
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()

            }
        }



        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
                success ->
            if(success){
                currentPhotoPath?.let{ path ->
                    val bitmap = BitmapFactory.decodeFile(path)
                    cameraImage.setImageBitmap(bitmap)
                    recognizeText(bitmap)
                }
            }
        }


        captureImgBtn.setOnClickListener{
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmms", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath= absolutePath
        }
    }

    private fun captureImage(){
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException){
            Toast.makeText(this, "Error occured while creating the file", Toast.LENGTH_SHORT).show()
            null
        }
        photoFile?.also {
            val photoUri: Uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", it)
            takePictureLauncher.launch(photoUri)
        }
    }

    private fun copyToClipboard(text: String) {
        var clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        var clip = ClipData.newPlainText("recognized text", text)
        clipboard?.setPrimaryClip(clip)
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun sendToLaptop(scannedText: String, button: Button) {
        button.text = "Sending..."
        button.isEnabled = false // Prevent multiple clicks

        var pcIpAddress = ""
        val retrofit = Retrofit.Builder()
            .baseUrl("http://$pcIpAddress:8080/")
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for ScanRequest object
            .build()

        val api = retrofit.create(NoteScanApi::class.java)
        val request = MediaRouter2.ScanRequest(content = scannedText) // Creating our data model

        api.sendScannedText(request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                button.isEnabled = true
                button.text = "Copy & Send to PC"
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Text Copied & Sent to PC!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                button.isEnabled = true
                button.text = "Retry Sync"
                Toast.makeText(this@MainActivity, "Sync Failed! Check Server/Wi-Fi", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }
        })
    }

    private fun recognizeText(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image).addOnSuccessListener { ocrText ->
            resultText.text = ocrText.text
            resultText.movementMethod = ScrollingMovementMethod()
            copyTextBtn.visibility = Button.VISIBLE
            copyTextBtn.setOnClickListener {
                val clipboard = ContextCompat.getSystemService(
                    this,
                    android.content.ClipboardManager::class.java
                )
                val clip = android.content.ClipData.newPlainText("recognized text", ocrText.text)
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        } .addOnFailureListener { e ->
            Toast.makeText(this, "Failed to recognize text: ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }
}