package com.daniela.saludpluscitas.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.navigation.Rutas
import com.daniela.saludpluscitas.ui.components.BarraNavegacionInferior
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun HomeScreen(
    onNavegarAMisDoctores: () -> Unit, // <-- Nuevo callback
    onNavegarAEspecialidades: () -> Unit,
    onNavegarAMedicos: (String) -> Unit,
    onNavegarBottomBar: (String) -> Unit,
    onIrANotificaciones: () -> Unit,
    rutaActual: String
) {
    val usuario = Repositorio.usuarioActual
    val especialidades = Repositorio.especialidadesDestacadas()

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, ${usuario?.nombre?.split(" ")?.firstOrNull() ?: "Diego"}!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "¿Qué deseas hacer hoy?",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = onIrANotificaciones) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = AzulPrimario)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Fila 1 (Intacta)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaMenuHome(
                        titulo = "Agendar cita",
                        icono = Icons.Default.DateRange,
                        colorFondo = Color(0xFFEBF3FF),
                        colorTexto = AzulPrimario,
                        modifier = Modifier.weight(1f),
                        onClick = onNavegarAEspecialidades
                    )
                    TarjetaMenuHome(
                        titulo = "Mis citas",
                        icono = Icons.Default.DateRange,
                        colorFondo = Color(0xFFE8F8EE),
                        colorTexto = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavegarBottomBar(Rutas.MisCitas.ruta) }
                    )
                }

                // Fila 2 (Intacta)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaMenuHome(
                        titulo = "Mis datos",
                        icono = Icons.Default.Person,
                        colorFondo = Color(0xFFFFF4E5),
                        colorTexto = Color(0xFFE65100),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavegarBottomBar(Rutas.Perfil.ruta) }
                    )
                    TarjetaMenuHome(
                        titulo = "Resultados",
                        icono = Icons.Default.Receipt,
                        colorFondo = Color(0xFFFFF8E1),
                        colorTexto = Color(0xFFF57F17),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavegarBottomBar(Rutas.Resultados.ruta) }
                    )
                }

                // Fila 3 (Nueva opción Mis doctores)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaMenuHome(
                        titulo = "Mis doctores",
                        icono = Icons.Default.Person,
                        colorFondo = Color(0xFFE3F2FD),
                        colorTexto = AzulPrimario,
                        modifier = Modifier.weight(1f),
                        onClick = onNavegarAMisDoctores
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onNavegarAEspecialidades) {
                    Text("Ver todas", color = AzulPrimario)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(especialidades) { item ->
                    Card(
                        modifier = Modifier
                            .size(width = 110.dp, height = 120.dp)
                            .clickable { onNavegarAMedicos(item.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(AzulClaroFondo, shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = AzulPrimario)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.nombre,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaMenuHome(
    titulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icono, contentDescription = null, tint = colorTexto, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = titulo, color = colorTexto, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}