# Diseño · Plan 4, móvil de la Persona B (M02b, M06–M11)

Fecha: 2026-09-23 · Autor: mmatallanar-ua (Persona B) con Claude · Estado: aprobado en chat el 2026-09-23.
Insumos: `docs/superpowers/specs/2026-09-20-maquetacion-persona-a-design.md` (arquitectura base, ya construida), `docs/PLAN_MAQUETACION.md` §3, `docs/FUNCIONALIDADES.md` (F-M02/M02b, F-M06 a F-M11), `docs/TRAZABILIDAD.md` §1/§1b/§4, `docs/NAVEGACION.md` §6, `docs/MOCKUPS.md` (medidas y copy exactos de M02b/M06–M11), `docs/DESIGN_SYSTEM.md`, `README.md` §«Cómo continuar (Persona B)», código ya construido en `apps/movil/app/src/main/java/co/edu/uniandes/alarmasqr/`.

## 0 · Alcance

Cierra el reparto de la Persona B en móvil (`docs/PLAN_MAQUETACION.md` §3): las 7 pantallas que hoy caen en el marcador de la Fase 0 porque `navegacion/EntradasApp.kt` no las registra (verificado por `grep`; ninguna de las 7 aparece).

| Pantalla | Ruta | F- | Componente destino |
|---|---|---|---|
| M02b | `/calendario` | F-M02 | `PantallaCalendario` |
| M06 | `/alarma/:id` | F-M06 | `PantallaEditarAlarma` |
| M07 | `/evento/nuevo` | F-M07 | `PantallaCrearEvento` |
| M08 | `/evento/:id/qr` | F-M08 | `PantallaCompartirQR` |
| M09 | `/alarma/:id/cambio` | F-M09 | `PantallaCambioEvento` |
| M10 | `/alarma/:id/sonando` (pantalla completa) | F-M10 | `PantallaAlarmaSonando` |
| M11 | `/ajustes` | F-M11 | `PantallaAjustes` |

Más los diálogos **M06d** («¿Eliminar alarma?») y **M11d** («¿Cerrar sesión?») — ambos reutilizan `DialogoConfirmacion`, ya construido por la Persona A, como estado del componente padre (no son rutas). **M09 no lleva diálogo**: desde la v1.1 de los mockups «Eliminar» salió de M09 y quedó solo en M06 (`FUNCIONALIDADES.md` F-M09, `MOCKUPS.md` §3b paso 2); M09 solo tiene «Aceptar cambio», «Mantener alarma» y la «×» al margen derecho de la barra.

La arquitectura (Navigation 3, `Pantalla` sellada, `RepositorioDataset`, `Tokens.kt`, `HojaInferiorSceneStrategy`, `DialogoConfirmacion`, `NotificacionesAlarma`, `LocalSnackbarApp`, el patrón `entry<Pantalla.Mxx> { … }` dentro de `entradasApp`) **ya está fijada y construida** por el Plan 2 de la Persona A; este documento no la repite, solo señala las extensiones puntuales que necesita (§3). Las 7 claves de `Pantalla` y sus rutas **ya existen** en `navegacion/Pantalla.kt`; no hay que crearlas.

## 1 · Decisiones de esta spec

1. **M09 no necesita ningún dato nuevo en `dataset.json` — ya está ahí, sin usar.** La alarma `a-entrega` («Entrega de proyecto UX») ya trae, en las tres copias del dataset, un objeto `cambioDelOrganizador` (`nuevoInicio`, `nuevaHoraDeAlarma`, `antesSonaba`, `autor`, `motivo`) y un objeto `alSonar` (`salEnMin`, `traficoActual`, `rutaDisponible`) que M10 necesita. Ninguno de los dos aparece en `Modelos.kt` porque, como ya advierte su comentario de cabecera, solo se declaran los campos que alguna pantalla usa — nadie los había consumido todavía. Nada que preguntar aquí: solo exponerlos (§2).
2. **M08 «Compartir por WhatsApp» usa el share sheet real de Android** (`Intent.ACTION_SEND`, tipo `text/plain` con el enlace del QR): es una llamada de plataforma trivial, sin backend ni estado, y coincide literalmente con `TRAZABILIDAD.md` («→ hoja del SO»). Descargar como imagen y copiar enlace quedan **simulados** con el snackbar único (`LocalSnackbarApp`), igual que W05 hizo con «Descargar QR en lote» en la web.
3. **El detalle del día seleccionado en M02b se deriva**, no se agrega al dataset: `calendario.diasConAlarmas` ya trae el conteo por día; el detalle sale de filtrar `RepositorioDataset.alarmas` por la fecha de `eventoInicio` igual al día tocado. No hace falta ningún campo nuevo.
4. **Dos extensiones pequeñas a componentes compartidos ya construidos** (§3), en vez de crear componentes paralelos.

## 2 · Exponer datos que ya existen en `dataset.json`

No se toca ningún `dataset.json` (ni sus tres copias, ni `meta.version`): el contenido de `a-entrega` ya trae exactamente lo que M09/M10 necesitan (verificado en las tres copias, byte a byte iguales).

```json
"cambioDelOrganizador": {
  "nuevoInicio": "2026-08-30T17:30:00-05:00",
  "nuevaHoraDeAlarma": "2026-08-30T16:45:00-05:00",
  "antesSonaba": "2026-08-30T15:15:00-05:00",
  "autor": "MISO · UniAndes",
  "motivo": "Cambio de hora del evento"
},
"alSonar": { "salEnMin": 12, "traficoActual": "moderado", "rutaDisponible": true }
```

- `Modelos.kt`: `Alarma` gana `cambioDelOrganizador: CambioDelOrganizador? = null` y `alSonar: AlSonar? = null` (dos `data class` nuevas, campos de arriba — `nuevoInicio`/`nuevaHoraDeAlarma`/`antesSonaba` son ISO como `eventoInicio`/`suena`, se formatean con `FormatoHora.horaConSufijo(iso)` al mostrarlos, igual que el resto de la app; nunca texto pre-formateado en el dataset). Ambos quedan `null` para las cinco alarmas que no los traen — M09/M10 solo se alcanzan navegando desde `a-entrega` (T4), así que no hace falta un valor por defecto para las demás.
- `RepositorioDataset` gana `fun aplicarCambioOrganizador(id: String)`, que toma `alarma(id)?.cambioDelOrganizador` y llama a `agregar(alarma.copy(eventoInicio = cambio.nuevoInicio, suena = cambio.nuevaHoraDeAlarma))` — reutiliza `agregar`, que ya reemplaza por id; no hace nada si la alarma no tiene `cambioDelOrganizador`.
- `RepositorioDataset` gana también `_eventosQR` (mismo patrón que `_alarmas`: `MutableStateFlow<List<EventoQR>>` sembrado con `dataset.eventosQR`) y `fun agregarEvento(evento: EventoQR)`, porque hoy `eventosQR` solo se lee de `dataset` (no hay dónde guardar el evento nuevo de M07). `evento(id)` pasa a leer de `_eventosQR.value` en vez de `dataset.eventosQR` directamente.

## 3 · Extensiones a componentes compartidos

- **`BotonPrimario`/`BotonSecundario`** (`ui/componentes/Botones.kt`) ganan un parámetro `alto: Dp = Tamanos.Boton` (hoy 52, sin tocar ningún llamado existente) para las dos acciones de **56 pt** de M10 sobre fondo Tinta («dos acciones grandes equivalentes», `MOCKUPS.md` §3b paso 1; `CLAUDE.md` ya documenta esta altura como excepción: «acciones de la alarma sonando 56»). `BotonPrimario` gana además `sobreTinta: Boolean = false`: relleno blanco con texto Tinta, distinto de `sobreAmarillo` (relleno Tinta con texto blanco, la variante de M01) — es la «excepción M10» que ya nombran `CLAUDE.md` y `design-tokens.json` («sobre Tinta el primario es blanco»). «Ya voy · ver ruta» usa `BotonPrimario(sobreTinta = true, alto = 56)`; «Posponer 10 min» usa `BotonSecundario(sobreTinta = true, alto = 56)` (ya soportado, sin cambios).
- **`FilaAjuste`** (nuevo, `ui/componentes/FilaAjuste.kt`): fila de 40 pt, rótulo a la izquierda y `Interruptor` o «›» a la derecha, contenido centrado verticalmente (DS, revisión de tutores del 2026-09-19: «filas fijas de 40 pt con el contenido centrado y 4 pt de separación», `MOCKUPS.md` §7 comentario 8). Dos parámetros de acción independientes — `alTocarFila: (() -> Unit)? = null` (clic en el rótulo/fila) y el `Interruptor`/«›» con su propio `onClick`/`alCambiar` — porque M06 necesita ambos en la misma fila (ver abajo). La reutilizan M06 (sonido, «Respetar “No molestar”», «Alarma conectada · Confirmar antes de auto-ajustarse») y M11 (las once filas de ajustes agrupadas por sección con encabezado `sección · …` de 16 pt de relleno superior).

Ningún otro componente nuevo hace falta: `ChipControl` (anticipación/sonido en M06), `CampoTexto` (M07), `Casilla`, `CodigoQR`, `TarjetaAlarma`, `DialogoConfirmacion`, `ColumnaDesplazable`, `SnackbarDeshacer`/`LocalSnackbarApp` ya cubren el resto.

## 4 · Pantallas

Alturas, radios, colores y textura por `Tokens.kt` (nunca un número suelto); scroll vertical con `ColumnaDesplazable` salvo donde se anota `LazyColumn` (regla del README); `ViewModel` con `viewModel { … }` dentro de la `entry`, no fuera.

### M02b · Vista calendario (`/calendario`)

- Mes compacto (agosto 2026) con contador por día desde `calendario.diasConAlarmas`; día seleccionado resaltado (`calendario.diaSeleccionado` como valor inicial, mutable en memoria).
- Detalle del día: `LazyColumn` de `TarjetaAlarma` (ya construida) filtrando `alarmas` por fecha de `eventoInicio`; tarjeta → M06. Lista larga → regla README de `LazyColumn`.
- Barra inferior + FAB, igual que M02/M05 (`conNavegacionInferior`, `conFab` ya en la clave `Pantalla.M02b`); FAB toque/mantener → M12⏩/M03 o M02h, igual que en M02.
- Pestaña «Alarmas» de la barra inferior → M02.

### M06 · Editar alarma (`/alarma/:id`)

- `PantallaEditarAlarma(id, …)`: carga `repositorio.alarma(id)`.
- Anticipación (10 min / 30 min / 1 h / otro) y sonido (sonar / vibrar / silencio) como filas de `ChipControl` en una `ColumnaDesplazable`.
- «Alarma conectada · Confirmar antes de auto-ajustarse»: `FilaAjuste` con el switch de la preferencia **y** `alTocarFila` → M09⏩ con el `id` de la alarma en edición (tocar el rótulo simula la llegada del push del organizador, `NAVEGACION.md` §6; tocar el switch solo cambia la preferencia, sin navegar).
- «Gestionar en el calendario» → M02b.
- «Guardar cambios» → M02 (aplica los cambios en memoria vía `repositorio.agregar(alarma.copy(...))`).
- «Eliminar alarma» como `BotonEnlace` Coral (DS: el destructivo dentro de una pantalla es enlace, nunca píldora) → abre `M06d` con `repositorio.mensajeEliminar(alarma)` como cuerpo (ya genera el texto con `{evento}`/`{fecha}`/`{hora}`); «Conservar» cierra (vuelve a M06), «Eliminar» → `repositorio.eliminar(id)` → M02.
- Sin miga de pan (`MOCKUPS.md` §7 paso 1: «M06 sin miga»); «‹» → M02.

### M07 · Crear evento a mano (`/evento/nuevo`)

- `CampoTexto` para título, lugar (opcional), descripción (opcional); campo vacío muestra solo su etiqueta, sin valor de ejemplo ni placeholder (revisión de tutores, `MOCKUPS.md` §7 paso 5 noveno comentario). Fecha/hora sí precargadas con etiqueta + valor.
- Anticipación con `ChipControl`, igual que M06.
- «Guardar y crear QR»: arma una `Alarma` nueva (`origen = "creada-por-mi"`, `estado = "activa"`, `chips = listOf("Creada por mí")`) con id `"a-manual-" + System.currentTimeMillis()` (mismo esquema de prefijo que las del dataset, único por sesión — no persiste entre reinicios, consistente con «los datos viven en memoria mientras el proceso exista») y un `EventoQR` asociado (id `"e-" + id.removePrefix("a-")`, `codigoQR = "alarmasqr://evento/" + eventoId`, `escaneos = 0`, `etiqueta = "Aún sin escaneos · recién creado"`, mismo texto que ya usa `a-tutor`/`e-tutor` en el dataset). Ambos se agregan al repositorio (`agregar` para la alarma, `agregarEvento` para el evento — §2) y navega a M08 con el id del evento.

### M08 · QR del evento (`/evento/:id/qr`)

- `CodigoQR` (ya construido, ZXing) a tamaño grande con el `codigoQR` del evento; contador de escaneos (`EventoQR.escaneos`).
- «Compartir por WhatsApp» → `Intent.ACTION_SEND` real (decisión §1.2) con el enlace `codigoQR` como texto.
- «Descargar como imagen» → snackbar único (`LocalSnackbarApp`) con `dataset.mensajes.descargaCompletada` (ya existe, mismo mensaje que usa W05 en la web). «Copiar enlace» → snackbar con un mensaje nuevo `mensajes.enlaceCopiado` («Enlace copiado al portapapeles»); copia el `codigoQR` al portapapeles real (`ClipboardManager`, llamada de plataforma trivial, mismo criterio que el share sheet). Este es el **único** cambio real de `dataset.json` del plan (las tres copias, con `meta.version` de `1.2` a `1.3` y una nota nueva en `meta.notes`) — todo lo demás (§2) ya estaba ahí.
- «‹» → M02.

### M09 · Cambio del organizador (`/alarma/:id/cambio`)

- Bloque héroe «SI ACEPTAS, SONARÁ» con `alarma.cambioDelOrganizador`: `FormatoHora.horaConSufijo(nuevaHoraDeAlarma)` (4:45 pm), margen + trayecto de la alarma (`anticipacionMin`/`trayectoMin`, ya existen), «antes sonaba `FormatoHora.horaConSufijo(antesSonaba)`» (3:15 pm), `autor` sobre textura Tinta (banda de 120 pt, `MOCKUPS.md` §5).
- «Aceptar cambio» → `repositorio.aplicarCambioOrganizador(id)` → M10⏩ con el `id`.
- «Mantener alarma» → M02 (no cambia la alarma).
- «×» al margen derecho de la barra superior (v1.7, espaciador FILL en vez de fijo) → M02.
- El título del bloque de cambio → M06 (edición de la misma alarma).
- Si `alarma.cambioDelOrganizador` es `null` (cualquier alarma que no sea `a-entrega`), M09 no tiene contenido que mostrar — no ocurre en los recorridos documentados (T4 y el enlace de M06 solo se ejercitan sobre `a-entrega`), así que no hace falta un estado vacío dedicado; un `requireNotNull` con mensaje claro basta para que una prueba mal escrita falle rápido en vez de mostrar una pantalla rota.

### M10 · Alarma sonando (`/alarma/:id/sonando`, pantalla completa)

- Es el **destino real** de `NotificacionesAlarma.intentSonando` (ya wireado desde el Plan 2 — `MainActivity` resuelve el deep link con `Pantalla.porRuta`); hoy cae en el marcador porque M10 no está registrada. Debe llegar tanto por navegación normal (M09 → M10⏩) como por el deep link real de una alarma disparada.
- Fondo Tinta pleno + banda de textura blanca; ilustración pequeña de alarma sonando arriba; hora en Amarillo Energía 96 pt (Spline Sans Mono Bold, `Tokens.kt` ya trae el estilo); destacado «Sal en `alarma.alSonar.salEnMin` min» (tráfico `traficoActual`) con el enlace «Ver ruta ›» adentro.
- «Ver ruta ›» → `Intent.ACTION_VIEW` real a `geo:0,0?q=` + el `lugar` de la alarma codificado (abre la app de mapas del sistema, `TRAZABILIDAD.md`: «"Ver ruta ›" → mapas del SO») **solo si** `alarma.alSonar?.rutaDisponible == true`; si no, el enlace no se muestra. Mismo criterio de «llamada de plataforma trivial, sin backend» que el share sheet de M08.
- Dos acciones de **56 pt** ancladas abajo (§3): «Ya voy · ver ruta» (`BotonPrimario(sobreTinta = true, alto = 56)`, relleno blanco/texto Tinta — la excepción «sobre Tinta el primario es blanco») y «Posponer 10 min» (`BotonSecundario(sobreTinta = true, alto = 56)`, contorno blanco). Ambas → M02 (detienen y cierran el recorrido); ninguna abre el mapa, eso es solo el enlace «Ver ruta ›» del destacado.
- Igual que M09, `alarma.alSonar` puede ser `null` fuera de `a-entrega`; el destacado «Sal en…» no se dibuja si es `null` (la alarma sigue sonando igual, solo sin la sugerencia de tráfico).
- Sin barra superior, sin back stack normal (`esRaiz = true`, ya en la clave): `NavegacionApp` no debe mostrar navegación inferior sobre M10.

### M11 · Ajustes (`/ajustes`)

- Secciones (`FilaAjuste` agrupadas, encabezado `sección · …`): «No molestar» (switch), sonido predeterminado, anticipación por defecto, «Confirmar antes de auto-ajustar» (switch), permisos (alarmas exactas / notificaciones / batería sin restricciones, con advertencia si falta alguno — `Usuario.ajustes.permisos` ya trae los 4 booleanos), calendarios vinculados, gestión de datos (Ley 1581).
- «Cerrar sesión» (fila, no botón) → abre `M11d` con `dataset.mensajes.confirmarCerrarSesionCuerpo` (ya existe); «Cancelar» cierra (vuelve a M11), «Cerrar sesión» (contorno Tinta, no destruye) → M01.
- Barra inferior (`conNavegacionInferior` ya en la clave); pestaña «Alarmas» → M02.

## 5 · Verificación pixel-perfect

Mismo método que ya usa el Plan 2 (evolucionado respecto a la spec original: `ui/Verificacion.kt` con `capturar()` dibuja el `decorView` de la actividad de prueba directamente con Robolectric — `captureToImage()` cuelga bajo Robolectric en este proyecto, ver `docs/verificacion/README.md`), comparado contra la exportación del marco de Figma a 390×844 (ids de `docs/MOCKUPS.md` §5: M02b, M06, M06d, M07…M11, M11d). Evidencia en `docs/verificacion/<código>.png` y una fila nueva por pantalla en `docs/verificacion/README.md`.

## 6 · Pruebas

- JUnit: `RepositorioDataset.aplicarCambioOrganizador(id)`, `agregarEvento` + `agregar` desde M07 (evento + alarma nuevos), `eliminar` desde M06d.
- `createComposeRule` + Robolectric por pantalla, navegando por código (`pila.irA(Pantalla.M06("a-tutor"))`) y afirmando `testTag("pantalla-M06")`, igual que las pantallas de la Persona A.
- Flujo T2 (`M05 → M06`, ya en `TRAZABILIDAD.md` §4) y T4 (`M06 → M09 → M10 → M02`) como pruebas de extremo a extremo, análogas a `FlujosPersonaATest`; nombre sugerido `FlujosPersonaBTest`.
- `PantallasDesplazablesTest` gana M02b y M11 (`LazyColumn`/`ColumnaDesplazable`, scroll hasta el último elemento con `performScrollTo()` a 390×560, regla del README).
- Comandos: `./gradlew testDebugUnitTest lintDebug assembleDebug` desde `apps/movil`.

## 7 · Fuera de alcance

- Prueba manual de la alarma real en un dispositivo físico (checklist de `PLAN_MAQUETACION.md` §7): sigue pendiente, no se puede automatizar en este entorno; queda como paso manual explícito al final del plan, no como tarea del SDD.
- Cualquier cambio a las pantallas ya construidas por la Persona A o a la web (Plan 5, ya entregado).
