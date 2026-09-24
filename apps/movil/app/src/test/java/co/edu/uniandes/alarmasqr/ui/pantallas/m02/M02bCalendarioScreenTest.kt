package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
class M02bCalendarioScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    private fun montar(vm: M02bCalendarioViewModel = M02bCalendarioViewModel(repo), alVerLista: () -> Unit = {}): M02bCalendarioViewModel {
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme {
                M02bCalendarioScreen(
                    estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = {}, alCambiarActiva = vm::cambiarActiva,
                    alMesAnterior = vm::mesAnterior, alMesSiguiente = vm::mesSiguiente, alVerLista = alVerLista,
                )
            }
        }
        return vm
    }

    @Test
    fun `muestra el mes y el dia seleccionado del dataset con sus alarmas`() {
        montar()
        regla.onNodeWithTag("pantalla-M02b").assertIsDisplayed()
        regla.onNodeWithText("Agosto 2026").assertIsDisplayed()
        regla.onNodeWithText("JUE 27 · 2 ALARMAS").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()   // 2026-08-27, día precargado de dataset.calendario.diaSeleccionado
        regla.capturar("M02b")
    }

    @Test
    fun `tocar un dia distinto cambia las alarmas mostradas`() {
        montar()
        // 2026-08-31 tiene una alarma confirmada (a-asado), fuera del rango de dataset.calendario.diasConAlarmas
        // (ya no se usa: el conteo ahora se deriva en vivo de RepositorioDataset.alarmas).
        regla.onNodeWithTag("dia-2026-08-31").performClick()
        regla.onNodeWithText("Asado del semillero").assertIsDisplayed()
    }

    @Test
    fun `las flechas del mes navegan y tocar Lista dispara el cambio de vista`() {
        var listaTocada = false
        montar(alVerLista = { listaTocada = true })
        regla.onNodeWithTag("mes-siguiente").performClick()
        regla.onNodeWithText("Septiembre 2026").assertIsDisplayed()
        regla.onNodeWithTag("mes-anterior").performClick()
        regla.onNodeWithText("Agosto 2026").assertIsDisplayed()
        regla.onNodeWithTag("boton-lista").performClick()
        assert(listaTocada)
    }
}
