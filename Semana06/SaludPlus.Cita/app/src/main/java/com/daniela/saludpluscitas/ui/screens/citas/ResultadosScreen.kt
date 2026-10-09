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
fun ResultadosScreen(
    citaId: String,
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
            text = "Resultados Clínicos",
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
                    text = "Informe Atendido por:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = medico?.nombre ?: "Médico Evaluador",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Diagnóstico / Observaciones:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Paciente con parámetros estables. Se recomienda continuar con hidratación adecuada, mantener reposo moderado y control preventivo en 6 meses.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Receta / Indicaciones:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Paracetamol 500mg cada 8 horas por 3 días (si hay malestar).\n• Multivitamínico 1 cápsula diaria con el desayuno.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BotonSaludPlus(
            texto = "Regresar a Mis Citas",
            onClick = onVolver
        )
    }
}