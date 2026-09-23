package com.daniela.clinicasalud.model

// Modelo de datos para los médicos
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviewsCount: Int,
    val experienceYears: Int,
    val description: String
)

// Modelo para los botones/chips de fecha
data class DateOption(
    val dayOfWeek: String,
    val dayNumber: String
)

// Modelo para la lista de mis citas
data class Appointment(
    val id: Int,
    val doctorName: String,
    val dateInfo: String,
    val status: String // "Confirmada" o "Completada"
)

// Datos iniciales para alimentar las vistas
object LocalData {
    val specialties = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")

    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dra. Ana Torres",
            specialty = "Cardióloga",
            rating = 4.9,
            reviewsCount = 128,
            experienceYears = 12,
            description = "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."
        ),
        Doctor(
            id = 2,
            name = "Dr. Luis Vega",
            specialty = "Pediatra",
            rating = 4.7,
            reviewsCount = 95,
            experienceYears = 8,
            description = "Especialista en salud infantil y desarrollo de la infancia."
        ),
        Doctor(
            id = 3,
            name = "Dra. Rosa Díaz",
            specialty = "Dermatóloga",
            rating = 4.8,
            reviewsCount = 110,
            experienceYears = 10,
            description = "Especialista en dermatología clínica y cuidado cutáneo."
        )
    )

    val datesAvailable = listOf(
        DateOption("Jue", "26"),
        DateOption("Vie", "27"),
        DateOption("Sáb", "28")
    )

    val timesAvailable = listOf("9:00", "10:30", "3:00")

    val initialAppointments = mutableListOf(
        Appointment(1, "Dra. Ana Torres", "Viernes 27, 10:30 am", "Confirmada"),
        Appointment(2, "Dr. Luis Vega", "Miércoles 15, 3:00 pm", "Completada")
    )
}