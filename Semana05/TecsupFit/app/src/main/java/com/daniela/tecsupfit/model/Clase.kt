package com.daniela.tecsupfit.model

data class Clase(
    val id: Int,
    val nombre: String,
    val hora: String,
    val sala: String,
    val duracion: String = "45 min",
    val cuposDisponibles: Int,
    val cuposTotales: Int = 12,
    val descripcion: String = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
    var esFavorito: Boolean = false
)