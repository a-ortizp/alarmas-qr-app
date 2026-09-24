# Alarmas QR · aplicaciones

> `README.md` del repositorio de código. Stack decidido el 2026-09-16: **Kotlin + Jetpack Compose** (móvil) y **Angular + TypeScript** (web); ver `docs/PLAN_MAQUETACION.md` §1.

Alarmas QR crea alarmas escaneando el código QR de un evento, sin digitar fecha, hora ni nombre. Este repositorio contiene la **maquetación** de sus dos aplicaciones: pantallas navegables con datos simulados, sin backend.

| Aplicación | Qué es | Pantallas | Entregable |
|---|---|---|---|
| `apps/movil` | App del asistente y organizador (Android) | 19 (M00a–M13 + M02v, M02h, M03b) + 3 diálogos de confirmación (M04d, M06d, M11d) | APK instalable |
| `apps/web` | Administración y consulta para organizadores | 7 páginas (W00, W01, W03, W04 Reportes, W05 Descargar QR, W06) con 24 estados, el modal «Eliminar cuenta» y el diálogo «¿Cerrar sesión?» (web v1.5) | Sitio desplegado |

Investigación, prototipos y diseño viven en el repositorio de UX: https://github.com/alejortizp/alarmas-qr-ux. Una copia curada está en `docs/`.

## Guía para el tutor · cómo validar la entrega

Esta guía va en orden: lo más rápido primero. Cada paso dice qué se debe ver, para poder marcarlo como correcto.
No hay backend ni cuentas reales: todos los datos salen de `dataset.json`, idéntico en las dos apps.

### Paso 0 · Requisitos según lo que se quiera probar

| Quiero… | Necesito |
|---|---|
| Instalar la app en un celular Android | Solo el celular (Android 8+). Nada más: el APK se descarga de la Release |
| Ver la app web | Node **22.23.2** (o 24/26) y npm |
| Abrir y compilar el proyecto móvil | JDK 17 y Android Studio con el SDK de Android 36. Gradle se descarga solo |

**Sobre Node:** Angular 22 exige Node 22.22.3 o superior y se niega a arrancar con Node 20. La versión exacta está
fijada en `apps/web/.nvmrc`. Si la máquina tiene otra, lo cómodo es un gestor de versiones que la cambie por
carpeta: **[fnm](https://github.com/Schniz/fnm)** (`winget install Schniz.fnm`) o
**[nvm-windows](https://github.com/coreybutler/nvm-windows)** en Windows, y **[nvm](https://github.com/nvm-sh/nvm)**
en Linux/macOS. Con cualquiera de ellos, dentro de `apps/web` basta `fnm use` o `nvm use`, que leen el `.nvmrc`.

### Paso 1 · Instalar el APK en un celular (5 minutos, sin herramientas)

1. Abrir la pestaña **Releases** de este repositorio y descargar el `alarmas-qr-vX.Y.Z.apk` de la última versión.
2. En el celular, permitir «instalar apps de origen desconocido» para el navegador o el gestor de archivos.
3. Abrir el archivo e instalar. Con el celular conectado por USB también sirve `adb install -r alarmas-qr-vX.Y.Z.apk`.
4. La app pide **permiso de cámara** (pantalla M12) y, en Android 13+, **permiso de notificaciones** la primera vez
   que se programa una alarma. Sin el de notificaciones la alarma no se ve ni se oye (ver el Paso 5).

Si todavía no hay ninguna Release, el APK se genera con el Paso 4 o etiquetando una versión (Paso 6).

### Paso 2 · Probar la aplicación web

```bash
cd apps/web
fnm use            # o nvm use · fija Node 22.23.2 desde .nvmrc
npm ci             # instala las dependencias exactas del package-lock.json
npx ng serve       # queda escuchando en http://localhost:4200
```

Abrir <http://localhost:4200> con la ventana **maximizada o en 1280×820 o más**: el diseño es pixel-perfect a esa
medida y más angosto se degrada a propósito (bajo 1200 la barra lateral se colapsa sola; bajo 1100 las columnas de
las páginas de dos columnas se apilan). Las credenciales son de mentira: cualquier texto entra.

| # | Dónde | Qué hacer | Qué se debe ver |
|---|---|---|---|
| 1 | `/login` (W00) | «Iniciar sesión» | El tablero **W01 · Mis Alarmas**, con cuatro indicadores y la gráfica «Escaneos por semana» |
| 2 | W01 | Pestañas «Próximos / Pasados / Borradores» y el filtro «Todos / Creados / Escaneados» | La tabla cambia; «Borradores» muestra el estado vacío |
| 3 | W01 | «Ver detalle ›» de un evento | **W03 · Detalle Evento**, con la tabla anónima «Quiénes escanearon» (Ley 1581) y su paginador |
| 4 | W03 | «Exportar reporte» | **W04 · Reportes**: rango, formato y la tarjeta lateral «Reportes generados» |
| 5 | Barra lateral | «Descargar QR» | **W05**: selección de eventos, formato PNG/PDF y la vista previa del afiche |
| 6 | W05 | «Descargar» | Vuelve a W01 con el aviso «Descarga completada exitosamente», que se va solo a los 3 s |
| 7 | Barra lateral | «Ajustes de Perfil» → «Eliminar mi cuenta» | El modal exige escribir **ELIMINAR**; «Conservar mi cuenta» es la acción segura |
| 8 | Barra lateral | «Cerrar Sesión» | Un diálogo de confirmación: «Cancelar» es el primario y el velo también cancela |

El flujo T5 de `docs/TRAZABILIDAD.md` es exactamente el recorrido 1 → 6. Para detener el servidor: `Ctrl+C`.

### Paso 3 · Probar la aplicación móvil en Android Studio

1. Abrir Android Studio con **Open** y apuntar a la carpeta **`apps/movil`**, no a la raíz del repositorio (la raíz
   no es un proyecto Gradle y el IDE no encontraría nada que sincronizar).
2. Si pide actualizar el IDE o el Android Gradle Plugin, **actualizar el IDE**: el proyecto usa AGP 9.4.
3. Esperar el «Gradle sync». La primera vez descarga Gradle 9.6 y las dependencias, y tarda varios minutos.
4. Elegir un emulador (o un celular conectado con depuración USB) y darle **Run ▶**.

| # | Dónde | Qué hacer | Qué se debe ver |
|---|---|---|---|
| 1 | M01 Bienvenida | «Comenzar» → M00a → «Crear cuenta» | **M02v**, el estado vacío «Aún no tienes alarmas» |
| 2 | M02v / M02 | Tocar el FAB **«Escanear»** | **M12 · Permiso de cámara**; «Abrir ajustes» pide el permiso real y pasa a **M03** |
| 3 | M03 Escáner | Apuntar la cámara a un QR que contenga `alarmasqr://evento/e-entrega`. **Sin cámara** (emulador): tocar el visor simula esa misma lectura | **M04**, la hoja «¡Alarma programada!» con los datos del evento |
| 3b | M03 Escáner | Tocar «vibra al detectar el código» | **M13 · QR sin evento**, el desvío anti-quishing |
| 4 | M04 | «Listo» | **M05**: la lista con la alarma nueva resaltada y el snackbar «Alarma guardada · Deshacer» de 5 s |
| 5 | M05 | Tocar la alarma «Entrega de proyecto UX» | **M06 · Editar alarma**: anticipación, sonido, trayecto y notas |
| 6 | M06 | Fila «Confirmar antes de auto-ajustarse» (⏩ simula el push del organizador) | **M09 · Cambio en tu evento**, con el antes/ahora y el bloque «SI ACEPTAS, SONARÁ» |
| 7 | M09 | «Aceptar cambio» (⏩ salta a la hora del aviso) | **M10 · La alarma suena**, con la hora heroica y las dos acciones de 56 |
| 8 | M06 | «Eliminar alarma» | El diálogo **M06d**: «Conservar» es el primario amarillo y tocar el velo también conserva |
| 9 | Barra inferior | «Calendario» y «Ajustes» | **M02b** (mes navegable) y **M11 · Ajustes**, cuyo «Cerrar sesión» abre el diálogo M11d |
| 10 | M02 | Mantener presionado el FAB medio segundo → «Crear a mano» | **M07**: «Guardar y crear QR» con el título vacío **no crea nada** y marca el campo; con título pasa a **M08**, el QR del evento |

Los pasos 6 y 7 son los disparadores ⏩ que reemplazan al push del organizador y a la hora del aviso, que en la app
real llegarían del sistema (`docs/NAVEGACION.md` §6). El paso 6 solo está disponible en las alarmas que traen un
cambio del organizador en el dataset: «Entrega de proyecto UX», «Reunión semillero» y «Reunión con el tutor».

### Paso 4 · Compilar el APK a mano

Desde una terminal, sin abrir Android Studio (necesita JDK 17 y el SDK de Android 36):

```bash
cd apps/movil
./gradlew assembleDebug     # APK en app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug      # lo instala en el emulador o celular conectado
./gradlew assembleRelease   # APK de release en app/build/outputs/apk/release/app-release.apk
```

En PowerShell o CMD el comando es `.\gradlew.bat assembleDebug`. Si Gradle no encuentra el SDK, crear
`apps/movil/local.properties` con `sdk.dir=C\:\\Users\\<usuario>\\AppData\\Local\\Android\\Sdk`; Android Studio lo
genera solo al abrir la carpeta. El APK de release se firma con el **keystore de depuración**, suficiente para
instalarlo en un celular de prueba, pero no para publicar en Google Play.

### Paso 5 · Comprobar que la alarma suena con la app cerrada

Es la única capacidad real de la maquetación y necesita un celular o emulador de verdad:

1. Llegar a **M04** (Paso 3): ahí se programa una alarma real con `AlarmManager`.
2. Conceder el permiso de **notificaciones** que pide Android 13+. Sin él no hay nada que ver ni oír.
3. En Android 14+, activar además «Notificaciones de pantalla completa» en Ajustes → Apps → Alarmas QR →
   Notificaciones, para que aparezca sobre la pantalla de bloqueo.
4. **Cerrar la app** (o bloquear el celular) y esperar **un minuto**: como el dataset vive en agosto de 2026 la hora
   de la alarma ya pasó, así que se reprograma a *ahora + 1 minuto* para poder demostrarla.
5. Debe llegar la notificación a pantalla completa; al tocarla abre **M10 · La alarma suena**.

Las alarmas **no sobreviven a un reinicio** del celular: reprogramarlas tras el arranque quedó fuera del alcance.

### Paso 6 · Generar una Release con el APK (opcional)

El APK de las Releases lo compila GitHub Actions al empujar un tag de versión:

```bash
git tag v1.0.0
git push origin v1.0.0
```

`.github/workflows/apk.yml` compila `assembleRelease` y publica la Release con `alarmas-qr-v1.0.0.apk` adjunto. En
cada pull request corre además `.github/workflows/ci.yml`: lint y pruebas de las dos apps, build de producción de la
web y una comprobación de que `dataset.json` y `tokens.css` son idénticos en móvil, web y `packages/tokens`.

### Paso 7 · Correr las pruebas

```bash
cd apps/movil && ./gradlew testDebugUnitTest lintDebug    # Compose + Robolectric
cd apps/web   && npm ci && npx ng test --watch=false      # Vitest + TestBed
```

Móvil cubre los flujos T1–T4, T6 y T7 de `docs/TRAZABILIDAD.md`, la regla de scroll vertical y las capturas de
verificación contra Figma; web cubre T5 y T8. Las capturas y su comparación con los marcos de Figma están en
`docs/verificacion/` (una pareja de imágenes por pantalla, con las diferencias aceptadas explicadas una por una).

### Qué queda fuera del alcance

No hay persistencia (todo vive en memoria y se reinicia al cerrar la app), ni backend, ni autenticación real; las
alarmas no se reprograman tras reiniciar el celular. Las divergencias conocidas entre la documentación de UX y lo
construido están registradas, con su resolución, en `docs/NAVEGACION.md` §7.

## Cómo correr

Resumen para quien ya conoce el proyecto; el paso a paso explicado está en «Guía para el tutor», arriba.

Requisitos: JDK 17, Android Studio compatible con AGP 9.4 (si al sincronizar pide actualizar el IDE, actualizarlo) con Android SDK 36 y un emulador o dispositivo con Android 8+ (minSdk 26); Node 22.23.2 (`nvm use`, ver `apps/web/.nvmrc`) y npm 10.

Gradle corre siempre con JDK 17 (`gradle/gradle-daemon-jvm.properties`): si la máquina no lo tiene, lo descarga. Los finales de línea los fija `.gitattributes` (LF, salvo `.bat`/`.cmd`), así `gradlew` funciona igual en Windows, WSL y CI.

`apps/movil/local.properties` (git-ignorado) debe apuntar a un Android SDK con la plataforma 36 instalada — `sdk.dir=/ruta/al/Android/Sdk` — cuando `ANDROID_HOME` no está exportado en el entorno; Android Studio lo genera solo al abrir `apps/movil`.

```
# apps/movil · abrir la carpeta apps/movil en Android Studio (no la raíz del repo)
cd apps/movil
./gradlew assembleDebug            # compila el APK de depuración
./gradlew installDebug             # lo instala en el emulador o dispositivo conectado
./gradlew testDebugUnitTest lintDebug

# apps/web · antes de cualquier npm/npx: source ~/.nvm/nvm.sh && nvm use 22.23.2 (o `nvm use` con el .nvmrc)
cd apps/web
npm ci
npx ng serve                       # http://localhost:4200
npx ng test --watch=false          # Vitest vía @angular/build:unit-test
npx ng build --configuration production
```

## Frameworks y versiones

| Ámbito | Herramienta | Versión | Dónde se fija |
|---|---|---|---|
| Móvil | JDK (código y daemon de Gradle) | 17 | `apps/movil/app/build.gradle.kts` y `apps/movil/gradle/gradle-daemon-jvm.properties` (Gradle lo busca o lo descarga solo, sin importar `JAVA_HOME`) |
| Móvil | Gradle | 9.6.0 | `apps/movil/gradle/wrapper/gradle-wrapper.properties` |
| Móvil | Android Gradle Plugin (Kotlin integrado, DSL nuevo) | 9.4.1 | `apps/movil/gradle/libs.versions.toml` |
| Móvil | Kotlin (plugins Compose y serialization; sin `kotlin-android`, lo integra AGP 9) | 2.2.21 | ídem |
| Móvil | Jetpack Compose BOM | 2026.06.00 | ídem |
| Móvil | Navigation 3 | 1.1.7 | ídem |
| Móvil | Lifecycle / ViewModel | 2.10.0 | ídem |
| Móvil | kotlinx-serialization-json | 1.11.0 | ídem |
| Móvil | CameraX · ML Kit Barcode · ZXing | 1.6.2 · 17.3.0 · 3.5.4 | ídem |
| Móvil | CameraX `camera-mlkit-vision` (analizador QR del visor M03) | 1.6.2 | ídem (mismo `version.ref = "camerax"`) |
| Móvil | compileSdk / targetSdk / minSdk | 36 / 36 / 26 | `app/build.gradle.kts` |
| Móvil | Robolectric (pruebas JVM de Compose) | 4.16 | `libs.versions.toml` |
| Web | Node / npm | 22.23.2 / 10.9.8 | `apps/web/.nvmrc`, `package.json` (`packageManager`), CI |
| Web | Angular core / CDK · Angular CLI | 22.1.7 · 22.1.7 / 22.1.8 | `apps/web/package-lock.json` (declarado `^22.1.0` / `^22.1.8` en `package.json`) |
| Web | TypeScript | 6.0.3 | `apps/web/package-lock.json` (declarado `~6.0.2`) |
| Web | Vitest (vía `@angular/build:unit-test`) | 4.1.11 | `apps/web/package-lock.json` (declarado `^4.0.8`) |

## Instalar el APK

Versión corta; los detalles y qué debe verse están en «Guía para el tutor», Paso 1.

1. Descargar el `.apk` de la última **Release** de este repositorio.
2. En el celular, permitir «instalar apps de origen desconocido» para el navegador o gestor de archivos.
3. Abrir el archivo e instalar. Alternativa con el celular conectado: `adb install -r alarmas-qr-vX.Y.Z.apk`.
4. Al primer uso la app pide permiso de cámara (M12) y de alarmas exactas (M11, Android 12+). La alarma debe sonar con la app cerrada.

## Estructura

```
apps/movil/          proyecto Android Studio · Kotlin + Jetpack Compose (Material 3) · Navigation 3
apps/web/            proyecto Angular CLI · componentes independientes · señales · CDK para modales y tabla
packages/tokens/     design-tokens.json · Tokens.kt (Compose) · tokens.css (Angular) · fuentes OFL
docs/                documentación UX copiada del repo de UX (ver abajo)
.github/workflows/   ci.yml (lint + tests) · apk.yml (APK en cada tag vX.Y.Z)
```

## Diseño

- Sistema «Energía puntual»: fuente de verdad `docs/DESIGN_SYSTEM.md`; tokens en `packages/tokens/design-tokens.json`, consumidos como `Tokens.kt` en Compose (tema `AlarmasQRTheme`) y como variables CSS en Angular (`tokens.css` importado en `styles.css`).
- Los componentes web se construyen propios sobre el Angular CDK (overlay, tabla, a11y); no se usa `mat-button` ni el tema de Angular Material para no pelear con la anatomía del sistema.
- Reglas que el código debe respetar: **un solo elemento amarillo por pantalla** (la acción principal o el FAB); estados activos en Tinta; **botones de 52 pt en móvil y 44 pt en web**; radio 14 en tarjetas y modales, 20 en el diálogo de confirmación, 12 en campos; **eliminar y cerrar sesión siempre pasan por un diálogo de confirmación** (acción segura prominente, confirmación en contorno, rótulos de máximo dos palabras); texto ≤ 15 pt en los tonos AA (Coral, Verde, Azul, Gris Texto); horas en formato 12 h con Spline Sans Mono.
- Fuentes (Google Fonts, licencia OFL) empaquetadas en cada app: Bricolage Grotesque (titulares), Archivo (UI), Spline Sans Mono (horas y cifras).
- Transiciones de 250 ms con ease in-out; mantener presionado el FAB 500 ms abre la hoja «Agregar evento».

## Trazabilidad

Cada pantalla conserva su código de los mockups. La tabla pantalla → funcionalidad → ruta → componente está en `docs/TRAZABILIDAD.md`; las funcionalidades en `docs/FUNCIONALIDADES.md` (F-Mxx / F-Wxx) y los recorridos en `docs/NAVEGACION.md` §6 y §6b.

Convenciones de git: una rama por pantalla o flujo (`feature/M06-editar-alarma`), PR pequeño revisado por el otro integrante, mensaje de commit con el código de pantalla al inicio («M06: selector de anticipación»).

## Plan 2 · móvil de la Persona A

Doce pantallas de `apps/movil` pixel-perfect contra Figma, con cámara y alarma reales (`docs/superpowers/plans/2026-09-20-plan2-movil-persona-a.md`).

**Pantallas hechas** (todas registradas en `navegacion/EntradasApp.kt`): M01 «Bienvenida», M00a «Crear cuenta», M00b «Iniciar sesión», M02v «Inicio · sin alarmas», M02 «Mis alarmas», M02h «Agregar evento» (hoja), M12 «Permiso de cámara», M03 «Escanear QR» (CameraX + ML Kit real), M13 «QR no reconocido», M03b «Pantallazo recibido», M04 «Alarma programada» (hoja) + M04d «¿Eliminar alarma?» (diálogo), M05 «Guardada + Deshacer». Fuera del alcance de este plan: M02b y M06–M11, construidas después por el Plan 4 (ver abajo).

**Cómo probar la cámara:** desde M02 (o M02v), tocar el FAB «Escanear» (o «Escanear QR del evento») abre M12, que pide el permiso real `CAMERA`; «Abrir ajustes» completa la solicitud (o abre los ajustes de la app si el sistema ya no va a volver a preguntar) y navega a M03 con el visor encendido. Apuntar un QR con el contenido `alarmasqr://evento/e-entrega` (el único evento con QR del dataset) crea la alarma real («Entrega de proyecto UX») y navega a M04. Cualquier otro QR —por ejemplo uno con una URL— navega a M13 («QR sin evento»), cuyo enlace intenta abrirlo con `Intent.ACTION_VIEW`. El chip «Linterna · auto» enciende la lámpara del dispositivo (`LifecycleCameraController.enableTorch`).
**Sin cámara real** (emulador sin cámara, o para no tener que imprimir un QR): los dos toques ⏩ de M03 simulan la lectura — tocar el visor («Apunta al código QR del evento») simula leer el QR de `e-entrega` (mismo resultado que escanearlo); tocar «vibra al detectar el código» simula un QR inválido (→ M13). `FlujosPersonaATest` usa estos mismos toques ⏩, no la cámara real.

**Cómo probar la alarma con la app cerrada:** al llegar a M04 se programa una alarma real (`AlarmManager.setExactAndAllowWhileIdle`). Decisión D4: como `dataset.json` vive en agosto de 2026, en un dispositivo con la fecha real (posterior) la hora de la alarma ya pasó, así que `ProgramadorAlarmas` la reprograma a **ahora + 1 minuto** para poder demostrarla; cerrar la app (o el celular) y esperar ese minuto. Android 13+ pide el permiso `POST_NOTIFICATIONS` la primera vez que se llega a M04 (un diálogo del sistema); **sin concederlo no hay nada que ver ni oír**: `AlarmManager` solo dispara el `PendingIntent` de `ReceptorAlarma`, no reproduce sonido ni vibración por sí mismo, y `NotificacionesAlarma.publicar` corta con `return` temprano si `POST_NOTIFICATIONS` no está concedido, así que no se crea ninguna notificación (ni siquiera silenciosa). Android 14+ puede exigir además activar manualmente «Notificaciones de pantalla completa» para la app en Ajustes → Apps → Alarmas QR → Notificaciones, para que la notificación se muestre sobre la pantalla de bloqueo con la app cerrada; sin ese permiso la notificación llega igual, pero no a pantalla completa. Tocar la notificación (o su intent de pantalla completa) abre `alarma/{id}/sonando`, que `destinoDesdeIntent` resuelve a M10 (marcador de la Persona B hasta que lo construya). **Las alarmas no sobreviven un reinicio del dispositivo:** no hay un `BroadcastReceiver` de `BOOT_COMPLETED` que las reprograme (el permiso está declarado en el manifiesto pero no se usa); fuera de alcance de la maquetación.

**Toques ⏩ simulados** (docs/NAVEGACION.md §6): visor de M03 (lee `e-entrega`), «vibra al detectar el código» de M03 (lee el QR inválido del dataset), «Abrir ajustes» de M12 (pide `CAMERA` real; con el permiso ya concedido —como en las pruebas— completa de inmediato y navega a M03).

**Decisiones D1–D10** (`docs/superpowers/plans/2026-09-20-plan2-movil-persona-a.md`, sección «Decisiones tomadas al escribir el plan»):

| # | Decisión |
|---|---|
| D1 | Un solo `feature/plan2-movil-persona-a` con commits prefijados por pantalla y un PR, en vez de una rama por pantalla (CLAUDE.md): las pantallas comparten componentes y navegación. |
| D2 | Capturas de verificación con Robolectric (`decorView.draw(Canvas)`, no `captureToImage()` ni el plugin de screenshot de Compose) — ver «capturar() multi-ventana» en la tabla de abajo. |
| D3 | La alarma «Entrega de proyecto UX» (`a-entrega`, `esNueva: true`) arranca fuera de la lista, «pendiente de escaneo»; solo aparece cuando T1 la crea al escanear `e-entrega`. |
| D4 | Hora real de la alarma: si `alarma.suena` ya pasó (dataset de agosto de 2026), se programa a ahora + 1 min para poder demostrarla en un dispositivo real. |
| D5 | M12 «Abrir ajustes» pide el permiso real `CAMERA`; si el sistema no va a volver a preguntar, abre los ajustes de la app; al volver, con o sin permiso, siempre navega a M03. |
| D6 | La píldora activa de la barra inferior va en Gris Niebla (no en Tinta, a diferencia del resto de estados activos) — mockups M02/M02v y DS §6. |
| D7 | El bloque «Sonará» (M04) separa la hora del sufijo am/pm en dos estilos (`HoraSonara` + `HoraProtagonistaSufijo` Medium más pequeño), aunque el mockup los dibuja en un solo tramo — sigue la regla de CLAUDE.md/DS. Anotar en el repo de UX. |
| D8 | «vibra al detectar el código» (M03, texto secundario sobre Tinta) usa `Colores.GrisBorde` en vez del `#B9B7BF` exacto del mockup (sin token nuevo). Anotar en el repo de UX. |
| D9 | Solo hay `ViewModel` donde hay estado que muta: `M02InicioViewModel` (M02/M02v/M05), `M03EscanerViewModel`, `M04AlarmaCreadaViewModel`. M01, M00a, M00b, M02h, M03b, M12 y M13 son composables de estado local (`rememberSaveable`) con callbacks. |
| D10 | La altura de los campos es 48 (token `Tamanos.Campo`, DS comp. 06) aunque el mockup mide 45: manda el token. |

**Otras decisiones que cambian el comportamiento o cómo probar** (del acta de ejecución, no estaban en el plan original):

| Qué | Dónde | Por qué |
|---|---|---|
| `capturar()` multi-ventana | `ui/Verificacion.kt` (prueba), Tarea 14 | Una hoja (`ModalBottomSheet`, M02h/M04) o un diálogo (`Dialog`, M04d) se dibujan en su propia ventana Android, no en el `decorView` de la actividad de prueba. `capturar()` ahora compone **todas** las ventanas visibles —leídas por reflexión de `WindowManagerGlobal.mViews`, sin shadow público en Robolectric 4.16— en el mismo z-order en que Android las agrega, así una captura de M04/M04d/M02h incluye la hoja o el diálogo sobre la pantalla de fondo atenuada. Si la reflexión fallara, cae al único `decorView` como antes (documentado en su KDoc). |
| `MlKitContext.initializeIfNeeded` en pruebas de flujo | `navegacion/FlujosPersonaATest.kt`, Tarea 14 | Con `CAMERA` concedido, M03 real monta `VisorCamara`, que construye un `BarcodeScanner` de ML Kit; en un dispositivo, `MlKitInitProvider` (`ContentProvider` de la AAR `common`) inicializa `MlKitContext` al arrancar el proceso, pero Robolectric no lo dispara a tiempo dentro de una prueba JVM. La prueba llama la misma inicialización pública en `@Before` para poder montar el visor real. |
| Columna de la hora con `widthIn(min = …)` | `ui/componentes/TarjetaAlarma.kt`, Tarea 7 | El ancho fijo de 81 dp partía el sufijo am/pm letra por letra en horas de 5 dígitos (p. ej. «12:00 pm»); ahora es un mínimo, no un fijo. |
| Esquinas redondeadas del marco de enfoque (20 dp) | `ui/pantallas/m03/M03EscanerScreen.kt` (`Medidas.RadioEsquinaEnfoque`), Tarea 9 | El mockup dibuja un corchete con arco de 90°, no los dos segmentos rectos de la primera versión. |
| «Deshacer» con alcance por alarma y ventana que expira | `datos/RepositorioDataset.kt` (`cambiarEstado`, `olvidarDeshacer`), `navegacion/EntradasApp.kt` (entrada M05), Tarea 13 | Antes, «Deshacer» revertía también los toques del interruptor de *otras* alarmas hechos durante la ventana de 5 s; ahora `cambiarEstado` aplica el cambio también al snapshot `anterior`. Y la ventana no se cerraba sola al vencer los 5 s (`olvidarDeshacer()` la cierra), así que volver a M05 ya no reabre un «Deshacer» viejo. |
| Chips «Nueva» + «✓ Escaneada» en M05 | `dataset.json` (`a-entrega.chips`), Tarea 13 | El mockup solo dibuja «Nueva»; el dataset (fuente única de datos, igual en móvil y web) ya trae ambos chips — diferencia aceptada, no se toca el dataset. |
| M13 «abrir enlace» falla en silencio sin navegador | `navegacion/EntradasApp.kt` (`alAbrirEnlace`, `runCatching`), Tarea 8, **parked** | Si el dispositivo no tiene ninguna app que resuelva `ACTION_VIEW` para el enlace, el toque no hace nada visible (no hay snackbar de error). Real pero no estructural: todo Android trae un navegador y el enlace es de demostración. |
| M03b «Continuar» sin respuesta si falta el evento | `navegacion/EntradasApp.kt` (`alContinuar`), Tarea 10, **parked** | Si `agregarDesdeEvento` devolviera `null` (evento inexistente), el toque no navega ni avisa. Imposible con el dataset fijo actual (`e-entrega` siempre existe). |

**Pruebas pendientes en dispositivo real** (no se pueden correr en este entorno): cámara real (CameraX + ML Kit leyendo un QR impreso o en otra pantalla), intent de compartir (pantallazo desde WhatsApp/galería hacia «Alarmas QR» → M03b), y la alarma sonando con la app cerrada (notificación de pantalla completa, permisos de Android 13+/14+) — ver `docs/PLAN_MAQUETACION.md` §7.

**Verificación pixel-perfect:** cada pantalla tiene su pareja `<código>.png` / `<código>-figma.png` en `docs/verificacion/`, con el estado de la comparación y las diferencias aceptadas — ver `docs/verificacion/README.md`.

**Notas para el repositorio de UX** (`https://github.com/alejortizp/alarmas-qr-ux`, no se pueden abrir desde aquí):

- D7: el bloque «Sonará» de M04 debería dibujar la hora y el sufijo am/pm en dos estilos separados (Medium más pequeño), no en un solo tramo como el mockup actual.
- D8: «vibra al detectar el código» (M03) debería usar el gris de texto secundario del DS (`gris.texto` / Gris Borde) sobre Tinta, no el `#B9B7BF` suelto del mockup.
- M03b: los nombres de capa en Figma de dos textos están desactualizados respecto al contenido real — la capa «Encontramos un QR de event…» contiene «QR de evento detectado en tu pantallazo», y la capa «Nos vemos el viernes 30 en…» contiene «…el domingo 30…» (manda el contenido, no el nombre de capa).
- M05: la tarjeta nueva («Entrega de proyecto UX») debería mostrar los chips «Nueva» y «✓ Escaneada» juntos en el mockup, ya que `dataset.json` (fuente única de datos) los trae así.

**Scroll vertical en pantallas de columna (móvil):** los mockups miden 390×844. Para que en un teléfono más bajo, en horizontal, con la fuente del sistema agrandada o con el teclado abierto nada quede cortado, las pantallas de columna usan `ColumnaDesplazable` (`ui/componentes/ColumnaDesplazable.kt`). Ya la usan M01, M00a, M00b, M02v, M03b, M12 y M13. A 390×844 se ven idénticas: las capturas de `docs/verificacion/` son las mismas, píxel a píxel. En pantallas más bajas el contenido se desplaza, y los `Spacer(Modifier.weight(1f))` siguen anclando el pie abajo cuando sobra espacio. Las hojas M02h y M04 llevan `Modifier.verticalScroll(rememberScrollState())` en su columna raíz. M02 no lo necesita porque ya es una `LazyColumn`, y M03 tampoco, porque su visor de cámara es flexible y ocupa el alto que sobra. La prueba es `PantallasDesplazablesTest` (teléfono de 390×560).

## Plan 3 · web de la Persona A

Páginas de `apps/web` pixel-perfect contra Figma a 1280×820 (`docs/superpowers/plans/2026-09-21-plan3-web-persona-a.md`; medidas en `docs/superpowers/specs/2026-09-21-medidas-figma-web-persona-a.md`).

**Páginas hechas:**
- W00 «Inicio de sesión» (`/login`), con el error de credenciales ⏩ (el primer foco en la contraseña lo simula) y los avisos de 3 s `?estado=correo-enviado` y `?estado=eliminada`.
- W00 «Recuperar contraseña» (`/login/recuperar`).
- W06 «Ajustes de Perfil» (`/perfil`), con el aviso «Perfil actualizado».
- Modal «Eliminar cuenta» (`/perfil/eliminar`): hay que escribir ELIMINAR y «Conservar mi cuenta» es el primario.
- Diálogo «¿Cerrar sesión?» (`?dialogo=cerrar-sesion` sobre cualquier página con barra lateral).
- Barra superior y barra lateral reales (iconos del DS, colapsable a 64).

W01, W03, W04 y W05 siguen como marcadores de la Persona B, ya dentro del layout real.

**Componentes L09** (`src/app/componentes/`, selector `aq-*`, solo tokens):
- `aq-icono` y `aq-logotipo`.
- `button|a[aq-boton]` (primario, secundario o destructivo, y `bloque`) y `a|button[aq-enlace]`.
- `aq-campo` (Signal Forms, `[formField]`), `aq-switch` y `aq-selector-segmentado`.
- `aq-tarjeta` (normal o peligro) y `aq-tarjeta-acceso`.
- `aq-snackbar` + `SnackbarService.mostrar(texto)`.
- `aq-modal` y `aq-dialogo-confirmacion`.
- `aq-barra-superior` y `aq-barra-lateral`.

**Guardia de tokens:** `npm run lint` ejecuta `scripts/verificar-tokens.mjs`, que falla si `src/app` escribe a mano un color, un `rgb()`/`rgba()` o una medida en `px`.

**Degradación elegante fuera del marco (tokens v1.12):** la entrega es pixel-perfect a 1280×820; por debajo de ese ancho la app no se rompe, con dos cortes que viven en `design-tokens.json` (`breakpoint`) y en `tokens.css`:
- `--breakpoint-web-colapsar-barra` (**1200**): con la ventana más angosta, **la barra lateral se colapsa sola** a 64. Al superarlo se expande otra vez. Entre cruces el usuario puede expandir o colapsar con el control, y el siguiente cruce vuelve a mandar.
- `--breakpoint-web-apilar-columnas` (**1100**): con la ventana más angosta, las páginas de dos columnas las apilan en una (W06: la tarjeta Perfil de 561 y la columna Privacidad/Eliminación).
- La barra superior y la lateral quedan fijas; solo el `<main>` hace scroll.

A 1280 los dos cortes quedan por encima, así que las capturas de `docs/verificacion/` no cambian. Las variables CSS no funcionan dentro de `@media`, así que los cortes se leen en tiempo de ejecución con `CortesService` (`src/app/navegacion/cortes.service.ts`, señales `colapsarBarra` y `apilarColumnas`, vía `matchMedia`). Nunca se escribe una `@media` con px a mano, porque la guardia de tokens lo rechaza.

**Decisiones D1–D16** (sección «Decisiones» del plan). Las que se pueden querer revertir:
- El modal «Eliminar cuenta» sigue el mockup: 481, sin miga + ✕ y título 18 (D2).
- Campos de 48 con etiqueta 12 y placeholder Gris Medio, frente a los 46/11 del mockup (D3).
- Tarjeta de acceso de 520 (D4).
- Botones uniformes de 14.5 (D5).
- Modal y diálogo en la URL con `cdkTrapFocus` en vez de `CdkOverlay` (D6).
- Snackbar global centrado (D7).
- Error de credenciales por foco ⏩ (D8).
- Switch apagado en contorno Tinta (D9).

**Pendiente en el repo de UX:**
- DS §7, filas «Tarjeta de acceso» (400 → 520) y «Diálogo modal» (540–600 con miga + ✕ → 481 sin miga, título 18).
- La regla de CLAUDE.md sobre la fila de cabecera del modal.
- Los marcos de W00 con snackbar sin el enlace «¿Olvidaste tu contraseña?».
- El snackbar descentrado de «correo enviado».
- El estado apagado del switch y la excepción del logotipo amarillo.

## Plan 4 · móvil de la Persona B

Las ocho pantallas móviles que faltaban, con lo que cierra el recorrido completo de la app
(`docs/superpowers/plans/2026-09-23-plan4-movil-persona-b.md`).

**Pantallas hechas** (registradas en `navegacion/EntradasApp.kt`, ya no quedan marcadores de la Fase 0 en móvil):

- M02b «Vista calendario» (`/calendario`): mes navegable con marca en los días con alarmas, detalle del día
  seleccionado y el selector «Lista / Mes» de la barra superior.
- M06 «Editar alarma» (`/alarma/:id`): tarjeta de resumen, anticipación, trayecto, sonido, «No molestar», posponer,
  cambios del organizador, notas, «Gestionar en el calendario» y el diálogo M06d.
- M07 «Crear evento a mano» (`/evento/nuevo`): formulario con validación inline del título, que crea la alarma y su
  evento QR (`RepositorioDataset.crearAlarmaManual`).
- M08 «QR del evento» (`/evento/:id/qr`): QR real del evento, compartir por WhatsApp/correo/otras apps con intents
  reales, y descargar o copiar el enlace simulados con snackbar.
- M09 «Cambio en tu evento» (`/alarma/:id/cambio`): notificación del organizador con el antes/ahora y las dos salidas.
- M10 «La alarma suena» (`/alarma/:id/sonando`): destino real de la notificación de pantalla completa que programa
  el `AlarmManager`.
- M11 «Ajustes» (`/ajustes`): ajustes por defecto, permisos del sistema, calendarios, cuenta y el diálogo M11d.

**Flujos cubiertos:** T2, T3 y T4 en `navegacion/FlujosPersonaBTest.kt`, sumados a T1, T6 y T7 de la Persona A.

**Ajustes posteriores a la revisión (2026-09-24):** los días del mes vecino de M02b usan Gris Texto (tono AA, no el
Gris Borde de los bordes); las filas de ajuste que navegan miden 48 por el área táctil mínima, aunque el mockup las
dibuja de 40; la hora de M07 va en Spline Sans Mono con dígitos tabulares; «Guardar y crear QR» no crea nada con el
título vacío; y el peso de las horas grandes subió a Black en el token y en el Design System (`design-tokens.json`
v1.20, DS v1.12), que hasta entonces decían Bold. Las divergencias entre la documentación de UX y lo construido
quedaron registradas en `docs/NAVEGACION.md` §7 — entre ellas, que **a M08 solo se llega desde M07**: el marco de
M06 no tiene control de compartir, aunque el mapa de navegación dibujaba esa arista.

## Plan 5 · web de la Persona B

Cuatro páginas de tablero de `apps/web`, cerrando las siete páginas web de TRAZABILIDAD (`docs/superpowers/plans/2026-09-21-plan5-web-persona-b.md`).

**Páginas hechas** (ya no son el `<aq-pantalla-marcador>` de la Persona A, dentro del layout real con barra superior/lateral):
- W01 «Mis Alarmas» (`/alarmas`): cuatro indicadores, gráfica de barras de escaneos por semana, pestañas Próximos/Pasados/Borradores, filtro de origen (Todos/Creados/Escaneados), búsqueda con Enter y tabla de eventos; recibe también el aviso de 3 s `?estado=descarga-completada` que dispara W05.
- W03 «Detalle Evento» (`/eventos/:id`): indicadores del evento y tabla anónima «Quiénes escanearon» (Ley 1581) con búsqueda y paginador de 2 páginas.
- W04 «Reportes» (`/reportes`): tarjeta de formulario (rango + formato) y tarjeta lateral «Reportes generados», que se apilan bajo el corte de 1100.
- W05 «Descargar QR» (`/qr`): selección de eventos activos, formato PNG/PDF y vista previa del afiche; «Descargar» vuelve a `/alarmas?estado=descarga-completada`.

**Componentes L09 nuevos** (`src/app/componentes/`, selector `aq-*`, solo tokens): `aq-indicador`, `aq-chip`, `aq-pildoras`, `aq-campo-busqueda`, `aq-tabla` (`ViewEncapsulation.None` sobre contenido proyectado, D2), `aq-estado-vacio`, `aq-paginador`, `aq-grafica-barras` y `aq-vista-previa-afiche`.

**Tokens v1.13:** medidas de tablero tomadas de Figma — campo de búsqueda (240 en W01 / 260 en W03, alto 40), píldora del paginador (30), alto máximo de la gráfica de barras (96) y vista previa del afiche (miniatura 132, QR 72); `radius.casilla` (ya usado en móvil) se deriva ahora también a la web para la casilla de selección de W05.

**Decisiones D1–D9** (`docs/superpowers/plans/2026-09-21-plan5-web-persona-b.md`, sección «Decisiones tomadas al escribir el plan»):

| # | Decisión |
|---|---|
| D1 | Un solo `feature/plan5-web-persona-b` con commits prefijados por pantalla y un PR — mismo criterio que D1 del Plan 3, sin la línea de coautoría de Claude, ejecutado antes del Plan 4 (móvil). |
| D2 | `aq-tabla` con `ViewEncapsulation.None` en vez de `::ng-deep` (deprecado): cada página arma su propio `<thead>`/`<tbody>` (columnas distintas en W01 y W03), así que el componente solo puede diseñar contenido proyectado, con el estilo prefijado por su propio atributo de host. |
| D3 | «Pasados» sin «Ver detalle»: `dataset.json` (`web.eventosPasados`) trae eventos finalizados sin `id`; la fila de acción de la tabla queda vacía en ese filtro, sin inventar un id. |
| D4 | W03 solo tiene datos completos de asistentes para `w-partido` (único bloque de `web.asistentes` en el dataset); cualquier otro id (`w-seminario`) muestra sus indicadores reales pero la tabla de asistentes en estado vacío. |
| D5 | El filtro `estado` de W01 comparte el query param con el aviso de descarga: `/alarmas?estado=descarga-completada` no filtra nada (cae al mismo listado que `proximos`) y solo dispara el snackbar vía `effect()`, igual que D8 del Plan 3 en W00. |
| D6 | La búsqueda actualiza su propio valor en cada tecla, pero solo navega (y filtra) al presionar Enter — evita una ráfaga de navegaciones y hace la prueba determinista. |
| D7 | «Descargar de nuevo» (W04) y los checkboxes de W05 no descargan nada real: maquetación sin backend, solo snackbar `mensajes.descargaCompletada` o estado de selección en memoria. |
| D8 | Casilla de selección de W05: el DS no la mide en la web, así que reutiliza `size.movil.casilla` (20) y `radius.casilla` (4), ya en `design-tokens.json`, por ser el mismo componente conceptual. |
| D9 | Separación de 20 entre grupos de píldoras de filtro (DS §7): 10 dentro de un grupo (`--space-10`, ya existía) y 20 entre grupos (`--space-20`, tokens v1.13). |

**Flujo T5** (`apps/web/src/app/flujos-persona-b.spec.ts`, TRAZABILIDAD §4): W01 → «Ver detalle ›» → W03 → «Exportar reporte» → W04 → «Generar y descargar» → «‹ Mis alarmas» → W01 → «Descargar QR en lote» → W05 → «Descargar» → W01 con el snackbar «Descarga completada exitosamente».

**Verificación pixel-perfect:** pendiente de una comparación manual contra Figma (no se pudo capturar en este entorno) — ver la fila «Web · Plan 5 (Persona B)» de `docs/verificacion/README.md` con las rutas a revisar y las diferencias ya aceptadas por este plan.

## Cómo continuar (Persona B)

- Móvil · **regla de scroll vertical** (ver «Plan 2 · Scroll vertical en pantallas de columna»): toda pantalla nueva cuyo contenido sea una columna usa `ColumnaDesplazable` en vez de `Column(Modifier.fillMaxSize().padding(…))`:
  - El fondo y el `testTag` van en `modifier`, el relleno en `relleno = PaddingValues(…)` y el resto (`verticalArrangement`, `horizontalAlignment`, los `Spacer(Modifier.weight(1f))`) queda igual. Ejemplo: `ColumnaDesplazable(Modifier.fillMaxSize().background(Colores.Blanco).testTag("pantalla-M06"), relleno = PaddingValues(horizontal = Espacio.Margen), verticalArrangement = Arrangement.spacedBy(Espacio.EntreBloques)) { … }`.
  - Si la pantalla tiene barra superior, la barra va fuera y `ColumnaDesplazable` debajo, como en M12 y M13.
  - Las hojas inferiores (M09 si es hoja, o cualquier hoja nueva) llevan `Modifier.verticalScroll(rememberScrollState())` en su columna raíz.
  - Las listas largas (M02b, M11) van con `LazyColumn`.
  - No uses `ColumnaDesplazable` en pantallas cuyo contenido ya se estira para llenar el alto, como un visor de cámara (M03).
  - Agrega tu pantalla a `PantallasDesplazablesTest`: el último elemento debe alcanzarse con `performScrollTo()` a 390×560.
- Móvil: cada pantalla es una clave en `navegacion/Pantalla.kt`, pero desde el Plan 2 las pantallas **reales** (ya construidas por la Persona A) se registran todas en un único lugar, `navegacion/EntradasApp.kt` (`fun EntryProviderScope<NavKey>.entradasApp(pila, repositorio) { entry<Pantalla.M01> { … } … }`); tanto `MainActivity` como las pruebas de flujo (`FlujosPersonaATest`) pasan ese mismo bloque a `NavegacionApp(pila, repositorio) { entradasApp(pila, repositorio) }`. Para construir M06: añadir su `entry<Pantalla.M06> { clave -> M06EditarAlarmaScreen(clave.id, …) }` dentro de `entradasApp` (no en `MainActivity`) y crear `ui/pantallas/m06/M06EditarAlarmaScreen.kt` (+ `ViewModel` si hay estado que muta). El marcador (`PantallaMarcador`) desaparece solo — `NavegacionApp` arma su `NavDisplay` con `entryProvider(fallback = { clave -> entradaMarcador(clave, alVolver) }) { entradas() }`, así que el `fallback` solo dibuja las claves que `entradas` no registró; las pantallas reales tienen prioridad por construcción, no por orden. Cada clase de pantalla debe registrarse una sola vez dentro de `entradas`: Navigation 3 lanza una excepción si una clave se registra dos veces. Las hojas M02h y M04 se registran con `entry<Pantalla.M04>(metadata = HojaInferiorSceneStrategy.hoja()) { … }`; sin ese metadato se dibujan a pantalla completa. Un `ViewModel` con estado que sobrevive a recomposiciones se crea con `viewModel { MiViewModel(repositorio) }` (o `viewModel(key = clave.id) { … }` cuando la clave lleva un id, como M04/M05) dentro de la `entry`, no fuera: el decorador `rememberViewModelStoreNavEntryDecorator()` ya está registrado en `NavegacionApp` (`entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator(), rememberViewModelStoreNavEntryDecorator())`), así que no hay que añadirlo. El snackbar único de la app es `LocalSnackbarApp.current` (un `SnackbarHostState` provisto por `NavegacionApp`, comparte Scaffold con el FAB); no crear otro `SnackbarHostState` por pantalla. La notificación de la alarma abre M10 vía `NotificacionesAlarma.intentSonando(context, id)`, que arma un `Intent` a la ruta `alarma/{id}/sonando` (deep link resuelto por `destinoDesdeIntent` → `Pantalla.porRuta` en `MainActivity`); M10 (marcador hasta que la Persona B lo construya) es el destino real de "sonando la alarma". Los componentes compartidos (botones, campos, chips, tarjetas, snackbar, diálogo, etc.) viven en `ui/componentes/` y solo usan `Tokens.kt`. Datos: `RepositorioDataset` (`alarmas`, `alarma(id)`, `agregar`, `agregarDesdeEvento`, `eliminar`, `deshacer`, `cambiarEstado`). Pruebas: `createComposeRule` + Robolectric, navegar con `pila.irA(Pantalla.M06("a-tutor"))` y afirmar `testTag("pantalla-M06")`; los ayudantes de prueba `ui/Verificacion.kt` dan `capturar(nombre)` (captura de verificación pixel-perfect, ahora compone todas las ventanas Android visibles — hojas y diálogos incluidos, ver `docs/verificacion/README.md`) y la constante `QUALIFIERS_MOVIL` (qualifiers de Robolectric para el marco 390×844 a 2×). Nota: `Pantalla.todas` y `Pantalla.inicio` son `by lazy` en el companion porque la interfaz `Pantalla` tiene getters con valor por defecto (ciclo de inicialización de la JVM); no "simplificarlos" quitando el `lazy`. Tres rutas son sintéticas y no están literalmente en TRAZABILIDAD §1 (M01 `bienvenida`, M02v `inicio/vacio`, M05 `inicio/guardada/{id}`): ver el KDoc de cada una en `Pantalla.kt`.
- **Las dos apps están completas:** el Plan 5 cerró las páginas web y el Plan 4 las pantallas móviles que faltaban. Lo que sigue son las reglas vigentes para cualquier pantalla nueva.
- Web: **ya está hecha.** El Plan 5 construyó las cuatro páginas que faltaban (W01, W03, W04, W05 — ver «Plan 5 · web de la Persona B» arriba), así que `apps/web` no tiene más trabajo pendiente de la Persona B. Sigue el **Plan 4** (móvil: M02b, M06–M11), con las reglas de abajo.
- Convenciones: rama por pantalla, commit «M06: …», PR revisado por el otro integrante; tokens siempre, nunca valores a mano.

## Datos simulados

`packages/tokens/../dataset.json` (o `apps/*/assets/dataset.json`) contiene el dataset único de los mockups: las alarmas de Andrés (Reunión con el tutor 7:30 am, Gimnasio, Vuelo BOG–MDE, Reunión semillero, Entrega de proyecto UX, Asado), los eventos web (Seminario UX, Partido Sintética), los asistentes con alias, los indicadores y los escaneos por semana. Móvil y web deben mostrar los mismos datos.

## Documentación en `docs/`

| Archivo | Uso |
|---|---|
| `FUNCIONALIDADES.md` | Contrato funcional: cada F-Mxx / F-Wxx es una historia con criterios de aceptación |
| `NAVEGACION.md` | Mapa de rutas (§6 móvil, §6b web) |
| `TRAZABILIDAD.md` | Pantalla → F → ruta → componente |
| `DESIGN_SYSTEM.md` · `STYLE_TILE.md` | Tokens y reglas visuales |
| `MOCKUPS.md` | Decisiones de diseño y medidas web (§7) |
| `Mockups_Figma_Movil.pdf` · `Mockups_Figma_Web.pdf` · `Design_System_Alarmas_QR.pdf` | Referencia visual sin depender de Figma |
| `USER_FLOWS.md` · `CONCLUSIONES_PRUEBAS.md` | Flujos para pruebas y el porqué de las decisiones |
| `PLAN_MAQUETACION.md` | Plan y reparto acordados |

## Equipo

Alejandro Ortiz (alejortizp) y mmatallanar-ua · MISO · Universidad de los Andes · 2026.
