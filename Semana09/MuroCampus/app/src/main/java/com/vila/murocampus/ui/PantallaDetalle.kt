package com.vila.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vila.murocampus.viewmodel.PostsViewModel
import com.vila.murocampus.viewmodel.UiState

// Detalle de una publicación con sus comentarios
@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    onVolver: () -> Unit,
    onBorrado: (Int) -> Unit,          // Mejora 3: avisa a MainActivity el código de respuesta
    modifier: Modifier = Modifier
) {
    // El botón Atrás del sistema vuelve a la lista
    BackHandler { onVolver() }

    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()

    // Mejora 3: ¿se muestra el diálogo de confirmación?
    var confirmar by rememberSaveable { mutableStateOf(false) }

    // Mejora 3: si el borrado salió bien, se limpia y se vuelve a la lista
    LaunchedEffect(borrado) {
        val b = borrado
        if (b is UiState.Exito) {
            vm.limpiarBorrado()
            onBorrado(b.datos)
        }
    }

    when (val e = estado) {
        is UiState.Cargando -> Cargando(texto = "Cargando publicación…", modifier = modifier)

        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.reintentarDetalle() },
            modifier = modifier
        )

        is UiState.Exito -> {
            val d = e.datos
            val borrando = borrado is UiState.Cargando

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

                // Mejora 3: botón Eliminar, a la derecha, en tono de error
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FilledTonalButton(
                            onClick = { confirmar = true },
                            enabled = !borrando,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text(if (borrando) "Eliminando…" else "Eliminar")
                        }
                    }
                }

                // Mejora 3: si el borrado falló, se muestra el mensaje y se queda en el detalle
                val b = borrado
                if (b is UiState.Error) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "No se pudo eliminar. ${b.mensaje}",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
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

            // Mejora 3: diálogo de confirmación (Material 3)
            if (confirmar) {
                AlertDialog(
                    onDismissRequest = { confirmar = false },
                    title = { Text("¿Eliminar publicación?") },
                    text = { Text("Se eliminará \"${d.post.title}\". Esta acción no se puede deshacer.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                confirmar = false
                                vm.borrar(d.post.id)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) { Text("Eliminar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}