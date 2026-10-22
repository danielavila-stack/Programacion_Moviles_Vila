package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class ResumenDueno(val proximas: Int = 0, val atenciones: Int = 0)

// Inicio del dueño: sus mascotas y su próxima cita.
class InicioViewModel(repo: VetRepository, duenoId: Int) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Primera cita PENDIENTE o CONFIRMADA desde ahora, ordenada por fecha y hora
    val proximaCita: StateFlow<CitaConDetalle?> = repo.observarCitasDeDueno(duenoId)
        .map { citas ->
            val ahora = LocalDateTime.now()
            citas.filter {
                val estado = EstadoCita.desde(it.cita.estado)
                (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) &&
                    !fechaHora(it).isBefore(ahora)
            }.minByOrNull { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Contadores del perfil: citas próximas y atenciones
    val resumen: StateFlow<ResumenDueno> = repo.observarCitasDeDueno(duenoId)
        .map { citas ->
            val ahora = LocalDateTime.now()
            ResumenDueno(
                proximas = citas.count {
                    val e = EstadoCita.desde(it.cita.estado)
                    (e == EstadoCita.PENDIENTE || e == EstadoCita.CONFIRMADA) && !fechaHora(it).isBefore(ahora)
                },
                atenciones = citas.count { EstadoCita.desde(it.cita.estado) == EstadoCita.ATENDIDA }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResumenDueno())

    // Accesos rápidos: solo los servicios activos
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun edad(m: Mascota): Int = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)

    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
}
