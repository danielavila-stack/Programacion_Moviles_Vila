package com.daniela.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.tecsupfit.data.DatosMock
import com.daniela.tecsupfit.ui.theme.TecsupPrimary

@Composable
fun ConfirmacionScreen(
    claseId: Int,
    onVerReservas: () -> Unit
) {
    val clase = DatosMock.listaClases.find { it.id == claseId } ?: DatosMock.listaClases[0]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE8F5E9),
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = TecsupPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Cupo reservado!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = clase.nombre,
            fontSize = 16.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Hoy, ${clase.hora} · ${clase.sala}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onVerReservas,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Ver mis reservas",
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}