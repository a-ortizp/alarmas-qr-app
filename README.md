# Alarmas QR · aplicaciones

> `README.md` del repositorio de código. Stack decidido el 2026-09-16: **Kotlin + Jetpack Compose** (móvil) y **Angular + TypeScript** (web); ver `docs/PLAN_MAQUETACION.md` §1.

Alarmas QR crea alarmas escaneando el código QR de un evento, sin digitar fecha, hora ni nombre. Este repositorio contiene la **maquetación** de sus dos aplicaciones: pantallas navegables con datos simulados, sin backend.

| Aplicación | Qué es | Pantallas | Entregable |
|---|---|---|---|
| `apps/movil` | App del asistente y organizador (Android) | 19 (M00a–M13 + M02v, M02h, M03b) + 3 diálogos de confirmación (M04d, M06d, M11d) | APK instalable |
| `apps/web` | Administración y consulta para organizadores | 7 páginas (W00, W01, W03, W04 Reportes, W05 Descargar QR, W06) con 24 estados, el modal «Eliminar cuenta» y el diálogo «¿Cerrar sesión?» (web v1.5) | Sitio desplegado |

Investigación, prototipos y diseño viven en el repositorio de UX: https://github.com/alejortizp/alarmas-qr-ux. Una copia curada está en `docs/`.

## Cómo correr

Requisitos: JDK 17, Android Studio (Koala o posterior) con SDK 34 y un emulador o dispositivo con Android 8+; Node 20 y npm 10.

```
# apps/movil · abrir la carpeta apps/movil en Android Studio (no la raíz del repo)
cd apps/movil
./gradlew assembleDebug            # compila el APK de depuración
./gradlew installDebug             # lo instala en el emulador o dispositivo conectado
./gradlew testDebugUnitTest lintDebug

# apps/web
cd apps/web
npm ci
npx ng serve                       # http://localhost:4200
npx ng test --watch=false
npx ng build --configuration production
```

## Instalar el APK

1. Descargar el `.apk` de la última **Release** de este repositorio.
2. En el celular, permitir «instalar apps de origen desconocido» para el navegador o gestor de archivos.
3. Abrir el archivo e instalar. Alternativa con el celular conectado: `adb install -r alarmas-qr-vX.Y.Z.apk`.
4. Al primer uso la app pide permiso de cámara (M12) y de alarmas exactas (M11, Android 12+). La alarma debe sonar con la app cerrada.

## Estructura

```
apps/movil/          proyecto Android Studio · Kotlin + Jetpack Compose (Material 3) · Navigation Compose
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
