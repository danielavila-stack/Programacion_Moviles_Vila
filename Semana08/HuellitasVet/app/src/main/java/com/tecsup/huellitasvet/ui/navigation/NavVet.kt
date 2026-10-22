package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.VetApp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

@Composable
fun NavVet(app: VetApp) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            VetTheme(Rol.CLIENTE) {
                SplashScreen(
                    // TODO (Paso 6): ir al Login (o directo a la pantalla principal si hay perfil guardado)
                    onComenzar = { nav.navigate(Rutas.SERVICIOS) },
                    onTengoCuenta = { nav.navigate(Rutas.SERVICIOS) }
                )
            }
        }

        // TODO (Pasos 6 a 13): Login, grafo del dueño y grafo del veterinario con NavigationBar

        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(
                factory = VetViewModelFactory { ServiciosViewModel(app.repository) }
            )
            VetTheme(Rol.ADMIN) {
                ServiciosScreen(
                    vm = vm,
                    onEditar = { /* TODO (Paso 13): nav.navigate(Rutas.formServicio(it)) */ }
                )
            }
        }
    }
}
