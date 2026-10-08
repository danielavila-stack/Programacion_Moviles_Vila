package com.daniela.clinicasaludplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class ResultadoMedico(
    val id: String,
    val titulo: String,
    val fecha: String,
    val doctor: String,
    val estado: String
)

val listaResultadosFija = listOf(
    ResultadoMedico("r1", "Hemograma Completo", "10/10/2026", "Dr. Carlos Mendoza", "Disponible"),
    ResultadoMedico("r2", "Perfil Lipídico", "02/10/2026", "Dra. Ana López", "Disponible"),
    ResultadoMedico("r3", "Radiografía de Tórax", "25/09/2026", "Dr. Roberto Gómez", "Archivado")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadosScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Resultados Médicos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaResultadosFija) { res ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(res.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Atendido por: ${res.doctor}", style = MaterialTheme.typography.bodyMedium)
                        Text("Fecha: ${res.fecha}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estado: ${res.estado}",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}