package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * Fila de ajuste (revisión de tutores del 2026-09-19, MOCKUPS.md §7 comentario 8): 40 de alto, contenido centrado,
 * rótulo a la izquierda y [control] (un [Interruptor] o «›») a la derecha. [alTocarFila], si no es null, hace
 * clicable el rótulo por separado de [control] — M06 necesita las dos cosas en la misma fila: el switch cambia la
 * preferencia «Confirmar antes de auto-ajustarse» y tocar el rótulo navega a M09 (⏩, simula el push del organizador).
 */
@Composable
fun FilaAjuste(texto: String, modifier: Modifier = Modifier, alTocarFila: (() -> Unit)? = null, control: @Composable () -> Unit) {
    Row(
        modifier.fillMaxWidth().height(Medidas.FilaAjuste),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val rotulo = if (alTocarFila != null) Modifier.clickable(role = Role.Button, onClick = alTocarFila) else Modifier
        Text(texto, style = Tipografia.Opcion, color = Colores.Tinta, modifier = Modifier.weight(1f).then(rotulo))
        control()
    }
}
