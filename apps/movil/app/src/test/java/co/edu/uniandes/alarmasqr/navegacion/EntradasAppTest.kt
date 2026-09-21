package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Fix round de revisión final: M04 con un id que el repositorio no conoce ya no debe estrellar la app
 * (`repositorio.alarma(id) ?: error(...)` en `M04AlarmaCreadaViewModel`) — alcanzable tras la muerte del proceso
 * con la hoja abierta (la pila restaurada trae `M04("a-entrega")` pero el repositorio fresco esconde las alarmas
 * `esNueva`) o vía un intent externo (`MainActivity` es exportada).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class EntradasAppTest {
    @get:Rule val regla = createComposeRule()
    private val repositorio = RepositorioDataset.desdeAssets(ApplicationProvider.getApplicationContext())

    @Before fun reiniciar() = repositorio.reiniciar()

    @Test
    fun `M04 con un id desconocido saca la hoja de la pila en vez de estrellar la app`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) } }
        }
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        regla.runOnUiThread { pila.irA(Pantalla.M04("no-existe")) }
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02), pila.toList())
    }
}
