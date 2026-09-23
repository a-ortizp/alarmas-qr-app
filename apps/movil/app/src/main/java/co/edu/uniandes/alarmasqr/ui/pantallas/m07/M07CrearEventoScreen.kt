package co.edu.uniandes.alarmasqr.ui.pantallas.m07

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.CampoTexto
import co.edu.uniandes.alarmasqr.ui.componentes.ChipControl
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M07 · Crear evento a mano (F-M07): campo vacío muestra solo su etiqueta, sin valor de ejemplo ni pista (revisión
 * de tutores, MOCKUPS.md §7 paso 5 noveno comentario). Fecha y hora se precargan (MISO no tiene selector de fecha
 * construido todavía: se muestran de solo lectura con el valor por defecto, mañana 10:00 am). «Guardar y crear QR»
 * entrega los campos a la entrada de navegación, que arma la `Alarma`/`EventoQR` vía `RepositorioDataset.crearAlarmaManual`.
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
        BarraSuperior("Crear evento", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            CampoTexto(titulo, { titulo = it }, etiqueta = "Título", modifier = Modifier.testTag("titulo"))
            CampoTexto("Mañana · 10:00 am", {}, etiqueta = "Fecha y hora", modifier = Modifier.testTag("fecha-hora"))
            CampoTexto(lugar, { lugar = it }, etiqueta = "Lugar", modifier = Modifier.testTag("lugar"))
            CampoTexto(descripcion, { descripcion = it }, etiqueta = "Descripción", modifier = Modifier.testTag("descripcion"))
            Column(verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
                Text("ANTICIPACIÓN", style = Tipografia.H3, color = Colores.GrisTexto)
                Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
                    listOf(10 to "10 min", 30 to "30 min", 60 to "1 h").forEach { (min, texto) ->
                        ChipControl(texto, activo = anticipacionMin == min, onClick = { anticipacionMin = min })
                    }
                }
            }
            BotonPrimario(
                "Guardar y crear QR",
                onClick = { alGuardar(titulo, lugar.ifBlank { null }, descripcion.ifBlank { null }, anticipacionMin) },
                modifier = Modifier.testTag("guardar"),
            )
        }
    }
}
