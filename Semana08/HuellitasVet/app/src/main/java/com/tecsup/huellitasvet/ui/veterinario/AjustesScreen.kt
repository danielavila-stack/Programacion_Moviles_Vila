package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

// Ajustes del veterinario (preferencias guardadas con DataStore)
@Composable
fun AjustesScreen(
    sesion: SesionViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuario by sesion.usuarioActual.collectAsStateWithLifecycle()
    val oscuro by sesion.modoOscuro.collectAsStateWithLifecycle()
    val notificaciones by sesion.notificaciones.collectAsStateWithLifecycle()
    val aceptarCitas by sesion.aceptarCitas.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar("Ajustes") },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TarjetaUsuario(usuario?.nombre.orEmpty(), usuario?.correo.orEmpty(), "Perfil activo")
            Spacer(Modifier.height(12.dp))
            TarjetaCambiarPerfil("Dueño", onCambiarPerfil)
            Spacer(Modifier.height(16.dp))

            TarjetaPreferencias {
                FilaSwitch("Modo oscuro", "Cambia el tema de la app", oscuro, sesion::guardarModoOscuro)
                FilaSwitch("Notificaciones", "Avisar citas nuevas", notificaciones, sesion::guardarNotificaciones)
                FilaSwitch("Aceptar citas", "Abre o cierra la agenda", aceptarCitas, sesion::guardarAceptarCitas, divisor = false)
            }
            Spacer(Modifier.height(12.dp))
            NotaDataStore()
            Spacer(Modifier.height(20.dp))

            VetButton("Cerrar sesión", onClick = onCerrarSesion, estilo = EstiloBoton.PELIGRO)
            Spacer(Modifier.height(16.dp))
        }
    }
}
