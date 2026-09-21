package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
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
class M04AlarmaCreadaSheetTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())
    private val entrega = repo.agregarDesdeEvento("e-entrega")!!

    @Test
    fun `la hoja muestra el evento, el bloque Sonara y las dos salidas`() {
        val salidas = mutableListOf<String>()
        val estado = EstadoAlarmaCreada(entrega, mensajeEliminar = repo.mensajeEliminar(entrega))
        regla.setContent {
            AlarmasQRTheme { M04AlarmaCreadaSheet(estado, repo.dataset.mensajes, alListo = { salidas += "listo" }, alEditar = {}, alAbrirDialogo = { salidas += "dialogo" }, alConservar = {}, alEliminar = {}) }
        }
        regla.onNodeWithTag("pantalla-M04").assertIsDisplayed()
        regla.onNodeWithText("¡Alarma programada!").assertIsDisplayed()
        regla.onNodeWithText("Datos leídos del QR — verifícalos").assertIsDisplayed()
        regla.onNodeWithText("Entrega de proyecto UX").assertIsDisplayed()
        regla.onNodeWithText("Dom 30 de agosto · 4:00 pm (GMT-5)").assertIsDisplayed()
        regla.onNodeWithText("MISO · UniAndes").assertIsDisplayed()
        regla.onNodeWithText("✓ verificado").assertIsDisplayed()
        regla.onNodeWithText("SONARÁ").assertIsDisplayed()
        regla.onNodeWithText("3:15").assertIsDisplayed()
        regla.onNodeWithText("También se agregó a Google Calendar.").assertIsDisplayed()
        regla.capturar("M04-contenido")
        regla.onNodeWithTag("listo").performClick()
        regla.onNodeWithTag("eliminar").performClick()
        assertEquals(listOf("listo", "dialogo"), salidas)
    }

    @Test
    fun `con el dialogo abierto, Conservar y el velo vuelven y Eliminar confirma`() {
        val salidas = mutableListOf<String>()
        val estado = EstadoAlarmaCreada(entrega, dialogoAbierto = true, mensajeEliminar = repo.mensajeEliminar(entrega))
        regla.setContent {
            AlarmasQRTheme { M04AlarmaCreadaSheet(estado, repo.dataset.mensajes, alListo = {}, alEditar = {}, alAbrirDialogo = {}, alConservar = { salidas += "conservar" }, alEliminar = { salidas += "eliminar" }) }
        }
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.onNodeWithText(estado.mensajeEliminar).assertIsDisplayed()
        regla.capturar("M04d")
        regla.onNodeWithTag("dialogo-seguro").performClick()
        // performClick() por defecto toca el centro geométrico de "velo" (pantalla completa), que con el cuerpo
        // real de M04d (dom 30 · 4:00 pm) coincide exactamente con el centro del diálogo (Box centra su hijo):
        // Robolectric, a diferencia de un dispositivo real, entrega ese toque al diálogo (que lo absorbe) y no al
        // velo — limitación conocida de Robolectric con toques sintéticos sobre clics anidados (p. ej.
        // robolectric/robolectric#8420, #9595). Se toca una esquina del velo, claramente fuera del diálogo, para
        // probar el mismo comportamiento («tocar el velo equivale a la acción segura») sin depender de esa zona.
        regla.onNodeWithTag("velo").performTouchInput { click(Offset(10f, 10f)) }
        regla.onNodeWithTag("dialogo-confirmar").performClick()
        assertEquals(listOf("conservar", "conservar", "eliminar"), salidas)
    }
}
