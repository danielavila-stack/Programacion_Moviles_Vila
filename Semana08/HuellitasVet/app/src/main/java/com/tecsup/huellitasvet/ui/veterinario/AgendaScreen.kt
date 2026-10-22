package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.diaSemanaCorto
import com.tecsup.huellitasvet.ui.components.nombreCorto
import com.tecsup.huellitasvet.ui.components.pelajePara
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate

@Composable
fun AgendaScreen(vm: AgendaViewModel, onAtender: (Int) -> Unit) {
    val fecha by vm.fechaSeleccionada.collectAsStateWithLifecycle()
    val citas by vm.citas.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar("Agenda") },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Chips de los próximos días
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.fechas) { f ->
                    val activa = f == fecha
                    Surface(
                        onClick = { vm.elegirFecha(f) },
                        shape = RoundedCornerShape(50),
                        color = if (activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (activa) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        border = if (activa) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            "${if (f == LocalDate.now()) "Hoy" else diaSemanaCorto(f)} ${f.dayOfMonth}",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            if (citas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay citas para este día", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                // Línea de tiempo por hora
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                ) {
                    items(citas, key = { it.cita.id }) { c ->
                        FilaAgenda(
                            c,
                            onConfirmar = { vm.confirmar(c) },
                            onAtender = { onAtender(c.cita.id) },
                            onCancelar = { vm.cancelar(c) }
                        )
                    }
                }
            }
        }
    }

    if (mensaje != null) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("Agenda") },
            text = { Text(mensaje!!) },
            confirmButton = { TextButton(onClick = vm::limpiarMensaje) { Text("Entendido") } }
        )
    }
}

@Composable
private fun FilaAgenda(
    c: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit,
    onCancelar: () -> Unit
) {
    val m = c.mascota.mascota
    val especie = Especie.desde(m.especie)
    val estado = EstadoCita.desde(c.cita.estado)
    Row(Modifier.height(IntrinsicSize.Min)) {
        Text(
            c.cita.hora,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(52.dp)
                .padding(top = 14.dp)
        )
        VerticalDivider(Modifier.fillMaxHeight(), color = MaterialTheme.colorScheme.outline)
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, bottom = 10.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(especie, tamano = 40.dp, pelaje = pelajePara(especie, m.nombre))
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text("${m.nombre} · ${c.servicio.nombre}", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Dueño: ${nombreCorto(c.mascota.dueno.nombre)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EstadoCitaChip(estado)
                    Spacer(Modifier.weight(1f))
                    if (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) {
                        TextButton(onClick = onCancelar) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    when (estado) {
                        EstadoCita.PENDIENTE -> Button(onClick = onConfirmar) { Text("Confirmar") }
                        EstadoCita.CONFIRMADA -> Button(onClick = onAtender) { Text("Atender") }
                        else -> {}
                    }
                }
            }
        }
    }
}
