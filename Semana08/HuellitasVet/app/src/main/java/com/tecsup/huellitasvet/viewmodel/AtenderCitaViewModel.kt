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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresAtencion(
    val diagnostico: String? = null,
    val peso: String? = null
)

// Registro clínico de una cita: diagnóstico, tratamiento y peso.
class AtenderCitaViewModel(private val repo: VetRepository, citaId: Int) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errores = MutableStateFlow(ErroresAtencion())
    val errores: StateFlow<ErroresAtencion> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // true cuando se registró o canceló: la pantalla vuelve a la agenda
    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    // Solo se puede atender (o cancelar) una cita PENDIENTE o CONFIRMADA
    fun sePuedeAtender(c: CitaConDetalle?): Boolean {
        val e = c?.let { EstadoCita.desde(it.cita.estado) } ?: return false
        return e == EstadoCita.PENDIENTE || e == EstadoCita.CONFIRMADA
    }

    fun registrar(diagnostico: String, tratamiento: String, peso: String) {
        val c = cita.value
        if (!sePuedeAtender(c)) {
            _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
            return
        }
        val pesoNum = peso.replace(',', '.').toDoubleOrNull()
        val errores = ErroresAtencion(
            diagnostico = if (diagnostico.trim().length < 5) "El diagnóstico debe tener al menos 5 caracteres" else null,
            peso = if (pesoNum == null || pesoNum <= 0.0) "El peso debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.diagnostico != null || errores.peso != null) return

        viewModelScope.launch {
            // Transacción: la cita queda ATENDIDA y el peso de la mascota se actualiza a la vez
            repo.registrarAtencion(c!!.cita.id, diagnostico.trim(), tratamiento.trim(), pesoNum!!)
            _terminado.value = true
        }
    }

    fun cancelar() {
        val c = cita.value
        if (!sePuedeAtender(c)) {
            _mensaje.value = "Esta cita ya no se puede cancelar"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(c!!.cita.id, EstadoCita.CANCELADA)
            _terminado.value = true
        }
    }

    fun limpiarError(campo: String) {
        _errores.value = when (campo) {
            "diagnostico" -> _errores.value.copy(diagnostico = null)
            else -> _errores.value.copy(peso = null)
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}
