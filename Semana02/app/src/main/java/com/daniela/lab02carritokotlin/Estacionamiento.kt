package com.daniela.lab02carritokotlin

import kotlin.math.max

enum class TipoVehiculo(val tarifaBase: Double) {
    MOTO(2.0),
    AUTO(4.0),
    CAMIONETA(10.0)
}

data class RegistroEstacionamiento(
    val cliente: String,
    val placa: String,
    val tipo: TipoVehiculo,
    val horasIngresadas: Int,
    val esClienteFrecuente: Boolean
) {
    val horasEfectivas: Int = max(1, horasIngresadas)
}

data class DetalleHora(
    val hora: Int,
    val tarifaBase: Double,
    val porcentajeRecargo: Double,
    val importe: Double
)

data class ResultadoCalculo(
    val registro: RegistroEstacionamiento,
    val detalles: List<DetalleHora>,
    val subtotal: Double,
    val descuento: Double,
    val total: Double
)

class CalculadorEstacionamiento {

    fun procesar(registro: RegistroEstacionamiento): ResultadoCalculo {
        val detalles = mutableListOf<DetalleHora>()
        var subtotal = 0.0

        for (h in 1..registro.horasEfectivas) {
            val porcentajeRecargo = when {
                h <= 2 -> 0.0
                h <= 5 -> 0.20
                else -> 0.50
            }

            val importeHora = registro.tipo.tarifaBase * (1 + porcentajeRecargo)
            subtotal += importeHora

            detalles.add(
                DetalleHora(
                    hora = h,
                    tarifaBase = registro.tipo.tarifaBase,
                    porcentajeRecargo = porcentajeRecargo * 100,
                    importe = importeHora
                )
            )
        }

        val descuento = if (registro.esClienteFrecuente) subtotal * 0.10 else 0.0
        val total = subtotal - descuento

        return ResultadoCalculo(registro, detalles, subtotal, descuento, total)
    }
}

// Clase para formatear e imprimir la boleta de cobro
class ReporteEstacionamiento {

    fun imprimirBoleta(resultado: ResultadoCalculo) {
        val reg = resultado.registro

        println("\n==========================================")
        println("Cliente : ${reg.cliente}")
        println("Placa   : ${reg.placa}")
        println("Tipo    : ${reg.tipo.name}")
        println("Horas   : ${reg.horasEfectivas}")
        println("Estado  : ${if (reg.esClienteFrecuente) "Cliente Frecuente (10% Desc.)" else "Cliente Regular"}")
        println("------------------------------------------")
        println("TARIFA BÁSICA S/ ${String.format("%.2f", reg.tipo.tarifaBase)}")
        println("HORA\tTARIFA\tRECARGO\tIMPORTE")

        for (d in resultado.detalles) {
            println("${d.hora}\t${String.format("%.2f", d.tarifaBase)}\t${d.porcentajeRecargo.toInt()}%\t${String.format("%.2f", d.importe)}")
        }

        println("------------------------------------------")
        if (reg.esClienteFrecuente) {
            println("Subtotal  : S/ ${String.format("%.2f", resultado.subtotal)}")
            println("Descuento : -S/ ${String.format("%.2f", resultado.descuento)}")
        }
        println("TOTAL     : S/ ${String.format("%.2f", resultado.total)}")
        println("==========================================")
    }
}

fun main() {
    println("=== REGISTRO DE ESTACIONAMIENTO ===")

    print("Ingrese el nombre del cliente: ")
    val cliente = readLine().orEmpty()

    print("Ingrese la placa del vehículo: ")
    val placa = readLine().orEmpty()

    println("\nSeleccione el tipo de vehículo:")
    println("1. Moto (S/ 2.00)")
    println("2. Auto (S/ 4.00)")
    println("3. Camioneta (S/ 10.00)")
    print("Opción (1-3): ")
    val opcionTipo = readLine()?.toIntOrNull() ?: 2

    val tipo = when (opcionTipo) {
        1 -> TipoVehiculo.MOTO
        3 -> TipoVehiculo.CAMIONETA
        else -> TipoVehiculo.AUTO
    }

    print("Ingrese la cantidad de horas: ")
    val horasIngresadas = readLine()?.toIntOrNull() ?: 1

    print("¿Es cliente frecuente? (s/n): ")
    val esFrecuenteInput = readLine().orEmpty().lowercase()
    val esClienteFrecuente = esFrecuenteInput == "s" || esFrecuenteInput == "si"

    // 1. Ingreso
    val registro = RegistroEstacionamiento(cliente, placa, tipo, horasIngresadas, esClienteFrecuente)

    // 2. Cálculos
    val calculador = CalculadorEstacionamiento()
    val resultado = calculador.procesar(registro)

    // 3. Mostrar resultados
    val reporte = ReporteEstacionamiento()
    reporte.imprimirBoleta(resultado)
}