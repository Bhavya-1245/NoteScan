package src.main.java.com.notescan.app


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteScanApi {

    @POST("/api/scab/sebd")
    fun sendScannedText(@Body extractedText: String): Call<String>
}