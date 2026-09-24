package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 19 «Chip de estado» (no se toca): 20 de alto, relleno 12/3, píldora, Archivo 12. Origen siempre
 * («Creada por mí» contorno Tinta, «✓ Escaneada» contorno Verde Texto); estado temporal «Nueva» relleno Tinta.
 * `Coral` = «enlace externo» (M13); `Suave` = «Datos leídos del QR» (M04, Amarillo Suave sin borde).
 */
enum class VarianteChip(val fondo: Color, val borde: Color?, val texto: Color) {
    CreadaPorMi(Color.Transparent, Colores.Tinta, Colores.Tinta),
    Escaneada(Color.Transparent, Colores.VerdeTexto, Colores.VerdeTexto),
    Nueva(Colores.Tinta, null, Colores.Blanco),
    Coral(Colores.CoralSuave, Colores.CoralTexto, Colores.CoralTexto),
    Suave(Colores.AmarilloSuave, null, Colores.Tinta),
    AlertaTexto(Color.Transparent, Colores.CoralTexto, Colores.CoralTexto),  // «Revisar» (M11, permiso del sistema faltante)
}

@Composable
fun ChipEstado(texto: String, variante: VarianteChip, modifier: Modifier = Modifier) {
    Box(
        modifier.height(Tamanos.Chip)
            .background(variante.fondo, Radios.Pildora)
            .then(variante.borde?.let { Modifier.border(Trazos.Borde, it, Radios.Pildora) } ?: Modifier)
            .padding(horizontal = Espacio.Medianil),
        contentAlignment = Alignment.Center,
    ) { Text(texto, style = Tipografia.Chip, color = variante.texto, maxLines = 1) }
}

/** Variante del chip de estado por el texto del dataset: «Nueva», «✓ Escaneada» o «Creada por mí». */
fun varianteDeChip(texto: String): VarianteChip = when {
    texto == "Nueva" -> VarianteChip.Nueva
    texto.contains("Escaneada") -> VarianteChip.Escaneada
    else -> VarianteChip.CreadaPorMi
}

/**
 * DS set 49 «Chip como control» (v1.11): 32 de alto, relleno lateral 14, radio 16. Inactivo contorno 1.5 (Tinta;
 * Gris Medio sobre Tinta, comp. 33), activo relleno Tinta con texto blanco (blanco con texto Tinta sobre Tinta).
 */
@Composable
fun ChipControl(texto: String, activo: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false) {
    val fondo = when { !activo -> Color.Transparent; sobreTinta -> Colores.Blanco; else -> Colores.Tinta }
    val color = when { activo && sobreTinta -> Colores.Tinta; activo -> Colores.Blanco; sobreTinta -> Colores.Blanco; else -> Colores.Tinta }
    val borde = if (sobreTinta) Colores.GrisMedio else Colores.Tinta
    Box(
        modifier.height(Tamanos.ChipControl)
            .toggleable(value = activo, role = Role.Switch, onValueChange = { onClick() })
            .background(fondo, Radios.Pildora)
            .then(if (activo) Modifier else Modifier.border(Trazos.Borde, borde, Radios.Pildora))
            .padding(horizontal = Espacio.PaddingChipControl),
        contentAlignment = Alignment.Center,
    ) { Text(texto, style = Tipografia.ChipControl, color = color, maxLines = 1) }
}

/**
 * Selector segmentado (M06 ANTICIPACIÓN/SONIDO, M02b «Lista/Mes»): una sola pista Gris Niebla en píldora con el
 * segmento activo relleno Tinta flotando adentro — a diferencia de [ChipControl], que dibuja cada opción como una
 * píldora independiente con su propio contorno. El ancho lo decide quien llama (p. ej. `fillMaxWidth()` en M06,
 * `width(IntrinsicSize.Min)` para la pista compacta de M02b); [modificadorSegmento] deja marcar un segmento puntual
 * (`testTag`) sin exponer la fila interna.
 */
@Composable
fun SelectorSegmentado(
    opciones: List<Pair<String, Boolean>>, alElegir: (Int) -> Unit, modifier: Modifier = Modifier,
    modificadorSegmento: (Int) -> Modifier = { Modifier },
) {
    Row(
        modifier.background(Colores.GrisNiebla, Radios.Pildora).padding(Espacio.PistaSegmento),
        horizontalArrangement = Arrangement.spacedBy(Espacio.PistaSegmento),
    ) {
        opciones.forEachIndexed { i, (texto, activo) ->
            Box(
                Modifier.weight(1f).height(Tamanos.ChipControl).clip(Radios.Pildora)
                    .background(if (activo) Colores.Tinta else Color.Transparent)
                    .clickable(role = Role.Button, onClick = { alElegir(i) })
                    .then(modificadorSegmento(i)),
                contentAlignment = Alignment.Center,
            ) { Text(texto, style = Tipografia.ChipControl, color = if (activo) Colores.Blanco else Colores.Tinta, maxLines = 1) }
        }
    }
}
