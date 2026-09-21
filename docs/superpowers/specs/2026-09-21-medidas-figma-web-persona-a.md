# Medidas de Figma · pantallas web de la Persona A

Anexo de `2026-09-20-maquetacion-persona-a-design.md`. Tomadas el 2026-09-21 del archivo `4nHD4ygcnP33UH0gAhaii5` (página `03 · Web`, marcos 1280×820; iconos de `00 · Recursos gráficos`) con `get_metadata` + `get_design_context` + `get_screenshot`. Las medidas se transcriben tal cual; cuando contradicen un token o una regla del DS, el plan decide y lo anota (ver `plans/2026-09-21-plan3-web-persona-a.md` §«Decisiones»).

Marcos: W00 `4072:1861` · W00 cuenta eliminada `4072:1878` · W00 error `4362:474` · W00 recuperar `4362:427` · W00 correo enviado `4362:449` · W06 `4072:233` · W06 actualizado `4072:279` · W06 modal `4072:256` · diálogo ¿Cerrar sesión? `4360:347` · barra lateral en W06 `4072:243`, W01 `4072:36`, colapsada `4357:1879` · barra superior `4072:234`.

---

# Hoja de medidas · Grupo W00

Fuente: Figma `4nHD4ygcnP33UH0gAhaii5`, página `03 · Web`. Medido el 2026-09-21 con `get_metadata` + `get_design_context` + capturas.
Unidades en px. «Pos.» = (x, y) relativa al padre; «abs.» = relativa al marco 1280×820.
Convenciones comunes a todos los textos salvo que se diga otra cosa: interlineado `normal` (auto de la fuente; Figma no fija line-height), tracking 0 (no se declara letter-spacing), `font-variation-settings: "wdth" 100` en Archivo y `"opsz" 14, "wdth" 100` en Bricolage Grotesque. No hay sombras (ningún `box-shadow`/efecto) en ningún nodo de este grupo.

---

## 1. W00 · Inicio de sesión — nodeId `4072:1861`

Marco 1280×820, fondo `#FFFFFF`, borde 1 `#DAD8D2` sólido (borde del propio marco del mockup). Auto-layout: centra la tarjeta en ambos ejes (`items-center justify-center`).

### Bloques de arriba abajo

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 0 | Tarjeta de acceso `4072:1862` | (380, 190.5) = abs. | **520 × 439** | Fondo `#FFFFFF`; borde **1.5** `#DAD8D2`; radio **14**; relleno **34** en los 4 lados; auto-layout vertical, **gap 20**, `items-start`, `overflow: clip`; sin sombra. Ancho útil 452. |
| 1 | Fila de marca `4072:1863` | (34, 34) · abs. (414, 224.5) | 452 × 36 | Fondo `#FFFFFF`; auto-layout horizontal, gap **10**, centrado horizontal y vertical. |
| 1a | Logotipo `4072:1864` | (135, 0) · abs. (549, 224.5) | 36 × 36 | Ver «Iconos / vectores». |
| 1b | Texto «Alarmas QR» `4072:1866` | (181, 3.5) · abs. (595, 228) | 136 × 29 | Bricolage Grotesque Bold 24, `#17161C`. |
| 2 | Subtítulo `4072:1867` | (34, 90) · abs. (414, 280.5) | 452 × 15 | Archivo Regular 14, `#66636D`, centrado. |
| 3 | Grupo de campos `4072:1868` | (34, 125) · abs. (414, 315.5) | 452 × 104 | Fondo `#FFFFFF`; auto-layout vertical, **gap 12**. |
| 3a | Campo correo `4072:1869` | (0, 0) · abs. (414, 315.5) | 452 × **46** | Ver «Controles». |
| 3b | Campo contraseña `4072:1872` («campo · Contraseña (⏩ error)») | (0, 58) · abs. (414, 373.5) | 452 × **46** | Ver «Controles». Es un enlace de prototipo (⏩) hacia el estado de error. |
| 4 | Botón «Iniciar sesión» `4072:1875` | (34, 249) · abs. (414, 439.5) | 452 × **44** | Ver «Controles». |
| 5 | Enlace `4362:496` («enlace · recuperar contraseña») | (34, 313) · abs. (414, 503.5) | 452 × **44** | Ver «Controles». |
| 6 | Nota final `4072:1877` | (34, 377) · abs. (414, 567.5) | 452 × 28 | Archivo Regular 13, `#66636D`, centrado, 2 párrafos (salto de línea explícito). |

Comprobación vertical: 34 + 36 + 20 + 15 + 20 + 104 + 20 + 44 + 20 + 44 + 20 + 28 + 34 = 439. Tarjeta centrada: (820 − 439) / 2 = 190.5; (1280 − 520) / 2 = 380.

### Controles

**Campo (correo y contraseña, idénticos)**
- Tamaño 452 × 46 (alto por contenido: 8 + 12 + 2 + 16 + 8 = 46; no hay alto fijo).
- Fondo `#FFFFFF`; borde **1.5** `#DAD8D2` sólido; radio **12**; relleno **8 vertical / 14 horizontal**; auto-layout vertical, **gap 2**, `overflow: clip`.
- Etiqueta: (14, 8), 131 × 12 («CORREO ELECTRÓNICO») / 78 × 12 («CONTRASEÑA»); Archivo **SemiBold 11**, `#66636D`, en mayúsculas escritas tal cual (no `text-transform`), sin tracking.
- Valor: (14, 22), 424 × 16; Archivo Regular **15**, color **`#DAD8D2`** (es un placeholder: «nombre@correo.com» / «••••••••» con 8 puntos).
- Estado de foco: no dibujado en este grupo. Estado de error: ver marco 3.

**Botón primario «Iniciar sesión»**
- 452 × 44 (ancho completo); fondo `#FFC400`; radio **999**; relleno **14 vertical, 0 horizontal** (el ancho lo da el padre); contenido centrado.
- Texto: (180, 14), 92 × 16; Archivo **SemiBold 15**, `#17161C`.

**Enlace «¿Olvidaste tu contraseña?»**
- Marco 452 × **44** fijo, sin fondo ni borde, contenido centrado.
- Texto: (144.5, 14.5), 163 × 15; Archivo Regular **14**, `#1A5BC4`, **subrayado** sólido (`text-decoration: underline`, grosor y posición «from-font»).

### Textos literales

| Capa | Literal exacto | Fuente | Tamaño | Color | Alineación |
|---|---|---|---|---|---|
| `4072:1866` | `Alarmas QR` | Bricolage Grotesque Bold (700) | 24 | `#17161C` | izquierda (sin salto) |
| `4072:1867` | `Administración y consulta de tus eventos y alarmas` | Archivo Regular (400) | 14 | `#66636D` | centro |
| `4072:1870` | `CORREO ELECTRÓNICO` | Archivo SemiBold (600) | 11 | `#66636D` | izquierda |
| `4072:1871` | `nombre@correo.com` | Archivo Regular | 15 | `#DAD8D2` | izquierda |
| `4072:1873` | `CONTRASEÑA` | Archivo SemiBold | 11 | `#66636D` | izquierda |
| `4072:1874` | `••••••••` (8 puntos) | Archivo Regular | 15 | `#DAD8D2` | izquierda |
| `4072:1876` | `Iniciar sesión` | Archivo SemiBold | 15 | `#17161C` | centro (por el contenedor) |
| `4362:497` | `¿Olvidaste tu contraseña?` | Archivo Regular, subrayado | 14 | `#1A5BC4` | centro (por el contenedor) |
| `4072:1877` | línea 1: `Entra cualquier usuario registrado en el sistema.` · línea 2: `¿Aún no tienes cuenta? Créala desde la app móvil al registrarte.` | Archivo Regular | 13 | `#66636D` | centro |

Interlineado de todos: `normal`. Alturas de caja resultantes: 24 → 29; 15 → 16; 14 → 15; 13 → 14 por línea; 11 → 12.

### Iconos / vectores

**Logotipo** (`4072:1864`, repetido idéntico en los 5 marcos): no es un SVG importado sino un marco con dos rectángulos.
- Marco 36 × 36, fondo `#FFC400`, radio **9**, `overflow: clip`.
- Rectángulo 1 (`4087:1585`): (9, 9), 18 × 18, sin relleno, trazo **2.571** `#17161C` hacia dentro (el código de Figma lo sitúa dentro de la caja de 18), radio **4.5**.
- Rectángulo 2 (`4087:1586`): (14.79, 14.79), 6.429 × 6.429, relleno `#17161C`, radio **1.607**. Centro exacto en (18, 18).
- SVG equivalente (derivado de lo anterior; trazo interior convertido a trazo centrado desplazando media línea, 2.571 / 2 = 1.2855):

```svg
<svg width="36" height="36" viewBox="0 0 36 36" xmlns="http://www.w3.org/2000/svg">
  <rect width="36" height="36" rx="9" fill="#FFC400"/>
  <rect x="10.2855" y="10.2855" width="15.429" height="15.429" rx="3.2145" fill="none" stroke="#17161C" stroke-width="2.571"/>
  <rect x="14.79" y="14.79" width="6.429" height="6.429" rx="1.607" fill="#17161C"/>
</svg>
```

(Proporción respecto a 36: marco interior 1/2, trazo 1/14, punto 5/28; los radios 4.5 y 1.607 = 1/4 de sus lados.)

**Texto de marca**: la «Q» de «Alarmas QR» muestra una cola horizontal bajo la letra; es el glifo propio de Bricolage Grotesque Bold, no un subrayado ni un vector aparte.

---

## 2. W00 · cuenta eliminada — nodeId `4072:1878`

Nombre de la capa: «W00 · Inicio de sesión (cuenta eliminada)». Todo igual al marco 1 salvo lo siguiente.

### Bloques de arriba abajo (diferencias)

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 0 | Tarjeta `4072:1879` | (380, **222.5**) = abs. | 520 × **375** | Igual estilo. Más baja porque **no tiene el enlace «¿Olvidaste tu contraseña?»**. |
| 6 | Nota final `4072:1894` | (34, **313**) · abs. (414, 535.5) | 452 × 28 | Sube 64 (44 + gap 20). |
| 7 | Snackbar `4072:1895` | **abs. (509, 760)** (el código lo da como left 508 / top 759 por el borde de 1 del marco) | **262 × 36** | Posición absoluta. Fondo `#17161C`; radio **999**; relleno **10 vertical / 18 horizontal**; auto-layout horizontal, **gap 8**, centrado vertical; `overflow: clip`; sin sombra. Margen inferior 820 − 796 = **24**. Centrado horizontal: 509 + 262/2 = 640 ✓. |
| 7a | Icono `4072:1896` | (18, 10) | 16 × 16 | Ver «Iconos / vectores». |
| 7b | Texto `4072:1899` | (42, 10.5) | 202 × 15 | Archivo **SemiBold 14**, `#FFFFFF`, interlineado normal. |

### Controles
Sin cambios (sin el enlace).

### Textos literales
- Snackbar: `Cuenta eliminada exitosamente`

### Iconos / vectores

Icono del snackbar (asset `b509b.svg`, idéntico en marco 5), exportado tal cual:

```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
<g id="Frame">
<path id="Vector" d="M8 14C11.3137 14 14 11.3137 14 8C14 4.68629 11.3137 2 8 2C4.68629 2 2 4.68629 2 8C2 11.3137 4.68629 14 8 14Z" fill="white"/>
<path id="Vector_2" d="M5.33333 8L7.33333 10L10.6667 6" stroke="#0B7048" stroke-width="1.46667" stroke-linecap="round" stroke-linejoin="round"/>
</g>
</svg>
```

Círculo blanco de radio 6 centrado en (8, 8) con visto `#0B7048` (Verde texto), trazo 1.46667, extremos y uniones redondeados.

---

## 3. W00 · error de credenciales — nodeId `4362:474`

Nombre de la capa: «W00 · Inicio de sesión (error de credenciales)». Marco 1280×820, fondo `#FFFFFF`, borde 1 `#DAD8D2`.

### Bloques de arriba abajo

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 0 | Tarjeta `4362:475` | (380, **177.5**) = abs. | 520 × **465** | Mismo estilo que marco 1 (borde 1.5 `#DAD8D2`, radio 14, relleno 34, gap 20). +26 por el mensaje de error. |
| 1 | Fila de marca `4362:476` | (34, 34) · abs. (414, 211.5) | 452 × 36 | Igual que marco 1 (logo `4362:477` en (135, 0); texto `4362:480` en (181, 3.5), 136 × 29). |
| 2 | Subtítulo `4362:481` | (34, 90) · abs. (414, 267.5) | 452 × 15 | Igual que marco 1. |
| 3 | Grupo de campos `4362:482` | (34, 125) · abs. (414, 302.5) | 452 × **130** | Gap 12; ahora con 3 hijos. |
| 3a | Campo correo `4362:483` | (0, 0) · abs. (414, 302.5) | 452 × 46 | Borde 1.5 `#DAD8D2` (sin cambio). **Valor rellenado** en Tinta. |
| 3b | Campo contraseña `4362:486` | (0, 58) · abs. (414, 360.5) | 452 × 46 | **Borde 1.5 `#C4362E`** (Coral Texto). Valor rellenado en Tinta. |
| 3c | Mensaje de error `4362:492` («mensaje · error de credenciales») | (0, 116) · abs. (414, 418.5) | 452 × 14 | Archivo Regular **12.5**, `#C4362E`, alineado a la **izquierda**, una línea. Sin icono. Separado del campo por el gap 12. |
| 4 | Botón «Iniciar sesión» `4362:489` | (34, 275) · abs. (414, 452.5) | 452 × 44 | Igual que marco 1. |
| 5 | Enlace `4362:493` | (34, 339) · abs. (414, 516.5) | 452 × 44 | Igual que marco 1 (texto `4362:494` en (144.5, 14.5), 163 × 15). |
| 6 | Nota final `4362:491` | (34, 403) · abs. (414, 580.5) | 452 × 28 | Igual que marco 1. |

Comprobación: 439 + 12 (gap) + 14 (mensaje) = 465; (820 − 465) / 2 = 177.5.

### Controles

**Campo correo (lleno)**: igual geometría que marco 1 (46 alto, radio 12, borde 1.5 `#DAD8D2`, relleno 8/14, gap 2). Etiqueta Archivo SemiBold 11 `#66636D`. Valor Archivo Regular 15 **`#17161C`**.

**Campo contraseña (error)**: igual geometría; **único cambio de estilo: color de borde `#C4362E`**, grosor 1.5 (no engrosa). Fondo sigue `#FFFFFF`. Etiqueta «CONTRASEÑA» sigue en `#66636D` (no se colorea). Valor `••••••` (**6 puntos**) Archivo Regular 15 `#17161C`. Sin icono de error dentro del campo.

**Botón y enlace**: sin cambios respecto al marco 1.

### Textos literales

| Capa | Literal exacto | Fuente | Tamaño | Color | Alineación |
|---|---|---|---|---|---|
| `4362:485` | `andres@correo.com` | Archivo Regular | 15 | `#17161C` | izquierda |
| `4362:488` | `••••••` (6 puntos) | Archivo Regular | 15 | `#17161C` | izquierda |
| `4362:492` | `Correo o contraseña incorrectos. Inténtalo de nuevo o recupera tu contraseña.` | Archivo Regular | 12.5 | `#C4362E` | izquierda |

El resto de literales, idénticos al marco 1.

### Iconos / vectores
Solo el logotipo (igual que marco 1: `4362:478` anillo, `4362:479` punto). No hay icono de error.

---

## 4. W00 · Recuperar contraseña — nodeId `4362:427`

Marco 1280×820, fondo `#FFFFFF`, borde 1 `#DAD8D2`.

### Bloques de arriba abajo

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 0 | Tarjeta `4362:428` | (380, **213**) = abs. | 520 × **394** | Mismo estilo que marco 1 (borde 1.5 `#DAD8D2`, radio 14, relleno 34, gap 20). |
| 1 | Fila de marca `4362:429` | (34, 34) · abs. (414, 247) | 452 × 36 | Igual que marco 1 (logo `4362:430`; texto `4362:433` en (181, 3.5), 136 × 29). |
| 2 | **Título** `4362:445` | (34, 90) · abs. (414, 303) | 452 × **26** | **Bricolage Grotesque SemiBold 22**, `#17161C`, centrado. Nuevo respecto al marco 1. |
| 3 | Descripción `4362:434` | (34, 136) · abs. (414, 349) | 452 × **30** | Archivo Regular 14, `#66636D`, centrado; **2 líneas por ajuste automático** (la palabra «nueva.» cae sola en la segunda línea). Sustituye al subtítulo. |
| 4 | Grupo de campos `4362:435` | (34, 186) · abs. (414, 399) | 452 × 46 | Gap 12; un solo campo visible. Contiene el campo contraseña `4362:439` **oculto** (`hidden`), que no ocupa espacio. |
| 4a | Campo correo `4362:436` | (0, 0) · abs. (414, 399) | 452 × 46 | Idéntico al marco 1 (placeholder `#DAD8D2`). |
| 5 | Botón «Enviar enlace» `4362:442` («botón · Enviar enlace») | (34, 252) · abs. (414, 465) | 452 × 44 | Mismo estilo que «Iniciar sesión». Texto `4362:443` en (179.5, 14), 93 × 16. |
| 6 | Enlace `4362:446` («enlace · volver a iniciar sesión») | (34, 316) · abs. (414, 529) | 452 × 44 | Mismo estilo que el enlace del marco 1. Texto `4362:447` en (155.5, 14.5), 141 × 15. |
| — | Nota final `4362:444` | (34, 316) | 452 × 28 | **Oculta** (`hidden`); no se dibuja ni ocupa espacio. |

Comprobación: 34 + 36 + 20 + 26 + 20 + 30 + 20 + 46 + 20 + 44 + 20 + 44 + 34 = 394; (820 − 394) / 2 = 213.

### Controles

**Campo correo**: igual que marco 1 (452 × 46, radio 12, borde 1.5 `#DAD8D2`, relleno 8/14, gap 2; etiqueta SemiBold 11 `#66636D`; valor placeholder Regular 15 `#DAD8D2`).

**Botón «Enviar enlace»**: 452 × 44, `#FFC400`, radio 999, relleno 14/0, texto Archivo SemiBold 15 `#17161C`.

**Enlace «‹ Volver a iniciar sesión»**: marco 452 × 44, texto Archivo Regular 14 `#1A5BC4` subrayado. El «‹» es un carácter del texto (U+2039), no un icono, y queda dentro del subrayado.

### Textos literales

| Capa | Literal exacto | Fuente | Tamaño | Color | Alineación |
|---|---|---|---|---|---|
| `4362:433` | `Alarmas QR` | Bricolage Grotesque Bold | 24 | `#17161C` | — |
| `4362:445` | `Recuperar contraseña` | Bricolage Grotesque SemiBold (600) | 22 | `#17161C` | centro |
| `4362:434` | `Te enviaremos un enlace al correo registrado para crear una contraseña nueva.` | Archivo Regular | 14 | `#66636D` | centro |
| `4362:437` | `CORREO ELECTRÓNICO` | Archivo SemiBold | 11 | `#66636D` | izquierda |
| `4362:438` | `nombre@correo.com` | Archivo Regular | 15 | `#DAD8D2` | izquierda |
| `4362:443` | `Enviar enlace` | Archivo SemiBold | 15 | `#17161C` | centro |
| `4362:447` | `‹ Volver a iniciar sesión` | Archivo Regular, subrayado | 14 | `#1A5BC4` | centro |

### Iconos / vectores
Solo el logotipo (`4362:431` anillo, `4362:432` punto), idéntico al marco 1.

---

## 5. W00 · correo enviado — nodeId `4362:449`

Nombre de la capa: «W00 · Inicio de sesión (correo enviado)». Idéntico al marco 2 (tarjeta `4362:450` en (380, 222.5), 520 × 375, **sin enlace «¿Olvidaste tu contraseña?»**, nota `4362:466` en (34, 313)) salvo el snackbar.

### Bloques de arriba abajo (diferencias)

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 7 | Snackbar `4362:467` | **abs. (509, 760)** (código: left 508 / top 759) | **459 × 36** | Mismo estilo que marco 2: fondo `#17161C`, radio 999, relleno 10/18, gap 8, sin sombra. **Mismo x que el del marco 2 → no está centrado** (su centro cae en 738.5, no en 640). |
| 7a | Icono `4362:468` | (18, 10) | 16 × 16 | Mismo SVG que marco 2 (`b509b.svg`). |
| 7b | Texto `4362:471` | (42, 10.5) | 399 × 15 | Archivo SemiBold 14, `#FFFFFF`. |

### Controles
Sin cambios respecto al marco 2.

### Textos literales
- Snackbar: `Te enviamos un correo de recuperación a andres@correo.com`

### Iconos / vectores
Icono de visto del marco 2 (círculo blanco + visto `#0B7048`).

---

## Observaciones

Contradicciones con los tokens del proyecto:

1. **Tarjeta de acceso: 520 de ancho, no 400.** Radio 14 y borde 1.5 `#DAD8D2` sí coinciden. Relleno 34 y gap 20 (no hay token para ellos).
2. **Campos: 46 de alto, no 48.** El alto no es fijo: sale de relleno 8 + etiqueta 12 + gap 2 + valor 16 + 8. Radio 12 ✓, borde 1.5 ✓.
3. **Etiqueta del campo: Archivo SemiBold 11, no 13** (token «texto etiqueta 13»). Está por debajo del mínimo de 12 que marca CLAUDE.md (solo la barra inferior móvil puede ir en 11).
4. **Placeholder en `#DAD8D2` (Gris borde) sobre blanco**: contraste muy bajo; no es un tono AA. Los valores rellenos (marco 3) van en Tinta.
5. **Botón**: alto 44 ✓ y radio 999 ✓, pero **relleno 14 vertical / 0 horizontal** (ancho completo, 452), no 13/24; **texto Archivo SemiBold 15, no 14.5**.
6. **Marca: Bricolage Bold 24, no 17** (el token de marca web 17 se refiere probablemente a la barra lateral; aquí la marca va a 24, igual que el token h1).
7. **Título «Recuperar contraseña»: Bricolage SemiBold 22**, no el h1 web Bold 24.
8. **Nota final: 13**, no 12.5. El único texto en 12.5 es el mensaje de error del marco 3.
9. **Snackbar**: alto 36 ✓; relleno 10/18, gap 8, radio 999, fondo Tinta, texto SemiBold 14 blanco, icono 16. A 24 del borde inferior.
10. Enlaces: Azul texto `#1A5BC4` ✓, 14, subrayado, en un marco de 44 de alto (área de puntero igual al botón).
11. Colores usados, todos del conjunto de tokens: `#17161C`, `#FFC400`, `#C4362E`, `#1A5BC4`, `#66636D`, `#DAD8D2`, `#0B7048`, `#FFFFFF`. No aparecen Gris medio `#77747E` ni Gris niebla `#F4F3EF`. El fondo de la página es blanco, no Gris niebla.

Cosas raras:

- **Snackbar del marco 5 descentrado**: comparte x = 509 con el del marco 2 pese a ser más ancho (459 frente a 262). Parece copiado sin recentrar; si la intención es un snackbar centrado, su x debería ser 410.5.
- **Los marcos 2 y 5 (con snackbar) no tienen el enlace «¿Olvidaste tu contraseña?»** que sí tienen los marcos 1 y 3; por eso la tarjeta mide 375 en lugar de 439 y queda en y = 222.5. Podría ser un olvido al añadir el enlace después (los ids `4362:*` del enlace son posteriores a `4072:*`).
- El campo contraseña del marco 1 cuenta 8 puntos (placeholder); el del marco 3, 6 puntos (valor escrito).
- El mensaje de error comunica el estado con color + texto (no hay icono); el campo en error solo cambia el color del borde, sin cambiar grosor ni añadir símbolo.
- No hay estado de foco dibujado en ningún marco del grupo.
- El marco 4 conserva capas ocultas (campo contraseña `4362:439`, nota `4362:444`) que no deben maquetarse.
- En el código de Figma el snackbar sale en left 508 / top 759 y en los metadatos en (509, 760): la diferencia de 1 es el borde de 1 del marco del mockup; la posición real respecto al lienzo 1280×820 es (509, 760).
- Todos los textos usan interlineado `normal` (no hay line-height explícito); para reproducir las alturas: 24 → 29, 22 → 26, 15 → 16, 14 → 15, 13 → 14, 12.5 → 14, 11 → 12.

---

# Hoja de medidas · Grupo W06

Fuente: Figma `4nHD4ygcnP33UH0gAhaii5`, página `03 · Web`, marcos 1280×820. Medido el 2026-09-21 con `get_metadata` + `get_design_context` + `get_screenshot`.

Convenciones de esta hoja:
- Pos. = (x, y) relativa al padre; «abs» = respecto al marco 1280×820.
- Interlineado «normal» = `line-height: normal` en Figma (auto). Tracking = 0 en todos los textos salvo que se diga otra cosa (ningún texto trae `letter-spacing`).
- Fuentes: Bricolage Grotesque (`opsz 14, wdth 100`) y Archivo (`wdth 100`).
- Las mayúsculas de las etiquetas están escritas así en el texto (no hay `text-transform`).

Marco común a los tres W06 (no es el foco; lo mide otro agente):
- Barra superior `4072:234`: (0,0) 1280×64, fondo #FFFFFF, borde inferior 1 px #DAD8D2, relleno horizontal 28, `justify-between`, `items-center`. Logo 32×32 fondo #FFC400 radio 8 + «Alarmas QR» Bricolage 700 17 #17161C (gap 10). A la derecha «Andrés Rojas» Archivo 400 14 #66636D + avatar 32×32 radio 16, fondo #F4F3EF, borde 1.5 #17161C (gap 10); bloque en (1126,16) 126×32.
- Barra lateral `4072:243` («side»): ancho **208**, alto 756, fondo **#FFFFFF**, borde derecho 1 px #DAD8D2 (abs (0,64)).
- Área de contenido «main» `4072:255`: abs (208,64), 1072×756, fondo #FFFFFF, `flex-col`, relleno **28 vertical / 32 horizontal**, gap **18**.

---

## 1. W06 · Ajustes de Perfil — nodeId `4072:233`

### Bloques de arriba abajo

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | Título «Ajustes de Perfil» (`4075:1673`) | (32,28) · abs (240,92) | 189×29 | Bricolage Grotesque Bold 700, 24, interlineado normal, #17161C. No hay subtítulo ni avatar en la cabecera del contenido. |
| 2 | «row» (`4099:2`) | (32,75) · abs (240,139) | 1008×383 | `flex` fila, gap **18**, `items-start`, fondo #FFFFFF. Dos columnas: tarjeta Perfil 561 fija + «col» flexible (429). |
| 2a | Tarjeta «Perfil» (`4075:1674`) | (0,0) · abs (240,139) | **561×310** | Fondo #FFFFFF, borde **1.5 #DAD8D2**, radio **14**, relleno **22**, `flex-col` gap **16**. |
| 2a.1 | Título «Perfil» (`4075:1675`) | (22,22) · abs (262,161) | 45×20 | Bricolage 700, 17, normal, #17161C. |
| 2a.2 | Campo Nombres (`4075:1676`) | (22,58) · abs (262,197) | 517×**46** | Ver Controles · campo. |
| 2a.3 | Campo Alias (`4075:1679`) | (22,120) · abs (262,259) | 517×46 | Ídem. |
| 2a.4 | Campo Correo (`4075:1682`) | (22,182) · abs (262,321) | 517×46 | Ídem. |
| 2a.5 | Fila acción (`4075:1685`) | (22,244) · abs (262,383) | 517×44 | `justify-end`. Botón «Guardar cambios» (`4075:1686`) en (354,0) · abs (616,383), 163×44. |
| 2b | «col» (`4099:3`) | (579,0) · abs (819,139) | 429×383 | `flex-col`, gap **18**, `flex: 1 0 0`. |
| 2b.1 | Tarjeta «Privacidad ante organizadores» (`4075:1688`) | (0,0) · abs (819,139) | **429×211** | Fondo #FFFFFF, borde 1.5 #DAD8D2, radio 14, relleno **22**, `flex-col` gap **12**. |
| 2b.1.a | Título (`4075:1689`) | (22,22) · abs (841,161) | 253×20 | Bricolage 700, 17, #17161C. |
| 2b.1.b | Descripción (`4075:1690`) | (22,54) · abs (841,193) | 385×15 | Archivo 400, 13.5, normal, #66636D. |
| 2b.1.c | Selector segmentado (`4075:1691`) | (22,81) · abs (841,220) | 385×**36** | Ver Controles. |
| 2b.1.d | Fila switch 1 (`4075:1698`) | (22,129) · abs (841,268) | 385×24 | Rótulo (0,4) 343×16 + switch (`4075:1700`) en (343,0) · abs (1184,268) 42×24. `justify-between`, `items-center`. |
| 2b.1.e | Fila switch 2 (`4075:1702`) | (22,165) · abs (841,304) | 385×24 | Ídem; switch (`4075:1704`) abs (1184,304). |
| 2b.2 | Tarjeta «Eliminación de cuenta» (`4075:1706`) | (0,229) · abs (819,368) | **429×154** | Fondo **#FFFFFF** (no coral suave), borde **1.5 #C4362E**, radio 14, relleno **20**, `flex-col` gap **10**. |
| 2b.2.a | Título (`4075:1707`) | (20,20) · abs (839,388) | 183×20 | Bricolage 700, 17, #17161C. |
| 2b.2.b | Texto (`4075:1708`) | (20,50) · abs (839,418) | 389×30 (2 líneas) | Archivo 400, 13.5, normal, #66636D. |
| 2b.2.c | Botón «Eliminar mi cuenta» (`4075:1709`) | (20,90) · abs (839,458) | 171×44 | Ver Controles. |
| 3 | Nota (`4075:1711`) | (32,476) · abs (240,540) | 1008×15 | Archivo 400, **14**, normal, #66636D. |

Layout: dos columnas (561 fija + 429 que ocupa el resto), gap 18; la columna derecha apila dos tarjetas con gap 18. La tarjeta Perfil (310) es más baja que la columna derecha (383): `items-start`, no se estiran. Nota a 18 bajo la fila.

### Controles

**Campo con etiqueta interna** (`4075:1676`, `…1679`, `…1682`)
- 517×46 (alto resultante: 8 + 12 + 2 + 16 + 8 = 46; el borde va por dentro).
- Fondo #FFFFFF, borde **1.5 #DAD8D2**, radio **12**, relleno **8 vertical / 14 horizontal**, `flex-col` gap **2**.
- Etiqueta: Archivo SemiBold 600, **11**, normal, #66636D, mayúsculas escritas, tracking 0 (posición (14,8), alto 12).
- Valor: Archivo Regular 400, **15**, normal, #17161C (posición (14,22), alto 16).
- Estado: reposo (sin foco).

**Botón primario «Guardar cambios»** (`4075:1686`)
- 163×44, fondo #FFC400, radio 999, relleno **13 / 24**, sin borde.
- Texto Archivo SemiBold 600, **14.5**, normal, #17161C (posición (24,14), 115×16).

**Selector segmentado** (`4075:1691`)
- Pista: 385×36, fondo **#F4F3EF**, radio 999, relleno **3**, gap **2**.
- Segmentos: 3 × **125×30**, `flex: 1`, radio 999, relleno vertical **8**, centrados. Posiciones: (3,3), (130,3), (257,3).
- Inactivo («Nombre completo», «Alias»): fondo #F4F3EF, texto Archivo SemiBold 600, **13**, normal, **#66636D**.
- Activo («Solo iniciales»): fondo **#17161C**, texto Archivo 600, 13, **#FFFFFF**. Sin icono ni otra marca de forma más allá del relleno Tinta.

**Switch** (`4075:1700`, `4075:1704`) — vector (SVG exportado):
- Tamaño **42×24**; pista `rect` 42×24 **rx 12**, relleno **#17161C** (encendido).
- Pulgar `circle` **r 9** (diámetro 18), centro **(29, 11)**, relleno **#FFFFFF**. Nota: cy = 11, no 12 → el pulgar queda 1 px arriba del centro vertical (margen sup. 2, inf. 4); margen derecho 42 − 38 = 4.
- Ambos switches están **encendidos** y usan el mismo SVG. **No hay estado apagado dibujado** en estos marcos (no se puede medir su color).
- Rótulos: Archivo Regular 400, **14.5**, normal, #17161C, ancho 343.

**Botón contorno destructivo «Eliminar mi cuenta»** (`4075:1709`)
- 171×44, fondo #FFFFFF, borde **1.5 #C4362E**, radio 999, relleno **13 / 24**.
- Texto Archivo SemiBold 600, 14.5, normal, **#C4362E** (123×16).

### Textos literales

| id | Literal exacto | Fuente | Tamaño / interl. | Color |
|---|---|---|---|---|
| 4075:1673 | `Ajustes de Perfil` | Bricolage 700 | 24 / normal | #17161C |
| 4075:1675 | `Perfil` | Bricolage 700 | 17 / normal | #17161C |
| 4075:1677 | `NOMBRES Y APELLIDOS` | Archivo 600 | 11 / normal | #66636D |
| 4075:1678 | `Andrés Rojas` | Archivo 400 | 15 / normal | #17161C |
| 4075:1680 | `ALIAS PÚBLICO` | Archivo 600 | 11 | #66636D |
| 4075:1681 | `Andrés R.` | Archivo 400 | 15 | #17161C |
| 4075:1683 | `CORREO ELECTRÓNICO` | Archivo 600 | 11 | #66636D |
| 4075:1684 | `andres@correo.com` | Archivo 400 | 15 | #17161C |
| 4075:1687 | `Guardar cambios` | Archivo 600 | 14.5 | #17161C |
| 4075:1689 | `Privacidad ante organizadores` | Bricolage 700 | 17 | #17161C |
| 4075:1690 | `Así apareces en "Quiénes escanearon" (Ley 1581).` (comillas rectas ASCII) | Archivo 400 | 13.5 | #66636D |
| 4075:1693 | `Nombre completo` | Archivo 600 | 13 | #66636D |
| 4075:1695 | `Solo iniciales` | Archivo 600 | 13 | #FFFFFF |
| 4075:1697 | `Alias` | Archivo 600 | 13 | #66636D |
| 4075:1699 | `Mostrar el estado de mi alarma` | Archivo 400 | 14.5 | #17161C |
| 4075:1703 | `Contar mi "Ya voy" en las métricas` (comillas rectas) | Archivo 400 | 14.5 | #17161C |
| 4075:1707 | `Eliminación de cuenta` | Bricolage 700 | 17 | #17161C |
| 4075:1708 | `Borra permanentemente tu perfil, eventos creados y el historial de escaneos.` | Archivo 400 | 13.5 | #66636D |
| 4075:1710 | `Eliminar mi cuenta` | Archivo 600 | 14.5 | #C4362E |
| 4075:1711 | `Los cambios de perfil no afectan tus alarmas en el celular: en modo invitado siguen siendo locales.` | Archivo 400 | 14 | #66636D |

### Iconos / vectores

Switch encendido (42×24), igual en `4075:1700` y `4075:1704`:

```svg
<svg width="42" height="24" viewBox="0 0 42 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <rect width="42" height="24" rx="12" fill="#17161C"/>
  <circle cx="29" cy="11" r="9" fill="white"/>
</svg>
```

No hay otros iconos en el área de contenido (los de la barra lateral los mide el otro agente).

---

## 2. W06 · Actualizado — nodeId `4072:279`

Idéntico a §1 (mismas medidas, ids `4075:1782…1821`) salvo el snackbar.

### Bloques de arriba abajo

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | Snackbar (`4075:1822`) | abs (**554.5, 762**) — hijo directo del marco | **171×36** | Centrado en el ancho del **marco completo** (centro x = 640), no del área de contenido (cuyo centro sería 744). Margen inferior 820 − 798 = **22**. |
| 1a | Icono (`4075:1823`) | (18,10) · abs (572.5,772) | 16×16 | Vector, ver abajo. |
| 1b | Texto (`4075:1826`) | (42,10.5) · abs (596.5,772.5) | 111×15 | — |

### Controles

**Snackbar**: fondo **#17161C**, radio **999**, relleno **10 vertical / 18 horizontal**, `flex` fila, gap **8**, `items-center`. Sin botón de acción («Deshacer») ni cierre. Sin sombra.

### Textos literales

| id | Literal | Fuente | Tamaño / interl. | Color |
|---|---|---|---|---|
| 4075:1826 | `Perfil actualizado` | Archivo SemiBold 600 | 14 / normal | #FFFFFF |

### Iconos / vectores

Icono de éxito 16×16 (círculo blanco + visto Verde texto):

```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M8 14C11.3137 14 14 11.3137 14 8C14 4.68629 11.3137 2 8 2C4.68629 2 2 4.68629 2 8C2 11.3137 4.68629 14 8 14Z" fill="white"/>
  <path d="M5.33333 8L7.33333 10L10.6667 6" stroke="#0B7048" stroke-width="1.46667" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

---

## 3. W06 · Modal eliminar cuenta — nodeId `4072:256`

Fondo: W06 idéntico a §1 (ids `4075:1712…1751`), cubierto por el velo.

### Bloques de arriba abajo

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | Velo «scrim» (`4075:1752`) | abs (0,0) | 1280×820 | **rgba(23,22,28,0.45)**; cubre también barra superior y lateral. `flex` centrado. |
| 2 | Modal (`4075:1753`) | abs (**399.5, 217**) | **481×386** (ancho fijo 481; alto por contenido) | Fondo #FFFFFF, borde **1.5 #C4362E**, radio **14**, relleno **22**, `flex-col` gap **16**. Sin sombra. Centrado en el marco (centro 640, 410). |
| 2a | Cabecera (`4075:1754`) | (22,22) · abs (421.5,239) | 437×24 | `flex` fila gap **10**, `items-center`: icono advertencia 24×24 + título (34,1) 317×22. **No hay fila miga + ✕** (ni miga «‹ …» ni botón cerrar). |
| 2b | Intro (`4075:1759`) | (22,62) · abs (421.5,279) | 437×16 | Archivo 400 14.5 #17161C con tramo en Bold. |
| 2c | Lista (`4075:1760`) | (22,94) · abs (421.5,311) | 437×136 | `flex-col` gap **8**. Cada ítem: `flex` fila gap **8**, `items-start`, icono 16×16 + texto 413 de ancho. Ítems en y = 0 (32 alto), 40 (32), 80 (32), 120 (16). |
| 2d | Grupo campo (`4075:1773`) | (22,246) · abs (421.5,463) | 437×58 | `flex-col` gap **4**: etiqueta (0,0) 201×14 + campo (0,18) 437×40. |
| 2e | Acciones (`4075:1777`) | (22,320) · abs (421.5,537) | 437×44 | `flex` fila gap **10**; dos botones `flex: 1` de **213.5×44**: «Conservar mi cuenta» (0,0) abs (421.5,537) y «Eliminar definitivamente» (223.5,0) abs (645,537). |

### Controles

**Campo «Escribe ELIMINAR»** (`4075:1775`)
- 437×**40**, fondo #FFFFFF, borde **1.5 #DAD8D2**, radio **12**, relleno **12 / 14**.
- Etiqueta externa (`4075:1774`): Archivo SemiBold 600, **13**, normal, #17161C, encima del campo con gap 4.
- Contenido: placeholder `ELIMINAR` Archivo 400, 15, normal, color **#BFBDBD** (vacío, sin foco).

**Botón «Conservar mi cuenta»** (`4075:1778`) — primario, izquierda
- 213.5×44, fondo #FFC400, radio 999, relleno vertical 13 (sin relleno horizontal explícito; ancho por `flex: 1`), texto centrado.
- Texto Archivo 600, 14.5, normal, #17161C.

**Botón «Eliminar definitivamente»** (`4075:1780`) — contorno coral, derecha
- 213.5×44, fondo #FFFFFF, borde **1.5 #C4362E**, radio 999, relleno vertical 13.
- Texto Archivo 600, 14.5, normal, #C4362E.
- **Opacidad 45 %** (estado deshabilitado hasta escribir ELIMINAR).

Orden: seguro (amarillo) a la izquierda, destructivo a la derecha, en fila, mismo ancho.

### Textos literales

| id | Literal exacto (en **negrita** los tramos Archivo Bold 700) | Fuente | Tamaño | Color |
|---|---|---|---|---|
| 4075:1758 | `¿Eliminar tu cuenta definitivamente?` | Bricolage Grotesque Bold 700 | **18** / normal | #17161C |
| 4075:1759 | `Esta acción `**`no se puede deshacer`**`. Al confirmar:` | Archivo 400 (+700) | 14.5 / normal | #17161C |
| 4075:1763 | `Tus `**`4 eventos publicados`**` se despublican y sus QR dejan de funcionar.` | Archivo 400 (+700) | 14.5 | #17161C |
| 4075:1766 | `Las `**`97 alarmas de asistentes`**` dejan de recibir actualizaciones (no se borran de sus celulares).` | Archivo 400 (+700) | 14.5 | #17161C |
| 4075:1769 | `Tus datos personales se eliminan en máximo `**`30 días`**` (Ley 1581 · habeas data).` | Archivo 400 (+700) | 14.5 | #17161C |
| 4075:1772 | `Consejo: descarga antes tus reportes (Reportes → PDF / CSV).` | Archivo 400 | 14.5 | #17161C |
| 4075:1774 | `Escribe ELIMINAR para confirmar` | Archivo 600 | 13 | #17161C |
| 4075:1776 | `ELIMINAR` (placeholder) | Archivo 400 | 15 | #BFBDBD |
| 4075:1779 | `Conservar mi cuenta` | Archivo 600 | 14.5 | #17161C |
| 4075:1781 | `Eliminar definitivamente` | Archivo 600 | 14.5 | #C4362E (a opacidad 45 %) |

El consejo es el 4.º ítem de la lista, mismo estilo que los demás (sin caja ni color propio), con icono de descarga.

### Iconos / vectores

Advertencia 24×24 (`4075:1755`), trazo #C4362E 2:

```svg
<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M12 3L2 20H22L12 3Z" stroke="#C4362E" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M12 9V14M12 17.5V18" stroke="#C4362E" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

Viñetas 16×16, trazo #17161C 1.33333, extremos y uniones redondeados:

QR (`4117:2`, eventos):
```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M6.66667 2H2V6.66667H6.66667V2Z" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M14 2H9.33333V6.66667H14V2Z" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M6.66667 9.33333H2V14H6.66667V9.33333Z" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M12.6667 9.33333H14M9.33333 12.6667H10.6667M12.6667 12.6667H14V14M9.33333 9.33333H11.3333V11.3333H9.33333V9.33333Z" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

Calendario (`4117:7`, alarmas):
```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M12.6667 3.33333H3.33333C2.59695 3.33333 2 3.93029 2 4.66667V12.6667C2 13.403 2.59695 14 3.33333 14H12.6667C13.403 14 14 13.403 14 12.6667V4.66667C14 3.93029 13.403 3.33333 12.6667 3.33333Z" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M2 6.66667H14M5.33333 2V4.66667M10.6667 2V4.66667" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

Persona (`4117:10`, datos personales):
```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M8 8C9.47276 8 10.6667 6.80609 10.6667 5.33333C10.6667 3.86057 9.47276 2.66667 8 2.66667C6.52724 2.66667 5.33333 3.86057 5.33333 5.33333C5.33333 6.80609 6.52724 8 8 8Z" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M2.66667 14C2.66667 12.5855 3.22857 11.229 4.22876 10.2288C5.22896 9.22857 6.58551 8.66667 8 8.66667C9.41449 8.66667 10.771 9.22857 11.7712 10.2288C12.7714 11.229 13.3333 12.5855 13.3333 14" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

Descarga (`4117:13`, consejo):
```svg
<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M8 2.66667V10M11.3333 6.66667L8 10L4.66667 6.66667M2.66667 13.3333H13.3333" stroke="#17161C" stroke-width="1.33333" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

---

## 4. Diálogo · ¿Cerrar sesión? — nodeId `4360:347`

Fondo: pantalla W01 «Mis Alarmas» completa (`4360:348`) debajo del velo (no W06).

### Bloques de arriba abajo

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | Velo «atenuado · cerrar» (`4360:540`) | abs (0,0) | 1280×820 | **rgba(23,22,28,0.55)**; es un enlace (`cursor-pointer`): tocar el velo cierra. |
| 2 | Diálogo (`4360:541`) | abs (**430, 289**) | **420×242** | Fondo #FFFFFF, **sin borde**, radio **14**, relleno **24**, `flex-col` gap **12**, sombra **0 12 32 0 rgba(0,0,0,0.18)**. Centrado (centro 640, 410). |
| 2a | Título (`4360:542`) | (24,24) · abs (454,313) | 372×26 | — |
| 2b | Cuerpo (`4360:543`) | (24,62) · abs (454,351) | 372×40 (2 líneas) | — |
| 2c | «acciones» (`4360:544`) | (24,114) · abs (454,403) | 372×104 | `flex-col` gap **8**, relleno superior **8**. |
| 2c.1 | «Cancelar» (`4360:545`) | (0,8) · abs (454,411) | **372×44** | Primario amarillo, arriba. |
| 2c.2 | «Cerrar sesión» (`4360:547`) | (0,60) · abs (454,463) | **372×44** | Contorno Tinta, abajo. |

### Controles

Botones **apilados**, ancho completo (372), gap 8; orden: «Cancelar» (seguro) arriba, «Cerrar sesión» abajo.

- **Cancelar**: 44 alto, fondo #FFC400, radio 999, relleno horizontal 24 (sin relleno vertical explícito, alto fijo 44), texto centrado Archivo SemiBold 600, **14**, normal, #17161C (58×15 en (157,14.5)).
- **Cerrar sesión**: 44 alto, fondo #FFFFFF, borde **1.5 #17161C**, radio 999, relleno horizontal 24, texto Archivo 600, **14**, normal, **#17161C** (87×15 en (142.5,14.5)).

### Textos literales

| id | Literal exacto | Fuente | Tamaño / interl. | Color |
|---|---|---|---|---|
| 4360:542 | `¿Cerrar sesión?` | Bricolage Grotesque SemiBold 600 | 22 / normal | #17161C |
| 4360:543 | `Tus alarmas siguen activas en el celular. Para volver a consultar tus eventos tendrás que iniciar sesión de nuevo.` | Archivo Regular 400 | 14 / **1.4** | **#66636D** |
| 4360:546 | `Cancelar` | Archivo 600 | 14 / normal | #17161C |
| 4360:548 | `Cerrar sesión` | Archivo 600 | 14 / normal | #17161C |

### Iconos / vectores

Ninguno en el diálogo (sin icono en el título, sin ✕).

---

## Observaciones (contradicciones y coincidencias con los tokens)

Coinciden: contenido web relleno 28/32 y gap 18; h1 Bricolage 700 24; botón 44 alto, radio 999, relleno 13/24 y texto Archivo 600 14.5 (Guardar cambios, Eliminar mi cuenta, botones del modal); radio de campo 12; tarjetas radio 14 y borde 1.5 #DAD8D2; selector segmentado 36; snackbar 36; velo del modal rgba(23,22,28,0.45); diálogo 420, radio 14, velo 0.55, título Bricolage 600 22, cuerpo Archivo 400 14/1.4.

Contradicciones:
1. **Campos de W06 miden 46 de alto**, no 48 (relleno 8/14 + etiqueta 11 + gap 2 + valor 15). El campo del modal mide **40** (relleno 12/14). Token: campo 48.
2. **Etiqueta interna del campo en Archivo 600 11** (#66636D, sin tracking): por debajo del mínimo de 12 pt del DS (la única excepción documentada es la barra inferior móvil, 11). Tampoco coincide con el h3 (Archivo 700 13 mayúsculas .08em).
3. **Modal «Eliminar cuenta»: ancho 481**, fuera del rango token min 540 / max 600; **relleno 22** (token 24); **gap 16**; borde **1.5 #C4362E** (el token no habla de borde); sin sombra.
4. **Título del modal: Bricolage 700 18**, no 21 como el token.
5. **El modal no tiene fila de cabecera miga + ✕** (CLAUDE.md exige «la fila de cabecera de un modal (miga + ✕) es un solo control que cierra»). Lleva icono de advertencia 24 + título.
6. Botón «Eliminar definitivamente» con **opacidad 45 %** (estado deshabilitado) y sin relleno horizontal (ancho por flex). Placeholder **#BFBDBD**: color que no existe entre los tokens (ni Gris medio #77747E ni Gris texto #66636D).
7. **Botones del diálogo con texto Archivo 600 14**, no 14.5; relleno solo horizontal 24 (alto fijo 44, sin 13 vertical). Sombra 0 12 32 rgba(0,0,0,0.18): negro puro, no Tinta.
8. **Cuerpo del diálogo en #66636D** (Gris texto); el token dice 14/1.4 pero no especifica color.
9. **Tarjeta «Eliminación de cuenta» no es coral de fondo**: fondo #FFFFFF, solo borde 1.5 #C4362E (Coral Texto, no Coral alarma #E8443A ni Coral suave #FDECEA). Relleno 20 y gap 10, distintos de las otras tarjetas (22 y 16/12).
10. Relleno de tarjetas **22** (Perfil y Privacidad), no hay token; gaps internos 16 (Perfil) y 12 (Privacidad).
11. **Switch**: solo existe el estado encendido (pista #17161C, pulgar blanco r 9). El pulgar está en cy = 11 (descentrado 1 px hacia arriba respecto a la pista de 24). No hay estado apagado para medir.
12. Textos de 13.5 y 14.5 (descripciones y rótulos de switch) y títulos de tarjeta Bricolage 700 17: no hay tokens de texto listados para esos tamaños en la tabla recibida.
13. **Snackbar**: centrado respecto al marco completo (x 640), no al área de contenido; texto Archivo 600 14 blanco, relleno 10/18, gap 8, sin acción «Deshacer».
14. Comillas rectas ASCII (`"…"`) en «Quiénes escanearon» y «Ya voy», no comillas latinas «».
15. Fila de botones de la tarjeta Perfil: botón alineado a la derecha (`justify-end`); el botón del modal y el diálogo usan orden seguro-primero (izquierda / arriba).

---

# Hoja de medidas · Barra lateral e iconos web

Fuente: Figma `4nHD4ygcnP33UH0gAhaii5`, leído el 2026-09-21 con `get_metadata` + `get_design_context` (nodos `side`, `topbar` y símbolos de `00 · Recursos gráficos`). SVG descargados de los assets del MCP y transcritos literalmente (solo se quitaron `preserveAspectRatio`, `overflow`, `style`, `width/height` y los `id` de capa).

Convenciones: «Pos.» = x,y relativa al padre; «abs.» = respecto al marco 1280×820. Colores en hex.

---

## 1. Barra superior web (común a W01 y W06) — nodeId `4072:234` (W06) / `4072:27` (W01)

Idéntica en ambos marcos (mismos tamaños y posiciones). **La marca «Alarmas QR» vive aquí, no en la barra lateral.**

### Bloques

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | `topbar` (4072:234) | 0,0 (abs. 0,0) | 1280×64 | Fondo `#FFFFFF`; borde inferior 1 px `#DAD8D2`; flex fila, `justify-between`, `align-items:center`; relleno horizontal 28 (sin relleno vertical) |
| 2 | Grupo marca `Frame` (4072:235) | 28,16 (abs. 28,16) | 139×32 | Fondo `#FFFFFF`; flex fila, gap 10, centrado vertical |
| 3 | Logo `Frame` (4072:236) | 0,0 (abs. 28,16) | 32×32 | Fondo `#FFC400`, radio 8, `overflow: clip` |
| 3a | `Rectangle` (4087:1579) | 8,8 dentro del logo (abs. 36,24) | 16×16 | Sin relleno; borde 2.286 px `#17161C`; radio 4 |
| 3b | `Rectangle` (4087:1580) | 13.14,13.14 (abs. 41.14,29.14) | 5.714×5.714 | Relleno `#17161C`; radio 1.429 |
| 4 | Texto «Alarmas QR» (4072:238) | 42,6 (abs. 70,22) | 97×20 | Bricolage Grotesque Bold (700), 17 px, `#17161C`, line-height normal, `opsz 14` |
| 5 | `avatarLink` (4072:239) | 1126,16 (abs. 1126,16) | 126×32 | Fondo `#FFFFFF`; flex fila, gap 10, centrado vertical; borde derecho del grupo a 28 del marco |
| 6 | Texto «Andrés Rojas» (4072:240) | 0,8.5 (abs. 1126,24.5) | 84×15 | Archivo Regular (400), 14 px, `#66636D` |
| 7 | Avatar `Frame` (4072:241) | 94,0 (abs. 1220,16) | 32×32 | Fondo `#F4F3EF`; borde 1.5 px `#17161C`; radio 16 (círculo); **vacío: sin iniciales ni imagen** |

### Título de página (dentro de `main`, no en la barra superior)

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 8 | W06 «Ajustes de Perfil» (4075:1673) | 32,28 en `main` (abs. 240,92) | 189×29 | Bricolage Grotesque Bold 24 px `#17161C`, line-height normal |
| 9 | W01 fila título (4073:2) | 32,28 en `main` (abs. 240,92) | 1008×44 | Título «Mis Alarmas» (4073:3) en 0,7.5 (144×29), Bricolage Bold 24 `#17161C`; botones a la derecha |

`main` empieza en x=208 (expandida) / x=64 (colapsada), y=64; relleno izquierdo del contenido 32, superior 28.

### Textos literales

- «Alarmas QR»
- «Andrés Rojas»
- Título W06: «Ajustes de Perfil» · Título W01: «Mis Alarmas»

---

## 2. Barra lateral EXPANDIDA · W06 Ajustes de Perfil — nodeId `4072:243`

Padre `bodyRow` (4072:242) en abs. 0,64, 1280×756.

### Bloques

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | `side` (4072:243) | 0,0 (abs. 0,64) | 208×756 | Fondo `#FFFFFF`; **borde derecho 1 px `#DAD8D2`**; flex columna, `align-items:flex-start`, **gap 4**; relleno 20 (arriba/abajo) × 12 (izq./der.) |
| 2 | `botón · colapsar menú` (4357:1810) | 12,20 (abs. 12,84) | 36×36 | Relleno 8; radio 999 (píldora); **sin fondo**; `overflow: clip`; centra el icono. En W06 es `div`, en W01 es `<a>` (cursor puntero) |
| 2a | `icono · colapsar` (4357:1811) | 8,8 (abs. 20,92) | 20×20 | Doble chevrón ««» apuntando a la izquierda, trazo `#17161C` |
| 3 | `nav:Mis Alarmas` (4072:244) | 12,60 (abs. 12,124) | 184×38 | Inactivo: fondo `rgba(255,255,255,0)`, radio 999; relleno 9 (vert.) / 16 (horiz.); flex fila gap 10, centrado vertical; `<a>` cursor puntero |
| 3a | `icono · alarma` (4357:1787) | 16,9 (abs. 28,133) | 20×20 | Trazo `#17161C` |
| 3b | Texto (4072:245) | 46,11 (abs. 58,135) | 83×16 | Archivo SemiBold (600) 14.5 px `#17161C`, line-height normal |
| 4 | `nav:Reportes` (4072:246) | 12,102 (abs. 12,166) | 184×38 | Inactivo (igual a 3) |
| 4a | `icono · reportes` (4357:1792) | 16,9 (abs. 28,175) | 20×20 | Trazo `#17161C` |
| 4b | Texto (4072:247) | 46,11 (abs. 58,177) | 62×16 | Archivo 600 14.5 `#17161C` |
| 5 | `nav:Descargar QR` (4072:248) | 12,144 (abs. 12,208) | 184×38 | Inactivo |
| 5a | `icono · escanear` (4357:1797) | 16,9 (abs. 28,217) | 20×20 | Trazo y cuadro central `#17161C` |
| 5b | Texto (4072:249) | 46,11 (abs. 58,219) | 94×16 | Archivo 600 14.5 `#17161C` |
| 6 | `nav:Ajustes de Perfil` (4072:250) — **ACTIVO** | 12,186 (abs. 12,250) | 184×38 | Fondo `#17161C` (Tinta), radio 999; relleno 9/16; gap 10; es `div` (no enlace) |
| 6a | `icono · ajustes` (4357:1803) | 16,9 (abs. 28,259) | 20×20 | Trazo `#FFFFFF` |
| 6b | Texto (4072:251) | 46,11 (abs. 58,261) | 108×16 | Archivo 600 14.5 `#FFFFFF` |
| 7 | `spacer` (4072:252) | 12,228 (abs. 12,292) | 180×466 | `flex: 1 0 0`, ancho fijo 180; empuja «Cerrar Sesión» al fondo |
| 8 | `nav:Cerrar Sesión` (4072:253) | 12,698 (abs. 12,762) | 184×38 | Inactivo; al fondo (756 − 20 relleno − 38 = 698); separado del grupo por el espaciador |
| 8a | `icono · cerrar sesión` (4357:1806) | 16,9 (abs. 28,771) | 20×20 | Trazo `#17161C` |
| 8b | Texto (4072:254) | 46,11 (abs. 58,773) | 92×16 | Archivo 600 14.5 `#17161C` |

Ritmo vertical: botón colapsar 20→56, gap 4, ítems cada 42 (38 + gap 4): y = 60, 102, 144, 186. Ancho útil = 208 − 12 − 12 = 184 (ítems `w-full`).

No hay bloque de usuario ni marca dentro de la barra lateral (ambos están en la barra superior). No hay divisor sobre «Cerrar Sesión».

### Textos literales

- «Mis Alarmas»
- «Reportes»
- «Descargar QR»
- «Ajustes de Perfil»
- «Cerrar Sesión» (C y S mayúsculas)

---

## 3. Barra lateral EXPANDIDA · W01 Mis Alarmas — nodeId `4072:36`

Geometría idéntica a la sección 2 (mismas posiciones, tamaños, relleno, gap, colores). Diferencias:

### Bloques

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | `botón · colapsar menú` (4357:110) | 12,20 (abs. 12,84) | 36×36 | `<a>` con cursor puntero; sin fondo; radio 999; relleno 8 |
| 2 | `nav:Mis Alarmas` (4072:37) — **ACTIVO** | 12,60 (abs. 12,124) | 184×38 | Fondo `#17161C`; icono `icono · alarma` (4357:87) trazo `#FFFFFF`; texto (4072:38) `#FFFFFF` Archivo 600 14.5 |
| 3 | `nav:Ajustes de Perfil` (4072:43) | 12,186 (abs. 12,250) | 184×38 | Inactivo: fondo `rgba(255,255,255,0)`, icono y texto `#17161C` |
| 4 | Resto (Reportes 4072:39, Descargar QR 4072:41, spacer 4072:45, Cerrar Sesión 4072:46) | igual que §2 | igual | igual |

### Textos literales

Idénticos a §2.

---

## 4. Barra lateral COLAPSADA · W01 menú colapsado — nodeId `4357:1879` (marco `4357:1868`)

### Bloques

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | `side` (4357:1879) | 0,0 (abs. 0,64) | 64×756 | Fondo `#FFFFFF`; borde derecho 1 px `#DAD8D2`; flex columna, gap 4; relleno 20 × 12 → ancho útil 40 |
| 2 | `botón · expandir menú` (4357:1880) | 12,20 (abs. 12,84) | 36×36 | `<a>`, relleno 8, radio 999, sin fondo; **alineado a la izquierda (12–48), no centrado sobre las píldoras (12–52)**: centro x=30 vs 32 de las píldoras |
| 2a | `icono · colapsar` (4357:1881) | caja 8,8 (metadata informa 28,28 por la rotación) (abs. 20,92) | 20×20 | Mismo símbolo rotado 180° (`rotate-180`) → «»» apuntando a la derecha; trazo `#17161C` |
| 3 | `nav:Mis Alarmas` (4357:1882) — **ACTIVO** | 12,60 (abs. 12,124) | 40×40 | Fondo `#17161C`, radio 999 (círculo); relleno 10; centrado; icono (4357:1883) en 10,10 (abs. 22,134), 20×20, trazo `#FFFFFF`; texto oculto (`hidden`) |
| 4 | `nav:Reportes` (4357:1885) | 12,104 (abs. 12,168) | 40×40 | Fondo `rgba(255,255,255,0)`; icono (4357:1886) 10,10 (abs. 22,178) trazo `#17161C`; texto oculto |
| 5 | `nav:Descargar QR` (4357:1888) | 12,148 (abs. 12,212) | 40×40 | Inactivo; icono (4357:1889) abs. 22,222 |
| 6 | `nav:Ajustes de Perfil` (4357:1891) | 12,192 (abs. 12,256) | 40×40 | Inactivo; icono (4357:1892) abs. 22,266 |
| 7 | `spacer` (4357:1894) | 12,236 (abs. 12,300) | 180×456 | `flex: 1 0 0`; conserva ancho 180 (desborda la barra de 64; invisible) |
| 8 | `nav:Cerrar Sesión` (4357:1895) | 12,696 (abs. 12,760) | 40×40 | Inactivo; icono (4357:1896) abs. 22,770; al fondo (756 − 20 − 40) |

Ritmo: píldoras cada 44 (40 + gap 4): y = 60, 104, 148, 192. Los textos siguen existiendo como capas ocultas (útil para `aria-label`/tooltip). No hay marca en la barra colapsada: la marca queda en la barra superior, sin cambios.

### Textos literales

- Sin textos visibles. Capas ocultas: «Mis Alarmas», «Reportes», «Descargar QR», «Ajustes de Perfil», «Cerrar Sesión».

---

## 5. Iconos SVG — página `00 · Recursos gráficos` (`4005:2`)

Todos: caja 24×24, `fill="none"`, trazo `#17161C` (recolorear con `currentColor`), stroke-width 2 salvo «atrás» (2.5). En la barra lateral se dibujan a 20×20 escalados (trazo efectivo 1.66667, como exporta Figma). Descripción del componente: «Ícono de línea 24 px, trazo 2 px, terminales redondeadas (Style Tile §4.3). Recolorear el trazo según contexto.»

### `icono · alarma` — `4006:37` (path exacto)

```svg
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <circle cx="12" cy="13" r="8" stroke="#17161C" stroke-width="2"/>
  <path d="M12 9V13L15 15" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M3 5L6 2" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M21 5L18 2" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

### `icono · reportes` — `4356:2` (path exacto) · «gráfica de barras»

```svg
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M6 20V12" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M12 20V6" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M18 20V10" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M3 21H21" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

### `icono · escanear` — `4006:51` (path exacto) · cuadro central relleno

```svg
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M3 8V5C3 3.66667 3.66667 3 5 3H8" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M16 3H19C20.3333 3 21 3.66667 21 5V8" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M21 16V19C21 20.3333 20.3333 21 19 21H16" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M8 21H5C3.66667 21 3 20.3333 3 19V16" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <rect x="8" y="8" width="8" height="8" rx="1.5" fill="#17161C"/>
</svg>
```

### `icono · ajustes` — `4006:48` (path exacto) · engranaje de 8 puntas, sin `stroke-linecap` (contorno cerrado)

```svg
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M11.6582 3.68945C11.8504 3.50952 12.1496 3.50952 12.3418 3.68945L13.8896 5.13867C14.332 5.55272 14.91 5.79149 15.5156 5.81152L17.6348 5.88184C17.8979 5.89054 18.1095 6.10207 18.1182 6.36523L18.1885 8.48438C18.2085 9.08996 18.4473 9.66797 18.8613 10.1104L20.3105 11.6582C20.4905 11.8504 20.4905 12.1496 20.3105 12.3418L18.8613 13.8896C18.4473 14.332 18.2085 14.91 18.1885 15.5156L18.1182 17.6348C18.1095 17.8979 17.8979 18.1095 17.6348 18.1182L15.5156 18.1885C14.91 18.2085 14.332 18.4473 13.8896 18.8613L12.3418 20.3105C12.1496 20.4905 11.8504 20.4905 11.6582 20.3105L10.1104 18.8613C9.66797 18.4473 9.08996 18.2085 8.48438 18.1885L6.36523 18.1182C6.10207 18.1095 5.89054 17.8979 5.88184 17.6348L5.81152 15.5156C5.79149 14.91 5.55272 14.332 5.13867 13.8896L3.68945 12.3418C3.50952 12.1496 3.50952 11.8504 3.68945 11.6582L5.13867 10.1104C5.55272 9.66797 5.79149 9.08996 5.81152 8.48438L5.88184 6.36523C5.89054 6.10206 6.10207 5.89054 6.36523 5.88184L8.48438 5.81152C9.08996 5.79149 9.66797 5.55272 10.1104 5.13867L11.6582 3.68945Z" stroke="#17161C" stroke-width="2" stroke-linejoin="round"/>
  <circle cx="12" cy="12" r="3.5" stroke="#17161C" stroke-width="2"/>
</svg>
```

### `icono · cerrar sesión` — `4356:7` (path exacto) · «salir»

```svg
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M9 3H5C3.9 3 3 3.9 3 5V19C3 20.1 3.9 21 5 21H9" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M16 8L20 12L16 16" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M10 12H20" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

### `icono · colapsar` — `4356:11` (path exacto) · doble chevrón a la izquierda; «rotar 180° para expandir»

```svg
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M11 7L6 12L11 17" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
  <path d="M18 7L13 12L18 17" stroke="#17161C" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

### `icono · atrás` — `4006:64` (path exacto) · chevrón «‹», **trazo 2.5**

```svg
<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
  <path d="M15 5L8 12L15 19" stroke="#17161C" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
```

### Icono de cierre «✕»

No existe en `00 · Recursos gráficos` (la sección 4006:72 solo tiene: alarma, calendario, ajustes, escanear, galería, más, atrás, compartir, reportes, cerrar sesión, colapsar, google, outlook, teléfono). No se obtuvo path; si hace falta, buscarlo en el marco del modal W07 o dibujarlo tras acordarlo en el repo de UX.

---

## 6. Observaciones

1. **Coincide con los tokens**: barra lateral 208 / colapsada 64; ítem 38 de alto (184 ancho, relleno 9/16, gap 10); icono 20; control colapsar 36×36 (relleno 8, píldora); texto nav Archivo 600 14.5; marca web Bricolage 700 17; barra superior web 64; colores Tinta `#17161C`, Blanco `#FFFFFF`, Gris niebla `#F4F3EF` (solo en el avatar), Gris borde `#DAD8D2` (bordes derecho de la barra lateral e inferior de la barra superior), Gris texto `#66636D` (nombre del usuario).
2. **Píldoras colapsadas 40×40, no 38**: en la colapsada el ítem es 40×40 con relleno 10 (ritmo 44), mientras en la expandida es 38 de alto (ritmo 42). «Cerrar Sesión» queda en y=696 colapsada vs 698 expandida. Si los tokens solo tienen 38, falta un token de 40 (o `chip-control`/píldora 40).
3. **Botón expandir no centrado** en la barra colapsada: 36×36 en x=12 (centro 30) frente a píldoras 40 en x=12 (centro 32). Desfase de 2 px; transcribir tal cual o decidir en UX.
4. **Marca fuera de la barra lateral**: el logo (32×32 `#FFC400` radio 8 con cuadro de borde 2.286 y punto 5.714 en Tinta) y «Alarmas QR» están en la barra superior, a 28 del borde; no cambian al colapsar. El amarillo `#FFC400` del logo convive con el primario amarillo de la página (W01 «Exportar reporte»): revisar la regla «un solo elemento amarillo por pantalla» (es la marca, probablemente excepción).
5. **Avatar sin iniciales**: círculo 32 Gris niebla con borde 1.5 Tinta, vacío. El nombre «Andrés Rojas» va a la izquierda en Archivo 400 14 Gris texto (14 px, no 14.5).
6. **Sin bloque de usuario en la barra lateral**, sin divisor antes de «Cerrar Sesión»: solo el espaciador flexible (ancho fijo 180, incluso en la colapsada).
7. **Grosor de trazo**: los iconos 24 tienen trazo 2 (atrás 2.5); al escalarlos a 20 el trazo efectivo es 1.667. Usar `viewBox 0 0 24 24` a 20 px reproduce exactamente el export de Figma (no fijar `vector-effect: non-scaling-stroke`).
8. **Estado activo**: fondo Tinta píldora (radio 999), icono y texto `#FFFFFF`; el ítem activo es `div` (no enlace) en Figma. Estado inactivo: fondo `rgba(255,255,255,0)`. No hay estado hover dibujado en Figma.
9. **Título de página**: Bricolage Grotesque Bold 24 `#17161C` en `main` (relleno 32 izq., 28 arriba); no forma parte de la barra superior.
10. No se verificó el fondo de `main`/`bodyRow` (no se pidió su design context); la barra lateral es blanca con borde, no Gris niebla.
