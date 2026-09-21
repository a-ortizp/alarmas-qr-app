# Plan 2 · Móvil de la Persona A — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Construir en `apps/movil` los componentes L03–L08 y las doce pantallas de la Persona A (M01, M00a, M00b, M02v, M02, M02h, M03, M03b, M04 + M04d, M05, M12, M13), pixel-perfect contra Figma, con la cámara real (CameraX + ML Kit) y la alarma real (`AlarmManager` + notificación de pantalla completa), verificadas con pruebas JVM y capturas.

**Architecture:** Cada pantalla es un composable `MxxNombreScreen` sin estado propio (recibe estado y callbacks) más, cuando hay estado que sobrevive a recomposiciones, un `ViewModel` con `StateFlow` (M02/M05, M03, M04). Las entradas reales se registran en `navegacion/EntradasApp.kt` (`entradasApp`) que `MainActivity` y las pruebas de flujo pasan a `NavegacionApp`; el marcador de la Fase 0 sigue siendo el `fallback` para las pantallas de la Persona B. Los componentes viven en `ui/componentes` y solo usan `Tokens.kt`; las medidas de Figma que no existían como token se agregan primero a `packages/tokens/design-tokens.json` (v1.10) y a sus derivados. La cámara usa `LifecycleCameraController` + `MlKitAnalyzer`; la alarma usa `AlarmManager.setExactAndAllowWhileIdle` y un `BroadcastReceiver` que publica una notificación de pantalla completa cuyo intent abre `alarma/{id}/sonando` (marcador M10 de la Persona B).

**Tech Stack:** Kotlin 2.2.21 · AGP 8.13.2 · Compose BOM 2026.06.00 (Material 3) · Navigation 3 1.1.7 · lifecycle 2.10.0 · CameraX 1.6.2 (+ `camera-mlkit-vision`) · ML Kit barcode 17.3.0 · ZXing core 3.5.4 · JUnit 4 + Robolectric 4.16 + `compose-ui-test-junit4`.

**Spec:** `docs/superpowers/specs/2026-09-20-maquetacion-persona-a-design.md` (§2 móvil, §4 componentes, §5 método pixel-perfect, §6 pruebas). **Medidas:** `docs/superpowers/specs/2026-09-20-medidas-figma-movil-persona-a.md` (anexo con los 13 marcos medidos en Figma; cada tarea cita su sección).

## Global Constraints

- Versiones fijas de `apps/movil/gradle/libs.versions.toml`: `agp = 8.13.2`, `kotlin = 2.2.21`, `composeBom = 2026.06.00`, `navigation3 = 1.1.7`, `lifecycle = 2.10.0`, `camerax = 1.6.2`, `mlkitBarcode = 17.3.0`, `zxing = 3.5.4`, `robolectric = 4.16`; `compileSdk = targetSdk = 36`, `minSdk = 26`, JDK 17. No subir ni bajar ninguna sin actualizar el README (§«Frameworks y versiones»).
- Nunca escribir colores, tamaños ni radios a mano: solo `Colores`, `Fuentes`, `Tipografia`, `Tamanos`, `Medidas`, `Radios`, `Trazos`, `Espacio`, `Elevaciones`, `Movimiento` de `ui/theme/Tokens.kt`. Una medida nueva se agrega primero a `packages/tokens/design-tokens.json` (v1.10), luego a `packages/tokens/Tokens.kt` y `apps/movil/.../Tokens.kt` (Tarea 1); si la web la necesita, también a `packages/tokens/tokens.css` **y** `apps/web/src/tokens.css` (CI compara ambos con `cmp`).
- Un solo elemento amarillo por pantalla (acción principal o FAB). Estados activos en Tinta; la píldora activa de la barra inferior va en Gris Niebla (mockups y DS §6). Sobre Tinta el primario es blanco.
- Alturas: botones 52, acciones de la alarma 56, campos 48, área táctil mínima 48, enlaces 32, flecha «‹» 44×44, chip de estado 20, chip que se toca 32, barra superior 56, barra inferior 64, FAB 56.
- Radios: 14 tarjetas/hojas de contenido, 24 arriba en la hoja inferior, 20 diálogo de confirmación, 12 campos y snackbar, píldora en botones y chips.
- Texto ≤ 15 sp en tonos AA (`CoralTexto`, `VerdeTexto`, `AzulTexto`, `GrisTexto`); nada por debajo de 12 salvo la barra inferior (11). Sobre Tinta el texto secundario va en `GrisBorde`.
- Horas en 12 h con Spline Sans Mono y dígitos tabulares; el sufijo am/pm en Medium más pequeño en la misma línea (`Tipografia.HoraAmPm` en tarjetas, `Tipografia.HoraProtagonistaSufijo` en el bloque «Sonará»).
- Ninguna acción irreversible en un toque: eliminar en M04 abre `DialogoConfirmacion` (velo Tinta 55 %, «Conservar» primario amarillo, «Eliminar» contorno Coral Texto, tocar el velo = «Conservar»).
- Textura de módulos QR solo como banda de 120 dp en M01, M04, M12 y M13 (M09/M10 son de la Persona B). Todo vector: nada se pinta con imágenes.
- Todo el texto visible en español y **exactamente** el de las hojas de medidas; identificadores en inglés o español, nombres de pantalla con su código (`M04AlarmaCreadaScreen`). Archivos de pantalla con el código como prefijo en `ui/pantallas/<código en minúsculas>/`.
- Cada pantalla real lleva `Modifier.testTag("pantalla-<código>")` en su raíz (misma convención que el marcador), para que las pruebas de flujo naveguen por código.
- Commits con el código de pantalla al inicio («M04: hoja de alarma programada»); un commit por tarea como mínimo. Rama de trabajo: `feature/plan2-movil-persona-a` (ver «Decisiones» D1).
- Comandos de verificación (desde `apps/movil`): `./gradlew testDebugUnitTest --tests '<clase>'` por tarea; al cerrar cada tarea `./gradlew lintDebug testDebugUnitTest assembleDebug --no-daemon` debe quedar en verde. Robolectric corre con `@Config(sdk = [35])` y `@GraphicsMode(NATIVE)` (tope de Robolectric 4.16).
- `dataset.json` no se modifica (es idéntico en móvil y web y CI lo compara). Los campos nuevos que el móvil lee ya existen en el JSON (`organizador`, `detalle`, `esNueva`, `etiquetaEvento`, `verificado`).

## Decisiones tomadas al escribir el plan (revisar si se discrepa)

- **D1 · Rama:** todo el Plan 2 va en una sola rama `feature/plan2-movil-persona-a` con commits prefijados por pantalla, y un PR revisado por la Persona B. CLAUDE.md pide «una rama por pantalla»; las pantallas comparten componentes y navegación, y 12 ramas encadenadas costarían más de lo que aportan. Si el equipo prefiere ramas por pantalla, cada tarea de pantalla (5–13) puede ir en su propia rama a partir de la anterior.
- **D2 · Capturas de verificación:** en vez del plugin `com.android.compose.screenshot` (spec §5.2), las capturas se hacen con Robolectric (`captureToImage()` en modo gráfico nativo, ya activo en las pruebas de la Fase 0) y se guardan en `app/build/verificacion/<código>.png`; la pareja con la exportación de Figma se copia a `docs/verificacion/`. Motivo: cero dependencias nuevas y el mismo runner que las pruebas de navegación.
- **D3 · Alarma «Entrega de proyecto UX»:** el repositorio arranca con las 5 alarmas cuyo `esNueva` es falso; `a-entrega` (`esNueva: true`) queda «pendiente de escaneo» y solo aparece cuando el flujo T1 la crea desde `e-entrega` (`agregarDesdeEvento`). Así M02 muestra las 4+1 tarjetas del mockup y M05 la nueva resaltada, sin tocar el dataset.
- **D4 · Hora real de la alarma:** `ProgramadorAlarmas.programar(alarma)` usa `alarma.suena` si está en el futuro; si ya pasó (el dataset vive en agosto de 2026), programa **ahora + 1 minuto** para que la alarma sea demostrable en un dispositivo real con la app cerrada (PLAN §7). La decisión se documenta en el README.
- **D5 · M12 «Abrir ajustes»:** lanza la solicitud real del permiso `CAMERA` (`ActivityResultContracts.RequestPermission`); si el sistema ya no muestra el diálogo (denegado dos veces), abre los ajustes de la app. Al volver, con o sin permiso, navega a M03 (⏩ NAVEGACION §6 paso 3). M03 sin permiso muestra el visor apagado y conserva los toques simulados.
- **D6 · Píldora activa de la barra inferior:** Gris Niebla con icono y rótulo en Tinta (mockups M02/M02v y DS §6 «6 píldoras de pestaña activa a Gris Niebla»); se corrige el `NavigationBar` de la Fase 0, que la pintaba en Tinta. CLAUDE.md generaliza «estados activos en Tinta» para switch, chip «Nueva» y píldoras de filtro; la barra inferior es la excepción documentada en el DS.
- **D7 · Sufijo del bloque «Sonará» (M04):** el mockup dibuja «3:15 pm» en un solo tramo de 52; CLAUDE.md y el DS piden el sufijo en Medium más pequeño. Se sigue la regla (`HoraSonara` 52 + `HoraProtagonistaSufijo` 28 Medium). Anotar en el repo de UX.
- **D8 · Texto secundario sobre Tinta (M03 «vibra al detectar el código»):** el mockup usa `#B9B7BF`; el DS y los tokens dicen Gris Borde. Se usa `Colores.GrisBorde` (sin token nuevo).
- **D9 · ViewModels:** solo donde hay estado que muta (M02/M02v/M05 comparten `M02InicioViewModel`; `M03EscanerViewModel`; `M04AlarmaCreadaViewModel`). M01, M00a, M00b, M02h, M03b, M12 y M13 son composables de estado local (`rememberSaveable`) con callbacks; la spec §2.1 los admite como «estado de UI inmutable» sin exigir un ViewModel vacío.
- **D10 · Altura de campos:** el mockup mide 45; el token `Tamanos.Campo` es 48 (DS comp. 06). Manda el token.

## Mapa de archivos

```
packages/tokens/design-tokens.json                      modificar · v1.10: color.coral.suave, radius.*, typography.*, size.movil.*
packages/tokens/tokens.css · apps/web/src/tokens.css    modificar · --color-coral-suave (ambos idénticos)
packages/tokens/Tokens.kt                               modificar · mismas adiciones que el derivado móvil
apps/movil/gradle/libs.versions.toml                    modificar · androidx-camera-mlkit-vision
apps/movil/app/build.gradle.kts                         modificar · dependencia camera-mlkit-vision
apps/movil/app/src/main/AndroidManifest.xml             modificar · launchMode, ReceptorAlarma
apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/
  MainActivity.kt                                       modificar · entradasApp, intent SEND, deep link de la alarma
  datos/Modelos.kt                                      modificar · Organizador, campos nuevos de Alarma y EventoQR
  datos/FormatoHora.kt                                  crear · horas 12 h, agrupadores de día, fechas largas y cortas
  datos/RepositorioDataset.kt                           modificar · pendientes de escaneo, agregarDesdeEvento, mensajeEliminar
  ui/theme/Tokens.kt                                    modificar · tokens nuevos (v1.10)
  ui/iconos/Iconos.kt                                   crear · ImageVector de línea 24 (escanear, galería, más, alarma, calendario, ajustes, google, outlook, teléfono)
  ui/componentes/Botones.kt                             crear · BotonPrimario, BotonSecundario, BotonEnlace, BotonAtras
  ui/componentes/Campos.kt                              crear · CampoTexto, Casilla
  ui/componentes/Chips.kt                               crear · ChipEstado, ChipControl
  ui/componentes/FilaOpcionCalendario.kt                crear · fila de M01 (icono 20 + rótulo + casilla)
  ui/componentes/BarraSuperior.kt                       crear · barra 56 con «‹», título y espaciador flexible
  ui/componentes/NavegacionInferior.kt                  crear · barra 64, píldoras 40×22, iconos 20
  ui/componentes/FabEscanear.kt                         crear · FAB extendido con toque y mantener 500 ms
  ui/componentes/Interruptor.kt                         crear · switch 44×26 Tinta / contorno
  ui/componentes/TarjetaAlarma.kt                       crear · tarjeta 350×75 + AgrupadorDia
  ui/componentes/TarjetaEvento.kt                       crear · tarjeta de M04 con SelloVerificado
  ui/componentes/BandaTextura.kt                        crear · Canvas de módulos QR 120 dp
  ui/componentes/CodigoQR.kt                            crear · ZXing → ImageBitmap + DianaQR (marca)
  ui/componentes/SnackbarDeshacer.kt                    crear · snackbar Tinta 48 con «Deshacer · 5 s»
  ui/componentes/DialogoConfirmacion.kt                 crear · DS comp. 47
  ui/componentes/Divisor.kt                             crear · «o continúa con» / «mientras tanto»
  navegacion/NavegacionApp.kt                           modificar · NavegacionInferior propia, FAB, decorador de ViewModel
  navegacion/EntradasApp.kt                             crear · entradasApp(): registro de las 12 pantallas reales
  navegacion/PermisoCamara.kt                           crear · rememberSolicitudPermisoCamara (D5)
  ui/pantallas/m01/M01BienvenidaScreen.kt               crear
  ui/pantallas/m00/M00aRegistroScreen.kt                crear
  ui/pantallas/m00/M00bEntrarScreen.kt                  crear
  ui/pantallas/m00/FormularioAcceso.kt                  crear · bloque compartido (logotipo, campos, Google/Outlook, pie)
  ui/pantallas/m02/M02InicioScreen.kt                   crear · M02, M02v (EstadoVacio) y M05 (snackbar)
  ui/pantallas/m02/M02InicioViewModel.kt                crear
  ui/pantallas/m02/M02hAgregarEventoSheet.kt            crear · contenido de la hoja
  ui/pantallas/m12/M12PermisoCamaraScreen.kt            crear
  ui/pantallas/m13/M13QRInvalidoScreen.kt               crear
  ui/pantallas/m03/M03EscanerScreen.kt                  crear
  ui/pantallas/m03/M03EscanerViewModel.kt               crear
  ui/pantallas/m03/M03bPantallazoScreen.kt              crear
  qr/AnalizadorQR.kt                                    crear · interpreta el contenido leído (EventoDetectado / QRInvalido)
  qr/VisorCamara.kt                                     crear · PreviewView + LifecycleCameraController + MlKitAnalyzer
  alarma/ProgramadorAlarmas.kt                          crear · AlarmManager
  alarma/ReceptorAlarma.kt                              crear · BroadcastReceiver + notificación de pantalla completa
  alarma/NotificacionesAlarma.kt                        crear · canal «Alarmas», permiso POST_NOTIFICATIONS
  ui/pantallas/m04/M04AlarmaCreadaSheet.kt              crear · hoja + M04d
  ui/pantallas/m04/M04AlarmaCreadaViewModel.kt          crear
apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/
  datos/FormatoHoraTest.kt · datos/RepositorioDatasetTest.kt (modificar)
  ui/componentes/ComponentesTest.kt · ui/componentes/FabEscanearTest.kt · ui/componentes/DialogoConfirmacionTest.kt
  ui/Verificacion.kt (helper de captura) · ui/pantallas/<código>/<Código>ScreenTest.kt por pantalla
  qr/AnalizadorQRTest.kt · alarma/ProgramadorAlarmasTest.kt
  navegacion/FlujosPersonaATest.kt (T1, T6, T7) · navegacion/NavegacionAppTest.kt (modificar)
docs/verificacion/README.md + <código>.png                crear · parejas Figma / implementación
README.md · CLAUDE.md · docs/PLAN_MAQUETACION.md          modificar · estado, decisiones D1–D10, cómo probar la alarma
```

---

### Task 1: Tokens v1.10, modelos completos, `FormatoHora` y alarmas pendientes de escaneo

**Files:**
- Modify: `packages/tokens/design-tokens.json`
- Modify: `packages/tokens/tokens.css`, `apps/web/src/tokens.css`
- Modify: `packages/tokens/Tokens.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/theme/Tokens.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/Modelos.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/FormatoHora.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/RepositorioDataset.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/FormatoHoraTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/RepositorioDatasetTest.kt`

**Interfaces:**
- Consumes: `Dataset`, `Alarma`, `EventoQR`, `RepositorioDataset` de la Fase 0.
- Produces:
  - `Colores.CoralSuave`, `Colores.BordeSobreTinta`; `Tipografia.FlechaAtras`, `Tipografia.TituloEvento`, `Tipografia.Opcion`, `Tipografia.ChipControl`; `Radios.Visor`, `Radios.VisorBienvenida`, `Radios.MarcoLectura`, `Radios.Casilla`, `Radios.Snackbar`, `Radios.CajaIcono`; `object Medidas` (ver código); `Espacio.PaddingBarra`, `Espacio.PaddingNavegacion`, `Espacio.HojaSuperior`, `Espacio.HojaInferior`, `Espacio.PieEnlace`, `Espacio.GapFila`, `Espacio.GapHoja`, `Espacio.GapDialogo`, `Espacio.GapTarjeta`; `object Elevaciones { Fab, Tarjeta }`.
  - `data class Organizador(val nombre: String, val verificado: Boolean)`; `Alarma.esNueva`, `Alarma.etiquetaEvento`, `Alarma.organizador`, `Alarma.detalle`; `EventoQR.organizador`, `EventoQR.verificado`.
  - `object FormatoHora { fun hora(iso): String; fun sufijo(iso): String; fun horaConSufijo(iso): String; fun etiquetaDia(iso, hoy: LocalDate): String; fun fechaLarga(iso): String; fun fechaCorta(iso): String; fun lineaEvento(alarma: Alarma): String; fun dia(iso): LocalDate }`.
  - `RepositorioDataset.hoy: LocalDate`, `RepositorioDataset.agregarDesdeEvento(eventoId: String): Alarma?`, `RepositorioDataset.mensajeEliminar(alarma: Alarma): String`, `RepositorioDataset.pendientesDeEscaneo: List<Alarma>`.

- [ ] **Step 1: Crear la rama**

```bash
git checkout main && git pull && git checkout -b feature/plan2-movil-persona-a
```

- [ ] **Step 2: Escribir las pruebas de `FormatoHora` (fallan: la clase no existe)**

`apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/FormatoHoraTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.datos

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class FormatoHoraTest {
    private val hoy = LocalDate.of(2026, 8, 27)
    private val tutor = Alarma(id = "a-tutor", titulo = "Reunión con el tutor", eventoInicio = "2026-08-27T08:00:00-05:00", suena = "2026-08-27T07:30:00-05:00", lugar = "Aula SD-703", origen = "creada-por-mi", estado = "activa", anticipacionMin = 30, trayectoMin = 0)

    @Test
    fun `hora en 12 h con sufijo en minusculas`() {
        assertEquals("7:30", FormatoHora.hora("2026-08-27T07:30:00-05:00"))
        assertEquals("am", FormatoHora.sufijo("2026-08-27T07:30:00-05:00"))
        assertEquals("3:15 pm", FormatoHora.horaConSufijo("2026-08-30T15:15:00-05:00"))
        assertEquals("12:00 pm", FormatoHora.horaConSufijo("2026-08-31T12:00:00-05:00"))
    }

    @Test
    fun `agrupadores de dia como en los mockups`() {
        assertEquals("HOY · JUEVES 27", FormatoHora.etiquetaDia("2026-08-27T08:00:00-05:00", hoy))
        assertEquals("MAÑANA · VIERNES 28", FormatoHora.etiquetaDia("2026-08-28T08:15:00-05:00", hoy))
        assertEquals("DOMINGO 30", FormatoHora.etiquetaDia("2026-08-30T16:00:00-05:00", hoy))
        assertEquals("LUNES 31", FormatoHora.etiquetaDia("2026-08-31T13:00:00-05:00", hoy))
    }

    @Test
    fun `fechas larga y corta de M04 y M04d`() {
        assertEquals("Dom 30 de agosto · 4:00 pm (GMT-5)", FormatoHora.fechaLarga("2026-08-30T16:00:00-05:00"))
        assertEquals("dom 30", FormatoHora.fechaCorta("2026-08-30T16:00:00-05:00"))
    }

    @Test
    fun `linea de evento segun estado, etiqueta y lugar`() {
        assertEquals("evento 8:00 am · Aula SD-703", FormatoHora.lineaEvento(tutor))
        assertEquals("pausada · evento 9:30 am", FormatoHora.lineaEvento(tutor.copy(titulo = "Gimnasio", eventoInicio = "2026-08-27T09:30:00-05:00", lugar = null, estado = "pausada")))
        assertEquals("vuelo 8:15 am · Aeropuerto", FormatoHora.lineaEvento(tutor.copy(eventoInicio = "2026-08-28T08:15:00-05:00", lugar = "Aeropuerto", etiquetaEvento = "vuelo")))
    }
}
```

- [ ] **Step 3: Actualizar las pruebas del repositorio (fallan: 5 alarmas iniciales y `agregarDesdeEvento`)**

Reemplazar el contenido de `RepositorioDatasetTest.kt` por:

```kotlin
package co.edu.uniandes.alarmasqr.datos

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.time.LocalDate

class RepositorioDatasetTest {
    private val json = File("src/main/assets/dataset.json").readText()

    @Test
    fun `lee el dataset y deja las alarmas nuevas pendientes de escaneo`() {
        val repo = RepositorioDataset(json)
        assertEquals("Andrés Rojas", repo.dataset.usuario.nombre)
        assertEquals(LocalDate.of(2026, 8, 27), repo.hoy)
        assertEquals(listOf("a-tutor", "a-gimnasio", "a-vuelo", "a-semillero", "a-asado"), repo.alarmas.value.map { it.id })
        assertEquals(listOf("a-entrega"), repo.pendientesDeEscaneo.map { it.id })
        assertEquals("MISO · UniAndes", repo.dataset.alarmas.first { it.id == "a-entrega" }.organizador?.nombre)
        assertEquals(true, repo.evento("e-entrega")?.verificado)
        assertEquals("¿Eliminar alarma?", repo.dataset.mensajes.confirmarEliminarTitulo)
    }

    @Test
    fun `agregarDesdeEvento crea la alarma del evento, marcada como nueva y en orden`() {
        val repo = RepositorioDataset(json)
        val nueva = repo.agregarDesdeEvento("e-entrega")!!
        assertEquals("a-entrega", nueva.id)
        assertTrue(nueva.esNueva)
        assertEquals(listOf("Nueva", "✓ Escaneada"), nueva.chips)
        assertEquals(listOf("a-tutor", "a-gimnasio", "a-vuelo", "a-semillero", "a-entrega", "a-asado"), repo.alarmas.value.map { it.id })
        assertNull(repo.agregarDesdeEvento("e-no-existe"))
        repo.deshacer()
        assertNull(repo.alarma("a-entrega"))
    }

    @Test
    fun `eliminar y deshacer restauran la lista`() {
        val repo = RepositorioDataset(json)
        repo.eliminar("a-tutor")
        assertNull(repo.alarma("a-tutor"))
        assertEquals(4, repo.alarmas.value.size)
        repo.deshacer()
        assertEquals(5, repo.alarmas.value.size)
        assertEquals("a-tutor", repo.alarmas.value.first().id)
    }

    @Test
    fun `reiniciar vuelve a las 5 alarmas iniciales`() {
        val repo = RepositorioDataset(json)
        repo.agregarDesdeEvento("e-entrega")
        repo.permisoCamaraPedido = true
        repo.reiniciar()
        assertEquals(5, repo.alarmas.value.size)
        assertEquals(false, repo.permisoCamaraPedido)
    }

    @Test
    fun `mensajeEliminar rellena evento, fecha y hora`() {
        val repo = RepositorioDataset(json)
        val entrega = repo.dataset.alarmas.first { it.id == "a-entrega" }
        assertEquals(
            "Dejarás de recibir el aviso de “Entrega de proyecto UX” (dom 30 · 4:00 pm). Si cambias de opinión, puedes volver a escanear el QR del evento.",
            repo.mensajeEliminar(entrega),
        )
    }
}
```

- [ ] **Step 4: Correr las pruebas para verlas fallar**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests 'co.edu.uniandes.alarmasqr.datos.*' --no-daemon
```
Esperado: error de compilación («Unresolved reference: FormatoHora», «organizador», «agregarDesdeEvento»).

- [ ] **Step 5: Ampliar `design-tokens.json` a v1.10**

En `packages/tokens/design-tokens.json`:
1. `meta.version` → `"1.10"`, `meta.date` → `"2026-09-20"`, y agregar a `meta.notes`: `"v1.10 (2026-09-20): medidas móviles de la Persona A tomadas de Figma para el Plan 2 (coral suave, radios de visor/casilla/snackbar, flecha «‹», título de evento, rótulo de opción, chip que se toca, medidas size.movil)."`.
2. Dentro de `color.coral`, junto a `texto`:
```json
"suave": {
  "$value": "#FDECEA",
  "$type": "color",
  "$description": "Coral Suave · relleno del sello «!» y del chip «enlace externo» de M13"
}
```
3. Dentro de `typography` (mismo formato que `h3`, `chip`), agregar:
```json
"flecha-atras": { "family": "ui", "weight": 700, "size": 26, "$description": "Glifo «‹» de la barra superior móvil, área 44×44 (DS §6)" },
"titulo-evento": { "family": "titulares", "weight": 600, "size": 20, "lineHeight": 24, "$description": "Nombre del evento en la tarjeta de M04 y titular de M03b" },
"opcion": { "family": "ui", "weight": 700, "size": 14, "$description": "Rótulo de la fila de opción de calendario (M01, set 48)" },
"chip-control": { "family": "ui", "weight": 600, "size": 12, "$description": "Texto del chip que se toca («Linterna · auto», M03; mockup v1.7)" }
```
4. En `radius` agregar `"visor": 16, "visor-bienvenida": 20, "marco-lectura": 10, "casilla": 4, "snackbar": 12, "caja-icono": 12`.
5. En `size` agregar el bloque:
```json
"movil": {
  "switch": [44, 26], "icono-fab": 26, "snackbar": 48, "casilla": 20, "icono-fila": 20, "fila-opcion": 36,
  "fila-hoja": 100, "caja-icono": 40, "sello": 30, "sello-grande": 64, "diana": 104, "visor-apagado": 110,
  "visor-bienvenida": 170, "marco-enfoque": 220, "vista-previa": 300, "numeral": 22, "logotipo": 56, "enlace": 32,
  "boton-atras": 44, "asa": [36, 4], "pildora-nav": [40, 22], "icono-nav": 20, "qr-bienvenida": 84, "qr-visor": 100,
  "qr-pantallazo": 120, "marco-lectura": 136, "indicador-pagina": [22, 6],
  "$description": "Medidas de los mockups móviles v1.7 medidas en Figma el 2026-09-20 (Plan 2)"
}
```
6. En `space.movil` agregar `"padding-barra": 16, "padding-navegacion": 36, "hoja-superior": 12, "hoja-inferior": 32, "pie-enlace": 32, "gap-fila": 10, "gap-hoja": 6, "gap-dialogo": 16, "gap-tarjeta": 8`.
7. En `elevation` agregar `"tarjeta": "0 4px 12px rgba(23, 22, 28, 0.18)"` (la tarjeta del evento y la burbuja de M03b usan la misma sombra que el FAB).
8. Al final, `"version": "1.10"`.

Validar: `python3 -c "import json; json.load(open('packages/tokens/design-tokens.json'))"`.

- [ ] **Step 6: Agregar el color a los dos `tokens.css`**

En `packages/tokens/tokens.css`, después de la línea `--color-coral-texto: #c4362e;`, insertar `  --color-coral-suave: #fdecea;`. Luego copiar: `cp packages/tokens/tokens.css apps/web/src/tokens.css` y comprobar `cmp packages/tokens/tokens.css apps/web/src/tokens.css`.

- [ ] **Step 7: Ampliar `Tokens.kt` del móvil**

En `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/theme/Tokens.kt`:

1. Primera línea del encabezado → `// Alarmas QR · Energía puntual · tokens v1.10 (2026-09-20) · derivado de packages/tokens/design-tokens.json`.
2. Imports nuevos: `import androidx.compose.ui.unit.DpSize`.
3. En `object Colores`, tras `VerdeFondo`:
```kotlin
    val CoralSuave = Color(0xFFFDECEA)                       // sello «!» y chip «enlace externo» (M13)
    val BordeSobreTinta = Color(0xFFFFFFFF).copy(alpha = 0.15f) // borde inferior de la barra superior sobre Tinta (M03)
```
4. En `object Tipografia`, tras `Enlace`:
```kotlin
    val FlechaAtras = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 26.sp)               // «‹» (DS §6)
    val TituloEvento = TextStyle(fontFamily = Fuentes.Titulares, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 24.sp)
    val Opcion = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Bold, fontSize = 14.sp)                    // fila de opción (M01)
    val ChipControl = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)          // «Linterna · auto»
```
5. Nuevo `object Medidas` después de `Tamanos`:
```kotlin
/** Medidas de los mockups móviles v1.7 (Figma, 2026-09-20; design-tokens.json `size.movil`). */
object Medidas {
    val Switch = DpSize(44.dp, 26.dp)
    val IconoFab = 26.dp
    val Snackbar = 48.dp
    val Casilla = 20.dp
    val IconoFila = 20.dp
    val FilaOpcion = 36.dp
    val FilaHoja = 100.dp
    val CajaIcono = 40.dp
    val Sello = 30.dp
    val SelloGrande = 64.dp
    val Diana = 104.dp
    val VisorApagado = 110.dp
    val VisorBienvenida = 170.dp
    val MarcoEnfoque = 220.dp
    val VistaPrevia = 300.dp
    val Numeral = 22.dp
    val Logotipo = 56.dp
    val Enlace = 32.dp
    val BotonAtras = 44.dp
    val Asa = DpSize(36.dp, 4.dp)
    val PildoraNav = DpSize(40.dp, 22.dp)
    val IconoNav = 20.dp
    val QRBienvenida = 84.dp
    val QRVisor = 100.dp
    val QRPantallazo = 120.dp
    val MarcoLectura = 136.dp
    val IndicadorPagina = DpSize(22.dp, 6.dp)
}
```
6. En `object Radios`, tras `Barra`:
```kotlin
    val Visor = RoundedCornerShape(16.dp)             // visor apagado (M12), vista previa (M03b)
    val VisorBienvenida = RoundedCornerShape(20.dp)   // visor Tinta de M01
    val MarcoLectura = RoundedCornerShape(10.dp)      // marco del QR en el pantallazo (M03b)
    val Casilla = RoundedCornerShape(4.dp)
    val Snackbar = RoundedCornerShape(12.dp)
    val CajaIcono = RoundedCornerShape(12.dp)         // caja del icono en las filas de M02h y de M01
```
7. En `object Espacio`, tras `EntreBotonesDialogo`:
```kotlin
    val PaddingBarra = 16.dp        // laterales de la barra superior
    val PaddingNavegacion = 36.dp   // laterales de la barra inferior
    val HojaSuperior = 12.dp        // relleno superior de la hoja (asa)
    val HojaInferior = 32.dp        // relleno inferior de la hoja y de los pies con enlace
    val PieEnlace = 32.dp
    val GapFila = 10.dp             // entre hora, texto y switch en la tarjeta
    val GapHoja = 6.dp              // entre filas de la hoja M02h
    val GapDialogo = 16.dp
    val GapTarjeta = 8.dp           // entre filas de la tarjeta del evento (M04)
```
8. Nuevo objeto al final del archivo:
```kotlin
/** Sombra 0 4 12 Tinta 18 % (FAB, hoja, tarjeta del evento, burbuja de M03b) expresada como elevación de Compose. */
object Elevaciones {
    val Fab = 6.dp
    val Tarjeta = 6.dp
    val ColorSombra = Colores.Tinta.copy(alpha = 0.18f)
}
```

Aplicar las mismas adiciones (3–8, con el mismo texto) a `packages/tokens/Tokens.kt` (plantilla con `package com.alarmasqr.ui.theme`; conservar su encabezado y subir su versión a v1.10).

- [ ] **Step 8: Completar los modelos**

En `Modelos.kt`, reemplazar `Alarma` y `EventoQR` y agregar `Organizador`:

```kotlin
@Serializable data class Organizador(val nombre: String, val verificado: Boolean = false)

@Serializable
data class Alarma(
    val id: String,
    val titulo: String,
    /** ISO-8601 con zona, p. ej. 2026-08-27T08:00:00-05:00 */
    val eventoInicio: String,
    val suena: String,
    val lugar: String? = null,
    val origen: String,
    val estado: String,
    val anticipacionMin: Int,
    val trayectoMin: Int,
    val chips: List<String> = emptyList(),
    /** Solo la alarma que el flujo T1 crea al escanear (M04 → M05); arranca fuera de la lista (Plan 2, D3). */
    val esNueva: Boolean = false,
    /** «vuelo» en «vuelo 8:15 am · Aeropuerto»; por defecto «evento». */
    val etiquetaEvento: String? = null,
    val organizador: Organizador? = null,
    val detalle: String? = null,
) {
    val pausada: Boolean get() = estado == "pausada"
}

@Serializable
data class EventoQR(
    val id: String,
    val alarmaId: String,
    val titulo: String,
    val codigoQR: String,
    val escaneos: Int? = null,
    val etiqueta: String? = null,
    val organizador: String? = null,
    val verificado: Boolean = false,
)
```

- [ ] **Step 9: Crear `FormatoHora`**

`apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/FormatoHora.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.datos

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.Locale

/**
 * Formatos de fecha y hora de los mockups (MOCKUPS.md §4): 12 h con am/pm en minúsculas, agrupadores de día en
 * mayúsculas («HOY · JUEVES 27») y fechas largas de M04. Todo se calcula en la zona del dataset (GMT-5).
 */
object FormatoHora {
    private val locale = Locale.forLanguageTag("es-CO")

    private fun parsear(iso: String): OffsetDateTime = OffsetDateTime.parse(iso)

    fun dia(iso: String): LocalDate = parsear(iso).toLocalDate()

    /** «7:30» — sin cero inicial, minutos con dos cifras. */
    fun hora(iso: String): String {
        val t = parsear(iso)
        val h12 = when (val h = t.hour % 12) { 0 -> 12; else -> h }
        return "%d:%02d".format(h12, t.minute)
    }

    fun sufijo(iso: String): String = if (parsear(iso).hour < 12) "am" else "pm"

    fun horaConSufijo(iso: String): String = "${hora(iso)} ${sufijo(iso)}"

    private fun nombreDia(fecha: LocalDate): String =
        fecha.dayOfWeek.getDisplayName(TextStyle.FULL, locale).uppercase(locale)

    /** «HOY · JUEVES 27», «MAÑANA · VIERNES 28», «DOMINGO 30». */
    fun etiquetaDia(iso: String, hoy: LocalDate): String {
        val fecha = dia(iso)
        val base = "${nombreDia(fecha)} ${fecha.dayOfMonth}"
        return when (fecha) {
            hoy -> "HOY · $base"
            hoy.plusDays(1) -> "MAÑANA · $base"
            else -> base
        }
    }

    /** «Dom 30 de agosto · 4:00 pm (GMT-5)» (tarjeta del evento, M04). */
    fun fechaLarga(iso: String): String {
        val t = parsear(iso)
        val diaCorto = t.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).trimEnd('.').replaceFirstChar { it.uppercase(locale) }
        val mes = t.month.getDisplayName(TextStyle.FULL, locale).lowercase(locale)
        val zona = "GMT" + t.offset.id.substringBefore(':').replace("-0", "-").replace("+0", "+")
        return "$diaCorto ${t.dayOfMonth} de $mes · ${horaConSufijo(iso)} ($zona)"
    }

    /** «dom 30» (cuerpo del diálogo M04d: «{fecha} · {hora}»). */
    fun fechaCorta(iso: String): String {
        val t = parsear(iso)
        return "${t.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).trimEnd('.').lowercase(locale)} ${t.dayOfMonth}"
    }

    /** «evento 8:00 am · Aula SD-703» · «pausada · evento 9:30 am» · «vuelo 8:15 am · Aeropuerto». */
    fun lineaEvento(alarma: Alarma): String {
        val etiqueta = alarma.etiquetaEvento ?: "evento"
        val base = "$etiqueta ${horaConSufijo(alarma.eventoInicio)}"
        val conLugar = alarma.lugar?.let { "$base · $it" } ?: base
        return if (alarma.pausada) "pausada · $conLugar" else conLugar
    }
}
```

- [ ] **Step 10: Ampliar `RepositorioDataset`**

Reemplazar el cuerpo de la clase (conservar el `companion object`):

```kotlin
class RepositorioDataset(json: String) {
    val dataset: Dataset = formato.decodeFromString(Dataset.serializer(), json)

    /** HOY de los mockups (jueves 27 de agosto de 2026). */
    val hoy: LocalDate = LocalDate.parse(dataset.meta.hoy)

    /** Alarmas que el flujo crea al escanear (D3): fuera de la lista hasta que llegue su evento. */
    val pendientesDeEscaneo: List<Alarma> = dataset.alarmas.filter { it.esNueva }

    private fun iniciales(): List<Alarma> = dataset.alarmas.filterNot { it.esNueva }.ordenadas()

    private val _alarmas = MutableStateFlow(iniciales())
    val alarmas: StateFlow<List<Alarma>> = _alarmas.asStateFlow()

    /** Última lista antes de la mutación más reciente; la usa «Deshacer · 5 s». */
    private var anterior: List<Alarma>? = null

    /** ⏩ Primer uso: el FAB pasa por M12 solo la primera vez (docs/NAVEGACION.md §6, decisión a). */
    var permisoCamaraPedido: Boolean = false

    fun alarma(id: String): Alarma? = _alarmas.value.firstOrNull { it.id == id }

    fun evento(id: String): EventoQR? = dataset.eventosQR.firstOrNull { it.id == id }

    fun agregar(alarma: Alarma) = mutar { lista -> (lista.filterNot { it.id == alarma.id } + alarma).ordenadas() }

    /**
     * Crea la alarma del evento leído (M03 → M04): toma la alarma del dataset que apunta el evento, la marca
     * «Nueva» y la agrega. Devuelve null si el evento no existe (→ M13).
     */
    fun agregarDesdeEvento(eventoId: String): Alarma? {
        val evento = evento(eventoId) ?: return null
        val base = dataset.alarmas.firstOrNull { it.id == evento.alarmaId } ?: return null
        val nueva = base.copy(esNueva = true, chips = listOf("Nueva") + base.chips.filterNot { it == "Nueva" })
        agregar(nueva)
        return nueva
    }

    fun eliminar(id: String) = mutar { lista -> lista.filterNot { it.id == id } }

    /** Un solo nivel: revierte la última mutación; una segunda llamada no hace nada (comportamiento del snackbar de 5 s). */
    fun deshacer() {
        anterior?.let { _alarmas.value = it }
        anterior = null
    }

    /** Cuerpo de M04d/M06d con {evento}, {fecha} y {hora} rellenos (dataset.meta.notes). */
    fun mensajeEliminar(alarma: Alarma): String = dataset.mensajes.confirmarEliminarCuerpo
        .replace("{evento}", alarma.titulo)
        .replace("{fecha}", FormatoHora.fechaCorta(alarma.eventoInicio))
        .replace("{hora}", FormatoHora.horaConSufijo(alarma.eventoInicio))

    private fun mutar(cambio: (List<Alarma>) -> List<Alarma>) {
        anterior = _alarmas.value
        _alarmas.value = cambio(_alarmas.value)
    }

    private fun List<Alarma>.ordenadas() = sortedBy { OffsetDateTime.parse(it.eventoInicio) }

    /** Restaura el estado inicial (5 alarmas, sin deshacer pendiente ni permiso pedido); solo para pruebas. */
    @VisibleForTesting
    fun reiniciar() {
        _alarmas.value = iniciales()
        anterior = null
        permisoCamaraPedido = false
    }

    companion object { /* sin cambios */ }
}
```
Agregar `import java.time.LocalDate`.

- [ ] **Step 11: Correr las pruebas de datos y las de la Fase 0**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
```
Esperado: `FormatoHoraTest` (4) y `RepositorioDatasetTest` (5) en verde; `NavegacionAppTest` y `PantallaTest` siguen en verde (no dependen del número de alarmas).

- [ ] **Step 12: Commit**

```bash
git add packages/tokens apps/web/src/tokens.css apps/movil
git commit -m "Plan 2: tokens v1.10, modelos completos, FormatoHora y alarmas pendientes de escaneo"
```

---

### Task 2: Iconos vectoriales y componentes L03–L04 (botones, campos, casilla, chips, fila de opción)

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/iconos/Iconos.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/Botones.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/Campos.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/Chips.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/FilaOpcionCalendario.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/componentes/Divisor.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/ComponentesTest.kt`

**Interfaces:**
- Consumes: `Tokens.kt` v1.10 (Tarea 1).
- Produces:
  - `object Iconos { val Escanear, Galeria, Mas, Alarma, Calendario, Ajustes, Google, Outlook, Telefono: ImageVector }` (24×24, trazo 2, remates redondos; se tiñen con `Icon(tint = …)`).
  - `BotonPrimario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, sobreAmarillo: Boolean = false, habilitado: Boolean = true)`
  - `BotonSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false)`
  - `enum class ColorEnlace { Azul, Tinta, Gris, Coral, Blanco }`; `BotonEnlace(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: ColorEnlace = ColorEnlace.Azul, estilo: TextStyle = Tipografia.Enlace)`
  - `BotonAtras(onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false)` (44×44, testTag `atras`)
  - `CampoTexto(valor: String, alCambiar: (String) -> Unit, etiqueta: String, modifier: Modifier = Modifier, pista: String = "", contrasena: Boolean = false)` (48, r12; etiqueta interna en mayúsculas)
  - `Casilla(marcada: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier, texto: (@Composable () -> Unit)? = null)`
  - `enum class VarianteChip { CreadaPorMi, Escaneada, Nueva, Coral, Suave }`; `ChipEstado(texto: String, variante: VarianteChip, modifier: Modifier = Modifier)` (20 de alto)
  - `ChipControl(texto: String, activo: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false)` (32 de alto)
  - `FilaOpcionCalendario(icono: ImageVector, texto: String, marcada: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier)` (36, r12, blanco)
  - `Divisor(texto: String, modifier: Modifier = Modifier)` («o continúa con», «mientras tanto»)

- [ ] **Step 1: Escribir las pruebas (fallan: los componentes no existen)**

`apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/ComponentesTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ComponentesTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `los botones miden 52, los enlaces 32 y la flecha 44, y avisan el toque`() {
        var toques = 0
        regla.setContent {
            AlarmasQRTheme {
                BotonPrimario("Comenzar", onClick = { toques++ }, modifier = Modifier.testTag("primario"))
                BotonSecundario("Google", onClick = { toques++ }, modifier = Modifier.testTag("secundario"))
                BotonEnlace("Continuar como invitado", onClick = { toques++ }, modifier = Modifier.testTag("enlace"))
                BotonAtras(onClick = { toques++ })
            }
        }
        regla.onNodeWithTag("primario").assertHeightIsEqualTo(52.dp).performClick()
        regla.onNodeWithTag("secundario").assertHeightIsEqualTo(52.dp).performClick()
        regla.onNodeWithTag("enlace").assertHeightIsEqualTo(32.dp).performClick()
        regla.onNodeWithTag("atras").assertHeightIsEqualTo(44.dp).assertWidthIsEqualTo(44.dp).performClick()
        assertEquals(4, toques)
    }

    @Test
    fun `el campo mide 48, muestra la etiqueta en mayusculas y devuelve lo escrito`() {
        var valor by mutableStateOf("")
        regla.setContent { AlarmasQRTheme { CampoTexto(valor, { valor = it }, etiqueta = "Correo", pista = "tucorreo@ejemplo.com", modifier = Modifier.testTag("campo")) } }
        regla.onNodeWithTag("campo").assertHeightIsEqualTo(48.dp)
        regla.onNodeWithText("CORREO").assertIsDisplayed()
        regla.onNodeWithText("tucorreo@ejemplo.com").assertIsDisplayed()
        regla.onNodeWithTag("campo").performTextInput("andres@correo.com")
        assertEquals("andres@correo.com", valor)
    }

    @Test
    fun `la casilla y la fila de opcion alternan su estado`() {
        var casilla by mutableStateOf(false)
        var fila by mutableStateOf(false)
        regla.setContent {
            AlarmasQRTheme {
                Casilla(casilla, { casilla = it }, modifier = Modifier.testTag("casilla"))
                FilaOpcionCalendario(Iconos.Google, "Google Calendar", fila, { fila = it }, modifier = Modifier.testTag("fila"))
            }
        }
        regla.onNodeWithTag("casilla").assertIsOff().performClick().assertIsOn()
        regla.onNodeWithTag("fila").assertHeightIsEqualTo(36.dp).assertIsOff().performClick().assertIsOn()
        assertTrue(casilla && fila)
    }

    @Test
    fun `los chips miden 20 y 32 y el chip de control se activa`() {
        var activo by mutableStateOf(false)
        regla.setContent {
            AlarmasQRTheme {
                ChipEstado("✓ Escaneada", VarianteChip.Escaneada, modifier = Modifier.testTag("estado"))
                ChipControl("Linterna · auto", activo, onClick = { activo = !activo }, modifier = Modifier.testTag("control"))
            }
        }
        regla.onNodeWithTag("estado").assertHeightIsEqualTo(20.dp)
        regla.onNodeWithTag("control").assertHeightIsEqualTo(32.dp).assertIsOff().performClick().assertIsOn()
    }
}
```

- [ ] **Step 2: Correr para ver el fallo**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests 'co.edu.uniandes.alarmasqr.ui.componentes.ComponentesTest' --no-daemon
```
Esperado: error de compilación (referencias sin resolver).

- [ ] **Step 3: Crear `Iconos.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.iconos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Iconos de línea del DS (MOCKUPS.md §3: 24 px, trazo 2, terminales redondeadas; «recolorear el trazo según
 * contexto» → se tiñen con `Icon(tint = …)`). Set 48 (google, outlook, teléfono) y la iconografía base.
 * Redibujados a mano sobre la misma anatomía; no son los SVG de Figma, que no se exportan (spec §0.3).
 */
object Iconos {
    private fun icono(nombre: String, vararg trazos: PathBuilder.() -> Unit): ImageVector =
        ImageVector.Builder(name = nombre, defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
            trazos.forEach { trazo ->
                path(
                    fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round, pathFillType = PathFillType.NonZero,
                    pathBuilder = trazo,
                )
            }
        }.build()

    /** Cuatro esquinas del visor + línea de lectura. */
    val Escanear: ImageVector by lazy {
        icono(
            "escanear",
            { moveTo(3f, 7f); verticalLineTo(5f); curveTo(3f, 3.9f, 3.9f, 3f, 5f, 3f); horizontalLineTo(7f) },
            { moveTo(17f, 3f); horizontalLineTo(19f); curveTo(20.1f, 3f, 21f, 3.9f, 21f, 5f); verticalLineTo(7f) },
            { moveTo(21f, 17f); verticalLineTo(19f); curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f); horizontalLineTo(17f) },
            { moveTo(7f, 21f); horizontalLineTo(5f); curveTo(3.9f, 21f, 3f, 20.1f, 3f, 19f); verticalLineTo(17f) },
            { moveTo(7f, 12f); horizontalLineTo(17f) },
        )
    }

    val Galeria: ImageVector by lazy {
        icono(
            "galeria",
            { moveTo(5f, 3f); horizontalLineTo(19f); curveTo(20.1f, 3f, 21f, 3.9f, 21f, 5f); verticalLineTo(19f); curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f); horizontalLineTo(5f); curveTo(3.9f, 21f, 3f, 20.1f, 3f, 19f); verticalLineTo(5f); curveTo(3f, 3.9f, 3.9f, 3f, 5f, 3f); close() },
            { moveTo(10f, 8.5f); curveTo(10f, 9.3f, 9.3f, 10f, 8.5f, 10f); curveTo(7.7f, 10f, 7f, 9.3f, 7f, 8.5f); curveTo(7f, 7.7f, 7.7f, 7f, 8.5f, 7f); curveTo(9.3f, 7f, 10f, 7.7f, 10f, 8.5f); close() },
            { moveTo(21f, 15f); lineTo(16f, 10f); lineTo(5f, 21f) },
        )
    }

    val Mas: ImageVector by lazy { icono("mas", { moveTo(12f, 5f); verticalLineTo(19f) }, { moveTo(5f, 12f); horizontalLineTo(19f) }) }

    val Alarma: ImageVector by lazy {
        icono(
            "alarma",
            { moveTo(20f, 13f); curveTo(20f, 17.4f, 16.4f, 21f, 12f, 21f); curveTo(7.6f, 21f, 4f, 17.4f, 4f, 13f); curveTo(4f, 8.6f, 7.6f, 5f, 12f, 5f); curveTo(16.4f, 5f, 20f, 8.6f, 20f, 13f); close() },
            { moveTo(12f, 9f); verticalLineTo(13f); lineTo(14.5f, 15f) },
            { moveTo(5f, 3f); lineTo(2f, 6f) },
            { moveTo(19f, 3f); lineTo(22f, 6f) },
        )
    }

    val Calendario: ImageVector by lazy {
        icono(
            "calendario",
            { moveTo(5f, 4f); horizontalLineTo(19f); curveTo(20.1f, 4f, 21f, 4.9f, 21f, 6f); verticalLineTo(20f); curveTo(21f, 21.1f, 20.1f, 22f, 19f, 22f); horizontalLineTo(5f); curveTo(3.9f, 22f, 3f, 21.1f, 3f, 20f); verticalLineTo(6f); curveTo(3f, 4.9f, 3.9f, 4f, 5f, 4f); close() },
            { moveTo(16f, 2f); verticalLineTo(6f) },
            { moveTo(8f, 2f); verticalLineTo(6f) },
            { moveTo(3f, 10f); horizontalLineTo(21f) },
        )
    }

    /** Tres deslizadores. */
    val Ajustes: ImageVector by lazy {
        icono(
            "ajustes",
            { moveTo(4f, 6f); horizontalLineTo(20f) }, { moveTo(4f, 12f); horizontalLineTo(20f) }, { moveTo(4f, 18f); horizontalLineTo(20f) },
            { moveTo(10f, 6f); curveTo(10f, 7.1f, 9.1f, 8f, 8f, 8f); curveTo(6.9f, 8f, 6f, 7.1f, 6f, 6f); curveTo(6f, 4.9f, 6.9f, 4f, 8f, 4f); curveTo(9.1f, 4f, 10f, 4.9f, 10f, 6f); close() },
            { moveTo(18f, 12f); curveTo(18f, 13.1f, 17.1f, 14f, 16f, 14f); curveTo(14.9f, 14f, 14f, 13.1f, 14f, 12f); curveTo(14f, 10.9f, 14.9f, 10f, 16f, 10f); curveTo(17.1f, 10f, 18f, 10.9f, 18f, 12f); close() },
            { moveTo(12f, 18f); curveTo(12f, 19.1f, 11.1f, 20f, 10f, 20f); curveTo(8.9f, 20f, 8f, 19.1f, 8f, 18f); curveTo(8f, 16.9f, 8.9f, 16f, 10f, 16f); curveTo(11.1f, 16f, 12f, 16.9f, 12f, 18f); close() },
        )
    }

    /** Monograma «G» (set 48). */
    val Google: ImageVector by lazy {
        icono(
            "google",
            { moveTo(21f, 12f); curveTo(21f, 17f, 17f, 21f, 12f, 21f); curveTo(7f, 21f, 3f, 17f, 3f, 12f); curveTo(3f, 7f, 7f, 3f, 12f, 3f); curveTo(14.5f, 3f, 16.7f, 4f, 18.3f, 5.6f) },
            { moveTo(12f, 12f); horizontalLineTo(21f) },
        )
    }

    /** Sobre (set 48, Outlook · Teams). */
    val Outlook: ImageVector by lazy {
        icono(
            "outlook",
            { moveTo(5f, 5f); horizontalLineTo(19f); curveTo(20.1f, 5f, 21f, 5.9f, 21f, 7f); verticalLineTo(17f); curveTo(21f, 18.1f, 20.1f, 19f, 19f, 19f); horizontalLineTo(5f); curveTo(3.9f, 19f, 3f, 18.1f, 3f, 17f); verticalLineTo(7f); curveTo(3f, 5.9f, 3.9f, 5f, 5f, 5f); close() },
            { moveTo(3f, 7f); lineTo(12f, 13f); lineTo(21f, 7f) },
        )
    }

    /** Smartphone (set 48). */
    val Telefono: ImageVector by lazy {
        icono(
            "telefono",
            { moveTo(9f, 2f); horizontalLineTo(15f); curveTo(16.1f, 2f, 17f, 2.9f, 17f, 4f); verticalLineTo(20f); curveTo(17f, 21.1f, 16.1f, 22f, 15f, 22f); horizontalLineTo(9f); curveTo(7.9f, 22f, 7f, 21.1f, 7f, 20f); verticalLineTo(4f); curveTo(7f, 2.9f, 7.9f, 2f, 9f, 2f); close() },
            { moveTo(11f, 18f); horizontalLineTo(13f) },
        )
    }
}
```

- [ ] **Step 4: Crear `Botones.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 01 «Botón primario»: píldora de 52, Amarillo Energía con texto Tinta. Es el único amarillo de la
 * pantalla; [sobreAmarillo] es la variante de M01 (relleno Tinta, texto blanco) porque el fondo ya es amarillo.
 */
@Composable
fun BotonPrimario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, sobreAmarillo: Boolean = false, habilitado: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier.fillMaxWidth().height(Tamanos.Boton),
        shape = Radios.Pildora,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (sobreAmarillo) Colores.Tinta else Colores.AmarilloEnergia,
            contentColor = if (sobreAmarillo) Colores.Blanco else Colores.Tinta,
            disabledContainerColor = Colores.GrisNiebla,
            disabledContentColor = Colores.GrisTexto,
        ),
        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
    ) { Text(texto, style = Tipografia.Boton) }
}

/** DS comp. 02 «Botón secundario»: contorno 1.5 Tinta (o blanco sobre Tinta, comp. 32), texto del mismo color. */
@Composable
fun BotonSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false) {
    val color = if (sobreTinta) Colores.Blanco else Colores.Tinta
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(Tamanos.Boton),
        shape = Radios.Pildora,
        border = BorderStroke(Trazos.Borde, color),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = color),
        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
    ) { Text(texto, style = Tipografia.Boton) }
}

enum class ColorEnlace(val color: Color) {
    Azul(Colores.AzulTexto), Tinta(Colores.Tinta), Gris(Colores.GrisTexto), Coral(Colores.CoralTexto), Blanco(Colores.Blanco)
}

/**
 * DS comp. 04 «Botón de texto / enlace»: 32 de alto, relleno horizontal 20, texto subrayado Bold 15 (o el [estilo]
 * dado: Medium 14 en el descarte de M04, Bold 14 en M13). La acción destructiva dentro de una pantalla es un enlace
 * Coral Texto, nunca una píldora (DS v1.6).
 */
@Composable
fun BotonEnlace(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: ColorEnlace = ColorEnlace.Azul, estilo: TextStyle = Tipografia.Enlace) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(Medidas.Enlace),
        shape = Radios.Pildora,
        colors = ButtonDefaults.textButtonColors(contentColor = color.color),
        contentPadding = PaddingValues(horizontal = Espacio.Margen),
    ) { Text(texto, style = estilo.copy(textDecoration = TextDecoration.Underline)) }
}

/** Flecha «‹» de la barra superior: área táctil 44×44, Archivo Bold 26 (DS §6). testTag `atras`. */
@Composable
fun BotonAtras(onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false) {
    TextButton(
        onClick = onClick,
        modifier = modifier.size(Medidas.BotonAtras).testTag("atras").semantics { role = Role.Button },
        shape = Radios.Pildora,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = if (sobreTinta) Colores.Blanco else Colores.Tinta),
    ) { Box(contentAlignment = Alignment.Center) { Text("‹", style = Tipografia.FlechaAtras) } }
}
```
(Agregar `import androidx.compose.ui.unit.dp` para `PaddingValues(0.dp)`.)

- [ ] **Step 5: Crear `Campos.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 06 «Campo de texto» (v1.1): etiqueta dentro del contorno en Archivo SemiBold 12 Gris Texto y mayúsculas,
 * valor Archivo 15, altura 48, radio 12, borde 1.5 Gris Borde; con foco borde 2 Tinta (mockup M00b). La pista de
 * formato («tucorreo@ejemplo.com») se muestra en Gris Medio cuando el valor está vacío.
 */
@Composable
fun CampoTexto(valor: String, alCambiar: (String) -> Unit, etiqueta: String, modifier: Modifier = Modifier, pista: String = "", contrasena: Boolean = false) {
    val interaccion = remember { MutableInteractionSource() }
    val enfocado by interaccion.collectIsFocusedAsState()
    val borde = if (enfocado) Trazos.Foco else Trazos.Borde
    val colorBorde = if (enfocado) Colores.Tinta else Colores.GrisBorde
    BasicTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = modifier.fillMaxWidth().height(Tamanos.Campo),
        singleLine = true,
        interactionSource = interaccion,
        textStyle = Tipografia.Cuerpo.copy(fontSize = 15.sp, lineHeight = 16.sp, color = Colores.Tinta),
        cursorBrush = SolidColor(Colores.Tinta),
        visualTransformation = if (contrasena) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (contrasena) KeyboardType.Password else KeyboardType.Email),
        decorationBox = { campo ->
            Column(
                Modifier.fillMaxWidth().height(Tamanos.Campo).background(Colores.Blanco, Radios.Campo).border(borde, colorBorde, Radios.Campo)
                    .padding(horizontal = Espacio.Medianil, vertical = 7.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(etiqueta.uppercase(), style = Tipografia.EtiquetaCampo, color = Colores.GrisTexto)
                Box {
                    if (valor.isEmpty()) Text(pista, style = Tipografia.Cuerpo.copy(fontSize = 15.sp, lineHeight = 16.sp), color = Colores.GrisMedio)
                    campo()
                }
            }
        },
    )
}

/** Solo el dibujo de la casilla (sin semántica): lo usan [Casilla] y la fila de opción, cuyo control es toda la fila. */
@Composable
fun CasillaVisual(marcada: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier.size(Medidas.Casilla)
            .background(if (marcada) Colores.Tinta else Colores.Blanco, Radios.Casilla)
            .border(Trazos.Foco, Colores.Tinta, Radios.Casilla),
        contentAlignment = Alignment.Center,
    ) { if (marcada) Text("✓", style = Tipografia.Chip, color = Colores.Blanco) }
}

/** DS comp. 11 «Checkbox»: 20×20, borde 2 Tinta, radio 4; marcada = relleno Tinta con «✓» blanco (color + forma). */
@Composable
fun Casilla(marcada: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier, texto: (@Composable () -> Unit)? = null) {
    Row(
        modifier = modifier.toggleable(value = marcada, role = Role.Checkbox, onValueChange = alCambiar),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.Top,
    ) {
        CasillaVisual(marcada)
        texto?.invoke()
    }
}
```
Nota sobre `7.dp`, `2.dp`, `15.sp`, `16.sp`: son el relleno vertical y el gap del campo y el tamaño del valor medidos en Figma (campo 3:11). Agregarlos como tokens: en `Espacio` → `val PaddingCampoVertical = 7.dp; val GapCampo = 2.dp`; en `Tipografia` → `val ValorCampo = TextStyle(fontFamily = Fuentes.Ui, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 16.sp)`; y en `design-tokens.json` `space.movil.padding-campo-vertical: 7`, `space.movil.gap-campo: 2`, `typography.valor-campo {family ui, weight 400, size 15}`. Sustituir los literales por esos tokens antes de continuar (regla global).

- [ ] **Step 6: Crear `Chips.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 19 «Chip de estado» (no se toca): 20 de alto, relleno 12/3, píldora, Archivo 12. Origen siempre
 * («Creada por mí» contorno Tinta, «✓ Escaneada» contorno Verde Texto); estado temporal «Nueva» relleno Tinta.
 * `Coral` = «enlace externo» (M13); `Suave` = «Datos leídos del QR» (M04, Amarillo Suave sin borde).
 */
enum class VarianteChip(val fondo: Color, val borde: Color?, val texto: Color) {
    CreadaPorMi(Color.Transparent, Colores.Tinta, Colores.Tinta),
    Escaneada(Color.Transparent, Colores.VerdeTexto, Colores.VerdeTexto),
    Nueva(Colores.Tinta, null, Colores.Blanco),
    Coral(Colores.CoralSuave, Colores.CoralTexto, Colores.CoralTexto),
    Suave(Colores.AmarilloSuave, null, Colores.Tinta),
}

@Composable
fun ChipEstado(texto: String, variante: VarianteChip, modifier: Modifier = Modifier) {
    Box(
        modifier.height(Tamanos.Chip)
            .background(variante.fondo, Radios.Pildora)
            .then(variante.borde?.let { Modifier.border(Trazos.Borde, it, Radios.Pildora) } ?: Modifier)
            .padding(horizontal = Espacio.Medianil),
        contentAlignment = Alignment.Center,
    ) { Text(texto, style = Tipografia.Chip, color = variante.texto, maxLines = 1) }
}

/** Variante del chip de estado por el texto del dataset: «Nueva», «✓ Escaneada» o «Creada por mí». */
fun varianteDeChip(texto: String): VarianteChip = when {
    texto == "Nueva" -> VarianteChip.Nueva
    texto.contains("Escaneada") -> VarianteChip.Escaneada
    else -> VarianteChip.CreadaPorMi
}

/**
 * DS set 49 «Chip como control» (v1.11): 32 de alto, relleno lateral 14, radio 16. Inactivo contorno 1.5 (Tinta;
 * Gris Medio sobre Tinta, comp. 33), activo relleno Tinta con texto blanco (blanco con texto Tinta sobre Tinta).
 */
@Composable
fun ChipControl(texto: String, activo: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, sobreTinta: Boolean = false) {
    val fondo = when { !activo -> Color.Transparent; sobreTinta -> Colores.Blanco; else -> Colores.Tinta }
    val color = when { activo && sobreTinta -> Colores.Tinta; activo -> Colores.Blanco; sobreTinta -> Colores.Blanco; else -> Colores.Tinta }
    val borde = if (sobreTinta) Colores.GrisMedio else Colores.Tinta
    Box(
        modifier.height(Tamanos.ChipControl)
            .toggleable(value = activo, role = Role.Switch, onValueChange = { onClick() })
            .background(fondo, Radios.Pildora)
            .then(if (activo) Modifier else Modifier.border(Trazos.Borde, borde, Radios.Pildora))
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) { Text(texto, style = Tipografia.ChipControl, color = color, maxLines = 1) }
}
```
Sustituir `14.dp` por un token: `Espacio.PaddingChipControl = 14.dp` (JSON `space.movil.padding-chip-control: 14`; el DS set 49 ya lo documenta como «relleno lateral 14»).

- [ ] **Step 7: Crear `FilaOpcionCalendario.kt` y `Divisor.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * DS set 48 «uso · fila de opción (M01)»: 350×36, blanco, radio 12, relleno 12/8; icono de línea a 20 + rótulo
 * Archivo Bold 14 + casilla 20 a la derecha. Toda la fila es el control (áreas de toque, NAVEGACION §6 d).
 */
@Composable
fun FilaOpcionCalendario(icono: ImageVector, texto: String, marcada: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().height(Medidas.FilaOpcion)
            .toggleable(value = marcada, role = Role.Checkbox, onValueChange = alCambiar)
            .background(Colores.Blanco, Radios.CajaIcono)
            .padding(horizontal = Espacio.Medianil, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icono, contentDescription = null, tint = Colores.Tinta, modifier = Modifier.size(Medidas.IconoFila))
        Text(texto, style = Tipografia.Opcion, color = Colores.Tinta, modifier = Modifier.weight(1f))
        CasillaVisual(marcada)   // decorativa: la fila entera es el control (un solo toggleable; la prueba usa assertIsOn sobre la fila)
    }
}
```
(`8.dp` → token `Espacio.PaddingFilaVertical = 8.dp`, JSON `space.movil.padding-fila-vertical: 8`.)

`Divisor.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** Línea Gris Borde · texto Archivo 12 Gris Texto · línea («o continúa con» en M00a/M00b, «mientras tanto» en M12). */
@Composable
fun Divisor(texto: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(Modifier.weight(1f), thickness = Trazos.BordeFino, color = Colores.GrisBorde)
        Text(texto, style = Tipografia.Chip.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Normal), color = Colores.GrisTexto)
        HorizontalDivider(Modifier.weight(1f), thickness = Trazos.BordeFino, color = Colores.GrisBorde)
    }
}
```
(`8.dp` → `Espacio.GapDivisor = 8.dp`, JSON `space.movil.gap-divisor: 8`; el estilo del texto → `Tipografia.Divisor = TextStyle(Fuentes.Ui, Normal, 12.sp)`, JSON `typography.divisor {ui, 400, 12}`.)

- [ ] **Step 8: Correr las pruebas**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests 'co.edu.uniandes.alarmasqr.ui.componentes.ComponentesTest' --no-daemon
```
Esperado: 4 pruebas en verde. Si `assertHeightIsEqualTo` falla por medio píxel, comprobar que la prueba no corre con `qualifiers` distintos del valor por defecto (mdpi): las medidas en dp se comparan con tolerancia de 0.5 dp.

- [ ] **Step 9: Commit**

```bash
git add apps/movil packages/tokens
git commit -m "L03–L04: iconos de línea, botones, campo, casilla, chips y fila de opción"
```

---

### Task 3: Barra superior, barra inferior propia, FAB «Escanear», interruptor y tarjeta de alarma; `NavegacionApp` con FAB y ViewModels

**Files:**
- Create: `ui/componentes/BarraSuperior.kt`, `ui/componentes/NavegacionInferior.kt`, `ui/componentes/FabEscanear.kt`, `ui/componentes/Interruptor.kt`, `ui/componentes/TarjetaAlarma.kt` (todos bajo `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/`)
- Modify: `navegacion/NavegacionApp.kt`
- Modify: `ui/theme/Tokens.kt` (dígitos tabulares, tokens de la tarjeta)
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/FabEscanearTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/TarjetaAlarmaTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/NavegacionAppTest.kt` (modificar)

**Interfaces:**
- Consumes: Tarea 1 (`FormatoHora`, `Alarma.pausada`, `Medidas`), Tarea 2 (`Iconos`, `ChipEstado`, `varianteDeChip`).
- Produces:
  - `BarraSuperior(titulo: String, modifier: Modifier = Modifier, alVolver: (() -> Unit)? = null, sobreTinta: Boolean = false, accion: @Composable RowScope.() -> Unit = {})`
  - `NavegacionInferior(activa: Pantalla, alCambiar: (Pantalla) -> Unit, modifier: Modifier = Modifier)` (ítems con testTag `nav-M02`, `nav-M02b`, `nav-M11`)
  - `FabEscanear(alTocar: () -> Unit, alMantener: () -> Unit, modifier: Modifier = Modifier)` (testTag `fab-escanear`; mantener = 500 ms)
  - `Interruptor(activo: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier)` (44×26)
  - `TarjetaAlarma(alarma: Alarma, onClick: () -> Unit, alCambiarActiva: (Boolean) -> Unit, modifier: Modifier = Modifier)` (testTag `alarma-<id>`)
  - `AgrupadorDia(texto: String, modifier: Modifier = Modifier)`
  - `NavegacionApp(backStack, repositorio, entradas)` ahora dibuja `NavegacionInferior`, el FAB cuando `visible.conFab` (toque → M12 la primera vez, luego M03; mantener → M02h), un `SnackbarHost` propio expuesto como `LocalSnackbarApp` (`ProvidableCompositionLocal<SnackbarHostState>`) y registra `rememberViewModelStoreNavEntryDecorator()`.

- [ ] **Step 1: Pruebas del FAB y de la tarjeta (fallan)**

`FabEscanearTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.unit.dp
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
class FabEscanearTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `toque y mantener disparan acciones distintas`() {
        var toques = 0
        var mantenidos = 0
        regla.setContent { AlarmasQRTheme { FabEscanear(alTocar = { toques++ }, alMantener = { mantenidos++ }) } }
        regla.onNodeWithTag("fab-escanear").assertHeightIsEqualTo(56.dp).performClick()
        regla.onNodeWithTag("fab-escanear").performSemanticsAction(SemanticsActions.OnLongClick)
        regla.onNodeWithText("Escanear").assertExists()
        assertEquals(1, toques)
        assertEquals(1, mantenidos)
    }
}
```

`TarjetaAlarmaTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class TarjetaAlarmaTest {
    @get:Rule val regla = createComposeRule()

    private val tutor = Alarma(id = "a-tutor", titulo = "Reunión con el tutor", eventoInicio = "2026-08-27T08:00:00-05:00", suena = "2026-08-27T07:30:00-05:00", lugar = "Aula SD-703", origen = "creada-por-mi", estado = "activa", anticipacionMin = 30, trayectoMin = 0, chips = listOf("Creada por mí"))

    @Test
    fun `muestra hora, sufijo, linea del evento, chip e interruptor y avisa el toque`() {
        var tocada = false
        regla.setContent { AlarmasQRTheme { TarjetaAlarma(tutor, onClick = { tocada = true }, alCambiarActiva = {}) } }
        regla.onNodeWithText("7:30").assertIsDisplayed()
        regla.onNodeWithText("am").assertIsDisplayed()
        regla.onNodeWithText("Reunión con el tutor").assertIsDisplayed()
        regla.onNodeWithText("evento 8:00 am · Aula SD-703").assertIsDisplayed()
        regla.onNodeWithText("Creada por mí").assertIsDisplayed()
        regla.onNodeWithTag("interruptor-a-tutor").assertIsOn()
        regla.onNodeWithTag("alarma-a-tutor").performClick()
        assertTrue(tocada)
    }

    @Test
    fun `una alarma pausada apaga el interruptor y lo dice en la linea`() {
        val gimnasio = tutor.copy(id = "a-gimnasio", titulo = "Gimnasio", eventoInicio = "2026-08-27T09:30:00-05:00", lugar = null, estado = "pausada", chips = emptyList())
        regla.setContent { AlarmasQRTheme { TarjetaAlarma(gimnasio, onClick = {}, alCambiarActiva = {}) } }
        regla.onNodeWithText("pausada · evento 9:30 am").assertIsDisplayed()
        regla.onNodeWithTag("interruptor-a-gimnasio").assertIsOff()
    }
}
```

- [ ] **Step 2: Correr para ver el fallo**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests '*FabEscanearTest' --tests '*TarjetaAlarmaTest' --no-daemon
```
Esperado: error de compilación.

- [ ] **Step 3: Tokens de esta tarea**

En `Tokens.kt` (y `packages/tokens/Tokens.kt` + JSON):
- Dígitos tabulares: agregar `fontFeatureSettings = "tnum"` a `HoraProtagonista`, `HoraProtagonistaSufijo`, `HoraSonara`, `HoraTarjeta` y `HoraAmPm` (CLAUDE.md «dígitos tabulares»).
- `Espacio.GapHoraSufijo = 6.dp` (JSON `space.movil.gap-hora-sufijo: 6`), `Espacio.GapTextoTarjeta = 3.dp` (`gap-texto-tarjeta: 3`), `Espacio.GapChips = 6.dp` (`gap-chips: 6`), `Espacio.GapNavegacion = 3.dp` (`gap-navegacion: 3`), `Espacio.PaddingFabInicio = 20.dp`, `Espacio.PaddingFabFin = 24.dp` (`padding-fab: [20, 24]`), `Espacio.PaddingTarjetaVertical = 10.dp` (`padding-tarjeta-vertical: 10`).
- `Medidas.Perilla = 20.dp` (`perilla: 20`), `Medidas.HoraTarjetaAncho = 81.dp` (`hora-tarjeta-ancho: 81`; columna fija de la hora para que las tarjetas alineen el texto).

- [ ] **Step 4: `BarraSuperior.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 12 «Barra superior móvil»: 56, relleno lateral 16, gap 12, borde inferior 1.5; título Bricolage Bold 22
 * a la izquierda y el control de la derecha separados por un espaciador flexible (mockups v1.7). Con [alVolver]
 * dibuja la flecha «‹» de 44×44. [sobreTinta] = M03 (fondo Tinta, texto blanco, borde blanco 15 %).
 */
@Composable
fun BarraSuperior(titulo: String, modifier: Modifier = Modifier, alVolver: (() -> Unit)? = null, sobreTinta: Boolean = false, accion: @Composable RowScope.() -> Unit = {}) {
    val fondo = if (sobreTinta) Colores.Tinta else Colores.Blanco
    val borde = if (sobreTinta) Colores.BordeSobreTinta else Colores.GrisBorde
    Row(
        modifier.fillMaxWidth().height(Tamanos.BarraSuperior).background(fondo)
            .drawBehind { val y = size.height - Trazos.Borde.toPx() / 2; drawLine(borde, Offset(0f, y), Offset(size.width, y), Trazos.Borde.toPx()) }
            .padding(horizontal = Espacio.PaddingBarra),
        horizontalArrangement = Arrangement.spacedBy(Espacio.Medianil),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        alVolver?.let { BotonAtras(onClick = it, sobreTinta = sobreTinta) }
        Text(titulo, style = Tipografia.BarraSuperior, color = if (sobreTinta) Colores.Blanco else Colores.Tinta)
        Spacer(Modifier.weight(1f))
        accion()
    }
}
```

- [ ] **Step 5: `NavegacionInferior.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

private data class Pestana(val pantalla: Pantalla, val rotulo: String, val icono: ImageVector, val codigosActivos: Set<String>)

private val pestanas = listOf(
    Pestana(Pantalla.M02, "Alarmas", Iconos.Alarma, setOf("M02", "M02v", "M05")),
    Pestana(Pantalla.M02b, "Calendario", Iconos.Calendario, setOf("M02b")),
    Pestana(Pantalla.M11, "Ajustes", Iconos.Ajustes, setOf("M11")),
)

/**
 * DS comp. 13 «Navegación inferior móvil»: 64, borde superior 1.5, relleno lateral 36, tres pestañas distribuidas;
 * píldora 40×22 (activa Gris Niebla, DS §6 / D6) con icono 20 y rótulo 11 (Bold Tinta activo, Medium Gris Texto inactivo).
 */
@Composable
fun NavegacionInferior(activa: Pantalla, alCambiar: (Pantalla) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().height(Tamanos.NavegacionInferior).background(Colores.Blanco)
            .drawBehind { val y = Trazos.Borde.toPx() / 2; drawLine(Colores.GrisBorde, Offset(0f, y), Offset(size.width, y), Trazos.Borde.toPx()) }
            .padding(horizontal = Espacio.PaddingNavegacion),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        pestanas.forEach { p ->
            val seleccionada = activa.codigo in p.codigosActivos
            Column(
                Modifier.selectable(selected = seleccionada, role = Role.Tab, onClick = { alCambiar(p.pantalla) }).testTag("nav-${p.pantalla.codigo}"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Espacio.GapNavegacion),
            ) {
                Box(Modifier.size(Medidas.PildoraNav.width, Medidas.PildoraNav.height).background(if (seleccionada) Colores.GrisNiebla else Color.Transparent, Radios.Pildora), contentAlignment = Alignment.Center) {
                    Icon(p.icono, contentDescription = null, tint = if (seleccionada) Colores.Tinta else Colores.GrisTexto, modifier = Modifier.size(Medidas.IconoNav))
                }
                Text(
                    p.rotulo,
                    style = Tipografia.NavegacionInferior.copy(fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium),
                    color = if (seleccionada) Colores.Tinta else Colores.GrisTexto,
                )
            }
        }
    }
}
```

- [ ] **Step 6: `FabEscanear.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * DS comp. 05 «FAB extendido “Escanear”»: 56, radio 16, Amarillo Energía, icono escanear 26 + Archivo Bold 16,
 * relleno 20/24, sombra 0 4 12. Toque → cámara; mantener 500 ms (`Movimiento.ToqueLargoMs`, no los 400 del sistema)
 * → hoja «Agregar evento». Es el único amarillo de M02/M02b/M05.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FabEscanear(alTocar: () -> Unit, alMantener: () -> Unit, modifier: Modifier = Modifier) {
    val base = LocalViewConfiguration.current
    val con500 = remember(base) { object : ViewConfiguration by base { override val longPressTimeoutMillis: Long get() = Movimiento.ToqueLargoMs } }
    CompositionLocalProvider(LocalViewConfiguration provides con500) {
        Surface(
            modifier = modifier.height(Tamanos.Fab).testTag("fab-escanear"),
            shape = Radios.Fab,
            color = Colores.AmarilloEnergia,
            contentColor = Colores.Tinta,
            shadowElevation = Elevaciones.Fab,
        ) {
            Row(
                Modifier.combinedClickable(role = Role.Button, onClick = alTocar, onLongClick = alMantener)
                    .padding(start = Espacio.PaddingFabInicio, end = Espacio.PaddingFabFin),
                horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Iconos.Escanear, contentDescription = null, modifier = Modifier.size(Medidas.IconoFab))
                Text("Escanear", style = Tipografia.Destacado)
            }
        }
    }
}
```

- [ ] **Step 7: `Interruptor.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 10 «Switch» (v1.1, Tinta): 44×26; activo = pista Tinta con perilla blanca a la derecha; inactivo = pista
 * blanca con borde Gris Borde y perilla Gris Medio a la izquierda (color + forma). Transición 250 ms.
 */
@Composable
fun Interruptor(activo: Boolean, alCambiar: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val pista by animateColorAsState(if (activo) Colores.Tinta else Colores.Blanco, tween(Movimiento.TransicionMs), label = "pista")
    val perilla by animateColorAsState(if (activo) Colores.Blanco else Colores.GrisMedio, tween(Movimiento.TransicionMs), label = "perilla")
    val margen = (Medidas.Switch.height - Medidas.Perilla) / 2
    val desplazamiento by animateDpAsState(if (activo) Medidas.Switch.width - Medidas.Perilla - margen else margen, tween(Movimiento.TransicionMs), label = "perilla-x")
    Box(
        modifier.size(Medidas.Switch)
            .toggleable(value = activo, role = Role.Switch, onValueChange = alCambiar)
            .background(pista, Radios.Pildora)
            .then(if (activo) Modifier else Modifier.border(Trazos.Borde, Colores.GrisBorde, Radios.Pildora)),
        contentAlignment = Alignment.CenterStart,
    ) { Box(Modifier.offset(x = desplazamiento).size(Medidas.Perilla).background(perilla, Radios.Pildora)) }
}
```

- [ ] **Step 8: `TarjetaAlarma.kt` (con `AgrupadorDia`)**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** Agrupador de día «HOY · JUEVES 27»: H3 (Archivo Bold 13, +8 %) Gris Texto. */
@Composable
fun AgrupadorDia(texto: String, modifier: Modifier = Modifier) {
    Text(texto, style = Tipografia.H3, color = Colores.GrisTexto, modifier = modifier)
}

/**
 * DS comp. 18 «Tarjeta de alarma» (anatomía única v1.3): 350×75 (53 sin chip), borde 1.5 Gris Borde (2 Verde Texto
 * si es «recién guardada»), radio 14, relleno 14/10; hora Spline Sans Mono Bold 26 + am/pm Medium 12 en la misma
 * línea, título Archivo Bold 15, «evento h:mm · lugar» 13 Gris Texto, chips debajo y el interruptor a la derecha.
 * Pausada: sin fondo (tutores v1.6), todo en Gris Texto e interruptor apagado.
 */
@Composable
fun TarjetaAlarma(alarma: Alarma, onClick: () -> Unit, alCambiarActiva: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val principal = if (alarma.pausada) Colores.GrisTexto else Colores.Tinta
    val borde = if (alarma.esNueva) Trazos.Foco else Trazos.Borde
    val colorBorde = if (alarma.esNueva) Colores.VerdeTexto else Colores.GrisBorde
    Row(
        modifier.fillMaxWidth().clip(Radios.Tarjeta).background(Colores.Blanco).border(borde, colorBorde, Radios.Tarjeta)
            .clickable(role = Role.Button, onClick = onClick).testTag("alarma-${alarma.id}")
            .padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingTarjetaVertical),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(Modifier.width(Medidas.HoraTarjetaAncho), horizontalArrangement = Arrangement.spacedBy(Espacio.GapHoraSufijo)) {
            Text(FormatoHora.hora(alarma.suena), style = Tipografia.HoraTarjeta, color = principal, modifier = Modifier.alignByBaseline())
            Text(FormatoHora.sufijo(alarma.suena), style = Tipografia.HoraAmPm, color = principal, modifier = Modifier.alignByBaseline())
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoTarjeta)) {
            Text(alarma.titulo, style = Tipografia.TituloTarjeta, color = principal)
            Text(FormatoHora.lineaEvento(alarma), style = Tipografia.Etiqueta, color = Colores.GrisTexto)
            if (alarma.chips.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapChips)) {
                    alarma.chips.forEach { ChipEstado(it, varianteDeChip(it)) }
                }
            }
        }
        Interruptor(activo = !alarma.pausada, alCambiar = alCambiarActiva, modifier = Modifier.testTag("interruptor-${alarma.id}"))
    }
}
```

- [ ] **Step 9: `NavegacionApp.kt` con barra propia, FAB, snackbar y decoradores**

Reemplazar el archivo completo:

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

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
                modifier = Modifier.padding(relleno),
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
```
`SnackbarDeshacer(datos: SnackbarData)` se crea en la Tarea 4; para compilar esta tarea, crear ya el archivo `ui/componentes/SnackbarDeshacer.kt` con la versión de la Tarea 4 Step 6 (es independiente).

- [ ] **Step 10: Ajustar `NavegacionAppTest`**

Agregar una prueba y dejar las cinco existentes (siguen válidas: los `testTag` `nav-M11` y `pantalla-*` se conservan):

```kotlin
    @Test
    fun `el FAB pasa por M12 la primera vez y por M03 despues; mantenerlo abre M02h`() {
        repositorio.reiniciar()
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.onNodeWithTag("fab-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.runOnUiThread { pila.removeLastOrNull() }
        regla.onNodeWithTag("fab-escanear").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.runOnUiThread { pila.removeLastOrNull() }
        regla.onNodeWithTag("fab-escanear").performSemanticsAction(SemanticsActions.OnLongClick)
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02h").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M02h), pila.toList())
    }
```
(imports: `androidx.compose.ui.semantics.SemanticsActions`, `androidx.compose.ui.test.performSemanticsAction`).

- [ ] **Step 11: Correr todo**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
```
Esperado: verde (Fase 0: 12 + Tarea 1: 9 + Tarea 2: 4 + esta tarea: 4 nuevas).

- [ ] **Step 12: Commit**

```bash
git add apps/movil packages/tokens
git commit -m "L05–L06: barra superior, navegación inferior propia, FAB con toque largo, interruptor y tarjeta de alarma; NavegacionApp con FAB, snackbar y ViewModels"
```

---

### Task 4: Banda de textura, QR (ZXing) y diana, snackbar «Deshacer», `DialogoConfirmacion` y tarjeta del evento

**Files:**
- Create: `ui/componentes/BandaTextura.kt`, `ui/componentes/CodigoQR.kt`, `ui/componentes/SnackbarDeshacer.kt`, `ui/componentes/DialogoConfirmacion.kt`, `ui/componentes/TarjetaEvento.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/DialogoConfirmacionTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/componentes/CodigoQRTest.kt`

**Interfaces:**
- Consumes: Tareas 1–3.
- Produces:
  - `BandaTextura(modifier: Modifier = Modifier, sobreTinta: Boolean = false, alto: Dp = Tamanos.BandaTextura)`
  - `CodigoQR(contenido: String, tamano: Dp, modifier: Modifier = Modifier)`; `DianaQR(tamano: Dp, modifier: Modifier = Modifier, color: Color = Colores.Tinta, acento: Color = Colores.AmarilloEnergia)`; `Logotipo(modifier: Modifier = Modifier)` (56); `Destello(tamano: Dp, color: Color, modifier: Modifier = Modifier)`
  - `SnackbarDeshacer(datos: SnackbarData, modifier: Modifier = Modifier)` (testTag `snackbar`, acción con testTag `deshacer`)
  - `DialogoConfirmacion(titulo: String, cuerpo: String, rotuloSeguro: String, rotuloConfirmar: String, destructivo: Boolean, alSeguro: () -> Unit, alConfirmar: () -> Unit)` (testTags `velo`, `dialogo-confirmacion`, `dialogo-seguro`, `dialogo-confirmar`)
  - `TarjetaEvento(alarma: Alarma, modifier: Modifier = Modifier)`; `SelloVerificado(modifier: Modifier = Modifier)`

- [ ] **Step 1: Pruebas (fallan)**

`DialogoConfirmacionTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class DialogoConfirmacionTest {
    @get:Rule val regla = createComposeRule()

    private fun montar(alSeguro: () -> Unit, alConfirmar: () -> Unit) = regla.setContent {
        AlarmasQRTheme {
            DialogoConfirmacion(
                titulo = "¿Eliminar alarma?", cuerpo = "Dejarás de recibir el aviso.", rotuloSeguro = "Conservar", rotuloConfirmar = "Eliminar",
                destructivo = true, alSeguro = alSeguro, alConfirmar = alConfirmar,
            )
        }
    }

    @Test
    fun `muestra titulo y cuerpo y cada boton avisa su accion`() {
        var seguro = 0; var confirmar = 0
        montar({ seguro++ }, { confirmar++ })
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.onNodeWithText("Dejarás de recibir el aviso.").assertIsDisplayed()
        regla.onNodeWithTag("dialogo-seguro").performClick()
        regla.onNodeWithTag("dialogo-confirmar").performClick()
        assertEquals(1, seguro); assertEquals(1, confirmar)
    }

    @Test
    fun `tocar el velo equivale a la accion segura`() {
        var seguro = 0; var confirmar = 0
        montar({ seguro++ }, { confirmar++ })
        regla.onNodeWithTag("velo").performClick()
        assertEquals(1, seguro); assertEquals(0, confirmar)
    }
}
```

`CodigoQRTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.dp
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
class CodigoQRTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `genera un QR cuadrado del tamano pedido`() {
        regla.setContent { AlarmasQRTheme { CodigoQR("alarmasqr://evento/e-entrega", tamano = 120.dp) } }
        regla.onNodeWithContentDescription("Código QR del evento").assertWidthIsEqualTo(120.dp).assertHeightIsEqualTo(120.dp)
    }
}
```

- [ ] **Step 2: Correr para ver el fallo**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests '*DialogoConfirmacionTest' --tests '*CodigoQRTest' --no-daemon
```

- [ ] **Step 3: Tokens de esta tarea**

`Medidas.ModuloTextura = 8.dp`, `Medidas.PasoTextura = 14.dp` (JSON `size.movil.modulo-textura: 8`, `paso-textura: 14`); `Radios.Modulo = RoundedCornerShape(2.dp)` y `Radios.QR = RoundedCornerShape(4.dp)` (JSON `radius.modulo: 2`, `radius.qr: 4`); `Movimiento.TexturaOpacidadMin = 0.02f` (JSON `texture.modulos-qr.opacityMin: 0.02`); `Medidas.AccionSnackbar = DpSize(110.dp, 26.dp)` (`accion-snackbar: [110, 26]`); `Medidas.EtiquetaDato = 74.dp` (`etiqueta-dato: 74`); `Espacio.PaddingTarjetaEvento = 16.dp` (`padding-tarjeta-evento: 16`); `Tipografia.EtiquetaDato = TextStyle(Fuentes.Ui, Bold, 12.sp, letterSpacing = 0.08.em)` y `Tipografia.Dato = TextStyle(Fuentes.Ui, Normal, 13.5.sp)` (JSON `typography.etiqueta-dato {ui, 700, 12, letterSpacing 8%}`, `typography.dato {ui, 400, 13.5}`).

- [ ] **Step 4: `BandaTextura.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos

/**
 * «textura · módulos QR» (MOCKUPS.md §3): retícula de módulos 8×8 radio 2 con paso 14, opacidad 8 % arriba que
 * baja hasta 2 % en la última fila. Solo como banda de 120 en M01, M04, M12 y M13 (Style Tile §4.6). Vector puro.
 */
@Composable
fun BandaTextura(modifier: Modifier = Modifier, sobreTinta: Boolean = false, alto: Dp = Tamanos.BandaTextura) {
    val color = if (sobreTinta) Colores.Blanco else Colores.Tinta
    Canvas(modifier.fillMaxWidth().height(alto)) {
        val modulo = Medidas.ModuloTextura.toPx()
        val paso = Medidas.PasoTextura.toPx()
        val radio = CornerRadius(modulo / 4)
        val filas = (size.height / paso).toInt()
        val columnas = (size.width / paso).toInt() + 1
        for (f in 0 until filas) {
            val alfa = (Movimiento.TexturaOpacidadMax - (Movimiento.TexturaOpacidadMax - Movimiento.TexturaOpacidadMin) * f / (filas - 1).coerceAtLeast(1))
            for (c in 0 until columnas) {
                drawRoundRect(color.copy(alpha = alfa), Offset(c * paso + (paso - modulo) / 2, f * paso + (paso - modulo) / 2), Size(modulo, modulo), radio)
            }
        }
    }
}
```

- [ ] **Step 5: `CodigoQR.kt` (ZXing, diana, logotipo, destello)**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/** DS comp. 22 «Código QR · real»: generado con ZXing (spec §0.3), fondo blanco radio 4, módulos Tinta. */
@Composable
fun CodigoQR(contenido: String, tamano: Dp, modifier: Modifier = Modifier) {
    val px = with(LocalDensity.current) { tamano.roundToPx() }
    val imagen = remember(contenido, px) { generarQR(contenido, px) }
    Box(modifier.size(tamano).background(Colores.Blanco, Radios.QR), contentAlignment = Alignment.Center) {
        Image(imagen, contentDescription = "Código QR del evento", contentScale = ContentScale.FillBounds, modifier = Modifier.size(tamano))
    }
}

private fun generarQR(contenido: String, px: Int): ImageBitmap {
    val matriz = QRCodeWriter().encode(contenido, BarcodeFormat.QR_CODE, px, px, mapOf(EncodeHintType.MARGIN to 1, EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M))
    val tinta = Colores.Tinta.toArgb()
    val blanco = Colores.Blanco.toArgb()
    val pixeles = IntArray(px * px) { i -> if (matriz[i % px, i / px]) tinta else blanco }
    return Bitmap.createBitmap(pixeles, px, px, Bitmap.Config.ARGB_8888).asImageBitmap()
}

/**
 * «ilustración · diana QR» (patrón de posición del QR, marca de la app): cuadro con trazo del 10 % del tamaño y
 * radio 25 %, centro relleno al 43 % y un módulo de acento al 22 % en la esquina superior derecha. Medido a 104.
 */
@Composable
fun DianaQR(tamano: Dp, modifier: Modifier = Modifier, color: Color = Colores.Tinta, acento: Color = Colores.AmarilloEnergia) {
    Canvas(modifier.size(tamano)) {
        val t = size.width
        val trazo = t * 0.1f
        drawRoundRect(color, Offset(trazo / 2, trazo / 2), Size(t - trazo, t - trazo), CornerRadius(t * 0.25f), style = Stroke(trazo))
        drawRoundRect(color, Offset(t * 0.283f, t * 0.283f), Size(t * 0.433f, t * 0.433f), CornerRadius(t * 0.096f))
        drawRoundRect(acento, Offset(t * 0.767f, -t * 0.017f), Size(t * 0.217f, t * 0.217f), CornerRadius(t * 0.058f))
    }
}

/** Logotipo de M00a/M00b (56×56): cuadro Amarillo Energía con la diana en Tinta (el SVG de Figma no se exporta). */
@Composable
fun Logotipo(modifier: Modifier = Modifier) {
    Box(modifier.size(Medidas.Logotipo).background(Colores.AmarilloEnergia, Radios.Tarjeta), contentAlignment = Alignment.Center) {
        DianaQR(tamano = Medidas.Logotipo * 0.6f, color = Colores.Tinta, acento = Colores.Blanco)
    }
}

/** «ilustración · destello»: estrella de cuatro puntas (M01, en blanco sobre el visor Tinta desde la v1.3). */
@Composable
fun Destello(tamano: Dp, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(tamano)) {
        val t = size.width; val c = t / 2; val r = t * 0.18f
        val camino = Path().apply {
            moveTo(c, 0f); lineTo(c + r, c - r); lineTo(t, c); lineTo(c + r, c + r); lineTo(c, t); lineTo(c - r, c + r); lineTo(0f, c); lineTo(c - r, c - r); close()
        }
        drawPath(camino, color)
    }
}
```
Las proporciones (0.1, 0.25, 0.283, 0.433, 0.096, 0.767, 0.017, 0.217, 0.058, 0.6, 0.18) son la geometría del vector medido, no medidas de diseño: se dejan en el archivo con el comentario que las explica (misma excepción que los `pathData` de `Iconos.kt`).

- [ ] **Step 6: `SnackbarDeshacer.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * DS comp. 26 «Snackbar “Deshacer”»: 350×48 Tinta radio 12, relleno 14/10, mensaje Archivo 13 blanco (2 líneas)
 * y la acción «Deshacer · 5 s» Bold 12 subrayada en un marco de 110×26 (áreas de toque, NAVEGACION §6 d).
 */
@Composable
fun SnackbarDeshacer(datos: SnackbarData, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton).height(Medidas.Snackbar)
            .background(Colores.Tinta, Radios.Snackbar).padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingTarjetaVertical)
            .testTag("snackbar"),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(datos.visuals.message, style = Tipografia.Etiqueta, color = Colores.Blanco, maxLines = 2, modifier = Modifier.weight(1f))
        datos.visuals.actionLabel?.let { rotulo ->
            Box(
                Modifier.size(Medidas.AccionSnackbar.width, Medidas.AccionSnackbar.height).clickable(role = Role.Button) { datos.performAction() }.testTag("deshacer"),
                contentAlignment = Alignment.CenterEnd,
            ) { Text(rotulo, style = Tipografia.Chip.copy(textDecoration = TextDecoration.Underline), color = Colores.Blanco) }
        }
    }
}
```

- [ ] **Step 7: `DialogoConfirmacion.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * DS comp. 47 «Diálogo de confirmación · móvil» (M04d, M06d, M11d): velo Tinta 55 % sobre la pantalla de origen,
 * diálogo de 342 (390 − 2×24), radio 20, relleno 24, gap 16; título H2, cuerpo Archivo 14/140 %, dos botones de 52
 * apilados a 10: la acción segura es el primario amarillo y la que confirma va en contorno 1.5 (Coral Texto si
 * [destructivo], Tinta si solo sale). Tocar el velo o el botón atrás equivale a la acción segura. Va en una ventana
 * propia (`Dialog`) para cubrir también la hoja inferior de M04; el atenuado del sistema se apaga para que el velo
 * sea exactamente `Colores.VeloMovil`.
 */
@Composable
fun DialogoConfirmacion(
    titulo: String, cuerpo: String, rotuloSeguro: String, rotuloConfirmar: String, destructivo: Boolean,
    alSeguro: () -> Unit, alConfirmar: () -> Unit,
) {
    Dialog(onDismissRequest = alSeguro, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        val vista = LocalView.current
        SideEffect { (vista.parent as? DialogWindowProvider)?.window?.setDimAmount(0f) }
        val sinOndas = remember { MutableInteractionSource() }
        Box(
            Modifier.fillMaxSize().background(Colores.VeloMovil).clickable(interactionSource = sinOndas, indication = null, onClick = alSeguro).testTag("velo"),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                Modifier.width(Tamanos.DialogoAncho)
                    .shadow(Elevaciones.Tarjeta, Radios.Dialogo, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
                    .background(Colores.Blanco, Radios.Dialogo)
                    .clickable(interactionSource = sinOndas, indication = null) { /* absorbe el toque: no cierra */ }
                    .padding(Espacio.PaddingDialogo).testTag("dialogo-confirmacion"),
                verticalArrangement = Arrangement.spacedBy(Espacio.GapDialogo),
            ) {
                Text(titulo, style = Tipografia.TituloDialogo, color = Colores.Tinta)
                Text(cuerpo, style = Tipografia.CuerpoDialogo, color = Colores.Tinta)
                Column(verticalArrangement = Arrangement.spacedBy(Espacio.EntreBotonesDialogo)) {
                    BotonPrimario(rotuloSeguro, onClick = alSeguro, modifier = Modifier.testTag("dialogo-seguro"))
                    val color = if (destructivo) Colores.CoralTexto else Colores.Tinta
                    OutlinedButton(
                        onClick = alConfirmar,
                        modifier = Modifier.fillMaxWidth().height(Tamanos.Boton).testTag("dialogo-confirmar"),
                        shape = Radios.Pildora,
                        border = BorderStroke(Trazos.Borde, color),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = color),
                        contentPadding = PaddingValues(horizontal = Espacio.PaddingBoton),
                    ) { Text(rotuloConfirmar, style = Tipografia.Boton) }
                }
            }
        }
    }
}
```

- [ ] **Step 8: `TarjetaEvento.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** DS comp. 25 «Sello verificado»: tramo de texto «✓ verificado» Archivo Bold 13.5 Verde Texto (M04, mockup 4:209). */
@Composable
fun SelloVerificado(modifier: Modifier = Modifier) {
    Text("✓ verificado", style = Tipografia.Dato.copy(fontWeight = FontWeight.Bold), color = Colores.VerdeTexto, modifier = modifier)
}

/**
 * Tarjeta del evento de M04 (mockup 4:201): 350×144, blanco, borde 1.5 Gris Borde, radio 14, relleno 16/14, gap 8,
 * sombra 0 4 12; título Bricolage SemiBold 20 y filas FECHA · LUGAR · ORGANIZA · DETALLE (etiqueta Bold 12 +8 % a
 * 74 de ancho, valor Archivo 13.5).
 */
@Composable
fun TarjetaEvento(alarma: Alarma, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth()
            .shadow(Elevaciones.Tarjeta, Radios.Tarjeta, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
            .background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta)
            .padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingTarjeta),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapTarjeta),
    ) {
        Text(alarma.titulo, style = Tipografia.TituloEvento, color = Colores.Tinta)
        FilaDato("FECHA") { Text(FormatoHora.fechaLarga(alarma.eventoInicio), style = Tipografia.Dato, color = Colores.Tinta) }
        alarma.lugar?.let { lugar -> FilaDato("LUGAR") { Text(lugar, style = Tipografia.Dato, color = Colores.Tinta) } }
        alarma.organizador?.let { org ->
            FilaDato("ORGANIZA") {
                Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapDivisor)) {
                    Text(org.nombre, style = Tipografia.Dato, color = Colores.Tinta)
                    if (org.verificado) SelloVerificado()
                }
            }
        }
        alarma.detalle?.let { detalle -> FilaDato("DETALLE") { Text("$detalle.", style = Tipografia.Dato, color = Colores.Tinta) } }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila)) {
        Text(etiqueta, style = Tipografia.EtiquetaDato, color = Colores.GrisTexto, modifier = Modifier.width(Medidas.EtiquetaDato))
        valor()
    }
}
```

- [ ] **Step 9: Correr las pruebas**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
```
Esperado: verde. Si `DialogoConfirmacionTest` no encuentra `velo`, comprobar que `createComposeRule` incluye las ventanas de diálogo (lo hace por defecto) y que el `Box` del velo no queda debajo de otro `clickable`.

- [ ] **Step 10: Commit**

```bash
git add apps/movil packages/tokens
git commit -m "L06–L08: banda de textura, QR con ZXing, diana, snackbar Deshacer, DialogoConfirmacion y tarjeta del evento"
```

---

### Task 5: Ayudante de captura, `entradasApp` y M01 Bienvenida

**Files:**
- Create: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/Verificacion.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/EntradasApp.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/m01/M01BienvenidaScreen.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/MainActivity.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m01/M01BienvenidaScreenTest.kt`
- Create: `docs/verificacion/README.md`

**Interfaces:**
- Consumes: Tareas 1–4; `HojaInferiorSceneStrategy.hoja()`; `NavBackStack` helpers (`irA`, `reemplazarTodo`, `reemplazarCima`).
- Produces:
  - `fun ComposeContentTestRule.capturar(nombre: String)` → `app/build/verificacion/<nombre>.png`; constante `QUALIFIERS_MOVIL = "w390dp-h844dp-xhdpi"`.
  - `fun EntryProviderScope<NavKey>.entradasApp(pila: NavBackStack<NavKey>, repositorio: RepositorioDataset)` — registro único de las pantallas reales; cada tarea de pantalla agrega su `entry<…>`.
  - `M01BienvenidaScreen(alComenzar: () -> Unit, alConectarLuego: () -> Unit, modifier: Modifier = Modifier)` (testTag `pantalla-M01`).
  - Tokens: `Tipografia.Titular` (Bricolage Bold 32), `Tipografia.Parrafo` (Archivo 15 / 21), `Colores.Tinta25`, `Medidas.PuntoPagina = 6.dp`, `Espacio.AntesBoton = 28.dp`, `Espacio.PaddingPantallaSuperior = 24.dp`, `Espacio.PaddingPantallaInferior = 16.dp` (JSON: `typography.titular {titulares,700,32}`, `typography.parrafo {ui,400,15,lineHeight 21}`, `color.tinta-25 rgba(23,22,28,0.25)`, `size.movil.punto-pagina 6`, `space.movil.antes-boton 28`, `padding-pantalla: [24, 16]`).

- [ ] **Step 1: Ayudante de captura (test)**

`apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/Verificacion.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import java.io.File
import java.io.FileOutputStream

/** Marco de los mockups: 390×844 a 2× (Robolectric, `@Config(qualifiers = QUALIFIERS_MOVIL)`). */
const val QUALIFIERS_MOVIL = "w390dp-h844dp-xhdpi"

/**
 * Captura de verificación pixel-perfect (spec §5.2, D2): guarda la raíz compuesta en build/verificacion/<nombre>.png
 * para compararla con la exportación del marco de Figma. No afirma nada: la comparación es visual y la pareja
 * Figma / implementación se copia a docs/verificacion/ al cerrar cada pantalla.
 */
fun ComposeContentTestRule.capturar(nombre: String) {
    waitForIdle()
    val mapa = onRoot().captureToImage().asAndroidBitmap()
    val carpeta = File("build/verificacion").apply { mkdirs() }
    FileOutputStream(carpeta.resolve("$nombre.png")).use { mapa.compress(Bitmap.CompressFormat.PNG, 100, it) }
}
```

- [ ] **Step 2: Prueba de M01 (falla)**

`M01BienvenidaScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M01BienvenidaScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `muestra los textos del mockup, alterna calendarios y avisa las dos salidas`() {
        var comenzar = 0; var luego = 0
        regla.setContent { AlarmasQRTheme { M01BienvenidaScreen(alComenzar = { comenzar++ }, alConectarLuego = { luego++ }) } }
        regla.onNodeWithTag("pantalla-M01").assertIsDisplayed()
        regla.onNodeWithText("Escanea y listo").assertIsDisplayed()
        regla.onNodeWithText("Apunta la cámara al QR del evento: la alarma queda programada sin escribir fecha, hora ni nombre.").assertIsDisplayed()
        regla.onNodeWithText("CONECTA TU CALENDARIO (OPCIONAL)").assertIsDisplayed()
        regla.onNodeWithTag("calendario-google").assertIsOff().performClick().assertIsOn()
        regla.onNodeWithTag("calendario-outlook").assertIsOff()
        regla.onNodeWithTag("calendario-telefono").assertIsOff()
        regla.capturar("M01")
        regla.onNodeWithText("Comenzar").performClick()
        regla.onNodeWithText("Conectar luego en Ajustes").performClick()
        assertEquals(1, comenzar); assertEquals(1, luego)
    }
}
```

- [ ] **Step 3: Correr para ver el fallo**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests '*M01BienvenidaScreenTest' --no-daemon
```

- [ ] **Step 4: Tokens de esta tarea** (ver «Produces»; aplicar en los tres archivos de tokens).

- [ ] **Step 5: `M01BienvenidaScreen.kt`**

Medidas: anexo §1 (marco 3:71). Fondo Amarillo Energía, banda de textura Tinta 120 detrás; columna con relleno 24/16/20 y gap 12: indicador de página (22×6 Tinta + 2 × 6×6 Tinta 25 %), visor 350×170 Tinta r20 con textura blanca, QR 84 y dos destellos blancos (40 en (22,18), 24 en (298,110)), «Escanea y listo» Bricolage Bold 32, párrafo 15, rótulo H3, tres filas de opción, espaciador 28, «Comenzar» (primario sobre amarillo = Tinta) y el enlace Tinta.

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m01

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.Destello
import co.edu.uniandes.alarmasqr.ui.componentes.FilaOpcionCalendario
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M01 · Bienvenida (F-M01): onboarding en amarillo pleno; «Comenzar» → M00a, «Conectar luego en Ajustes» → M02v. */
@Composable
fun M01BienvenidaScreen(alComenzar: () -> Unit, alConectarLuego: () -> Unit, modifier: Modifier = Modifier) {
    var google by rememberSaveable { mutableStateOf(false) }
    var outlook by rememberSaveable { mutableStateOf(false) }
    var telefono by rememberSaveable { mutableStateOf(false) }
    Box(modifier.fillMaxSize().background(Colores.AmarilloEnergia).testTag("pantalla-M01")) {
        BandaTextura(Modifier.align(Alignment.TopCenter))
        Column(
            Modifier.fillMaxSize().padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingPantallaSuperior, bottom = Espacio.PaddingPantallaInferior),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IndicadorPagina()
            VisorBienvenida()
            Text("Escanea y listo", style = Tipografia.Titular, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(
                "Apunta la cámara al QR del evento: la alarma queda programada sin escribir fecha, hora ni nombre.",
                style = Tipografia.Parrafo, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            )
            Text("CONECTA TU CALENDARIO (OPCIONAL)", style = Tipografia.H3, color = Colores.Tinta, modifier = Modifier.fillMaxWidth())
            FilaOpcionCalendario(Iconos.Google, "Google Calendar", google, { google = it }, Modifier.testTag("calendario-google"))
            FilaOpcionCalendario(Iconos.Outlook, "Outlook · Teams", outlook, { outlook = it }, Modifier.testTag("calendario-outlook"))
            FilaOpcionCalendario(Iconos.Telefono, "Calendario del teléfono", telefono, { telefono = it }, Modifier.testTag("calendario-telefono"))
            Spacer(Modifier.height(Espacio.AntesBoton))
            BotonPrimario("Comenzar", onClick = alComenzar, sobreAmarillo = true)
            BotonEnlace("Conectar luego en Ajustes", onClick = alConectarLuego, color = ColorEnlace.Tinta)
        }
    }
}

/** Indicador de página del mockup (3:73): activo 22×6 Tinta, dos puntos 6×6 Tinta 25 %, gap 6. */
@Composable
private fun IndicadorPagina() {
    Row(horizontalArrangement = Arrangement.spacedBy(Medidas.PuntoPagina), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(Medidas.IndicadorPagina.width, Medidas.IndicadorPagina.height).background(Colores.Tinta, Radios.Pildora))
        repeat(2) { Box(Modifier.size(Medidas.PuntoPagina).background(Colores.Tinta25, Radios.Pildora)) }
    }
}

/** Visor Tinta 350×170 r20 (3:77) con textura blanca, QR de 84 centrado y dos destellos blancos. */
@Composable
private fun VisorBienvenida() {
    Box(Modifier.fillMaxWidth().height(Medidas.VisorBienvenida).clip(Radios.VisorBienvenida).background(Colores.Tinta), contentAlignment = Alignment.Center) {
        BandaTextura(Modifier.align(Alignment.TopCenter), sobreTinta = true, alto = Medidas.VisorBienvenida)
        CodigoQR("alarmasqr://evento/e-entrega", tamano = Medidas.QRBienvenida)
        Destello(tamano = Medidas.CajaIcono, color = Colores.Blanco, modifier = Modifier.align(Alignment.TopStart).offset(x = 22.dp, y = 18.dp))
        Destello(tamano = Tamanos.Icono, color = Colores.Blanco, modifier = Modifier.align(Alignment.TopStart).offset(x = 298.dp, y = 110.dp))
    }
}
```
Los cuatro `offset` de los destellos son posiciones del vector medido (anexo §1, capas 4013:2900 y 4013:2902): dejarlos con el comentario, como las proporciones de `DianaQR`.

- [ ] **Step 6: `EntradasApp.kt` y `MainActivity`**

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
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
```

En `MainActivity.kt`, cambiar la llamada a `NavegacionApp(backStack = pila, repositorio = repositorio) { entradasApp(pila, repositorio) }` (import `co.edu.uniandes.alarmasqr.navegacion.entradasApp`).

- [ ] **Step 7: `docs/verificacion/README.md`**

```markdown
# Verificación pixel-perfect (Plan 2)

Cada pantalla de la Persona A tiene una pareja de imágenes: la exportación del marco de Figma a 390×844 (`<código>-figma.png`)
y la captura de la implementación (`<código>.png`, generada por la prueba de la pantalla en `apps/movil/app/build/verificacion/`
con `capturar("<código>")`, Robolectric a 2×). Se comparan a ojo (spec §5.2, decisión D2 del plan) con la lista de
comprobación de `docs/PLAN_MAQUETACION.md` §7. Exportar el marco desde Figma: `get_screenshot` del MCP (o «Export» del
marco) con los ids de `docs/MOCKUPS.md` §5.

| Código | Marco Figma | Estado |
|---|---|---|
| M01 | 3:71 | pendiente |
| M00a | 3:2 | pendiente |
| M00b | 3:37 | pendiente |
| M02v | 4020:3553 | pendiente |
| M02 | 3:131 | pendiente |
| M02h | 4019:3139 | pendiente |
| M12 | 6:87 | pendiente |
| M13 | 6:122 | pendiente |
| M03 | 4:135 | pendiente |
| M03b | 4020:3295 | pendiente |
| M04 | 4:189 | pendiente |
| M04d | 4330:1432 | pendiente |
| M05 | 4:223 | pendiente |
```

- [ ] **Step 8: Correr las pruebas y mirar la captura**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon && ls -la app/build/verificacion/
```
Esperado: verde; existe `M01.png`. Abrir la captura (herramienta Read sobre el PNG) y compararla con la captura de Figma del marco 3:71 (`get_screenshot`, fileKey `4nHD4ygcnP33UH0gAhaii5`): mismos bloques, mismos textos, un solo amarillo (el fondo) y el botón en Tinta. Corregir diferencias de relleno o tamaño antes de continuar; copiar la pareja a `docs/verificacion/M01.png` y `M01-figma.png` y marcar la fila como «ok».

- [ ] **Step 9: Commit**

```bash
git add apps/movil packages/tokens docs/verificacion
git commit -m "M01: pantalla de bienvenida, registro entradasApp y ayudante de captura"
```

---

### Task 6: M00a Crear cuenta y M00b Iniciar sesión

**Files:**
- Create: `ui/pantallas/m00/FormularioAcceso.kt`, `ui/pantallas/m00/M00aRegistroScreen.kt`, `ui/pantallas/m00/M00bEntrarScreen.kt`
- Modify: `navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m00/M00AccesoScreenTest.kt`

**Interfaces:**
- Consumes: `CampoTexto`, `Casilla`, `BotonPrimario`, `BotonSecundario`, `BotonEnlace`, `Divisor`, `Logotipo`, `capturar`.
- Produces:
  - `M00aRegistroScreen(alCrearCuenta: () -> Unit, alInvitado: () -> Unit, alYaTengoCuenta: () -> Unit, modifier)` (testTag `pantalla-M00a`; botones con testTag `crear-cuenta`, `google`, `outlook`, `invitado`, `pie-acceso`).
  - `M00bEntrarScreen(correoInicial: String, alEntrar: () -> Unit, alInvitado: () -> Unit, alCrearCuenta: () -> Unit, alRecuperar: () -> Unit, modifier)` (testTag `pantalla-M00b`; `entrar`, `google`, `outlook`, `invitado`, `pie-acceso`, `recuperar`).
  - `BotonesTerceros(alGoogle: () -> Unit, alOutlook: () -> Unit)`, `PieAcceso(prefijo: String, enlace: String, onClick: () -> Unit)` (44 de alto, dos tramos SemiBold 13 Tinta + Bold 13 Azul Texto).
  - Tokens: `Tipografia.PieAcceso` (Archivo SemiBold 13), `Tipografia.NotaLarga` (Archivo 12 / 17, para el consentimiento) → JSON `typography.pie-acceso {ui,600,13}`, `typography.nota-larga {ui,400,12,lineHeight 17}`; `Espacio.PaddingAccesoSuperior = 28.dp` (JSON `space.movil.padding-acceso-superior: 28`).

- [ ] **Step 1: Prueba (falla)**

`M00AccesoScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m00

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M00AccesoScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `M00a muestra el formulario de registro y sus cuatro salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent { AlarmasQRTheme { M00aRegistroScreen(alCrearCuenta = { salidas += "crear" }, alInvitado = { salidas += "invitado" }, alYaTengoCuenta = { salidas += "entrar" }) } }
        regla.onNodeWithTag("pantalla-M00a").assertIsDisplayed()
        regla.onNodeWithText("Crea tu cuenta").assertIsDisplayed()
        regla.onNodeWithText("Respalda tus alarmas en la nube y úsalas también en la web.").assertIsDisplayed()
        regla.onNodeWithText("CORREO").assertIsDisplayed()
        regla.onNodeWithText("CONTRASEÑA").assertIsDisplayed()
        regla.onNodeWithText("Acepto el tratamiento de mis datos según la política de privacidad (Ley 1581 de 2012).").assertIsDisplayed()
        regla.onNodeWithText("o continúa con").assertIsDisplayed()
        regla.onNodeWithText("Como invitado, las alarmas quedan solo en este teléfono.").assertIsDisplayed()
        regla.capturar("M00a")
        regla.onNodeWithTag("crear-cuenta").performClick()
        regla.onNodeWithTag("google").performClick()
        regla.onNodeWithTag("invitado").performClick()
        regla.onNodeWithTag("pie-acceso").performClick()
        assertEquals(listOf("crear", "crear", "invitado", "entrar"), salidas)
    }

    @Test
    fun `M00b muestra el inicio de sesion con el correo del usuario y sus salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M00bEntrarScreen("andres@correo.com", alEntrar = { salidas += "entrar" }, alInvitado = { salidas += "invitado" }, alCrearCuenta = { salidas += "crear" }, alRecuperar = { salidas += "recuperar" })
            }
        }
        regla.onNodeWithTag("pantalla-M00b").assertIsDisplayed()
        regla.onNodeWithText("Hola de nuevo").assertIsDisplayed()
        regla.onNodeWithText("Entra para sincronizar tus alarmas con la nube y la web.").assertIsDisplayed()
        regla.onNodeWithText("andres@correo.com").assertIsDisplayed()
        regla.capturar("M00b")
        regla.onNodeWithTag("recuperar").performClick()
        regla.onNodeWithTag("entrar").performClick()
        regla.onNodeWithTag("outlook").performClick()
        regla.onNodeWithTag("invitado").performClick()
        regla.onNodeWithTag("pie-acceso").performClick()
        assertEquals(listOf("recuperar", "entrar", "entrar", "invitado", "crear"), salidas)
    }
}
```

- [ ] **Step 2: Correr para ver el fallo** (`--tests '*M00AccesoScreenTest'`).

- [ ] **Step 3: Tokens de esta tarea** (ver «Produces»).

- [ ] **Step 4: `FormularioAcceso.kt`**

```kotlin
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
```

- [ ] **Step 5: `M00aRegistroScreen.kt`**

Medidas: anexo §2 (3:2). Columna blanca, relleno 28/32/20, gap 12, espaciadores flexibles arriba y abajo del bloque (tutores v1.6: formulario centrado), pie de 44 al final.

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m00

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
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

/** M00a · Crear cuenta (F-M00a): registro opcional; «Crear cuenta», Google, Outlook e invitado → M02v; pie → M00b. */
@Composable
fun M00aRegistroScreen(alCrearCuenta: () -> Unit, alInvitado: () -> Unit, alYaTengoCuenta: () -> Unit, modifier: Modifier = Modifier) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var consentimiento by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M00a")
            .padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingAccesoSuperior, bottom = Espacio.PieEnlace),
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
```

- [ ] **Step 6: `M00bEntrarScreen.kt`**

Medidas: anexo §3 (3:37). Igual que M00a sin casilla ni nota; «¿Olvidaste tu contraseña?» alineado a la derecha en una fila de 32; pie «¿Primera vez aquí? Crea tu cuenta».

```kotlin
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
```

- [ ] **Step 7: Registrar en `entradasApp`**

```kotlin
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
```

- [ ] **Step 8: Correr, verificar capturas (M00a, M00b contra 3:2 y 3:37), commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
git add apps/movil packages/tokens docs/verificacion && git commit -m "M00a/M00b: crear cuenta e iniciar sesión"
```

---

### Task 7: M02 Inicio (lista), M02v (estado vacío) y hoja M02h «Agregar evento»

**Files:**
- Create: `ui/pantallas/m02/M02InicioViewModel.kt`, `ui/pantallas/m02/M02InicioScreen.kt`, `ui/pantallas/m02/M02hAgregarEventoSheet.kt`
- Modify: `datos/RepositorioDataset.kt` (`cambiarEstado`), `navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/AgruparPorDiaTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M02InicioScreenTest.kt`

**Interfaces:**
- Consumes: `TarjetaAlarma`, `AgrupadorDia`, `BarraSuperior`, `DianaQR`, botones, `LocalSnackbarApp` (Tarea 13 lo usa), `FormatoHora`.
- Produces:
  - `data class GrupoDia(val etiqueta: String, val alarmas: List<Alarma>)`; `fun agruparPorDia(alarmas: List<Alarma>, hoy: LocalDate): List<GrupoDia>`.
  - `data class EstadoInicio(val grupos: List<GrupoDia>, val alarmaNueva: String? = null)`.
  - `class M02InicioViewModel(repositorio: RepositorioDataset) : ViewModel { val estado: StateFlow<EstadoInicio>; fun cambiarActiva(id: String, activa: Boolean); fun deshacer() }`.
  - `M02InicioScreen(estado: EstadoInicio, alTocarAlarma: (String) -> Unit, alCambiarActiva: (String, Boolean) -> Unit, modifier: Modifier = Modifier, codigo: String = "M02")` (testTag `pantalla-<codigo>`).
  - `M02vEstadoVacio(mensajes: Mensajes, alEscanear: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit, modifier)` (testTag `pantalla-M02v`; botones `vacio-escanear`, `vacio-pantallazo`, `vacio-a-mano`).
  - `M02hAgregarEventoSheet(alEscanear: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit)` (testTag `pantalla-M02h`; filas `hoja-escanear`, `hoja-pantallazo`, `hoja-a-mano`).
  - `RepositorioDataset.cambiarEstado(id: String, pausada: Boolean)`.
  - Tokens: `Tipografia.TituloVacio` (Bricolage Bold 24), `Tipografia.Chevron` (Archivo Bold 20), `Tipografia.NotaHoja` (Archivo 13 / 17.5); `Espacio.VacioSuperior = 64.dp`, `Espacio.VacioEntre = 8.dp`, `Espacio.HojaInferiorCorta = 28.dp`, `Espacio.PaddingFilaHoja = 16.dp`, `Espacio.GapFilaHoja = 14.dp`, `Espacio.GapTextoHoja = 2.dp` (JSON: `typography.titulo-vacio {titulares,700,24}`, `typography.chevron {ui,700,20}`, `typography.nota-hoja {ui,400,13,lineHeight 17.5}`, `space.movil.vacio-superior 64`, `vacio-entre 8`, `hoja-inferior-corta 28`, `padding-fila-hoja 16`, `gap-fila-hoja 14`, `gap-texto-hoja 2`).

- [ ] **Step 1: Pruebas (fallan)**

`AgruparPorDiaTest.kt` (JUnit puro):

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class AgruparPorDiaTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `agrupa las 5 alarmas iniciales en HOY, MANANA y LUNES 31`() {
        val grupos = agruparPorDia(repo.alarmas.value, repo.hoy)
        assertEquals(listOf("HOY · JUEVES 27", "MAÑANA · VIERNES 28", "LUNES 31"), grupos.map { it.etiqueta })
        assertEquals(listOf("a-tutor", "a-gimnasio"), grupos[0].alarmas.map { it.id })
        assertEquals(listOf("a-vuelo", "a-semillero"), grupos[1].alarmas.map { it.id })
    }

    @Test
    fun `tras escanear aparece DOMINGO 30 antes del lunes`() {
        repo.agregarDesdeEvento("e-entrega")
        val grupos = agruparPorDia(repo.alarmas.value, repo.hoy)
        assertEquals(listOf("HOY · JUEVES 27", "MAÑANA · VIERNES 28", "DOMINGO 30", "LUNES 31"), grupos.map { it.etiqueta })
    }
}
```

`M02InicioScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M02InicioScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `la lista agrupa por dia, muestra las 5 tarjetas y avisa el toque con el id`() {
        var tocada = ""
        val estado = EstadoInicio(agruparPorDia(repo.alarmas.value, repo.hoy))
        regla.setContent { AlarmasQRTheme { M02InicioScreen(estado, alTocarAlarma = { tocada = it }, alCambiarActiva = { _, _ -> }) } }
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        regla.onNodeWithText("Mis alarmas").assertIsDisplayed()
        regla.onNodeWithText("HOY · JUEVES 27").assertIsDisplayed()
        regla.onNodeWithText("MAÑANA · VIERNES 28").assertIsDisplayed()
        regla.onNodeWithText("Vuelo BOG–MDE").assertIsDisplayed()
        regla.onNodeWithText("✓ Escaneada").assertIsDisplayed()
        regla.capturar("M02")
        regla.onNodeWithTag("alarma-a-semillero").performClick()
        assertEquals("a-semillero", tocada)
    }

    @Test
    fun `el estado vacio muestra el mensaje del dataset y tres salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent { AlarmasQRTheme { M02vEstadoVacio(repo.dataset.mensajes, alEscanear = { salidas += "escanear" }, alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" }) } }
        regla.onNodeWithTag("pantalla-M02v").assertIsDisplayed()
        regla.onNodeWithText("Aún no tienes alarmas").assertIsDisplayed()
        regla.onNodeWithText(repo.dataset.mensajes.sinAlarmasDetalle).assertIsDisplayed()
        regla.capturar("M02v")
        regla.onNodeWithTag("vacio-escanear").performClick()
        regla.onNodeWithTag("vacio-pantallazo").performClick()
        regla.onNodeWithTag("vacio-a-mano").performClick()
        assertEquals(listOf("escanear", "pantallazo", "a-mano"), salidas)
    }

    @Test
    fun `la hoja Agregar evento tiene tres filas y el aviso del pantallazo`() {
        val salidas = mutableListOf<String>()
        regla.setContent { AlarmasQRTheme { M02hAgregarEventoSheet(alEscanear = { salidas += "escanear" }, alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" }) } }
        regla.onNodeWithTag("pantalla-M02h").assertIsDisplayed()
        regla.onNodeWithText("Agregar evento").assertIsDisplayed()
        regla.onNodeWithText("La alarma queda lista sin escribir nada").assertIsDisplayed()
        regla.onNodeWithText("Leemos el QR que aparezca en la imagen").assertIsDisplayed()
        regla.onNodeWithText("Escribe fecha, hora y lugar; te damos su QR").assertIsDisplayed()
        regla.onNodeWithText("También puedes compartir un pantallazo desde WhatsApp o la galería con Alarmas QR.").assertIsDisplayed()
        regla.capturar("M02h-contenido")
        regla.onNodeWithTag("hoja-escanear").performClick()
        regla.onNodeWithTag("hoja-pantallazo").performClick()
        regla.onNodeWithTag("hoja-a-mano").performClick()
        assertEquals(listOf("escanear", "pantallazo", "a-mano"), salidas)
    }
}
```

- [ ] **Step 2: Correr para ver el fallo** (`--tests '*m02*'`).

- [ ] **Step 3: Tokens de esta tarea** (ver «Produces») y `RepositorioDataset.cambiarEstado`:

```kotlin
    /** Interruptor de la tarjeta: pausa o reactiva sin afectar «Deshacer» (no pasa por `mutar`). */
    fun cambiarEstado(id: String, pausada: Boolean) {
        _alarmas.value = _alarmas.value.map { if (it.id == id) it.copy(estado = if (pausada) "pausada" else "activa") else it }
    }
```

- [ ] **Step 4: `M02InicioViewModel.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class GrupoDia(val etiqueta: String, val alarmas: List<Alarma>)

/** Estado de M02/M02v/M05: grupos por día y, en M05, el id de la alarma recién guardada. */
data class EstadoInicio(val grupos: List<GrupoDia>, val alarmaNueva: String? = null)

/** Agrupa en orden cronológico con los rótulos de FormatoHora («HOY · JUEVES 27»…); la lista ya viene ordenada. */
fun agruparPorDia(alarmas: List<Alarma>, hoy: LocalDate): List<GrupoDia> =
    alarmas.groupBy { FormatoHora.dia(it.eventoInicio) }.entries.sortedBy { it.key }
        .map { (_, lista) -> GrupoDia(FormatoHora.etiquetaDia(lista.first().eventoInicio, hoy), lista) }

/** ViewModel compartido por M02, M02v y M05 (misma pantalla, tres claves); vive en la entrada de Navigation 3. */
class M02InicioViewModel(private val repositorio: RepositorioDataset, private val alarmaNueva: String? = null) : ViewModel() {
    val estado: StateFlow<EstadoInicio> = repositorio.alarmas
        .map { EstadoInicio(agruparPorDia(it, repositorio.hoy), alarmaNueva) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, EstadoInicio(agruparPorDia(repositorio.alarmas.value, repositorio.hoy), alarmaNueva))

    fun cambiarActiva(id: String, activa: Boolean) = repositorio.cambiarEstado(id, pausada = !activa)

    fun deshacer() = repositorio.deshacer()
}
```

- [ ] **Step 5: `M02InicioScreen.kt` (lista + estado vacío)**

Medidas: anexo grupo 2 §0–§1 (3:131) y grupo 1 §4 (4020:3553).

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.AgrupadorDia
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.DianaQR
import co.edu.uniandes.alarmasqr.ui.componentes.TarjetaAlarma
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/**
 * M02 · Inicio · lista (F-M02) y M05 (misma pantalla con la alarma nueva resaltada; el snackbar lo muestra la
 * entrada, Tarea 13). Barra «Mis alarmas» sin flecha, lista con relleno 20/16 y gap 12, agrupadores de día y
 * tarjetas; el FAB y la barra inferior los pone NavegacionApp.
 */
@Composable
fun M02InicioScreen(estado: EstadoInicio, alTocarAlarma: (String) -> Unit, alCambiarActiva: (String, Boolean) -> Unit, modifier: Modifier = Modifier, codigo: String = "M02") {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-$codigo")) {
        BarraSuperior("Mis alarmas")
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        ) {
            estado.grupos.forEach { grupo ->
                item(key = grupo.etiqueta) { AgrupadorDia(grupo.etiqueta) }
                items(grupo.alarmas, key = { it.id }) { alarma ->
                    TarjetaAlarma(alarma, onClick = { alTocarAlarma(alarma.id) }, alCambiarActiva = { alCambiarActiva(alarma.id, it) })
                }
            }
        }
    }
}

/**
 * M02v · Inicio sin alarmas (primer uso; DS comp. 30 «Estado vacío»): diana QR 104, «Aún no tienes alarmas» 24,
 * párrafo del dataset, primario «Escanear QR del evento» (→ M12 ⏩) y dos secundarios (→ M03b, → M07).
 */
@Composable
fun M02vEstadoVacio(mensajes: Mensajes, alEscanear: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M02v")) {
        BarraSuperior("Mis alarmas")
        Column(
            Modifier.fillMaxSize().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(Espacio.VacioSuperior))
            DianaQR(tamano = Medidas.Diana)
            Spacer(Modifier.height(Espacio.VacioEntre))
            Text(mensajes.sinAlarmas, style = Tipografia.TituloVacio, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(mensajes.sinAlarmasDetalle, style = Tipografia.Parrafo, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(Espacio.VacioEntre))
            BotonPrimario("Escanear QR del evento", onClick = alEscanear, modifier = Modifier.testTag("vacio-escanear"))
            BotonSecundario("Elegir pantallazo de la galería", onClick = alElegirPantallazo, modifier = Modifier.testTag("vacio-pantallazo"))
            BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("vacio-a-mano"))
        }
    }
}
```

- [ ] **Step 6: `M02hAgregarEventoSheet.kt`**

Medidas: anexo grupo 2 §2 (4019:3139). La asa y el radio 24 los pone `HojaInferiorSceneStrategy` (ModalBottomSheet); aquí va el contenido: relleno 20 lateral y 28 inferior, gap 6; título Bricolage Bold 22; tres filas de 100 (primera Amarillo Energía sin borde, caja de icono blanca; las otras contorno 1.5 Gris Borde con caja Gris Niebla); aviso de pie.

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M02h · Hoja «Agregar evento» (F-M02): única entrada de captura; la primera fila es el único amarillo de la hoja. */
@Composable
fun M02hAgregarEventoSheet(alEscanear: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(start = Espacio.Margen, end = Espacio.Margen, bottom = Espacio.HojaInferiorCorta).testTag("pantalla-M02h"),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapHoja),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Agregar evento", style = Tipografia.BarraSuperior, color = Colores.Tinta, modifier = Modifier.fillMaxWidth())
        FilaAgregar(Iconos.Escanear, "Escanear el QR del evento", "La alarma queda lista sin escribir nada", primaria = true, onClick = alEscanear, modifier = Modifier.testTag("hoja-escanear"))
        FilaAgregar(Iconos.Galeria, "Elegir pantallazo de la galería", "Leemos el QR que aparezca en la imagen", primaria = false, onClick = alElegirPantallazo, modifier = Modifier.testTag("hoja-pantallazo"))
        FilaAgregar(Iconos.Mas, "Crear el evento a mano", "Escribe fecha, hora y lugar; te damos su QR", primaria = false, onClick = alCrearAMano, modifier = Modifier.testTag("hoja-a-mano"))
        Text(
            "También puedes compartir un pantallazo desde WhatsApp o la galería con Alarmas QR.",
            style = Tipografia.NotaHoja, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Fila de 350×100: caja de icono 40 r12 + título Bold 16 + subtítulo 13 + chevrón «›» Bold 20. */
@Composable
private fun FilaAgregar(icono: ImageVector, titulo: String, subtitulo: String, primaria: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val texto = if (primaria) Colores.Tinta else Colores.GrisTexto
    Row(
        modifier.fillMaxWidth().height(Medidas.FilaHoja).clip(Radios.Tarjeta)
            .background(if (primaria) Colores.AmarilloEnergia else Colores.Blanco)
            .then(if (primaria) Modifier else Modifier.border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = Espacio.PaddingFilaHoja, vertical = Espacio.EntreBloques),
        horizontalArrangement = Arrangement.spacedBy(Espacio.GapFilaHoja),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(Medidas.CajaIcono).background(if (primaria) Colores.Blanco else Colores.GrisNiebla, Radios.CajaIcono), contentAlignment = Alignment.Center) {
            Icon(icono, contentDescription = null, tint = Colores.Tinta, modifier = Modifier.size(Tamanos.Icono))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espacio.GapTextoHoja)) {
            Text(titulo, style = Tipografia.Destacado, color = Colores.Tinta)
            Text(subtitulo, style = Tipografia.Etiqueta, color = texto)
        }
        Text("›", style = Tipografia.Chevron, color = texto)
    }
}
```

- [ ] **Step 7: Registrar en `entradasApp`**

```kotlin
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
```
Imports: `androidx.lifecycle.viewmodel.compose.viewModel`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `androidx.compose.runtime.getValue`.

- [ ] **Step 8: Correr todo, verificar capturas (M02 contra 3:131, M02v contra 4020:3553; M02h se captura completa en la Tarea 14 desde el flujo), commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
git add apps/movil packages/tokens docs/verificacion && git commit -m "M02/M02v/M02h: lista de alarmas por día, estado vacío y hoja Agregar evento"
```

---

### Task 8: M12 Permiso de cámara (permiso real) y M13 QR sin evento

**Files:**
- Create: `navegacion/PermisoCamara.kt`, `ui/pantallas/m12/M12PermisoCamaraScreen.kt`, `ui/pantallas/m13/M13QRInvalidoScreen.kt`
- Modify: `ui/componentes/BandaTextura.kt` (parámetro `opacidad`), `navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m12/M12PermisoCamaraScreenTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m13/M13QRInvalidoScreenTest.kt`

**Interfaces:**
- Consumes: `BarraSuperior`, `BandaTextura`, `Divisor`, botones, `ChipEstado(VarianteChip.Coral)`, `Iconos.Escanear`, `QRInvalido` del dataset.
- Produces:
  - `fun tienePermisoCamara(context: Context): Boolean`; `@Composable fun rememberSolicitudPermisoCamara(alTerminar: () -> Unit): () -> Unit` (D5).
  - `M12PermisoCamaraScreen(alVolver: () -> Unit, alAbrirAjustes: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit, modifier)` (testTag `pantalla-M12`; botones `abrir-ajustes`, `permiso-pantallazo`, `permiso-a-mano`).
  - `M13QRInvalidoScreen(diagnostico: QRInvalido, alVolver: () -> Unit, alVolverAEscanear: () -> Unit, alCrearAMano: () -> Unit, alAbrirEnlace: () -> Unit, modifier)` (testTag `pantalla-M13`; `volver-a-escanear`, `invalido-a-mano`, `abrir-enlace`).
  - `BandaTextura(..., opacidad: Float = 1f)`; token `Movimiento.TexturaAtenuada = 0.5f` (JSON `texture.modulos-qr.opacityTitulares: 0.5`; MOCKUPS §3c paso 4: «banda al 4 % detrás de los titulares»).
  - Tokens: `Espacio.PaddingPermisoSuperior = 14.dp`, `Espacio.GapPermiso = 11.dp`, `Espacio.GapPasos = 7.dp`, `Espacio.PaddingPasosVertical = 12.dp`, `Espacio.AntesAcciones = 32.dp` (JSON `space.movil.padding-permiso-superior 14`, `gap-permiso 11`, `gap-pasos 7`, `padding-pasos-vertical 12`, `antes-acciones 32`); `Tipografia.Codigo` (Spline Sans Mono Medium 12, = `HoraAmPm`; JSON `typography.codigo {datos,500,12}`), `Tipografia.EnlaceCorto` (Archivo Bold 14 subrayado; JSON `typography.enlace-corto {ui,700,14}`).

- [ ] **Step 1: Pruebas (fallan)**

`M12PermisoCamaraScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m12

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M12PermisoCamaraScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `explica el permiso en tres pasos y ofrece las alternativas abajo`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M12PermisoCamaraScreen(alVolver = { salidas += "volver" }, alAbrirAjustes = { salidas += "ajustes" }, alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" })
            }
        }
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.onNodeWithText("Permiso de cámara").assertIsDisplayed()
        regla.onNodeWithText("La cámara está apagada para la app").assertIsDisplayed()
        regla.onNodeWithText("Solo la usamos para leer códigos QR de eventos. Nunca guardamos fotos ni videos.").assertIsDisplayed()
        regla.onNodeWithText("ACTÍVALA EN 3 PASOS").assertIsDisplayed()
        regla.onNodeWithText("Permisos › Cámara").assertIsDisplayed()
        regla.onNodeWithText("Elegir “Permitir con la app en uso”").assertIsDisplayed()
        regla.onNodeWithText("mientras tanto").assertIsDisplayed()
        regla.capturar("M12")
        regla.onNodeWithTag("abrir-ajustes").performClick()
        regla.onNodeWithTag("permiso-pantallazo").performClick()
        regla.onNodeWithTag("permiso-a-mano").performClick()
        regla.onNodeWithTag("atras").performClick()
        assertEquals(listOf("ajustes", "pantallazo", "a-mano", "volver"), salidas)
    }
}
```

`M13QRInvalidoScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m13

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.QRInvalido
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M13QRInvalidoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val diagnostico = QRInvalido("https://menu.resturl.co/…", "enlace externo", "Parece el menú de un restaurante. Por tu seguridad no abrimos enlaces automáticamente (protección anti-quishing).")

    @Test
    fun `muestra el diagnostico anti-quishing y las tres salidas`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M13QRInvalidoScreen(diagnostico, alVolver = { salidas += "volver" }, alVolverAEscanear = { salidas += "escanear" }, alCrearAMano = { salidas += "a-mano" }, alAbrirEnlace = { salidas += "enlace" })
            }
        }
        regla.onNodeWithTag("pantalla-M13").assertIsDisplayed()
        regla.onNodeWithText("QR sin evento").assertIsDisplayed()
        regla.onNodeWithText("Este QR no contiene un evento").assertIsDisplayed()
        regla.onNodeWithText("Leímos el código, pero no trae fecha ni datos de evento para crear una alarma.").assertIsDisplayed()
        regla.onNodeWithText("QUÉ DETECTAMOS").assertIsDisplayed()
        regla.onNodeWithText("enlace externo").assertIsDisplayed()
        regla.onNodeWithText("https://menu.resturl.co/…").assertIsDisplayed()
        regla.onNodeWithText(diagnostico.diagnostico).assertIsDisplayed()
        regla.capturar("M13")
        regla.onNodeWithTag("volver-a-escanear").performClick()
        regla.onNodeWithTag("invalido-a-mano").performClick()
        regla.onNodeWithTag("abrir-enlace").performClick()
        assertEquals(listOf("escanear", "a-mano", "enlace"), salidas)
    }
}
```

- [ ] **Step 2: Correr para ver el fallo.**

- [ ] **Step 3: Tokens y `BandaTextura(opacidad)`**

Agregar los tokens de «Produces». En `BandaTextura`, nuevo parámetro `opacidad: Float = 1f` y multiplicar `alfa` por `opacidad`.

- [ ] **Step 4: `PermisoCamara.kt` (D5)**

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun tienePermisoCamara(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

/**
 * «Abrir ajustes» de M12 (F-M12, decisión D5): pide el permiso real; si ya se pidió y el sistema no volverá a
 * preguntar, abre los ajustes de la app. En ambos casos, al volver se llama [alTerminar] (⏩ → M03), tenga o no
 * permiso: M03 muestra el visor apagado si sigue denegado.
 */
@Composable
fun rememberSolicitudPermisoCamara(alTerminar: () -> Unit): () -> Unit {
    val context = LocalContext.current
    var solicitado by rememberSaveable { mutableStateOf(false) }
    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { alTerminar() }
    val ajustes = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { alTerminar() }
    return {
        val actividad = context as? Activity
        when {
            tienePermisoCamara(context) -> alTerminar()
            solicitado && actividad != null && !ActivityCompat.shouldShowRequestPermissionRationale(actividad, Manifest.permission.CAMERA) ->
                ajustes.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null)))
            else -> { solicitado = true; permiso.launch(Manifest.permission.CAMERA) }
        }
    }
}
```

- [ ] **Step 5: `M12PermisoCamaraScreen.kt`**

Medidas: anexo grupo 3 §4 (6:87).

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m12

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.Divisor
import co.edu.uniandes.alarmasqr.ui.iconos.Iconos
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tamanos
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M12 · Permiso de cámara (F-M12): explicación + «Abrir ajustes»; alternativas ancladas abajo (tutores v1.6). */
@Composable
fun M12PermisoCamaraScreen(alVolver: () -> Unit, alAbrirAjustes: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M12")) {
        BarraSuperior("Permiso de cámara", alVolver = alVolver)
        Box(Modifier.fillMaxSize()) {
            BandaTextura(Modifier.align(Alignment.TopCenter), opacidad = Movimiento.TexturaAtenuada)
            Column(
                Modifier.fillMaxSize().padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingPermisoSuperior, bottom = Espacio.HojaInferior),
                verticalArrangement = Arrangement.spacedBy(Espacio.GapPermiso),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.fillMaxWidth().height(Medidas.VisorApagado).background(Colores.GrisNiebla, Radios.Visor), contentAlignment = Alignment.Center) {
                    Icon(Iconos.Escanear, contentDescription = null, tint = Colores.Tinta, modifier = Modifier.size(Tamanos.IconoVisor))
                }
                Text("La cámara está apagada para la app", style = Tipografia.BarraSuperior, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("Solo la usamos para leer códigos QR de eventos. Nunca guardamos fotos ni videos.", style = Tipografia.CuerpoDialogo, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                TarjetaPasos()
                BotonPrimario("Abrir ajustes", onClick = alAbrirAjustes, modifier = Modifier.testTag("abrir-ajustes"))
                Spacer(Modifier.weight(1f))
                Divisor("mientras tanto")
                BotonSecundario("Elegir pantallazo de la galería", onClick = alElegirPantallazo, modifier = Modifier.testTag("permiso-pantallazo"))
                BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("permiso-a-mano"))
            }
        }
    }
}

/** Tarjeta «ACTÍVALA EN 3 PASOS» (6:96): blanca, borde 1.5, r14, sombra, relleno 14/12, gap 7, numerales 22 en Tinta. */
@Composable
private fun TarjetaPasos() {
    Column(
        Modifier.fillMaxWidth()
            .shadow(Elevaciones.Tarjeta, Radios.Tarjeta, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
            .background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.GrisBorde, Radios.Tarjeta)
            .padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingPasosVertical),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapPasos),
    ) {
        Text("ACTÍVALA EN 3 PASOS", style = Tipografia.H3, color = Colores.GrisTexto)
        listOf("Abrir los ajustes del teléfono", "Permisos › Cámara", "Elegir “Permitir con la app en uso”").forEachIndexed { i, paso ->
            Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapFila), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(Medidas.Numeral).background(Colores.Tinta, Radios.Pildora), contentAlignment = Alignment.Center) {
                    Text("${i + 1}", style = Tipografia.Chip, color = Colores.Blanco)
                }
                Text(paso, style = Tipografia.Etiqueta, color = Colores.Tinta)
            }
        }
    }
}
```

- [ ] **Step 6: `M13QRInvalidoScreen.kt`**

Medidas: anexo grupo 3 §5 (6:122).

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m13

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.QRInvalido
import co.edu.uniandes.alarmasqr.ui.componentes.BandaTextura
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M13 · QR sin evento (F-M13): diagnóstico anti-quishing; el enlace solo se abre bajo decisión explícita. */
@Composable
fun M13QRInvalidoScreen(diagnostico: QRInvalido, alVolver: () -> Unit, alVolverAEscanear: () -> Unit, alCrearAMano: () -> Unit, alAbrirEnlace: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M13")) {
        BarraSuperior("QR sin evento", alVolver = alVolver)
        Box(Modifier.fillMaxSize()) {
            BandaTextura(Modifier.align(Alignment.TopCenter), opacidad = Movimiento.TexturaAtenuada)
            Column(
                Modifier.fillMaxSize().padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.PaddingPermisoSuperior, bottom = Espacio.PaddingBoton),
                verticalArrangement = Arrangement.spacedBy(Espacio.GapPermiso),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.size(Medidas.SelloGrande).background(Colores.CoralSuave, Radios.Pildora).border(Trazos.MarcoPantalla, Colores.CoralTexto, Radios.Pildora), contentAlignment = Alignment.Center) {
                    Text("!", style = Tipografia.H1, color = Colores.CoralTexto)
                }
                Text("Este QR no contiene un evento", style = Tipografia.BarraSuperior, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text("Leímos el código, pero no trae fecha ni datos de evento para crear una alarma.", style = Tipografia.CuerpoDialogo, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Column(
                    Modifier.fillMaxWidth().background(Colores.Blanco, Radios.Tarjeta).border(Trazos.Borde, Colores.CoralTexto, Radios.Tarjeta)
                        .padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingPasosVertical),
                    verticalArrangement = Arrangement.spacedBy(Espacio.GapPasos),
                ) {
                    Text("QUÉ DETECTAMOS", style = Tipografia.H3, color = Colores.GrisTexto)
                    Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapDivisor), verticalAlignment = Alignment.CenterVertically) {
                        ChipEstado(diagnostico.tipoDetectado, VarianteChip.Coral)
                        Text(diagnostico.contenido, style = Tipografia.Codigo, color = Colores.GrisTexto, maxLines = 1)
                    }
                    Text(diagnostico.diagnostico, style = Tipografia.Nota, color = Colores.GrisTexto)
                }
                Spacer(Modifier.height(Espacio.AntesAcciones))
                BotonPrimario("Volver a escanear", onClick = alVolverAEscanear, modifier = Modifier.testTag("volver-a-escanear"))
                BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("invalido-a-mano"))
                BotonEnlace("Abrir el enlace bajo mi responsabilidad", onClick = alAbrirEnlace, color = ColorEnlace.Azul, estilo = Tipografia.EnlaceCorto, modifier = Modifier.testTag("abrir-enlace"))
            }
        }
    }
}
```
`Trazos.MarcoPantalla = 3.dp` ya existe en el JSON (`stroke.marco-pantalla`); agregarlo a `Tokens.kt` (es el borde del sello «!»).

- [ ] **Step 7: Registrar en `entradasApp`**

```kotlin
    entry<Pantalla.M12> {
        val abrirAjustes = rememberSolicitudPermisoCamara(alTerminar = { pila.reemplazarCima(Pantalla.M03) })
        M12PermisoCamaraScreen(
            alVolver = { pila.removeLastOrNull() },
            alAbrirAjustes = abrirAjustes,
            alElegirPantallazo = { pila.reemplazarCima(Pantalla.M03b) },
            alCrearAMano = { pila.reemplazarCima(Pantalla.M07) },
        )
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
```

- [ ] **Step 8: Correr todo, verificar capturas (M12 contra 6:87, M13 contra 6:122), commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
git add apps/movil packages/tokens docs/verificacion && git commit -m "M12/M13: permiso de cámara real y QR sin evento"
```

---

### Task 9: M03 Escáner con CameraX + ML Kit, linterna y toques simulados

**Files:**
- Modify: `apps/movil/gradle/libs.versions.toml`, `apps/movil/app/build.gradle.kts` (`androidx.camera:camera-mlkit-vision`)
- Create: `qr/AnalizadorQR.kt`, `qr/VisorCamara.kt`, `ui/componentes/Asa.kt`, `ui/pantallas/m03/M03EscanerViewModel.kt`, `ui/pantallas/m03/M03EscanerScreen.kt`
- Modify: `navegacion/HojaInferiorSceneStrategy.kt` (usa `Asa` pública), `navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/qr/AnalizadorQRTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m03/M03EscanerScreenTest.kt`

**Interfaces:**
- Consumes: `ChipControl`, `ChipEstado`, `BarraSuperior(sobreTinta)`, `BotonSecundario`, `CodigoQR`, `tienePermisoCamara`, `RepositorioDataset.agregarDesdeEvento`.
- Produces:
  - `sealed interface ResultadoQR { data class EventoDetectado(val eventoId: String) : ResultadoQR; data class QRInvalido(val contenido: String) : ResultadoQR }`; `object AnalizadorQR { fun interpretar(contenido: String, repositorio: RepositorioDataset): ResultadoQR }`.
  - `@Composable fun VisorCamara(linterna: Boolean, alLeer: (String) -> Unit, modifier: Modifier = Modifier)`.
  - `@Composable fun Asa(modifier: Modifier = Modifier)` (36×4 Gris Borde).
  - `data class EstadoEscaner(val linterna: Boolean = false, val resultado: ResultadoQR? = null)`; `class M03EscanerViewModel(repositorio) : ViewModel { val estado: StateFlow<EstadoEscaner>; fun alternarLinterna(); fun leer(contenido: String); fun simularEventoValido(); fun simularInvalido(); fun consumirResultado() }`.
  - `M03EscanerScreen(estado: EstadoEscaner, tienePermiso: Boolean, alVolver, alAlternarLinterna, alLeer: (String) -> Unit, alTocarVisor, alTocarVibra, alElegirPantallazo, alCrearAMano, modifier)` (testTag `pantalla-M03`; `linterna`, `visor`, `vibra`, `escaner-pantallazo`, `escaner-a-mano`).
  - Tokens: `Trazos.MarcoEnfoque = 4.dp` (JSON `stroke.marco-enfoque 4`), `Medidas.EsquinaEnfoque = 40.dp` (`size.movil.esquina-enfoque 40`), `Espacio.GapVisor = 16.dp` (`gap-visor 16`), `Espacio.ChipVisorSuperior = 24.dp` (`chip-visor-superior 24`), `Movimiento.QROpacidadVisor = 0.5f` (`texture.qr-visor-opacity 0.5`), `Movimiento.VibracionMs = 80L` (`motion.vibracion.duration 80`).

- [ ] **Step 1: Dependencia**

En `libs.versions.toml` (`[libraries]`): `androidx-camera-mlkit-vision = { group = "androidx.camera", name = "camera-mlkit-vision", version.ref = "camerax" }`; en `build.gradle.kts`: `implementation(libs.androidx.camera.mlkit.vision)`. Si Gradle no resuelve `camera-mlkit-vision:1.6.2`, fijar en `[versions]` `cameraxMlkit` con la última 1.x publicada en maven.google.com y anotarla en el README (§versiones).

- [ ] **Step 2: Pruebas (fallan)**

`AnalizadorQRTest.kt` (JUnit puro):

```kotlin
package co.edu.uniandes.alarmasqr.qr

import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class AnalizadorQRTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `un QR de evento conocido devuelve su id`() {
        assertEquals(ResultadoQR.EventoDetectado("e-entrega"), AnalizadorQR.interpretar("alarmasqr://evento/e-entrega", repo))
    }

    @Test
    fun `un evento desconocido o cualquier otro contenido es invalido`() {
        assertEquals(ResultadoQR.QRInvalido("alarmasqr://evento/e-nadie"), AnalizadorQR.interpretar("alarmasqr://evento/e-nadie", repo))
        assertEquals(ResultadoQR.QRInvalido("https://menu.resturl.co/…"), AnalizadorQR.interpretar("https://menu.resturl.co/…", repo))
    }
}
```

`M03EscanerScreenTest.kt` (sin permiso: visor apagado; la cámara real no se prueba en JVM):

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M03EscanerScreenTest {
    @get:Rule val regla = createComposeRule()

    @Test
    fun `sobre Tinta muestra el chip de linterna, el visor y la hoja con dos secundarios; los toques simulados avisan`() {
        val salidas = mutableListOf<String>()
        regla.setContent {
            AlarmasQRTheme {
                M03EscanerScreen(
                    estado = EstadoEscaner(), tienePermiso = false,
                    alVolver = { salidas += "volver" }, alAlternarLinterna = { salidas += "linterna" }, alLeer = {},
                    alTocarVisor = { salidas += "visor" }, alTocarVibra = { salidas += "vibra" },
                    alElegirPantallazo = { salidas += "pantallazo" }, alCrearAMano = { salidas += "a-mano" },
                )
            }
        }
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.onNodeWithText("Escanear QR").assertIsDisplayed()
        regla.onNodeWithTag("linterna").assertIsOff()
        regla.onNodeWithText("● Cámara activa").assertIsDisplayed()
        regla.onNodeWithText("Apunta al código QR del evento").assertIsDisplayed()
        regla.capturar("M03")
        regla.onNodeWithTag("linterna").performClick()
        regla.onNodeWithTag("visor").performClick()
        regla.onNodeWithTag("vibra").performClick()
        regla.onNodeWithTag("escaner-pantallazo").performClick()
        regla.onNodeWithTag("escaner-a-mano").performClick()
        regla.onNodeWithTag("atras").performClick()
        assertEquals(listOf("linterna", "visor", "vibra", "pantallazo", "a-mano", "volver"), salidas)
    }
}
```

- [ ] **Step 3: Correr para ver el fallo.**

- [ ] **Step 4: `AnalizadorQR.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.qr

import co.edu.uniandes.alarmasqr.datos.RepositorioDataset

sealed interface ResultadoQR {
    data class EventoDetectado(val eventoId: String) : ResultadoQR
    data class QRInvalido(val contenido: String) : ResultadoQR
}

/** Interpreta lo leído por la cámara (F-M03 / F-M13): `alarmasqr://evento/{id}` de un evento del dataset → M04; lo demás → M13. */
object AnalizadorQR {
    private val patron = Regex("^alarmasqr://evento/([A-Za-z0-9_-]+)$")

    fun interpretar(contenido: String, repositorio: RepositorioDataset): ResultadoQR {
        val id = patron.find(contenido.trim())?.groupValues?.get(1) ?: return ResultadoQR.QRInvalido(contenido)
        return if (repositorio.evento(id) != null) ResultadoQR.EventoDetectado(id) else ResultadoQR.QRInvalido(contenido)
    }
}
```

- [ ] **Step 5: `VisorCamara.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.qr

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Visor real de M03: `PreviewView` con `LifecycleCameraController` y `MlKitAnalyzer` (skill camerax: usar el
 * analizador de ML Kit en vez de un `ImageAnalysis.Analyzer` manual). Solo QR; [alLeer] recibe el contenido crudo
 * cada vez que ML Kit lo detecta (el ViewModel descarta repeticiones). [linterna] enciende la lámpara.
 */
@Composable
fun VisorCamara(linterna: Boolean, alLeer: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val propietario = LocalLifecycleOwner.current
    val controlador = remember { LifecycleCameraController(context) }
    val escaner = remember { BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()) }

    DisposableEffect(controlador, propietario) {
        val ejecutor = ContextCompat.getMainExecutor(context)
        controlador.setImageAnalysisAnalyzer(
            ejecutor,
            MlKitAnalyzer(listOf(escaner), ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED, ejecutor) { resultado ->
                resultado?.getValue(escaner)?.firstOrNull()?.rawValue?.let(alLeer)
            },
        )
        controlador.bindToLifecycle(propietario)
        onDispose { controlador.unbind(); escaner.close() }
    }
    LaunchedEffect(linterna) { controlador.enableTorch(linterna) }

    AndroidView(
        factory = { PreviewView(it).apply { controller = controlador; scaleType = PreviewView.ScaleType.FILL_CENTER } },
        modifier = modifier,
    )
}
```

- [ ] **Step 6: `Asa.kt` y `HojaInferiorSceneStrategy`**

Crear `ui/componentes/Asa.kt` con la función `Asa` que hoy es privada en `HojaInferiorSceneStrategy.kt` (mismo cuerpo, `Medidas.Asa`, Gris Borde, píldora) y borrar la privada; la estrategia importa la pública.

- [ ] **Step 7: `M03EscanerViewModel.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.qr.AnalizadorQR
import co.edu.uniandes.alarmasqr.qr.ResultadoQR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoEscaner(val linterna: Boolean = false, val resultado: ResultadoQR? = null)

/** M03: linterna y un solo resultado por lectura; la entrada navega al consumirlo. Los ⏩ usan el dataset. */
class M03EscanerViewModel(private val repositorio: RepositorioDataset) : ViewModel() {
    private val _estado = MutableStateFlow(EstadoEscaner())
    val estado: StateFlow<EstadoEscaner> = _estado.asStateFlow()

    fun alternarLinterna() = _estado.update { it.copy(linterna = !it.linterna) }

    fun leer(contenido: String) {
        if (_estado.value.resultado != null) return
        _estado.update { it.copy(resultado = AnalizadorQR.interpretar(contenido, repositorio)) }
    }

    /** ⏩ Tocar «Apunta al código QR del evento» = leer el QR de e-entrega (NAVEGACION §6 paso 4). */
    fun simularEventoValido() = leer(repositorio.evento("e-entrega")?.codigoQR ?: "")

    /** ⏩ Tocar «vibra al detectar el código» = leer el QR inválido del dataset (paso 4b). */
    fun simularInvalido() = leer(repositorio.dataset.qrInvalido.contenido)

    fun consumirResultado() = _estado.update { it.copy(resultado = null) }
}
```

- [ ] **Step 8: `M03EscanerScreen.kt`**

Medidas: anexo grupo 3 §1 (4:135).

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import co.edu.uniandes.alarmasqr.qr.VisorCamara
import co.edu.uniandes.alarmasqr.ui.componentes.Asa
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonSecundario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipControl
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Movimiento
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/**
 * M03 · Escáner dual (F-M03): superficie Tinta, barra con «Linterna · auto» (chip de control 32), visor con marco
 * de enfoque amarillo (único amarillo: la hoja lleva dos secundarios de contorno) y hoja blanca r24 con las
 * alternativas. Sin permiso, el visor queda apagado y siguen los toques simulados ⏩.
 */
@Composable
fun M03EscanerScreen(
    estado: EstadoEscaner, tienePermiso: Boolean,
    alVolver: () -> Unit, alAlternarLinterna: () -> Unit, alLeer: (String) -> Unit,
    alTocarVisor: () -> Unit, alTocarVibra: () -> Unit, alElegirPantallazo: () -> Unit, alCrearAMano: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(Colores.Tinta).testTag("pantalla-M03")) {
        BarraSuperior("Escanear QR", alVolver = alVolver, sobreTinta = true) {
            ChipControl("Linterna · auto", activo = estado.linterna, onClick = alAlternarLinterna, sobreTinta = true, modifier = Modifier.testTag("linterna"))
        }
        Box(Modifier.fillMaxWidth().weight(1f)) {
            if (tienePermiso) VisorCamara(linterna = estado.linterna, alLeer = alLeer, modifier = Modifier.fillMaxSize())
            ChipEstado("● Cámara activa", VarianteChip.Escaneada, modifier = Modifier.align(Alignment.TopCenter).padding(top = Espacio.ChipVisorSuperior))
            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Espacio.GapVisor)) {
                MarcoEnfoque(Modifier.clickable(role = Role.Button, onClick = alTocarVisor).testTag("visor"))
                Text("Apunta al código QR del evento", style = Tipografia.Destacado, color = Colores.Blanco)
                Text("vibra al detectar el código", style = Tipografia.Divisor, color = Colores.GrisBorde, modifier = Modifier.clickable(onClick = alTocarVibra).testTag("vibra"))
            }
        }
        Column(
            Modifier.fillMaxWidth().clip(Radios.Hoja).background(Colores.Blanco)
                .padding(start = Espacio.Margen, end = Espacio.Margen, top = Espacio.HojaSuperior, bottom = Espacio.HojaInferior),
            verticalArrangement = Arrangement.spacedBy(Espacio.GapFila),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Asa()
            BotonSecundario("Elegir pantallazo de la galería", onClick = alElegirPantallazo, modifier = Modifier.testTag("escaner-pantallazo"))
            BotonSecundario("Crear el evento a mano", onClick = alCrearAMano, modifier = Modifier.testTag("escaner-a-mano"))
        }
    }
}

/** Marco de enfoque 220 (4:143): cuatro esquinas Amarillo Energía de 40 con trazo 4 y remates redondos; QR de 100 al 50 %. */
@Composable
private fun MarcoEnfoque(modifier: Modifier = Modifier) {
    Box(modifier.size(Medidas.MarcoEnfoque), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val t = size.width; val e = Medidas.EsquinaEnfoque.toPx(); val g = Trazos.MarcoEnfoque.toPx()
            val esquinas = Path().apply {
                moveTo(0f, e); lineTo(0f, 0f); lineTo(e, 0f)
                moveTo(t - e, 0f); lineTo(t, 0f); lineTo(t, e)
                moveTo(t, t - e); lineTo(t, t); lineTo(t - e, t)
                moveTo(e, t); lineTo(0f, t); lineTo(0f, t - e)
            }
            drawPath(esquinas, Colores.AmarilloEnergia, style = Stroke(width = g, cap = StrokeCap.Round))
        }
        CodigoQR("alarmasqr://evento/e-entrega", tamano = Medidas.QRVisor, modifier = Modifier.alpha(Movimiento.QROpacidadVisor))
    }
}
```

- [ ] **Step 9: Registrar en `entradasApp`**

```kotlin
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
```
Con la función privada en `EntradasApp.kt`:

```kotlin
/** «vibra al detectar el código» (F-M03). */
private fun vibrar(context: Context) {
    val vibrador = context.getSystemService(Vibrator::class.java) ?: return
    vibrador.vibrate(VibrationEffect.createOneShot(Movimiento.VibracionMs, VibrationEffect.DEFAULT_AMPLITUDE))
}
```

- [ ] **Step 10: Correr todo, `assembleDebug`, probar en un dispositivo real la cámara (QR generado en M01 o M03b: apuntar a otra pantalla que muestre `alarmasqr://evento/e-entrega` → M04; cualquier QR de una URL → M13; el chip enciende la linterna), verificar la captura M03 contra 4:135 (el visor sale apagado en JVM), commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest assembleDebug --no-daemon
git add apps/movil packages/tokens docs/verificacion && git commit -m "M03: escáner con CameraX y ML Kit, linterna, toques simulados y vibración"
```

---

### Task 10: M03b Pantallazo recibido e intent de compartir

**Files:**
- Create: `ui/pantallas/m03/M03bPantallazoScreen.kt`, `navegacion/DestinoExterno.kt`
- Modify: `MainActivity.kt`, `AndroidManifest.xml` (`android:launchMode="singleTop"`), `navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m03/M03bPantallazoScreenTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/DestinoExternoTest.kt`

**Interfaces:**
- Consumes: `ChipEstado`, `CodigoQR`, botones, `PantallazoRecibido`, `EventoQR`, `Pantalla.porRuta`.
- Produces:
  - `M03bPantallazoScreen(datos: PantallazoRecibido, evento: EventoQR, alVolver, alContinuar, alElegirOtra, modifier)` (testTag `pantalla-M03b`; `continuar`, `elegir-otra`).
  - `fun destinoDesdeIntent(intent: Intent?): Pantalla?` — `ACTION_SEND` con `image/*` → `M03b`; `data.path` → `Pantalla.porRuta`; si no, null.
  - `MainActivity`: `destinoPendiente: MutableState<Pantalla?>` consumido por un `LaunchedEffect` que hace `reemplazarTodo(M02)` + `irA(destino)`; `onNewIntent` lo actualiza (la Tarea 11 lo usa para la alarma).
  - Tokens: `Medidas.Burbuja = DpSize(250.dp, 227.dp)` (JSON `size.movil.burbuja [250, 227]`), `Tipografia.Mensaje` (Archivo 13 / 17; JSON `typography.mensaje {ui,400,13,lineHeight 17}`), `Espacio.PaddingMarcoLectura = 8.dp` (`padding-marco-lectura 8`).

- [ ] **Step 1: Pruebas (fallan)**

`M03bPantallazoScreenTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M03bPantallazoScreenTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())

    @Test
    fun `muestra el mensaje de WhatsApp con el QR enmarcado y las salidas`() {
        val salidas = mutableListOf<String>()
        val datos = repo.dataset.pantallazoRecibido
        regla.setContent {
            AlarmasQRTheme { M03bPantallazoScreen(datos, repo.evento(datos.eventoDetectado)!!, alVolver = { salidas += "volver" }, alContinuar = { salidas += "continuar" }, alElegirOtra = { salidas += "otra" }) }
        }
        regla.onNodeWithTag("pantalla-M03b").assertIsDisplayed()
        regla.onNodeWithText("Pantallazo recibido").assertIsDisplayed()
        regla.onNodeWithText("✓ QR detectado").assertIsDisplayed()
        regla.onNodeWithText("QR de evento detectado en tu pantallazo").assertIsDisplayed()
        regla.onNodeWithText("Grupo MISO UX · hoy 8:12 am").assertIsDisplayed()
        regla.onNodeWithText(datos.mensaje).assertIsDisplayed()
        regla.onNodeWithText("Origen: ${datos.origen}").assertIsDisplayed()
        regla.onNodeWithText("Si el pantallazo no trae un QR, puedes crear el evento a mano.").assertIsDisplayed()
        regla.capturar("M03b")
        regla.onNodeWithTag("continuar").performClick()
        regla.onNodeWithTag("elegir-otra").performClick()
        regla.onNodeWithTag("atras").performClick()
        assertEquals(listOf("continuar", "otra", "volver"), salidas)
    }
}
```

`DestinoExternoTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import android.content.Intent
import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DestinoExternoTest {
    @Test
    fun `un pantallazo compartido abre M03b y un deep link de alarma abre M10`() {
        assertEquals(Pantalla.M03b, destinoDesdeIntent(Intent(Intent.ACTION_SEND).setType("image/png")))
        assertEquals(Pantalla.M10("a-entrega"), destinoDesdeIntent(Intent(Intent.ACTION_VIEW, Uri.parse("alarmasqr://app/alarma/a-entrega/sonando"))))
        assertNull(destinoDesdeIntent(Intent(Intent.ACTION_MAIN)))
        assertNull(destinoDesdeIntent(null))
    }
}
```

- [ ] **Step 2: Correr para ver el fallo.**

- [ ] **Step 3: `DestinoExterno.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import android.content.Intent

/**
 * Entradas externas (spec §2.2): el pantallazo compartido (`ACTION_SEND image/*`) abre M03b y la notificación de la
 * alarma trae `alarmasqr://app/<ruta>` que resuelve `Pantalla.porRuta` (alarma/{id}/sonando → M10).
 */
fun destinoDesdeIntent(intent: Intent?): Pantalla? = when {
    intent == null -> null
    intent.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true -> Pantalla.M03b
    intent.data?.path != null -> Pantalla.porRuta(intent.data!!.path!!)
    else -> null
}
```

- [ ] **Step 4: `MainActivity.kt`**

```kotlin
package co.edu.uniandes.alarmasqr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.navegacion.NavegacionApp
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.navegacion.destinoDesdeIntent
import co.edu.uniandes.alarmasqr.navegacion.entradasApp
import co.edu.uniandes.alarmasqr.navegacion.irA
import co.edu.uniandes.alarmasqr.navegacion.reemplazarTodo
import co.edu.uniandes.alarmasqr.navegacion.rememberBackStackApp
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme

class MainActivity : ComponentActivity() {
    /** Pantalla pedida desde fuera (pantallazo compartido, notificación de la alarma); se consume una vez. */
    private val destinoPendiente = mutableStateOf<Pantalla?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repositorio = RepositorioDataset.desdeAssets(this)
        if (savedInstanceState == null) destinoPendiente.value = destinoDesdeIntent(intent)
        setContent {
            AlarmasQRTheme {
                val pila = rememberBackStackApp()
                LaunchedEffect(destinoPendiente.value) {
                    destinoPendiente.value?.let { destino ->
                        pila.reemplazarTodo(Pantalla.M02)   // pila sintética: Inicio debajo del destino (Nav 3, deep links)
                        pila.irA(destino)
                        destinoPendiente.value = null
                    }
                }
                NavegacionApp(backStack = pila, repositorio = repositorio) { entradasApp(pila, repositorio) }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        destinoPendiente.value = destinoDesdeIntent(intent)
    }
}
```
En el manifiesto, `MainActivity` recibe `android:launchMode="singleTop"`.

- [ ] **Step 5: `M03bPantallazoScreen.kt`**

Medidas: anexo grupo 2 §4 (4020:3295).

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m03

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import co.edu.uniandes.alarmasqr.datos.EventoQR
import co.edu.uniandes.alarmasqr.datos.PantallazoRecibido
import co.edu.uniandes.alarmasqr.ui.componentes.BarraSuperior
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.CodigoQR
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Elevaciones
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia
import co.edu.uniandes.alarmasqr.ui.theme.Trazos

/** M03b · Pantallazo recibido (F-M03): confirmación del QR leído en una imagen compartida; «Continuar» → M04. */
@Composable
fun M03bPantallazoScreen(datos: PantallazoRecibido, evento: EventoQR, alVolver: () -> Unit, alContinuar: () -> Unit, alElegirOtra: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M03b")) {
        BarraSuperior("Pantallazo recibido", alVolver = alVolver)
        Column(
            Modifier.fillMaxSize().padding(horizontal = Espacio.Margen, vertical = Espacio.PaddingBoton),
            verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ChipEstado("✓ QR detectado", VarianteChip.Escaneada)
            Text("QR de evento detectado en tu pantallazo", style = Tipografia.TituloEvento, color = Colores.Tinta, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Box(Modifier.fillMaxWidth().height(Medidas.VistaPrevia).background(Colores.GrisNiebla, Radios.Visor), contentAlignment = Alignment.Center) {
                Column(
                    Modifier.size(Medidas.Burbuja.width, Medidas.Burbuja.height)
                        .shadow(Elevaciones.Tarjeta, Radios.Tarjeta, ambientColor = Elevaciones.ColorSombra, spotColor = Elevaciones.ColorSombra)
                        .background(Colores.Blanco, Radios.Tarjeta).padding(horizontal = Espacio.PaddingTarjeta, vertical = Espacio.PaddingPasosVertical),
                    verticalArrangement = Arrangement.spacedBy(Espacio.GapFila),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("${datos.grupo} · hoy 8:12 am", style = Tipografia.Divisor, color = Colores.GrisTexto)   // hora literal del mockup 4020:3452
                    Text(datos.mensaje, style = Tipografia.Mensaje, color = Colores.Tinta, modifier = Modifier.fillMaxWidth())
                    Box(Modifier.size(Medidas.MarcoLectura).border(Trazos.MarcoPantalla, Colores.Tinta, Radios.MarcoLectura).padding(Espacio.PaddingMarcoLectura)) {
                        CodigoQR(evento.codigoQR, tamano = Medidas.QRPantallazo)
                    }
                }
            }
            Text("Origen: ${datos.origen}", style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.weight(1f))
            BotonPrimario("Continuar", onClick = alContinuar, modifier = Modifier.testTag("continuar"))
            BotonEnlace("Elegir otra imagen", onClick = alElegirOtra, modifier = Modifier.testTag("elegir-otra"))
            Text("Si el pantallazo no trae un QR, puedes crear el evento a mano.", style = Tipografia.Etiqueta, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}
```

- [ ] **Step 6: Registrar en `entradasApp`**

```kotlin
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
```

- [ ] **Step 7: Correr todo; probar en dispositivo: compartir una imagen desde la galería a «Alarmas QR» abre M03b; verificar captura M03b contra 4020:3295; commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest assembleDebug --no-daemon
git add apps/movil packages/tokens docs/verificacion && git commit -m "M03b: pantallazo recibido e intent de compartir imagen"
```

---

### Task 11: Alarma real — `ProgramadorAlarmas`, `ReceptorAlarma`, notificación de pantalla completa y deep link

**Files:**
- Create: `alarma/Programador.kt` (interfaz), `alarma/ProgramadorAlarmas.kt`, `alarma/ReceptorAlarma.kt`, `alarma/NotificacionesAlarma.kt`
- Modify: `AndroidManifest.xml` (receiver), `MainActivity.kt` (canal al arrancar)
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/alarma/ProgramadorAlarmasTest.kt`

**Interfaces:**
- Consumes: `Alarma`, `destinoDesdeIntent` (Tarea 10), `Pantalla.M10(id).ruta`.
- Produces:
  - `interface Programador { fun programar(alarma: Alarma): Long; fun cancelar(id: String) }`
  - `class ProgramadorAlarmas(context: Context, ahora: () -> Long = System::currentTimeMillis) : Programador` (D4: si `suena` ya pasó, ahora + `DEMORA_DEMO_MS` = 60 000).
  - `class ReceptorAlarma : BroadcastReceiver` (extras `EXTRA_ID`, `EXTRA_TITULO`, `EXTRA_LUGAR`).
  - `object NotificacionesAlarma { const val CANAL = "alarmas"; fun crearCanal(context); fun publicar(context, id, titulo, lugar: String?); fun intentSonando(context, id): Intent; fun tienePermiso(context): Boolean }`; `@Composable fun rememberSolicitudPermisoNotificaciones(): () -> Unit`.

- [ ] **Step 1: Prueba (falla)**

`ProgramadorAlarmasTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.alarma

import android.app.AlarmManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.Alarma
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.OffsetDateTime

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ProgramadorAlarmasTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val am = context.getSystemService(AlarmManager::class.java)
    private val entrega = Alarma(id = "a-entrega", titulo = "Entrega de proyecto UX", eventoInicio = "2026-08-30T16:00:00-05:00", suena = "2026-08-30T15:15:00-05:00", lugar = "Aula SD-703, Universidad", origen = "escaneada", estado = "activa", anticipacionMin = 30, trayectoMin = 15)

    @Test
    fun `programa a la hora de suena cuando esta en el futuro`() {
        val ahora = OffsetDateTime.parse("2026-08-30T10:00:00-05:00").toInstant().toEpochMilli()
        val cuando = ProgramadorAlarmas(context) { ahora }.programar(entrega)
        assertEquals(OffsetDateTime.parse("2026-08-30T15:15:00-05:00").toInstant().toEpochMilli(), cuando)
        val programada = shadowOf(am).scheduledAlarms.single()
        assertEquals(cuando, programada.triggerAtMs)
        assertEquals(AlarmManager.RTC_WAKEUP, programada.type)
    }

    @Test
    fun `si la hora ya paso programa un minuto despues de ahora (demo D4)`() {
        val ahora = OffsetDateTime.parse("2026-09-20T14:00:00-05:00").toInstant().toEpochMilli()
        val cuando = ProgramadorAlarmas(context) { ahora }.programar(entrega)
        assertEquals(ahora + ProgramadorAlarmas.DEMORA_DEMO_MS, cuando)
    }

    @Test
    fun `cancelar retira la alarma programada`() {
        val programador = ProgramadorAlarmas(context)
        programador.programar(entrega)
        assertEquals(1, shadowOf(am).scheduledAlarms.size)
        programador.cancelar("a-entrega")
        assertTrue(shadowOf(am).scheduledAlarms.isEmpty())
    }
}
```

- [ ] **Step 2: Correr para ver el fallo.**

- [ ] **Step 3: `Programador.kt` y `ProgramadorAlarmas.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.alarma

import co.edu.uniandes.alarmasqr.datos.Alarma

/** Abstracción para que M04 se pruebe sin AlarmManager. */
interface Programador {
    /** Programa y devuelve el instante (epoch ms) elegido. */
    fun programar(alarma: Alarma): Long
    fun cancelar(id: String)
}
```

```kotlin
package co.edu.uniandes.alarmasqr.alarma

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import co.edu.uniandes.alarmasqr.datos.Alarma
import java.time.OffsetDateTime

/**
 * F-M10 «la alarma debe sonar con la app cerrada»: `setExactAndAllowWhileIdle` (RTC_WAKEUP) con el permiso
 * `SCHEDULE_EXACT_ALARM`/`USE_EXACT_ALARM` del manifiesto; si el usuario lo revocó (Android 12+), cae a
 * `setAndAllowWhileIdle`. Decisión D4: el dataset vive en agosto de 2026, así que una hora ya pasada se programa
 * a ahora + 1 min para poder demostrar el disparo real.
 */
class ProgramadorAlarmas(private val context: Context, private val ahora: () -> Long = System::currentTimeMillis) : Programador {
    private val gestor: AlarmManager get() = context.getSystemService(AlarmManager::class.java)

    override fun programar(alarma: Alarma): Long {
        val objetivo = OffsetDateTime.parse(alarma.suena).toInstant().toEpochMilli()
        val cuando = if (objetivo > ahora()) objetivo else ahora() + DEMORA_DEMO_MS
        val pendiente = pendiente(alarma.id, alarma.titulo, alarma.lugar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !gestor.canScheduleExactAlarms()) {
            gestor.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuando, pendiente)
        } else {
            gestor.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuando, pendiente)
        }
        return cuando
    }

    override fun cancelar(id: String) = gestor.cancel(pendiente(id, "", null))

    private fun pendiente(id: String, titulo: String, lugar: String?): PendingIntent = PendingIntent.getBroadcast(
        context, id.hashCode(),
        Intent(context, ReceptorAlarma::class.java).putExtra(ReceptorAlarma.EXTRA_ID, id).putExtra(ReceptorAlarma.EXTRA_TITULO, titulo).putExtra(ReceptorAlarma.EXTRA_LUGAR, lugar),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    companion object { const val DEMORA_DEMO_MS = 60_000L }
}
```

- [ ] **Step 4: `ReceptorAlarma.kt` y `NotificacionesAlarma.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.alarma

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Recibe el disparo de AlarmManager y publica la notificación de pantalla completa que abre M10. */
class ReceptorAlarma : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra(EXTRA_ID) ?: return
        NotificacionesAlarma.publicar(context, id, intent.getStringExtra(EXTRA_TITULO) ?: "", intent.getStringExtra(EXTRA_LUGAR))
    }

    companion object {
        const val EXTRA_ID = "alarmaId"
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_LUGAR = "lugar"
    }
}
```

```kotlin
package co.edu.uniandes.alarmasqr.alarma

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import co.edu.uniandes.alarmasqr.MainActivity
import co.edu.uniandes.alarmasqr.navegacion.Pantalla

/** Canal «Alarmas» de importancia alta con sonido de alarma; la notificación es de pantalla completa (spec §2.1). */
object NotificacionesAlarma {
    const val CANAL = "alarmas"
    private const val ESQUEMA = "alarmasqr://app/"

    fun crearCanal(context: Context) {
        val canal = NotificationChannel(CANAL, "Alarmas", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Aviso de cada evento escaneado; suena aunque la app esté cerrada"
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
            enableVibration(true)
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    /** Deep link que MainActivity resuelve con `destinoDesdeIntent` → `Pantalla.porRuta` → M10. */
    fun intentSonando(context: Context, id: String): Intent =
        Intent(Intent.ACTION_VIEW, Uri.parse(ESQUEMA + Pantalla.M10(id).ruta), context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    fun publicar(context: Context, id: String, titulo: String, lugar: String?) {
        if (!tienePermiso(context)) return
        val pendiente = PendingIntent.getActivity(context, id.hashCode(), intentSonando(context, id), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(titulo)
            .setContentText(lugar?.let { "Es hora de salir · $it" } ?: "Es hora de salir")
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendiente)
            .setFullScreenIntent(pendiente, true)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(id.hashCode(), notificacion)
    }
}

/** Pide `POST_NOTIFICATIONS` (Android 13+) la primera vez que se programa una alarma (M04). */
@Composable
fun rememberSolicitudPermisoNotificaciones(): () -> Unit {
    val context = LocalContext.current
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    return {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !NotificacionesAlarma.tienePermiso(context)) {
            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
```

- [ ] **Step 5: Manifiesto y `MainActivity`**

En `AndroidManifest.xml`, dentro de `<application>`: `<receiver android:name=".alarma.ReceptorAlarma" android:exported="false" />`. En `MainActivity.onCreate`, antes de `setContent`: `NotificacionesAlarma.crearCanal(this)`.

- [ ] **Step 6: Correr todo y commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
git add apps/movil && git commit -m "Alarma real: ProgramadorAlarmas, ReceptorAlarma y notificación de pantalla completa con deep link a M10"
```

---

### Task 12: M04 Alarma programada (hoja) y M04d ¿Eliminar alarma?

**Files:**
- Create: `ui/pantallas/m04/M04AlarmaCreadaViewModel.kt`, `ui/pantallas/m04/M04AlarmaCreadaSheet.kt`
- Modify: `navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m04/M04AlarmaCreadaViewModelTest.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m04/M04AlarmaCreadaSheetTest.kt`

**Interfaces:**
- Consumes: `TarjetaEvento`, `ChipEstado(Suave)`, `DialogoConfirmacion`, `BotonPrimario`, `BotonEnlace`, `Programador`, `RepositorioDataset.mensajeEliminar`, `FormatoHora`.
- Produces:
  - `data class EstadoAlarmaCreada(val alarma: Alarma, val dialogoAbierto: Boolean = false, val mensajeEliminar: String)`; `class M04AlarmaCreadaViewModel(repositorio, programador: Programador, id: String) : ViewModel { val estado: StateFlow<EstadoAlarmaCreada>; fun abrirDialogo(); fun cerrarDialogo(); fun eliminar() }` (programa la alarma en `init`).
  - `M04AlarmaCreadaSheet(estado: EstadoAlarmaCreada, mensajes: Mensajes, alListo, alEditar, alAbrirDialogo, alConservar, alEliminar)` (testTag `pantalla-M04`; `listo`, `eliminar`; el diálogo usa los testTags de `DialogoConfirmacion`).
  - Tokens: `Tipografia.EnlaceDescarte` (Archivo Medium 14; JSON `typography.enlace-descarte {ui,500,14}`), `Tipografia.MargenSonara` (Archivo 13 / 17.5; JSON `typography.margen-sonara {ui,400,13,lineHeight 17.5}`), `Espacio.GapSonara = 2.dp` (`gap-sonara 2`), `Espacio.GapTitulo = 8.dp` (`gap-titulo 8`).

- [ ] **Step 1: Pruebas (fallan)**

`M04AlarmaCreadaViewModelTest.kt` (JUnit puro con un `Programador` falso):

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import co.edu.uniandes.alarmasqr.alarma.Programador
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class M04AlarmaCreadaViewModelTest {
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())
    private val programadas = mutableListOf<String>()
    private val canceladas = mutableListOf<String>()
    private val programador = object : Programador {
        override fun programar(alarma: Alarma): Long { programadas += alarma.id; return 0L }
        override fun cancelar(id: String) { canceladas += id }
    }

    @Test
    fun `al crearse programa la alarma y expone el mensaje de M04d`() {
        repo.agregarDesdeEvento("e-entrega")
        val vm = M04AlarmaCreadaViewModel(repo, programador, "a-entrega")
        assertEquals(listOf("a-entrega"), programadas)
        assertEquals("Entrega de proyecto UX", vm.estado.value.alarma.titulo)
        assertTrue(vm.estado.value.mensajeEliminar.startsWith("Dejarás de recibir el aviso de “Entrega de proyecto UX” (dom 30 · 4:00 pm)."))
        assertFalse(vm.estado.value.dialogoAbierto)
    }

    @Test
    fun `abrir, conservar y eliminar`() {
        repo.agregarDesdeEvento("e-entrega")
        val vm = M04AlarmaCreadaViewModel(repo, programador, "a-entrega")
        vm.abrirDialogo(); assertTrue(vm.estado.value.dialogoAbierto)
        vm.cerrarDialogo(); assertFalse(vm.estado.value.dialogoAbierto)
        vm.eliminar()
        assertEquals(listOf("a-entrega"), canceladas)
        assertNull(repo.alarma("a-entrega"))
    }
}
```

`M04AlarmaCreadaSheetTest.kt`:

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M04AlarmaCreadaSheetTest {
    @get:Rule val regla = createComposeRule()
    private val repo = RepositorioDataset(File("src/main/assets/dataset.json").readText())
    private val entrega = repo.agregarDesdeEvento("e-entrega")!!

    @Test
    fun `la hoja muestra el evento, el bloque Sonara y las dos salidas`() {
        val salidas = mutableListOf<String>()
        val estado = EstadoAlarmaCreada(entrega, mensajeEliminar = repo.mensajeEliminar(entrega))
        regla.setContent {
            AlarmasQRTheme { M04AlarmaCreadaSheet(estado, repo.dataset.mensajes, alListo = { salidas += "listo" }, alEditar = {}, alAbrirDialogo = { salidas += "dialogo" }, alConservar = {}, alEliminar = {}) }
        }
        regla.onNodeWithTag("pantalla-M04").assertIsDisplayed()
        regla.onNodeWithText("¡Alarma programada!").assertIsDisplayed()
        regla.onNodeWithText("Datos leídos del QR — verifícalos").assertIsDisplayed()
        regla.onNodeWithText("Entrega de proyecto UX").assertIsDisplayed()
        regla.onNodeWithText("Dom 30 de agosto · 4:00 pm (GMT-5)").assertIsDisplayed()
        regla.onNodeWithText("MISO · UniAndes").assertIsDisplayed()
        regla.onNodeWithText("✓ verificado").assertIsDisplayed()
        regla.onNodeWithText("SONARÁ").assertIsDisplayed()
        regla.onNodeWithText("3:15").assertIsDisplayed()
        regla.onNodeWithText("También se agregó a Google Calendar.").assertIsDisplayed()
        regla.capturar("M04-contenido")
        regla.onNodeWithTag("listo").performClick()
        regla.onNodeWithTag("eliminar").performClick()
        assertEquals(listOf("listo", "dialogo"), salidas)
    }

    @Test
    fun `con el dialogo abierto, Conservar y el velo vuelven y Eliminar confirma`() {
        val salidas = mutableListOf<String>()
        val estado = EstadoAlarmaCreada(entrega, dialogoAbierto = true, mensajeEliminar = repo.mensajeEliminar(entrega))
        regla.setContent {
            AlarmasQRTheme { M04AlarmaCreadaSheet(estado, repo.dataset.mensajes, alListo = {}, alEditar = {}, alAbrirDialogo = {}, alConservar = { salidas += "conservar" }, alEliminar = { salidas += "eliminar" }) }
        }
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.onNodeWithText(estado.mensajeEliminar).assertIsDisplayed()
        regla.capturar("M04d")
        regla.onNodeWithTag("dialogo-seguro").performClick()
        regla.onNodeWithTag("velo").performClick()
        regla.onNodeWithTag("dialogo-confirmar").performClick()
        assertEquals(listOf("conservar", "conservar", "eliminar"), salidas)
    }
}
```

- [ ] **Step 2: Correr para ver el fallo.**

- [ ] **Step 3: Tokens de esta tarea** (ver «Produces»).

- [ ] **Step 4: `M04AlarmaCreadaViewModel.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import androidx.lifecycle.ViewModel
import co.edu.uniandes.alarmasqr.alarma.Programador
import co.edu.uniandes.alarmasqr.datos.Alarma
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoAlarmaCreada(val alarma: Alarma, val dialogoAbierto: Boolean = false, val mensajeEliminar: String)

/**
 * F-M04: la alarma se programa al llegar (sin «Guardar»). El diálogo M04d es estado de esta hoja, no una ruta
 * (TRAZABILIDAD §1b). «Eliminar» cancela la alarma real y la quita del repositorio (queda «Deshacer» en la lista).
 */
class M04AlarmaCreadaViewModel(private val repositorio: RepositorioDataset, private val programador: Programador, private val id: String) : ViewModel() {
    private val alarma = repositorio.alarma(id) ?: error("Alarma $id no existe")
    private val _estado = MutableStateFlow(EstadoAlarmaCreada(alarma, mensajeEliminar = repositorio.mensajeEliminar(alarma)))
    val estado: StateFlow<EstadoAlarmaCreada> = _estado.asStateFlow()

    init { programador.programar(alarma) }

    fun abrirDialogo() = _estado.update { it.copy(dialogoAbierto = true) }
    fun cerrarDialogo() = _estado.update { it.copy(dialogoAbierto = false) }

    fun eliminar() {
        programador.cancelar(id)
        repositorio.eliminar(id)
        cerrarDialogo()
    }
}
```

- [ ] **Step 5: `M04AlarmaCreadaSheet.kt`**

Medidas: anexo grupo 3 §2 y §3 (4:189, 4330:1432). La asa, el radio 24 y el velo los pone la escena de hoja.

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m04

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import co.edu.uniandes.alarmasqr.datos.FormatoHora
import co.edu.uniandes.alarmasqr.datos.Mensajes
import co.edu.uniandes.alarmasqr.ui.componentes.BotonEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.BotonPrimario
import co.edu.uniandes.alarmasqr.ui.componentes.ChipEstado
import co.edu.uniandes.alarmasqr.ui.componentes.ColorEnlace
import co.edu.uniandes.alarmasqr.ui.componentes.DialogoConfirmacion
import co.edu.uniandes.alarmasqr.ui.componentes.TarjetaEvento
import co.edu.uniandes.alarmasqr.ui.componentes.VarianteChip
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Medidas
import co.edu.uniandes.alarmasqr.ui.theme.Radios
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** M04 · Alarma programada (F-M04): hoja sobre la lista; «Listo» → M05, el enlace de descarte abre M04d. */
@Composable
fun M04AlarmaCreadaSheet(
    estado: EstadoAlarmaCreada, mensajes: Mensajes,
    alListo: () -> Unit, alEditar: () -> Unit, alAbrirDialogo: () -> Unit, alConservar: () -> Unit, alEliminar: () -> Unit,
) {
    val alarma = estado.alarma
    Column(
        Modifier.fillMaxWidth().padding(start = Espacio.Margen, end = Espacio.Margen, bottom = Espacio.HojaInferior).testTag("pantalla-M04"),
        verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapTitulo), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(Medidas.Sello).background(Colores.VerdeConfirmado, Radios.Pildora), contentAlignment = Alignment.Center) {
                Text("✓", style = Tipografia.Boton, color = Colores.Blanco)
            }
            Text("¡Alarma programada!", style = Tipografia.BarraSuperior, color = Colores.Tinta)
        }
        ChipEstado("Datos leídos del QR — verifícalos", VarianteChip.Suave)
        TarjetaEvento(alarma)
        BloqueSonara(anticipacion = alarma.anticipacionMin, trayecto = alarma.trayectoMin, hora = alarma.suena, alEditar = alEditar)
        Text("También se agregó a Google Calendar.", style = Tipografia.Divisor, color = Colores.GrisTexto, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        BotonPrimario("Listo", onClick = alListo, modifier = Modifier.testTag("listo"))
        BotonEnlace("No puedo asistir · eliminar alarma", onClick = alAbrirDialogo, color = ColorEnlace.Gris, estilo = Tipografia.EnlaceDescarte, modifier = Modifier.testTag("eliminar"))
    }
    if (estado.dialogoAbierto) {
        DialogoConfirmacion(
            titulo = mensajes.confirmarEliminarTitulo, cuerpo = estado.mensajeEliminar,
            rotuloSeguro = mensajes.confirmarEliminarSeguro, rotuloConfirmar = mensajes.confirmarEliminarAccion,
            destructivo = true, alSeguro = alConservar, alConfirmar = alEliminar,
        )
    }
}

/**
 * Bloque «Sonará» (4:215; DS §6 «la hora calculada es el héroe»): Amarillo Suave r14, relleno 16/12, gap 2;
 * «SONARÁ» H3, hora Spline Sans Mono Bold 52 + sufijo Medium 28 (D7), margen en Archivo 13 con «Editar» enlazado.
 */
@Composable
private fun BloqueSonara(anticipacion: Int, trayecto: Int, hora: String, alEditar: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().background(Colores.AmarilloSuave, Radios.Tarjeta).padding(horizontal = Espacio.PaddingTarjetaEvento, vertical = Espacio.PaddingPasosVertical),
        verticalArrangement = Arrangement.spacedBy(Espacio.GapSonara),
    ) {
        Text("SONARÁ", style = Tipografia.H3, color = Colores.GrisTexto)
        Row(horizontalArrangement = Arrangement.spacedBy(Espacio.GapHoraSufijo)) {
            Text(FormatoHora.hora(hora), style = Tipografia.HoraSonara, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
            Text(FormatoHora.sufijo(hora), style = Tipografia.HoraProtagonistaSufijo, color = Colores.Tinta, modifier = Modifier.alignByBaseline())
        }
        val margen = buildString {
            append("$anticipacion min de margen")
            if (trayecto > 0) append(" + $trayecto min de trayecto desde tu ubicación habitual")
            append(" · ")
        }
        Text(
            buildAnnotatedString {
                append(margen)
                withLink(LinkAnnotation.Clickable("editar", TextLinkStyles(SpanStyle(color = Colores.AzulTexto, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline))) { alEditar() }) { append("Editar") }
            },
            style = Tipografia.MargenSonara, color = Colores.Tinta,
        )
    }
}
```

- [ ] **Step 6: Registrar en `entradasApp`**

```kotlin
    entry<Pantalla.M04>(metadata = HojaInferiorSceneStrategy.hoja()) { clave ->
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
```

- [ ] **Step 7: Correr todo; en dispositivo: escanear (o tocar el visor) → M04; cerrar la app; al minuto suena la notificación de pantalla completa y abre el marcador M10; «Eliminar» en M04d la cancela. Verificar capturas M04-contenido (4:189) y M04d (4330:1432); commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest assembleDebug --no-daemon
git add apps/movil packages/tokens docs/verificacion && git commit -m "M04/M04d: hoja de alarma programada con alarma real y diálogo de confirmación"
```

---

### Task 13: M05 Guardada + «Deshacer · 5 s»

**Files:**
- Modify: `navegacion/EntradasApp.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/ui/pantallas/m02/M05GuardadaTest.kt`

**Interfaces:**
- Consumes: `M02InicioViewModel(repositorio, alarmaNueva)`, `M02InicioScreen(codigo = "M05")`, `LocalSnackbarApp`, `SnackbarDeshacer`, `Movimiento.DeshacerMs`, `mensajes.alarmaGuardada` / `mensajes.deshacer`.
- Produces: entrada M05 registrada; comportamiento: snackbar 5 s con «Deshacer · 5 s»; deshacer revierte la alarma y vuelve a M02.

- [ ] **Step 1: Prueba (falla: M05 aún cae en el marcador)**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas.m02

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.navegacion.NavegacionApp
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.navegacion.entradasApp
import co.edu.uniandes.alarmasqr.navegacion.reemplazarTodo
import co.edu.uniandes.alarmasqr.navegacion.rememberBackStackApp
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class M05GuardadaTest {
    @get:Rule val regla = createComposeRule()
    private val repositorio = RepositorioDataset.desdeAssets(ApplicationProvider.getApplicationContext())

    @Before fun reiniciar() = repositorio.reiniciar()

    @Test
    fun `M05 resalta la alarma nueva, muestra el snackbar y Deshacer la quita y vuelve a M02`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) } }
        }
        regla.runOnUiThread { repositorio.agregarDesdeEvento("e-entrega"); pila.reemplazarTodo(Pantalla.M05("a-entrega")) }
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M05").assertIsDisplayed()
        regla.onNodeWithText("DOMINGO 30").assertIsDisplayed()
        regla.onNodeWithText("Nueva").assertIsDisplayed()
        regla.onNodeWithText(repositorio.dataset.mensajes.alarmaGuardada).assertIsDisplayed()
        regla.onNodeWithTag("fab-escanear").assertIsDisplayed()
        regla.capturar("M05")
        regla.onNodeWithTag("deshacer").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertNull(repositorio.alarma("a-entrega"))
    }
}
```

- [ ] **Step 2: Correr para ver el fallo.**

- [ ] **Step 3: Registrar M05 en `entradasApp`**

```kotlin
    entry<Pantalla.M05> { clave ->
        val vm = viewModel(key = clave.id) { M02InicioViewModel(repositorio, alarmaNueva = clave.id) }
        val estado by vm.estado.collectAsStateWithLifecycle()
        val snackbar = LocalSnackbarApp.current
        val mensajes = repositorio.dataset.mensajes
        LaunchedEffect(clave.id) {
            // F-M05: «Deshacer (5 s)». Ventana fija de Movimiento.DeshacerMs; al vencer, el snackbar se retira solo.
            val resultado = withTimeoutOrNull(Movimiento.DeshacerMs) {
                snackbar.showSnackbar(message = mensajes.alarmaGuardada, actionLabel = mensajes.deshacer, duration = SnackbarDuration.Indefinite)
            }
            if (resultado == SnackbarResult.ActionPerformed) { vm.deshacer(); pila.reemplazarTodo(Pantalla.M02) }
            else if (resultado == null) snackbar.currentSnackbarData?.dismiss()
        }
        M02InicioScreen(estado, alTocarAlarma = { pila.irA(Pantalla.M06(it)) }, alCambiarActiva = vm::cambiarActiva, codigo = "M05")
    }
```
Imports: `androidx.compose.material3.SnackbarDuration`, `androidx.compose.material3.SnackbarResult`, `kotlinx.coroutines.withTimeoutOrNull`, `co.edu.uniandes.alarmasqr.navegacion.LocalSnackbarApp`, `co.edu.uniandes.alarmasqr.ui.theme.Movimiento`.

- [ ] **Step 4: Correr todo, verificar la captura M05 contra 4:223 (snackbar 350×48 a 16 de la barra; el FAB sube con él), commit**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --no-daemon
git add apps/movil docs/verificacion && git commit -m "M05: alarma guardada con snackbar Deshacer de 5 s"
```

---

### Task 14: Flujos T1, T6 y T7, capturas de las hojas, documentación y cierre

**Files:**
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/FlujosPersonaATest.kt`
- Modify: `README.md`, `CLAUDE.md`, `docs/PLAN_MAQUETACION.md`, `docs/verificacion/README.md`
- Create: `docs/verificacion/<código>.png` y `<código>-figma.png` (13 parejas)

**Interfaces:**
- Consumes: todo lo anterior; `ShadowApplication.grantPermissions` para simular el permiso de cámara.
- Produces: pruebas de navegación T1, T6, T7 (spec §6); documentación al día.

- [ ] **Step 1: Pruebas de flujo**

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import android.Manifest
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.ui.QUALIFIERS_MOVIL
import co.edu.uniandes.alarmasqr.ui.capturar
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Flujos T1, T6 y T7 de FUNCIONALIDADES.md sobre las entradas reales (entradasApp), navegando por código. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = QUALIFIERS_MOVIL)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FlujosPersonaATest {
    @get:Rule val regla = createComposeRule()
    private val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    private val repositorio = RepositorioDataset.desdeAssets(app)
    private lateinit var pila: NavBackStack<NavKey>

    @Before
    fun preparar() {
        repositorio.reiniciar()
        shadowOf(app).grantPermissions(Manifest.permission.CAMERA)   // «Abrir ajustes» ⏩ termina de inmediato (D5)
    }

    private fun montar(inicio: Pantalla) = regla.setContent {
        pila = rememberBackStackApp(inicio)
        AlarmasQRTheme { NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) } }
    }

    @Test
    fun `T1 escanear un QR deja la alarma guardada M02 - M12 - M03 - M04 - M05`() {
        montar(Pantalla.M02)
        regla.onNodeWithTag("fab-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.onNodeWithTag("abrir-ajustes").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.onNodeWithTag("visor").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M04").assertIsDisplayed()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()          // hoja sobre la lista atenuada
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M04("a-entrega")), pila.toList())
        regla.capturar("M04")
        regla.onNodeWithTag("eliminar").performClick()
        regla.onNodeWithText("¿Eliminar alarma?").assertIsDisplayed()
        regla.capturar("M04d-flujo")
        regla.onNodeWithTag("dialogo-seguro").performClick()
        regla.onNodeWithTag("listo").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M05").assertIsDisplayed()
        regla.onNodeWithText("Nueva").assertIsDisplayed()
        assertNotNull(repositorio.alarma("a-entrega"))
    }

    @Test
    fun `T6 errores - QR que no es evento M03 - M13 - M03 y hoja M02h`() {
        montar(Pantalla.M02)
        regla.onNodeWithTag("fab-escanear").performSemanticsAction(SemanticsActions.OnLongClick)
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M02h").assertIsDisplayed()
        regla.capturar("M02h")
        regla.onNodeWithTag("hoja-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
        regla.onNodeWithTag("abrir-ajustes").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        regla.onNodeWithTag("vibra").performClick()
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M13").assertIsDisplayed()
        regla.onNodeWithTag("volver-a-escanear").performClick()
        regla.onNodeWithTag("pantalla-M03").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M03), pila.toList())
    }

    @Test
    fun `T7 crear cuenta o continuar como invitado M01 - M00a - M00b`() {
        montar(Pantalla.M01)
        regla.onNodeWithText("Comenzar").performClick()
        regla.onNodeWithTag("pantalla-M00a").assertIsDisplayed()
        regla.onNodeWithTag("pie-acceso").performClick()
        regla.onNodeWithTag("pantalla-M00b").assertIsDisplayed()
        regla.onNodeWithTag("entrar").performClick()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02), pila.toList())
    }

    @Test
    fun `T7b invitado desde M01 llega al inicio vacio`() {
        montar(Pantalla.M01)
        regla.onNodeWithText("Conectar luego en Ajustes").performClick()
        regla.onNodeWithTag("pantalla-M02v").assertIsDisplayed()
        regla.onNodeWithTag("vacio-escanear").performClick()
        regla.onNodeWithTag("pantalla-M12").assertIsDisplayed()
    }
}
```

- [ ] **Step 2: Correr todo con lint y APK**

```bash
cd apps/movil && ./gradlew lintDebug testDebugUnitTest assembleDebug --no-daemon
```
Esperado: verde; sin advertencias nuevas de lint sobre `MissingPermission` (la notificación comprueba `tienePermiso`) ni `UnusedResources`. Si lint reclama `FullScreenIntent` en API 34+, agregar `USE_FULL_SCREEN_INTENT` ya está en el manifiesto; documentar que Android 14 puede pedir activarlo en ajustes.

- [ ] **Step 3: Completar `docs/verificacion/`**

Para cada código de la tabla del README de verificación: exportar el marco de Figma (`get_screenshot` con `maxDimension = 1688`, fileKey `4nHD4ygcnP33UH0gAhaii5`) como `<código>-figma.png`, copiar `app/build/verificacion/<código>.png` (M02h, M04, M05 salen del flujo; M04d de la prueba de la hoja) y marcar la fila «ok» o anotar la diferencia aceptada (D7, D8, D10, snackbar a 16 en vez de 60 sobre el FAB).

- [ ] **Step 4: Documentación**

- `README.md`: en «Frameworks y versiones» agregar `camera-mlkit-vision` (versión resuelta); nueva sección «Plan 2 · móvil de la Persona A» con: pantallas hechas, cómo probar la cámara (QR `alarmasqr://evento/e-entrega`), cómo probar la alarma con la app cerrada (D4: suena al minuto; Android 13+ pide notificaciones, Android 14 puede pedir «pantalla completa» en ajustes), toques ⏩ (visor, «vibra al detectar», «Abrir ajustes»), decisiones D1–D10 en una tabla, y las capturas de `docs/verificacion/`. En «Cómo continuar (Persona B)»: las pantallas reales se registran en `navegacion/EntradasApp.kt` (`entradasApp`), los ViewModels se crean con `viewModel { … }` dentro de la entrada (decorador ya registrado), el snackbar global es `LocalSnackbarApp`, y `NotificacionesAlarma.intentSonando` abre M10 con `alarma/{id}/sonando`.
- `CLAUDE.md`: en «Reglas de diseño», precisar «píldora activa de la barra inferior en Gris Niebla (DS §6)»; en «Navegación», añadir «las pantallas reales se registran en `EntradasApp.kt`; el marcador es el fallback»; en «Stack», `camera-mlkit-vision`.
- `docs/PLAN_MAQUETACION.md`: marcar en §7 «La alarma suena con la app cerrada en un dispositivo real» según la prueba manual de la Tarea 12 y anotar el estado del Plan 2.
- Replicar en el repo de UX (issue o PR) las notas D7 (sufijo del bloque «Sonará») y D8 (gris sobre Tinta), y los nombres de capa desactualizados de M03b (anexo grupo 2 §4).

- [ ] **Step 5: Commit y cierre de rama**

```bash
git add -A && git commit -m "Plan 2: flujos T1/T6/T7, capturas de verificación y documentación"
```
Luego seguir `superpowers:finishing-a-development-branch`: PR `feature/plan2-movil-persona-a` → `main` con el resumen de pantallas, decisiones D1–D10 y las parejas de capturas; revisión de la Persona B antes de fusionar.

---

## Verificación de cobertura de la spec (Plan 2)

| Spec | Tarea |
|---|---|
| §0.2 mismo kit (Material 3 re-tematizado con `Tokens.kt`) | 2, 3, 4 (todos los componentes sobre M3 + tokens) |
| §0.3 sin imágenes: iconos, textura y QR vectoriales/generados | 2 (`Iconos`), 4 (`BandaTextura`, `CodigoQR`, `DianaQR`, `Logotipo`, `Destello`) |
| §0.4 pixel-perfect 390×844, anchos flexibles | todas las pantallas (`fillMaxWidth` + tokens); capturas en 5–14 |
| §2.1 `datos`: modelos, `RepositorioDataset` (agregar/eliminar/deshacer) | 1, 7 (`cambiarEstado`) |
| §2.1 `alarma`: `AlarmManager` exacto, receptor, notificación de pantalla completa, deep link a M10 | 11, 10 (`destinoDesdeIntent`), 12 (programa al llegar a M04) |
| §2.1 `qr`: CameraX + ML Kit, `EventoDetectado` / `QRInvalido` | 9 |
| §2.2 reglas del back stack (raíz, reemplazar, pestañas, hojas, diálogos como estado) | 3 (`NavegacionApp`), 7, 12, 13 |
| §2.2 controles ⏩ (M12 primera vez, «Abrir ajustes», visor, «vibra», M03b «Continuar») | 3, 7, 8, 9, 10 |
| §2.3 M01, M00a, M00b, M02v, M02, M02h, M12, M03, M13, M03b, M04 (+M04d), M05 | 5, 6, 7, 7, 7, 7, 8, 9, 8, 10, 12, 13 |
| §2.3 horas 12 h con sufijo | 1 (`FormatoHora`), 3, 12 |
| §4 componentes móviles L03–L08 | 2 (botones, campo, casilla, chips, fila, divisor), 3 (barra, nav, FAB, interruptor, tarjeta, agrupador), 4 (textura, QR, snackbar, diálogo, tarjeta de evento, sello), 9 (`Asa`) |
| §5.1 medidas → tokens primero | 1 (v1.10) y el paso «Tokens de esta tarea» de cada tarea |
| §5.2 capturas y parejas en `docs/verificacion` | 5 (helper), cada pantalla, 14 |
| §5.3 lista de comprobación | «Global Constraints» + 14 Step 3 |
| §6 pruebas: repositorio, «Sonará», back stack, T1/T6/T7 | 1, 3, 7, 12, 13, 14 |
| §6 CI | sin cambios de fondo: `ci.yml` ya corre `lintDebug testDebugUnitTest assembleDebug` |
| §7 README/CLAUDE/plan, rama y PR | 14 |
| Fuera de alcance (M02b, M06–M11, M07/M08 destinos) | siguen en el marcador; `entradasApp` los recibe cuando la Persona B los construya |
