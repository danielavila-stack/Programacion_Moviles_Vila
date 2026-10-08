package com.daniela.saludpluscitas.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BarraNavegacionInferior
import com.daniela.saludpluscitas.ui.components.CampoTextoSaludPlus

@Composable
fun EspecialidadesScreen(
    onSeleccionarEspecialidad: (String) -> Unit,
    onNavegarBottomBar: (String) -> Unit,
    rutaActual: String
) {
    var busqueda by remember { mutableStateOf("") }
    val especialidadesFiltradas = Repositorio.buscarEspecialidades(busqueda)

    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(
                rutaActual = rutaActual,
                onNavegar = onNavegarBottomBar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Especialidades Médicas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            CampoTextoSaludPlus(
                valor = busqueda,
                onValorChange = { busqueda = it },
                label = "Buscar especialidad...",
                icon = Icons.Default.Search
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(especialidadesFiltradas) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { onSeleccionarEspecialidad(item.id) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = item.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.descripcion,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}