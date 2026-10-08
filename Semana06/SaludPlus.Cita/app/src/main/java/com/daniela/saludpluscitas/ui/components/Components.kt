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
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.navigation.Rutas
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

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
            .height(50.dp),
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
    esPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    val ocultar = esContrasena || esPassword
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label) },
        leadingIcon = icon?.let { { Icon(imageVector = it, contentDescription = null) } },
        visualTransformation = if (ocultar) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun AvatarIniciales(
    iniciales: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(AzulClaroFondo),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario,
            fontSize = 18.sp
        )
    }
}

@Composable
fun BarraNavegacionInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == Rutas.Home.ruta,
            onClick = { onNavegar(Rutas.Home.ruta) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.Especialidades.ruta,
            onClick = { onNavegar(Rutas.Especialidades.ruta) },
            icon = { Icon(Icons.Default.List, contentDescription = "Especialidades") },
            label = { Text("Especialidades") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.MisCitas.ruta,
            onClick = { onNavegar(Rutas.MisCitas.ruta) },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Mis Citas") },
            label = { Text("Citas") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.Perfil.ruta,
            onClick = { onNavegar(Rutas.Perfil.ruta) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}