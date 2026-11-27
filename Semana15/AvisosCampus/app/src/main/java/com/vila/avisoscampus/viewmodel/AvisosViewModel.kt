package com.vila.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vila.avisoscampus.data.AvisosRepository
import com.vila.avisoscampus.model.Aviso
import com.vila.avisoscampus.model.Origen
import com.vila.avisoscampus.model.PermisoUi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

// El ViewModel decide QUÉ avisar. No conoce Context, NotificationManager
// ni BroadcastReceiver: solo habla con el Repository y emite eventos.
class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {

    // Historial de avisos (viene del Repository)
    val avisos: StateFlow<List<Aviso>> = repo.avisos

    // Evento de "muestra esta notificación": la pantalla lo recoge
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    // Estado del permiso de notificaciones
    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    // Mensaje de error para la pantalla (null = sin mensaje)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // La pantalla avisa qué respondió el usuario (o si ya estaba concedido).
    // El ViewModel solo guarda el resultado; no sabe pedir permisos.
    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    // Botón "Mostrar aviso": registra el aviso en el historial y emite el evento.
    // La pantalla recibe el evento y es ella quien muestra la notificación.
    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = repo.registrar(
                titulo = "Tarea lista",
                texto = "Tu entrega fue registrada",
                origen = Origen.BOTON
            )
            _eventos.emit(aviso)
        }
    }
}
