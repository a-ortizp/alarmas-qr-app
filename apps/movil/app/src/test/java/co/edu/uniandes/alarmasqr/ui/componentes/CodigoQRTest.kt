package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CodigoQRTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `genera un QR cuadrado del tamano pedido`() {
        regla.setContent { AlarmasQRTheme { CodigoQR("alarmasqr://evento/e-entrega", tamano = 120.dp) } }
        regla.onNodeWithContentDescription("Código QR del evento").assertWidthIsEqualTo(120.dp).assertHeightIsEqualTo(120.dp)
    }
}
