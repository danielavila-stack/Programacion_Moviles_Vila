package com.daniela.saludpluscitas.data.repository

import com.daniela.saludpluscitas.data.model.Cita
import com.daniela.saludpluscitas.data.model.Especialidad
import com.daniela.saludpluscitas.data.model.Medico
import com.daniela.saludpluscitas.data.model.Usuario

object Repositorio {

    private val usuarios = mutableListOf<Usuario>()
    private val especialidades = mutableListOf<Especialidad>()
    private val medicos = mutableListOf<Medico>()
    private val citas = mutableListOf<Cita>()

    var usuarioActual: Usuario? = null
        private set

    init {
        // Usuario principal registrado por defecto
        val usuarioDaniela = Usuario(
            id = "1",
            nombre = "Daniela Vila",
            dni = "71234567",
            telefono = "914847645",
            correo = "daniela@gmail.com",
            contrasena = "123456"
        )
        usuarios.add(usuarioDaniela)
        usuarioActual = usuarioDaniela

        // Especialidades registradas
        especialidades.addAll(
            listOf(
                Especialidad("1", "Medicina General", "Atención integral"),
                Especialidad("2", "Pediatría", "Niños y adolescentes"),
                Especialidad("3", "Ginecología", "Salud de la mujer"),
                Especialidad("4", "Cardiología", "Corazón y sistema circulatorio"),
                Especialidad("5", "Dermatología", "Piel, cabello y uñas"),
                Especialidad("6", "Traumatología", "Huesos, músculos y articulaciones"),
                Especialidad("7", "Oftalmología", "Salud visual"),
                Especialidad("8", "Neurología", "Sistema nervioso")
            )
        )

        // Médicos registrados
        medicos.addAll(
            listOf(
                Medico("101", "Dr. Carlos Mendoza", "1", "Medicina General", "CMP 11201", 4.8),
                Medico("102", "Dra. Ana Torres", "1", "Medicina General", "CMP 14520", 4.9),
                Medico("103", "Dr. Pedro Salas", "2", "Pediatría", "CMP 09842", 4.7),
                Medico("104", "Dra. Laura Gómez", "3", "Ginecología", "CMP 16733", 4.9)
            )
        )

        // Citas agendadas iniciales para la vista de Mis Citas
        citas.addAll(
            listOf(
                Cita("c1", "1", "101", "Dr. Carlos Mendoza", "Medicina General", "2026-10-08", "08:30"),
                Cita("c2", "1", "103", "Dr. Pedro Salas", "Niños y adolescentes", "2026-10-08", "10:00")
            )
        )
    }

    // --- Autenticación ---
    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) || it.telefono == usuario.telefono }) {
            return false
        }
        usuarios.add(usuario)
        usuarioActual = usuario
        return true
    }

    fun iniciarSesion(identificador: String, contrasena: String): Boolean {
        val encontrado = usuarios.find {
            (it.correo.equals(identificador, ignoreCase = true) || it.telefono == identificador) && it.contrasena == contrasena
        }
        return if (encontrado != null) {
            usuarioActual = encontrado
            true
        } else {
            false
        }
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // --- Consultas de Médicos y Especialidades ---
    fun obtenerEspecialidades(): List<Especialidad> = especialidades

    fun especialidadesDestacadas(): List<Especialidad> = especialidades.take(3)

    fun buscarEspecialidades(query: String): List<Especialidad> {
        return if (query.isBlank()) especialidades
        else especialidades.filter {
            it.nombre.contains(query, ignoreCase = true) || it.descripcion.contains(query, ignoreCase = true)
        }
    }

    fun obtenerEspecialidad(id: String): Especialidad? = especialidades.find { it.id == id }

    fun medicosPorEspecialidad(especialidadId: String): List<Medico> {
        return medicos.filter { it.especialidadId == especialidadId }.sortedByDescending { it.calificacion }
    }

    fun obtenerMedico(id: String): Medico? = medicos.find { it.id == id }

    // --- Citas ---
    fun horariosDisponibles(medicoId: String, fecha: String): List<String> {
        val horariosBase = listOf("08:00", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "14:00", "14:30", "15:00", "15:30")
        val ocupados = citas.filter { it.medicoId == medicoId && it.fecha == fecha }.map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(cita: Cita): Boolean {
        val yaExiste = citas.any { it.medicoId == cita.medicoId && it.fecha == cita.fecha && it.hora == cita.hora }
        if (yaExiste) return false
        citas.add(cita)
        return true
    }

    fun citasDelUsuario(): List<Cita> {
        val id = usuarioActual?.id ?: return emptyList()
        return citas.filter { it.usuarioId == id }
    }

    fun cancelarCita(citaId: String): Boolean {
        return citas.removeIf { it.id == citaId }
    }
}