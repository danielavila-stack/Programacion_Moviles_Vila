package com.daniela.saludpluscitas.ui.screens.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.AvatarIniciales
import com.daniela.saludpluscitas.ui.components.BarraNavegacionInferior
import com.daniela.saludpluscitas.ui.theme.AzulPrimario
import androidx.compose.foundation.layout.width

@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit,
    onNavegarBottomBar: (String) -> Unit = {},
    rutaActual: String = ""
) {
    // Obtenemos los datos dinámicos del usuario registrado
    val usuario = Repositorio.usuarioActual
    val nombreMostrar = usuario?.nombre ?: "Usuario"
    val correoMostrar = usuario?.correo?.ifEmpty { "Sin correo" } ?: "correo@ejemplo.com"
    val telefonoMostrar = usuario?.telefono ?: "999999999"

    // Calculamos las iniciales dinámicamente
    val iniciales = nombreMostrar.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .uppercase()

    // Reto 4: Contador de citas activas
    val citasActivas = Repositorio.citas.count { it.estado == "Confirmada" }

    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(
                rutaActual = rutaActual,
                onNavegar = onNavegarBottomBar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            AvatarIniciales(iniciales = iniciales, tamano = 80.dp, fontSize = 28.sp)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = nombreMostrar, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = correoMostrar, fontSize = 14.sp, color = Color.Gray)
            Text(text = telefonoMostrar, fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EstadisticaPerfil("Citas Activas", citasActivas.toString())
                EstadisticaPerfil("Historial", "0")
            }

            Spacer(modifier = Modifier.height(32.dp))

            MenuPerfilItem(icono = Icons.Default.Person, titulo = "Mis Datos Personales")
            MenuPerfilItem(icono = Icons.Default.Notifications, titulo = "Configuración de Notificaciones")
            MenuPerfilItem(icono = Icons.Default.Settings, titulo = "Ajustes de la Aplicación")

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = {
                    Repositorio.usuarioActual = null // Limpiamos la sesión
                    onCerrarSesion()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EstadisticaPerfil(titulo: String, valor: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA)),
        modifier = Modifier.size(100.dp, 80.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = valor, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulPrimario)
            Text(text = titulo, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun MenuPerfilItem(icono: androidx.compose.ui.graphics.vector.ImageVector, titulo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = titulo, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}