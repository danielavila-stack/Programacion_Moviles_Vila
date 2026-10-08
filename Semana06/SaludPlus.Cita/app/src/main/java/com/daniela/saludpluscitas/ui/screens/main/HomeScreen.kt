package com.daniela.saludpluscitas.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.data.repository.Repositorio
import com.daniela.saludpluscitas.ui.components.AvatarIniciales
import com.daniela.saludpluscitas.ui.components.BarraNavegacionInferior
import com.daniela.saludpluscitas.ui.theme.AzulClaroFondo
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@Composable
fun HomeScreen(
    onNavegarAEspecialidades: () -> Unit,
    onNavegarAMedicos: (String) -> Unit,
    onNavegarBottomBar: (String) -> Unit,
    rutaActual: String
) {
    val usuario = Repositorio.usuarioActual
    val especialidades = Repositorio.especialidadesDestacadas()

    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(
                rutaActual = rutaActual,
                onNavegar = onNavegarBottomBar
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "¡Hola!",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = usuario?.nombre ?: "Usuario",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulPrimario
                        )
                    }
                    AvatarIniciales(
                        iniciales = usuario?.nombre?.split(" ")
                            ?.mapNotNull { it.firstOrNull()?.toString() }
                            ?.take(2)
                            ?.joinToString("") ?: "DV"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AzulClaroFondo),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Reserva tu cita médica",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = AzulPrimario
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Encuentra especialistas capacitados en pocos pasos.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Especialidades",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onNavegarAEspecialidades) {
                        Text("Ver todas")
                    }
                }
            }

            items(especialidades) { especialidad ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onNavegarAMedicos(especialidad.id) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = especialidad.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = especialidad.descripcion,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}