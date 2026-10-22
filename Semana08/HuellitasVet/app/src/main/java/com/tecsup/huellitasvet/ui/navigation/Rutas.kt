package com.tecsup.huellitasvet.ui.navigation

// Todas las rutas de la app. Las que llevan argumentos tienen su función para armarlas.
object Rutas {
    // Grafos
    const val GRAFO_DUENO = "grafo_dueno"
    const val GRAFO_VET = "grafo_vet"

    // Comunes
    const val SPLASH = "splash"
    const val LOGIN = "login"

    // Dueño
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val FORM_MASCOTA = "form_mascota?id={id}"
    const val FICHA_MASCOTA = "ficha_mascota/{id}"
    const val PEDIR_CITA = "pedir_cita?mascotaId={mascotaId}"
    const val MIS_CITAS = "mis_citas"
    const val REPROGRAMAR_CITA = "reprogramar_cita/{id}"
    const val PERFIL = "perfil"

    // Veterinario
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val ATENDER_CITA = "atender_cita/{id}"
    const val PACIENTES = "pacientes"
    const val FICHA_PACIENTE = "ficha_paciente/{id}"
    const val SERVICIOS = "servicios"
    const val FORM_SERVICIO = "form_servicio?id={id}"
    const val AJUSTES = "ajustes"

    fun formMascota(id: Int = 0) = "form_mascota?id=$id"
    fun fichaMascota(id: Int) = "ficha_mascota/$id"
    fun pedirCita(mascotaId: Int = 0) = "pedir_cita?mascotaId=$mascotaId"
    fun atenderCita(id: Int) = "atender_cita/$id"
    fun reprogramarCita(id: Int) = "reprogramar_cita/$id"
    fun fichaPaciente(id: Int) = "ficha_paciente/$id"
    fun formServicio(id: Int = 0) = "form_servicio?id=$id"
}
