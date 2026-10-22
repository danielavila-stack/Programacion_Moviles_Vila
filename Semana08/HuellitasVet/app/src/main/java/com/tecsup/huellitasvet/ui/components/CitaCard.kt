package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.EstadoCita

// Tarjeta de una cita: ícono del servicio, título, detalle, estado y una acción opcional.
@Composable
fun CitaCard(
    titulo: String,
    detalle: String,
    estado: EstadoCita,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    accion: (@Composable () -> Unit)? = null
) {
    val contenido: @Composable () -> Unit = {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(titulo, style = MaterialTheme.typography.titleSmall)
                Text(
                    detalle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                EstadoCitaChip(estado)
            }
            if (accion != null) {
                Box(Modifier.align(Alignment.Bottom)) { accion() }
            }
        }
    }
    val colores = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val forma = RoundedCornerShape(16.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier.fillMaxWidth(), shape = forma, colors = colores,
            elevation = CardDefaults.cardElevation(1.dp)) { contenido() }
    } else {
        Card(modifier.fillMaxWidth(), shape = forma, colors = colores,
            elevation = CardDefaults.cardElevation(1.dp)) { contenido() }
    }
}
