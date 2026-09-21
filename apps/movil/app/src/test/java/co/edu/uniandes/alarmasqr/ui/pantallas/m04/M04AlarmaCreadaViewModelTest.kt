package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import co.edu.uniandes.alarmasqr.alarma.Programador
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class M04AlarmaCreadaViewModelTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())
    private val programadas = mutableListOf<String>()
    private val canceladas = mutableListOf<String>()
    private val programador = object : Programador {
        override fun programar(alarma: Alarma): Long { programadas += alarma.id; return 0L }
        override fun cancelar(id: String) { canceladas += id }
    }

    @Test
    fun `al crearse programa la alarma y expone el mensaje de M04d`() {
        repo.agregarDesdeEvento("e-entrega")
        val vm = M04AlarmaCreadaViewModel(repo, programador, "a-entrega")
        assertEquals(listOf("a-entrega"), programadas)
        assertEquals("Entrega de proyecto UX", vm.estado.value.alarma.titulo)
        assertTrue(vm.estado.value.mensajeEliminar.startsWith("Dejarás de recibir el aviso de “Entrega de proyecto UX” (dom 30 · 4:00 pm)."))
        assertFalse(vm.estado.value.dialogoAbierto)
    }

    @Test
    fun `abrir, conservar y eliminar`() {
        repo.agregarDesdeEvento("e-entrega")
        val vm = M04AlarmaCreadaViewModel(repo, programador, "a-entrega")
        vm.abrirDialogo(); assertTrue(vm.estado.value.dialogoAbierto)
        vm.cerrarDialogo(); assertFalse(vm.estado.value.dialogoAbierto)
        vm.eliminar()
        assertEquals(listOf("a-entrega"), canceladas)
        assertNull(repo.alarma("a-entrega"))
    }
}
