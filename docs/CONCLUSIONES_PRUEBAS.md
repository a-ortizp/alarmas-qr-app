# Alarmas QR · Conclusiones de las pruebas de usabilidad

Documento de conclusiones sobre problemas, errores y posibles mejoras del prototipo de baja fidelidad, separado por aplicación.
Exportable: `Conclusiones_Pruebas_Usabilidad.pdf` (raíz). Última actualización: 2026-09-06 (v3 — segunda sesión móvil sobre los wireframes de fidelidad media, CM-13…CM-17).

## A · Aplicación móvil

**Sesión:** prueba de usabilidad moderada sobre el prototipo de baja fidelidad de la app móvil · video de 12 min 17 s (24 ago 2026).
**Usuaria de pruebas:** Allison · **Moderador:** Alejandro · **Método:** recorrido guiado pantalla por pantalla (M01–M13) con preguntas abiertas.
**Fuentes:** transcripción automática del video (`transcripcion_video_app_movil.md`, Whisper — con ruido de reconocimiento; citas editadas mínimamente por legibilidad) y notas del equipo durante la sesión. Los tiempos [mm:ss] remiten al video.

**Resumen:** 12 conclusiones — 2 problemas, 3 errores de comprensión, 6 mejoras y 1 validación de aciertos.

### CM-01 · [Problema] La lista larga de alarmas genera carga; la usuaria espera una vista de calendario compacta
- **Pantalla(s):** M02 · Inicio
- **Evidencia** (Retroalimentación de la usuaria): «Creo que podría ser mejor no así en lista, sino poder ver el calendario… de una manera más compacta… que tenga numeritos indicativos de cuántas alarmas tengo para ese día» [03:39–03:58]
- **Actividades para mejorar el prototipo:**
   - Diseñar una vista alternativa de calendario (mensual/semanal) con contadores de alarmas por día, conmutable con la lista actual.
   - Prototipar el conmutador lista ⇄ calendario en M02 y validarlo en la siguiente sesión de prueba.
### CM-02 · [Error] El botón «+» para crear una alarma es demasiado pequeño y poco visible
- **Pantalla(s):** M02 · Inicio
- **Evidencia** (Nota del equipo durante la sesión): «Aquí sí hay un botón que está un poco pequeño, que es para agregar más» [03:11]
- **Actividades para mejorar el prototipo:**
   - Aumentar el tamaño y contraste del acceso «+ Nueva alarma» (área táctil ≥ 48 dp) o convertirlo en botón flotante (FAB).
   - Revisar la jerarquía de los tres accesos de creación (escanear / galería / manual) en M02.
### CM-03 · [Error] La usuaria creyó que debía digitar la información del evento
- **Pantalla(s):** M04 · Tarjeta del evento
- **Evidencia** (Retroalimentación de la usuaria): «¿Y esto lo tengo que poner yo? O sea, ¿toda esta descripción la diligencio yo según el evento?» [04:44]
- **Actividades para mejorar el prototipo:**
   - Agregar la etiqueta «Datos leídos del QR» en la tarjeta y diferenciar visualmente lo automático de lo editable.
   - Validar en la próxima prueba la versión v2 de M04 (guardado automático), que refuerza el mensaje «sin digitar nada».
### CM-04 · [Mejora] Las decisiones repetitivas (calendario, guardado) deberían tener un valor por defecto configurable
- **Pantalla(s):** M04 · Tarjeta / M11 · Ajustes
- **Evidencia** (Retroalimentación de la usuaria): «¿Esto siempre me lo va a preguntar o es algo ya estandarizado, por si lo quiero apagar?» [05:03]
- **Actividades para mejorar el prototipo:**
   - Añadir en M11 la preferencia «Agregar siempre a Google Calendar» para no preguntar por evento.
   - Mantener el guardado automático (ya incorporado en la v2 de M04) y comunicarlo en el onboarding.
### CM-05 · [Problema] Desorientación momentánea entre la lista y el detalle de una alarma
- **Pantalla(s):** M02 ⇄ M06
- **Evidencia** (Retroalimentación de la usuaria): «¿La primera parte era el listado de los eventos antes de aceptarlos?… volvimos a…» [05:26–05:41]
- **Actividades para mejorar el prototipo:**
   - Reforzar los títulos jerárquicos y el botón de regreso en M06 para diferenciar lista vs. detalle.
   - Ensayar una transición/encabezado que indique de dónde viene la pantalla («Mis alarmas → Detalle»).
### CM-06 · [Mejora] La usuaria quiere personalizar la descripción de una alarma registrada
- **Pantalla(s):** M06 · Detalle y ajuste
- **Evidencia** (Retroalimentación de la usuaria): «Ojalá la pueda yo personalizar (la descripción)» [05:41–06:00]
- **Actividades para mejorar el prototipo:**
   - Habilitar en M06 la edición de descripción/notas propias sin alterar los datos originales del evento (F-M06, corto plazo).
### CM-07 · [Mejora] El valor «funciona sin la app» no es visible en la pantalla de compartir
- **Pantalla(s):** M08 · Compartir QR
- **Evidencia** (Retroalimentación de la usuaria): «¿Estos QR que se generan funcionan para una persona que no tenga la aplicación?» [06:19]
- **Actividades para mejorar el prototipo:**
   - Agregar microcopy en M08 y en los afiches: «Quien lo escanee no necesita la app: abre la página del evento».
### CM-08 · [Mejora] El posponer fijo de 10 min se percibe rígido y el estimado de trayecto debe sentirse realista y «acompañador»
- **Pantalla(s):** M10 · La alarma suena
- **Evidencia** (Retroalimentación de la usuaria): «¿No se puede cambiar?… que te diga si ese tiempo estimado se puede cumplir o no según la ruta más recomendable… algo mucho más acompañador» [08:26–09:45]
- **Actividades para mejorar el prototipo:**
   - Ofrecer opciones de posponer (5 / 10 / 15 min) o un valor configurable en M11.
   - Mostrar la fuente y actualización del estimado («30 min con el tráfico de ahora») — valida priorizar F-M10-Ext (largo plazo).
   - Comunicar el estimado como acompañamiento («vas a llegar a tiempo si sales en X») y no solo como dato.
### CM-09 · [Mejora] Expectativa de anticipación «inteligente» que aprenda del historial de llegadas
- **Pantalla(s):** M06 / M11
- **Evidencia** (Retroalimentación de la usuaria): «Que a partir de la experiencia que tiene… te diga un estimado realista del tiempo de desplazamiento… no solo darle inicio y ya» [09:12–09:45]
- **Actividades para mejorar el prototipo:**
   - Registrar en el backlog (post-MVP) la sugerencia de anticipación basada en hábitos/historial del usuario.
   - Conectarla con la motivación racional del Design Scope al presentar el roadmap.
### CM-10 · [Error] El copy «Mientras tanto: usar un pantallazo» no se entiende
- **Pantalla(s):** M12 · Permiso de cámara
- **Evidencia** (Retroalimentación de la usuaria): «¿Cómo así “mientras tanto usar un pantallazo”?» [10:16]
- **Actividades para mejorar el prototipo:**
   - Reescribir el microcopy: «También puedes escanear desde una imagen de tu galería».
   - Re-probar la comprensión de las alternativas de M12 en la siguiente sesión.
### CM-11 · [Mejora] El bloqueo anti-quishing convence, pero la usuaria pide contexto sobre el enlace detectado
- **Pantalla(s):** M13 · QR no válido
- **Evidencia** (Retroalimentación de la usuaria): «Me gustaría que me dijera de qué es el enlace, por ejemplo una tienda o un evento… pero que por seguridad ustedes no lo abren, está bien» [11:15–11:58]
- **Actividades para mejorar el prototipo:**
   - Mostrar en M13 el dominio y una categoría segura del contenido («enlace a sitio externo: tienda-ropa.com») sin abrirlo.
   - Mantener la decisión de no apertura automática (validada por la usuaria).
### CM-12 · [Positivo] Elementos validados que deben conservarse
- **Pantalla(s):** General
- **Evidencia** (Retroalimentación de la usuaria): Propuesta de valor entendida («escanea y olvídate» [01:58]); vinculación con Google Calendar valorada («lo puedo vincular de una» [02:39]); el escaneo se percibe natural («eso ya es algo normal» [04:30]); el aviso de cambio de hora se entiende y gusta («me gusta» [08:53])
- **Actividades para mejorar el prototipo:**
   - No modificar estos elementos en la siguiente iteración; usarlos como ancla de la propuesta.
   - Priorizar los ajustes CM-01…CM-11 sin rediseñar lo que ya funciona.

### Notas de método para la próxima sesión

- La sesión fue muy dirigida (el moderador explicaba cada pantalla antes de dejar interactuar): para la próxima ronda conviene usar las tareas T1–T8 de la guía y protocolo think-aloud, dejando que la usuaria navegue sola.
- Mejorar el registro de audio: la transcripción automática perdió pasajes completos; grabar en un espacio silencioso o con micrófono cercano.
- La prueba se hizo con la versión previa de M04 (botón «Guardar alarma»); la v2 con guardado automático responde directamente a CM-03/CM-04 y debe validarse en la siguiente sesión.

## A2 · Aplicación móvil · segunda sesión (wireframes de fidelidad media)

**Sesión:** recorrido exploratorio moderado sobre el prototipo interactivo de Figma «Wireframes Alarmas QR Equipo UX» (recorrido unificado desde M01) · grabación de Teams de 15 min 41 s (6 sep 2026, 10:45 a. m.).
**Participante:** Rafael Caicedo · **Moderador:** Alejandro · **Método:** navegación libre con think-aloud parcial; el moderador contextualizó cada pantalla.
**Fuentes:** transcripción automática de Teams (`Prototipo UX Alarmas Wireframes Transcripcion.docx`, con ruido de reconocimiento; citas editadas mínimamente). Los tiempos [mm:ss] remiten a la grabación.
**Recorrido observado:** M01 → M00a → M02 → M12 → M03 → M04 → M05 → M06 → M02b → M07 → M08 → M11. No se alcanzaron M09, M10 ni M13.

**Resumen:** 5 conclusiones — 1 problema, 2 errores de comprensión, 1 mejora y 1 validación de aciertos.

### CM-13 · [Problema] Las opciones de calendario en M01 se leen como seleccionables, pero no responden
- **Pantalla(s):** M01 · Bienvenida
- **Evidencia** (Retroalimentación del participante): «A mí en este caso sería más fácil el calendario del teléfono… déjame ver si me puede dar esa opción… no me deja hundirme» [01:17–01:41]
- **Actividades para mejorar el prototipo:**
   - Dar estado seleccionado/no seleccionado a las tres tarjetas de calendario en el prototipo (al menos una variante conmutable), o marcar explícitamente qué controles no son navegables en la guía de la prueba.
   - Mantener la regla de áreas de toque: toda tarjeta con aspecto de control debe tener un marco tocable, aunque solo cambie de estado.
### CM-14 · [Error] «Ingresar el evento manualmente» se confunde con escanear
- **Pantalla(s):** M12 · Permiso de cámara · M03 · Escáner
- **Evidencia** (Retroalimentación del participante): «Cuando yo le hundo en ingresar el evento manualmente, ya me sale la cámara para poder escanear el QR, ¿cierto?» [06:46–07:05]; entendió la acción cuando el moderador la describió como «que tú lo hagas todo metiendo los datos» [07:05]
- **Actividades para mejorar el prototipo:**
   - Unificar el rótulo con el de M13, que sí se entendió: **«Crear el evento a mano»** en M03 y M12. *(Aplicado el 2026-09-06 en Figma y en las láminas de wireframes.)*
   - Revisar que el verbo «ingresar» no se use para acciones que no abren la cámara ni la galería.
### CM-15 · [Error] La hora calculada de la alarma en M04 no se lee de un vistazo
- **Pantalla(s):** M04 · Alarma creada
- **Evidencia** (Retroalimentación del participante): «Uy, ven, ven, ven, que no acabo de leer… dice sonará 3 y 15, 30 de margen más 15 trayecto desde tu ubicación» [08:36–08:44]; tras leerlo, lo valoró: «está mucho más práctica porque te puede avisar y yo la puedo programar a mi estilo» [09:36]
- **Actividades para mejorar el prototipo:**
   - Separar el dato principal («Sonará 3:15 pm», tipografía grande en negrita) de la explicación del cálculo (margen + trayecto, en texto secundario) dentro del bloque de dato sugerido. *(Aplicado el 2026-09-06 en Figma y en las láminas.)*
   - Validar en la siguiente sesión que la lectura sea inmediata sin intervención del moderador.
### CM-16 · [Mejora] El consentimiento de datos en M00a se confunde con una política de contraseñas
- **Pantalla(s):** M00a · Registro
- **Evidencia** (Retroalimentación del participante): «¿Cómo es el término de la política de seguridad en cuanto a las contraseñas que uno ingrese?» [02:54], al leer el texto «Acepto el tratamiento de mis datos según la política de privacidad (Ley 1581 de 2012)»
- **Actividades para mejorar el prototipo:**
   - Separar visualmente los requisitos de la contraseña (ayuda bajo el campo, «Mínimo 8 caracteres») del consentimiento de datos, y encabezar este último con «Tus datos» o similar.
   - Considerar un enlace «¿Qué datos guardamos?» junto al checkbox para responder la duda sin salir del registro.
### CM-17 · [Positivo] Elementos validados en la segunda sesión
- **Pantalla(s):** M02 · M02b · M03 · M04 · M07 · M08 · M12
- **Evidencia** (Retroalimentación del participante): la carga por pantallazo conecta con una experiencia propia: «cuando me envían fotos de QRs se me hace imposible pagar… increíble que esta aplicación tenga esos pantallazos» [04:22–04:50]; el permiso de cámara se entiende como habitual: «eso lo pide casi todas las aplicaciones» [06:22]; la vista calendario vuelve a preferirse: «así me parece mucho más fácil con el calendario… me parece muy bien esa modificación» [11:28–11:53]; el QR generado al crear una alarma y sus opciones de compartir se entienden solos: «se genera allí junto con la alarma… lo puedo escanear por WhatsApp, por correo… cargar en foto y en PDF» [13:00–13:53]
- **Actividades para mejorar el prototipo:**
   - Conservar el pantallazo como acceso de primer nivel en M02 y M12, y la vista calendario (M02b) como conmutador visible; considerar hacerla la vista por defecto si la tercera sesión lo confirma.

### Notas de método para la próxima sesión

- El participante coincide con la persona Andrés («el profesional del pantallazo») y su reacción al pantallazo lo confirma; conviene reclutar a alguien sin relación personal con el equipo para reducir el sesgo de cortesía (hubo muchos «increíble» y ninguna crítica espontánea).
- La sesión volvió a ser muy dirigida: el moderador explicó el pantallazo, el QR compartible y el trayecto antes de que el participante los descubriera, e indicó los siguientes pasos. Usar las tareas T1–T8 con consignas cerradas y responder preguntas solo al final de cada tarea.
- El recorrido unificado quedó a medias: no se probó M06 → M09 → M10 (cambio del organizador y alarma sonando, UF-M10.1 del MVP) ni el desvío M13. Incluir explícitamente la tarea T4 en la próxima sesión.
- La transcripción automática de Teams introduce palabras en inglés y frases sueltas; recortar el cierre posterior al fin de la grabación antes de anexarla.

## B · Aplicación web

**Sesión:** prueba con usuario real mediante la técnica **Mago de Oz** sobre los bocetos en papel de la plataforma web (consulta, métricas, afiches y reportes — sin creación ni edición, exclusivas del móvil).
**Método:** tareas del flujo web completadas en su totalidad; análisis del video y observaciones en voz alta del participante.
**Resumen:** 3 conclusiones — 1 problema de rotulado, 1 error de etiquetado y 1 conjunto de validaciones positivas.

### CW-01 · [Problema] El término «Confirmaciones» es ambiguo en las métricas
- **Pantalla(s):** métricas del tablero (W01 / W03, boceto en papel)
- **Evidencia** (retroalimentación del usuario): comentó expresamente que no entendía a qué hacía referencia «Confirmaciones» — si eran alarmas confirmadas u otra cosa. Impacto medio: confunde el significado del dato.
- **Actividades para mejorar el prototipo:**
   - Reemplazar «Confirmaciones» por una frase descriptiva: «Confirmaron 'Ya voy'» o «Personas que confirmaron salida».
   - Corregir el rótulo en los bocetos de papel. *Estado: aplicado — los mockups digitales usan «Confirmaron "Ya voy"» en W01, W03 y W04, y se agregó la descripción «personas que avisaron que van en camino» bajo la métrica (W01/W03).*

### CW-02 · [Error] La selección PNG/PDF de la descarga en lote no tiene título
- **Pantalla(s):** W05 · Afiches en lote (boceto en papel)
- **Evidencia** (nota del equipo al revisar el video): la sección para elegir el formato no tenía un rótulo que identificara la categoría de la opción. Impacto bajo: el usuario interactuó igual, pero el componente carecía de etiqueta.
- **Actividades para mejorar el prototipo:**
   - Dibujar el título explícito «Formato de salida:» sobre las opciones PNG/PDF en el boceto.
   - *Estado: aplicado en los mockups digitales (24 ago): rótulo «Formato de salida» en W05 y también en el selector PDF/CSV de W04.*

### CW-03 · [Positivo] Elementos validados de la web
- **Evidencia** (retroalimentación del usuario y video): recorrió con fluidez la navegación; comprendió la barra lateral, las tarjetas de métricas agregadas y la tabla de eventos discriminada entre creados y escaneados, sin bloqueos funcionales. La búsqueda, las pestañas de estado (Próximos / Pasados / Borradores) y los botones «Exportar reporte» y «Descargar afiches en lote» resultaron claros e intuitivos.
- **Actividades:**
   - Conservar la estructura de navegación y el rotulado de filtros/acciones en la siguiente iteración.
   - Limitar los ajustes a las correcciones de texto CW-01 y CW-02 antes de avanzar a la etapa digital.
