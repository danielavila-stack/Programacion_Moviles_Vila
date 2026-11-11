package com.tecsup.valorant.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.valorant.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(vm: AdminViewModel, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin") },
                navigationIcon = {
                    IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (val e = estado) {
                is UiState.Cargando -> CircularProgressIndicator()
                is UiState.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)
                is UiState.Exito -> {
                    TarjetaTotal("Agentes guardados", e.datos.guardados)
                    TarjetaTotal("Favoritos", e.datos.favoritos)
                }
            }
            Button(onClick = vm::sincronizar, modifier = Modifier.fillMaxWidth()) {
                Text("Sincronizar")
            }
            OutlinedButton(onClick = vm::limpiarCache, modifier = Modifier.fillMaxWidth()) {
                Text("Limpiar caché")
            }
            mensaje?.let { Text(it) }
        }
    }
}

@Composable
private fun TarjetaTotal(titulo: String, valor: Int) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.labelLarge)
            Text("$valor", style = MaterialTheme.typography.displaySmall)
        }
    }
}
