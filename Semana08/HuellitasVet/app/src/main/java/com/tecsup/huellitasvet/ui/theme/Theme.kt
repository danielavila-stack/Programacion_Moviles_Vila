package com.tecsup.huellitasvet.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.Rol

private val DuenoClaro = lightColorScheme(
    primary = Esmeralda,
    onPrimary = Color.White,
    primaryContainer = EsmeraldaContenedor,
    onPrimaryContainer = EsmeraldaOscuro,
    secondary = EsmeraldaOscuro,
    background = DuenoFondo,
    surface = Color.White,
    surfaceVariant = Color(0xFFEAF5EF),
    onSurfaceVariant = Color(0xFF5B6B63),
    outline = Color(0xFFD5E3DB),
    error = Cancelada
)

private val DuenoOscuro = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF00382A),
    primaryContainer = EsmeraldaOscuro,
    onPrimaryContainer = EsmeraldaContenedor,
    secondary = Color(0xFF6EE7B7),
    background = FondoOscuro,
    surface = SuperficieOscura,
    surfaceVariant = Color(0xFF26302C),
    onSurfaceVariant = Color(0xFFB4C2BB),
    outline = Color(0xFF3A4842),
    error = Color(0xFFF87171)
)

private val VetClaro = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulOscuro,
    secondary = AzulOscuro,
    background = VetFondo,
    surface = Color.White,
    surfaceVariant = Color(0xFFEDF1FA),
    onSurfaceVariant = Color(0xFF5B6475),
    outline = Color(0xFFD6DDEB),
    error = Cancelada
)

private val VetOscuro = darkColorScheme(
    primary = Color(0xFF93B4FF),
    onPrimary = Color(0xFF0B2A6B),
    primaryContainer = AzulOscuro,
    onPrimaryContainer = AzulContenedor,
    secondary = Color(0xFFBFD3FF),
    background = FondoOscuroVet,
    surface = SuperficieOscuraVet,
    surfaceVariant = Color(0xFF262C3A),
    onSurfaceVariant = Color(0xFFB6BDCC),
    outline = Color(0xFF3A4252),
    error = Color(0xFFF87171)
)

fun esquemaDe(rol: Rol, oscuro: Boolean): ColorScheme = when (rol) {
    Rol.CLIENTE -> if (oscuro) DuenoOscuro else DuenoClaro
    Rol.ADMIN -> if (oscuro) VetOscuro else VetClaro
}

// Entrega un ColorScheme según el perfil. En las pantallas usa MaterialTheme.colorScheme.
@Composable
fun VetTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = esquemaDe(rol, oscuro),
        typography = VetTypography,
        content = content
    )
}
