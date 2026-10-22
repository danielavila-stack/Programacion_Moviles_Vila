package com.tecsup.huellitasvet.model

// Especie de la mascota. Se guarda en la tabla como Especie.name
enum class Especie(val etiqueta: String) {
    PERRO("Perro"),
    GATO("Gato"),
    OTRO("Otro");

    companion object {
        fun desde(valor: String): Especie = entries.firstOrNull { it.name == valor } ?: OTRO
    }
}
