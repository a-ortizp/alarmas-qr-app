package co.edu.uniandes.alarmasqr.qr

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Visor real de M03: `PreviewView` con `LifecycleCameraController` y `MlKitAnalyzer` (skill camerax: usar el
 * analizador de ML Kit en vez de un `ImageAnalysis.Analyzer` manual). Solo QR; [alLeer] recibe el contenido crudo
 * cada vez que ML Kit lo detecta (el ViewModel descarta repeticiones). [linterna] enciende la lámpara.
 */
@Composable
fun VisorCamara(linterna: Boolean, alLeer: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val propietario = LocalLifecycleOwner.current
    val controlador = remember { LifecycleCameraController(context) }
    val escaner = remember { BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()) }

    DisposableEffect(controlador, propietario) {
        val ejecutor = ContextCompat.getMainExecutor(context)
        controlador.setImageAnalysisAnalyzer(
            ejecutor,
            MlKitAnalyzer(listOf(escaner), ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED, ejecutor) { resultado ->
                resultado?.getValue(escaner)?.firstOrNull()?.rawValue?.let(alLeer)
            },
        )
        controlador.bindToLifecycle(propietario)
        onDispose { controlador.unbind(); escaner.close() }
    }
    LaunchedEffect(linterna) { controlador.enableTorch(linterna) }

    AndroidView(
        factory = { PreviewView(it).apply { controller = controlador; scaleType = PreviewView.ScaleType.FILL_CENTER } },
        modifier = modifier,
    )
}
