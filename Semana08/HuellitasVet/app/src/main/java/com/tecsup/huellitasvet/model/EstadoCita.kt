package com.tecsup.huellitasvet.model

// Estados de una cita. Se guarda en la tabla como EstadoCita.name
enum class EstadoCita(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    ATENDIDA("Atendida"),
    CANCELADA("Cancelada");

    companion object {
        fun desde(valor: String): EstadoCita = entries.firstOrNull { it.name == valor } ?: PENDIENTE
    }
}
