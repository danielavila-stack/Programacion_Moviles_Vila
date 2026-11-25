package com.vila.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vila.murocampus.data.PostRepositoryRemote
import com.vila.murocampus.data.remote.RetrofitClient

// Crea el ViewModel y le entrega el Repository ya armado con la API
class PostsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            return PostsViewModel(PostRepositoryRemote(RetrofitClient.api)) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}