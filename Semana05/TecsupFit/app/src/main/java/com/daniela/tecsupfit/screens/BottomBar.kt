package com.daniela.tecsupfit.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.daniela.tecsupfit.navigation.AppRuta

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inicio : BottomNavItem(AppRuta.Inicio.route, "Inicio", Icons.Default.Home)
    object Reservas : BottomNavItem(AppRuta.Reservas.route, "Reservas", Icons.Default.DateRange)
    object Rutinas : BottomNavItem(AppRuta.Rutinas.route, "Rutinas", Icons.Default.List)
    object Perfil : BottomNavItem(AppRuta.Perfil.route, "Perfil", Icons.Default.Person)
}

@Composable
fun BottomBar(
    rutaActual: String?,
    onNavegar: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Reservas,
        BottomNavItem.Rutinas,
        BottomNavItem.Perfil
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.route,
                onClick = { onNavegar(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}