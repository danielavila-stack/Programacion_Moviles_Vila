package com.vila.murocampus.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vila.murocampus.ui.theme.EtiquetaAmarilla
import com.vila.murocampus.ui.theme.EtiquetaAzul

// Tarjeta de una publicación: etiqueta "Usuario N", título en negrita y el texto
@Composable
fun TarjetaPost(
    userId: Int,
    titulo: String,
    cuerpo: String,
    modifier: Modifier = Modifier,
    maxLineas: Int = 2,
    onClick: (() -> Unit)? = null
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Usuarios impares en azul y pares en amarillo
            val esPar = userId % 2 == 0
            val fondo = if (esPar) EtiquetaAmarilla else EtiquetaAzul
            val letra = if (esPar) Color(0xFF3E2A00) else Color.White

            Surface(
                color = fondo,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    "Usuario $userId",
                    style = MaterialTheme.typography.labelMedium,
                    color = letra,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                cuerpo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = maxLineas,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Indicador de carga centrado con un texto
@Composable
fun Cargando(texto: String = "Cargando publicaciones…", modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// Recuadro de error con el mensaje y el botón Reintentar
@Composable
fun ErrorConReintento(mensaje: String, onReintentar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}