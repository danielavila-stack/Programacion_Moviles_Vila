package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Fábrica genérica: recibe cómo crear el ViewModel (con su Repository y parámetros).
// Uso: viewModel(factory = VetViewModelFactory { ServiciosViewModel(app.repository) })
class VetViewModelFactory<VM : ViewModel>(private val crear: () -> VM) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = crear() as T
}
