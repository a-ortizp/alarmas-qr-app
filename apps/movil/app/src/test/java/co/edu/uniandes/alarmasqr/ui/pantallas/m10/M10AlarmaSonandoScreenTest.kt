package co.edu.uniandes.alarmasqr.ui.pantallas.m10

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
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
class M10AlarmaSonandoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `con alSonar muestra la hora, sal en X min y Ver ruta`() {
        val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }
        var yaVoy = false; var ruta = false
        regla.setContent {
            AlarmasQRTheme { M10AlarmaSonandoScreen(entrega, alYaVoy = { yaVoy = true }, alPosponer = {}, alVerRuta = { ruta = true }) }
        }
        regla.onNodeWithTag("pantalla-M10").assertIsDisplayed()
        regla.onNodeWithText("Sal en 12 min · tráfico moderado", substring = true).assertIsDisplayed()
        regla.onNodeWithText("Ver ruta ›").performClick(); assert(ruta)
        regla.onNodeWithTag("ya-voy").performClick(); assert(yaVoy)
    }

    @Test
    fun `sin alSonar no muestra el destacado de tráfico`() {
        val tutor = repo.dataset.alarmas.first { it.id == "a-tutor" }
        regla.setContent {
            AlarmasQRTheme { M10AlarmaSonandoScreen(tutor, alYaVoy = {}, alPosponer = {}, alVerRuta = null) }
        }
        regla.onNodeWithTag("pantalla-M10").assertIsDisplayed()
        regla.onNodeWithText("Ver ruta ›").assertDoesNotExist()
    }
}
