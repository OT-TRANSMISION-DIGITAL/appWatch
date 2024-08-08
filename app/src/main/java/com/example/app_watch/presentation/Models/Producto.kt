package com.example.app_watch.presentation.Models

data class Producto(
    val created_at: String,
    val descripcion: String,
    val estatus: Boolean,
    val id: Int,
    val img: String,
    val nombre: String,
    val precio: String,
    val updated_at: String
)