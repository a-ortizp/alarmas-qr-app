package co.edu.uniandes.alarmasqr.datos

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.time.OffsetDateTime

/**
 * Único origen de datos simulados de la app (docs/PLAN_MAQUETACION.md §5). Se carga una vez desde assets/dataset.json
 * y las mutaciones (agregar, eliminar, deshacer) viven en memoria mientras el proceso exista.
 */
class RepositorioDataset(json: String) {
    val dataset: Dataset = formato.decodeFromString(Dataset.serializer(), json)

    private val _alarmas = MutableStateFlow(dataset.alarmas.sortedBy { OffsetDateTime.parse(it.eventoInicio) })
    val alarmas: StateFlow<List<Alarma>> = _alarmas.asStateFlow()

    /** Última lista antes de la mutación más reciente; la usa «Deshacer · 5 s». */
    private var anterior: List<Alarma>? = null

    /** ⏩ Primer uso: el FAB pasa por M12 solo la primera vez (docs/NAVEGACION.md §6, decisión a). */
    var permisoCamaraPedido: Boolean = false

    fun alarma(id: String): Alarma? = _alarmas.value.firstOrNull { it.id == id }

    fun evento(id: String): EventoQR? = dataset.eventosQR.firstOrNull { it.id == id }

    fun agregar(alarma: Alarma) = mutar { lista ->
        (lista.filterNot { it.id == alarma.id } + alarma).sortedBy { OffsetDateTime.parse(it.eventoInicio) }
    }

    fun eliminar(id: String) = mutar { lista -> lista.filterNot { it.id == id } }

    fun deshacer() {
        anterior?.let { _alarmas.value = it }
        anterior = null
    }

    private fun mutar(cambio: (List<Alarma>) -> List<Alarma>) {
        anterior = _alarmas.value
        _alarmas.value = cambio(_alarmas.value)
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
    }
}
