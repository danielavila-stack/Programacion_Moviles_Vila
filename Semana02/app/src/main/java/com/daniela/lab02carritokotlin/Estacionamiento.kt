package com.daniela.lab02carritokotlin

import kotlin.math.max

// 1. Tarifas base por tipo de vehículo (Moto: S/ 2, Auto: S/ 4, Camioneta: S/ 10)
enum class TipoVehiculo(val tarifaBase: Double) {
    MOTO(2.0),
    AUTO(4.0),
    CAMIONETA(10.0)
}

// 2. Modelo de datos con la regla de mínimo 1 hora de estancia
data class RegistroEstacionamiento(
    val cliente: String,
    val placa: String,
    val tipo: TipoVehiculo,
    val horasIngresadas: Int,
    val esClienteFrecuente: Boolean
) {
    // Regla: Ningún vehículo puede registrar menos de 1 hora
    val horasEfectivas: Int = max(1, horasIngresadas)
}

fun main() {
    println("=== NGRESO DE DATOS ===")

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

    val registro = RegistroEstacionamiento(cliente, placa, tipo, horasIngresadas, esClienteFrecuente)

    println("\n--- Datos capturados ---")
    println("Cliente: ${registro.cliente}")
    println("Placa: ${registro.placa}")
    println("Tipo: ${registro.tipo.name} (S/ ${registro.tipo.tarifaBase}/hr)")
    println("Horas registradas (Efectivas): ${registro.horasEfectivas}")
    println("Es cliente frecuente: ${registro.esClienteFrecuente}")
}