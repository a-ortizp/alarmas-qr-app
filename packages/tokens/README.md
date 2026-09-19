# handoff/ · paquete para el repositorio de código

Archivos nuevos que pide `PLAN_MAQUETACION.md` §5 y que no dependen del stack. Se copian tal cual a la carpeta `docs/` (y `packages/tokens/`) del repositorio de código cuando se cree.

| Archivo | Destino sugerido | Qué es |
|---|---|---|
| `TRAZABILIDAD.md` | `docs/` | Pantalla → F-código → ruta → componente, para móvil y web, más las rutas de las tareas T1–T8 |
| `design-tokens.json` | `packages/tokens/` | Tokens del sistema «Energía puntual» v1.5 (color, tipografía, tamaños, radios, espacio, movimiento) en formato Design Tokens |
| `tokens.css` | `packages/tokens/` y `apps/web` | Derivado CSS de los tokens con roles y la superficie Tinta |
| `dataset.json` | `packages/tokens/` o `apps/*/assets/` | Dataset único de los mockups: usuario, alarmas, eventos, asistentes, indicadores, mensajes |
| `README_APP.md` | raíz del repo de código como `README.md` | README completo para Kotlin + Compose y Angular |
| `CLAUDE_APP.md` | raíz del repo de código como `CLAUDE.md` | Convenciones para las sesiones de trabajo con IA (stack, reglas de diseño, navegación, git) |
| `Tokens.kt` | `apps/movil/app/src/main/java/<paquete>/ui/theme/` | Derivado Kotlin de los tokens para Compose: colores, tipografía, tamaños, radios y movimiento |
| `workflows/ci.yml` · `workflows/apk.yml` | `.github/workflows/` | CI de ambas apps y APK adjunto a la Release en cada tag |

Stack decidido el 2026-09-16 (Kotlin + Jetpack Compose · Angular + TypeScript): el paquete incluye `Tokens.kt` para Compose, los workflows `ci.yml` y `apk.yml`, y las plantillas de README y CLAUDE.md ya completas.

Además de estos archivos, el repo de código lleva copiados del repositorio de UX: `FUNCIONALIDADES.md`, `NAVEGACION.md`, `DESIGN_SYSTEM.md`, `STYLE_TILE.md`, `MOCKUPS.md`, `USER_FLOWS.md`, `CONCLUSIONES_PRUEBAS.md`, `PLAN_MAQUETACION.md` y los PDF `Mockups_Figma_Movil.pdf`, `Mockups_Figma_Web.pdf` y `Design_System_Alarmas_QR.pdf`.

Los tokens y el dataset se generaron el 2026-09-16 a partir de `DESIGN_SYSTEM.md` v1.5 y de los mockups (móvil v1.4, web v1.2), y se actualizaron el 2026-09-17 (tokens v1.7, dataset v1.1, trazabilidad v1.1): botones móviles de 52, velo móvil 55 %, tokens y textos del diálogo de confirmación (M04d, M06d, M11d) tras la revisión de los tutores. Si el sistema cambia, se regeneran aquí y se vuelven a copiar.

**2026-09-19 (móvil v1.7):** tokens v1.9 (`chip-control` 32 y `icono-visor` 48 en `size`, con sus equivalentes `--size-chip-control` / `--size-icono-visor` y `Tamanos.ChipControl` / `Tamanos.IconoVisor`; regla de barras superiores con espaciador flexible), TRAZABILIDAD v1.3 (notas de forma de la revisión de los tutores sobre los mockups móviles en M01, M00a, M03, M09 y M12; sin cambios de rutas ni de estados). Sin cambios en el dataset. Pendiente de copiar al repo de código.

**2026-09-19 (web v1.5):** tokens v1.8 (relleno horizontal 24 en `boton-web-padding`, barra lateral colapsada 64, icono 20, control 36, tarjeta de formulario web 600, diálogo web 420/r14), dataset v1.2 (eventos pasados, filtros, asistentes página 2, reportes generados, textos de acceso, barra lateral, mensajes nuevos), TRAZABILIDAD v1.2 (Reportes y Descargar QR como páginas, 24 estados web, diálogo de cierre de sesión) y README/CLAUDE con la estructura de secciones. Copiado al repo de código con `cp`.
