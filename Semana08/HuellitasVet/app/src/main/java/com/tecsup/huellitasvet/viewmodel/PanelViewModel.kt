package com.tecsup.huellitasvet.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.graficos.DatoGrafico
import com.tecsup.huellitasvet.ui.theme.colorEstado
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

// Panel de la clínica: contadores, ingresos del mes y datos para los gráficos.
class PanelViewModel(repo: VetRepository) : ViewModel() {

    private val hoy = LocalDate.now()
    val fechaHoy: String = hoy.toString()

    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(hoy.toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendientes: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val atendidas: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Suma del precio de las citas ATENDIDAS desde el día 1 del mes
    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(hoy.withDayOfMonth(1).toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Room -> formato de los gráficos (se convierte aquí, no en la View)
    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        EstadoCita.entries.map { estado -> repo.observarCantidadPorEstado(estado).map { estado to it } }
    ) { pares ->
        pares.map { (estado, cantidad) -> DatoGrafico(estado.etiqueta, cantidad.toFloat(), colorEstado(estado)) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarCitasPorServicio()
        .map { lista ->
            lista.mapIndexed { i, c ->
                DatoGrafico(c.nombre.substringBefore(" "), c.cantidad.toFloat(), COLORES_BARRAS[i % COLORES_BARRAS.size])
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private companion object {
        val COLORES_BARRAS = listOf(Color(0xFF1D4ED8), Color(0xFF60A5FA), Color(0xFF93C5FD), Color(0xFFBFDBFE))
    }
}
