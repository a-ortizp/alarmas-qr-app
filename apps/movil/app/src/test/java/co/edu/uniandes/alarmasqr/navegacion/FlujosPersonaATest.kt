package co.edu.uniandes.alarmasqr.navegacion

import android.Manifest
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Flujos T1, T6 y T7 de FUNCIONALIDADES.md sobre las entradas reales (entradasApp), navegando por código. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FlujosPersonaATest {
    @get:Rule val regla = createComposeRule()
    private val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    private val repositorio = RepositorioDataset.desdeAssets(app)
    private lateinit var pila: NavBackStack<NavKey>

    @Before
    fun preparar() {
        repositorio.reiniciar()
        shadowOf(app).grantPermissions(Manifest.permission.CAMERA)   // «Abrir ajustes» ⏩ termina de inmediato (D5)
        shadowOf(app).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        // Con CAMERA concedido, la entrada real de M03 pinta VisorCamara (F-M03), que construye un
        // BarcodeScanner de ML Kit. En un dispositivo real, com.google.mlkit.common.internal.MlKitInitProvider
        // (declarado por la AAR `common`) inicializa MlKitContext solo al arrancar el proceso; Robolectric no
        // dispara ese ContentProvider a tiempo dentro de una prueba JVM aunque el manifiesto fusionado lo declare
        // (isIncludeAndroidResources = true), así que BarcodeScanning.getClient() falla con
        // «MlKitContext has not been initialized». Se llama la misma inicialización pública que usaría ese
        // proveedor, para que el visor real se pueda montar en esta prueba de flujo.
        com.google.mlkit.common.sdkinternal.MlKitContext.initializeIfNeeded(app)
    }

    private fun montar(inicio: Pantalla) = regla.setContent {
        pila = rememberBackStackApp(inicio)
        AlarmasQRTheme { NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) } }
    }

    @Test
    fun `T1 escanear un QR deja la alarma guardada M02 - M12 - M03 - M04 - M05`() {
        montar(Pantalla.M02)
        regla.onNodeWithTag("fab-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.onNodeWithTag("abrir-ajustes").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.onNodeWithTag("visor").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M04").assertIsDisplayed()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()          // hoja sobre la lista atenuada
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M04("a-entrega")), pila.toList())
        regla.capturar("M04")
        regla.onNodeWithTag("eliminar").performClick()
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.capturar("M04d-flujo")
        regla.onNodeWithTag("dialogo-seguro").performClick()
        regla.onNodeWithTag("listo").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M05").assertIsDisplayed()
        regla.onNodeWithText("Nueva").assertIsDisplayed()
        assertNotNull(repositorio.alarma("a-entrega"))
    }

    @Test
    fun `T6 errores - QR que no es evento M03 - M13 - M03 y hoja M02h`() {
        montar(Pantalla.M02)
        regla.onNodeWithTag("fab-escanear").performSemanticsAction(SemanticsActions.OnLongClick)
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02h").assertIsDisplayed()
        regla.capturar("M02h")
        regla.onNodeWithTag("hoja-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.onNodeWithTag("abrir-ajustes").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.onNodeWithTag("vibra").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M13").assertIsDisplayed()
        regla.onNodeWithTag("volver-a-escanear").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M03), pila.toList())
    }

    @Test
    fun `T7 crear cuenta o continuar como invitado M01 - M00a - M00b`() {
        montar(Pantalla.M01)
        regla.onNodeWithText("Comenzar").performClick()
        regla.onNodeWithTag("pantalla-M00a").assertIsDisplayed()
        regla.onNodeWithTag("pie-acceso").performClick()
        regla.onNodeWithTag("pantalla-M00b").assertIsDisplayed()
        regla.onNodeWithTag("entrar").performClick()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02), pila.toList())
    }

    @Test
    fun `T7b invitado desde M01 llega al inicio vacio`() {
        montar(Pantalla.M01)
        regla.onNodeWithText("Conectar luego en Ajustes").performClick()
        regla.onNodeWithTag("pantalla-M02v").assertIsDisplayed()
        regla.onNodeWithTag("vacio-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
    }
}
