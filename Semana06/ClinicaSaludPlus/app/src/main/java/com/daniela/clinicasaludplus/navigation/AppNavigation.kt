package com.daniela.clinicasaludplus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniela.clinicasaludplus.ui.screens.DetalleCitaScreen
import com.daniela.clinicasaludplus.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Pantallas principales que mostrarán la barra inferior
    val rutasConBottomBar = listOf(
        Rutas.HOME,
        Rutas.ESPECIALIDADES,
        Rutas.MIS_CITAS,
        Rutas.PERFIL
    )

    val mostrarBottomBar = currentRoute in rutasConBottomBar

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") },
                        selected = currentRoute == Rutas.HOME,
                        onClick = {
                            navController.navigate(Rutas.HOME) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Add, contentDescription = "Agendar") },
                        label = { Text("Agendar") },
                        selected = currentRoute == Rutas.ESPECIALIDADES,
                        onClick = {
                            navController.navigate(Rutas.ESPECIALIDADES) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = "Mis Citas") },
                        label = { Text("Citas") },
                        selected = currentRoute == Rutas.MIS_CITAS,
                        onClick = {
                            navController.navigate(Rutas.MIS_CITAS) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") },
                        selected = currentRoute == Rutas.PERFIL,
                        onClick = {
                            navController.navigate(Rutas.PERFIL) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Rutas.SPLASH) { SplashScreen(navController) }
            composable(Rutas.LOGIN) { LoginScreen(navController) }
            composable(Rutas.REGISTRO) { RegistroScreen(navController) }
            composable(Rutas.TERMINOS) { TerminosScreen(navController) }
            composable(Rutas.HOME) { HomeScreen(navController) }
            composable(Rutas.ESPECIALIDADES) { EspecialidadesScreen(navController) }

            composable(
                route = Rutas.MEDICOS,
                arguments = listOf(navArgument("especialidadId") { type = NavType.StringType })
            ) { backStackEntry ->
                val especialidadId = backStackEntry.arguments?.getString("especialidadId") ?: ""
                MedicosScreen(navController, especialidadId)
            }

            composable(
                route = Rutas.FECHA_HORA,
                arguments = listOf(navArgument("medicoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getString("medicoId") ?: ""
                FechaHoraScreen(navController, medicoId)
            }

            composable(
                route = Rutas.CONFIRMAR_CITA,
                arguments = listOf(
                    navArgument("medicoId") { type = NavType.StringType },
                    navArgument("fecha") { type = NavType.StringType },
                    navArgument("hora") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getString("medicoId") ?: ""
                val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
                val hora = backStackEntry.arguments?.getString("hora") ?: ""
                ConfirmarCitaScreen(navController, medicoId, fecha, hora)
            }

            composable(Rutas.CITA_EXITOSA) { CitaExitosaScreen(navController) }
            composable(Rutas.MIS_CITAS) { CitasScreen(navController) }

            composable(
                route = Rutas.DETALLE_CITA,
                arguments = listOf(navArgument("citaId") { type = NavType.StringType })
            ) { backStackEntry ->
                val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
                DetalleCitaScreen(navController, citaId)
            }

            composable(Rutas.PERFIL) { PerfilScreen(navController) }
            composable(Rutas.RESULTADOS) { ResultadosScreen(navController) }
            composable(Rutas.NOTIFICACIONES) { NotificacionesScreen(navController) }
        }
    }
}