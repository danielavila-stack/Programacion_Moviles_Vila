package com.vila.avisoscampus

import android.app.Application
import com.vila.avisoscampus.notificacion.Notificador

// Clase Application: se crea una sola vez, antes que cualquier pantalla.
// Es el lugar ideal para crear el canal, así ya existe cuando se notifique.
class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}
