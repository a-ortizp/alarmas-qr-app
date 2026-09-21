package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * DS set 48 «uso · fila de opción (M01)»: 350×36, blanco, radio 12, relleno 12/8; icono de línea a 20 + rótulo
 * Archivo Bold 14 + casilla 20 a la derecha. Toda la fila es el control (áreas de toque, NAVEGACION §6 d).
 */
@Composable
fun FilaOpcionCalendario(icono: ImageVector, texto: String, marcada: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().height(Medidas.FilaOpcion)
            .toggleable(value = marcada, role = Role.Checkbox, onValueChange = alCambiar)
            .background(Colores.Blanco, Radios.CajaIcono)
            .padding(horizontal = Espacio.Medianil, vertical = Espacio.PaddingFilaVertical),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icono, contentDescription = null, tint = Colores.Tinta, modifier = Modifier.size(Medidas.IconoFila))
        Text(texto, style = Tipografia.Opcion, color = Colores.Tinta, modifier = Modifier.weight(1f))
        CasillaVisual(marcada)   // decorativa: la fila entera es el control (un solo toggleable; la prueba usa assertIsOn sobre la fila)
    }
}
