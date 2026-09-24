package co.edu.uniandes.alarmasqr.ui.pantallas.m08

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * M08 · Compartir evento (F-M08): tarjeta con el QR real (ZXing) del evento, canales reales de plataforma
 * (WhatsApp/Correo/Más, «llamada de plataforma trivial, sin backend» como el «Ver ruta» de M10), descargar
 * PNG/PDF y copiar enlace simulados con snackbar.
 */
@Composable
fun M08CompartirQRScreen(
    evento: EventoQR, alarma: Alarma, alVolver: () -> Unit,
    alCompartirWhatsApp: () -> Unit, alCompartirCorreo: () -> Unit, alCompartirMas: () -> Unit,
    alDescargarPNG: () -> Unit, alDescargarPDF: () -> Unit, alCopiarEnlace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M08")) {
        BarraSuperior("Compartir evento", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TarjetaEventoQR(evento, alarma)
            Text(
                "Tu alarma quedó programada · sonará ${FormatoHora.horaConSufijo(alarma.suena)}",
                style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
            ChipEstado(evento.etiqueta ?: "${evento.escaneos ?: 0} escaneos", VarianteChip.Suave)
            Row(horizontalArrangement = Arrangement.spacedBy(Espacio.EntreBloques)) {
                IconoAccion(Iconos.Chat, "WhatsApp", alCompartirWhatsApp, Modifier.testTag("whatsapp"))
                IconoAccion(Iconos.Correo, "Correo", alCompartirCorreo, Modifier.testTag("correo"))
                IconoAccion(Iconos.Compartir, "Más", alCompartirMas, Modifier.testTag("mas"))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Espacio.Medianil)) {
                BotonSecundario("Descargar PNG", onClick = alDescargarPNG, modifier = Modifier.weight(1f).testTag("descargar-png"))
                BotonSecundario("Descargar PDF", onClick = alDescargarPDF, modifier = Modifier.weight(1f).testTag("descargar-pdf"))
            }
            BotonPrimario("Compartir por WhatsApp", onClick = alCompartirWhatsApp, modifier = Modifier.testTag("compartir"))
            BotonEnlace("Copiar enlace del evento", onClick = alCopiarEnlace, color = ColorEnlace.Azul, modifier = Modifier.testTag("copiar-enlace"))
        }
    }
}

/** «dom 30 de agosto · 4:00 pm»: reutiliza `fechaLarga` sin la zona horaria, de cara al invitado que recibe el enlace. */
private fun subtituloFecha(alarma: Alarma) = FormatoHora.fechaLarga(alarma.eventoInicio).substringBeforeLast(" (")

@Composable
private fun TarjetaEventoQR(evento: EventoQR, alarma: Alarma, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth().background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta)
            .padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingTarjeta),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTarjeta),
    ) {
        Text(evento.titulo, style = Tipografia.TituloEvento, color = Colores.Tinta, textAlign = TextAlign.Center)
        Text(subtituloFecha(alarma), style = Tipografia.Etiqueta, color = Colores.GrisTexto)
        CodigoQR(evento.codigoQR, tamano = Medidas.QRVisor, modifier = Modifier.testTag("qr"))
        Text("Escanéalo y te avisamos", style = Tipografia.Destacado, color = Colores.Tinta)
        Text(
            "Quien escanee no necesita la app: también puede guardar el evento en su calendario.",
            style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun IconoAccion(icono: ImageVector, etiqueta: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.clickable(role = Role.Button, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja),
    ) {
        Box(Modifier.size(Medidas.CajaIcono).background(Colores.GrisNiebla, Radios.Pildora), contentAlignment = Alignment.Center) {
            Icon(icono, contentDescription = etiqueta, tint = Colores.Tinta, modifier = Modifier.size(Tamanos.Icono))
        }
        Text(etiqueta, style = Tipografia.Etiqueta, color = Colores.GrisTexto)
    }
}
