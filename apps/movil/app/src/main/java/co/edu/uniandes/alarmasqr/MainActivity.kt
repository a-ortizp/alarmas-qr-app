package co.edu.uniandes.alarmasqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme

/**
 * Esqueleto de la fase 0. Las pantallas (docs/TRAZABILIDAD.md) y el grafo de Navigation Compose
 * se construyen en las fases 1 y 2 según el reparto de docs/PLAN_MAQUETACION.md §3.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmasQRTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
                    }
                }
            }
        }
    }
}
