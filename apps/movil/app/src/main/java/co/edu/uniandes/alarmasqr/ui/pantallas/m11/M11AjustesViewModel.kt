package co.edu.uniandes.alarmasqr.ui.pantallas.m11

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.datos.Ajustes
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoAjustes(val ajustes: Ajustes, val dialogoAbierto: Boolean = false)

/** F-M11: los ajustes viven en memoria (mismo alcance que el resto del dataset simulado); «Cerrar sesión» abre M11d. */
class M11AjustesViewModel(repositorio: RepositorioDataset) : ViewModel() {
    private val _estado = MutableStateFlow(EstadoAjustes(repositorio.dataset.usuario.ajustes))
    val estado: StateFlow<EstadoAjustes> = _estado.asStateFlow()

    fun cambiarNoMolestar(valor: Boolean) = _estado.update { it.copy(ajustes = it.ajustes.copy(respetarNoMolestar = valor)) }
    fun cambiarConfirmarAutoAjustar(valor: Boolean) = _estado.update { it.copy(ajustes = it.ajustes.copy(confirmarAntesDeAutoAjustar = valor)) }
    fun abrirDialogo() = _estado.update { it.copy(dialogoAbierto = true) }
    fun cerrarDialogo() = _estado.update { it.copy(dialogoAbierto = false) }
}
