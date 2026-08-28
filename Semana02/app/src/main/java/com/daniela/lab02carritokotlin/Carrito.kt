package com.daniela.lab02carritokotlin

interface Vendible {
    val nombre: String
    val precio: Double
    fun calcularSubtotal(): Double
}

abstract class Descuentable(
    override val nombre: String,
    override val precio: Double,
    open var cantidad: Int
) : Vendible {
    abstract fun aplicarDescuento(): Double
}

class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val porcentajeDescuento: Double = 0.0
) : Descuentable(nombre, precio, cantidad) {

    override fun aplicarDescuento(): Double {
        val totalSinDescuento = precio * cantidad
        return totalSinDescuento * (porcentajeDescuento / 100)
    }

    override fun calcularSubtotal(): Double {
        return (precio * cantidad) - aplicarDescuento()
    }
}

fun main() {
    println("==================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("==================================")
    val nombreCliente = "Daniela Vila Ramos"
    val carrito = mutableListOf<ProductoFisico>()
    println("Cliente: $nombreCliente")
    println()

    carrito.add(ProductoFisico("Laptop HP", 2500.0, 1, 10.0))
    carrito.add(ProductoFisico("Mouse Logitech", 45.5, 2))
    carrito.add(ProductoFisico("Audifonos Sony", 120.0, 1, 5.0))
    carrito.add(ProductoFisico("USB Kingston 64GB", 25.0, 3))

    for (item in carrito) {
        println("Producto : ${item.nombre}")
        println("Precio    : S/ ${item.precio}")
        println("Cantidad  : ${item.cantidad}")
        println("Subtotal  : S/ ${item.calcularSubtotal()}")
        println("----------------------------------")
    }
}