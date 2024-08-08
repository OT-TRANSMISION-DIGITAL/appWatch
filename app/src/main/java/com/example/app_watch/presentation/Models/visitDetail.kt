package com.example.app_watch.presentation.Models

data class visitDetail(
    val cliente: Cliente,
    val cliente_id: Int,
    val direccion: String,
    val estatus: String,
    val fechaHoraLlegada: String,
    val fechaHoraSalida: String,
    val fechaHoraSolicitud: String,
    val id: Int,
    val motivo: String,
    val sucursal: Sucursal,
    val sucursal_id: Int,
    val tecnico: Tecnico,
    val tecnico_id: Int
)