package com.example.app_watch.presentation.Models

data class Cliente(
    val correo: String,
    val created_at: Any,
    val estatus: Boolean,
    val id: Int,
    val nombre: String,
    val telefono: String,
    val updated_at: Any
)