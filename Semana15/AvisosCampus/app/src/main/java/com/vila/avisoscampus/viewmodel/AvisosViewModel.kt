package com.vila.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import com.vila.avisoscampus.data.AvisosRepository
import com.vila.avisoscampus.model.Aviso
import com.vila.avisoscampus.model.PermisoUi
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

    // TODO Paso 2: permisoRespondido(concedido) cambia _permiso a Concedido o Denegado.
    // TODO Paso 3: avisarAhora() registra "Tarea lista" y emite el aviso en _eventos.
    // TODO Paso 5: recordarEn(segundos) con cuenta regresiva y cancelarRecordatorio().
    // TODO Paso 6: cambiarEscucha(valor) y alCambiarModoAvion(activo).
    // TODO Paso 8: noSePudoMostrar() pone el mensaje de permiso denegado.
}
