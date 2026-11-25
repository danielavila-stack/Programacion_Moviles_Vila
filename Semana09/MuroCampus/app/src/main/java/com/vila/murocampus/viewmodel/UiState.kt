package com.vila.murocampus.viewmodel

// Los tres estados que puede tener una pantalla que espera datos de internet
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}