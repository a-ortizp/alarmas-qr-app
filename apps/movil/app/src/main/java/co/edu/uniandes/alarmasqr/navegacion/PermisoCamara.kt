package co.edu.uniandes.alarmasqr.navegacion

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/** true si la app ya tiene el permiso real de cámara (`Manifest.permission.CAMERA`) concedido por el sistema. */
fun tienePermisoCamara(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

/**
 * «Abrir ajustes» de M12 (F-M12, decisión D5): pide el permiso real; si ya se pidió y el sistema no volverá a
 * preguntar, abre los ajustes de la app. En ambos casos, al volver se llama [alTerminar] (⏩ → M03), tenga o no
 * permiso: M03 muestra el visor apagado si sigue denegado.
 */
@Composable
fun rememberSolicitudPermisoCamara(alTerminar: () -> Unit): () -> Unit {
    val context = LocalContext.current
    var solicitado by rememberSaveable { mutableStateOf(false) }
    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { alTerminar() }
    val ajustes = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { alTerminar() }
    return {
        val actividad = context as? Activity
        when {
            tienePermisoCamara(context) -> alTerminar()
            solicitado && actividad != null && !ActivityCompat.shouldShowRequestPermissionRationale(actividad, Manifest.permission.CAMERA) ->
                ajustes.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null)))
            else -> { solicitado = true; permiso.launch(Manifest.permission.CAMERA) }
        }
    }
}
