package com.example.app_watch.presentation.Models

data class AgendaTecnicoItem(
    val estatus: String,
    val fechaHoraSolicitud: String,
    val id: Int,
    val tecnico_id: Int
)