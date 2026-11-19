package com.vila.campusgps.data

import com.vila.campusgps.model.Posicion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun playServicesDisponible(): Boolean

    // MEJORA 3: ubicaciones continuas mientras alguien las esté recogiendo
    fun seguirPosicion(): Flow<Posicion>
}

// Se lanza cuando el GPS está apagado o no se pudo determinar la ubicación
class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")