package com.daniela.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.tecsupfit.data.DatosMock
import com.daniela.tecsupfit.ui.theme.TecsupPrimary

@Composable
fun RutinasScreen() {
    var nivelSeleccionado by remember { mutableStateOf("Todos") }

    val opcionesNivel = listOf("Todos", "Principiante", "Intermedio", "Avanzado")

    val rutinasFiltradas = remember(nivelSeleccionado) {
        if (nivelSeleccionado == "Todos") {
            DatosMock.listaRutinas
        } else {
            DatosMock.listaRutinas.filter { it.nivel.equals(nivelSeleccionado, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp)
    ) {
        Text(
            text = "Rutinas de Entrenamiento",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Fila horizontal con fichas de selección por nivel de dificultad
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(opcionesNivel) { opcion ->
                FilterChip(
                    selected = nivelSeleccionado == opcion,
                    onClick = { nivelSeleccionado = opcion },
                    label = { Text(opcion) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TecsupPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(rutinasFiltradas) { rutina ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = rutina.titulo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = rutina.descripcion,
                            color = Color.DarkGray,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Row(
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Text(
                                    text = rutina.duracion,
                                    color = TecsupPrimary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFEEEEEE)
                            ) {
                                Text(
                                    text = rutina.nivel,
                                    color = Color.DarkGray,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}