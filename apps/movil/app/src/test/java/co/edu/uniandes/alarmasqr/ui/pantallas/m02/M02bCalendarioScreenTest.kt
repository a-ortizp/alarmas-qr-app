package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M02bCalendarioScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `muestra el dia seleccionado del dataset y sus alarmas`() {
        val vm = M02bCalendarioViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme { M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = {}, alCambiarActiva = vm::cambiarActiva) }
        }
        regla.onNodeWithTag("pantalla-M02b").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()   // 2026-08-27, día precargado de dataset.calendario.diaSeleccionado
    }

    @Test
    fun `tocar un dia distinto cambia las alarmas mostradas`() {
        val vm = M02bCalendarioViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme { M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = {}, alCambiarActiva = vm::cambiarActiva) }
        }
        // 2026-08-30 solo tiene a-entrega (esNueva=true): es el molde del flujo de escaneo T1 (dataset.eventosQR
        // e-entrega → a-entrega), fuera de repositorio.alarmas hasta escanear, igual que en M02. 2026-08-31 tiene
        // una alarma confirmada (a-asado) con el mismo conteo (1) en dataset.calendario.diasConAlarmas.
        regla.onNodeWithTag("dia-2026-08-31").performClick()
        regla.onNodeWithText("Asado del semillero").assertIsDisplayed()
    }
}
