package com.daniela.lab02carritokotlin.lab02tarifas

fun Double.format(decimals: Int = 2): String = "%.${decimals}f".format(this)

fun main() {
    println("=== CONFIGURACIÓN DEL SISTEMA DE ESTACIONAMIENTO ===")
    print("Administrador, ingrese el aforo máximo del estacionamiento: ")
    val aforoMaximo = readln().toIntOrNull() ?: 10

    var vehiculosEstacionados = 0
    var opcionMenu = 0

    println("\n[SISTEMA INICIADO] Aforo establecido en: $aforoMaximo vehículos")

    while (opcionMenu != 4) {
        println("\n==========================================")
        println("       SISTEMA DE ESTACIONAMIENTO")
        println("   Aforo: $vehiculosEstacionados / $aforoMaximo vehículos ocupados")
        println("==========================================")
        println("1. Registrar ingreso de vehículos")
        println("2. Registrar salida de vehículo(s)")
        println("3. Consultar disponibilidad de aforo")
        println("4. Salir")
        print("Seleccione una opción: ")

        opcionMenu = readln().toIntOrNull() ?: 0

        when (opcionMenu) {
            1 -> {
                val espaciosDisponibles = aforoMaximo - vehiculosEstacionados

                if (espaciosDisponibles <= 0) {
                    println("\n[ALERTA] ¡AFORO COMPLETO! No hay espacios disponibles en el estacionamiento.")
                } else {
                    println("\nEspacios disponibles: $espaciosDisponibles")
                    print("¿Cuántos vehículos van a ingresar?: ")
                    val cantidadIngreso = readln().toIntOrNull() ?: 0

                    if (cantidadIngreso <= 0) {
                        println("[ERROR] Ingrese un número de vehículos válido.")
                    } else if (cantidadIngreso > espaciosDisponibles) {
                        println("\n[ERROR] No se puede registrar $cantidadIngreso vehículo(s).")
                        println("Supera el límite disponible. Solo hay lugar para $espaciosDisponibles vehículo(s).")
                    } else {
                        for (i in 1..cantidadIngreso) {
                            vehiculosEstacionados++
                            println("\n------------------------------------------")
                            println("       DATOS DEL VEHICULO $i DE $cantidadIngreso")
                            println("       (Ocupación actual: $vehiculosEstacionados de $aforoMaximo)")
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

                            // Impresión de Boleta / Recibo
                            println("\n==========================================")
                            println("            BOLETA DE VENTA               ")
                            println("==========================================")
                            println("Cliente : $cliente")
                            println("Placa   : $placa")
                            println("Tipo    : $tipoVehiculo")
                            println("Horas   : $horas")
                            println("Estado  : $estadoCliente")
                            println("------------------------------------------")
                            println("TARIFA BASICA S/ ${tarifaBasica.format(2)}")

                            println("HORA   TARIFA     RECARGO    IMPORTE")

                            var totalPagar = 0.0

                            for (hora in 1..horas) {
                                val porcentajeRecargo = when (hora) {
                                    in 1..2 -> 0
                                    in 3..5 -> 20
                                    in 6..10 -> 40
                                    else -> 50
                                }
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
                                val descuentoFrecuente = totalPagar * 0.10
                                totalPagar -= descuentoFrecuente
                                println("------------------------------------------")
                                println("Descuento frecuente (10%)        : -S/ ${descuentoFrecuente.format(2)}")
                            }

                            if (totalPagar > 500.0) {
                                val descuentoMonto = totalPagar * 0.20
                                totalPagar -= descuentoMonto
                                println("Descuento por consumo > S/ 500 (20%): -S/ ${descuentoMonto.format(2)}")
                            }

                            val igv = totalPagar * 0.18
                            val totalConIgv = totalPagar + igv

                            println("------------------------------------------")
                            println("SUBTOTAL                         : S/ ${totalPagar.format(2)}")
                            println("IGV (18%)                        : S/ ${igv.format(2)}")
                            println("TOTAL A PAGAR                    : S/ ${totalConIgv.format(2)}")
                            println("==========================================")

                            // Muestra de espacio disponible en tiempo real tras generar la boleta
                            val libres = aforoMaximo - vehiculosEstacionados
                            println(">>> ESPACIOS LIBRES EN ESTACIONAMIENTO: $libres de $aforoMaximo <<<")
                            println("==========================================\n")
                        }
                    }
                }
            }

            2 -> {
                if (vehiculosEstacionados == 0) {
                    println("\n[INFO] El estacionamiento está vacío, no hay vehículos para retirar.")
                } else {
                    print("\n¿Cuántos vehículos se retiran del estacionamiento?: ")
                    val salida = readln().toIntOrNull() ?: 0

                    if (salida <= 0) {
                        println("[ERROR] Ingrese un número válido mayor a 0.")
                    } else if (salida > vehiculosEstacionados) {
                        println("[ERROR] No puede retirar $salida vehículos. Solo hay $vehiculosEstacionados en el estacionamiento.")
                    } else {
                        vehiculosEstacionados -= salida
                        val libres = aforoMaximo - vehiculosEstacionados
                        println("\n[OK] Se retiraron $salida vehículo(s).")
                        println(" Espacios ocupados: $vehiculosEstacionados | Espacios libres: $libres")
                    }
                }
            }

            3 -> {
                val disponibles = aforoMaximo - vehiculosEstacionados
                println("\n--- ESTADO DEL AFORO ---")
                println("Aforo Definido por Administrador : $aforoMaximo")
                println("Vehículos Ocupados               : $vehiculosEstacionados")
                println("Espacios Disponibles             : $disponibles")
                if (disponibles == 0) {
                    println("Estado                           : ¡ESTACIONAMIENTO LLENO!")
                } else {
                    println("Estado                           : HAY ESPACIO DISPONIBLE")
                }
            }

            4 -> println("\nGracias por usar el sistema de estacionamiento. ¡Hasta luego!")
            else -> println("\n[ERROR] Opción no válida. Elija una opción del 1 al 4.")
        }
    }
}