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
    fun `cambiarEstado durante la ventana de deshacer no se revierte con la alarma guardada`() {
        val repo = RepositorioDataset(json)
        repo.agregarDesdeEvento("e-entrega")
        repo.cambiarEstado("a-gimnasio", pausada = false)
        repo.deshacer()
        assertNull(repo.alarma("a-entrega"))
        assertEquals("activa", repo.alarma("a-gimnasio")?.estado)
    }

    @Test
    fun `olvidarDeshacer cierra la ventana sin revertir la mutacion`() {
        val repo = RepositorioDataset(json)
        repo.agregarDesdeEvento("e-entrega")
        repo.olvidarDeshacer()
        repo.deshacer()
        assertEquals("a-entrega", repo.alarma("a-entrega")?.id)
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

    @Test
    fun `a-entrega ya trae cambioDelOrganizador y alSonar del dataset`() {
        val repo = RepositorioDataset(json)
        val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }
        val cambio = entrega.cambioDelOrganizador!!
        assertEquals("2026-08-30T17:30:00-05:00", cambio.nuevoInicio)
        assertEquals("2026-08-30T16:45:00-05:00", cambio.nuevaHoraDeAlarma)
        assertEquals("2026-08-30T15:15:00-05:00", cambio.antesSonaba)
        assertEquals("MISO · UniAndes", cambio.autor)
        val alSonar = entrega.alSonar!!
        assertEquals(12, alSonar.salEnMin)
        assertEquals("moderado", alSonar.traficoActual)
        assertTrue(alSonar.rutaDisponible)
        assertNull(repo.dataset.alarmas.first { it.id == "a-gimnasio" }.cambioDelOrganizador)
    }

    @Test
    fun `a-semillero tambien trae cambioDelOrganizador, para que M09 no dependa de una sola alarma de ejemplo`() {
        val repo = RepositorioDataset(json)
        val cambio = repo.dataset.alarmas.first { it.id == "a-semillero" }.cambioDelOrganizador!!
        assertEquals("2026-08-28T18:50:00-05:00", cambio.nuevoInicio)
        assertEquals("2026-08-28T18:30:00-05:00", cambio.nuevaHoraDeAlarma)
        assertEquals("2026-08-28T18:00:00-05:00", cambio.antesSonaba)
        assertEquals("Semillero UX", cambio.autor)
    }

    @Test
    fun `a-tutor (creada por mi) tambien trae cambioDelOrganizador y alSonar, no solo las escaneadas`() {
        val repo = RepositorioDataset(json)
        val tutor = repo.dataset.alarmas.first { it.id == "a-tutor" }
        assertEquals("creada-por-mi", tutor.origen)
        val cambio = tutor.cambioDelOrganizador!!
        assertEquals("2026-08-27T08:15:00-05:00", cambio.nuevoInicio)
        assertEquals("Prof. Andrea Ríos", cambio.autor)
        val alSonar = tutor.alSonar!!
        assertEquals(10, alSonar.salEnMin)
        assertEquals("2026-08-27T07:55:00-05:00", alSonar.llegaA)
        assertNull(repo.dataset.alarmas.first { it.id == "a-gimnasio" }.alSonar)   // sin lugar: no tiene sentido simularlo
    }

    @Test
    fun `aplicarCambioOrganizador actualiza eventoInicio y suena de la alarma`() {
        val repo = RepositorioDataset(json)
        repo.agregarDesdeEvento("e-entrega")
        repo.aplicarCambioOrganizador("a-entrega")
        val actualizada = repo.alarma("a-entrega")!!
        assertEquals("2026-08-30T17:30:00-05:00", actualizada.eventoInicio)
        assertEquals("2026-08-30T16:45:00-05:00", actualizada.suena)
    }

    @Test
    fun `aplicarCambioOrganizador no hace nada si la alarma no existe o no tiene cambio`() {
        val repo = RepositorioDataset(json)
        repo.aplicarCambioOrganizador("a-no-existe")   // no lanza
        val antes = repo.alarma("a-gimnasio")
        repo.aplicarCambioOrganizador("a-gimnasio")     // sin cambioDelOrganizador: no hace nada
        assertEquals(antes, repo.alarma("a-gimnasio"))
    }

    @Test
    fun `agregarEvento agrega un EventoQR nuevo, evento lo encuentra`() {
        val repo = RepositorioDataset(json)
        assertNull(repo.evento("e-manual-1"))
        repo.agregarEvento(EventoQR(id = "e-manual-1", alarmaId = "a-manual-1", titulo = "Prueba", codigoQR = "alarmasqr://evento/e-manual-1", escaneos = 0, etiqueta = "Aún sin escaneos · recién creado"))
        assertEquals("Prueba", repo.evento("e-manual-1")?.titulo)
    }

    @Test
    fun `crearAlarmaManual agrega la alarma y su EventoQR, origen creada-por-mi`() {
        val repo = RepositorioDataset(json)
        val alarma = repo.crearAlarmaManual("Cena de fin de año", "2026-08-28T19:00:00-05:00", "Casa de Andrés", null, 30)
        assertEquals("creada-por-mi", alarma.origen)
        assertEquals(listOf("Creada por mí"), alarma.chips)
        assertEquals("2026-08-28T18:30:00-05:00", alarma.suena)
        assertEquals(alarma.id, repo.alarma(alarma.id)?.id)
        val evento = repo.dataset.eventosQR.let { repo.evento("e-" + alarma.id.removePrefix("a-")) }
        assertEquals("Aún sin escaneos · recién creado", evento?.etiqueta)
    }

    @Test
    fun `reiniciar tambien limpia los eventosQR creados a mano`() {
        val repo = RepositorioDataset(json)
        val alarma = repo.crearAlarmaManual("Cena de fin de año", "2026-08-28T19:00:00-05:00", "Casa de Andrés", null, 30)
        val eventoId = "e-" + alarma.id.removePrefix("a-")
        assertEquals(alarma.id, repo.evento(eventoId)?.alarmaId)
        repo.reiniciar()
        assertNull(repo.evento(eventoId))
    }
}
