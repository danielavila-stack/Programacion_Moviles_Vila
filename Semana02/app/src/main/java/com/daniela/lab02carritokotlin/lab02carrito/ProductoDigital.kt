package com.daniela.lab02carritokotlin.lab02carrito

class ProductoDigital(
    nombre: String,
    precio: Double,
    cantidad: Int
) : ItemCarrito(nombre, precio, cantidad) {
    override fun calcularSubtotal(): Double = precio * cantidad
}