package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.daniela.saludpluscitas.data.model.Cita
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun AgendarCitaScreen(
    medicoId: String,
    onCitaConfirmada: (String) -> Unit
) {
    val medico = Repositorio.obtenerMedicoPorId(medicoId)
    val fechasDisponibles = listOf("12 Oct", "13 Oct", "14 Oct", "15 Oct")
    val horasDisponibles = listOf("09:00 AM", "10:30 AM", "03:00 PM", "04:30 PM")

    var fechaSeleccionada by remember { mutableStateOf(fechasDisponibles.first()) }
    var horaSeleccionada by remember { mutableStateOf(horasDisponibles.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Seleccionar Fecha y Hora",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario
        )

        Spacer(modifier = Modifier.height(16.dp))

        medico?.let { medicoItem ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AzulClaroFondo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Médico seleccionado:", fontSize = 12.sp)
                    Text(text = medicoItem.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "CMP: ${medicoItem.cmp}", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Días Disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(fechasDisponibles) { fecha ->
                val esSeleccionado = fecha == fechaSeleccionada
                Card(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { fechaSeleccionada = fecha },
                    colors = CardDefaults.cardColors(
                        containerColor = if (esSeleccionado) AzulPrimario else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = fecha,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = if (esSeleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Horarios Disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {
            items(horasDisponibles) { hora ->
                val esSeleccionado = hora == horaSeleccionada
                Card(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { horaSeleccionada = hora },
                    colors = CardDefaults.cardColors(
                        containerColor = if (esSeleccionado) AzulPrimario else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = hora,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = if (esSeleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        BotonSaludPlus(
            texto = "Confirmar Reserva",
            onClick = {
                val nuevaCita = Cita(
                    id = System.currentTimeMillis().toString(),
                    usuarioId = Repositorio.usuarioActual?.id ?: "1",
                    medicoId = medicoId,
                    fecha = fechaSeleccionada,
                    hora = horaSeleccionada,
                    estado = "Confirmada"
                )
                Repositorio.guardarCita(nuevaCita)
                onCitaConfirmada(nuevaCita.id)
            }
        )
    }
}