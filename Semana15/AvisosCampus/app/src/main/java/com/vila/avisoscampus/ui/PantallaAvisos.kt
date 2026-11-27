package com.vila.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.material3.Switch
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import com.vila.avisoscampus.receiver.ModoAvionReceiver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.vila.avisoscampus.model.PermisoUi
import com.vila.avisoscampus.ui.theme.GrisSinPedir
import com.vila.avisoscampus.ui.theme.RojoDenegado
import com.vila.avisoscampus.ui.theme.VerdePermitido
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
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val corriendo = faltan != null  // hay una cuenta regresiva en curso
    val escuchando by vm.escuchandoAvion.collectAsState()

    // Registra el receptor solo mientras el Switch esté activo.
    // Se vuelve a ejecutar cada vez que cambia "escuchando".
    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            // Al apagar el Switch, rotar o salir de la pantalla: se quita el receptor
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }  // no hay nada que quitar
        }
    }

    // Lanzador del cuadro de permiso de Android: al responder, avisa al ViewModel
    val lanzadorPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    // Al abrir la pantalla: si el permiso ya estaba concedido, lo marcamos
    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

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
        Tarjeta("Permiso de notificaciones") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Estado:")
                EtiquetaPermiso(permiso)
            }
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Android 13+: abre el cuadro "¿Permitir notificaciones?"
                    lanzadorPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    // Versiones anteriores: el permiso no existe, ya está concedido
                    vm.permisoRespondido(true)
                }
            }) {
                Text("Permitir notificaciones")
            }
        }
        Tarjeta("Avisos") {
            // El ViewModel registra el aviso y emite el evento
            Button(onClick = { vm.avisarAhora() }) {
                Text("Mostrar aviso")
            }
        }
        Tarjeta("Recordatorio") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Deshabilitado mientras la cuenta está corriendo
                Button(
                    onClick = { vm.recordarEn(10) },
                    enabled = !corriendo
                ) {
                    Text("Recordar en 10 s")
                }
                // Solo habilitado mientras la cuenta está corriendo
                OutlinedButton(
                    onClick = { vm.cancelarRecordatorio() },
                    enabled = corriendo
                ) {
                    Text("Cancelar")
                }
            }
            if (faltan != null) {
                Text(
                    text = "Faltan $faltan s",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Tarjeta("Receptor de anuncios") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Escuchar el modo avión")
                Switch(
                    checked = escuchando,
                    onCheckedChange = { vm.cambiarEscucha(it) }
                )
            }
            Text(
                text = "Activa o desactiva el modo avión del celular",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Tarjeta("Historial") { Text("Pendiente") }
    }
}

// Etiqueta de color según el estado del permiso
@Composable
fun EtiquetaPermiso(permiso: PermisoUi) {
    val (texto, color) = when (permiso) {
        PermisoUi.SinPedir -> "Sin pedir" to GrisSinPedir
        PermisoUi.Concedido -> "Permitido" to VerdePermitido
        PermisoUi.Denegado -> "Denegado" to RojoDenegado
    }
    Text(
        text = texto,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .background(color, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
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
