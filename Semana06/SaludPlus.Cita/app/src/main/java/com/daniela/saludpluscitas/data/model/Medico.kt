package com.daniela.saludpluscitas.data.model

data class Medico(
    val id: String,
    val nombre: String,
    val especialidadId: String,
    val especialidadNombre: String,
    val cmp: String,
    val calificacion: Double,
    val foto: String = ""
)