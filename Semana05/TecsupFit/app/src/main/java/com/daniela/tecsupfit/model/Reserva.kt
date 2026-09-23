package com.daniela.tecsupfit.model

data class Reserva(
    val id: Int,
    val clase: Clase,
    val fechaHora: String,
    val estado: String // "Confirmada" o "Completada"
)