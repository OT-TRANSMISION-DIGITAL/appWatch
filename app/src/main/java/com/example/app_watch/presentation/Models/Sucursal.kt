package com.example.app_watch.presentation.Models

data class Sucursal(
    val cliente_id: Int,
    val created_at: Any,
    val direccion: String,
    val estatus: Boolean,
    val id: Int,
    val nombre: String,
    val telefono: String,
    val updated_at: Any
)