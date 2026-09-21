package co.edu.uniandes.alarmasqr.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
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
 *
 * **Multi-ventana (Tarea 14):** una hoja inferior (`ModalBottomSheet`, M02h/M04) o un diálogo de confirmación
 * (`Dialog`, M04d) se dibujan en su propia ventana Android, no en el `decorView` de la actividad de prueba — por
 * eso las capturas de las Tareas 8–13 de esas pantallas solo mostraban el contenido, sin la hoja/velo compuestos
 * sobre el fondo. Esta versión compone TODAS las ventanas visibles, no solo la de la actividad: lee
 * `android.view.WindowManagerGlobal.getInstance().mViews` por reflexión (no hay shadow público en Robolectric 4.16
 * — `ShadowWindowManagerGlobal` no expone `getViews()`, añadido en versiones posteriores; ver `raicesDeVentanasVisibles`)
 * y dibuja cada raíz de ventana visible, en el mismo orden en que Android las agrega (que es también su z-order:
 * la actividad primero, cada `Dialog`/`Popup` después, encima), trasladada a su posición real en pantalla
 * (`getLocationOnScreen`) relativa al `decorView` de la actividad. Si la reflexión falla —campo interno renombrado
 * en otra versión de Android— cae al único `decorView` de la actividad, como antes (documentar la limitación si
 * ocurre; no se ha observado en `@Config(sdk = [35])`).
 */
fun ComposeContentTestRule.capturar(nombre: String) {
    waitForIdle()
    val actividad = (this as AndroidComposeTestRule<*, *>).activity
    val decor = actividad.window.decorView
    val mapa = Bitmap.createBitmap(decor.width, decor.height, Bitmap.Config.ARGB_8888)
    val lienzo = Canvas(mapa)
    val raices = raicesDeVentanasVisibles()
    if (raices.isEmpty()) {
        decor.draw(lienzo)
    } else {
        val origen = IntArray(2)
        decor.getLocationOnScreen(origen)
        for (raiz in raices) {
            runCatching {
                val posicion = IntArray(2)
                raiz.getLocationOnScreen(posicion)
                lienzo.save()
                lienzo.translate((posicion[0] - origen[0]).toFloat(), (posicion[1] - origen[1]).toFloat())
                raiz.draw(lienzo)
                lienzo.restore()
            }
        }
    }
    val carpeta = File("build/verificacion").apply { mkdirs() }
    FileOutputStream(carpeta.resolve("$nombre.png")).use { mapa.compress(Bitmap.CompressFormat.PNG, 100, it) }
}

/**
 * Todas las raíces de ventana visibles bajo Robolectric, de abajo hacia arriba (mismo orden que
 * `WindowManagerGlobal.mViews`: la actividad de prueba es la primera; cada hoja o diálogo abierto después queda
 * al final, encima). Es el campo privado real de AOSP que el propio `WindowManager` usa para pintar — Robolectric
 * no lo reimplementa, así que el contenido coincide con el de un dispositivo real. Sin shadow público que lo
 * exponga en esta versión de Robolectric (4.16), se lee por reflexión; si el campo no existe (renombrado en otra
 * versión de Android) devuelve la lista vacía y `capturar()` cae al `decorView` de la actividad únicamente.
 */
private fun raicesDeVentanasVisibles(): List<View> = runCatching {
    val claseWMG = Class.forName("android.view.WindowManagerGlobal")
    val instancia = claseWMG.getMethod("getInstance").invoke(null)
    val campoViews = claseWMG.getDeclaredField("mViews").apply { isAccessible = true }
    @Suppress("UNCHECKED_CAST")
    val vistas = (campoViews.get(instancia) as List<View>).toList()
    vistas.filter { it.visibility == View.VISIBLE && it.windowVisibility == View.VISIBLE }
}.getOrDefault(emptyList())
