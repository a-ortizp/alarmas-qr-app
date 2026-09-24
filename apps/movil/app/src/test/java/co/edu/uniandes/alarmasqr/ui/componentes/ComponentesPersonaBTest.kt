package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import org.junit.Rule
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ComponentesPersonaBTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `BotonPrimario sobreTinta y alto personalizado no rompen el toque`() {
        var tocado = false
        regla.setContent {
            AlarmasQRTheme {
                BotonPrimario("Ya voy", onClick = { tocado = true }, sobreTinta = true, alto = Tamanos.BotonAlarma)
            }
        }
        regla.onNodeWithText("Ya voy").performClick()
        assert(tocado)
    }

    @Test
    fun `FilaAjuste dispara alTocarFila al tocar el rotulo y el control por separado`() {
        var filaTocada = false
        val marcado = mutableStateOf(false)
        regla.setContent {
            AlarmasQRTheme {
                FilaAjuste("Alarma conectada", alTocarFila = { filaTocada = true }) {
                    Interruptor(marcado.value, { marcado.value = it })
                }
            }
        }
        regla.onNodeWithText("Alarma conectada").assertIsDisplayed()
        regla.onNodeWithText("Alarma conectada").performClick()
        assert(filaTocada)
        assert(!marcado.value)   // tocar el rótulo no cambia el switch
    }

    @Test
    fun `FilaAjuste sin alTocarFila deja el rotulo sin accion propia`() {
        regla.setContent {
            AlarmasQRTheme { FilaAjuste("No molestar") { Text("control") } }
        }
        regla.onNodeWithText("No molestar").assertIsDisplayed()
    }

    /** El rótulo que navega es el objetivo táctil de la fila y no puede quedarse en los 40 pt del mockup. */
    @Test
    fun `el rotulo tocable de FilaAjuste llega al area tactil minima`() {
        regla.setContent {
            AlarmasQRTheme { FilaAjuste("Cerrar sesión", alTocarFila = {}) { Text("›") } }
        }
        regla.onNodeWithText("Cerrar sesión").assertHeightIsEqualTo(Tamanos.AreaTactilMinima)
    }
}
