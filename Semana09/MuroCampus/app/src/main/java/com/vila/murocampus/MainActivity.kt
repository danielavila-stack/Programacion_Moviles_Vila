package com.vila.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vila.murocampus.ui.PantallaCrear
import com.vila.murocampus.ui.PantallaDetalle
import com.vila.murocampus.ui.PantallaLista
import com.vila.murocampus.ui.theme.MuroCampusTheme
import com.vila.murocampus.viewmodel.PostsViewModel
import com.vila.murocampus.viewmodel.PostsViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                // La Factory crea el ViewModel con el Repository y la API
                val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory())

                // Qué publicación está abierta: null = se ve la lista
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                // true = se ve el formulario de crear
                var creando by rememberSaveable { mutableStateOf(false) }

                // Mejora 3: aviso de abajo ("Publicación eliminada")
                val avisos = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                val enLista = idSeleccionado == null && !creando

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    snackbarHost = { SnackbarHost(avisos) },
                    floatingActionButton = {
                        // Paso 9: el botón + solo aparece en la lista
                        if (enLista) {
                            FloatingActionButton(onClick = {
                                vm.limpiarCreacion()
                                creando = true
                            }) {
                                Text("+", fontSize = 26.sp)
                            }
                        }
                    }
                ) { padding ->
                    when {
                        // Paso 9: crear publicación
                        creando -> PantallaCrear(
                            vm = vm,
                            onVolver = { creando = false },
                            modifier = Modifier.padding(padding)
                        )

                        // Paso 7 + Mejora 3: detalle con comentarios y botón Eliminar
                        idSeleccionado != null -> PantallaDetalle(
                            vm = vm,
                            onVolver = { idSeleccionado = null },
                            onBorrado = { codigo ->
                                idSeleccionado = null   // vuelve a la lista
                                scope.launch {
                                    avisos.showSnackbar("Publicación eliminada (código $codigo)")
                                }
                            },
                            modifier = Modifier.padding(padding)
                        )

                        // Pasos 6 y 8: lista con filtro y búsqueda
                        else -> PantallaLista(
                            vm = vm,
                            onAbrir = { id ->
                                idSeleccionado = id
                                vm.abrir(id)
                            },
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }
}