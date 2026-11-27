package com.vila.avisoscampus.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.vila.avisoscampus.MainActivity
import com.vila.avisoscampus.R
import com.vila.avisoscampus.model.Aviso

// Habla con el sistema Android (canal, permiso, NotificationManagerCompat).
// No guarda estado.
object Notificador {

    const val CANAL = "avisos"
    // Fase 2 · Mejora 1: canal para avisos importantes (salen flotando arriba)
    const val CANAL_URGENTE = "urgentes"

    // Crea los canales "Avisos" y "Urgentes". Desde Android 8 (API 26) toda
    // notificación necesita un canal; en versiones anteriores no se hace nada.
    // Crearlos varias veces no pasa nada: Android ignora si ya existen.
    // OJO: la importancia de un canal no se puede cambiar después de creado.
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL,                               // id interno
                "Avisos",                            // nombre que ve el usuario en Ajustes
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de la app"
            }
            // Fase 2 · Mejora 1: IMPORTANCE_HIGH = suena y aparece emergente
            val canalUrgente = NotificationChannel(
                CANAL_URGENTE,
                "Urgentes",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios importantes"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
            manager.createNotificationChannel(canalUrgente)  // Fase 2 · Mejora 1
        }
    }

    // ¿La app puede mostrar notificaciones?
    // Antes de Android 13 (API 33) el permiso no existe: se concede solo.
    fun tienePermiso(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Muestra el aviso como notificación.
    // Devuelve true si se mostró y false si no hay permiso (sin lanzar error).
    @SuppressLint("MissingPermission") // el permiso se revisa justo abajo
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        // Qué se abre al tocar la notificación: la pantalla principal
        val intent = Intent(context, MainActivity::class.java)
        // PendingIntent = "permiso" para que Android lance ese Intent después,
        // aunque la app esté cerrada. FLAG_IMMUTABLE es obligatorio desde Android 12.
        val pendiente = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Fase 2 · Mejora 1: el Notificador NO decide qué es urgente (eso lo hace
        // el ViewModel); solo elige el canal y la prioridad según aviso.urgente.
        // La prioridad sirve para Android 7 o menos, donde no hay canales.
        val canal = if (aviso.urgente) CANAL_URGENTE else CANAL
        val prioridad = if (aviso.urgente) {
            NotificationCompat.PRIORITY_HIGH
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }

        // Fase 2 · Mejora 2: texto que se ve al EXPANDIR la notificación.
        // Primera línea: el texto del aviso; segunda línea: origen y hora.
        val textoLargo = "${aviso.texto}\nOrigen: ${aviso.origen.texto} · Hora: ${aviso.hora}"

        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)       // campana blanca
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)                    // texto corto (cerrada)
            // Fase 2 · Mejora 2: BigTextStyle muestra el texto completo al expandirla
            .setStyle(NotificationCompat.BigTextStyle().bigText(textoLargo))
            .setPriority(prioridad)
            .setContentIntent(pendiente)                    // abre la app al tocarla
            .setAutoCancel(true)                            // se quita al tocarla
            .build()

        // Cada aviso tiene su propio id, así no se reemplazan entre sí
        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}
