package com.vila.daniela.tecsupstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vila.daniela.tecsupstore.screens.AppNavegacion
import com.vila.daniela.tecsupstore.ui.theme.TECSUPStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TECSUPStoreTheme {
                AppNavegacion()
            }
        }
    }
}