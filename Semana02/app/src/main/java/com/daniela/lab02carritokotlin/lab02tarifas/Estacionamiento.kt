package com.daniela.lab02carritokotlin.lab02tarifas

fun Double.format(decimals: Int = 2): String = "%.${decimals}f".format(this)

fun main() {
    println("=== SISTEMA DE ESTACIONAMIENTO ===")

    print("Cuantos vehiculos desea procesar?: ")
    val cantidadVehiculos = readln().toIntOrNull() ?: 0

    for (i in 1..cantidadVehiculos) {
        println("\n------------------------------------------")
        println("       DATOS DEL VEHICULO $i DE $cantidadVehiculos")
        println("------------------------------------------")

        print("Nombre del cliente: ")
        val cliente = readln()

        print("Placa del vehiculo: ")
        val placa = readln()

        println("\nSeleccione Tipo de Vehiculo:")
        println("1. Moto (S/ 2.00)")
        println("2. Auto (S/ 4.00)")
        println("3. Camioneta (S/ 10.00)")
        println("4. Trailer (S/ 20.00)")
        print("Opcion: ")

        val entradaTipo = readln().lowercase().trim()

        val (tipoVehiculo, tarifaBasica) = when (entradaTipo) {
            "1", "moto" -> Pair("MOTO", 2.0)
            "2", "auto" -> Pair("AUTO", 4.0)
            "3", "camioneta" -> Pair("CAMIONETA", 10.0)
            "4", "trailer" -> Pair("TRAILER", 20.0)
            else -> Pair("DESCONOCIDO", 0.0)
        }

        print("Cantidad de horas: ")
        val horas = readln().toIntOrNull() ?: 0

        print("Es cliente frecuente? (s/n): ")
        val esFrecuenteStr = readln()
        val esFrecuente = esFrecuenteStr.equals("s", ignoreCase = true)

        val estadoCliente = if (esFrecuente) "Cliente Frecuente" else "Cliente Regular"

        // Imprimir recibo por vehiculo
        println("\n==========================================")
        println("--- RESUMEN ---")
        println("Cliente : $cliente")
        println("Placa   : $placa")
        println("Tipo    : $tipoVehiculo")
        println("Horas   : $horas")
        println("Estado  : $estadoCliente")
        println("------------------------------------------")
        println("TARIFA BASICA S/ ${tarifaBasica.format(2)}")

        // Cabecera alineada manualmente sin utilidades de Java
        println("HORA   TARIFA     RECARGO    IMPORTE")

        var totalPagar = 0.0

        for (hora in 1..horas) {
            val porcentajeRecargo = if (hora >= 3) 20 else 0
            val recargoMonto = tarifaBasica * (porcentajeRecargo / 100.0)
            val importeHora = tarifaBasica + recargoMonto
            totalPagar += importeHora

            val horaStr = hora.toString().padEnd(6)
            val tarifaStr = tarifaBasica.format(2).padEnd(10)
            val recargoStr = "$porcentajeRecargo%".padEnd(10)
            val importeStr = importeHora.format(2)

            println("$horaStr $tarifaStr $recargoStr $importeStr")
        }

        if (esFrecuente) {
            val descuento = totalPagar * 0.10
            totalPagar -= descuento
            println("------------------------------------------")
            println("Descuento frecuente (10%): -S/ ${descuento.format(2)}")
        }

        println("------------------------------------------")
        println("TOTAL    : S/ ${totalPagar.format(2)}")
        println("==========================================")
    }

    println("\nProcesamiento finalizado con exito!")
}