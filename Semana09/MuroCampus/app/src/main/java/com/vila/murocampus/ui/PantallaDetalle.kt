package com.vila.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vila.murocampus.viewmodel.PostsViewModel
import com.vila.murocampus.viewmodel.UiState

// Detalle de una publicación con sus comentarios
@Composable
fun PantallaDetalle(vm: PostsViewModel, onVolver: () -> Unit, modifier: Modifier = Modifier) {
    // El botón Atrás del sistema vuelve a la lista
    BackHandler { onVolver() }

    val estado by vm.detalle.collectAsStateWithLifecycle()

    when (val e = estado) {
        is UiState.Cargando -> Cargando(texto = "Cargando publicación…", modifier = modifier)

        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.reintentarDetalle() },
            modifier = modifier
        )

        is UiState.Exito -> {
            val d = e.datos
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // La publicación completa (sin cortar el texto)
                item {
                    TarjetaPost(
                        userId = d.post.userId,
                        titulo = d.post.title,
                        cuerpo = d.post.body,
                        maxLineas = Int.MAX_VALUE
                    )
                }

                // Título de la sección
                item {
                    Text(
                        "Comentarios (${d.comentarios.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Un recuadro por comentario: correo en azul y el texto
                items(d.comentarios, key = { it.id }) { c ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                c.email,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(c.body, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}