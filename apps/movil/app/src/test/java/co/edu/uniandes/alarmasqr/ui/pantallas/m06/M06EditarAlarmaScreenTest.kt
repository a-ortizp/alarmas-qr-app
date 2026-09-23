package co.edu.uniandes.alarmasqr.ui.pantallas.m06

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import org.junit.Assert.assertNull
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
class M06EditarAlarmaScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    private fun montar(id: String): M06EditarAlarmaViewModel {
        val vm = M06EditarAlarmaViewModel(repo, id)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme {
                M06EditarAlarmaScreen(
                    estado = estado, mensajes = repo.dataset.mensajes, mensajeEliminar = vm.mensajeEliminar, puedeVerCambioOrganizador = vm.puedeVerCambioOrganizador,
                    alVolver = {}, alElegirAnticipacion = vm::elegirAnticipacion, alElegirSonido = vm::elegirSonido, alCambiarConfirmar = vm::cambiarConfirmar,
                    alTocarCambioOrganizador = {}, alGestionarCalendario = {}, alGuardar = vm::guardar,
                    alAbrirDialogo = vm::abrirDialogo, alConservar = vm::cerrarDialogo, alEliminar = vm::eliminar,
                )
            }
        }
        return vm
    }

    @Test
    fun `muestra el titulo de la alarma y guarda la anticipacion elegida`() {
        montar("a-tutor")
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()
        regla.capturar("M06")
        regla.onNodeWithText("1 h").performClick()
        regla.onNodeWithTag("guardar").performClick()
        assertEquals(60, repo.alarma("a-tutor")?.anticipacionMin)
    }

    @Test
    fun `eliminar abre M06d y confirma la eliminacion`() {
        montar("a-gimnasio")
        regla.onNodeWithTag("eliminar").performClick()
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.capturar("M06d")
        regla.onNodeWithTag("dialogo-confirmar").performClick()
        assertNull(repo.alarma("a-gimnasio"))
    }

    @Test
    fun `sin cambioDelOrganizador la fila de alarma conectada no navega`() {
        val vm = montar("a-tutor")
        assertEquals(false, vm.puedeVerCambioOrganizador)
    }

    @Test
    fun `guardar relee el repositorio y no revierte un cambio del organizador aplicado mientras M06 seguia en la pila`() {
        // Repro de la M06 retenida por rememberViewModelStoreNavEntryDecorator(): M06("a-entrega") construye su VM
        // (snapshot de la alarma pre-cambio), M09 aplica el cambio del organizador sobre el MISMO repositorio, y solo
        // luego M06 (con su VM ya construido, sin recrear) llama a guardar(). Antes de la corrección, guardar()
        // escribía el snapshot viejo y revertía eventoInicio/suena; ahora debe releer el repositorio primero.
        repo.agregarDesdeEvento("e-entrega")
        val vm = M06EditarAlarmaViewModel(repo, "a-entrega")
        repo.aplicarCambioOrganizador("a-entrega")
        vm.guardar()
        assertEquals("2026-08-30T17:30:00-05:00", repo.alarma("a-entrega")?.eventoInicio)
        assertEquals("2026-08-30T16:45:00-05:00", repo.alarma("a-entrega")?.suena)
    }
}
