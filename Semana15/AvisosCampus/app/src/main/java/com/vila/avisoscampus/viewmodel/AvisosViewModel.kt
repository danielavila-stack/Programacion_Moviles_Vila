package com.vila.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vila.avisoscampus.data.AvisosRepository
import com.vila.avisoscampus.model.Aviso
import com.vila.avisoscampus.model.Origen
import com.vila.avisoscampus.model.PermisoUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        // Si ya hay permiso, el mensaje de error ya no aplica
        if (concedido) _mensaje.value = null
    }

    // La pantalla avisa que Notificador.mostrar() devolvió false (sin permiso).
    // El aviso ya quedó en el historial; solo explicamos por qué no se vio.
    fun noSePudoMostrar() {
        _mensaje.value = "Sin permiso: el aviso se guardó en el historial, pero no se mostró"
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

    // ===== Recordatorio con cuenta regresiva =====

    // La corrutina de la cuenta regresiva (null si nunca se lanzó)
    private var trabajo: Job? = null

    // Segundos que faltan (null = no hay cuenta en curso)
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    // Inicia la cuenta. Vive en viewModelScope: sigue aunque se minimice la app
    // o se rote la pantalla, y se cancela sola si el ViewModel se destruye.
    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isActive == true) return  // ya hay una cuenta corriendo

        trabajo = viewModelScope.launch {
            try {
                for (s in segundos downTo 1) {
                    _faltan.value = s
                    delay(1000)  // espera 1 segundo sin bloquear la app
                }
                val aviso = repo.registrar(
                    titulo = "Recordatorio",
                    texto = "Revisa tu laboratorio pendiente",
                    origen = Origen.RECORDATORIO
                )
                // Fase 2 · Mejora 1: el ViewModel decide que el recordatorio es urgente
                _eventos.emit(aviso.copy(urgente = true))
            } finally {
                // Se ejecuta al terminar o al cancelar: limpia el contador
                _faltan.value = null
            }
        }
    }

    // Cancela la cuenta: delay() se interrumpe y no se emite ningún aviso
    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    // ===== Receptor del modo avión =====

    // ¿La pantalla debe tener registrado el receptor? (lo controla el Switch)
    // Vive en el ViewModel para que sobreviva a la rotación.
    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    // La pantalla llama a esto cuando el receptor recibe el anuncio.
    // El ViewModel no sabe qué es un BroadcastReceiver: solo recibe true/false.
    fun alCambiarModoAvion(activo: Boolean) {
        viewModelScope.launch {
            val aviso = repo.registrar(
                titulo = "Modo avión",
                texto = if (activo) "Activado" else "Desactivado",
                origen = Origen.MODO_AVION
            )
            _eventos.emit(aviso)
        }
    }
}
