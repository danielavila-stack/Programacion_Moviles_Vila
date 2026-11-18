package com.vila.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vila.campusgps.model.TipoError
import com.vila.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Recuadro de color de error con el texto
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = error.mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Botón según el tipo de error
        when (error.tipo) {
            TipoError.SIN_PERMISO ->
                OutlinedButton(onClick = onAbrirAjustes) { Text("Abrir ajustes") }
            TipoError.GPS_APAGADO, TipoError.OTRO ->
                OutlinedButton(onClick = onReintentar) { Text("Reintentar") }
            TipoError.SIN_PLAY_SERVICES -> {
                // Sin botón: no hay nada que reintentar
            }
        }
    }
}