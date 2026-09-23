package com.daniela.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniela.tecsupfit.navigation.AppRuta
import com.daniela.tecsupfit.screens.BottomBar
import com.daniela.tecsupfit.screens.ConfirmacionScreen
import com.daniela.tecsupfit.screens.DetalleScreen
import com.daniela.tecsupfit.screens.InicioScreen
import com.daniela.tecsupfit.screens.PerfilScreen
import com.daniela.tecsupfit.screens.ReservarScreen
import com.daniela.tecsupfit.screens.ReservasScreen
import com.daniela.tecsupfit.screens.RutinasScreen
import com.daniela.tecsupfit.ui.theme.TecsupFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TecsupFitTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val rutaActual = navBackStackEntry?.destination?.route

                val mostrarBottomBar = rutaActual in listOf(
                    AppRuta.Inicio.route,
                    AppRuta.Reservas.route,
                    AppRuta.Rutinas.route,
                    AppRuta.Perfil.route
                )

                Scaffold(
                    bottomBar = {
                        if (mostrarBottomBar) {
                            BottomBar(
                                rutaActual = rutaActual,
                                onNavegar = { ruta ->
                                    navController.navigate(ruta) {
                                        popUpTo(AppRuta.Inicio.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppRuta.Inicio.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(AppRuta.Inicio.route) {
                            InicioScreen(
                                onClaseClick = { claseId ->
                                    navController.navigate(AppRuta.Detalle.crearRuta(claseId))
                                }
                            )
                        }

                        composable(
                            route = AppRuta.Detalle.route,
                            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1
                            DetalleScreen(
                                claseId = claseId,
                                onVolver = { navController.popBackStack() },
                                onReservar = { id ->
                                    navController.navigate(AppRuta.Reservar.crearRuta(id))
                                }
                            )
                        }

                        composable(
                            route = AppRuta.Reservar.route,
                            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1
                            ReservarScreen(
                                claseId = claseId,
                                onVolver = { navController.popBackStack() },
                                onConfirmar = { id ->
                                    navController.navigate(AppRuta.Confirmacion.crearRuta(id)) {
                                        popUpTo(AppRuta.Inicio.route)
                                    }
                                }
                            )
                        }

                        composable(
                            route = AppRuta.Confirmacion.route,
                            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1
                            ConfirmacionScreen(
                                claseId = claseId,
                                onVerReservas = {
                                    navController.navigate(AppRuta.Reservas.route) {
                                        popUpTo(AppRuta.Inicio.route)
                                    }
                                }
                            )
                        }

                        composable(AppRuta.Reservas.route) {
                            ReservasScreen()
                        }

                        composable(AppRuta.Rutinas.route) {
                            RutinasScreen()
                        }

                        composable(AppRuta.Perfil.route) {
                            PerfilScreen()
                        }
                    }
                }
            }
        }
    }
}