package com.vila.daniela.tecsupstore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    destinoActual: String,
    cantidadFavoritos: Int = 0,
    onNavegar: (String) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DV",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Daniela Vila",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "daniela.vila@tecsup.edu.pe",
                style = MaterialTheme.typography.bodySmall
            )
        }

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
            badge = {
                // Mejora visual: Badge en morado solo si hay al menos 1 favorito seleccionado
                if (cantidadFavoritos > 0) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary, // Color morado (#6750A4)
                        contentColor = MaterialTheme.colorScheme.onPrimary   // Texto blanco para contraste
                    ) {
                        Text(text = cantidadFavoritos.toString())
                    }
                }
            },
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

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        NavigationDrawerItem(
            label = { Text("Cerrar sesion") },
            selected = false,
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            onClick = { onNavegar("login") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}