package com.vila.centrotareas.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    // Paso 5: cálculo pesado en un hilo de fondo, cancelable a mitad de camino
    // Paso 5 + MEJORA 1: cálculo pesado en segundo plano, cancelable y avisando el progreso
    override suspend fun contarPrimos(hasta: Int, alAvanzar: (Float) -> Unit): Int =
        withContext(Dispatchers.Default) {
            var cantidad = 0
            val total = hasta - 1                     // cuántos números se revisan (de 2 a hasta)
            val paso = maxOf(1, total / 100)          // avisar más o menos cada 1 %

            for (n in 2..hasta) {
                ensureActive()                        // si cancelaron, aquí se corta el cálculo
                if (esPrimo(n)) cantidad++

                val revisados = n - 1
                if (revisados % paso == 0) {
                    alAvanzar(revisados.toFloat() / total)
                }
            }
            alAvanzar(1f)                             // al terminar, 100 %
            cantidad
        }

    // false si algún d entre 2 y la raíz de n divide a n
    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var d = 2
        while (d.toLong() * d <= n) {  // toLong evita el desborde con números grandes
            if (n % d == 0) return false
            d++
        }
        return true
    }
}