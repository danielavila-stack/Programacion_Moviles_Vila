package com.tecsup.huellitasvet.model

// Razas que se pueden elegir al registrar una mascota
object Razas {
    val PERRO = listOf(
        "Labrador", "Golden Retriever", "Pastor Alemán", "Bulldog", "Poodle",
        "Chihuahua", "Beagle", "Schnauzer", "Shih Tzu", "Mestizo"
    )
    val GATO = listOf(
        "Siamés", "Persa", "Maine Coon", "Angora", "Bengalí",
        "Ragdoll", "Sphynx", "Británico de pelo corto", "Mestizo"
    )

    // Lista vacía = raza libre (especie Otro)
    fun de(especie: Especie?): List<String> = when (especie) {
        Especie.PERRO -> PERRO
        Especie.GATO -> GATO
        else -> emptyList()
    }
}
