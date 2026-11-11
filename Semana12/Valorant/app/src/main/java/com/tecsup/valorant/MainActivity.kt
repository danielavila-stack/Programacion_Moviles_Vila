package com.tecsup.valorant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.valorant.ui.navigation.AppNav
import com.tecsup.valorant.ui.theme.ValorantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as ValorantApp).container
        setContent {
            ValorantTheme { AppNav(container) }
        }
    }
}
