package co.edu.uniandes.alarmasqr.ui.pantallas.m00

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M00AccesoScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `M00a muestra el formulario de registro y sus cuatro salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent { AlarmasQRTheme { M00aRegistroScreen(alCrearCuenta = { salidas += "crear" }, alInvitado = { salidas += "invitado" }, alYaTengoCuenta = { salidas += "entrar" }) } }
        regla.onNodeWithTag("pantalla-M00a").assertIsDisplayed()
        regla.onNodeWithText("Crea tu cuenta").assertIsDisplayed()
        regla.onNodeWithText("Respalda tus alarmas en la nube y úsalas también en la web.").assertIsDisplayed()
        regla.onNodeWithText("CORREO").assertIsDisplayed()
        regla.onNodeWithText("CONTRASEÑA").assertIsDisplayed()
        regla.onNodeWithText("Acepto el tratamiento de mis datos según la política de privacidad (Ley 1581 de 2012).").assertIsDisplayed()
        regla.onNodeWithText("o continúa con").assertIsDisplayed()
        regla.onNodeWithText("Como invitado, las alarmas quedan solo en este teléfono.").assertIsDisplayed()
        regla.capturar("M00a")
        regla.onNodeWithTag("crear-cuenta").performClick()
        regla.onNodeWithTag("google").performClick()
        regla.onNodeWithTag("invitado").performClick()
        regla.onNodeWithTag("pie-acceso").performClick()
        assertEquals(listOf("crear", "crear", "invitado", "entrar"), salidas)
    }

    @Test
    fun `M00b muestra el inicio de sesion con el correo del usuario y sus salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M00bEntrarScreen("andres@correo.com", alEntrar = { salidas += "entrar" }, alInvitado = { salidas += "invitado" }, alCrearCuenta = { salidas += "crear" }, alRecuperar = { salidas += "recuperar" })
            }
        }
        regla.onNodeWithTag("pantalla-M00b").assertIsDisplayed()
        regla.onNodeWithText("Hola de nuevo").assertIsDisplayed()
        regla.onNodeWithText("Entra para sincronizar tus alarmas con la nube y la web.").assertIsDisplayed()
        regla.onNodeWithText("andres@correo.com").assertIsDisplayed()
        regla.capturar("M00b")
        regla.onNodeWithTag("recuperar").performClick()
        regla.onNodeWithTag("entrar").performClick()
        regla.onNodeWithTag("outlook").performClick()
        regla.onNodeWithTag("invitado").performClick()
        regla.onNodeWithTag("pie-acceso").performClick()
        assertEquals(listOf("recuperar", "entrar", "entrar", "invitado", "crear"), salidas)
    }
}
