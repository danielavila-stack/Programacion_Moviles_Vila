package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

// Selector de hora. 'ocupado' = gris y tachado, no se puede tocar.
@Composable
fun HoraChip(
    hora: String,
    seleccionado: Boolean,
    ocupado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        enabled = !ocupado,
        shape = RoundedCornerShape(10.dp),
        color = when {
            ocupado -> colores.surfaceVariant
            seleccionado -> colores.primary
            else -> colores.surface
        },
        contentColor = when {
            ocupado -> colores.onSurfaceVariant.copy(alpha = 0.5f)
            seleccionado -> colores.onPrimary
            else -> colores.onSurface
        },
        border = if (ocupado || seleccionado) null else BorderStroke(1.dp, colores.outline),
        modifier = modifier
    ) {
        Box(Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
            Text(
                hora,
                style = MaterialTheme.typography.labelLarge,
                textDecoration = if (ocupado) TextDecoration.LineThrough else null
            )
        }
    }
}
