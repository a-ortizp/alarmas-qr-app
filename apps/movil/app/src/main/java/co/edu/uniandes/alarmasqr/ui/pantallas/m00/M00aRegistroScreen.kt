package co.edu.uniandes.alarmasqr.ui.pantallas.m00

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import co.edu.uniandes.alarmasqr.ui.componentes.Casilla
import co.edu.uniandes.alarmasqr.ui.componentes.Divisor
import co.edu.uniandes.alarmasqr.ui.componentes.Logotipo
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import androidx.compose.foundation.layout.PaddingValues

/** M00a · Crear cuenta (F-M00a): registro opcional; «Crear cuenta», Google, Outlook e invitado → M02v; pie → M00b. */
@Composable
fun M00aRegistroScreen(alCrearCuenta: () -> Unit, alInvitado: () -> Unit, alYaTengoCuenta: () -> Unit, modifier: Modifier = Modifier) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var consentimiento by rememberSaveable { mutableStateOf(false) }
    ColumnaDesplazable(
        modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M00a"),
        relleno = PaddingValues(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingAccesoSuperior, bottom = Espacio.PieEnlace),
        verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Logotipo()
        Text("Crea tu cuenta", style = Tipografia.H1, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text("Respalda tus alarmas en la nube y úsalas también en la web.", style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        CampoTexto(correo, { correo = it }, etiqueta = "Correo", pista = "tucorreo@ejemplo.com", modifier = Modifier.testTag("correo"))
        CampoTexto(contrasena, { contrasena = it }, etiqueta = "Contraseña", pista = "Mínimo 8 caracteres", contrasena = true, modifier = Modifier.testTag("contrasena"))
        Casilla(consentimiento, { consentimiento = it }, modifier = Modifier.fillMaxWidth().testTag("consentimiento")) {
            Text("Acepto el tratamiento de mis datos según la política de privacidad (Ley 1581 de 2012).", style = Tipografia.NotaLarga, color = Colores.GrisTexto)
        }
        BotonPrimario("Crear cuenta", onClick = alCrearCuenta, modifier = Modifier.testTag("crear-cuenta"))
        Divisor("o continúa con")
        BotonesTerceros(alGoogle = alCrearCuenta, alOutlook = alCrearCuenta)
        BotonEnlace("Continuar como invitado", onClick = alInvitado, modifier = Modifier.testTag("invitado"))
        Text("Como invitado, las alarmas quedan solo en este teléfono.", style = Tipografia.Nota, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.weight(1f))
        PieAcceso("¿Ya tienes cuenta? ", "Inicia sesión", onClick = alYaTengoCuenta)
    }
}
