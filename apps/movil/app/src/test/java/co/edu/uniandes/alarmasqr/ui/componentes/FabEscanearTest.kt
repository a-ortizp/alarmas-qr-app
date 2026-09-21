package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FabEscanearTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `toque y mantener disparan acciones distintas`() {
        var toques = 0
        var mantenidos = 0
        regla.setContent { AlarmasQRTheme { FabEscanear(alTocar = { toques++ }, alMantener = { mantenidos++ }) } }
        regla.onNodeWithTag("fab-escanear").assertHeightIsEqualTo(56.dp).performClick()
        regla.onNodeWithTag("fab-escanear").performSemanticsAction(SemanticsActions.OnLongClick)
        regla.onNodeWithText("Escanear").assertExists()
        assertEquals(1, toques)
        assertEquals(1, mantenidos)
    }
}
