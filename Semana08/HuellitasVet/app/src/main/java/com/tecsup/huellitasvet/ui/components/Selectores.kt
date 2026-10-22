package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Fila de opciones del mismo ancho (especie, sexo, duración...). Una sola elegida.
@Composable
fun <T> OpcionesChip(
    opciones: List<T>,
    seleccionada: T?,
    texto: (T) -> String,
    onElegir: (T) -> Unit,
    modifier: Modifier = Modifier,
    rellena: Boolean = true
) {
    val c = MaterialTheme.colorScheme
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        opciones.forEach { op ->
            val activa = op == seleccionada
            Surface(
                onClick = { onElegir(op) },
                shape = RoundedCornerShape(12.dp),
                color = when {
                    activa && rellena -> c.primary
                    activa -> c.primaryContainer
                    else -> c.surface
                },
                contentColor = if (activa && rellena) c.onPrimary else if (activa) c.primary else c.onSurface,
                border = BorderStroke(1.dp, if (activa) c.primary else c.outline),
                modifier = Modifier.weight(1f)
            ) {
                Box(Modifier.padding(vertical = 11.dp), contentAlignment = Alignment.Center) {
                    Text(texto(op), style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

// Pestañas tipo segmento (Datos / Historial, Próximas / Historial)
@Composable
fun Pestanas(
    titulos: List<String>,
    seleccionada: Int,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = MaterialTheme.colorScheme
    Row(
        modifier
            .fillMaxWidth()
            .background(c.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        titulos.forEachIndexed { i, t ->
            val activa = i == seleccionada
            Surface(
                onClick = { onElegir(i) },
                shape = RoundedCornerShape(10.dp),
                color = if (activa) c.surface else c.surfaceVariant,
                contentColor = if (activa) c.primary else c.onSurfaceVariant,
                shadowElevation = if (activa) 1.dp else 0.dp,
                modifier = Modifier.weight(1f)
            ) {
                Box(Modifier.padding(vertical = 9.dp), contentAlignment = Alignment.Center) {
                    Text(t, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
