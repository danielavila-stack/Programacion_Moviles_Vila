package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.FilaSwitch
import com.tecsup.huellitasvet.ui.components.NotaDataStore
import com.tecsup.huellitasvet.ui.components.TarjetaCambiarPerfil
import com.tecsup.huellitasvet.ui.components.TarjetaPreferencias
import com.tecsup.huellitasvet.ui.components.TarjetaUsuario
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.theme.Atendida
import com.tecsup.huellitasvet.ui.theme.Pendiente
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

// Perfil del dueño: datos, contadores, preferencias y sesión
@Composable
fun PerfilScreen(
    sesion: SesionViewModel,
    inicio: InicioViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuario by sesion.usuarioActual.collectAsStateWithLifecycle()
    val oscuro by sesion.modoOscuro.collectAsStateWithLifecycle()
    val recordatorios by sesion.notificaciones.collectAsStateWithLifecycle()
    val mascotas by inicio.mascotas.collectAsStateWithLifecycle()
    val resumen by inicio.resumen.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar("Perfil") },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TarjetaUsuario(usuario?.nombre.orEmpty(), usuario?.correo.orEmpty(), "Dueño")
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador(mascotas.size, "Mascotas", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                Contador(resumen.proximas, "Próximas", Pendiente, Modifier.weight(1f))
                Contador(resumen.atenciones, "Atenciones", Atendida, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))

            TarjetaCambiarPerfil("Veterinario", onCambiarPerfil)
            Spacer(Modifier.height(16.dp))

            TarjetaPreferencias {
                FilaSwitch("Modo oscuro", "Cambia el tema de la app", oscuro, sesion::guardarModoOscuro)
                FilaSwitch("Recordatorios", "Avisos de tus citas y vacunas", recordatorios, sesion::guardarNotificaciones, divisor = false)
            }
            Spacer(Modifier.height(12.dp))
            NotaDataStore()
            Spacer(Modifier.height(20.dp))

            VetButton("Cerrar sesión", onClick = onCerrarSesion, estilo = EstiloBoton.PELIGRO)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Contador(valor: Int, texto: String, color: Color, modifier: Modifier) {
    Column(
        modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor.toString(), style = MaterialTheme.typography.headlineSmall, color = color)
        Text(texto, style = MaterialTheme.typography.labelMedium, color = color)
    }
}
