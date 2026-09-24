package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.ChipControl
import co.edu.uniandes.alarmasqr.ui.componentes.TarjetaAlarma
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import java.time.LocalDate

private val DIAS_SEMANA = listOf("L", "M", "X", "J", "V", "S", "D")

/** M02b · Vista calendario (F-M02, CM-01): mes navegable, grilla de semanas completas y el detalle del día seleccionado en una `LazyColumn` (lista potencialmente larga). */
@Composable
fun M02bCalendarioScreen(
    estado: EstadoCalendario, alSeleccionarDia: (LocalDate) -> Unit, alTocarAlarma: (String) -> Unit, alCambiarActiva: (String, Boolean) -> Unit,
    alMesAnterior: () -> Unit = {}, alMesSiguiente: () -> Unit = {}, alVerLista: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M02b")) {
        BarraSuperior("Mis alarmas") {
            ChipControl("Lista", activo = false, onClick = alVerLista, modifier = Modifier.testTag("boton-lista"))
            ChipControl("Mes", activo = true, onClick = {})
        }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            item(key = "encabezado-mes") { EncabezadoMes(estado.tituloMes, alMesAnterior, alMesSiguiente) }
            item(key = "rejilla") { RejillaMes(estado.dias, estado.diaSeleccionado, alSeleccionarDia) }
            if (estado.alarmasDelDia.isEmpty()) {
                item(key = "vacio") { Text("Sin alarmas este día.", style = Tipografia.Etiqueta, color = Colores.GrisTexto) }
            } else {
                item(key = "etiqueta-dia") { Text(estado.etiquetaDiaSeleccionado, style = Tipografia.H3, color = Colores.GrisTexto) }
                items(estado.alarmasDelDia, key = { "alarma-${it.id}" }) { alarma ->
                    TarjetaAlarma(alarma, onClick = { alTocarAlarma(alarma.id) }, alCambiarActiva = { alCambiarActiva(alarma.id, it) }, mostrarChips = false)
                }
            }
        }
    }
}

@Composable
private fun EncabezadoMes(titulo: String, alMesAnterior: () -> Unit, alMesSiguiente: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        FlechaMes("‹", alMesAnterior, Modifier.testTag("mes-anterior"))
        Text(titulo, style = Tipografia.H2, color = Colores.Tinta)
        FlechaMes("›", alMesSiguiente, Modifier.testTag("mes-siguiente"))
    }
}

@Composable
private fun FlechaMes(simbolo: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier.size(Tamanos.AreaTactilMinima).clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { Text(simbolo, style = Tipografia.Chevron, color = Colores.GrisTexto) }
}

private fun semanas(dias: List<DiaCalendario>): List<List<DiaCalendario>> = dias.chunked(7)

@Composable
private fun RejillaMes(dias: List<DiaCalendario>, seleccionado: LocalDate, alSeleccionar: (LocalDate) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DIAS_SEMANA.forEach { letra -> Text(letra, style = Tipografia.H3, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.weight(1f)) }
        }
        semanas(dias).forEach { semana ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                semana.forEach { dia -> CeldaDia(dia, dia.fecha == seleccionado, { alSeleccionar(dia.fecha) }, Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun CeldaDia(dia: DiaCalendario, seleccionado: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colorTexto = when { seleccionado -> Colores.Blanco; dia.enMes -> Colores.Tinta; else -> Colores.GrisBorde }
    val colorMarca = if (seleccionado) Colores.Blanco else Colores.GrisMedio
    Column(
        modifier.clip(Radios.Tarjeta)
            .then(if (dia.enMes) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .background(if (seleccionado) Colores.Tinta else Color.Transparent, Radios.Tarjeta)
            .padding(vertical = Espacio.GapFila)
            .testTag("dia-${dia.fecha}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoTarjeta),
    ) {
        Text(dia.fecha.dayOfMonth.toString(), style = Tipografia.TituloTarjeta, color = colorTexto)
        if (dia.alarmasCount > 0) Box(Modifier.size(Medidas.MarcaDia.width, Medidas.MarcaDia.height).background(colorMarca, Radios.Pildora))
    }
}
