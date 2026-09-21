package co.edu.uniandes.alarmasqr.ui.pantallas.m01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.Destello
import co.edu.uniandes.alarmasqr.ui.componentes.FilaOpcionCalendario
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import androidx.compose.foundation.layout.PaddingValues

/** M01 · Bienvenida (F-M01): onboarding en amarillo pleno; «Comenzar» → M00a, «Conectar luego en Ajustes» → M02v. */
@Composable
fun M01BienvenidaScreen(alComenzar: () -> Unit, alConectarLuego: () -> Unit, modifier: Modifier = Modifier) {
    var google by rememberSaveable { mutableStateOf(false) }
    var outlook by rememberSaveable { mutableStateOf(false) }
    var telefono by rememberSaveable { mutableStateOf(false) }
    Box(modifier.fillMaxSize().background(Colores.AmarilloEnergia).testTag("pantalla-M01")) {
        BandaTextura(Modifier.align(Alignment.TopCenter))
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingPantallaSuperior, bottom = Espacio.PaddingPantallaInferior),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IndicadorPagina()
            VisorBienvenida()
            Text("Escanea y listo", style = Tipografia.Titular, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(
                "Apunta la cámara al QR del evento: la alarma queda programada sin escribir fecha, hora ni nombre.",
                style = Tipografia.Parrafo, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
            Text("CONECTA TU CALENDARIO (OPCIONAL)", style = Tipografia.H3, color = Colores.Tinta, modifier = Modifier.fillMaxWidth())
            FilaOpcionCalendario(Iconos.Google, "Google Calendar", google, { google = it }, Modifier.testTag("calendario-google"))
            FilaOpcionCalendario(Iconos.Outlook, "Outlook · Teams", outlook, { outlook = it }, Modifier.testTag("calendario-outlook"))
            FilaOpcionCalendario(Iconos.Telefono, "Calendario del teléfono", telefono, { telefono = it }, Modifier.testTag("calendario-telefono"))
            Spacer(Modifier.height(Espacio.AntesBoton))
            BotonPrimario("Comenzar", onClick = alComenzar, sobreAmarillo = true)
            BotonEnlace("Conectar luego en Ajustes", onClick = alConectarLuego, color = ColorEnlace.Tinta)
        }
    }
}

/** Indicador de página del mockup (3:73): activo 22×6 Tinta, dos puntos 6×6 Tinta 25 %, gap 6. */
@Composable
private fun IndicadorPagina() {
    Row(horizontalArrangement = Arrangement.spacedBy(Medidas.PuntoPagina), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(Medidas.IndicadorPagina.width, Medidas.IndicadorPagina.height).background(Colores.Tinta, Radios.Pildora))
        repeat(2) { Box(Modifier.size(Medidas.PuntoPagina).background(Colores.Tinta25, Radios.Pildora)) }
    }
}

/** Visor Tinta 350×170 r20 (3:77) con textura blanca, QR de 84 centrado y dos destellos blancos. */
@Composable
private fun VisorBienvenida() {
    Box(Modifier.fillMaxWidth().height(Medidas.VisorBienvenida).clip(Radios.VisorBienvenida).background(Colores.Tinta), contentAlignment = Alignment.Center) {
        BandaTextura(Modifier.align(Alignment.TopCenter), sobreTinta = true, alto = Medidas.VisorBienvenida)
        CodigoQR("alarmasqr://evento/e-entrega", tamano = Medidas.QRBienvenida)
        // Posiciones del vector medido en Figma (anexo §1, capas 4013:2900 y 4013:2902); excepción documentada como las proporciones de DianaQR.
        Destello(tamano = Medidas.CajaIcono, color = Colores.Blanco, modifier = Modifier.align(Alignment.TopStart).offset(x = 22.dp, y = 18.dp))
        Destello(tamano = Tamanos.Icono, color = Colores.Blanco, modifier = Modifier.align(Alignment.TopStart).offset(x = 298.dp, y = 110.dp))
    }
}
