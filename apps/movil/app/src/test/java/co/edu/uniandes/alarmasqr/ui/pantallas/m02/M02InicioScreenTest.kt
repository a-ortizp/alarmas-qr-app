package co.edu.uniandes.alarmasqr.ui.pantallas.m02

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
class M02InicioScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `la lista agrupa por dia, muestra las 5 tarjetas y avisa el toque con el id`() {
        var tocada = ""
        val estado = EstadoInicio(agruparPorDia(repo.alarmas.value, repo.hoy))
        regla.setContent { AlarmasQRTheme { M02InicioScreen(estado, alTocarAlarma = { tocada = it }, alCambiarActiva = { _, _ -> }) } }
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        regla.onNodeWithText("Mis alarmas").assertIsDisplayed()
        regla.onNodeWithText("HOY · JUEVES 27").assertIsDisplayed()
        regla.onNodeWithText("MAÑANA · VIERNES 28").assertIsDisplayed()
        regla.onNodeWithText("Vuelo BOG–MDE").assertIsDisplayed()
        regla.onNodeWithText("✓ Escaneada").assertIsDisplayed()
        regla.capturar("M02")
        regla.onNodeWithTag("alarma-a-semillero").performClick()
        assertEquals("a-semillero", tocada)
    }

    @Test
    fun `el estado vacio muestra el mensaje del dataset y tres salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent { AlarmasQRTheme { M02vEstadoVacio(repo.dataset.mensajes, alEscanear = { salidas += "escanear" }, alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" }) } }
        regla.onNodeWithTag("pantalla-M02v").assertIsDisplayed()
        regla.onNodeWithText("Aún no tienes alarmas").assertIsDisplayed()
        regla.onNodeWithText(repo.dataset.mensajes.sinAlarmasDetalle).assertIsDisplayed()
        regla.capturar("M02v")
        regla.onNodeWithTag("vacio-escanear").performClick()
        regla.onNodeWithTag("vacio-pantallazo").performClick()
        regla.onNodeWithTag("vacio-a-mano").performClick()
        assertEquals(listOf("escanear", "pantallazo", "a-mano"), salidas)
    }

    @Test
    fun `la hoja Agregar evento tiene tres filas y el aviso del pantallazo`() {
        val salidas = mutableListOf<String>()
        regla.setContent { AlarmasQRTheme { M02hAgregarEventoSheet(alEscanear = { salidas += "escanear" }, alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" }) } }
        regla.onNodeWithTag("pantalla-M02h").assertIsDisplayed()
        regla.onNodeWithText("Agregar evento").assertIsDisplayed()
        regla.onNodeWithText("La alarma queda lista sin escribir nada").assertIsDisplayed()
        regla.onNodeWithText("Leemos el QR que aparezca en la imagen").assertIsDisplayed()
        regla.onNodeWithText("Escribe fecha, hora y lugar; te damos su QR").assertIsDisplayed()
        regla.onNodeWithText("También puedes compartir un pantallazo desde WhatsApp o la galería con Alarmas QR.").assertIsDisplayed()
        regla.capturar("M02h-contenido")
        regla.onNodeWithTag("hoja-escanear").performClick()
        regla.onNodeWithTag("hoja-pantallazo").performClick()
        regla.onNodeWithTag("hoja-a-mano").performClick()
        assertEquals(listOf("escanear", "pantallazo", "a-mano"), salidas)
    }
}
