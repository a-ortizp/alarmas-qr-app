package co.edu.uniandes.alarmasqr.datos

import android.content.Context
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.OffsetDateTime

/**
 * Único origen de datos simulados de la app (docs/PLAN_MAQUETACION.md §5). Se carga una vez desde assets/dataset.json
 * y las mutaciones (agregar, eliminar, deshacer) viven en memoria mientras el proceso exista.
 */
class RepositorioDataset(json: String) {
    val dataset: Dataset = formato.decodeFromString(Dataset.serializer(), json)

    /** HOY de los mockups (jueves 27 de agosto de 2026). */
    val hoy: LocalDate = LocalDate.parse(dataset.meta.hoy)

    /** Alarmas que el flujo crea al escanear (D3): fuera de la lista hasta que llegue su evento. */
    val pendientesDeEscaneo: List<Alarma> = dataset.alarmas.filter { it.esNueva }

    private fun iniciales(): List<Alarma> = dataset.alarmas.filterNot { it.esNueva }.ordenadas()

    private val _alarmas = MutableStateFlow(iniciales())
    val alarmas: StateFlow<List<Alarma>> = _alarmas.asStateFlow()

    /** Última lista antes de la mutación más reciente; la usa «Deshacer · 5 s». */
    private var anterior: List<Alarma>? = null

    /** ⏩ Primer uso: el FAB pasa por M12 solo la primera vez (docs/NAVEGACION.md §6, decisión a). */
    var permisoCamaraPedido: Boolean = false

    fun alarma(id: String): Alarma? = _alarmas.value.firstOrNull { it.id == id }

    private val _eventosQR = MutableStateFlow(dataset.eventosQR)
    val eventosQR: StateFlow<List<EventoQR>> = _eventosQR.asStateFlow()

    fun evento(id: String): EventoQR? = _eventosQR.value.firstOrNull { it.id == id }

    /** F-M07: agrega el EventoQR de una alarma creada a mano (hoy `dataset.eventosQR` solo se lee). */
    fun agregarEvento(evento: EventoQR) { _eventosQR.value = _eventosQR.value.filterNot { it.id == evento.id } + evento }

    fun agregar(alarma: Alarma) = mutar { lista -> (lista.filterNot { it.id == alarma.id } + alarma).ordenadas() }

    /**
     * Crea la alarma del evento leído (M03 → M04): toma la alarma del dataset que apunta el evento, la marca
     * «Nueva» y la agrega. Devuelve null si el evento no existe (→ M13).
     */
    fun agregarDesdeEvento(eventoId: String): Alarma? {
        val evento = evento(eventoId) ?: return null
        val base = dataset.alarmas.firstOrNull { it.id == evento.alarmaId } ?: return null
        val nueva = base.copy(esNueva = true, chips = listOf("Nueva") + base.chips.filterNot { it == "Nueva" })
        agregar(nueva)
        return nueva
    }

    fun eliminar(id: String) = mutar { lista -> lista.filterNot { it.id == id } }

    /**
     * Interruptor de la tarjeta: pausa o reactiva sin abrir un nuevo nivel de «Deshacer» (no pasa por `mutar`), pero
     * si hay una mutación pendiente de deshacer (p. ej. M05 con su snackbar de 5 s) el mismo cambio se aplica también
     * a `anterior`, así «Deshacer» solo revierte esa mutación y no un toque de interruptor hecho mientras tanto.
     */
    fun cambiarEstado(id: String, pausada: Boolean) {
        val cambio: (Alarma) -> Alarma = { if (it.id == id) it.copy(estado = if (pausada) "pausada" else "activa") else it }
        _alarmas.value = _alarmas.value.map(cambio)
        anterior = anterior?.map(cambio)
    }

    /** Un solo nivel: revierte la última mutación; una segunda llamada no hace nada (comportamiento del snackbar de 5 s). */
    fun deshacer() {
        anterior?.let { _alarmas.value = it }
        anterior = null
    }

    /** Cierra la ventana de «Deshacer» sin revertir nada (M05: al vencer los 5 s o al salir de la pantalla). */
    fun olvidarDeshacer() { anterior = null }

    /** Cuerpo de M04d/M06d con {evento}, {fecha} y {hora} rellenos (dataset.meta.notes). */
    fun mensajeEliminar(alarma: Alarma): String = dataset.mensajes.confirmarEliminarCuerpo
        .replace("{evento}", alarma.titulo)
        .replace("{fecha}", FormatoHora.fechaCorta(alarma.eventoInicio))
        .replace("{hora}", FormatoHora.horaConSufijo(alarma.eventoInicio))

    /** F-M09: aplica el cambio del organizador (M09 «Aceptar cambio») — no hace nada si la alarma no existe o no tiene `cambioDelOrganizador`. */
    fun aplicarCambioOrganizador(id: String) {
        val alarma = alarma(id) ?: return
        val cambio = alarma.cambioDelOrganizador ?: return
        agregar(alarma.copy(eventoInicio = cambio.nuevoInicio, suena = cambio.nuevaHoraDeAlarma))
    }

    private fun mutar(cambio: (List<Alarma>) -> List<Alarma>) {
        anterior = _alarmas.value
        _alarmas.value = cambio(_alarmas.value)
    }

    private fun List<Alarma>.ordenadas() = sortedBy { OffsetDateTime.parse(it.eventoInicio) }

    /** Restaura el estado inicial (5 alarmas, sin deshacer pendiente ni permiso pedido); solo para pruebas. */
    @VisibleForTesting
    fun reiniciar() {
        _alarmas.value = iniciales()
        anterior = null
        permisoCamaraPedido = false
    }

    companion object {
        private val formato = Json { ignoreUnknownKeys = true }

        @Volatile private var instancia: RepositorioDataset? = null

        /** Instancia única por proceso, leída de assets/dataset.json. */
        fun desdeAssets(context: Context): RepositorioDataset =
            instancia ?: synchronized(this) {
                instancia ?: RepositorioDataset(
                    context.applicationContext.assets.open("dataset.json").bufferedReader().use { it.readText() }
                ).also { instancia = it }
            }

        /** Olvida la instancia única; solo para pruebas que necesitan una instancia nueva entre casos. */
        @VisibleForTesting
        fun olvidarInstancia() { instancia = null }
    }
}
