package com.tecsup.valorant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tecsup.valorant.domain.model.Item
import com.tecsup.valorant.ui.lista.ListaAgentes
import com.tecsup.valorant.ui.theme.ValorantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prueba = listOf(
            Item("1", "Jett", "Duelista", null),
            Item("2", "Sage", "Centinela", null),
            Item("3", "Omen", "Controlador", null)
        )
        setContent {
            ValorantTheme {
                Scaffold { padding ->
                    Box(Modifier.padding(padding)) {
                        ListaAgentes(prueba, onClick = {}, onFavorito = {})
                    }
                }
            }
        }
    }
}
