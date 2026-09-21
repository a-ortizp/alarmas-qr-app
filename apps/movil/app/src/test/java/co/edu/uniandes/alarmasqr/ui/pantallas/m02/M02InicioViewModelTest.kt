package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import co.edu.uniandes.alarmasqr.alarma.Programador
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

/**
 * Fix round de revisión final: «Deshacer» en M05 debía cancelar también la alarma real de AlarmManager, no solo
 * quitarla de la lista. Robolectric (sin GraphicsMode, no hay UI): `M02InicioViewModel` usa `viewModelScope`
 * (`stateIn`), que necesita `Dispatchers.Main` inicializado — Robolectric provee el Looper principal real que
 * `kotlinx-coroutines-android` requiere; un JUnit plano sin él falla con «Main dispatcher had failed to initialize».
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class M02InicioViewModelTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())
    private val canceladas = mutableListOf<String>()
    private val programador = object : Programador {
        override fun programar(alarma: Alarma): Long = 0L
        override fun cancelar(id: String) { canceladas += id }
    }

    @Test
    fun `deshacer en M05 cancela la alarma real de la nueva alarma, no solo la quita de la lista`() {
        repo.agregarDesdeEvento("e-entrega")
        val vm = M02InicioViewModel(repo, alarmaNueva = "a-entrega", programador = programador)
        vm.deshacer()
        assertEquals(listOf("a-entrega"), canceladas)
        assertNull(repo.alarma("a-entrega"))
    }

    @Test
    fun `sin programador deshacer sigue revirtiendo la lista sin fallar`() {
        repo.agregarDesdeEvento("e-entrega")
        val vm = M02InicioViewModel(repo, alarmaNueva = "a-entrega")
        vm.deshacer()
        assertNull(repo.alarma("a-entrega"))
    }

    @Test
    fun `deshacer sin mutacion pendiente no cancela nada`() {
        val vm = M02InicioViewModel(repo, programador = programador)
        vm.deshacer()
        assertEquals(emptyList<String>(), canceladas)
    }
}
