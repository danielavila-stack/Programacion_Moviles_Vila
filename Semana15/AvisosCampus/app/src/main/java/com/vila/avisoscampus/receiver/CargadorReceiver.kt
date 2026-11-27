package com.vila.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Fase 2 · Mejora 3: escucha cuando se conecta o desconecta el cargador.
// No decide nada: solo le pasa true (conectado) o false (desconectado)
// a quien lo creó (la pantalla).
class CargadorReceiver(
    private val alCambiar: (Boolean) -> Unit
) : BroadcastReceiver() {

    // onReceive debe ser CORTO: solo lee la acción y llama a la función
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> alCambiar(true)
            Intent.ACTION_POWER_DISCONNECTED -> alCambiar(false)
        }
    }
}
