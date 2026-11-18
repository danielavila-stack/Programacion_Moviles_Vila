package com.vila.campusgps.util

import android.location.Location
import com.vila.campusgps.model.Lugar
import com.vila.campusgps.model.Posicion

// Función pura: calcula la distancia en metros entre el usuario y un lugar
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}