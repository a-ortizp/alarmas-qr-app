package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.componentes.FabEscanear
import co.edu.uniandes.alarmasqr.ui.componentes.NavegacionInferior
import co.edu.uniandes.alarmasqr.ui.componentes.SnackbarDeshacer
import co.edu.uniandes.alarmasqr.ui.pantallas.PantallaMarcador
import co.edu.uniandes.alarmasqr.ui.theme.Colores

/** Pila de navegación de la app, persistente ante recreaciones. */
@Composable
fun rememberBackStackApp(inicio: Pantalla = Pantalla.inicio): NavBackStack<NavKey> = rememberNavBackStack(inicio)

/** Apila una pantalla. */
fun NavBackStack<NavKey>.irA(p: Pantalla) { add(p) }

/** Sustituye toda la pila (entrar, registrarse, cerrar sesión, «Listo» tras M04). */
fun NavBackStack<NavKey>.reemplazarTodo(p: Pantalla) { clear(); add(p) }

/** Cambia de pestaña inferior: reemplaza la cima en vez de apilar. */
fun NavBackStack<NavKey>.irAPestana(p: Pantalla) { if (lastOrNull() != p) { removeLastOrNull(); add(p) } }

/** Sustituye la cima (M04 «Listo» → M05, M12 «Abrir ajustes» → M03). */
fun NavBackStack<NavKey>.reemplazarCima(p: Pantalla) { removeLastOrNull(); add(p) }

/**
 * Snackbar único de la app, en el Scaffold raíz para que el FAB suba con él (M05). Las pantallas lo usan con
 * `LocalSnackbarApp.current.showSnackbar(...)`; `SnackbarDeshacer` le da la forma del DS comp. 26.
 */
val LocalSnackbarApp: ProvidableCompositionLocal<SnackbarHostState> = compositionLocalOf { error("Fuera de NavegacionApp") }

/**
 * Raíz de navegación (Navigation 3). Las pantallas reales se registran con [entradas]; el marcador ([entradaMarcador])
 * es el `fallback` del `entryProvider`, así que solo se usa para las claves que [entradas] no registró. Barra inferior
 * y FAB los decide la clave visible (`conNavegacionInferior`, `conFab`). FAB: toque → M12 la primera vez (⏩) y
 * después M03; mantener 500 ms → hoja M02h. Una hoja real (M02h, M04) se registra con
 * `entry<Pantalla.M04>(metadata = HojaInferiorSceneStrategy.hoja()) { … }`.
 */
@Composable
fun NavegacionApp(
    backStack: NavBackStack<NavKey>,
    repositorio: RepositorioDataset,
    entradas: EntryProviderScope<NavKey>.() -> Unit = {},
) {
    val estrategiaHoja = remember { HojaInferiorSceneStrategy<NavKey>() }
    val snackbar = remember { SnackbarHostState() }
    val visible = backStack.lastOrNull { it is Pantalla && !it.esHoja } as? Pantalla ?: Pantalla.inicio
    val alVolver: () -> Unit = { backStack.removeLastOrNull() }

    CompositionLocalProvider(LocalSnackbarApp provides snackbar) {
        Scaffold(
            containerColor = Colores.Blanco,
            bottomBar = { if (visible.conNavegacionInferior) NavegacionInferior(activa = visible, alCambiar = { backStack.irAPestana(it) }) },
            floatingActionButton = {
                if (visible.conFab) {
                    FabEscanear(
                        alTocar = {
                            if (repositorio.permisoCamaraPedido) backStack.irA(Pantalla.M03)
                            else { repositorio.permisoCamaraPedido = true; backStack.irA(Pantalla.M12) }
                        },
                        alMantener = { backStack.irA(Pantalla.M02h) },
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbar) { datos -> SnackbarDeshacer(datos) } },
        ) { relleno ->
            NavDisplay(
                backStack = backStack,
                // consumeWindowInsets: el imePadding() de ColumnaDesplazable no vuelve a sumar la barra de navegación.
                modifier = Modifier.padding(relleno).consumeWindowInsets(relleno),
                onBack = { backStack.removeLastOrNull() },
                sceneStrategies = listOf(estrategiaHoja),
                entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator(), rememberViewModelStoreNavEntryDecorator()),
                entryProvider = entryProvider(fallback = { clave -> entradaMarcador(clave, alVolver) }) { entradas() },
            )
        }
    }
}

/** Marcador para toda clave que [entradas] no haya registrado: así las pantallas reales tienen prioridad por construcción. */
private fun entradaMarcador(clave: NavKey, alVolver: () -> Unit): NavEntry<NavKey> {
    val pantalla = clave as? Pantalla ?: error("Clave desconocida: $clave")
    val meta = if (pantalla.esHoja) HojaInferiorSceneStrategy.hoja() else emptyMap()
    return NavEntry(key = clave, metadata = meta) { PantallaMarcador(pantalla, alVolver) }
}
