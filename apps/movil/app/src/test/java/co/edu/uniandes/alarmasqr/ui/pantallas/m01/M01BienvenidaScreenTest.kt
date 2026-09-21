package co.edu.uniandes.alarmasqr.ui.pantallas.m01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
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
class M01BienvenidaScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `muestra los textos del mockup, alterna calendarios y avisa las dos salidas`() {
        var comenzar = 0; var luego = 0
        regla.setContent { AlarmasQRTheme { M01BienvenidaScreen(alComenzar = { comenzar++ }, alConectarLuego = { luego++ }) } }
        regla.onNodeWithTag("pantalla-M01").assertIsDisplayed()
        regla.onNodeWithText("Escanea y listo").assertIsDisplayed()
        regla.onNodeWithText("Apunta la cámara al QR del evento: la alarma queda programada sin escribir fecha, hora ni nombre.").assertIsDisplayed()
        regla.onNodeWithText("CONECTA TU CALENDARIO (OPCIONAL)").assertIsDisplayed()
        regla.capturar("M01")
        regla.onNodeWithTag("calendario-google").assertIsOff().performClick().assertIsOn()
        regla.onNodeWithTag("calendario-outlook").assertIsOff()
        regla.onNodeWithTag("calendario-telefono").assertIsOff()
        regla.onNodeWithText("Comenzar").performClick()
        regla.onNodeWithText("Conectar luego en Ajustes").performClick()
        assertEquals(1, comenzar); assertEquals(1, luego)
    }
}
