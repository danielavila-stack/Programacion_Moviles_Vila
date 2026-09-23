package com.daniela.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniela.clinicasalud.model.Appointment
import com.daniela.clinicasalud.model.LocalData
import com.daniela.clinicasalud.navigation.Screen
import com.daniela.clinicasalud.ui.screens.*
import com.daniela.clinicasalud.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClinicaSaludTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDrawerRoute by remember { mutableStateOf(Screen.Inicio.route) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(280.dp)
            ) {
                // Cabecera del Drawer (Avatar + Nombre paciente)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PurpleLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JP",
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Juan Pérez",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextDark
                    )

                    Text(
                        text = "Paciente",
                        fontSize = 13.sp,
                        color = TextGray
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = PurpleChipUnselected)
                }

                // Opciones de navegación del Drawer
                NavigationDrawerItem(
                    label = { Text("Inicio", fontWeight = FontWeight.Medium) },
                    selected = selectedDrawerRoute == Screen.Inicio.route,
                    onClick = {
                        selectedDrawerRoute = Screen.Inicio.route
                        navController.navigate(Screen.Inicio.route) {
                            popUpTo(Screen.Inicio.route) { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PurpleLight,
                        selectedTextColor = PurplePrimary
                    )
                )

                NavigationDrawerItem(
                    label = { Text("Mis citas", fontWeight = FontWeight.Medium) },
                    selected = selectedDrawerRoute == Screen.MisCitas.route,
                    onClick = {
                        selectedDrawerRoute = Screen.MisCitas.route
                        navController.navigate(Screen.MisCitas.route)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PurpleLight,
                        selectedTextColor = PurplePrimary
                    )
                )

                NavigationDrawerItem(
                    label = { Text("Historial médico", fontWeight = FontWeight.Medium) },
                    selected = selectedDrawerRoute == Screen.HistorialMedico.route,
                    onClick = {
                        selectedDrawerRoute = Screen.HistorialMedico.route
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PurpleLight,
                        selectedTextColor = PurplePrimary
                    )
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route
        ) {
            composable(Screen.Inicio.route) {
                HomeScreen(
                    onDoctorClick = { doctorId ->
                        navController.navigate(Screen.PerfilMedico.createRoute(doctorId))
                    },
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable(
                route = Screen.PerfilMedico.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
                DoctorDetailScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onScheduleClick = { id ->
                        navController.navigate(Screen.AgendarCita.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.AgendarCita.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
                AgendarCitaScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onConfirmClick = { doctorName, fecha, hora ->
                        LocalData.initialAppointments.add(
                            0,
                            Appointment(
                                id = LocalData.initialAppointments.size + 1,
                                doctorName = doctorName,
                                dateInfo = "$fecha, $hora",
                                status = "Confirmada"
                            )
                        )
                        navController.navigate(
                            Screen.Confirmacion.createRoute(doctorName, fecha, hora)
                        )
                    }
                )
            }

            composable(
                route = Screen.Confirmacion.route,
                arguments = listOf(
                    navArgument("doctorName") { type = NavType.StringType },
                    navArgument("fecha") { type = NavType.StringType },
                    navArgument("hora") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
                val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
                val hora = backStackEntry.arguments?.getString("hora") ?: ""

                ConfirmacionScreen(
                    doctorName = doctorName,
                    fecha = fecha,
                    hora = hora,
                    onVerCitasClick = {
                        selectedDrawerRoute = Screen.MisCitas.route
                        navController.navigate(Screen.MisCitas.route) {
                            popUpTo(Screen.Inicio.route)
                        }
                    }
                )
            }

            composable(Screen.MisCitas.route) {
                MisCitasScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}