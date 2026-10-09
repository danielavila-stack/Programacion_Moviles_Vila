package com.daniela.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.daniela.saludpluscitas.data.model.Usuario
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.components.CampoTextoSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onIrATerminos: () -> Unit,
    onIrALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Crear cuenta", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(text = "Regístrate para agendar tus citas", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        CampoTextoSaludPlus(
            valor = nombre,
            onValorChange = { nombre = it; errorMensaje = "" },
            label = "Nombre completo",
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = dni,
            onValorChange = { dni = it; errorMensaje = "" },
            label = "DNI (8 dígitos)",
            icon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = telefono,
            onValorChange = { telefono = it; errorMensaje = "" },
            label = "Teléfono",
            icon = Icons.Default.Phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = correo,
            onValorChange = { correo = it; errorMensaje = "" },
            label = "Correo (opcional)",
            icon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = contrasena,
            onValorChange = { contrasena = it; errorMensaje = "" },
            label = "Contraseña",
            icon = Icons.Default.Lock,
            esContrasena = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (errorMensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMensaje, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        BotonSaludPlus(
            texto = "Registrarme",
            onClick = {
                // Reto 1: Validaciones estrictas de formato DNI y Teléfono
                if (nombre.isBlank() || dni.isBlank() || telefono.isBlank() || contrasena.isBlank()) {
                    errorMensaje = "Por favor completa todos los campos requeridos"
                } else if (dni.length != 8 || !dni.all { it.isDigit() }) {
                    errorMensaje = "El DNI debe tener exactamente 8 dígitos"
                } else if (telefono.length != 9 || !telefono.all { it.isDigit() }) {
                    errorMensaje = "El teléfono debe tener exactamente 9 dígitos"
                } else {
                    val exito = Repositorio.registrarUsuario(
                        Usuario("1", nombre, dni, telefono, correo, contrasena)
                    )
                    if (exito) onRegistroExitoso() else errorMensaje = "El registro falló"
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Al registrarte aceptas los ", fontSize = 12.sp, color = Color.Gray)
            TextButton(onClick = onIrATerminos) {
                Text("Términos y Condiciones", fontSize = 12.sp, color = AzulPrimario)
            }
        }

        TextButton(onClick = onIrALogin) {
            Text("¿Ya tienes cuenta? Iniciar sesión")
        }
    }
}