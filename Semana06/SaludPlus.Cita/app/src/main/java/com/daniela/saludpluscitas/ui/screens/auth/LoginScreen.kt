package com.daniela.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.components.CampoTextoSaludPlus

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    var identificador by remember { mutableStateOf("987654321") }
    var contrasena by remember { mutableStateOf("123456") }
    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Iniciar sesión", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(text = "Ingresa con tu teléfono o correo", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        CampoTextoSaludPlus(
            valor = identificador,
            onValorChange = { identificador = it },
            label = "Teléfono o correo",
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        CampoTextoSaludPlus(
            valor = contrasena,
            onValorChange = { contrasena = it },
            label = "Contraseña",
            icon = Icons.Default.Lock,
            esContrasena = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (errorMensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMensaje, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(28.dp))

        BotonSaludPlus(
            texto = "Ingresar",
            onClick = {
                val exito = Repositorio.iniciarSesion(identificador, contrasena)
                if (exito) onLoginExitoso() else errorMensaje = "Credenciales incorrectas"
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onIrARegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }

        Text(text = "Demo: 987654321 / 123456", fontSize = 12.sp, color = Color.Gray)
    }
}