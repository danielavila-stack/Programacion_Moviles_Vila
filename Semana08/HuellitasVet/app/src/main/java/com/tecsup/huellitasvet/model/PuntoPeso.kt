package com.tecsup.huellitasvet.model

// Un punto del gráfico de peso: el peso registrado en una cita ATENDIDA.
// Los nombres de los campos deben coincidir con los alias del SELECT.
data class PuntoPeso(
    val fecha: String,
    val peso: Double
)
