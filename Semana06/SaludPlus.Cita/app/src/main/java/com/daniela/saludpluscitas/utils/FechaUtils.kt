package com.daniela.saludpluscitas.utils

import java.time.DayOfWeek
import java.time.LocalDate

object FechaUtils {

    private val meses = listOf(
        "enero", "febrero", "marzo", "abril", "mayo", "junio",
        "julio", "agosto", "setiembre", "octubre", "noviembre", "diciembre"
    )

    private val dias = listOf(
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    )

    // Próximos 5 días hábiles (sin sábado ni domingo).
    // semanaOffset = 0 es la semana actual, 1 la siguiente, etc.
    fun diasHabiles(semanaOffset: Int, cantidad: Int = 5): List<LocalDate> {
        var fecha = LocalDate.now().plusWeeks(semanaOffset.toLong())
        val resultado = mutableListOf<LocalDate>()
        while (resultado.size < cantidad) {
            if (fecha.dayOfWeek != DayOfWeek.SATURDAY && fecha.dayOfWeek != DayOfWeek.SUNDAY) {
                resultado.add(fecha)
            }
            fecha = fecha.plusDays(1)
        }
        return resultado
    }

    // Ejemplo: "Octubre 2026"
    fun mesYAnio(fecha: LocalDate): String =
        meses[fecha.monthValue - 1].replaceFirstChar { it.uppercase() } + " " + fecha.year

    // Ejemplo: "LUN", "MAR", "MIÉ"
    fun diaCorto(fecha: LocalDate): String =
        dias[fecha.dayOfWeek.value - 1].take(3).uppercase()

    // Recibe "2026-09-16" y devuelve "Miércoles 16 de setiembre 2026"
    fun fechaLarga(iso: String): String = try {
        val f = LocalDate.parse(iso)
        "${dias[f.dayOfWeek.value - 1]} ${f.dayOfMonth} de ${meses[f.monthValue - 1]} ${f.year}"
    } catch (e: Exception) {
        iso
    }
}