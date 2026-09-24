package co.edu.uniandes.alarmasqr.ui.pantallas.m09

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.CambioDelOrganizador
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * M09 · Cambio del organizador (F-M09): notificación push simulada, «antes → ahora» del evento y la hora
 * recalculada. Requiere `alarma.cambioDelOrganizador` — quien registra la entrada solo navega aquí para alarmas
 * que lo tienen (hoy, «a-entrega» y «a-semillero»).
 */
@Composable
fun M09CambioEventoScreen(alarma: Alarma, alAceptar: () -> Unit, alMantener: () -> Unit, alCerrar: () -> Unit, alVerAlarma: () -> Unit, modifier: Modifier = Modifier) {
    val cambio = requireNotNull(alarma.cambioDelOrganizador) { "M09 requiere una alarma con cambioDelOrganizador" }
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M09")) {
        BarraSuperior("Cambio en tu evento", accion = { BotonCerrar(onClick = alCerrar) })
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            ChipEstado("Notificación push · hace 2 min", VarianteChip.Notificacion)
            Text(
                "“${alarma.titulo}” cambió de hora", style = Tipografia.H1, color = Colores.Tinta,
                modifier = Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = alVerAlarma).testTag("ver-alarma"),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapDivisor), verticalAlignment = Alignment.CenterVertically) {
                Text("Cambio hecho por ${cambio.autor}", style = Tipografia.Etiqueta, color = Colores.GrisTexto)
                ChipEstado("✓ verificado", VarianteChip.Escaneada)
            }
            TarjetaAntesAhora(alarma, cambio)
            TarjetaSonara(alarma, cambio)
            Spacer(Modifier.height(Espacio.EntreBloques))
            BotonPrimario("Aceptar cambio", onClick = alAceptar, modifier = Modifier.testTag("aceptar"))
            BotonSecundario("Mantener alarma", onClick = alMantener, modifier = Modifier.testTag("mantener"))
        }
    }
}

@Composable
private fun TarjetaAntesAhora(alarma: Alarma, cambio: CambioDelOrganizador, modifier: Modifier = Modifier) {
    val antes = "${FormatoHora.fechaCorta(alarma.eventoInicio)} · ${FormatoHora.horaConSufijo(alarma.eventoInicio)}"
    val ahora = buildString {
        append(FormatoHora.fechaCorta(cambio.nuevoInicio)); append(" · "); append(FormatoHora.horaConSufijo(cambio.nuevoInicio))
        alarma.lugar?.let { append(" · "); append(it) }
    }
    Column(
        modifier.fillMaxWidth().background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta)
            .padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingTarjeta),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTarjeta),
    ) {
        FilaAntesAhora("ANTES") { Text(antes, style = Tipografia.Dato, color = Colores.GrisTexto, textDecoration = TextDecoration.LineThrough) }
        FilaAntesAhora("AHORA") { Text(ahora, style = Tipografia.Dato.copy(fontWeight = FontWeight.Bold), color = Colores.Tinta) }
    }
}

@Composable
private fun FilaAntesAhora(etiqueta: String, modifier: Modifier = Modifier, valor: @Composable () -> Unit) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        Text(etiqueta, style = Tipografia.EtiquetaDato, color = Colores.GrisTexto, modifier = Modifier.width(Medidas.EtiquetaDato))
        valor()
    }
}

@Composable
private fun TarjetaSonara(alarma: Alarma, cambio: CambioDelOrganizador, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth().background(Colores.AmarilloSuave, Radios.Tarjeta).padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingPasosVertical),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapSonara),
    ) {
        Text("SI ACEPTAS, SONARÁ", style = Tipografia.H3, color = Colores.GrisTexto)
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapHoraSufijo)) {
            Text(FormatoHora.hora(cambio.nuevaHoraDeAlarma), style = Tipografia.HoraSonara, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
            Text(FormatoHora.sufijo(cambio.nuevaHoraDeAlarma), style = Tipografia.HoraProtagonistaSufijo, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
        }
        Text(
            "${alarma.anticipacionMin} min de margen + trayecto · antes sonaba ${FormatoHora.horaConSufijo(cambio.antesSonaba)}",
            style = Tipografia.MargenSonara, color = Colores.GrisTexto,
        )
    }
}

@Composable
private fun BotonCerrar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier.size(Medidas.BotonAtras).testTag("cerrar"),
        colors = ButtonDefaults.textButtonColors(contentColor = Colores.Tinta),
    ) { Text("×", style = Tipografia.FlechaAtras) }
}
