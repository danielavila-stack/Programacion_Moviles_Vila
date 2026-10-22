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
import java.time.LocalDate

// Pacientes de la clínica: filtro por texto (mascota o dueño) y por especie con combine.
class PacientesViewModel(repo: VetRepository) : ViewModel() {

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    // null = Todos
    private val _especie = MutableStateFlow<Especie?>(null)
    val especie: StateFlow<Especie?> = _especie.asStateFlow()

    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(), _texto, _especie
    ) { lista, texto, especie ->
        val t = texto.trim().lowercase()
        lista.filter { p ->
            (especie == null || p.mascota.especie == especie.name) &&
                (t.isEmpty() || p.mascota.nombre.lowercase().contains(t) || p.dueno.nombre.lowercase().contains(t))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // mascotaId -> fecha de su última cita ATENDIDA
    val ultimaVisita: StateFlow<Map<Int, String>> = repo.observarUltimasVisitas()
        .map { lista -> lista.associate { it.mascotaId to it.fecha } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun buscar(t: String) { _texto.value = t }
    fun filtrar(e: Especie?) { _especie.value = e }

    fun edad(p: MascotaConDueno): Int = (LocalDate.now().year - p.mascota.anioNacimiento).coerceAtLeast(0)
}
