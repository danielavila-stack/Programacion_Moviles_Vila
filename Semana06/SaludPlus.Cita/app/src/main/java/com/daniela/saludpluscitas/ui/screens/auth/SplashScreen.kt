package com.daniela.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun SplashScreen(
    onNavegarLogin: () -> Unit,
    onNavegarRegistro: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AzulPrimario),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Clínica", fontSize = 18.sp, color = AzulPrimario)
        Text(text = "SaludPlus", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AzulPrimario)
        Text(text = "Tu salud, nuestra prioridad", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        // Espacio reservado para ilustración del médico
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(200.dp)
                .background(AzulClaroFondo, shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("👨‍⚕️", fontSize = 80.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        BotonSaludPlus(
            texto = "Comenzar",
            onClick = onNavegarRegistro
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavegarLogin) {
            Text(text = "Ya tengo una cuenta", color = AzulPrimario)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}