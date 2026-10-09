package com.daniela.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun TerminosScreen(onVolver: () -> Unit) {
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
            color = AzulPrimario
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = """
                Bienvenido a SaludPlusCitas. Al registrarse y hacer uso de nuestra plataforma, usted acepta cumplir con los siguientes términos:

                1. Uso del Servicio:
                La aplicación permite gestionar la reserva de citas médicas e inspeccionar historiales clínicos. Toda la información proporcionada por el usuario debe ser fidedigna.

                2. Privacidad de Datos:
                Nos comprometemos a resguardar la confidencialidad de sus datos personales y antecedentes médicos bajo los más estrictos estándares de seguridad digital.

                3. Cancelaciones:
                Las citas pueden ser canceladas a través de la aplicación en la sección "Detalle de Cita" previa notificación con al menos 2 horas de anticipación.

                4. Aceptación:
                Al presionar "Aceptar y Volver", el usuario declara estar conforme con las condiciones descritas.
            """.trimIndent(),
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        BotonSaludPlus(
            texto = "Aceptar y Volver",
            onClick = onVolver
        )
    }
}