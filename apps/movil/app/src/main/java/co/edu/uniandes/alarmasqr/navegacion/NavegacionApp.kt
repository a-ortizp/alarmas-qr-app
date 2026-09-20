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

/** Id de ejemplo para las claves parametrizadas al registrar marcadores (no se usa para resolver datos reales). */
private const val ID_MARCADOR = "{id}"

/**
 * Copia de [Pantalla.todas] para registrar los marcadores. No se reutiliza `Pantalla.todas` directamente: bajo
 * Robolectric, recorrer esa lista (obtenida vía el getter del companion) dentro del `entryProvider` produce un
 * `NullPointerException` espurio en cada rama del `when` de [registrarMarcador] — el mismo recorrido con una lista
 * construida en este módulo no falla. Mismo contenido, sin pasar por el getter del companion en ese punto.
 */
private val plantillasMarcador: List<Pantalla> = listOf(
    Pantalla.M01, Pantalla.M00a, Pantalla.M00b, Pantalla.M02v, Pantalla.M02, Pantalla.M02h,
    Pantalla.M02b, Pantalla.M03b, Pantalla.M03, Pantalla.M04(ID_MARCADOR), Pantalla.M05(ID_MARCADOR),
    Pantalla.M06(ID_MARCADOR), Pantalla.M07, Pantalla.M08(ID_MARCADOR), Pantalla.M09(ID_MARCADOR),
    Pantalla.M10(ID_MARCADOR), Pantalla.M11, Pantalla.M12, Pantalla.M13,
)

/**
 * Raíz de navegación (Navigation 3). Las pantallas reales se registran con [entradas]; lo que no esté registrado se
 * muestra como [PantallaMarcador]. Barra inferior y FAB los decide la clave visible (`conNavegacionInferior`, `conFab`).
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
            entryProvider = entryProvider {
                entradas()
                plantillasMarcador.forEach { plantilla -> registrarMarcador(plantilla, alVolver) }
            },
        )
    }
}

/** Registra un marcador por tipo de clave; `entryProvider` usa la primera entrada que coincide, así [entradas] gana. */
private fun EntryProviderScope<NavKey>.registrarMarcador(plantilla: Pantalla, alVolver: () -> Unit) {
    val meta = if (plantilla.esHoja) HojaInferiorSceneStrategy.hoja() else emptyMap()
    when (plantilla) {
        is Pantalla.M01 -> entry<Pantalla.M01> { PantallaMarcador(it, alVolver) }
        is Pantalla.M00a -> entry<Pantalla.M00a> { PantallaMarcador(it, alVolver) }
        is Pantalla.M00b -> entry<Pantalla.M00b> { PantallaMarcador(it, alVolver) }
        is Pantalla.M02v -> entry<Pantalla.M02v> { PantallaMarcador(it, alVolver) }
        is Pantalla.M02 -> entry<Pantalla.M02> { PantallaMarcador(it, alVolver) }
        is Pantalla.M02h -> entry<Pantalla.M02h>(metadata = meta) { PantallaMarcador(it, alVolver) }
        is Pantalla.M02b -> entry<Pantalla.M02b> { PantallaMarcador(it, alVolver) }
        is Pantalla.M03b -> entry<Pantalla.M03b> { PantallaMarcador(it, alVolver) }
        is Pantalla.M03 -> entry<Pantalla.M03> { PantallaMarcador(it, alVolver) }
        is Pantalla.M04 -> entry<Pantalla.M04>(metadata = meta) { PantallaMarcador(it, alVolver) }
        is Pantalla.M05 -> entry<Pantalla.M05> { PantallaMarcador(it, alVolver) }
        is Pantalla.M06 -> entry<Pantalla.M06> { PantallaMarcador(it, alVolver) }
        is Pantalla.M07 -> entry<Pantalla.M07> { PantallaMarcador(it, alVolver) }
        is Pantalla.M08 -> entry<Pantalla.M08> { PantallaMarcador(it, alVolver) }
        is Pantalla.M09 -> entry<Pantalla.M09> { PantallaMarcador(it, alVolver) }
        is Pantalla.M10 -> entry<Pantalla.M10> { PantallaMarcador(it, alVolver) }
        is Pantalla.M11 -> entry<Pantalla.M11> { PantallaMarcador(it, alVolver) }
        is Pantalla.M12 -> entry<Pantalla.M12> { PantallaMarcador(it, alVolver) }
        is Pantalla.M13 -> entry<Pantalla.M13> { PantallaMarcador(it, alVolver) }
    }
}
