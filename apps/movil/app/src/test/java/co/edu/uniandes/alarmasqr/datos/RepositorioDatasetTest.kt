package co.edu.uniandes.alarmasqr.datos

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.time.LocalDate

class RepositorioDatasetTest {
    private val json = File("src/main/assets/dataset.json").readText()

    @Test
    fun `lee el dataset y deja las alarmas nuevas pendientes de escaneo`() {
        val repo = RepositorioDataset(json)
        assertEquals("Andrés Rojas", repo.dataset.usuario.nombre)
        assertEquals(LocalDate.of(2026, 8, 27), repo.hoy)
        assertEquals(listOf("a-tutor", "a-gimnasio", "a-vuelo", "a-semillero", "a-asado"), repo.alarmas.value.map { it.id })
        assertEquals(listOf("a-entrega"), repo.pendientesDeEscaneo.map { it.id })
        assertEquals("MISO · UniAndes", repo.dataset.alarmas.first { it.id == "a-entrega" }.organizador?.nombre)
        assertEquals(true, repo.evento("e-entrega")?.verificado)
        assertEquals("¿Eliminar alarma?", repo.dataset.mensajes.confirmarEliminarTitulo)
    }

    @Test
    fun `agregarDesdeEvento crea la alarma del evento, marcada como nueva y en orden`() {
        val repo = RepositorioDataset(json)
        val nueva = repo.agregarDesdeEvento("e-entrega")!!
        assertEquals("a-entrega", nueva.id)
        assertTrue(nueva.esNueva)
        assertEquals(listOf("Nueva", "✓ Escaneada"), nueva.chips)
        assertEquals(listOf("a-tutor", "a-gimnasio", "a-vuelo", "a-semillero", "a-entrega", "a-asado"), repo.alarmas.value.map { it.id })
        assertNull(repo.agregarDesdeEvento("e-no-existe"))
        repo.deshacer()
        assertNull(repo.alarma("a-entrega"))
    }

    @Test
    fun `eliminar y deshacer restauran la lista`() {
        val repo = RepositorioDataset(json)
        repo.eliminar("a-tutor")
        assertNull(repo.alarma("a-tutor"))
        assertEquals(4, repo.alarmas.value.size)
        repo.deshacer()
        assertEquals(5, repo.alarmas.value.size)
        assertEquals("a-tutor", repo.alarmas.value.first().id)
    }

    @Test
    fun `reiniciar vuelve a las 5 alarmas iniciales`() {
        val repo = RepositorioDataset(json)
        repo.agregarDesdeEvento("e-entrega")
        repo.permisoCamaraPedido = true
        repo.reiniciar()
        assertEquals(5, repo.alarmas.value.size)
        assertEquals(false, repo.permisoCamaraPedido)
    }

    @Test
    fun `mensajeEliminar rellena evento, fecha y hora`() {
        val repo = RepositorioDataset(json)
        val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }
        assertEquals(
            "Dejarás de recibir el aviso de “Entrega de proyecto UX” (dom 30 · 4:00 pm). Si cambias de opinión, puedes volver a escanear el QR del evento.",
            repo.mensajeEliminar(entrega),
        )
    }
}
