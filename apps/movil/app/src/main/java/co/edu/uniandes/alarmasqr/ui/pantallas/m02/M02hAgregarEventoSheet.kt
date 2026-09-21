package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M02h · Hoja «Agregar evento» (F-M02): única entrada de captura; la primera fila es el único amarillo de la hoja. */
@Composable
fun M02hAgregarEventoSheet(alEscanear: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(start = Espacio.Margen, end = Espacio.Margen, bottom = Espacio.HojaInferiorCorta).testTag("pantalla-M02h"),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapHoja),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Agregar evento", style = Tipografia.BarraSuperior, color = Colores.Tinta, modifier = Modifier.fillMaxWidth())
        FilaAgregar(Iconos.Escanear, "Escanear el QR del evento", "La alarma queda lista sin escribir nada", primaria = true, onClick = alEscanear, modifier = Modifier.testTag("hoja-escanear"))
        FilaAgregar(Iconos.Galeria, "Elegir pantallazo de la galería", "Leemos el QR que aparezca en la imagen", primaria = false, onClick = alElegirPantallazo, modifier = Modifier.testTag("hoja-pantallazo"))
        FilaAgregar(Iconos.Mas, "Crear el evento a mano", "Escribe fecha, hora y lugar; te damos su QR", primaria = false, onClick = alCrearAMano, modifier = Modifier.testTag("hoja-a-mano"))
        Text(
            "También puedes compartir un pantallazo desde WhatsApp o la galería con Alarmas QR.",
            style = Tipografia.NotaHoja, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Fila de 350×100: caja de icono 40 r12 + título Bold 16 + subtítulo 13 + chevrón «›» Bold 20. */
@Composable
private fun FilaAgregar(icono: ImageVector, titulo: String, subtitulo: String, primaria: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val texto = if (primaria) Colores.Tinta else Colores.GrisTexto
    Row(
        modifier.fillMaxWidth().height(Medidas.FilaHoja).clip(Radios.Tarjeta)
            .background(if (primaria) Colores.AmarilloEnergia else Colores.Blanco)
            .then(if (primaria) Modifier else Modifier.border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = Espacio.PaddingFilaHoja, vertical = Espacio.EntreBloques),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFilaHoja),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(Medidas.CajaIcono).background(if (primaria) Colores.Blanco else Colores.GrisNiebla, Radios.CajaIcono), contentAlignment = Alignment.Center) {
            Icon(icono, contentDescription = null, tint = Colores.Tinta, modifier = Modifier.size(Tamanos.Icono))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
            Text(titulo, style = Tipografia.Destacado, color = Colores.Tinta)
            Text(subtitulo, style = Tipografia.Etiqueta, color = texto)
        }
        Text("›", style = Tipografia.Chevron, color = texto)
    }
}
