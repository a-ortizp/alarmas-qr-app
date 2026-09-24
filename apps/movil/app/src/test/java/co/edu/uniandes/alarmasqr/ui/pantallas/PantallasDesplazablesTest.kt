package co.edu.uniandes.alarmasqr.ui.pantallas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uniandes.alarmasqr.datos.QRInvalido
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.pantallas.m00.M00aRegistroScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m00.M00bEntrarScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m01.M01BienvenidaScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioViewModel
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02hAgregarEventoSheet
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02vEstadoVacio
import co.edu.uniandes.alarmasqr.ui.pantallas.m03.M03bPantallazoScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m04.EstadoAlarmaCreada
import co.edu.uniandes.alarmasqr.ui.pantallas.m04.M04AlarmaCreadaSheet
import co.edu.uniandes.alarmasqr.ui.pantallas.m07.M07CrearEventoScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m09.M09CambioEventoScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m11.M11AjustesScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m11.M11AjustesViewModel
import co.edu.uniandes.alarmasqr.ui.pantallas.m12.M12PermisoCamaraScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m13.M13QRInvalidoScreen
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/** Teléfono más bajo que el marco de los mockups (390×844): el alto se reduce a 560 dp. */
private const val QUALIFIERS_BAJO = "w390dp-h560dp-xhdpi"

/**
 * Regla de scroll vertical (README «Scroll vertical en pantallas de columna»): en un teléfono bajo, el último
 * elemento de cada pantalla de columna y de cada hoja se alcanza desplazando, en vez de quedar cortado.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_BAJO)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class PantallasDesplazablesTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    private fun montar(pantalla: @Composable () -> Unit) = regla.setContent { AlarmasQRTheme { pantalla() } }
    private fun alcanzarTexto(texto: String) = regla.onNodeWithText(texto, substring = true).performScrollTo().assertIsDisplayed()
    private fun alcanzarEtiqueta(etiqueta: String) = regla.onNodeWithTag(etiqueta).performScrollTo().assertIsDisplayed()

    @Test
    fun `M01 alcanza «Conectar luego en Ajustes»`() {
        montar { M01BienvenidaScreen(alComenzar = {}, alConectarLuego = {}) }
        alcanzarTexto("Conectar luego en Ajustes")
    }

    @Test
    fun `M00a alcanza el pie «Inicia sesión»`() {
        montar { M00aRegistroScreen(alCrearCuenta = {}, alInvitado = {}, alYaTengoCuenta = {}) }
        alcanzarTexto("Inicia sesión")
    }

    @Test
    fun `M00b alcanza el pie «Crea tu cuenta»`() {
        montar { M00bEntrarScreen("andres@correo.com", alEntrar = {}, alInvitado = {}, alCrearCuenta = {}, alRecuperar = {}) }
        alcanzarTexto("Crea tu cuenta")
    }

    @Test
    fun `M02v alcanza «Crear el evento a mano»`() {
        montar { M02vEstadoVacio(repo.dataset.mensajes, alEscanear = {}, alElegirPantallazo = {}, alCrearAMano = {}) }
        alcanzarEtiqueta("vacio-a-mano")
    }

    @Test
    fun `M12 alcanza las alternativas ancladas abajo`() {
        montar { M12PermisoCamaraScreen(alVolver = {}, alAbrirAjustes = {}, alElegirPantallazo = {}, alCrearAMano = {}) }
        alcanzarEtiqueta("permiso-a-mano")
    }

    @Test
    fun `M13 alcanza «Abrir el enlace bajo mi responsabilidad»`() {
        val diagnostico = QRInvalido("https://menu.resturl.co/…", "enlace externo", "Parece el menú de un restaurante.")
        montar { M13QRInvalidoScreen(diagnostico, alVolver = {}, alVolverAEscanear = {}, alCrearAMano = {}, alAbrirEnlace = {}) }
        alcanzarEtiqueta("abrir-enlace")
    }

    @Test
    fun `M03b alcanza la nota final`() {
        val datos = repo.dataset.pantallazoRecibido
        montar { M03bPantallazoScreen(datos, repo.evento(datos.eventoDetectado)!!, alVolver = {}, alContinuar = {}, alElegirOtra = {}) }
        alcanzarTexto("puedes crear el evento a mano")
    }

    @Test
    fun `la hoja M02h alcanza su nota de pie`() {
        montar { M02hAgregarEventoSheet(alEscanear = {}, alElegirPantallazo = {}, alCrearAMano = {}) }
        alcanzarTexto("compartir un pantallazo desde WhatsApp")
    }

    @Test
    fun `la hoja M04 alcanza «No puedo asistir»`() {
        val entrega = repo.agregarDesdeEvento("e-entrega")!!
        val estado = EstadoAlarmaCreada(entrega, mensajeEliminar = repo.mensajeEliminar(entrega))
        montar {
            M04AlarmaCreadaSheet(estado, repo.dataset.mensajes, alListo = {}, alEditar = {}, alAbrirDialogo = {}, alConservar = {}, alEliminar = {})
        }
        alcanzarTexto("No puedo asistir")
    }

    @Test
    fun `M07 alcanza «Guardar y crear QR», anclado abajo con Spacer(weight)`() {
        montar { M07CrearEventoScreen(alVolver = {}, alGuardar = { _, _, _, _ -> }) }
        alcanzarEtiqueta("guardar")
    }

    @Test
    fun `M09 alcanza «Mantener alarma» en un telefono bajo`() {
        val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }
        montar { M09CambioEventoScreen(entrega, alAceptar = {}, alMantener = {}, alCerrar = {}, alVerAlarma = {}) }
        alcanzarEtiqueta("mantener")
    }

    @Test
    fun `M02b alcanza la ultima alarma del dia seleccionado en un telefono bajo`() {
        val vm = M02bCalendarioViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme { M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = {}, alCambiarActiva = vm::cambiarActiva) }
        }
        alcanzarEtiqueta("alarma-a-tutor")
    }

    @Test
    fun `M11 alcanza la fila Cerrar sesion en un telefono bajo`() {
        val vm = M11AjustesViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme {
                M11AjustesScreen(
                    estado = estado, mensajes = repo.dataset.mensajes,
                    alCambiarNoMolestar = {}, alCambiarConfirmarAutoAjustar = {}, alAbrirDialogo = {}, alConservar = {}, alCerrarSesion = {},
                )
            }
        }
        alcanzarEtiqueta("cerrar-sesion")
    }
}
