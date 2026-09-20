package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
@Config(sdk = [35])
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
}
