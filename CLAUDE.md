# CLAUDE.md

> `CLAUDE.md` del repositorio de código de Alarmas QR (stack decidido el 2026-09-16).

## Qué es este repositorio

Maquetación de las dos aplicaciones de **Alarmas QR** (crear alarmas escaneando el QR de un evento): `apps/movil` (Android, APK) y `apps/web` (administración y consulta). Pantallas navegables con datos simulados; sin backend ni autenticación real. La única capacidad real es la alarma del móvil, que debe sonar con la app cerrada.

Toda la investigación y el diseño viven en https://github.com/alejortizp/alarmas-qr-ux; una copia curada está en `docs/`. Antes de tocar una pantalla, leer su fila en `docs/TRAZABILIDAD.md` y su F-código en `docs/FUNCIONALIDADES.md`.

Todo el contenido visible es en español; los identificadores de código pueden ir en inglés, pero los nombres de pantalla conservan su código (M02, W04).

## Stack

- **Móvil** (`apps/movil`): Kotlin 2.x + Jetpack Compose con Material 3 (Compose BOM), Navigation 3 (`NavDisplay`, claves `Pantalla : NavKey`, hoja inferior como `SceneStrategy`), `ViewModel` + `StateFlow`, CameraX (+ `camera-mlkit-vision`) + ML Kit para el QR, `AlarmManager` (alarmas exactas) + notificación de pantalla completa para la alarma, intent filter para el pantallazo compartido. JDK 17, Gradle wrapper versionado. Comandos desde `apps/movil`: `./gradlew assembleDebug`, `installDebug`, `testDebugUnitTest lintDebug`, `assembleRelease`. Abrir en Android Studio la carpeta `apps/movil`, nunca la raíz.
- **Web** (`apps/web`): Angular 22 con componentes independientes y señales, TypeScript estricto, Angular CDK (overlay, tabla, a11y) sin Angular Material ni su tema; estilos con variables CSS de `tokens.css`; pruebas con Vitest + `TestBed`. Comandos desde `apps/web`: `npm ci`, `npx ng serve`, `npx ng test --watch=false`, `npx ng build --configuration production`.
- **Pruebas**: JUnit + pruebas de Compose (`createComposeRule`) en móvil; Vitest con `TestBed` en web; un flujo T1–T8 por app como prueba de navegación.
- **CI**: `.github/workflows/ci.yml` (lint + tests + build de lo que cambió, en cada PR) y `apk.yml` (APK de release firmado con el keystore de depuración, adjunto a la Release en cada tag `vX.Y.Z`).
- **Tokens**: nunca escribir colores, tamaños ni radios a mano; en Compose usar `Colores`, `Tipografia`, `Tamanos`, `Radios` de `Tokens.kt`; en Angular las variables `--color-*`, `--text-*`, `--size-*`, `--radius-*` de `tokens.css`.

## Reglas de diseño que el código debe respetar

Fuente de verdad: `packages/tokens/design-tokens.json` v1.11 (2026-09-21: medidas web de la Persona A, Plan 3; derivado de `docs/DESIGN_SYSTEM.md` v1.10; mockups móviles v1.7 del 2026-09-19: `chip-control` 32 para chips que se tocan, `icono-visor` 48, espaciador flexible en barras superiores). Nunca escribir colores, tamaños ni radios a mano: usar los tokens.

- **Un solo elemento amarillo por pantalla**: la acción principal o el FAB extendido. Estados activos (switch, píldoras, pestañas, chip «Nueva») en Tinta; la píldora activa de la barra inferior en Gris Niebla (DS §6) es la excepción documentada. Sobre Tinta el primario es blanco.
- **Alturas**: botones 52 en móvil (toque; 48 hasta el 2026-09-17) y 44 en web (puntero); acciones de la alarma sonando 56; campos 48; área táctil mínima 48.
- **Radios**: 14 en tarjetas, modales y contenedores; 20 en el diálogo de confirmación móvil; 12 en campos; píldora en botones, chips y filtros.
- **Prevención de errores**: ninguna acción irreversible o de salida se ejecuta en un toque. Eliminar una alarma (M04, M06) y cerrar sesión (M11) abren `DialogoConfirmacion` (DS comp. 47, `docs/TRAZABILIDAD.md` §1b): velo Tinta 55 %, título «¿Eliminar alarma?» / «¿Cerrar sesión?», consecuencia concreta, acción segura como primario amarillo («Conservar» / «Cancelar») y confirmación en contorno (Coral Texto si destruye, Tinta si solo sale); rótulos de máximo dos palabras; tocar el velo equivale a la acción segura. Eliminar la cuenta (web) usa el modal W07 con fricción.
- **Texto ≤ 15 pt** usa los tonos AA (`coral.texto`, `verde.texto`, `azul.texto`, `gris.texto`); nada por debajo de 12 salvo la barra inferior (11). Sobre Tinta el texto secundario va en Gris Borde.
- **Horas** en formato 12 h con Spline Sans Mono y dígitos tabulares; sufijo am/pm en Medium más pequeño en la misma línea.
- **Movimiento**: transiciones de 250 ms ease in-out; mantener presionado el FAB 500 ms abre la hoja; snackbar «Deshacer» de 5 s; respetar `prefers-reduced-motion`.
- **Textura de módulos QR** solo como banda de 120 pt en M01, M04, M09, M10, M12 y M13.
- Los estados nunca se comunican solo con color: color + forma o símbolo.

## Navegación

- Las rutas y nombres de `docs/TRAZABILIDAD.md` son obligatorios; cada ruta lleva el código de pantalla como nombre para poder navegar por código en las pruebas.
- En móvil, las pantallas reales se registran en `navegacion/EntradasApp.kt` (`entradasApp`); el marcador de la Fase 0 es el `fallback` del `entryProvider`, así que solo dibuja las claves que `entradasApp` todavía no registró.
- Los eventos externos (permiso de cámara, push del organizador, hora de la alarma) se simulan con los controles indicados con ⏩ en `docs/NAVEGACION.md` §6; no inventar pantallas ni puntos de entrada nuevos.
- En la web (v1.5) Reportes (`/reportes`) y Descargar QR (`/qr`) son páginas con miga «‹ Mis alarmas», título y una tarjeta de formulario de 600 px con una tarjeta lateral; el único modal es «Eliminar cuenta» (ruta propia sobre `/perfil`, velo Tinta al 45 %) y «Cerrar Sesión» abre el diálogo de confirmación web (velo 55 %, 420 px, «Cancelar» primario / «Cerrar sesión» contorno). La barra lateral lleva un icono por ítem y se colapsa a 64 px. En móvil, las hojas M02h y M04 son claves del back stack dibujadas como hoja inferior por `HojaInferiorSceneStrategy` sobre velo Tinta al 55 %; los diálogos de confirmación (M04d, M06d, M11d) no son rutas: son estado del componente padre sobre el mismo velo.
- La fila de cabecera de un modal (miga + ✕) es un solo control que cierra. Excepción vigente: el modal «Eliminar cuenta» sigue el mockup web v1.5 (sin miga; se cierra con «Conservar mi cuenta», Escape o el velo; D2 del Plan 3, pendiente de reflejar en el repo de UX).

## Datos

`dataset.json` es el único origen de datos simulados y debe ser idéntico en móvil y web (mismas alarmas, eventos, asistentes e indicadores; HOY = jueves 27 de agosto de 2026). No agregar datos de ejemplo distintos en una app.

## Convenciones de trabajo

- Una rama por pantalla o flujo: `feature/M06-editar-alarma`, `feature/W04-exportar-reporte`.
- PR pequeño, revisado y aprobado por el otro integrante antes de fusionar; ambos deben tener commits en `apps/movil` y en `apps/web`.
- Mensajes de commit con el código de pantalla al inicio: «M06: selector de anticipación».
- Archivos de pantalla con el código como prefijo: en Compose un archivo por pantalla `M06EditarAlarmaScreen.kt` con el composable `M06EditarAlarmaScreen` y su `ViewModel`; en Angular una carpeta por pantalla `w04-exportar-reporte/` con `w04-exportar-reporte.component.ts` y selector `aq-w04-exportar-reporte`.
- Al cambiar algo de diseño que contradiga `docs/DESIGN_SYSTEM.md`, primero se actualiza el documento en el repo de UX y luego el token; los mockups mandan.
- Derivados (APK, build web) se regeneran por CI; no se versionan a mano.

## Documentación

`docs/` es una copia: si se detecta un error en ella, corregirlo en el repositorio de UX y volver a copiar. `README.md` explica cómo correr e instalar; `docs/PLAN_MAQUETACION.md` el plan y el reparto.
