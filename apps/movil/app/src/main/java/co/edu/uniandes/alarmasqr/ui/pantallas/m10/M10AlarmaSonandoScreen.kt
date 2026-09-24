package co.edu.uniandes.alarmasqr.ui.pantallas.m10

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.AlSonar
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.IconoAlarmaSonando
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M10 · Alarma sonando (F-M10, pantalla completa): fondo Tinta, hora protagonista en Amarillo Energía (excepción
 * «sobre Tinta el primario es blanco»: los dos botones usan [sobreTinta]). Destino real de
 * `NotificacionesAlarma.intentSonando` — llega tanto navegando desde M09 como por el deep link de una alarma
 * disparada de verdad; `alarma.alSonar` es `null` fuera de «a-entrega», así que la tarjeta de tráfico es opcional.
 */
@Composable
fun M10AlarmaSonandoScreen(alarma: Alarma, alYaVoy: () -> Unit, alPosponer: () -> Unit, alVerRuta: (() -> Unit)?, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Tinta).testTag("pantalla-M10")) {
        BandaTextura(sobreTinta = true)
        ColumnaDesplazable(
            Modifier.fillMaxSize().weight(1f),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            IconoAlarmaSonando(tamano = Medidas.SelloGrande)
            ChipEstado("● Alarma de evento", VarianteChip.AlarmaEvento)
            Row {
                Text(FormatoHora.hora(alarma.suena), style = Tipografia.HoraProtagonista, color = Colores.AmarilloEnergia)
                Text(FormatoHora.sufijo(alarma.suena), style = Tipografia.HoraProtagonistaSufijo, color = Colores.AmarilloEnergia)
            }
            Text(alarma.titulo, style = Tipografia.H2, color = Colores.Blanco, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(subtituloEvento(alarma), style = Tipografia.Etiqueta, color = Colores.GrisBorde, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            alarma.alSonar?.let { alSonar -> TarjetaTrafico(alSonar, alVerRuta) }
        }
        Column(
            Modifier.fillMaxWidth().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBotonesDialogo),
        ) {
            BotonPrimario("Ya voy", onClick = alYaVoy, sobreTinta = true, alto = Tamanos.BotonAlarma, modifier = Modifier.testTag("ya-voy"))
            BotonSecundario("Posponer 10 min", onClick = alPosponer, sobreTinta = true, alto = Tamanos.BotonAlarma, modifier = Modifier.testTag("posponer"))
            Text(
                "En reunión o clase solo vibra · si no confirmas, re-aviso en 5 min",
                style = Tipografia.Nota, color = Colores.GrisBorde, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** «Hoy · evento 5:30 pm · Aula SD-703»: M10 simula el salto temporal al momento en que suena (⏩), así que la fecha siempre es «Hoy». */
private fun subtituloEvento(alarma: Alarma): String {
    val base = "Hoy · evento ${FormatoHora.horaConSufijo(alarma.eventoInicio)}"
    return alarma.lugar?.substringBefore(",")?.let { "$base · $it" } ?: base
}

@Composable
private fun TarjetaTrafico(alSonar: AlSonar, alVerRuta: (() -> Unit)?, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth().background(Colores.AmarilloSuave, Radios.Tarjeta).padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingPasosVertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja),
    ) {
        val texto = buildString {
            append("Sal en ${alSonar.salEnMin} min")
            alSonar.llegaA?.let { append(" · con el tráfico actual llegas ${FormatoHora.horaConSufijo(it)}") }
        }
        Text(texto, style = Tipografia.Cuerpo, color = Colores.Tinta, textAlign = TextAlign.Center)
        if (alSonar.rutaDisponible && alVerRuta != null) BotonEnlace("Ver ruta ›", onClick = alVerRuta, color = ColorEnlace.Tinta)
    }
}
