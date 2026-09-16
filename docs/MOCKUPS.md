# Alarmas QR · Mockups de alta fidelidad (app móvil y web)

Propuestas de pantalla finales (alta fidelidad) de la app móvil, construidas a partir del prototipo de navegación corregido (wireframes Figma, un solo recorrido desde M01) y del Style Tile «Energía puntual». Sirven de base al prototipo de interacción para la tercera ronda de pruebas de usabilidad (problemas de comprensión y navegación que no aparecen en blanco y negro).
Fecha: 2026-09-07 · v1.1/v1.2/v1.3 del 2026-09-08 (dos rondas de crítica de diseño) · v1.4 del 2026-09-14 · **mockups web** del 2026-09-14 en la página `03 · Web` del mismo archivo (autoría de mmatallanar-ua, ver §7) · Insumos: `NAVEGACION.md` §6, `STYLE_TILE.md`, `DESIGN_SYSTEM.md`, `CONCLUSIONES_PRUEBAS.md`.

## 1 · Entregables

| Entregable | Dónde |
|---|---|
| Archivo Figma «Mockups Alarmas QR Equipo UX» (equipo MISO-UX, cuenta Uniandes) | https://www.figma.com/design/4nHD4ygcnP33UH0gAhaii5/Mockups-Alarmas---QR-Equipo-UX |
| Prototipo de interacción publicado (abre en M01) | https://www.figma.com/proto/4nHD4ygcnP33UH0gAhaii5/Mockups-Alarmas---QR-Equipo-UX?node-id=3-71&p=f&scaling=min-zoom&content-scaling=fixed&page-id=1%3A6&starting-point-node-id=3%3A71&show-proto-sidebar=1 |
| PDF con las 19 pantallas (16 del recorrido + M02v, M02h y M03b; vectorial, 390×844 pt, un marcador por pantalla) | `Mockups_Figma_Movil.pdf` |
| Versión web de este documento con las pantallas ampliables (artefacto, 2026-09-14) | https://claude.ai/code/artifact/ad2b46ab-7215-4aab-9e19-9f0ca0aa92cd |
| **Mockups web** · prototipo publicado (abre en W00, página `03 · Web`) | https://www.figma.com/proto/4nHD4ygcnP33UH0gAhaii5/Mockups-Alarmas---QR-Equipo-UX?node-id=4072-1861&p=f&scaling=min-zoom&content-scaling=fixed&page-id=4072%3A2&starting-point-node-id=4072%3A1861&show-proto-sidebar=1 |
| **Mockups web** · PDF con los 14 marcos (1280×820, vectorial, un marcador por pantalla; web v1.1) | `Mockups_Figma_Web.pdf` (sustituye a `Alarmas QR - Mockups Web.pdf`, la exportación inicial de mmatallanar-ua) |

El archivo es un **duplicado del archivo de wireframes** (opción «duplicar los wireframes y editar los componentes» del enunciado): conserva los 16 frames de la página `02 · Móvil` con su retícula de 4 columnas (margen 20, medianil 12), las 59 conexiones de prototipo y el único punto de inicio «Flujo principal · recorrido completo» en M01. La página `03 · Web` se duplicó con el archivo y el 2026-09-14 fue **rehecha por mmatallanar-ua** con 14 marcos de 1280×820 en el mismo lenguaje «Energía puntual» (ver §7).

## 2 · Pasos del enunciado y cómo se cumplieron

| Paso | Cumplimiento |
|---|---|
| 1 · Documento nuevo | Duplicado del archivo de wireframes, renombrado «Mockups Alarmas QR Equipo UX». |
| 2–4 · Mapa de navegación + wireframes; duplicar o rehacer | Se duplicó: cada uno de los 16 wireframes se re-estilizó en sitio sin borrar controles con conexiones. |
| 5 · ¿Faltan pantallas? | Tras la crítica de diseño del 2026-09-08 se agregaron tres: **M02v** (inicio sin alarmas, primer uso), **M02h** (hoja inferior «Agregar evento», única entrada de captura) y **M03b** (pantallazo recibido desde WhatsApp/galería). Total 19. |
| 6 · Columnas / cuadrícula | Layout grid de 4 columnas heredada de los wireframes en las 16 pantallas; márgenes de 20 px y alturas de control 44/48 sobre la retícula. |
| 7 · Design System personalizado | Base Material 3 re-tematizada con «Energía puntual» (ver `DESIGN_SYSTEM.md`): botones píldora, tarjetas r14, switch, chips, barra inferior, snackbar, banner. |
| 8–9 · Seleccionar y ubicar componentes | Colores, tipografías y anatomía copiados de los componentes del archivo «Design System Alarmas QR Equipo UX» (botón primario #FFC400 / Archivo Bold, secundario contorno Tinta, destructivo contorno Coral, FAB r16, chip «Nueva» amarillo, «Escaneada» verde, tarjeta «recién guardada» con borde verde…). |
| 10 · Textos finales | Copy de los wireframes ya validado en las pruebas CM-01…CM-17 («Crear el evento a mano», «Sonará 3:15 pm» en línea propia, etc.). |
| 11–12 · Alineación y ortografía | Verificación visual pantalla por pantalla; un desborde corregido (enlace largo de M13). |
| 13 · Links de navegación | 67 conexiones (59 heredadas + las de M02v/M02h/M03b), todas en el marco del control (regla del 2026-09-06); sin destinos rotos. |
| 14–15 · Animaciones y micro-interacciones | Todas las conexiones pasaron de transición instantánea a **Smart Animate 250 ms (ease in-out)**: la barra superior, las tarjetas y los botones se desplazan/morfan entre pantallas; el switch, el FAB y los chips cambian de color en lugar de saltar. |
| 16 · Link de publicación | Enlace del prototipo en la tabla anterior (requiere que el archivo esté compartido «cualquiera con el enlace · puede ver»). |
| 17 · PDF | `Mockups_Figma_Movil.pdf`, export vectorial frame por frame. |

## 3 · Capas de comprensión

El enunciado pide fondos de color o texturas (2º nivel) y recursos gráficos (3º y 4º nivel) en **todas** las pantallas. Todos los recursos son vectoriales y propios (Style Tile §4.4–4.6: identidad 100 % vectorial, sin fotos), guardados como componentes en la página `00 · Recursos gráficos` del archivo:

| Recurso | Uso |
|---|---|
| `textura · módulos QR / tinta` y `/ blanco` | Retícula de módulos QR que se desvanece hacia abajo, opacidad máxima 8 %. Una instancia como primer hijo (absoluto) del área de contenido de **cada** pantalla; la variante blanca en las pantallas oscuras y en el visor de M01. |
| `código QR · evento` | QR verosímil de 21×21 módulos (patrones de posición y temporización) que reemplaza al tablero de ajedrez del wireframe en M01, M03, M07 y M08. |
| `ilustración · esfera de reloj`, `diana QR`, `destello` | Cluster de bienvenida en M01; destellos en el visor de M01 y en la pieza QR de M08. |
| `ilustración · alarma sonando` | Esfera con ondas coral detrás de la hora 4:45 en M10 (opacidad 28 %). |
| `ilustración · cámara` | Visor apagado de M12. |
| `ilustración · advertencia`, `check` | Disponibles para variantes; en M13 y M04 se usó el círculo del wireframe recoloreado (coral / verde). |
| `icono · alarma / calendario / ajustes / escanear / galería / más / atrás / compartir` + chat y correo | Íconos de línea 24 px, trazo 2 px, terminales redondeadas. Barra inferior de M02, M02b, M05 y M11; compartir en M08. |

**Fondos por pantalla** (decisión del usuario: «claro con momentos plenos»):

| Pantalla | Fondo (2º nivel) | 3º–4º nivel |
|---|---|---|
| M01 Bienvenida | Amarillo Energía pleno + banda de textura tinta (120 pt) | Visor Tinta con QR blanco y destellos; diana QR como marca |
| M00a / M00b | Blanco Papel (sin textura desde la v1.2) | Marca (diana amarilla), enlaces azules |
| M02 / M02b / M05 / M11 | Blanco Papel (sin textura desde la v1.2) | Íconos de la barra inferior, píldora activa Gris Niebla, switches Tinta, chips semánticos |
| M03 Escáner | Tinta pleno (sin textura desde la v1.2) | Esquinas del visor en Amarillo Energía, QR verosímil, hoja inferior blanca con dos botones secundarios de contorno (v1.4) |
| M04 / M09 | Blanco Papel + textura tinta | Tarjeta del evento con sombra, destacado Amarillo Suave, sello «✓ verificado» verde |
| M06 / M07 | Blanco Papel (sin textura desde la v1.2) | Selectores segmentados, switches Tinta, QR en nota |
| M08 Compartir | Blanco Papel (sin textura desde la v1.2) | Pieza QR con sombra, íconos de chat/correo/compartir |
| M10 Alarma sonando | Tinta pleno + banda de textura blanca | Ilustración de alarma sonando (pequeña, arriba), hora en Amarillo Energía 96 pt, chip coral, dos acciones de 56 pt |
| M12 / M13 | Blanco Papel + textura tinta | Cámara plana / círculo de advertencia coral, tarjeta de diagnóstico coral |

## 3b · Mejoras aplicadas tras la crítica de diseño (2026-09-08, v1.1)

Crítica con la skill Impeccable (`critique`, doble evaluación: revisión de diseño + detector determinista). Puntaje inicial 23/36 (heurísticas de Nielsen, H10 n/a). Se ejecutaron los diez pasos del plan: 1–5 en la v1.1 y 6–10 en la v1.2 (mismo día).

| Paso | Hallazgo (severidad) | Qué se cambió |
|---|---|---|
| 1 | **P0** · M10 sin acción inequívoca para detener la alarma | Dos acciones grandes equivalentes de 56 pt ancladas abajo: «Ya voy · ver ruta» (amarillo) y «Posponer 10 min ›» (contorno blanco). Se retiran el selector de posponer y el enlace «Silenciar solo esta vez». Hora «4:45 pm» con sufijo; anillo de reloj retirado de detrás de los dígitos y llevado arriba como ilustración pequeña; nota inferior a 13 pt blanco 70 %. |
| 2 | **P1** · M09 contradecía el ajuste «Confirmar antes de auto-ajustarse» | Bloque héroe «SI ACEPTAS, SONARÁ · 4:45 pm · 30 min de margen + trayecto · antes sonaba 3:15 pm». Botones «Aceptar el cambio» / «Mantener mi alarma de 3:15 pm»; «Eliminar alarma» sale del cuerpo (sigue en M06); «×» de cierre en la barra → M02. |
| 2 | **P2** · M04 con el destructivo junto a «Listo» | «Eliminar alarma · no puedo asistir» pasa a enlace gris «No puedo asistir · eliminar alarma» bajo «Listo». |
| 3 | **P2** · La hora calculada no era el héroe | Bloque «SONARÁ · 3:15 pm» en Spline Sans Mono Bold 52 (M04) y 4:45 pm (M09) sobre Amarillo Suave; horas en tarjetas a 28 pt con sufijo am/pm de 14 pt; columna central de las tarjetas en FILL con títulos que envuelven. |
| 4 | **P1** · M02 con tres accesos de creación y dos primarios | Se retiran los dos botones superiores; un solo **FAB extendido «Escanear»** (M02, M02b, M05) que abre la hoja **M02h «Agregar evento»** con tres opciones: Escanear el QR (→ M12 ⏩), Elegir pantallazo (→ M03b), Crear a mano (→ M07), más el aviso «comparte un pantallazo desde WhatsApp o la galería». **M02v** estado vacío del primer uso (diana QR + «Aún no tienes alarmas» + «Escanear QR del evento»), al que llegan «Crear cuenta» y «Continuar como invitado» de M00a; «Entrar» de M00b sigue yendo a la lista poblada. **M03b «Pantallazo recibido»**: vista previa del mensaje de WhatsApp con el QR enmarcado en amarillo, chip «✓ QR detectado», «Leer el QR» → M04. |
| 5 | **P1** · Formato horario y datos rotos en el recorrido | Un solo formato 12 h con «am/pm» en Spline Sans Mono Medium (M02, M02b, M05, M06, M10). Un solo dataset: la alarma de las 7:30 am pasa a llamarse «Reunión con el tutor» (antes duplicaba el nombre del evento escaneado), «Vuelo BOG–MDE» deja de ser «Nueva», y M05 muestra las cuatro alarmas de M02 más la nueva «Entrega de proyecto UX · 3:15 pm · Nueva». Chip de M06 corregido a «✓ Escaneada»; sección «Alarma conectada» renombrada «Cambios del organizador». Botones con relleno o contorno a 48 pt (20 botones). Chip «● Cámara activa» en el visor de M03. |

| 6 | **P2** · Amarillo en estados y decoración (5 amarillos en M06) | **Acento estricto** (decisión del usuario): un solo elemento amarillo por pantalla. 16 switches activos pasan a Tinta con perilla blanca, 6 píldoras de pestaña activa a Gris Niebla, chip «Nueva» y numerales de M12 a Tinta/blanco, punto del calendario a Tinta; destello decorativo de M08 retirado; M02v pierde el FAB (ya tiene el primario) y gana el enlace «Elegir pantallazo de la galería». Regla reescrita en `STYLE_TILE.md` §4.1 y `DESIGN_SYSTEM.md` §6. |
| 7 | **P2** · Contraste de los semánticos y escala un escalón abajo | Tonos de texto AA: Coral Texto #C4362E, Verde Texto #0B7048, Azul Texto #1A5BC4, Gris Texto #66636D (161 rangos de texto y 16 trazos de chips/botones); sobre Tinta el texto secundario va en Gris Borde; placeholders a Gris Medio. Escala: 132 textos Archivo subidos (nada por debajo de 12 pt salvo la barra inferior; descripciones y agrupadores a 13). Un desborde corregido (nota de M11). `<meta charset>` añadido a los dos HTML del Style Tile y los wireframes. |
| 8 | **P2** · Textura en 16/16 pantallas y vacíos con ilustraciones de relleno | Textura solo en M01, M04, M09, M10, M12 y M13 como banda recortada de 120 pt (13 instancias retiradas de listas, formularios, visor y hojas). M01 pierde la esfera de reloj; la diana QR queda centrada como marca. |
| 9 | Design System como fuente de verdad | Archivo Figma del DS actualizado: barra superior en Bricolage 22 en ambas variantes, switch activo Tinta, píldoras de navegación Niebla, chip «Nueva» Tinta y «✓ Escaneada» en Verde Texto, destructivo y enlace en tonos de texto, FAB extendido «Escanear» con ícono, 4 estilos de color nuevos y **lámina L08 · Complementos v1.1** (comp. 31 acciones de alarma sonando, 32–34 controles sobre Tinta, 35 primario sobre amarillo, 36 hora en tarjeta 28 + 14, muestrario de tonos de texto). `Design_System_Alarmas_QR.pdf` regenerado (8 páginas) y `DESIGN_SYSTEM.md` §6 con la tabla antes/ahora. |
| 10 | Polish | Inspección por lotes de las 19 pantallas tras cada paso (sin desbordes ni destinos rotos), PDF de mockups regenerado (v1.2) y documentación sincronizada. |

Pendiente del paso 5 (harden): estados de error de campo en M00a/M00b/M07 como frames aparte, para no ensuciar el camino feliz del prototipo.

## 3c · Segunda crítica y v1.3 (2026-09-08)

Segunda corrida de `critique` sobre la v1.2: **25/36** (antes 23). Plan de 8 pasos ejecutado completo el mismo día:

| Paso | Hallazgo | Qué se cambió |
|---|---|---|
| 1 | **P1** · Cinco anatomías de tarjeta y sin hora del evento; días de la semana contradictorios | Una sola anatomía en M02, M02b, M02h, M05 y M06: hora 26 + am/pm 12 en línea, título Archivo Bold 15, «evento 8:00 am · Aula SD-703» a 13 en Gris Texto, chip debajo del texto; M06 sin miga de pan. Calendario coherente en agosto 2026: JUE 27, VIE 28, entrega DOM 30, asado LUN 31 (13 textos). M08 «Aún sin escaneos · recién creado». M02h reconstruida sobre el M02 corregido. |
| 2 | **P1** · M10 sin «Detener» puro | «Ya voy» detiene sin salir de la app; «Posponer 10 min» sin chevron; «Ver ruta ›» vive dentro del destacado «Sal en 12 min» (con conexión); chip «Alarma de evento» en Coral Alarma base (4.6:1 sobre Tinta); nota a 14 pt. |
| 3 | **P2** · «Un solo gesto» con tres toques | FAB «Escanear»: toque → cámara (M12 ⏩ / M03); mantener presionado (trigger «Mouse down» con retardo de 0,5 s, el toque largo real de Figma; el toque corto es «Mouse up» sin retardo; hasta el 2026-09-14 era «While pressing» + «On tap», ver §8) → hoja M02h. M03b pasa a confirmación: «QR de evento detectado en tu pantallazo», botón «Continuar», «Elegir otra imagen» como enlace. |
| 4 | **P2** · Plantilla «contenido + hueco + CTA» | En M04, M09 y M13 los botones quedan 32 pt bajo el héroe; M01 sin ilustración de relleno y «Comenzar» junto a los calendarios; M08 con primaria «Compartir por WhatsApp» y la línea «Tu alarma quedó programada · sonará 12:00 pm»; banda de textura al 4 % detrás de los titulares de M04, M09, M12 y M13. |
| 5 | **P2** · M06 destructivo ancho completo; consentimientos pre-marcados | «Eliminar alarma» como enlace Coral Texto bajo «Guardar cambios»; casillas de Ley 1581 (M00a) y Google Calendar (M01) sin marcar; Google/Outlook lado a lado también en M00a; «Mía · QR» → «Creada por mí»; URL de M13 a 12 pt. |
| 6 | **P3** · Amarillo decorativo residual | Destellos del visor de M01 en blanco, destello de M02v retirado, marco del QR de M03b en Tinta, «Deshacer · 5 s» en blanco subrayado, punto del reloj y lente/flash de las ilustraciones en blanco. **Excepción escrita:** sobre Tinta (M10) el primario es blanco para que la hora sea el único amarillo. |
| 7 | **P2** · Documentos detrás de los mockups | DS Figma: L02 (hora 28 + 14, H3 13, am/pm 14), L03 FAB con las tres variantes extendidas (normal · presionado · mantener presionado «Agregar evento…»), L04 campo con etiqueta dentro (comp. 06) y rótulos de switch/píldora, L06 tarjeta 18 con hora 28 + 12, Archivo Bold 15, «evento…», switches y chip en Tinta, L07 estado vacío sin «+», L08 «Ya voy» blanco y descripciones corregidas (sin «Silenciar» ni segmentado para M10). `Design_System_Alarmas_QR.pdf` regenerado. Style Tile: componentes con los tonos de texto, FAB extendido «Escanear» / «Agregar evento…», chip «Creada por mí» y «✓ Escaneada», H3 13, tarjeta con «evento 8:00 am»; HTML, PDF y artefacto republicados (v1.2). |
| 8 | Polish | Inspección por lotes (72 conexiones en 69 nodos, sin destinos rotos, punto de inicio en M01), PDF de mockups regenerado (v1.3) y documentación sincronizada. |

Conexiones del gesto principal (mockups): FAB toque («Mouse up» sin retardo) → M12; FAB mantener presionado («Mouse down» · 0,5 s) → M02h; la hoja conserva sus tres salidas (M12, M03b, M07). M03 «Elegir pantallazo» y M12 «Elegir pantallazo» → M03b → «Continuar» → M04.

## 4 · Reglas de estilo aplicadas (resumen)

- **Color (v1.2):** Amarillo Energía solo en la acción principal de cada pantalla y en la marca; los estados activos (switch, píldora, chip «Nueva») van en Tinta. Coral solo en urgencia; Verde en verificado/escaneado/recién guardada; Azul en enlaces; Gris Niebla en superficies. Como **texto** se usan los tonos AA: Coral Texto #C4362E, Verde Texto #0B7048, Azul Texto #1A5BC4 y Gris Texto #66636D (Gris Borde sobre Tinta).
- **Tipografía:** Bricolage Grotesque Bold 28/22 en títulos de pantalla y barra superior, SemiBold 20–22 en nombres de evento; Archivo en UI y cuerpo (botones Bold 15, agrupadores Bold 12 en mayúsculas con +8 % de tracking); Spline Sans Mono Bold en horas (26 en tarjetas con sufijo am/pm de 12 en línea, 52 en los bloques «Sonará», 96 en la alarma sonando).
- **Forma:** botones píldora de 48 px (44 hasta la v1), tarjetas y campos r14/r12 con borde Gris Borde 1.5, FAB r16 con sombra, snackbar Tinta r12 con «Deshacer» en amarillo.
- **Estados con color + forma:** switch activo Tinta con perilla blanca a la derecha / inactivo blanco con borde y perilla gris a la izquierda; chip «✓ Escaneada» con marca; tarjeta pausada en Gris Niebla y texto gris.

## 5 · Regeneración del PDF

`download_assets` (Figma MCP) con `defaultFormat: "pdf"` por frame (IDs en orden de flujo: 3:71, 3:2, 3:37, 4020:3553 M02v, 3:131, 4019:3139 M02h, 4:2, 4020:3295 M03b, 4:135, 4:189, 4:223, 5:2, 5:60, 5:130, 5:195, 6:2, 6:28, 6:87, 6:122), descarga inmediata con curl (URLs efímeras) y ensamblado con pypdf (`PdfWriter.append` + `add_outline_item`) en orden de flujo: M01, M00a, M00b, M02v, M02, M02h, M02b, M03b, M03, M04…M13.

## 6 · Pendientes

- Tercera ronda de pruebas de usabilidad con el prototipo de interacción en color (tareas T1–T7 de `FUNCIONALIDADES.md`); registrar hallazgos como CM-18… en `CONCLUSIONES_PRUEBAS.md`.
- Mockups web (§7): decidir si la gráfica de escaneos por semana de W01 usa datos reales del organizador o queda como indicador agregado; retirar `Alarmas QR - Mockups Web.pdf` cuando el equipo lo confirme (ya está sustituido por `Mockups_Figma_Web.pdf`).

## 7 · Mockups de la aplicación web (página `03 · Web`)

Construidos por mmatallanar-ua el 2026-09-14 sobre la página `03 · Web` del archivo de mockups, a partir de los wireframes W00–W07 y del Style Tile «Energía puntual». Marco de 1280×820, barra lateral persistente («Mis Alarmas · Reportes · Descargar QR · Ajustes de Perfil · Cerrar Sesión»), cabecera con la marca y el usuario (Andrés Rojas). Prototipo con **un solo punto de inicio en W00** («Inicio», `4072:1861`) y, desde la **web v1.1** (2026-09-14, observaciones de §7.4 aplicadas), **109 conexiones** «On tap» con Smart Animate 250 ms, todas en el marco del control. PDF: `Mockups_Figma_Web.pdf` (14 láminas en el orden de la tabla).

### 7.1 · Marcos

| Marco (id) | Qué muestra | Funcionalidades |
|---|---|---|
| **W00 · Inicio de sesión** (`4072:1861`) | Tarjeta centrada con la marca, «Administración y consulta de tus eventos y alarmas», correo + contraseña, «Iniciar sesión» y la nota «¿Aún no tienes cuenta? Créala desde la app móvil». | F-W00 |
| W00 · Inicio de sesión (cuenta eliminada) (`4072:1878`) | La misma pantalla con el snackbar «Cuenta eliminada exitosamente»; cierre del flujo de F-W08. | F-W00 · F-W08 |
| **W01 · Mis Alarmas** (`4072:26`) | Hub: cuatro indicadores (6 eventos activos · 128 escaneos totales · 97 alarmas activas, conversión 76 % · 41 «Ya voy»), botones «Exportar reporte» (primario) y «Descargar QR en lote» (contorno desde la v1.1), buscador, filtros Próximos / Pasados / Borradores y pestañas Todos / Creados / Escaneados, tabla de eventos con chips «Creada por mí» / «✓ Escaneada», estado «Publicado» y «Ver detalle ›», y (v1.1) tarjeta **«Escaneos por semana»**: 8 barras (6 jul → 24 ago, total 128) con la semana actual en Tinta y las anteriores en Gris Texto. Nota: «Las métricas de eventos escaneados pertenecen a su organizador». | F-W01 · F-W02 · F-W06 |
| W01 · Mis Alarmas (Creados) (`4072:49`) · (Escaneados) (`4072:72`) | Estados de la pestaña: solo «Seminario UX» (creada) o solo «Partido Sintética» (escaneada). | F-W02 · F-W06 |
| **W03 · Detalle Evento** (`4072:95`) | Miga de pan «‹ Mis alarmas / Partido Sintética», título, fecha y lugar, tres indicadores (16 escaneos · 11 alarmas activas, 5 la eliminaron · 7 «Ya voy»), tabla «Quiénes escanearon» con alias (Joale7, Mike1008, J.P, CarrosC6), fecha, estado Activa / Eliminada, buscador y «Ver todos ›»; nota «Solo alias o iniciales — nunca teléfono ni correo (Ley 1581)». | F-W03 |
| **W04 · Modal Exportar Reporte** (`4072:118`) | Modal sobre W01 atenuada: rango (Último mes · Semestre · Rango personalizado), formato PDF / CSV, nota de datos agregados y anónimos, «Cancelar» / «Generar y descargar». | F-W04 |
| W04 · (Rango personalizado) (`4072:141`) · (Listo) (`4072:164`) | Estados del modal: campos de fecha 2026-08-01 → 2026-08-31; confirmación «reporte-alarmasqr-ago2026.pdf generado y descargado exitosamente» con «Generar de nuevo». | F-W04 |
| **W05 · Modal Descargar QR en lote** (`4072:187`) | Modal sobre W01: «Seleccionar todos» (2 de 2), lista de eventos con su QR y estado, (v1.1) bloque **«Vista previa del afiche»** (miniatura con barra de marca Tinta «Alarmas QR», QR del evento, nombre y «Escanéalo y te avisamos» + texto «QR mínimo 4 × 4 cm · PNG 300 ppp o PDF vectorial»), formato PNG / PDF, «Descargar». | F-W05 |
| W05 · (Completado) (`4072:210`) | Snackbar «Descarga completada exitosamente». | F-W05 |
| **W06 · Ajustes de Perfil** (`4072:233`) | Tarjeta «Perfil» (nombres, alias público, correo, «Guardar cambios»), tarjeta «Privacidad ante organizadores» (Nombre completo / Solo iniciales / Alias; switches «Mostrar el estado de mi alarma» y «Contar mi “Ya voy” en las métricas») y tarjeta coral «Eliminación de cuenta» con «Eliminar mi cuenta». Nota: «Los cambios de perfil no afectan tus alarmas en el celular». | F-W07 |
| W06 · Modal eliminar cuenta (`4072:256`) | «¿Eliminar tu cuenta definitivamente?» con consecuencias (4 eventos despublicados, 97 alarmas de asistentes sin actualizaciones, borrado en máximo 30 días, consejo de descargar reportes), campo «Escribe ELIMINAR», «Conservar mi cuenta» (amarillo, prominente) y «Eliminar definitivamente» (contorno coral). | F-W08 |
| W06 · Ajustes de Perfil (Actualizado) (`4072:279`) | Snackbar «Perfil actualizado». | F-W07 |

### 7.2 · Diferencias con los wireframes (W00–W07)

- **W02 desaparece como pantalla:** «Mis eventos» y el buscador con filtros (F-W02, F-W06) viven dentro de W01 como pestañas y filtros sobre la tabla. El tablero (F-W01) queda reducido a los cuatro indicadores; la gráfica de escaneos por semana no se incluyó.
- **W04 y W05 son modales** sobre W01, no secciones: los ítems «Reportes» y «Descargar QR» de la barra lateral abren el modal correspondiente.
- **W07 es «W06 · Modal eliminar cuenta»**, un modal sobre Ajustes de Perfil; al confirmar se vuelve a W00 con el snackbar «Cuenta eliminada exitosamente».
- Los códigos F-Wxx de `FUNCIONALIDADES.md` se conservan; el mapa funcionalidad → marco está en su sección web.

### 7.3 · Estilo

Mismo sistema que el móvil: Bricolage Grotesque Bold en títulos (24–28), Archivo en UI (SemiBold 14.5 en la barra lateral, Regular 13–15 en tablas), Spline Sans Mono Bold en los indicadores; Tinta `#17161C`, Amarillo Energía `#FFC400` en la acción principal, Gris Niebla `#F4F3EF` en superficies, Verde Texto `#0B7048` para «Publicado» / «Activa» / «✓ Escaneada» y Coral Texto `#C4362E` para «Eliminada» y la zona de riesgo. Píldora activa de la barra lateral en Tinta; botones píldora de **44 pt** (regla del DS v1.5: 48 para toque en móvil, 44 para puntero en web); tarjetas r12–14 y modales r14 con borde Gris Borde.

### 7.4 · Observaciones de la revisión y cómo se aplicaron (web v1.1, 2026-09-14)

| # | Observación | Qué se cambió |
|---|---|---|
| 1 | **Transiciones:** las 114 conexiones eran instantáneas; el móvil usa Smart Animate 250 ms (pasos 14–15 del enunciado). | Smart Animate 250 ms (ease in-out) en todas las conexiones de la página. |
| 2 | **Áreas de toque:** 15 conexiones colgaban de nodos de texto («Ver detalle ›», migas de pan, «✕»), contra la regla del 2026-09-06. | «Ver detalle ›» (×4) pasa a su marco padre `enlace · Ver detalle`; en los cinco modales la miga y la «✕» iban al mismo destino (W01), así que la fila completa `cabecera del modal · cerrar` es ahora el control (5 conexiones fusionadas: 114 → 109); la miga de W03 se envolvió en `enlace · volver a Mis alarmas` (`4274:11`). Cero conexiones sobre texto. |
| 3 | **Un solo primario amarillo por pantalla:** W01 tenía dos. | «Descargar QR en lote» pasa a botón secundario de contorno (blanco, borde Tinta 1,5) en los tres estados de W01; «Exportar reporte» sigue como único primario. |
| 4 | **F-W05:** el enunciado pide afiches con marca y «Escanéalo y te avisamos» (QR ≥ 4×4 cm). | Bloque «Vista previa del afiche» en los dos modales de W05 (`4275:2`, `4275:99`): miniatura 132 pt con barra Tinta «Alarmas QR», instancia de `código QR · evento`, nombre del evento y «Escanéalo y te avisamos», más el texto explicativo y «QR mínimo 4 × 4 cm · PNG 300 ppp o PDF vectorial». El modal pasa de 449 a 648 pt y sigue centrado en el área. |
| 5 | **F-W01:** faltaba la gráfica de escaneos por semana. | Tarjeta «Escaneos por semana» al final del contenido de W01 ×3 (`4276:172`, `4276:210`, `4276:248`): 8 semanas (6 jul → 24 ago) con valores en Spline Sans Mono Bold 12 y etiquetas Medium 11, total 128 coherente con el indicador; semana actual en Tinta, anteriores en Gris Texto; nota «Datos agregados y anónimos». |
| 6 | **PDF** sin marcadores ni metadatos. | `Mockups_Figma_Web.pdf` exportado con el procedimiento de §5 (IDs en orden: 4072:1861, 4072:26, 4072:49, 4072:72, 4072:95, 4072:118, 4072:141, 4072:164, 4072:187, 4072:210, 4072:233, 4072:256, 4072:279, 4072:1878), 14 páginas con un marcador por pantalla. |

## 8 · Changelog

- **2026-09-14 · web v1.2** — Divergencias del Design System resueltas: los 24 botones web pasan a 44 pt (cabecera de W01 39 → 44, modales y W06 42 → 44) y los 16 contenedores con radio 16 (modales de W04/W05/W06, tarjetas de W06, tarjeta de acceso de W00) pasan a radio 14. Sin cambios de navegación. `Mockups_Figma_Web.pdf` regenerado.

- **2026-09-14 · web v1.1** — Aplicadas las seis observaciones de §7.4 sobre los marcos de mmatallanar-ua: Smart Animate 250 ms en todas las conexiones; ninguna conexión sobre texto (fila de cabecera de los modales como control único, 114 → 109 conexiones); «Descargar QR en lote» a contorno para dejar un solo primario en W01; «Vista previa del afiche» en los modales de W05; tarjeta «Escaneos por semana» en W01 ×3; `Mockups_Figma_Web.pdf` con marcadores (sustituye a `Alarmas QR - Mockups Web.pdf`).

- **2026-09-14 · web v1** — mmatallanar-ua construye los mockups de la aplicación web en la página `03 · Web`: 14 marcos de 1280×820 (W00 ×2, W01 ×3, W03, W04 ×3, W05 ×2, W06 ×3), 114 conexiones «On tap» y un solo punto de inicio en W00. W02 se integra en W01 como pestañas, W04 y W05 son modales sobre W01 y W07 es el modal «Eliminar cuenta» sobre W06. Documentado en §7; observaciones en §7.4. PDF `Alarmas QR - Mockups Web.pdf`.

- **2026-09-14 · v1.4** — Feedback de usuarios: «Crear el evento a mano» se percibía como enlace y no como botón. En M03 (hoja inferior), M12 (permiso de cámara) y M02v (inicio sin alarmas) el enlace azul subrayado de 32 pt pasa a **botón secundario de contorno** con la anatomía de M13 (350×48, borde Tinta 1,5 pt interior, radio píldora, Archivo Bold 15 en Tinta); en M02v también «Elegir pantallazo de la galería», para que las dos acciones secundarias bajo el primario tengan la misma jerarquía que en M03/M12. M03 conserva 844 pt porque el visor (FILL) absorbe los 16 pt que crece la hoja; en M12 se retiró un envoltorio con espaciador vacío de 100×100. Las reacciones (→ M07) siguen en el marco del botón. `Mockups_Figma_Movil.pdf` regenerado.

- **2026-09-14 · v1.3.1** — Corrección del prototipo: los tres FAB «Escanear» (M02 `3:186`, M02b `4:133`, M05 `4:266`) tenían «On tap» → M12 y «While pressing» → M02h sobre el mismo nodo; como «While pressing» revierte la navegación al soltar y «On tap» se dispara justo en ese momento, la hoja M02h aparecía un instante y el prototipo saltaba a M12 sin elegir opción. Solución verificada en el prototipo publicado (Playwright headless, toque de 60 ms vs. presión de 1,2 s): «Mouse up» sin retardo → M12 y «Mouse down» con retardo de 0,5 s → M02h (`{ type: 'MOUSE_UP', delay: 0 }` / `{ type: 'MOUSE_DOWN', delay: 0.5 }`). Dos lecciones del Plugin API: (1) el `delay` se guarda en **segundos** aunque la documentación diga milisegundos (con 500 el visor nunca disparaba); (2) un «On tap» deja de dispararse cuando el mismo nodo tiene un «Mouse down» con retardo, por eso el toque corto pasó a «Mouse up». Toque corto → M12, mantener ≥ 0,5 s → M02h y la hoja permanece al soltar. Siguen siendo 72 conexiones y un solo punto de inicio (M01). Sin cambios visuales ni funcionales.

- **2026-09-08 · v1.3** — Segunda crítica (25/36) y sus 8 pasos (§3c): tarjeta única con hora del evento, calendario coherente, M10 «Ya voy» / «Posponer 10 min» / «Ver ruta ›», FAB toque = cámara y mantener = hoja, huecos reducidos, M06 y consentimientos, amarillo residual, DS y Style Tile alineados. 72 conexiones. PDFs regenerados.
- **2026-09-08 · v1.2** — Pasos 6–10 de la crítica: amarillo como acento estricto, tonos de texto AA y escala ≥ 12 pt, textura solo como banda en 6 pantallas, Design System corregido (lámina L08, 4 estilos de color, comp. 05/10/12/13/19) con PDF regenerado, `STYLE_TILE.md` §4.1 y `DESIGN_SYSTEM.md` §6 reescritos. PDF de mockups regenerado.
- **2026-09-08 · v1.1** — Pasos 1–5 del plan de la crítica Impeccable (ver §3b): M10 con dos acciones, M09/M04 con la hora como héroe, hoja «Agregar evento» + FAB extendido, nuevas M02v y M03b, formato 12 h y dataset único, botones de 48 pt. 19 pantallas, 67 conexiones. PDF regenerado.

- **2026-09-07 · v1** — 16 pantallas móviles re-estilizadas con «Energía puntual» sobre el duplicado de los wireframes; página `00 · Recursos gráficos` con 2 texturas, 8 ilustraciones y 10 íconos; Smart Animate en las 59 conexiones; `Mockups_Figma_Movil.pdf`.
