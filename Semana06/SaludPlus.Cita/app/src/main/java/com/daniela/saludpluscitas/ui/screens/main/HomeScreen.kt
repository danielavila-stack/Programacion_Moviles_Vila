package com.daniela.saludpluscitas.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.navigation.Rutas
import com.daniela.saludpluscitas.ui.components.BarraNavegacionInferior
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun HomeScreen(
    onNavegarALocales: () -> Unit,
    onNavegarAMisDoctores: () -> Unit,
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
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Entezado superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, ${usuario?.nombre?.split(" ")?.firstOrNull() ?: "Daniela"}! 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "¿Qué deseas hacer hoy?",
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }

                // Botón de Notificaciones estilizado
                Surface(
                    onClick = onIrANotificaciones,
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = AzulPrimario,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🌟 TARJETA HERO DESTACADA: "Nuestras Sedes / Agendar Cita"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavegarALocales() },
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(AzulPrimario, Color(0xFF1E40AF))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Sedes Disponibles",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Elige tu Sede y Agenda Cita",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Independencia, La Molina, Santa Clara y Anita",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Icono circular interactivo
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Locales",
                                tint = AzulPrimario,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔲 CUADRÍCULA SIMÉTRICA 2x2 DE ACCIONES
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaMenuCreativa(
                        titulo = "Mis citas",
                        subtitulo = "Historial y pendientes",
                        icono = Icons.Default.DateRange,
                        colorFondo = Color(0xFFE8F8EE),
                        colorIcono = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavegarBottomBar(Rutas.MisCitas.ruta) }
                    )
                    TarjetaMenuCreativa(
                        titulo = "Mis doctores",
                        subtitulo = "Lista de especialistas",
                        icono = Icons.Default.Person,
                        colorFondo = Color(0xFFEBF3FF),
                        colorIcono = AzulPrimario,
                        modifier = Modifier.weight(1f),
                        onClick = onNavegarAMisDoctores
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaMenuCreativa(
                        titulo = "Resultados",
                        subtitulo = "Exámenes y laboratorio",
                        icono = Icons.Default.Receipt,
                        colorFondo = Color(0xFFFFF8E1),
                        colorIcono = Color(0xFFF57F17),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavegarBottomBar(Rutas.Resultados.ruta) }
                    )
                    TarjetaMenuCreativa(
                        titulo = "Mis datos",
                        subtitulo = "Perfil y cuenta",
                        icono = Icons.Default.Person,
                        colorFondo = Color(0xFFFFF4E5),
                        colorIcono = Color(0xFFE65100),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavegarBottomBar(Rutas.Perfil.ruta) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🩺 SECCIÓN ESPECIALIDADES DESTACADAS REDISEÑADA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                TextButton(onClick = onNavegarAEspecialidades) {
                    Text("Ver todas", color = AzulPrimario, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                items(especialidades) { item ->
                    // Asignación de icono y colores según la especialidad
                    val (icono, colorIcono, colorFondoIcono) = when (item.nombre.lowercase()) {
                        "medicina general" -> Triple(Icons.Default.Person, Color(0xFF0284C7), Color(0xFFE0F2FE))
                        "pediatría", "pediatria" -> Triple(Icons.Default.Face, Color(0xFFDB2777), Color(0xFFFCE7F3))
                        "cardiología", "cardiologia" -> Triple(Icons.Default.Favorite, Color(0xFFDC2626), Color(0xFFFEE2E2))
                        else -> Triple(Icons.Default.Star, AzulPrimario, Color(0xFFE0E7FF))
                    }

                    Card(
                        modifier = Modifier
                            .width(125.dp)
                            .height(135.dp)
                            .clickable { onNavegarAMedicos(item.id) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                                    .size(50.dp)
                                    .background(colorFondoIcono, shape = RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icono,
                                    contentDescription = item.nombre,
                                    tint = colorIcono,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = item.nombre,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                textAlign = TextAlign.Center,
                                lineHeight = 15.sp,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaMenuCreativa(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorIcono: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(95.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icono, contentDescription = null, tint = colorIcono, modifier = Modifier.size(20.dp))
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = colorIcono.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }

            Column {
                Text(
                    text = titulo,
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitulo,
                    color = Color(0xFF64748B),
                    fontSize = 10.sp
                )
            }
        }
    }
}