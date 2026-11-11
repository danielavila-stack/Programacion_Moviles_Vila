package com.tecsup.valorant.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(vm: DetalleViewModel, onVolver: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val s = estado
    val item: Item? = if (s is UiState.Exito) s.datos else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (item != null) {
                        IconButton(onClick = vm::alternarFavorito) {
                            Icon(
                                if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                "Favorito"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (s) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(s.mensaje, onReintentar = vm::cargar)
                is UiState.Exito -> DetalleContenido(s.datos)
            }
        }
    }
}

@Composable
private fun DetalleContenido(item: Item) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AsyncImage(
                model = item.retratoUrl ?: item.iconoUrl,
                contentDescription = item.nombre,
                modifier = Modifier.fillMaxWidth().height(320.dp)
            )
        }
        item {
            Column {
                Text(item.nombre, style = MaterialTheme.typography.headlineMedium)
                Text(item.rol, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
        item {
            Text(item.descripcion.ifBlank { "Descripción no disponible sin conexión." })
        }
        if (item.habilidades.isNotEmpty()) {
            item { Text("Habilidades", style = MaterialTheme.typography.titleLarge) }
        }
        items(item.habilidades) { h ->
            Row(verticalAlignment = Alignment.Top) {
                AsyncImage(
                    model = h.iconoUrl,
                    contentDescription = h.nombre,
                    modifier = Modifier
                        .size(44.dp)
                        .background(MaterialTheme.colorScheme.inverseSurface, RoundedCornerShape(8.dp))
                        .padding(6.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(h.nombre, style = MaterialTheme.typography.titleSmall)
                    Text(h.descripcion, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}