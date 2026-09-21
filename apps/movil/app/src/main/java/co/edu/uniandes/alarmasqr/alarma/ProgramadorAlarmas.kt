package co.edu.uniandes.alarmasqr.alarma

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import co.edu.uniandes.alarmasqr.datos.Alarma
import java.time.OffsetDateTime

/**
 * F-M10 «la alarma debe sonar con la app cerrada»: `setExactAndAllowWhileIdle` (RTC_WAKEUP) con el permiso
 * `SCHEDULE_EXACT_ALARM`/`USE_EXACT_ALARM` del manifiesto; si el usuario lo revocó (Android 12+), cae a
 * `setAndAllowWhileIdle`. Decisión D4: el dataset vive en agosto de 2026, así que una hora ya pasada se programa
 * a ahora + 1 min para poder demostrar el disparo real.
 */
class ProgramadorAlarmas(private val context: Context, private val ahora: () -> Long = System::currentTimeMillis) : Programador {
    private val gestor: AlarmManager get() = context.getSystemService(AlarmManager::class.java)

    override fun programar(alarma: Alarma): Long {
        val objetivo = OffsetDateTime.parse(alarma.suena).toInstant().toEpochMilli()
        val cuando = if (objetivo > ahora()) objetivo else ahora() + DEMORA_DEMO_MS
        val pendiente = pendiente(alarma.id, alarma.titulo, alarma.lugar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !gestor.canScheduleExactAlarms()) {
            gestor.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuando, pendiente)
        } else {
            gestor.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuando, pendiente)
        }
        return cuando
    }

    override fun cancelar(id: String) = gestor.cancel(pendiente(id, "", null))

    private fun pendiente(id: String, titulo: String, lugar: String?): PendingIntent = PendingIntent.getBroadcast(
        context, id.hashCode(),
        Intent(context, ReceptorAlarma::class.java).putExtra(ReceptorAlarma.EXTRA_ID, id).putExtra(ReceptorAlarma.EXTRA_TITULO, titulo).putExtra(ReceptorAlarma.EXTRA_LUGAR, lugar),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    companion object { const val DEMORA_DEMO_MS = 60_000L }
}
