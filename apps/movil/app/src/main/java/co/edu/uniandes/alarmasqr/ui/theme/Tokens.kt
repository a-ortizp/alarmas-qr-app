// Alarmas QR · Energía puntual · tokens v1.5 · derivado de packages/tokens/design-tokens.json
// Fuente de verdad: docs/DESIGN_SYSTEM.md. Nunca escribir colores, tamaños ni radios a mano fuera de este archivo.
// Reglas: un solo elemento amarillo por pantalla (la acción principal o el FAB); estados activos en Tinta;
// texto ≤ 15 sp usa los tonos *Texto; botones de 48 dp; radio 14 en tarjetas y hojas, 12 en campos; horas en 12 h.
// Las tres fuentes son variables (Google Fonts, OFL) en res/font/; el peso se aplica por eje wght (API 26+).
package co.edu.uniandes.alarmasqr.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import co.edu.uniandes.alarmasqr.R

object Colores {
    val AmarilloEnergia = Color(0xFFFFC400)
    val AmarilloSuave = Color(0xFFFFF1BF)
    val Tinta = Color(0xFF17161C)
    val Blanco = Color(0xFFFFFFFF)
    val CoralAlarma = Color(0xFFE8443A)
    val CoralTexto = Color(0xFFC4362E)
    val VerdeConfirmado = Color(0xFF129E63)
    val VerdeTexto = Color(0xFF0B7048)
    val VerdeFondo = Color(0xFFE9F7F0)
    val AzulEnlace = Color(0xFF2E7CF6)
    val AzulTexto = Color(0xFF1A5BC4)
    val GrisNiebla = Color(0xFFF4F3EF)
    val GrisMedio = Color(0xFF77747E)
    val GrisTexto = Color(0xFF66636D)
    val GrisBorde = Color(0xFFDAD8D2)
    val Velo = Color(0xFF17161C).copy(alpha = 0.45f)
}

object Fuentes {
    val Titulares = FontFamily(
        Font(R.font.bricolage_grotesque, FontWeight.SemiBold),
        Font(R.font.bricolage_grotesque, FontWeight.Bold),
    )
    val Ui = FontFamily(
        Font(R.font.archivo, FontWeight.Normal),
        Font(R.font.archivo, FontWeight.Medium),
        Font(R.font.archivo, FontWeight.SemiBold),
        Font(R.font.archivo, FontWeight.Bold),
    )
    val Datos = FontFamily(
        Font(R.font.spline_sans_mono, FontWeight.Medium),
        Font(R.font.spline_sans_mono, FontWeight.Bold),
    )
}

object Tipografia {
    val HoraProtagonista = TextStyle(fontFamily = Fuentes.Datos, fontWeight = FontWeight.Bold, fontSize = 96.sp, lineHeight = 96.sp)
    val HoraProtagonistaSufijo = TextStyle(fontFamily = Fuentes.Datos, fontWeight = FontWeight.Medium, fontSize = 28.sp)
    val HoraSonara = TextStyle(fontFamily = Fuentes.Datos, fontWeight = FontWeight.Bold, fontSize = 52.sp, lineHeight = 52.sp)
    val HoraTarjeta = TextStyle(fontFamily = Fuentes.Datos, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 26.sp)
    val HoraAmPm = TextStyle(fontFamily = Fuentes.Datos, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    val H1 = TextStyle(fontFamily = Fuentes.Titulares, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 32.sp)
    val BarraSuperior = TextStyle(fontFamily = Fuentes.Titulares, fontWeight = FontWeight.Bold, fontSize = 22.sp)
    val H2 = TextStyle(fontFamily = Fuentes.Titulares, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 26.sp)
    val Destacado = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    val Cuerpo = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp)
    val Boton = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    val TituloTarjeta = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 20.sp)
    val H3 = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.08.em)
    val Etiqueta = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp)
    val Nota = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 12.5.sp, lineHeight = 17.sp)
    val Chip = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    val EtiquetaCampo = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    val NavegacionInferior = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 11.sp)
    val Enlace = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 15.sp, textDecoration = TextDecoration.Underline)
}

object Tamanos {
    val Boton = 48.dp
    val BotonAlarma = 56.dp
    val Campo = 48.dp
    val Fab = 56.dp
    val BarraSuperior = 56.dp
    val NavegacionInferior = 64.dp
    val Chip = 20.dp
    val AreaTactilMinima = 48.dp
    val BandaTextura = 120.dp
    val Icono = 24.dp
}

object Radios {
    val Pildora = RoundedCornerShape(percent = 50)
    val Tarjeta = RoundedCornerShape(14.dp)
    val Hoja = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val Campo = RoundedCornerShape(12.dp)
    val Fab = RoundedCornerShape(16.dp)
    val Barra = RoundedCornerShape(4.dp)
}

object Trazos {
    val Borde = 1.5.dp
    val BordeFino = 1.dp
    val Foco = 2.dp
}

object Espacio {
    val Margen = 20.dp
    val Medianil = 12.dp
    val EntreBloques = 12.dp
    val PaddingBoton = 16.dp
    val PaddingTarjeta = 14.dp
}

object Movimiento {
    const val TransicionMs = 250
    const val ToqueLargoMs = 500L
    const val DeshacerMs = 5_000L
    const val TexturaOpacidadMax = 0.08f
}
