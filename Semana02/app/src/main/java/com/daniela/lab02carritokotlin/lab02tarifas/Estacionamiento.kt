package com.daniela.lab02carritokotlin

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    println("=== SISTEMA DE ESTACIONAMIENTO ===")

    print("Cuantos vehiculos desea procesar?: ")
    val cantidadVehiculos = scanner.nextInt()
    scanner.nextLine() // Limpiar el salto de linea del scanner

    for (i in 1..cantidadVehiculos) {
        println("\n------------------------------------------")
        println("       DATOS DEL VEHICULO $i DE $cantidadVehiculos")
        println("------------------------------------------")

        print("Nombre del cliente: ")
        val cliente = scanner.nextLine()

        print("Placa del vehiculo: ")
        val placa = scanner.nextLine()

        println("\nSeleccione Tipo de Vehiculo:")
        println("1. Moto (S/ 2.00)")
        println("2. Auto (S/ 4.00)")
        println("3. Camioneta (S/ 10.00)")
        print("Opcion: ")

        // Se lee como texto para evitar la excepcion InputMismatchException
        val entradaTipo = scanner.nextLine().lowercase().trim()

        val (tipoVehiculo, tarifaBasica) = when (entradaTipo) {
            "1", "moto" -> Pair("MOTO", 2.0)
            "2", "auto" -> Pair("AUTO", 4.0)
            "3", "camioneta" -> Pair("CAMIONETA", 10.0)
            else -> Pair("DESCONOCIDO", 0.0)
        }

        print("Cantidad de horas: ")
        val horas = scanner.nextInt()

        print("Es cliente frecuente? (s/n): ")
        val esFrecuenteStr = scanner.next()
        scanner.nextLine() // Limpiar el bufer de texto
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
        println("TARIFA BASICA S/ ${String.format("%.2f", tarifaBasica)}")
        println(String.format("%-6s %-10s %-10s %-10s", "HORA", "TARIFA", "RECARGO", "IMPORTE"))

        var totalPagar = 0.0

        for (hora in 1..horas) {
            val porcentajeRecargo = if (hora >= 3) 20 else 0
            val recargoMonto = tarifaBasica * (porcentajeRecargo / 100.0)
            val importeHora = tarifaBasica + recargoMonto
            totalPagar += importeHora

            println(String.format("%-6d %-10.2f %-10s %-10.2f", hora, tarifaBasica, "$porcentajeRecargo%", importeHora))
        }

        if (esFrecuente) {
            val descuento = totalPagar * 0.10
            totalPagar -= descuento
            println("------------------------------------------")
            println("Descuento frecuente (10%): -S/ ${String.format("%.2f", descuento)}")
        }

        println("------------------------------------------")
        println("TOTAL    : S/ ${String.format("%.2f", totalPagar)}")
        println("==========================================")
    }

    println("\nProcesamiento finalizado con exito!")
}