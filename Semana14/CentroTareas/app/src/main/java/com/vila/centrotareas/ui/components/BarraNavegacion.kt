package com.vila.centrotareas.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.vila.centrotareas.R

// MEJORA 3: las pestañas de la barra inferior
private data class Pestana(val titulo: String, @DrawableRes val icono: Int)

private val pestanas = listOf(
    Pestana("Tarea", R.drawable.ic_tarea),
    Pestana("Primos", R.drawable.ic_primos),
    Pestana("Cargas", R.drawable.ic_cargas)
)

// Barra de navegación inferior. Stateless: recibe la pestaña elegida
// y avisa cuál se tocó; no guarda nada ni conoce el ViewModel.
@Composable
fun BarraNavegacion(
    seleccionada: Int,
    onSeleccionar: (Int) -> Unit
) {
    NavigationBar {
        pestanas.forEachIndexed { indice, p ->
            NavigationBarItem(
                selected = seleccionada == indice,
                onClick = { onSeleccionar(indice) },
                icon = { Icon(painterResource(p.icono), contentDescription = p.titulo) },
                label = { Text(p.titulo) }
            )
        }
    }
}