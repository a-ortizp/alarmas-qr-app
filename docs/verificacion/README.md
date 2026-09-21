# Verificación pixel-perfect (Plan 2)

Cada pantalla de la Persona A tiene una pareja de imágenes: la exportación del marco de Figma a 390×844 (`<código>-figma.png`)
y la captura de la implementación (`<código>.png`, generada por la prueba de la pantalla en `apps/movil/app/build/verificacion/`
con `capturar("<código>")`, Robolectric a 2×). Se comparan a ojo (spec §5.2, decisión D2 del plan) con la lista de
comprobación de `docs/PLAN_MAQUETACION.md` §7. Exportar el marco desde Figma: `get_screenshot` del MCP (o «Export» del
marco) con los ids de `docs/MOCKUPS.md` §5.

| Código | Marco Figma | Estado |
|---|---|---|
| M01 | 3:71 | ok · diferencias: párrafo parte en 3 líneas en vez de 2 (métrica de fuente de Robolectric); ver nota abajo sobre `capturar()` |
| M00a | 3:2 | ok · diferencias: subtítulo parte en 2 líneas en vez de 1 (métrica de fuente de Robolectric, igual que M01) |
| M00b | 3:37 | ok · diferencias: campo Correo enfocado con cursor y valor tecleado en Figma; la implementación lo precarga sin foco (aceptado, ver brief) |
| M02v | 4020:3553 | ok · pixel-perfect (diana, título, párrafo a 3 líneas y los tres botones alinean con el marco) |
| M02 | 3:131 | ok · diferencias aceptadas: 5ª alarma «Asado del semillero» / LUNES 31 (decisión D3, el marco solo muestra 4); «evento 6:20 pm» en la tarjeta «Reunión semillero» es el valor de `dataset.json` (fuente única de datos), 10 min distinto del marco. El sufijo am/pm ya no se parte de línea (ver nota abajo) |
| M02h | 4019:3139 | pendiente (Tarea 14) |
| M12 | 6:87 | pendiente |
| M13 | 6:122 | pendiente |
| M03 | 4:135 | pendiente |
| M03b | 4020:3295 | pendiente |
| M04 | 4:189 | pendiente |
| M04d | 4330:1432 | pendiente |
| M05 | 4:223 | pendiente |

**Nota M01 (Tarea 5, corregida en las rondas 1 y 2 de revisión):** el ayudante `capturar()` de `Verificacion.kt` ya
no usa `captureToImage()` — esa API cuelga bajo Robolectric en este proyecto (`forceRedraw()` espera hasta 2000 ms
un callback de redibujo real que solo se salta con el atajo `RobolectricIdlingStrategy.hasRobolectricFingerprint()`,
añadido en `compose-ui-test-junit4-android` 1.12; el `composeBom` fijado aquí resuelve 1.11.3 y subirlo exige
compileSdk 37 + AGP 9.1.0, fuera de alcance). `capturar()` ahora dibuja el `decorView` de la actividad de prueba
directamente (`decorView.draw(Canvas)` sobre un `Bitmap`), que sí completa de forma síncrona. `M01.png` es otra vez
la salida real de `regla.capturar("M01")` dentro de `M01BienvenidaScreenTest`, llamado antes de tocar cualquier fila
de calendario, así que las tres quedan sin marcar como en el marco de Figma. Diferencia aceptada frente a Figma: el
párrafo se parte en 3 líneas en vez de 2 (métrica de fuente de Robolectric).

**Nota M02 (Tarea 7, corregida en la ronda 1 de revisión):** en la primera versión de `M02.png` el sufijo am/pm de
`TarjetaAlarma` se partía en dos líneas («a» / «m») en vez de quedar en una sola línea junto a la hora. No era una
diferencia de métrica de fuente de Robolectric (como se pensó al principio): era un defecto real de tamaño. La
columna de la hora tenía un ancho fijo (`Modifier.width(Medidas.HoraTarjetaAncho)` = 81 dp) que no alcanza: 4 dígitos
tabulares a 26 sp (Spline Sans Mono Bold) miden ≈ 62 dp, más el gap de 6 dp, más «am»/«pm» a 12 sp (≈ 14 dp) suman
≈ 82 dp > 81 dp, así que el sufijo se partía letra por letra en toda tarjeta. Se corrigió en `TarjetaAlarma.kt`
(componente compartido con M04/M05, por eso se arregló en esta misma tarea): la columna ahora usa
`Modifier.widthIn(min = Medidas.HoraTarjetaAncho)` — conserva el ancho de 81 dp como mínimo (alinea igual que antes
en el caso común) pero puede crecer para horas de 5 dígitos como «12:00 pm» — y ambos `Text` (hora y sufijo) llevan
`maxLines = 1, softWrap = false`. `M02.png` es la salida real tras el arreglo: las cinco tarjetas, incluida
«12:00 pm», muestran el sufijo en una sola línea.
