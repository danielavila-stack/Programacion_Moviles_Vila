package com.daniela.lab02carritokotlin

abstract class ItemCarrito(
    open val nombre: String,
    open val precio: Double,
    open var cantidad: Int
) {
    abstract fun calcularSubtotal(): Double
}

class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int
) : ItemCarrito(nombre, precio, cantidad) {
    override fun calcularSubtotal(): Double = precio * cantidad
}

class ProductoDigital(
    nombre: String,
    precio: Double,
    cantidad: Int
) : ItemCarrito(nombre, precio, cantidad) {
    override fun calcularSubtotal(): Double = precio * cantidad
}

class GestorRecomendaciones {
    fun obtenerSugerencia(items: List<ItemCarrito>): String {
        return if (items.any { it.nombre.contains("Laptop", ignoreCase = true) }) {
            "Sugerencia: Agrega un Mouse Logitech o una funda con 15% de descuento"
        } else {
            "Sugerencia: Revisa las promociones destacadas del dia"
        }
    }
}

fun main() {
    println("==================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("==================================")
    val carrito = listOf<ItemCarrito>(
        ProductoFisico("Laptop HP", 2500.0, 1)
    )
    val recomendador = GestorRecomendaciones()

    for (item in carrito) {
        println("Producto : ${item.nombre}")
        println("Subtotal  : S/ ${item.calcularSubtotal()}")
    }
    println("\n${recomendador.obtenerSugerencia(carrito)}")
}