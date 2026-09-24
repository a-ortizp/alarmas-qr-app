package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 06 «Campo de texto» (v1.1): etiqueta dentro del contorno en Archivo SemiBold 12 Gris Texto y mayúsculas,
 * valor Archivo 15, altura 48, radio 12, borde 1.5 Gris Borde; con foco borde 2 Tinta (mockup M00b). La pista de
 * formato («tucorreo@ejemplo.com») se muestra en Gris Medio cuando el valor está vacío.
 */
@Composable
fun CampoTexto(
    valor: String, alCambiar: (String) -> Unit, etiqueta: String, modifier: Modifier = Modifier,
    pista: String = "", contrasena: Boolean = false, lineas: Int = 1,
) {
    val interaccion = remember { MutableInteractionSource() }
    val enfocado by interaccion.collectIsFocusedAsState()
    val borde = if (enfocado) Trazos.Foco else Trazos.Borde
    val colorBorde = if (enfocado) Colores.Tinta else Colores.GrisBorde
    val alto = if (lineas <= 1) Tamanos.Campo else Tamanos.Campo + Espacio.LineaCampoExtra * (lineas - 1)
    BasicTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = modifier.fillMaxWidth().height(alto),
        singleLine = lineas <= 1,
        interactionSource = interaccion,
        textStyle = Tipografia.ValorCampo.copy(color = Colores.Tinta),
        cursorBrush = SolidColor(Colores.Tinta),
        visualTransformation = if (contrasena) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (contrasena) KeyboardType.Password else KeyboardType.Email),
        decorationBox = { campo ->
            Column(
                Modifier.fillMaxWidth().height(alto).background(Colores.Blanco, Radios.Campo).border(borde, colorBorde, Radios.Campo)
                    .padding(horizontal = Espacio.Medianil, vertical = Espacio.PaddingCampoVertical),
                verticalArrangement = Arrangement.spacedBy(Espacio.GapCampo),
            ) {
                Text(etiqueta.uppercase(), style = Tipografia.EtiquetaCampo, color = Colores.GrisTexto)
                Box {
                    if (valor.isEmpty()) Text(pista, style = Tipografia.ValorCampo, color = Colores.GrisMedio)
                    campo()
                }
            }
        },
    )
}

/** Solo el dibujo de la casilla (sin semántica): lo usan [Casilla] y la fila de opción, cuyo control es toda la fila. */
@Composable
fun CasillaVisual(marcada: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier.size(Medidas.Casilla)
            .background(if (marcada) Colores.Tinta else Colores.Blanco, Radios.Casilla)
            .border(Trazos.Foco, Colores.Tinta, Radios.Casilla),
        contentAlignment = Alignment.Center,
    ) { if (marcada) Text("✓", style = Tipografia.Chip, color = Colores.Blanco) }
}

/** DS comp. 11 «Checkbox»: 20×20, borde 2 Tinta, radio 4; marcada = relleno Tinta con «✓» blanco (color + forma). */
@Composable
fun Casilla(marcada: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier, texto: (@Composable () -> Unit)? = null) {
    Row(
        modifier = modifier.toggleable(value = marcada, role = Role.Checkbox, onValueChange = alCambiar),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.Top,
    ) {
        CasillaVisual(marcada)
        texto?.invoke()
    }
}
