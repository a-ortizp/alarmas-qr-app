package co.edu.uniandes.alarmasqr.ui.pantallas.m12

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
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
class M12PermisoCamaraScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `explica el permiso en tres pasos y ofrece las alternativas abajo`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M12PermisoCamaraScreen(alVolver = { salidas += "volver" }, alAbrirAjustes = { salidas += "ajustes" }, alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" })
            }
        }
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.onNodeWithText("Permiso de cámara").assertIsDisplayed()
        regla.onNodeWithText("La cámara está apagada para la app").assertIsDisplayed()
        regla.onNodeWithText("Solo la usamos para leer códigos QR de eventos. Nunca guardamos fotos ni videos.").assertIsDisplayed()
        regla.onNodeWithText("ACTÍVALA EN 3 PASOS").assertIsDisplayed()
        regla.onNodeWithText("Permisos › Cámara").assertIsDisplayed()
        regla.onNodeWithText("Elegir “Permitir con la app en uso”").assertIsDisplayed()
        regla.onNodeWithText("mientras tanto").assertIsDisplayed()
        regla.capturar("M12")
        regla.onNodeWithTag("abrir-ajustes").performClick()
        regla.onNodeWithTag("permiso-pantallazo").performClick()
        regla.onNodeWithTag("permiso-a-mano").performClick()
        regla.onNodeWithTag("atras").performClick()
        assertEquals(listOf("ajustes", "pantallazo", "a-mano", "volver"), salidas)
    }
}
