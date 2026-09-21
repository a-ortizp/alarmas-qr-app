package co.edu.uniandes.alarmasqr.ui.pantallas.m00

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.CampoTexto
import co.edu.uniandes.alarmasqr.ui.componentes.Divisor
import co.edu.uniandes.alarmasqr.ui.componentes.Logotipo
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M00b · Iniciar sesión (F-M00b): «Entrar», Google y Outlook → M02; invitado → M02v; pie → M00a. */
@Composable
fun M00bEntrarScreen(correoInicial: String, alEntrar: () -> Unit, alInvitado: () -> Unit, alCrearCuenta: () -> Unit, alRecuperar: () -> Unit, modifier: Modifier = Modifier) {
    var correo by rememberSaveable { mutableStateOf(correoInicial) }
    var contrasena by rememberSaveable { mutableStateOf("") }
    Column(
        modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M00b")
            .padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingAccesoSuperior, bottom = Espacio.PieEnlace),
        verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Logotipo()
        Text("Hola de nuevo", style = Tipografia.H1, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text("Entra para sincronizar tus alarmas con la nube y la web.", style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        CampoTexto(correo, { correo = it }, etiqueta = "Correo", pista = "tucorreo@ejemplo.com", modifier = Modifier.testTag("correo"))
        CampoTexto(contrasena, { contrasena = it }, etiqueta = "Contraseña", pista = "••••••••", contrasena = true, modifier = Modifier.testTag("contrasena"))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            BotonEnlace("¿Olvidaste tu contraseña?", onClick = alRecuperar, modifier = Modifier.testTag("recuperar"))
        }
        BotonPrimario("Entrar", onClick = alEntrar, modifier = Modifier.testTag("entrar"))
        Divisor("o continúa con")
        BotonesTerceros(alGoogle = alEntrar, alOutlook = alEntrar)
        BotonEnlace("Continuar como invitado", onClick = alInvitado, modifier = Modifier.testTag("invitado"))
        Spacer(Modifier.weight(1f))
        PieAcceso("¿Primera vez aquí? ", "Crea tu cuenta", onClick = alCrearCuenta)
    }
}
