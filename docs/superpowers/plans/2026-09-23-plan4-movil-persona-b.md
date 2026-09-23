# Plan 4 · Móvil de la Persona B (M02b, M06–M11) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reemplazar el marcador de la Fase 0 por las 7 pantallas reales de la Persona B en `apps/movil` (M02b, M06–M11), sus dos diálogos de confirmación (M06d, M11d) y las pruebas correspondientes.

**Architecture:** Kotlin + Jetpack Compose + Navigation 3, arquitectura ya construida por el Plan 2 (Persona A): `Pantalla` sellada, `RepositorioDataset` (dataset en memoria), `Tokens.kt`, `DialogoConfirmacion`, `HojaInferiorSceneStrategy`, `NotificacionesAlarma`. Este plan solo añade composables y ViewModels nuevos bajo `ui/pantallas/m0X/`, registra sus `entry<Pantalla.Mxx>` en `navegacion/EntradasApp.kt`, y extiende `Modelos.kt`/`RepositorioDataset.kt`/dos componentes compartidos con las extensiones puntuales que la spec identificó.

**Tech Stack:** Kotlin 2.x, Jetpack Compose (Material 3 re-tematizado), Navigation 3, kotlinx.serialization, JUnit + Robolectric + `createComposeRule`.

**Spec:** `docs/superpowers/specs/2026-09-23-plan4-movil-persona-b-design.md`

## Global Constraints

- Nunca un color, tamaño o radio escrito a mano: todo sale de `ui/theme/Tokens.kt` (`Colores`, `Tipografia`, `Tamanos`, `Medidas`, `Radios`, `Espacio`, `Movimiento`, `Trazos`). Si una medida no existe como token, se agrega a `Tokens.kt` primero.
- Botones de toque: 52 dp (`Tamanos.Boton`), excepción M10 con 56 dp (`Tamanos.BotonAlarma`, ya existe — nunca `56.dp` escrito a mano).
- Diálogo de confirmación: siempre `DialogoConfirmacion` (comp. 47) sobre velo Tinta 55 % (`Colores.VeloMovil`); nunca una implementación paralela.
- Un solo elemento amarillo por pantalla.
- Scroll vertical: `ColumnaDesplazable` en toda pantalla de columna; `LazyColumn` en listas largas (M02b, M11 — según regla del README, aunque M11 termina siendo corta, se sigue la misma regla por consistencia con F-M02/F-M11).
- `ViewModel` con `viewModel { … }` (o `viewModel(key = clave.id) { … }` cuando la clave lleva id) dentro de la `entry`, nunca fuera.
- No se toca `dataset.json` salvo el mensaje nuevo `mensajes.enlaceCopiado` (Tarea 1) — `cambioDelOrganizador` y `alSonar` de `a-entrega` ya existen en las tres copias, solo se exponen en Kotlin.
- Rama única `feature/plan4-movil-persona-b` para todo el plan, PR único al final. Commits con el código de pantalla al inicio («M06: …»). **Ningún commit lleva línea de coautoría de Claude.**
- No se modifica ninguna pantalla ya construida por la Persona A (M01–M05, M12, M13) ni nada de `apps/web` (Plan 5, ya entregado).
- Comandos de verificación por tarea: `./gradlew testDebugUnitTest lintDebug` desde `apps/movil`.

---

## Task 1: Exponer `cambioDelOrganizador`/`alSonar` y extender el repositorio

**Files:**
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/Modelos.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/RepositorioDataset.kt`
- Modify: `packages/tokens/dataset.json`, `apps/movil/app/src/main/assets/dataset.json`, `apps/web/public/dataset.json`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/RepositorioDatasetTest.kt`

**Interfaces:**
- Produces: `Alarma.cambioDelOrganizador: CambioDelOrganizador?`, `Alarma.alSonar: AlSonar?`, `RepositorioDataset.aplicarCambioOrganizador(id: String)`, `RepositorioDataset.eventosQR: StateFlow<List<EventoQR>>`, `RepositorioDataset.agregarEvento(evento: EventoQR)`. Las tareas 4 (M06/M09), 5 (M07), 6 (M08), 8 (M10) consumen estos nombres exactos.

- [ ] **Step 1: Escribir la prueba que falla**

Editar `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/RepositorioDatasetTest.kt`, agregar al final de la clase (antes de la última llave):

```kotlin
    @Test
    fun `a-entrega ya trae cambioDelOrganizador y alSonar del dataset`() {
        val repo = RepositorioDataset(json)
        val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }
        val cambio = entrega.cambioDelOrganizador!!
        assertEquals("2026-08-30T17:30:00-05:00", cambio.nuevoInicio)
        assertEquals("2026-08-30T16:45:00-05:00", cambio.nuevaHoraDeAlarma)
        assertEquals("2026-08-30T15:15:00-05:00", cambio.antesSonaba)
        assertEquals("MISO · UniAndes", cambio.autor)
        val alSonar = entrega.alSonar!!
        assertEquals(12, alSonar.salEnMin)
        assertEquals("moderado", alSonar.traficoActual)
        assertTrue(alSonar.rutaDisponible)
        assertNull(repo.dataset.alarmas.first { it.id == "a-tutor" }.cambioDelOrganizador)
    }

    @Test
    fun `aplicarCambioOrganizador actualiza eventoInicio y suena de la alarma`() {
        val repo = RepositorioDataset(json)
        repo.agregarDesdeEvento("e-entrega")
        repo.aplicarCambioOrganizador("a-entrega")
        val actualizada = repo.alarma("a-entrega")!!
        assertEquals("2026-08-30T17:30:00-05:00", actualizada.eventoInicio)
        assertEquals("2026-08-30T16:45:00-05:00", actualizada.suena)
    }

    @Test
    fun `aplicarCambioOrganizador no hace nada si la alarma no existe o no tiene cambio`() {
        val repo = RepositorioDataset(json)
        repo.aplicarCambioOrganizador("a-no-existe")   // no lanza
        val antes = repo.alarma("a-tutor")
        repo.aplicarCambioOrganizador("a-tutor")        // sin cambioDelOrganizador: no hace nada
        assertEquals(antes, repo.alarma("a-tutor"))
    }

    @Test
    fun `agregarEvento agrega un EventoQR nuevo, evento lo encuentra`() {
        val repo = RepositorioDataset(json)
        assertNull(repo.evento("e-manual-1"))
        repo.agregarEvento(EventoQR(id = "e-manual-1", alarmaId = "a-manual-1", titulo = "Prueba", codigoQR = "alarmasqr://evento/e-manual-1", escaneos = 0, etiqueta = "Aún sin escaneos · recién creado"))
        assertEquals("Prueba", repo.evento("e-manual-1")?.titulo)
    }
```

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.datos.RepositorioDatasetTest"`
Expected: FAIL — `cambioDelOrganizador`/`alSonar`/`aplicarCambioOrganizador`/`agregarEvento` no existen todavía (error de compilación).

- [ ] **Step 3: Agregar los campos a `Modelos.kt`**

En `Modelos.kt`, agregar dos `data class` nuevas junto a `Organizador` (después de su declaración):

```kotlin
@Serializable data class CambioDelOrganizador(
    val nuevoInicio: String,
    val nuevaHoraDeAlarma: String,
    val antesSonaba: String,
    val autor: String,
    val motivo: String,
)

@Serializable data class AlSonar(val salEnMin: Int, val traficoActual: String, val rutaDisponible: Boolean)
```

En la `data class Alarma`, agregar dos campos opcionales al final (antes del cierre `) {`):

```kotlin
    val cambioDelOrganizador: CambioDelOrganizador? = null,
    val alSonar: AlSonar? = null,
) {
```

(reemplaza la línea `) {` existente que cierra la lista de parámetros de `Alarma`).

- [ ] **Step 4: Extender `RepositorioDataset.kt`**

Reemplazar la función `evento`:

```kotlin
    fun evento(id: String): EventoQR? = dataset.eventosQR.firstOrNull { it.id == id }
```

por:

```kotlin
    private val _eventosQR = MutableStateFlow(dataset.eventosQR)
    val eventosQR: StateFlow<List<EventoQR>> = _eventosQR.asStateFlow()

    fun evento(id: String): EventoQR? = _eventosQR.value.firstOrNull { it.id == id }

    /** F-M07: agrega el EventoQR de una alarma creada a mano (hoy `dataset.eventosQR` solo se lee). */
    fun agregarEvento(evento: EventoQR) { _eventosQR.value = _eventosQR.value.filterNot { it.id == evento.id } + evento }
```

Agregar, después de `mensajeEliminar`:

```kotlin
    /** F-M09: aplica el cambio del organizador (M09 «Aceptar cambio») — no hace nada si la alarma no existe o no tiene `cambioDelOrganizador`. */
    fun aplicarCambioOrganizador(id: String) {
        val alarma = alarma(id) ?: return
        val cambio = alarma.cambioDelOrganizador ?: return
        agregar(alarma.copy(eventoInicio = cambio.nuevoInicio, suena = cambio.nuevaHoraDeAlarma))
    }
```

- [ ] **Step 5: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.datos.RepositorioDatasetTest"`
Expected: PASS (10 pruebas: las 6 existentes + las 4 nuevas).

- [ ] **Step 6: Agregar `mensajes.enlaceCopiado` a las tres copias de `dataset.json`**

En `packages/tokens/dataset.json`, `apps/movil/app/src/main/assets/dataset.json` y `apps/web/public/dataset.json`: dentro del objeto `mensajes`, agregar `"enlaceCopiado": "Enlace copiado al portapapeles"` (junto a `descargaCompletada`). Cambiar `meta.version` de `"1.2"` a `"1.3"` en las tres. Agregar a `meta.notes` (las tres copias, mismo texto):

```
"v1.3 (2026-09-23): mensajes.enlaceCopiado para «Copiar enlace» de M08 (Plan 4, móvil de la Persona B). cambioDelOrganizador y alSonar de a-entrega ya estaban desde v1.1/v1.2, sin usar hasta este plan."
```

Verificar que las tres copias quedan byte a byte idénticas:

```bash
cmp packages/tokens/dataset.json apps/movil/app/src/main/assets/dataset.json
cmp packages/tokens/dataset.json apps/web/public/dataset.json
```

Expected: sin salida (idénticos).

- [ ] **Step 7: Ejecutar todas las pruebas de datos y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS, sin nuevas alertas de lint.

- [ ] **Step 8: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/Modelos.kt \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/RepositorioDataset.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/RepositorioDatasetTest.kt \
        packages/tokens/dataset.json apps/movil/app/src/main/assets/dataset.json apps/web/public/dataset.json
git commit -m "M09: expone cambioDelOrganizador y alSonar de a-entrega, agrega mensajes.enlaceCopiado"
```

---

## Task 2: Extender `Botones.kt` y crear `FilaAjuste`

**Files:**
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/Botones.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/theme/Tokens.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/FilaAjuste.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/ComponentesPersonaBTest.kt`

**Interfaces:**
- Produces: `BotonPrimario(..., sobreTinta: Boolean = false, alto: Dp = Tamanos.Boton)`, `BotonSecundario(..., alto: Dp = Tamanos.Boton)`, `FilaAjuste(texto: String, modifier: Modifier = Modifier, alTocarFila: (() -> Unit)? = null, control: @Composable () -> Unit)`, `Medidas.FilaAjuste`. Las tareas 4, 8, 9 los consumen.

- [ ] **Step 1: Agregar el token de altura de fila a `Tokens.kt`**

En `object Medidas` (`Tokens.kt`), agregar junto a `FilaOpcion`:

```kotlin
    val FilaAjuste = 40.dp             // fila de ajuste (M06, M11): rótulo + switch/«›», contenido centrado (tutores v1.9)
```

- [ ] **Step 2: Escribir la prueba que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/ComponentesPersonaBTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import org.junit.Rule
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ComponentesPersonaBTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `BotonPrimario sobreTinta y alto personalizado no rompen el toque`() {
        var tocado = false
        regla.setContent {
            AlarmasQRTheme {
                BotonPrimario("Ya voy", onClick = { tocado = true }, sobreTinta = true, alto = Tamanos.BotonAlarma)
            }
        }
        regla.onNodeWithText("Ya voy").performClick()
        assert(tocado)
    }

    @Test
    fun `FilaAjuste dispara alTocarFila al tocar el rotulo y el control por separado`() {
        var filaTocada = false
        val marcado = mutableStateOf(false)
        regla.setContent {
            AlarmasQRTheme {
                FilaAjuste("Alarma conectada", alTocarFila = { filaTocada = true }) {
                    Interruptor(marcado.value, { marcado.value = it })
                }
            }
        }
        regla.onNodeWithText("Alarma conectada").assertIsDisplayed()
        regla.onNodeWithText("Alarma conectada").performClick()
        assert(filaTocada)
        assert(!marcado.value)   // tocar el rótulo no cambia el switch
    }

    @Test
    fun `FilaAjuste sin alTocarFila deja el rotulo sin accion propia`() {
        regla.setContent {
            AlarmasQRTheme { FilaAjuste("No molestar") { Text("control") } }
        }
        regla.onNodeWithText("No molestar").assertIsDisplayed()
    }
}
```

- [ ] **Step 3: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.componentes.ComponentesPersonaBTest"`
Expected: FAIL — error de compilación (`sobreTinta`/`alto` no existen en `BotonPrimario`/`BotonSecundario`, `FilaAjuste` no existe).

- [ ] **Step 4: Extender `Botones.kt`**

Agregar el import que falta al inicio del archivo (junto a `import androidx.compose.ui.unit.dp`):

```kotlin
import androidx.compose.ui.unit.Dp
```

Reemplazar `BotonPrimario` completo:

```kotlin
/**
 * DS comp. 01 «Botón primario»: píldora de 52 (o [alto], p. ej. 56 en M10), Amarillo Energía con texto Tinta. Es el
 * único amarillo de la pantalla. [sobreAmarillo] es la variante de M01 (relleno Tinta, texto blanco) porque el
 * fondo ya es amarillo; [sobreTinta] es la excepción de M10 (relleno blanco, texto Tinta) porque «sobre Tinta el
 * primario es blanco» (DESIGN_SYSTEM.md, design-tokens.json). Los dos son mutuamente excluyentes en la práctica
 * (una pantalla no combina ambos fondos), pero no se valida: quien llama decide cuál aplica.
 */
@Composable
fun BotonPrimario(
    texto: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    sobreAmarillo: Boolean = false, sobreTinta: Boolean = false, habilitado: Boolean = true, alto: Dp = Tamanos.Boton,
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier.fillMaxWidth().height(alto),
        shape = Radios.Pildora,
        colors = ButtonDefaults.buttonColors(
            containerColor = when { sobreTinta -> Colores.Blanco; sobreAmarillo -> Colores.Tinta; else -> Colores.AmarilloEnergia },
            contentColor = when { sobreTinta -> Colores.Tinta; sobreAmarillo -> Colores.Blanco; else -> Colores.Tinta },
            disabledContainerColor = Colores.GrisNiebla,
            disabledContentColor = Colores.GrisTexto,
        ),
        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
    ) { Text(texto, style = Tipografia.Boton) }
}
```

Reemplazar `BotonSecundario` completo:

```kotlin
/** DS comp. 02 «Botón secundario»: contorno 1.5 Tinta (o blanco sobre Tinta, comp. 32), texto del mismo color; alto 52 (o [alto]). */
@Composable
fun BotonSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false, alto: Dp = Tamanos.Boton) {
    val color = if (sobreTinta) Colores.Blanco else Colores.Tinta
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(alto),
        shape = Radios.Pildora,
        border = BorderStroke(Trazos.Borde, color),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = color),
        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
    ) { Text(texto, style = Tipografia.Boton) }
}
```

- [ ] **Step 5: Crear `FilaAjuste.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * Fila de ajuste (revisión de tutores del 2026-09-19, MOCKUPS.md §7 comentario 8): 40 de alto, contenido centrado,
 * rótulo a la izquierda y [control] (un [Interruptor] o «›») a la derecha. [alTocarFila], si no es null, hace
 * clicable el rótulo por separado de [control] — M06 necesita las dos cosas en la misma fila: el switch cambia la
 * preferencia «Confirmar antes de auto-ajustarse» y tocar el rótulo navega a M09 (⏩, simula el push del organizador).
 */
@Composable
fun FilaAjuste(texto: String, modifier: Modifier = Modifier, alTocarFila: (() -> Unit)? = null, control: @Composable () -> Unit) {
    Row(
        modifier.fillMaxWidth().height(Medidas.FilaAjuste),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val rotulo = if (alTocarFila != null) Modifier.clickable(role = Role.Button, onClick = alTocarFila) else Modifier
        Text(texto, style = Tipografia.Opcion, color = Colores.Tinta, modifier = Modifier.weight(1f).then(rotulo))
        control()
    }
}
```

- [ ] **Step 6: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.componentes.ComponentesPersonaBTest"`
Expected: PASS (3 pruebas).

- [ ] **Step 7: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS — confirma que extender `BotonPrimario`/`BotonSecundario` con parámetros por defecto no rompió ningún llamado existente (M00–M05, M12, M13).

- [ ] **Step 8: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/Botones.kt \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/FilaAjuste.kt \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/theme/Tokens.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/ComponentesPersonaBTest.kt
git commit -m "M06: FilaAjuste nueva; BotonPrimario/BotonSecundario ganan alto y sobreTinta"
```

---

## Task 3: M02b · Vista calendario

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02bCalendarioScreen.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02bCalendarioViewModel.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02bCalendarioScreenTest.kt`

**Interfaces:**
- Consumes: `RepositorioDataset.alarmas: StateFlow<List<Alarma>>`, `RepositorioDataset.hoy`, `dataset.calendario` (`mes: String` «2026-08», `diasConAlarmas: Map<String, Int>`, `diaSeleccionado: String`), `FormatoHora.dia(iso): LocalDate`, `TarjetaAlarma`, `BarraSuperior`, `Medidas.PuntoPagina`.
- Produces: `M02bCalendarioScreen(estado: EstadoCalendario, alSeleccionarDia: (LocalDate) -> Unit, alTocarAlarma: (String) -> Unit, alCambiarActiva: (String, Boolean) -> Unit, modifier: Modifier = Modifier)`, `M02bCalendarioViewModel(repositorio: RepositorioDataset)` con `estado: StateFlow<EstadoCalendario>`, `seleccionarDia`, `cambiarActiva`. `Pantalla.M02b` ya existe (`conNavegacionInferior=true`, `conFab=true`).

- [ ] **Step 1: Escribir la prueba que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02bCalendarioScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M02bCalendarioScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `muestra el dia seleccionado del dataset y sus alarmas`() {
        val vm = M02bCalendarioViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme { M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = {}, alCambiarActiva = vm::cambiarActiva) }
        }
        regla.onNodeWithTag("pantalla-M02b").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()   // 2026-08-27, día precargado de dataset.calendario.diaSeleccionado
    }

    @Test
    fun `tocar un dia distinto cambia las alarmas mostradas`() {
        val vm = M02bCalendarioViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme { M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = {}, alCambiarActiva = vm::cambiarActiva) }
        }
        regla.onNodeWithTag("dia-2026-08-30").performClick()
        regla.onNodeWithText("Entrega de proyecto UX").assertIsDisplayed()
    }
}
```

`collectAsStateWithLifecycle()` es la única variante que usa el resto de la app (`EntradasApp.kt`, todas las entradas) — dentro de `regla.setContent { … }` bajo `createComposeRule()`/Robolectric hay un `LifecycleOwner` real (la actividad de prueba), así que funciona igual que en producción; no hace falta la variante `collectAsState()` sin ciclo de vida.

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioScreenTest"`
Expected: FAIL — `M02bCalendarioScreen`/`M02bCalendarioViewModel` no existen.

- [ ] **Step 3: Crear el ViewModel**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class DiaCalendario(val fecha: LocalDate, val alarmasCount: Int)
data class EstadoCalendario(val mes: String, val dias: List<DiaCalendario>, val diaSeleccionado: LocalDate, val alarmasDelDia: List<Alarma>)

private fun diasDelMes(mes: String, conteos: Map<String, Int>): List<DiaCalendario> {
    val (anio, mesNum) = mes.split("-").map { it.toInt() }
    val primerDia = LocalDate.of(anio, mesNum, 1)
    return (1..primerDia.lengthOfMonth()).map { d ->
        val fecha = primerDia.withDayOfMonth(d)
        DiaCalendario(fecha, conteos[fecha.toString()] ?: 0)
    }
}

/** F-M02 (M02b): mes compacto con contador por día (`dataset.calendario.diasConAlarmas`) y detalle del día seleccionado, derivado de `RepositorioDataset.alarmas` filtrando por fecha — no hay un campo de dataset por día. */
class M02bCalendarioViewModel(private val repositorio: RepositorioDataset) : ViewModel() {
    private val calendario = repositorio.dataset.calendario
    private val dias = diasDelMes(calendario.mes, calendario.diasConAlarmas)
    private val _diaSeleccionado = MutableStateFlow(LocalDate.parse(calendario.diaSeleccionado))

    val estado: StateFlow<EstadoCalendario> = combine(repositorio.alarmas, _diaSeleccionado) { alarmas, dia ->
        EstadoCalendario(calendario.mes, dias, dia, alarmas.filter { FormatoHora.dia(it.eventoInicio) == dia })
    }.stateIn(
        viewModelScope, SharingStarted.Eagerly,
        EstadoCalendario(calendario.mes, dias, _diaSeleccionado.value, repositorio.alarmas.value.filter { FormatoHora.dia(it.eventoInicio) == _diaSeleccionado.value }),
    )

    fun seleccionarDia(fecha: LocalDate) { _diaSeleccionado.value = fecha }
    fun cambiarActiva(id: String, activa: Boolean) = repositorio.cambiarEstado(id, pausada = !activa)
}
```

Agregar el import que falta: `import kotlinx.coroutines.flow.StateFlow`.

- [ ] **Step 4: Crear la pantalla**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.TarjetaAlarma
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import java.time.LocalDate

/** M02b · Vista calendario (F-M02, CM-01): mes compacto con contador por día y el detalle del día seleccionado en una `LazyColumn` (lista potencialmente larga). */
@Composable
fun M02bCalendarioScreen(
    estado: EstadoCalendario, alSeleccionarDia: (LocalDate) -> Unit, alTocarAlarma: (String) -> Unit, alCambiarActiva: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M02b")) {
        BarraSuperior("Calendario")
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            item(key = "rejilla") { RejillaMes(estado.dias, estado.diaSeleccionado, alSeleccionarDia) }
            if (estado.alarmasDelDia.isEmpty()) {
                item(key = "vacio") { Text("Sin alarmas este día.", style = Tipografia.Etiqueta, color = Colores.GrisTexto) }
            } else {
                items(estado.alarmasDelDia, key = { "alarma-${it.id}" }) { alarma ->
                    TarjetaAlarma(alarma, onClick = { alTocarAlarma(alarma.id) }, alCambiarActiva = { alCambiarActiva(alarma.id, it) })
                }
            }
        }
    }
}

private fun semanas(dias: List<DiaCalendario>): List<List<DiaCalendario?>> {
    val relleno: List<DiaCalendario?> = List(dias.first().fecha.dayOfWeek.value - 1) { null }   // lunes = 1 → 0 espacios
    val celdas = relleno + dias
    return celdas.chunked(7).map { semana -> semana + List(7 - semana.size) { null } }
}

@Composable
private fun RejillaMes(dias: List<DiaCalendario>, seleccionado: LocalDate, alSeleccionar: (LocalDate) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        semanas(dias).forEach { semana ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                semana.forEach { dia ->
                    if (dia == null) Box(Modifier.weight(1f)) else CeldaDia(dia, dia.fecha == seleccionado, { alSeleccionar(dia.fecha) }, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CeldaDia(dia: DiaCalendario, marcado: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.clip(Radios.Pildora).clickable(role = Role.Button, onClick = onClick)
            .background(if (marcado) Colores.Tinta else Color.Transparent, Radios.Pildora)
            .padding(vertical = Espacio.GapFila).testTag("dia-${dia.fecha}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoTarjeta),
    ) {
        Text(dia.fecha.dayOfMonth.toString(), style = Tipografia.TituloTarjeta, color = if (marcado) Colores.Blanco else Colores.Tinta)
        if (dia.alarmasCount > 0) Box(Modifier.size(Medidas.PuntoPagina).background(if (marcado) Colores.Blanco else Colores.Tinta, Radios.Pildora))
    }
}
```

- [ ] **Step 5: Registrar la entrada en `EntradasApp.kt`**

Agregar el import `import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioScreen` y `import co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioViewModel`, y dentro de `entradasApp`, después de la entrada de `Pantalla.M02h`:

```kotlin
    entry<Pantalla.M02b> {
        val vm = viewModel { M02bCalendarioViewModel(repositorio) }
        val estado by vm.estado.collectAsStateWithLifecycle()
        M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = { pila.irA(Pantalla.M06(it)) }, alCambiarActiva = vm::cambiarActiva)
    }
```

- [ ] **Step 6: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioScreenTest"`
Expected: PASS.

- [ ] **Step 7: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02bCalendarioScreen.kt \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02bCalendarioViewModel.kt \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02bCalendarioScreenTest.kt
git commit -m "M02b: vista calendario con mes compacto y detalle del día seleccionado"
```

---

## Task 4: M06 · Editar alarma + M06d

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m06/M06EditarAlarmaScreen.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m06/M06EditarAlarmaViewModel.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m06/M06EditarAlarmaScreenTest.kt`

**Interfaces:**
- Consumes: `RepositorioDataset.alarma(id)`, `RepositorioDataset.agregar`, `RepositorioDataset.eliminar`, `RepositorioDataset.mensajeEliminar`, `RepositorioDataset.dataset.usuario.ajustes`, `ChipControl`, `FilaAjuste`, `Interruptor`, `BotonEnlace`, `DialogoConfirmacion` (Task 2/ya existentes).
- Produces: `M06EditarAlarmaScreen(estado, mensajes, mensajeEliminar, puedeVerCambioOrganizador, alVolver, alElegirAnticipacion, alElegirSonido, alCambiarConfirmar, alTocarCambioOrganizador, alGestionarCalendario, alGuardar, alAbrirDialogo, alConservar, alEliminar, modifier)`. `M06EditarAlarmaViewModel(repositorio, id)` con `estado: StateFlow<EstadoEditarAlarma>`, `mensajeEliminar: String`, `puedeVerCambioOrganizador: Boolean`.

- [ ] **Step 1: Escribir la prueba que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m06/M06EditarAlarmaScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m06

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M06EditarAlarmaScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    private fun montar(id: String): M06EditarAlarmaViewModel {
        val vm = M06EditarAlarmaViewModel(repo, id)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme {
                M06EditarAlarmaScreen(
                    estado = estado, mensajes = repo.dataset.mensajes, mensajeEliminar = vm.mensajeEliminar, puedeVerCambioOrganizador = vm.puedeVerCambioOrganizador,
                    alVolver = {}, alElegirAnticipacion = vm::elegirAnticipacion, alElegirSonido = vm::elegirSonido, alCambiarConfirmar = vm::cambiarConfirmar,
                    alTocarCambioOrganizador = {}, alGestionarCalendario = {}, alGuardar = vm::guardar,
                    alAbrirDialogo = vm::abrirDialogo, alConservar = vm::cerrarDialogo, alEliminar = vm::eliminar,
                )
            }
        }
        return vm
    }

    @Test
    fun `muestra el titulo de la alarma y guarda la anticipacion elegida`() {
        montar("a-tutor")
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()
        regla.onNodeWithText("1 h").performClick()
        regla.onNodeWithTag("guardar").performClick()
        assertEquals(60, repo.alarma("a-tutor")?.anticipacionMin)
    }

    @Test
    fun `eliminar abre M06d y confirma la eliminacion`() {
        montar("a-gimnasio")
        regla.onNodeWithTag("eliminar").performClick()
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.onNodeWithTag("dialogo-confirmar").performClick()
        assertNull(repo.alarma("a-gimnasio"))
    }

    @Test
    fun `sin cambioDelOrganizador la fila de alarma conectada no navega`() {
        val vm = montar("a-tutor")
        assertEquals(false, vm.puedeVerCambioOrganizador)
    }
}
```

Agregar `import org.junit.Assert.assertEquals` junto a `assertNull`.

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m06.M06EditarAlarmaScreenTest"`
Expected: FAIL — `M06EditarAlarmaScreen`/`M06EditarAlarmaViewModel` no existen.

- [ ] **Step 3: Crear el ViewModel**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m06

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoEditarAlarma(
    val alarma: Alarma,
    val anticipacionMin: Int,
    val sonido: String,
    val confirmarAntesDeAutoAjustar: Boolean,
    val dialogoAbierto: Boolean = false,
)

/**
 * F-M06: personalización por alarma. Solo `anticipacionMin` existe en el modelo `Alarma`; `sonido` y
 * `confirmarAntesDeAutoAjustar` son estado de UI que arranca desde `usuario.ajustes` (no hay campo por alarma en el
 * dataset para ellos — «Guardar cambios» solo persiste la anticipación, consistente con la maquetación).
 */
class M06EditarAlarmaViewModel(private val repositorio: RepositorioDataset, private val id: String) : ViewModel() {
    private val original = repositorio.alarma(id) ?: error("Alarma $id no existe")
    private val _estado = MutableStateFlow(
        EstadoEditarAlarma(
            alarma = original,
            anticipacionMin = original.anticipacionMin,
            sonido = repositorio.dataset.usuario.ajustes.sonidoPorDefecto,
            confirmarAntesDeAutoAjustar = repositorio.dataset.usuario.ajustes.confirmarAntesDeAutoAjustar,
        ),
    )
    val estado: StateFlow<EstadoEditarAlarma> = _estado.asStateFlow()
    val mensajeEliminar: String = repositorio.mensajeEliminar(original)

    /** M09 solo tiene contenido para la alarma que trae `cambioDelOrganizador` (hoy, «a-entrega»). */
    val puedeVerCambioOrganizador: Boolean = original.cambioDelOrganizador != null

    fun elegirAnticipacion(min: Int) = _estado.update { it.copy(anticipacionMin = min) }
    fun elegirSonido(valor: String) = _estado.update { it.copy(sonido = valor) }
    fun cambiarConfirmar(valor: Boolean) = _estado.update { it.copy(confirmarAntesDeAutoAjustar = valor) }
    fun guardar() = repositorio.agregar(_estado.value.alarma.copy(anticipacionMin = _estado.value.anticipacionMin))
    fun abrirDialogo() = _estado.update { it.copy(dialogoAbierto = true) }
    fun cerrarDialogo() = _estado.update { it.copy(dialogoAbierto = false) }
    fun eliminar() { repositorio.eliminar(id); cerrarDialogo() }
}
```

- [ ] **Step 4: Crear la pantalla**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m06

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipControl
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.FilaAjuste
import co.edu.uniandes.alarmasqr.ui.componentes.Interruptor
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M06 · Editar alarma (F-M06): sin miga de pan (MOCKUPS.md §7 paso 1). «Eliminar alarma» abre M06d. */
@Composable
fun M06EditarAlarmaScreen(
    estado: EstadoEditarAlarma, mensajes: Mensajes, mensajeEliminar: String, puedeVerCambioOrganizador: Boolean,
    alVolver: () -> Unit, alElegirAnticipacion: (Int) -> Unit, alElegirSonido: (String) -> Unit, alCambiarConfirmar: (Boolean) -> Unit,
    alTocarCambioOrganizador: () -> Unit, alGestionarCalendario: () -> Unit, alGuardar: () -> Unit,
    alAbrirDialogo: () -> Unit, alConservar: () -> Unit, alEliminar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M06")) {
        BarraSuperior("Editar alarma", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            Text(estado.alarma.titulo, style = Tipografia.H2, color = Colores.Tinta)
            SelectorAnticipacion(estado.anticipacionMin, alElegirAnticipacion)
            SelectorSonido(estado.sonido, alElegirSonido)
            FilaAjuste("Alarma conectada · Confirmar antes de auto-ajustarse", alTocarFila = if (puedeVerCambioOrganizador) alTocarCambioOrganizador else null) {
                Interruptor(estado.confirmarAntesDeAutoAjustar, alCambiarConfirmar)
            }
            BotonEnlace("Gestionar en el calendario", onClick = alGestionarCalendario, color = ColorEnlace.Azul)
            BotonPrimario("Guardar cambios", onClick = alGuardar, modifier = Modifier.testTag("guardar"))
            BotonEnlace("Eliminar alarma", onClick = alAbrirDialogo, color = ColorEnlace.Coral, modifier = Modifier.testTag("eliminar"))
        }
    }
    if (estado.dialogoAbierto) {
        DialogoConfirmacion(
            titulo = mensajes.confirmarEliminarTitulo, cuerpo = mensajeEliminar,
            rotuloSeguro = mensajes.confirmarEliminarSeguro, rotuloConfirmar = mensajes.confirmarEliminarAccion,
            destructivo = true, alSeguro = alConservar, alConfirmar = alEliminar,
        )
    }
}

private val OPCIONES_ANTICIPACION = listOf(10, 30, 60)
private fun etiquetaAnticipacion(min: Int) = if (min < 60) "$min min" else "1 h"

@Composable
private fun SelectorAnticipacion(seleccionado: Int, alElegir: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text("ANTICIPACIÓN", style = Tipografia.H3, color = Colores.GrisTexto)
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
            OPCIONES_ANTICIPACION.forEach { min -> ChipControl(etiquetaAnticipacion(min), activo = seleccionado == min, onClick = { alElegir(min) }) }
            // «Otro»: representa el valor actual cuando no es 10/30/60; sin selector de minutos personalizado (maquetación).
            ChipControl("Otro · ${seleccionado} min", activo = seleccionado !in OPCIONES_ANTICIPACION, onClick = {})
        }
    }
}

private val OPCIONES_SONIDO = listOf("sonar" to "Sonar", "vibrar" to "Vibrar", "silencio" to "Silencio")

@Composable
private fun SelectorSonido(seleccionado: String, alElegir: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text("SONIDO", style = Tipografia.H3, color = Colores.GrisTexto)
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
            OPCIONES_SONIDO.forEach { (valor, texto) -> ChipControl(texto, activo = seleccionado == valor, onClick = { alElegir(valor) }) }
        }
    }
}
```

- [ ] **Step 5: Registrar la entrada en `EntradasApp.kt`**

Agregar los imports `M06EditarAlarmaScreen` y `M06EditarAlarmaViewModel`; dentro de `entradasApp`, reemplazar la línea de import de `M04AlarmaCreadaViewModel` por el bloque siguiente después de la entrada `Pantalla.M05` (M06 sustituye cualquier resto de marcador):

```kotlin
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
```

- [ ] **Step 6: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m06.M06EditarAlarmaScreenTest"`
Expected: PASS.

- [ ] **Step 7: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m06 \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m06
git commit -m "M06: editar alarma (anticipación, sonido, alarma conectada) y diálogo M06d"
```

---

## Task 5: M07 · Crear evento a mano

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m07/M07CrearEventoScreen.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/RepositorioDataset.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m07/M07CrearEventoScreenTest.kt`, `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/RepositorioDatasetTest.kt`

**Interfaces:**
- Consumes: `CampoTexto`, `ChipControl`, `BotonPrimario`, `RepositorioDataset.agregar`, `RepositorioDataset.agregarEvento` (Task 1).
- Produces: `M07CrearEventoScreen(titulo, lugar, descripcion, anticipacionMin, fechaHoraTexto, alCambiarTitulo, alCambiarLugar, alCambiarDescripcion, alElegirAnticipacion, alVolver, alGuardar, modifier)` — sin ViewModel (mismo patrón que M00a/M00b: estado local `rememberSaveable`). `RepositorioDataset.crearAlarmaManual(titulo, eventoInicio, lugar, detalle, anticipacionMin): Alarma`, consumida por la Tarea 6 (M08 recibe el id del evento creado).

- [ ] **Step 1: Escribir la prueba del repositorio que falla**

Agregar a `RepositorioDatasetTest.kt`:

```kotlin
    @Test
    fun `crearAlarmaManual agrega la alarma y su EventoQR, origen creada-por-mi`() {
        val repo = RepositorioDataset(json)
        val alarma = repo.crearAlarmaManual("Cena de fin de año", "2026-08-28T19:00:00-05:00", "Casa de Andrés", null, 30)
        assertEquals("creada-por-mi", alarma.origen)
        assertEquals(listOf("Creada por mí"), alarma.chips)
        assertEquals("2026-08-28T18:30:00-05:00", alarma.suena)
        assertEquals(alarma.id, repo.alarma(alarma.id)?.id)
        val evento = repo.dataset.eventosQR.let { repo.evento("e-" + alarma.id.removePrefix("a-")) }
        assertEquals("Aún sin escaneos · recién creado", evento?.etiqueta)
    }
```

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.datos.RepositorioDatasetTest"`
Expected: FAIL — `crearAlarmaManual` no existe.

- [ ] **Step 3: Agregar `crearAlarmaManual` a `RepositorioDataset.kt`**

Agregar después de `agregarEvento` (Task 1):

```kotlin
    /** F-M07: alarma creada a mano + su EventoQR, id por marca de tiempo (única por sesión; no persiste entre reinicios). */
    fun crearAlarmaManual(titulo: String, eventoInicio: String, lugar: String?, detalle: String?, anticipacionMin: Int): Alarma {
        val id = "a-manual-" + System.currentTimeMillis()
        val suena = OffsetDateTime.parse(eventoInicio).minusMinutes(anticipacionMin.toLong()).toString()
        val alarma = Alarma(
            id = id, titulo = titulo, eventoInicio = eventoInicio, suena = suena, lugar = lugar,
            origen = "creada-por-mi", estado = "activa", anticipacionMin = anticipacionMin, trayectoMin = 0,
            chips = listOf("Creada por mí"), detalle = detalle,
        )
        agregar(alarma)
        val eventoId = "e-" + id.removePrefix("a-")
        agregarEvento(EventoQR(id = eventoId, alarmaId = id, titulo = titulo, codigoQR = "alarmasqr://evento/$eventoId", escaneos = 0, etiqueta = "Aún sin escaneos · recién creado"))
        return alarma
    }
```

- [ ] **Step 4: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.datos.RepositorioDatasetTest"`
Expected: PASS.

- [ ] **Step 5: Escribir la prueba de pantalla que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m07/M07CrearEventoScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m07

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M07CrearEventoScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `campo vacio muestra solo la etiqueta y guardar entrega los valores escritos`() {
        var guardado: Triple<String, String?, Int>? = null
        regla.setContent {
            AlarmasQRTheme {
                M07CrearEventoScreen(
                    alVolver = {},
                    alGuardar = { titulo, lugar, _, anticipacionMin -> guardado = Triple(titulo, lugar, anticipacionMin) },
                )
            }
        }
        regla.onNodeWithTag("pantalla-M07").assertIsDisplayed()
        regla.onNodeWithTag("titulo").performTextInput("Asado familiar")
        regla.onNodeWithTag("lugar").performTextInput("Casa de mis papás")
        regla.onNodeWithText("1 h").performClick()
        regla.onNodeWithTag("guardar").performClick()
        assertEquals(Triple("Asado familiar", "Casa de mis papás", 60), guardado)
    }
}
```

- [ ] **Step 6: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m07.M07CrearEventoScreenTest"`
Expected: FAIL — `M07CrearEventoScreen` no existe.

- [ ] **Step 7: Crear la pantalla**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m07

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.CampoTexto
import co.edu.uniandes.alarmasqr.ui.componentes.ChipControl
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M07 · Crear evento a mano (F-M07): campo vacío muestra solo su etiqueta, sin valor de ejemplo ni pista (revisión
 * de tutores, MOCKUPS.md §7 paso 5 noveno comentario). Fecha y hora se precargan (MISO no tiene selector de fecha
 * construido todavía: se muestran de solo lectura con el valor por defecto, mañana 10:00 am). «Guardar y crear QR»
 * entrega los campos a la entrada de navegación, que arma la `Alarma`/`EventoQR` vía `RepositorioDataset.crearAlarmaManual`.
 */
@Composable
fun M07CrearEventoScreen(
    alVolver: () -> Unit,
    alGuardar: (titulo: String, lugar: String?, descripcion: String?, anticipacionMin: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var lugar by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var anticipacionMin by rememberSaveable { mutableStateOf(30) }

    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M07")) {
        BarraSuperior("Crear evento", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            CampoTexto(titulo, { titulo = it }, etiqueta = "Título", modifier = Modifier.testTag("titulo"))
            CampoTexto("Mañana · 10:00 am", {}, etiqueta = "Fecha y hora", modifier = Modifier.testTag("fecha-hora"))
            CampoTexto(lugar, { lugar = it }, etiqueta = "Lugar", modifier = Modifier.testTag("lugar"))
            CampoTexto(descripcion, { descripcion = it }, etiqueta = "Descripción", modifier = Modifier.testTag("descripcion"))
            Column(verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
                Text("ANTICIPACIÓN", style = Tipografia.H3, color = Colores.GrisTexto)
                Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
                    listOf(10 to "10 min", 30 to "30 min", 60 to "1 h").forEach { (min, texto) ->
                        ChipControl(texto, activo = anticipacionMin == min, onClick = { anticipacionMin = min })
                    }
                }
            }
            BotonPrimario(
                "Guardar y crear QR",
                onClick = { alGuardar(titulo, lugar.ifBlank { null }, descripcion.ifBlank { null }, anticipacionMin) },
                modifier = Modifier.testTag("guardar"),
            )
        }
    }
}
```

- [ ] **Step 8: Registrar la entrada en `EntradasApp.kt`**

Agregar el import `M07CrearEventoScreen`; dentro de `entradasApp`, después de la entrada de `Pantalla.M06`:

```kotlin
    entry<Pantalla.M07> {
        M07CrearEventoScreen(
            alVolver = { pila.removeLastOrNull() },
            alGuardar = { titulo, lugar, descripcion, anticipacionMin ->
                val eventoInicio = "${repositorio.hoy.plusDays(1)}T10:00:00-05:00"
                val alarma = repositorio.crearAlarmaManual(titulo, eventoInicio, lugar, descripcion, anticipacionMin)
                val eventoId = "e-" + alarma.id.removePrefix("a-")
                pila.reemplazarCima(Pantalla.M08(eventoId))
            },
        )
    }
```

- [ ] **Step 9: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m07.M07CrearEventoScreenTest"`
Expected: PASS.

- [ ] **Step 10: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 11: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m07 \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/RepositorioDataset.kt \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m07 \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/RepositorioDatasetTest.kt
git commit -m "M07: crear evento a mano, RepositorioDataset.crearAlarmaManual"
```

---

## Task 6: M08 · QR del evento

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m08/M08CompartirQRScreen.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m08/M08CompartirQRScreenTest.kt`

**Interfaces:**
- Consumes: `EventoQR` (`titulo`, `codigoQR`, `escaneos`, `etiqueta`), `CodigoQR`, `BotonPrimario`, `BotonSecundario`.
- Produces: `M08CompartirQRScreen(evento: EventoQR, alVolver, alCompartir, alDescargar, alCopiarEnlace, modifier)`, sin ViewModel (pantalla de solo lectura, mismo patrón que `M03bPantallazoScreen`).

- [ ] **Step 1: Escribir la prueba que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m08/M08CompartirQRScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m08

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M08CompartirQRScreenTest {
    @get:Rule val regla = createComposeRule()
    private val evento = EventoQR(id = "e-tutor", alarmaId = "a-tutor", titulo = "Reunión con el tutor", codigoQR = "alarmasqr://evento/e-tutor", escaneos = 0, etiqueta = "Aún sin escaneos · recién creado")

    @Test
    fun `muestra el titulo, el QR y dispara los tres callbacks`() {
        var compartido = false; var descargado = false; var copiado = false
        regla.setContent {
            AlarmasQRTheme {
                M08CompartirQRScreen(evento, alVolver = {}, alCompartir = { compartido = true }, alDescargar = { descargado = true }, alCopiarEnlace = { copiado = true })
            }
        }
        regla.onNodeWithTag("pantalla-M08").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()
        regla.onNodeWithTag("qr").assertIsDisplayed()
        regla.onNodeWithTag("compartir").performClick(); assert(compartido)
        regla.onNodeWithTag("descargar").performClick(); assert(descargado)
        regla.onNodeWithTag("copiar-enlace").performClick(); assert(copiado)
    }
}
```

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m08.M08CompartirQRScreenTest"`
Expected: FAIL — `M08CompartirQRScreen` no existe.

- [ ] **Step 3: Crear la pantalla**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m08

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M08 · QR del evento (F-M08): QR real (ZXing) de cualquier alarma propia o escaneada; compartir/descargar/copiar enlace. */
@Composable
fun M08CompartirQRScreen(
    evento: EventoQR, alVolver: () -> Unit, alCompartir: () -> Unit, alDescargar: () -> Unit, alCopiarEnlace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M08")) {
        BarraSuperior("QR del evento", alVolver = alVolver)
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(evento.titulo, style = Tipografia.H2, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            CodigoQR(evento.codigoQR, tamano = Medidas.QRVisor, modifier = Modifier.testTag("qr"))
            Text(evento.etiqueta ?: "${evento.escaneos ?: 0} escaneos", style = Tipografia.Etiqueta, color = Colores.GrisTexto)
            BotonPrimario("Compartir por WhatsApp", onClick = alCompartir, modifier = Modifier.testTag("compartir"))
            BotonSecundario("Descargar como imagen", onClick = alDescargar, modifier = Modifier.testTag("descargar"))
            BotonSecundario("Copiar enlace", onClick = alCopiarEnlace, modifier = Modifier.testTag("copiar-enlace"))
        }
    }
}
```

- [ ] **Step 4: Registrar la entrada en `EntradasApp.kt`**

Agregar los imports `M08CompartirQRScreen`, `android.content.ClipData`, `android.content.ClipboardManager`, `androidx.compose.runtime.rememberCoroutineScope`, `kotlinx.coroutines.launch`; dentro de `entradasApp`, después de la entrada de `Pantalla.M07`:

```kotlin
    entry<Pantalla.M08> { clave ->
        val evento = repositorio.evento(clave.id)
        if (evento == null) {
            LaunchedEffect(clave) { pila.removeLastOrNull() }
        } else {
            val context = LocalContext.current
            val snackbar = LocalSnackbarApp.current
            val mensajes = repositorio.dataset.mensajes
            val alcance = rememberCoroutineScope()
            M08CompartirQRScreen(
                evento = evento,
                alVolver = { pila.removeLastOrNull() },
                alCompartir = {
                    val intent = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, evento.codigoQR) }
                    context.startActivity(Intent.createChooser(intent, "Compartir QR"))
                },
                alDescargar = { alcance.launch { snackbar.showSnackbar(mensajes.descargaCompletada) } },
                alCopiarEnlace = {
                    val portapapeles = context.getSystemService(ClipboardManager::class.java)
                    portapapeles.setPrimaryClip(ClipData.newPlainText("Enlace del QR", evento.codigoQR))
                    alcance.launch { snackbar.showSnackbar(mensajes.enlaceCopiado) }
                },
            )
        }
    }
```

- [ ] **Step 5: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m08.M08CompartirQRScreenTest"`
Expected: PASS.

- [ ] **Step 6: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m08 \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m08
git commit -m "M08: QR del evento, compartir real por WhatsApp y descargar/copiar simulados"
```

---

## Task 7: M09 · Cambio del organizador

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m09/M09CambioEventoScreen.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m09/M09CambioEventoScreenTest.kt`

**Interfaces:**
- Consumes: `Alarma.cambioDelOrganizador` (Task 1), `FormatoHora.horaConSufijo`, `BandaTextura`, `BotonPrimario(sobreTinta=true)`, `BotonSecundario(sobreTinta=true)`, `BarraSuperior` (con `accion`).
- Produces: `M09CambioEventoScreen(alarma: Alarma, alAceptar, alMantener, alCerrar, alVerAlarma, modifier)`.

- [ ] **Step 1: Escribir la prueba que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m09/M09CambioEventoScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m09

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M09CambioEventoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())
    private val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }

    @Test
    fun `muestra la hora nueva y la anterior, y dispara aceptar-mantener-cerrar`() {
        var aceptado = false; var mantenido = false; var cerrado = false
        regla.setContent {
            AlarmasQRTheme { M09CambioEventoScreen(entrega, alAceptar = { aceptado = true }, alMantener = { mantenido = true }, alCerrar = { cerrado = true }, alVerAlarma = {}) }
        }
        regla.onNodeWithTag("pantalla-M09").assertIsDisplayed()
        regla.onNodeWithText("4:45 pm").assertIsDisplayed()
        regla.onNodeWithText("Antes sonaba 3:15 pm").assertIsDisplayed()
        regla.onNodeWithTag("aceptar").performClick(); assert(aceptado)
        regla.onNodeWithTag("mantener").performClick(); assert(mantenido)
        regla.onNodeWithTag("cerrar").performClick(); assert(cerrado)
    }
}
```

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m09.M09CambioEventoScreenTest"`
Expected: FAIL — `M09CambioEventoScreen` no existe.

- [ ] **Step 3: Crear la pantalla**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m09

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M09 · Cambio del organizador (F-M09): la «×» va al margen derecho de la barra (v1.7); sin diálogo de eliminar
 * (desde la v1.1 de los mockups, «Eliminar» solo vive en M06). Requiere `alarma.cambioDelOrganizador` — quien
 * registra la entrada solo navega aquí para alarmas que lo tienen (hoy, «a-entrega»).
 */
@Composable
fun M09CambioEventoScreen(alarma: Alarma, alAceptar: () -> Unit, alMantener: () -> Unit, alCerrar: () -> Unit, alVerAlarma: () -> Unit, modifier: Modifier = Modifier) {
    val cambio = requireNotNull(alarma.cambioDelOrganizador) { "M09 requiere una alarma con cambioDelOrganizador" }
    Column(modifier.fillMaxSize().background(Colores.Tinta).testTag("pantalla-M09")) {
        BarraSuperior("Cambio en el evento", sobreTinta = true, accion = { BotonCerrar(onClick = alCerrar) })
        ColumnaDesplazable(
            Modifier.fillMaxSize(),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            BandaTextura(sobreTinta = true)
            Text("SI ACEPTAS, SONARÁ", style = Tipografia.H3, color = Colores.GrisBorde)
            Text(FormatoHora.horaConSufijo(cambio.nuevaHoraDeAlarma), style = Tipografia.HoraSonara, color = Colores.AmarilloEnergia)
            Text("${alarma.anticipacionMin} min de margen + ${alarma.trayectoMin} min de trayecto", style = Tipografia.MargenSonara, color = Colores.GrisBorde)
            Text("Antes sonaba ${FormatoHora.horaConSufijo(cambio.antesSonaba)}", style = Tipografia.Etiqueta, color = Colores.GrisBorde)
            Text(cambio.autor, style = Tipografia.Etiqueta, color = Colores.GrisBorde)
            TextButton(onClick = alVerAlarma, modifier = Modifier.testTag("ver-alarma")) {
                Text(alarma.titulo, style = Tipografia.TituloTarjeta, color = Colores.Blanco)
            }
            BotonPrimario("Aceptar cambio", onClick = alAceptar, sobreTinta = true, modifier = Modifier.testTag("aceptar"))
            BotonSecundario("Mantener alarma", onClick = alMantener, sobreTinta = true, modifier = Modifier.testTag("mantener"))
        }
    }
}

@Composable
private fun BotonCerrar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier.size(Medidas.BotonAtras).testTag("cerrar"),
        colors = ButtonDefaults.textButtonColors(contentColor = Colores.Blanco),
    ) { Text("×", style = Tipografia.FlechaAtras) }
}
```

- [ ] **Step 4: Registrar la entrada en `EntradasApp.kt`**

Agregar el import `M09CambioEventoScreen`; dentro de `entradasApp`, después de la entrada de `Pantalla.M08`:

```kotlin
    entry<Pantalla.M09> { clave ->
        val alarma = repositorio.alarma(clave.id)
        if (alarma == null) {
            LaunchedEffect(clave) { pila.reemplazarTodo(Pantalla.M02) }
        } else {
            M09CambioEventoScreen(
                alarma = alarma,
                alAceptar = { repositorio.aplicarCambioOrganizador(clave.id); pila.irA(Pantalla.M10(clave.id)) },
                alMantener = { pila.reemplazarTodo(Pantalla.M02) },
                alCerrar = { pila.reemplazarTodo(Pantalla.M02) },
                alVerAlarma = { pila.reemplazarCima(Pantalla.M06(clave.id)) },
            )
        }
    }
```

- [ ] **Step 5: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m09.M09CambioEventoScreenTest"`
Expected: PASS.

- [ ] **Step 6: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m09 \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m09
git commit -m "M09: cambio del organizador, «×» al margen derecho, sin diálogo de eliminar"
```

---

## Task 8: M10 · Alarma sonando

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m10/M10AlarmaSonandoScreen.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m10/M10AlarmaSonandoScreenTest.kt`

**Interfaces:**
- Consumes: `Alarma.alSonar` (Task 1), `Destello`, `BandaTextura`, `BotonPrimario(sobreTinta=true, alto=Tamanos.BotonAlarma)`, `BotonSecundario(sobreTinta=true, alto=Tamanos.BotonAlarma)` (Task 2), `Tipografia.HoraProtagonista`.
- Produces: `M10AlarmaSonandoScreen(alarma: Alarma, alYaVoy, alPosponer, alVerRuta: (() -> Unit)?, modifier)`. Es el destino real de `NotificacionesAlarma.intentSonando` (ya wireado desde el Plan 2).

- [ ] **Step 1: Escribir la prueba que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m10/M10AlarmaSonandoScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m10

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M10AlarmaSonandoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `con alSonar muestra la hora, sal en X min y Ver ruta`() {
        val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }
        var yaVoy = false; var ruta = false
        regla.setContent {
            AlarmasQRTheme { M10AlarmaSonandoScreen(entrega, alYaVoy = { yaVoy = true }, alPosponer = {}, alVerRuta = { ruta = true }) }
        }
        regla.onNodeWithTag("pantalla-M10").assertIsDisplayed()
        regla.onNodeWithText("Sal en 12 min · tráfico moderado", substring = true).assertIsDisplayed()
        regla.onNodeWithText("Ver ruta ›").performClick(); assert(ruta)
        regla.onNodeWithTag("ya-voy").performClick(); assert(yaVoy)
    }

    @Test
    fun `sin alSonar no muestra el destacado de tráfico`() {
        val tutor = repo.dataset.alarmas.first { it.id == "a-tutor" }
        regla.setContent {
            AlarmasQRTheme { M10AlarmaSonandoScreen(tutor, alYaVoy = {}, alPosponer = {}, alVerRuta = null) }
        }
        regla.onNodeWithTag("pantalla-M10").assertIsDisplayed()
        regla.onNodeWithText("Ver ruta ›").assertDoesNotExist()
    }
}
```

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m10.M10AlarmaSonandoScreenTest"`
Expected: FAIL — `M10AlarmaSonandoScreen` no existe.

- [ ] **Step 3: Crear la pantalla**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m10

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.Destello
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M10 · Alarma sonando (F-M10, pantalla completa): fondo Tinta, hora protagonista en Amarillo Energía (excepción
 * «sobre Tinta el primario es blanco»: los dos botones usan [sobreTinta]). Destino real de
 * `NotificacionesAlarma.intentSonando` — llega tanto navegando desde M09 como por el deep link de una alarma
 * disparada de verdad; `alarma.alSonar` es `null` fuera de «a-entrega», así que el destacado de tráfico es opcional.
 */
@Composable
fun M10AlarmaSonandoScreen(alarma: Alarma, alYaVoy: () -> Unit, alPosponer: () -> Unit, alVerRuta: (() -> Unit)?, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Tinta).testTag("pantalla-M10")) {
        BandaTextura(sobreTinta = true)
        ColumnaDesplazable(
            Modifier.fillMaxSize().weight(1f),
            relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            Destello(tamano = Medidas.Sello, color = Colores.Blanco)
            Text(alarma.titulo, style = Tipografia.H2, color = Colores.Blanco, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Row {
                Text(FormatoHora.hora(alarma.suena), style = Tipografia.HoraProtagonista, color = Colores.AmarilloEnergia)
                Text(FormatoHora.sufijo(alarma.suena), style = Tipografia.HoraProtagonistaSufijo, color = Colores.AmarilloEnergia)
            }
            alarma.alSonar?.let { alSonar ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
                    Text("Sal en ${alSonar.salEnMin} min · tráfico ${alSonar.traficoActual}", style = Tipografia.Cuerpo, color = Colores.Blanco, textAlign = TextAlign.Center)
                    if (alSonar.rutaDisponible && alVerRuta != null) BotonEnlace("Ver ruta ›", onClick = alVerRuta, color = ColorEnlace.Blanco)
                }
            }
        }
        Column(
            Modifier.fillMaxWidth().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBotonesDialogo),
        ) {
            BotonPrimario("Ya voy · ver ruta", onClick = alYaVoy, sobreTinta = true, alto = Tamanos.BotonAlarma, modifier = Modifier.testTag("ya-voy"))
            BotonSecundario("Posponer 10 min", onClick = alPosponer, sobreTinta = true, alto = Tamanos.BotonAlarma, modifier = Modifier.testTag("posponer"))
        }
    }
}
```

- [ ] **Step 4: Registrar la entrada en `EntradasApp.kt`**

Agregar el import `M10AlarmaSonandoScreen`; dentro de `entradasApp`, después de la entrada de `Pantalla.M09`:

```kotlin
    entry<Pantalla.M10> { clave ->
        val context = LocalContext.current
        val alarma = repositorio.alarma(clave.id)
        if (alarma == null) {
            LaunchedEffect(clave) { pila.reemplazarTodo(Pantalla.M02) }
        } else {
            val verRuta: (() -> Unit)? = if (alarma.alSonar?.rutaDisponible == true && alarma.lugar != null) {
                { runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(alarma.lugar)))) } }
            } else null
            M10AlarmaSonandoScreen(
                alarma = alarma,
                alYaVoy = { pila.reemplazarTodo(Pantalla.M02) },
                alPosponer = { pila.reemplazarTodo(Pantalla.M02) },
                alVerRuta = verRuta,
            )
        }
    }
```

- [ ] **Step 5: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m10.M10AlarmaSonandoScreenTest"`
Expected: PASS.

- [ ] **Step 6: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 7: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m10 \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m10
git commit -m "M10: alarma sonando, destino real de la notificación de pantalla completa"
```

---

## Task 9: M11 · Ajustes + M11d

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m11/M11AjustesScreen.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m11/M11AjustesViewModel.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m11/M11AjustesScreenTest.kt`

**Interfaces:**
- Consumes: `Usuario.ajustes` (`respetarNoMolestar`, `confirmarAntesDeAutoAjustar`, `permisos.alarmasExactas/notificaciones/bateriaSinRestricciones`, `calendariosVinculados`), `FilaAjuste`, `Interruptor`, `DialogoConfirmacion`, `dataset.mensajes.confirmarCerrarSesion*`.
- Produces: `M11AjustesScreen(estado, mensajes, alCambiarNoMolestar, alCambiarConfirmarAutoAjustar, alAbrirDialogo, alConservar, alCerrarSesion, modifier)`, `M11AjustesViewModel(repositorio)`.

- [ ] **Step 1: Escribir la prueba que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m11/M11AjustesScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m11

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M11AjustesScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    private fun montar(): M11AjustesViewModel {
        val vm = M11AjustesViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme {
                M11AjustesScreen(
                    estado = estado, mensajes = repo.dataset.mensajes,
                    alCambiarNoMolestar = vm::cambiarNoMolestar, alCambiarConfirmarAutoAjustar = vm::cambiarConfirmarAutoAjustar,
                    alAbrirDialogo = vm::abrirDialogo, alConservar = vm::cerrarDialogo, alCerrarSesion = vm::cerrarDialogo,
                )
            }
        }
        return vm
    }

    @Test
    fun `muestra los permisos del dataset y abre el dialogo de cerrar sesion`() {
        montar()
        regla.onNodeWithTag("pantalla-M11").assertIsDisplayed()
        regla.onNodeWithText("Concedido").assertIsDisplayed()   // alarmasExactas y notificaciones: true en dataset.json
        regla.onNodeWithText("Falta").assertIsDisplayed()       // bateriaSinRestricciones: false
        regla.onNodeWithTag("cerrar-sesion").performClick()
        regla.onNodeWithText("¿Cerrar sesión?").assertIsDisplayed()
    }
}
```

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m11.M11AjustesScreenTest"`
Expected: FAIL — `M11AjustesScreen`/`M11AjustesViewModel` no existen.

- [ ] **Step 3: Crear el ViewModel**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m11

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.datos.Ajustes
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoAjustes(val ajustes: Ajustes, val dialogoAbierto: Boolean = false)

/** F-M11: los ajustes viven en memoria (mismo alcance que el resto del dataset simulado); «Cerrar sesión» abre M11d. */
class M11AjustesViewModel(repositorio: RepositorioDataset) : ViewModel() {
    private val _estado = MutableStateFlow(EstadoAjustes(repositorio.dataset.usuario.ajustes))
    val estado: StateFlow<EstadoAjustes> = _estado.asStateFlow()

    fun cambiarNoMolestar(valor: Boolean) = _estado.update { it.copy(ajustes = it.ajustes.copy(respetarNoMolestar = valor)) }
    fun cambiarConfirmarAutoAjustar(valor: Boolean) = _estado.update { it.copy(ajustes = it.ajustes.copy(confirmarAntesDeAutoAjustar = valor)) }
    fun abrirDialogo() = _estado.update { it.copy(dialogoAbierto = true) }
    fun cerrarDialogo() = _estado.update { it.copy(dialogoAbierto = false) }
}
```

- [ ] **Step 4: Crear la pantalla**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m11

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.ColumnaDesplazable
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.FilaAjuste
import co.edu.uniandes.alarmasqr.ui.componentes.Interruptor
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M11 · Ajustes (F-M11): secciones de 16 de relleno superior con filas de 40 (revisión de tutores, MOCKUPS.md §7 paso 8); «Cerrar sesión» abre M11d. */
@Composable
fun M11AjustesScreen(
    estado: EstadoAjustes, mensajes: Mensajes,
    alCambiarNoMolestar: (Boolean) -> Unit, alCambiarConfirmarAutoAjustar: (Boolean) -> Unit,
    alAbrirDialogo: () -> Unit, alConservar: () -> Unit, alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M11")) {
        BarraSuperior("Ajustes")
        ColumnaDesplazable(Modifier.fillMaxSize(), relleno = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton)) {
            SeccionAjustes("NOTIFICACIONES") {
                FilaAjuste("No molestar") { Interruptor(estado.ajustes.respetarNoMolestar, alCambiarNoMolestar) }
                FilaAjuste("Confirmar antes de auto-ajustar") { Interruptor(estado.ajustes.confirmarAntesDeAutoAjustar, alCambiarConfirmarAutoAjustar) }
            }
            SeccionAjustes("PERMISOS") {
                FilaAjuste("Alarmas exactas") { EstadoPermiso(estado.ajustes.permisos.alarmasExactas) }
                FilaAjuste("Notificaciones") { EstadoPermiso(estado.ajustes.permisos.notificaciones) }
                FilaAjuste("Batería sin restricciones") { EstadoPermiso(estado.ajustes.permisos.bateriaSinRestricciones) }
            }
            SeccionAjustes("CALENDARIOS") {
                FilaAjuste("Vinculados: ${estado.ajustes.calendariosVinculados.joinToString()}") { }
            }
            SeccionAjustes("CUENTA") {
                FilaAjuste("Cerrar sesión", alTocarFila = alAbrirDialogo, modifier = Modifier.testTag("cerrar-sesion")) { }
            }
        }
    }
    if (estado.dialogoAbierto) {
        DialogoConfirmacion(
            titulo = mensajes.confirmarCerrarSesionTitulo, cuerpo = mensajes.confirmarCerrarSesionCuerpo,
            rotuloSeguro = mensajes.confirmarCerrarSesionSeguro, rotuloConfirmar = mensajes.confirmarCerrarSesionAccion,
            destructivo = false, alSeguro = alConservar, alConfirmar = alCerrarSesion,
        )
    }
}

@Composable
private fun SeccionAjustes(titulo: String, modifier: Modifier = Modifier, contenido: @Composable ColumnScope.() -> Unit) {
    Column(modifier.fillMaxWidth().padding(top = Espacio.EntreBloques), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
        Text(titulo, style = Tipografia.H3, color = Colores.GrisTexto)
        contenido()
    }
}

@Composable
private fun EstadoPermiso(concedido: Boolean, modifier: Modifier = Modifier) {
    Text(if (concedido) "Concedido" else "Falta", style = Tipografia.Etiqueta, color = if (concedido) Colores.VerdeTexto else Colores.CoralTexto, modifier = modifier)
}
```

- [ ] **Step 5: Registrar la entrada en `EntradasApp.kt`**

Agregar los imports `M11AjustesScreen` y `M11AjustesViewModel`; dentro de `entradasApp`, después de la entrada de `Pantalla.M10`:

```kotlin
    entry<Pantalla.M11> {
        val vm = viewModel { M11AjustesViewModel(repositorio) }
        val estado by vm.estado.collectAsStateWithLifecycle()
        M11AjustesScreen(
            estado = estado, mensajes = repositorio.dataset.mensajes,
            alCambiarNoMolestar = vm::cambiarNoMolestar, alCambiarConfirmarAutoAjustar = vm::cambiarConfirmarAutoAjustar,
            alAbrirDialogo = vm::abrirDialogo, alConservar = vm::cerrarDialogo,
            alCerrarSesion = { vm.cerrarDialogo(); pila.reemplazarTodo(Pantalla.M01) },
        )
    }
```

- [ ] **Step 6: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.m11.M11AjustesScreenTest"`
Expected: PASS.

- [ ] **Step 7: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m11 \
        apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m11
git commit -m "M11: ajustes por sección, permisos y diálogo M11d de cerrar sesión"
```

---

## Task 10: Flujo T2/T4 de extremo a extremo y regla de scroll vertical

**Files:**
- Create: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/FlujosPersonaBTest.kt`
- Modify: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/PantallasDesplazablesTest.kt`

**Interfaces:**
- Consumes: `entradasApp`, `NavegacionApp`, `rememberBackStackApp`, todas las entradas de las Tareas 3–9.

- [ ] **Step 1: Escribir la prueba de flujo que falla**

Crear `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/FlujosPersonaBTest.kt`, mismo patrón que `FlujosPersonaATest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Flujos T2 y T4 de TRAZABILIDAD.md §4 sobre las entradas reales (entradasApp), navegando por código. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FlujosPersonaBTest {
    @get:Rule val regla = createComposeRule()
    private val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    private val repositorio = RepositorioDataset.desdeAssets(app)
    private lateinit var pila: NavBackStack<NavKey>

    @Before
    fun preparar() { repositorio.reiniciar() }

    private fun montar(inicio: Pantalla) = regla.setContent {
        pila = rememberBackStackApp(inicio)
        AlarmasQRTheme { NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) } }
    }

    @Test
    fun `T2 cambiar anticipacion y sonido M05 - M06`() {
        // T1 ya corrió en otra prueba: aquí se entra directo a M06 sobre una alarma existente («a-gimnasio»).
        montar(Pantalla.M06("a-gimnasio"))
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        regla.onNodeWithText("1 h").performClick()
        regla.onNodeWithTag("guardar").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(60, repositorio.alarma("a-gimnasio")?.anticipacionMin)
    }

    @Test
    fun `T4 reaccionar a un cambio de hora M06 - M09 - M10 - M02`() {
        // «a-entrega» solo existe en el repositorio después de T1 (escanear su QR); se simula agregándola primero.
        repositorio.agregarDesdeEvento("e-entrega")
        montar(Pantalla.M06("a-entrega"))
        regla.onNodeWithTag("pantalla-M06").assertIsDisplayed()
        regla.onNodeWithText("Alarma conectada · Confirmar antes de auto-ajustarse").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M09").assertIsDisplayed()
        regla.onNodeWithTag("aceptar").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M10").assertIsDisplayed()
        regla.onNodeWithTag("ya-voy").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals("2026-08-30T17:30:00-05:00", repositorio.alarma("a-entrega")?.eventoInicio)
    }
}
```

- [ ] **Step 2: Ejecutar la prueba y verificar que falla**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.navegacion.FlujosPersonaBTest"`
Expected: FAIL si alguna entrada de las Tareas 3–9 quedó mal cableada (navegación equivocada, testTag distinto); si todas las tareas anteriores están completas y probadas, puede pasar directo — igual ejecutar para confirmar el flujo de extremo a extremo, no solo cada pantalla aislada.

- [ ] **Step 3: Corregir cualquier cableado de navegación que la prueba señale**

Revisar contra las Tareas 3–9: `alTocarCambioOrganizador` de M06 debe llevar a `Pantalla.M09(id)`; «Aceptar cambio» de M09 debe llevar a `Pantalla.M10(id)` después de `aplicarCambioOrganizador`; «Ya voy» de M10 debe llevar a `Pantalla.M02`.

- [ ] **Step 4: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.navegacion.FlujosPersonaBTest"`
Expected: PASS (2 pruebas).

- [ ] **Step 5: Agregar M02b y M11 a `PantallasDesplazablesTest`**

En `PantallasDesplazablesTest.kt`, agregar los imports `co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioScreen`, `co.edu.uniandes.alarmasqr.ui.pantallas.m02.M02bCalendarioViewModel`, `co.edu.uniandes.alarmasqr.ui.pantallas.m11.M11AjustesScreen`, `co.edu.uniandes.alarmasqr.ui.pantallas.m11.M11AjustesViewModel`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, y dos pruebas nuevas al final de la clase:

```kotlin
    @Test
    fun `M02b alcanza la ultima alarma del dia seleccionado en un telefono bajo`() {
        val vm = M02bCalendarioViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme { M02bCalendarioScreen(estado, alSeleccionarDia = vm::seleccionarDia, alTocarAlarma = {}, alCambiarActiva = vm::cambiarActiva) }
        }
        alcanzarEtiqueta("alarma-a-tutor")
    }

    @Test
    fun `M11 alcanza la fila Cerrar sesion en un telefono bajo`() {
        val vm = M11AjustesViewModel(repo)
        regla.setContent {
            val estado by vm.estado.collectAsStateWithLifecycle()
            AlarmasQRTheme {
                M11AjustesScreen(
                    estado = estado, mensajes = repo.dataset.mensajes,
                    alCambiarNoMolestar = {}, alCambiarConfirmarAutoAjustar = {}, alAbrirDialogo = {}, alConservar = {}, alCerrarSesion = {},
                )
            }
        }
        alcanzarEtiqueta("cerrar-sesion")
    }
```

- [ ] **Step 6: Ejecutar la prueba y verificar que pasa**

Run: `cd apps/movil && ./gradlew testDebugUnitTest --tests "co.edu.uniandes.alarmasqr.ui.pantallas.PantallasDesplazablesTest"`
Expected: PASS (7 + 2 = 9 pruebas).

- [ ] **Step 7: Ejecutar toda la suite y lint**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug`
Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/FlujosPersonaBTest.kt \
        apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/PantallasDesplazablesTest.kt
git commit -m "M10: flujo T2/T4 de extremo a extremo; M02b y M11 en la regla de scroll vertical"
```

---

## Task 11: Verificación pixel-perfect

**Files:**
- Modify: `docs/verificacion/README.md`
- No se crean archivos de prueba nuevos (usa `capturar()` dentro de las pruebas ya escritas en las Tareas 3–9).

**Interfaces:**
- Consumes: `ui.capturar(nombre: String)` (ya existe, Task de Verificacion.kt del Plan 2), exportación de los marcos de Figma listados en `docs/MOCKUPS.md` §5 (ids de M02b, M06, M06d, M07…M11, M11d).

- [ ] **Step 1: Agregar `regla.capturar(...)` a una prueba de cada pantalla**

En cada prueba de las Tareas 3, 4, 7, 8, 9 (`M02bCalendarioScreenTest`, `M06EditarAlarmaScreenTest`, `M09CambioEventoScreenTest`, `M10AlarmaSonandoScreenTest`, `M11AjustesScreenTest`) y en `FlujosPersonaBTest` para M07/M08 (pantallas alcanzadas solo por flujo), agregar una llamada `regla.capturar("M0X")` (y `regla.capturar("M06d")` con el diálogo abierto) en el punto donde la pantalla ya está completamente compuesta, siguiendo el mismo patrón que `FlujosPersonaATest.kt` usa para M04/M04d. Import: `co.edu.uniandes.alarmasqr.ui.capturar`.

- [ ] **Step 2: Ejecutar toda la suite para generar las capturas**

Run: `cd apps/movil && ./gradlew testDebugUnitTest`
Expected: PASS; genera `apps/movil/app/build/verificacion/M02b.png`, `M06.png`, `M06d.png`, `M07.png`, `M08.png`, `M09.png`, `M10.png`, `M11.png`, `M11d.png`.

- [ ] **Step 3: Exportar los marcos de Figma y comparar**

Para cada código, exportar el marco de `docs/MOCKUPS.md` §5 (ids de M02b, M06, M06d, M07…M11, M11d) a 390×844 y guardarlo como `docs/verificacion/<código>-figma.png`; copiar la captura generada como `docs/verificacion/<código>.png`. Comparar a ojo contra la lista de comprobación de `docs/PLAN_MAQUETACION.md` §7 (un solo amarillo, 52/56, radios 14/20, tonos AA, horas 12 h, color + forma, diálogo de confirmación).

- [ ] **Step 4: Documentar cada pantalla en `docs/verificacion/README.md`**

Agregar una fila por pantalla a la tabla existente (mismo formato que las filas de M01–M13 ya documentadas), con el id del marco de Figma y las diferencias aceptadas o corregidas encontradas durante la comparación (p. ej. partición de línea por métrica de fuente de Robolectric, igual que las notas ya existentes de M01/M00a/M03/M03b).

- [ ] **Step 5: Ejecutar toda la suite, lint y build de release**

Run: `cd apps/movil && ./gradlew testDebugUnitTest lintDebug assembleRelease`
Expected: PASS — build de release genera `app/build/outputs/apk/release/app-release.apk` sin errores.

- [ ] **Step 6: Commit**

```bash
git add apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr docs/verificacion/README.md
git commit -m "M11: verificación pixel-perfect de M02b, M06-M11 y sus diálogos"
```

---

## Cierre

Al terminar la Tarea 11: revisión final de toda la rama (`superpowers:subagent-driven-development` la dispara automáticamente tras la última tarea), y luego `superpowers:finishing-a-development-branch` para decidir merge local / PR / dejar la rama como está — igual que se hizo con el Plan 5. Antes de esa decisión, recordar el punto pendiente de `PLAN_MAQUETACION.md` §7 que este plan no puede cerrar: la prueba manual de la alarma real en un dispositivo físico (sigue sin marcar en la lista de verificación).
