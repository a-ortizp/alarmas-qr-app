package co.edu.uniandes.alarmasqr.ui.pantallas.m11

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.FilaAjuste
import co.edu.uniandes.alarmasqr.ui.componentes.Interruptor
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M11 · Ajustes (F-M11): secciones de 16 de relleno superior con filas de 40 (revisión de tutores, MOCKUPS.md §7 paso 8); «Cerrar sesión» abre M11d. */
@Composable
fun M11AjustesScreen(
    estado: EstadoAjustes, mensajes: Mensajes,
    alCambiarNoMolestar: (Boolean) -> Unit, alCambiarConfirmarAutoAjustar: (Boolean) -> Unit,
    alAbrirDialogo: () -> Unit, alConservar: () -> Unit, alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ajustes = estado.ajustes
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M11")) {
        BarraSuperior("Ajustes")
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            SeccionAjustes("ALARMAS") {
                FilaAjuste("Anticipación por defecto") { ValorConChevron("${ajustes.anticipacionPorDefectoMin} min") }
                FilaAjuste("Sonido predeterminado") { ValorConChevron(etiquetaSonido(ajustes.sonidoPorDefecto)) }
                FilaAjuste("Posponer predeterminado") { ValorConChevron("${ajustes.posponerPorDefectoMin} min") }
                FilaAjuste("Respetar \"No molestar\" (vibrar)") { Interruptor(ajustes.respetarNoMolestar, alCambiarNoMolestar) }
                FilaAjuste("Confirmar reajustes del organizador") { Interruptor(ajustes.confirmarAntesDeAutoAjustar, alCambiarConfirmarAutoAjustar) }
            }
            SeccionAjustes("PERMISOS DEL SISTEMA") {
                FilaPermiso("Alarmas exactas", ajustes.permisos.alarmasExactas, "Sin este permiso, la alarma puede sonar tarde o no sonar.")
                FilaPermiso("Notificaciones", ajustes.permisos.notificaciones, "Sin este permiso, no verás el aviso de la alarma.")
                FilaPermiso("Batería sin restricciones", ajustes.permisos.bateriaSinRestricciones, "Sin este permiso, Android puede silenciar la alarma en segundo plano.")
            }
            SeccionAjustes("CALENDARIOS") {
                ajustes.calendariosVinculados.forEach { id -> FilaAjuste(nombreCalendario(id)) { ValorConChevron("Conectado") } }
            }
            SeccionAjustes("CUENTA Y DATOS") {
                FilaAjuste("Gestionar mis datos (Ley 1581)") { IconoChevron() }
                FilaAjuste("Cerrar sesión", alTocarFila = alAbrirDialogo, modifier = Modifier.testTag("cerrar-sesion")) { IconoChevron() }
            }
            Text(
                "Alarmas QR · versión 1.0 · prototipo", style = Tipografia.Nota, color = Colores.GrisTexto, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
    if (estado.dialogoAbierto) {
        DialogoConfirmacion(
            titulo = mensajes.confirmarCerrarSesionTitulo, cuerpo = mensajes.confirmarCerrarSesionCuerpo,
            rotuloSeguro = mensajes.confirmarCerrarSesionSeguro, rotuloConfirmar = mensajes.confirmarCerrarSesionAccion,
            destructivo = false, alSeguro = alConservar, alConfirmar = alCerrarSesion,
        )
    }
}

@Composable
private fun SeccionAjustes(titulo: String, modifier: Modifier = Modifier, contenido: @Composable ColumnScope.() -> Unit) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text(titulo, style = Tipografia.H3, color = Colores.GrisTexto)
        contenido()
    }
}

/** Fila de permiso con su píldora de estado; la advertencia solo se dibuja cuando falta (F-M11: «con advertencia si falta alguno»). */
@Composable
private fun FilaPermiso(etiqueta: String, concedido: Boolean, advertencia: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        FilaAjuste(etiqueta) { EstadoPermiso(concedido) }
        if (!concedido) Text(advertencia, style = Tipografia.Nota, color = Colores.GrisTexto)
    }
}

@Composable
private fun EstadoPermiso(concedido: Boolean, modifier: Modifier = Modifier) {
    if (concedido) ChipEstado("✓ activo", VarianteChip.Escaneada, modifier) else ChipEstado("Revisar", VarianteChip.AlertaTexto, modifier)
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

private fun etiquetaSonido(valor: String) = when (valor) {
    "sonar" -> "Sonar"
    "vibrar" -> "Vibrar"
    "silencio" -> "Silencio"
    else -> valor
}

/** F-M11: «Conexión opcional de Google Calendar, Outlook/Teams o calendario del teléfono» (FUNCIONALIDADES.md F-M02). */
private fun nombreCalendario(id: String) = when (id) {
    "google" -> "Google Calendar"
    "outlook" -> "Outlook/Teams"
    "telefono" -> "Calendario del teléfono"
    else -> id
}
