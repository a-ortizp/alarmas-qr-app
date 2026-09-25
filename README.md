# Alarmas QR · aplicaciones

> `README.md` del repositorio de código. Stack decidido el 2026-09-16: **Kotlin + Jetpack Compose** (móvil) y **Angular + TypeScript** (web); ver `docs/PLAN_MAQUETACION.md` §1.

Alarmas QR crea alarmas escaneando el código QR de un evento, sin digitar fecha, hora ni nombre. Este repositorio contiene la **maquetación** de sus dos aplicaciones: pantallas navegables con datos simulados, sin backend.

| Aplicación | Qué es | Pantallas | Entregable |
|---|---|---|---|
| `apps/movil` | App del asistente y organizador (Android) | 19 (M00a–M13 + M02v, M02h, M03b) + 3 diálogos de confirmación (M04d, M06d, M11d) | APK instalable |
| `apps/web` | Administración y consulta para organizadores | 7 páginas (W00, W01, W03, W04 Reportes, W05 Descargar QR, W06) con 24 estados, el modal «Eliminar cuenta» y el diálogo «¿Cerrar sesión?» (web v1.5) | Sitio desplegado |

Investigación, prototipos y diseño viven en el repositorio de UX: https://github.com/alejortizp/alarmas-qr-ux. Una copia curada está en `docs/`.

## Entrega · qué se entrega y dónde

| Qué | Dónde |
|---|---|
| Código de las dos aplicaciones | <https://github.com/a-ortizp/alarmas-qr-app> · repositorio **público**, se clona sin credenciales |
| APK instalable | [Release v1.0.2](https://github.com/a-ortizp/alarmas-qr-app/releases/tag/v1.0.2) → `alarmas-qr-v1.0.2.apk` (descarga directa, 39 MB) |
| Cómo validarlo | La «Guía para el tutor» de abajo, paso a paso |

El APK declara `minSdkVersion 26`, así que se instala en Android 8 en adelante — por encima del API 27 pedido — y
trae las cuatro arquitecturas (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`), de modo que sirve tanto en un celular
real como en un emulador.

### Cobertura de pantallas

Las **22 pantallas móviles** de los mockups están construidas y son navegables; ninguna quedó como marcador.

| Código | Pantalla | Cómo se llega |
|---|---|---|
| M01 | Bienvenida | Pantalla inicial de la app |
| M00a · M00b | Crear cuenta · Iniciar sesión | «Comenzar» desde M01, y «Ya tengo cuenta» entre ellas |
| M02v | Inicio sin alarmas | Tras crear la cuenta o entrar como invitado |
| M02 | Mis alarmas (hub) | Pestaña «Alarmas» de la barra inferior |
| M02h | Agregar evento (hoja) | Mantener presionado el FAB medio segundo |
| M02b | Vista calendario | Pestaña «Calendario» |
| M12 | Permiso de cámara | Primer toque del FAB «Escanear» |
| M03 | Escáner QR | Tras conceder el permiso en M12 |
| M03b | Pantallazo recibido | «Elegir pantallazo» en la hoja M02h |
| M13 | QR sin evento | Tocar «vibra al detectar el código» en M03 |
| M04 · M04d | Alarma programada (hoja) · ¿Eliminar alarma? | Al leer un QR válido · «No puedo asistir» dentro de M04 |
| M05 | Guardada + Deshacer | «Listo» en M04 |
| M06 · M06d | Editar alarma · ¿Eliminar alarma? | Tocar cualquier alarma de la lista · «Eliminar alarma» dentro de M06 |
| M07 | Crear evento a mano | «Crear a mano» en la hoja M02h |
| M08 | QR del evento | «Guardar y crear QR» en M07 |
| M09 | Cambio en tu evento | Fila «Confirmar antes de auto-ajustarse» en M06 (simula el push del organizador) |
| M10 | La alarma suena | «Aceptar cambio» en M09, o la notificación real de la alarma |
| M11 · M11d | Ajustes · ¿Cerrar sesión? | Pestaña «Ajustes» · fila «Cerrar sesión» |

Las **7 páginas web** cubren los ocho códigos W00–W07 de los mockups. Dos no son páginas propias, por decisión de
los mockups web v1.5: **W02** (mis eventos) vive dentro de W01 como pestañas, filtros y buscador, y **W07**
(eliminar cuenta) es el modal sobre W06. Cada estado de la tabla completa está en `docs/TRAZABILIDAD.md` §2.

| Código | Página | Ruta |
|---|---|---|
| W00 | Inicio de sesión, con error de credenciales y recuperar contraseña | `/login` · `/login/recuperar` |
| W01 · W02 | Mis Alarmas: indicadores, gráfica, pestañas, filtros y buscador | `/alarmas` |
| W03 | Detalle del evento, con la tabla anónima de asistentes | `/eventos/:id` |
| W04 | Reportes | `/reportes` |
| W05 | Descargar QR en lote | `/qr` |
| W06 | Ajustes de perfil | `/perfil` |
| W07 | Modal «Eliminar cuenta» | `/perfil/eliminar` |

### Componentes interactivos

Los controles responden de verdad, sin backend detrás: los switches cambian de estado, los selectores segmentados
(anticipación, sonido, Lista/Mes, formato PNG/PDF) marcan su opción, las pestañas y los filtros cambian la lista,
el buscador filtra al presionar Enter, el paginador avanza, las casillas se marcan y los diálogos de confirmación
abren y cierran. Lo que no existe es la capa de servidor: no hay cuentas ni persistencia, los datos salen de
`dataset.json` y al cerrar la app todo vuelve al estado inicial. Las descargas y los envíos muestran su aviso de
confirmación sin generar el archivo, salvo compartir por WhatsApp o correo desde M08, que sí abre la app del
sistema. Las únicas capacidades reales son la cámara con lectura de QR (M03) y la alarma que suena con la app
cerrada (M10).

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
4. Si en ese teléfono ya estaba instalada una compilación de desarrollo de la app, Android rechaza la instalación
   con «signatures do not match»: cada máquina firma con su propio keystore de depuración. Se desinstala la anterior
   y se vuelve a instalar. En un teléfono donde nunca estuvo, no pasa.
5. Desde la v1.0.2 todas las Releases se firman con la misma clave (ver el Paso 6), así que cada APK nuevo se
   instala encima del anterior como actualización y conserva las alarmas. Si el teléfono tiene la v1.0.0 o la v1.0.1,
   cada una firmada con una clave distinta, hay que desinstalarla una última vez antes de instalar la v1.0.2.
6. La app pide **permiso de cámara** (pantalla M12) y, en Android 13+, **permiso de notificaciones** la primera vez
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
instalarlo en un celular de prueba, pero no para publicar en Google Play. Compilado a mano usa el
`~/.android/debug.keystore` de ese computador, no la clave fija de las Releases (Paso 6): por eso no se instala
encima de un APK descargado de una Release, ni al revés.

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
git tag v1.0.2
git push origin v1.0.2
```

Antes del tag se sube `versionCode` y `versionName` en `apps/movil/app/build.gradle.kts`: Android solo instala una
actualización con un `versionCode` mayor. `.github/workflows/apk.yml` compila `assembleRelease` y publica la Release
con `alarmas-qr-v1.0.2.apk` adjunto.

**Firma fija.** El flujo firma el APK con un keystore guardado en el secreto del repositorio `DEBUG_KEYSTORE_BASE64`
(el archivo en base64, con las credenciales estándar de depuración `android` / `androiddebugkey`), así que todas las
Releases desde la v1.0.2 comparten firma (huella SHA-256 `9B:B9:87:3F:…:57:BE:4F:EE`) y se instalan como
actualización. Qué implica:

- GitHub guarda el secreto cifrado y no lo muestra en los registros; no hay copia local. Si se borra o se
  reemplaza, la Release siguiente tendrá otra firma y los teléfonos deberán desinstalar la app una vez.
- Las contraseñas son públicas: la protección depende de que el archivo no se filtre. Quien lo tuviera podría
  firmar un APK que se instale como actualización de Alarmas QR. Sirve para celulares de prueba, no para Google Play.
- Sin el secreto (por ejemplo, en un fork) el flujo genera una clave nueva en cada ejecución, como antes de la v1.0.2.
- Para reemplazarlo: `keytool -genkeypair -keystore alarmas-qr.keystore -storepass android -keypass android
  -alias androiddebugkey -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US"` y luego
  `base64 -w0 alarmas-qr.keystore | gh secret set DEBUG_KEYSTORE_BASE64`.

En
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
   Desde la v1.0.2 cada versión se instala encima de la anterior; la v1.0.0 y la v1.0.1 se desinstalan primero.
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

## Notas técnicas

**Scroll vertical en pantallas de columna (móvil):** los mockups miden 390×844. Para que en un teléfono más bajo, en
horizontal, con la fuente del sistema agrandada o con el teclado abierto nada quede cortado, las pantallas de columna
usan `ColumnaDesplazable` (`ui/componentes/ColumnaDesplazable.kt`) en vez de `Column(Modifier.fillMaxSize()…)`. A
390×844 se ven idénticas: las capturas de `docs/verificacion/` son las mismas, píxel a píxel. En pantallas más bajas
el contenido se desplaza, y los `Spacer(Modifier.weight(1f))` siguen anclando el pie abajo cuando sobra espacio.
- Si la pantalla tiene barra superior, la barra va fuera y `ColumnaDesplazable` debajo, como en M12 y M13.
- Las hojas inferiores (M02h, M04) llevan `Modifier.verticalScroll(rememberScrollState())` en su columna raíz.
- Las listas largas van con `LazyColumn` (M02), y las pantallas cuyo contenido ya se estira para llenar el alto, como
  el visor de cámara de M03, no la necesitan.
- La prueba es `PantallasDesplazablesTest` (teléfono de 390×560): el último elemento de cada pantalla debe alcanzarse
  con `performScrollTo()`.

**Degradación elegante (web):** la app es pixel-perfect a 1280×820; por debajo de ese ancho no se rompe, con dos
cortes que viven en `design-tokens.json` (`breakpoint`) y en `tokens.css`:
- `--breakpoint-web-colapsar-barra` (**1200**): con la ventana más angosta, **la barra lateral se colapsa sola** a 64.
  Al superarlo se expande otra vez. Entre cruces el usuario puede expandir o colapsar con el control, y el siguiente
  cruce vuelve a mandar.
- `--breakpoint-web-apilar-columnas` (**1100**): con la ventana más angosta, las páginas de dos columnas las apilan en
  una (W04, W05 y W06).
- La barra superior y la lateral quedan fijas; solo el `<main>` hace scroll.

A 1280 los dos cortes quedan por encima, así que las capturas de `docs/verificacion/` no cambian. Las variables CSS no
funcionan dentro de `@media`, así que los cortes se leen en tiempo de ejecución con `CortesService`
(`src/app/navegacion/cortes.service.ts`, señales `colapsarBarra` y `apilarColumnas`, vía `matchMedia`).

**Guardia de tokens (web):** `npm run lint` ejecuta `scripts/verificar-tokens.mjs`, que falla si `src/app` escribe a
mano un color, un `rgb()`/`rgba()` o una medida en `px` (incluida una `@media` con px).

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

## Equipo

Alejandro Ortiz (alejortizp) y mmatallanar-ua · MISO · Universidad de los Andes · 2026.
