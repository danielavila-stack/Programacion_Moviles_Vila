package com.daniela.saludpluscitas.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Registro : Rutas("registro")
    object Terminos : Rutas("terminos")
    object Home : Rutas("home")
    object Especialidades : Rutas("especialidades")
    object Medicos : Rutas("medicos")
    object AgendarCita : Rutas("agendar_cita")
    object Confirmacion : Rutas("confirmacion")
    object MisCitas : Rutas("mis_citas")
    object DetalleCita : Rutas("detalle_cita")
    object Resultados : Rutas("resultados")
    object Perfil : Rutas("perfil")
}