package com.vila.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vila.murocampus.data.PostRepository
import com.vila.murocampus.model.Comentario
import com.vila.murocampus.model.NuevoPost
import com.vila.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    // ---------- Búsqueda (Fase 2 · Mejora 2) ----------
    // Texto escrito en el campo de búsqueda
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    // Lo que dibuja la pantalla: la lista descargada + el texto de búsqueda.
    // Se recalcula cada vez que cambia cualquiera de los dos. NO llama a la red.
    val listaVisible: StateFlow<UiState<List<Post>>> =
        combine(_lista, _busqueda) { estado, texto ->
            if (estado is UiState.Exito && texto.isNotBlank()) {
                UiState.Exito(
                    estado.datos.filter { it.title.contains(texto.trim(), ignoreCase = true) }
                )
            } else {
                estado   // Cargando, Error o sin texto: se muestra tal cual
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Cargando
        )

    // ---------- Detalle (Paso 7) ----------
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private var idDetalle: Int? = null       // para poder reintentar
    private var trabajoDetalle: Job? = null  // para cancelar si abres otra rápido

    fun abrir(id: Int) {
        idDetalle = id
        _borrado.value = null                // Mejora 3: cada detalle empieza sin mensaje de borrado
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

    // ---------- Crear (Paso 9) ----------
    // null = todavía no se envió nada
    private val _creacion = MutableStateFlow<UiState<Post>?>(null)
    val creacion: StateFlow<UiState<Post>?> = _creacion.asStateFlow()

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _creacion.value = UiState.Cargando
            // POST posts con el NuevoPost en el cuerpo (@Body)
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _creacion.value = UiState.Exito(it) }
                .onFailure { _creacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // Al abrir la pantalla de crear, empieza limpia
    fun limpiarCreacion() {
        _creacion.value = null
    }

    // ---------- Borrar (Fase 2 · Mejora 3) ----------
    // null = no se pidió borrar. Éxito guarda el código de respuesta (200).
    private val _borrado = MutableStateFlow<UiState<Int>?>(null)
    val borrado: StateFlow<UiState<Int>?> = _borrado.asStateFlow()

    fun borrar(id: Int) {
        viewModelScope.launch {
            _borrado.value = UiState.Cargando
            repo.borrar(id)
                .onSuccess { codigo ->
                    // JSONPlaceholder no borra de verdad: la quitamos de la lista local
                    // para que el usuario vea el resultado
                    val actual = _lista.value
                    if (actual is UiState.Exito) {
                        _lista.value = UiState.Exito(actual.datos.filter { it.id != id })
                    }
                    _borrado.value = UiState.Exito(codigo)
                }
                .onFailure { _borrado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // Después de mostrar el aviso, se limpia
    fun limpiarBorrado() {
        _borrado.value = null
    }
}