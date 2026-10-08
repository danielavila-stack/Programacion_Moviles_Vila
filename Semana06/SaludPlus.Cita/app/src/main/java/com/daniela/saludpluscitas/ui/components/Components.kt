package com.daniela.saludpluscitas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.navigation.Rutas
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario
import com.daniela.saludpluscitas.ui.theme.GrisBorde
import com.daniela.saludpluscitas.ui.theme.TextoPrincipal
import com.daniela.saludpluscitas.ui.theme.TextoSecundario

// 1. Botón Principal Azul SaludPlus
@Composable
fun BotonSaludPlus(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrimario,
            contentColor = Color.White,
            disabledContainerColor = AzulClaroFondo,
            disabledContentColor = TextoSecundario
        )
    ) {
        Text(
            text = texto,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// 2. Campo de Texto Outlined Personalizado
@Composable
fun CampoTextoSaludPlus(
    valor: String,
    onValorChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label, color = TextoSecundario) },
        leadingIcon = if (icon != null) {
            { Icon(imageVector = icon, contentDescription = null, tint = TextoSecundario) }
        } else null,
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AzulPrimario,
            unfocusedBorderColor = GrisBorde,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

// 3. Avatar Circular de Iniciales (ej: "DV" o "CM")
@Composable
fun AvatarIniciales(
    iniciales: String,
    tamano: Int = 50,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(tamano.dp)
            .background(color = AzulClaroFondo, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            color = AzulPrimario,
            fontSize = (tamano / 2.5).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// 4. Barra de Navegación Inferior (BottomBar)
@Composable
fun BarraNavegacionInferior(
    rutaActual: String?,
    onNavegar: (String) -> Unit
) {
    val items = listOf(
        NavegacionItem("Inicio", Rutas.Home.ruta, Icons.Default.Home),
        NavegacionItem("Citas", Rutas.MisCitas.ruta, Icons.Default.DateRange),
        NavegacionItem("Resultados", Rutas.Resultados.ruta, Icons.Default.List),
        NavegacionItem("Perfil", Rutas.Perfil.ruta, Icons.Default.Person)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val seleccionado = rutaActual == item.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = { if (!seleccionado) onNavegar(item.ruta) },
                icon = { Icon(imageVector = item.icono, contentDescription = item.titulo) },
                label = { Text(item.titulo) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulPrimario,
                    selectedTextColor = AzulPrimario,
                    indicatorColor = AzulClaroFondo,
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario
                )
            )
        }
    }
}

private data class NavegacionItem(
    val titulo: String,
    val ruta: String,
    val icono: ImageVector
)