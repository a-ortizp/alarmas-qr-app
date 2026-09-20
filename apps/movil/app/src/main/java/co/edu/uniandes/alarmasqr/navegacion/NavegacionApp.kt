package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.pantallas.PantallaMarcador
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** Pila de navegación de la app, persistente ante recreaciones. */
@Composable
fun rememberBackStackApp(inicio: Pantalla = Pantalla.inicio): NavBackStack<NavKey> = rememberNavBackStack(inicio)

/** Apila una pantalla. */
fun NavBackStack<NavKey>.irA(p: Pantalla) { add(p) }

/** Sustituye toda la pila (entrar, registrarse, cerrar sesión, «Listo» tras M04). */
fun NavBackStack<NavKey>.reemplazarTodo(p: Pantalla) { clear(); add(p) }

/** Cambia de pestaña inferior: reemplaza la cima en vez de apilar. */
fun NavBackStack<NavKey>.irAPestana(p: Pantalla) { if (lastOrNull() != p) { removeLastOrNull(); add(p) } }

private val pestanas = listOf(
    Triple(Pantalla.M02, "Alarmas", Icons.Outlined.Alarm),
    Triple(Pantalla.M02b, "Calendario", Icons.Outlined.CalendarMonth),
    Triple(Pantalla.M11, "Ajustes", Icons.Outlined.Settings),
)

/**
 * Raíz de navegación (Navigation 3). Las pantallas reales se registran con [entradas]; el marcador
 * ([entradaMarcador]) es el `fallback` del `entryProvider`, así que solo se usa para las claves que [entradas] no
 * registró — las pantallas reales tienen prioridad por construcción, no por orden. Barra inferior y FAB los decide
 * la clave visible (`conNavegacionInferior`, `conFab`).
 *
 * Una pantalla real que sea hoja (M02h, M04) debe registrarse en [entradas] con el metadato de
 * [HojaInferiorSceneStrategy.hoja]: `entry<Pantalla.M04>(metadata = HojaInferiorSceneStrategy.hoja()) { … }`.
 * Sin ese metadato la entrada real se dibuja a pantalla completa en vez de como hoja modal sobre la anterior.
 */
@Composable
fun NavegacionApp(
    backStack: NavBackStack<NavKey>,
    repositorio: RepositorioDataset,
    entradas: EntryProviderScope<NavKey>.() -> Unit = {},
) {
    val estrategiaHoja = remember { HojaInferiorSceneStrategy<NavKey>() }
    val visible = backStack.lastOrNull { it is Pantalla && !it.esHoja } as? Pantalla ?: Pantalla.inicio
    val alVolver: () -> Unit = { backStack.removeLastOrNull() }

    Scaffold(
        containerColor = Colores.Blanco,
        bottomBar = {
            if (visible.conNavegacionInferior) {
                NavigationBar(containerColor = Colores.Blanco, contentColor = Colores.Tinta) {
                    pestanas.forEach { (pantalla, rotulo, icono) ->
                        val activa = visible.codigo == pantalla.codigo || (pantalla == Pantalla.M02 && visible.codigo in setOf("M02v", "M05"))
                        NavigationBarItem(
                            selected = activa,
                            onClick = { backStack.irAPestana(pantalla) },
                            icon = { Icon(icono, contentDescription = null) },
                            label = { Text(rotulo, style = Tipografia.NavegacionInferior) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Colores.Blanco, selectedTextColor = Colores.Tinta,
                                indicatorColor = Colores.Tinta, unselectedIconColor = Colores.GrisTexto, unselectedTextColor = Colores.GrisTexto,
                            ),
                            modifier = Modifier.testTag("nav-${pantalla.codigo}"),
                        )
                    }
                }
            }
        },
    ) { relleno ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(relleno),
            onBack = { backStack.removeLastOrNull() },
            sceneStrategies = listOf(estrategiaHoja),
            entryProvider = entryProvider(
                fallback = { clave -> entradaMarcador(clave, alVolver) },
            ) { entradas() },
        )
    }
}

/** Marcador para toda clave que [entradas] no haya registrado: así las pantallas reales tienen prioridad por construcción. */
private fun entradaMarcador(clave: NavKey, alVolver: () -> Unit): NavEntry<NavKey> {
    val pantalla = clave as? Pantalla ?: error("Clave desconocida: $clave")
    val meta = if (pantalla.esHoja) HojaInferiorSceneStrategy.hoja() else emptyMap()
    return NavEntry(key = clave, metadata = meta) { PantallaMarcador(pantalla, alVolver) }
}
