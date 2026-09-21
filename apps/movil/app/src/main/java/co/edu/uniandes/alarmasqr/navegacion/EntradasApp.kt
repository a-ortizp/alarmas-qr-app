package co.edu.uniandes.alarmasqr.navegacion

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.pantallas.m01.M01BienvenidaScreen

/**
 * Registro de las pantallas reales de la Persona A (Plan 2). Lo comparten MainActivity y las pruebas de flujo, así
 * las pruebas navegan por las mismas entradas que el APK. Las claves que no aparecen aquí caen en el marcador.
 * Cada tarea del plan agrega su `entry<…>`; la Persona B agrega las suyas en el mismo archivo.
 */
fun EntryProviderScope<NavKey>.entradasApp(pila: NavBackStack<NavKey>, repositorio: RepositorioDataset) {
    entry<Pantalla.M01> {
        M01BienvenidaScreen(alComenzar = { pila.irA(Pantalla.M00a) }, alConectarLuego = { pila.reemplazarTodo(Pantalla.M02v) })
    }
}
