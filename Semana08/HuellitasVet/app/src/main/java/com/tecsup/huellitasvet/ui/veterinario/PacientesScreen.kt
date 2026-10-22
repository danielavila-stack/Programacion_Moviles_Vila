package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.components.diaMes
import com.tecsup.huellitasvet.ui.components.nombreCorto
import com.tecsup.huellitasvet.ui.components.pelajePara
import com.tecsup.huellitasvet.ui.components.textoEdad
import com.tecsup.huellitasvet.viewmodel.OrdenPacientes
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel

// Filtros: Todos / Perros / Gatos / Otros
private val FILTROS = listOf<Pair<String, Especie?>>(
    "Todos" to null, "Perros" to Especie.PERRO, "Gatos" to Especie.GATO, "Otros" to Especie.OTRO
)

@Composable
fun PacientesScreen(vm: PacientesViewModel, onVerPaciente: (Int) -> Unit) {
    val filtros by vm.filtros.collectAsStateWithLifecycle()
    val pacientes by vm.pacientes.collectAsStateWithLifecycle()
    val ultimaVisita by vm.ultimaVisita.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar("Pacientes") },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            VetTextField(filtros.texto, vm::buscar, "Buscar mascota, dueño o raza...", icono = Icons.Filled.Search)
            Spacer(Modifier.height(10.dp))
            // Especie
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FILTROS.forEach { (etiqueta, e) ->
                    Chip(etiqueta, activo = e == filtros.especie, onClick = { vm.filtrar(e) })
                }
            }
            Spacer(Modifier.height(8.dp))
            // Orden y visitas recientes
            Row(
                Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Chip("A–Z", activo = filtros.orden == OrdenPacientes.NOMBRE, onClick = { vm.ordenar(OrdenPacientes.NOMBRE) })
                Chip("Última visita", activo = filtros.orden == OrdenPacientes.ULTIMA_VISITA, onClick = { vm.ordenar(OrdenPacientes.ULTIMA_VISITA) })
                Chip("Sin visitas recientes", activo = filtros.sinVisitasRecientes, onClick = vm::alternarSinVisitas)
            }
            // Contador y limpiar
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (pacientes.size == 1) "1 paciente" else "${pacientes.size} pacientes",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                if (filtros.activos) {
                    TextButton(onClick = vm::limpiarFiltros) { Text("Limpiar filtros") }
                }
            }
            if (pacientes.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text("No se encontraron pacientes", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(pacientes, key = { it.mascota.id }) { p ->
                    val m = p.mascota
                    val esp = Especie.desde(m.especie)
                    Card(
                        onClick = { onVerPaciente(m.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            MascotaAvatar(esp, tamano = 52.dp, pelaje = pelajePara(esp, m.nombre))
                            Column(
                                Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            ) {
                                Text(m.nombre, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    "${m.raza} · ${textoEdad(vm.edad(p))}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Dueño: ${nombreCorto(p.dueno.nombre)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    ultimaVisita[m.id]?.let { "Última visita: ${diaMes(it)}" } ?: "Sin visitas",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Chip(texto: String, activo: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        contentColor = if (activo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        border = if (activo) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            texto,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
        )
    }
}
