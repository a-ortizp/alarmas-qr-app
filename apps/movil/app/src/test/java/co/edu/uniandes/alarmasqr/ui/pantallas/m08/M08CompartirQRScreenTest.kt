package co.edu.uniandes.alarmasqr.ui.pantallas.m08

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M08CompartirQRScreenTest {
    @get:Rule val regla = createComposeRule()
    private val evento = EventoQR(id = "e-tutor", alarmaId = "a-tutor", titulo = "Reunión con el tutor", codigoQR = "alarmasqr://evento/e-tutor", escaneos = 0, etiqueta = "Aún sin escaneos · recién creado")

    @Test
    fun `muestra el titulo, el QR y dispara los tres callbacks`() {
        var compartido = false; var descargado = false; var copiado = false
        regla.setContent {
            AlarmasQRTheme {
                M08CompartirQRScreen(evento, alVolver = {}, alCompartir = { compartido = true }, alDescargar = { descargado = true }, alCopiarEnlace = { copiado = true })
            }
        }
        regla.onNodeWithTag("pantalla-M08").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()
        regla.onNodeWithTag("qr").assertIsDisplayed()
        regla.onNodeWithTag("compartir").performClick(); assert(compartido)
        regla.onNodeWithTag("descargar").performClick(); assert(descargado)
        regla.onNodeWithTag("copiar-enlace").performClick(); assert(copiado)
    }
}
