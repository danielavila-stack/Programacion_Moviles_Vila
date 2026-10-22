package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Gráfico de dona con el total al centro y la leyenda a la derecha.
@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    tamano: Dp = 110.dp,
    subtitulo: String = "citas"
) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val fondo = MaterialTheme.colorScheme.surfaceVariant
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(tamano), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val grosor = size.minDimension * 0.16f
                val arco = Size(size.width - grosor, size.height - grosor)
                val origen = Offset(grosor / 2, grosor / 2)
                drawArc(fondo, 0f, 360f, false, origen, arco, style = Stroke(grosor))
                var inicio = -90f
                if (total > 0f) {
                    datos.forEach { d ->
                        val barrido = 360f * d.valor / total
                        drawArc(d.color, inicio, barrido, false, origen, arco, style = Stroke(grosor))
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(total.toInt().toString(), style = MaterialTheme.typography.titleLarge)
                Text(subtitulo, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.width(20.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            datos.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(d.color, RoundedCornerShape(3.dp)))
                    Text(
                        d.etiqueta,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                    Text(d.valor.toInt().toString(), style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
