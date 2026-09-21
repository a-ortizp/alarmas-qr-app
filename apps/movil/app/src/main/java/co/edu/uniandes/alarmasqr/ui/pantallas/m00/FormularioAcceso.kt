package co.edu.uniandes.alarmasqr.ui.pantallas.m00

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** Fila «Google» / «Outlook»: dos secundarios de 170×52 con gap 10; el contenedor ajusta al contenido (tutores v1.7). */
@Composable
fun BotonesTerceros(alGoogle: () -> Unit, alOutlook: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        BotonSecundario("Google", onClick = alGoogle, modifier = Modifier.weight(1f).testTag("google"))
        BotonSecundario("Outlook", onClick = alOutlook, modifier = Modifier.weight(1f).testTag("outlook"))
    }
}

/**
 * Enlace de pie de pantalla (DS v1.6): marco de control de 44 a ancho completo con 32 de relleno inferior en la
 * pantalla; «¿Ya tienes cuenta? » SemiBold 13 Tinta + «Inicia sesión» Bold 13 Azul Texto, sin subrayado.
 */
@Composable
fun PieAcceso(prefijo: String, enlace: String, onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(Medidas.BotonAtras).clickable(role = Role.Button, onClick = onClick).testTag("pie-acceso"), contentAlignment = Alignment.Center) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = Colores.Tinta)) { append(prefijo) }
                withStyle(SpanStyle(color = Colores.AzulTexto, fontWeight = FontWeight.Bold)) { append(enlace) }
            },
            style = Tipografia.PieAcceso, textAlign = TextAlign.Center,
        )
    }
}
