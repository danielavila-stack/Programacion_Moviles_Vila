package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.graficos.GraficoBarras
import com.tecsup.huellitasvet.ui.components.graficos.GraficoDona
import com.tecsup.huellitasvet.ui.components.soles
import com.tecsup.huellitasvet.ui.theme.Atendida
import com.tecsup.huellitasvet.ui.theme.AvisoFondo
import com.tecsup.huellitasvet.ui.theme.AvisoTexto
import com.tecsup.huellitasvet.ui.theme.Pendiente
import com.tecsup.huellitasvet.viewmodel.PanelViewModel

@Composable
fun PanelScreen(vm: PanelViewModel, onVerAgenda: () -> Unit) {
    val citasHoy by vm.citasHoy.collectAsStateWithLifecycle()
    val pendientes by vm.pendientes.collectAsStateWithLifecycle()
    val atendidas by vm.atendidas.collectAsStateWithLifecycle()
    val ingresos by vm.ingresosMes.collectAsStateWithLifecycle()
    val porEstado by vm.porEstado.collectAsStateWithLifecycle()
    val porServicio by vm.porServicio.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Panel de la clínica", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Hoy · ${fechaCorta(vm.fechaHoy)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones")
        }
        Spacer(Modifier.height(16.dp))

        // 3 contadores de color
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador(citasHoy, "Citas hoy", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            Contador(pendientes, "Pendientes", Pendiente, Modifier.weight(1f))
            Contador(atendidas, "Atendidas", Atendida, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))

        // Ingresos del mes
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Ingresos del mes", style = MaterialTheme.typography.labelMedium)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(soles(ingresos), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                    Text("$atendidas atenciones", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        Tarjeta("Citas por estado") { GraficoDona(porEstado) }
        Spacer(Modifier.height(12.dp))
        Tarjeta("Citas por servicio") { GraficoBarras(porServicio) }

        if (pendientes > 0) {
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(AvisoFondo, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Warning, null, tint = Pendiente, modifier = Modifier.size(22.dp))
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(
                        if (pendientes == 1) "1 cita por confirmar" else "$pendientes citas por confirmar",
                        style = MaterialTheme.typography.titleSmall,
                        color = AvisoTexto
                    )
                    Text("Revísalas en la agenda", style = MaterialTheme.typography.bodySmall, color = AvisoTexto)
                }
                Button(
                    onClick = onVerAgenda,
                    colors = ButtonDefaults.buttonColors(containerColor = Pendiente, contentColor = Color.White)
                ) { Text("Ver") }
            }
        }
    }
}

@Composable
private fun Contador(valor: Int, texto: String, color: Color, modifier: Modifier) {
    Column(
        modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor.toString(), style = MaterialTheme.typography.headlineMedium, color = color)
        Text(texto, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(12.dp))
            contenido()
        }
    }
}
