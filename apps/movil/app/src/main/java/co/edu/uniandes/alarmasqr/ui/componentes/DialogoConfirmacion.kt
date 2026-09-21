package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 47 «Diálogo de confirmación · móvil» (M04d, M06d, M11d): velo Tinta 55 % sobre la pantalla de origen,
 * diálogo de 342 (390 − 2×24), radio 20, relleno 24, gap 16; título H2, cuerpo Archivo 14/140 %, dos botones de 52
 * apilados a 10: la acción segura es el primario amarillo y la que confirma va en contorno 1.5 (Coral Texto si
 * [destructivo], Tinta si solo sale). Tocar el velo o el botón atrás equivale a la acción segura. Va en una ventana
 * propia (`Dialog`) para cubrir también la hoja inferior de M04; el atenuado del sistema se apaga para que el velo
 * sea exactamente `Colores.VeloMovil`.
 */
@Composable
fun DialogoConfirmacion(
    titulo: String, cuerpo: String, rotuloSeguro: String, rotuloConfirmar: String, destructivo: Boolean,
    alSeguro: () -> Unit, alConfirmar: () -> Unit,
) {
    Dialog(onDismissRequest = alSeguro, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        val vista = LocalView.current
        SideEffect { (vista.parent as? DialogWindowProvider)?.window?.setDimAmount(0f) }
        val sinOndas = remember { MutableInteractionSource() }
        Box(
            Modifier.fillMaxSize().background(Colores.VeloMovil).clickable(interactionSource = sinOndas, indication = null, onClick = alSeguro).testTag("velo"),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                Modifier.width(Tamanos.DialogoAncho)
                    .shadow(Elevaciones.Tarjeta, Radios.Dialogo, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
                    .background(Colores.Blanco, Radios.Dialogo)
                    .clickable(interactionSource = sinOndas, indication = null) { /* absorbe el toque: no cierra */ }
                    .padding(Espacio.PaddingDialogo).testTag("dialogo-confirmacion"),
                verticalArrangement = Arrangement.spacedBy(Espacio.GapDialogo),
            ) {
                Text(titulo, style = Tipografia.TituloDialogo, color = Colores.Tinta)
                Text(cuerpo, style = Tipografia.CuerpoDialogo, color = Colores.Tinta)
                Column(verticalArrangement = Arrangement.spacedBy(Espacio.EntreBotonesDialogo)) {
                    BotonPrimario(rotuloSeguro, onClick = alSeguro, modifier = Modifier.testTag("dialogo-seguro"))
                    val color = if (destructivo) Colores.CoralTexto else Colores.Tinta
                    OutlinedButton(
                        onClick = alConfirmar,
                        modifier = Modifier.fillMaxWidth().height(Tamanos.Boton).testTag("dialogo-confirmar"),
                        shape = Radios.Pildora,
                        border = BorderStroke(Trazos.Borde, color),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = color),
                        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
                    ) { Text(rotuloConfirmar, style = Tipografia.Boton) }
                }
            }
        }
    }
}
