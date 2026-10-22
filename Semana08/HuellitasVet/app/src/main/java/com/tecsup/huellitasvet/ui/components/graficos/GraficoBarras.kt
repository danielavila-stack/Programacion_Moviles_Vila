package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Gráfico de barras verticales: valor arriba, barra dibujada con Canvas y etiqueta abajo.
@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    altoMaximo: Dp = 90.dp
) {
    val maximo = datos.maxOfOrNull { it.valor }?.takeIf { it > 0f } ?: 1f
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        datos.forEach { d ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(d.valor.toInt().toString(), style = MaterialTheme.typography.labelSmall)
                Canvas(
                    Modifier
                        .width(44.dp)
                        .height(altoMaximo)
                ) {
                    val alto = (size.height * d.valor / maximo).coerceAtLeast(4.dp.toPx())
                    drawRoundRect(
                        color = d.color,
                        topLeft = Offset(0f, size.height - alto),
                        size = Size(size.width, alto),
                        cornerRadius = CornerRadius(6.dp.toPx())
                    )
                }
                Text(
                    d.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(72.dp)
                )
            }
        }
    }
}
