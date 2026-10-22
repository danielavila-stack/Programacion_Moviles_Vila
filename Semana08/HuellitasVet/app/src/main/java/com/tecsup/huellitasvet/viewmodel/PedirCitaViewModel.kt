package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// Pedir cita: mascota, servicio, fecha y hora sin chocar con otras citas.
@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(
    private val repo: VetRepository,
    duenoId: Int,
    mascotaInicial: Int
) : ViewModel() {

    companion object {
        const val DIAS_ADELANTE = 7L
    }

    private val _mascotaId = MutableStateFlow(mascotaInicial)
    val mascotaId: StateFlow<Int> = _mascotaId.asStateFlow()

    private val _servicioId = MutableStateFlow(0)
    val servicioId: StateFlow<Int> = _servicioId.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // true cuando la cita se creó: la pantalla va a Mis citas
    private val _creada = MutableStateFlow(false)
    val creada: StateFlow<Boolean> = _creada.asStateFlow()

    // Solo las mascotas del dueño; si no venía una elegida, se elige la primera
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .onEach { lista ->
            if (lista.none { it.id == _mascotaId.value }) _mascotaId.value = lista.firstOrNull()?.id ?: 0
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Los servicios inactivos NO aparecen
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // De hoy a 7 días
    val fechas: List<LocalDate> = (0..DIAS_ADELANTE).map { LocalDate.now().plusDays(it) }

    val horarios: List<String> = Horarios.delDia()

    // Horas con una cita no cancelada; cambia cada vez que cambia la fecha
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ocupadas + (si la fecha es hoy) las que ya pasaron
    val horasNoDisponibles: StateFlow<Set<String>> = combine(_fecha, horasOcupadas) { f, ocupadas ->
        ocupadas.toSet() + horarios.filter { yaPaso(f, it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val puedeConfirmar: StateFlow<Boolean> =
        combine(_mascotaId, _servicioId, _hora) { m, s, h -> m > 0 && s > 0 && h != null }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun elegirMascota(id: Int) { _mascotaId.value = id }
    fun elegirServicio(id: Int) { _servicioId.value = id }

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
        val mascota = mascotas.value.firstOrNull { it.id == _mascotaId.value }
        val servicio = servicios.value.firstOrNull { it.id == _servicioId.value }
        val f = _fecha.value
        val h = _hora.value
        when {
            mascota == null -> { _mensaje.value = "Elige una de tus mascotas"; return }
            servicio == null || !servicio.activo -> { _mensaje.value = "Elige un servicio disponible"; return }
            f.isBefore(LocalDate.now()) || f.isAfter(LocalDate.now().plusDays(DIAS_ADELANTE)) -> {
                _mensaje.value = "La fecha debe estar entre hoy y los próximos 7 días"; return
            }
            h == null || h !in horarios -> { _mensaje.value = "Elige una hora"; return }
            yaPaso(f, h) -> {
                _hora.value = null
                _mensaje.value = "Esa hora ya pasó, elige otra"; return
            }
        }
        viewModelScope.launch {
            // Se vuelve a consultar: el horario pudo ocuparse mientras el dueño elegía
            if (repo.horarioOcupado(f.toString(), h!!)) {
                _hora.value = null
                _mensaje.value = "Ese horario acaba de ocuparse. Elige otra hora"
            } else {
                repo.crearCita(mascota!!.id, servicio!!.id, f.toString(), h, nota = "")
                _creada.value = true
            }
        }
    }

    private fun yaPaso(f: LocalDate, h: String): Boolean =
        !LocalDateTime.of(f, LocalTime.parse(h)).isAfter(LocalDateTime.now())
}
