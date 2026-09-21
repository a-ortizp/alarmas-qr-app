package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Columna de pantalla con scroll vertical. Los mockups miden 390×844: en ese alto se ve idéntica al mockup y los
 * `Spacer(Modifier.weight(1f))` siguen anclando el pie abajo. En un teléfono más bajo, en horizontal, con la fuente
 * del sistema agrandada o con el teclado abierto, el contenido se desplaza en vez de cortarse.
 *
 * Por qué funciona el `weight` dentro del scroll: el alto máximo de un `verticalScroll` es infinito, y en ese caso
 * `Column` reparte el sobrante con el alto **mínimo**; `heightIn(min = maxHeight)` fija ese mínimo en el alto visible.
 * `imePadding()` encoge el área cuando aparece el teclado (con edge-to-edge, `adjustResize` ya no lo hace solo).
 * El relleno va en [relleno] (dentro del scroll), y el fondo y el `testTag` en [modifier].
 */
@Composable
fun ColumnaDesplazable(
    modifier: Modifier = Modifier,
    relleno: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    estado: ScrollState = rememberScrollState(),
    contenido: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(modifier.imePadding()) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(estado).heightIn(min = maxHeight).padding(relleno),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = contenido,
        )
    }
}
