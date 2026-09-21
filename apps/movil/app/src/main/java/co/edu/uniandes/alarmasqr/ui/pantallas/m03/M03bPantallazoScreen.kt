package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.datos.PantallazoRecibido
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M03b · Pantallazo recibido (F-M03): confirmación del QR leído en una imagen compartida; «Continuar» → M04. */
@Composable
fun M03bPantallazoScreen(datos: PantallazoRecibido, evento: EventoQR, alVolver: () -> Unit, alContinuar: () -> Unit, alElegirOtra: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M03b")) {
        BarraSuperior("Pantallazo recibido", alVolver = alVolver)
        Column(
            Modifier.fillMaxSize().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ChipEstado("✓ QR detectado", VarianteChip.Escaneada)
            Text("QR de evento detectado en tu pantallazo", style = Tipografia.TituloEvento, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Box(Modifier.fillMaxWidth().height(Medidas.VistaPrevia).background(Colores.GrisNiebla, Radios.Visor), contentAlignment = Alignment.Center) {
                Column(
                    Modifier.size(Medidas.Burbuja.width, Medidas.Burbuja.height)
                        .shadow(Elevaciones.Tarjeta, Radios.Tarjeta, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
                        .background(Colores.Blanco, Radios.Tarjeta).padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingPasosVertical),
                    verticalArrangement = Arrangement.spacedBy(Espacio.GapFila),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("${datos.grupo} · hoy 8:12 am", style = Tipografia.Divisor, color = Colores.GrisTexto)   // hora literal del mockup 4020:3452
                    Text(datos.mensaje, style = Tipografia.Mensaje, color = Colores.Tinta, modifier = Modifier.fillMaxWidth())
                    Box(Modifier.size(Medidas.MarcoLectura).border(Trazos.MarcoPantalla, Colores.Tinta, Radios.MarcoLectura).padding(Espacio.PaddingMarcoLectura)) {
                        CodigoQR(evento.codigoQR, tamano = Medidas.QRPantallazo)
                    }
                }
            }
            Text("Origen: ${datos.origen}", style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.weight(1f))
            BotonPrimario("Continuar", onClick = alContinuar, modifier = Modifier.testTag("continuar"))
            BotonEnlace("Elegir otra imagen", onClick = alElegirOtra, modifier = Modifier.testTag("elegir-otra"))
            Text("Si el pantallazo no trae un QR, puedes crear el evento a mano.", style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}
