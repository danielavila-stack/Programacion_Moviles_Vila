package com.vila.centrotareas.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Círculo de progreso con el porcentaje y un texto al centro (lo usan Tarea y Primos).
// Stateless: solo dibuja lo que recibe.
@Composable
fun CirculoProgreso(progreso: Float, texto: String, tamano: Dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(tamano)
            .padding(8.dp)
    ) {
        CircularProgressIndicator(
            progress = { progreso },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 14.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${(progreso * 100).toInt()} %",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                texto,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}