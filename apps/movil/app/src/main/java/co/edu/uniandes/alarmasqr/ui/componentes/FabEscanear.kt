package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * DS comp. 05 «FAB extendido “Escanear”»: 56, radio 16, Amarillo Energía, icono escanear 26 + Archivo Bold 16,
 * relleno 20/24, sombra 0 4 12. Toque → cámara; mantener 500 ms (`Movimiento.ToqueLargoMs`, no los 400 del sistema)
 * → hoja «Agregar evento». Es el único amarillo de M02/M02b/M05.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FabEscanear(alTocar: () -> Unit, alMantener: () -> Unit, modifier: Modifier = Modifier) {
    val base = LocalViewConfiguration.current
    val con500 = remember(base) { object : ViewConfiguration by base { override val longPressTimeoutMillis: Long get() = Movimiento.ToqueLargoMs } }
    CompositionLocalProvider(LocalViewConfiguration provides con500) {
        Surface(
            modifier = modifier.height(Tamanos.Fab),
            shape = Radios.Fab,
            color = Colores.AmarilloEnergia,
            contentColor = Colores.Tinta,
            shadowElevation = Elevaciones.Fab,
        ) {
            Row(
                Modifier.testTag("fab-escanear")
                    .combinedClickable(role = Role.Button, onClick = alTocar, onLongClick = alMantener)
                    .padding(start = Espacio.PaddingFabInicio, end = Espacio.PaddingFabFin),
                horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Iconos.Escanear, contentDescription = null, modifier = Modifier.size(Medidas.IconoFab))
                Text("Escanear", style = Tipografia.Destacado)
            }
        }
    }
}
