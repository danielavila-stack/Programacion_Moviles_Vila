package com.daniela.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.BarraNavegacionInferior
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario
import com.daniela.saludpluscitas.utils.FechaUtils

@Composable
fun MisCitasScreen(
    onVerDetalle: (String) -> Unit,
    onVerResultados: (String) -> Unit,
    onNavegarBottomBar: (String) -> Unit = {},
    rutaActual: String = "",
    onVolver: () -> Unit = {} // <-- Parámetro agregado
) {
    val citas = Repositorio.citas

    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(
                rutaActual = rutaActual,
                onNavegar = onNavegarBottomBar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Cabecera con botón de retroceso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) { // <-- Conectado aquí
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Mis Citas", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (citas.isEmpty()) {
                Text(
                    text = "No tienes citas agendadas.",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(citas) { cita ->
                        val medico = Repositorio.obtenerMedicoPorId(cita.medicoId)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = AzulClaroFondo)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${FechaUtils.fechaLarga(cita.fecha)} - ${cita.hora}",
                                    fontWeight = FontWeight.Bold,
                                    color = AzulPrimario
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = medico?.nombre ?: "Médico",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = medico?.especialidadNombre ?: "Especialidad",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row {
                                    Button(
                                        onClick = { onVerDetalle(cita.id) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                                    ) {
                                        Text("Ver detalle")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}