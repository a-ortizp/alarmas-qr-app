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
    val chipOrigen: String?,
    val anticipacionMin: Int,
    val sumarTrayecto: Boolean,
    val sonido: String,
    val respetarNoMolestar: Boolean,
    val posponerMin: Int,
    val confirmarAntesDeAutoAjustar: Boolean,
    val notas: String,
    val dialogoAbierto: Boolean = false,
)

/**
 * F-M06: personalización por alarma. Solo `anticipacionMin` existe en el modelo `Alarma`; el resto de los ajustes
 * (`sumarTrayecto`, `sonido`, `respetarNoMolestar`, `posponerMin`, `confirmarAntesDeAutoAjustar`) son estado de UI
 * que arranca desde `usuario.ajustes` (no hay campo por alarma en el dataset para ellos — «Guardar cambios» solo
 * persiste la anticipación, consistente con la maquetación).
 */
class M06EditarAlarmaViewModel(private val repositorio: RepositorioDataset, private val id: String) : ViewModel() {
    init {
        // Entrar a editarla también cuenta como «ya la vi»: apaga el resaltado de «recién creada/escaneada» de
        // cualquier alarma que lo tenga, no solo esta (mismo criterio simple que M02InicioViewModel).
        repositorio.limpiarRecienCreadas()
    }

    private val original = repositorio.alarma(id) ?: error("Alarma $id no existe")
    private val ajustes = repositorio.dataset.usuario.ajustes
    private val _estado = MutableStateFlow(
        EstadoEditarAlarma(
            alarma = original,
            chipOrigen = original.chips.firstOrNull { it != "Nueva" },
            anticipacionMin = original.anticipacionMin,
            sumarTrayecto = ajustes.sumarTrayectoDesdeUbicacionHabitual,
            sonido = ajustes.sonidoPorDefecto,
            respetarNoMolestar = ajustes.respetarNoMolestar,
            posponerMin = ajustes.posponerPorDefectoMin,
            confirmarAntesDeAutoAjustar = ajustes.confirmarAntesDeAutoAjustar,
            notas = original.notas ?: "",
        ),
    )
    val estado: StateFlow<EstadoEditarAlarma> = _estado.asStateFlow()
    val mensajeEliminar: String = repositorio.mensajeEliminar(original)

    /** M09 solo tiene contenido para la alarma que trae `cambioDelOrganizador` (hoy, «a-entrega»). */
    val puedeVerCambioOrganizador: Boolean = original.cambioDelOrganizador != null

    fun elegirAnticipacion(min: Int) = _estado.update { it.copy(anticipacionMin = min) }
    fun cambiarSumarTrayecto(valor: Boolean) = _estado.update { it.copy(sumarTrayecto = valor) }
    fun elegirSonido(valor: String) = _estado.update { it.copy(sonido = valor) }
    fun cambiarRespetarNoMolestar(valor: Boolean) = _estado.update { it.copy(respetarNoMolestar = valor) }
    fun cambiarConfirmar(valor: Boolean) = _estado.update { it.copy(confirmarAntesDeAutoAjustar = valor) }
    fun cambiarNotas(valor: String) = _estado.update { it.copy(notas = valor) }
    fun guardar() {
        repositorio.alarma(id)?.let {
            repositorio.agregar(it.copy(anticipacionMin = _estado.value.anticipacionMin, notas = _estado.value.notas.ifBlank { null }))
        }
    }
    fun abrirDialogo() = _estado.update { it.copy(dialogoAbierto = true) }
    fun cerrarDialogo() = _estado.update { it.copy(dialogoAbierto = false) }
    fun eliminar() { repositorio.eliminar(id); cerrarDialogo() }
}
