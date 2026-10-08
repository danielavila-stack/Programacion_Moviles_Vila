package com.daniela.clinicasaludplus.data.model

data class Usuario(
    val id: String,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)