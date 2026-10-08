package com.daniela.clinicasaludplus.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniela.clinicasaludplus.data.model.Cita
import com.daniela.clinicasaludplus.data.repository.Repositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(navController: NavController) {
    val usuario = Repositorio.usuarioActual
    val misCitas: List<Cita> = Repositorio.obtenerCitasPorUsuario(usuario?.id ?: "u1")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Citas Programadas") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (misCitas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes citas registradas.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(misCitas) { cita ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("detalle_cita/${cita.id}")
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = cita.nombreMedico,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Especialidad: ${cita.especialidad}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Fecha: ${cita.fecha} - Hora: ${cita.hora}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(navController: NavController, citaId: String) {
    val cita = Repositorio.obtenerCitaPorId(citaId)
    var mostrarDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle de la Cita") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cita != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Médico: ${cita.nombreMedico}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Especialidad: ${cita.especialidad}")
                        Text("Fecha: ${cita.fecha}")
                        Text("Hora: ${cita.hora}")
                    }
                }

                Button(
                    onClick = { mostrarDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar / Eliminar Cita")
                }
            } else {
                Text("Cita no encontrada o ya fue cancelada.")
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }

        // RETO: AlertDialog para remover de la lista
        if (mostrarDialog) {
            AlertDialog(
                onDismissRequest = { mostrarDialog = false },
                title = { Text("Cancelar Cita") },
                text = { Text("¿Estás segura de que deseas cancelar esta cita médica?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            Repositorio.cancelarCita(citaId)
                            mostrarDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialog = false }) {
                        Text("No, mantener")
                    }
                }
            )
        }
    }
}