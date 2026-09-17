# Alarmas QR · Design System "Energía puntual"

Sistema de diseño de alta fidelidad derivado del prototipo de navegación (24 pantallas: 16 móviles M00a–M13 + 8 web W00–W07), del Style Tile "Energía puntual" y de la sábana de 17 controles de los wireframes.
Fecha: 2026-09-01 · v1.1/v1.2 del 2026-09-08 (correcciones tras la crítica de diseño, ver §6) · **v1.3/v1.4 del 2026-09-14: componentes web tal como quedaron en los mockups de la página `03 · Web` (ver §7), construidos como lámina L09 en Figma** · **v1.6 del 2026-09-17: botones móviles a 52 pt por la revisión de los tutores** · **v1.7 del 2026-09-17: set 47 «Diálogo de confirmación · móvil» en L08** · Insumos: `STYLE_TILE.md`, `Wireframes_Alarmas_QR.html`, prototipos publicados (Figma), `FUNCIONALIDADES.md`.
Entregables: archivo Figma «Design System Alarmas QR Equipo UX» (láminas 1920×1080 + componentes con variantes), `Design_System_Alarmas_QR.pdf` y la versión web de este documento (artefacto, ver §4).

---

## 1 · Análisis de pantallas → componentes requeridos (pasos 1–2)

Inventario levantado pantalla por pantalla sobre el prototipo de navegación. Cada número remite al catálogo de la sección 2.

### Aplicación móvil

| Pantalla | Componentes requeridos |
|---|---|
| M00a · Registro | 1, 2, 4, 6, 11, 13 (raíz), 29 |
| M00b · Inicio de sesión | 1, 2, 4, 6, 13 (raíz), 29 |
| M01 · Bienvenida | 1, 4, 10, 22 (ilustración QR), 19 |
| M02 · Inicio · lista | 1, 2, 5, 9, 12, 14, 18, 19, 13 (raíz) |
| M02b · Vista calendario | 5, 9, 12, 14, 18, 24 (calendario), 13 (raíz) |
| M03 · Escáner dual | 2, 4, 12, 22 (marco visor), 29 |
| M04 · Alarma creada | 1, 3, 12, 19, 25, 29, 30 |
| M05 · Guardada + deshacer | 5, 9, 12, 14, 18, 26, 13 (raíz) |
| M06 · Edición de alarma | 1, 3, 6, 9, 10, 12, 16, 19, 25 |
| M07 · Creación manual + QR | 1, 6, 8, 9, 12, 29 |
| M08 · Compartir QR | 2, 4, 12, 20 (métrica escaneos), 22, 29 |
| M09 · Cambio del organizador | 1, 2, 3, 25, 28, 29 |
| M10 · Alarma sonando | 1, 4, 9, 19 (chip evento), 29 (sugerencia de salida) |
| M11 · Ajustes | 4, 10, 12, 16 (filas de ajuste), 19, 25, 29 |
| M12 · Permiso de cámara | 1, 2, 4, 12, 29 |
| M13 · QR inválido | 1, 2, 4, 12, 19, 29, 30 |

### Aplicación web

| Pantalla | Componentes requeridos |
|---|---|
| W00 · Inicio de sesión | 1, 4, 6, 29 |
| W01 · Tablero | 15, 14w, 16, 20, 23, 24, 29 |
| W02 · Mis eventos + filtros | 7, 8, 9, 14w, 15, 16, 19, 21, 30 (estado vacío) |
| W03 · Monitoreo anónimo | 2, 7, 14w, 15, 16, 19, 20, 21, 25, 29 |
| W04 · Reportes | 1, 8, 9, 11, 14w, 15, 16, 19, 21, 24, 29 |
| W05 · Afiches en lote | 1, 2, 14w, 15, 16, 22, 23 (tarjeta afiche), 29 |
| W06 · Perfil de usuario | 1, 3, 4, 6, 9, 10, 14w, 15, 16, 29 |
| W07 · Eliminar cuenta | 2, 3, 6, 14w, 15, 16, 27, 29 |

**Mockups web (2026-09-14, página `03 · Web`):** W02 vive dentro de W01 (pestañas + filtros, comp. 7, 9, 19, 21), W04 y W05 son modales (comp. 27) sobre W01 y W07 es el modal destructivo sobre W06; W01 suma la gráfica de barras (comp. 24) y W05 la tarjeta de afiche (comp. 23). Especificaciones medidas en §7.

**Paso 4 del enunciado (¿faltan pantallas?):** el prototipo ya excede el prototipo en papel — se agregaron M02b (calendario), M12/M13 (desvíos de error), W03 (monitoreo) y W07 (modal de confirmación) a partir de flujos y pruebas de usabilidad. No se requieren pantallas adicionales para el MVP.

---

## 2 · Catálogo de componentes: estados y variaciones (paso 3)

Convención de estados base: **normal · presionado/hover · deshabilitado**, más los estados propios de cada componente. "Variantes" = ejes de variación en Figma.

### A · Acciones

| # | Componente | Estados | Variantes | Total |
|---|---|---|---|---|
| 1 | Botón primario | normal · presionado · deshabilitado | con/sin icono | 6 |
| 2 | Botón secundario (contorno) | normal · presionado · deshabilitado | — | 3 |
| 3 | Botón destructivo | normal · presionado · confirmación | — | 3 |
| 4 | Botón de texto / enlace | normal · presionado · deshabilitado | — | 3 |
| 5 | FAB «+» | normal · presionado · extendido | — | 3 |

### B · Entradas

| # | Componente | Estados | Variantes | Total |
|---|---|---|---|---|
| 6 | Campo de texto | normal · foco · error · deshabilitado | — | 4 |
| 7 | Campo de búsqueda | normal · foco · con texto | — | 3 |
| 8 | Campo de fecha / rango | normal · foco | — | 2 |
| 9 | Selector segmentado | opción activa (por posición) | 2 · 3 · 4 opciones | 3 |
| 10 | Switch | activa · inactiva · deshabilitada | — | 3 |
| 11 | Checkbox | marcado · sin marcar · deshabilitado | — | 3 |

### C · Navegación

| # | Componente | Estados | Variantes | Total |
|---|---|---|---|---|
| 12 | Barra superior móvil | — | con atrás · raíz (sin atrás) | 2 |
| 13 | Navegación inferior móvil | pestaña activa (×3) · con badge | — | 4 |
| 14 | Ítem de barra lateral web (14w) | activo · inactivo · hover | — | 3 |
| 15 | Cabecera web (logo + usuario) | — | — | 1 |
| 16 | Fila de lista / breadcrumb | normal · con chevron › · breadcrumb | — | 3 |

### D · Contenido y datos

| # | Componente | Estados | Variantes | Total |
|---|---|---|---|---|
| 18 | Tarjeta de alarma | activa · pausada · recién guardada · eliminada | — | 4 |
| 19 | Chip de estado | — | Mía·QR · Escaneada ✓ · Nueva · Propio · Escaneado · activa · pausada · eliminada | 8 |
| 20 | Tarjeta de estadística (web) | — | número · número + detalle | 2 |
| 21 | Tabla de datos (web) | encabezado · fila normal · fila hover | — | 3 |
| 22 | Código QR | real · placeholder · con marco de visor | — | 3 |
| 23 | Tarjeta de afiche QR | — | — | 1 |
| 24 | Gráfica de barras | — | — | 1 |
| 25 | Sello verificado | verificado · sin verificar | — | 2 |

### E · Retroalimentación

| # | Componente | Estados | Variantes | Total |
|---|---|---|---|---|
| 26 | Snackbar «Deshacer» | con cuenta atrás · en curso · confirmado | — | 3 |
| 27 | Diálogo modal | — | informativo · destructivo | 2 |
| 28 | Tarjeta de notificación push | — | — | 1 |
| 29 | Banner / aviso en línea | informativo · error · éxito | — | 3 |
| 30 | Estado vacío / sin resultados | — | móvil · web | 2 |

**Totales: 29 componentes · 79 estados/variantes** en L03–L07, más los complementos 31–36 (L08) y los componentes web 37–46 (L09, v1.4) y el diálogo de confirmación móvil 47 (L08, v1.7): 46 componentes en el archivo Figma.

---

## 3 · Clasificación (paso 4) y láminas del documento

| Lámina (1920×1080) | Contenido |
|---|---|
| L01 · Paleta de colores | Principal (Amarillo Energía #FFC400, Amarillo Suave #FFF1BF, Tinta #17161C, Blanco Papel #FFFFFF) + secundaria (Coral Alarma #E8443A, Verde Confirmado #129E63, Azul Enlace #2E7CF6, Gris Niebla #F4F3EF, Gris Medio #77747E, Gris Borde #DAD8D2) con roles y reglas de uso |
| L02 · Carta de tipografías | Bricolage Grotesque (titulares) · Archivo (UI/cuerpo) · Spline Sans Mono (horas/datos) — escala de 9 niveles con ejemplos reales |
| L03 · Acciones | Componentes 1–5 |
| L04 · Entradas | Componentes 6–11 |
| L05 · Navegación | Componentes 12–16 |
| L06 · Contenido y datos | Componentes 18–25 |
| L07 · Retroalimentación | Componentes 26–30 |
| L08 · Complementos v1.7 | Componentes 31–36: acciones de alarma sonando, controles sobre Tinta, primario sobre amarillo, hora en tarjeta 28 + 14, tonos de texto; **47 · Diálogo de confirmación · móvil** (set `45:38`, variantes Eliminar alarma / Cerrar sesión, 2026-09-17) |
| L09 · Web v1.3 (2026-09-14) | Componentes 37–46: píldora de filtro (set Activa/Inactiva), chip web de estado (set Publicado/Activa/Eliminada/Creada por mí), cabecera de modal como control de cierre, gráfica «Escaneos por semana», vista previa del afiche (comp. 23 concretado, con instancia del QR real), snackbar web, botón web de 42 pt (set Primario/Secundario/Destructivo), barra lateral de 208, tarjeta de acceso web y selector segmentado web de 36 pt |

Base estructural: **Material 3** (anatomía y métricas de los componentes: alturas 44/52/56 — **52 para toque en móvil (v1.6, revisión de los tutores; 48 entre la v1.1 y la v1.5), 44 para puntero en web** (v1.5) —, radio 14 px/píldora en tarjetas, modales y campos, área táctil 48 px) re-tematizado con la paleta "Energía puntual". Reglas transversales: amarillo solo en la acción principal; coral solo en urgencia/destrucción; sobre amarillo siempre texto Tinta (12.9:1); estados nunca comunicados solo con color (color + forma/símbolo).

## 6 · Correcciones v1.1 (2026-09-08) — fuente de verdad tras la crítica de diseño

La crítica Impeccable de los mockups detectó redefiniciones silenciosas entre este documento y las pantallas. Se decide que **los mockups mandan** y el sistema se actualiza así:

| Tema | Antes (v1) | Ahora (v1.1) |
|---|---|---|
| Regla del amarillo | Acción principal + marca; el switch activo también era amarillo | **Acento estricto:** un solo elemento amarillo por pantalla (botón primario o FAB extendido). Switch activo, píldora de pestaña activa, chip «Nueva» y numerales de pasos van en **Tinta** (comp. 10, 13, 19). |
| Barra superior (comp. 12) | «Con atrás» en Archivo Bold 26, «Raíz» en Bricolage Bold 24 | Ambas variantes en **Bricolage Grotesque Bold 22**; la flecha «‹» en Archivo Bold 26 (el glifo de Bricolage se ve mal). |
| Altura de botones | 48 | **52 desde la v1.6** (2026-09-17, revisión de los tutores: «hay usuarios con pulgares anchos que no alcanzan a pulsar bien un botón delgado»); los mockups v1 usaban 44 y de la v1.1 a la v1.5 fueron 48. Acciones de la alarma sonando: 56. Los enlaces de texto siguen en 32. |
| Hora en tarjeta | Spline Sans Mono Bold 34 + am/pm 18 | **Spline Sans Mono Bold 26–28 + am/pm Medium 12–14** en la misma línea, seguida de título Archivo Bold 15, «evento h:mm · lugar» 13 Gris Texto y chip debajo (una sola anatomía en lista, calendario, hoja y edición) (34 no cabe en 4 columnas de 390 pt). Bloques «Sonará» (M04/M09): Bold 52 con etiqueta 12 pt encima. Alarma sonando: Bold 96 + pm 28. |
| Formato horario | Mixto (24 h en tarjetas, 12 h en detalle) | **12 h con am/pm** en toda la app. |
| Campo de texto (comp. 06) | Etiqueta encima del campo, radio 10 | **Etiqueta dentro del contorno** (Archivo SemiBold 12 Gris Texto sobre el valor), radio 12, altura 48. |
| Chip de estado (comp. 19) | Origen y estado mezclados sin regla | **Origen siempre** («Mía · QR» contorno Tinta, «✓ Escaneada» contorno Verde Texto); **estado solo temporal** («Nueva» relleno Tinta/texto blanco, 24 h; «pausada» como texto gris, no chip). Una alarma escaneada nunca lleva «Mía · QR». |
| FAB (comp. 05) | «+» hacia creación manual; «+ Nueva alarma» extendido | **FAB extendido «Escanear»** (ícono escanear + Archivo Bold 16, 56 pt, r16) como única entrada de captura: **toque → cámara; mantener presionado → hoja «Agregar evento»** (Escanear QR · Pantallazo · A mano). Sin variante «+». |
| Tonos de texto | Coral/Verde/Azul/Gris Medio usados como texto | Nuevos estilos **Coral Texto #C4362E, Verde Texto #0B7048, Azul Texto #1A5BC4, Gris Texto #66636D** para texto ≤ 15 pt; los base quedan para rellenos, íconos y trazos. Sobre Tinta, texto secundario en Gris Borde #DAD8D2. |
| Escala secundaria | Etiqueta 13, chip 12, agrupador 15 | Etiqueta de tarjeta 13, chip 12, agrupador H3 13 (Bold, mayúsculas, +8 %), notas 12; nada por debajo de 12 salvo etiquetas de la barra inferior (11). |
| Superficies oscuras | Sin variantes | **Nuevos (lámina L08):** botón secundario sobre Tinta (contorno blanco 1.5, texto blanco), enlace sobre Tinta (blanco subrayado), segmentado sobre Tinta (contorno Gris Medio, opción activa blanca), chip sobre Tinta (contorno Gris Medio/Coral). |
| Alarma sonando | Sin patrón | **Nuevo comp. 31 «Acciones de alarma sonando»:** dos botones equivalentes de 56 pt apilados: «Ya voy» (relleno **blanco**, detiene sin salir) y «Posponer 10 min» (contorno blanco); «Ver ruta ›» va dentro del destacado. Excepción escrita: sobre Tinta el primario es blanco para que la hora sea el único amarillo. |
| Primario sobre amarillo | Sin variante | **Nuevo:** botón primario «Sobre amarillo» = relleno Tinta, texto blanco (M01 «Comenzar»); enlaces sobre amarillo en Tinta subrayado. |
| Textura | «≤ 8 % en fondos» | Solo banda superior de 120 pt en M01, M04, M09, M10, M12 y M13; nunca en listas, formularios ni visor. |
| Estado vacío (comp. 30) | Definido, no usado | Usado en **M02v**: diana QR + «Aún no tienes alarmas» + primario «Escanear QR del evento» + botones secundarios de contorno «Elegir pantallazo» / «Crear a mano» (enlaces hasta el 2026-09-14; feedback de usuarios: «Crear el evento a mano» debe verse como botón, también en M03 y M12). |


## 7 · Componentes web en los mockups (v1.3, 2026-09-14)

Los mockups de la aplicación web (página `03 · Web`, 14 marcos de 1280×820) concretan los componentes «w» del catálogo. Medidas tomadas sobre los marcos tras aplicar la revisión web v1.1 (`MOCKUPS.md` §7.4). Las divergencias detectadas en la v1.3 (botones web de 39–44 pt frente a 48 en móvil; radio 16 en modales, tarjetas de W06 y tarjeta de acceso) se resolvieron en la v1.5: **botones web a 44 pt** en todas las pantallas (regla 48 móvil / 44 web; desde la v1.6 el móvil sube a 52) y **radio 14** en todos los contenedores, en los mockups y en las láminas L07 y L09.

### 7.1 · Estructura de página

| Componente | Especificación | Catálogo |
|---|---|---|
| Barra superior web | 1280×64, relleno Blanco Papel, borde inferior Gris Borde, relleno lateral 28. Marca: diana amarilla + «Alarmas QR» Bricolage Grotesque Bold 17. Derecha: nombre del usuario Archivo Regular 14 + avatar circular con borde; el avatar abre W06. | 15 |
| Barra lateral | 208 de ancho, relleno 20/12, separación 4, borde derecho Gris Borde. Ítems de 38 pt: **activo** = píldora Tinta con texto blanco Archivo SemiBold 14.5, relleno 11/14; **inactivo** = sin relleno, texto Tinta. «Cerrar Sesión» anclado abajo con un espaciador flexible. Las píldoras activas van en Tinta, no en amarillo (regla del acento estricto). | 14w |
| Área de contenido | 1072 de ancho, relleno 28/32, separación vertical 18 entre bloques. | — |
| Velo de modal | Capa absoluta sobre el área de contenido (1280×756) en Tinta al 45 %, con el modal centrado en ambos ejes. | 27 |

### 7.2 · Datos y contenido

| Componente | Especificación | Catálogo |
|---|---|---|
| Indicador (tarjeta de estadística) | 242×104 en fila de 4 con separación 14; borde Gris Borde 1.5, radio 14, relleno 14/18. Número Spline Sans Mono Bold 24 en Tinta, etiqueta Archivo Regular 14 en Tinta, detalle Archivo Regular 12.5 en Gris Texto. | 20 |
| Píldoras de filtro y pestaña | 34 de alto, relleno 10/14, radio píldora, Archivo SemiBold 13. Activa: relleno Tinta, texto blanco; inactiva: relleno Gris Niebla, texto Gris Texto. Dos grupos (Próximos / Pasados / Borradores · Todos / Creados / Escaneados) con separación 20 entre grupos y 10 dentro. | 9 |
| Campo de búsqueda | 240×40, «Buscar» como marcador de posición, mismo contorno que el campo de texto. | 7 |
| Tabla de datos | Encabezado de 26 pt con borde inferior Tinta y rótulos Archivo Bold 11 en Gris Texto, mayúsculas; filas de 48 pt (relleno 12/0) separadas por líneas Gris Borde; siete columnas; «Ver detalle ›» en Azul Texto Archivo Regular 14, con la conexión en su marco de 100×20. | 21 |
| Chips | 19–20 de alto, relleno 3/12, radio píldora, Archivo Bold 12–12.5. **Origen:** «Creada por mí» contorno Tinta 1.5 sobre blanco; «✓ Escaneada» contorno Verde Texto. **Estado:** «Publicado» relleno #E9F7F0 con contorno y texto Verde Texto; «Activa» igual; «Eliminada» contorno y texto Coral Texto. | 19 |
| Gráfica de barras · «Escaneos por semana» | Tarjeta con borde Gris Borde 1.5, radio 14, relleno 16/18. Encabezado: título Archivo SemiBold 14.5 + nota Archivo Regular 13 Gris Texto alineada a la derecha. Ocho columnas en FILL con separación 12, alineadas al eje inferior: valor Spline Sans Mono Bold 12 encima, barra de radio 4 con altura proporcional (máximo 96 pt), etiqueta de semana Spline Sans Mono Medium 11 Gris Texto debajo. La semana actual en Tinta, las anteriores en Gris Texto; sin amarillo. Pie: «Datos agregados y anónimos». | 24 |
| Tarjeta de afiche QR · «Vista previa del afiche» | Bloque de fondo Gris Niebla con borde Gris Borde 1.5, radio 12, relleno 12, dos columnas: **miniatura** de 132 de ancho (blanco, borde Gris Borde, radio 8) con barra de marca Tinta (punto amarillo + «Alarmas QR» Archivo Bold 9.5 blanco), instancia de `código QR · evento` a 72 pt, nombre del evento Archivo Bold 10.5 y «Escanéalo y te avisamos» Archivo Regular 8.5 Gris Texto; **texto** con rótulo «VISTA PREVIA DEL AFICHE» Archivo Bold 11 Gris Texto, explicación Archivo Regular 13 y la regla «QR mínimo 4 × 4 cm · PNG a 300 ppp o PDF vectorial» 12.5 Gris Texto. | 23 |

### 7.3 · Acciones, entradas y retroalimentación

| Componente | Especificación | Catálogo |
|---|---|---|
| Botón primario web | Píldora Amarillo Energía, texto Tinta Archivo SemiBold 14–15, **44 pt de alto en toda la web** (v1.5: cabecera de página, modales, W06 y tarjeta de acceso; antes 39/42/44). Regla: 52 para toque en móvil (v1.6; antes 48), 44 para puntero en web, ambas en la escala 44/52/56. Un solo primario por pantalla: en W01 «Exportar reporte»; «Descargar QR en lote» va en contorno. | 1 |
| Botón secundario web | Misma píldora en blanco con contorno Tinta 1.5 («Cancelar», «Descargar QR en lote»). | 2 |
| Botón destructivo web | Contorno Coral Texto 1.5 y texto Coral Texto («Eliminar mi cuenta», «Eliminar definitivamente»); dentro del modal destructivo la acción segura («Conservar mi cuenta») es la primaria amarilla. | 3 |
| Selector segmentado | Contenedor Gris Niebla de 36 pt, radio píldora, relleno 3, separación 2; opciones de 30 pt Archivo SemiBold 13, activa en Tinta con texto blanco, inactivas en Gris Texto (W06 «Nombre completo / Solo iniciales / Alias»). | 9 |
| Switch | 42×24, píldora Tinta con perilla blanca a la derecha cuando está activo (misma regla que el móvil). | 10 |
| Campo de texto web | Etiqueta en mayúsculas Archivo 11 Gris Texto dentro del contorno, valor Archivo 14; grupo de campos con separación 12. | 6 |
| Tarjeta de acceso (W00) | 400 de ancho, relleno 34, separación 20, borde Gris Borde 1.5, radio 14 (v1.5; antes 16), centrada; marca arriba, subtítulo Archivo Regular 14 Gris Texto, campos, primario a ancho completo y nota de 13 pt. | — |
| Diálogo modal | 540 (reporte) a 600 (QR en lote) de ancho, relleno 24–26, separación 16–18, radio 14 (v1.5; antes 16, también en las variantes del comp. 27 de L07), sobre el velo Tinta 45 %. Fila de cabecera con miga «‹ Mis alarmas / …» Archivo Regular 14 Gris Texto y «✕» Archivo Regular 16: la fila completa es el control de cierre. Título Bricolage Grotesque Bold 21; acciones alineadas a la derecha. Variante destructiva (W06) con icono de advertencia coral y lista de consecuencias. | 27 |
| Snackbar web | Píldora Tinta de 36 pt, relleno 10/18, icono de verificación + texto Archivo SemiBold 14 blanco, centrada al pie del área de contenido («Descarga completada exitosamente», «Perfil actualizado», «Cuenta eliminada exitosamente»). | 26 |
| Aviso en línea | Texto Archivo Regular 13–14 Gris Texto con icono de información («Las métricas de eventos escaneados pertenecen a su organizador», «Solo alias o iniciales…»). | 29 |

### 7.4 · Reglas que la web confirma

- **Un solo amarillo por pantalla** también en escritorio: los estados activos (píldora de barra lateral, pestañas, segmentado, switch) van en Tinta.
- **Movimiento:** todas las conexiones del prototipo web usan Smart Animate 250 ms, como el móvil.
- **Áreas de toque:** las conexiones cuelgan del marco del control (fila de cabecera del modal, marco de «Ver detalle ›», envoltorio de la miga de pan), nunca del texto.
- **Divergencias con las láminas del DS, corregidas el 2026-09-14 (v1.4):** el comp. 24 de L06 mostraba la barra actual en amarillo (ahora Tinta, anteriores en Gris Texto); el comp. 27 de L07 decía velo al 40 % (ahora 45 %); el comp. 14 de L05 anunciaba barra lateral de 232 px (ahora 208).
- **Lámina L09 · Web (v1.4):** los componentes de esta sección existen ahora como componentes reales en el archivo Figma del DS (37–46, ver §3), construidos con los estilos de color del archivo y una instancia del comp. 22 «Código QR · Real» en el afiche.

## 4 · Enlaces

- Archivo Figma: https://www.figma.com/design/lHYLJJIbltBS5Joo850iks (equipo MISO-UX) — 7 láminas 1920×1080, 10 estilos de color, 9 estilos de texto, componentes con variantes (`combineAsVariants`).
- PDF: `Design_System_Alarmas_QR.pdf` (9 páginas, export vectorial de Figma, una por lámina L01–L09).
- Versión web de este documento (artefacto con las ocho láminas y las especificaciones web): https://claude.ai/code/artifact/0e8a6337-5ba9-4f70-b42e-096d556b6c2a

## 5 · Changelog

- **2026-09-17 · v1.7** — Comentario general de los tutores sobre la navegación («agregar modales de verificación y prevención de errores, por ejemplo al dar clic en eliminar»). Nuevo set **47 · Diálogo de confirmación · móvil** en L08 (columna libre a la derecha, `45:38`; variantes `Acción=Eliminar alarma` `45:36` y `Acción=Cerrar sesión` `45:37`), construido con los estilos del archivo (Blanco Papel, Amarillo Energía, Tinta, Coral Texto, Gris Texto; título con el estilo H2). Anatomía: 342 pt de ancho (390 − 2×24), relleno 24, radio 20, sombra suave; título H2 Bricolage SemiBold 22; cuerpo Archivo Regular 14 / 140 % con la consecuencia concreta; botones de 52 pt apilados: la **acción segura es el primario amarillo** («Conservar», «Cancelar») y la que confirma va en **contorno 1,5 pt** — Coral Texto si destruye («Eliminar»), Tinta si solo sale («Cerrar sesión»); rótulos de máximo dos palabras; velo Tinta 55 % sobre la pantalla de origen, y tocar el velo equivale a la acción segura. Relación con el comp. 27 «Diálogo modal» (L07): el 27 es la versión web/horizontal (360 pt, botones lado a lado, velo 45 %); el 47 es la móvil/vertical, y ambos cumplen «lo seguro siempre prominente». El comp. 03 «Botón destructivo» queda así con su uso previsto desde la v1.6: dentro de diálogos de confirmación (móvil 47, web W07) y nunca suelto en una pantalla. Instancias en los mockups: M04d, M06d, M11d; en los wireframes también M09d. Título de L08 actualizado a v1.7; `Design_System_Alarmas_QR.pdf` con L08 reexportada (sigue en 9 páginas).
- **2026-09-17 · v1.6** — Revisión de los tutores sobre los wireframes móviles (comentario en M01: «pueden agrandar el height del botón Comenzar para que el usuario en mobile tenga una mejor experiencia; hay usuarios con pulgares anchos que al ser tan delgado el botón no alcanzan a hacer clic bien; les recomiendo 52 px»). El argumento aplica a todo botón principal, así que la regla móvil pasa de 48 a **52 pt**: 25 botones de relleno/contorno en los wireframes (44 → 52), 26 en los mockups móviles (48 → 52), las 12 variantes de los sets 01/02/03 de L03 y los marcos de los comp. 32 y 35 de L08 (48 → 52), rótulos de L03 y L09 actualizados («altura 52», «botón web · 44 pt (móvil 52)»). Sin cambios en los enlaces de texto (32), en las acciones de la alarma sonando (56), en el FAB (56) ni en la web (44). Láminas L03, L08 y L09 reexportadas en el PDF. Segundo comentario (M00a, junto a «¿Ya tienes cuenta? Inicia sesión»: «agregar un mayor padding bottom»): regla nueva para el **enlace de pie de pantalla** — va dentro de un marco de control de 44 pt (`botón · …`, con la conexión en el marco y no en el texto) y el contenedor cierra con 32 pt de relleno inferior (antes 16, y el texto suelto de 13 pt tocaba el borde). Aplicado en M00a y M00b de wireframes y mockups; el relleno de 32 también en los cierres con enlace de M01 y de la hoja de M03 (wireframes) y de M03 y M06 (mockups). Tercer comentario (M00b: «centrar la composición»): en las pantallas de acceso (M00a/M00b) el bloque de formulario se centra verticalmente entre la parte superior y el pie anclado (espaciadores FILL arriba y abajo). Cuarto comentario (M02: «eliminar el background» de la tarjeta pausada): la tarjeta de alarma pausada ya no lleva relleno Gris Niebla; el estado «pausada» se comunica solo con texto en Gris Texto, la etiqueta «pausada» y el switch apagado (comp. 18 / 19). Sexto comentario (M09: «Eliminar alarma» sin borde ni fondo): la acción destructiva dentro de una pantalla es un **enlace de texto** (comp. 04 con Coral Texto en alta fidelidad), no una píldora; el comp. 03 «Botón destructivo» queda reservado para la confirmación en diálogos y para la web (W06/W07). Séptimo comentario (M10: «Ya voy · ver ruta» solo contorno): en los wireframes, sobre fondo oscuro el botón principal de la alarma sonando va en contorno blanco para que la hora sea el héroe; en alta fidelidad se mantiene la excepción de la v1.3 (comp. 31: sobre Tinta el primario es blanco relleno y el secundario en contorno), porque M10 tiene dos acciones y la diferencia relleno/contorno es la que las jerarquiza. Octavo comentario (M11: «padding entre elementos»): regla para listas de ajustes — filas de **40 pt** con contenido centrado y 4 pt de separación (paso 44) y encabezado de sección con 16 pt de relleno superior (comp. 20 «Fila de ajuste» / agrupadores). Noveno comentario (M07: solo la etiqueta dentro del campo): regla para el comp. 06 «Campo de texto» — **vacío = solo la etiqueta** (Archivo SemiBold 12 Gris Texto, centrada verticalmente en campos de una línea); **con valor = etiqueta pequeña arriba + valor**; no se combina etiqueta con placeholder de instrucción. Pendiente decidir si M00a/M00b («tucorreo@ejemplo.com», «Mínimo 8 caracteres») siguen la misma regla o conservan la pista de formato. Décimo comentario (M12: «mientras tanto» alineado abajo): patrón de pantalla de permiso/error — la acción principal acompaña a la explicación y las **alternativas secundarias se anclan al borde inferior** (divisor + botones, 32 pt al borde), como ya hacían M03 y M13. Undécimo comentario (M04: «esto puede ser un modal»): las **confirmaciones transitorias** (alarma programada tras escanear) se presentan como hoja inferior (comp. 28 «Hoja inferior»: velo Tinta 55 %, r24 superior, asa 36×4 Gris Borde, relleno 12/20/32/20) sobre la pantalla de origen atenuada, no como pantalla con barra superior.

- **2026-09-14 · v1.5** — Divergencias resueltas: los 24 botones web de los mockups pasan a **44 pt** (cabecera 39 → 44, modales y W06 42 → 44; la tarjeta de acceso ya estaba en 44) y la regla queda escrita: 48 para toque en móvil, 44 para puntero en web. **Radio 14** en los 16 contenedores web que usaban 16 (modales de W04/W05/W06, tarjetas de W06, tarjeta de acceso de W00) y en las dos variantes del comp. 27 de L07; L09 comp. 43 renombrado «Botón web · 44 pt» y comp. 45 con radio 14. PDFs de mockups web y del DS regenerados.
- **2026-09-14 · v1.4** — Archivo Figma: nueva **lámina L09 · Web v1.3** con los componentes 37–46 (píldora de filtro, chip web de estado, cabecera de modal, gráfica «Escaneos por semana», vista previa del afiche, snackbar web, botón web de 42 pt, barra lateral de 208, tarjeta de acceso web, selector segmentado web), todos con estilos de color del archivo; correcciones en L05 (barra lateral 232 → 208), L06 (comp. 24: barra actual en Tinta, anteriores en Gris Texto) y L07 (velo del modal 40 → 45 %). `Design_System_Alarmas_QR.pdf` regenerado con 9 páginas; artefacto republicado.
- **2026-09-14 · v1.3** — §7: especificaciones de los componentes web medidas sobre los mockups de la página `03 · Web` (barra superior 64, barra lateral 208 con píldora activa Tinta, indicador 242×104, píldoras de filtro 34, tabla 26/48, chips 20, modal 540–600 r16 sobre velo Tinta 45 %, snackbar píldora Tinta, tarjeta de acceso 400, botones web 42–44) y dos componentes nuevos concretados: **gráfica de barras (comp. 24)** y **tarjeta de afiche QR (comp. 23)**. Divergencias registradas: botones web de 42–44 pt frente a 48 en móvil; radio 16 en modales y tarjeta de acceso. Versión web del documento publicada como artefacto.
- **2026-09-08 · v1.2** — Segunda crítica: láminas L02–L08 alineadas con los mockups (hora 28 + 14 y H3 13 en L02, FAB extendido en sus tres variantes, campo con etiqueta dentro, tarjeta 18 con «evento…» y estados en Tinta, estado vacío sin «+», L08 con «Ya voy» blanco y la excepción del amarillo sobre Tinta). PDF regenerado.
- **2026-09-08 · v1.1** — §6: correcciones tras la crítica de diseño (amarillo estricto, barra superior en Bricolage, hora 28+14 en tarjeta, campo con etiqueta interna, regla de chips, FAB extendido «Escanear», tonos de texto AA, variantes sobre Tinta, patrón de alarma sonando, primario sobre amarillo). Aplicadas en el archivo Figma del DS (componentes 05, 10, 12, 13, 19 y nueva lámina L08) y en el PDF.

- **2026-09-01 · v1** — Análisis de las 24 pantallas del prototipo de navegación, catálogo de 29 componentes con 79 estados/variantes, clasificación en 5 categorías + 2 láminas de fundamentos. Construcción nativa en Figma y export PDF.
