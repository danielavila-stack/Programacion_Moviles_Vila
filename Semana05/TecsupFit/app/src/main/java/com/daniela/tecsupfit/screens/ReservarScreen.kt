package com.daniela.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.tecsupfit.data.DatosMock
import com.daniela.tecsupfit.model.Reserva
import com.daniela.tecsupfit.ui.theme.TecsupPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservarScreen(
    claseId: Int,
    onVolver: () -> Unit,
    onConfirmar: (Int) -> Unit
) {
    val clase = DatosMock.listaClases.find { it.id == claseId } ?: DatosMock.listaClases[0]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Reserva", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Resumen de tu clase", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Clase: ${clase.nombre}", fontSize = 16.sp)
                    Text("Horario: ${clase.hora}", fontSize = 14.sp, color = Color.Gray)
                    Text("Ubicación: ${clase.sala}", fontSize = 14.sp, color = Color.Gray)
                    Text("Duración: ${clase.duracion}", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Button(
                onClick = {
                    if (DatosMock.reservasAgendadas.none { it.clase.id == clase.id }) {
                        DatosMock.reservasAgendadas.add(
                            0,
                            Reserva(
                                id = System.currentTimeMillis().toInt(),
                                clase = clase,
                                fechaHora = "Hoy, ${clase.hora}",
                                estado = "Confirmada"
                            )
                        )
                    }
                    onConfirmar(clase.id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TecsupPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirmar y Guardar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}