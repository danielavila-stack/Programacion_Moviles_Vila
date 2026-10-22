package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.iconoServicio
import com.tecsup.huellitasvet.ui.components.pelajePara
import com.tecsup.huellitasvet.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    nombreDueno: String,
    onVerMascota: (Int) -> Unit,
    onVerTodas: () -> Unit,
    onAgregarMascota: () -> Unit,
    onPedirCita: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsStateWithLifecycle()
    val proxima by vm.proximaCita.collectAsStateWithLifecycle()
    val servicios by vm.servicios.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Saludo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Hola, ${nombreDueno.substringBefore(" ")}", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "¿Cómo están tus mascotas hoy?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones")
        }
        Spacer(Modifier.height(18.dp))

        TarjetaProximaCita(proxima)
        Spacer(Modifier.height(22.dp))

        // Mis mascotas
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mis mascotas", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Text(
                "Ver todas",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable(onClick = onVerTodas)
                    .padding(4.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 4.dp)
        ) {
            items(mascotas, key = { it.id }) { m ->
                MascotaMini(m, onClick = { onVerMascota(m.id) })
            }
            item { AgregarMini(onAgregarMascota) }
        }
        Spacer(Modifier.height(22.dp))

        // Servicios (accesos rápidos para pedir cita)
        Text("Servicios", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            servicios.take(3).forEach { s ->
                Surface(
                    onClick = onPedirCita,
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        Modifier.padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(iconoServicio(s.icono), null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            s.nombre.substringBefore(" "),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        VetButton("Pedir una cita", onClick = onPedirCita)
    }
}

@Composable
private fun TarjetaProximaCita(proxima: CitaConDetalle?) {
    val primario = MaterialTheme.colorScheme.primary
    val sobre = MaterialTheme.colorScheme.onPrimary
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = primario, contentColor = sobre),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (proxima == null) {
            Column(Modifier.padding(18.dp)) {
                Text("Próxima cita", style = MaterialTheme.typography.labelMedium, color = sobre.copy(alpha = 0.8f))
                Spacer(Modifier.height(4.dp))
                Text("Aún no tienes citas", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            val mascota = proxima.mascota.mascota
            val especie = Especie.desde(mascota.especie)
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Próxima cita", style = MaterialTheme.typography.labelMedium, color = sobre.copy(alpha = 0.8f))
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${mascota.nombre} · ${proxima.servicio.nombre}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schedule, null, modifier = Modifier.size(16.dp))
                        Text(
                            "  ${fechaCorta(proxima.cita.fecha)} · ${proxima.cita.hora}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        EstadoCita.desde(proxima.cita.estado).etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        color = primario,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
                Box(
                    Modifier
                        .background(Color.White, RoundedCornerShape(14.dp))
                        .padding(6.dp)
                ) {
                    MascotaAvatar(especie, tamano = 48.dp, pelaje = pelajePara(especie, mascota.nombre))
                }
            }
        }
    }
}

@Composable
private fun MascotaMini(m: Mascota, onClick: () -> Unit) {
    val especie = Especie.desde(m.especie)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MascotaAvatar(especie, tamano = 56.dp, pelaje = pelajePara(especie, m.nombre))
            Spacer(Modifier.height(8.dp))
            Text(m.nombre, style = MaterialTheme.typography.titleSmall)
            Text(
                "Ver ficha",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AgregarMini(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .width(100.dp)
            .height(124.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.Add, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(6.dp))
            Text("Agregar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}
