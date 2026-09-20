package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** Línea Gris Borde · texto Archivo 12 Gris Texto · línea («o continúa con» en M00a/M00b, «mientras tanto» en M12). */
@Composable
fun Divisor(texto: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Espacio.GapDivisor), verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(Modifier.weight(1f), thickness = Trazos.BordeFino, color = Colores.GrisBorde)
        Text(texto, style = Tipografia.Divisor, color = Colores.GrisTexto)
        HorizontalDivider(Modifier.weight(1f), thickness = Trazos.BordeFino, color = Colores.GrisBorde)
    }
}
