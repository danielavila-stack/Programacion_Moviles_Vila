package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario
import com.daniela.saludpluscitas.utils.FechaUtils

@Composable
fun DetalleCitaScreen(
    citaId: String,
    onVerResultados: (String) -> Unit,
    onVolver: () -> Unit
) {
    val cita = Repositorio.obtenerCitaPorId(citaId)
    val medico = cita?.let { Repositorio.obtenerMedicoPorId(it.medicoId) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Detalle de la Cita",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AzulClaroFondo)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Especialista", fontSize = 12.sp, color = Color.Gray)
                Text(text = medico?.nombre ?: "Dr. Carlos Mendoza", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = medico?.especialidadNombre ?: "Medicina General", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Fecha y Hora", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "${cita?.let { FechaUtils.fechaLarga(it.fecha) } ?: "Jueves 8 de octubre 2026"} - ${cita?.hora ?: "15:30"}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Estado", fontSize = 12.sp, color = Color.Gray)
                Text(text = cita?.estado ?: "Confirmada", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Reto 1: Botón para cancelar con AlertDialog
        OutlinedButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Cancelar Cita", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        BotonSaludPlus(
            texto = "Volver a Mis Citas",
            onClick = onVolver
        )
    }

    // AlertDialog de Confirmación
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Cancelar cita médica?", fontWeight = FontWeight.Bold) },
            text = { Text("Esta acción eliminará la cita de tu lista de manera permanente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        Repositorio.eliminarCita(citaId)
                        mostrarDialogo = false
                        onVolver()
                    }
                ) {
                    Text("Sí, cancelar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No, mantener")
                }
            }
        )
    }
}