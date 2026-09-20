package co.edu.uniandes.alarmasqr.datos

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class RepositorioDatasetTest {
    private val json = File("src/main/assets/dataset.json").readText()

    @Test
    fun `lee el dataset de los mockups`() {
        val repo = RepositorioDataset(json)
        assertEquals("Andrés Rojas", repo.dataset.usuario.nombre)
        assertEquals(6, repo.alarmas.value.size)
        assertEquals("Reunión con el tutor", repo.alarma("a-tutor")?.titulo)
        assertEquals("¿Eliminar alarma?", repo.dataset.mensajes.confirmarEliminarTitulo)
        assertEquals("e-entrega", repo.dataset.pantallazoRecibido.eventoDetectado)
    }

    @Test
    fun `eliminar y deshacer restauran la lista`() {
        val repo = RepositorioDataset(json)
        repo.eliminar("a-tutor")
        assertNull(repo.alarma("a-tutor"))
        assertEquals(5, repo.alarmas.value.size)
        repo.deshacer()
        assertEquals(6, repo.alarmas.value.size)
        assertEquals("a-tutor", repo.alarmas.value.first().id)
    }

    @Test
    fun `agregar pone la alarma en orden cronologico y deshacer la quita`() {
        val repo = RepositorioDataset(json)
        val nueva = repo.alarma("a-tutor")!!.copy(id = "a-nueva", titulo = "Nueva", eventoInicio = "2026-08-27T08:30:00-05:00", suena = "2026-08-27T08:00:00-05:00", chips = listOf("Nueva"))
        repo.agregar(nueva)
        assertEquals(7, repo.alarmas.value.size)
        assertEquals(listOf("a-tutor", "a-nueva"), repo.alarmas.value.take(2).map { it.id })
        repo.deshacer()
        assertEquals(6, repo.alarmas.value.size)
        assertTrue(repo.alarmas.value.none { it.id == "a-nueva" })
    }
}
