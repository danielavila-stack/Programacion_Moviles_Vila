package com.daniela.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.tecsupfit.data.DatosMock
import com.daniela.tecsupfit.ui.theme.TecsupPrimary

@Composable
fun InicioScreen(onClaseClick: (Int) -> Unit) {
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    // Filtrar clases dinámicamente según el chip seleccionado
    val clasesFiltradas = remember(filtroSeleccionado) {
        if (filtroSeleccionado == "Hoy") {
            DatosMock.listaClases.take(2)
        } else {
            DatosMock.listaClases
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Encabezado verde característico
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TecsupPrimary)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Text(
                    text = "TECSUP Fit",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hola, Daniela",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 15.sp
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Chips de filtrado
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                FilterChip(
                    selected = filtroSeleccionado == "Hoy",
                    onClick = { filtroSeleccionado = "Hoy" },
                    label = { Text("Hoy") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TecsupPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                FilterChip(
                    selected = filtroSeleccionado == "Esta semana",
                    onClick = { filtroSeleccionado = "Esta semana" },
                    label = { Text("Esta semana") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TecsupPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }

            Text(
                text = "Clases disponibles",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(clasesFiltradas) { clase ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClaseClick(clase.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFE8F5E9),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = TecsupPrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = clase.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${clase.hora} · ${clase.sala}",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}