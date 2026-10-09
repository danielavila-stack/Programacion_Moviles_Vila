package com.daniela.saludpluscitas.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.theme.AzulPrimario
import com.daniela.saludpluscitas.utils.FechaUtils

@Composable
fun NotificacionesScreen(onVolver: () -> Unit) {
    // Reto 3: Transformación con .map sobre la lista de citas
    val notificaciones = Repositorio.citas.map { cita ->
        val medico = Repositorio.obtenerMedicoPorId(cita.medicoId)
        "Recordatorio: Tienes una cita programada con ${medico?.nombre ?: "tu médico"} el día ${FechaUtils.fechaLarga(cita.fecha)} a las ${cita.hora}."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Notificaciones", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (notificaciones.isEmpty()) {
            Text("No tienes notificaciones pendientes.", color = Color.Gray, modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(notificaciones) { mensaje ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = AzulPrimario)
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(text = mensaje, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}