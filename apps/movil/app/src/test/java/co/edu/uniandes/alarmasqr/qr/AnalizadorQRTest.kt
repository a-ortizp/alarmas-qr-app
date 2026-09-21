package co.edu.uniandes.alarmasqr.qr

import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class AnalizadorQRTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `un QR de evento conocido devuelve su id`() {
        assertEquals(ResultadoQR.EventoDetectado("e-entrega"), AnalizadorQR.interpretar("alarmasqr://evento/e-entrega", repo))
    }

    @Test
    fun `un evento desconocido o cualquier otro contenido es invalido`() {
        assertEquals(ResultadoQR.QRInvalido("alarmasqr://evento/e-nadie"), AnalizadorQR.interpretar("alarmasqr://evento/e-nadie", repo))
        assertEquals(ResultadoQR.QRInvalido("https://menu.resturl.co/…"), AnalizadorQR.interpretar("https://menu.resturl.co/…", repo))
    }
}
