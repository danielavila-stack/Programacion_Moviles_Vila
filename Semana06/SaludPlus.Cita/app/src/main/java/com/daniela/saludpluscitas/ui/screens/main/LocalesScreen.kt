package com.daniela.saludpluscitas.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

data class LocalSede(
    val id: String,
    val nombre: String,
    val direccion: String,
    val horario: String,
    val telefono: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalesScreen(
    onSeleccionarLocal: (String) -> Unit,
    onVolver: () -> Unit
) {
    val listaLocales = listOf(
        LocalSede(
            id = "1",
            nombre = "Sede Independencia",
            direccion = "Av. Carlos Izaguirre 512, Independencia",
            horario = "Lunes a Sábado: 07:00 - 20:00",
            telefono = "(01) 619-0001"
        ),
        LocalSede(
            id = "2",
            nombre = "Sede La Molina",
            direccion = "Av. Javier Prado Este 4820, La Molina",
            horario = "Lunes a Sábado: 07:00 - 21:00",
            telefono = "(01) 619-0002"
        ),
        LocalSede(
            id = "3",
            nombre = "Sede Santa Clara",
            direccion = "Av. Nicolás de Piérola 350, Ate - Santa Clara",
            horario = "Lunes a Viernes: 08:00 - 19:00",
            telefono = "(01) 619-0003"
        ),
        LocalSede(
            id = "4",
            nombre = "Sede Santa Anita",
            direccion = "Av. Los Eucaliptos 145, Santa Anita",
            horario = "Lunes a Sábado: 07:00 - 20:00",
            telefono = "(01) 619-0004"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestros Locales", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Selecciona la sede donde deseas agendar tu cita",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(listaLocales) { sede ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSeleccionarLocal(sede.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FA))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sede.nombre,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario
                            )
                            Text(
                                text = "Agendar >",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sede.direccion,
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sede.horario,
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sede.telefono,
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}