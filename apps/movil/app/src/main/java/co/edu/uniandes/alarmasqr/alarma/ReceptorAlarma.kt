package co.edu.uniandes.alarmasqr.alarma

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Recibe el disparo de AlarmManager y publica la notificación de pantalla completa que abre M10. */
class ReceptorAlarma : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra(EXTRA_ID) ?: return
        NotificacionesAlarma.publicar(context, id, intent.getStringExtra(EXTRA_TITULO) ?: "", intent.getStringExtra(EXTRA_LUGAR))
    }

    companion object {
        const val EXTRA_ID = "alarmaId"
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_LUGAR = "lugar"
    }
}
