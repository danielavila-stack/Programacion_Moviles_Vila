package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun DetalleCitaScreen(
    citaId: String,
    onVerResultados: (String) -> Unit,
    onVolver: () -> Unit
) {
    val cita = Repositorio.obtenerCitaPorId(citaId)
    val medico = cita?.let { Repositorio.obtenerMedicoPorId(it.medicoId) }

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
                Text(
                    text = "Información del Especialista",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = medico?.nombre ?: "Doctor de Guardia",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = medico?.especialidadNombre ?: "Atención Médica",
                    fontSize = 14.sp
                )
                Text(
                    text = medico?.cmp ?: "CMP ---",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Fecha y Hora Programada",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${cita?.fecha ?: "Fecha no definida"} - ${cita?.hora ?: ""}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Estado de la Cita",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = cita?.estado ?: "Pendiente",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BotonSaludPlus(
            texto = "Ver Resultados Médicos",
            onClick = { onVerResultados(citaId) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        BotonSaludPlus(
            texto = "Volver",
            onClick = onVolver
        )
    }
}