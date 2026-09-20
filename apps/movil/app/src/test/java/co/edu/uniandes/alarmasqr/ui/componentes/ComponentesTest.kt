package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
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
class ComponentesTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `los botones miden 52, los enlaces 32 y la flecha 44, y avisan el toque`() {
        var primario = 0
        var secundario = 0
        var enlace = 0
        var atras = 0
        regla.setContent {
            // Column: sin ella, los cuatro controles (todos fillMaxWidth o superpuestos en (0,0)) se apilan bajo
            // AlarmasQRTheme (MaterialTheme no aporta un layout) y el toque a uno puede ser interceptado por otro
            // (mismo problema diagnosticado en la prueba de casilla/fila de este archivo).
            AlarmasQRTheme {
                Column {
                    BotonPrimario("Comenzar", onClick = { primario++ }, modifier = Modifier.testTag("primario"))
                    BotonSecundario("Google", onClick = { secundario++ }, modifier = Modifier.testTag("secundario"))
                    BotonEnlace("Continuar como invitado", onClick = { enlace++ }, modifier = Modifier.testTag("enlace"))
                    BotonAtras(onClick = { atras++ })
                }
            }
        }
        regla.onNodeWithTag("primario").assertHeightIsEqualTo(52.dp).performClick()
        assertEquals(1, primario)
        regla.onNodeWithTag("secundario").assertHeightIsEqualTo(52.dp).performClick()
        assertEquals(1, secundario)
        regla.onNodeWithTag("enlace").assertHeightIsEqualTo(32.dp).performClick()
        assertEquals(1, enlace)
        regla.onNodeWithTag("atras").assertHeightIsEqualTo(44.dp).assertWidthIsEqualTo(44.dp).performClick()
        assertEquals(1, atras)
    }

    @Test
    fun `el campo mide 48, muestra la etiqueta en mayusculas y devuelve lo escrito`() {
        var valor by mutableStateOf("")
        regla.setContent { AlarmasQRTheme { CampoTexto(valor, { valor = it }, etiqueta = "Correo", pista = "tucorreo@ejemplo.com", modifier = Modifier.testTag("campo")) } }
        regla.onNodeWithTag("campo").assertHeightIsEqualTo(48.dp)
        regla.onNodeWithText("CORREO").assertIsDisplayed()
        regla.onNodeWithText("tucorreo@ejemplo.com").assertIsDisplayed()
        regla.onNodeWithTag("campo").performTextInput("andres@correo.com")
        assertEquals("andres@correo.com", valor)
    }

    @Test
    fun `la casilla y la fila de opcion alternan su estado`() {
        var casilla by mutableStateOf(false)
        var fila by mutableStateOf(false)
        regla.setContent {
            // Column: sin ella, Casilla (20×20) y FilaOpcionCalendario (fillMaxWidth) se apilan superpuestas en
            // (0,0) bajo AlarmasQRTheme (MaterialTheme no aporta un layout), y la fila —compuesta después—
            // intercepta el toque destinado a la casilla (confirmado con un caso mínimo instrumentado).
            AlarmasQRTheme {
                Column {
                    Casilla(casilla, { casilla = it }, modifier = Modifier.testTag("casilla"))
                    FilaOpcionCalendario(Iconos.Google, "Google Calendar", fila, { fila = it }, modifier = Modifier.testTag("fila"))
                }
            }
        }
        regla.onNodeWithTag("casilla").assertIsOff().performClick().assertIsOn()
        regla.onNodeWithTag("fila").assertHeightIsEqualTo(36.dp).assertIsOff().performClick().assertIsOn()
        assertTrue(casilla && fila)
    }

    @Test
    fun `los chips miden 20 y 32 y el chip de control se activa`() {
        var activo by mutableStateOf(false)
        regla.setContent {
            AlarmasQRTheme {
                ChipEstado("✓ Escaneada", VarianteChip.Escaneada, modifier = Modifier.testTag("estado"))
                ChipControl("Linterna · auto", activo, onClick = { activo = !activo }, modifier = Modifier.testTag("control"))
            }
        }
        regla.onNodeWithTag("estado").assertHeightIsEqualTo(20.dp)
        regla.onNodeWithTag("control").assertHeightIsEqualTo(32.dp).assertIsOff().performClick().assertIsOn()
    }
}
