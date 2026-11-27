package com.vila.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vila.avisoscampus.model.Aviso
import com.vila.avisoscampus.model.Origen

// Lista de avisos. No conoce el ViewModel: solo recibe la lista.
// Usa Column + forEach (no LazyColumn) porque la pantalla ya tiene scroll vertical.
@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    if (avisos.isEmpty()) {
        Text(
            text = "Sin avisos todavía",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Solo los 20 más recientes (el Repository ya los guarda con el nuevo arriba)
        avisos.take(20).forEach { aviso ->
            FilaAviso(aviso)
        }
    }
}

// Una fila: etiqueta de origen, título, texto y hora
@Composable
private fun FilaAviso(aviso: Aviso) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EtiquetaOrigen(aviso.origen)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = aviso.titulo,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = aviso.texto,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = aviso.hora,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Etiqueta pequeña con un color distinto por origen
@Composable
private fun EtiquetaOrigen(origen: Origen) {
    val color = when (origen) {
        Origen.BOTON -> Color(0xFF1565C0)        // azul
        Origen.RECORDATORIO -> Color(0xFFEF6C00) // naranja
        Origen.MODO_AVION -> Color(0xFF00838F)   // turquesa
        Origen.CARGADOR -> Color(0xFF2E7D32)     // verde · Fase 2 · Mejora 3
    }
    Text(
        text = origen.texto,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(color, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
