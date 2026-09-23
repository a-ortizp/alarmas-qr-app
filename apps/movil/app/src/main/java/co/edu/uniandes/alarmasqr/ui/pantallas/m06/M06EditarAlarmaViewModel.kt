package co.edu.uniandes.alarmasqr.ui.pantallas.m06

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoEditarAlarma(
    val alarma: Alarma,
    val anticipacionMin: Int,
    val sonido: String,
    val confirmarAntesDeAutoAjustar: Boolean,
    val dialogoAbierto: Boolean = false,
)

/**
 * F-M06: personalización por alarma. Solo `anticipacionMin` existe en el modelo `Alarma`; `sonido` y
 * `confirmarAntesDeAutoAjustar` son estado de UI que arranca desde `usuario.ajustes` (no hay campo por alarma en el
 * dataset para ellos — «Guardar cambios» solo persiste la anticipación, consistente con la maquetación).
 */
class M06EditarAlarmaViewModel(private val repositorio: RepositorioDataset, private val id: String) : ViewModel() {
    private val original = repositorio.alarma(id) ?: error("Alarma $id no existe")
    private val _estado = MutableStateFlow(
        EstadoEditarAlarma(
            alarma = original,
            anticipacionMin = original.anticipacionMin,
            sonido = repositorio.dataset.usuario.ajustes.sonidoPorDefecto,
            confirmarAntesDeAutoAjustar = repositorio.dataset.usuario.ajustes.confirmarAntesDeAutoAjustar,
        ),
    )
    val estado: StateFlow<EstadoEditarAlarma> = _estado.asStateFlow()
    val mensajeEliminar: String = repositorio.mensajeEliminar(original)

    /** M09 solo tiene contenido para la alarma que trae `cambioDelOrganizador` (hoy, «a-entrega»). */
    val puedeVerCambioOrganizador: Boolean = original.cambioDelOrganizador != null

    fun elegirAnticipacion(min: Int) = _estado.update { it.copy(anticipacionMin = min) }
    fun elegirSonido(valor: String) = _estado.update { it.copy(sonido = valor) }
    fun cambiarConfirmar(valor: Boolean) = _estado.update { it.copy(confirmarAntesDeAutoAjustar = valor) }
    fun guardar() {
        repositorio.alarma(id)?.let { repositorio.agregar(it.copy(anticipacionMin = _estado.value.anticipacionMin)) }
    }
    fun abrirDialogo() = _estado.update { it.copy(dialogoAbierto = true) }
    fun cerrarDialogo() = _estado.update { it.copy(dialogoAbierto = false) }
    fun eliminar() { repositorio.eliminar(id); cerrarDialogo() }
}
