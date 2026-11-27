package com.vila.avisoscampus.model

// De dónde salió el aviso (se muestra como etiqueta en el historial)
enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión")
}

// Un aviso registrado en el historial
data class Aviso(
    val id: Int,          // también será el id de la notificación
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String,     // formato HH:mm:ss
    // Fase 2 · Mejora 1: si es urgente se muestra por el canal "Urgentes".
    // Tiene valor por defecto para no romper el código que ya crea avisos.
    val urgente: Boolean = false
)

// Estado del permiso de notificaciones que ve la pantalla
sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}
