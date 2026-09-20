# Diseño · Maquetación de Alarmas QR, Fase 0 y pantallas de la Persona A

Fecha: 2026-09-20 · Autor: Alejandro Ortiz (Persona A) con Claude · Estado: aprobado en chat el 2026-09-20.
Insumos: `docs/PLAN_MAQUETACION.md`, `docs/TRAZABILIDAD.md`, `docs/NAVEGACION.md` §6/§6b, `docs/FUNCIONALIDADES.md`, `docs/DESIGN_SYSTEM.md`, `packages/tokens/*`, archivo Figma `4nHD4ygcnP33UH0gAhaii5`.

## 0 · Alcance y lineamientos del curso

Se cierra la **Fase 0** (cimientos compartidos) y se construyen las pantallas de la **Persona A**:

- Móvil: M01, M00a, M00b, M02v, M02, M02h, M03, M03b, M04 (+ M04d), M05, M12, M13.
- Web: W00 (inicio, error de credenciales, recuperar contraseña, correo enviado, cuenta eliminada), W06 (perfil, actualizado), modal «Eliminar cuenta» y el diálogo transversal «¿Cerrar sesión?».

Lineamientos de los tutores que el código debe cumplir:

1. `README.md` lista frameworks, versiones exactas e instrucciones.
2. Los componentes usan el mismo kit que Figma, personalizado: el DS «Energía puntual» (48 componentes propios sobre anatomía Material 3). En Compose es Material 3 re-tematizado con `Tokens.kt`; en Angular son componentes propios sobre Angular CDK que replican la lámina L09 (decisión A, confirmada el 2026-09-20; no se usa Angular Material).
3. Sin trampas: ninguna imagen sustituye a un componente; nada se «pinta» con una imagen de fondo. Iconos y textura QR son vectores (`ImageVector`/SVG/Canvas); el QR se genera con ZXing.
4. Pixel-perfect contra Figma (390×844 móvil, 1280×820 web) con flexibilidad de ancho: alturas, radios, rellenos y tipografías fijos por tokens; anchos y márgenes laterales se estiran en pantallas más anchas, salvo los anchos fijos del mockup (diálogo 342, formulario web 600, barra lateral 208/64, diálogo web 420). No es responsive estricto.

## 1 · Versiones base (fijadas el 2026-09-20)

| Ámbito | Herramienta | Versión |
|---|---|---|
| Móvil | JDK | 17 |
| Móvil | Android Gradle Plugin | 8.13.x (estable; no 9.x alpha) |
| Móvil | Kotlin + plugin Compose + kotlinx.serialization | 2.2.x |
| Móvil | Compose BOM | 2026.09.00 |
| Móvil | Navigation 3 (`navigation3-runtime`, `navigation3-ui`) | 1.1.7 estable |
| Móvil | `lifecycle-viewmodel-navigation3` / lifecycle | 2.11.0 |
| Móvil | compileSdk / targetSdk / minSdk | 36 / 36 / 26 |
| Móvil | CameraX, ML Kit barcode, ZXing core | últimas estables al fijar |
| Web | Node / npm | 22 / 10 |
| Web | Angular (core, cli, cdk) | 22.1.x |
| Web | Pruebas | Karma + Jasmine (las del CLI) |

Las versiones exactas se leen de `apps/movil/gradle/libs.versions.toml` y `apps/web/package.json`; el README las copia.

## 2 · Móvil (`apps/movil`)

### 2.1 Paquetes (`co.edu.uniandes.alarmasqr`)

- `ui/theme`: `Tokens.kt` (copiado de `packages/tokens`), `Theme.kt`.
- `ui/componentes`: composables L03–L08 (ver §4).
- `ui/pantallas/<código>`: `M06EditarAlarmaScreen.kt` + `M06EditarAlarmaViewModel.kt` por pantalla. Estado de UI inmutable expuesto como `StateFlow`.
- `navegacion`: `Pantalla.kt` (claves), `NavegacionApp.kt` (`NavDisplay`), `HojaInferiorSceneStrategy.kt`, `AndamioPrincipal.kt` (barra inferior, FAB).
- `datos`: modelos `Alarma`, `EventoQR`, `Usuario`…; `RepositorioDataset` lee `assets/dataset.json` con kotlinx.serialization una vez y expone `StateFlow<List<Alarma>>` mutable en memoria (agregar, eliminar, deshacer). Es el único origen de datos; `dataset.json` es idéntico al de la web.
- `alarma`: `ProgramadorAlarmas` (`AlarmManager.setExactAndAllowWhileIdle`, permiso `SCHEDULE_EXACT_ALARM` en Android 12+), `ReceptorAlarma` (`BroadcastReceiver`) que publica una notificación de pantalla completa cuyo intent abre la app en `alarma/{id}/sonando`; canal «Alarmas» con importancia alta. El disparo se programa al llegar a M04; M10 (Persona B) recibe la clave.
- `qr`: `AnalizadorQR` con CameraX `ImageAnalysis` + ML Kit `BarcodeScanning`; resultado `EventoDetectado(id)` o `QRInvalido(contenido)`.

### 2.2 Navegación (Navigation 3)

- `sealed interface Pantalla : NavKey` con `@Serializable data object`/`data class`; cada clave conserva `codigo`, `ruta` (la de TRAZABILIDAD, `{id}` sustituido) y `funcionalidad`. Un `Pantalla.porRuta(ruta)` resuelve deep links (notificación de la alarma, intent SEND de pantallazo → M03b).
- Un solo `NavBackStack<Pantalla>` guardado con `rememberNavBackStack`. Reglas: M01 es raíz; entrar/registrarse reemplaza la pila por `[M02v]` o `[M02]`; las pestañas inferiores (M02, M02b, M11) reemplazan la cima sin apilar.
- `HojaInferiorSceneStrategy`: si la cima es M02h o M04, la escena dibuja la entrada anterior atenuada con velo Tinta 55 % y la cima como hoja inferior (radio 24 arriba, asa); tocar el velo = `removeLastOrNull()`.
- Diálogos M04d/M06d/M11d: `estadoDialogo` en el ViewModel de la pantalla padre; `DialogoConfirmacion` sobre velo Tinta 55 %; tocar el velo = acción segura.
- `AndamioPrincipal` decide barra inferior y FAB según la cima (`conNavegacionInferior`, `conFab`); en M03 y M10 la superficie es Tinta con edge-to-edge (skill `edge-to-edge`).
- Controles ⏩ (NAVEGACION §6): FAB/«Escanear QR del evento» → M12 solo la primera vez (`permisoCamaraPedido` en `RepositorioDataset`); «Abrir ajustes» en M12 → M03; tocar el visor de M03 → M04 con `e-entrega`; tocar «vibra al detectar» → M13; en M03b «Continuar» → M04. La cámara real navega igual cuando ML Kit detecta un código.

### 2.3 Pantallas de la Persona A (móvil)

| Código | Ruta | Estado y datos | Notas de construcción |
|---|---|---|---|
| M01 | `bienvenida` | filas de calendario (set 48) con casilla; banda de textura 120 | «Comenzar» → M00a; «Ahora no» → M02v |
| M00a | `registro` | campos correo/contraseña, fila Google/Outlook, consentimiento | «Crear cuenta»/«invitado» → M02v; «Ya tengo cuenta» → M00b; contenedores no fijos (regla v1.10 §5) |
| M00b | `entrar` | campos + enlace recuperar | «Entrar» → M02 |
| M02v | `inicio` vacío | `EstadoVacio` con tres acciones | «Escanear» → M12 ⏩ |
| M02 | `inicio` | lista agrupada por día desde el dataset, chips de origen, FAB extendido | toque FAB → M12/M03; mantener 500 ms → M02h; tarjeta → M06 (B) |
| M02h | `inicio/agregar` | hoja con tres acciones | escena hoja sobre M02 |
| M12 | `permiso-camara` | visor apagado con icono `escanear` 48 | «Abrir ajustes» abre ajustes del SO y al volver → M03 |
| M03 | `escanear` | superficie Tinta, `PreviewView` CameraX, marco de enfoque, `ChipControl` «Linterna · auto» 32 | detecta → M04; inválido → M13; sin permiso muestra visor apagado y el toque simulado |
| M13 | `escanear/invalido` | banda 120, diagnóstico del dataset (`qrInvalido`) | «Volver a escanear» → M03; «Abrir bajo mi responsabilidad» → navegador |
| M03b | `pantallazo` | datos `pantallazoRecibido`; llega también por intent SEND | «Continuar» → M04 |
| M04 | `alarma/{id}/creada` | hoja sobre M02: chip «Datos leídos del QR», tarjeta evento, bloque «Sonará» (AmarilloSuave), «Listo», enlace destructivo | programa la alarma real; M04d con `DialogoConfirmacion(Eliminar)` |
| M05 | `inicio` + snackbar | alarma nueva resaltada con chip «Nueva» (Tinta), snackbar 5 s con «Deshacer» | deshacer revierte en el repositorio |

Las horas se muestran en 12 h con Spline Sans Mono y dígitos tabulares; el sufijo am/pm en `HoraAmPm`.

## 3 · Web (`apps/web`)

### 3.1 Estructura

- `src/app/datos`: `DatosService` (`httpResource` sobre `dataset.json`, señales derivadas), `SesionService` (usuario simulado, `cerrarSesion()`), modelos TypeScript generados del JSON.
- `src/app/componentes/<nombre>/`: componentes L09 con selector `aq-*`, estilos por componente que solo usan variables de `tokens.css`.
- `src/app/pantallas/<código-nombre>/`: `w00-login/`, `w00-recuperar-contrasena/`, `w06-perfil/`, `w06-modal-eliminar-cuenta/`; carpetas de W01, W03, W04, W05 creadas con un componente marcador («Pantalla pendiente · Persona B») para que las rutas y la barra lateral ya funcionen.
- `src/app/layout/aq-layout-app`: barra lateral 208 (colapsable a 64, preferencia en señal) + área de contenido; W00 usa un layout sin barra.
- `styles.css` importa `tokens.css` y declara `@font-face` de las tres fuentes empaquetadas en `public/fonts`.

### 3.2 Rutas (`app.routes.ts`, generadas desde `PANTALLAS`)

`/login` (W00; `?estado=correo-enviado|eliminada` muestra snackbar 3 s), `/login/recuperar`, `/alarmas` (W01), `/eventos/:id` (W03), `/reportes` (W04), `/qr` (W05), `/perfil` (W06) con hija `/perfil/eliminar` (modal en `CdkOverlay`, velo 45 %), y el query global `?dialogo=cerrar-sesion` que abre `aq-dialogo-confirmacion` (420, velo 55 %) sobre la página actual; «Cerrar sesión» → `/login`. Cada ruta lleva `data: { codigo: 'W06' }` para navegar por código en las pruebas. El estado de error de W00 se dispara con el foco del campo contraseña (⏩), como en el prototipo.

## 4 · Componentes que construye la Persona A

Móvil (L03–L08): `BotonPrimario`, `BotonSecundario`, `BotonDestructivo`, `BotonEnlace`, `CampoTexto` (etiqueta interna, 48, r12), `ChipEstado` (20), `ChipControl` (32, set 49), `Casilla`, `FilaOpcionCalendario` (set 48, iconos google/outlook/teléfono como `ImageVector`), `TarjetaAlarma`, `TarjetaEvento`, `SelloVerificado`, `FabEscanear` (extendido, un solo amarillo), `BarraSuperior` (espaciador flexible, relleno 16), `NavegacionInferior` (64, texto 11), `SnackbarDeshacer`, `HojaInferior`, `DialogoConfirmacion` (comp. 47: 342, r20, relleno 24, botones 52 apilados a 10), `BandaTextura` (Canvas, 120), `CodigoQR` (ZXing → `ImageBitmap` generado), `EstadoVacio`.

Web (L09): `aq-boton` (primario/secundario/destructivo, 44, relleno 24, píldora), `aq-campo` (con estado de error Coral Texto), `aq-enlace`, `aq-tarjeta-acceso`, `aq-tarjeta` (r14, borde Gris Borde), `aq-snackbar` (3 s), `aq-modal` + `aq-cabecera-modal` (fila miga + ✕ como un solo control), `aq-dialogo-confirmacion`, `aq-barra-lateral` + `aq-item-barra-lateral` (icono 20, píldora activa Tinta, colapsar 36×36), `aq-switch`, `aq-selector-segmentado`, `aq-icono` (SVG en línea del DS).

Los componentes de tablero (indicador, píldora de filtro, tabla, gráfica, afiche, paginador) los construye la Persona B.

## 5 · Método pixel-perfect y verificación

1. Por cada marco se obtiene el nodo de Figma (`get_design_context` + captura) y se transcriben medidas a tokens existentes; si una medida no existe como token, primero se agrega a `design-tokens.json` y a sus derivados (nunca un número suelto en una pantalla).
2. Verificación móvil: pruebas de captura de `@Preview` con el plugin oficial `com.android.compose.screenshot` (JVM, sin emulador), comparadas visualmente con la exportación del marco de Figma a 390×844. Verificación web: capturas de Chrome a 1280×820 por ruta y estado. Las parejas Figma/implementación se guardan en `docs/verificacion/<código>.png` como evidencia.
3. Lista de comprobación por pantalla (de PLAN §7 y CLAUDE.md): un solo amarillo; 52/44; radios 14/20/12; tonos AA en texto ≤ 15; horas 12 h; estados con color + forma; diálogo de confirmación en eliminar/salir.

## 6 · Pruebas y CI

- Móvil: JUnit para `RepositorioDataset` (agregar/eliminar/deshacer), cálculo de «Sonará» y reglas del back stack; `createComposeRule` para T1 (M02→M12→M03→M04→M05), T6 (M13) y T7 (M01→M00a/M00b) afirmando la clave visible por código. Comandos: `./gradlew testDebugUnitTest lintDebug assembleDebug`.
- Web: `TestBed` + `RouterTestingHarness` para T5 tramo W00 (login → `/alarmas`) y T8 (`/perfil` → `/perfil/eliminar` → `/login?estado=eliminada`). Comandos: `npx ng test --watch=false --browsers=ChromeHeadless`, `npx ng build --configuration production`.
- `ci.yml`: Node 22, JDK 17, SDK 36 (lo instala `setup-android`/AGP); `apk.yml` sin cambios de fondo.

## 7 · Entrega y trabajo con la Persona B

- Ramas: `feature/fase0-cimientos` (esta spec, versiones, Nav 3, dataset, andamio, layout, CI) y después una rama por pantalla (`feature/M03-escaner`, `feature/W00-login`). PR pequeño; nada se fusiona a `main` sin revisión.
- `README.md`: tabla de versiones, cómo correr, cómo instalar el APK, sección «Cómo continuar como Persona B» (pantallas marcadoras, componentes disponibles, cómo agregar una clave de navegación). `CLAUDE.md` y `docs/PLAN_MAQUETACION.md` se actualizan con Navigation 3 y las versiones; el cambio de plan se replica en el repo de UX.
- Skills del proyecto (`.claude/skills`, `skills-lock.json`) se versionan para que ambos las tengan.

## 8 · Fuera de alcance

Pantallas de la Persona B (M02b, M06–M11, W01, W03, W04, W05) más allá de sus marcadores y rutas; backend, autenticación y push reales; despliegue web (Fase 4).
