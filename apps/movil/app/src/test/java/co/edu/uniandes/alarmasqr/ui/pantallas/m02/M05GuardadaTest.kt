package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.navegacion.NavegacionApp
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.navegacion.entradasApp
import co.edu.uniandes.alarmasqr.navegacion.reemplazarTodo
import co.edu.uniandes.alarmasqr.navegacion.rememberBackStackApp
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M05GuardadaTest {
    @get:Rule val regla = createComposeRule()
    private val repositorio = RepositorioDataset.desdeAssets(ApplicationProvider.getApplicationContext())

    @Before fun reiniciar() = repositorio.reiniciar()

    @Test
    fun `M05 resalta la alarma nueva, muestra el snackbar y Deshacer la quita y vuelve a M02`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) } }
        }
        regla.runOnUiThread { repositorio.agregarDesdeEvento("e-entrega"); pila.reemplazarTodo(Pantalla.M05("a-entrega")) }
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M05").assertIsDisplayed()
        regla.onNodeWithText("DOMINGO 30").assertIsDisplayed()
        regla.onNodeWithText("Nueva").assertIsDisplayed()
        regla.onNodeWithText(repositorio.dataset.mensajes.alarmaGuardada).assertIsDisplayed()
        regla.onNodeWithTag("fab-escanear").assertIsDisplayed()
        regla.capturar("M05")
        regla.onNodeWithTag("deshacer").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertNull(repositorio.alarma("a-entrega"))
    }
}
