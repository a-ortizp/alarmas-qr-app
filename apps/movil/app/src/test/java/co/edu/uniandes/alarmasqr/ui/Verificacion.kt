package co.edu.uniandes.alarmasqr.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import java.io.File
import java.io.FileOutputStream

/** Marco de los mockups: 390×844 a 2× (Robolectric, `@Config(qualifiers = QUALIFIERS_MOVIL)`). */
const val QUALIFIERS_MOVIL = "w390dp-h844dp-xhdpi"

/**
 * Captura de verificación pixel-perfect (spec §5.2, D2): guarda la raíz compuesta en build/verificacion/<nombre>.png
 * para compararla con la exportación del marco de Figma. No afirma nada: la comparación es visual y la pareja
 * Figma / implementación se copia a docs/verificacion/ al cerrar cada pantalla.
 *
 * Dibuja el `decorView` de la actividad de prueba directamente (`decorView.draw(Canvas)` sobre un `Bitmap`) en vez
 * de `captureToImage()`: bajo Robolectric esa API cuelga en `forceRedraw()` (espera hasta 2000 ms un callback de
 * redibujo real que solo se salta con el atajo `RobolectricIdlingStrategy.hasRobolectricFingerprint()`, añadido en
 * `compose-ui-test-junit4-android` 1.12; el `composeBom` fijado en este proyecto resuelve 1.11.3 y subirlo exige
 * compileSdk 37 + AGP 9.1.0, fuera de alcance). `createComposeRule()` siempre construye por debajo un
 * `AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>`, así que el cast a esa clase
 * concreta para llegar a `activity.window.decorView` es seguro.
 */
fun ComposeContentTestRule.capturar(nombre: String) {
    waitForIdle()
    val actividad = (this as AndroidComposeTestRule<*, *>).activity
    val decor = actividad.window.decorView
    val mapa = Bitmap.createBitmap(decor.width, decor.height, Bitmap.Config.ARGB_8888)
    decor.draw(Canvas(mapa))
    val carpeta = File("build/verificacion").apply { mkdirs() }
    FileOutputStream(carpeta.resolve("$nombre.png")).use { mapa.compress(Bitmap.CompressFormat.PNG, 100, it) }
}
