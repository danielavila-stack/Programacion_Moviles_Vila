package com.vila.campusgps.data

import com.vila.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun playServicesDisponible(): Boolean
}

// Se lanza cuando el GPS está apagado o no se pudo determinar la ubicación
class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")