package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.AvatarIniciales
import com.daniela.saludpluscitas.ui.components.BotonSaludPlus
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun AgendarCitaScreen(
    medicoId: String,
    onCitaConfirmada: (medicoId: String, fecha: String, hora: String) -> Unit,
    onVolver: () -> Unit = {}
) {
    val medico = Repositorio.obtenerMedicoPorId(medicoId)
    val dias = listOf(
        Pair("Jue", "8"), Pair("Vie", "9"), Pair("Lun", "12"), Pair("Mar", "13"), Pair("Mié", "14")
    )
    val horarios = listOf(
        "08:00", "09:00", "09:30", "10:00", "10:30", "11:00",
        "11:30", "14:00", "14:30", "15:00", "15:30"
    )

    var diaSeleccionado by remember { mutableStateOf("8") }
    var horaSeleccionada by remember { mutableStateOf("15:30") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Seleccionar fecha y hora", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AzulClaroFondo)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                AvatarIniciales(
                    iniciales = medico?.nombre?.split(" ")
                        ?.mapNotNull { it.firstOrNull()?.toString() }
                        ?.take(2)
                        ?.joinToString("") ?: "DR"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(medico?.nombre ?: "Médico Especialista", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(medico?.especialidadNombre ?: "Especialidad", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null, tint = Color.Gray)
            Text(text = "Octubre 2026", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(dias) { (nom, num) ->
                val esSel = num == diaSeleccionado
                Card(
                    modifier = Modifier
                        .width(62.dp)
                        .clickable { diaSeleccionado = num },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (esSel) AzulPrimario else Color(0xFFF5F7FA)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(nom, fontSize = 12.sp, color = if (esSel) Color.White else Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(num, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (esSel) Color.White else Color.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Horarios disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(horarios) { h ->
                val esSel = h == horaSeleccionada
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { horaSeleccionada = h },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (esSel) AzulPrimario else Color(0xFFEDF2F7)
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(h, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (esSel) Color.White else AzulPrimario)
                    }
                }
            }
        }

        BotonSaludPlus(
            texto = "Continuar",
            onClick = {
                val fechaFormateada = when(diaSeleccionado) {
                    "8" -> "Jueves 8 de octubre 2026"
                    "9" -> "Viernes 9 de octubre 2026"
                    "12" -> "Lunes 12 de octubre 2026"
                    "13" -> "Martes 13 de octubre 2026"
                    else -> "Miércoles 14 de octubre 2026"
                }
                onCitaConfirmada(medicoId, fechaFormateada, horaSeleccionada)
            }
        )
    }
}