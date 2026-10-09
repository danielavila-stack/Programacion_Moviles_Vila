package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun ConfirmacionScreen(
    medicoId: String,
    localId: String,
    fecha: String,
    hora: String,
    onVolverInicio: () -> Unit
) {
    // Obtener nombre del médico según su ID
    val nombreDoctor = when (medicoId) {
        "1" -> "Dr. Carlos Mendoza"
        "2" -> "Dra. Ana Torres"
        "3" -> "Dr. Luis Paredes"
        "4" -> "Dra. Sofía Ramos"
        "5" -> "Dr. Roberto Gómez"
        "6" -> "Dra. Elena Benítez"
        else -> "Dr. Carlos Mendoza"
    }

    // Obtener nombre de la sede según el ID
    val nombreSede = when (localId) {
        "1" -> "Sede Independencia"
        "2" -> "Sede La Molina"
        "3" -> "Sede Santa Clara"
        "4" -> "Sede Santa Anita"
        else -> "Sede Principal"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono Check Exitoso
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color(0xFFE6F4EA), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Éxito",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Cita Confirmada!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Text(
                    text = "Tu cita médica ha sido registrada con éxito.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(20.dp))

                // Detalle de la Cita
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Doctor
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Médico", fontSize = 11.sp, color = Color.Gray)
                            Text(nombreDoctor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Sede
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Lugar / Sede", fontSize = 11.sp, color = Color.Gray)
                            Text(nombreSede, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AzulPrimario)
                        }
                    }

                    // Fecha
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Fecha", fontSize = 11.sp, color = Color.Gray)
                            Text(fecha, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Hora
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Hora", fontSize = 11.sp, color = Color.Gray)
                            Text(hora, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onVolverInicio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) {
                    Text("Volver al Inicio", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}