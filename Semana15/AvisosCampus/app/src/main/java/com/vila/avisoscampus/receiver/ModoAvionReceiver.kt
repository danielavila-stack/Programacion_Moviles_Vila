package com.vila.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Escucha el anuncio del sistema "cambió el modo avión".
// No decide nada: solo le pasa true/false a quien lo creó (la pantalla).
class ModoAvionReceiver(
    private val alCambiar: (Boolean) -> Unit
) : BroadcastReceiver() {

    // onReceive corre en el hilo principal y debe ser CORTO:
    // si tarda mucho, Android puede cerrar la app (ANR).
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            // "state" = true si el modo avión quedó activado
            val activo = intent.getBooleanExtra("state", false)
            alCambiar(activo)
        }
    }
}
