package co.edu.uniandes.alarmasqr.ui.pantallas.m11

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.FilaAjuste
import co.edu.uniandes.alarmasqr.ui.componentes.Interruptor
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
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M11")) {
        BarraSuperior("Ajustes")
        ColumnaDesplazable(Modifier.fillMaxSize(), relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton)) {
            SeccionAjustes("NOTIFICACIONES") {
                FilaAjuste("No molestar") { Interruptor(estado.ajustes.respetarNoMolestar, alCambiarNoMolestar) }
                FilaAjuste("Confirmar antes de auto-ajustar") { Interruptor(estado.ajustes.confirmarAntesDeAutoAjustar, alCambiarConfirmarAutoAjustar) }
            }
            SeccionAjustes("PERMISOS") {
                FilaAjuste("Alarmas exactas") { EstadoPermiso(estado.ajustes.permisos.alarmasExactas) }
                FilaAjuste("Notificaciones") { EstadoPermiso(estado.ajustes.permisos.notificaciones) }
                FilaAjuste("Batería sin restricciones") { EstadoPermiso(estado.ajustes.permisos.bateriaSinRestricciones) }
            }
            SeccionAjustes("CALENDARIOS") {
                FilaAjuste("Vinculados: ${estado.ajustes.calendariosVinculados.joinToString()}") { }
            }
            SeccionAjustes("CUENTA") {
                FilaAjuste("Cerrar sesión", alTocarFila = alAbrirDialogo, modifier = Modifier.testTag("cerrar-sesion")) { }
            }
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
    Column(modifier.fillMaxWidth().padding(top = Espacio.EntreBloques), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text(titulo, style = Tipografia.H3, color = Colores.GrisTexto)
        contenido()
    }
}

@Composable
private fun EstadoPermiso(concedido: Boolean, modifier: Modifier = Modifier) {
    Text(if (concedido) "Concedido" else "Falta", style = Tipografia.Etiqueta, color = if (concedido) Colores.VerdeTexto else Colores.CoralTexto, modifier = modifier)
}
