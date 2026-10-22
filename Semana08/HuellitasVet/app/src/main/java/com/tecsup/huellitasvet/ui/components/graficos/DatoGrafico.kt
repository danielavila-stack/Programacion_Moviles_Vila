package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.ui.graphics.Color

// Un valor para los gráficos (dona y barras).
data class DatoGrafico(
    val etiqueta: String,
    val valor: Float,
    val color: Color
)
