package com.daniela.clinicasalud.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniela.clinicasalud.model.Appointment
import com.daniela.clinicasalud.model.LocalData
import com.daniela.clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    onOpenDrawer: () -> Unit
) {
    val appointments = remember { LocalData.initialAppointments }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis citas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú lateral",
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = appointments,
                key = { it.id }
            ) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onCancelAppointment = { apptToCancel ->
                        val index = appointments.indexOfFirst { it.id == apptToCancel.id }
                        if (index != -1) {
                            appointments[index] = apptToCancel.copy(status = "Cancelada")
                        }
                    },
                    onDeleteAppointment = { apptToDelete ->
                        appointments.remove(apptToDelete)
                    }
                )
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onCancelAppointment: (Appointment) -> Unit = {},
    onDeleteAppointment: (Appointment) -> Unit = {}
) {
    var showCancelDialog by remember { mutableStateOf(false) }

    val isConfirmed = appointment.status == "Confirmada"
    val isCancelled = appointment.status == "Cancelada"
    val isCompleted = appointment.status == "Completada"
    val isRemovable = isCancelled || isCompleted

    val badgeBgColor = when {
        isConfirmed -> StatusGreenBg
        isCancelled -> Color(0xFFFFEBEE)
        else -> PurpleChipUnselected
    }

    val badgeTextColor = when {
        isConfirmed -> StatusGreen
        isCancelled -> Color(0xFFD32F2F)
        else -> TextGray
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Text(
                    text = "Cancelar cita",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cancelar tu cita con ${appointment.doctorName}?",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onCancelAppointment(appointment)
                        showCancelDialog = false
                    }
                ) {
                    Text(
                        text = "Sí, cancelar",
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelDialog = false }
                ) {
                    Text(
                        text = "Cancelar",
                        color = TextGray
                    )
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Indicador vertical izquierdo
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        when {
                            isConfirmed -> PurplePrimary
                            isCancelled -> Color(0xFFD32F2F)
                            else -> Color.Transparent
                        }
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = appointment.doctorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = appointment.dateInfo,
                    fontSize = 13.sp,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge de estado (Confirmada / Cancelada / Completada)
                    Surface(
                        color = badgeBgColor,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = appointment.status,
                            color = badgeTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    // Botón 'Cancelar cita' solo si está 'Confirmada'
                    if (isConfirmed) {
                        OutlinedButton(
                            onClick = { showCancelDialog = true },
                            border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFD32F2F)
                            )
                        ) {
                            Text(
                                text = "Cancelar cita",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Botón de papelera para eliminar cita si está 'Cancelada' o 'Completada'
                    if (isRemovable) {
                        IconButton(
                            onClick = { onDeleteAppointment(appointment) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar cita",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }
        }
    }
}