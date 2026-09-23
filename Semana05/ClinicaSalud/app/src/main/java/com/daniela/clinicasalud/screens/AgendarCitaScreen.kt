package com.daniela.clinicasalud.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.clinicasalud.model.DateOption
import com.daniela.clinicasalud.model.LocalData
import com.daniela.clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(
    doctorId: Int,
    onBackClick: () -> Unit,
    onConfirmClick: (doctorName: String, fecha: String, hora: String) -> Unit
) {
    val doctor = LocalData.doctors.find { it.id == doctorId } ?: LocalData.doctors.first()

    // Selección única de fecha y hora con remember (SIN ViewModel)
    var selectedDateIndex by remember { mutableStateOf(1) } // "Vie 27" por defecto
    var selectedTimeIndex by remember { mutableStateOf(1) } // "10:30" por defecto

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agendar cita",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextDark,
                    navigationIconContentColor = TextDark
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Selecciona fecha",
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 28.dp)
            ) {
                items(LocalData.datesAvailable.size) { index ->
                    val dateObj = LocalData.datesAvailable[index]
                    val isSelected = index == selectedDateIndex

                    DateChip(
                        dateOption = dateObj,
                        isSelected = isSelected,
                        onClick = { selectedDateIndex = index }
                    )
                }
            }

            Text(
                text = "Selecciona hora",
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(LocalData.timesAvailable.size) { index ->
                    val timeText = LocalData.timesAvailable[index]
                    val isSelected = index == selectedTimeIndex

                    TimeChip(
                        timeText = timeText,
                        isSelected = isSelected,
                        onClick = { selectedTimeIndex = index }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val dateObj = LocalData.datesAvailable[selectedDateIndex]
                    val dateFormatted = "${dateObj.dayOfWeek} ${dateObj.dayNumber}"
                    val timeFormatted = LocalData.timesAvailable[selectedTimeIndex]
                    onConfirmClick(doctor.name, dateFormatted, timeFormatted)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Confirmar cita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DateChip(
    dateOption: DateOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(68.dp)
            .height(72.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PurplePrimary else PurpleChipUnselected
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dateOption.dayOfWeek,
                fontSize = 12.sp,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextGray
            )
            Text(
                text = dateOption.dayNumber,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else TextDark
            )
        }
    }
}

@Composable
fun TimeChip(
    timeText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(84.dp)
            .height(44.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PurplePrimary else PurpleChipUnselected
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = timeText,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextDark
            )
        }
    }
}