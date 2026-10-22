package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.theme.Cancelada

// Un punto de la línea de tiempo del historial clínico.
@Composable
fun HistorialItem(
    fecha: String,
    servicio: String,
    diagnostico: String,
    peso: Double?,
    esUltimo: Boolean,
    modifier: Modifier = Modifier
) {
    val primario = MaterialTheme.colorScheme.primary
    val linea = MaterialTheme.colorScheme.outline
    Row(modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Canvas(
            Modifier
                .width(28.dp)
                .fillMaxHeight()
        ) {
            val cx = size.width / 2
            val r = 10.dp.toPx()
            val cy = 14.dp.toPx() + r
            if (!esUltimo) {
                drawLine(linea, Offset(cx, cy), Offset(cx, size.height), strokeWidth = 2.dp.toPx())
            }
            drawCircle(primario, r, Offset(cx, cy))
            val check = Path().apply {
                moveTo(cx - r * 0.45f, cy)
                lineTo(cx - r * 0.1f, cy + r * 0.35f)
                lineTo(cx + r * 0.5f, cy - r * 0.35f)
            }
            drawPath(check, Color.White, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))
        }
        Card(
            Modifier
                .weight(1f)
                .padding(start = 8.dp, bottom = 12.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(servicio, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                    Text(fecha, style = MaterialTheme.typography.labelMedium, color = primario)
                }
                Text(
                    diagnostico,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (peso != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Favorite, null, tint = Cancelada, modifier = Modifier.size(14.dp))
                        Text(
                            "  Peso: $peso kg",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}
