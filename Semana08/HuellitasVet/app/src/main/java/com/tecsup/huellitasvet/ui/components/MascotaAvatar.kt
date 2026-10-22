package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie

private val Ojo = Color(0xFF1F2937)
private val Rosado = Color(0xFFF9A8D4)
private val Crema = Color(0xFFFFF1DC)

// Pelaje sugerido según el nombre, para que cada mascota se distinga.
fun pelajePara(especie: Especie, nombre: String): Color {
    val paleta = when (especie) {
        Especie.PERRO -> listOf(Color(0xFFD08B4F), Color(0xFF4B3A33), Color(0xFFE0B27A))
        Especie.GATO -> listOf(Color(0xFF9CA3AF), Color(0xFFF59E0B), Color(0xFF6B7280))
        Especie.OTRO -> listOf(Color(0xFFE7D7C1), Color(0xFFC8B6A6), Color(0xFFF5F5F4))
    }
    return paleta[Math.floorMod(nombre.hashCode(), paleta.size)]
}

// Carita de perro, gato u otro animal dibujada con Canvas.
@Composable
fun MascotaAvatar(
    especie: Especie,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp,
    pelaje: Color? = null,
    fondo: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Canvas(
        modifier
            .size(tamano)
            .clip(CircleShape)
            .background(fondo)
    ) {
        when (especie) {
            Especie.PERRO -> perro(pelaje ?: Color(0xFFD08B4F))
            Especie.GATO -> gato(pelaje ?: Color(0xFF9CA3AF))
            Especie.OTRO -> conejo(pelaje ?: Color(0xFFE7D7C1))
        }
    }
}

private fun DrawScope.perro(pelo: Color) {
    val s = size.minDimension
    val cx = size.width / 2
    val oreja = lerp(pelo, Color.Black, 0.3f)
    // orejas caídas
    drawOval(oreja, Offset(cx - 0.36f * s, 0.26f * s), Size(0.18f * s, 0.34f * s))
    drawOval(oreja, Offset(cx + 0.18f * s, 0.26f * s), Size(0.18f * s, 0.34f * s))
    // cabeza
    drawCircle(pelo, 0.26f * s, Offset(cx, 0.52f * s))
    // hocico
    drawOval(Crema, Offset(cx - 0.13f * s, 0.55f * s), Size(0.26f * s, 0.18f * s))
    drawOval(Ojo, Offset(cx - 0.05f * s, 0.55f * s), Size(0.10f * s, 0.065f * s))
    drawLine(Ojo, Offset(cx, 0.61f * s), Offset(cx, 0.66f * s), strokeWidth = 0.015f * s)
    // ojos
    drawCircle(Ojo, 0.032f * s, Offset(cx - 0.10f * s, 0.46f * s))
    drawCircle(Ojo, 0.032f * s, Offset(cx + 0.10f * s, 0.46f * s))
    drawCircle(Color.White, 0.010f * s, Offset(cx - 0.09f * s, 0.45f * s))
    drawCircle(Color.White, 0.010f * s, Offset(cx + 0.11f * s, 0.45f * s))
}

private fun DrawScope.gato(pelo: Color) {
    val s = size.minDimension
    val cx = size.width / 2
    // orejas triangulares
    fun oreja(signo: Float, color: Color, escala: Float) {
        val p = Path().apply {
            moveTo(cx + signo * 0.25f * s, 0.44f * s)
            lineTo(cx + signo * (0.25f - 0.04f * escala) * s, (0.40f - 0.24f * escala) * s)
            lineTo(cx + signo * (0.25f - 0.20f * escala) * s, 0.32f * s)
            close()
        }
        drawPath(p, color)
    }
    oreja(-1f, pelo, 1f)
    oreja(1f, pelo, 1f)
    oreja(-1f, Rosado, 0.55f)
    oreja(1f, Rosado, 0.55f)
    // cabeza
    drawCircle(pelo, 0.26f * s, Offset(cx, 0.54f * s))
    // ojos
    drawOval(Ojo, Offset(cx - 0.135f * s, 0.45f * s), Size(0.06f * s, 0.08f * s))
    drawOval(Ojo, Offset(cx + 0.075f * s, 0.45f * s), Size(0.06f * s, 0.08f * s))
    // nariz
    val nariz = Path().apply {
        moveTo(cx - 0.035f * s, 0.575f * s)
        lineTo(cx + 0.035f * s, 0.575f * s)
        lineTo(cx, 0.615f * s)
        close()
    }
    drawPath(nariz, Rosado)
    // bigotes
    val b = 0.008f * s
    drawLine(Ojo, Offset(cx - 0.08f * s, 0.61f * s), Offset(cx - 0.24f * s, 0.58f * s), b)
    drawLine(Ojo, Offset(cx - 0.08f * s, 0.63f * s), Offset(cx - 0.24f * s, 0.65f * s), b)
    drawLine(Ojo, Offset(cx + 0.08f * s, 0.61f * s), Offset(cx + 0.24f * s, 0.58f * s), b)
    drawLine(Ojo, Offset(cx + 0.08f * s, 0.63f * s), Offset(cx + 0.24f * s, 0.65f * s), b)
    drawArc(
        Ojo, 20f, 140f, false,
        topLeft = Offset(cx - 0.04f * s, 0.59f * s), size = Size(0.08f * s, 0.06f * s),
        style = Stroke(width = b)
    )
}

private fun DrawScope.conejo(pelo: Color) {
    val s = size.minDimension
    val cx = size.width / 2
    // orejas largas
    drawOval(pelo, Offset(cx - 0.17f * s, 0.08f * s), Size(0.12f * s, 0.38f * s))
    drawOval(pelo, Offset(cx + 0.05f * s, 0.08f * s), Size(0.12f * s, 0.38f * s))
    drawOval(Rosado, Offset(cx - 0.14f * s, 0.13f * s), Size(0.06f * s, 0.28f * s))
    drawOval(Rosado, Offset(cx + 0.08f * s, 0.13f * s), Size(0.06f * s, 0.28f * s))
    // cabeza
    drawCircle(pelo, 0.24f * s, Offset(cx, 0.60f * s))
    // ojos y nariz
    drawCircle(Ojo, 0.03f * s, Offset(cx - 0.09f * s, 0.56f * s))
    drawCircle(Ojo, 0.03f * s, Offset(cx + 0.09f * s, 0.56f * s))
    drawCircle(Rosado, 0.03f * s, Offset(cx, 0.64f * s))
    // dientes
    drawRect(Color.White, Offset(cx - 0.03f * s, 0.67f * s), Size(0.06f * s, 0.05f * s))
    drawLine(Ojo, Offset(cx, 0.67f * s), Offset(cx, 0.72f * s), 0.006f * s)
}
