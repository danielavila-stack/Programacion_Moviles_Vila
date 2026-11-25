package com.vila.murocampus.viewmodel

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

// Los tres estados que puede tener una pantalla que espera datos de internet
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

// Convierte el error en un mensaje para el usuario
fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${e.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}