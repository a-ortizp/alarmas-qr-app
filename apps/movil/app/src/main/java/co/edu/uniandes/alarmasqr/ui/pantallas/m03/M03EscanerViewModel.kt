package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.qr.AnalizadorQR
import co.edu.uniandes.alarmasqr.qr.ResultadoQR
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoEscaner(val linterna: Boolean = false, val resultado: ResultadoQR? = null)

/**
 * M03: linterna y un solo resultado por lectura; la entrada navega al consumirlo. Los ⏩ usan el dataset.
 * [ahora] es inyectable para poder probar la ventana de [Movimiento.IgnorarRelecturaMs] sin depender del reloj real.
 */
class M03EscanerViewModel(private val repositorio: RepositorioDataset, private val ahora: () -> Long = System::currentTimeMillis) : ViewModel() {
    init {
        // Entrar a escanear un evento nuevo también apaga el resaltado de «recién creada/escaneada» de la alarma
        // anterior (mismo criterio simple que M02InicioViewModel/M06EditarAlarmaViewModel).
        repositorio.limpiarRecienCreadas()
    }

    private val _estado = MutableStateFlow(EstadoEscaner())
    val estado: StateFlow<EstadoEscaner> = _estado.asStateFlow()

    private var ultimoLeido: String? = null
    private var contenidoConsumido: String? = null
    private var consumidoEn: Long = 0L

    fun alternarLinterna() = _estado.update { it.copy(linterna = !it.linterna) }

    /**
     * ML Kit llama esto en cada cuadro mientras el QR siga frente a la cámara. Sin ignorar la relectura, volver de
     * M13 con el mismo QR inválido todavía en cuadro reabriría M13 de inmediato en bucle: se descarta una lectura
     * igual a la última consumida (resultado ya navegado) durante [Movimiento.IgnorarRelecturaMs].
     */
    fun leer(contenido: String) {
        if (_estado.value.resultado != null) return
        if (contenido == contenidoConsumido && ahora() - consumidoEn < Movimiento.IgnorarRelecturaMs) return
        ultimoLeido = contenido
        _estado.update { it.copy(resultado = AnalizadorQR.interpretar(contenido, repositorio)) }
    }

    /** ⏩ Tocar «Apunta al código QR del evento» = leer el QR de e-entrega (NAVEGACION §6 paso 4). */
    fun simularEventoValido() = leer(repositorio.evento("e-entrega")?.codigoQR ?: "")

    /** ⏩ Tocar «vibra al detectar el código» = leer el QR inválido del dataset (paso 4b). */
    fun simularInvalido() = leer(repositorio.dataset.qrInvalido.contenido)

    fun consumirResultado() {
        if (_estado.value.resultado != null) { contenidoConsumido = ultimoLeido; consumidoEn = ahora() }
        _estado.update { it.copy(resultado = null) }
    }
}
