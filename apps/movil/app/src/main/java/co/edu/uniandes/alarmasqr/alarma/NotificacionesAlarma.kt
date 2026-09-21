package co.edu.uniandes.alarmasqr.alarma

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import co.edu.uniandes.alarmasqr.MainActivity
import co.edu.uniandes.alarmasqr.navegacion.Pantalla

/** Canal «Alarmas» de importancia alta con sonido de alarma; la notificación es de pantalla completa (spec §2.1). */
object NotificacionesAlarma {
    const val CANAL = "alarmas"
    private const val ESQUEMA = "alarmasqr://app/"

    fun crearCanal(context: Context) {
        val canal = NotificationChannel(CANAL, "Alarmas", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Aviso de cada evento escaneado; suena aunque la app esté cerrada"
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
            enableVibration(true)
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    /** Deep link que MainActivity resuelve con `destinoDesdeIntent` → `Pantalla.porRuta` → M10. */
    fun intentSonando(context: Context, id: String): Intent =
        Intent(Intent.ACTION_VIEW, Uri.parse(ESQUEMA + Pantalla.M10(id).ruta), context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    fun publicar(context: Context, id: String, titulo: String, lugar: String?) {
        if (!tienePermiso(context)) return
        val pendiente = PendingIntent.getActivity(context, id.hashCode(), intentSonando(context, id), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(titulo)
            .setContentText(lugar?.let { "Es hora de salir · $it" } ?: "Es hora de salir")
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendiente)
            .setFullScreenIntent(pendiente, true)
            .setAutoCancel(true)
            .build()
        // Patrón que Lint (MissingPermission) reconoce: el checkSelfPermission debe quedar justo antes de notify().
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        NotificationManagerCompat.from(context).notify(id.hashCode(), notificacion)
    }
}

/** Pide `POST_NOTIFICATIONS` (Android 13+) la primera vez que se programa una alarma (M04). */
@Composable
fun rememberSolicitudPermisoNotificaciones(): () -> Unit {
    val context = LocalContext.current
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    return {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !NotificacionesAlarma.tienePermiso(context)) {
            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
