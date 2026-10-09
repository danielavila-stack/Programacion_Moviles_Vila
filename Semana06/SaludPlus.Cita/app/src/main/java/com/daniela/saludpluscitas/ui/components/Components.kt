package com.daniela.saludpluscitas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.navigation.Rutas
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

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
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
    ) {
        Text(text = texto, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CampoTextoSaludPlus(
    valor: String,
    onValorChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    esContrasena: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label) },
        leadingIcon = icon?.let { { Icon(imageVector = it, contentDescription = null) } },
        visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun AvatarIniciales(
    iniciales: String,
    tamano: Dp = 44.dp, // Valor por defecto
    fontSize: TextUnit = 16.sp // Valor por defecto
) {
    Box(
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(Color(0xFFD0E1FD)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario,
            fontSize = fontSize
        )
    }
}

@Composable
fun BarraNavegacionInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = rutaActual == Rutas.Home.ruta,
            onClick = { onNavegar(Rutas.Home.ruta) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.MisCitas.ruta,
            onClick = { onNavegar(Rutas.MisCitas.ruta) },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Citas") },
            label = { Text("Citas") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.Resultados.ruta,
            onClick = { onNavegar(Rutas.Resultados.ruta) },
            icon = { Icon(Icons.Default.Receipt, contentDescription = "Resultados") },
            label = { Text("Resultados") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.Perfil.ruta,
            onClick = { onNavegar(Rutas.Perfil.ruta) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}