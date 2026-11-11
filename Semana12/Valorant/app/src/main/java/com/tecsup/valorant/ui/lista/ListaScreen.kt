package com.tecsup.valorant.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.valorant.domain.model.Item
import com.tecsup.valorant.ui.UiState
import com.tecsup.valorant.ui.components.Cargando
import com.tecsup.valorant.ui.components.ErrorView
import com.tecsup.valorant.ui.components.Vacio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    vm: ListaViewModel,
    esAdmin: Boolean,
    onAbrirDetalle: (String) -> Unit,
    onFavoritos: () -> Unit,
    onAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agentes de Valorant") },
                actions = {
                    IconButton(onClick = onFavoritos) { Icon(Icons.Filled.Favorite, "Favoritos") }
                    if (esAdmin) {
                        IconButton(onClick = onAdmin) { Icon(Icons.Filled.Settings, "Panel Admin") }
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            // (Paso 11: aquí va el aviso de sin conexión)
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(e.mensaje, onReintentar = vm::refrescar)
                is UiState.Exito ->
                    if (e.datos.isEmpty()) Vacio("No hay agentes guardados.")
                    else ListaAgentes(e.datos, onClick = onAbrirDetalle, onFavorito = vm::alternarFavorito)
            }
        }
    }
}

@Composable
fun ListaAgentes(lista: List<Item>, onClick: (String) -> Unit, onFavorito: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lista, key = { it.id }) { item ->
            AgenteFila(item, onClick = { onClick(item.id) }, onFavorito = { onFavorito(item.id) })
        }
    }
}

@Composable
fun AgenteFila(item: Item, onClick: () -> Unit, onFavorito: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.iconoUrl,
                contentDescription = item.nombre,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Text(item.rol, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onFavorito) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito"
                )
            }
        }
    }
}
