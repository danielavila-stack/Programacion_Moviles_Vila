package com.vila.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vila.campusgps.model.LugarConDistancia
import com.vila.campusgps.ui.theme.VerdeCercano
import com.vila.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            val esSeleccionado = item.lugar.id == seleccionado
            val esCercano = item.lugar.id == masCercanoId
            val primario = MaterialTheme.colorScheme.primary

            // Colores según el estado de la tarjeta
            val fondoTarjeta = when {
                esCercano -> VerdeCercano.copy(alpha = 0.15f)
                esSeleccionado -> primario.copy(alpha = 0.12f)
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
            val borde = when {
                esSeleccionado -> BorderStroke(1.dp, primario)
                esCercano -> BorderStroke(1.dp, VerdeCercano)
                else -> null
            }
            val fondoIcono = if (esCercano) VerdeCercano else primario.copy(alpha = 0.15f)
            val colorIcono = if (esCercano) Color.White else primario
            val colorDistancia = if (esCercano) VerdeCercano else primario

            Card(
                onClick = { onClick(item.lugar.id) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = fondoTarjeta),
                border = borde
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // MEJORA 2: ícono de la categoría dentro de un círculo
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(fondoIcono, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(iconoCategoria(item.lugar.categoria)),
                            contentDescription = item.lugar.categoria,
                            tint = colorIcono,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.lugar.nombre, fontWeight = FontWeight.Bold)
                        Text(
                            if (esCercano) "${item.lugar.categoria} · más cercano" else item.lugar.categoria,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                        color = colorDistancia,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}