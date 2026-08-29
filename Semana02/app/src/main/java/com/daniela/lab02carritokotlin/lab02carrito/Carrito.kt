package com.daniela.lab02carritokotlin.lab02carrito

fun main() {
    val nombreCliente = "Daniela Vila Ramos"

    println("==================================")
    println("  CARRITO DE COMPRAS - TIENDA TECSUP")
    println("==================================")
    println("Cliente: $nombreCliente\n")

    val listaProductos = listOf<ItemCarrito>(
        ProductoFisico("Laptop HP", 2505.00, 1),
        ProductoFisico("Mouse Logitech", 45.70, 2),
        ProductoFisico("Audifonos Sony", 121.00, 1),
        ProductoFisico("USB Kingston 64GB", 25.00, 3)
    )

    for (prod in listaProductos) {
        println("Producto agregado: ${prod.nombre}")
    }

    println("\n-------- DETALLE DEL CARRITO --------")
    var subtotal = 0.0
    var productoMasCaro: ItemCarrito? = null

    for ((index, prod) in listaProductos.withIndex()) {
        val subtotalItem = prod.calcularSubtotal()
        subtotal += subtotalItem

        if (productoMasCaro == null || prod.precio > productoMasCaro.precio) {
            productoMasCaro = prod
        }

        val formatoNombre = prod.nombre.padEnd(23, ' ')
        val subtotalFormateado = String.format("%.2f", subtotalItem)
        println("${index + 1}. $formatoNombre x${prod.cantidad} S/ $subtotalFormateado")
    }

    val igv = subtotal * 0.18
    val totalAPagar = subtotal + igv

    println("------------------------------------")
    println("Cantidad de productos : ${listaProductos.size}")
    println("Subtotal             : S/ ${String.format("%.2f", subtotal)}")
    println("IGV (18%)            : S/ ${String.format("%.2f", igv)}")
    println("TOTAL A PAGAR        : S/ ${String.format("%.2f", totalAPagar)}")
    println("------------------------------------")

    if (productoMasCaro != null) {
        println("Producto mas caro: ${productoMasCaro.nombre} (S/ ${String.format("%.2f", productoMasCaro.precio)})")
    }

    val gestor = GestorRecomendaciones()
    val descPromo = gestor.calcularDescuentoPromocional(totalAPagar)
    val totalConDescuento = totalAPagar - descPromo

    println("Descuento aplicado: 5% por compra mayor a S/ 3000")
    println("TOTAL CON DESCUENTO  : S/ ${String.format("%.2f", totalConDescuento)}")

    println("\n${gestor.obtenerSugerencia(listaProductos)}")
    println("\nGracias por su compra, $nombreCliente!")
}