package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

// Lo que el dueño escribe en el formulario (texto tal cual, se valida al guardar)
data class FormMascota(
    val nombre: String = "",
    val especie: Especie? = null,
    val raza: String = "",
    val anio: String = "",
    val peso: String = "",
    val sexo: Sexo? = null
)

// Un mensaje por campo; null = sin error
data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val anio: String? = null,
    val peso: String? = null,
    val sexo: String? = null
) {
    val hayErrores get() = listOf(nombre, especie, anio, peso, sexo).any { it != null }
}

// Registrar / editar mascota y ver su ficha con el historial clínico.
// mascotaId = 0 -> mascota nueva.
class MascotaViewModel(
    private val repo: VetRepository,
    private val duenoId: Int,
    private val mascotaId: Int
) : ViewModel() {

    val mascota: StateFlow<Mascota?> =
        (if (mascotaId > 0) repo.observarMascota(mascotaId) else flowOf(null))
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _form = MutableStateFlow(FormMascota())
    val form: StateFlow<FormMascota> = _form.asStateFlow()

    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // true cuando se guardó o eliminó: la pantalla vuelve atrás
    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    private val historiales = mutableMapOf<Int, StateFlow<List<CitaConDetalle>>>()

    init {
        // Al editar, se llena el formulario con los datos actuales
        if (mascotaId > 0) {
            viewModelScope.launch {
                repo.observarMascota(mascotaId).first()?.let { m ->
                    _form.value = FormMascota(
                        nombre = m.nombre,
                        especie = Especie.desde(m.especie),
                        raza = m.raza,
                        anio = m.anioNacimiento.toString(),
                        peso = m.peso.toString(),
                        sexo = Sexo.desde(m.sexo)
                    )
                }
            }
        }
    }

    // Citas ATENDIDAS de la mascota, la más reciente primero
    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> = historiales.getOrPut(mascotaId) {
        repo.observarHistorial(mascotaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun edad(m: Mascota): Int = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)

    // Cambios del formulario: se limpia el error del campo que se edita
    fun cambiarNombre(v: String) { _form.update { it.copy(nombre = v) }; _errores.update { it.copy(nombre = null) } }
    fun cambiarEspecie(v: Especie) { _form.update { it.copy(especie = v) }; _errores.update { it.copy(especie = null) } }
    fun cambiarRaza(v: String) { _form.update { it.copy(raza = v) } }
    fun cambiarAnio(v: String) { _form.update { it.copy(anio = v.filter(Char::isDigit).take(4)) }; _errores.update { it.copy(anio = null) } }
    fun cambiarPeso(v: String) { _form.update { it.copy(peso = v.replace(',', '.')) }; _errores.update { it.copy(peso = null) } }
    fun cambiarSexo(v: Sexo) { _form.update { it.copy(sexo = v) }; _errores.update { it.copy(sexo = null) } }

    fun guardar() {
        val f = _form.value
        val anioActual = LocalDate.now().year
        val anio = f.anio.toIntOrNull()
        val peso = f.peso.toDoubleOrNull()
        val errores = ErroresMascota(
            nombre = if (f.nombre.trim().length < 2) "El nombre debe tener al menos 2 caracteres" else null,
            especie = if (f.especie == null) "Elige la especie" else null,
            anio = if (anio == null || anio !in 1990..anioActual) "El año debe estar entre 1990 y $anioActual" else null,
            peso = if (peso == null || peso <= 0.0) "El peso debe ser mayor que 0" else null,
            sexo = if (f.sexo == null) "Elige el sexo" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        viewModelScope.launch {
            val actual = if (mascotaId > 0) repo.observarMascota(mascotaId).first() else null
            repo.guardarMascota(
                Mascota(
                    id = mascotaId,
                    duenoId = actual?.duenoId ?: duenoId,
                    nombre = f.nombre.trim(),
                    especie = f.especie!!.name,
                    raza = f.raza.trim().ifEmpty { "Sin especificar" },
                    anioNacimiento = anio!!,
                    peso = peso!!,
                    sexo = f.sexo!!.name
                )
            )
            _terminado.value = true
        }
    }

    // Regla: una mascota con citas no se elimina
    fun eliminar(m: Mascota) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(m.id)) {
                _mensaje.value = "No se puede eliminar a ${m.nombre}: tiene citas registradas"
            } else {
                repo.eliminarMascota(m)
                _terminado.value = true
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
