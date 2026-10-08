package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun ConfirmacionScreen(
    citaId: String,
    onVolverInicio: () -> Unit
) {
    val cita = Repositorio.obtenerCitaPorId(citaId)
    val medico = cita?.let { Repositorio.obtenerMedicoPorId(it.medicoId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Éxito",
            tint = AzulPrimario,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Cita Reservada con Éxito!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Médico: ${medico?.nombre ?: "Especialista"}",
            fontSize = 16.sp
        )
        Text(
            text = "Fecha: ${cita?.fecha ?: ""} - ${cita?.hora ?: ""}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        BotonSaludPlus(
            texto = "Ir al Inicio",
            onClick = onVolverInicio
        )
    }
}