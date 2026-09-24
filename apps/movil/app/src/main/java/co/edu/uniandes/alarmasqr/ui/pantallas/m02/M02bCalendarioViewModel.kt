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
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val LOCALE_ES = Locale.forLanguageTag("es-CO")

data class DiaCalendario(val fecha: LocalDate, val enMes: Boolean, val alarmasCount: Int)
data class EstadoCalendario(
    val mes: YearMonth,
    val tituloMes: String,
    val dias: List<DiaCalendario>,
    val diaSeleccionado: LocalDate,
    val etiquetaDiaSeleccionado: String,
    val alarmasDelDia: List<Alarma>,
)

private fun tituloMes(mes: YearMonth): String {
    val nombre = mes.month.getDisplayName(TextStyle.FULL, LOCALE_ES).replaceFirstChar { it.uppercase(LOCALE_ES) }
    return "$nombre ${mes.year}"
}

private fun etiquetaDiaSeleccionado(fecha: LocalDate, cantidadAlarmas: Int): String {
    val diaCorto = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, LOCALE_ES).trimEnd('.').uppercase(LOCALE_ES)
    val sufijo = if (cantidadAlarmas == 1) "ALARMA" else "ALARMAS"
    return "$diaCorto ${fecha.dayOfMonth} · $cantidadAlarmas $sufijo"
}

/** Grilla completa de semanas (lunes a domingo) que cubre [mes], con los días de los meses vecinos que rellenan la primera y la última semana. */
private fun celdasDelMes(mes: YearMonth, conteos: Map<LocalDate, Int>): List<DiaCalendario> {
    val primerDia = mes.atDay(1)
    val ultimoDia = mes.atEndOfMonth()
    val inicioGrilla = primerDia.minusDays((primerDia.dayOfWeek.value - 1).toLong())
    val finGrilla = ultimoDia.plusDays((7 - ultimoDia.dayOfWeek.value).toLong())
    return generateSequence(inicioGrilla) { it.plusDays(1) }
        .takeWhile { !it.isAfter(finGrilla) }
        .map { DiaCalendario(it, YearMonth.from(it) == mes, conteos[it] ?: 0) }
        .toList()
}

/**
 * F-M02 (M02b): mes navegable (‹ ›) con contador por día derivado de `RepositorioDataset.alarmas` (no hay un campo
 * de dataset por día que sobreviva a cambiar de mes) y el detalle del día seleccionado.
 */
class M02bCalendarioViewModel(private val repositorio: RepositorioDataset) : ViewModel() {
    private val calendario = repositorio.dataset.calendario
    private val _diaSeleccionado = MutableStateFlow(LocalDate.parse(calendario.diaSeleccionado))
    private val _mes = MutableStateFlow(YearMonth.from(_diaSeleccionado.value))

    val estado: StateFlow<EstadoCalendario> = combine(repositorio.alarmas, _mes, _diaSeleccionado) { alarmas, mes, dia ->
        val conteos = alarmas.groupingBy { FormatoHora.dia(it.eventoInicio) }.eachCount()
        EstadoCalendario(
            mes, tituloMes(mes), celdasDelMes(mes, conteos), dia,
            etiquetaDiaSeleccionado(dia, conteos[dia] ?: 0),
            alarmas.filter { FormatoHora.dia(it.eventoInicio) == dia },
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, run {
        val conteos = repositorio.alarmas.value.groupingBy { FormatoHora.dia(it.eventoInicio) }.eachCount()
        val dia = _diaSeleccionado.value
        EstadoCalendario(_mes.value, tituloMes(_mes.value), celdasDelMes(_mes.value, conteos), dia, etiquetaDiaSeleccionado(dia, conteos[dia] ?: 0), repositorio.alarmas.value.filter { FormatoHora.dia(it.eventoInicio) == dia })
    })

    fun seleccionarDia(fecha: LocalDate) { _diaSeleccionado.value = fecha; _mes.value = YearMonth.from(fecha) }
    fun mesAnterior() = _mes.update { it.minusMonths(1) }
    fun mesSiguiente() = _mes.update { it.plusMonths(1) }
    fun cambiarActiva(id: String, activa: Boolean) = repositorio.cambiarEstado(id, pausada = !activa)
}
