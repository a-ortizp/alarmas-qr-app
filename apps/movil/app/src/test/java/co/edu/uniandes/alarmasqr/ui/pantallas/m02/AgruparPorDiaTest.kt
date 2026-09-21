package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class AgruparPorDiaTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `agrupa las 5 alarmas iniciales en HOY, MANANA y LUNES 31`() {
        val grupos = agruparPorDia(repo.alarmas.value, repo.hoy)
        assertEquals(listOf("HOY · JUEVES 27", "MAÑANA · VIERNES 28", "LUNES 31"), grupos.map { it.etiqueta })
        assertEquals(listOf("a-tutor", "a-gimnasio"), grupos[0].alarmas.map { it.id })
        assertEquals(listOf("a-vuelo", "a-semillero"), grupos[1].alarmas.map { it.id })
    }

    @Test
    fun `tras escanear aparece DOMINGO 30 antes del lunes`() {
        repo.agregarDesdeEvento("e-entrega")
        val grupos = agruparPorDia(repo.alarmas.value, repo.hoy)
        assertEquals(listOf("HOY · JUEVES 27", "MAÑANA · VIERNES 28", "DOMINGO 30", "LUNES 31"), grupos.map { it.etiqueta })
    }
}
