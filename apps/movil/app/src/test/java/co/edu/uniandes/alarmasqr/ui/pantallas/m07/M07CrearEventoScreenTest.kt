package co.edu.uniandes.alarmasqr.ui.pantallas.m07

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M07CrearEventoScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `campo vacio muestra solo la etiqueta y guardar entrega los valores escritos`() {
        var guardado: Triple<String, String?, Int>? = null
        regla.setContent {
            AlarmasQRTheme {
                M07CrearEventoScreen(
                    alVolver = {},
                    alGuardar = { titulo, lugar, _, anticipacionMin -> guardado = Triple(titulo, lugar, anticipacionMin) },
                )
            }
        }
        regla.onNodeWithTag("pantalla-M07").assertIsDisplayed()
        regla.onNodeWithTag("titulo").performTextInput("Asado familiar")
        regla.onNodeWithTag("lugar").performTextInput("Casa de mis papás")
        regla.onNodeWithText("1 h").performClick()
        regla.onNodeWithTag("guardar").performClick()
        assertEquals(Triple("Asado familiar", "Casa de mis papás", 60), guardado)
    }
}
