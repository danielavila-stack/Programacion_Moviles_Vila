package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.Normalizer
import java.time.LocalDate

enum class OrdenPacientes { NOMBRE, ULTIMA_VISITA }

// Todos los filtros de la búsqueda juntos
data class FiltrosPacientes(
    val texto: String = "",
    val especie: Especie? = null, // null = Todos
    val orden: OrdenPacientes = OrdenPacientes.NOMBRE,
    val sinVisitasRecientes: Boolean = false
) {
    val activos: Boolean get() = this != FiltrosPacientes()
}

// Pacientes de la clínica: búsqueda por mascota, dueño o raza (sin importar tildes ni mayúsculas),
// filtro por especie y por visitas recientes, y orden por nombre o por última visita. Todo con combine.
class PacientesViewModel(repo: VetRepository) : ViewModel() {

    companion object {
        const val MESES_SIN_VISITA = 6L
    }

    private val _filtros = MutableStateFlow(FiltrosPacientes())
    val filtros: StateFlow<FiltrosPacientes> = _filtros.asStateFlow()

    // mascotaId -> fecha de su última cita ATENDIDA
    val ultimaVisita: StateFlow<Map<Int, String>> = repo.observarUltimasVisitas()
        .map { lista -> lista.associate { it.mascotaId to it.fecha } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(), ultimaVisita, _filtros
    ) { lista, visitas, f ->
        val t = normalizar(f.texto.trim())
        val limite = LocalDate.now().minusMonths(MESES_SIN_VISITA).toString()
        lista
            .filter { p ->
                t.isEmpty() ||
                    normalizar(p.mascota.nombre).contains(t) ||
                    normalizar(p.dueno.nombre).contains(t) ||
                    normalizar(p.mascota.raza).contains(t)
            }
            .filter { p -> f.especie == null || p.mascota.especie == f.especie.name }
            .filter { p ->
                // "Sin visitas recientes": nunca atendida o su última atención fue hace más de 6 meses
                val ultima = visitas[p.mascota.id]
                !f.sinVisitasRecientes || ultima == null || ultima < limite
            }
            .let { filtrada ->
                when (f.orden) {
                    OrdenPacientes.NOMBRE -> filtrada.sortedBy { normalizar(it.mascota.nombre) }
                    // La más reciente primero; las que nunca tuvieron visita, al final
                    OrdenPacientes.ULTIMA_VISITA -> filtrada.sortedWith(
                        compareByDescending<MascotaConDueno> { visitas[it.mascota.id] ?: "" }
                            .thenBy { normalizar(it.mascota.nombre) }
                    )
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun buscar(t: String) { _filtros.value = _filtros.value.copy(texto = t) }
    fun filtrar(e: Especie?) { _filtros.value = _filtros.value.copy(especie = e) }
    fun ordenar(o: OrdenPacientes) { _filtros.value = _filtros.value.copy(orden = o) }
    fun alternarSinVisitas() {
        _filtros.value = _filtros.value.copy(sinVisitasRecientes = !_filtros.value.sinVisitasRecientes)
    }
    fun limpiarFiltros() { _filtros.value = FiltrosPacientes() }

    fun edad(p: MascotaConDueno): Int = (LocalDate.now().year - p.mascota.anioNacimiento).coerceAtLeast(0)

    // "Siamés" -> "siames": sin tildes y en minúsculas para comparar
    private fun normalizar(s: String): String =
        Normalizer.normalize(s, Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "").lowercase()
}
