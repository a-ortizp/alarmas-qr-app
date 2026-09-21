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

**Nota M01 (Tarea 5, corregida en la ronda 1 de revisión):** el ayudante `capturar()` de `Verificacion.kt` ya no usa
`captureToImage()` — esa API cuelga bajo Robolectric en este proyecto (`forceRedraw()` espera hasta 2000 ms un
callback de redibujo real que solo se salta con el atajo `RobolectricIdlingStrategy.hasRobolectricFingerprint()`,
añadido en `compose-ui-test-junit4-android` 1.12; el `composeBom` fijado aquí resuelve 1.11.3 y subirlo exige
compileSdk 37 + AGP 9.1.0, fuera de alcance). `capturar()` ahora dibuja el `decorView` de la actividad de prueba
directamente (`decorView.draw(Canvas)` sobre un `Bitmap`), que sí completa de forma síncrona. `M01.png` es otra vez
la salida real de `regla.capturar("M01")` dentro de `M01BienvenidaScreenTest` (por eso «Google Calendar» aparece
marcado: la prueba llama a `capturar()` justo después de alternar esa fila). Diferencia aceptada frente a Figma: el
párrafo se parte en 3 líneas en vez de 2 (métrica de fuente de Robolectric).
