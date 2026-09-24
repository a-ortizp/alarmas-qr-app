package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Flujos T2 y T4 de TRAZABILIDAD.md §4 sobre las entradas reales (entradasApp), navegando por código. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FlujosPersonaBTest {
    @get:Rule val regla = createComposeRule()
    private val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    private val repositorio = RepositorioDataset.desdeAssets(app)
    private lateinit var pila: NavBackStack<NavKey>

    @Before
    fun preparar() { repositorio.reiniciar() }

    private fun montar(inicio: Pantalla) = regla.setContent {
        pila = rememberBackStackApp(inicio)
        AlarmasQRTheme { NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) } }
    }

    @Test
    fun `T2 cambiar anticipacion y sonido M05 - M06`() {
        // T1 ya corrió en otra prueba: aquí se entra directo a M06 sobre una alarma existente («a-gimnasio»).
        montar(Pantalla.M06("a-gimnasio"))
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        regla.onNodeWithText("1 h").performClick()
        regla.onNodeWithTag("guardar").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(60, repositorio.alarma("a-gimnasio")?.anticipacionMin)
    }

    @Test
    fun `T4 reaccionar a un cambio de hora M06 - M09 - M10 - M02`() {
        // «a-entrega» solo existe en el repositorio después de T1 (escanear su QR); se simula agregándola primero.
        repositorio.agregarDesdeEvento("e-entrega")
        montar(Pantalla.M06("a-entrega"))
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        regla.onNodeWithText("Confirmar antes de auto-ajustarse").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M09").assertIsDisplayed()
        regla.onNodeWithTag("aceptar").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M10").assertIsDisplayed()
        regla.onNodeWithTag("ya-voy").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals("2026-08-30T17:30:00-05:00", repositorio.alarma("a-entrega")?.eventoInicio)
    }

    @Test
    fun `M09 al ver alarma vuelve al M06 que ya estaba debajo, sin duplicarlo en la pila`() {
        // Regresión: `alVerAlarma` reemplazaba la cima de la pila (M09) por un SEGUNDO Pantalla.M06(id), dejando dos
        // claves M06 idénticas seguidas (rompe el back y arriesga un crash de SaveableStateHolder). Debe hacer pop.
        repositorio.agregarDesdeEvento("e-entrega")
        montar(Pantalla.M06("a-entrega"))
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        val tamanoEnM06 = pila.size
        regla.onNodeWithText("Confirmar antes de auto-ajustarse").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M09").assertIsDisplayed()
        assertEquals(tamanoEnM06 + 1, pila.size)
        regla.onNodeWithTag("ver-alarma").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        assertEquals(tamanoEnM06, pila.size)
    }

    @Test
    fun `eliminar desde M06 no deja la pila vacia`() {
        // Regresión: `alEliminar` hacía pila.reemplazarTodo(M02) mientras la guarda de «alarma inexistente» de esta
        // misma entrada (para cuando M06 se alcanza tras la muerte del proceso) también le hacía pop a la pila en la
        // recomposición siguiente, dejándola vacía → «NavDisplay backstack cannot be empty» (estrella la app; visto
        // en el emulador, no solo en teoría).
        montar(Pantalla.M06("a-gimnasio"))
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        regla.onNodeWithTag("eliminar").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("dialogo-confirmar").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(1, pila.size)
        assertEquals(null, repositorio.alarma("a-gimnasio"))
    }

    @Test
    fun `T3 crear evento propio y compartir M02 - M02h - M07 - M08`() {
        // M07 y M08 solo se alcanzan por flujo (la hoja M02h las abre con reemplazarCima): no tienen entrada
        // propia fuera de la navegación real, así que su captura de verificación pixel-perfect vive aquí.
        montar(Pantalla.M02)
        regla.onNodeWithTag("fab-escanear").performSemanticsAction(SemanticsActions.OnLongClick)
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02h").assertIsDisplayed()
        regla.onNodeWithTag("hoja-a-mano").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M07").assertIsDisplayed()
        regla.capturar("M07")
        regla.onNodeWithTag("titulo").performTextInput("Asado familiar")
        regla.onNodeWithTag("lugar").performTextInput("Casa de mis papás")
        regla.onNodeWithTag("guardar").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M08").assertIsDisplayed()
        regla.onNodeWithText("Asado familiar").assertIsDisplayed()
        regla.capturar("M08")
    }
}
