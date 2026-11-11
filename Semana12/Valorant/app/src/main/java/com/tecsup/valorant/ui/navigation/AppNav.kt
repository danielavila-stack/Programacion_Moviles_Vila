package com.tecsup.valorant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.valorant.AppContainer
import com.tecsup.valorant.ui.lista.ListaScreen
import com.tecsup.valorant.ui.lista.ListaViewModel
import com.tecsup.valorant.ui.detalle.DetalleScreen
import com.tecsup.valorant.ui.detalle.DetalleViewModel
import com.tecsup.valorant.ui.favoritos.FavoritosScreen
import com.tecsup.valorant.ui.favoritos.FavoritosViewModel

@Composable
fun AppNav(container: AppContainer) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "lista") {
        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = ListaViewModel.factory(container))
            ListaScreen(
                vm = vm,
                esAdmin = false,
                onAbrirDetalle = { id -> nav.navigate("detalle/$id") },
                onFavoritos = { nav.navigate("favoritos") },
                onAdmin = {},
                onCerrarSesion = {}
            )
        }
        composable("detalle/{id}") { entrada ->
            val id = entrada.arguments?.getString("id") ?: return@composable
            val vm: DetalleViewModel = viewModel(factory = DetalleViewModel.factory(container, id))
            DetalleScreen(vm, onVolver = { nav.popBackStack() })
        }
        composable("favoritos") {
            val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModel.factory(container))
            FavoritosScreen(
                vm = vm,
                onAbrirDetalle = { id -> nav.navigate("detalle/$id") },
                onVolver = { nav.popBackStack() }
            )
        }
    }
}
