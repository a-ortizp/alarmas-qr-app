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
| M02h | 4019:3139 | ok · pixel-perfect a pantalla completa (Tarea 14: captura del flujo T6, `regla.capturar("M02h")` tras mantener presionado el FAB desde M02 real): la hoja «Agregar evento» compuesta sobre la lista M02 atenuada por el velo Tinta 55 % alinea con el marco (título, primario amarillo «Escanear el QR del evento», los dos secundarios y la nota final). Diferencias aceptadas, iguales que M02: la tarjeta «Reunión semillero» visible al fondo muestra «evento 6:20 pm» (valor de `dataset.json`) en vez de 6:10 pm del marco, con su chip «✓ Escaneada» (también del dataset) asomando en el borde inferior |
| M12 | 6:87 | ok · pixel-perfect (visor apagado, título a 2 líneas, tarjeta «ACTÍVALA EN 3 PASOS», «Abrir ajustes» y las dos alternativas ancladas abajo alinean con el marco; banda de textura atenuada al 50 % verificada por muestreo de píxeles, sin diferencias de recorte de línea) |
| M13 | 6:122 | ok · pixel-perfect (sello «!», título, cuerpo, tarjeta «QUÉ DETECTAMOS» con el chip y el código Spline Sans Mono, «Volver a escanear», «Crear el evento a mano» y el enlace subrayado alinean con el marco; banda de textura atenuada al 50 % verificada por muestreo de píxeles, sin diferencias de recorte de línea) |
| M03 | 4:135 | ok · esquina del marco de enfoque corregida (fix round 1): ahora es un corchete redondeado (tramo recto + arco de 90° de `Medidas.RadioEsquinaEnfoque` = 20 dp, medido contra el marco de Figma por muestreo de píxeles) en vez de los dos segmentos rectos con remate de radio pequeño de la primera versión; comparado a ojo y en zoom contra `M03-figma.png`, el radio y la forma del corchete ya coinciden. Diferencia aceptada: título «Escanear QR» algo más grueso que en Figma (métrica de fuente de Robolectric, igual que M01/M00a). El resto (barra Tinta, chip «Linterna · auto», chip «● Cámara activa», textos del visor, hoja blanca r24 con los dos secundarios y el asa) alinea con el marco. Cámara real: pendiente de prueba en dispositivo (no se puede probar en este entorno) |
| M03b | 4020:3295 | ok · pixel-perfect (chip «✓ QR detectado», título, burbuja de WhatsApp con «Grupo MISO UX · hoy 8:12 am», el marco de lectura con el QR, «Origen: WhatsApp · compartido con Alarmas QR», «Continuar», «Elegir otra imagen» y la nota final alinean con el marco). Diferencia aceptada: el mensaje parte en «Nos vemos el domingo 30 en» / «SD-703. Escanea para agendar 👇» en vez de «…en SD-703.» / «Escanea…» (métrica de fuente de Robolectric, igual que M01/M00a/M03). Intent de compartir: pendiente de prueba en dispositivo (compartir una imagen desde la galería a «Alarmas QR» no se puede probar en este entorno) |
| M04 | 4:189 | ok · pixel-perfect a pantalla completa (Tarea 14: captura del flujo T1, `regla.capturar("M04")` tras leer el QR de `e-entrega` desde M02 real) — ver nota M04 actualizada abajo |
| M04d | 4330:1432 | ok · pixel-perfect a pantalla completa (Tarea 14: captura del flujo T1, `regla.capturar("M04d-flujo")` con el diálogo «¿Eliminar alarma?» abierto sobre la hoja M04 y la lista M02, copiada como `M04d.png`) — ver nota M04 actualizada abajo |
| M05 | 4:223 | ok · diferencias aceptadas: 5ª alarma «Asado del semillero» / LUNES 31 (decisión D3, el marco solo muestra 4); snackbar «Alarma guardada · También en Google Calendar» con «Deshacer · 5 s» sobre el FAB, que es la colocación por defecto del `Scaffold` de Material 3 (el snackbar se ubica siempre encima del FAB cuando hay uno, con un hueco de ~16 dp; el marco los separa 60 dp) — ver nota M05 abajo. La tarjeta «Entrega de proyecto UX» muestra los chips «Nueva» y «✓ Escaneada» juntos (el marco solo muestra «Nueva»): ambos chips vienen de `dataset.json` (`a-entrega.chips`), fuente única de datos que esta tarea no puede tocar. El texto del snackbar parte en «Alarma guardada · También en» / «Google Calendar» (2 líneas) en vez de 1 línea (métrica de fuente de Robolectric, igual que M01/M03b). El resto (barra, agrupadores, tarjetas de HOY/MAÑANA, borde Verde Texto de la tarjeta nueva, FAB) alinea con el marco |

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

**Nota M04 (Tarea 12):** `M04-contenido.png` es la hoja `M04AlarmaCreadaSheet` sola (sin el marcador de escena de hoja),
así que le faltan la asa, la forma r24 de la hoja y el velo Tinta 55 % de `HojaInferiorSceneStrategy` — se compara
contra `M04-figma.png` (marco 4:189) solo por los bloques internos (título + sello, chip «Datos leídos del QR —
verifícalos», tarjeta del evento, bloque SONARÁ con 3:15 pm y «Editar», nota de Google Calendar, «Listo» y el enlace
de descarte), que alinean con el marco. La captura de pantalla completa (con la hoja, el velo y la lista de fondo)
queda para la Tarea 14, cuando el flujo real las monta desde `EntradasApp`. `M04d.png` **no muestra el diálogo**:
`DialogoConfirmacion` se abre en su propia ventana Android (`Dialog(...)`, necesaria para cubrir también la hoja
inferior de M04), y el ayudante `capturar()` solo dibuja el `decorView` de la actividad de prueba (ver comentario de
`Verificacion.kt`), que es una ventana distinta; `M04d.png` queda entonces idéntico a `M04-contenido.png` (la hoja
detrás, sin el diálogo). El contenido del diálogo (título, cuerpo con el mensaje de `mensajeEliminar`, «Conservar» y
«Eliminar») se verificó con `onNodeWithText`/`assertIsDisplayed` en la prueba y se comparó a ojo contra `M04d-figma.png`
(marco 4330:1432) a partir de esas aserciones y del `DialogoConfirmacion` ya verificado en su propia prueba; la
captura de píxeles del diálogo mismo también queda para la Tarea 14 (o para una captura dedicada de `DialogoConfirmacion`
con el diálogo en primer plano). Aparte, se descubrió durante esta tarea que `regla.onNodeWithTag("velo").performClick()`
—que toca por defecto el centro geométrico de «velo»— falla bajo Robolectric quedando sin efecto cuando el cuerpo del
diálogo es largo (como el `mensajeEliminar` real), porque ese centro coincide con el diálogo centrado por `Box` y
Robolectric no enruta el toque sintético al velo sino al diálogo (que lo absorbe); con un cuerpo corto (como en
`DialogoConfirmacionTest`) el mismo `performClick()` sí funciona. Se comprobó que esto es una limitación conocida de
Robolectric con toques sintéticos sobre elementos anidados (no un defecto de `DialogoConfirmacion` ni de M04: un
`performSemanticsAction(SemanticsActions.OnClick)` directo sobre «velo» sí dispara `alSeguro` con el cuerpo largo, y
un dispositivo real no tiene este problema — véase p. ej. robolectric/robolectric#8420 y #9595 para casos similares).
`M04AlarmaCreadaSheetTest` toca en su lugar una esquina del velo (`performTouchInput { click(Offset(10f, 10f)) }`),
claramente fuera del diálogo, para probar el mismo comportamiento («tocar el velo equivale a la acción segura») sin
depender de esa zona; el comentario en el test documenta la razón.

**Nota M04 · actualización (Tarea 14):** `capturar()` ahora compone todas las ventanas Android visibles (no solo el
`decorView` de la actividad de prueba): lee `android.view.WindowManagerGlobal.getInstance().mViews` por reflexión
(sin shadow público en Robolectric 4.16) y dibuja cada raíz de ventana, en el mismo orden en que Android las agrega
(que es también su z-order), trasladada a su posición real en pantalla. Con esto, `FlujosPersonaATest` (T1) navega
por las entradas reales desde M02, llega a M04 vía `agregarDesdeEvento("e-entrega")` y captura `M04.png`: la hoja
`M04AlarmaCreadaSheet` completa —asa, forma r24, velo Tinta 55 %— compuesta sobre la lista M02 atenuada detrás,
pixel-perfect contra `M04-figma.png`. La misma prueba abre el diálogo (`eliminar` → `dialogoAbierto`) y captura
`M04d-flujo.png` (copiada a `docs/verificacion/M04d.png`): el `Dialog` de `DialogoConfirmacion`, con su propio velo
Tinta 55 % dimiendo también la hoja M04 detrás, aparece ahora en la captura — pixel-perfect contra `M04d-figma.png`
(marco 4330:1432), incluido el degradado doble (velo de la hoja + velo del diálogo) que ya predecía el marco.
`M04-contenido.png` y el `M04d.png` anterior (solo contenido, de la Tarea 12) se conservan sin cambios como
referencia de los bloques internos verificados por aserción; `M04.png`/`M04d.png` en `docs/verificacion/` son ahora
las capturas de pantalla completa de esta tarea. La limitación de `performClick()` sobre «velo» con un diálogo largo
(nota original de la Tarea 12, arriba) sigue vigente: no la corrige la compositura multi-ventana, es un problema de
enrutado de toques sintéticos de Robolectric, no de qué se pinta.

**Nota M05 (Tarea 13):** `M05.png` es la salida real de `regla.capturar("M05")` en `M05GuardadaTest`, montando
`NavegacionApp` completo con `entradasApp`, así que incluye la barra superior, la lista con la alarma nueva, el
snackbar y el FAB, y la barra inferior. Se verificó por muestreo de píxeles que el snackbar (fondo Tinta) queda por
encima del FAB, con un hueco de ≈19 dp entre ambos: es la colocación por defecto del `Scaffold` de Material 3
(siempre ubica el snackbar encima del FAB cuando hay uno, calculado a partir de la altura real del FAB; no es
configurable sin un `Scaffold` a medida, fuera de alcance) — el marco de Figma los separa 60 dp en vez de los ~16 dp
resultantes, diferencia aceptada en el plan. La tarjeta «Entrega de proyecto UX» muestra dos chips («Nueva» y
«✓ Escaneada») porque `dataset.json` ya trae ambos en `alarmas[].chips` para `a-entrega` (la entrada `esNueva: true`
que `agregarDesdeEvento` activa al escanear); el marco solo dibuja «Nueva». `dataset.json` es la única fuente de
datos simulados (CLAUDE.md, «Datos») y esta tarea no la modifica ni toca `TarjetaAlarma.kt` (fuera del alcance de
archivos de la Tarea 13), así que se documenta como diferencia aceptada en vez de «corregirse». El texto del
snackbar (`mensajes.alarmaGuardada`) parte en 2 líneas en `M05.png` («Alarma guardada · También en» / «Google
Calendar») donde el marco lo deja en 1: es la misma métrica de fuente de Robolectric que ya afecta a M01 y M03b, no
un defecto de `SnackbarDeshacer.kt`.

**Fix round 1 (revisión de la Tarea 13):** `RepositorioDataset.deshacer()` revertía el snapshot completo de
`anterior`, y `cambiarEstado` (el interruptor de la tarjeta) mutaba `_alarmas` sin tocar `anterior`; en M05, tocar el
interruptor de otra alarma durante los 5 s y luego pulsar «Deshacer» revertía también ese toque, no solo el guardado.
`cambiarEstado` ahora aplica el mismo cambio a `anterior` (cuando existe), así «Deshacer» solo revierte la mutación
que abrió la ventana. Además, la ventana de «Deshacer» no se cerraba al vencer los 5 s (`anterior` seguía con
valor), así que volver a entrar a M05 (p. ej. tras editar en M06 y volver) volvía a disparar el efecto y mostraba el
snackbar de nuevo, permitiendo deshacer mucho después de los 5 s reales; la entrada de M05 en `EntradasApp.kt` ahora
guarda un `mostrado` (`rememberSaveable(clave.id)`) para mostrar el snackbar una sola vez por alarma, y llama a la
nueva `RepositorioDataset.olvidarDeshacer()` (vía `M02InicioViewModel.olvidarDeshacer()`) cuando la ventana vence sin
que se toque «Deshacer», cerrándola de forma explícita. `M05.png`/`M05-figma.png` no cambiaron con este fix (no es un
cambio visual).

**Nota M02h (Tarea 14):** hasta esta tarea, M02h no tenía captura porque solo existía como composable aislado sin
una entrada real que la abriera sobre la lista. `FlujosPersonaATest` (T6) monta `entradasApp` desde M02, mantiene
presionado el FAB 500 ms (`performSemanticsAction(SemanticsActions.OnLongClick)`, ya que Robolectric no simula el
temporizador real de un toque largo) y captura `M02h.png` con la hoja «Agregar evento» ya compuesta —por el mismo
`capturar()` multi-ventana de la nota M04 de arriba— sobre la lista M02 atenuada por el velo Tinta 55 %; pixel-perfect
contra `M02h-figma.png` (marco 4019:3139). Diferencias aceptadas, iguales que M02: la tarjeta «Reunión semillero»
visible al fondo trae «evento 6:20 pm» de `dataset.json` en vez de 6:10 pm del marco, con su chip «✓ Escaneada»
(también del dataset) asomando en el borde inferior de la captura.
