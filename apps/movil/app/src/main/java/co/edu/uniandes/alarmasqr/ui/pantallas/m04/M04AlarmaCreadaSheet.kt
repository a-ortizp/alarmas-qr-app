package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.TarjetaEvento
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M04 · Alarma programada (F-M04): hoja sobre la lista; «Listo» → M05, el enlace de descarte abre M04d. */
@Composable
fun M04AlarmaCreadaSheet(
    estado: EstadoAlarmaCreada, mensajes: Mensajes,
    alListo: () -> Unit, alEditar: () -> Unit, alAbrirDialogo: () -> Unit, alConservar: () -> Unit, alEliminar: () -> Unit,
) {
    val alarma = estado.alarma
    Column(
        Modifier.fillMaxWidth().padding(start = Espacio.Margen, end = Espacio.Margen, bottom = Espacio.HojaInferior).testTag("pantalla-M04"),
        verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapTitulo), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(Medidas.Sello).background(Colores.VerdeConfirmado, Radios.Pildora), contentAlignment = Alignment.Center) {
                Text("✓", style = Tipografia.Boton, color = Colores.Blanco)
            }
            Text("¡Alarma programada!", style = Tipografia.BarraSuperior, color = Colores.Tinta)
        }
        ChipEstado("Datos leídos del QR — verifícalos", VarianteChip.Suave)
        TarjetaEvento(alarma)
        BloqueSonara(anticipacion = alarma.anticipacionMin, trayecto = alarma.trayectoMin, hora = alarma.suena, alEditar = alEditar)
        Text("También se agregó a Google Calendar.", style = Tipografia.Divisor, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        BotonPrimario("Listo", onClick = alListo, modifier = Modifier.testTag("listo"))
        BotonEnlace("No puedo asistir · eliminar alarma", onClick = alAbrirDialogo, color = ColorEnlace.Gris, estilo = Tipografia.EnlaceDescarte, modifier = Modifier.testTag("eliminar"))
    }
    if (estado.dialogoAbierto) {
        DialogoConfirmacion(
            titulo = mensajes.confirmarEliminarTitulo, cuerpo = estado.mensajeEliminar,
            rotuloSeguro = mensajes.confirmarEliminarSeguro, rotuloConfirmar = mensajes.confirmarEliminarAccion,
            destructivo = true, alSeguro = alConservar, alConfirmar = alEliminar,
        )
    }
}

/**
 * Bloque «Sonará» (4:215; DS §6 «la hora calculada es el héroe»): Amarillo Suave r14, relleno 16/12, gap 2;
 * «SONARÁ» H3, hora Spline Sans Mono Bold 52 + sufijo Medium 28 (D7), margen en Archivo 13 con «Editar» enlazado.
 */
@Composable
private fun BloqueSonara(anticipacion: Int, trayecto: Int, hora: String, alEditar: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().background(Colores.AmarilloSuave, Radios.Tarjeta).padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingPasosVertical),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapSonara),
    ) {
        Text("SONARÁ", style = Tipografia.H3, color = Colores.GrisTexto)
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapHoraSufijo)) {
            Text(FormatoHora.hora(hora), style = Tipografia.HoraSonara, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
            Text(FormatoHora.sufijo(hora), style = Tipografia.HoraProtagonistaSufijo, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
        }
        val margen = buildString {
            append("$anticipacion min de margen")
            if (trayecto > 0) append(" + $trayecto min de trayecto desde tu ubicación habitual")
            append(" · ")
        }
        Text(
            buildAnnotatedString {
                append(margen)
                withLink(LinkAnnotation.Clickable("editar", TextLinkStyles(SpanStyle(color = Colores.AzulTexto, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline))) { alEditar() }) { append("Editar") }
            },
            style = Tipografia.MargenSonara, color = Colores.Tinta,
        )
    }
}
