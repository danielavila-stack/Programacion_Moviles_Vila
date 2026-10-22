package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.iconoServicio
import com.tecsup.huellitasvet.ui.components.kilos
import com.tecsup.huellitasvet.ui.components.nombreCorto
import com.tecsup.huellitasvet.ui.components.pelajePara
import com.tecsup.huellitasvet.ui.theme.Cancelada
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import java.time.LocalDate

@Composable
fun AtenderCitaScreen(vm: AtenderCitaViewModel, onListo: () -> Unit) {
    val cita by vm.cita.collectAsStateWithLifecycle()
    val errores by vm.errores.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val terminado by vm.terminado.collectAsStateWithLifecycle()

    var peso by rememberSaveable { mutableStateOf("") }
    var diagnostico by rememberSaveable { mutableStateOf("") }
    var tratamiento by rememberSaveable { mutableStateOf("") }
    var confirmarCancelar by remember { mutableStateOf(false) }

    LaunchedEffect(terminado) { if (terminado) onListo() }

    Scaffold(
        topBar = { AppTopBar("Atender cita", onBack = onListo) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        val c = cita ?: return@Scaffold
        val m = c.mascota.mascota
        val especie = Especie.desde(m.especie)
        val estado = EstadoCita.desde(c.cita.estado)
        val editable = vm.sePuedeAtender(c)

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Cabecera: mascota, dueño, servicio y hora
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(especie, tamano = 60.dp, pelaje = pelajePara(especie, m.nombre))
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text("${m.nombre} · ${especie.etiqueta} ${m.raza.lowercase()}", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "${LocalDate.now().year - m.anioNacimiento} años · ${Sexo.desde(m.sexo).etiqueta} · Dueño: ${nombreCorto(c.mascota.dueno.nombre)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(iconoServicio(c.servicio.icono), null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Text(
                                " ${c.servicio.nombre} · ${fechaCorta(c.cita.fecha)} · ${c.cita.hora}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    EstadoCitaChip(estado)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Registro clínico", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            // Peso nuevo y peso anterior
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                VetTextField(
                    peso, { peso = it; vm.limpiarError("peso") }, "Peso (kg)",
                    modifier = Modifier.weight(1f), error = errores.peso, teclado = KeyboardType.Decimal
                )
                Column(
                    Modifier
                        .weight(1f)
                        .padding(top = 8.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Favorite, null, tint = Cancelada, modifier = Modifier.size(14.dp))
                        Text("  Peso anterior", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(kilos(m.peso), style = MaterialTheme.typography.titleSmall)
                }
            }
            Spacer(Modifier.height(10.dp))
            VetTextField(
                diagnostico, { diagnostico = it; vm.limpiarError("diagnostico") }, "Diagnóstico",
                error = errores.diagnostico, lineas = 3
            )
            Spacer(Modifier.height(10.dp))
            VetTextField(tratamiento, { tratamiento = it }, "Tratamiento", lineas = 3)
            Spacer(Modifier.height(12.dp))

            // Aviso: la transacción también actualiza el peso de la mascota
            val pesoNuevo = peso.replace(',', '.').toDoubleOrNull()
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Text(
                    "  Se actualizará el peso de ${m.nombre}" + (pesoNuevo?.let { " a ${kilos(it)}" } ?: ""),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(20.dp))

            VetButton("Registrar atención", onClick = { vm.registrar(diagnostico, tratamiento, peso) }, enabled = editable)
            Spacer(Modifier.height(10.dp))
            VetButton("Cancelar cita", onClick = { confirmarCancelar = true }, enabled = editable, estilo = EstiloBoton.PELIGRO)
            if (!editable) {
                Text(
                    "Esta cita está ${estado.etiqueta.lowercase()}: ya no se puede atender.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (confirmarCancelar) {
        AlertDialog(
            onDismissRequest = { confirmarCancelar = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que quieres cancelar esta cita?") },
            confirmButton = {
                TextButton(onClick = { confirmarCancelar = false; vm.cancelar() }) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { confirmarCancelar = false }) { Text("No") } }
        )
    }

    if (mensaje != null) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("Atender cita") },
            text = { Text(mensaje!!) },
            confirmButton = { TextButton(onClick = vm::limpiarMensaje) { Text("Entendido") } }
        )
    }
}
