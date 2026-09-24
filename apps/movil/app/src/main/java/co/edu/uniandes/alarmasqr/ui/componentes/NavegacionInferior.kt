package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

private data class Pestana(val pantalla: Pantalla, val rotulo: String, val icono: ImageVector, val codigosActivos: Set<String>)

private val pestanas = listOf(
    Pestana(Pantalla.M02, "Alarmas", Iconos.Alarma, setOf("M02", "M02v", "M05")),
    Pestana(Pantalla.M02b, "Calendario", Iconos.Calendario, setOf("M02b")),
    Pestana(Pantalla.M11, "Ajustes", Iconos.Ajustes, setOf("M11")),
)

/**
 * DS comp. 13 «Navegación inferior móvil»: 64, borde superior 1.5, relleno lateral 36, tres pestañas distribuidas;
 * píldora 40×22 (activa Gris Niebla, DS §6 / D6) con icono 20 y rótulo 11 (Bold Tinta activo, Medium Gris Texto inactivo).
 */
@Composable
fun NavegacionInferior(activa: Pantalla, alCambiar: (Pantalla) -> Unit, modifier: Modifier = Modifier) {
    // El inset real de WindowInsets.navigationBars (edge-to-edge) puede ser bastante alto (barra de 3 botones);
    // topado a Espacio.Medianil para un respiro chico en vez del hueco completo, sin volver a pegar la barra
    // al borde (el toque no depende de esto: el gesto de inicio solo intercepta el deslizar, no el toque).
    val margenInferior = minOf(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(), Espacio.Medianil)
    Row(
        modifier.fillMaxWidth().background(Colores.Blanco)
            .drawBehind { val y = Trazos.Borde.toPx() / 2; drawLine(Colores.GrisBorde, Offset(0f, y), Offset(size.width, y), Trazos.Borde.toPx()) }
            .padding(bottom = margenInferior)
            .height(Tamanos.NavegacionInferior)
            .padding(horizontal = Espacio.PaddingNavegacion),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        pestanas.forEach { p ->
            val seleccionada = activa.codigo in p.codigosActivos
            Column(
                Modifier.selectable(selected = seleccionada, role = Role.Tab, onClick = { alCambiar(p.pantalla) }).testTag("nav-${p.pantalla.codigo}"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Espacio.GapNavegacion),
            ) {
                Box(Modifier.size(Medidas.PildoraNav.width, Medidas.PildoraNav.height).background(if (seleccionada) Colores.GrisNiebla else Color.Transparent, Radios.Pildora), contentAlignment = Alignment.Center) {
                    Icon(p.icono, contentDescription = null, tint = if (seleccionada) Colores.Tinta else Colores.GrisTexto, modifier = Modifier.size(Medidas.IconoNav))
                }
                Text(
                    p.rotulo,
                    style = Tipografia.NavegacionInferior.copy(fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium),
                    color = if (seleccionada) Colores.Tinta else Colores.GrisTexto,
                )
            }
        }
    }
}
