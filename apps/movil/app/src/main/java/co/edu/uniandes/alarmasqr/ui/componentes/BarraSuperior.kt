package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 12 «Barra superior móvil»: 56, relleno lateral 16, gap 12, borde inferior 1.5; título Bricolage Bold 22
 * a la izquierda y el control de la derecha separados por un espaciador flexible (mockups v1.7). Con [alVolver]
 * dibuja la flecha «‹» de 44×44. [sobreTinta] = M03 (fondo Tinta, texto blanco, borde blanco 15 %).
 */
@Composable
fun BarraSuperior(titulo: String, modifier: Modifier = Modifier, alVolver: (() -> Unit)? = null, sobreTinta: Boolean = false, accion: @Composable RowScope.() -> Unit = {}) {
    val fondo = if (sobreTinta) Colores.Tinta else Colores.Blanco
    val borde = if (sobreTinta) Colores.BordeSobreTinta else Colores.GrisBorde
    Row(
        modifier.fillMaxWidth().height(Tamanos.BarraSuperior).background(fondo)
            .drawBehind { val y = size.height - Trazos.Borde.toPx() / 2; drawLine(borde, Offset(0f, y), Offset(size.width, y), Trazos.Borde.toPx()) }
            .padding(horizontal = Espacio.PaddingBarra),
        horizontalArrangement = Arrangement.spacedBy(Espacio.Medianil),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        alVolver?.let { BotonAtras(onClick = it, sobreTinta = sobreTinta) }
        Text(titulo, style = Tipografia.BarraSuperior, color = if (sobreTinta) Colores.Blanco else Colores.Tinta)
        Spacer(Modifier.weight(1f))
        accion()
    }
}
