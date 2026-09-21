package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
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
class M03EscanerScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `sobre Tinta muestra el chip de linterna, el visor y la hoja con dos secundarios, los toques simulados avisan`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M03EscanerScreen(
                    estado = EstadoEscaner(), tienePermiso = false,
                    alVolver = { salidas += "volver" }, alAlternarLinterna = { salidas += "linterna" }, alLeer = {},
                    alTocarVisor = { salidas += "visor" }, alTocarVibra = { salidas += "vibra" },
                    alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" },
                )
            }
        }
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.onNodeWithText("Escanear QR").assertIsDisplayed()
        regla.onNodeWithTag("linterna").assertIsOff()
        // Sin permiso real el visor queda apagado (D5): el chip «● Cámara activa» mentiría sobre su estado, así
        // que no se dibuja (fix round de revisión final; con permiso sí aparece, ver FlujosPersonaATest T1).
        regla.onNodeWithText("● Cámara activa").assertDoesNotExist()
        regla.onNodeWithText("Apunta al código QR del evento").assertIsDisplayed()
        regla.capturar("M03")
        regla.onNodeWithTag("linterna").performClick()
        regla.onNodeWithTag("visor").performClick()
        regla.onNodeWithTag("vibra").performClick()
        regla.onNodeWithTag("escaner-pantallazo").performClick()
        regla.onNodeWithTag("escaner-a-mano").performClick()
        regla.onNodeWithTag("atras").performClick()
        assertEquals(listOf("linterna", "visor", "vibra", "pantallazo", "a-mano", "volver"), salidas)
    }
}
