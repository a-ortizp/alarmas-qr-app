package co.edu.uniandes.alarmasqr.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** Marcador de la Fase 0: ocupa el lugar de una pantalla hasta que su plan la construya. */
@Composable
fun PantallaMarcador(pantalla: Pantalla, alVolver: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(Espacio.Margen).testTag("pantalla-${pantalla.codigo}"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(pantalla.codigo, style = Tipografia.H1, color = Colores.Tinta)
        Text(pantalla.titulo, style = Tipografia.Cuerpo, color = Colores.GrisTexto)
        Text("Pantalla pendiente · ${pantalla.funcionalidad}", style = Tipografia.Nota, color = Colores.GrisTexto)
        if (!pantalla.esRaiz) {
            TextButton(onClick = alVolver, modifier = Modifier.testTag("volver")) { Text("‹ Volver", style = Tipografia.Enlace, color = Colores.Tinta) }
        }
    }
}
