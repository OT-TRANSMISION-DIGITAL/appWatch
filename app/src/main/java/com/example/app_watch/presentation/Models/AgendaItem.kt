package com.example.app_watch.presentation.Models

data class AgendaItem(
    val estatus: String,
    val fechaHoraSolicitud: String,
    val id: Int,
    val tecnico: Tecnico,
    val tecnico_id: Int
)