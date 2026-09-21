package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.AgrupadorDia
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.DianaQR
import co.edu.uniandes.alarmasqr.ui.componentes.TarjetaAlarma
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M02 · Inicio · lista (F-M02) y M05 (misma pantalla con la alarma nueva resaltada; el snackbar lo muestra la
 * entrada, Tarea 13). Barra «Mis alarmas» sin flecha, lista con relleno 20/16 y gap 12, agrupadores de día y
 * tarjetas; el FAB y la barra inferior los pone NavegacionApp.
 */
@Composable
fun M02InicioScreen(estado: EstadoInicio, alTocarAlarma: (String) -> Unit, alCambiarActiva: (String, Boolean) -> Unit, modifier: Modifier = Modifier, codigo: String = "M02") {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-$codigo")) {
        BarraSuperior("Mis alarmas")
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            estado.grupos.forEach { grupo ->
                item(key = "grupo-${grupo.etiqueta}") { AgrupadorDia(grupo.etiqueta) }
                items(grupo.alarmas, key = { "alarma-${it.id}" }) { alarma ->
                    TarjetaAlarma(alarma, onClick = { alTocarAlarma(alarma.id) }, alCambiarActiva = { alCambiarActiva(alarma.id, it) })
                }
            }
        }
    }
}

/**
 * M02v · Inicio sin alarmas (primer uso; DS comp. 30 «Estado vacío»): diana QR 104, «Aún no tienes alarmas» 24,
 * párrafo del dataset, primario «Escanear QR del evento» (→ M12 ⏩) y dos secundarios (→ M03b, → M07).
 */
@Composable
fun M02vEstadoVacio(mensajes: Mensajes, alEscanear: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M02v")) {
        BarraSuperior("Mis alarmas")
        Column(
            Modifier.fillMaxSize().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(Espacio.VacioSuperior))
            DianaQR(tamano = Medidas.Diana)
            Spacer(Modifier.height(Espacio.VacioEntre))
            Text(mensajes.sinAlarmas, style = Tipografia.TituloVacio, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(mensajes.sinAlarmasDetalle, style = Tipografia.Parrafo, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(Espacio.VacioEntre))
            BotonPrimario("Escanear QR del evento", onClick = alEscanear, modifier = Modifier.testTag("vacio-escanear"))
            BotonSecundario("Elegir pantallazo de la galería", onClick = alElegirPantallazo, modifier = Modifier.testTag("vacio-pantallazo"))
            BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("vacio-a-mano"))
        }
    }
}
