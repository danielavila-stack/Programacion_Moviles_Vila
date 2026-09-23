package com.daniela.clinicasalud.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")

    object PerfilMedico : Screen("perfil_medico/{doctorId}") {
        fun createRoute(doctorId: Int) = "perfil_medico/$doctorId"
    }

    object AgendarCita : Screen("agendar_cita/{doctorId}") {
        fun createRoute(doctorId: Int) = "agendar_cita/$doctorId"
    }

    object Confirmacion : Screen("confirmacion/{doctorName}/{fecha}/{hora}") {
        fun createRoute(doctorName: String, fecha: String, hora: String) =
            "confirmacion/$doctorName/$fecha/$hora"
    }

    object MisCitas : Screen("mis_citas")
    object HistorialMedico : Screen("historial_medico")
}