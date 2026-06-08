package com.notescan.app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteApiService {
    @POST("api/v1/scan")
    fun sendNote(@Body request: NoteRequest): Call<Void>
}