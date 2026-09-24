package co.edu.uniandes.alarmasqr.ui.pantallas.m09

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
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
class M09CambioEventoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())
    private val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }

    @Test
    fun `muestra la hora nueva y la anterior, y dispara aceptar-mantener-cerrar`() {
        var aceptado = false; var mantenido = false; var cerrado = false
        regla.setContent {
            AlarmasQRTheme { M09CambioEventoScreen(entrega, alAceptar = { aceptado = true }, alMantener = { mantenido = true }, alCerrar = { cerrado = true }, alVerAlarma = {}) }
        }
        regla.onNodeWithTag("pantalla-M09").assertIsDisplayed()
        regla.onNodeWithText("Notificación push · hace 2 min").assertIsDisplayed()
        regla.onNodeWithText("“Entrega de proyecto UX” cambió de hora").assertIsDisplayed()
        regla.onNodeWithText("Cambio hecho por MISO · UniAndes").assertIsDisplayed()
        regla.onNodeWithText("✓ verificado").assertIsDisplayed()
        regla.onNodeWithText("dom 30 · 4:00 pm").assertIsDisplayed()   // ANTES: evento sin el cambio
        regla.onNodeWithText("dom 30 · 5:30 pm · Aula SD-703, Universidad").assertIsDisplayed()   // AHORA
        regla.onNodeWithText("4:45").assertIsDisplayed()   // hora y sufijo en Text separados, como en M06/M04
        regla.onNodeWithText("30 min de margen + trayecto · antes sonaba 3:15 pm").assertIsDisplayed()
        regla.capturar("M09")
        regla.onNodeWithTag("aceptar").performClick(); assert(aceptado)
        regla.onNodeWithTag("mantener").performClick(); assert(mantenido)
        regla.onNodeWithTag("cerrar").performClick(); assert(cerrado)
    }
}
