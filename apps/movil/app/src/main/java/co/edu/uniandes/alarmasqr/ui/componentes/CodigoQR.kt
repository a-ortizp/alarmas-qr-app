package co.edu.uniandes.alarmasqr.ui.componentes

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/** DS comp. 22 «Código QR · real»: generado con ZXing (spec §0.3), fondo blanco radio 4, módulos Tinta. */
@Composable
fun CodigoQR(contenido: String, tamano: Dp, modifier: Modifier = Modifier) {
    val px = with(LocalDensity.current) { tamano.roundToPx() }
    val imagen = remember(contenido, px) { generarQR(contenido, px) }
    Box(modifier.size(tamano).background(Colores.Blanco, Radios.QR), contentAlignment = Alignment.Center) {
        Image(imagen, contentDescription = "Código QR del evento", contentScale = ContentScale.FillBounds, modifier = Modifier.size(tamano))
    }
}

private fun generarQR(contenido: String, px: Int): ImageBitmap {
    // Corrección de errores baja (L, no M): con el contenido corto de esta app (alarmasqr://evento/…) alcanza para
    // la versión de QR más chica posible, con módulos grandes como en los mockups — sigue siendo un QR real y
    // escaneable por M03 (ML Kit), no una imagen decorativa.
    val matriz = QRCodeWriter().encode(contenido, BarcodeFormat.QR_CODE, px, px, mapOf(EncodeHintType.MARGIN to 1, EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L))
    val tinta = Colores.Tinta.toArgb()
    val blanco = Colores.Blanco.toArgb()
    val pixeles = IntArray(px * px) { i -> if (matriz[i % px, i / px]) tinta else blanco }
    return Bitmap.createBitmap(pixeles, px, px, Bitmap.Config.ARGB_8888).asImageBitmap()
}

/**
 * «ilustración · diana QR» (patrón de posición del QR, marca de la app): cuadro con trazo del 10 % del tamaño y
 * radio 25 %, centro relleno al 43 % y un módulo de acento al 22 % en la esquina superior derecha. Medido a 104.
 */
@Composable
fun DianaQR(tamano: Dp, modifier: Modifier = Modifier, color: Color = Colores.Tinta, acento: Color = Colores.AmarilloEnergia) {
    Canvas(modifier.size(tamano)) {
        val t = size.width
        val trazo = t * 0.1f
        drawRoundRect(color, Offset(trazo / 2, trazo / 2), Size(t - trazo, t - trazo), CornerRadius(t * 0.25f), style = Stroke(trazo))
        drawRoundRect(color, Offset(t * 0.283f, t * 0.283f), Size(t * 0.433f, t * 0.433f), CornerRadius(t * 0.096f))
        drawRoundRect(acento, Offset(t * 0.767f, -t * 0.017f), Size(t * 0.217f, t * 0.217f), CornerRadius(t * 0.058f))
    }
}

/** Logotipo de M00a/M00b (56×56): cuadro Amarillo Energía con la diana en Tinta (el SVG de Figma no se exporta). */
@Composable
fun Logotipo(modifier: Modifier = Modifier) {
    Box(modifier.size(Medidas.Logotipo).background(Colores.AmarilloEnergia, Radios.Tarjeta), contentAlignment = Alignment.Center) {
        DianaQR(tamano = Medidas.Logotipo * 0.6f, color = Colores.Tinta, acento = Colores.Blanco)
    }
}

/** «ilustración · alarma sonando»: reloj con ondas de sonido a los lados (M10, pantalla completa). */
@Composable
fun IconoAlarmaSonando(tamano: Dp, modifier: Modifier = Modifier) {
    Canvas(modifier.size(tamano)) {
        val t = size.width
        val centro = Offset(t / 2f, t / 2f)
        val radioReloj = t * 0.22f
        val trazoReloj = t * 0.05f
        drawCircle(Colores.Blanco, radioReloj, centro, style = Stroke(trazoReloj))
        drawLine(Colores.Blanco, centro, Offset(centro.x, centro.y - radioReloj * 0.55f), strokeWidth = trazoReloj * 0.7f, cap = StrokeCap.Round)
        drawLine(Colores.Blanco, centro, Offset(centro.x + radioReloj * 0.4f, centro.y), strokeWidth = trazoReloj * 0.7f, cap = StrokeCap.Round)
        val trazoOnda = t * 0.045f
        listOf(1, 2).forEach { i ->
            val r = radioReloj + i * t * 0.16f
            val cuadro = Size(r * 2, r * 2)
            val esquina = Offset(centro.x - r, centro.y - r)
            drawArc(Colores.CoralAlarma, startAngle = -35f, sweepAngle = 70f, useCenter = false, topLeft = esquina, size = cuadro, style = Stroke(trazoOnda, cap = StrokeCap.Round))
            drawArc(Colores.CoralAlarma, startAngle = 145f, sweepAngle = 70f, useCenter = false, topLeft = esquina, size = cuadro, style = Stroke(trazoOnda, cap = StrokeCap.Round))
        }
    }
}

/** «ilustración · destello»: estrella de cuatro puntas (M01, en blanco sobre el visor Tinta desde la v1.3). */
@Composable
fun Destello(tamano: Dp, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(tamano)) {
        val t = size.width; val c = t / 2; val r = t * 0.18f
        val camino = Path().apply {
            moveTo(c, 0f); lineTo(c + r, c - r); lineTo(t, c); lineTo(c + r, c + r); lineTo(c, t); lineTo(c - r, c + r); lineTo(0f, c); lineTo(c - r, c - r); close()
        }
        drawPath(camino, color)
    }
}
