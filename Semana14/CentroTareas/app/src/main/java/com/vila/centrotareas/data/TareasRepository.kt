package com.vila.centrotareas.data

interface TareasRepository {
    // MEJORA 1: alAvanzar recibe el avance de 0f a 1f mientras calcula
    suspend fun contarPrimos(hasta: Int, alAvanzar: (Float) -> Unit = {}): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}