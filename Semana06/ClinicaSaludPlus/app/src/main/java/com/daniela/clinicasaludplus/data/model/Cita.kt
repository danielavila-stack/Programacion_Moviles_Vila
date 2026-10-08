package com.daniela.clinicasaludplus.data.model

data class Cita(
    val id: String = "",
    val usuarioId: String = "",
    val nombreMedico: String = "",
    val especialidad: String = "",
    val fecha: String = "",
    val hora: String = ""
)