package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.alarma.Programador
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoAlarmaCreada(val alarma: Alarma, val dialogoAbierto: Boolean = false, val mensajeEliminar: String)

/**
 * F-M04: la alarma se programa al llegar (sin «Guardar»). El diálogo M04d es estado de esta hoja, no una ruta
 * (TRAZABILIDAD §1b). «Eliminar» cancela la alarma real y la quita del repositorio (queda «Deshacer» en la lista).
 */
class M04AlarmaCreadaViewModel(private val repositorio: RepositorioDataset, private val programador: Programador, private val id: String) : ViewModel() {
    private val alarma = repositorio.alarma(id) ?: error("Alarma $id no existe")
    private val _estado = MutableStateFlow(EstadoAlarmaCreada(alarma, mensajeEliminar = repositorio.mensajeEliminar(alarma)))
    val estado: StateFlow<EstadoAlarmaCreada> = _estado.asStateFlow()

    init { programador.programar(alarma) }

    fun abrirDialogo() = _estado.update { it.copy(dialogoAbierto = true) }
    fun cerrarDialogo() = _estado.update { it.copy(dialogoAbierto = false) }

    fun eliminar() {
        programador.cancelar(id)
        repositorio.eliminar(id)
        cerrarDialogo()
    }
}
