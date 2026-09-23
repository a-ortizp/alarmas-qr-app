package co.edu.uniandes.alarmasqr.navegacion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import co.edu.uniandes.alarmasqr.alarma.ProgramadorAlarmas
import co.edu.uniandes.alarmasqr.alarma.rememberSolicitudPermisoNotificaciones
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.navegacion.LocalSnackbarApp
import co.edu.uniandes.alarmasqr.qr.ResultadoQR
import co.edu.uniandes.alarmasqr.ui.pantallas.m00.M00aRegistroScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m00.M00bEntrarScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m01.M01BienvenidaScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02InicioScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02InicioViewModel
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioViewModel
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02hAgregarEventoSheet
import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02vEstadoVacio
import co.edu.uniandes.alarmasqr.ui.pantallas.m03.M03EscanerScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m03.M03EscanerViewModel
import co.edu.uniandes.alarmasqr.ui.pantallas.m03.M03bPantallazoScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m04.M04AlarmaCreadaSheet
import co.edu.uniandes.alarmasqr.ui.pantallas.m04.M04AlarmaCreadaViewModel
import co.edu.uniandes.alarmasqr.ui.pantallas.m06.M06EditarAlarmaScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m06.M06EditarAlarmaViewModel
import co.edu.uniandes.alarmasqr.ui.pantallas.m12.M12PermisoCamaraScreen
import co.edu.uniandes.alarmasqr.ui.pantallas.m13.M13QRInvalidoScreen
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Registro de las pantallas reales de la Persona A (Plan 2). Lo comparten MainActivity y las pruebas de flujo, así
 * las pruebas navegan por las mismas entradas que el APK. Las claves que no aparecen aquí caen en el marcador.
 * Cada tarea del plan agrega su `entry<…>`; la Persona B agrega las suyas en el mismo archivo.
 */
fun EntryProviderScope<NavKey>.entradasApp(pila: NavBackStack<NavKey>, repositorio: RepositorioDataset) {
    entry<Pantalla.M01> {
        M01BienvenidaScreen(alComenzar = { pila.irA(Pantalla.M00a) }, alConectarLuego = { pila.reemplazarTodo(Pantalla.M02v) })
    }
    entry<Pantalla.M00a> {
        M00aRegistroScreen(
            alCrearCuenta = { pila.reemplazarTodo(Pantalla.M02v) },
            alInvitado = { pila.reemplazarTodo(Pantalla.M02v) },
            alYaTengoCuenta = { pila.reemplazarCima(Pantalla.M00b) },
        )
    }
    entry<Pantalla.M00b> {
        M00bEntrarScreen(
            correoInicial = repositorio.dataset.usuario.correo,
            alEntrar = { pila.reemplazarTodo(Pantalla.M02) },
            alInvitado = { pila.reemplazarTodo(Pantalla.M02v) },
            alCrearCuenta = { pila.reemplazarCima(Pantalla.M00a) },
            alRecuperar = { /* sin pantalla en móvil: la recuperación vive en la web (F-W00) */ },
        )
    }
    entry<Pantalla.M02v> {
        M02vEstadoVacio(
            mensajes = repositorio.dataset.mensajes,
            alEscanear = { repositorio.permisoCamaraPedido = true; pila.irA(Pantalla.M12) },
            alElegirPantallazo = { pila.irA(Pantalla.M03b) },
            alCrearAMano = { pila.irA(Pantalla.M07) },
        )
    }
    entry<Pantalla.M02> {
        val vm = viewModel { M02InicioViewModel(repositorio) }
        val estado by vm.estado.collectAsStateWithLifecycle()
        M02InicioScreen(estado, alTocarAlarma = { pila.irA(Pantalla.M06(it)) }, alCambiarActiva = vm::cambiarActiva)
    }
    entry<Pantalla.M02h>(metadata = HojaInferiorSceneStrategy.hoja()) {
        M02hAgregarEventoSheet(
            alEscanear = { if (repositorio.permisoCamaraPedido) pila.reemplazarCima(Pantalla.M03) else { repositorio.permisoCamaraPedido = true; pila.reemplazarCima(Pantalla.M12) } },
            alElegirPantallazo = { pila.reemplazarCima(Pantalla.M03b) },
            alCrearAMano = { pila.reemplazarCima(Pantalla.M07) },
        )
    }
    entry<Pantalla.M02b> {
        val vm = viewModel { M02bCalendarioViewModel(repositorio) }
        val estado by vm.estado.collectAsStateWithLifecycle()
        M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = { pila.irA(Pantalla.M06(it)) }, alCambiarActiva = vm::cambiarActiva)
    }
    entry<Pantalla.M12> {
        val abrirAjustes = rememberSolicitudPermisoCamara(alTerminar = { pila.reemplazarCima(Pantalla.M03) })
        M12PermisoCamaraScreen(
            alVolver = { pila.removeLastOrNull() },
            alAbrirAjustes = abrirAjustes,
            alElegirPantallazo = { pila.reemplazarCima(Pantalla.M03b) },
            alCrearAMano = { pila.reemplazarCima(Pantalla.M07) },
        )
    }
    entry<Pantalla.M03> {
        val context = LocalContext.current
        val vm = viewModel { M03EscanerViewModel(repositorio) }
        val estado by vm.estado.collectAsStateWithLifecycle()
        val tienePermiso = remember { tienePermisoCamara(context) }
        LaunchedEffect(estado.resultado) {
            when (val r = estado.resultado) {
                is ResultadoQR.EventoDetectado -> {
                    vibrar(context)
                    val alarma = repositorio.agregarDesdeEvento(r.eventoId)
                    vm.consumirResultado()
                    if (alarma != null) pila.reemplazarCima(Pantalla.M04(alarma.id))
                }
                is ResultadoQR.QRInvalido -> { vm.consumirResultado(); pila.irA(Pantalla.M13) }
                null -> Unit
            }
        }
        M03EscanerScreen(
            estado = estado, tienePermiso = tienePermiso,
            alVolver = { pila.removeLastOrNull() }, alAlternarLinterna = vm::alternarLinterna, alLeer = vm::leer,
            alTocarVisor = vm::simularEventoValido, alTocarVibra = vm::simularInvalido,
            alElegirPantallazo = { pila.reemplazarCima(Pantalla.M03b) }, alCrearAMano = { pila.reemplazarCima(Pantalla.M07) },
        )
    }
    entry<Pantalla.M03b> {
        val datos = repositorio.dataset.pantallazoRecibido
        val elegirImagen = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { /* maquetación: la imagen elegida no se lee */ }
        M03bPantallazoScreen(
            datos = datos, evento = repositorio.evento(datos.eventoDetectado)!!,
            alVolver = { pila.removeLastOrNull() },
            alContinuar = { repositorio.agregarDesdeEvento(datos.eventoDetectado)?.let { pila.reemplazarCima(Pantalla.M04(it.id)) } },
            alElegirOtra = { elegirImagen.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        )
    }
    entry<Pantalla.M04>(metadata = HojaInferiorSceneStrategy.hoja()) { clave ->
        // Alcanzable tras la muerte del proceso con la hoja abierta (la pila restaurada trae M04("a-entrega") pero
        // el repositorio fresco esconde las alarmas esNueva) o vía un intent externo (MainActivity es exportada):
        // sin esta guarda, `M04AlarmaCreadaViewModel.init` hacía `repositorio.alarma(id) ?: error(...)` y estrellaba
        // la app. Si la alarma no existe, la hoja se saca sola de la pila y no se crea el ViewModel.
        if (repositorio.alarma(clave.id) == null) {
            LaunchedEffect(clave) { pila.removeLastOrNull() }
        } else {
            val context = LocalContext.current
            val programador = remember { ProgramadorAlarmas(context) }
            val vm = viewModel(key = clave.id) { M04AlarmaCreadaViewModel(repositorio, programador, clave.id) }
            val estado by vm.estado.collectAsStateWithLifecycle()
            val pedirNotificaciones = rememberSolicitudPermisoNotificaciones()
            LaunchedEffect(Unit) { pedirNotificaciones() }
            M04AlarmaCreadaSheet(
                estado = estado, mensajes = repositorio.dataset.mensajes,
                alListo = { pila.reemplazarTodo(Pantalla.M05(clave.id)) },
                alEditar = { pila.reemplazarCima(Pantalla.M06(clave.id)) },
                alAbrirDialogo = vm::abrirDialogo,
                alConservar = vm::cerrarDialogo,
                alEliminar = { vm.eliminar(); pila.reemplazarTodo(Pantalla.M02) },
            )
        }
    }
    entry<Pantalla.M05> { clave ->
        val context = LocalContext.current
        // Programador real: «Deshacer» debe cancelar también la alarma de AlarmManager que M04 dejó armada, no
        // solo revertir la lista (si no, la alarma «eliminada» seguiría sonando ~1 min después).
        val vm = viewModel(key = clave.id) { M02InicioViewModel(repositorio, alarmaNueva = clave.id, programador = ProgramadorAlarmas(context)) }
        val estado by vm.estado.collectAsStateWithLifecycle()
        val snackbar = LocalSnackbarApp.current
        val mensajes = repositorio.dataset.mensajes
        // Ventana de «Deshacer» de una sola vez por alarma guardada: sin esto, volver a M05 (p. ej. desde M06) vuelve
        // a disparar el efecto y muestra el snackbar de nuevo, dejando deshacer mucho después de los 5 s reales.
        var mostrado by rememberSaveable(clave.id) { mutableStateOf(false) }
        LaunchedEffect(clave.id) {
            if (mostrado) return@LaunchedEffect
            mostrado = true
            var deshecho = false
            // finally: si el usuario sale de M05 antes de los 5 s, esta corrutina se cancela (CancellationException)
            // sin pasar por la rama de abajo; sin el finally, `anterior` del repositorio quedaba con la mutación
            // pendiente y un «Deshacer» disparado mucho después (p. ej. tras editar en M06 y volver) la revertía.
            try {
                // F-M05: «Deshacer (5 s)». Ventana fija de Movimiento.DeshacerMs; al vencer, el snackbar se retira solo.
                val resultado = withTimeoutOrNull(Movimiento.DeshacerMs) {
                    snackbar.showSnackbar(message = mensajes.alarmaGuardada, actionLabel = mensajes.deshacer, duration = SnackbarDuration.Indefinite)
                }
                if (resultado == SnackbarResult.ActionPerformed) { deshecho = true; vm.deshacer(); pila.reemplazarTodo(Pantalla.M02) }
            } finally {
                if (!deshecho) vm.olvidarDeshacer()
            }
        }
        M02InicioScreen(estado, alTocarAlarma = { pila.irA(Pantalla.M06(it)) }, alCambiarActiva = vm::cambiarActiva, codigo = "M05")
    }
    entry<Pantalla.M06> { clave ->
        if (repositorio.alarma(clave.id) == null) {
            LaunchedEffect(clave) { pila.removeLastOrNull() }
        } else {
            val vm = viewModel(key = clave.id) { M06EditarAlarmaViewModel(repositorio, clave.id) }
            val estado by vm.estado.collectAsStateWithLifecycle()
            M06EditarAlarmaScreen(
                estado = estado, mensajes = repositorio.dataset.mensajes, mensajeEliminar = vm.mensajeEliminar, puedeVerCambioOrganizador = vm.puedeVerCambioOrganizador,
                alVolver = { pila.removeLastOrNull() },
                alElegirAnticipacion = vm::elegirAnticipacion, alElegirSonido = vm::elegirSonido, alCambiarConfirmar = vm::cambiarConfirmar,
                alTocarCambioOrganizador = { pila.irA(Pantalla.M09(clave.id)) },
                alGestionarCalendario = { pila.irA(Pantalla.M02b) },
                alGuardar = { vm.guardar(); pila.reemplazarTodo(Pantalla.M02) },
                alAbrirDialogo = vm::abrirDialogo, alConservar = vm::cerrarDialogo,
                alEliminar = { vm.eliminar(); pila.reemplazarTodo(Pantalla.M02) },
            )
        }
    }
    entry<Pantalla.M13> {
        val context = LocalContext.current
        M13QRInvalidoScreen(
            diagnostico = repositorio.dataset.qrInvalido,
            alVolver = { pila.removeLastOrNull() },
            alVolverAEscanear = { pila.removeLastOrNull() },      // vuelve a M03 sin pasar por M12 (NAVEGACION §6 a)
            alCrearAMano = { pila.reemplazarCima(Pantalla.M07) },
            alAbrirEnlace = {
                runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repositorio.dataset.qrInvalido.contenido))) }
            },
        )
    }
}

/** «vibra al detectar el código» (F-M03). */
private fun vibrar(context: Context) {
    val vibrador = context.getSystemService(Vibrator::class.java) ?: return
    vibrador.vibrate(VibrationEffect.createOneShot(Movimiento.VibracionMs, VibrationEffect.DEFAULT_AMPLITUDE))
}
