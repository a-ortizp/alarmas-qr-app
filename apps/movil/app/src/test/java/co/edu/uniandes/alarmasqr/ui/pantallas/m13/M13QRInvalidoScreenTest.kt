package co.edu.uniandes.alarmasqr.ui.pantallas.m13

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.QRInvalido
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
class M13QRInvalidoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val diagnostico = QRInvalido("https://menu.resturl.co/…", "enlace externo", "Parece el menú de un restaurante. Por tu seguridad no abrimos enlaces automáticamente (protección anti-quishing).")

    @Test
    fun `muestra el diagnostico anti-quishing y las tres salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M13QRInvalidoScreen(diagnostico, alVolver = { salidas += "volver" }, alVolverAEscanear = { salidas += "escanear" }, alCrearAMano = { salidas += "a-mano" }, alAbrirEnlace = { salidas += "enlace" })
            }
        }
        regla.onNodeWithTag("pantalla-M13").assertIsDisplayed()
        regla.onNodeWithText("QR sin evento").assertIsDisplayed()
        regla.onNodeWithText("Este QR no contiene un evento").assertIsDisplayed()
        regla.onNodeWithText("Leímos el código, pero no trae fecha ni datos de evento para crear una alarma.").assertIsDisplayed()
        regla.onNodeWithText("QUÉ DETECTAMOS").assertIsDisplayed()
        regla.onNodeWithText("enlace externo").assertIsDisplayed()
        regla.onNodeWithText("https://menu.resturl.co/…").assertIsDisplayed()
        regla.onNodeWithText(diagnostico.diagnostico).assertIsDisplayed()
        regla.capturar("M13")
        regla.onNodeWithTag("volver-a-escanear").performClick()
        regla.onNodeWithTag("invalido-a-mano").performClick()
        regla.onNodeWithTag("abrir-enlace").performClick()
        assertEquals(listOf("escanear", "a-mano", "enlace"), salidas)
    }
}
