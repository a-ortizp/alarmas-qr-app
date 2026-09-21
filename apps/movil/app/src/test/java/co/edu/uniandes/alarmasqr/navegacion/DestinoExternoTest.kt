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

    @Test
    fun `un esquema distinto de alarmasqr no resuelve pantalla, aunque la ruta coincida`() {
        // MainActivity es exportada y destinoDesdeIntent acepta cualquier intent ACTION_VIEW entrante: sin filtrar
        // el esquema, un enlace https con la misma ruta abriría M10 igual que la notificación real de la app.
        assertNull(destinoDesdeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://evil/alarma/x/creada"))))
        assertNull(destinoDesdeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://evil/alarma/a-entrega/sonando"))))
    }
}
