package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.diaSemanaCorto
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.iconoServicio
import com.tecsup.huellitasvet.ui.components.pelajePara
import com.tecsup.huellitasvet.ui.components.soles
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel

@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    onBack: () -> Unit,
    onCitaCreada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsStateWithLifecycle()
    val servicios by vm.servicios.collectAsStateWithLifecycle()
    val mascotaId by vm.mascotaId.collectAsStateWithLifecycle()
    val servicioId by vm.servicioId.collectAsStateWithLifecycle()
    val fecha by vm.fecha.collectAsStateWithLifecycle()
    val hora by vm.hora.collectAsStateWithLifecycle()
    val noDisponibles by vm.horasNoDisponibles.collectAsStateWithLifecycle()
    val puedeConfirmar by vm.puedeConfirmar.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val creada by vm.creada.collectAsStateWithLifecycle()

    LaunchedEffect(creada) { if (creada) onCitaCreada() }

    Scaffold(
        topBar = { AppTopBar("Pedir cita", onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ¿Quién viene?
            Titulo("¿Quién viene?")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(mascotas, key = { it.id }) { m ->
                    val especie = Especie.desde(m.especie)
                    val activa = m.id == mascotaId
                    Surface(
                        onClick = { vm.elegirMascota(m.id) },
                        shape = RoundedCornerShape(50),
                        color = if (activa) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.5.dp, if (activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                    ) {
                        Row(
                            Modifier.padding(start = 6.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MascotaAvatar(especie, tamano = 32.dp, pelaje = pelajePara(especie, m.nombre))
                            Text(
                                "  ${m.nombre}",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Servicio
            Titulo("Servicio")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                servicios.forEach { s ->
                    val activo = s.id == servicioId
                    Surface(
                        onClick = { vm.elegirServicio(s.id) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (activo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.5.dp, if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                iconoServicio(s.icono), null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Column(
                                Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            ) {
                                Text(s.nombre, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    "${s.duracionMin} min",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(soles(s.precio), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // Fecha: de hoy a 7 días
            Titulo("Fecha")
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

            // Hora: 18 horarios, los ocupados o pasados en gris
            Row(verticalAlignment = Alignment.Bottom) {
                Titulo("Hora", Modifier.weight(1f))
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

            // Resumen
            val mascota = mascotas.firstOrNull { it.id == mascotaId }
            val servicio = servicios.firstOrNull { it.id == servicioId }
            if (mascota != null && servicio != null) {
                Spacer(Modifier.height(16.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        "${mascota.nombre} · ${servicio.nombre}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${fechaCorta(fecha.toString())} · ${hora ?: "--:--"} · ${soles(servicio.precio)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            VetButton("Confirmar cita", onClick = vm::confirmar, enabled = puedeConfirmar)
            Spacer(Modifier.height(16.dp))
        }
    }

    if (mensaje != null) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("Pedir cita") },
            text = { Text(mensaje!!) },
            confirmButton = { TextButton(onClick = vm::limpiarMensaje) { Text("Entendido") } }
        )
    }
}

@Composable
private fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}
