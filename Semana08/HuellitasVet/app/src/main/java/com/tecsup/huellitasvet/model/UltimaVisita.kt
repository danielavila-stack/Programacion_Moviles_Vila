package com.tecsup.huellitasvet.model

// Resultado de "última visita por mascota" (MAX(fecha) de las citas ATENDIDAS).
// Los nombres de los campos deben coincidir con los alias del SELECT.
data class UltimaVisita(
    val mascotaId: Int,
    val fecha: String
)
