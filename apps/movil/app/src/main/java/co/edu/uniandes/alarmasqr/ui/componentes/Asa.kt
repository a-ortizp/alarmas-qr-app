package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios

/** Asa de la hoja (DS comp. 30 «Hoja inferior»): 36×4, Gris Borde, píldora. */
@Composable
fun Asa(modifier: Modifier = Modifier) {
    Box(modifier.width(Medidas.Asa.width).height(Medidas.Asa.height).background(Colores.GrisBorde, Radios.Pildora))
}
