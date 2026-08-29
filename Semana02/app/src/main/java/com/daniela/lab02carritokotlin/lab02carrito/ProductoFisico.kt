package com.daniela.lab02carritokotlin.lab02carrito

class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int
) : ItemCarrito(nombre, precio, cantidad) {
    override fun calcularSubtotal(): Double = precio * cantidad
}