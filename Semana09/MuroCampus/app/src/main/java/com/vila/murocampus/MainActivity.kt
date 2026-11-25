package com.vila.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vila.murocampus.ui.PantallaLista
import com.vila.murocampus.ui.theme.MuroCampusTheme
import com.vila.murocampus.viewmodel.PostsViewModel
import com.vila.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                // La Factory crea el ViewModel con el Repository y la API
                val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory())

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { padding ->
                    // Paso 6: la lista de publicaciones
                    PantallaLista(
                        vm = vm,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}