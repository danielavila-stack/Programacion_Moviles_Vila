package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Un punto de la línea: etiqueta del eje X (fecha) y valor (peso)
data class PuntoLinea(val etiqueta: String, val valor: Float)

// Gráfico de línea dibujado con Canvas: grilla, área sombreada, línea, puntos y etiquetas.
@Composable
fun GraficoLineas(
    puntos: List<PuntoLinea>,
    modifier: Modifier = Modifier,
    alto: Dp = 170.dp,
    formatoValor: (Float) -> String = { String.format(java.util.Locale.US, "%.1f", it) }
) {
    val color = MaterialTheme.colorScheme.primary
    val grilla = MaterialTheme.colorScheme.outline
    val texto = MaterialTheme.colorScheme.onSurfaceVariant
    val fondoPunto = MaterialTheme.colorScheme.surface
    val medidor = rememberTextMeasurer()
    val estilo = TextStyle(fontSize = 10.sp, color = texto)
    val estiloValor = TextStyle(fontSize = 11.sp, color = color)

    Canvas(
        modifier
            .fillMaxWidth()
            .height(alto)
    ) {
        if (puntos.isEmpty()) return@Canvas
        val izquierda = 8.dp.toPx()
        val derecha = size.width - 8.dp.toPx()
        val arriba = 22.dp.toPx()
        val abajo = size.height - 22.dp.toPx()

        // Rango con un margen para que la línea no toque los bordes
        val minimo = puntos.minOf { it.valor }
        val maximo = puntos.maxOf { it.valor }
        val margen = ((maximo - minimo) * 0.25f).coerceAtLeast(maximo * 0.05f).coerceAtLeast(0.1f)
        val bajo = minimo - margen
        val alto2 = maximo + margen

        fun x(i: Int) = if (puntos.size == 1) (izquierda + derecha) / 2
        else izquierda + (derecha - izquierda) * i / (puntos.size - 1)
        fun y(v: Float) = abajo - (abajo - arriba) * (v - bajo) / (alto2 - bajo)

        // Grilla horizontal
        repeat(3) { k ->
            val gy = arriba + (abajo - arriba) * k / 2
            drawLine(grilla, Offset(izquierda, gy), Offset(derecha, gy), strokeWidth = 1.dp.toPx())
        }

        // Área bajo la línea y la línea
        if (puntos.size > 1) {
            val linea = Path()
            puntos.forEachIndexed { i, p ->
                if (i == 0) linea.moveTo(x(i), y(p.valor)) else linea.lineTo(x(i), y(p.valor))
            }
            val area = Path().apply {
                addPath(linea)
                lineTo(x(puntos.lastIndex), abajo)
                lineTo(x(0), abajo)
                close()
            }
            drawPath(area, Brush.verticalGradient(listOf(color.copy(alpha = 0.25f), color.copy(alpha = 0.02f)), arriba, abajo))
            drawPath(linea, color, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
        }

        // Puntos, valor encima y fecha debajo
        puntos.forEachIndexed { i, p ->
            val c = Offset(x(i), y(p.valor))
            drawCircle(color, 6.dp.toPx(), c)
            drawCircle(fondoPunto, 3.dp.toPx(), c)

            val valor = medidor.measure(formatoValor(p.valor), estiloValor)
            drawText(
                valor,
                topLeft = Offset(
                    (c.x - valor.size.width / 2).coerceIn(0f, size.width - valor.size.width),
                    c.y - valor.size.height - 6.dp.toPx()
                )
            )
            val fecha = medidor.measure(p.etiqueta, estilo)
            drawText(
                fecha,
                topLeft = Offset(
                    (c.x - fecha.size.width / 2).coerceIn(0f, size.width - fecha.size.width),
                    abajo + 5.dp.toPx()
                )
            )
        }
    }
}
