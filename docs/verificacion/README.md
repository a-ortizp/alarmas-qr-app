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
| M12 | 6:87 | ok · pixel-perfect (visor apagado, título a 2 líneas, tarjeta «ACTÍVALA EN 3 PASOS», «Abrir ajustes» y las dos alternativas ancladas abajo alinean con el marco; banda de textura atenuada al 50 % verificada por muestreo de píxeles, sin diferencias de recorte de línea) |
| M13 | 6:122 | ok · pixel-perfect (sello «!», título, cuerpo, tarjeta «QUÉ DETECTAMOS» con el chip y el código Spline Sans Mono, «Volver a escanear», «Crear el evento a mano» y el enlace subrayado alinean con el marco; banda de textura atenuada al 50 % verificada por muestreo de píxeles, sin diferencias de recorte de línea) |
| M03 | 4:135 | ok · esquina del marco de enfoque corregida (fix round 1): ahora es un corchete redondeado (tramo recto + arco de 90° de `Medidas.RadioEsquinaEnfoque` = 20 dp, medido contra el marco de Figma por muestreo de píxeles) en vez de los dos segmentos rectos con remate de radio pequeño de la primera versión; comparado a ojo y en zoom contra `M03-figma.png`, el radio y la forma del corchete ya coinciden. Diferencia aceptada: título «Escanear QR» algo más grueso que en Figma (métrica de fuente de Robolectric, igual que M01/M00a). El resto (barra Tinta, chip «Linterna · auto», chip «● Cámara activa», textos del visor, hoja blanca r24 con los dos secundarios y el asa) alinea con el marco. Cámara real: pendiente de prueba en dispositivo (no se puede probar en este entorno) |
| M03b | 4020:3295 | ok · pixel-perfect (chip «✓ QR detectado», título, burbuja de WhatsApp con «Grupo MISO UX · hoy 8:12 am», el marco de lectura con el QR, «Origen: WhatsApp · compartido con Alarmas QR», «Continuar», «Elegir otra imagen» y la nota final alinean con el marco). Diferencia aceptada: el mensaje parte en «Nos vemos el domingo 30 en» / «SD-703. Escanea para agendar 👇» en vez de «…en SD-703.» / «Escanea…» (métrica de fuente de Robolectric, igual que M01/M00a/M03). Intent de compartir: pendiente de prueba en dispositivo (compartir una imagen desde la galería a «Alarmas QR» no se puede probar en este entorno) |
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
