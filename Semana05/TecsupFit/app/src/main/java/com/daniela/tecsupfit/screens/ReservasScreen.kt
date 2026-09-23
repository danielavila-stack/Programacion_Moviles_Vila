package com.daniela.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.tecsupfit.data.DatosMock
import com.daniela.tecsupfit.ui.theme.TecsupPrimary

@Composable
fun ReservasScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp)
    ) {
        Text(
            text = "Mis reservas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(DatosMock.reservasAgendadas) { reserva ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = reserva.clase.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = reserva.fechaHora,
                            color = Color.Gray,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        val colorFondo = if (reserva.estado == "Confirmada") Color(0xFFE8F5E9) else Color(0xFFEEEEEE)
                        val colorTexto = if (reserva.estado == "Confirmada") TecsupPrimary else Color.Gray

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = colorFondo,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = reserva.estado,
                                color = colorTexto,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}