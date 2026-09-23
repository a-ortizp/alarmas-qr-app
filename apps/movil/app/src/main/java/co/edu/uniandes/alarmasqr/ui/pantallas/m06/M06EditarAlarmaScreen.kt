package co.edu.uniandes.alarmasqr.ui.pantallas.m06

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipControl
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.FilaAjuste
import co.edu.uniandes.alarmasqr.ui.componentes.Interruptor
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M06 · Editar alarma (F-M06): sin miga de pan (MOCKUPS.md §7 paso 1). «Eliminar alarma» abre M06d. */
@Composable
fun M06EditarAlarmaScreen(
    estado: EstadoEditarAlarma, mensajes: Mensajes, mensajeEliminar: String, puedeVerCambioOrganizador: Boolean,
    alVolver: () -> Unit, alElegirAnticipacion: (Int) -> Unit, alElegirSonido: (String) -> Unit, alCambiarConfirmar: (Boolean) -> Unit,
    alTocarCambioOrganizador: () -> Unit, alGestionarCalendario: () -> Unit, alGuardar: () -> Unit,
    alAbrirDialogo: () -> Unit, alConservar: () -> Unit, alEliminar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M06")) {
        BarraSuperior("Editar alarma", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            Text(estado.alarma.titulo, style = Tipografia.H2, color = Colores.Tinta)
            SelectorAnticipacion(estado.anticipacionMin, alElegirAnticipacion)
            SelectorSonido(estado.sonido, alElegirSonido)
            FilaAjuste("Alarma conectada · Confirmar antes de auto-ajustarse", alTocarFila = if (puedeVerCambioOrganizador) alTocarCambioOrganizador else null) {
                Interruptor(estado.confirmarAntesDeAutoAjustar, alCambiarConfirmar)
            }
            BotonEnlace("Gestionar en el calendario", onClick = alGestionarCalendario, color = ColorEnlace.Azul)
            BotonPrimario("Guardar cambios", onClick = alGuardar, modifier = Modifier.testTag("guardar"))
            BotonEnlace("Eliminar alarma", onClick = alAbrirDialogo, color = ColorEnlace.Coral, modifier = Modifier.testTag("eliminar"))
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

private val OPCIONES_ANTICIPACION = listOf(10, 30, 60)
private fun etiquetaAnticipacion(min: Int) = if (min < 60) "$min min" else "1 h"

@Composable
private fun SelectorAnticipacion(seleccionado: Int, alElegir: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text("ANTICIPACIÓN", style = Tipografia.H3, color = Colores.GrisTexto)
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
            OPCIONES_ANTICIPACION.forEach { min -> ChipControl(etiquetaAnticipacion(min), activo = seleccionado == min, onClick = { alElegir(min) }) }
            // «Otro»: representa el valor actual cuando no es 10/30/60; sin selector de minutos personalizado (maquetación).
            ChipControl("Otro · ${seleccionado} min", activo = seleccionado !in OPCIONES_ANTICIPACION, onClick = {})
        }
    }
}

private val OPCIONES_SONIDO = listOf("sonar" to "Sonar", "vibrar" to "Vibrar", "silencio" to "Silencio")

@Composable
private fun SelectorSonido(seleccionado: String, alElegir: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text("SONIDO", style = Tipografia.H3, color = Colores.GrisTexto)
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
            OPCIONES_SONIDO.forEach { (valor, texto) -> ChipControl(texto, activo = seleccionado == valor, onClick = { alElegir(valor) }) }
        }
    }
}
