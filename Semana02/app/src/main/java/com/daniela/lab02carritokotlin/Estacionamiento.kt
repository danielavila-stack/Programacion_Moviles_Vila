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

// Estructura para registrar el desglose por cada hora
data class DetalleHora(
    val hora: Int,
    val tarifaBase: Double,
    val porcentajeRecargo: Double,
    val importe: Double
)

// Estructura con la información consolidada
data class ResultadoCalculo(
    val registro: RegistroEstacionamiento,
    val detalles: List<DetalleHora>,
    val subtotal: Double,
    val descuento: Double,
    val total: Double
)

// Clase encargada del procesamiento de las reglas de negocio
class CalculadorEstacionamiento {

    fun procesar(registro: RegistroEstacionamiento): ResultadoCalculo {
        val detalles = mutableListOf<DetalleHora>()
        var subtotal = 0.0

        for (h in 1..registro.horasEfectivas) {
            // Reglas de recargo:
            // 1-2 horas: 0% | 3-5 horas: 20% | >5 horas: 50%
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

        // Regla: 10% de descuento sobre el total acumulado si es cliente frecuente
        val descuento = if (registro.esClienteFrecuente) subtotal * 0.10 else 0.0
        val total = subtotal - descuento

        return ResultadoCalculo(registro, detalles, subtotal, descuento, total)
    }
}

fun main() {
    println("=== CÁLCULOS ===")

    print("Ingrese el nombre del cliente: ")
    val cliente = readLine().orEmpty()

    print("Ingrese la placa del vehículo: ")
    val placa = readLine().orEmpty()

    println("\nSeleccione el tipo de vehículo (1. Moto, 2. Auto, 3. Camioneta): ")
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

    val registro = RegistroEstacionamiento(cliente, placa, tipo, horasIngresadas, esClienteFrecuente)

    // Ejecución de la lógica de negocio
    val calculador = CalculadorEstacionamiento()
    val resultado = calculador.procesar(registro)

    println("\n--- Resumen del Cálculo ---")
    println("Subtotal acumulado : S/ ${resultado.subtotal}")
    println("Descuento (10%)    : -S/ ${resultado.descuento}")
    println("Total a pagar      : S/ ${resultado.total}")
}