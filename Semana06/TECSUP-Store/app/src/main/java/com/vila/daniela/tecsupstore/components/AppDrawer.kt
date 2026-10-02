package com.vila.daniela.tecsupstore.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    destinoActual: String,
    onNavegar: (String) -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))

        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = destinoActual == "inicio",
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            onClick = { onNavegar("inicio") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            selected = destinoActual == "pedidos",
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            onClick = { onNavegar("pedidos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Favoritos") },
            selected = destinoActual == "favoritos",
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            onClick = { onNavegar("favoritos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = destinoActual == "perfil",
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            onClick = { onNavegar("perfil") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Cerrar sesion") },
            selected = false,
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            onClick = { onNavegar("login") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}