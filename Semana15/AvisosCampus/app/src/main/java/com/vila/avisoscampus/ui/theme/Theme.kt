package com.vila.avisoscampus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Esquema oscuro: todo en azules para que nada salga lila
private val DarkColorScheme = darkColorScheme(
    primary = Azul80,
    onPrimary = Color(0xFF003062),
    primaryContainer = AzulContenedor30,
    onPrimaryContainer = AzulClaro90,
    secondary = AzulGris80,
    secondaryContainer = AzulGrisContenedor30,
    onSecondaryContainer = AzulGrisClaro90,
    tertiary = Celeste80
)

// Esquema claro: primary azul 0xFF1565C0
private val LightColorScheme = lightColorScheme(
    primary = Azul40,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor90,
    onPrimaryContainer = AzulOscuro10,
    secondary = AzulGris40,
    secondaryContainer = AzulGrisContenedor90,
    onSecondaryContainer = AzulGrisOscuro10,
    tertiary = Celeste40
)

@Composable
fun AvisosCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivado: así el celular no cambia los colores por los del fondo de pantalla
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
