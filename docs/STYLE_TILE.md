# Alarmas QR · Benchmarking visual y Style Tile

Artefacto de dirección visual de alta fidelidad. Parte del público objetivo (persona **Andrés Rojas**, 29 años, Android gama media, "el profesional del pantallazo") y del objetivo central del proyecto: **no llegar tarde a un evento por no haber programado la alarma**.
Fecha: 2026-08-26 · Entregables: `Style_Tile_Alarmas_QR.html` (lienzo 1920×1080), `Style_Tile_Alarmas_QR.pdf`, artefacto publicado.

---

## 1 · Conceptos de comunicación

Derivados de la investigación (74 % registra eventos con pantallazo, 84 % pidió reajuste automático, 58 % desconfía de un QR sin contexto):

1. **Escanea y listo** — inmediatez sin fricción: un solo gesto separa al usuario de su alarma programada. Nada de formularios cuando no hacen falta.
2. **Energía que llega a tiempo** — puntualidad optimista: la app no regaña ni estresa; da el empujón enérgico y amable que evita la llegada tarde.
3. **Confianza verificada** — transparencia contra el *quishing*: cada dato escaneado se muestra con contexto (organizador verificado, evento legible) antes de sonar.

**Nube de conceptos:** puntualidad · energía · inmediatez · un solo gesto · confianza · verificado · sencillez · sin fricción · contexto · amable.

## 2 · Objetivos de diseño

- **Jerarquía extrema para la hora:** el dato hora/fecha domina cada pantalla; todo lo demás lo sirve.
- **El acento se gana:** el amarillo señala únicamente la acción principal; el coral, solo la urgencia/destrucción. Nunca decoran.
- **Estados siempre visibles:** activa/inactiva, verificada/no verificada, guardada/deshacer se distinguen de un vistazo (color + forma, no solo color).
- **Contraste AA mínimo** en todo texto (Andrés usa un Android gama media, a menudo bajo el sol camino al evento).
- **Lenguaje modular QR:** la retícula de módulos del código QR es el ADN gráfico (esquinas, texturas, vectores, íconos).

## 3 · Benchmarking visual (investigación de escritorio)

| App | Paleta y fondo | Tipografía | Componentes y gestos | Qué tomamos / qué evitamos |
|---|---|---|---|---|
| **Alarmy** (droom.sleepIfUCan) | Rojo enérgico sobre blanco; alto contraste | Sans bold, CTAs grandes | Tarjetas de "misión", botones gigantes al sonar | ✔ Energía y CTAs contundentes · ✘ Agresividad del rojo como color base |
| **Google Clock** (Material 3 Expressive, v8.1 2025) | Color dinámico pastel; **fondo de color pleno para alarmas activas** | Fuente display muy alta para la hora; botones de texto | FAB cuadrado inferior-derecha, panel inferior para editar, botón (ya no slider) para descartar | ✔ Estados activos con fondo pleno, hora gigante, FAB accesible · ✘ Paleta que cambia con el wallpaper (rompe identidad) |
| **Sleep Cycle** | Degradados nocturnos azules, serenidad | Neo-grotesca propia (Innovator Grotesk) | Gráficas suaves, imaginería onírica | ✔ Consistencia tipográfica de marca · ✘ Calma somnolienta: nuestro momento clave es diurno y urgente |
| **Reloj de Apple (iOS 26)** | Oscuro translúcido (Liquid Glass); acento naranja sólido | Hora en cuerpo enorme | Dos botones grandes iguales Detener/Posponer al sonar | ✔ Dos acciones grandes y equivalentes al sonar · ✘ Translucidez costosa en gama media |
| **Samsung Clock (One UI 8.5/9)** | Degradados con profundidad en claro y oscuro | Texto más grande, espaciado extendido | Botones más grandes, snooze unificado | ✔ Aire y tamaño de toque (público Android) · ✘ Ornamento de degradados |
| **Escáneres QR (genérico)** | Blanco/negro duro + un acento (amarillo/verde) | Utilitaria | Visor con esquinas marcadas, vibración al detectar | ✔ Marco de visor y acento único · ✘ Frialdad sin marca |

**Patrones del mercado:** numerales tabulares enormes para la hora; el acento reservado a la acción; estados activo/inactivo de altísimo contraste; objetivos de toque grandes (crítico al sonar la alarma); tarjetas redondeadas y switches tipo píldora; iconografía de línea.

## 4 · Decisiones de estilo — "Energía puntual"

Evolución de alta fidelidad del amarillo `#ffe45c` + tinta `#1d1d1b` de los wireframes: continuidad con todo lo entregado, pero llevada a producto real.

### 4.1 Paleta de colores

**Principal**

| Rol | Nombre | Hex |
|---|---|---|
| Acción primaria / marca | Amarillo Energía | `#FFC400` |
| Tinte / resaltado suave | Amarillo Suave | `#FFF1BF` |
| Texto / marca | Tinta | `#17161C` |
| Fondo | Blanco Papel | `#FFFFFF` |

**Secundaria (semántica y neutros)**

| Rol | Nombre | Hex |
|---|---|---|
| Urgencia / destructivo / alarma sonando | Coral Alarma | `#E8443A` |
| Éxito / organizador verificado | Verde Confirmado | `#129E63` |
| Informativo / enlaces | Azul Enlace | `#2E7CF6` |
| Superficies | Gris Niebla | `#F4F3EF` |
| Texto secundario | Gris Medio | `#77747E` |
| Bordes | Gris Borde | `#DAD8D2` |

Reglas (v1.1, 2026-09-08): amarillo **solo** en la acción principal de cada pantalla (un único elemento amarillo: botón primario o FAB extendido) y en la marca; **nunca** en estados (switch activo, pestaña activa, chip «Nueva» y numerales van en Tinta) ni como decoración. Coral **solo** en urgencia; sobre amarillo siempre texto Tinta (contraste 11.3:1); nunca texto amarillo sobre blanco.

**Tonos de texto (AA ≥ 4.5:1 en cuerpos pequeños):** los semánticos base sirven para rellenos y trazos grandes, pero como texto de 11–15 pt no cumplen AA (Coral 3.9:1, Verde 3.4:1, Azul 3.9:1 sobre blanco; Gris Medio 4.1:1 sobre Gris Niebla). Para texto se usan sus variantes oscuras:

| Rol de texto | Nombre | Hex | Sobre blanco / Niebla |
|---|---|---|---|
| Texto destructivo, chip coral | Coral Texto | `#C4362E` | 5.4 / 4.8 |
| Texto verificado / escaneado | Verde Texto | `#0B7048` | 6.1 / 5.5 |
| Enlaces | Azul Texto | `#1A5BC4` | 6.3 / 5.7 |
| Texto secundario | Gris Texto | `#66636D` | 5.9 / 5.3 |
| Texto secundario sobre Tinta | Gris Borde | `#DAD8D2` | 12.6 sobre Tinta |

### 4.2 Tipografía y jerarquía de textos

- **Titulares — Bricolage Grotesque** (Google Fonts): grotesca contemporánea con carácter; enérgica sin ser agresiva.
- **Cuerpo y UI — Archivo**: neutra, legible en pantallas de gama media, excelente en cuerpos pequeños.
- **Horas y datos — Spline Sans Mono**: monoespaciada; los dígitos alinean siempre (eco técnico del módulo QR).

| Nivel | Uso | Fuente / peso | Puntaje |
|---|---|---|---|
| Hora protagonista | Alarma sonando | Spline Sans Mono Bold | **96 pt** |
| Hora en tarjeta | Lista de alarmas | Spline Sans Mono Bold | 34 pt |
| Título 1er nivel (H1) | Nombre de pantalla | Bricolage Grotesque Bold | 28 pt |
| Título 2º nivel (H2) | Sección / nombre de evento | Bricolage Grotesque SemiBold | 22 pt |
| Título 3er nivel (H3) | Agrupador ("HOY", "MAÑANA") | Archivo Bold, mayúsculas, +0.08 em | 15 pt |
| Destacado | Dato clave en línea ("en 45 min") | Archivo Bold sobre Amarillo Suave | 16 pt |
| Cuerpo | Descripciones, ajustes | Archivo Regular | 16 pt |
| Etiqueta de descripción de alarma | Lugar, anticipación, origen | Archivo Regular | 13 pt |
| Minutos y segundos secundarios / AM–PM | Junto a la hora | Spline Sans Mono Medium | 18 pt |

### 4.3 Iconografía

Estilo **línea + área de color**: trazo de 2 px con terminales y esquinas redondeadas sobre retícula de 24 px; cuando el ícono está activo se rellena un plano Amarillo Energía detrás (nunca cambia el trazo). Curvas dominantes con vértices rectos heredados del QR.

### 4.4 Figuras vectoriales

Geométricas y modulares, derivadas de la anatomía del código QR: *finder patterns* (dianas cuadradas concéntricas) como firma gráfica, retículas de módulos redondeados, círculos de esfera de reloj y destellos de puntualidad. Abstractas, nunca figurativas.

### 4.5 Ilustración

Plana geométrica, **líneas y planos**: personajes/objetos construidos con formas simples de esquinas redondeadas, paleta limitada (tinta + amarillo + un semántico), sin degradados ni sombras. Momentos: escanear, llegar a tiempo, compartir.

### 4.6 Texturas

**Repetición y modulación de patrones** geométricos, siempre ≤ 8 % de opacidad sobre fondo y, desde la v1.1, solo como banda de 120 pt en pantallas de momento (bienvenida, confirmaciones, alarma sonando, errores), nunca sobre listas, formularios ni el visor de cámara: (a) retícula de módulos QR desvaneciéndose (modulación), (b) trama de puntos de reloj, (c) franjas diagonales amarillas solo como cinta de urgencia en bordes. Sin fotografías (decisión: identidad 100 % vectorial).

### 4.7 Controles y componentes (10, con 3 estados c/u)

Base: esquinas 14 px (tarjetas) / píldora (botones), borde 1.5 px Gris Borde, fondo Blanco Papel, sombra mínima solo en elementos flotantes.

| # | Componente (pantallas donde se repite) | Estados mostrados |
|---|---|---|
| 1 | Botón primario "Escanear QR" (M02, M03, M04…) | normal · presionado · deshabilitado |
| 2 | Botón secundario contorno (M04, M06, M11) | normal · presionado · deshabilitado |
| 3 | Botón destructivo "Eliminar alarma" (M04, M06) | normal · presionado · confirmación |
| 4 | FAB "+" flotante (M02/M02b, CM-02) | normal · presionado · extendido |
| 5 | Campo de texto (M00a/b, M07) | normal · foco · error |
| 6 | Switch de alarma (M02, M06, M11) — activa en Tinta (v1.1) | activa · inactiva · deshabilitada |
| 7 | Tarjeta de alarma (M02, M05) | activa · inactiva · recién guardada |
| 8 | Chip de origen (M02, M04) | "Mía · QR" · "Escaneada ✓" · "Nueva" |
| 9 | Snackbar Deshacer 5 s (M05) | visible con cuenta atrás · en curso · confirmado |
| 10 | Ítem de navegación inferior (M02→) | activo · inactivo · con badge |

## 5 · Fuentes de la investigación

- Google Clock 8.1 — Material 3 Expressive: [9to5google.com](https://9to5google.com/2025/08/22/google-clock-material-3-expressive/), [androidauthority.com](https://www.androidauthority.com/google-clock-material-3-expressive-design-3554619/)
- iOS 26 Clock / Liquid Glass: [macrumors.com](https://www.macrumors.com/2025/06/24/new-alarm-design-ios-26-make-you-oversleep/)
- Samsung Clock One UI 8.5/9: [sammyguru.com](https://sammyguru.com/samsung-clock-app-looks-completely-different-in-one-ui-8-5/), [sammobile.com](https://www.sammobile.com/news/one-ui-9-slight-changes-alarms-timer-design/)
- Sleep Cycle — identidad y tipografía: [sleepcycle.com](https://sleepcycle.com/newsroom/press-release/a-fresh-set-of-sheets-for-your-app-experience)
- Alarmy — misión y producto: [alar.my](https://alar.my/en/blog/alarmys-mission-wake-people-up-fully-and-completely), [Google Play](https://play.google.com/store/apps/details?id=droom.sleepIfUCan&hl=en_US)

## 6 · Changelog

- **2026-09-08 · v1.2** — Segunda crítica: los componentes del lienzo usan los tonos de texto (destructivo, chip escaneada, error, enlaces), FAB extendido «Escanear» con estado «mantener» («Agregar evento…»), chip «Creada por mí», H3 13 pt y tarjeta con «evento 8:00 am»; snackbar con «Deshacer» blanco. HTML, PDF y artefacto republicados.
- **2026-09-08 · v1.1** — Tras la crítica de diseño de los mockups: amarillo como acento estricto (un solo elemento amarillo por pantalla, estados en Tinta), tabla de tonos de texto AA (Coral/Verde/Azul/Gris Texto) y textura solo como banda de 120 pt en 6 pantallas. El lienzo HTML/PDF se regeneró el mismo día (v1.1 y v1.2).

- **2026-08-26 · v1** — Benchmarking de 6 referentes, conceptos de comunicación, objetivos de diseño y Style Tile "Energía puntual" (lienzo 1920×1080 + PDF + artefacto).
