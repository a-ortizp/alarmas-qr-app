package co.edu.uniandes.alarmasqr.ui.pantallas.m07

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.CampoTexto
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.SelectorSegmentado
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

private val OPCIONES_ANTICIPACION = listOf(10, 30, 60)
private fun etiquetaAnticipacion(min: Int) = if (min < 60) "$min min" else "1 h"

/**
 * M07 · Crear evento a mano (F-M07): campo vacío muestra solo su etiqueta, sin valor de ejemplo ni pista (revisión
 * de tutores, MOCKUPS.md §7 paso 5 noveno comentario). Fecha y hora se precargan (MISO no tiene selector de fecha
 * construido todavía: se muestran de solo lectura con el valor por defecto, mañana 10:00 am) en dos campos lado a
 * lado. «Guardar y crear QR» entrega los campos a la entrada de navegación, que arma la `Alarma`/`EventoQR` vía
 * `RepositorioDataset.crearAlarmaManual`.
 */
@Composable
fun M07CrearEventoScreen(
    alVolver: () -> Unit,
    alGuardar: (titulo: String, lugar: String?, descripcion: String?, anticipacionMin: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var lugar by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var anticipacionMin by rememberSaveable { mutableStateOf(30) }

    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M07")) {
        BarraSuperior("Nuevo evento", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            CampoTexto(titulo, { titulo = it }, etiqueta = "Título", modifier = Modifier.testTag("titulo"))
            Row(horizontalArrangement = Arrangement.spacedBy(Espacio.Medianil)) {
                CampoTexto("Mañana", {}, etiqueta = "Fecha", modifier = Modifier.weight(1f).testTag("fecha"))
                CampoTexto("10:00 am", {}, etiqueta = "Hora", modifier = Modifier.weight(1f).testTag("hora"))
            }
            CampoTexto(lugar, { lugar = it }, etiqueta = "Lugar (opcional)", modifier = Modifier.testTag("lugar"))
            CampoTexto(descripcion, { descripcion = it }, etiqueta = "Descripción (opcional)", lineas = 3, modifier = Modifier.testTag("descripcion"))
            Column(verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
                Text("ANTICIPACIÓN", style = Tipografia.H3, color = Colores.GrisTexto)
                val opciones = OPCIONES_ANTICIPACION.map { etiquetaAnticipacion(it) to (anticipacionMin == it) } + ("Otro" to (anticipacionMin !in OPCIONES_ANTICIPACION))
                SelectorSegmentado(opciones, alElegir = { i -> OPCIONES_ANTICIPACION.getOrNull(i)?.let { anticipacionMin = it } })
            }
            TarjetaQRAutomatico()
            BotonPrimario(
                "Guardar y crear QR",
                onClick = { alGuardar(titulo, lugar.ifBlank { null }, descripcion.ifBlank { null }, anticipacionMin) },
                modifier = Modifier.testTag("guardar"),
            )
        }
    }
}

/** Aviso de que el QR se genera automáticamente (F-M07): el evento no existe todavía, así que el QR es solo ilustrativo. */
@Composable
private fun TarjetaQRAutomatico(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().background(Colores.GrisNiebla, Radios.Tarjeta).padding(Espacio.Medianil),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFilaHoja),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CodigoQR("alarmasqr://evento/nuevo", tamano = Medidas.QRAyuda)
        Text(
            "El código QR del evento se genera automáticamente al guardar, listo para compartir.",
            style = Tipografia.Etiqueta, color = Colores.GrisTexto,
        )
    }
}
