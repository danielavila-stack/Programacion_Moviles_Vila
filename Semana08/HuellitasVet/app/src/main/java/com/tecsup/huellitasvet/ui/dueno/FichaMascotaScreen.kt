package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.Pestanas
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.diaMes
import com.tecsup.huellitasvet.ui.components.kilos
import com.tecsup.huellitasvet.ui.components.pelajePara
import com.tecsup.huellitasvet.ui.components.textoEdad
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

// Ficha de la mascota con pestañas Datos / Historial clínico.
// esDueno = false (veterinario): solo consulta, sin editar ni pedir cita.
@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    esDueno: Boolean,
    onBack: () -> Unit,
    onPedirCita: (Int) -> Unit,
    onEditar: (Int) -> Unit
) {
    val mascota by vm.mascota.collectAsStateWithLifecycle()
    val historial by remember(mascotaId) { vm.historial(mascotaId) }.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val terminado by vm.terminado.collectAsStateWithLifecycle()
    var pestana by rememberSaveable { mutableIntStateOf(1) }
    var confirmarEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(terminado) { if (terminado) onBack() }

    Scaffold(
        topBar = { AppTopBar(mascota?.nombre ?: "", onBack = onBack) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        val m = mascota ?: return@Scaffold
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Cabecera(m, vm.edad(m), historial.size)
            Spacer(Modifier.height(12.dp))

            if (esDueno) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { onPedirCita(m.id) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Icon(Icons.Filled.CalendarMonth, null, modifier = Modifier.size(18.dp))
                        Text("  Pedir cita")
                    }
                    OutlinedButton(
                        onClick = { onEditar(m.id) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Icon(Icons.Filled.Edit, null, modifier = Modifier.size(18.dp))
                        Text("  Editar")
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Pestanas(listOf("Datos", "Historial clínico"), pestana, { pestana = it })
            Spacer(Modifier.height(14.dp))

            if (pestana == 0) {
                Datos(m)
                if (esDueno) {
                    Spacer(Modifier.height(20.dp))
                    VetButton("Eliminar mascota", onClick = { confirmarEliminar = true }, estilo = EstiloBoton.PELIGRO)
                }
            } else if (historial.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no tiene atenciones", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                historial.forEachIndexed { i, c ->
                    HistorialItem(
                        fecha = diaMes(c.cita.fecha),
                        servicio = c.servicio.nombre,
                        diagnostico = c.cita.diagnostico.orEmpty(),
                        peso = c.cita.pesoRegistrado,
                        esUltimo = i == historial.lastIndex
                    )
                }
            }
        }
    }

    if (confirmarEliminar) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar mascota") },
            text = { Text("¿Seguro que quieres eliminar a ${mascota?.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminar = false
                    mascota?.let(vm::eliminar)
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { confirmarEliminar = false }) { Text("Cancelar") } }
        )
    }

    if (mensaje != null) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("No se puede eliminar") },
            text = { Text(mensaje!!) },
            confirmButton = { TextButton(onClick = vm::limpiarMensaje) { Text("Entendido") } }
        )
    }
}

@Composable
private fun Cabecera(m: Mascota, edad: Int, atenciones: Int) {
    val especie = Especie.desde(m.especie)
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            MascotaAvatar(especie, tamano = 72.dp, pelaje = pelajePara(especie, m.nombre))
            Column(Modifier.padding(start = 16.dp)) {
                Text(m.nombre, style = MaterialTheme.typography.titleLarge)
                Text(
                    "${especie.etiqueta} ${m.raza.lowercase()} · ${textoEdad(edad)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${Sexo.desde(m.sexo).etiqueta} · ${kilos(m.peso)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    if (atenciones == 1) "1 atención" else "$atenciones atenciones",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(0.dp)
                )
            }
        }
    }
}

@Composable
private fun Datos(m: Mascota) {
    val filas = listOf(
        "Especie" to Especie.desde(m.especie).etiqueta,
        "Raza" to m.raza,
        "Sexo" to Sexo.desde(m.sexo).etiqueta,
        "Año de nacimiento" to m.anioNacimiento.toString(),
        "Peso actual" to kilos(m.peso)
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            filas.forEachIndexed { i, (k, v) ->
                Row(Modifier.padding(vertical = 10.dp)) {
                    Text(k, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(v, style = MaterialTheme.typography.titleSmall)
                }
                if (i < filas.lastIndex) HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}
