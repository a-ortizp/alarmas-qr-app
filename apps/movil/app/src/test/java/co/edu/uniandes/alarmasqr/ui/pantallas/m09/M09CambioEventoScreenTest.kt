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
        regla.onNodeWithText("4:45 pm").assertIsDisplayed()
        regla.onNodeWithText("Antes sonaba 3:15 pm").assertIsDisplayed()
        regla.capturar("M09")
        regla.onNodeWithTag("aceptar").performClick(); assert(aceptado)
        regla.onNodeWithTag("mantener").performClick(); assert(mantenido)
        regla.onNodeWithTag("cerrar").performClick(); assert(cerrado)
    }
}
