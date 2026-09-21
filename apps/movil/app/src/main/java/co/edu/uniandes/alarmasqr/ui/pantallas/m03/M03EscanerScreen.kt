package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.qr.VisorCamara
import co.edu.uniandes.alarmasqr.ui.componentes.Asa
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipControl
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * M03 · Escáner dual (F-M03): superficie Tinta, barra con «Linterna · auto» (chip de control 32), visor con marco
 * de enfoque amarillo (único amarillo: la hoja lleva dos secundarios de contorno) y hoja blanca r24 con las
 * alternativas. Sin permiso, el visor queda apagado y siguen los toques simulados ⏩.
 */
@Composable
fun M03EscanerScreen(
    estado: EstadoEscaner, tienePermiso: Boolean,
    alVolver: () -> Unit, alAlternarLinterna: () -> Unit, alLeer: (String) -> Unit,
    alTocarVisor: () -> Unit, alTocarVibra: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Tinta).testTag("pantalla-M03")) {
        BarraSuperior("Escanear QR", alVolver = alVolver, sobreTinta = true) {
            ChipControl("Linterna · auto", activo = estado.linterna, onClick = alAlternarLinterna, sobreTinta = true, modifier = Modifier.testTag("linterna"))
        }
        Box(Modifier.fillMaxWidth().weight(1f)) {
            if (tienePermiso) VisorCamara(linterna = estado.linterna, alLeer = alLeer, modifier = Modifier.fillMaxSize())
            ChipEstado("● Cámara activa", VarianteChip.Escaneada, modifier = Modifier.align(Alignment.TopCenter).padding(top = Espacio.ChipVisorSuperior))
            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Espacio.GapVisor)) {
                MarcoEnfoque(Modifier.clickable(role = Role.Button, onClick = alTocarVisor).testTag("visor"))
                Text("Apunta al código QR del evento", style = Tipografia.Destacado, color = Colores.Blanco)
                Text("vibra al detectar el código", style = Tipografia.Divisor, color = Colores.GrisBorde, modifier = Modifier.clickable(onClick = alTocarVibra).testTag("vibra"))
            }
        }
        Column(
            Modifier.fillMaxWidth().clip(Radios.Hoja).background(Colores.Blanco)
                .padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.HojaSuperior, bottom = Espacio.HojaInferior),
            verticalArrangement = Arrangement.spacedBy(Espacio.GapFila),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Asa()
            BotonSecundario("Elegir pantallazo de la galería", onClick = alElegirPantallazo, modifier = Modifier.testTag("escaner-pantallazo"))
            BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("escaner-a-mano"))
        }
    }
}

/**
 * Marco de enfoque 220 (4:135): cuatro esquinas en corchete redondeado — Amarillo Energía, trazo 4
 * (`Trazos.MarcoEnfoque`), remates y uniones redondas; cada esquina mide `Medidas.EsquinaEnfoque` (40) desde su
 * vértice a lo largo de cada lado, con un tramo recto y una curva de 90° de radio `Medidas.RadioEsquinaEnfoque`
 * (20) en la unión (el vértice recto original quedaba demasiado anguloso frente al corchete de Figma). QR de 100
 * al 50 %.
 */
@Composable
private fun MarcoEnfoque(modifier: Modifier = Modifier) {
    Box(modifier.size(Medidas.MarcoEnfoque), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val t = size.width; val e = Medidas.EsquinaEnfoque.toPx(); val r = Medidas.RadioEsquinaEnfoque.toPx(); val g = Trazos.MarcoEnfoque.toPx()
            val esquinas = Path().apply {
                // superior izquierda
                moveTo(0f, e); lineTo(0f, r); arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false); lineTo(e, 0f)
                // superior derecha
                moveTo(t - e, 0f); lineTo(t - r, 0f); arcTo(Rect(t - 2 * r, 0f, t, 2 * r), 270f, 90f, false); lineTo(t, e)
                // inferior derecha
                moveTo(t, t - e); lineTo(t, t - r); arcTo(Rect(t - 2 * r, t - 2 * r, t, t), 0f, 90f, false); lineTo(t - e, t)
                // inferior izquierda
                moveTo(e, t); lineTo(r, t); arcTo(Rect(0f, t - 2 * r, 2 * r, t), 90f, 90f, false); lineTo(0f, t - e)
            }
            drawPath(esquinas, Colores.AmarilloEnergia, style = Stroke(width = g, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        CodigoQR("alarmasqr://evento/e-entrega", tamano = Medidas.QRVisor, modifier = Modifier.alpha(Movimiento.QROpacidadVisor))
    }
}
