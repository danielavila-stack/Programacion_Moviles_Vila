package com.daniela.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun main() {
    println("====================================")
    println("CARRITO DE COMPRAS TIENDA TECSUP")
    println("====================================")
    val nombreCliente = "Daniela Vila Ramos"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("USB Kingston 64GB", 25.0, 3))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    println()


    mostrarDetalle(carrito)

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("%-22s: %d", "Cantidad de productos", carrito.size))
    println(String.format("%-22s: S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-22s: S/ %8.2f", "IGV (18%)", igv))
    println(String.format("%-22s: S/ %8.2f", "TOTAL A PAGAR", total))

    println()

    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/%.2f)", masCaro.precio))
    }

    //Aplicación de descuento con 'when'
    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    if (total > 5000) {
        println("Descuento aplicado: 10% por compra mayor a S/ 5000")
    } else if (total > 3000) {
        println("Descuento aplicado: 5% por compra mayor a S/ 3000")
    } else {
        println("Descuento aplicado: 0%")
    }

    println(String.format("TOTAL CON DESCUENTO : S/ %.2f", totalConDescuento))
    println()

    //reto adicional
    println("Gracias por su compra, $nombreCliente!")

    println("=========================================")
    println("            RETO ADICIONAL               ")
    println("=========================================")

    // 1. Buscar producto con find
    val productoBuscado = buscarProducto(carrito, "Mouse Logitech")
    if (productoBuscado != null) {
        println("Producto encontrado: ${productoBuscado.nombre} - Precio: S/ ${productoBuscado.precio}")
    } else {
        println("Producto no encontrado en el carrito.")
    }

    println()

    // 2. Eliminar producto con removeIf ("Mouse Logitech")
    val nombreAEliminar = "Mouse Logitech"
    println("Eliminando producto: $nombreAEliminar...")
    carrito.removeIf { it.nombre.equals(nombreAEliminar, ignoreCase = true) }

    println()

    // 3. Volver a mostrar el detalle y totales actualizados
    println("--- CARRITO ACTUALIZADO TRAS ELIMINACIÓN ---")
    mostrarDetalle(carrito)

    val subtotalActualizado = calcularSubtotal(carrito)
    val igvActualizado = calcularIGV(subtotalActualizado)
    val totalActualizado = calcularTotal(subtotalActualizado, igvActualizado)

    println(String.format("%-22s: %d", "Cantidad de productos", carrito.size))
    println(String.format("%-22s: S/ %8.2f", "Subtotal", subtotalActualizado))
    println(String.format("%-22s: S/ %8.2f", "IGV (18%)", igvActualizado))
    println(String.format("%-22s: S/ %8.2f", "TOTAL A PAGAR", totalActualizado))

    println()

}
fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}
fun mostrarDetalle(productos: List<Producto>) {
    println("---------------- DETALLE DEL CARRITO ----------------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d  S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("-----------------------------------------------------")
}
fun calcularSubtotal(productos:List<Producto>): Double {
    var subtotal = 0.0
    for(p in productos){
        subtotal += p.precio *   p.cantidad
    }
    return subtotal
}
fun calcularIGV(subtotal: Double): Double {
    // TODO: devuelve el 18% del subtotal
    var igv = 0.18
    return subtotal * igv
}
fun calcularTotal(subtotal: Double, igv: Double): Double {
    // TODO: devuelve la suma de ambos
    val precioTotal = subtotal + igv

    return precioTotal
}
fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}