package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// Citas del dueño en Próximas e Historial, con la regla de cancelación de 2 horas.
class MisCitasViewModel(private val repo: VetRepository, duenoId: Int) : ViewModel() {

    companion object {
        val ANTICIPACION_MINIMA: Duration = Duration.ofHours(2)
    }

    private val citas = repo.observarCitasDeDueno(duenoId)

    // PENDIENTE y CONFIRMADA desde ahora, la más cercana primero
    val proximas: StateFlow<List<CitaConDetalle>> = citas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { esActiva(it) && !fechaHora(it).isBefore(ahora) }.sortedBy { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ATENDIDA, CANCELADA y las pasadas, la más reciente primero
    val historial: StateFlow<List<CitaConDetalle>> = citas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { !esActiva(it) || fechaHora(it).isBefore(ahora) }.sortedByDescending { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Regla: solo PENDIENTE o CONFIRMADA y si faltan más de 2 horas
    fun puedeCancelar(c: CitaConDetalle): Boolean =
        esActiva(c) && Duration.between(LocalDateTime.now(), fechaHora(c)) > ANTICIPACION_MINIMA

    // La View pregunta antes de mostrar la confirmación
    fun validarCancelacion(c: CitaConDetalle): Boolean {
        val ok = puedeCancelar(c)
        if (!ok) _mensaje.value = "Ya no puedes cancelar esta cita"
        return ok
    }

    fun cancelar(c: CitaConDetalle) {
        if (!validarCancelacion(c)) return
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA) }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    private fun esActiva(c: CitaConDetalle): Boolean {
        val e = EstadoCita.desde(c.cita.estado)
        return e == EstadoCita.PENDIENTE || e == EstadoCita.CONFIRMADA
    }

    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
}
