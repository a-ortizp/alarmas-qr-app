# Alarmas QR · Registro de funcionalidades

Documento de referencia del prototipo de baja fidelidad (mockups en `mockups/`).
Última actualización: 2026-09-19 (v2.5.7 — mockups móviles v1.7: iconos de línea en las tres opciones de calendario de M01 y corte de los botones Google/Outlook de M00a corregido, «×» de M09 al margen derecho, visor de M12 con el icono de escaneo a 48 px en vez de la cámara fotográfica y chip «Linterna · auto» de M03 con margen y 32 pt, por la revisión de los tutores; v2.5.6 — mockups web v1.5: propuesta de pantallas ejecutada, 24 marcos, W04 y W05 como secciones, error y recuperación de contraseña en W00, estados de filtro y búsqueda en W01, paginación y búsqueda en W03, diálogo «¿Cerrar sesión?»; v2.5.5 — revisión de los tutores sobre los mockups web: relleno 24 en botones, barra lateral con iconos y colapsable; v2.5.4 — diálogos de confirmación antes de eliminar una alarma (M04d, M06d, M09d) y de cerrar sesión (M11d), en wireframes, mockups y Design System, por el comentario general de los tutores sobre la navegación; v2.5.3 — revisión de los tutores sobre los wireframes web: buscador con lupa, paginación en W03, formato PNG/PDF compacto, «Completado» vuelve a Mis Alarmas, W00 más ancha, en wireframes y mockups; v2.5.2 — revisión de los tutores: botones móviles de relleno/contorno a 52 px en wireframes, mockups y Design System; v2.5.1 — observaciones de los mockups web aplicadas: gráfica semanal en W01, vista previa del afiche en W05, un primario por pantalla, Smart Animate; v2.5 — mockups de alta fidelidad de la web en la página `03 · Web` (14 marcos, W02 integrada en W01, W04/W05/W07 como modales); v2.4.2 — «Crear el evento a mano» como botón secundario en M03/M12/M02v por feedback de usuarios; v2.4.1 — corrección del gesto «mantener presionado» del FAB en el prototipo de mockups; v2.4 — segunda crítica: tarjeta única, calendario coherente, FAB toque/mantener, M10 «Ya voy»; v2.3 — mejoras de la crítica de diseño en los mockups: M02v, M02h, M03b, M10 con dos acciones, formato 12 h; v2.3b el mismo día: amarillo estricto, tonos de texto AA, textura en 6 pantallas, DS v1.1; v2.2 — mockups de alta fidelidad de la app móvil, ver `MOCKUPS.md`; v2.1 — conexiones del prototipo Figma unificadas en un solo recorrido y ajustes de copy tras la segunda prueba; v2 del 2026-08-23: nomenclatura F-Mxx / F-Wxx y alcance web replanteado).

**Concepto:** app para crear alarmas escaneando el código QR de un evento, sin digitar fecha, hora ni nombre. Dos productos complementarios:

- **App móvil (asistente y organizador):** 16 pantallas, M00a–M13 (incluye M02b, vista calendario), más tres auxiliares en los mockups de alta fidelidad: M02v (inicio sin alarmas), M02h (hoja «Agregar evento») y M03b (pantallazo recibido). Desde el 2026-09-17, cuatro **diálogos de confirmación**: M04d, M06d y M11d en ambos archivos, y M09d solo en los wireframes (en los mockups M09 no tiene «Eliminar»).
- **Web (administración y consulta):** 8 pantallas, W00–W07. La web ya **no crea ni edita eventos** (eso vive en el móvil, F-M07); es de consulta, reportes, difusión y gestión de la cuenta. En los **mockups de alta fidelidad** (página `03 · Web`, web v1.5 del 2026-09-19, ver `MOCKUPS.md` §7) son 24 marcos: W02 se integra en W01 como pestañas Todos / Creados / Escaneados y estados Pasados / Borradores / Búsqueda; W04 «Reportes» y W05 «Descargar QR» son secciones propias (fueron modales del 14 al 19 de septiembre); W07 es el modal «Eliminar cuenta» sobre W06; W00 tiene estados de error de credenciales, recuperación de contraseña y correo enviado; la barra lateral lleva iconos, se colapsa a 64 px y su «Cerrar Sesión» abre el diálogo «¿Cerrar sesión?».

**Persona:** Andrés Rojas, 29 años, Android gama media, "el profesional del pantallazo".

---

## App móvil · Asistente

### F-M00a · Registro de usuario — pantalla M00a
- Creación de cuenta **opcional** con correo/contraseña o cuentas de terceros (Google, Outlook) para respaldar la información en la nube.
- "Continuar como invitado" siempre disponible: sin cuenta, las alarmas quedan solo en el celular.
- Consentimiento de datos (Ley 1581).

### F-M00b · Inicio de sesión — pantalla M00b
- Autenticación con credenciales existentes para sincronizar alarmas en la nube (y habilitar la web, F-W00), recuperación de contraseña, acceso con terceros.
- Modo invitado como alternativa permanente.

### F-M01 · Onboarding y vinculación opcional de calendario — pantalla M01
- Guía de bienvenida en 3 pasos para aprender el gesto de escaneo.
- Conexión opcional de Google Calendar, Outlook/Teams o calendario del teléfono; se puede posponer y activar luego en Ajustes.
- En los mockups (v1.7, revisión de los tutores del 2026-09-19) cada opción lleva un icono de línea al inicio de la fila (monograma «G», sobre, smartphone) para distinguirlas de un vistazo, además del rótulo.

### F-M02 · Consulta y visualización de alarmas programadas — pantallas M02 / M02b / M05
- Listado cronológico agrupado por día con horarios, anticipación configurada y lugar.
- **Vista calendario conmutable (M02b)**: mes compacto con contadores de alarmas por día y detalle del día seleccionado — agregada tras la prueba de usabilidad (CM-01).
- Accesos directos de captura: "Escanear QR del evento" y "Elegir pantallazo de la galería"; botón "+" flotante grande (CM-02).
- Etiquetas de origen ("Mía · QR" / escaneada, "Nueva").
- **Conexiones del prototipo (mockups v1.3):** FAB «Escanear»: toque → M12 (⏩ primer uso) → M03; mantener presionado ≥ 0,5 s («Mouse down» con retardo; el toque corto es «Mouse up») → hoja M02h «Agregar evento»; en la hoja, «Escanear el QR del evento» → M12 (⏩ primer uso, permiso pendiente) → M03, «Elegir pantallazo de la galería» → M03b → M04, «Crear el evento a mano» → M07; tocar una alarma → M06; pestaña Calendario → M02b; Ajustes → M11. Primer uso: «Crear cuenta» / «Continuar como invitado» (M00a) → M02v (sin alarmas) con «Escanear QR del evento» → M12. En los wireframes siguen los dos botones superiores y el «+» → M07.

### F-M03 · Escaneo y lectura dual de códigos QR — pantalla M03
- Captura en tiempo real con la cámara, linterna automática y vibración al detectar.
- Importación de pantallazos guardados en la galería (lectura "dual").
- En los mockups (v1.7) «Linterna · auto» es un control de 32 pt en la barra superior, con el margen de 16 del resto de la barra; no cambia de pantalla.

### F-M04 · Confirmación automática y descarte de alarma — pantalla M04
- **La alarma se programa inmediatamente al escanear** (sin botón "Guardar"): la tarjeta muestra los datos del evento (título, fecha/hora con zona, lugar, descripción, organizador verificado).
- Aviso calculado: margen + trayecto desde la ubicación habitual, editable.
- Opción **"Eliminar alarma · no puedo asistir"** para descartarla; "Listo" confirma y regresa.
- **Confirmación antes de descartar (desde el 2026-09-17, comentario general de los tutores):** el enlace abre el diálogo **M04d «¿Eliminar alarma?»** («Dejarás de recibir el aviso de “Entrega de proyecto UX” (vie 30 · 4:00 pm). Si cambias de opinión, puedes volver a escanear el QR del evento.») con «Conservar» (primario, vuelve a M04) y «Eliminar» (contorno; → M02). Tocar el velo equivale a «Conservar».
- **Presentación (desde el 2026-09-17, revisión de tutores):** M04 es una **hoja inferior (modal)** sobre la lista de alarmas atenuada, no una pantalla con barra superior: asa, «¡Alarma programada!», chip «Datos leídos del QR», tarjeta del evento, bloque «Sonará…», «Listo» y el enlace de descarte; tocar el velo también cierra y vuelve a M02. Se retira la flecha «‹» (volver al escáner ya no aplica a una confirmación).

### F-M05 · Cancelación rápida "Deshacer" — pantalla M05
- Retorno a la lista con la alarma resaltada, snackbar "Alarma guardada · También en Google Calendar" y **Deshacer (5 s)**.

### F-M06 · Edición y ajuste de alarmas — pantalla M06
- Personalización independiente por alarma: anticipación (10 min / 30 min / 1 h / otro, + trayecto), sonido (sonar / vibrar / silencio), respetar "No molestar", posponer, eliminar.
- Alarma "conectada" con opción de pedir confirmación antes de auto-ajustarse; gestión del evento en el calendario.
- **Eliminar pide confirmación (2026-09-17):** diálogo M06d con la consecuencia (evento y hora) y la acción segura prominente; la eliminación solo ocurre al tocar «Eliminar» en el diálogo.
- **Conexiones del prototipo:** el bloque "Alarma conectada · Confirmar antes de auto-ajustarse" → M09 (⏩ simula la llegada del push del organizador); "Guardar cambios" y "‹" → M02; "Eliminar alarma" → diálogo **M06d «¿Eliminar alarma?»** (desde el 2026-09-17; «Conservar» → M06, «Eliminar» → M02); "Gestionar en el calendario" → M02b.

### F-M07 · Creación manual con generación de QR — pantalla M07
- Formulario para compromisos propios (título, fecha, hora, lugar y descripción opcionales, anticipación) directamente en el dispositivo.
- **El código QR se genera automáticamente al guardar** ("Guardar y crear QR").

### F-M08 · Difusión y descarga de QR por alarma — pantalla M08
- Visualización del QR de **un evento propio**, creado a mano en M07. Hasta el 2026-09-24 esta línea decía
  «cualquier alarma guardada (propia o escaneada)», pero el marco `5:2` de M06 no tiene control de compartir y una
  alarma escaneada es de un evento ajeno, del que el asistente no difunde el QR (ver `NAVEGACION.md` §7).
- Compartir por WhatsApp, correo o redes; descargar como imagen (PNG/PDF); copiar enlace; contador de escaneos.

### F-M09 · Sincronización push y reajuste por cambios — pantalla M09
- Push cuando el organizador modifica hora o lugar, con detalle antes → después, autor del cambio y alarma recalculada (calendario actualizado).
- Tres salidas: **«Aceptar cambio»** (aceptar; hasta el 2026-09-17 «Perfecto, así queda»), **«Mantener alarma»** (conservar la previa; antes «Mantener mi alarma anterior») o **«Eliminar alarma»**. Regla de la revisión de los tutores: rótulos de botón de máximo dos palabras.
- **Conexiones del prototipo:** M09 ya no es un punto de inicio aparte; se llega desde M06. «Aceptar cambio» (mismo rótulo en wireframes y mockups desde el 2026-09-17; antes «Perfecto, así queda» / «Aceptar el cambio») → M10 (⏩ salto temporal a las 4:45 pm, la hora recalculada); «Mantener alarma» → M02; el título del cambio → M06; «Eliminar alarma» → diálogo **M09d «¿Eliminar alarma?»** (wireframes, desde el 2026-09-17; «Conservar» → M09, «Eliminar» → M02). En los mockups v1.1 «Eliminar alarma» sale del cuerpo de M09 (queda en M06) y la barra tiene «×» → M02.
- En los mockups (v1.7) la «×» de cierre de la barra superior queda al margen derecho; sigue llevando a M02.

### F-M10 · Disparo de alarma contextual con tiempo de viaje — pantalla M10
- Interfaz de sonido con el evento, lugar y sugerencia "Sal en X min" según el tráfico actual.
- Botones directos **"Ya voy · ver ruta"** y **"Posponer 10 min"**; silenciar solo esta vez; re-aviso en 5 min si no confirma; solo vibra en reunión/clase.
- **Conexiones del prototipo:** M10 ya no es un punto de inicio aparte; se llega desde M09. "Ya voy" (detiene), "Posponer 10 min" y el enlace "Ver ruta ›" del destacado → M02, cerrando el recorrido. En los mockups v1.3 son dos acciones grandes equivalentes más el enlace de ruta; el selector 5/10/15 y «Silenciar solo esta vez» solo existen en los wireframes.

### F-M11 · Configuración de persistencia, sonido y privacidad — pantalla M11
- "No molestar", sonido predeterminado, anticipación por defecto, confirmación ante cambios.
- Permisos de alarmas exactas, notificaciones y batería sin restricciones (con advertencia).
- Gestión de datos bajo Ley 1581; calendarios; cerrar sesión.
- **Cerrar sesión pide confirmación (desde el 2026-09-17):** la fila «Cerrar sesión» abre el diálogo **M11d «¿Cerrar sesión?»** («Tus alarmas quedan guardadas en tu cuenta. Para volver a verlas y recibir los cambios del organizador tendrás que iniciar sesión de nuevo.») con «Cancelar» (primario, vuelve a M11) y «Cerrar sesión» (contorno Tinta, no coral: no destruye datos) → M01. Antes la fila no tenía conexión en el prototipo.

### F-M12 · Solicitud y gestión de permisos de cámara — pantalla M12
- Guía para otorgar/activar el permiso (por qué se necesita, política de no guardar fotos, pasos en Android, "Abrir ajustes").
- Salidas alternativas: pantallazo de la galería o ingreso manual.
- **Conexiones del prototipo:** M12 ya no es un punto de inicio aparte; se llega desde M02 "Escanear QR" (⏩ primer uso). "Abrir ajustes" → M03 (permiso concedido); "Elegir pantallazo" → M04 (en los mockups → M03b → M04); "Crear el evento a mano" → M07; "‹" → M02. Los reintentos desde M13 van directo a M03, así el permiso se pide una sola vez.
- En los mockups (v1.7) el visor apagado muestra el icono de escaneo del DS a 48 px (vista de escaneo del teléfono) en vez de una cámara fotográfica, por la revisión de los tutores.

### F-M13 · Protección contra códigos QR inválidos — pantalla M13
- Detección de QR que no contienen eventos (menús, pagos, enlaces) con diagnóstico de contenido.
- **Bloqueo de apertura automática** de enlaces (anti-quishing); recuperación: reescanear, crear a mano o abrir bajo decisión explícita.

---

## Web · Administración y consulta

### F-W00 · Inicio de sesión general — pantalla W00
- Acceso con correo y contraseña para **cualquier usuario registrado** en el sistema (la cuenta se crea desde el móvil, F-M00a).
- **Mockups web:** W00 `4072:1861`, tarjeta centrada con la marca, «Iniciar sesión» → W01 y la nota «¿Aún no tienes cuenta? Créala desde la app móvil»; variante W00 (cuenta eliminada) `4072:1878` con snackbar «Cuenta eliminada exitosamente». Web v1.5: error de credenciales `4362:474` (campo en Coral con «Correo o contraseña incorrectos…», disparado desde el campo de contraseña), «¿Olvidaste tu contraseña?» → «Recuperar contraseña» `4362:427` («Enviar enlace», «‹ Volver a iniciar sesión») → W00 con snackbar «Te enviamos un correo de recuperación a andres@correo.com» `4362:449` (vuelve sola a los 3 s).

### F-W01 · Tablero de control y métricas agregadas — pantalla W01
- Métricas consolidadas: eventos activos (propios/escaneados), escaneos totales, alarmas activas con % de conversión, confirmaciones "Ya voy".
- Gráfica de escaneos por semana; accesos directos a reportes (W04) y afiches (W05).
- **Mockups web:** W01 «Mis Alarmas» `4072:26` es el hub: cuatro indicadores (6 eventos activos · 128 escaneos · 97 alarmas activas, conversión 76 % · 41 «Ya voy») sobre la tabla de eventos, más los botones «Exportar reporte» (→ W04 Reportes) y «Descargar QR en lote» (contorno, → W05 Descargar QR). Desde la web v1.1 incluye la tarjeta «Escaneos por semana» (8 semanas, total 128); desde la web v1.4 la barra lateral lleva un icono por ítem y se colapsa a 64 px (estado «menú colapsado» `4357:1868`).

### F-W02 · Visualización de eventos propios y escaneados — pantalla W02
- Listado centralizado de eventos **generados por el usuario** y eventos **a los que se suscribió escaneando** un QR (etiquetas Propio / Escaneado).
- De los escaneados solo se ve el estado de la propia alarma; sus métricas pertenecen al organizador.
- **Mockups web:** integrada en W01 como pestañas Todos / Creados (`4072:49`) / Escaneados (`4072:72`), chips «Creada por mí» / «✓ Escaneada», estado «Publicado», «Ver detalle ›» → W03 y la nota «Las métricas de eventos escaneados pertenecen a su organizador». Web v1.5: estados Pasados `4361:367` (eventos finalizados), Borradores `4361:560` («Sin resultados con estos filtros» + «Ver todos») y Búsqueda `4361:760` («Sem» → Seminario UX, «Limpiar»).

### F-W03 · Monitoreo anónimo de asistencia — pantalla W03
- Conversiones por evento (escaneos, alarmas activas/eliminadas, calendario, "Ya voy").
- Lista de asistentes mostrando **únicamente alias o iniciales** (Ley 1581 — sin teléfonos ni correos); búsqueda y exportación.
- **Mockups web:** W03 «Detalle Evento» `4072:95` (Partido Sintética): miga de pan, 16 escaneos · 11 alarmas activas (5 la eliminaron) · 7 «Ya voy», tabla «Quiénes escanearon» con alias (Joale7, Mike1008, J.P, CarrosC6), estado Activa / Eliminada, buscador con lupa y pie «Mostrando 1–4 de 16 asistentes · 4 por página» con paginador (web v1.3). Web v1.5: página 2 `4364:430` (LauM, Nico_R, D.G, Vale22), búsqueda de asistente `4364:583` («Mi» → Mike1008, «Limpiar») y botón secundario «Exportar reporte» → W04.

### F-W04 · Exportación de reportes consolidados (PDF / CSV) — pantalla W04
- Descarga de informes con métricas agregadas de asistencia, porcentaje de escaneos y conversión por evento.
- Filtros de rango de fechas y eventos; contenido configurable; solo datos anónimos.
- **Mockups web:** sección «W04 · Reportes» (`4072:118`; modal sobre W01 hasta la web v1.4): ítem activo en la barra, miga «‹ Mis alarmas / Reportes», tarjeta «Exportar reporte consolidado» con rango Último mes / Semestre / Rango personalizado (fechas, `4072:141`), formato PDF / CSV, nota Ley 1581, «Generar y descargar»; estado Listo (`4072:164`) con «reporte-alarmasqr-ago2026.pdf generado y descargado exitosamente» y «Generar de nuevo»; al lado, tarjeta «Reportes generados» con tres archivos y «Descargar de nuevo». Se llega desde la barra lateral, desde «Exportar reporte» de W01 y desde W03.

### F-W05 · Gestión y descarga de afiches en lote — pantalla W05
- Galería de piezas gráficas con el QR listo para imprimir (marca + "Escanéalo y te avisamos", QR mínimo 4×4 cm).
- **"Descargar todos"** en una sola acción (PNG + PDF) para los eventos activos.
- **Mockups web:** sección «W05 · Descargar QR» (`4072:187`; modal sobre W01 hasta la web v1.4): ítem activo en la barra, miga → W01, tarjeta de selección («Seleccionar todos» 2 de 2, lista con QR y estado, formato PNG / PDF segmentado, «Cancelar» / «Descargar») y, en la columna derecha, «Vista previa del afiche» (marca Alarmas QR + QR + nombre del evento + «Escanéalo y te avisamos»; QR mínimo 4 × 4 cm); estado Completado (`4072:210`) = W01 con snackbar que vuelve sola a los 3 s.

### F-W06 · Buscador y filtros avanzados de eventos — integrada en la pantalla W02
- Filtrado por rango de fechas, estado (próximos / pasados) y tipo (propios / escaneados), más búsqueda por título o lugar, para auditar el historial.
- **Mockups web:** en W01, buscador «Buscar por nombre» con lupa + filtros Próximos / Pasados / Borradores + pestañas Todos / Creados / Escaneados; desde la web v1.5 los filtros y el buscador responden con sus propios estados (Pasados, Borradores sin resultados, Búsqueda con «Limpiar»).

### F-W07 · Edición de perfil de usuario — pantalla W06
- Datos de la cuenta: nombre para mostrar, foto opcional, correo, cambio de contraseña.
- **Privacidad ante organizadores** (Ley 1581): elegir cómo aparecer en "Quiénes escanearon" (nombre completo / solo iniciales / alias) y qué compartir (estado de alarma, "Ya voy").
- "Zona de riesgo" con acceso a la eliminación de cuenta (W07).
- **Mockups web:** W06 «Ajustes de Perfil» `4072:233`: tarjeta Perfil (nombres, alias público, correo, «Guardar cambios» → estado Actualizado `4072:279` con snackbar «Perfil actualizado»), tarjeta «Privacidad ante organizadores» (Nombre completo / Solo iniciales / Alias; switches «Mostrar el estado de mi alarma» y «Contar mi “Ya voy” en las métricas») y tarjeta coral «Eliminación de cuenta». Nota: «Los cambios de perfil no afectan tus alarmas en el celular».

### F-W08 · Eliminación definitiva de cuenta — pantalla W07
- Modal de confirmación con **consecuencias explícitas**: eventos despublicados (sus QR dejan de funcionar), alarmas de asistentes sin actualizaciones, borrado de datos en máximo 30 días (habeas data · Ley 1581).
- Fricción deliberada: escribir "ELIMINAR" para confirmar; la acción segura ("Conservar mi cuenta") es la prominente; consejo de descargar reportes antes.
- **Mockups web:** «W06 · Modal eliminar cuenta» `4072:256` sobre Ajustes de Perfil: 4 eventos despublicados, 97 alarmas de asistentes sin actualizaciones, borrado en máximo 30 días, consejo de descargar reportes, campo «Escribe ELIMINAR», «Conservar mi cuenta» (amarillo) y «Eliminar definitivamente» (contorno coral) → W00 con «Cuenta eliminada exitosamente».

---

## Funcionalidades transversales

- **Cuenta y sincronización:** registro/login opcionales (F-M00a/b) respaldan alarmas en la nube y conectan el móvil con la web (F-W00); el modo invitado mantiene todo local.
- **Aviso con trayecto:** anticipación = margen + desplazamiento, con tráfico en tiempo real al sonar (F-M04, F-M06, F-M10).
- **Calendarios:** Google, Outlook/Teams y teléfono — alta, actualización ante cambios y quitar (F-M01, F-M04, F-M05, F-M06, F-M09, F-M11).
- **Alarmas conectadas:** cambios del organizador propagados con push; ajuste automático o con confirmación (F-M06, F-M09, F-M11).
- **Privacidad · Ley 1581:** consentimiento, gestión de datos, anonimización y reportes solo agregados (F-M00a, F-M11, F-W01, F-W03, F-W04).
- **Seguridad anti-quishing:** enlaces desconocidos no se abren solos; QR siempre con marca y contexto (F-M13, F-W05).
- **Confiabilidad:** permisos exactos/notificaciones/batería, "No molestar" con vibración, re-aviso (F-M10, F-M11).
- **Prevención de errores (2026-09-17):** ninguna acción irreversible o de salida se ejecuta en un toque; eliminar una alarma (M04d, M06d, M09d), cerrar sesión (M11d) y eliminar la cuenta (W07) pasan por un diálogo con la consecuencia explícita, la acción segura como botón prominente y la confirmación en contorno (coral si destruye). Complementa el «Deshacer» de M05 (F-M05): confirmación para lo irreversible, reversa para lo frecuente. En la web (2026-09-19): «Cerrar Sesión» abre el diálogo «¿Cerrar sesión?» («Cancelar» → W01, «Cerrar sesión» → W00) y eliminar la cuenta sigue pasando por su modal.

## Mapa funcionalidad → pantalla

| Funcionalidad | Pantalla | | Funcionalidad | Pantalla |
|---|---|---|---|---|
| F-M00a | M00a | | F-M07 | M07 |
| F-M00b | M00b | | F-M08 | M08 |
| F-M01 | M01 | | F-M09 | M09 |
| F-M02 | M02 · M02b · M05 | | F-M10 | M10 |
| F-M03 | M03 | | F-M11 | M11 |
| F-M04 | M04 | | F-M12 | M12 |
| F-M05 | M05 | | F-M13 | M13 |
| F-M06 | M06 | | F-W00…F-W05 | W00…W05 |
| | | | F-W06 | W02 (integrada) |
| | | | F-W07 | W06 |
| | | | F-W08 | W07 |

En los **mockups web** (página `03 · Web`, web v1.5 del 2026-09-19): F-W00 → W00 (+ error de credenciales, recuperar contraseña, correo enviado) · F-W01, F-W02 y F-W06 → W01 «Mis Alarmas» (indicadores + tabla con pestañas y estados Pasados / Borradores / Búsqueda) · F-W03 → W03 (+ página 2 y búsqueda de asistente) · F-W04 → sección «W04 · Reportes» (antes modal) · F-W05 → sección «W05 · Descargar QR» (antes modal) · F-W07 → W06 · F-W08 → modal «Eliminar cuenta» sobre W06 · cierre de sesión → diálogo «¿Cerrar sesión?».

## Convenciones visuales de los mockups

- Negro relleno = acción principal.
- Caja con X = imagen / cámara / logo (placeholder).
- Fondo amarillo = dato sugerido por la app.
- Línea punteada = separador / opcional.

## Tareas de la prueba de usuario (guía Main)

| Tarea | Descripción | Flujo |
|---|---|---|
| T1 | Escanear un QR: la alarma queda guardada | M02 → M12 → M03 → M04 → M05 |
| T2 | Cambiar la anticipación y el modo de sonido | M05 → M06 |
| T3 | Crear tu propio evento y compartir el QR | M02 → M07 → M08 |
| T4 | Reaccionar a un cambio de hora | M05/M02 → M06 → M09 → M10 → M02 |
| T5 | Web: entrar, filtrar eventos y bajar reporte y afiches | W00 → W01 → W02 → W03 → W04 · W05 (mockups: W00 → W01 con pestañas y filtros → W03 → secciones W04 · W05) |
| T6 | Errores: cámara sin permiso y QR que no es evento | M02 → M12 → M03 · M03 → M13 → M03 |
| T7 | Crear cuenta o continuar como invitado | M00a · M00b |
| T8 | Web: editar el perfil y eliminar la cuenta | W06 → W07 (mockups: W06 → modal «Eliminar cuenta» → W00 «Cuenta eliminada») |

## Recorrido unificado del prototipo interactivo (Figma)

Desde el 2026-09-04 el prototipo móvil tiene **un solo punto de inicio (M01)** en vez de cuatro (M01, M09 push, M10 sistema, M12 desvío), para que quien prueba lo recorra de principio a fin sin cambiar de flujo. Los tres eventos externos se simulan con controles ya existentes (⏩): M02 "Escanear QR" → M12 · M06 "Alarma conectada" → M09 · M09 «Aceptar cambio» (antes «Perfecto, así queda») → M10. El prototipo web ya era un único flujo desde W00. Detalle paso a paso en `NAVEGACION.md` §6.

Prototipo: https://www.figma.com/proto/epn1MSPTAFtO0pDOcPdbAv/Wireframes-Alarmas-QR-Equipo-UX?node-id=3-131&p=f&scaling=min-zoom&content-scaling=fixed&page-id=1%3A6&starting-point-node-id=3%3A71&show-proto-sidebar=1

Prototipo de interacción en alta fidelidad (mockups, 2026-09-07, mismo recorrido y punto de inicio M01): https://www.figma.com/proto/4nHD4ygcnP33UH0gAhaii5/Mockups-Alarmas---QR-Equipo-UX?node-id=3-71&p=f&scaling=min-zoom&content-scaling=fixed&page-id=1%3A6&starting-point-node-id=3%3A71&show-proto-sidebar=1

Prototipo web en alta fidelidad (mockups, página `03 · Web`, 2026-09-14, abre en W00): https://www.figma.com/proto/4nHD4ygcnP33UH0gAhaii5/Mockups-Alarmas---QR-Equipo-UX?node-id=4072-1861&p=f&scaling=min-zoom&content-scaling=fixed&page-id=4072%3A2&starting-point-node-id=4072%3A1861&show-proto-sidebar=1

## Historial de cambios funcionales

| Fecha | Alcance | Cambio |
|---|---|---|
| 2026-09-19 (v2.5.7) | Mockups móviles v1.7 · M01 | Revisión de los tutores sobre los mockups móviles (comentario en M01: «Agregar iconos para mejor distinción entre las opciones»): **F-M01** las filas Google Calendar, Outlook · Teams y Calendario del teléfono llevan un icono de línea de 20 pt al inicio (tres componentes nuevos en `00 · Recursos gráficos`: `icono · google`, `icono · outlook`, `icono · teléfono`). Segundo comentario (M00a: «Corte extraño en el frame» en los botones Google / Outlook): el marco de la fila estaba fijo en 48 pt y recortaba los botones de 52; pasa a ajustar al contenido (**F-M00a**, solo visual). Tercer comentario (M09: «x alineada a la derecha»): la «×» de cerrar de la barra superior queda pegada al margen derecho (**F-M09**, solo visual; su conexión → M02 no cambia). Cuarto comentario (M12: icono de cámara «no tan grande» y que muestre el uso desde el celular): la cámara fotográfica de 108×90 se reemplaza por el icono de escaneo del DS (visor con QR) a 48 px (**F-M12**, solo visual). Quinto comentario (M03: «Agregar padding» en «Linterna · auto»): el chip estaba pegado al borde derecho y medía 19 pt; ahora respeta el margen de 16 y es un control de 32 pt (**F-M03**, solo visual). Sin cambios de navegación ni de altura de pantalla. `Mockups_Figma_Movil.pdf` con las páginas M01, M00a, M03, M09 y M12 reexportadas. Wireframes sin cambios. |
| 2026-09-19 (v2.5.6) | Mockups web v1.5 (24 marcos) | `PROPUESTA_PANTALLAS_WEB.md` ejecutada (`MOCKUPS.md` §7.7): **F-W00** error de credenciales (⏩ desde el campo de contraseña), «¿Olvidaste tu contraseña?» → «Recuperar contraseña» → snackbar «Te enviamos un correo de recuperación…»; **F-W02/F-W06** los filtros Pasados y Borradores y el buscador responden (estados Pasados, Borradores sin resultados y Búsqueda con «Limpiar»); **F-W03** paginación (página 2), búsqueda de asistente y botón «Exportar reporte» → W04; **F-W04** y **F-W05** dejan de ser modales y son secciones propias (W04 · Reportes con «Reportes generados»; W05 · Descargar QR con la vista previa del afiche al lado); **transversal** «Cerrar Sesión» abre el diálogo «¿Cerrar sesión?» (Cancelar → W01, Cerrar sesión → W00). 24 marcos, 220 conexiones, un solo punto de inicio. `Mockups_Figma_Web.pdf` regenerado (24 págs.). Los wireframes no cambian. |
| 2026-09-19 (v2.5.5) | Mockups web v1.4 (15 marcos) | Revisión de los tutores sobre los mockups web (`MOCKUPS.md` §7.6). Comentario general («solo pude navegar por 3 pantallas»): el prototipo se verificó completo; la causa es estructural (solo W01, W03 y W06 son páginas) y se atiende con `PROPUESTA_PANTALLAS_WEB.md` (F-W00 con error y recuperación de contraseña, F-W02/F-W06 con estados de filtro y búsqueda, F-W03 con paginación, F-W04/F-W05 como secciones, confirmación de cierre de sesión), pendiente de ejecutar. Aplicados: **transversal web** relleno horizontal 24 en los botones de 44 pt; **barra lateral** (F-W01…F-W08) con icono en cada ítem y control «colapsar menú», y estado «menú colapsado» de 64 pt en W01 (`4357:1868`, conectado en ambos sentidos). `Mockups_Figma_Web.pdf` regenerado (15 págs.). |
| 2026-09-17 (v2.5.4) | Wireframes Figma (20 marcos) · Mockups Figma v1.6 (22 marcos) · Design System v1.7 · láminas HTML/PDF | Comentario general de los tutores sobre la navegación de los wireframes móviles: «El prototipo interactivo funciona, pude navegar por todas las pantallas, les sugiero agregar modales de verificación y prevención de errores, por ejemplo cuando el usuario da clic en eliminar algo, que prevenga al usuario si está seguro de realizar esas acciones». Aplicado en wireframes y mockups: **F-M04, F-M06 y F-M09** — los enlaces «Eliminar alarma» ya no van directo a M02 sino a un diálogo de confirmación (M04d, M06d y, solo en wireframes, M09d) con título «¿Eliminar alarma?», la consecuencia (evento y hora) y dos botones de máximo dos palabras: «Conservar» (primario relleno, vuelve a la pantalla) y «Eliminar» (contorno, coral en alta fidelidad) → M02; tocar el velo equivale a «Conservar». **F-M11** — la fila «Cerrar sesión», que no tenía conexión, abre M11d «¿Cerrar sesión?» con «Cancelar» (primario) y «Cerrar sesión» (contorno Tinta, no destruye datos) → M01. Técnica: marco = pantalla de origen atenuada (clon sin conexiones) + velo Tinta 55 % + diálogo centrado de 342 pt (relleno 24, r20, título Bricolage SemiBold 22 / Archivo Bold 20 en wireframes, cuerpo Archivo 14). Conexiones: wireframes 59 → 72, mockups 72 → 82 (Smart Animate 250 ms), un solo punto de inicio en M01, ninguna sobre texto. Design System v1.7: set **47 · Diálogo de confirmación · móvil** (variantes Eliminar alarma / Cerrar sesión) en L08. Regenerados `Prototipo_Figma_Movil.pdf` (20 págs), `Mockups_Figma_Movil.pdf` (22 págs), `Design_System_Alarmas_QR.pdf` (L08) y `Wireframes_Alarmas_QR.html/.pdf` (lámina 16 nueva, v1.3). El canvas de baja fidelidad de `mockups/` gana cuatro artboards (`M04dConfirmarEliminar`, `M06dConfirmarEliminar`, `M09dConfirmarEliminar`, `M11dConfirmarCerrarSesion`, fila nueva bajo M10–M13 con su nota amarilla), la guía Main los lista, el canvas publicado 📱 se republicó y los cuatro `Mockups_Alarmas_QR*.pdf` se regeneraron con `build_print.py`. |
| 2026-09-17 (v2.5.3) | Wireframes web (página `03 · Web` nueva, 14 pantallas) · Mockups web v1.3 | Revisión de los tutores sobre los wireframes web (nueve comentarios, `MOCKUPS.md` §7.5). Evaluados contra ambos archivos: cuatro ya estaban resueltos en los mockups (segmentos de filtro que no llenaban el alto y descentrados en W01, tarjetas de W03 de distinto alto, PDF/CSV de 64 px en W04) y se corrigieron solo en los wireframes; cinco se aplicaron en los dos: **F-W02/F-W06** el buscador muestra un único placeholder «Buscar por nombre» con ícono de lupa (también «Buscar asistente» en W03); **F-W03** el pie de la tabla de asistentes indica «Mostrando 1–4 de 16 asistentes · 4 por página» con paginador ‹ 1 2 3 4 › (sustituye a «Ver todos»); **F-W05** el formato PNG/PDF pasa a control segmentado compacto para que «Descargar» sea la única acción dominante, y al completarse la descarga el modal se cierra, se vuelve a W01 con la snackbar «Descarga completada exitosamente» y esta desaparece sola a los 3 s (After delay → W01); **F-W00** la tarjeta de acceso pasa a 520 px de ancho (correos largos). Regenerados `Mockups_Figma_Web.pdf` (14 págs) y `Prototipo_Figma_Web.pdf` (ahora 14 págs, exportado de la página nueva de wireframes web). |
| 2026-09-17 (v2.5.2) | Wireframes Figma (16 pantallas) · Mockups Figma v1.5 (19 pantallas) · Design System v1.6 · láminas HTML/PDF | Revisión de los tutores sobre los wireframes móviles (comentario general: «ajustes agregados directamente en el Figma, pequeñas mejoras y mejor uso de espaciados y estructura»; comentario en M01: el botón «Comenzar» es demasiado delgado para pulgares anchos, recomiendan 52 px). Aplicado a toda acción principal para mantener un solo estándar: los botones de relleno y de contorno pasan a **52 px** en los wireframes (25 botones, antes 44), en los mockups móviles (26 botones, antes 48) y en el Design System (regla «52 toque móvil / 44 puntero web», sets 01/02/03 y comp. 32/35). Enlaces de texto (32), acciones de la alarma sonando (56) y FAB (56) sin cambios. Segundo comentario (M00a: «agregar un mayor padding bottom» junto a «¿Ya tienes cuenta? Inicia sesión»): los enlaces de pie de M00a y M00b pasan a un marco de control de 44 px con 32 px al borde (antes texto suelto a 16 px) y sus conexiones M00a ↔ M00b se mueven del texto al marco; relleno inferior de 32 también en M01 y en la hoja de M03 (wireframes) y en M03 y M06 (mockups). Tercer comentario (M00b: «centrar la composición»): el formulario de M00b y M00a se centra verticalmente entre la parte superior y el pie anclado (espaciador FILL arriba y abajo) en wireframes, mockups y láminas HTML. Cuarto comentario (M02: «eliminar el background» de la alarma pausada «Gimnasio»): la tarjeta pausada pierde el fondo gris en M02/M02b/M05 (wireframes y mockups, más M02h) y en las láminas HTML; «pausada» se sigue leyendo por el texto gris, la etiqueta y el switch apagado. Quinto comentario (M09: «los títulos de los botones están muy largos, pueden confundir; usar máximo 2 palabras por botón»): «Perfecto, así queda» / «Aceptar el cambio» → **«Aceptar cambio»** y «Mantener mi alarma anterior» / «Mantener mi alarma de 3:15 pm» → **«Mantener alarma»** en wireframes, mockups y láminas; «Eliminar alarma» ya cumplía. Las conexiones (→ M10 ⏩ y → M02) siguen en los mismos marcos, renombrados `botón · Aceptar cambio` / `botón · Mantener alarma`. Sexto comentario (M09: «Eliminar alarma» sin borde ni fondo): en los wireframes móviles el botón destructivo pasa a enlace subrayado sin borde ni fondo en M09, M04 y M06 (como ya estaba en los mockups); en la web (W06/W07) conserva la píldora con trama porque cierra un diálogo de confirmación. Séptimo comentario (M10: «Ya voy · ver ruta» solo con contorno para no competir con la hora): en los wireframes el botón pasa a contorno blanco sobre el fondo oscuro; en los mockups se mantiene relleno porque allí convive con «Posponer 10 min» en contorno y la jerarquía entre ambos depende de esa diferencia. Octavo comentario (M11: más separación entre elementos): filas de ajustes de 40 px con paso de 44 y encabezados de sección con 16 px de aire superior, en wireframes, mockups y láminas HTML. Noveno comentario (M07: en el campo «Título» dejar solo la etiqueta dentro): los campos vacíos de M07 (Título, Lugar, Descripción) muestran únicamente su etiqueta, sin valor de ejemplo ni placeholder; Fecha y Hora conservan el valor precargado. Aplicado en wireframes, mockups y láminas. Décimo comentario (M12: alinear «mientras tanto» a la parte inferior): el divisor y las dos alternativas de captura se anclan al borde inferior de M12 (espaciador FILL + 32 px de relleno), en wireframes, mockups y láminas. Undécimo comentario (M04, sobre la tarjeta del evento: «esto puede ser un modal»; interpretado con el autor como toda la confirmación): M04 se reconstruye como **hoja inferior** sobre M02 atenuada (velo Tinta 55 % que cierra → M02, hoja blanca r24 con asa), conservando el nodo `4:189` y las conexiones «Listo» → M05 y «Eliminar» → M02; desaparecen la barra «‹ Alarma creada» (y su conexión de retorno a M03) y, en los mockups, la banda de textura. Wireframes, mockups y láminas. Sin cambios funcionales. Comentario positivo en M10: «muy buen uso de la paleta». Revisión cerrada: once comentarios aplicados. Regenerados `Prototipo_Figma_Movil.pdf`, `Mockups_Figma_Movil.pdf`, `Design_System_Alarmas_QR.pdf` y `Wireframes_Alarmas_QR.html/.pdf` (v1.2). |
| 2026-09-14 (v2.5.1) | Mockups web · W01 ×3, W03, W04 ×3, W05 ×2 | Observaciones de `MOCKUPS.md` §7.4 aplicadas: Smart Animate 250 ms en todas las conexiones; ninguna conexión sobre texto (la fila de cabecera de cada modal cierra → W01; 109 conexiones); «Descargar QR en lote» como botón de contorno para dejar un solo primario en W01; F-W05 gana la «Vista previa del afiche» (marca + QR + «Escanéalo y te avisamos», QR ≥ 4×4 cm); F-W01 gana la gráfica «Escaneos por semana». `Mockups_Figma_Web.pdf` exportado con marcadores. |
| 2026-09-14 (v2.5) | Mockups web · página `03 · Web` (mmatallanar-ua) | Mockups de alta fidelidad de la aplicación web: 14 marcos de 1280×820 con barra lateral persistente, 114 conexiones «On tap» y un solo punto de inicio en W00. Reorganización respecto a los wireframes: W02 (mis eventos + filtros) se integra en W01 como pestañas Todos / Creados / Escaneados; W04 (reporte) y W05 (QR en lote) son modales sobre W01; W07 (eliminar cuenta) es un modal sobre W06 que devuelve a W00 con «Cuenta eliminada exitosamente». Los códigos F-Wxx no cambian; se añade una nota «Mockups web» a cada F-W. Pendientes en `MOCKUPS.md` §7.4 (Smart Animate, áreas de toque, un primario por pantalla, afiches con marca). PDF `Alarmas QR - Mockups Web.pdf`. |
| 2026-09-14 (v2.4.2) | Mockups Figma v1.4 · M03, M12, M02v | Feedback de usuarios: la acción «Crear el evento a mano» no debe parecer un enlace. En M03 (hoja del escáner), M12 (permiso de cámara) y M02v (inicio sin alarmas) pasa de enlace azul subrayado a botón secundario de contorno (misma anatomía que en M13); en M02v también «Elegir pantallazo de la galería» para mantener la jerarquía primario → dos secundarios. Sin cambios de navegación (siguen → M07). `Mockups_Figma_Movil.pdf` regenerado. |
| 2026-09-14 (v2.4.1) | Prototipo de mockups Figma · FAB «Escanear» de M02, M02b y M05 | Corrección de un error de interacción: el gesto «mantener presionado» usaba el trigger «While pressing», que revierte al soltar, y el «On tap» del mismo botón se disparaba en ese instante: la hoja M02h destellaba y el prototipo saltaba a M12 sin que el usuario eligiera nada. Ahora el toque largo es «Mouse down» con retardo de 0,5 s (solo navega si se mantiene el dedo) y el toque corto es «Mouse up» sin retardo → M12 (con «Mouse down» presente, «On tap» deja de dispararse). Verificado en el prototipo publicado con Playwright. 72 conexiones, sin cambios funcionales ni visuales. |
| 2026-09-08 (v2.4) | Mockups Figma v1.3 · DS v1.2 · Style Tile v1.2 | Segunda crítica de diseño (25/36): tarjeta de alarma única con «evento h:mm · lugar», calendario coherente (jue 27, vie 28, dom 30, lun 31 de agosto 2026), M10 con «Ya voy» (detiene), «Posponer 10 min» y «Ver ruta ›» dentro del destacado, FAB «Escanear» con toque → cámara y mantener presionado → hoja, M03b con «Continuar», botones bajo el héroe en M04/M09/M13, M08 con «Compartir por WhatsApp», M06 con «Eliminar» como enlace, consentimientos sin marcar, «Mía · QR» → «Creada por mí», amarillo decorativo retirado (excepción: sobre Tinta el primario es blanco). DS (láminas L02–L08) y Style Tile alineados con los mockups. Sin cambios funcionales. |
| 2026-09-08 (v2.3b) | Mockups Figma (19 pantallas) + Design System Figma/PDF + STYLE_TILE.md | Pasos 6–10 de la crítica: amarillo solo en la acción principal (switches, píldoras, chip «Nueva» y numerales en Tinta), tonos de texto AA (Coral/Verde/Azul/Gris Texto), escala mínima 12 pt, textura solo como banda de 120 pt en M01/M04/M09/M10/M12/M13, DS con lámina L08 (alarma sonando, controles sobre Tinta, primario sobre amarillo, hora 28+14) y componentes 05/10/12/13/19 corregidos. Sin cambios funcionales ni de navegación. |
| 2026-09-08 (v2.3) | Mockups Figma · M02, M02b, M04, M05, M06, M09, M10 + nuevas M02v, M02h, M03b | Pasos 1–5 del plan de la crítica de diseño (Impeccable, 23/36): M10 con dos acciones equivalentes (Ya voy · ver ruta / Posponer 10 min) y sin selector ni enlace «Silenciar»; M09 con bloque «Si aceptas, sonará 4:45 pm», botones «Aceptar el cambio» / «Mantener mi alarma de 3:15 pm» y sin «Eliminar»; M04 con «Sonará 3:15 pm» a 52 pt y el destructivo como enlace; M02 sin botones superiores, FAB extendido «Escanear» → hoja M02h (Escanear / Pantallazo / A mano); M02v estado vacío del primer uso (desde M00a); M03b pantallazo recibido desde WhatsApp/galería; formato 12 h y dataset único en M02/M02b/M05/M06; botones a 48 pt. Solo en los mockups; los wireframes no cambian. |
| 2026-09-07 (v2.2) | Mockups Figma · M00a–M13 | Propuestas de pantalla finales (alta fidelidad) en el archivo «Mockups Alarmas QR Equipo UX» (duplicado de los wireframes, estilo «Energía puntual», capas de comprensión: fondos plenos en M01/M03/M10, textura de módulos QR, ilustraciones e íconos propios) y prototipo de interacción con Smart Animate en las 59 conexiones. PDF `Mockups_Figma_Movil.pdf`. Sin cambios funcionales ni de navegación; detalle en `MOCKUPS.md`. |
| 2026-09-06 | M03, M04, M12 (wireframes Figma + láminas) | Segunda prueba de usabilidad (CM-13…CM-17): el botón «Ingresar el evento manualmente» pasa a **«Crear el evento a mano»** en M03 y M12, igual que en M13 (CM-14); en M04 la hora calculada «Sonará 3:15 pm» se separa en línea propia y mayor tamaño sobre la explicación del margen + trayecto (CM-15). Sin cambios funcionales ni de navegación. |
| 2026-09-04 (v2.1) | Prototipo Figma · M02, M06, M09, M10, M12 | Recorrido unificado: un solo punto de inicio (M01). Nuevas conexiones M02 "Escanear QR" → M12, M06 "Alarma conectada" → M09, M09 "Perfecto, así queda" → M10, M10 "Ya voy · ver ruta" → M02; se eliminan los puntos de inicio de M09, M10 y M12. Sin cambios en las pantallas. |
| 2026-08-24 | M02, M02b, M04, M06, M08, M10, M11, M12, M13 | Mejoras derivadas de la prueba de usabilidad con la usuaria (conclusiones CM-01…CM-11): nueva **M02b** vista calendario con contadores por día, FAB "+" grande, etiqueta "Datos leídos del QR" en M04, miga de pan y notas editables en M06, "no necesita la app" en M08, posponer 5/10/15 y ruta recomendada en M10, defaults de calendario/posponer en M11, copy de galería en M12, contexto del enlace en M13. |
| 2026-08-23 | W06, W07 | Nuevas pantallas para F-W07 (perfil de usuario) y F-W08 (eliminación de cuenta con confirmación), derivadas del análisis Red Route. Nueva tarea T8. |
| 2026-08-23 (v2) | Todo el prototipo | Adopción del catálogo F-Mxx / F-Wxx. Nuevas pantallas: M00a (registro), M00b (login), W00 (login web), W02 (mis eventos + filtros), W04 (reportes PDF/CSV), W05 (afiches en lote). M04 pasa a guardado automático con opción de descarte. M09 ofrece aceptar / mantener / eliminar. La web deja de crear y editar eventos: las antiguas W02 (crear/editar) y W04 (cambiar hora) se archivaron en `mockups/archivo/`. |
| 2026-08-23 | M09 | Botón "Mantener mi alarma…" → "Eliminar alarma" (luego reemplazado por las tres acciones de la v2). |
| 2026-08-21 | M12, M13 | Se agregaron las pantallas de error para la tarea T6. |
