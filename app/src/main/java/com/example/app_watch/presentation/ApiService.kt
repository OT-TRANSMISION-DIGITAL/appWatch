package com.example.app_watch.presentation


import retrofit2.http.*
import com.example.app_watch.presentation.Models.ApiResponse
import okhttp3.RequestBody
import retrofit2.Call

interface ApiService {
    @POST("validateCode")
    fun validateCode(@Body request: RequestBody): Call<ApiResponse>
}