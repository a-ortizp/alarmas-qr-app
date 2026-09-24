package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * Fila de ajuste (revisión de tutores del 2026-09-19, MOCKUPS.md §7 comentario 8): 40 de alto, contenido centrado,
 * rótulo a la izquierda y [control] (un [Interruptor] o «›») a la derecha. [alTocarFila], si no es null, hace
 * clicable el rótulo por separado de [control] — M06 necesita las dos cosas en la misma fila: el switch cambia la
 * preferencia «Confirmar antes de auto-ajustarse» y tocar el rótulo navega a M09 (⏩, simula el push del organizador).
 *
 * Las filas que sí se tocan miden 48 en vez de 40: las dos reglas del DS se contradicen (fila de ajuste de 40 pt
 * frente al área táctil mínima de 48) y manda la de accesibilidad, porque 40 se queda corto para un pulgar ancho —
 * el mismo argumento con el que los tutores subieron los botones a 52. El rótulo ocupa todo el alto de la fila, así
 * que el objetivo es 48 completo y no solo la caja del texto. Las filas que solo llevan un switch (el switch tiene
 * su propia área táctil) conservan los 40 del mockup.
 */
@Composable
fun FilaAjuste(texto: String, modifier: Modifier = Modifier, alTocarFila: (() -> Unit)? = null, control: @Composable () -> Unit) {
    Row(
        modifier.fillMaxWidth().height(if (alTocarFila != null) Tamanos.AreaTactilMinima else Medidas.FilaAjuste),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val rotulo = if (alTocarFila != null) {
            Modifier.fillMaxHeight().clickable(role = Role.Button, onClick = alTocarFila).wrapContentHeight(Alignment.CenterVertically)
        } else {
            Modifier
        }
        Text(texto, style = Tipografia.Opcion, color = Colores.Tinta, modifier = Modifier.weight(1f).then(rotulo))
        control()
    }
}
