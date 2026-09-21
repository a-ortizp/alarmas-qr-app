package co.edu.uniandes.alarmasqr.ui.pantallas.m12

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.Divisor
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M12 · Permiso de cámara (F-M12): explicación + «Abrir ajustes»; alternativas ancladas abajo (tutores v1.6). */
@Composable
fun M12PermisoCamaraScreen(alVolver: () -> Unit, alAbrirAjustes: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M12")) {
        BarraSuperior("Permiso de cámara", alVolver = alVolver)
        Box(Modifier.fillMaxSize()) {
            BandaTextura(Modifier.align(Alignment.TopCenter), opacidad = Movimiento.TexturaAtenuada)
            Column(
                Modifier.fillMaxSize().padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingPermisoSuperior, bottom = Espacio.HojaInferior),
                verticalArrangement = Arrangement.spacedBy(Espacio.GapPermiso),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.fillMaxWidth().height(Medidas.VisorApagado).background(Colores.GrisNiebla, Radios.Visor), contentAlignment = Alignment.Center) {
                    Icon(Iconos.Escanear, contentDescription = null, tint = Colores.Tinta, modifier = Modifier.size(Tamanos.IconoVisor))
                }
                Text("La cámara está apagada para la app", style = Tipografia.BarraSuperior, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("Solo la usamos para leer códigos QR de eventos. Nunca guardamos fotos ni videos.", style = Tipografia.CuerpoDialogo, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                TarjetaPasos()
                BotonPrimario("Abrir ajustes", onClick = alAbrirAjustes, modifier = Modifier.testTag("abrir-ajustes"))
                Spacer(Modifier.weight(1f))
                Divisor("mientras tanto")
                BotonSecundario("Elegir pantallazo de la galería", onClick = alElegirPantallazo, modifier = Modifier.testTag("permiso-pantallazo"))
                BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("permiso-a-mano"))
            }
        }
    }
}

/** Tarjeta «ACTÍVALA EN 3 PASOS» (6:96): blanca, borde 1.5, r14, sombra, relleno 14/12, gap 7, numerales 22 en Tinta. */
@Composable
private fun TarjetaPasos() {
    Column(
        Modifier.fillMaxWidth()
            .shadow(Elevaciones.Tarjeta, Radios.Tarjeta, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
            .background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta)
            .padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingPasosVertical),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapPasos),
    ) {
        Text("ACTÍVALA EN 3 PASOS", style = Tipografia.H3, color = Colores.GrisTexto)
        listOf("Abrir los ajustes del teléfono", "Permisos › Cámara", "Elegir “Permitir con la app en uso”").forEachIndexed { i, paso ->
            Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(Medidas.Numeral).background(Colores.Tinta, Radios.Pildora), contentAlignment = Alignment.Center) {
                    Text("${i + 1}", style = Tipografia.Chip, color = Colores.Blanco)
                }
                Text(paso, style = Tipografia.Etiqueta, color = Colores.Tinta)
            }
        }
    }
}
