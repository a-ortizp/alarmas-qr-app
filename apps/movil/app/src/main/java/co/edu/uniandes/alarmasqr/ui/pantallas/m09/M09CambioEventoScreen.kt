package co.edu.uniandes.alarmasqr.ui.pantallas.m09

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M09 · Cambio del organizador (F-M09): la «×» va al margen derecho de la barra (v1.7); sin diálogo de eliminar
 * (desde la v1.1 de los mockups, «Eliminar» solo vive en M06). Requiere `alarma.cambioDelOrganizador` — quien
 * registra la entrada solo navega aquí para alarmas que lo tienen (hoy, «a-entrega»).
 */
@Composable
fun M09CambioEventoScreen(alarma: Alarma, alAceptar: () -> Unit, alMantener: () -> Unit, alCerrar: () -> Unit, alVerAlarma: () -> Unit, modifier: Modifier = Modifier) {
    val cambio = requireNotNull(alarma.cambioDelOrganizador) { "M09 requiere una alarma con cambioDelOrganizador" }
    Column(modifier.fillMaxSize().background(Colores.Tinta).testTag("pantalla-M09")) {
        BarraSuperior("Cambio en el evento", sobreTinta = true, accion = { BotonCerrar(onClick = alCerrar) })
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            BandaTextura(sobreTinta = true)
            Text("SI ACEPTAS, SONARÁ", style = Tipografia.H3, color = Colores.GrisBorde)
            Text(FormatoHora.horaConSufijo(cambio.nuevaHoraDeAlarma), style = Tipografia.HoraSonara, color = Colores.AmarilloEnergia)
            Text("${alarma.anticipacionMin} min de margen + ${alarma.trayectoMin} min de trayecto", style = Tipografia.MargenSonara, color = Colores.GrisBorde)
            Text("Antes sonaba ${FormatoHora.horaConSufijo(cambio.antesSonaba)}", style = Tipografia.Etiqueta, color = Colores.GrisBorde)
            Text(cambio.autor, style = Tipografia.Etiqueta, color = Colores.GrisBorde)
            TextButton(onClick = alVerAlarma, modifier = Modifier.testTag("ver-alarma")) {
                Text(alarma.titulo, style = Tipografia.TituloTarjeta, color = Colores.Blanco)
            }
            BotonPrimario("Aceptar cambio", onClick = alAceptar, sobreTinta = true, modifier = Modifier.testTag("aceptar"))
            BotonSecundario("Mantener alarma", onClick = alMantener, sobreTinta = true, modifier = Modifier.testTag("mantener"))
        }
    }
}

@Composable
private fun BotonCerrar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier.size(Medidas.BotonAtras).testTag("cerrar"),
        colors = ButtonDefaults.textButtonColors(contentColor = Colores.Blanco),
    ) { Text("×", style = Tipografia.FlechaAtras) }
}
