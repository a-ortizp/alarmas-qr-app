package co.edu.uniandes.alarmasqr.ui

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import java.io.File
import java.io.FileOutputStream

/** Marco de los mockups: 390×844 a 2× (Robolectric, `@Config(qualifiers = QUALIFIERS_MOVIL)`). */
const val QUALIFIERS_MOVIL = "w390dp-h844dp-xhdpi"

/**
 * Captura de verificación pixel-perfect (spec §5.2, D2): guarda la raíz compuesta en build/verificacion/<nombre>.png
 * para compararla con la exportación del marco de Figma. No afirma nada: la comparación es visual y la pareja
 * Figma / implementación se copia a docs/verificacion/ al cerrar cada pantalla.
 */
fun ComposeContentTestRule.capturar(nombre: String) {
    waitForIdle()
    val mapa = onRoot().captureToImage().asAndroidBitmap()
    val carpeta = File("build/verificacion").apply { mkdirs() }
    FileOutputStream(carpeta.resolve("$nombre.png")).use { mapa.compress(Bitmap.CompressFormat.PNG, 100, it) }
}
