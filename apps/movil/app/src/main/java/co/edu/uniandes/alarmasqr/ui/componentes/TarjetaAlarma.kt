package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** Agrupador de día «HOY · JUEVES 27»: H3 (Archivo Bold 13, +8 %) Gris Texto. */
@Composable
fun AgrupadorDia(texto: String, modifier: Modifier = Modifier) {
    Text(texto, style = Tipografia.H3, color = Colores.GrisTexto, modifier = modifier)
}

/**
 * DS comp. 18 «Tarjeta de alarma» (anatomía única v1.3): 350×75 (53 sin chip), borde 1.5 Gris Borde (2 Verde Texto
 * si es «recién guardada»), radio 14, relleno 14/10; hora Spline Sans Mono Bold 26 + am/pm Medium 12 en la misma
 * línea, título Archivo Bold 15, «evento h:mm · lugar» 13 Gris Texto, chips debajo y el interruptor a la derecha.
 * Pausada: sin fondo (tutores v1.6), todo en Gris Texto e interruptor apagado.
 */
@Composable
fun TarjetaAlarma(alarma: Alarma, onClick: () -> Unit, alCambiarActiva: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val principal = if (alarma.pausada) Colores.GrisTexto else Colores.Tinta
    val borde = if (alarma.esNueva) Trazos.Foco else Trazos.Borde
    val colorBorde = if (alarma.esNueva) Colores.VerdeTexto else Colores.GrisBorde
    Row(
        modifier.fillMaxWidth().clip(Radios.Tarjeta).background(Colores.Blanco).border(borde, colorBorde, Radios.Tarjeta)
            .clickable(role = Role.Button, onClick = onClick).testTag("alarma-${alarma.id}")
            .padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingTarjetaVertical),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(Modifier.width(Medidas.HoraTarjetaAncho), horizontalArrangement = Arrangement.spacedBy(Espacio.GapHoraSufijo)) {
            Text(FormatoHora.hora(alarma.suena), style = Tipografia.HoraTarjeta, color = principal, modifier = Modifier.alignByBaseline())
            Text(FormatoHora.sufijo(alarma.suena), style = Tipografia.HoraAmPm, color = principal, modifier = Modifier.alignByBaseline())
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoTarjeta)) {
            Text(alarma.titulo, style = Tipografia.TituloTarjeta, color = principal)
            Text(FormatoHora.lineaEvento(alarma), style = Tipografia.Etiqueta, color = Colores.GrisTexto)
            if (alarma.chips.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
                    alarma.chips.forEach { ChipEstado(it, varianteDeChip(it)) }
                }
            }
        }
        Interruptor(activo = !alarma.pausada, alCambiar = alCambiarActiva, modifier = Modifier.testTag("interruptor-${alarma.id}"))
    }
}
