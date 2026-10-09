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

import com.daniela.saludpluscitas.ui.theme.AzulPrimario
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.daniela.saludpluscitas.R

@Composable
fun SplashScreen(
    onNavegarLogin: () -> Unit,
    onNavegarRegistro: () -> Unit
) {
    val azul = Color(0xFF1F6FEB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Logo: círculo celeste + cuadrado azul + cruz blanca
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFD6E6FF)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(azul),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Clínica", fontSize = 20.sp, color = azul)
        Text(text = "SaludPlus", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = azul)
        Text(text = "Tu salud, nuestra prioridad", fontSize = 14.sp, color = Color.Gray)

        // Imagen del doctor (ocupa el espacio del centro)
        Image(
            painter = painterResource(id = R.drawable.doc),
            contentDescription = "Doctor",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        BotonSaludPlus(
            texto = "Comenzar",
            onClick = onNavegarRegistro
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavegarLogin) {
            Text(text = "Ya tengo una cuenta", color = azul)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}