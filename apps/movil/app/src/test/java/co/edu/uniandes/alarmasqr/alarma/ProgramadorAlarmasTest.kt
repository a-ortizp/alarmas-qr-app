package co.edu.uniandes.alarmasqr.alarma

import android.app.AlarmManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.Alarma
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.OffsetDateTime

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ProgramadorAlarmasTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val am = context.getSystemService(AlarmManager::class.java)
    private val entrega = Alarma(id = "a-entrega", titulo = "Entrega de proyecto UX", eventoInicio = "2026-08-30T16:00:00-05:00", suena = "2026-08-30T15:15:00-05:00", lugar = "Aula SD-703, Universidad", origen = "escaneada", estado = "activa", anticipacionMin = 30, trayectoMin = 15)

    @Test
    fun `programa a la hora de suena cuando esta en el futuro`() {
        val ahora = OffsetDateTime.parse("2026-08-30T10:00:00-05:00").toInstant().toEpochMilli()
        val cuando = ProgramadorAlarmas(context) { ahora }.programar(entrega)
        assertEquals(OffsetDateTime.parse("2026-08-30T15:15:00-05:00").toInstant().toEpochMilli(), cuando)
        val programada = shadowOf(am).scheduledAlarms.single()
        assertEquals(cuando, programada.triggerAtMs)
        assertEquals(AlarmManager.RTC_WAKEUP, programada.type)
    }

    @Test
    fun `si la hora ya paso programa un minuto despues de ahora (demo D4)`() {
        val ahora = OffsetDateTime.parse("2026-09-20T14:00:00-05:00").toInstant().toEpochMilli()
        val cuando = ProgramadorAlarmas(context) { ahora }.programar(entrega)
        assertEquals(ahora + ProgramadorAlarmas.DEMORA_DEMO_MS, cuando)
    }

    @Test
    fun `cancelar retira la alarma programada`() {
        val programador = ProgramadorAlarmas(context)
        programador.programar(entrega)
        assertEquals(1, shadowOf(am).scheduledAlarms.size)
        programador.cancelar("a-entrega")
        assertTrue(shadowOf(am).scheduledAlarms.isEmpty())
    }
}
