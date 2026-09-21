package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
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
class ColumnaDesplazableTest {
    @get:Rule val regla = createComposeRule()

    /** Contenido de 300 dp + un Spacer(weight) + un pie de 40 dp, en un contenedor de [alto]. */
    private fun montar(alto: Int) = regla.setContent {
        AlarmasQRTheme {
            Box(Modifier.height(alto.dp).testTag("contenedor")) {
                ColumnaDesplazable(Modifier.fillMaxSize()) {
                    Spacer(Modifier.height(300.dp))
                    Spacer(Modifier.weight(1f))
                    Text("Pie", Modifier.height(40.dp).testTag("pie"))
                }
            }
        }
    }

    @Test
    fun `con espacio de sobra el Spacer weight sigue anclando el pie abajo, como en el mockup`() {
        montar(alto = 600)
        val contenedor = regla.onNodeWithTag("contenedor").getUnclippedBoundsInRoot()
        val pie = regla.onNodeWithTag("pie").getUnclippedBoundsInRoot()
        assertEquals(contenedor.bottom, pie.bottom)
    }

    @Test
    fun `sin espacio el contenido se desplaza y el pie se alcanza en vez de cortarse`() {
        montar(alto = 200)
        val pie = regla.onNodeWithTag("pie").getUnclippedBoundsInRoot()
        assertTrue("el pie queda por debajo del contenedor antes de desplazar", pie.top >= 200.dp)
        regla.onNodeWithTag("pie").performScrollTo().assertIsDisplayed()
    }
}
