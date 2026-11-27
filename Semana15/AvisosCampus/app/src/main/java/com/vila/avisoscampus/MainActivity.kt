package com.vila.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vila.avisoscampus.ui.PantallaAvisos
import com.vila.avisoscampus.ui.theme.AvisosCampusTheme
import com.vila.avisoscampus.viewmodel.AvisosViewModel
import com.vila.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                // El ViewModel sobrevive a la rotación; la fábrica le da su Repository
                val vm: AvisosViewModel = viewModel(factory = AvisosViewModelFactory())

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // Barra superior azul con el título de la app
                        TopAppBar(
                            title = { Text("AvisosCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    PantallaAvisos(vm = vm, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
