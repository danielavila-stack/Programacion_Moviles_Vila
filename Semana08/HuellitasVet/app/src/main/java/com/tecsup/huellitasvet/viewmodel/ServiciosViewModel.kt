package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresServicio(
    val nombre: String? = null,
    val duracion: String? = null,
    val precio: String? = null
) {
    val hayErrores get() = nombre != null || duracion != null || precio != null
}

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    companion object {
        val DURACIONES = listOf(15, 30, 45, 60)
    }

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Servicio que se está editando (null = nuevo o aún cargando)
    private val _editando = MutableStateFlow<Servicio?>(null)
    val editando: StateFlow<Servicio?> = _editando.asStateFlow()

    private val _errores = MutableStateFlow(ErroresServicio())
    val errores: StateFlow<ErroresServicio> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // true cuando se guardó o eliminó: el formulario vuelve a la lista
    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    fun cargar(id: Int) {
        if (id > 0) viewModelScope.launch { _editando.value = repo.obtenerServicio(id) }
    }

    // Nuevo (id = 0) o editado. Nombre obligatorio, duración 15/30/45/60 y precio > 0
    fun guardar(s: Servicio) {
        val errores = ErroresServicio(
            nombre = if (s.nombre.isBlank()) "El nombre es obligatorio" else null,
            duracion = if (s.duracionMin !in DURACIONES) "Elige 15, 30, 45 o 60 minutos" else null,
            precio = if (s.precio <= 0.0) "El precio debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return
        viewModelScope.launch {
            repo.guardarServicio(s.copy(nombre = s.nombre.trim(), descripcion = s.descripcion.trim()))
            _terminado.value = true
        }
    }

    // Regla: un servicio con citas no se elimina, solo se desactiva
    fun eliminar(s: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(s.id)) {
                _mensaje.value = "\"${s.nombre}\" tiene citas registradas: no se puede eliminar. Puedes desactivarlo."
            } else {
                repo.eliminarServicio(s)
                _terminado.value = true
            }
        }
    }

    fun limpiarError(campo: String) {
        _errores.value = when (campo) {
            "nombre" -> _errores.value.copy(nombre = null)
            "duracion" -> _errores.value.copy(duracion = null)
            else -> _errores.value.copy(precio = null)
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}
