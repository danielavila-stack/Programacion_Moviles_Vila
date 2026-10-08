package com.daniela.saludpluscitas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    secondary = AzulSecundario,
    background = FondoPantalla,
    surface = Color.White,
    onBackground = TextoPrincipal,
    onSurface = TextoPrincipal,
    surfaceVariant = AzulClaroFondo,
    onSurfaceVariant = TextoSecundario,
    outline = GrisBorde
)

@Composable
fun SaludPlusCitasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}