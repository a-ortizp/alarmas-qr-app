package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** DS comp. 25 «Sello verificado»: tramo de texto «✓ verificado» Archivo Bold 13.5 Verde Texto (M04, mockup 4:209). */
@Composable
fun SelloVerificado(modifier: Modifier = Modifier) {
    Text("✓ verificado", style = Tipografia.Dato.copy(fontWeight = FontWeight.Bold), color = Colores.VerdeTexto, modifier = modifier)
}

/**
 * Tarjeta del evento de M04 (mockup 4:201): 350×144, blanco, borde 1.5 Gris Borde, radio 14, relleno 16/14, gap 8,
 * sombra 0 4 12; título Bricolage SemiBold 20 y filas FECHA · LUGAR · ORGANIZA · DETALLE (etiqueta Bold 12 +8 % a
 * 74 de ancho, valor Archivo 13.5).
 */
@Composable
fun TarjetaEvento(alarma: Alarma, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth()
            .shadow(Elevaciones.Tarjeta, Radios.Tarjeta, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
            .background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta)
            .padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingTarjeta),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTarjeta),
    ) {
        Text(alarma.titulo, style = Tipografia.TituloEvento, color = Colores.Tinta)
        FilaDato("FECHA") { Text(FormatoHora.fechaLarga(alarma.eventoInicio), style = Tipografia.Dato, color = Colores.Tinta) }
        alarma.lugar?.let { lugar -> FilaDato("LUGAR") { Text(lugar, style = Tipografia.Dato, color = Colores.Tinta) } }
        alarma.organizador?.let { org ->
            FilaDato("ORGANIZA") {
                Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapDivisor)) {
                    Text(org.nombre, style = Tipografia.Dato, color = Colores.Tinta)
                    if (org.verificado) SelloVerificado()
                }
            }
        }
        alarma.detalle?.let { detalle -> FilaDato("DETALLE") { Text("$detalle.", style = Tipografia.Dato, color = Colores.Tinta) } }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        Text(etiqueta, style = Tipografia.EtiquetaDato, color = Colores.GrisTexto, modifier = Modifier.width(Medidas.EtiquetaDato))
        valor()
    }
}
