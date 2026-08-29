package com.daniela.lab02carritokotlin.lab02carrito

class GestorRecomendaciones {
    fun obtenerSugerencia(items: List<ItemCarrito>): String {
        return if (items.any { it.nombre.contains("Laptop", ignoreCase = true) }) {
            "Sugerencia: Agrega un Mouse Logitech o una funda con 15% de descuento"
        } else {
            "Sugerencia: Revisa las promociones destacadas del dia"
        }
    }

    fun calcularDescuentoPromocional(total: Double): Double {
        return if (total > 2000.0) total * 0.05 else 0.0
    }
}