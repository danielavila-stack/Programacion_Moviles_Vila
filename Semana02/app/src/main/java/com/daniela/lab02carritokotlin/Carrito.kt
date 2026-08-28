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
    cantidad: Int,
    val pesoKg: Double
) : ItemCarrito(nombre, precio, cantidad) {

    override fun calcularSubtotal(): Double {
        val costoEnvio = pesoKg * 5.0
        return (precio * cantidad) + costoEnvio
    }
}

class ProductoDigital(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val esSuscripcion: Boolean
) : ItemCarrito(nombre, precio, cantidad) {

    override fun calcularSubtotal(): Double {
        val total = precio * cantidad
        return if (esSuscripcion) total * 0.90 else total
    }
}

fun main() {
    println("==================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("==================================")
    val nombreCliente = "Daniela Vila Ramos"
    val carrito: List<ItemCarrito> = listOf(
        ProductoFisico("Laptop HP", 2500.0, 1, 2.5),
        ProductoDigital("Licencia IntelliJ", 150.0, 1, true)
    )

    println("Cliente: $nombreCliente\n")

    for (item in carrito) {
        println("Producto : ${item.nombre}")
        println("Subtotal  : S/ ${item.calcularSubtotal()}")
        println("----------------------------------")
    }
}