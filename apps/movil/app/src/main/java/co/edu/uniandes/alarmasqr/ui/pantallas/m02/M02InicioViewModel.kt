package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class GrupoDia(val etiqueta: String, val alarmas: List<Alarma>)

/** Estado de M02/M02v/M05: grupos por día y, en M05, el id de la alarma recién guardada. */
data class EstadoInicio(val grupos: List<GrupoDia>, val alarmaNueva: String? = null)

/** Agrupa en orden cronológico con los rótulos de FormatoHora («HOY · JUEVES 27»…); la lista ya viene ordenada. */
fun agruparPorDia(alarmas: List<Alarma>, hoy: LocalDate): List<GrupoDia> =
    alarmas.groupBy { FormatoHora.dia(it.eventoInicio) }.entries.sortedBy { it.key }
        .map { (_, lista) -> GrupoDia(FormatoHora.etiquetaDia(lista.first().eventoInicio, hoy), lista) }

/** ViewModel compartido por M02, M02v y M05 (misma pantalla, tres claves); vive en la entrada de Navigation 3. */
class M02InicioViewModel(private val repositorio: RepositorioDataset, private val alarmaNueva: String? = null) : ViewModel() {
    val estado: StateFlow<EstadoInicio> = repositorio.alarmas
        .map { EstadoInicio(agruparPorDia(it, repositorio.hoy), alarmaNueva) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, EstadoInicio(agruparPorDia(repositorio.alarmas.value, repositorio.hoy), alarmaNueva))

    fun cambiarActiva(id: String, activa: Boolean) = repositorio.cambiarEstado(id, pausada = !activa)

    fun deshacer() = repositorio.deshacer()

    /** Cierra la ventana de «Deshacer» sin revertir nada (M05: al vencer los 5 s o al salir de la pantalla). */
    fun olvidarDeshacer() = repositorio.olvidarDeshacer()
}
