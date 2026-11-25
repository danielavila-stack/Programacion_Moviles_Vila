package com.vila.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vila.murocampus.ui.theme.VerdeExito
import com.vila.murocampus.ui.theme.VerdeExitoTexto
import com.vila.murocampus.viewmodel.PostsViewModel
import com.vila.murocampus.viewmodel.UiState

// Formulario para crear una publicación nueva
@Composable
fun PantallaCrear(vm: PostsViewModel, onVolver: () -> Unit, modifier: Modifier = Modifier) {
    // El botón Atrás del sistema vuelve a la lista
    BackHandler { onVolver() }

    val estado by vm.creacion.collectAsStateWithLifecycle()

    // Lo escrito en los campos (se mantiene al rotar)
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }

    // Si se publicó con éxito, se limpia el formulario
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    val enviando = estado is UiState.Cargando
    // Publicar solo si los dos campos tienen texto y no se está enviando
    val puedePublicar = titulo.isNotBlank() && contenido.isNotBlank() && !enviando

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            minLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { vm.publicar(titulo, contenido) },
            enabled = puedePublicar
        ) {
            Text(if (enviando) "Publicando…" else "Publicar")
        }

        // Resultado del envío
        when (val e = estado) {
            null, is UiState.Cargando -> {}   // nada que mostrar todavía

            // Éxito: fondo verde claro con texto verde oscuro
            is UiState.Exito -> Surface(
                color = VerdeExito,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Publicado con id ${e.datos.id}",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = VerdeExitoTexto,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Si falla, se muestra el error y se CONSERVA lo escrito para reintentar
            is UiState.Error -> Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    e.mensaje,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}