package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class EstiloBoton { PRINCIPAL, SECUNDARIO, PELIGRO }

// Botón del color del rol (primary). SECUNDARIO = contorno; PELIGRO = contorno rojo.
@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    estilo: EstiloBoton = EstiloBoton.PRINCIPAL
) {
    val forma = RoundedCornerShape(12.dp)
    when (estilo) {
        EstiloBoton.PRINCIPAL -> Button(
            onClick = onClick,
            enabled = enabled,
            shape = forma,
            modifier = modifier.height(50.dp)
        ) { Text(texto, style = MaterialTheme.typography.labelLarge) }

        EstiloBoton.SECUNDARIO, EstiloBoton.PELIGRO -> {
            val color = if (estilo == EstiloBoton.PELIGRO) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                shape = forma,
                border = BorderStroke(1.5.dp, color),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
                modifier = modifier.height(50.dp)
            ) { Text(texto, style = MaterialTheme.typography.labelLarge) }
        }
    }
}
