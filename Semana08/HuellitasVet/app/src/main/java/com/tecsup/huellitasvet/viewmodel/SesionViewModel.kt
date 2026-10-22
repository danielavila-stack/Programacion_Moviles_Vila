package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Sesión de la app: quién entró, con qué perfil y si usa modo oscuro.
class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // true cuando ya se revisó si había un perfil guardado en DataStore
    private val _sesionCargada = MutableStateFlow(false)
    val sesionCargada: StateFlow<Boolean> = _sesionCargada.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Última cuenta usada de cada rol en esta sesión (para volver al cambiar de perfil)
    private val ultimaCuenta = mutableMapOf<Rol, Usuario>()

    init {
        // Si ya hay perfil guardado, se recupera el usuario para saltar el login
        viewModelScope.launch {
            val perfil = prefs.perfilActivo.first()
            val id = prefs.usuarioId.first()
            if (perfil.isNotEmpty() && id > 0) {
                _usuarioActual.value = repo.obtenerUsuario(id)
            }
            _sesionCargada.value = true
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Ingresa tu correo y tu contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo, clave)
            when {
                usuario == null ->
                    _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Esta cuenta no es de ${rolSeleccionado.etiqueta}. " +
                        "Elige la pestaña ${Rol.valueOf(usuario.rol).etiqueta}"
                else -> {
                    _error.value = null
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun guardarModoOscuro(b: Boolean) { viewModelScope.launch { prefs.guardarModoOscuro(b) } }
    fun guardarNotificaciones(b: Boolean) { viewModelScope.launch { prefs.guardarNotificaciones(b) } }
    fun guardarAceptarCitas(b: Boolean) { viewModelScope.launch { prefs.guardarAceptarCitas(b) } }

    // Modo práctica: guarda el otro perfil y devuelve su rol para navegar a su grafo
    fun cambiarPerfil(onCambiado: (Rol) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val rolActual = Rol.valueOf(actual.rol)
        val otro = if (rolActual == Rol.CLIENTE) Rol.ADMIN else Rol.CLIENTE
        ultimaCuenta[rolActual] = actual
        viewModelScope.launch {
            val destino = ultimaCuenta[otro] ?: repo.obtenerUsuarioPorRol(otro)
            if (destino == null) {
                _error.value = "No hay una cuenta de ${otro.etiqueta} para practicar"
                return@launch
            }
            prefs.guardarSesion(otro, destino.id)
            _usuarioActual.value = destino
            onCambiado(otro)
        }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch { prefs.cerrarSesion() }
    }
}
