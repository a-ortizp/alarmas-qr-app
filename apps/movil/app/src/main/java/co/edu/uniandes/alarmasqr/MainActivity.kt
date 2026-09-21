package co.edu.uniandes.alarmasqr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.navegacion.NavegacionApp
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.navegacion.destinoDesdeIntent
import co.edu.uniandes.alarmasqr.navegacion.entradasApp
import co.edu.uniandes.alarmasqr.navegacion.irA
import co.edu.uniandes.alarmasqr.navegacion.reemplazarTodo
import co.edu.uniandes.alarmasqr.navegacion.rememberBackStackApp
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme

class MainActivity : ComponentActivity() {
    /** Pantalla pedida desde fuera (pantallazo compartido, notificación de la alarma); se consume una vez. */
    private val destinoPendiente = mutableStateOf<Pantalla?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repositorio = RepositorioDataset.desdeAssets(this)
        if (savedInstanceState == null) destinoPendiente.value = destinoDesdeIntent(intent)
        setContent {
            AlarmasQRTheme {
                val pila = rememberBackStackApp()
                LaunchedEffect(destinoPendiente.value) {
                    destinoPendiente.value?.let { destino ->
                        pila.reemplazarTodo(Pantalla.M02)   // pila sintética: Inicio debajo del destino (Nav 3, deep links)
                        pila.irA(destino)
                        destinoPendiente.value = null
                    }
                }
                NavegacionApp(backStack = pila, repositorio = repositorio) { entradasApp(pila, repositorio) }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        destinoPendiente.value = destinoDesdeIntent(intent)
    }
}
