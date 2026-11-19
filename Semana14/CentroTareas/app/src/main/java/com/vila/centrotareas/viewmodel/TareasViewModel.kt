package com.vila.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vila.centrotareas.data.TareasRepository
import com.vila.centrotareas.model.CargaUi
import com.vila.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // Paso 3: progreso de 0f a 1f (solo lectura hacia afuera)
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    // Paso 4: el Job guarda la corrutina para poder cancelarla
    private var trabajo: Job? = null

    // Paso 4: true mientras la tarea trabaja
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    // Pasos 2, 3 y 4: tarea en segundo plano, con progreso y cancelable
    fun tareaLarga() {
        if (_corriendo.value) return // Paso 4: si ya está corriendo, no lanzar otra

        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")

                withContext(Dispatchers.Default) {
                    Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
                    for (i in 1..100) {
                        delay(40)                   // si la cancelan, delay corta aquí
                        _progreso.value = i / 100f  // de 0.01 a 1.0
                    }
                }

                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                // Se ejecuta SIEMPRE: termine bien o la cancelen
                _corriendo.value = false
            }
        }
    }

    // Paso 4: detiene la tarea; la barra se queda donde estaba
    fun cancelar() {
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    // Paso 5: Job del cálculo de primos
    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (hasta < 2) return                               // ignora números menores que 2
        if (_primos.value is PrimosUi.Calculando) return    // no lanzar dos cálculos

        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)     // el trabajo pesado está en el Repository
                val ms = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad, ms)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e                                     // relanzar: si no, la cancelación se rompe
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    // Paso 6: primero el perfil y DESPUÉS los pedidos (≈ 2 s + 3 s = 5 s)
    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return   // no lanzar dos cargas

        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()

            val perfil = repo.cargarPerfil()
            val pedidos = repo.cargarPedidos(false)

            val ms = System.currentTimeMillis() - inicio
            _carga.value = CargaUi.Exito("Una por una", perfil, pedidos.size, ms)
        }
    }

    // Paso 6: las dos llamadas AL MISMO TIEMPO con async (≈ máximo(2 s, 3 s) = 3 s)
    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando) return

        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()

            val (perfil, pedidos) = coroutineScope {
                val a = async { repo.cargarPerfil() }
                val b = async { repo.cargarPedidos(false) }
                Pair(a.await(), b.await())
            }

            val ms = System.currentTimeMillis() - inicio
            _carga.value = CargaUi.Exito("A la vez", perfil, pedidos.size, ms)
        }
    }

    // TODO Paso 7: simularError y manejo de errores
}