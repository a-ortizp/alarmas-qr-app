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
| M02h | 4019:3139 | ok · pixel-perfect a pantalla completa (Tarea 14: captura del flujo T6, `regla.capturar("M02h")` tras mantener presionado el FAB desde M02 real): la hoja «Agregar evento» compuesta sobre la lista M02 atenuada por el velo Tinta 55 % alinea con el marco (título, primario amarillo «Escanear el QR del evento», los dos secundarios y la nota final). Diferencias aceptadas, iguales que M02: la tarjeta «Reunión semillero» visible al fondo muestra «evento 6:20 pm» (valor de `dataset.json`) en vez de 6:10 pm del marco, con su chip «✓ Escaneada» (también del dataset) asomando en el borde inferior. **Fix round de revisión final:** el asa ya tiene su relleno de 12 dp (`Espacio.HojaSuperior`) y el título su gap de 6 dp (`Espacio.GapHoja`) antes de este; y el icono «Escanear» de la fila primaria ya tiene el cuadro relleno central de Figma (antes una línea horizontal) — ver nota de icono abajo |
| M12 | 6:87 | ok · pixel-perfect (visor apagado, título a 2 líneas, tarjeta «ACTÍVALA EN 3 PASOS», «Abrir ajustes» y las dos alternativas ancladas abajo alinean con el marco; banda de textura atenuada al 50 % verificada por muestreo de píxeles, sin diferencias de recorte de línea). **Fix round de revisión final:** el icono del visor apagado (`Iconos.Escanear`, `IconoVisor` = 48) ya muestra el cuadro relleno central de Figma en vez de la línea horizontal anterior — ver nota de icono abajo |
| M13 | 6:122 | ok · pixel-perfect (sello «!», título, cuerpo, tarjeta «QUÉ DETECTAMOS» con el chip y el código Spline Sans Mono, «Volver a escanear», «Crear el evento a mano» y el enlace subrayado alinean con el marco; banda de textura atenuada al 50 % verificada por muestreo de píxeles, sin diferencias de recorte de línea) |
| M03 | 4:135 | ok · esquina del marco de enfoque corregida (fix round 1): ahora es un corchete redondeado (tramo recto + arco de 90° de `Medidas.RadioEsquinaEnfoque` = 20 dp, medido contra el marco de Figma por muestreo de píxeles) en vez de los dos segmentos rectos con remate de radio pequeño de la primera versión; comparado a ojo y en zoom contra `M03-figma.png`, el radio y la forma del corchete ya coinciden. Diferencia aceptada: título «Escanear QR» algo más grueso que en Figma (métrica de fuente de Robolectric, igual que M01/M00a). El resto (barra Tinta, chip «Linterna · auto», textos del visor, hoja blanca r24 con los dos secundarios y el asa) alinea con el marco. Cámara real: pendiente de prueba en dispositivo (no se puede probar en este entorno). **Fix round de revisión final:** `M03.png` se captura en `M03EscanerScreenTest` con `tienePermiso = false` (visor apagado, D5), y desde este fix el chip «● Cámara activa» ya no se dibuja sin permiso real (antes se mostraba igual, mintiendo sobre el estado de la cámara) — por eso `M03.png` ya no trae ese chip que sí aparece en `M03-figma.png`; el chip sí aparece con el permiso real concedido, verificado por separado en `FlujosPersonaATest` (T1, `onNodeWithText("● Cámara activa").assertIsDisplayed()` tras `abrir-ajustes`) |
| M03b | 4020:3295 | ok · pixel-perfect (chip «✓ QR detectado», título, burbuja de WhatsApp con «Grupo MISO UX · hoy 8:12 am», el marco de lectura con el QR, «Origen: WhatsApp · compartido con Alarmas QR», «Continuar», «Elegir otra imagen» y la nota final alinean con el marco). Diferencia aceptada: el mensaje parte en «Nos vemos el domingo 30 en» / «SD-703. Escanea para agendar 👇» en vez de «…en SD-703.» / «Escanea…» (métrica de fuente de Robolectric, igual que M01/M00a/M03). Intent de compartir: pendiente de prueba en dispositivo (compartir una imagen desde la galería a «Alarmas QR» no se puede probar en este entorno) |
| M04 | 4:189 | ok · pixel-perfect a pantalla completa (Tarea 14: captura del flujo T1, `regla.capturar("M04")` tras leer el QR de `e-entrega` desde M02 real) — ver nota M04 actualizada abajo. **Fix round de revisión final:** el asa ya tiene su relleno de 12 dp (`Espacio.HojaSuperior`) y el título «¡Alarma programada!» su gap de 12 dp (`Espacio.EntreBloques`) antes de este |
| M04d | 4330:1432 | ok · pixel-perfect a pantalla completa (Tarea 14: captura del flujo T1, `regla.capturar("M04d-flujo")` con el diálogo «¿Eliminar alarma?» abierto sobre la hoja M04 y la lista M02, copiada como `M04d.png`) — ver nota M04 actualizada abajo. **Fix round de revisión final:** el mismo relleno del asa de M04 se ve también detrás del diálogo |
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
`M04-contenido.png` se conserva como referencia de los bloques internos verificados por aserción; el `M04d.png` de
solo contenido de la Tarea 12 no se conserva aparte, porque `M04.png`/`M04d.png` en `docs/verificacion/` pasan a ser,
desde esta tarea, las capturas de pantalla completa (la hoja/el diálogo con el velo y la lista de fondo). La limitación de `performClick()` sobre «velo» con un diálogo largo
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

**Fix round de revisión final (hallazgos 3 y 6 del informe de revisión completa):**

- **Espaciado del asa y del título de las hojas (hallazgo 3):** `HojaInferiorSceneStrategy` no le daba relleno al asa
  (`dragHandle = { Asa() }`), y el primer hijo de `M02hAgregarEventoSheet`/`M04AlarmaCreadaSheet` no dejaba gap antes
  del título; el asa quedaba pegada al techo redondeado de la hoja, distinto del marco de Figma. Ahora
  `dragHandle = { Asa(Modifier.padding(top = Espacio.HojaSuperior)) }` (12 dp) y el `Column` raíz de cada hoja añade
  `top = Espacio.GapHoja` (M02h, 6 dp) o `top = Espacio.EntreBloques` (M04, 12 dp). `M02h.png`, `M04.png` y `M04d.png`
  (este último hereda el fondo de M04) se regeneraron con `FlujosPersonaATest`; `M04-contenido.png` también, porque
  usa el mismo `Column`. Las tres siguen pixel-perfect contra su marco de Figma.
- **Icono «Escanear» (hallazgo 6):** `Iconos.Escanear` dibujaba una línea horizontal en el centro del marco de cuatro
  esquinas; el componente de Figma «icono · escanear» (`4006:51`, archivo `4nHD4ygcnP33UH0gAhaii5`, capturado con
  `mcp__plugin_figma_figma__get_screenshot` y medido por muestreo de píxeles del PNG) tiene en su lugar un cuadro
  relleno de 8×8 con esquinas levemente redondeadas, centrado en (12,12) del viewport de 24. Se redibujó ese path
  (`icono()` ahora acepta un `relleno` opcional, con `fill = SolidColor(Color.Black)`, que `Icon(tint = …)` recolorea
  igual que los trazos) manteniendo las cuatro esquinas sin cambios. Afecta `M02h.png` (fila primaria «Escanear el QR
  del evento») y `M12.png` (visor apagado, `IconoVisor` = 48); ambas se regeneraron y siguen pixel-perfect. `M03.png`
  no usa este icono (confirmado al revisar `Iconos.Escanear` solo en `FabEscanear`, `M02hAgregarEventoSheet` y
  `M12PermisoCamaraScreen`) y el FAB de M02 no aparece en la captura plana de M02, así que ninguna otra captura cambia
  por este fix.
- **Chip «● Cámara activa» solo con permiso (hallazgo 7, minor):** `M03.png` se captura en `M03EscanerScreenTest` con
  `tienePermiso = false`; antes el chip se dibujaba igual (mintiendo sobre el estado de la cámara apagada, D5), ahora
  no. `M03.png` se regeneró y por eso ya no trae ese chip que sí aparece en `M03-figma.png` — diferencia aceptada,
  documentada en la fila de M03 de la tabla; el chip con permiso real concedido se verifica por separado en
  `FlujosPersonaATest` (T1).

## Web · Plan 3 (Persona A)

Parejas a 1280×820: `<nombre>-figma.png` (exportada con `get_screenshot` del MCP de Figma, `maxDimension` 1280) y `<nombre>.png`. Esta última se captura con Chrome sin interfaz (`--window-size=1280,820 --virtual-time-budget`) sobre `ng serve`, o con Playwright MCP en los estados que requieren interacción (ver `docs/superpowers/plans/2026-09-21-plan3-web-persona-a.md`, Tarea 8). Diferencias aceptadas por decisión del plan: campos de 48 con etiqueta 12 y placeholder Gris Medio (D3), botones de 14.5 (D5), snackbar centrado en la ventana (D7) y «¿Olvidaste tu contraseña?» también en los estados con snackbar (D8).

Nota de captura (2026-09-21): `google-chrome --headless=new --window-size=1280,820` deja un viewport de 1280×733 (la ventana descuenta la interfaz del navegador), así que la tarjeta de W00 salía 44 px más arriba y la franja inferior en blanco. Por eso las diez capturas finales se tomaron con Playwright MCP con el viewport fijado a 1280×820 (`page.setViewportSize`), esperando `document.fonts.ready` y con el puntero fuera de los controles. En los marcos con aviso se captura a menos de 1,2 s de la carga o del toque, antes de los 3 s del snackbar. Chrome a escala 1 dibuja los bordes de 1.5 como 1 px (Figma los antialiasa a 1.5); es métrica del navegador, no una diferencia de diseño.

| Nombre | Marco Figma | Estado |
|---|---|---|
| W00 | 4072:1861 | ok · diferencias aceptadas: tarjeta 520×442 frente a 520×439 por los campos de 48 (D3), placeholder en Gris Medio en vez de `#DAD8D2` (D3), etiqueta 12 (D3) y «Iniciar sesión» en 14.5 (D5). Logotipo amarillo junto al primario (D11). |
| W00-error | 4362:474 | ok · corregido en la Tarea 8 (el mensaje de error usaba `--text-nota`, 12.5/1.4, caja de 17.5; ahora `--text-error-campo-web`, 12.5/normal, caja de 14 como el marco). Tarjeta 520×468 frente a 520×465. Diferencias aceptadas: campos de 48 y etiqueta 12 (D3), botón 14.5 (D5). Borde Coral Texto y mensaje en Coral Texto bajo el campo, como en el mockup (color + mensaje). El error se enciende con el primer foco en la contraseña (D8). |
| W00-recuperar | 4362:427 | ok · diferencias aceptadas: campo de 48, etiqueta 12 y placeholder Gris Medio (D3), «Enviar enlace» 14.5 (D5). Subrayado de «‹ Volver a iniciar sesión» a la altura «from-font» del navegador, 1 px más arriba que en Figma (métrica de fuente). |
| W00-correo-enviado | 4362:449 | ok · diferencias aceptadas: snackbar centrado en la ventana (x 410.7) en vez de x 509 (D7); «¿Olvidaste tu contraseña?» presente (D8), por lo que la tarjeta mide 442 en vez de 374 y queda más arriba; campos de 48 (D3). Snackbar 36 de alto a 24 del borde inferior, igual que Figma. |
| W00-eliminada | 4072:1878 | ok · diferencias aceptadas: «¿Olvidaste tu contraseña?» presente (D8) y campos de 48 (D3). Snackbar «Cuenta eliminada exitosamente» en (509, 760) 262×36, idéntico al marco. |
| W06 | 4072:233 | ok · corregido en la Tarea 8 (iconos y logotipo de las barras no se veían; relleno de las tarjetas medido desde el borde interior). Tarjetas «Privacidad» 429×211 y «Eliminación de cuenta» 429×154 idénticas al marco. Diferencias aceptadas: «Perfil» 561×316 frente a 561×310 por los tres campos de 48 (D3), etiqueta 12 (D3); título «Ajustes de Perfil» 1,4 px más bajo por el interlineado 1.15 del token `--text-h1-web` (Figma «normal»; pendiente, ver abajo). |
| W06-actualizado | 4072:279 | ok · diferencias aceptadas: las de W06 y el snackbar «Perfil actualizado» a 24 del borde inferior (Figma 22) centrado en la ventana (D7); mismo ancho 171 y alto 36. |
| W06-eliminar | 4072:256 | ok · diferencias aceptadas: modal 481×393 frente a 481×386 por el campo ELIMINAR de 48 (Figma 40) (D3); el campo recibe el foco al abrir (`cdkTrapFocus`, D6), por eso su borde va en Tinta, mientras que el marco lo dibuja en reposo; botones 14.5 (D5). Sin miga + ✕ según el mockup (D2); velo 45 %. |
| dialogo-cerrar-sesion | 4360:347 | ok · pixel-perfect en el diálogo: 420×241.6 en (430, 289) frente a 420×242 en (430, 289). Diferencias aceptadas: botones 14.5 en vez de 14 (D5), sombra en Tinta (D10), anillo de foco visible en «Cancelar» porque recibe el foco inicial (accesibilidad de teclado) y el fondo es el marcador W01 de la Persona B. |
| barra-colapsada | 4357:1868 | ok · pixel-perfect en la barra lateral y la barra superior: control 36×36 en (12, 84), píldoras 40×40 en y = 124, 168, 212, 256 y «Cerrar Sesión» en y = 760, idénticas al marco. El contenido es el marcador W01 de la Persona B. |

Pendiente de decisión (no se corrigió por ser un token compartido del DS): `--text-h1-web` usa interlineado 1.15 (caja de 27.6) y los marcos web usan «normal» (caja de 29), así que el contenido bajo el título de W06 queda 1,4 px más arriba. Cambiarlo afecta también los títulos de W01–W05 de la Persona B.
