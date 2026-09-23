package com.daniela.navlab_05.models

data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val email: String,
    val faculty: String,
    val bio: String,
    val studentCode: String = "2024-${id.toString().padStart(4, '0')}",
    val cycle: String = "4to Ciclo"
)

val sampleStudents = listOf(
    Student(
        id = 1,
        name = "Juan León",
        career = "Ingeniería de Software",
        email = "juan.leon@example.com",
        faculty = "Ingeniería y Tecnología",
        bio = "Estudiante apasionado por la programación móvil, desarrollo frontend con Jetpack Compose y arquitecturas modernas en Android.",
        studentCode = "2024-0001",
        cycle = "4to Ciclo"
    ),
    Student(
        id = 2,
        name = "Maria Garcia",
        career = "Ingeniería de Sistemas",
        email = "maria.garcia@example.com",
        faculty = "Ingeniería y Tecnología",
        bio = "Especialista en gestión de bases de datos, análisis de requerimientos y gestión de proyectos ágiles.",
        studentCode = "2024-0002",
        cycle = "5to Ciclo"
    ),
    Student(
        id = 3,
        name = "Carlos Perez",
        career = "Ciencias de la Computación",
        email = "carlos.perez@example.com",
        faculty = "Ingeniería y Tecnología",
        bio = "Interesado en inteligencia artificial, estructuras de datos avanzadas y desarrollo de algoritmos de optimización.",
        studentCode = "2024-0003",
        cycle = "3er Ciclo"
    ),
    Student(
        id = 4,
        name = "Ana Lopez",
        career = "Ingeniería de Software",
        email = "ana.lopez@example.com",
        faculty = "Ingeniería y Tecnología",
        bio = "Enfocada en diseño de interfaces UX/UI y desarrollo de componentes reutilizables en Kotlin.",
        studentCode = "2024-0004",
        cycle = "6to Ciclo"
    ),
    Student(
        id = 5,
        name = "Luis Ramirez",
        career = "Sistemas de Información",
        email = "luis.ramirez@example.com",
        faculty = "Ingeniería y Tecnología",
        bio = "Apasionado por la ciberseguridad, redes informáticas y auditoría de sistemas de información.",
        studentCode = "2024-0005",
        cycle = "4to Ciclo"
    )
)
