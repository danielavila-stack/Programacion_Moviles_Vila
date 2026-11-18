package com.vila.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vila.campusgps.data.LugaresRepository
import com.vila.campusgps.data.UbicacionNoDisponibleException
import com.vila.campusgps.data.UbicacionRepository
import com.vila.campusgps.model.Lugar
import com.vila.campusgps.model.LugarConDistancia
import com.vila.campusgps.model.Posicion
import com.vila.campusgps.model.TipoError
import com.vila.campusgps.model.UbicacionUi
import com.vila.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    // ---------- MEJORA 1: filtro por categoría ----------

    // Categorías disponibles, sacadas de los lugares (sin repetir)
    val categorias: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    // null = "Todos"
    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    // Lo que la pantalla dibuja: la lista ordenada, filtrada por la categoría elegida
    val lugaresVisibles: StateFlow<List<LugarConDistancia>> =
        combine(_lugares, _categoria) { lista, cat ->
            if (cat == null) lista else lista.filter { it.lugar.categoria == cat }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _lugares.value)

    fun filtrar(categoria: String?) {
        _categoria.value = categoria
        _seleccionado.value = null // la selección anterior podría quedar oculta
    }

    // El más cercano DE LO VISIBLE: el primero con distancia calculada
    val masCercano: StateFlow<Lugar?> = lugaresVisibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // ------------------------------------------------------

    fun buscar() {
        // Antes de buscar, revisa si hay Google Play Services (a través de la interface)
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    // Calcula la distancia a cada lugar, ordena de menor a mayor y publica la lista
    private fun actualizarDistancias(pos: Posicion) {
        val ordenados = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
        _lugares.value = ordenados

        ordenados.forEach { Log.d("CampusGPS", "${it.lugar.nombre}: ${it.metros} m") }
    }

    // Convierte la excepción en un Error con su tipo
    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
        is UbicacionNoDisponibleException -> UbicacionUi.Error(
            "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
            TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            e.message ?: "No se pudo obtener la ubicación",
            TipoError.OTRO
        )
    }
}