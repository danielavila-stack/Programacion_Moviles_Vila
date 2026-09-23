package com.daniela.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.tecsupfit.data.DatosMock
import com.daniela.tecsupfit.ui.theme.TecsupPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    claseId: Int,
    onVolver: () -> Unit,
    onReservar: (Int) -> Unit
) {
    val clase = DatosMock.listaClases.find { it.id == claseId }
    var esFavorito by remember { mutableStateOf(clase?.esFavorito ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            esFavorito = !esFavorito
                            clase?.esFavorito = esFavorito
                        }
                    ) {
                        if (esFavorito) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Favorito",
                                tint = Color.Red
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorito"
                            )
                        }
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
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = TecsupPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = clase?.nombre ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${clase?.hora ?: ""} · ${clase?.sala ?: ""} · ${clase?.duracion ?: ""}",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                Text(
                    text = clase?.descripcion ?: "",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "${clase?.cuposDisponibles ?: 0} de ${clase?.cuposTotales ?: 12} cupos disponibles",
                    fontWeight = FontWeight.SemiBold,
                    color = TecsupPrimary,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = {
                    clase?.let { onReservar(it.id) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TecsupPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reservar cupo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}