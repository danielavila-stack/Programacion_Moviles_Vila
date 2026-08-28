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

    fun calcularDescuentoPromocional(total: Double): Double {
        return if (total > 2000.0) total * 0.05 else 0.0
    }
}

fun main() {
    println("==================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("==================================")
    val nombreCliente = "Daniela Vila Ramos"
    val carrito = listOf<ItemCarrito>(
        ProductoFisico("Laptop HP", 2500.0, 1),
        ProductoFisico("Mouse Logitech", 45.5, 2),
        ProductoDigital("Licencia IntelliJ", 150.0, 1)
    )
    val gestorPromociones = GestorRecomendaciones()

    println("Cliente: $nombreCliente\n")

    var subtotalGeneral = 0.0
    for (item in carrito) {
        val st = item.calcularSubtotal()
        subtotalGeneral += st
        println("Producto : ${item.nombre}")
        println("Subtotal  : S/ $st")
        println("----------------------------------")
    }

    val descPromo = gestorPromociones.calcularDescuentoPromocional(subtotalGeneral)
    val totalFinal = subtotalGeneral - descPromo

    println("Subtotal           : S/ $subtotalGeneral")
    println("Descuento Especial : S/ $descPromo")
    println("TOTAL A PAGAR      : S/ $totalFinal")
    println("\n${gestorPromociones.obtenerSugerencia(carrito)}")
}