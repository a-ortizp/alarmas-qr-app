package co.edu.uniandes.alarmasqr.ui.pantallas.m11

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
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
class M11AjustesScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    private fun montar(): M11AjustesViewModel {
        val vm = M11AjustesViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme {
                M11AjustesScreen(
                    estado = estado, mensajes = repo.dataset.mensajes,
                    alCambiarNoMolestar = vm::cambiarNoMolestar, alCambiarConfirmarAutoAjustar = vm::cambiarConfirmarAutoAjustar,
                    alAbrirDialogo = vm::abrirDialogo, alConservar = vm::cerrarDialogo, alCerrarSesion = vm::cerrarDialogo,
                )
            }
        }
        return vm
    }

    @Test
    fun `muestra los ajustes por defecto, los permisos del dataset y abre el dialogo de cerrar sesion`() {
        montar()
        regla.onNodeWithTag("pantalla-M11").assertIsDisplayed()
        regla.onNodeWithText("30 min").assertIsDisplayed()   // anticipacionPorDefectoMin del dataset
        regla.onNodeWithText("Google Calendar").assertIsDisplayed()
        regla.onNodeWithText("Gestionar mis datos (Ley 1581)").assertIsDisplayed()
        regla.onAllNodesWithText("✓ activo").onFirst().assertIsDisplayed()   // alarmasExactas y notificaciones: true en dataset.json
        regla.onNodeWithText("Revisar").assertIsDisplayed()       // bateriaSinRestricciones: false
        regla.onNodeWithText("Sin este permiso, Android puede silenciar la alarma en segundo plano.").assertIsDisplayed()
        regla.capturar("M11")
        regla.onNodeWithTag("cerrar-sesion").performClick()
        regla.onNodeWithText("¿Cerrar sesión?").assertIsDisplayed()
        regla.capturar("M11d")
    }
}
