# Alarmas QR · Plan de maquetación de las aplicaciones

Recomendaciones para pasar de los mockups de alta fidelidad a las dos aplicaciones maquetadas (app móvil con APK instalable y web de administración), en un repositorio nuevo y separado de este de UX.
Fecha: 2026-09-14 · **Stack decidido el 2026-09-16: Kotlin + Jetpack Compose (móvil) y Angular + TypeScript (web)** · Autores: Alejandro Ortiz (alejortizp) y mmatallanar-ua · Insumos: `FUNCIONALIDADES.md`, `NAVEGACION.md`, `DESIGN_SYSTEM.md`, `MOCKUPS.md`, `STYLE_TILE.md`.

## 1 · Decisiones

| Decisión | Recomendación | Por qué |
|---|---|---|
| Repositorio | **Un monorepo** nuevo (`alarmas-qr-app`) con `apps/movil`, `apps/web`, `packages/tokens` y `docs/` | El requisito de que ambos tengan commits en las dos apps se cumple en un solo historial; los tokens del Design System y la documentación viven una sola vez; un único enlace para el curso. Los conflictos de git son por archivo, así que trabajar en las dos carpetas a la vez no estorba. Repos separados solo si el curso exige un enlace por aplicación. |
| App móvil | **Kotlin + Jetpack Compose** (Material 3, Android Studio, Gradle) | Es la implementación de referencia de Material 3, la base estructural declarada en el Design System. La alarma que suena con la app cerrada usa `AlarmManager` (alarmas exactas) con notificación de pantalla completa; el QR se lee con CameraX + ML Kit; el pantallazo compartido desde WhatsApp llega por un intent filter. El APK sale de `./gradlew assembleRelease`. Ambos integrantes ya trabajaron con Kotlin en la maestría. Flutter se descartó por curva de aprendizaje, no por capacidad. **2026-09-20: se usa Navigation 3 en lugar de Navigation Compose; versiones fijadas en `README.md`.** |
| App web | **Angular + TypeScript** (Angular CLI, componentes independientes, señales), datos en JSON local | Marco completo (enrutador, formularios, inyección de dependencias) que encaja con una aplicación de administración de tablas, filtros y modales. Angular Material implementa Material 3, pero los diez componentes web de L09 se construyen **propios sobre el CDK** (overlay para modales, tabla, a11y) para no pelear con la anatomía de `mat-button`. Gráfica «Escaneos por semana» en SVG propio. Ambos integrantes ya trabajaron con Angular. |
| Alcance | **Maquetación**: pantallas navegables con datos simulados. Sin backend, sin autenticación real, sin push. | La alarma sí debe sonar de verdad en el APK: es el corazón del concepto y se prueba en un dispositivo. |
| Diseño | Los tokens se generan desde `DESIGN_SYSTEM.md` a `design-tokens.json` y de ahí a Kotlin (`Tokens.kt`, Compose) y CSS (`tokens.css`, Angular) | Una sola fuente de verdad; las reglas del sistema (un amarillo por pantalla, 52 pt móvil / 44 pt web, radio 14, tonos de texto AA, diálogo de confirmación antes de eliminar o salir) se vuelven constantes, no recuerdos. Tokens v1.7 desde el 2026-09-17. |

## 2 · Estructura del repositorio

```
alarmas-qr-app/
├── README.md                  stack, cómo correr cada app, cómo instalar el APK
├── CLAUDE.md                  convenciones para las sesiones de trabajo con IA
├── apps/
│   ├── movil/                 proyecto Android Studio (settings.gradle.kts aquí) · Kotlin + Compose · M00a–M13 + M02v, M02h, M03b
│   └── web/                   proyecto Angular CLI (angular.json aquí) · W00, W01, W03, W04 Reportes, W05 Descargar QR, W06 (páginas), modal «Eliminar cuenta» y diálogo «¿Cerrar sesión?»
├── packages/
│   └── tokens/                design-tokens.json → Tokens.kt (Compose) · tokens.css (Angular) · fuentes OFL
├── docs/                      copia curada de la documentación UX (ver §5)
└── .github/workflows/         ci.yml (lint + tests de ambas apps) · apk.yml (APK en cada tag)
```

### 2b · Trabajar el monorepo con Android Studio

- **Abrir `apps/movil`, no la raíz.** Android Studio necesita la carpeta que contiene `settings.gradle.kts`; si se abre la raíz del repo intentará indexar `node_modules`. La web se abre con VS Code o WebStorm en `apps/web`. Cada carpeta es un proyecto independiente que comparte el mismo historial de git.
- **Un solo `.gitignore` en la raíz** con las dos familias: Android (`apps/movil/build/`, `apps/movil/**/build/`, `apps/movil/.gradle/`, `apps/movil/local.properties`, `apps/movil/.idea/`, `*.iml`, `*.keystore` salvo el de depuración compartido) y Angular (`apps/web/node_modules/`, `apps/web/dist/`, `apps/web/.angular/`).
- **Gradle wrapper versionado** (`gradlew`, `gradle/wrapper/`) y JDK 17: así CI y los dos computadores compilan igual. Versiones fijadas con el Compose BOM y Kotlin 2.x.
- **Las sesiones con Claude Code se abren en la raíz del repo**: trabaja con archivos y con `./gradlew`, `npm` y `git` desde la terminal, así que ve las dos apps a la vez; Android Studio solo hace falta para el emulador, la vista previa de Compose y el depurador.
- **Fuentes**: los `.ttf` de Bricolage Grotesque, Archivo y Spline Sans Mono se copian a `apps/movil/app/src/main/res/font/` y a `apps/web/src/assets/fonts/`; `packages/tokens/` guarda el original y la licencia OFL.

Nombren ramas, rutas y componentes con los códigos que ya existen (`feature/M06-editar-alarma`, ruta `/m06`, componente `W04ReportesPage`): la trazabilidad con `FUNCIONALIDADES.md` queda gratis.

## 3 · Reparto para que ambos firmen en las dos apps

Repartir por **flujos**, no por aplicación, y revisar siempre el PR del otro.

| Quién | Móvil | Web |
|---|---|---|
| Persona A | Captura: M01, M00a, M00b, M02v, M02, M02h, M03, M03b, M04, M05, M12, M13 | Acceso y perfil: W00 (+ cuenta eliminada), W06, W06 · Modal eliminar cuenta, W06 · Actualizado |
| Persona B | Gestión: M02b, M06, M07, M08, M09, M10, M11 | Tablero: W01 (Todos · Creados · Escaneados), W03, W04 (3 estados), W05 (2 estados) |
| Ambos | Tokens, navegación raíz, dataset compartido, APK, README | |

Reglas de trabajo: una rama por pantalla o flujo; PR pequeño; el otro aprueba antes de fusionar; mensajes de commit con el código de pantalla al inicio («M06: selector de anticipación»). Con esto los commits de ambos aparecen en `apps/movil` y `apps/web` sin forzarlo.

## 4 · APK

- Firma de depuración basta para la prueba del curso; no hace falta cuenta de Play. En `app/build.gradle.kts`, `signingConfigs.release` apunta al mismo keystore de depuración para que `assembleRelease` produzca un APK instalable y optimizado.
- `cd apps/movil && ./gradlew assembleRelease` genera `app/build/outputs/apk/release/app-release.apk`; `adb install -r` para probar. Para iterar rápido, `assembleDebug` e instalar desde Android Studio.
- Publicar cada entrega como **GitHub Release** con el APK adjunto, generado por el workflow `apk.yml` al crear un tag (`v0.1.0`): `actions/setup-java` con JDK 17, `gradle/actions/setup-gradle`, `./gradlew assembleRelease` y `softprops/action-gh-release`. Plantilla lista en `handoff/workflows/apk.yml`.
- Probar en un dispositivo real la alarma con la app cerrada y el permiso de alarmas exactas (Android 12+), que es lo que M11 y M12 prometen.

## 5 · Documentación que se lleva al repo de código

No llevar todo el repositorio de UX: una copia curada en `docs/` más el enlace a `alejortizp/alarmas-qr-ux`, que sigue siendo la fuente de la investigación. **Los archivos «nuevos» de esta tabla ya están creados en la carpeta `handoff/` de este repositorio (2026-09-16), listos para copiar; solo las partes marcadas [STACK] esperan la decisión tecnológica.** Stack decidido el 2026-09-16 y repo `a-ortizp/alarmas-qr-app` creado ese día; el 2026-09-17 el paquete se actualizó (tokens v1.7, dataset v1.1, trazabilidad v1.1 con los diálogos de confirmación M04d/M06d/M11d como estados, docs v2.5.4) y se copió de nuevo al repo de código. `docs/` es una copia: las correcciones se hacen aquí y se vuelven a copiar.

| Nivel | Archivos | Para qué |
|---|---|---|
| Obligatorio | `FUNCIONALIDADES.md` | Es el contrato: cada F-Mxx / F-Wxx se vuelve una historia con criterios de aceptación. |
| Obligatorio | `NAVEGACION.md` (§6 y §6b) | Las tablas del recorrido son literalmente el mapa de rutas de cada app. |
| Obligatorio | `DESIGN_SYSTEM.md` y `STYLE_TILE.md` | Tokens y reglas; de aquí sale `design-tokens.json`. |
| Obligatorio | `Mockups_Figma_Movil.pdf` y `Mockups_Figma_Web.pdf` | Referencia visual sin depender de Figma. |
| Recomendado | `MOCKUPS.md` §7 | Medidas web ya tomadas (barra lateral 208, indicador 242×104, tabla 26/48, tarjeta de formulario de 600 en Reportes y Descargar QR, diálogo de confirmación web de 420). |
| Recomendado | `USER_FLOWS.md` | Base para escribir pruebas de flujo. |
| Recomendado | `CONCLUSIONES_PRUEBAS.md` | Explica el porqué de cada decisión cuando alguien pregunte. |
| Nuevo | `README.md`, `CLAUDE.md` (plantillas `handoff/README_APP.md`, `handoff/CLAUDE_APP.md`) | Stack, cómo correr, cómo instalar el APK, convenciones. |
| Nuevo | `handoff/TRAZABILIDAD.md` (pantalla → código F → ruta → componente) | Trazabilidad en ambos sentidos, con las rutas de las tareas T1–T8. |
| Nuevo | `handoff/design-tokens.json` + `handoff/tokens.css` (derivado móvil pendiente del stack) | Paleta, escala tipográfica, tamaños, radios, espacio, movimiento. |
| Nuevo | `handoff/dataset.json` | El dataset único de los mockups (Reunión con el tutor 7:30 am, Partido Sintética, agosto 2026): usuario, alarmas, eventos, asistentes, indicadores y mensajes. |

Las tres fuentes (Bricolage Grotesque, Archivo, Spline Sans Mono) son de Google Fonts con licencia OFL: empaquetarlas en cada app en vez de cargarlas por red.

## 6 · Fases sugeridas

| Fase | Entregable | Ambos firman |
|---|---|---|
| 0 · Cimientos | Monorepo, proyecto Android Studio en `apps/movil` (Compose Material3, tema con `Tokens.kt`, Navigation Compose con las rutas de `TRAZABILIDAD.md`), proyecto Angular en `apps/web` (enrutador, `tokens.css`, layout con barra lateral), fuentes, dataset, README, CI que compila ambas apps · **2026-09-20: Navigation 3 en lugar de Navigation Compose; versiones fijadas en README** | Sí |
| 1 · Componentes | Móvil: composables de L03–L08 (botón, chip, tarjeta de alarma, campo, switch, FAB, barra superior, navegación inferior, snackbar). Web: los diez de L09 sobre el CDK (barra lateral, indicador, píldora, chip, tabla, cabecera de modal, modal, gráfica, afiche, snackbar) | Cada quien los de su flujo |
| 2 · Pantallas | Las 19 móviles (más los 3 diálogos de confirmación como estado de M04, M06 y M11, `DialogoConfirmacion`) y las 7 web con sus estados, con datos del `dataset.json` | Según §3 |
| 3 · Navegación | Recorridos de `NAVEGACION.md` §6 y §6b; alarma real en el APK | Sí |
| 4 · Entrega | APK en Release, web desplegada (GitHub Pages o Vercel apuntando a `apps/web`), `docs/` completa | Sí |

## 7 · Lista de verificación antes de entregar

- [ ] Cada pantalla de `FUNCIONALIDADES.md` existe y se alcanza por la ruta de `NAVEGACION.md`.
- [ ] Un solo elemento amarillo por pantalla; botones de 52 pt en móvil y 44 pt en web; radio 14.
- [ ] Eliminar una alarma y cerrar sesión pasan por un diálogo de confirmación (M04d, M06d, M11d) con la acción segura prominente y rótulos de máximo dos palabras.
- [ ] Tonos de texto AA (Coral, Verde, Azul y Gris Texto) para todo texto ≤ 15 pt.
- [ ] La alarma suena con la app cerrada en un dispositivo real.
  > 2026-09-21 (Plan 2, Persona A): la implementación está completa (`AlarmManager.setExactAndAllowWhileIdle` + `ReceptorAlarma` + notificación de pantalla completa con deep link a M10, decisión D4); falta la prueba manual en un dispositivo real, así que la casilla sigue sin marcar. Ver README «Plan 2 · móvil de la Persona A» § «Cómo probar la alarma con la app cerrada».
- [ ] El APK está adjunto a un Release y el enlace abre desde el celular.
- [ ] Ambos autores tienen commits en `apps/movil` y en `apps/web`.
- [ ] `docs/` contiene los archivos de §5 y el enlace al repositorio de UX.

## 8 · Enlaces

- Repositorio de UX (fuente): https://github.com/alejortizp/alarmas-qr-ux
- Repositorio de código (privado, cuenta educativa): https://github.com/a-ortizp/alarmas-qr-app
- Prototipo móvil: https://www.figma.com/proto/4nHD4ygcnP33UH0gAhaii5/Mockups-Alarmas---QR-Equipo-UX?node-id=3-71&p=f&scaling=min-zoom&content-scaling=fixed&page-id=1%3A6&starting-point-node-id=3%3A71&show-proto-sidebar=1
- Prototipo web: https://www.figma.com/proto/4nHD4ygcnP33UH0gAhaii5/Mockups-Alarmas---QR-Equipo-UX?node-id=4072-1861&p=f&scaling=min-zoom&content-scaling=fixed&page-id=4072%3A2&starting-point-node-id=4072%3A1861&show-proto-sidebar=1
- Design System en Figma: https://www.figma.com/design/lHYLJJIbltBS5Joo850iks
- Versión web de este plan: https://claude.ai/code/artifact/3609f3cc-05e3-4f1d-a697-c36551136c6b
