package com.tecsup.huellitasvet.ui.components

import java.time.LocalDate
import java.util.Locale

// Formatos de texto en español para mostrar fechas, precios y edades.
private val DIAS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
private val MESES = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "set", "oct", "nov", "dic")

fun diaSemanaCorto(fecha: LocalDate): String = DIAS[fecha.dayOfWeek.value - 1]

// "21 oct"
fun diaMes(fecha: String): String {
    val f = LocalDate.parse(fecha)
    return "${f.dayOfMonth} ${MESES[f.monthValue - 1]}"
}

// "Mié 21 oct"
fun fechaCorta(fecha: String): String = "${diaSemanaCorto(LocalDate.parse(fecha))} ${diaMes(fecha)}"

// "Mié 21 oct 2026"
fun fechaLarga(fecha: String): String = "${fechaCorta(fecha)} ${LocalDate.parse(fecha).year}"

fun soles(monto: Double): String = "S/ " + String.format(Locale.US, "%,.2f", monto)

// "Daniela Fransua" -> "Daniela F."
fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size < 2) nombre else "${partes[0]} ${partes[1].first()}."
}

fun kilos(peso: Double): String = String.format(Locale.US, "%.1f kg", peso)

fun textoEdad(anios: Int): String = when (anios) {
    0 -> "Menos de 1 año"
    1 -> "1 año"
    else -> "$anios años"
}
