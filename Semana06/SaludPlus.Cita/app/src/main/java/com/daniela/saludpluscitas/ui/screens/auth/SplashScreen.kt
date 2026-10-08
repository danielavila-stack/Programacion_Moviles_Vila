package com.daniela.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun SplashScreen(
    onNavegarLogin: () -> Unit,
    onNavegarRegistro: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulPrimario)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SaludPlus",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Citas Médicas",
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(64.dp))

        BotonSaludPlus(
            texto = "Iniciar Sesión",
            onClick = onNavegarLogin
        )

        Spacer(modifier = Modifier.height(12.dp))

        BotonSaludPlus(
            texto = "Crear Cuenta",
            onClick = onNavegarRegistro
        )
    }
}