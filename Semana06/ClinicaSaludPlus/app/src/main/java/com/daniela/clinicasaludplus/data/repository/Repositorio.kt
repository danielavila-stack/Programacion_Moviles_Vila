package com.daniela.clinicasaludplus.data.repository

import com.daniela.clinicasaludplus.data.model.Cita
import com.daniela.clinicasaludplus.data.model.Especialidad
import com.daniela.clinicasaludplus.data.model.Medico
import com.daniela.clinicasaludplus.data.model.Usuario

object Repositorio {

    // 1. GESTIÓN DE USUARIOS
    private val listaUsuarios = mutableListOf(
        Usuario("u1", "Daniela Vila", "987654321", "daniela@gmail.com", "123456")
    )
    // Cambia esto en la línea 14 de Repositorio.kt:
    var usuarioActual: Usuario? = listaUsuarios.firstOrNull()

    fun registrarUsuario(usuario: Usuario): Boolean {
        if (listaUsuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }) {
            return false // El correo ya existe
        }
        listaUsuarios.add(usuario)
        return true
    }

    fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        val usuario = listaUsuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena
        }
        if (usuario != null) {
            usuarioActual = usuario
        }
        return usuario
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // 2. ESPECIALIDADES Y BÚSQUEDAS
    val especialidades = listOf(
        Especialidad("1", "Medicina General", "Atención primaria integral y diagnósticos preventivos.", "general"),
        Especialidad("2", "Pediatría", "Cuidado médico especializado para bebés, niños y adolescentes.", "pediatria"),
        Especialidad("3", "Cardiología", "Diagnóstico y tratamiento de enfermedades del corazón.", "cardiologia"),
        Especialidad("4", "Dermatología", "Tratamiento de afecciones en la piel, cabello y uñas.", "dermatologia"),
        Especialidad("5", "Neurología", "Evaluación y cuidado del sistema nervioso central.", "neurologia")
    )

    fun obtenerEspecialidades(): List<Especialidad> = especialidades

    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades
        return especialidades.filter {
            it.nombre.contains(query, ignoreCase = true) || it.descripcion.contains(query, ignoreCase = true)
        }
    }

    fun obtenerEspecialidadPorId(id: String): Especialidad? {
        return especialidades.find { it.id == id }
    }

    // 3. MÉDICOS Y FILTROS POR ESPECIALIDAD
    val medicos = listOf(
        Medico("m1", "1", "Dr. Roberto Carlos Silva", "CMP 45123", 4.8, "Lun - Vie: 08:00 - 14:00"),
        Medico("m2", "1", "Dra. Ana María Torres", "CMP 52104", 4.6, "Lun - Sáb: 15:00 - 19:00"),
        Medico("m3", "2", "Dr. Carlos Mendoza", "CMP 38901", 4.9, "Lun - Vie: 09:00 - 13:00"),
        Medico("m4", "3", "Dra. Sofía Benítez", "CMP 61230", 4.7, "Mar - Vie: 10:00 - 16:00"),
        Medico("m5", "4", "Dr. Javier Ríos", "CMP 29841", 4.5, "Lun - Mié: 08:00 - 12:00")
    )

    fun obtenerMedicosPorEspecialidad(especialidadId: String): List<Medico> {
        return medicos.filter { it.especialidadId == especialidadId }
    }

    fun obtenerMedicoPorId(id: String): Medico? {
        return medicos.find { it.id == id }
    }

    // 4. GESTIÓN DE CITAS

    private val listaCitas = mutableListOf<Cita>()

    fun agregarCita(cita: Cita) {
        listaCitas.add(cita)
    }

    fun obtenerCitasPorUsuario(usuarioId: String): List<Cita> {
        // Retorna las citas guardadas o todas si es usuario de prueba
        return listaCitas
    }
}