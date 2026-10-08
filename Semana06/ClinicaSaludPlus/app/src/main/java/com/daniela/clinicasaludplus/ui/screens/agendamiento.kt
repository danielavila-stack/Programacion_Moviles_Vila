package com.daniela.clinicasaludplus.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daniela.clinicasaludplus.data.model.Cita
import com.daniela.clinicasaludplus.data.repository.Repositorio
import com.daniela.clinicasaludplus.navigation.Rutas

// PANTALLA SELECCIÓN DE ESPECIALIDAD
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EspecialidadesScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    val especialidades = Repositorio.buscarEspecialidades(query)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Seleccionar Especialidad") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar especialidad...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(especialidades) { esp ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("medicos/${esp.id}") },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = esp.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = esp.descripcion, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

// PANTALLA SELECCIÓN DE MÉDICO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicosScreen(navController: NavController, especialidadId: String) {
    val especialidad = Repositorio.obtenerEspecialidadPorId(especialidadId)
    val medicos = Repositorio.obtenerMedicosPorEspecialidad(especialidadId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(especialidad?.nombre ?: "Médicos Disponibles") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(medicos) { med ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("fecha_hora/${med.id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = med.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Especialista en ${especialidad?.nombre ?: "Consulta"}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

// PANTALLA FECHA Y HORA (Estática en Fase 1)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaHoraScreen(navController: NavController, medicoId: String) {
    val medico = Repositorio.obtenerMedicoPorId(medicoId)
    val fechasEstaticas = listOf("Lunes 13 Oct", "Martes 14 Oct", "Miércoles 15 Oct")
    val horasEstaticas = listOf("09:00 AM", "10:30 AM", "03:00 PM")

    var fechaSeleccionada by remember { mutableStateOf(fechasEstaticas.first()) }
    var horaSeleccionada by remember { mutableStateOf(horasEstaticas.first()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar Fecha y Hora") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(text = "Médico: ${medico?.nombre ?: "Especialista"}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Días Disponibles", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                fechasEstaticas.forEach { f ->
                    FilterChip(
                        selected = fechaSeleccionada == f,
                        onClick = { fechaSeleccionada = f },
                        label = { Text(f) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Horarios Disponibles", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                horasEstaticas.forEach { h ->
                    FilterChip(
                        selected = horaSeleccionada == h,
                        onClick = { horaSeleccionada = h },
                        label = { Text(h) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("confirmar_cita/$medicoId/$fechaSeleccionada/$horaSeleccionada") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar")
            }
        }
    }
}

// PANTALLA CONFIRMACIÓN DE CITA
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarCitaScreen(navController: NavController, medicoId: String, fecha: String, hora: String) {
    val medico = Repositorio.obtenerMedicoPorId(medicoId)
    val usuario = Repositorio.usuarioActual

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Resumen de la Cita", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "Paciente: ${usuario?.nombre ?: "Paciente"}")
                    Text(text = "Médico: ${medico?.nombre ?: "Especialista"}")
                    Text(text = "Fecha: $fecha")
                    Text(text = "Hora: $hora")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val nuevaCita = Cita(
                        "c_${System.currentTimeMillis()}",
                        usuario?.id?.toString() ?: "1",
                        medicoId,
                        medico?.nombre ?: "Especialista",
                        "Consulta Médica",
                        fecha,

                    )
                    Repositorio.agregarCita(nuevaCita)
                    navController.navigate(Rutas.CITA_EXITOSA) { popUpTo(Rutas.HOME) }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Cita")
            }
        }
    }
}

// PANTALLA CITA EXITOSA
@Composable
fun CitaExitosaScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¡Cita Reservada con Éxito!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate(Rutas.HOME) { popUpTo(Rutas.HOME) { inclusive = true } } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Inicio")
        }
    }
}