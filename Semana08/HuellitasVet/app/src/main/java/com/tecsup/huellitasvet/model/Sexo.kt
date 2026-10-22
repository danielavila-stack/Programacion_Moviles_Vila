package com.tecsup.huellitasvet.model

// Sexo de la mascota. Se guarda en la tabla como Sexo.name
enum class Sexo(val etiqueta: String) {
    MACHO("Macho"),
    HEMBRA("Hembra");

    companion object {
        fun desde(valor: String): Sexo = entries.firstOrNull { it.name == valor } ?: MACHO
    }
}
