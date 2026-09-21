package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 10 «Switch» (v1.1, Tinta): 44×26; activo = pista Tinta con perilla blanca a la derecha; inactivo = pista
 * blanca con borde Gris Borde y perilla Gris Medio a la izquierda (color + forma). Transición 250 ms.
 */
@Composable
fun Interruptor(activo: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val pista by animateColorAsState(if (activo) Colores.Tinta else Colores.Blanco, tween(Movimiento.TransicionMs), label = "pista")
    val perilla by animateColorAsState(if (activo) Colores.Blanco else Colores.GrisMedio, tween(Movimiento.TransicionMs), label = "perilla")
    val margen = (Medidas.Switch.height - Medidas.Perilla) / 2
    val desplazamiento by animateDpAsState(if (activo) Medidas.Switch.width - Medidas.Perilla - margen else margen, tween(Movimiento.TransicionMs), label = "perilla-x")
    Box(
        modifier.size(Medidas.Switch)
            .toggleable(value = activo, role = Role.Switch, onValueChange = alCambiar)
            .background(pista, Radios.Pildora)
            .then(if (activo) Modifier else Modifier.border(Trazos.Borde, Colores.GrisBorde, Radios.Pildora)),
        contentAlignment = Alignment.CenterStart,
    ) { Box(Modifier.offset(x = desplazamiento).size(Medidas.Perilla).background(perilla, Radios.Pildora)) }
}
