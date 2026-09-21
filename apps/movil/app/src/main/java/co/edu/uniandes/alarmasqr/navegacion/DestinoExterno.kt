package co.edu.uniandes.alarmasqr.navegacion

import android.content.Intent

/**
 * Entradas externas (spec §2.2): el pantallazo compartido (`ACTION_SEND` con MIME que empieza por `image/`) abre
 * M03b y la notificación de la alarma trae `alarmasqr://app/<ruta>` que resuelve `Pantalla.porRuta` (alarma/{id}/sonando → M10).
 */
fun destinoDesdeIntent(intent: Intent?): Pantalla? = when {
    intent == null -> null
    intent.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true -> Pantalla.M03b
    intent.data?.path != null -> Pantalla.porRuta(intent.data!!.path!!)
    else -> null
}
