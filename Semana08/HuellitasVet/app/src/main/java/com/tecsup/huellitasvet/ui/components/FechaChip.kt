package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Selector de día: muestra el día de la semana ("Mié") y el número ("21").
@Composable
fun FechaChip(
    diaSemana: String,
    numero: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (seleccionado) colores.primary else colores.surface,
        contentColor = if (seleccionado) colores.onPrimary else colores.onSurface,
        border = if (seleccionado) null else BorderStroke(1.dp, colores.outline),
        modifier = modifier.width(56.dp)
    ) {
        Column(
            Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(diaSemana, style = MaterialTheme.typography.labelSmall)
            Text(numero, style = MaterialTheme.typography.titleMedium)
        }
    }
}
