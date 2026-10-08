package com.daniela.saludpluscitas.data.model

data class Usuario(
    val id: String,
    val nombre: String,
    val dni: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)