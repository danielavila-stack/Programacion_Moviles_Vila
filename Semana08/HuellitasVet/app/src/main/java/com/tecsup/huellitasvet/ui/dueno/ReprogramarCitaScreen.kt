package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.diaSemanaCorto
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.pelajePara
import com.tecsup.huellitasvet.ui.theme.AvisoFondo
import com.tecsup.huellitasvet.ui.theme.AvisoTexto
import com.tecsup.huellitasvet.viewmodel.ReprogramarCitaViewModel

@Composable
fun ReprogramarCitaScreen(vm: ReprogramarCitaViewModel, onListo: () -> Unit) {
    val cita by vm.cita.collectAsStateWithLifecycle()
    val fecha by vm.fecha.collectAsStateWithLifecycle()
    val hora by vm.hora.collectAsStateWithLifecycle()
    val noDisponibles by vm.horasNoDisponibles.collectAsStateWithLifecycle()
    val puedeConfirmar by vm.puedeConfirmar.collectAsStateWithLifecycle()
    val clinicaAbierta by vm.clinicaAbierta.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val terminado by vm.terminado.collectAsStateWithLifecycle()

    LaunchedEffect(terminado) { if (terminado) onListo() }

    Scaffold(
        topBar = { AppTopBar("Reprogramar cita", onBack = onListo) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        val c = cita ?: return@Scaffold
        val m = c.mascota.mascota
        val especie = Especie.desde(m.especie)
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Cita actual
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(especie, tamano = 48.dp, pelaje = pelajePara(especie, m.nombre))
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text("${m.nombre} · ${c.servicio.nombre}", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Actual: ${fechaCorta(c.cita.fecha)} · ${c.cita.hora}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    EstadoCitaChip(EstadoCita.desde(c.cita.estado))
                }
            }

            if (!clinicaAbierta) {
                Spacer(Modifier.height(10.dp))
                Aviso("La clínica no está recibiendo citas")
            }

            Titulo("Nueva fecha")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f ->
                    FechaChip(
                        diaSemana = diaSemanaCorto(f),
                        numero = f.dayOfMonth.toString(),
                        seleccionado = f == fecha,
                        onClick = { vm.elegirFecha(f) }
                    )
                }
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Titulo("Nueva hora", Modifier.weight(1f))
                Text(
                    "Ocupado = gris",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.horarios.chunked(4).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { h ->
                            HoraChip(
                                hora = h,
                                seleccionado = h == hora,
                                ocupado = h in noDisponibles,
                                onClick = { vm.elegirHora(h) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text("Nuevo horario", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(
                    "${fechaCorta(fecha.toString())} · ${hora ?: "--:--"}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "La cita quedará Pendiente hasta que la clínica la confirme",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(16.dp))
            VetButton("Confirmar cambio", onClick = vm::confirmar, enabled = puedeConfirmar)
            Spacer(Modifier.height(16.dp))
        }
    }

    if (mensaje != null) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("Reprogramar cita") },
            text = { Text(mensaje!!) },
            confirmButton = { TextButton(onClick = vm::limpiarMensaje) { Text("Entendido") } }
        )
    }
}

@Composable
private fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(texto, style = MaterialTheme.typography.titleSmall, modifier = modifier.padding(top = 16.dp, bottom = 8.dp))
}

@Composable
private fun Aviso(texto: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(AvisoFondo, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Info, null, tint = AvisoTexto, modifier = Modifier.size(20.dp))
        Text("  $texto", style = MaterialTheme.typography.titleSmall, color = AvisoTexto)
    }
}
