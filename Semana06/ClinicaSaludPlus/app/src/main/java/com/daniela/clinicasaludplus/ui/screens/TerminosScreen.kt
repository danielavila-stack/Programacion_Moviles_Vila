package com.daniela.clinicasaludplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(navController: NavController) {
    // RETO: Pantalla de términos y condiciones con Scroll
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Términos y Condiciones del Servicio",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = """
                        1. ACEPTACIÓN DE LOS TÉRMINOS
                        Al acceder y utilizar la aplicación Clínica Salud Plus, el usuario acepta de manera completa y sin reservas todos los términos y condiciones aquí presentados.

                        2. USO DEL SERVICIO Y PRIVACIDAD
                        La aplicación permite la programación y gestión de citas médicas. Los datos personales como correo, teléfono y nombre completo serán tratados bajo políticas estrictas de privacidad.

                        3. CANCELACIÓN DE CITAS
                        El usuario podrá cancelar sus citas desde la sección 'Mis Citas' utilizando la confirmación de diálogo.

                        4. PROPIEDAD INTELECTUAL
                        Todo el contenido y software de Clínica Salud Plus son propiedad exclusiva del equipo de desarrollo.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aceptar y Volver")
            }
        }
    }
}