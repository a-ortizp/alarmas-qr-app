package co.edu.uniandes.alarmasqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.navegacion.NavegacionApp
import co.edu.uniandes.alarmasqr.navegacion.entradasApp
import co.edu.uniandes.alarmasqr.navegacion.rememberBackStackApp
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repositorio = RepositorioDataset.desdeAssets(this)
        setContent {
            AlarmasQRTheme {
                val pila = rememberBackStackApp()
                NavegacionApp(backStack = pila, repositorio = repositorio) { entradasApp(pila, repositorio) }
            }
        }
    }
}
