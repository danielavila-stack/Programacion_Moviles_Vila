package com.vila.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vila.murocampus.data.PostRepository
import com.vila.murocampus.model.Comentario
import com.vila.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Lo que muestra la pantalla de detalle: la publicación y sus comentarios
data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

// Solo conoce la interface PostRepository: nada de Retrofit, PostApi ni Context
class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---------- Lista (Paso 6) + Filtro (Paso 8) ----------
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // Usuario elegido en los chips: null = Todos
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private var trabajoLista: Job? = null   // si tocas chips rápido, se cancela la carga anterior

    init { cargar() }   // al crearse, descarga la lista

    fun cargar() {
        trabajoLista?.cancel()
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val filtro = _usuarioFiltro.value
            // Sin filtro: GET posts. Con filtro: GET posts?userId=N
            val resultado = if (filtro == null) repo.listar() else repo.porUsuario(filtro)
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // Guarda el filtro y vuelve a cargar
    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    // ---------- Detalle (Paso 7) ----------
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private var idDetalle: Int? = null       // para poder reintentar
    private var trabajoDetalle: Job? = null  // para cancelar si abres otra rápido

    fun abrir(id: Int) {
        idDetalle = id
        trabajoDetalle?.cancel()             // si había otro detalle cargando, se cancela
        trabajoDetalle = viewModelScope.launch {
            _detalle.value = UiState.Cargando

            // Las dos llamadas AL MISMO TIEMPO, como en la Semana 14
            val (resPost, resComentarios) = coroutineScope {
                val p = async { repo.detalle(id) }
                val c = async { repo.comentarios(id) }
                p.await() to c.await()
            }

            // Si cualquiera de las dos falla, todo el detalle muestra el error
            val post = resPost.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it)); return@launch
            }
            val comentarios = resComentarios.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it)); return@launch
            }
            _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
        }
    }

    fun reintentarDetalle() {
        idDetalle?.let { abrir(it) }
    }
}