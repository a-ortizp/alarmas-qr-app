// Alarmas QR · Energía puntual · tokens v1.5 (2026-09-16)
// Derivado de design-tokens.json. Fuente de verdad: DESIGN_SYSTEM.md.
// Copiar a apps/movil/app/src/main/java/<paquete>/ui/theme/Tokens.kt y ajustar el package.
// Las fuentes van en res/font/ con estos nombres de archivo (Google Fonts, licencia OFL):
//   bricolage_grotesque_semibold.ttf · bricolage_grotesque_bold.ttf
//   archivo_regular.ttf · archivo_medium.ttf · archivo_semibold.ttf · archivo_bold.ttf
//   spline_sans_mono_medium.ttf · spline_sans_mono_bold.ttf
//
// Reglas: un solo elemento amarillo por pantalla (la acción principal o el FAB); estados activos en Tinta;
// texto ≤ 15 sp usa los tonos *Texto; botones de 48 dp; radio 14 en tarjetas y hojas, 12 en campos; horas en 12 h.

package com.alarmasqr.ui.theme

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
import com.alarmasqr.R

object Colores {
    val AmarilloEnergia = Color(0xFFFFC400)   // acción primaria y marca
    val AmarilloSuave   = Color(0xFFFFF1BF)   // tinte · bloques «Sonará»
    val Tinta           = Color(0xFF17161C)   // texto · marca · estados activos · fondos plenos (M03, M10)
    val Blanco          = Color(0xFFFFFFFF)   // Blanco Papel · fondo
    val CoralAlarma     = Color(0xFFE8443A)   // urgencia · alarma sonando · rellenos e íconos
    val CoralTexto      = Color(0xFFC4362E)   // destructivo como texto · 5.4:1
    val VerdeConfirmado = Color(0xFF129E63)   // éxito · verificado · rellenos e íconos
    val VerdeTexto      = Color(0xFF0B7048)   // escaneada · publicado · 6.1:1
    val VerdeFondo      = Color(0xFFE9F7F0)   // relleno de chips Publicado / Activa
    val AzulEnlace      = Color(0xFF2E7CF6)   // informativo · íconos
    val AzulTexto       = Color(0xFF1A5BC4)   // enlaces · 6.3:1
    val GrisNiebla      = Color(0xFFF4F3EF)   // superficies · píldora inactiva
    val GrisMedio       = Color(0xFF77747E)   // marcadores de posición
    val GrisTexto       = Color(0xFF66636D)   // texto secundario ≤ 15 sp · 5.9:1
    val GrisBorde       = Color(0xFFDAD8D2)   // bordes 1.5 · divisores · texto secundario sobre Tinta
    val Velo            = Color(0xFF17161C).copy(alpha = 0.45f)
}

object Fuentes {
    val Titulares = FontFamily(
        Font(R.font.bricolage_grotesque_semibold, FontWeight.SemiBold),
        Font(R.font.bricolage_grotesque_bold, FontWeight.Bold),
    )
    val Ui = FontFamily(
        Font(R.font.archivo_regular, FontWeight.Normal),
        Font(R.font.archivo_medium, FontWeight.Medium),
        Font(R.font.archivo_semibold, FontWeight.SemiBold),
        Font(R.font.archivo_bold, FontWeight.Bold),
    )
    val Datos = FontFamily(
        Font(R.font.spline_sans_mono_medium, FontWeight.Medium),
        Font(R.font.spline_sans_mono_bold, FontWeight.Bold),
    )
}

/** Escala tipográfica del Design System (L02 + complementos). Los colores se aplican en el uso, no aquí. */
object Tipografia {
    val HoraProtagonista = TextStyle(Fuentes.Datos, fontWeight = FontWeight.Bold, fontSize = 96.sp, lineHeight = 96.sp)   // M10
    val HoraProtagonistaSufijo = TextStyle(Fuentes.Datos, fontWeight = FontWeight.Medium, fontSize = 28.sp)
    val HoraSonara = TextStyle(Fuentes.Datos, fontWeight = FontWeight.Bold, fontSize = 52.sp, lineHeight = 52.sp)         // M04 / M09
    val HoraTarjeta = TextStyle(Fuentes.Datos, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 26.sp)        // tarjeta de alarma
    val HoraAmPm = TextStyle(Fuentes.Datos, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    val H1 = TextStyle(Fuentes.Titulares, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 32.sp)             // nombre de pantalla
    val BarraSuperior = TextStyle(Fuentes.Titulares, fontWeight = FontWeight.Bold, fontSize = 22.sp)
    val H2 = TextStyle(Fuentes.Titulares, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 26.sp)        // nombre de evento
    val Destacado = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    val Cuerpo = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp)
    val Boton = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    val TituloTarjeta = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 20.sp)
    val H3 = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.08.em)               // «HOY · JUEVES 27», en mayúsculas
    val Etiqueta = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp)           // «evento 8:00 am · Aula SD-703»
    val Nota = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 12.5.sp, lineHeight = 17.sp)
    val Chip = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    val EtiquetaCampo = TextStyle(Fuentes.Ui, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)                          // etiqueta dentro del contorno
    val NavegacionInferior = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 11.sp)                       // única excepción < 12
    val Enlace = TextStyle(Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 15.sp, textDecoration = TextDecoration.Underline)
}

object Tamanos {
    val Boton = 48.dp                 // toque; la web usa 44
    val BotonAlarma = 56.dp           // «Ya voy» · «Posponer 10 min»
    val Campo = 48.dp
    val Fab = 56.dp
    val BarraSuperior = 56.dp
    val NavegacionInferior = 64.dp
    val Chip = 20.dp
    val Switch = 42.dp to 24.dp
    val AreaTactilMinima = 48.dp
    val BandaTextura = 120.dp
    val Icono = 24.dp
}

object Radios {
    val Pildora = RoundedCornerShape(percent = 50)
    val Tarjeta = RoundedCornerShape(14.dp)
    val Hoja = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)   // M02h, hoja inferior de M03
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
    val Margen = 20.dp        // retícula de 4 columnas
    val Medianil = 12.dp
    val EntreBloques = 12.dp
    val PaddingBoton = 16.dp
    val PaddingTarjeta = 14.dp
}

object Movimiento {
    const val TransicionMs = 250          // transiciones entre pantallas, ease in-out
    const val ToqueLargoMs = 500L         // mantener presionado el FAB abre la hoja «Agregar evento»
    const val DeshacerMs = 5_000L         // ventana del snackbar «Deshacer»
    const val TexturaOpacidadMax = 0.08f  // retícula de módulos QR, solo banda superior en M01, M04, M09, M10, M12, M13
}
