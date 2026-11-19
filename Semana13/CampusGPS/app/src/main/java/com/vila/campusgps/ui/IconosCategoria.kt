package com.vila.campusgps.ui

import androidx.annotation.DrawableRes
import com.vila.campusgps.R

// Solo presentación: qué dibujo usa cada categoría
@DrawableRes
fun iconoCategoria(categoria: String): Int = when (categoria) {
    "Estudio" -> R.drawable.ic_estudio
    "Comida" -> R.drawable.ic_comida
    "Eventos" -> R.drawable.ic_eventos
    else -> R.drawable.ic_servicios
}