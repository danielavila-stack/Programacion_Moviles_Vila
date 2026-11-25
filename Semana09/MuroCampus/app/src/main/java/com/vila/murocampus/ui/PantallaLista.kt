package com.vila.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vila.murocampus.viewmodel.PostsViewModel
import com.vila.murocampus.viewmodel.UiState

// La pantalla solo dibuja el estado: nunca llama a Retrofit
@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit, modifier: Modifier = Modifier) {
    // Mejora 2: se dibuja la lista YA filtrada por la búsqueda
    val estado by vm.listaVisible.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Mejora 2: barra de búsqueda estilo Material 3
        BarraBusqueda(
            texto = busqueda,
            onCambiar = { vm.buscar(it) }
        )

        // Paso 8: fila de chips
        FiltroUsuarios(
            seleccionado = filtro,
            onElegir = { vm.filtrar(it) }
        )

        // El resto del espacio es para la lista, la carga o el error
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()   // ocupa todo el ancho para poder centrar
        ) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando()

                is UiState.Error -> ErrorConReintento(
                    mensaje = e.mensaje,
                    onReintentar = { vm.cargar() }
                )

                is UiState.Exito -> {
                    if (e.datos.isEmpty()) {
                        SinResultados(
                            busqueda = busqueda,
                            onBorrar = { vm.buscar("") },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Contador de resultados arriba de las tarjetas
                            item {
                                Text(
                                    if (e.datos.size == 1) "1 publicación" else "${e.datos.size} publicaciones",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
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

// Mejora 2: barra redondeada con fondo tonal, sin línea de abajo (estilo SearchBar de M3)
@Composable
private fun BarraBusqueda(texto: String, onCambiar: (String) -> Unit) {
    val teclado = LocalSoftwareKeyboardController.current

    TextField(
        value = texto,
        onValueChange = onCambiar,
        placeholder = { Text("Buscar por título") },
        trailingIcon = {
            // Botón para borrar la búsqueda (solo si hay texto)
            if (texto.isNotEmpty()) {
                IconButton(onClick = { onCambiar("") }) {
                    Text("✕", style = MaterialTheme.typography.titleMedium)
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),  // forma de píldora
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,     // sin línea de abajo
            unfocusedIndicatorColor = Color.Transparent
        ),
        // La tecla del teclado dice "Buscar" y al tocarla se oculta el teclado
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { teclado?.hide() }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp)
    )
}

// Fila desplazable de chips: Todos, Usuario 1 … Usuario 10. Solo avisa qué se tocó.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltroUsuarios(seleccionado: Int?, onElegir: (Int?) -> Unit) {
    // Colores del chip seleccionado: contenedor tonal del color principal
    val colores = FilterChipDefaults.filterChipColors(
        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = seleccionado == null,
                onClick = { onElegir(null) },
                label = { Text("Todos") },
                leadingIcon = if (seleccionado == null) { { Text("✓") } } else null,
                colors = colores
            )
        }
        items((1..10).toList()) { id ->
            FilterChip(
                selected = seleccionado == id,
                onClick = { onElegir(id) },
                label = { Text("Usuario $id") },
                leadingIcon = if (seleccionado == id) { { Text("✓") } } else null,
                colors = colores
            )
        }
    }
}

// Estado vacío: distinto si es por la búsqueda o por el filtro
@Composable
private fun SinResultados(busqueda: String, onBorrar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            if (busqueda.isNotBlank()) "Sin resultados" else "Sin publicaciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (busqueda.isNotBlank()) {
            Text(
                "Ningún título contiene \"${busqueda.trim()}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            FilledTonalButton(onClick = onBorrar) {
                Text("Borrar búsqueda")
            }
        }
    }
}