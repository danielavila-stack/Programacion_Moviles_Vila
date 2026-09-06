package com.daniela.laboratorio3_tarea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RegistroNotasScreen()
            }
        }
    }
}

@Composable
fun RegistroNotasScreen() {
    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }

    var calculado by remember { mutableStateOf(false) }

    val promedioPonderado = (nota1 * 0.20f) + (nota2 * 0.25f) + (nota3 * 0.30f) + (nota4 * 0.25f)
    val promedioFinal = if (redondear) promedioPonderado.roundToInt().toFloat() else promedioPonderado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF3EDF7), Color.White)))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6750A4))
                .padding(20.dp)
        ) {
            Text(
                text = "Registro de Notas",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Notas del ciclo", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Desliza para asignar cada nota (0 a 20)", fontSize = 12.sp, color = Color.Gray)

            CursoItem("Fundamentos de Programación", 20, nota1) { nota1 = it; calculado = false }
            CursoItem("Programación Orientada a Objetos", 25, nota2) { nota2 = it; calculado = false }
            CursoItem("Programación en Móviles", 30, nota3) { nota3 = it; calculado = false }
            CursoItem("Base de Datos", 25, nota4) { nota4 = it; calculado = false }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Redondear promedio final", fontSize = 14.sp)
                Switch(
                    checked = redondear,
                    onCheckedChange = { redondear = it; calculado = false }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = confirmado,
                    onCheckedChange = { confirmado = it }
                )
                Text("Confirmo que las notas son correctas", fontSize = 14.sp)
            }

            Button(
                onClick = { calculado = true },
                enabled = confirmado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text("CALCULAR PROMEDIO", fontWeight = FontWeight.Bold)
            }

            if (!calculado) {
                Text(
                    text = "Asigna las notas y confirma para calcular",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                ResultadoCard(
                    promedioPonderado = promedioPonderado,
                    promedioFinal = promedioFinal,
                    isRedondeado = redondear
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Desarrollado por: Daniela Vila",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CursoItem(nombre: String, peso: Int, nota: Float, onNotaChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(nombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("($peso%)", color = Color(0xFF6750A4), fontSize = 12.sp)
            }

            Surface(
                color = Color(0xFFE8DEF8),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${nota.toInt()}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1B20)
                )
            }
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF6750A4),
                activeTrackColor = Color(0xFF6750A4)
            )
        )
    }
}

@Composable
fun ResultadoCard(promedioPonderado: Float, promedioFinal: Float, isRedondeado: Boolean) {
    val (observacion, chipBgColor, chipTextColor) = when {
        promedioFinal >= 17f -> Triple("EXCELENTE", Color(0xFFD0E8D0), Color(0xFF0F5128))
        promedioFinal >= 13f -> Triple("APROBADO", Color(0xFFE1F0DA), Color(0xFF2E7D32))
        promedioFinal >= 10f -> Triple("EN RECUPERACIÓN", Color(0xFFFFF3CD), Color(0xFF856404))
        else -> Triple("DESAPROBADO", Color(0xFFF8D7DA), Color(0xFF721C24))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Promedio ponderado:  ${String.format("%.2f", promedioPonderado)}",
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Promedio final:  ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6750A4)
                    )
                    Text(
                        text = if (isRedondeado) "${promedioFinal.toInt()}" else String.format("%.2f", promedioFinal),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6750A4)
                    )
                }

                if (isRedondeado) {
                    Text(
                        text = "(redondeado)",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = chipBgColor,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = observacion,
                        color = chipTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Text(
            text = "✓  Promedio calculado correctamente",
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}