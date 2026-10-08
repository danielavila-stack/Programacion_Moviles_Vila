package com.daniela.saludpluscitas.data.model

data class Cita(
    val id: String,
    val usuarioId: String,
    val medicoId: String,
    val medicoNombre: String,
    val especialidadNombre: String,
    val fecha: String,
    val hora: String,
    val estado: String = "Agendada"
)