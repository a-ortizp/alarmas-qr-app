package co.edu.uniandes.alarmasqr.ui.iconos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Iconos de línea del DS (MOCKUPS.md §3: 24 px, trazo 2, terminales redondeadas; «recolorear el trazo según
 * contexto» → se tiñen con `Icon(tint = …)`). Set 48 (google, outlook, teléfono) y la iconografía base.
 * Redibujados a mano sobre la misma anatomía; no son los SVG de Figma, que no se exportan (spec §0.3).
 */
object Iconos {
    private fun icono(nombre: String, vararg trazos: PathBuilder.() -> Unit, relleno: (PathBuilder.() -> Unit)? = null): ImageVector =
        ImageVector.Builder(name = nombre, defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
            trazos.forEach { trazo ->
                path(
                    fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round, pathFillType = PathFillType.NonZero,
                    pathBuilder = trazo,
                )
            }
            relleno?.let { r -> path(fill = SolidColor(Color.Black), pathFillType = PathFillType.NonZero, pathBuilder = r) }
        }.build()

    /**
     * Cuatro esquinas del visor + cuadro relleno central. Centro redibujado sobre el marco de Figma («icono ·
     * escanear», componente 4006:51 del archivo 4nHD4ygcnP33UH0gAhaii5, capturado con `get_screenshot`): el marco
     * no tiene una línea horizontal en el centro (versión anterior) sino un cuadro relleno de 8×8 con las esquinas
     * levemente redondeadas, centrado en (12,12) del viewport de 24 (medido por muestreo de píxeles del PNG).
     */
    val Escanear: ImageVector by lazy {
        icono(
            "escanear",
            { moveTo(3f, 7f); verticalLineTo(5f); curveTo(3f, 3.9f, 3.9f, 3f, 5f, 3f); horizontalLineTo(7f) },
            { moveTo(17f, 3f); horizontalLineTo(19f); curveTo(20.1f, 3f, 21f, 3.9f, 21f, 5f); verticalLineTo(7f) },
            { moveTo(21f, 17f); verticalLineTo(19f); curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f); horizontalLineTo(17f) },
            { moveTo(7f, 21f); horizontalLineTo(5f); curveTo(3.9f, 21f, 3f, 20.1f, 3f, 19f); verticalLineTo(17f) },
            relleno = {
                moveTo(9f, 8f); horizontalLineTo(15f)
                curveTo(15.55f, 8f, 16f, 8.45f, 16f, 9f); verticalLineTo(15f)
                curveTo(16f, 15.55f, 15.55f, 16f, 15f, 16f); horizontalLineTo(9f)
                curveTo(8.45f, 16f, 8f, 15.55f, 8f, 15f); verticalLineTo(9f)
                curveTo(8f, 8.45f, 8.45f, 8f, 9f, 8f)
                close()
            },
        )
    }

    val Galeria: ImageVector by lazy {
        icono(
            "galeria",
            { moveTo(5f, 3f); horizontalLineTo(19f); curveTo(20.1f, 3f, 21f, 3.9f, 21f, 5f); verticalLineTo(19f); curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f); horizontalLineTo(5f); curveTo(3.9f, 21f, 3f, 20.1f, 3f, 19f); verticalLineTo(5f); curveTo(3f, 3.9f, 3.9f, 3f, 5f, 3f); close() },
            { moveTo(10f, 8.5f); curveTo(10f, 9.3f, 9.3f, 10f, 8.5f, 10f); curveTo(7.7f, 10f, 7f, 9.3f, 7f, 8.5f); curveTo(7f, 7.7f, 7.7f, 7f, 8.5f, 7f); curveTo(9.3f, 7f, 10f, 7.7f, 10f, 8.5f); close() },
            { moveTo(21f, 15f); lineTo(16f, 10f); lineTo(5f, 21f) },
        )
    }

    val Mas: ImageVector by lazy { icono("mas", { moveTo(12f, 5f); verticalLineTo(19f) }, { moveTo(5f, 12f); horizontalLineTo(19f) }) }

    val Alarma: ImageVector by lazy {
        icono(
            "alarma",
            { moveTo(20f, 13f); curveTo(20f, 17.4f, 16.4f, 21f, 12f, 21f); curveTo(7.6f, 21f, 4f, 17.4f, 4f, 13f); curveTo(4f, 8.6f, 7.6f, 5f, 12f, 5f); curveTo(16.4f, 5f, 20f, 8.6f, 20f, 13f); close() },
            { moveTo(12f, 9f); verticalLineTo(13f); lineTo(14.5f, 15f) },
            { moveTo(5f, 3f); lineTo(2f, 6f) },
            { moveTo(19f, 3f); lineTo(22f, 6f) },
        )
    }

    /** Calendario de pared: dos anillos de argolla arriba, línea de encabezado y una fecha marcada (rellena). */
    val Calendario: ImageVector by lazy {
        icono(
            "calendario",
            { moveTo(6f, 6f); horizontalLineTo(18f); curveTo(19.1f, 6f, 20f, 6.9f, 20f, 8f); verticalLineTo(19f); curveTo(20f, 20.1f, 19.1f, 21f, 18f, 21f); horizontalLineTo(6f); curveTo(4.9f, 21f, 4f, 20.1f, 4f, 19f); verticalLineTo(8f); curveTo(4f, 6.9f, 4.9f, 6f, 6f, 6f); close() },
            { moveTo(4f, 10f); horizontalLineTo(20f) },
            {
                moveTo(9.6f, 5f)
                curveTo(9.6f, 5.88f, 8.88f, 6.6f, 8f, 6.6f)
                curveTo(7.12f, 6.6f, 6.4f, 5.88f, 6.4f, 5f)
                curveTo(6.4f, 4.12f, 7.12f, 3.4f, 8f, 3.4f)
                curveTo(8.88f, 3.4f, 9.6f, 4.12f, 9.6f, 5f)
                close()
            },
            {
                moveTo(17.6f, 5f)
                curveTo(17.6f, 5.88f, 16.88f, 6.6f, 16f, 6.6f)
                curveTo(15.12f, 6.6f, 14.4f, 5.88f, 14.4f, 5f)
                curveTo(14.4f, 4.12f, 15.12f, 3.4f, 16f, 3.4f)
                curveTo(16.88f, 3.4f, 17.6f, 4.12f, 17.6f, 5f)
                close()
            },
            relleno = {
                // fecha marcada, esquina inferior izquierda del cuerpo.
                moveTo(7f, 13f); horizontalLineTo(11f)
                curveTo(11.55f, 13f, 12f, 13.45f, 12f, 14f); verticalLineTo(17f)
                curveTo(12f, 17.55f, 11.55f, 18f, 11f, 18f); horizontalLineTo(7f)
                curveTo(6.45f, 18f, 6f, 17.55f, 6f, 17f); verticalLineTo(14f)
                curveTo(6f, 13.45f, 6.45f, 13f, 7f, 13f)
                close()
            },
        )
    }

    /** Engranaje «flor» de ocho puntas (zigzag con uniones redondeadas) con un aro central. */
    val Ajustes: ImageVector by lazy {
        icono(
            "ajustes",
            {
                moveTo(21.3f, 12f)
                lineTo(18.1f, 14.53f); lineTo(18.58f, 18.58f); lineTo(14.53f, 18.1f); lineTo(12f, 21.3f)
                lineTo(9.47f, 18.1f); lineTo(5.42f, 18.58f); lineTo(5.9f, 14.53f); lineTo(2.7f, 12f)
                lineTo(5.9f, 9.47f); lineTo(5.42f, 5.42f); lineTo(9.47f, 5.9f); lineTo(12f, 2.7f)
                lineTo(14.53f, 5.9f); lineTo(18.58f, 5.42f); lineTo(18.1f, 9.47f); lineTo(21.3f, 12f)
                close()
            },
            {
                moveTo(15.2f, 12f)
                curveTo(15.2f, 13.77f, 13.77f, 15.2f, 12f, 15.2f)
                curveTo(10.23f, 15.2f, 8.8f, 13.77f, 8.8f, 12f)
                curveTo(8.8f, 10.23f, 10.23f, 8.8f, 12f, 8.8f)
                curveTo(13.77f, 8.8f, 15.2f, 10.23f, 15.2f, 12f)
                close()
            },
        )
    }

    /** Monograma «G» (set 48). */
    val Google: ImageVector by lazy {
        icono(
            "google",
            { moveTo(21f, 12f); curveTo(21f, 17f, 17f, 21f, 12f, 21f); curveTo(7f, 21f, 3f, 17f, 3f, 12f); curveTo(3f, 7f, 7f, 3f, 12f, 3f); curveTo(14.5f, 3f, 16.7f, 4f, 18.3f, 5.6f) },
            { moveTo(12f, 12f); horizontalLineTo(21f) },
        )
    }

    /** Sobre (set 48, Outlook · Teams). */
    val Outlook: ImageVector by lazy {
        icono(
            "outlook",
            { moveTo(5f, 5f); horizontalLineTo(19f); curveTo(20.1f, 5f, 21f, 5.9f, 21f, 7f); verticalLineTo(17f); curveTo(21f, 18.1f, 20.1f, 19f, 19f, 19f); horizontalLineTo(5f); curveTo(3.9f, 19f, 3f, 18.1f, 3f, 17f); verticalLineTo(7f); curveTo(3f, 5.9f, 3.9f, 5f, 5f, 5f); close() },
            { moveTo(3f, 7f); lineTo(12f, 13f); lineTo(21f, 7f) },
        )
    }

    /** Smartphone (set 48). */
    val Telefono: ImageVector by lazy {
        icono(
            "telefono",
            { moveTo(9f, 2f); horizontalLineTo(15f); curveTo(16.1f, 2f, 17f, 2.9f, 17f, 4f); verticalLineTo(20f); curveTo(17f, 21.1f, 16.1f, 22f, 15f, 22f); horizontalLineTo(9f); curveTo(7.9f, 22f, 7f, 21.1f, 7f, 20f); verticalLineTo(4f); curveTo(7f, 2.9f, 7.9f, 2f, 9f, 2f); close() },
            { moveTo(11f, 18f); horizontalLineTo(13f) },
        )
    }

    /** Burbuja de chat con cola (M08, canal «WhatsApp»: solo la forma genérica, sin el logo de la marca). */
    val Chat: ImageVector by lazy {
        icono(
            "chat",
            {
                moveTo(5f, 4f); horizontalLineTo(19f); curveTo(20.1f, 4f, 21f, 4.9f, 21f, 6f); verticalLineTo(14f)
                curveTo(21f, 15.1f, 20.1f, 16f, 19f, 16f); horizontalLineTo(8f); lineTo(5f, 20f); verticalLineTo(16f)
                curveTo(3.9f, 16f, 3f, 15.1f, 3f, 14f); verticalLineTo(6f); curveTo(3f, 4.9f, 3.9f, 4f, 5f, 4f); close()
            },
        )
    }

    /** Sobre (M08, canal «Correo»; misma anatomía que [Outlook], con su propio nombre por el uso). */
    val Correo: ImageVector by lazy {
        icono(
            "correo",
            { moveTo(5f, 5f); horizontalLineTo(19f); curveTo(20.1f, 5f, 21f, 5.9f, 21f, 7f); verticalLineTo(17f); curveTo(21f, 18.1f, 20.1f, 19f, 19f, 19f); horizontalLineTo(5f); curveTo(3.9f, 19f, 3f, 18.1f, 3f, 17f); verticalLineTo(7f); curveTo(3f, 5.9f, 3.9f, 5f, 5f, 5f); close() },
            { moveTo(3f, 7f); lineTo(12f, 13f); lineTo(21f, 7f) },
        )
    }

    /** Tres nodos conectados (M08, canal «Más»: hoja de compartir del sistema). */
    val Compartir: ImageVector by lazy {
        icono(
            "compartir",
            { moveTo(18f, 8f); curveTo(19.66f, 8f, 21f, 6.66f, 21f, 5f); curveTo(21f, 3.34f, 19.66f, 2f, 18f, 2f); curveTo(16.34f, 2f, 15f, 3.34f, 15f, 5f); curveTo(15f, 6.66f, 16.34f, 8f, 18f, 8f); close() },
            { moveTo(6f, 15f); curveTo(7.66f, 15f, 9f, 13.66f, 9f, 12f); curveTo(9f, 10.34f, 7.66f, 9f, 6f, 9f); curveTo(4.34f, 9f, 3f, 10.34f, 3f, 12f); curveTo(3f, 13.66f, 4.34f, 15f, 6f, 15f); close() },
            { moveTo(18f, 22f); curveTo(19.66f, 22f, 21f, 20.66f, 21f, 19f); curveTo(21f, 17.34f, 19.66f, 16f, 18f, 16f); curveTo(16.34f, 16f, 15f, 17.34f, 15f, 19f); curveTo(15f, 20.66f, 16.34f, 22f, 18f, 22f); close() },
            { moveTo(8.5f, 10.5f); lineTo(15.5f, 6.5f) },
            { moveTo(8.5f, 13.5f); lineTo(15.5f, 17.5f) },
        )
    }
}
