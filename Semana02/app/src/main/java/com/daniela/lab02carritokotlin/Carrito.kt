package com.daniela.lab02carritokotlin

abstract class ItemCarrito(
    open val nombre: String,
    open val precio: Double,
    open var cantidad: Int
) {
    abstract fun calcularSubtotal(): Double
}

open class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val pesoKg: Double
) : ItemCarrito(nombre, precio, cantidad) {

    override fun calcularSubtotal(): Double {
        return precio * cantidad
    }
}

class ProductoImportado(
    nombre: String,
    precio: Double,
    cantidad: Int,
    pesoKg: Double,
    val arancel: Double
) : ProductoFisico(nombre, precio, cantidad, pesoKg) {

    override fun calcularSubtotal(): Double {
        val base = super.calcularSubtotal()
        return base + arancel
    }
}

fun main() {
    println("==================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("==================================")
    val nombreCliente = "Daniela Vila Ramos"
    val carrito = mutableListOf<ItemCarrito>()
    println("Cliente: $nombreCliente\n")

    carrito.add(ProductoFisico("Laptop HP", 2500.0, 1, 2.5))
    carrito.add(ProductoImportado("Teclado Mecanico", 300.0, 1, 0.8, 45.0))

    for (item in carrito) {
        println("Producto : ${item.nombre}")
        println("Subtotal  : S/ ${item.calcularSubtotal()}")
        println("----------------------------------")
    }
}