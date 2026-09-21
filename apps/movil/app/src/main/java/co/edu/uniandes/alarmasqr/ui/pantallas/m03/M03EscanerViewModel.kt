package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.qr.AnalizadorQR
import co.edu.uniandes.alarmasqr.qr.ResultadoQR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoEscaner(val linterna: Boolean = false, val resultado: ResultadoQR? = null)

/** M03: linterna y un solo resultado por lectura; la entrada navega al consumirlo. Los ⏩ usan el dataset. */
class M03EscanerViewModel(private val repositorio: RepositorioDataset) : ViewModel() {
    private val _estado = MutableStateFlow(EstadoEscaner())
    val estado: StateFlow<EstadoEscaner> = _estado.asStateFlow()

    fun alternarLinterna() = _estado.update { it.copy(linterna = !it.linterna) }

    fun leer(contenido: String) {
        if (_estado.value.resultado != null) return
        _estado.update { it.copy(resultado = AnalizadorQR.interpretar(contenido, repositorio)) }
    }

    /** ⏩ Tocar «Apunta al código QR del evento» = leer el QR de e-entrega (NAVEGACION §6 paso 4). */
    fun simularEventoValido() = leer(repositorio.evento("e-entrega")?.codigoQR ?: "")

    /** ⏩ Tocar «vibra al detectar el código» = leer el QR inválido del dataset (paso 4b). */
    fun simularInvalido() = leer(repositorio.dataset.qrInvalido.contenido)

    fun consumirResultado() = _estado.update { it.copy(resultado = null) }
}
