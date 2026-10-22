package com.tecsup.huellitasvet.model

import java.time.LocalTime

// Genera los 18 horarios del día: de 09:00 a 17:30 cada 30 minutos.
object Horarios {
    val INICIO: LocalTime = LocalTime.of(9, 0)
    val FIN: LocalTime = LocalTime.of(17, 30)
    const val INTERVALO_MIN = 30L

    fun delDia(): List<String> =
        generateSequence(INICIO) { it.plusMinutes(INTERVALO_MIN) }
            .takeWhile { !it.isAfter(FIN) }
            .map { it.toString() } // "HH:mm"
            .toList()
}
