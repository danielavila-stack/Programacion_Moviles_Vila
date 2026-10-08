package com.daniela.clinicasaludplus.data.model

data class Medico(
    val id: String,
    val especialidadId: String,
    val nombre: String,
    val cmp: String,
    val calificacion: Double,
    val disponibilidad: String
)