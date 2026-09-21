package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class TarjetaAlarmaTest {
    @get:Rule val regla = createComposeRule()

    private val tutor = Alarma(id = "a-tutor", titulo = "Reunión con el tutor", eventoInicio = "2026-08-27T08:00:00-05:00", suena = "2026-08-27T07:30:00-05:00", lugar = "Aula SD-703", origen = "creada-por-mi", estado = "activa", anticipacionMin = 30, trayectoMin = 0, chips = listOf("Creada por mí"))

    @Test
    fun `muestra hora, sufijo, linea del evento, chip e interruptor y avisa el toque`() {
        var tocada = false
        regla.setContent { AlarmasQRTheme { TarjetaAlarma(tutor, onClick = { tocada = true }, alCambiarActiva = {}) } }
        regla.onNodeWithText("7:30").assertIsDisplayed()
        regla.onNodeWithText("am").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()
        regla.onNodeWithText("evento 8:00 am · Aula SD-703").assertIsDisplayed()
        regla.onNodeWithText("Creada por mí").assertIsDisplayed()
        regla.onNodeWithTag("interruptor-a-tutor").assertIsOn()
        regla.onNodeWithTag("alarma-a-tutor").performClick()
        assertTrue(tocada)
    }

    @Test
    fun `una alarma pausada apaga el interruptor y lo dice en la linea`() {
        val gimnasio = tutor.copy(id = "a-gimnasio", titulo = "Gimnasio", eventoInicio = "2026-08-27T09:30:00-05:00", lugar = null, estado = "pausada", chips = emptyList())
        regla.setContent { AlarmasQRTheme { TarjetaAlarma(gimnasio, onClick = {}, alCambiarActiva = {}) } }
        regla.onNodeWithText("pausada · evento 9:30 am").assertIsDisplayed()
        regla.onNodeWithTag("interruptor-a-gimnasio").assertIsOff()
    }
}
