package com.daniela.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus

@Composable
fun TerminosScreen(
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Términos y Condiciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = """
                Bienvenido a SaludPlus. Al utilizar nuestra aplicación para la gestión y reserva de citas médicas, aceptas cumplir con los siguientes términos:
                
                1. Uso de la Aplicación:
                La aplicación está destinada a facilitar la reserva de citas con profesionales médicos. Debes proporcionar datos verdaderos y actualizados.
                
                2. Privacidad de Datos:
                Tus datos personales y registros de citas médica se mantendrán de forma confidencial y protegida conforme a las normativas vigentes.
                
                3. Cancelación de Citas:
                Puedes cancelar tus citas reservadas directamente desde el módulo de 'Mis Citas' con al menos 2 horas de anticipación.
            """.trimIndent(),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        BotonSaludPlus(
            texto = "Entendido y Volver",
            onClick = onVolver
        )
    }
}