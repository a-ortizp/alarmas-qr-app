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
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.TarjetaAlarma
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import java.time.LocalDate

/** M02b · Vista calendario (F-M02, CM-01): mes compacto con contador por día y el detalle del día seleccionado en una `LazyColumn` (lista potencialmente larga). */
@Composable
fun M02bCalendarioScreen(
    estado: EstadoCalendario, alSeleccionarDia: (LocalDate) -> Unit, alTocarAlarma: (String) -> Unit, alCambiarActiva: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M02b")) {
        BarraSuperior("Calendario")
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            item(key = "rejilla") { RejillaMes(estado.dias, estado.diaSeleccionado, alSeleccionarDia) }
            if (estado.alarmasDelDia.isEmpty()) {
                item(key = "vacio") { Text("Sin alarmas este día.", style = Tipografia.Etiqueta, color = Colores.GrisTexto) }
            } else {
                items(estado.alarmasDelDia, key = { "alarma-${it.id}" }) { alarma ->
                    TarjetaAlarma(alarma, onClick = { alTocarAlarma(alarma.id) }, alCambiarActiva = { alCambiarActiva(alarma.id, it) })
                }
            }
        }
    }
}

private fun semanas(dias: List<DiaCalendario>): List<List<DiaCalendario?>> {
    val relleno: List<DiaCalendario?> = List(dias.first().fecha.dayOfWeek.value - 1) { null }   // lunes = 1 → 0 espacios
    val celdas = relleno + dias
    return celdas.chunked(7).map { semana -> semana + List(7 - semana.size) { null } }
}

@Composable
private fun RejillaMes(dias: List<DiaCalendario>, seleccionado: LocalDate, alSeleccionar: (LocalDate) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        semanas(dias).forEach { semana ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                semana.forEach { dia ->
                    if (dia == null) Box(Modifier.weight(1f)) else CeldaDia(dia, dia.fecha == seleccionado, { alSeleccionar(dia.fecha) }, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CeldaDia(dia: DiaCalendario, marcado: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.clip(Radios.Pildora).clickable(role = Role.Button, onClick = onClick)
            .background(if (marcado) Colores.Tinta else Color.Transparent, Radios.Pildora)
            .padding(vertical = Espacio.GapFila).testTag("dia-${dia.fecha}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoTarjeta),
    ) {
        Text(dia.fecha.dayOfMonth.toString(), style = Tipografia.TituloTarjeta, color = if (marcado) Colores.Blanco else Colores.Tinta)
        if (dia.alarmasCount > 0) Box(Modifier.size(Medidas.PuntoPagina).background(if (marcado) Colores.Blanco else Colores.Tinta, Radios.Pildora))
    }
}
