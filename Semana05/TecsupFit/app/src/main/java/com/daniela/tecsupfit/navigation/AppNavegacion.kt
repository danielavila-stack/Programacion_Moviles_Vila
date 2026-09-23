package com.daniela.tecsupfit.navigation

sealed class AppRuta(val route: String) {
    object Inicio : AppRuta("inicio")
    object Detalle : AppRuta("detalle/{claseId}") {
        fun crearRuta(claseId: Int) = "detalle/$claseId"
    }
    object Reservar : AppRuta("reservar/{claseId}") {
        fun crearRuta(claseId: Int) = "reservar/$claseId"
    }
    object Confirmacion : AppRuta("confirmacion/{claseId}") {
        fun crearRuta(claseId: Int) = "confirmacion/$claseId"
    }
    object Reservas : AppRuta("reservas")
    object Rutinas : AppRuta("rutinas")
    object Perfil : AppRuta("perfil")
}