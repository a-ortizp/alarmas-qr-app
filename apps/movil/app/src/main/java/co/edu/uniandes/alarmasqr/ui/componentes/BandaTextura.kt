package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos

/**
 * «textura · módulos QR» (MOCKUPS.md §3): retícula de módulos 8×8 radio 2 con paso 14, opacidad 8 % arriba que
 * baja hasta 2 % en la última fila. Solo como banda de 120 en M01, M04, M12 y M13 (Style Tile §4.6). Vector puro.
 * [opacidad] multiplica el alfa calculado; M12 y M13 la atenúan al 50 % detrás de los titulares
 * ([co.edu.uniandes.alarmasqr.ui.theme.Movimiento.TexturaAtenuada], MOCKUPS §3c paso 4).
 */
@Composable
fun BandaTextura(modifier: Modifier = Modifier, sobreTinta: Boolean = false, alto: Dp = Tamanos.BandaTextura, opacidad: Float = 1f) {
    val color = if (sobreTinta) Colores.Blanco else Colores.Tinta
    Canvas(modifier.fillMaxWidth().height(alto)) {
        val modulo = Medidas.ModuloTextura.toPx()
        val paso = Medidas.PasoTextura.toPx()
        val radio = CornerRadius(modulo / 4)
        val filas = (size.height / paso).toInt()
        val columnas = (size.width / paso).toInt() + 1
        for (f in 0 until filas) {
            val alfa = (Movimiento.TexturaOpacidadMax - (Movimiento.TexturaOpacidadMax - Movimiento.TexturaOpacidadMin) * f / (filas - 1).coerceAtLeast(1)) * opacidad
            for (c in 0 until columnas) {
                drawRoundRect(color.copy(alpha = alfa), Offset(c * paso + (paso - modulo) / 2, f * paso + (paso - modulo) / 2), Size(modulo, modulo), radio)
            }
        }
    }
}
