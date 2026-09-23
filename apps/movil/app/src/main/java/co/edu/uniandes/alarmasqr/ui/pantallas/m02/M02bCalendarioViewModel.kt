package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class DiaCalendario(val fecha: LocalDate, val alarmasCount: Int)
data class EstadoCalendario(val mes: String, val dias: List<DiaCalendario>, val diaSeleccionado: LocalDate, val alarmasDelDia: List<Alarma>)

private fun diasDelMes(mes: String, conteos: Map<String, Int>): List<DiaCalendario> {
    val (anio, mesNum) = mes.split("-").map { it.toInt() }
    val primerDia = LocalDate.of(anio, mesNum, 1)
    return (1..primerDia.lengthOfMonth()).map { d ->
        val fecha = primerDia.withDayOfMonth(d)
        DiaCalendario(fecha, conteos[fecha.toString()] ?: 0)
    }
}

/** F-M02 (M02b): mes compacto con contador por día (`dataset.calendario.diasConAlarmas`) y detalle del día seleccionado, derivado de `RepositorioDataset.alarmas` filtrando por fecha — no hay un campo de dataset por día. */
class M02bCalendarioViewModel(private val repositorio: RepositorioDataset) : ViewModel() {
    private val calendario = repositorio.dataset.calendario
    private val dias = diasDelMes(calendario.mes, calendario.diasConAlarmas)
    private val _diaSeleccionado = MutableStateFlow(LocalDate.parse(calendario.diaSeleccionado))

    val estado: StateFlow<EstadoCalendario> = combine(repositorio.alarmas, _diaSeleccionado) { alarmas, dia ->
        EstadoCalendario(calendario.mes, dias, dia, alarmas.filter { FormatoHora.dia(it.eventoInicio) == dia })
    }.stateIn(
        viewModelScope, SharingStarted.Eagerly,
        EstadoCalendario(calendario.mes, dias, _diaSeleccionado.value, repositorio.alarmas.value.filter { FormatoHora.dia(it.eventoInicio) == _diaSeleccionado.value }),
    )

    fun seleccionarDia(fecha: LocalDate) { _diaSeleccionado.value = fecha }
    fun cambiarActiva(id: String, activa: Boolean) = repositorio.cambiarEstado(id, pausada = !activa)
}
