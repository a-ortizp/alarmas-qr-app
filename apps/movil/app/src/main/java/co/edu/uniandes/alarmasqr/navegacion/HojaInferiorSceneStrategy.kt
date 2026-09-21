package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import co.edu.uniandes.alarmasqr.ui.componentes.Asa
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Radios

/**
 * Hoja inferior de los mockups (M02h «Agregar evento», M04 «Alarma programada»): la entrada anterior queda visible
 * y atenuada bajo el velo Tinta 55 % (`Colores.VeloMovil`); tocar el velo saca la hoja de la pila (docs/TRAZABILIDAD.md §1).
 * Se registra antes que cualquier estrategia que no sea de superposición.
 */
@OptIn(ExperimentalMaterial3Api::class)
class HojaInferiorSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val cima = entries.lastOrNull() ?: return null
        if (cima.metadata[CLAVE] != true) return null
        @Suppress("UNCHECKED_CAST")
        return EscenaHoja(
            key = cima.contentKey as T,
            previousEntries = entries.dropLast(1),
            overlaidEntries = entries.dropLast(1),
            entrada = cima,
            alCerrar = onBack,
        )
    }

    companion object {
        private const val CLAVE = "hojaInferior"
        /** Metadatos que marcan una entrada como hoja: `entry<Pantalla.M02h>(metadata = HojaInferiorSceneStrategy.hoja())`. */
        fun hoja(): Map<String, Any> = mapOf(CLAVE to true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private data class EscenaHoja<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entrada: NavEntry<T>,
    private val alCerrar: () -> Unit,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(entrada)

    override val content: @Composable (() -> Unit) = {
        val propietario = rememberLifecycleOwner()
        ModalBottomSheet(
            onDismissRequest = alCerrar,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = Radios.Hoja,
            containerColor = Colores.Blanco,
            scrimColor = Colores.VeloMovil,
            // Figma: el asa queda 12 dp bajo el techo de la hoja (Espacio.HojaSuperior), no pegada a él.
            dragHandle = { Asa(Modifier.padding(top = Espacio.HojaSuperior)) },
        ) {
            CompositionLocalProvider(LocalLifecycleOwner provides propietario) { entrada.Content() }
        }
    }
}
