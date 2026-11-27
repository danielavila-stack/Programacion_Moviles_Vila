package com.vila.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vila.avisoscampus.data.AvisosRepositoryMemoria

// Fábrica: le entrega al ViewModel su Repository
class AvisosViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AvisosViewModel(AvisosRepositoryMemoria()) as T
    }
}
