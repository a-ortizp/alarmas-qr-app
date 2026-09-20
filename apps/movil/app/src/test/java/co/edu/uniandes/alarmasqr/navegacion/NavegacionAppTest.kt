package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35]) // tope de Robolectric 4.16; subir junto con Robolectric (compileSdk es 36)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class NavegacionAppTest {
    @get:Rule val regla = createComposeRule()

    private val repositorio = RepositorioDataset.desdeAssets(ApplicationProvider.getApplicationContext())

    @Test
    fun `arranca en M01 y navega por codigo hasta M04 como hoja sobre M02`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp()
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.onNodeWithTag("pantalla-M01").assertIsDisplayed()

        regla.runOnUiThread { pila.reemplazarTodo(Pantalla.M02); pila.irA(Pantalla.M04("a-tutor")) }
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M04").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M04("a-tutor")), pila.toList())
    }

    @Test
    fun `la barra inferior cambia de pestana sin apilar`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.onNodeWithTag("nav-M11").performClick()
        regla.onNodeWithTag("pantalla-M11").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M11), pila.toList())
    }

    @Test
    fun `volver desde el marcador saca la cima de la pila`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.runOnUiThread { pila.irA(Pantalla.M12) }
        regla.onNodeWithTag("volver").performClick()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(1, pila.size)
    }

    @Test
    fun `una hoja real registrada con el metadato de hoja se dibuja sobre la entrada anterior`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme {
                NavegacionApp(pila, repositorio) {
                    entry<Pantalla.M04>(metadata = HojaInferiorSceneStrategy.hoja()) {
                        Text("Hoja real", Modifier.testTag("real-M04"))
                    }
                }
            }
        }
        regla.runOnUiThread { pila.irA(Pantalla.M04("a-tutor")) }
        regla.waitForIdle()
        regla.onNodeWithTag("real-M04").assertIsDisplayed()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M04("a-tutor")), pila.toList())
    }

    @Test
    fun `una entrada registrada en entradas sustituye al marcador`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp()
            AlarmasQRTheme {
                NavegacionApp(pila, repositorio) {
                    entry<Pantalla.M01> { Text("Bienvenida real", Modifier.testTag("real-M01")) }
                }
            }
        }
        regla.onNodeWithTag("real-M01").assertIsDisplayed()
        regla.onNodeWithTag("pantalla-M01").assertDoesNotExist()
        regla.runOnUiThread { pila.irA(Pantalla.M12) }
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
    }

    @Test
    fun `el FAB pasa por M12 la primera vez y por M03 despues, mantenerlo abre M02h`() {
        repositorio.reiniciar()
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.onNodeWithTag("fab-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.runOnUiThread { pila.removeLastOrNull() }
        regla.onNodeWithTag("fab-escanear").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.runOnUiThread { pila.removeLastOrNull() }
        regla.onNodeWithTag("fab-escanear").performSemanticsAction(SemanticsActions.OnLongClick)
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02h").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M02h), pila.toList())
    }
}
