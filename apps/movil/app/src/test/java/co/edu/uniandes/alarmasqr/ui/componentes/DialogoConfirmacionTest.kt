package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class DialogoConfirmacionTest {
    @get:Rule val regla = createComposeRule()

    private fun montar(alSeguro: () -> Unit, alConfirmar: () -> Unit) = regla.setContent {
        AlarmasQRTheme {
            DialogoConfirmacion(
                titulo = "¿Eliminar alarma?", cuerpo = "Dejarás de recibir el aviso.", rotuloSeguro = "Conservar", rotuloConfirmar = "Eliminar",
                destructivo = true, alSeguro = alSeguro, alConfirmar = alConfirmar,
            )
        }
    }

    @Test
    fun `muestra titulo y cuerpo y cada boton avisa su accion`() {
        var seguro = 0; var confirmar = 0
        montar({ seguro++ }, { confirmar++ })
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.onNodeWithText("Dejarás de recibir el aviso.").assertIsDisplayed()
        regla.onNodeWithTag("dialogo-seguro").performClick()
        regla.onNodeWithTag("dialogo-confirmar").performClick()
        assertEquals(1, seguro); assertEquals(1, confirmar)
    }

    @Test
    fun `tocar el velo equivale a la accion segura`() {
        var seguro = 0; var confirmar = 0
        montar({ seguro++ }, { confirmar++ })
        regla.onNodeWithTag("velo").performClick()
        assertEquals(1, seguro); assertEquals(0, confirmar)
    }
}
