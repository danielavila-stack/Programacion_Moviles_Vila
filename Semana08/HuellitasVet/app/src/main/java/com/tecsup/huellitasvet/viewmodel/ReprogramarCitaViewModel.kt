package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// Reprogramar una cita: nueva fecha y hora respetando horarios libres y la regla de 2 horas.
@OptIn(ExperimentalCoroutinesApi::class)
class ReprogramarCitaViewModel(
    private val repo: VetRepository,
    citaId: Int,
    aceptaCitas: Flow<Boolean>
) : ViewModel() {

    companion object {
        const val DIAS_ADELANTE = 7L
        val ANTICIPACION_MINIMA: Duration = Duration.ofHours(2)

        // Misma regla que cancelar: solo PENDIENTE o CONFIRMADA y si faltan más de 2 horas
        fun sePuedeReprogramar(c: CitaConDetalle, ahora: LocalDateTime = LocalDateTime.now()): Boolean {
            val e = EstadoCita.desde(c.cita.estado)
            val cuando = LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
            return (e == EstadoCita.PENDIENTE || e == EstadoCita.CONFIRMADA) &&
                Duration.between(ahora, cuando) > ANTICIPACION_MINIMA
        }
    }

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val clinicaAbierta: StateFlow<Boolean> = aceptaCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val fechas: List<LocalDate> = (0..DIAS_ADELANTE).map { LocalDate.now().plusDays(it) }
    val horarios: List<String> = Horarios.delDia()

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    // Ocupadas (incluye el horario actual de la propia cita) + las que ya pasaron si es hoy
    val horasNoDisponibles: StateFlow<Set<String>> = combine(
        _fecha,
        _fecha.flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
    ) { f, ocupadas ->
        ocupadas.toSet() + horarios.filter { yaPaso(f, it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val puedeConfirmar: StateFlow<Boolean> = combine(cita, _hora, clinicaAbierta) { c, h, abierta ->
        abierta && c != null && h != null && sePuedeReprogramar(c)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Si la fecha cambia, se limpia la hora elegida
    fun elegirFecha(f: LocalDate) {
        if (f != _fecha.value) {
            _fecha.value = f
            _hora.value = null
        }
    }

    fun elegirHora(h: String) {
        if (h !in horasNoDisponibles.value) _hora.value = h
    }

    fun limpiarMensaje() { _mensaje.value = null }

    fun confirmar() {
        val c = cita.value ?: return
        val f = _fecha.value
        val h = _hora.value
        when {
            !clinicaAbierta.value -> { _mensaje.value = "La clínica no está recibiendo citas"; return }
            !sePuedeReprogramar(c) -> { _mensaje.value = "Ya no puedes reprogramar esta cita"; return }
            h == null -> { _mensaje.value = "Elige la nueva hora"; return }
            f.isBefore(LocalDate.now()) || f.isAfter(LocalDate.now().plusDays(DIAS_ADELANTE)) -> {
                _mensaje.value = "La fecha debe estar entre hoy y los próximos 7 días"; return
            }
            yaPaso(f, h) -> { _hora.value = null; _mensaje.value = "Esa hora ya pasó, elige otra"; return }
            f.toString() == c.cita.fecha && h == c.cita.hora -> {
                _mensaje.value = "Elige un horario distinto al actual"; return
            }
        }
        viewModelScope.launch {
            // Se vuelve a consultar: el horario pudo ocuparse mientras el dueño elegía
            if (repo.horarioOcupado(f.toString(), h!!)) {
                _hora.value = null
                _mensaje.value = "Ese horario acaba de ocuparse. Elige otra hora"
            } else {
                repo.reprogramarCita(c.cita.id, f.toString(), h)
                _terminado.value = true
            }
        }
    }

    private fun yaPaso(f: LocalDate, h: String): Boolean =
        !LocalDateTime.of(f, LocalTime.parse(h)).isAfter(LocalDateTime.now())
}
