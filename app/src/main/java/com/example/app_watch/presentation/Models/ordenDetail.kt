package com.example.app_watch.presentation.Models

data class ordenDetail(
    val cliente: Cliente,
    val cliente_id: Int,
    val created_at: Any,
    val detalles: List<Detalle>,
    val direccion: String,
    val estatus: String,
    val fechaHoraLlegada: String,
    val fechaHoraSalida: String,
    val fechaHoraSolicitud: String,
    val firma: Any,
    val id: Int,
    val persona_solicitante: String,
    val puesto: String,
    val sucursal: Sucursal,
    val sucursal_id: Int,
    val tecnico: Tecnico,
    val tecnico_id: Int,
    val updated_at: Any
)