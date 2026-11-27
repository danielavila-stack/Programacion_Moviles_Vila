package com.vila.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vila.avisoscampus.notificacion.Notificador
import com.vila.avisoscampus.viewmodel.AvisosViewModel

// Pantalla principal: solo conoce a su ViewModel y al Notificador.
@Composable
fun PantallaAvisos(vm: AvisosViewModel, modifier: Modifier = Modifier) {
    val contexto = LocalContext.current

    // Recoge los eventos del ViewModel. Como la pantalla tiene Context,
    // es ella quien le pide al Notificador que muestre la notificación.
    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            Notificador.mostrar(contexto, aviso)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Tarjeta("Permiso de notificaciones") { Text("Pendiente") }
        Tarjeta("Avisos") { Text("Pendiente") }
        Tarjeta("Recordatorio") { Text("Pendiente") }
        Tarjeta("Receptor de anuncios") { Text("Pendiente") }
        Tarjeta("Historial") { Text("Pendiente") }
    }
}

// Tarjeta reutilizable: título azul en negrita y su contenido debajo
@Composable
fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            contenido()
        }
    }
}
