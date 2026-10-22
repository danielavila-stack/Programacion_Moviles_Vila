package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.VetApp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

@Composable
fun NavVet(app: VetApp) {
    val nav = rememberNavController()

    // La sesión vive mientras la Activity exista: la comparten todas las pantallas
    val sesionVm: SesionViewModel = viewModel(
        factory = VetViewModelFactory { SesionViewModel(app.repository, app.preferencias) }
    )
    val oscuro by sesionVm.modoOscuro.collectAsStateWithLifecycle()
    val usuario by sesionVm.usuarioActual.collectAsStateWithLifecycle()
    val sesionCargada by sesionVm.sesionCargada.collectAsStateWithLifecycle()

    fun irAPrincipal(rol: Rol) = nav.irLimpiando(if (rol == Rol.CLIENTE) Rutas.GRAFO_DUENO else Rutas.GRAFO_VET)

    fun cerrarSesion() {
        sesionVm.cerrarSesion()
        nav.irLimpiando(Rutas.LOGIN)
    }

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            // Si ya hay perfil guardado, salta el login
            LaunchedEffect(sesionCargada, usuario) {
                val u = usuario
                if (sesionCargada && u != null) irAPrincipal(Rol.valueOf(u.rol))
            }
            VetTheme(Rol.CLIENTE, oscuro) {
                SplashScreen(
                    onComenzar = { nav.navigate(Rutas.LOGIN) },
                    onTengoCuenta = { nav.navigate(Rutas.LOGIN) }
                )
            }
        }

        composable(Rutas.LOGIN) {
            LoginScreen(vm = sesionVm, oscuro = oscuro, onIngreso = ::irAPrincipal)
        }

        // Grafo del dueño
        navigation(startDestination = Rutas.INICIO, route = Rutas.GRAFO_DUENO) {
            composable(Rutas.INICIO) {
                // TODO (Paso 7): InicioScreen
                VetTheme(Rol.CLIENTE, oscuro) {
                    PantallaTemporal("Hola, ${usuario?.nombre.orEmpty()}", onCerrarSesion = ::cerrarSesion)
                }
            }
        }

        // Grafo del veterinario
        navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_VET) {
            composable(Rutas.PANEL) {
                // TODO (Paso 11): PanelScreen
                VetTheme(Rol.ADMIN, oscuro) {
                    PantallaTemporal(
                        "Panel de ${usuario?.nombre.orEmpty()}",
                        onCerrarSesion = ::cerrarSesion,
                        onServicios = { nav.navigate(Rutas.SERVICIOS) }
                    )
                }
            }
            composable(Rutas.SERVICIOS) {
                val vm: ServiciosViewModel = viewModel(
                    factory = VetViewModelFactory { ServiciosViewModel(app.repository) }
                )
                VetTheme(Rol.ADMIN, oscuro) {
                    ServiciosScreen(
                        vm = vm,
                        onEditar = { /* TODO (Paso 13): nav.navigate(Rutas.formServicio(it)) */ }
                    )
                }
            }
        }
    }
}

// Navega y limpia toda la pila (no se puede volver con "atrás")
private fun NavHostController.irLimpiando(ruta: String) {
    navigate(ruta) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

// Marcador mientras se construyen las pantallas principales de cada perfil
@Composable
private fun PantallaTemporal(
    titulo: String,
    onCerrarSesion: () -> Unit,
    onServicios: (() -> Unit)? = null
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(titulo, style = MaterialTheme.typography.headlineSmall)
            if (onServicios != null) VetButton("Servicios", onClick = onServicios)
            VetButton("Cerrar sesión", onClick = onCerrarSesion, estilo = EstiloBoton.PELIGRO)
        }
    }
}
