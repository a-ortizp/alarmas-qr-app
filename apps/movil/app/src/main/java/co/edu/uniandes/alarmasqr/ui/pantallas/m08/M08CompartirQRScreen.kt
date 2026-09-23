package co.edu.uniandes.alarmasqr.ui.pantallas.m08

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M08 · QR del evento (F-M08): QR real (ZXing) de cualquier alarma propia o escaneada; compartir/descargar/copiar enlace. */
@Composable
fun M08CompartirQRScreen(
    evento: EventoQR, alVolver: () -> Unit, alCompartir: () -> Unit, alDescargar: () -> Unit, alCopiarEnlace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M08")) {
        BarraSuperior("QR del evento", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(evento.titulo, style = Tipografia.H2, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            CodigoQR(evento.codigoQR, tamano = Medidas.QRVisor, modifier = Modifier.testTag("qr"))
            Text(evento.etiqueta ?: "${evento.escaneos ?: 0} escaneos", style = Tipografia.Etiqueta, color = Colores.GrisTexto)
            BotonPrimario("Compartir por WhatsApp", onClick = alCompartir, modifier = Modifier.testTag("compartir"))
            BotonSecundario("Descargar como imagen", onClick = alDescargar, modifier = Modifier.testTag("descargar"))
            BotonSecundario("Copiar enlace", onClick = alCopiarEnlace, modifier = Modifier.testTag("copiar-enlace"))
        }
    }
}
