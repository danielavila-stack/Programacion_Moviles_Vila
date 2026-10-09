package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.model.Cita
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.AvatarIniciales
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun ConfirmacionScreen(
    medicoId: String,
    fecha: String,
    hora: String,
    onVolverInicio: () -> Unit
) {
    val medico = Repositorio.obtenerMedicoPorId(medicoId)
    var motivo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolverInicio) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Confirmar cita", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarIniciales(
                        iniciales = medico?.nombre?.split(" ")
                            ?.mapNotNull { it.firstOrNull()?.toString() }
                            ?.take(2)
                            ?.joinToString("") ?: "DR"
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(medico?.nombre ?: "Dr. Carlos Mendoza", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(medico?.especialidadNombre ?: "Medicina General", fontSize = 12.sp, color = Color.Gray)
                        Text(medico?.cmp ?: "CMP 11201", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(12.dp))

                ItemInfoConfirmar(icon = Icons.Default.DateRange, label = "Fecha", valor = fecha)
                Spacer(modifier = Modifier.height(10.dp))
                ItemInfoConfirmar(icon = Icons.Default.Info, label = "Hora", valor = hora)
                Spacer(modifier = Modifier.height(10.dp))
                ItemInfoConfirmar(icon = Icons.Default.Add, label = "Tipo de atención", valor = "Consulta presencial")
                Spacer(modifier = Modifier.height(10.dp))
                ItemInfoConfirmar(icon = Icons.Default.Place, label = "Dirección", valor = "Av. Los Olivos 123, Lima")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            placeholder = { Text("Motivo de consulta (opcional)", color = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        BotonSaludPlus(
            texto = "Agendar cita",
            onClick = {
                val usuarioActual = Repositorio.usuarioActual
                Repositorio.guardarCita(
                    Cita(
                        id = System.currentTimeMillis().toString(),
                        usuarioId = usuarioActual?.id ?: "1",
                        medicoId = medicoId,
                        fecha = fecha,
                        hora = hora,
                        estado = "Confirmada"
                    )
                )
                onVolverInicio()
            }
        )
    }
}

@Composable
fun ItemInfoConfirmar(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(valor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}