package com.daniela.tecsupfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CustomColorScheme = lightColorScheme(
    primary = TecsupPrimary,
    secondary = TecsupSecondary,
    background = TecsupBackground,
    surface = TecsupBackground,
    onPrimary = androidx.compose.ui.graphics.Color.White
)

@Composable
fun TecsupFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CustomColorScheme,
        content = content
    )
}