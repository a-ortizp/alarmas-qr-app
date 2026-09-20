package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * DS comp. 26 «Snackbar “Deshacer”»: 350×48 Tinta radio 12, relleno 14/10, mensaje Archivo 13 blanco (2 líneas)
 * y la acción «Deshacer · 5 s» Bold 12 subrayada en un marco de 110×26 (áreas de toque, NAVEGACION §6 d).
 */
@Composable
fun SnackbarDeshacer(datos: SnackbarData, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton).height(Medidas.Snackbar)
            .background(Colores.Tinta, Radios.Snackbar).padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingTarjetaVertical)
            .testTag("snackbar"),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(datos.visuals.message, style = Tipografia.Etiqueta, color = Colores.Blanco, maxLines = 2, modifier = Modifier.weight(1f))
        datos.visuals.actionLabel?.let { rotulo ->
            Box(
                Modifier.size(Medidas.AccionSnackbar.width, Medidas.AccionSnackbar.height).clickable(role = Role.Button) { datos.performAction() }.testTag("deshacer"),
                contentAlignment = Alignment.CenterEnd,
            ) { Text(rotulo, style = Tipografia.Chip.copy(textDecoration = TextDecoration.Underline), color = Colores.Blanco) }
        }
    }
}
