package com.example.app_watch.presentation.Models

data class Usuario(
    val codigo: String,
    val correo: String,
    val created_at: String,
    val estatus: Int,
    val id: Int,
    val img: Any,
    val nombre: String,
    val rol_id: Int,
    val telefono: String,
    val updated_at: String,
    val watch_codigo: String
)