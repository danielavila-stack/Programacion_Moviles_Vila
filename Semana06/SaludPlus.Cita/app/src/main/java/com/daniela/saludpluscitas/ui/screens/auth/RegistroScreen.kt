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
import androidx.compose.material3.Checkbox
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
    var aceptoTerminos by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Crear Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario
        )

        Spacer(modifier = Modifier.height(24.dp))

        CampoTextoSaludPlus(
            valor = nombre,
            onValorChange = { nombre = it },
            label = "Nombre Completo",
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = dni,
            onValorChange = { dni = it },
            label = "DNI",
            icon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = telefono,
            onValorChange = { telefono = it },
            label = "Teléfono",
            icon = Icons.Default.Phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = correo,
            onValorChange = { correo = it },
            label = "Correo Electrónico",
            icon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoTextoSaludPlus(
            valor = contrasena,
            onValorChange = { contrasena = it },
            label = "Contraseña",
            icon = Icons.Default.Lock,
            esContrasena = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = aceptoTerminos,
                onCheckedChange = { aceptoTerminos = it }
            )
            Text(text = "Acepto los ", fontSize = 13.sp)
            TextButton(
                onClick = onIrATerminos,
                modifier = Modifier.padding(0.dp)
            ) {
                Text(text = "Términos y Condiciones", fontSize = 13.sp, color = AzulPrimario)
            }
        }

        if (errorMensaje.isNotEmpty()) {
            Text(
                text = errorMensaje,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BotonSaludPlus(
            texto = "Registrarse",
            onClick = {
                if (nombre.isBlank() || dni.isBlank() || telefono.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                    errorMensaje = "Por favor completa todos los campos"
                } else if (!aceptoTerminos) {
                    errorMensaje = "Debes aceptar los términos y condiciones"
                } else {
                    val nuevo = Usuario(
                        id = System.currentTimeMillis().toString(),
                        nombre = nombre,
                        dni = dni,
                        telefono = telefono,
                        correo = correo,
                        clave = contrasena
                    )
                    val exito = Repositorio.registrarUsuario(nuevo)
                    if (exito) {
                        onRegistroExitoso()
                    } else {
                        errorMensaje = "El correo o teléfono ya están registrados"
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onIrALogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}