package com.daniela.saludpluscitas.data.model

data class ResultadoMedico(
    val id: String,
    val titulo: String,
    val fecha: String,
    val diagnostico: String,
    val medicoEncargado: String
)