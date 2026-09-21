package co.edu.uniandes.alarmasqr.ui.pantallas.m13

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.QRInvalido
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M13 · QR sin evento (F-M13): diagnóstico anti-quishing; el enlace solo se abre bajo decisión explícita. */
@Composable
fun M13QRInvalidoScreen(diagnostico: QRInvalido, alVolver: () -> Unit, alVolverAEscanear: () -> Unit, alCrearAMano: () -> Unit, alAbrirEnlace: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M13")) {
        BarraSuperior("QR sin evento", alVolver = alVolver)
        Box(Modifier.fillMaxSize()) {
            BandaTextura(Modifier.align(Alignment.TopCenter), opacidad = Movimiento.TexturaAtenuada)
            Column(
                Modifier.fillMaxSize().padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingPermisoSuperior, bottom = Espacio.PaddingBoton),
                verticalArrangement = Arrangement.spacedBy(Espacio.GapPermiso),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.size(Medidas.SelloGrande).background(Colores.CoralSuave, Radios.Pildora).border(Trazos.MarcoPantalla, Colores.CoralTexto, Radios.Pildora), contentAlignment = Alignment.Center) {
                    Text("!", style = Tipografia.H1, color = Colores.CoralTexto)
                }
                Text("Este QR no contiene un evento", style = Tipografia.BarraSuperior, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("Leímos el código, pero no trae fecha ni datos de evento para crear una alarma.", style = Tipografia.CuerpoDialogo, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Column(
                    Modifier.fillMaxWidth().background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.CoralTexto, Radios.Tarjeta)
                        .padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingPasosVertical),
                    verticalArrangement = Arrangement.spacedBy(Espacio.GapPasos),
                ) {
                    Text("QUÉ DETECTAMOS", style = Tipografia.H3, color = Colores.GrisTexto)
                    Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapDivisor), verticalAlignment = Alignment.CenterVertically) {
                        ChipEstado(diagnostico.tipoDetectado, VarianteChip.Coral)
                        Text(diagnostico.contenido, style = Tipografia.Codigo, color = Colores.GrisTexto, maxLines = 1)
                    }
                    Text(diagnostico.diagnostico, style = Tipografia.Nota, color = Colores.GrisTexto)
                }
                Spacer(Modifier.height(Espacio.AntesAcciones))
                BotonPrimario("Volver a escanear", onClick = alVolverAEscanear, modifier = Modifier.testTag("volver-a-escanear"))
                BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("invalido-a-mano"))
                BotonEnlace("Abrir el enlace bajo mi responsabilidad", onClick = alAbrirEnlace, color = ColorEnlace.Azul, estilo = Tipografia.EnlaceCorto, modifier = Modifier.testTag("abrir-enlace"))
            }
        }
    }
}
