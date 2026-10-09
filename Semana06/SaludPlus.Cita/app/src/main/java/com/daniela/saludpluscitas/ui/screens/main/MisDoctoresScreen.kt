package com.daniela.saludpluscitas.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.saludpluscitas.R
import com.daniela.saludpluscitas.data.model.Medico
import com.daniela.saludpluscitas.ui.theme.AzulPrimario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisDoctoresScreen(
    onVolver: () -> Unit
) {
    val doctoresPorEspecialidad = mapOf(
        "Medicina General" to listOf(
            Medico("1", "Dr. Carlos Mendoza", "1", "Medicina General", "CMP-12345"),
            Medico("2", "Dra. Ana Torres", "1", "Medicina General", "CMP-65432")
        ),
        "Pediatría" to listOf(
            Medico("3", "Dr. Luis Paredes", "2", "Pediatría", "CMP-78901"),
            Medico("4", "Dra. Sofía Ramos", "2", "Pediatría", "CMP-21098")
        ),
        "Cardiología" to listOf(
            Medico("5", "Dr. Roberto Gómez", "3", "Cardiología", "CMP-34567"),
            Medico("6", "Dra. Elena Benítez", "3", "Cardiología", "CMP-87654")
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Doctores", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp)
        ) {
            doctoresPorEspecialidad.forEach { (especialidad, listaMedicos) ->
                item {
                    Text(
                        text = especialidad,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                items(listaMedicos) { medico ->
                    // ASIGNACIÓN DE IMÁGENES DESDE TU CARPETA DRAWABLE
                    val imagenDoctor = when (medico.id) {
                        "1" -> R.drawable.img
                        "2" -> R.drawable.img_1
                        "3" -> R.drawable.img_2
                        "4" -> R.drawable.img_3
                        "5" -> R.drawable.doc
                        "6" -> R.drawable.img
                        else -> R.drawable.doc
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FA))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // COMPONENTE IMAGE PARA MOSTRAR LAS FOTOS
                            Image(
                                painter = painterResource(id = imagenDoctor),
                                contentDescription = medico.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = medico.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = especialidad,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = medico.cmp,
                                    fontSize = 12.sp,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}