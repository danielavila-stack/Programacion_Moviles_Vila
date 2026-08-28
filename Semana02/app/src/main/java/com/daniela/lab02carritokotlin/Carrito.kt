package com.daniela.lab02carritokotlin

abstract class ItemCarrito(
    open val nombre: String,
    open val precio: Double,
    open var cantidad: Int
) {
    abstract fun calcularSubtotal(): Double
}

class ProductoConDescuento(
    nombre: String,
    precio: Double,
    cantidad: Int,
    private val porcentajeDescuento: Double = 0.0
) : ItemCarrito(nombre, precio, cantidad) {

    private fun calcularDescuento(): Double {
        return (precio * cantidad) * (porcentajeDescuento / 100)
    }

    override fun calcularSubtotal(): Double {
        return (precio * cantidad) - calcularDescuento()
    }
}

fun main() {
    println("==================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("==================================")
    val nombreCliente = "Daniela Vila Ramos"
    val carrito = mutableListOf<ItemCarrito>()
    println("Cliente: $nombreCliente\n")

    carrito.add(ProductoConDescuento("Laptop HP", 2500.0, 1, 10.0))
    carrito.add(ProductoConDescuento("Mouse Logitech", 45.5, 2))

    for (item in carrito) {
        println("Producto : ${item.nombre}")
        println("Subtotal  : S/ ${item.calcularSubtotal()}")
        println("----------------------------------")
    }
}