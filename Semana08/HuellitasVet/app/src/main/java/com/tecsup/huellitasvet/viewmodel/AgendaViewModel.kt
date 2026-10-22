package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// Agenda diaria del veterinario.
@OptIn(ExperimentalCoroutinesApi::class)
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    val fechas: List<LocalDate> = (0..6L).map { LocalDate.now().plusDays(it) }

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now())
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    // Citas del día elegido, ordenadas por hora (la consulta ya ordena)
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { repo.observarCitasPorFecha(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun elegirFecha(f: LocalDate) { _fechaSeleccionada.value = f }

    // PENDIENTE -> CONFIRMADA
    fun confirmar(c: CitaConDetalle) {
        if (EstadoCita.desde(c.cita.estado) != EstadoCita.PENDIENTE) {
            _mensaje.value = "Solo se puede confirmar una cita pendiente"
            return
        }
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CONFIRMADA) }
    }

    // Desde PENDIENTE o CONFIRMADA -> CANCELADA. ATENDIDA y CANCELADA son finales
    fun cancelar(c: CitaConDetalle) {
        val estado = EstadoCita.desde(c.cita.estado)
        if (estado != EstadoCita.PENDIENTE && estado != EstadoCita.CONFIRMADA) {
            _mensaje.value = "Una cita ${estado.etiqueta.lowercase()} ya no se puede cancelar"
            return
        }
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA) }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}
