package com.vila.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(applicationContext))
                PantallaMapa(vm)
            }
        }
    }
}