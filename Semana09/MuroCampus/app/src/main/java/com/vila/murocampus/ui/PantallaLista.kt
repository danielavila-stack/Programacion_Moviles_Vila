package com.vila.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vila.murocampus.viewmodel.PostsViewModel
import com.vila.murocampus.viewmodel.UiState

// La pantalla solo dibuja el estado: nunca llama a Retrofit
@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Paso 8: fila de chips arriba de la lista
        FiltroUsuarios(
            seleccionado = filtro,
            onElegir = { vm.filtrar(it) }
        )

        // El resto del espacio es para la lista, la carga o el error
        Box(modifier = Modifier.weight(1f)) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando()

                is UiState.Error -> ErrorConReintento(
                    mensaje = e.mensaje,
                    onReintentar = { vm.cargar() }
                )

                is UiState.Exito -> {
                    if (e.datos.isEmpty()) {
                        // Si el filtro no trae nada
                        Text(
                            "Sin publicaciones",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(e.datos, key = { it.id }) { post ->
                                TarjetaPost(
                                    userId = post.userId,
                                    titulo = post.title,
                                    cuerpo = post.body,
                                    onClick = { onAbrir(post.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Fila desplazable de chips: Todos, Usuario 1 … Usuario 10. Solo avisa qué se tocó.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltroUsuarios(seleccionado: Int?, onElegir: (Int?) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = seleccionado == null,
                onClick = { onElegir(null) },
                label = { Text("Todos") }
            )
        }
        items((1..10).toList()) { id ->
            FilterChip(
                selected = seleccionado == id,
                onClick = { onElegir(id) },
                label = { Text("Usuario $id") }
            )
        }
    }
}