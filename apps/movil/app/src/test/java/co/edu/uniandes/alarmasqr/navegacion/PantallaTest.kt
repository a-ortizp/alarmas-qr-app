package co.edu.uniandes.alarmasqr.navegacion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class PantallaTest {
    @Test
    fun `las 19 pantallas conservan codigo y ruta de TRAZABILIDAD`() {
        assertEquals(19, Pantalla.todas.size)
        assertEquals("bienvenida", Pantalla.M01.ruta)
        assertEquals("alarma/a-tutor/creada", Pantalla.M04("a-tutor").ruta)
        assertEquals("F-M04", Pantalla.M04("a-tutor").funcionalidad)
        assertEquals(Pantalla.M01, Pantalla.inicio)
        assertFalse(Pantalla.M03.esRaiz)
    }

    @Test
    fun `porRuta resuelve rutas fijas y con id`() {
        assertEquals(Pantalla.M03, Pantalla.porRuta("escanear"))
        assertEquals(Pantalla.M13, Pantalla.porRuta("escanear/invalido"))
        assertEquals(Pantalla.M10("a-tutor"), Pantalla.porRuta("alarma/a-tutor/sonando"))
        assertEquals(Pantalla.M06("a-x"), Pantalla.porRuta("alarma/a-x"))
        assertNull(Pantalla.porRuta("no-existe"))
        assertEquals(Pantalla.M10("a-tutor"), Pantalla.porRuta("/alarma/a-tutor/sonando"))
        assertEquals(Pantalla.M01, Pantalla.porRuta("/bienvenida"))
    }

    @Test
    fun `solo M02v, M02, M02b, M05 y M11 llevan barra inferior`() {
        val conBarra = Pantalla.todas.filter { it.conNavegacionInferior }.map { it.codigo }.toSet()
        assertEquals(setOf("M02v", "M02", "M02b", "M05", "M11"), conBarra)
    }
}
