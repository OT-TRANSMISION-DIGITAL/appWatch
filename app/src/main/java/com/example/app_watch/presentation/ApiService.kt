package com.example.app_watch.presentation


import com.example.app_watch.Singleton
import com.example.app_watch.presentation.Models.Agenda
import retrofit2.http.*
import com.example.app_watch.presentation.Models.ApiResponse
import com.example.app_watch.presentation.Models.LogOut
import okhttp3.RequestBody
import retrofit2.Call

interface ApiService {
    @POST("validateCode")
    fun validateCode(@Body request: RequestBody): Call<ApiResponse>

    @GET("logout")
    fun cerrarSesion(@Header("Authorization") token:String= "Bearer ${Singleton.token}"): Call<LogOut>

    @GET("agenda")
    fun agenda(): Call<Agenda>
}