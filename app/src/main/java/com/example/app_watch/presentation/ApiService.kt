package com.example.app_watch.presentation


import com.example.app_watch.Singleton
import com.example.app_watch.presentation.Models.AgendaAdmin
import com.example.app_watch.presentation.Models.AgendaTecnico
import retrofit2.http.*
import com.example.app_watch.presentation.Models.ApiResponse
import com.example.app_watch.presentation.Models.LogOut
import com.example.app_watch.presentation.Models.ordenDetail
import com.example.app_watch.presentation.Models.visitDetail
import okhttp3.RequestBody
import retrofit2.Call

interface ApiService {
    @POST("validateCode")
    fun validateCode(@Body request: RequestBody): Call<ApiResponse>

    @GET("logout")
    fun cerrarSesion(@Header("Authorization") token:String= "Bearer ${Singleton.token}"): Call<LogOut>

    @GET("agenda")
    fun agenda(@Query("tipo") tipo: String?,@Query("fecha") fecha: String?): Call<AgendaAdmin>

    @GET("agenda")
    fun agendaTecnico(
        @Query("tipo") tipo: String?,
        @Query("tecnico") tecnico: String?
    ): Call<AgendaTecnico>

    @GET("ordenes/{id}")
    fun ordenDetail(@Path("id") id: String): Call<ordenDetail>

    @GET("visitas/{id}")
    fun visitaDetail(@Path("id") id: String): Call<visitDetail>
}