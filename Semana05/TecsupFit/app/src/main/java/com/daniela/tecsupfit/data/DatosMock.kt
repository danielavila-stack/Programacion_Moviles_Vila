package com.daniela.tecsupfit.data

import androidx.compose.runtime.mutableStateListOf
import com.daniela.tecsupfit.model.Clase
import com.daniela.tecsupfit.model.Reserva
import com.daniela.tecsupfit.model.Rutina

object DatosMock {
    val listaClases = listOf(
        Clase(
            id = 1,
            nombre = "Yoga funcional",
            hora = "7:00 am",
            sala = "Sala 2",
            duracion = "45 min",
            cuposDisponibles = 5,
            cuposTotales = 10,
            descripcion = "Sesión enfocada en postura, movilidad y respiración guiada."
        ),
        Clase(
            id = 2,
            nombre = "Cross Training",
            hora = "6:00 pm",
            sala = "Sala 1",
            duracion = "45 min",
            cuposDisponibles = 8,
            cuposTotales = 12,
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados."
        ),
        Clase(
            id = 3,
            nombre = "Spinning",
            hora = "7:30 pm",
            sala = "Sala 3",
            duracion = "45 min",
            cuposDisponibles = 4,
            cuposTotales = 15,
            descripcion = "Ejercicio cardiovascular sobre bicicleta estática con ritmo dinámico."
        )
    )

    val listaRutinas = listOf(
        Rutina(1, "Cardio Quema Calorías", "45 min", "Intermedio", "Rutina cardiovascular de alta quema calórica."),
        Rutina(2, "Fuerza Muscular Completa", "60 min", "Avanzado", "Entrenamiento enfocado en hipertrofia y fuerza."),
        Rutina(3, "Estiramiento y Movilidad", "30 min", "Principiante", "Ejercicios suaves para articulaciones y flexibilidad.")
    )

    // Reservas iniciales que coinciden con la captura
    val reservasAgendadas = mutableStateListOf(
        Reserva(
            id = 1,
            clase = listaClases[1], // Cross Training
            fechaHora = "Hoy, 6:00 pm",
            estado = "Confirmada"
        ),
        Reserva(
            id = 2,
            clase = listaClases[0], // Yoga funcional
            fechaHora = "Ayer, 7:00 am",
            estado = "Completada"
        )
    )
}