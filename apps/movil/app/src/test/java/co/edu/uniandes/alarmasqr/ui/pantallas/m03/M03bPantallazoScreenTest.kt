package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
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
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M03bPantallazoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `muestra el mensaje de WhatsApp con el QR enmarcado y las salidas`() {
        val salidas = mutableListOf<String>()
        val datos = repo.dataset.pantallazoRecibido
        regla.setContent {
            AlarmasQRTheme { M03bPantallazoScreen(datos, repo.evento(datos.eventoDetectado)!!, alVolver = { salidas += "volver" }, alContinuar = { salidas += "continuar" }, alElegirOtra = { salidas += "otra" }) }
        }
        regla.onNodeWithTag("pantalla-M03b").assertIsDisplayed()
        regla.onNodeWithText("Pantallazo recibido").assertIsDisplayed()
        regla.onNodeWithText("✓ QR detectado").assertIsDisplayed()
        regla.onNodeWithText("QR de evento detectado en tu pantallazo").assertIsDisplayed()
        regla.onNodeWithText("Grupo MISO UX · hoy 8:12 am").assertIsDisplayed()
        regla.onNodeWithText(datos.mensaje).assertIsDisplayed()
        regla.onNodeWithText("Origen: ${datos.origen}").assertIsDisplayed()
        regla.onNodeWithText("Si el pantallazo no trae un QR, puedes crear el evento a mano.").assertIsDisplayed()
        regla.capturar("M03b")
        regla.onNodeWithTag("continuar").performClick()
        regla.onNodeWithTag("elegir-otra").performClick()
        regla.onNodeWithTag("atras").performClick()
        assertEquals(listOf("continuar", "otra", "volver"), salidas)
    }
}
