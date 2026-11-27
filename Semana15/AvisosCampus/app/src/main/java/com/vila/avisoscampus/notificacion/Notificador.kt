package com.vila.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.vila.avisoscampus.model.Aviso

// Habla con el sistema Android (canal, permiso, NotificationManagerCompat).
// No guarda estado.
object Notificador {

    const val CANAL = "avisos"

    // Crea el canal "Avisos". Desde Android 8 (API 26) toda notificación
    // necesita un canal; en versiones anteriores no existe y no se hace nada.
    // Crearlo varias veces no pasa nada: Android ignora si ya existe.
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL,                               // id interno
                "Avisos",                            // nombre que ve el usuario en Ajustes
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de la app"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    fun tienePermiso(context: Context): Boolean {
        return true
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        return false
    }
}
