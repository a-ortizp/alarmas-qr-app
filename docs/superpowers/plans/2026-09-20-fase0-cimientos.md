# Plan 1 · Fase 0 · Cimientos de ambas apps

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Dejar `apps/movil` y `apps/web` compilando con las versiones fijadas, navegando por las rutas de `docs/TRAZABILIDAD.md` con pantallas marcadoras, leyendo `dataset.json`, con CI verde y README con versiones.

**Architecture:** Móvil: Kotlin + Compose Material 3 re-tematizado, Navigation 3 con un solo `NavBackStack<Pantalla>` y una estrategia de escena de hoja inferior; datos en memoria desde `assets/dataset.json` con kotlinx.serialization. Web: Angular 22 standalone + señales, rutas generadas desde una constante `PANTALLAS`, `DatosService` con `httpResource`, layout con barra lateral; pruebas con Vitest + `TestBed`.

**Tech Stack:** JDK 17 · Gradle 8.14.3 · AGP 8.13.2 · Kotlin 2.2.21 · Compose BOM 2026.06.00 · Navigation 3 1.1.7 · lifecycle 2.10.0 · kotlinx-serialization-json 1.11.0 · Robolectric 4.16 · Node 22.23.2 · Angular 22.1 · Vitest.

**Spec:** `docs/superpowers/specs/2026-09-20-maquetacion-persona-a-design.md`

## Global Constraints

- compileSdk = targetSdk = 36, minSdk = 26 (Navigation 3 exige compileSdk 36).
- Nunca escribir colores, tamaños ni radios a mano: `Colores`, `Tipografia`, `Tamanos`, `Radios`, `Espacio` de `Tokens.kt`; variables `--color-*`, `--text-*`, `--size-*`, `--radius-*`, `--space-*` de `tokens.css`.
- Cada ruta lleva el código de pantalla (M06, W04) para navegar por código en las pruebas; rutas y nombres de `docs/TRAZABILIDAD.md` son obligatorios.
- Todo texto visible en español; identificadores de código pueden ir en inglés, los nombres de pantalla conservan su código.
- `dataset.json` es idéntico en móvil y web (fuente: `packages/tokens/dataset.json`).
- Sin imágenes en lugar de componentes; sin imágenes de fondo para imitar el diseño.
- Commits con el código de pantalla o el ámbito al inicio («Fase 0: …», «M03: …»); firma `Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>`.
- Todo el trabajo en la rama `feature/fase0-cimientos`; no fusionar a `main`.

## Mapa de archivos

Móvil (`apps/movil`, paquete `co.edu.uniandes.alarmasqr`, raíz de fuentes `app/src/main/java/co/edu/uniandes/alarmasqr/`):

| Archivo | Responsabilidad |
|---|---|
| `gradle/wrapper/gradle-wrapper.properties` · `gradle/libs.versions.toml` · `app/build.gradle.kts` | Versiones y dependencias fijadas |
| `datos/Modelos.kt` | Data classes serializables del dataset (usuario, alarma, evento QR, mensajes…) |
| `datos/RepositorioDataset.kt` | Carga única del JSON; `StateFlow` de alarmas; agregar / eliminar / deshacer; bandera de permiso de cámara |
| `navegacion/Pantalla.kt` | Claves `NavKey` con código, ruta y F-código; `porRuta`, `con(id)` |
| `navegacion/HojaInferiorSceneStrategy.kt` | Escena de hoja inferior (M02h, M04) sobre la entrada anterior con velo Tinta 55 % |
| `navegacion/NavegacionApp.kt` | `NavDisplay` + `entryProvider` (marcadores) + andamio con barra inferior y FAB |
| `ui/pantallas/PantallaMarcador.kt` | Composable marcador «Pantalla pendiente» con el código de la pantalla |
| `MainActivity.kt` | Arranca `NavegacionApp` dentro de `AlarmasQRTheme` |
| `app/src/test/.../datos/RepositorioDatasetTest.kt` · `navegacion/PantallaTest.kt` · `navegacion/NavegacionAppTest.kt` | Pruebas JVM (Robolectric para Compose) |

Web (`apps/web`):

| Archivo | Responsabilidad |
|---|---|
| `package.json` · `angular.json` · `tsconfig*.json` | Proyecto Angular 22 regenerado (prefijo `aq`, zoneless, Vitest) |
| `src/styles.css` · `src/tokens.css` · `public/fonts/*` · `public/dataset.json` | Tokens, fuentes empaquetadas, dataset |
| `src/app/datos/modelos.ts` | Interfaces TypeScript del dataset |
| `src/app/datos/datos.service.ts` | `httpResource` sobre `dataset.json` + señales derivadas |
| `src/app/datos/sesion.service.ts` | Sesión simulada (`iniciada`, `iniciar()`, `cerrar()`) |
| `src/app/navegacion/pantallas.ts` | Constante `PANTALLAS` (código → ruta → título) |
| `src/app/app.routes.ts` | Rutas generadas desde `PANTALLAS` con `data.codigo` |
| `src/app/layout/aq-layout-app/` | Barra lateral (marcadora) + `router-outlet` para páginas autenticadas |
| `src/app/pantallas/<código>/…component.ts` | Un componente marcador por página (W00, W01, W03, W04, W05, W06) |
| `src/app/**/*.spec.ts` | Pruebas Vitest |

Raíz: `.github/workflows/ci.yml`, `README.md`, `CLAUDE.md`, `docs/PLAN_MAQUETACION.md`.

---

### Task 1: Versiones móviles y compilación con Navigation 3

**Files:**
- Modify: `apps/movil/gradle/wrapper/gradle-wrapper.properties`
- Modify: `apps/movil/gradle/libs.versions.toml`
- Modify: `apps/movil/app/build.gradle.kts`
- Modify: `apps/movil/gradle.properties`

**Interfaces:**
- Produces: catálogo `libs.*` que usan las tareas 2–5 (`libs.androidx.navigation3.runtime`, `libs.androidx.navigation3.ui`, `libs.androidx.lifecycle.viewmodel.navigation3`, `libs.kotlinx.serialization.json`, `libs.robolectric`, `libs.androidx.compose.ui.test.junit4`).

- [ ] **Step 1: Exportar el SDK y comprobar que el esqueleto actual compila**

```bash
export ANDROID_HOME=$HOME/Android/Sdk
cd apps/movil && ./gradlew --version && ./gradlew assembleDebug --no-daemon -q
```
Expected: `BUILD SUCCESSFUL` con Gradle 8.9 (línea base antes de tocar versiones). Si Gradle pide `local.properties`, crear `apps/movil/local.properties` con `sdk.dir=/home/alejo/Android/Sdk` (está en `.gitignore`).

- [ ] **Step 2: Subir el wrapper a Gradle 8.14.3**

`apps/movil/gradle/wrapper/gradle-wrapper.properties`:
```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.14.3-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

- [ ] **Step 3: Reescribir `libs.versions.toml`**

Nota (ruling 2026-09-20): el BOM 2026.09.00, core-ktx 1.19.0 y lifecycle 2.11.0 exigen compileSdk 37 + AGP 9.1 (AAR metadata verificado en Maven); se fijan las últimas versiones compatibles con compileSdk 36 / AGP 8.13: BOM 2026.06.00 (Compose 1.11.3, Material3 1.4.0), core-ktx 1.18.0, lifecycle 2.10.0.

```toml
[versions]
agp = "8.13.2"
kotlin = "2.2.21"
coreKtx = "1.18.0"
lifecycle = "2.10.0"
activityCompose = "1.13.0"
composeBom = "2026.06.00"
navigation3 = "1.1.7"
serializationJson = "1.11.0"
camerax = "1.6.2"
mlkitBarcode = "17.3.0"
zxing = "3.5.4"
junit = "4.13.2"
androidxJunit = "1.3.0"
espresso = "3.7.0"
robolectric = "4.16"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-navigation3 = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-navigation3", version.ref = "lifecycle" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
androidx-navigation3-runtime = { group = "androidx.navigation3", name = "navigation3-runtime", version.ref = "navigation3" }
androidx-navigation3-ui = { group = "androidx.navigation3", name = "navigation3-ui", version.ref = "navigation3" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "serializationJson" }
androidx-camera-core = { group = "androidx.camera", name = "camera-core", version.ref = "camerax" }
androidx-camera-camera2 = { group = "androidx.camera", name = "camera-camera2", version.ref = "camerax" }
androidx-camera-lifecycle = { group = "androidx.camera", name = "camera-lifecycle", version.ref = "camerax" }
androidx-camera-view = { group = "androidx.camera", name = "camera-view", version.ref = "camerax" }
mlkit-barcode-scanning = { group = "com.google.mlkit", name = "barcode-scanning", version.ref = "mlkitBarcode" }
zxing-core = { group = "com.google.zxing", name = "core", version.ref = "zxing" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "androidxJunit" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espresso" }
robolectric = { group = "org.robolectric", name = "robolectric", version.ref = "robolectric" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

- [ ] **Step 4: Actualizar `app/build.gradle.kts`**

Reemplazar el archivo completo por:
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "co.edu.uniandes.alarmasqr"
    compileSdk = 36

    defaultConfig {
        applicationId = "co.edu.uniandes.alarmasqr"
        minSdk = 26           // fuentes variables y canales de notificación
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    signingConfigs {
        // Maquetación académica: la release se firma con el keystore de depuración
        // para que el APK de la Release sea instalable sin cuenta de Play.
        create("release") { initWith(getByName("debug")) }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }

    buildFeatures { compose = true }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    testOptions { unitTests { isIncludeAndroidResources = true } }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.mlkit.barcode.scanning)
    implementation(libs.zxing.core)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.junit)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

- [ ] **Step 5: `gradle.properties`**

Asegurar que contenga (añadir las que falten):
```properties
org.gradle.jvmargs=-Xmx3g -Dfile.encoding=UTF-8
org.gradle.caching=true
org.gradle.configuration-cache=true
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
```

- [ ] **Step 6: Compilar y correr lint**

```bash
cd apps/movil && ./gradlew assembleDebug lintDebug --no-daemon -q
```
Expected: `BUILD SUCCESSFUL`. Si AGP 8.13.2 pide una versión de Gradle mayor, usar la que indique el mensaje en el wrapper. Si lint falla por `ObsoleteLintCustomCheck` o similar de librerías externas, añadir en `android { lint { abortOnError = true; checkDependencies = false } }` y repetir.

- [ ] **Step 7: Commit**

```bash
git add apps/movil/gradle apps/movil/app/build.gradle.kts apps/movil/gradle.properties
git commit -m "Fase 0: versiones móviles (AGP 8.13, Kotlin 2.2, Compose BOM 2026.06, Navigation 3 1.1.7)

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```

---

### Task 2: Modelos y `RepositorioDataset`

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/Modelos.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/datos/RepositorioDataset.kt`
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/datos/RepositorioDatasetTest.kt`

**Interfaces:**
- Produces: `data class Dataset`, `Alarma`, `Usuario`, `EventoQR`, `Mensajes`; `class RepositorioDataset(json: String)` con `val dataset: Dataset`, `val alarmas: StateFlow<List<Alarma>>`, `fun alarma(id: String): Alarma?`, `fun agregar(alarma: Alarma)`, `fun eliminar(id: String)`, `fun deshacer()`, `var permisoCamaraPedido: Boolean`; `companion object { fun desdeAssets(context: Context): RepositorioDataset }`.

- [ ] **Step 1: Prueba que falla**

`RepositorioDatasetTest.kt`:
```kotlin
package co.edu.uniandes.alarmasqr.datos

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class RepositorioDatasetTest {
    private val json = File("src/main/assets/dataset.json").readText()

    @Test
    fun `lee el dataset de los mockups`() {
        val repo = RepositorioDataset(json)
        assertEquals("Andrés Rojas", repo.dataset.usuario.nombre)
        assertEquals(6, repo.alarmas.value.size)
        assertEquals("Reunión con el tutor", repo.alarma("a-tutor")?.titulo)
        assertEquals("¿Eliminar alarma?", repo.dataset.mensajes.confirmarEliminarTitulo)
        assertEquals("e-entrega", repo.dataset.pantallazoRecibido.eventoDetectado)
    }

    @Test
    fun `eliminar y deshacer restauran la lista`() {
        val repo = RepositorioDataset(json)
        repo.eliminar("a-tutor")
        assertNull(repo.alarma("a-tutor"))
        assertEquals(5, repo.alarmas.value.size)
        repo.deshacer()
        assertEquals(6, repo.alarmas.value.size)
        assertEquals("a-tutor", repo.alarmas.value.first().id)
    }

    @Test
    fun `agregar pone la alarma en orden cronologico y deshacer la quita`() {
        val repo = RepositorioDataset(json)
        val nueva = repo.alarma("a-tutor")!!.copy(id = "a-nueva", titulo = "Nueva", eventoInicio = "2026-08-27T08:30:00-05:00", suena = "2026-08-27T08:00:00-05:00", chips = listOf("Nueva"))
        repo.agregar(nueva)
        assertEquals(7, repo.alarmas.value.size)
        assertEquals(listOf("a-tutor", "a-nueva"), repo.alarmas.value.take(2).map { it.id })
        repo.deshacer()
        assertEquals(6, repo.alarmas.value.size)
        assertTrue(repo.alarmas.value.none { it.id == "a-nueva" })
    }
}
```

- [ ] **Step 2: Correrla y verificar que falla**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests '*RepositorioDatasetTest*' --no-daemon -q
```
Expected: FAIL de compilación («Unresolved reference: RepositorioDataset»).

- [ ] **Step 3: Modelos**

`Modelos.kt`:
```kotlin
package co.edu.uniandes.alarmasqr.datos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Espejo tipado de dataset.json (v1.2). Solo se declaran los campos que usan las pantallas; el resto se ignora. */
@Serializable
data class Dataset(
    val meta: Meta,
    val usuario: Usuario,
    val alarmas: List<Alarma>,
    val calendario: Calendario,
    val eventosQR: List<EventoQR>,
    val qrInvalido: QRInvalido,
    val pantallazoRecibido: PantallazoRecibido,
    val mensajes: Mensajes,
)

@Serializable data class Meta(val version: String, val hoy: String, val zonaHoraria: String)

@Serializable
data class Usuario(
    val id: String,
    val nombre: String,
    val aliasPublico: String,
    val correo: String,
    val iniciales: String,
    val privacidad: Privacidad,
    val ajustes: Ajustes,
)

@Serializable
data class Privacidad(
    @SerialName("apariciónEnQuienesEscanearon") val aparicionEnQuienesEscanearon: String,
    val mostrarEstadoDeMiAlarma: Boolean,
    val contarMiYaVoyEnMetricas: Boolean,
)

@Serializable
data class Ajustes(
    val anticipacionPorDefectoMin: Int,
    val sumarTrayectoDesdeUbicacionHabitual: Boolean,
    val sonidoPorDefecto: String,
    val respetarNoMolestar: Boolean,
    val posponerPorDefectoMin: Int,
    val confirmarAntesDeAutoAjustar: Boolean,
    val calendariosVinculados: List<String>,
    val permisos: Permisos,
)

@Serializable
data class Permisos(val alarmasExactas: Boolean, val notificaciones: Boolean, val bateriaSinRestricciones: Boolean, val camara: Boolean)

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
)

@Serializable data class Calendario(val mes: String, val diasConAlarmas: Map<String, Int>, val diaSeleccionado: String)

@Serializable
data class EventoQR(val id: String, val alarmaId: String, val titulo: String, val codigoQR: String, val escaneos: Int, val etiqueta: String)

@Serializable data class QRInvalido(val contenido: String, val tipoDetectado: String, val diagnostico: String)

@Serializable data class PantallazoRecibido(val origen: String, val grupo: String, val mensaje: String, val eventoDetectado: String)

@Serializable
data class Mensajes(
    val alarmaGuardada: String,
    val deshacer: String,
    val camaraActiva: String,
    val sinAlarmas: String,
    val sinAlarmasDetalle: String,
    val confirmarEliminarTitulo: String,
    val confirmarEliminarCuerpo: String,
    val confirmarEliminarSeguro: String,
    val confirmarEliminarAccion: String,
    val confirmarCerrarSesionTitulo: String,
    val confirmarCerrarSesionCuerpo: String,
    val confirmarCerrarSesionSeguro: String,
    val confirmarCerrarSesionAccion: String,
)
```

- [ ] **Step 4: Repositorio**

`RepositorioDataset.kt`:
```kotlin
package co.edu.uniandes.alarmasqr.datos

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import java.time.OffsetDateTime

/**
 * Único origen de datos simulados de la app (docs/PLAN_MAQUETACION.md §5). Se carga una vez desde assets/dataset.json
 * y las mutaciones (agregar, eliminar, deshacer) viven en memoria mientras el proceso exista.
 */
class RepositorioDataset(json: String) {
    val dataset: Dataset = formato.decodeFromString(Dataset.serializer(), json)

    private val _alarmas = MutableStateFlow(dataset.alarmas.sortedBy { OffsetDateTime.parse(it.eventoInicio) })
    val alarmas: StateFlow<List<Alarma>> = _alarmas.asStateFlow()

    /** Última lista antes de la mutación más reciente; la usa «Deshacer · 5 s». */
    private var anterior: List<Alarma>? = null

    /** ⏩ Primer uso: el FAB pasa por M12 solo la primera vez (docs/NAVEGACION.md §6, decisión a). */
    var permisoCamaraPedido: Boolean = false

    fun alarma(id: String): Alarma? = _alarmas.value.firstOrNull { it.id == id }

    fun evento(id: String): EventoQR? = dataset.eventosQR.firstOrNull { it.id == id }

    fun agregar(alarma: Alarma) = mutar { lista ->
        (lista.filterNot { it.id == alarma.id } + alarma).sortedBy { OffsetDateTime.parse(it.eventoInicio) }
    }

    fun eliminar(id: String) = mutar { lista -> lista.filterNot { it.id == id } }

    fun deshacer() {
        anterior?.let { _alarmas.value = it }
        anterior = null
    }

    private fun mutar(cambio: (List<Alarma>) -> List<Alarma>) {
        anterior = _alarmas.value
        _alarmas.value = cambio(_alarmas.value)
    }

    companion object {
        private val formato = Json { ignoreUnknownKeys = true }

        @Volatile private var instancia: RepositorioDataset? = null

        /** Instancia única por proceso, leída de assets/dataset.json. */
        fun desdeAssets(context: Context): RepositorioDataset =
            instancia ?: synchronized(this) {
                instancia ?: RepositorioDataset(
                    context.applicationContext.assets.open("dataset.json").bufferedReader().use { it.readText() }
                ).also { instancia = it }
            }
    }
}
```

- [ ] **Step 5: Correr la prueba**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests '*RepositorioDatasetTest*' --no-daemon -q
```
Expected: PASS (3 pruebas). Si el JSON trae claves no modeladas, `ignoreUnknownKeys` las descarta; si falta una clave modelada, el error dirá cuál: corregir el modelo, no el JSON.

- [ ] **Step 6: Commit**

```bash
git add apps/movil/app/src
git commit -m "Fase 0: modelos y RepositorioDataset con kotlinx.serialization

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```

---

### Task 3: `Pantalla` como claves de Navigation 3

**Files:**
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navigation/Pantalla.kt` → mover a `navegacion/Pantalla.kt` (borrar la carpeta `navigation`)
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/PantallaTest.kt`

**Interfaces:**
- Produces: `sealed interface Pantalla : NavKey { val codigo; val ruta; val titulo; val funcionalidad; val esRaiz; val conNavegacionInferior; val conFab }`, objetos `Pantalla.M01`, `M00a`, `M00b`, `M02v`, `M02`, `M02h`, `M02b`, `M03b`, `M03`, `M07`, `M11`, `M12`, `M13`, clases `M04(id)`, `M05(id)`, `M06(id)`, `M08(id)`, `M09(id)`, `M10(id)`; `Pantalla.inicio`, `Pantalla.todas`, `Pantalla.porRuta(ruta: String): Pantalla?`.

- [ ] **Step 1: Prueba que falla**

`PantallaTest.kt`:
```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PantallaTest {
    @Test
    fun `las 19 pantallas conservan codigo y ruta de TRAZABILIDAD`() {
        assertEquals(19, Pantalla.todas.size)
        assertEquals("bienvenida", Pantalla.M01.ruta)
        assertEquals("alarma/a-tutor/creada", Pantalla.M04("a-tutor").ruta)
        assertEquals("F-M04", Pantalla.M04("a-tutor").funcionalidad)
        assertEquals(Pantalla.M01, Pantalla.inicio)
    }

    @Test
    fun `porRuta resuelve rutas fijas y con id`() {
        assertEquals(Pantalla.M03, Pantalla.porRuta("escanear"))
        assertEquals(Pantalla.M13, Pantalla.porRuta("escanear/invalido"))
        assertEquals(Pantalla.M10("a-tutor"), Pantalla.porRuta("alarma/a-tutor/sonando"))
        assertEquals(Pantalla.M06("a-x"), Pantalla.porRuta("alarma/a-x"))
        assertNull(Pantalla.porRuta("no-existe"))
    }

    @Test
    fun `solo M02, M02b, M05 y M11 llevan barra inferior`() {
        val conBarra = Pantalla.todas.filter { it.conNavegacionInferior }.map { it.codigo }.toSet()
        assertEquals(setOf("M02v", "M02", "M02b", "M05", "M11"), conBarra)
        assertTrue(Pantalla.M03.esRaiz.not())
    }
}
```

- [ ] **Step 2: Verificar que falla**

```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests '*PantallaTest*' --no-daemon -q
```
Expected: FAIL («Unresolved reference: todas» o por paquete `navegacion` inexistente).

- [ ] **Step 3: Implementar `navegacion/Pantalla.kt` y borrar `navigation/`**

```bash
git mv apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navigation/Pantalla.kt apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/Pantalla.kt
```

Contenido nuevo:
```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Las 19 pantallas de la app móvil como claves de Navigation 3, con su código de mockup, ruta y funcionalidad
 * (docs/TRAZABILIDAD.md §1). La ruta sirve para deep links (notificación de la alarma, intent SEND) y para las
 * pruebas, que navegan por código. Las hojas M02h y M04 son claves (tienen ruta); los diálogos M04d/M06d/M11d no.
 */
@Serializable
sealed interface Pantalla : NavKey {
    val codigo: String
    val ruta: String
    val titulo: String
    val funcionalidad: String
    val esRaiz: Boolean get() = false
    val conNavegacionInferior: Boolean get() = false
    val conFab: Boolean get() = false
    /** Se dibuja como hoja inferior sobre la entrada anterior (HojaInferiorSceneStrategy). */
    val esHoja: Boolean get() = false

    @Serializable data object M01 : Pantalla {
        override val codigo = "M01"; override val ruta = "bienvenida"; override val titulo = "Bienvenida"; override val funcionalidad = "F-M01"
        override val esRaiz = true
    }
    @Serializable data object M00a : Pantalla {
        override val codigo = "M00a"; override val ruta = "registro"; override val titulo = "Crear cuenta"; override val funcionalidad = "F-M00a"
    }
    @Serializable data object M00b : Pantalla {
        override val codigo = "M00b"; override val ruta = "entrar"; override val titulo = "Iniciar sesión"; override val funcionalidad = "F-M00b"
    }
    @Serializable data object M02v : Pantalla {
        override val codigo = "M02v"; override val ruta = "inicio/vacio"; override val titulo = "Inicio · sin alarmas"; override val funcionalidad = "F-M02"
        override val esRaiz = true; override val conNavegacionInferior = true
    }
    @Serializable data object M02 : Pantalla {
        override val codigo = "M02"; override val ruta = "inicio"; override val titulo = "Mis alarmas"; override val funcionalidad = "F-M02"
        override val esRaiz = true; override val conNavegacionInferior = true; override val conFab = true
    }
    @Serializable data object M02h : Pantalla {
        override val codigo = "M02h"; override val ruta = "inicio/agregar"; override val titulo = "Agregar evento"; override val funcionalidad = "F-M02"
        override val esHoja = true
    }
    @Serializable data object M02b : Pantalla {
        override val codigo = "M02b"; override val ruta = "calendario"; override val titulo = "Calendario"; override val funcionalidad = "F-M02"
        override val esRaiz = true; override val conNavegacionInferior = true; override val conFab = true
    }
    @Serializable data object M03b : Pantalla {
        override val codigo = "M03b"; override val ruta = "pantallazo"; override val titulo = "Pantallazo recibido"; override val funcionalidad = "F-M03"
    }
    @Serializable data object M03 : Pantalla {
        override val codigo = "M03"; override val ruta = "escanear"; override val titulo = "Escanear QR"; override val funcionalidad = "F-M03"
    }
    @Serializable data class M04(val id: String) : Pantalla {
        override val codigo = "M04"; override val ruta = "alarma/$id/creada"; override val titulo = "Alarma programada"; override val funcionalidad = "F-M04"
        override val esHoja = true
    }
    @Serializable data class M05(val id: String) : Pantalla {
        override val codigo = "M05"; override val ruta = "inicio/guardada/$id"; override val titulo = "Guardada + deshacer"; override val funcionalidad = "F-M05"
        override val esRaiz = true; override val conNavegacionInferior = true; override val conFab = true
    }
    @Serializable data class M06(val id: String) : Pantalla {
        override val codigo = "M06"; override val ruta = "alarma/$id"; override val titulo = "Editar alarma"; override val funcionalidad = "F-M06"
    }
    @Serializable data object M07 : Pantalla {
        override val codigo = "M07"; override val ruta = "evento/nuevo"; override val titulo = "Crear evento a mano"; override val funcionalidad = "F-M07"
    }
    @Serializable data class M08(val id: String) : Pantalla {
        override val codigo = "M08"; override val ruta = "evento/$id/qr"; override val titulo = "QR del evento"; override val funcionalidad = "F-M08"
    }
    @Serializable data class M09(val id: String) : Pantalla {
        override val codigo = "M09"; override val ruta = "alarma/$id/cambio"; override val titulo = "Cambio en el evento"; override val funcionalidad = "F-M09"
    }
    @Serializable data class M10(val id: String) : Pantalla {
        override val codigo = "M10"; override val ruta = "alarma/$id/sonando"; override val titulo = "La alarma suena"; override val funcionalidad = "F-M10"
        override val esRaiz = true
    }
    @Serializable data object M11 : Pantalla {
        override val codigo = "M11"; override val ruta = "ajustes"; override val titulo = "Ajustes"; override val funcionalidad = "F-M11"
        override val esRaiz = true; override val conNavegacionInferior = true
    }
    @Serializable data object M12 : Pantalla {
        override val codigo = "M12"; override val ruta = "permiso-camara"; override val titulo = "Permiso de cámara"; override val funcionalidad = "F-M12"
    }
    @Serializable data object M13 : Pantalla {
        override val codigo = "M13"; override val ruta = "escanear/invalido"; override val titulo = "QR sin evento"; override val funcionalidad = "F-M13"
    }

    companion object {
        val inicio: Pantalla = M01
        private const val ID = "{id}"

        /** Las 19 pantallas con un id de ejemplo para las parametrizadas (para tablas, pruebas y el marcador). */
        val todas: List<Pantalla> = listOf(
            M01, M00a, M00b, M02v, M02, M02h, M02b, M03b, M03, M04(ID), M05(ID), M06(ID), M07, M08(ID), M09(ID), M10(ID), M11, M12, M13,
        )

        /** Resuelve una ruta concreta («alarma/a-tutor/creada») a su clave; null si no existe. */
        fun porRuta(ruta: String): Pantalla? {
            todas.firstOrNull { !it.ruta.contains(ID) && it.ruta == ruta }?.let { return it }
            val partes = ruta.split("/")
            return todas.filter { it.ruta.contains(ID) }.firstNotNullOfOrNull { plantilla ->
                val patron = plantilla.ruta.split("/")
                if (patron.size != partes.size) return@firstNotNullOfOrNull null
                var id: String? = null
                val coincide = patron.zip(partes).all { (p, r) -> if (p == ID) { id = r; true } else p == r }
                if (!coincide || id == null) null else when (plantilla) {
                    is M04 -> M04(id!!); is M05 -> M05(id!!); is M06 -> M06(id!!)
                    is M08 -> M08(id!!); is M09 -> M09(id!!); is M10 -> M10(id!!)
                    else -> null
                }
            }
        }
    }
}
```

- [ ] **Step 4: Actualizar el import en `MainActivity.kt` si lo tuviera y correr la prueba**

```bash
cd apps/movil && grep -rn "alarmasqr.navigation" app/src && ./gradlew testDebugUnitTest --tests '*PantallaTest*' --no-daemon -q
```
Expected: sin referencias al paquete viejo; PASS (3 pruebas).

- [ ] **Step 5: Commit**

```bash
git add -A apps/movil/app/src
git commit -m "Fase 0: Pantalla como claves NavKey de Navigation 3 con rutas de TRAZABILIDAD

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```

---

### Task 4: `NavegacionApp` con marcadores, hoja inferior y andamio

**Files:**
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/HojaInferiorSceneStrategy.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/navegacion/NavegacionApp.kt`
- Create: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/ui/pantallas/PantallaMarcador.kt`
- Modify: `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/MainActivity.kt`
- Modify: `apps/movil/app/build.gradle.kts` (añadir `testImplementation(libs.androidx.compose.ui.test.manifest)`)
- Test: `apps/movil/app/src/test/java/co/edu/uniandes/alarmasqr/navegacion/NavegacionAppTest.kt`

**Interfaces:**
- Consumes: `Pantalla` (Task 3), `RepositorioDataset` (Task 2), `AlarmasQRTheme`, `Colores`, `Radios`, `Tamanos`, `Tipografia`, `Espacio` (`ui/theme`).
- Produces: `@Composable fun NavegacionApp(backStack: NavBackStack<NavKey>, repositorio: RepositorioDataset, entradas: EntryProviderScope<NavKey>.() -> Unit = {})` — las pantallas reales de los planes 2 y 3 se registran con `entradas`, que tiene prioridad sobre los marcadores; `@Composable fun rememberBackStackApp(inicio: Pantalla = Pantalla.inicio): NavBackStack<NavKey>`; `fun NavBackStack<NavKey>.irA(p: Pantalla)`, `fun NavBackStack<NavKey>.reemplazarTodo(p: Pantalla)`, `fun NavBackStack<NavKey>.irAPestana(p: Pantalla)`; `class HojaInferiorSceneStrategy<T : Any> : SceneStrategy<T>`; `@Composable fun PantallaMarcador(pantalla: Pantalla, alVolver: () -> Unit)` con `testTag("pantalla-${codigo}")`.

- [ ] **Step 1: Prueba que falla (Robolectric + Compose)**

`NavegacionAppTest.kt`:
```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
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
class NavegacionAppTest {
    @get:Rule val regla = createComposeRule()

    private val repositorio = RepositorioDataset.desdeAssets(ApplicationProvider.getApplicationContext())

    @Test
    fun `arranca en M01 y navega por codigo hasta M04 como hoja sobre M02`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp()
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.onNodeWithTag("pantalla-M01").assertIsDisplayed()

        regla.runOnUiThread { pila.reemplazarTodo(Pantalla.M02); pila.irA(Pantalla.M04("a-tutor")) }
        regla.waitForIdle()
        regla.onNodeWithTag("pantalla-M04").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M02, Pantalla.M04("a-tutor")), pila.toList())
    }

    @Test
    fun `la barra inferior cambia de pestana sin apilar`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.onNodeWithTag("nav-M11").performClick()
        regla.onNodeWithTag("pantalla-M11").assertIsDisplayed()
        assertEquals(listOf<NavKey>(Pantalla.M11), pila.toList())
    }

    @Test
    fun `volver desde el marcador saca la cima de la pila`() {
        lateinit var pila: NavBackStack<NavKey>
        regla.setContent {
            pila = rememberBackStackApp(Pantalla.M02)
            AlarmasQRTheme { NavegacionApp(pila, repositorio) }
        }
        regla.runOnUiThread { pila.irA(Pantalla.M12) }
        regla.onNodeWithTag("volver").performClick()
        regla.onNodeWithTag("pantalla-M02").assertIsDisplayed()
        assertEquals(1, pila.size)
    }
}
```

- [ ] **Step 2: Añadir la dependencia de manifiesto de pruebas y verificar que falla**

En `app/build.gradle.kts`, bloque `dependencies`, tras `testImplementation(libs.androidx.compose.ui.test.junit4)`:
```kotlin
    testImplementation(libs.androidx.compose.ui.test.manifest)
```
```bash
cd apps/movil && ./gradlew testDebugUnitTest --tests '*NavegacionAppTest*' --no-daemon -q
```
Expected: FAIL de compilación («Unresolved reference: rememberBackStackApp»).

- [ ] **Step 3: `PantallaMarcador.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import co.edu.uniandes.alarmasqr.navegacion.Pantalla
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Espacio
import co.edu.uniandes.alarmasqr.ui.theme.Tipografia

/** Marcador de la Fase 0: ocupa el lugar de una pantalla hasta que su plan la construya. */
@Composable
fun PantallaMarcador(pantalla: Pantalla, alVolver: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(Espacio.Margen).testTag("pantalla-${pantalla.codigo}"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(pantalla.codigo, style = Tipografia.H1, color = Colores.Tinta)
        Text(pantalla.titulo, style = Tipografia.Cuerpo, color = Colores.GrisTexto)
        Text("Pantalla pendiente · ${pantalla.funcionalidad}", style = Tipografia.Nota, color = Colores.GrisTexto)
        if (!pantalla.esRaiz) {
            TextButton(onClick = alVolver, modifier = Modifier.testTag("volver")) { Text("‹ Volver", style = Tipografia.Enlace, color = Colores.Tinta) }
        }
    }
}
```

- [ ] **Step 4: `HojaInferiorSceneStrategy.kt`**

```kotlin
package co.edu.uniandes.alarmasqr.navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import co.edu.uniandes.alarmasqr.ui.theme.Colores
import co.edu.uniandes.alarmasqr.ui.theme.Radios

/**
 * Hoja inferior de los mockups (M02h «Agregar evento», M04 «Alarma programada»): la entrada anterior queda visible
 * y atenuada bajo el velo Tinta 55 % (`Colores.VeloMovil`); tocar el velo saca la hoja de la pila (docs/TRAZABILIDAD.md §1).
 * Se registra antes que cualquier estrategia que no sea de superposición.
 */
@OptIn(ExperimentalMaterial3Api::class)
class HojaInferiorSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val cima = entries.lastOrNull() ?: return null
        if (cima.metadata[CLAVE] != true) return null
        @Suppress("UNCHECKED_CAST")
        return EscenaHoja(
            key = cima.contentKey as T,
            previousEntries = entries.dropLast(1),
            overlaidEntries = entries.dropLast(1),
            entrada = cima,
            alCerrar = onBack,
        )
    }

    companion object {
        private const val CLAVE = "hojaInferior"
        /** Metadatos que marcan una entrada como hoja: `entry<Pantalla.M02h>(metadata = HojaInferiorSceneStrategy.hoja())`. */
        fun hoja(): Map<String, Any> = mapOf(CLAVE to true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private data class EscenaHoja<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entrada: NavEntry<T>,
    private val alCerrar: () -> Unit,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(entrada)

    override val content: @Composable (() -> Unit) = {
        val propietario = rememberLifecycleOwner()
        ModalBottomSheet(
            onDismissRequest = alCerrar,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = Radios.Hoja,
            containerColor = Colores.Blanco,
            scrimColor = Colores.VeloMovil,
            dragHandle = { Asa() },
        ) {
            CompositionLocalProvider(LocalLifecycleOwner provides propietario) { entrada.Content() }
        }
    }
}

/** Asa de la hoja (DS comp. 30 «Hoja inferior»): 36×4, Gris Borde, píldora. */
@Composable
private fun Asa() {
    Box(Modifier.width(36.dp).height(4.dp).background(Colores.GrisBorde, Radios.Pildora))
}
```
Nota: `36.dp`/`4.dp` son la única medida de la asa y no existe como token; en el Plan 2 se agrega `Tamanos.Asa` a `design-tokens.json`, `Tokens.kt` y `tokens.css` y se reemplaza aquí. Si el compilador de Navigation 3 1.1.7 no encuentra `SceneStrategyScope`, la firma alternativa es `@Composable override fun calculateScene(entries: List<NavEntry<T>>, onBack: (Int) -> Unit): Scene<T>?` y `alCerrar = { onBack(1) }`.

- [ ] **Step 5: `NavegacionApp.kt`**

```kotlin
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
                Pantalla.todas.forEach { plantilla -> registrarMarcador(plantilla, alVolver) }
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
```
Si `entryProvider` de 1.1.7 lanza excepción por registrar dos veces el mismo tipo (cuando [entradas] ya lo registró), envolver cada `entry<…>` del marcador en `runCatching { … }`; si `entry` no acepta `metadata` como `Map`, usar `metadata = HojaInferiorSceneStrategy.hoja()` con el tipo que pida el compilador y adaptar `hoja()`.

- [ ] **Step 6: `MainActivity.kt`**

```kotlin
package co.edu.uniandes.alarmasqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.edu.uniandes.alarmasqr.datos.RepositorioDataset
import co.edu.uniandes.alarmasqr.navegacion.NavegacionApp
import co.edu.uniandes.alarmasqr.navegacion.rememberBackStackApp
import co.edu.uniandes.alarmasqr.ui.theme.AlarmasQRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repositorio = RepositorioDataset.desdeAssets(this)
        setContent {
            AlarmasQRTheme {
                val pila = rememberBackStackApp()
                NavegacionApp(backStack = pila, repositorio = repositorio)
            }
        }
    }
}
```

- [ ] **Step 7: Correr pruebas, lint y compilar**

```bash
cd apps/movil && ./gradlew testDebugUnitTest lintDebug assembleDebug --no-daemon -q
```
Expected: PASS (3 + 3 + 3 pruebas), `BUILD SUCCESSFUL`, APK en `app/build/outputs/apk/debug/app-debug.apk`. Si Robolectric no encuentra `android-all` para SDK 35, cambiar `@Config(sdk = [34])`.

- [ ] **Step 8: Commit**

```bash
git add apps/movil
git commit -m "Fase 0: NavDisplay con marcadores, hoja inferior y barra inferior (Navigation 3)

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```

---

### Task 5: Node 22.23.2 y proyecto Angular 22 regenerado

**Files:**
- Replace: todo `apps/web/` salvo `public/dataset.json`, `public/fonts/*`, `src/tokens.css`
- Modify: `apps/web/src/styles.css`, `apps/web/src/index.html`, `apps/web/angular.json`

**Interfaces:**
- Produces: proyecto con prefijo `aq`, zoneless, Vitest; `npm run build` y `npm test` verdes; `tokens.css` importado y `@font-face` de las tres fuentes.

- [ ] **Step 1: Node 22.23.2 con nvm**

```bash
source ~/.nvm/nvm.sh && nvm install 22.23.2 && nvm alias default 22.23.2 && nvm use 22.23.2 && node -v && npm -v
```
Expected: `v22.23.2`. Todos los comandos `npm`/`npx` siguientes se ejecutan tras `source ~/.nvm/nvm.sh && nvm use 22.23.2`.

- [ ] **Step 2: Preservar los archivos propios y generar el proyecto**

```bash
cd apps && mkdir -p /tmp/aq-web-keep && cp -r web/public/dataset.json web/public/fonts web/src/tokens.css /tmp/aq-web-keep/ \
 && rm -rf web && npx -y @angular/cli@22 new web --directory=web --style=css --ssr=false --zoneless --test-runner=vitest --prefix=aq --package-manager=npm --skip-git --defaults \
 && mkdir -p web/public/fonts && cp /tmp/aq-web-keep/dataset.json web/public/ && cp /tmp/aq-web-keep/fonts/* web/public/fonts/ && cp /tmp/aq-web-keep/tokens.css web/src/
```
Expected: carpeta `apps/web` nueva con `src/app/app.ts`, `app.config.ts`, `app.routes.ts`, `package.json` con `@angular/core` `^22.1.x`. Si `ng new` pregunta por `aiConfig` u otra opción, responder con la opción por defecto.

- [ ] **Step 3: Instalar el CDK y verificar `tokens.css` y `dataset.json`**

```bash
cd apps/web && npm install @angular/cdk@22 && cmp public/dataset.json ../../packages/tokens/dataset.json && cmp src/tokens.css ../../packages/tokens/tokens.css && echo IGUALES
```
Expected: `IGUALES`. Si difieren, copiar desde `packages/tokens/` (es la fuente).

- [ ] **Step 4: `styles.css`**

```css
/* Alarmas QR · estilos globales. Los valores viven en tokens.css; aquí solo se aplican. */
@import './tokens.css';

@font-face { font-family: 'Bricolage Grotesque'; src: url('/fonts/BricolageGrotesque.ttf') format('truetype'); font-weight: 200 800; font-display: swap; }
@font-face { font-family: 'Archivo'; src: url('/fonts/Archivo.ttf') format('truetype'); font-weight: 100 900; font-display: swap; }
@font-face { font-family: 'Spline Sans Mono'; src: url('/fonts/SplineSansMono.ttf') format('truetype'); font-weight: 300 700; font-display: swap; }

*, *::before, *::after { box-sizing: border-box; }
html, body { height: 100%; margin: 0; }
body {
  background: var(--color-fondo);
  color: var(--color-texto);
  font-family: var(--font-ui);
  -webkit-font-smoothing: antialiased;
}
@media (prefers-reduced-motion: reduce) { *, *::before, *::after { transition-duration: 0.01ms !important; animation-duration: 0.01ms !important; } }
```
Comprobar en `tokens.css` que existen `--color-fondo`, `--color-texto` y `--font-ui` (sí: están en la lista de variables del paquete). Si `tokens.css` define `--font-ui` con un nombre de familia distinto a los `@font-face`, ajustar los `@font-face` al nombre del token, no al revés.

- [ ] **Step 5: `index.html`**

```html
<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <title>Alarmas QR · Administración</title>
  <base href="/">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image/x-icon" href="favicon.ico">
</head>
<body>
  <aq-root></aq-root>
</body>
</html>
```
Y en `src/app/app.ts` el selector debe ser `aq-root` (lo genera el prefijo).

- [ ] **Step 6: Build y pruebas**

```bash
cd apps/web && npm run build -- --configuration production && npx ng test --watch=false
```
Expected: build OK en `dist/web/browser`; la prueba generada de `App` pasa.

- [ ] **Step 7: Commit**

```bash
git add -A apps/web
git commit -m "Fase 0: proyecto Angular 22 regenerado (prefijo aq, zoneless, Vitest) con tokens y fuentes

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```

---

### Task 6: `PANTALLAS`, rutas y marcadores web

**Files:**
- Create: `apps/web/src/app/navegacion/pantallas.ts`
- Modify: `apps/web/src/app/app.routes.ts`
- Create: `apps/web/src/app/pantallas/pantalla-marcador/pantalla-marcador.component.ts`
- Create: `apps/web/src/app/pantallas/w00-login/w00-login.component.ts`, `w01-mis-alarmas/w01-mis-alarmas.component.ts`, `w03-detalle-evento/w03-detalle-evento.component.ts`, `w04-reportes/w04-reportes.component.ts`, `w05-descargar-qr/w05-descargar-qr.component.ts`, `w06-perfil/w06-perfil.component.ts`
- Create: `apps/web/src/app/layout/aq-layout-app/aq-layout-app.component.ts`
- Modify: `apps/web/src/app/app.ts`, `app.html`
- Test: `apps/web/src/app/app.routes.spec.ts`

**Interfaces:**
- Produces: `export interface PantallaWeb { codigo: 'W00'|'W01'|'W03'|'W04'|'W05'|'W06'; ruta: string; titulo: string; funcionalidad: string; conBarraLateral: boolean }`; `export const PANTALLAS: readonly PantallaWeb[]`; `export function pantallaPorCodigo(codigo): PantallaWeb`; rutas con `data: { codigo }`; componentes `W00LoginComponent`, `W01MisAlarmasComponent`, `W03DetalleEventoComponent`, `W04ReportesComponent`, `W05DescargarQrComponent`, `W06PerfilComponent` (selector `aq-<código-nombre>`), `AqLayoutAppComponent`.

- [ ] **Step 1: Prueba que falla**

`app.routes.spec.ts`:
```ts
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { routes } from './app.routes';
import { PANTALLAS } from './navegacion/pantallas';

describe('rutas de TRAZABILIDAD §2', () => {
  beforeEach(() => TestBed.configureTestingModule({ providers: [provideZonelessChangeDetection(), provideRouter(routes)] }));

  it('declara las 6 páginas con su código', () => {
    expect(PANTALLAS.map(p => p.codigo)).toEqual(['W00', 'W01', 'W03', 'W04', 'W05', 'W06']);
    expect(PANTALLAS.find(p => p.codigo === 'W06')?.ruta).toBe('perfil');
  });

  it('/perfil muestra el marcador W06 dentro del layout con barra lateral', async () => {
    const harness = await RouterTestingHarness.create('/perfil');
    const html = harness.routeNativeElement?.ownerDocument.body.innerHTML ?? '';
    expect(html).toContain('data-codigo="W06"');
    expect(html).toContain('aq-barra-lateral');
  });

  it('/login no lleva barra lateral y / redirige a /login', async () => {
    const harness = await RouterTestingHarness.create('/');
    const html = harness.routeNativeElement?.ownerDocument.body.innerHTML ?? '';
    expect(html).toContain('data-codigo="W00"');
    expect(html).not.toContain('aq-barra-lateral');
  });
});
```

- [ ] **Step 2: Verificar que falla**

```bash
cd apps/web && npx ng test --watch=false
```
Expected: FAIL («Cannot find module './navegacion/pantallas'»).

- [ ] **Step 3: `pantallas.ts`**

```ts
/** Páginas de la web (docs/TRAZABILIDAD.md §2, web v1.5). Los estados (pestañas, filtros, snackbars) no son rutas. */
export type CodigoPantalla = 'W00' | 'W01' | 'W03' | 'W04' | 'W05' | 'W06';

export interface PantallaWeb {
  codigo: CodigoPantalla;
  ruta: string;
  titulo: string;
  funcionalidad: string;
  /** Las páginas autenticadas se muestran dentro del layout con barra lateral. */
  conBarraLateral: boolean;
}

export const PANTALLAS: readonly PantallaWeb[] = [
  { codigo: 'W00', ruta: 'login', titulo: 'Inicio de sesión', funcionalidad: 'F-W00', conBarraLateral: false },
  { codigo: 'W01', ruta: 'alarmas', titulo: 'Mis alarmas', funcionalidad: 'F-W01 · F-W02 · F-W06', conBarraLateral: true },
  { codigo: 'W03', ruta: 'eventos/:id', titulo: 'Detalle del evento', funcionalidad: 'F-W03', conBarraLateral: true },
  { codigo: 'W04', ruta: 'reportes', titulo: 'Reportes', funcionalidad: 'F-W04', conBarraLateral: true },
  { codigo: 'W05', ruta: 'qr', titulo: 'Descargar QR', funcionalidad: 'F-W05', conBarraLateral: true },
  { codigo: 'W06', ruta: 'perfil', titulo: 'Ajustes de perfil', funcionalidad: 'F-W07 · F-W08', conBarraLateral: true },
] as const;

export function pantallaPorCodigo(codigo: CodigoPantalla): PantallaWeb {
  const p = PANTALLAS.find(x => x.codigo === codigo);
  if (!p) throw new Error(`Pantalla ${codigo} no declarada`);
  return p;
}
```

- [ ] **Step 4: Marcador y componentes de página**

`pantallas/pantalla-marcador/pantalla-marcador.component.ts`:
```ts
import { Component, input } from '@angular/core';
import { PantallaWeb } from '../../navegacion/pantallas';

/** Marcador de la Fase 0: ocupa el lugar de una página hasta que su plan la construya. */
@Component({
  selector: 'aq-pantalla-marcador',
  template: `
    <section class="marcador" [attr.data-codigo]="pantalla().codigo">
      <h1>{{ pantalla().codigo }}</h1>
      <p>{{ pantalla().titulo }}</p>
      <small>Pantalla pendiente · {{ pantalla().funcionalidad }}</small>
    </section>
  `,
  styles: `
    .marcador { display: grid; gap: var(--space-web-bloques); padding: var(--space-web-contenido-y) var(--space-web-contenido-x); }
    h1 { font: var(--text-h1-web); font-family: var(--font-titulares); margin: 0; }
    p { margin: 0; color: var(--color-texto-secundario); }
  `,
})
export class PantallaMarcadorComponent {
  pantalla = input.required<PantallaWeb>();
}
```
Si `--text-h1-web` en `tokens.css` no es una abreviatura `font` válida sino solo un tamaño, usar `font-size: var(--text-h1-web)`.

Un componente por página, todos con la misma forma; ejemplo `pantallas/w06-perfil/w06-perfil.component.ts`:
```ts
import { Component } from '@angular/core';
import { PantallaMarcadorComponent } from '../pantalla-marcador/pantalla-marcador.component';
import { pantallaPorCodigo } from '../../navegacion/pantallas';

@Component({
  selector: 'aq-w06-perfil',
  imports: [PantallaMarcadorComponent],
  template: `<aq-pantalla-marcador [pantalla]="pantalla" />`,
})
export class W06PerfilComponent {
  readonly pantalla = pantallaPorCodigo('W06');
}
```
Repetir para: `w00-login` (`W00LoginComponent`, `'W00'`), `w01-mis-alarmas` (`W01MisAlarmasComponent`, `'W01'`), `w03-detalle-evento` (`W03DetalleEventoComponent`, `'W03'`), `w04-reportes` (`W04ReportesComponent`, `'W04'`), `w05-descargar-qr` (`W05DescargarQrComponent`, `'W05'`), cambiando selector, clase y código.

- [ ] **Step 5: Layout con barra lateral marcadora**

`layout/aq-layout-app/aq-layout-app.component.ts`:
```ts
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { PANTALLAS } from '../../navegacion/pantallas';

/** Layout de las páginas autenticadas: barra lateral (marcadora en la Fase 0; el componente aq-barra-lateral real llega en el Plan 3) + contenido. */
@Component({
  selector: 'aq-layout-app',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="layout">
      <nav class="aq-barra-lateral" aria-label="Menú principal">
        <span class="marca">Alarmas QR</span>
        @for (p of paginas; track p.codigo) {
          <a [routerLink]="'/' + p.ruta" routerLinkActive="activa" [attr.data-codigo]="p.codigo">{{ p.titulo }}</a>
        }
      </nav>
      <main class="contenido"><router-outlet /></main>
    </div>
  `,
  styles: `
    .layout { display: grid; grid-template-columns: var(--size-barra-lateral-web) 1fr; min-height: 100vh; }
    .aq-barra-lateral { display: flex; flex-direction: column; gap: var(--space-web-bloques); padding: var(--space-web-contenido-y) var(--space-web-contenido-x); background: var(--color-gris-niebla); }
    .marca { font-family: var(--font-titulares); font-weight: 700; }
    a { color: var(--color-texto); text-decoration: none; padding: 0 var(--space-boton-web); height: var(--size-item-barra-lateral); display: flex; align-items: center; border-radius: var(--radius-pildora); }
    a.activa { background: var(--color-tinta); color: var(--color-blanco); }
  `,
})
export class AqLayoutAppComponent {
  readonly paginas = PANTALLAS.filter(p => p.conBarraLateral && !p.ruta.includes(':'));
}
```

- [ ] **Step 6: `app.routes.ts`, `app.ts`, `app.html`**

`app.routes.ts`:
```ts
import { Routes } from '@angular/router';
import { PANTALLAS, PantallaWeb } from './navegacion/pantallas';
import { AqLayoutAppComponent } from './layout/aq-layout-app/aq-layout-app.component';
import { W00LoginComponent } from './pantallas/w00-login/w00-login.component';
import { W01MisAlarmasComponent } from './pantallas/w01-mis-alarmas/w01-mis-alarmas.component';
import { W03DetalleEventoComponent } from './pantallas/w03-detalle-evento/w03-detalle-evento.component';
import { W04ReportesComponent } from './pantallas/w04-reportes/w04-reportes.component';
import { W05DescargarQrComponent } from './pantallas/w05-descargar-qr/w05-descargar-qr.component';
import { W06PerfilComponent } from './pantallas/w06-perfil/w06-perfil.component';

const componentes = {
  W00: W00LoginComponent, W01: W01MisAlarmasComponent, W03: W03DetalleEventoComponent,
  W04: W04ReportesComponent, W05: W05DescargarQrComponent, W06: W06PerfilComponent,
} as const;

const ruta = (p: PantallaWeb) => ({ path: p.ruta, component: componentes[p.codigo], title: `${p.titulo} · Alarmas QR`, data: { codigo: p.codigo } });

/** Rutas de docs/TRAZABILIDAD.md §2, generadas desde PANTALLAS. Los estados (?estado=, ?dialogo=) se leen con query params. */
export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  ...PANTALLAS.filter(p => !p.conBarraLateral).map(ruta),
  { path: '', component: AqLayoutAppComponent, children: PANTALLAS.filter(p => p.conBarraLateral).map(ruta) },
  { path: '**', redirectTo: 'login' },
];
```
`app.ts`: dejar solo `RouterOutlet` en `imports` y `app.html` con `<router-outlet />` (borrar la plantilla de bienvenida del CLI). Actualizar `app.spec.ts` para que solo compruebe que el componente se crea.

- [ ] **Step 7: Correr pruebas y build**

```bash
cd apps/web && npx ng test --watch=false && npm run build -- --configuration production
```
Expected: PASS (3 + 1) y build OK. Si `RouterTestingHarness` no encuentra `routeNativeElement`, usar `document.body.innerHTML`.

- [ ] **Step 8: Commit**

```bash
git add apps/web/src
git commit -m "Fase 0: rutas web generadas desde PANTALLAS, layout con barra lateral y marcadores

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```

---

### Task 7: `DatosService` y `SesionService`

**Files:**
- Create: `apps/web/src/app/datos/modelos.ts`
- Create: `apps/web/src/app/datos/datos.service.ts`
- Create: `apps/web/src/app/datos/sesion.service.ts`
- Modify: `apps/web/src/app/app.config.ts` (`provideHttpClient()`)
- Test: `apps/web/src/app/datos/datos.service.spec.ts`, `sesion.service.spec.ts`

**Interfaces:**
- Produces: `DatosService { dataset: HttpResourceRef<Dataset|undefined>; usuario: Signal<Usuario|undefined>; mensajes: Signal<Mensajes|undefined>; web: Signal<DatosWeb|undefined> }`; `SesionService { iniciada: Signal<boolean>; iniciar(): void; cerrar(): void }`.

- [ ] **Step 1: Pruebas que fallan**

`datos.service.spec.ts`:
```ts
import { TestBed } from '@angular/core/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { DatosService } from './datos.service';
import dataset from '../../../public/dataset.json';

describe('DatosService', () => {
  it('carga dataset.json y expone usuario y mensajes como señales', async () => {
    TestBed.configureTestingModule({ providers: [provideZonelessChangeDetection(), provideHttpClient(), provideHttpClientTesting()] });
    const servicio = TestBed.inject(DatosService);
    const http = TestBed.inject(HttpTestingController);
    await TestBed.inject(ApplicationRef).whenStable();
    http.expectOne('dataset.json').flush(dataset);
    await TestBed.inject(ApplicationRef).whenStable();
    expect(servicio.usuario()?.nombre).toBe('Andrés Rojas');
    expect(servicio.mensajes()?.cuentaEliminada).toBe('Cuenta eliminada exitosamente');
    expect(servicio.web()?.indicadores.escaneosTotales.valor).toBe(128);
    http.verify();
  });
});
```
Añadir `import { ApplicationRef } from '@angular/core';` y, en `tsconfig.json` → `compilerOptions`, `"resolveJsonModule": true` para importar el JSON de referencia.

`sesion.service.spec.ts`:
```ts
import { TestBed } from '@angular/core/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { SesionService } from './sesion.service';

describe('SesionService', () => {
  it('empieza sin sesión y la conmuta', () => {
    TestBed.configureTestingModule({ providers: [provideZonelessChangeDetection()] });
    const s = TestBed.inject(SesionService);
    expect(s.iniciada()).toBe(false);
    s.iniciar();
    expect(s.iniciada()).toBe(true);
    s.cerrar();
    expect(s.iniciada()).toBe(false);
  });
});
```

- [ ] **Step 2: Verificar que fallan**

```bash
cd apps/web && npx ng test --watch=false
```
Expected: FAIL («Cannot find module './datos.service'»).

- [ ] **Step 3: `modelos.ts`** (espejo de `dataset.json` v1.2; solo lo que usan las páginas)

```ts
export interface Dataset {
  meta: { version: string; hoy: string; zonaHoraria: string };
  usuario: Usuario;
  alarmas: Alarma[];
  mensajes: Mensajes;
  web: DatosWeb;
}
export interface Usuario {
  id: string; nombre: string; aliasPublico: string; correo: string; iniciales: string;
  privacidad: { apariciónEnQuienesEscanearon: 'nombre-completo' | 'solo-iniciales' | 'alias'; mostrarEstadoDeMiAlarma: boolean; contarMiYaVoyEnMetricas: boolean };
}
export interface Alarma {
  id: string; titulo: string; eventoInicio: string; suena: string; lugar: string | null;
  origen: string; estado: string; anticipacionMin: number; trayectoMin: number; chips: string[];
}
export interface Mensajes {
  perfilActualizado: string; descargaCompletada: string; cuentaEliminada: string; correoRecuperacionEnviado: string;
  confirmarCerrarSesionTitulo: string; confirmarCerrarSesionCuerpoWeb: string; confirmarCerrarSesionSeguro: string; confirmarCerrarSesionAccion: string;
  sinResultadosFiltros: string; sinBorradores: string;
}
export interface Indicador { valor: number; detalle: string }
export interface EventoWeb {
  id: string; titulo: string; fechaHora: string; origen: string; escaneos: number; alarmasActivas: number; estado: string; lugar: string;
}
export interface DatosWeb {
  indicadores: { eventosActivos: Indicador; escaneosTotales: Indicador; alarmasActivas: Indicador; confirmaronYaVoy: Indicador };
  eventos: EventoWeb[];
  eliminarCuenta: { eventosPublicados: number; alarmasDeAsistentes: number; diasParaBorrado: number; palabraDeConfirmacion: string; consejo: string };
}
```

- [ ] **Step 4: Servicios**

`datos.service.ts`:
```ts
import { Injectable, computed } from '@angular/core';
import { httpResource } from '@angular/common/http';
import { Dataset } from './modelos';

/** Único origen de datos simulados de la web: public/dataset.json (idéntico al del móvil). */
@Injectable({ providedIn: 'root' })
export class DatosService {
  readonly dataset = httpResource<Dataset>(() => 'dataset.json');
  readonly usuario = computed(() => this.dataset.value()?.usuario);
  readonly mensajes = computed(() => this.dataset.value()?.mensajes);
  readonly web = computed(() => this.dataset.value()?.web);
  readonly alarmas = computed(() => this.dataset.value()?.alarmas ?? []);
}
```
`sesion.service.ts`:
```ts
import { Injectable, signal } from '@angular/core';

/** Sesión simulada (sin autenticación real): W00 la inicia, el diálogo «¿Cerrar sesión?» la cierra. */
@Injectable({ providedIn: 'root' })
export class SesionService {
  private readonly _iniciada = signal(false);
  readonly iniciada = this._iniciada.asReadonly();
  iniciar(): void { this._iniciada.set(true); }
  cerrar(): void { this._iniciada.set(false); }
}
```
`app.config.ts`: añadir `provideHttpClient()` de `@angular/common/http` a `providers`.

- [ ] **Step 5: Correr pruebas**

```bash
cd apps/web && npx ng test --watch=false
```
Expected: PASS. Si `httpResource` no dispara la petición antes de `expectOne`, envolver la lectura en `TestBed.tick()` antes de `expectOne`.

- [ ] **Step 6: Commit**

```bash
git add apps/web
git commit -m "Fase 0: DatosService con httpResource y SesionService simulada

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```

---

### Task 8: CI para las versiones nuevas

**Files:**
- Modify: `.github/workflows/ci.yml`
- Modify: `.github/workflows/apk.yml` (solo si usa `node`/`java` distintos)

- [ ] **Step 1: Ajustar el job web**

En `ci.yml`, job `web`: `node-version: '22.23.2'`; paso «Pruebas»: `run: npx ng test --watch=false` (Vitest no acepta `--browsers`). Mantener `npm ci` (ya hay `package-lock.json`).

- [ ] **Step 2: Ajustar el job móvil**

Job `movil`: entre `setup-java` y `setup-gradle` añadir
```yaml
      - uses: android-actions/setup-android@v3
        with:
          packages: 'platform-tools platforms;android-36 build-tools;36.0.0'
```
y dejar `./gradlew lintDebug testDebugUnitTest --no-daemon` y `assembleDebug`. Revisar `apk.yml`: si tiene `java-version: '17'` y `assembleRelease`, añadir el mismo paso de `setup-android`.

- [ ] **Step 3: Validar sintaxis y commit**

```bash
python3 -c "import yaml,sys;[yaml.safe_load(open(f)) for f in ['.github/workflows/ci.yml','.github/workflows/apk.yml']];print('YAML OK')" \
 && git add .github && git commit -m "Fase 0: CI con Node 22.23, SDK 36 y Vitest

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```
Expected: `YAML OK`. Si `python3 -c "import yaml"` falla por falta del módulo, usar `npx -y yaml-lint .github/workflows/*.yml`.

---

### Task 9: README con versiones, CLAUDE.md y plan actualizados

**Files:**
- Modify: `README.md`
- Modify: `CLAUDE.md`
- Modify: `docs/PLAN_MAQUETACION.md`

- [ ] **Step 1: README · sección «Frameworks y versiones»**

Insertar tras «## Cómo correr» una tabla generada leyendo `libs.versions.toml` y `package.json` (copiar los valores exactos):

```markdown
## Frameworks y versiones

| Ámbito | Herramienta | Versión | Dónde se fija |
|---|---|---|---|
| Móvil | JDK | 17 | `apps/movil/app/build.gradle.kts` |
| Móvil | Gradle | 8.14.3 | `apps/movil/gradle/wrapper/gradle-wrapper.properties` |
| Móvil | Android Gradle Plugin | 8.13.2 | `apps/movil/gradle/libs.versions.toml` |
| Móvil | Kotlin (+ Compose y serialization) | 2.2.21 | ídem |
| Móvil | Jetpack Compose BOM | 2026.06.00 | ídem |
| Móvil | Navigation 3 | 1.1.7 | ídem |
| Móvil | Lifecycle / ViewModel | 2.10.0 | ídem |
| Móvil | kotlinx-serialization-json | 1.11.0 | ídem |
| Móvil | CameraX · ML Kit Barcode · ZXing | 1.6.2 · 17.3.0 · 3.5.4 | ídem |
| Móvil | compileSdk / targetSdk / minSdk | 36 / 36 / 26 | `app/build.gradle.kts` |
| Móvil | Robolectric (pruebas JVM de Compose) | 4.16 | `libs.versions.toml` |
| Web | Node / npm | 22.23.2 / 10 | `.nvmrc`, CI |
| Web | Angular (core, CLI, CDK) | 22.1.x (ver `package.json`) | `apps/web/package.json` |
| Web | TypeScript | ver `package.json` | ídem |
| Web | Vitest (vía `@angular/build`) | ver `package.json` | ídem |
```
Crear `apps/web/.nvmrc` con `22.23.2`. Actualizar «Cómo correr»: requisitos «JDK 17, Android SDK 36, Node 22.23.2 (nvm use)», y el comando de pruebas web `npx ng test --watch=false`. En «Pruebas» sustituir «Karma/Jasmine o Jest» por «Vitest con TestBed».

- [ ] **Step 2: README · «Cómo continuar como Persona B»**

Añadir sección:
```markdown
## Cómo continuar (Persona B)

- Móvil: cada pantalla es una clave en `navegacion/Pantalla.kt`. Para construir M06: crear `ui/pantallas/m06/M06EditarAlarmaScreen.kt` (+ `ViewModel`) y registrarla en `MainActivity` con `NavegacionApp(pila, repositorio) { entry<Pantalla.M06> { M06EditarAlarmaScreen(it.id, …) } }`; el marcador desaparece solo. Datos: `RepositorioDataset` (`alarmas`, `alarma(id)`, `agregar`, `eliminar`, `deshacer`). Pruebas: `createComposeRule` + Robolectric, navegar con `pila.irA(Pantalla.M06("a-tutor"))` y afirmar `testTag("pantalla-M06")`.
- Web: cada página está en `src/app/pantallas/<código>/`; sustituir el `<aq-pantalla-marcador>` por la página real. Rutas y barra lateral salen de `navegacion/pantallas.ts`; datos de `DatosService` (señales `usuario`, `web`, `alarmas`, `mensajes`). Componentes compartidos en `src/app/componentes/` (los de tablero: indicador, tabla, gráfica, píldora, afiche, paginador, quedan por construir en L09).
- Convenciones: rama por pantalla, commit «M06: …», PR revisado por el otro integrante; tokens siempre, nunca valores a mano.
```

- [ ] **Step 3: `CLAUDE.md` y `docs/PLAN_MAQUETACION.md`**

En `CLAUDE.md` § Stack: móvil «Navigation 3 (`NavDisplay`, claves `Pantalla : NavKey`, hoja inferior como `SceneStrategy`)» en lugar de «Navigation Compose»; web «Angular 22 … pruebas con Vitest + `TestBed`»; comando `npx ng test --watch=false`. En § Navegación añadir: «En móvil las hojas M02h y M04 son claves del back stack dibujadas por `HojaInferiorSceneStrategy`; los diálogos siguen siendo estado». En `docs/PLAN_MAQUETACION.md` §1 fila «App móvil» y §6 Fase 0: anotar «2026-09-20: Navigation 3 en lugar de Navigation Compose; versiones fijadas en README». Anotar en el commit que el cambio debe replicarse en el repo de UX (`docs/` es copia).

- [ ] **Step 4: Verificación final de ambas apps y commit**

```bash
(cd apps/movil && ./gradlew testDebugUnitTest lintDebug assembleDebug --no-daemon -q) && (source ~/.nvm/nvm.sh && nvm use 22.23.2 >/dev/null && cd apps/web && npx ng test --watch=false && npm run build -- --configuration production) && echo TODO_VERDE
git add README.md CLAUDE.md docs/PLAN_MAQUETACION.md apps/web/.nvmrc
git commit -m "Fase 0: README con versiones y guía para la Persona B; CLAUDE.md y plan con Navigation 3

Co-Authored-By: Claude Fable 5.1 <noreply@anthropic.com>"
```
Expected: `TODO_VERDE`.

---

## Verificación de cobertura de la spec (Plan 1)

| Spec | Tarea |
|---|---|
| §1 versiones | 1, 5, 9 |
| §2.1 `datos`, `navegacion` | 2, 3, 4 |
| §2.2 back stack, hoja, andamio | 4 |
| §3.1–3.2 estructura y rutas web, `DatosService`, `SesionService`, layout | 5, 6, 7 |
| §6 CI | 8 |
| §7 README, CLAUDE.md, plan | 9 |
| §2.3 pantallas móviles, §4 componentes, §5 pixel-perfect, `alarma`, `qr` | **Plan 2** (móvil Persona A) |
| §3 páginas web W00/W06/modal, componentes L09 | **Plan 3** (web Persona A) |
