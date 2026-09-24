package co.edu.uniandes.alarmasqr.ui.pantallas.m06

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.CampoTexto
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.FilaAjuste
import co.edu.uniandes.alarmasqr.ui.componentes.Interruptor
import co.edu.uniandes.alarmasqr.ui.componentes.SelectorSegmentado
import co.edu.uniandes.alarmasqr.ui.componentes.varianteDeChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M06 · Editar alarma (F-M06): «‹ Editar alarma» con flecha (mockups v1.8). «Eliminar alarma» abre M06d. */
@Composable
fun M06EditarAlarmaScreen(
    estado: EstadoEditarAlarma, mensajes: Mensajes, mensajeEliminar: String, puedeVerCambioOrganizador: Boolean,
    alVolver: () -> Unit, alElegirAnticipacion: (Int) -> Unit, alCambiarSumarTrayecto: (Boolean) -> Unit,
    alElegirSonido: (String) -> Unit, alCambiarRespetarNoMolestar: (Boolean) -> Unit, alCambiarConfirmar: (Boolean) -> Unit,
    alCambiarNotas: (String) -> Unit,
    alTocarCambioOrganizador: () -> Unit, alGestionarCalendario: () -> Unit, alGuardar: () -> Unit,
    alAbrirDialogo: () -> Unit, alConservar: () -> Unit, alEliminar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M06")) {
        BarraSuperior("Editar alarma", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.GapTarjeta),
        ) {
            TarjetaResumenAlarma(estado.alarma, estado.chipOrigen)
            SelectorAnticipacion(estado.anticipacionMin, alElegirAnticipacion)
            FilaAjuste("Sumar trayecto desde mi ubicación") { Interruptor(estado.sumarTrayecto, alCambiarSumarTrayecto) }
            SelectorSonido(estado.sonido, alElegirSonido)
            FilaAjuste("Respetar \"No molestar\"") { Interruptor(estado.respetarNoMolestar, alCambiarRespetarNoMolestar) }
            FilaAjuste("Posponer") { ValorConChevron("${estado.posponerMin} min") }
            SeccionAjuste("CAMBIOS DEL ORGANIZADOR") {
                FilaAjuste("Confirmar antes de auto-ajustarse", alTocarFila = if (puedeVerCambioOrganizador) alTocarCambioOrganizador else null) {
                    Interruptor(estado.confirmarAntesDeAutoAjustar, alCambiarConfirmar)
                }
            }
            CampoTexto(estado.notas, alCambiarNotas, etiqueta = "Notas", lineas = 2, modifier = Modifier.testTag("notas"))
            FilaAjuste("Gestionar en el calendario", alTocarFila = alGestionarCalendario) { IconoChevron() }
            BotonPrimario("Guardar cambios", onClick = alGuardar, modifier = Modifier.testTag("guardar"))
            BotonEnlace("Eliminar alarma", onClick = alAbrirDialogo, color = ColorEnlace.Coral, modifier = Modifier.fillMaxWidth().testTag("eliminar"))
        }
    }
    if (estado.dialogoAbierto) {
        DialogoConfirmacion(
            titulo = mensajes.confirmarEliminarTitulo, cuerpo = mensajeEliminar,
            rotuloSeguro = mensajes.confirmarEliminarSeguro, rotuloConfirmar = mensajes.confirmarEliminarAccion,
            destructivo = true, alSeguro = alConservar, alConfirmar = alEliminar,
        )
    }
}

/** «hora suena» + título + «fecha corta · evento h:mm» + chip de origen (sin «Nueva»: no aporta nada al editar). */
@Composable
private fun TarjetaResumenAlarma(alarma: Alarma, chipOrigen: String?, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().clip(Radios.Tarjeta).background(Colores.Blanco).border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta)
            .padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingTarjeta),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapHoraSufijo)) {
            Text(FormatoHora.hora(alarma.suena), style = Tipografia.HoraTarjeta, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
            Text(FormatoHora.sufijo(alarma.suena), style = Tipografia.HoraAmPm, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoTarjeta)) {
            Text(alarma.titulo, style = Tipografia.TituloEvento, color = Colores.Tinta)
            Text(subtituloEvento(alarma), style = Tipografia.Etiqueta, color = Colores.GrisTexto)
            chipOrigen?.let { ChipEstado(it, varianteDeChip(it)) }
        }
    }
}

/** «dom 30 · evento 4:00 pm»: sin el lugar (a diferencia de `FormatoHora.lineaEvento`), la tarjeta de M06 no lo repite. */
private fun subtituloEvento(alarma: Alarma): String {
    val etiqueta = alarma.etiquetaEvento ?: "evento"
    return "${FormatoHora.fechaCorta(alarma.eventoInicio)} · $etiqueta ${FormatoHora.horaConSufijo(alarma.eventoInicio)}"
}

@Composable
private fun SeccionAjuste(titulo: String, modifier: Modifier = Modifier, contenido: @Composable () -> Unit) {
    Column(modifier.fillMaxWidth().padding(top = Espacio.AntesSeccion), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text(titulo, style = Tipografia.H3, color = Colores.GrisTexto)
        contenido()
    }
}

@Composable
private fun ValorConChevron(texto: String, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        Text(texto, style = Tipografia.Etiqueta, color = Colores.GrisTexto)
        IconoChevron()
    }
}

@Composable
private fun IconoChevron(modifier: Modifier = Modifier) {
    Text("›", style = Tipografia.Chevron, color = Colores.GrisTexto, modifier = modifier)
}

private val OPCIONES_ANTICIPACION = listOf(10, 30, 60)
private fun etiquetaAnticipacion(min: Int) = if (min < 60) "$min min" else "1 h"

@Composable
private fun SelectorAnticipacion(seleccionado: Int, alElegir: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text("ANTICIPACIÓN", style = Tipografia.H3, color = Colores.GrisTexto)
        // «Otro»: representa el valor actual cuando no es 10/30/60; sin selector de minutos personalizado (maquetación).
        val opciones = OPCIONES_ANTICIPACION.map { etiquetaAnticipacion(it) to (seleccionado == it) } + ("Otro" to (seleccionado !in OPCIONES_ANTICIPACION))
        SelectorSegmentado(opciones, alElegir = { i -> OPCIONES_ANTICIPACION.getOrNull(i)?.let(alElegir) }, modifier = Modifier.fillMaxWidth())
    }
}

private val OPCIONES_SONIDO = listOf("sonar" to "Sonar", "vibrar" to "Vibrar", "silencio" to "Silencio")

@Composable
private fun SelectorSonido(seleccionado: String, alElegir: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text("SONIDO", style = Tipografia.H3, color = Colores.GrisTexto)
        val opciones = OPCIONES_SONIDO.map { (valor, texto) -> texto to (seleccionado == valor) }
        SelectorSegmentado(opciones, alElegir = { i -> alElegir(OPCIONES_SONIDO[i].first) }, modifier = Modifier.fillMaxWidth())
    }
}
