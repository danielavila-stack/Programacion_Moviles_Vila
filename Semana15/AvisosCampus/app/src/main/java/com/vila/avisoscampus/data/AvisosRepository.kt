package com.vila.avisoscampus.data

import com.vila.avisoscampus.model.Aviso
import com.vila.avisoscampus.model.Origen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Contrato que conoce el ViewModel (no sabe cómo se guardan los avisos)
interface AvisosRepository {
    val avisos: StateFlow<List<Aviso>>
    fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}

// Implementación en memoria: los avisos viven mientras viva el proceso. No usa Context.
class AvisosRepositoryMemoria : AvisosRepository {

    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos: StateFlow<List<Aviso>> = _avisos.asStateFlow()

    private var siguienteId = 1
    private val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    override fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val aviso = Aviso(
            id = siguienteId++,
            titulo = titulo,
            texto = texto,
            origen = origen,
            hora = formatoHora.format(Date())
        )
        // Lo más reciente va arriba
        _avisos.update { lista -> listOf(aviso) + lista }
        return aviso
    }
}
