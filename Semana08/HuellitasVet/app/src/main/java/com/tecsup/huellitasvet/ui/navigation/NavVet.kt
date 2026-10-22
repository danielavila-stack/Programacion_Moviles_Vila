package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.VetApp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.EstiloBoton
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.dueno.FichaMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
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
            composable(Rutas.INICIO) { entry ->
                val vm = inicioVm(nav, entry, app, usuario?.id ?: 0)
                VetTheme(Rol.CLIENTE, oscuro) {
                    ScaffoldDueno(nav, Rutas.INICIO) {
                        InicioScreen(
                            vm = vm,
                            nombreDueno = usuario?.nombre.orEmpty(),
                            onVerMascota = { nav.navigate(Rutas.fichaMascota(it)) },
                            onVerTodas = { nav.irATab(Rutas.MASCOTAS) },
                            onAgregarMascota = { nav.navigate(Rutas.formMascota()) },
                            onPedirCita = { /* TODO (Paso 9): nav.navigate(Rutas.pedirCita()) */ }
                        )
                    }
                }
            }
            composable(Rutas.MASCOTAS) { entry ->
                val vm = inicioVm(nav, entry, app, usuario?.id ?: 0)
                VetTheme(Rol.CLIENTE, oscuro) {
                    ScaffoldDueno(nav, Rutas.MASCOTAS) {
                        MascotasScreen(
                            vm = vm,
                            onVerMascota = { nav.navigate(Rutas.fichaMascota(it)) },
                            onAgregar = { nav.navigate(Rutas.formMascota()) }
                        )
                    }
                }
            }
            composable(
                Rutas.FICHA_MASCOTA,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val vm: MascotaViewModel = viewModel(
                    factory = VetViewModelFactory { MascotaViewModel(app.repository, usuario?.id ?: 0, id) }
                )
                VetTheme(Rol.CLIENTE, oscuro) {
                    ScaffoldDueno(nav, Rutas.MASCOTAS) {
                        FichaMascotaScreen(
                            vm = vm,
                            mascotaId = id,
                            esDueno = true,
                            onBack = { nav.popBackStack() },
                            onPedirCita = { /* TODO (Paso 9): nav.navigate(Rutas.pedirCita(it)) */ },
                            onEditar = { nav.navigate(Rutas.formMascota(it)) }
                        )
                    }
                }
            }
            composable(
                Rutas.FORM_MASCOTA,
                arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val vm: MascotaViewModel = viewModel(
                    factory = VetViewModelFactory { MascotaViewModel(app.repository, usuario?.id ?: 0, id) }
                )
                VetTheme(Rol.CLIENTE, oscuro) {
                    FormularioMascotaScreen(vm = vm, esNueva = id == 0, onListo = { nav.popBackStack() })
                }
            }
            composable(Rutas.MIS_CITAS) {
                // TODO (Paso 10): MisCitasScreen
                VetTheme(Rol.CLIENTE, oscuro) {
                    ScaffoldDueno(nav, Rutas.MIS_CITAS) {
                        PantallaTemporal("Mis citas", onCerrarSesion = ::cerrarSesion)
                    }
                }
            }
            composable(Rutas.PERFIL) {
                // TODO (Paso 13): PerfilScreen
                VetTheme(Rol.CLIENTE, oscuro) {
                    ScaffoldDueno(nav, Rutas.PERFIL) {
                        PantallaTemporal(usuario?.nombre.orEmpty(), onCerrarSesion = ::cerrarSesion)
                    }
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

// Cambia de pestaña de la barra inferior sin apilar pantallas repetidas
private fun NavHostController.irATab(ruta: String) {
    navigate(ruta) {
        popUpTo(Rutas.INICIO) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

// InicioViewModel compartido por Inicio y Mascotas (vive mientras exista el grafo del dueño)
@Composable
private fun inicioVm(nav: NavHostController, entry: NavBackStackEntry, app: VetApp, duenoId: Int): InicioViewModel {
    val grafo = remember(entry) { nav.getBackStackEntry(Rutas.GRAFO_DUENO) }
    return viewModel(
        viewModelStoreOwner = grafo,
        factory = VetViewModelFactory { InicioViewModel(app.repository, duenoId) }
    )
}

private data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

private val BARRA_DUENO = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
    ItemBarra(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

// Scaffold con NavigationBar de 4 ítems para el dueño
@Composable
private fun ScaffoldDueno(nav: NavHostController, actual: String, contenido: @Composable () -> Unit) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                BARRA_DUENO.forEach { item ->
                    NavigationBarItem(
                        selected = item.ruta == actual,
                        onClick = { if (item.ruta != actual) nav.irATab(item.ruta) },
                        icon = { Icon(item.icono, contentDescription = null) },
                        label = { Text(item.texto) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) { contenido() }
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
