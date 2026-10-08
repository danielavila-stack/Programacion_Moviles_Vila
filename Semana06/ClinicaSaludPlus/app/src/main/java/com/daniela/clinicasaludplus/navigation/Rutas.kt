package com.daniela.clinicasaludplus.navigation

object Rutas {
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"
    const val HOME = "home"
    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fecha_hora/{medicoId}"
    const val CONFIRMAR_CITA = "confirmar_cita/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "cita_exitosa"
    const val MIS_CITAS = "mis_citas"
    const val DETALLE_CITA = "detalle_cita/{citaId}"
    const val PERFIL = "perfil"
    const val RESULTADOS = "resultados"
    const val NOTIFICACIONES = "notificaciones"
}