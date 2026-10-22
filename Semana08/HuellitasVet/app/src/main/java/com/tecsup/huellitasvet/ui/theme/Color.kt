package com.tecsup.huellitasvet.ui.theme

import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.EstadoCita

// Dueño (esmeralda)
val Esmeralda = Color(0xFF059669)
val EsmeraldaContenedor = Color(0xFFD1FAE5)
val EsmeraldaOscuro = Color(0xFF065F46)
val DuenoFondo = Color(0xFFF6FDF9)

// Veterinario (azul)
val Azul = Color(0xFF1D4ED8)
val AzulContenedor = Color(0xFFDBEAFE)
val AzulOscuro = Color(0xFF1E3A8A)
val VetFondo = Color(0xFFF7F9FF)

// Estados de la cita
val Pendiente = Color(0xFFD97706)
val Confirmada = Color(0xFF16A34A)
val Atendida = Color(0xFF475569)
val Cancelada = Color(0xFFDC2626)

// Avisos
val AvisoFondo = Color(0xFFFEF3C7)
val AvisoTexto = Color(0xFF92400E)

// Modo oscuro
val FondoOscuro = Color(0xFF0F1513)
val SuperficieOscura = Color(0xFF1A2220)
val FondoOscuroVet = Color(0xFF0F131C)
val SuperficieOscuraVet = Color(0xFF1A1F2B)

fun colorEstado(estado: EstadoCita): Color = when (estado) {
    EstadoCita.PENDIENTE -> Pendiente
    EstadoCita.CONFIRMADA -> Confirmada
    EstadoCita.ATENDIDA -> Atendida
    EstadoCita.CANCELADA -> Cancelada
}
