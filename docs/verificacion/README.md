# Verificación pixel-perfect (Plan 2)

Cada pantalla de la Persona A tiene una pareja de imágenes: la exportación del marco de Figma a 390×844 (`<código>-figma.png`)
y la captura de la implementación (`<código>.png`, generada por la prueba de la pantalla en `apps/movil/app/build/verificacion/`
con `capturar("<código>")`, Robolectric a 2×). Se comparan a ojo (spec §5.2, decisión D2 del plan) con la lista de
comprobación de `docs/PLAN_MAQUETACION.md` §7. Exportar el marco desde Figma: `get_screenshot` del MCP (o «Export» del
marco) con los ids de `docs/MOCKUPS.md` §5.

| Código | Marco Figma | Estado |
|---|---|---|
| M01 | 3:71 | ok · diferencias: párrafo parte en 3 líneas en vez de 2 (métrica de fuente de Robolectric); ver nota abajo sobre `capturar()` |
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

**Nota M01 (Tarea 5):** `regla.capturar("M01")` de `M01BienvenidaScreenTest` no completa en este entorno (WSL2, Robolectric
4.16, `compose-ui-test-junit4-android` 1.11.3 vía compose-bom 2026.06.00): `forceRedraw()` espera hasta 2000 ms un
`ComposeTimeoutException` porque `androidx.compose.ui.test.RobolectricIdlingStrategy_androidKt.hasRobolectricFingerprint()`
solo se añadió como atajo en `compose-ui-test-android` 1.12.1 (verificado descompilando ambas versiones); subir a esa
versión exige compileSdk 37 y AGP 9.1.0 (fuera de alcance de esta tarea). El resto de la prueba (textos, alternar
calendarios, las dos salidas) pasa. El par de `M01.png` / `M01-figma.png` de esta carpeta se generó con una captura
manual (`View.draw(Canvas)` sobre el `decorView` de la actividad de prueba, sin pasar por `forceRedraw`/`PixelCopy`)
solo para esta verificación visual puntual; no es la salida de `capturar()` y no reemplaza el ayudante oficial.
