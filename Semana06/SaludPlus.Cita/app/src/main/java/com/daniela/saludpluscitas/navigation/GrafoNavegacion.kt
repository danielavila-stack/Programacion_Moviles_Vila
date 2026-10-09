package com.daniela.saludpluscitas.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniela.saludpluscitas.data.model.Cita
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.screens.auth.LoginScreen
import com.daniela.saludpluscitas.ui.screens.auth.RegistroScreen
import com.daniela.saludpluscitas.ui.screens.auth.SplashScreen
import com.daniela.saludpluscitas.ui.screens.auth.TerminosScreen
import com.daniela.saludpluscitas.ui.screens.citas.AgendarCitaScreen
import com.daniela.saludpluscitas.ui.screens.citas.ConfirmacionScreen
import com.daniela.saludpluscitas.ui.screens.citas.DetalleCitaScreen
import com.daniela.saludpluscitas.ui.screens.citas.MisCitasScreen
import com.daniela.saludpluscitas.ui.screens.citas.ResultadosScreen
import com.daniela.saludpluscitas.ui.screens.main.EspecialidadesScreen
import com.daniela.saludpluscitas.ui.screens.main.HomeScreen
import com.daniela.saludpluscitas.ui.screens.main.LocalesScreen
import com.daniela.saludpluscitas.ui.screens.main.MedicosScreen
import com.daniela.saludpluscitas.ui.screens.main.MisDoctoresScreen
import com.daniela.saludpluscitas.ui.screens.main.NotificacionesScreen
import com.daniela.saludpluscitas.ui.screens.perfil.PerfilScreen

@Composable
fun GrafoNavegacion() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        // 1. Splash
        composable(Rutas.Splash.ruta) {
            SplashScreen(
                onNavegarRegistro = { navController.navigate(Rutas.Registro.ruta) },
                onNavegarLogin = { navController.navigate(Rutas.Login.ruta) }
            )
        }

        // 2. Registro
        composable(Rutas.Registro.ruta) {
            val context = LocalContext.current
            RegistroScreen(
                onRegistroExitoso = {
                    Toast.makeText(context, "¡Registro exitoso! Por favor inicia sesión.", Toast.LENGTH_SHORT).show()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Registro.ruta) { inclusive = true }
                    }
                },
                onIrATerminos = { navController.navigate(Rutas.Terminos.ruta) },
                onIrALogin = { navController.navigate(Rutas.Login.ruta) }
            )
        }

        // 3. Login
        composable(Rutas.Login.ruta) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.Registro.ruta) }
            )
        }

        // Términos y condiciones
        composable(Rutas.Terminos.ruta) {
            TerminosScreen(onVolver = { navController.popBackStack() })
        }

        // Home
        composable(Rutas.Home.ruta) {
            HomeScreen(
                onNavegarALocales = { navController.navigate(Rutas.Locales.ruta) },
                onNavegarAMisDoctores = { navController.navigate(Rutas.MisDoctores.ruta) },
                onNavegarAEspecialidades = { navController.navigate("${Rutas.Especialidades.ruta}/1") },
                onNavegarAMedicos = { especialidadId ->
                    navController.navigate("${Rutas.Medicos.ruta}/$especialidadId/1")
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                onIrANotificaciones = { navController.navigate(Rutas.Notificaciones.ruta) },
                rutaActual = Rutas.Home.ruta
            )
        }

        // Pantalla de Locales (pasa el localId a Especialidades)
        composable(Rutas.Locales.ruta) {
            LocalesScreen(
                onSeleccionarLocal = { localId ->
                    navController.navigate("${Rutas.Especialidades.ruta}/$localId")
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // Especialidades con localId
        composable(
            route = "${Rutas.Especialidades.ruta}/{localId}",
            arguments = listOf(navArgument("localId") { type = NavType.StringType })
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getString("localId") ?: "1"
            EspecialidadesScreen(
                onSeleccionarEspecialidad = { especialidadId ->
                    navController.navigate("${Rutas.Medicos.ruta}/$especialidadId/$localId")
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Especialidades.ruta,
                onVolver = { navController.popBackStack() }
            )
        }

        // Medicos con especialidadId y localId
        composable(
            route = "${Rutas.Medicos.ruta}/{especialidadId}/{localId}",
            arguments = listOf(
                navArgument("especialidadId") { type = NavType.StringType },
                navArgument("localId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getString("especialidadId") ?: "1"
            val localId = backStackEntry.arguments?.getString("localId") ?: "1"
            MedicosScreen(
                especialidadId = especialidadId,
                onSeleccionarMedico = { medicoId ->
                    navController.navigate("${Rutas.AgendarCita.ruta}/$medicoId/$localId")
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Medicos.ruta
            )
        }

        // Agendar cita con medicoId y localId
        composable(
            route = "${Rutas.AgendarCita.ruta}/{medicoId}/{localId}",
            arguments = listOf(
                navArgument("medicoId") { type = NavType.StringType },
                navArgument("localId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: "1"
            val localId = backStackEntry.arguments?.getString("localId") ?: "1"
            AgendarCitaScreen(
                medicoId = medicoId,
                onCitaConfirmada = { idMed, fecha, hora ->
                    // Crear y guardar la cita con todos los parámetros requeridos
                    val nuevaCita = Cita(
                        id = System.currentTimeMillis().toString(),
                        usuarioId = Repositorio.usuarioActual?.id ?: "1",
                        medicoId = idMed,
                        fecha = fecha,
                        hora = hora,
                        estado = "Confirmada"
                    )
                    Repositorio.guardarCita(nuevaCita)

                    navController.navigate("${Rutas.Confirmacion.ruta}/$idMed/$localId/$fecha/$hora")
                },
                onVolver = { navController.popBackStack() }
            )
        }

        // Confirmación con localId incluido
        composable(
            route = "${Rutas.Confirmacion.ruta}/{medicoId}/{localId}/{fecha}/{hora}",
            arguments = listOf(
                navArgument("medicoId") { type = NavType.StringType },
                navArgument("localId") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: "1"
            val localId = backStackEntry.arguments?.getString("localId") ?: "1"
            val fecha = backStackEntry.arguments?.getString("fecha") ?: "Jueves 8 de octubre 2026"
            val hora = backStackEntry.arguments?.getString("hora") ?: "15:30"

            ConfirmacionScreen(
                medicoId = medicoId,
                localId = localId,
                fecha = fecha,
                hora = hora,
                onVolverInicio = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                }
            )
        }

        // Mis Doctores
        composable(Rutas.MisDoctores.ruta) {
            MisDoctoresScreen(onVolver = { navController.popBackStack() })
        }

        // Notificaciones
        composable(Rutas.Notificaciones.ruta) {
            NotificacionesScreen(onVolver = { navController.popBackStack() })
        }

        // Mis Citas
        composable(Rutas.MisCitas.ruta) {
            MisCitasScreen(
                onVerDetalle = { citaId -> navController.navigate("${Rutas.DetalleCita.ruta}/$citaId") },
                onVerResultados = { navController.navigate(Rutas.Resultados.ruta) },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.MisCitas.ruta,
                onVolver = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                }
            )
        }

        // Detalle Cita
        composable(
            route = "${Rutas.DetalleCita.ruta}/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            DetalleCitaScreen(
                citaId = citaId,
                onVerResultados = { navController.navigate(Rutas.Resultados.ruta) },
                onVolver = { navController.popBackStack() }
            )
        }

        // Resultados
        composable(Rutas.Resultados.ruta) {
            ResultadosScreen(
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Resultados.ruta
            )
        }

        // Perfil
        composable(Rutas.Perfil.ruta) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Perfil.ruta
            )
        }
    }
}