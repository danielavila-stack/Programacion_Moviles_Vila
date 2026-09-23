package com.daniela.navlab_05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.daniela.navlab_05.navigation.AppNavigation
import com.daniela.navlab_05.ui.theme.NavLab05Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavLab05Theme {
                AppNavigation()
            }
        }
    }
}