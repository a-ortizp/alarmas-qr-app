package co.edu.uniandes.alarmasqr.ui.pantallas.m08

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M08CompartirQRScreenTest {
    @get:Rule val regla = createComposeRule()
    private val evento = EventoQR(id = "e-tutor", alarmaId = "a-tutor", titulo = "Reunión con el tutor", codigoQR = "alarmasqr://evento/e-tutor", escaneos = 0, etiqueta = "Aún sin escaneos · recién creado")
    private val alarma = Alarma(
        id = "a-tutor", titulo = "Reunión con el tutor", eventoInicio = "2026-08-27T08:00:00-05:00", suena = "2026-08-27T07:30:00-05:00",
        origen = "creada", estado = "activa", anticipacionMin = 30, trayectoMin = 0,
    )

    @Test
    fun `muestra la tarjeta, el estado del QR y dispara los seis callbacks`() {
        var whatsapp = false; var correo = false; var mas = false; var png = false; var pdf = false; var copiado = false
        regla.setContent {
            AlarmasQRTheme {
                M08CompartirQRScreen(
                    evento, alarma, alVolver = {},
                    alCompartirWhatsApp = { whatsapp = true }, alCompartirCorreo = { correo = true }, alCompartirMas = { mas = true },
                    alDescargarPNG = { png = true }, alDescargarPDF = { pdf = true }, alCopiarEnlace = { copiado = true },
                )
            }
        }
        regla.onNodeWithTag("pantalla-M08").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()
        regla.onNodeWithText("Escanéalo y te avisamos").assertIsDisplayed()
        regla.onNodeWithText("Tu alarma quedó programada · sonará 7:30 am").assertIsDisplayed()
        regla.onNodeWithText("Aún sin escaneos · recién creado").assertIsDisplayed()
        regla.onNodeWithTag("qr").assertIsDisplayed()
        regla.onNodeWithTag("whatsapp").performClick(); assert(whatsapp)
        regla.onNodeWithTag("correo").performClick(); assert(correo)
        regla.onNodeWithTag("mas").performClick(); assert(mas)
        regla.onNodeWithTag("descargar-png").performClick(); assert(png)
        regla.onNodeWithTag("descargar-pdf").performClick(); assert(pdf)
        regla.onNodeWithTag("copiar-enlace").performClick(); assert(copiado)
    }
}
