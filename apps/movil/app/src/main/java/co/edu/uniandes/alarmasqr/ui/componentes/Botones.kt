package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 01 «Botón primario»: píldora de 52, Amarillo Energía con texto Tinta. Es el único amarillo de la
 * pantalla; [sobreAmarillo] es la variante de M01 (relleno Tinta, texto blanco) porque el fondo ya es amarillo.
 */
@Composable
fun BotonPrimario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, sobreAmarillo: Boolean = false, habilitado: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier.fillMaxWidth().height(Tamanos.Boton),
        shape = Radios.Pildora,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (sobreAmarillo) Colores.Tinta else Colores.AmarilloEnergia,
            contentColor = if (sobreAmarillo) Colores.Blanco else Colores.Tinta,
            disabledContainerColor = Colores.GrisNiebla,
            disabledContentColor = Colores.GrisTexto,
        ),
        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
    ) { Text(texto, style = Tipografia.Boton) }
}

/** DS comp. 02 «Botón secundario»: contorno 1.5 Tinta (o blanco sobre Tinta, comp. 32), texto del mismo color. */
@Composable
fun BotonSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false) {
    val color = if (sobreTinta) Colores.Blanco else Colores.Tinta
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(Tamanos.Boton),
        shape = Radios.Pildora,
        border = BorderStroke(Trazos.Borde, color),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = color),
        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
    ) { Text(texto, style = Tipografia.Boton) }
}

enum class ColorEnlace(val color: Color) {
    Azul(Colores.AzulTexto), Tinta(Colores.Tinta), Gris(Colores.GrisTexto), Coral(Colores.CoralTexto), Blanco(Colores.Blanco)
}

/**
 * DS comp. 04 «Botón de texto / enlace»: 32 de alto, relleno horizontal 20, texto subrayado Bold 15 (o el [estilo]
 * dado: Medium 14 en el descarte de M04, Bold 14 en M13). La acción destructiva dentro de una pantalla es un enlace
 * Coral Texto, nunca una píldora (DS v1.6).
 */
@Composable
fun BotonEnlace(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: ColorEnlace = ColorEnlace.Azul, estilo: TextStyle = Tipografia.Enlace) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(Medidas.Enlace),
        shape = Radios.Pildora,
        colors = ButtonDefaults.textButtonColors(contentColor = color.color),
        contentPadding = PaddingValues(horizontal = Espacio.Margen),
    ) { Text(texto, style = estilo.copy(textDecoration = TextDecoration.Underline)) }
}

/** Flecha «‹» de la barra superior: área táctil 44×44, Archivo Bold 26 (DS §6). testTag `atras`. */
@Composable
fun BotonAtras(onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false) {
    TextButton(
        onClick = onClick,
        modifier = modifier.size(Medidas.BotonAtras).testTag("atras").semantics { role = Role.Button; contentDescription = "Atrás" },
        shape = Radios.Pildora,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = if (sobreTinta) Colores.Blanco else Colores.Tinta),
    ) { Box(contentAlignment = Alignment.Center) { Text("‹", style = Tipografia.FlechaAtras) } }
}
