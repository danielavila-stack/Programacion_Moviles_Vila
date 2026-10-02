package com.vila.daniela.tecsupstore.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.vila.daniela.tecsupstore.components.AppDrawer
import com.vila.daniela.tecsupstore.model.Producto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoActual by remember { mutableStateOf("inicio") }

    val productos = listOf(
        Producto(1, "Audifonos Bluetooth", "89.00"),
        Producto(2, "Smartwatch Deportivo", "199.00"),
        Producto(3, "Funda de Celular", "25.00"),
        Producto(4, "Cargador Carga Rápida", "45.00")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                destinoActual = destinoActual,
                onNavegar = { destino ->
                    destinoActual = destino
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("TECSUP Store", color = MaterialTheme.colorScheme.onPrimary)
                            Text(
                                "Mas vendidos",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {
                HomeScreen(productos = productos)
            }
        }
    }
}