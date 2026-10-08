package com.daniela.saludpluscitas.data.repository

import com.daniela.saludpluscitas.data.model.Cita
import com.daniela.saludpluscitas.data.model.Especialidad
import com.daniela.saludpluscitas.data.model.Medico
import com.daniela.saludpluscitas.data.model.Usuario

object Repositorio {
    // Colecciones en memoria (se llenarán en el Commit 2)
    private val usuarios = mutableListOf<Usuario>()
    private val especialidades = mutableListOf<Especialidad>()
    private val medicos = mutableListOf<Medico>()
    private val citas = mutableListOf<Cita>()

    var usuarioActual: Usuario? = null
}