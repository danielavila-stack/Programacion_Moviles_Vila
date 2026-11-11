package com.tecsup.valorant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.valorant.AppContainer
import com.tecsup.valorant.ui.lista.ListaScreen
import com.tecsup.valorant.ui.lista.ListaViewModel

@Composable
fun AppNav(container: AppContainer) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "lista") {
        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = ListaViewModel.factory(container))
            ListaScreen(
                vm = vm,
                esAdmin = false,
                onAbrirDetalle = {},
                onFavoritos = {},
                onAdmin = {},
                onCerrarSesion = {}
            )
        }
    }
}
