package co.edu.uniandes.alarmasqr.datos

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class FormatoHoraTest {
    private val hoy = LocalDate.of(2026, 8, 27)
    private val tutor = Alarma(id = "a-tutor", titulo = "Reunión con el tutor", eventoInicio = "2026-08-27T08:00:00-05:00", suena = "2026-08-27T07:30:00-05:00", lugar = "Aula SD-703", origen = "creada-por-mi", estado = "activa", anticipacionMin = 30, trayectoMin = 0)

    @Test
    fun `hora en 12 h con sufijo en minusculas`() {
        assertEquals("7:30", FormatoHora.hora("2026-08-27T07:30:00-05:00"))
        assertEquals("am", FormatoHora.sufijo("2026-08-27T07:30:00-05:00"))
        assertEquals("3:15 pm", FormatoHora.horaConSufijo("2026-08-30T15:15:00-05:00"))
        assertEquals("12:00 pm", FormatoHora.horaConSufijo("2026-08-31T12:00:00-05:00"))
    }

    @Test
    fun `agrupadores de dia como en los mockups`() {
        assertEquals("HOY · JUEVES 27", FormatoHora.etiquetaDia("2026-08-27T08:00:00-05:00", hoy))
        assertEquals("MAÑANA · VIERNES 28", FormatoHora.etiquetaDia("2026-08-28T08:15:00-05:00", hoy))
        assertEquals("DOMINGO 30", FormatoHora.etiquetaDia("2026-08-30T16:00:00-05:00", hoy))
        assertEquals("LUNES 31", FormatoHora.etiquetaDia("2026-08-31T13:00:00-05:00", hoy))
    }

    @Test
    fun `fechas larga y corta de M04 y M04d`() {
        assertEquals("Dom 30 de agosto · 4:00 pm (GMT-5)", FormatoHora.fechaLarga("2026-08-30T16:00:00-05:00"))
        assertEquals("dom 30", FormatoHora.fechaCorta("2026-08-30T16:00:00-05:00"))
    }

    @Test
    fun `linea de evento segun estado, etiqueta y lugar`() {
        assertEquals("evento 8:00 am · Aula SD-703", FormatoHora.lineaEvento(tutor))
        assertEquals("pausada · evento 9:30 am", FormatoHora.lineaEvento(tutor.copy(titulo = "Gimnasio", eventoInicio = "2026-08-27T09:30:00-05:00", lugar = null, estado = "pausada")))
        assertEquals("vuelo 8:15 am · Aeropuerto", FormatoHora.lineaEvento(tutor.copy(eventoInicio = "2026-08-28T08:15:00-05:00", lugar = "Aeropuerto", etiquetaEvento = "vuelo")))
    }
}
