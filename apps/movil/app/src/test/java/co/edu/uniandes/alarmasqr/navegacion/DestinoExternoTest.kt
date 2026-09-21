package co.edu.uniandes.alarmasqr.navegacion

import android.content.Intent
import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class DestinoExternoTest {
    @Test
    fun `un pantallazo compartido abre M03b y un deep link de alarma abre M10`() {
        assertEquals(Pantalla.M03b, destinoDesdeIntent(Intent(Intent.ACTION_SEND).setType("image/png")))
        assertEquals(Pantalla.M10("a-entrega"), destinoDesdeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("alarmasqr://app/alarma/a-entrega/sonando"))))
        assertNull(destinoDesdeIntent(Intent(Intent.ACTION_MAIN)))
        assertNull(destinoDesdeIntent(null))
    }
}
