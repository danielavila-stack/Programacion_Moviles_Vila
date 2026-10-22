package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.ICONOS_SERVICIO
import com.tecsup.huellitasvet.ui.components.OpcionesChip
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.components.iconoServicio
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import java.util.Locale

// Sirve para crear (servicioId = 0) y para editar
@Composable
fun FormularioServicioScreen(vm: ServiciosViewModel, servicioId: Int, onListo: () -> Unit) {
    val editando by vm.editando.collectAsStateWithLifecycle()
    val errores by vm.errores.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val terminado by vm.terminado.collectAsStateWithLifecycle()

    var nombre by rememberSaveable { mutableStateOf("") }
    var duracion by rememberSaveable { mutableIntStateOf(30) }
    var precio by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var icono by rememberSaveable { mutableStateOf("heart") }
    var activo by rememberSaveable { mutableStateOf(true) }
    var cargado by rememberSaveable { mutableStateOf(false) }
    var confirmarEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(servicioId) { vm.cargar(servicioId) }
    // Al editar, se llenan los campos una sola vez
    LaunchedEffect(editando) {
        val s = editando
        if (s != null && !cargado) {
            nombre = s.nombre
            duracion = s.duracionMin
            precio = String.format(Locale.US, "%.2f", s.precio)
            descripcion = s.descripcion
            icono = s.icono
            activo = s.activo
            cargado = true
        }
    }
    LaunchedEffect(terminado) { if (terminado) onListo() }

    Scaffold(
        topBar = { AppTopBar(if (servicioId == 0) "Nuevo servicio" else "Editar servicio", onBack = onListo) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            VetTextField(nombre, { nombre = it; vm.limpiarError("nombre") }, "Nombre del servicio", error = errores.nombre)
            Spacer(Modifier.height(12.dp))

            Text("Duración", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 8.dp))
            OpcionesChip(ServiciosViewModel.DURACIONES, duracion, { "$it min" }, { duracion = it; vm.limpiarError("duracion") })
            errores.duracion?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(Modifier.height(12.dp))

            VetTextField(
                precio, { precio = it.replace(',', '.'); vm.limpiarError("precio") }, "Precio (S/)",
                error = errores.precio, teclado = KeyboardType.Decimal
            )
            Spacer(Modifier.height(10.dp))
            VetTextField(descripcion, { descripcion = it }, "Descripción", lineas = 3)
            Spacer(Modifier.height(12.dp))

            Text("Ícono", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ICONOS_SERVICIO.forEach { ic ->
                    val elegido = ic == icono
                    Surface(
                        onClick = { icono = ic },
                        shape = RoundedCornerShape(12.dp),
                        color = if (elegido) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(if (elegido) 2.dp else 1.dp, if (elegido) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(Modifier.padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                            Icon(iconoServicio(ic), contentDescription = ic, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Servicio activo", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Los dueños pueden pedirlo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = activo, onCheckedChange = { activo = it })
                }
            }
            Spacer(Modifier.height(20.dp))

            VetButton("Guardar servicio", onClick = {
                vm.guardar(
                    Servicio(
                        id = servicioId,
                        nombre = nombre,
                        descripcion = descripcion,
                        duracionMin = duracion,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        activo = activo,
                        icono = icono
                    )
                )
            })
            Spacer(Modifier.height(10.dp))
            VetButton("Cancelar", onClick = onListo, estilo = EstiloBoton.SECUNDARIO)
            if (servicioId > 0) {
                Spacer(Modifier.height(10.dp))
                VetButton("Eliminar servicio", onClick = { confirmarEliminar = true }, estilo = EstiloBoton.PELIGRO)
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (confirmarEliminar) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar servicio") },
            text = { Text("¿Seguro que quieres eliminar \"$nombre\"?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminar = false
                    editando?.let(vm::eliminar)
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
