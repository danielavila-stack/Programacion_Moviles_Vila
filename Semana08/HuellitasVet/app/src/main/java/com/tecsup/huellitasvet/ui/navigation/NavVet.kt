package com.tecsup.huellitasvet.ui.navigation

import android.graphics.Color as AndroidColor
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.tecsup.huellitasvet.ui.dueno.FichaMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.MisCitasScreen
import com.tecsup.huellitasvet.ui.dueno.PedirCitaScreen
import com.tecsup.huellitasvet.ui.dueno.PerfilScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.AgendaScreen
import com.tecsup.huellitasvet.ui.veterinario.AjustesScreen
import com.tecsup.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.tecsup.huellitasvet.ui.veterinario.FormularioServicioScreen
import com.tecsup.huellitasvet.ui.veterinario.PacientesScreen
import com.tecsup.huellitasvet.ui.veterinario.PanelScreen
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
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

    // Guarda el otro perfil y navega a su grafo limpiando la pila
    fun cambiarPerfil() = sesionVm.cambiarPerfil(onCambiado = ::irAPrincipal)

    // Íconos de la barra de estado legibles en modo claro y oscuro
    val activity = LocalContext.current as? ComponentActivity
    LaunchedEffect(oscuro) {
        val estilo = if (oscuro) SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        else SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT)
        activity?.enableEdgeToEdge(statusBarStyle = estilo, navigationBarStyle = estilo)
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
                            onPedirCita = { nav.navigate(Rutas.pedirCita()) }
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
                            onPedirCita = { nav.navigate(Rutas.pedirCita(it)) },
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
            composable(
                Rutas.PEDIR_CITA,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType; defaultValue = 0 })
            ) { entry ->
                val mascotaId = entry.arguments?.getInt("mascotaId") ?: 0
                val vm: PedirCitaViewModel = viewModel(
                    factory = VetViewModelFactory {
                        PedirCitaViewModel(app.repository, usuario?.id ?: 0, mascotaId, app.preferencias.aceptarCitas)
                    }
                )
                VetTheme(Rol.CLIENTE, oscuro) {
                    PedirCitaScreen(
                        vm = vm,
                        onBack = { nav.popBackStack() },
                        onCitaCreada = { nav.irATab(Rutas.MIS_CITAS) }
                    )
                }
            }
            composable(Rutas.MIS_CITAS) {
                val vm: MisCitasViewModel = viewModel(
                    factory = VetViewModelFactory { MisCitasViewModel(app.repository, usuario?.id ?: 0) }
                )
                VetTheme(Rol.CLIENTE, oscuro) {
                    ScaffoldDueno(nav, Rutas.MIS_CITAS) {
                        MisCitasScreen(vm = vm, onPedirCita = { nav.navigate(Rutas.pedirCita()) })
                    }
                }
            }
            composable(Rutas.PERFIL) { entry ->
                val inicio = inicioVm(nav, entry, app, usuario?.id ?: 0)
                VetTheme(Rol.CLIENTE, oscuro) {
                    ScaffoldDueno(nav, Rutas.PERFIL) {
                        PerfilScreen(
                            sesion = sesionVm,
                            inicio = inicio,
                            onCambiarPerfil = ::cambiarPerfil,
                            onCerrarSesion = ::cerrarSesion
                        )
                    }
                }
            }
        }

        // Grafo del veterinario
        navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_VET) {
            composable(Rutas.PANEL) {
                val vm: PanelViewModel = viewModel(factory = VetViewModelFactory { PanelViewModel(app.repository) })
                VetTheme(Rol.ADMIN, oscuro) {
                    ScaffoldVet(nav, Rutas.PANEL) {
                        PanelScreen(vm = vm, onVerAgenda = { nav.irATab(Rutas.AGENDA, Rutas.PANEL) })
                    }
                }
            }
            composable(Rutas.AGENDA) {
                val vm: AgendaViewModel = viewModel(factory = VetViewModelFactory { AgendaViewModel(app.repository) })
                VetTheme(Rol.ADMIN, oscuro) {
                    ScaffoldVet(nav, Rutas.AGENDA) {
                        AgendaScreen(vm = vm, onAtender = { nav.navigate(Rutas.atenderCita(it)) })
                    }
                }
            }
            composable(
                Rutas.ATENDER_CITA,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val vm: AtenderCitaViewModel = viewModel(
                    factory = VetViewModelFactory { AtenderCitaViewModel(app.repository, id) }
                )
                VetTheme(Rol.ADMIN, oscuro) {
                    AtenderCitaScreen(vm = vm, onListo = { nav.popBackStack() })
                }
            }
            composable(Rutas.PACIENTES) {
                val vm: PacientesViewModel = viewModel(factory = VetViewModelFactory { PacientesViewModel(app.repository) })
                VetTheme(Rol.ADMIN, oscuro) {
                    ScaffoldVet(nav, Rutas.PACIENTES) {
                        PacientesScreen(vm = vm, onVerPaciente = { nav.navigate(Rutas.fichaPaciente(it)) })
                    }
                }
            }
            composable(
                Rutas.FICHA_PACIENTE,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val vm: MascotaViewModel = viewModel(
                    factory = VetViewModelFactory { MascotaViewModel(app.repository, 0, id) }
                )
                VetTheme(Rol.ADMIN, oscuro) {
                    ScaffoldVet(nav, Rutas.PACIENTES) {
                        // El veterinario solo consulta: sin editar, eliminar ni pedir cita
                        FichaMascotaScreen(
                            vm = vm,
                            mascotaId = id,
                            esDueno = false,
                            onBack = { nav.popBackStack() },
                            onPedirCita = {},
                            onEditar = {}
                        )
                    }
                }
            }
            composable(Rutas.SERVICIOS) {
                val vm: ServiciosViewModel = viewModel(
                    factory = VetViewModelFactory { ServiciosViewModel(app.repository) }
                )
                VetTheme(Rol.ADMIN, oscuro) {
                    ScaffoldVet(nav, Rutas.SERVICIOS) {
                        ServiciosScreen(vm = vm, onEditar = { nav.navigate(Rutas.formServicio(it)) })
                    }
                }
            }
            composable(
                Rutas.FORM_SERVICIO,
                arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val vm: ServiciosViewModel = viewModel(
                    factory = VetViewModelFactory { ServiciosViewModel(app.repository) }
                )
                VetTheme(Rol.ADMIN, oscuro) {
                    FormularioServicioScreen(vm = vm, servicioId = id, onListo = { nav.popBackStack() })
                }
            }
            composable(Rutas.AJUSTES) {
                VetTheme(Rol.ADMIN, oscuro) {
                    ScaffoldVet(nav, Rutas.AJUSTES) {
                        AjustesScreen(
                            sesion = sesionVm,
                            onCambiarPerfil = ::cambiarPerfil,
                            onCerrarSesion = ::cerrarSesion
                        )
                    }
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

// Cambia de pestaña de la barra inferior: deja solo la pantalla principal debajo (Inicio o Panel)
private fun NavHostController.irATab(ruta: String, principal: String = Rutas.INICIO) {
    navigate(ruta) {
        popUpTo(principal)
        launchSingleTop = true
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

private val BARRA_VET = listOf(
    ItemBarra(Rutas.PANEL, "Panel", Icons.Filled.BarChart),
    ItemBarra(Rutas.AGENDA, "Agenda", Icons.Filled.CalendarMonth),
    ItemBarra(Rutas.PACIENTES, "Pacientes", Icons.Filled.Pets),
    ItemBarra(Rutas.SERVICIOS, "Servicios", Icons.Filled.LocalOffer),
    ItemBarra(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings)
)

// NavigationBar de 4 ítems para el dueño
@Composable
private fun ScaffoldDueno(nav: NavHostController, actual: String, contenido: @Composable () -> Unit) =
    ScaffoldConBarra(nav, actual, BARRA_DUENO, Rutas.INICIO, contenido)

// NavigationBar de 5 ítems para el veterinario
@Composable
private fun ScaffoldVet(nav: NavHostController, actual: String, contenido: @Composable () -> Unit) =
    ScaffoldConBarra(nav, actual, BARRA_VET, Rutas.PANEL, contenido)

@Composable
private fun ScaffoldConBarra(
    nav: NavHostController,
    actual: String,
    items: List<ItemBarra>,
    principal: String,
    contenido: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = item.ruta == actual,
                        onClick = { if (item.ruta != actual) nav.irATab(item.ruta, principal) },
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
