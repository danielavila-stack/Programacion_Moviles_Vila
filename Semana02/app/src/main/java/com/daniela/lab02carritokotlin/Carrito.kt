package com.daniela.lab02carritokotlin

interface Vendible {
    val nombre: String
    val precio: Double
    fun calcularSubtotal(): Double
}

data class Producto(
    override val nombre: String,
    override val precio: Double,
    var cantidad: Int
) : Vendible {
    override fun calcularSubtotal(): Double = precio * cantidad
}

fun main() {
    println("==================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("==================================")
    val nombreCliente = "Daniela Vila Ramos"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("USB Kingston 64GB", 25.0, 3))

    for (item in carrito) {
        println("Producto : ${item.nombre}")
        println("Precio    : S/ ${item.precio}")
        println("Cantidad  : ${item.cantidad}")
        println("Subtotal  : S/ ${item.calcularSubtotal()}")
        println("----------------------------------")
    }
}