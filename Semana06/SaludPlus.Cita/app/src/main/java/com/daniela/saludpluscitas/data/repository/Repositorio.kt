package com.daniela.saludpluscitas.data.repository

import com.daniela.saludpluscitas.data.model.Cita
import com.daniela.saludpluscitas.data.model.Especialidad
import com.daniela.saludpluscitas.data.model.Medico
import com.daniela.saludpluscitas.data.model.Usuario
import com.daniela.saludpluscitas.data.model.ResultadoMedico
import java.time.LocalDate
import java.time.LocalTime

object Repositorio {
    var usuarioActual: Usuario? = Usuario(
        id = "1",
        nombre = "Daniela Vila",
        dni = "77654321",
        telefono = "987654321",
        correo = "daniela@gmail.com",
        clave = "123456"
    )

    val usuarios = mutableListOf<Usuario>().apply {
        usuarioActual?.let { add(it) }
    }

    val especialidades = listOf(
        Especialidad("1", "Medicina General", "Atención médica primaria y preventiva"),
        Especialidad("2", "Pediatría", "Atención para niños y adolescentes"),
        Especialidad("3", "Cardiología", "Cuidado de la salud cardiovascular"),
        Especialidad("4", "Dermatología", "Tratamiento de la piel y cabello"),
        Especialidad("5", "Odontología", "Cuidado y tratamiento dental")
    )

    val medicos = listOf(
        Medico("1", "Dr. Carlos Mendoza", "1", "Medicina General", "CMP 45892", "4.8"),
        Medico("2", "Dra. Ana Gutiérrez", "1", "Medicina General", "CMP 51203", "4.9"),
        Medico("3", "Dr. Luis Paredes", "2", "Pediatría", "CMP 38910", "4.7"),
        Medico("4", "Dra. Sofía Torres", "3", "Cardiología", "CMP 60124", "4.9"),
        Medico("5", "Dr. Roberto Gómez", "4", "Dermatología", "CMP 42105", "4.6")
    )

    val resultadosMedicos = mutableListOf(
        ResultadoMedico("1", "Hemograma completo", "2026-08-12", "Valores dentro del rango normal.", "Dr. Carlos Mendoza"),
        ResultadoMedico("2", "Perfil lipídico", "2026-08-12", "Colesterol LDL ligeramente elevado.", "Dra. Ana López"),
        ResultadoMedico("3", "Radiografía de tórax", "2026-07-03", "Sin hallazgos relevantes.", "Dr. Pedro Salas"),
        ResultadoMedico("4", "Glucosa en ayunas", "2026-06-20", "Normal: 88 mg/dL.", "Dr. Carlos Mendoza")
    )

    val citas = mutableListOf<Cita>()

    private val horariosBase = listOf(
        "08:00", "09:00", "10:00", "11:00", "12:00", "15:00", "16:00", "17:00"
    )

    fun horariosDisponibles(medicoId: String, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }

        val esHoy = fecha == LocalDate.now().toString()
        val ahora = LocalTime.now()

        return horariosBase.filter { hora ->
            hora !in ocupados && (!esHoy || LocalTime.parse(hora).isAfter(ahora))
        }
    }

    fun iniciarSesion(identificador: String, clave: String): Boolean {
        val user = usuarios.find {
            (it.correo == identificador || it.telefono == identificador) && it.clave == clave
        }
        if (user != null) {
            usuarioActual = user
            return true
        }
        return false
    }

    fun registrarUsuario(usuario: Usuario): Boolean {
        if (usuarios.any { it.correo == usuario.correo || it.telefono == usuario.telefono }) {
            return false
        }
        usuarios.add(usuario)
        usuarioActual = usuario
        return true
    }

    fun especialidadesDestacadas(): List<Especialidad> = especialidades.take(3)

    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades
        return especialidades.filter {
            it.nombre.contains(query, ignoreCase = true) || it.descripcion.contains(query, ignoreCase = true)
        }
    }

    fun obtenerEspecialidad(id: String): Especialidad? = especialidades.find { it.id == id }

    fun medicosPorEspecialidad(especialidadId: String): List<Medico> {
        return medicos.filter { it.especialidadId == especialidadId }
    }

    fun guardarCita(cita: Cita) {
        citas.add(cita)
    }

    fun eliminarCita(id: String) {
        citas.removeAll { it.id == id }
    }

    fun obtenerCitaPorId(id: String): Cita? = citas.find { it.id == id }

    fun obtenerMedicoPorId(id: String): Medico? = medicos.find { it.id == id }
}