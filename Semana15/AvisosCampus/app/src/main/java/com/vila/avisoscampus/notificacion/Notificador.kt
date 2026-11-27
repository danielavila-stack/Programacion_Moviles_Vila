package com.vila.avisoscampus.notificacion

import android.content.Context
import com.vila.avisoscampus.model.Aviso

// Habla con el sistema Android (canal, permiso, NotificationManagerCompat).
// No guarda estado.
object Notificador {

    const val CANAL = "avisos"

    // TODO Paso 1: crear el NotificationChannel "Avisos" (API 26+)
    fun crearCanal(context: Context) {
    }

    // TODO Paso 2: revisar POST_NOTIFICATIONS (API 33+)
    fun tienePermiso(context: Context): Boolean {
        return true
    }

    // TODO Paso 3: construir y mostrar la notificación con NotificationCompat
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        return false
    }
}
