package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.qr.ResultadoQR
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.File

/**
 * Fix round de revisión final: sin esto, volver de M13 con el mismo QR inválido todavía frente a la cámara
 * reabre M13 de inmediato en bucle (ML Kit sigue llamando `alLeer` con el mismo contenido cada cuadro).
 */
class M03EscanerViewModelTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `una relectura del mismo contenido antes de 2 s tras consumirlo se ignora`() {
        var ahora = 0L
        val vm = M03EscanerViewModel(repo, ahora = { ahora })

        vm.leer("qr-invalido")
        assertEquals(ResultadoQR.QRInvalido("qr-invalido"), vm.estado.value.resultado)
        vm.consumirResultado()
        assertNull(vm.estado.value.resultado)

        ahora += Movimiento.IgnorarRelecturaMs - 1
        vm.leer("qr-invalido")
        assertNull(vm.estado.value.resultado)   // ignorada: mismo contenido, dentro de la ventana
    }

    @Test
    fun `la misma lectura vuelve a procesarse pasados los 2 s`() {
        var ahora = 0L
        val vm = M03EscanerViewModel(repo, ahora = { ahora })

        vm.leer("qr-invalido")
        vm.consumirResultado()

        ahora += Movimiento.IgnorarRelecturaMs
        vm.leer("qr-invalido")
        assertEquals(ResultadoQR.QRInvalido("qr-invalido"), vm.estado.value.resultado)
    }

    @Test
    fun `un contenido distinto no se ignora aunque llegue de inmediato`() {
        var ahora = 0L
        val vm = M03EscanerViewModel(repo, ahora = { ahora })

        vm.leer("qr-invalido")
        vm.consumirResultado()

        vm.leer(repo.evento("e-entrega")!!.codigoQR)
        assertEquals(ResultadoQR.EventoDetectado("e-entrega"), vm.estado.value.resultado)
    }
}
