package com.example.app_watch.presentation.Models

data class Detalle(
    val cantidad: Int,
    val created_at: Any,
    val descripcion: String,
    val estatus: Boolean,
    val id: Int,
    val orden_id: Int,
    val producto: Producto,
    val producto_id: Int,
    val updated_at: Any
)