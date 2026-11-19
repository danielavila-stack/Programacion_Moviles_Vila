package com.vila.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vila.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Tarjeta(titulo = "Tarea con progreso") {
                // TODO Paso 1: botón Tócame (contador) y botón Tarea larga (MAL)
                // TODO Paso 3: barra de progreso y porcentaje
                // TODO Paso 4: botones Iniciar y Cancelar
                Text(mensaje)
            }

            Tarjeta(titulo = "Contar números primos") {
                // TODO Paso 5: campo numérico, botones Contar y Cancelar, resultado
                Text("")
            }

            Tarjeta(titulo = "Dos cargas") {
                // TODO Paso 6: botones Una por una y A la vez, resultado
                // TODO Paso 7: interruptor Simular error y botón Reintentar
                Text("")
            }
        }
    }
}

// Tarjeta con el título en azul; se reutiliza para las tres tareas
@Composable
fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            contenido()
        }
    }
}