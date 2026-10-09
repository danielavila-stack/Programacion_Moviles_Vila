package com.daniela.saludpluscitas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.daniela.saludpluscitas.ui.screens.main.MedicosScreen
import com.daniela.saludpluscitas.ui.screens.perfil.PerfilScreen

@Composable
fun GrafoNavegacion() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        // --- Autenticación ---
        composable(Rutas.Splash.ruta) {
            SplashScreen(
                onNavegarLogin = { navController.navigate(Rutas.Login.ruta) },
                onNavegarRegistro = { navController.navigate(Rutas.Registro.ruta) }
            )
        }

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

        composable(Rutas.Registro.ruta) {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onIrATerminos = { navController.navigate(Rutas.Terminos.ruta) },
                onIrALogin = { navController.navigate(Rutas.Login.ruta) }
            )
        }

        composable(Rutas.Terminos.ruta) {
            TerminosScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // --- Pantallas Principales ---
        composable(Rutas.Home.ruta) {
            HomeScreen(
                onNavegarAEspecialidades = { navController.navigate(Rutas.Especialidades.ruta) },
                onNavegarAMedicos = { especialidadId ->
                    navController.navigate("${Rutas.Medicos.ruta}/$especialidadId")
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Home.ruta
            )
        }

        composable(Rutas.Especialidades.ruta) {
            EspecialidadesScreen(
                onSeleccionarEspecialidad = { especialidadId ->
                    navController.navigate("${Rutas.Medicos.ruta}/$especialidadId")
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Especialidades.ruta
            )
        }

        composable(
            route = "${Rutas.Medicos.ruta}/{especialidadId}",
            arguments = listOf(navArgument("especialidadId") { type = NavType.StringType })
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getString("especialidadId") ?: "1"
            MedicosScreen(
                especialidadId = especialidadId,
                onSeleccionarMedico = { medicoId ->
                    navController.navigate("${Rutas.AgendarCita.ruta}/$medicoId")
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Medicos.ruta
            )
        }

        // --- Flujo de Reserva de Citas ---
        composable(
            route = "${Rutas.AgendarCita.ruta}/{medicoId}",
            arguments = listOf(navArgument("medicoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: "1"
            AgendarCitaScreen(
                medicoId = medicoId,
                onCitaConfirmada = { citaId ->
                    navController.navigate("${Rutas.Confirmacion.ruta}/$citaId")
                }
            )
        }

        composable(
            route = "${Rutas.Confirmacion.ruta}/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            ConfirmacionScreen(
                citaId = citaId,
                onVolverInicio = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                }
            )
        }

        // --- Gestión de Citas y Perfil ---
        composable(Rutas.MisCitas.ruta) {
            MisCitasScreen(
                onVerDetalle = { citaId ->
                    navController.navigate("${Rutas.DetalleCita.ruta}/$citaId")
                },
                onVerResultados = { citaId ->
                    navController.navigate("${Rutas.Resultados.ruta}/$citaId")
                },
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.MisCitas.ruta
            )
        }

        composable(
            route = "${Rutas.DetalleCita.ruta}/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            DetalleCitaScreen(
                citaId = citaId,
                onVerResultados = { id ->
                    navController.navigate("${Rutas.Resultados.ruta}/$id")
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Resultados.ruta) {
            ResultadosScreen(
                onNavegarBottomBar = { ruta -> navController.navigate(ruta) },
                rutaActual = Rutas.Resultados.ruta
            )
        }

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