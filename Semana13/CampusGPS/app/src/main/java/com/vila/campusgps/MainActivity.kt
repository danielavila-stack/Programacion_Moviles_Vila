package com.vila.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vila.campusgps.ui.PantallaMapa
import com.vila.campusgps.ui.theme.CampusGPSTheme
import com.vila.campusgps.viewmodel.CampusViewModelFactory
import com.vila.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // MEJORA 2: empieza según el tema del celular y se puede cambiar con el botón
            val sistemaOscuro = isSystemInDarkTheme()
            var modoOscuro by rememberSaveable { mutableStateOf(sistemaOscuro) }

            CampusGPSTheme(darkTheme = modoOscuro) {
                val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(applicationContext))
                PantallaMapa(
                    vm = vm,
                    modoOscuro = modoOscuro,
                    onCambiarTema = { modoOscuro = !modoOscuro }
                )
            }
        }
    }
}