# Medidas de Figma · pantallas móviles de la Persona A

Anexo de `2026-09-20-maquetacion-persona-a-design.md`. Tomadas el 2026-09-20 del archivo `4nHD4ygcnP33UH0gAhaii5` (página `02 · Móvil`, marcos 390×844) con `get_metadata` + `get_design_context`. Las medidas se transcriben tal cual; cuando contradicen un token o una regla del DS, el plan decide y lo anota (ver `plans/2026-09-20-plan2-movil-persona-a.md` §«Decisiones»).

---

# Hoja de medidas · Grupo 1 (M01, M00a, M00b, M02v)

Fuente: Figma `4nHD4ygcnP33UH0gAhaii5`, página «02 · Móvil», marcos 390×844. Medidas en px de Figma (1 px = 1 dp). Posiciones (x, y) relativas al padre indicado; cuando se indica «abs.» son relativas al marco de 390×844. Colores en hex; las transparencias se indican como rgba tal cual las devuelve Figma.

Común a los cuatro marcos:
- El marco raíz tiene relleno propio, borde 3 px `#17161C` y radio 26 (bisel del mockup; no forma parte del contenido de la pantalla).
- Fuentes: títulos en **Bricolage Grotesque Bold** (opsz 14, wdth 100); cuerpo y controles en **Archivo** (wdth 100). Interlineado «normal» salvo donde se indica 1.4.
- Botones de 52 de alto, radio píldora (999). Enlaces subrayados de 32 de alto con relleno horizontal 20.
- Márgenes laterales del contenido: **20** a cada lado → ancho útil **350** (en M02v la barra superior usa 16 y la navegación 36).
- Paleta observada: Tinta `#17161C`, Amarillo `#FFC400`, Blanco `#FFFFFF`, Gris texto `#66636D`, Gris placeholder `#77747E`, Gris borde `#DAD8D2`, Gris fondo `#F4F3EF`, Azul texto `#1A5BC4`.

---

## 1. M01 · Bienvenida — nodeId `3:71`

**Marco** `M01 · Bienvenida` 390×844, relleno `#FFC400`.
**Contenedor** `contenido` (3:72) 390×844, relleno `#FFC400`, columna vertical, **gap 12**, relleno interno: arriba 24, abajo 16, laterales 20.

### Bloques de arriba abajo (x, y abs.; w×h)

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 0 | `textura · módulos QR` (4010:2030), instancia de «textura · módulos QR / tinta» (4005:3) | (0, 0) | 390×120 | Hijo absoluto detrás del contenido, recorte a 120 de alto. Módulos de 8×8, radio 2, relleno `rgba(23,22,28,0.08)` en las filas superiores y baja gradualmente 0.07 → 0.06 → 0.05 → 0.04 → 0.03 → 0.02 hacia abajo. Paso de retícula 14 px (filas en y = 4, 18, 32, 46, 60, 74, 88, 102, 116…; columnas en x = 4, 18, 32, 46…). Descripción del componente: «opacidad máxima 8 % (Style Tile §4.6)». |
| 1 | Indicador de página `Frame` (3:73) | (20, 24) | 350×6 | Fila centrada, **gap 6**. Hijos: `Rectangle` (3:74) 22×6 `#17161C` radio 3 (activo); `Rectangle` (3:75) 6×6 `rgba(23,22,28,0.25)` radio 3; `Rectangle` (3:76) 6×6 `rgba(23,22,28,0.25)` radio 3. |
| 2 | `visor` (3:77) | (20, 42) | 350×170 | Relleno `#17161C`, **radio 20**, hijos centrados, recorte. Contiene: `textura · módulos QR` (4010:2176, variante «blanco» 4005:149) absoluta en (0,0) 390×260 (módulos 8×8 radio 2 `rgba(255,255,255,0.08)` → 0.02, misma retícula; queda recortada por el visor); `QR 1` (3:78) en (133, 43) 84×84 con `código QR · evento` (4012:2487 → componente 4010:2) 84×84: fondo `#FFFFFF`, radio 4, módulos `#17161C` en retícula de 4 px (21×21 módulos con patrones de posición); `destello` (4013:2900) en (22, 18) 40×40; `destello` (4013:2902) en (298, 110) 24×24 (componente «ilustración · destello» 4006:15: estrella de 4 puntas, acento Amarillo Energía). |
| 3 | Texto «Escanea y listo» (3:112) | (20, 224) | 350×38 | Bricolage Grotesque Bold 32, interlineado normal, centrado, `#17161C`. |
| 4 | Texto (3:113) | (20, 274) | 350×32 | «Apunta la cámara al QR del evento: la alarma queda programada sin escribir fecha, hora ni nombre.» Archivo Regular 15, normal, centrado, `#17161C`. |
| 5 | Texto (3:114) | (20, 318) | 290×14 | «CONECTA TU CALENDARIO (OPCIONAL)» Archivo Bold 13, tracking 1.04, normal, alineado a la izquierda, `#17161C`. |
| 6 | Fila `Frame` (3:115) Google Calendar | (20, 344) | 350×36 | Ver «Controles». |
| 7 | Fila `Frame` (3:119) Outlook · Teams | (20, 392) | 350×36 | Ver «Controles». |
| 8 | Fila `Frame` (3:122) Calendario del teléfono | (20, 440) | 350×36 | Ver «Controles». |
| 9 | Espaciador `Frame` (3:125) | (20, 488) | 10×28 | Vacío, sin relleno. |
| 10 | `botón · Comenzar` (3:126) | (20, 528) | 350×52 | Ver «Controles». |
| 11 | `Frame` (3:128) | (20, 592) | 350×32 | Fila centrada; contiene `botón · Conectar luego en Ajustes` (3:129) en (61.5, 0) 227×32. |

Separación vertical real entre bloques: 12 en todos los casos (24→30 indicador, 42 visor, 212→224 título, 262→274 subtítulo, 306→318 rótulo, 332→344 fila 1, 380→392, 428→440, 476→488 espaciador, 516→528 botón, 580→592 enlace).

### Controles

**Fila de opción de calendario** (×3: 3:115, 3:119, 3:122) — 350×36, relleno `#FFFFFF`, **radio 12**, sin borde, relleno interno horizontal 12 / vertical 8, fila con **gap 10**, hijos centrados verticalmente.
- Icono 20×20 en (12, 8): `icono · google` (4373:1468), `icono · outlook` (4373:1471), `icono · teléfono` (4373:1474). Descripción del componente: «Icono de línea 24 px, trazo 2 Tinta, remates redondos» (instanciado a 20×20).
- Texto (flex 1) en (42, 10.5) 266×15: «Google Calendar» / «Outlook · Teams» / «Calendario del teléfono», Archivo Bold 14, normal, `#17161C`.
- Casilla `Frame` (3:117 / 3:121 / 3:124) en (318, 8) 20×20: relleno `#FFFFFF`, **borde 2 `#17161C`**, radio 4. Dentro de 3:117 hay un texto «✓» (3:118) 10×13 en (5, 3.5) **oculto** (estado marcado; fuente/color no visibles en la respuesta).

**`botón · Comenzar`** (3:126) — 350×52, relleno `#17161C`, radio 999, relleno interno horizontal 20, contenido centrado. Texto «Comenzar» (3:127) en (138, 18) 74×16, Archivo Bold 15, normal, `#FFFFFF`. Sin borde, sin icono.

**`botón · Conectar luego en Ajustes`** (3:129) — 227×32, sin relleno ni borde, radio 999, relleno interno horizontal 20. Texto «Conectar luego en Ajustes» (3:130) en (20, 8) 187×16, Archivo Bold 15, normal, **subrayado**, `#17161C`.

### Iconos / ilustraciones (sin descargar)
- `textura · módulos QR` ×2 (tinta 390×120; blanco 390×260 dentro del visor).
- `código QR · evento` 84×84.
- `destello` 40×40 y 24×24 (SVG exportado).
- `icono · google`, `icono · outlook`, `icono · teléfono` 20×20 (SVG exportados).

---

## 2. M00a · Crear cuenta (Registro) — nodeId `3:2`

**Marco** `M00a · Registro` 390×844, relleno `#FFFFFF`.
**Contenedor** `contenido` (3:3) 390×844, relleno `#FFFFFF`, columna, **gap 12**, relleno interno: arriba 28, abajo 32, laterales 20. Los dos espaciadores son flexibles (flex 1) y reparten el sobrante: 97 cada uno.

### Bloques de arriba abajo (x, y abs.; w×h)

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | `espaciador · superior` (4307:1562) | (20, 28) | 350×97 | Flexible, vacío. |
| 2 | `Frame` (3:4) | (20, 137) | 350×56 | Fila centrada, relleno `#FFFFFF`. Hijo `Frame` (3:5) en (147, 0) **56×56**: logotipo exportado como SVG (`08904.svg`); en la captura se ve un cuadrado amarillo redondeado con un patrón de posición QR en Tinta. |
| 3 | Texto «Crea tu cuenta» (3:9) | (20, 205) | 350×34 | Bricolage Grotesque Bold 28, normal, centrado, `#17161C`. |
| 4 | Texto (3:10) | (20, 251) | 350×14 | «Respalda tus alarmas en la nube y úsalas también en la web.» Archivo Regular 13, normal, centrado, `#66636D`. |
| 5 | `campo · Correo` (3:11) | (20, 277) | 350×45 | Ver «Controles». |
| 6 | `campo · Contraseña` (3:14) | (20, 334) | 350×45 | Ver «Controles». |
| 7 | `Frame` (3:17) casilla de consentimiento | (20, 391) | 350×34 | Ver «Controles». |
| 8 | `botón · Crear cuenta` (3:21) | (20, 437) | 350×52 | Ver «Controles». |
| 9 | `Frame` (3:23) separador «o continúa con» | (20, 501) | 350×13 | Fila, **gap 8**, centrada verticalmente: `Rectangle` (3:24) 128×1 `#DAD8D2` en (0, 6); texto «o continúa con» (3:25) 78×13 Archivo Regular 12, normal, `#66636D`; `Rectangle` (3:26) 128×1 `#DAD8D2` en (222, 6). Las líneas son flexibles (flex 1). |
| 10 | `fila · Google / Outlook` (4032:1577) | (20, 526) | 350×52 | Fila **gap 10**; dos botones de 170×52 (flex 1). Ver «Controles». |
| 11 | `Frame` (3:31) | (20, 590) | 350×32 | Fila centrada; `botón · Continuar como invitado` (3:32) en (67.5, 0) 215×32. |
| 12 | Texto (3:34) | (20, 634) | 350×13 | «Como invitado, las alarmas quedan solo en este teléfono.» Archivo Regular 12, normal, centrado, `#66636D`. |
| 13 | `espaciador · inferior` (3:35) | (20, 659) | 10×97 | Flexible, vacío. |
| 14 | `botón · ¿Ya tienes cuenta? Inicia sesión` (4306:1561) | (20, 768) | 350×44 | Ver «Controles». Termina en y = 812; quedan 32 de relleno inferior. |

Separación vertical entre bloques: 12 en todos los casos.

### Controles

**Campo de texto** (`campo · Correo` 3:11, `campo · Contraseña` 3:14) — 350×45, relleno `#FFFFFF`, **borde 1.5 `#DAD8D2`**, **radio 12**, relleno interno horizontal 12 / vertical 7, columna con **gap 2**, sin icono.
- Rótulo (3:12 / 3:15) en (12, 7): «CORREO» 56×13 / «CONTRASEÑA» 91×13, Archivo SemiBold 12, tracking 0.6, normal, `#66636D`.
- Valor/placeholder (3:13 / 3:16) en (12, 22): «tucorreo@ejemplo.com» 158×16 / «Mínimo 8 caracteres» 135×16, Archivo Regular 15, normal, `#77747E`.

**Casilla de consentimiento** (`Frame` 3:17) — 350×34, fila con **gap 10**, hijos alineados arriba, relleno `#FFFFFF`.
- Casilla `Frame` (3:18) en (0, 0) 20×20: relleno `#FFFFFF`, **borde 2 `#17161C`**, radio 4, contenido centrado. Texto «✓» (3:19) 10×13 en (5, 3.5) **oculto** (estado marcado; fuente/color no visibles en la respuesta).
- Texto (3:20) en (30, 0) 320×34: «Acepto el tratamiento de mis datos según la política de privacidad (Ley 1581 de 2012).» Archivo Regular 12, **interlineado 1.4**, `#66636D`.

**`botón · Crear cuenta`** (3:21) — 350×52, relleno `#FFC400`, radio 999, relleno interno horizontal 20, contenido centrado, sin borde. Texto «Crear cuenta» (3:22) en (129, 18) 92×16, Archivo Bold 15, normal, `#17161C`.

**`botón · Google`** (3:27) — (0, 0) 170×52, sin relleno, **borde 1.5 `#17161C`**, radio 999, relleno interno horizontal 20, centrado. Texto «Google» (3:28) en (58.5, 18) 53×16, Archivo Bold 15, `#17161C`. Sin icono.
**`botón · Outlook`** (3:29) — (180, 0) 170×52, mismas propiedades. Texto «Outlook» (3:30) en (56.5, 18) 57×16.

**`botón · Continuar como invitado`** (3:32) — 215×32, sin relleno ni borde, radio 999, relleno interno horizontal 20. Texto «Continuar como invitado» (3:33) en (20, 8) 175×16, Archivo Bold 15, normal, **subrayado**, `#1A5BC4`.

**`botón · ¿Ya tienes cuenta? Inicia sesión`** (4306:1561) — 350×44, sin relleno ni borde, contenido centrado. Texto (3:36) 350×44 centrado, dos tramos: «¿Ya tienes cuenta? » Archivo SemiBold 13 `#17161C` + «Inicia sesión» Archivo Bold 13 `#1A5BC4` (sin subrayado).

### Iconos / ilustraciones
- `Frame` (3:5) 56×56, logotipo exportado como SVG.

---

## 3. M00b · Iniciar sesión — nodeId `3:37`

**Marco** `M00b · Inicio de sesión` 390×844, relleno `#FFFFFF`.
**Contenedor** `contenido` (3:38) 390×844, relleno `#FFFFFF`, columna, **gap 12**, relleno interno: arriba 28, abajo 32, laterales 20. Espaciadores flexibles de 110.5 cada uno.

### Bloques de arriba abajo (x, y abs.; w×h)

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | `espaciador · superior` (4307:1561) | (20, 28) | 350×110.5 | Flexible, vacío. |
| 2 | `Frame` (3:39) | (20, 150.5) | 350×56 | Fila centrada, relleno `#FFFFFF`. Hijo `Frame` (3:40) en (147, 0) **56×56**: mismo logotipo SVG que M00a (`08904.svg`). |
| 3 | Texto «Hola de nuevo» (3:44) | (20, 218.5) | 350×34 | Bricolage Grotesque Bold 28, normal, centrado, `#17161C`. |
| 4 | Texto (3:45) | (20, 264.5) | 350×14 | «Entra para sincronizar tus alarmas con la nube y la web.» Archivo Regular 13, normal, centrado, `#66636D`. |
| 5 | `campo · Correo` (3:46) — **estado enfocado** | (20, 290.5) | 350×45 | Ver «Controles». |
| 6 | `campo · Contraseña` (3:49) | (20, 347.5) | 350×45 | Ver «Controles». |
| 7 | `Frame` (3:52) | (20, 404.5) | 350×32 | Fila alineada a la **derecha** (justify end); contiene `botón · ¿Olvidaste tu contraseña?` (3:53) en (122, 0) 228×32. |
| 8 | `botón · Entrar` (3:55) | (20, 448.5) | 350×52 | Ver «Controles». |
| 9 | `Frame` (3:57) separador «o continúa con» | (20, 512.5) | 350×13 | Igual que M00a: `Rectangle` (3:58) 128×1 `#DAD8D2`; texto «o continúa con» (3:59) 78×13 Archivo Regular 12 `#66636D`; `Rectangle` (3:60) 128×1 `#DAD8D2`; gap 8. |
| 10 | `Frame` (3:61) | (20, 537.5) | 350×52 | Fila gap 10: `botón · Google` (3:62) 170×52 y `botón · Outlook` (3:64) en (180, 0) 170×52. |
| 11 | `Frame` (3:66) | (20, 601.5) | 350×32 | Fila centrada; `botón · Continuar como invitado` (3:67) en (67.5, 0) 215×32. |
| 12 | `espaciador · inferior` (3:69) | (20, 645.5) | 10×110.5 | Flexible, vacío. |
| 13 | `botón · ¿Primera vez aquí? Crea tu cuenta` (4306:1563) | (20, 768) | 350×44 | Ver «Controles». |

Separación vertical entre bloques: 12 en todos los casos. No hay texto de aclaración «Como invitado…» en este marco.

### Controles

**`campo · Correo`** (3:46) — 350×45, relleno `#FFFFFF`, **borde 2 `#17161C`** (enfocado), radio 12, relleno interno horizontal 12 / vertical 7, columna gap 2.
- Rótulo «CORREO» (3:47) en (12, 7) 56×13, Archivo SemiBold 12, tracking 0.6, `#66636D`.
- Valor «andres.rojas@corr|» (3:48) en (12, 22) 128×16, Archivo Regular 15, `#17161C` (el «|» es el cursor dibujado como texto).

**`campo · Contraseña`** (3:49) — 350×45, relleno `#FFFFFF`, **borde 1.5 `#DAD8D2`**, radio 12, mismo relleno interno.
- Rótulo «CONTRASEÑA» (3:50) en (12, 7) 91×13, Archivo SemiBold 12, tracking 0.6, `#66636D`.
- Valor «••••••••» (3:51) en (12, 22) 47×16, Archivo Regular 15, `#77747E`.

**`botón · ¿Olvidaste tu contraseña?`** (3:53) — 228×32, sin relleno ni borde, radio 999, relleno interno horizontal 20. Texto «¿Olvidaste tu contraseña?» (3:54) en (20, 8) 188×16, Archivo Bold 15, normal, **subrayado**, `#1A5BC4`.

**`botón · Entrar`** (3:55) — 350×52, relleno `#FFC400`, radio 999, relleno interno horizontal 20, sin borde. Texto «Entrar» (3:56) en (152.5, 18) 45×16, Archivo Bold 15, `#17161C`.

**`botón · Google`** (3:62) / **`botón · Outlook`** (3:64) — 170×52 cada uno, sin relleno, borde 1.5 `#17161C`, radio 999, relleno interno horizontal 20. Textos «Google» (3:63) (58.5, 18) 53×16 y «Outlook» (3:65) (56.5, 18) 57×16, Archivo Bold 15, `#17161C`.

**`botón · Continuar como invitado`** (3:67) — 215×32, sin relleno ni borde, radio 999, relleno horizontal 20. Texto (3:68) en (20, 8) 175×16, Archivo Bold 15, subrayado, `#1A5BC4`.

**`botón · ¿Primera vez aquí? Crea tu cuenta`** (4306:1563) — 350×44, sin relleno ni borde, centrado. Texto (3:70) 350×44: «¿Primera vez aquí? » Archivo SemiBold 13 `#17161C` + «Crea tu cuenta» Archivo Bold 13 `#1A5BC4` (sin subrayado).

### Iconos / ilustraciones
- `Frame` (3:40) 56×56, logotipo exportado como SVG.

---

## 4. M02v · Inicio · sin alarmas — nodeId `4020:3553`

**Marco** `M02v · Inicio · sin alarmas` 390×844, relleno `#FFFFFF`, columna con tres hijos: barra superior, contenido (flexible) y navegación.

### Bloques de arriba abajo (x, y abs.; w×h)

| # | Capa (id) | Pos. | Tamaño | Detalle |
|---|---|---|---|---|
| 1 | `barra superior` (4020:3554) | (0, 0) | 390×56 | Relleno `#FFFFFF`, **borde inferior 1.5 `#DAD8D2`**, relleno interno horizontal **16**, hijos centrados verticalmente. Texto «Mis alarmas» (4020:3555) en (16, 15) 130×26, Bricolage Grotesque Bold 22, normal, `#17161C`. |
| 2 | `contenido` (4020:3556) | (0, 56) | 390×724 | Relleno `#FFFFFF`, columna, **gap 12**, hijos centrados horizontalmente, relleno interno horizontal 20 / vertical 16. |
| 2.1 | `espacio` (4020:3755) | (145, 16) rel. | 100×64 | Vacío. |
| 2.2 | `ilustración` (4020:3756) | (143, 92) rel. → (143, 148) abs. | 104×104 | Contiene `ilustración · diana QR` (4020:3757, componente 4006:2 «patrón de posición del QR»): `Rectangle` 104×104 sin relleno, **borde 10.4 `#17161C`**, radio 26; `Rectangle` 45.067×45.067 relleno `#17161C`, radio 10, en (29.47, 29.47); `Rectangle` 22.533×22.533 relleno `#FFC400`, radio 6, en (79.73, −1.73) (esquina superior derecha). |
| 2.3 | `espacio` (4020:3763) | (145, 208) rel. | 100×8 | Vacío. |
| 2.4 | Texto «Aún no tienes alarmas» (4020:3764) | (20, 228) rel. → (20, 284) abs. | 350×29 | Bricolage Grotesque Bold 24, normal, centrado, `#17161C`. |
| 2.5 | Texto (4020:3765) | (20, 269) rel. → (20, 325) abs. | 350×63 | «Escanea el QR de un evento o comparte un pantallazo con Alarmas QR: la alarma queda programada sin escribir fecha ni hora.» Archivo Regular 15, **interlineado 1.4**, centrado, `#66636D`. (El nombre de capa está truncado a «Escanea el QR de un evento».) |
| 2.6 | `espacio` (4020:3766) | (145, 344) rel. | 100×8 | Vacío. |
| 2.7 | `botón · Escanear QR del evento` (4020:3767) | (20, 364) rel. → (20, 420) abs. | 350×52 | Ver «Controles». |
| 2.8 | `botón · Elegir pantallazo de la galería` (4021:3460) | (20, 428) rel. → (20, 484) abs. | 350×52 | Ver «Controles». |
| 2.9 | `botón · Crear el evento a mano` (4020:3770) | (20, 492) rel. → (20, 548) abs. | 350×52 | Ver «Controles». |
| 3 | `navegación` (4020:3594) | (0, 780) | 390×64 | Relleno `#FFFFFF`, **borde superior 1.5 `#DAD8D2`**, relleno interno horizontal **36**, hijos distribuidos con espacio entre ellos (space-between), centrados verticalmente. |

Separación vertical dentro de `contenido`: 12 entre todos los hijos (16+64=80→92; 196→208; 216→228; 257→269; 332→344; 352→364; 416→428; 480→492). El contenido termina en y rel. 544 y el resto (hasta 724) queda vacío.

### Controles

**`botón · Escanear QR del evento`** (4020:3767) — 350×52, relleno `#FFC400`, radio 999, sin borde, contenido centrado (relleno interno horizontal no visible en la respuesta). Texto «Escanear QR del evento» (4020:3768) en (89.5, 18) 171×16, Archivo Bold 15, normal, `#17161C`. Sin icono.

**`botón · Elegir pantallazo de la galería`** (4021:3460) — 350×52, sin relleno, **borde 1.5 `#17161C`**, radio 999, relleno interno horizontal 20. Texto (4021:3461) en (71.5, 18) 207×16, Archivo Bold 15, `#17161C`.

**`botón · Crear el evento a mano`** (4020:3770) — 350×52, sin relleno, borde 1.5 `#17161C`, radio 999, relleno interno horizontal 20. Texto (4020:3771) en (93.5, 18) 163×16, Archivo Bold 15, `#17161C`.

**Barra de navegación** (4020:3594) — tres ítems, cada uno columna con **gap 3** e hijos centrados:
- Ítem `Frame` (4020:3595) **Alarmas (activo)** en (36, 13.5) 44×37: `píldora` (4020:3596) en (2, 0) **40×22**, relleno `#F4F3EF`, radio 999; `icono · alarma` (4020:3597) 20×20 en (10, 1) dentro de la píldora; texto «Alarmas» (4020:3598) en (0, 25) 44×12, Archivo **Bold** 11, `#17161C`.
- Ítem `Frame` (4020:3599) **Calendario** en (169.5, 13.5) 55×37: `píldora` (4020:3600) en (7.5, 0) 40×22 sin relleno, radio 999; `icono · calendario` (4020:3601) 20×20 en (10, 1); texto «Calendario» (4020:3602) en (0, 25) 55×12, Archivo Medium 11, `#66636D`.
- Ítem `Frame` (4020:3603) **Ajustes** en (314, 13.5) 40×37: `píldora` (4020:3604) en (0, 0) 40×22 sin relleno, radio 999; `icono · ajustes` (4020:3605) 20×20 en (10, 1); texto «Ajustes» (4020:3606) en (1, 25) 38×12, Archivo Medium 11, `#66636D`.
- Descripción de los iconos: «Ícono de línea 24 px, trazo 2 px, terminales redondeadas (Style Tile §4.3). Recolorear el trazo según contexto» (instanciados a 20×20).

### Iconos / ilustraciones
- `ilustración · diana QR` 104×104 (vector compuesto por tres rectángulos, medidas arriba).
- `icono · alarma`, `icono · calendario`, `icono · ajustes` 20×20 (SVG exportados).

---

## Observaciones (no son medidas nuevas)
- En M02v la píldora activa de la navegación es `#F4F3EF` (Gris fondo) con icono y texto en Tinta; no se observa una píldora en Tinta.
- En M01 el rótulo «CONECTA TU CALENDARIO (OPCIONAL)» mide 290 de ancho (hug), no 350.
- Estados marcados de las casillas (texto «✓» oculto en 3:118 y 3:19): fuente y color no visibles en la respuesta.
- Relleno horizontal interno del botón amarillo de M02v (4020:3767): no visible en la respuesta (los demás botones de 52 declaran 20).


---

# Hoja de medidas · Grupo 2 (M02, M02h, M05, M03b)

Fuente: Figma `4nHD4ygcnP33UH0gAhaii5`, página «02 · Móvil», marcos de 390×844. Medidas tomadas de `get_metadata` (posiciones/tamaños) y `get_design_context` (relleno, colores, tipografía). Posiciones en px relativas al marco salvo que se indique «rel. a <capa>». Hex en mayúsculas. «Interlineado normal» = `leading normal` (auto de la fuente); la altura de caja de texto que reporta Figma se anota como referencia.

## 0. Constantes comunes a los cuatro marcos

| Elemento | Valor |
|---|---|
| Marco del dispositivo | 390×844, fondo `#FFFFFF`, borde 3 `#17161C`, radio 26 (decoración del mockup; no forma parte de la app) |
| Barra superior | 0,0 · 390×56 · fondo `#FFFFFF` · borde inferior 1.5 `#DAD8D2` · padding horizontal 16 · hijos centrados verticalmente |
| Título de barra | Bricolage Grotesque Bold 22, interlineado normal (caja 26), `#17161C`, `opsz 14, wdth 100` |
| Columna «contenido» | x 0, y 56, 390 de ancho · fondo `#FFFFFF` · padding 20 horizontal / 16 vertical · gap 12 entre hijos · dirección vertical |
| Margen lateral del contenido | 20 a cada lado (hijos de 350 de ancho: 20 → 370) |
| Barra de navegación | 0,780 · 390×64 · fondo `#FFFFFF` · borde superior 1.5 `#DAD8D2` · padding horizontal 36 · `space-between` |
| Ítem de navegación | columna centrada, gap 3: píldora 40×22 radio 999 (activa fondo `#F4F3EF`; inactiva sin fondo) con icono 20×20 en (10,1); rótulo 11 px (caja 12) |
| Rótulo activo («Alarmas») | Archivo Bold 11, `#17161C` |
| Rótulo inactivo («Calendario», «Ajustes») | Archivo Medium 11, `#66636D` |
| Iconos de navegación | `icono · alarma` (4006:37), `icono · calendario` (4006:42), `icono · ajustes` (4006:48); instancias de 20×20 (componente base 24 px, trazo 2 px, terminales redondeadas, se recolorea) |
| Posiciones de ítems nav | Alarmas: (36,13.5) 44×37 · Calendario: (169.5,13.5) 55×37 · Ajustes: (314,13.5) 40×37 |
| FAB «Escanear» | 153×56 · fondo `#FFC400` · radio 16 · padding izq 20 / der 24 · gap 10 · sombra 0 4 12 rgba(23,22,28,0.18) · icono `icono · escanear` (4006:51) 26×26 en (20,15) · texto «Escanear» Archivo Bold 16 (caja 17) `#17161C` en (56,19.5) |
| Posición FAB (M02/M02h) | (221,708) → 16 del borde derecho, 16 por encima de la barra de navegación (termina en y 764) |
| Sombra estándar | 0 4 12 rgba(23,22,28,0.18) (FAB, hoja, tarjeta «mensaje») |

### Anatomía de la tarjeta de alarma (idéntica en M02, M02h y M05)

Ejemplo: `tarjeta · Reunión con el tutor` (3:141) en (20,42) rel. a contenido → abs (20,98).

| Parte | Medida |
|---|---|
| Contenedor | 350×75 (con chip) / 350×53 (sin chip) · fondo `#FFFFFF` · borde 1.5 `#DAD8D2` · radio 14 · padding 14 horizontal / 10 vertical · fila horizontal, gap 10, hijos centrados verticalmente |
| Hora | caja 81×31 en (14,22) rel. a tarjeta (con chip) / (14,11) (sin chip) · un solo texto con tres tramos en la misma línea: «7:30» Spline Sans Mono Bold 26 · espacio de 6 px (tramo « » a 6 px) · «am» Spline Sans Mono Medium 12 · color `#17161C` · interlineado normal |
| Hora en alarma pausada («Gimnasio») | mismos tramos en `#66636D` (la capa declara `#77747E` pero los tramos sobrescriben a `#66636D`) |
| Columna de texto | x 105 (14 + 81 + 10), y 10 · 177×55 (con chip) / 177×33 (sin chip) · columna, gap 3, flex 1 |
| Título | «Reunión con el tutor» · Archivo Bold 15 · caja 16 · `#17161C` (pausada: `#66636D`) |
| Línea evento · lugar | «evento 8:00 am · Aula SD-703» · Archivo Regular 13 · caja 14 · `#66636D` · y 19 rel. a columna |
| Chip | y 36 rel. a columna · alto 19 · padding 12 horizontal / 3 vertical · radio 999 · texto Archivo SemiBold 12 (caja 13) |
| Chip «Creada por mí» | 101×19 · borde 1.5 `#17161C` · texto `#17161C` · fondo `#FFFFFF`/sin relleno |
| Chip «✓ Escaneada» | 99×19 · borde 1.5 `#0B7048` · texto `#0B7048` · sin relleno |
| Chip «Nueva» (solo M05) | 60×19 · fondo `#17161C` · sin borde · texto `#FFFFFF` |
| Switch | 44×26 en (292,24.5) (con chip) / (292,13.5) (sin chip) · 292 = 350 − 14 − 44 · exportado como SVG (`switch`); colores del asset no visibles en la respuesta. En la captura: activo = pista `#17161C` con perilla blanca a la derecha; pausado = pista blanca con borde gris y perilla gris a la izquierda |
| Separación entre tarjetas | 12 (gap de contenido): 42→117, 129→182, 220→295, 307→382 |

### Agrupador de día

| Parte | Medida |
|---|---|
| Texto | «HOY · JUEVES 27» · Archivo Bold 13 · tracking 1.04 (8 %) · caja 124×14 · `#66636D` · interlineado normal |
| Posición | (20,16) rel. a contenido (abs y 72) |
| Distancia a la primera tarjeta | 12 (16+14 = 30 → 42) |
| Distancia desde la tarjeta anterior | 12 (182 → 194 «MAÑANA · VIERNES 28», 165×14) |
| Otros rótulos | «MAÑANA · VIERNES 28» (165×14) · «DOMINGO 30» (94×14, solo M05) |

---

## 1. M02 · Inicio · lista de alarmas (3:131)

### Bloques de arriba abajo

| # | Capa | Pos (x,y) | Tamaño | Padding | Gap | Fondo | Radio |
|---|---|---|---|---|---|---|---|
| 1 | `barra superior` (3:132) | 0,0 | 390×56 | 16 h | — | `#FFFFFF`, borde inf. 1.5 `#DAD8D2` | 0 |
| 2 | `contenido` (3:135) | 0,56 | 390×724 | 20 h / 16 v | 12 | `#FFFFFF` | 0 |
| 2.1 | `HOY · JUEVES 27` (3:140) | 20,16 rel. | 124×14 | — | — | — | — |
| 2.2 | `tarjeta · Reunión con el tutor` (3:141) | 20,42 rel. | 350×75 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 2.3 | `tarjeta · Gimnasio` (3:150) | 20,129 rel. | 350×53 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 2.4 | `MAÑANA · VIERNES 28` (3:157) | 20,194 rel. | 165×14 | — | — | — | — |
| 2.5 | `tarjeta · Vuelo BOG–MDE` (3:158) | 20,220 rel. | 350×75 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 2.6 | `tarjeta · Reunión semillero` (3:167) | 20,307 rel. | 350×75 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 3 | `FAB · Escanear` (3:186) | 221,708 | 153×56 | 20 izq / 24 der | 10 | `#FFC400` | 16 |
| 4 | `navegación` (3:176) | 0,780 | 390×64 | 36 h | space-between | `#FFFFFF`, borde sup. 1.5 `#DAD8D2` | 0 |

### Textos literales

| Texto | Fuente | Peso | Tamaño | Interlineado | Color |
|---|---|---|---|---|---|
| Mis alarmas | Bricolage Grotesque | Bold | 22 | normal (caja 26) | `#17161C` |
| HOY · JUEVES 27 | Archivo | Bold | 13 (tracking 1.04) | normal (14) | `#66636D` |
| 7:30 / am | Spline Sans Mono | Bold / Medium | 26 / 12 | normal (31) | `#17161C` |
| Reunión con el tutor | Archivo | Bold | 15 | normal (16) | `#17161C` |
| evento 8:00 am · Aula SD-703 | Archivo | Regular | 13 | normal (14) | `#66636D` |
| Creada por mí | Archivo | SemiBold | 12 | normal (13) | `#17161C` |
| 9:00 / am | Spline Sans Mono | Bold / Medium | 26 / 12 | normal (31) | `#66636D` |
| Gimnasio | Archivo | Bold | 15 | normal (16) | `#66636D` |
| pausada · evento 9:30 am | Archivo | Regular | 13 | normal (14) | `#66636D` |
| MAÑANA · VIERNES 28 | Archivo | Bold | 13 (tracking 1.04) | normal (14) | `#66636D` |
| 6:45 / am | Spline Sans Mono | Bold / Medium | 26 / 12 | normal (31) | `#17161C` |
| Vuelo BOG–MDE | Archivo | Bold | 15 | normal (16) | `#17161C` |
| vuelo 8:15 am · Aeropuerto | Archivo | Regular | 13 | normal (14) | `#66636D` |
| 6:00 / pm | Spline Sans Mono | Bold / Medium | 26 / 12 | normal (31) | `#17161C` |
| Reunión semillero | Archivo | Bold | 15 | normal (16) | `#17161C` |
| evento 6:10 pm · Virtual | Archivo | Regular | 13 | normal (14) | `#66636D` |
| ✓ Escaneada | Archivo | SemiBold | 12 | normal (13) | `#0B7048` |
| Escanear | Archivo | Bold | 16 | normal (17) | `#17161C` |
| Alarmas | Archivo | Bold | 11 | normal (12) | `#17161C` |
| Calendario | Archivo | Medium | 11 | normal (12) | `#66636D` |
| Ajustes | Archivo | Medium | 11 | normal (12) | `#66636D` |

### Controles

- **Tarjeta de alarma**: ver §0. Cuatro instancias; «Gimnasio» sin chip (53 de alto, switch apagado, todo en `#66636D`).
- **Chip**: ver §0 (`Creada por mí` ×2, `✓ Escaneada` ×1).
- **Switch**: 44×26, asset SVG; ver §0.
- **FAB · Escanear**: ver §0. Único elemento amarillo de la pantalla.
- **Barra de navegación**: ver §0; «Alarmas» activo con píldora `#F4F3EF`.

### Iconos vectoriales

| Capa | Tamaño | Dónde |
|---|---|---|
| `icono · alarma` (4007:10) | 20×20 | píldora Alarmas, (10,1) rel. a píldora |
| `icono · calendario` (4007:15) | 20×20 | píldora Calendario |
| `icono · ajustes` (4007:21) | 20×20 | píldora Ajustes |
| `icono · escanear` (4019:3391) | 26×26 | FAB, (20,15) rel. a FAB |
| `switch` (3:148, 3:155, 3:165, 3:174) | 44×26 | tarjetas (SVG completo) |

---

## 2. M02h · Hoja «Agregar evento» (4019:3139)

Misma base que M02 (barra, lista con 4 tarjetas, FAB en (221,708), navegación) debajo del velo; medidas de la base idénticas a §1 (ids 4031:*). Sólo se documentan las capas propias de la hoja.

### Bloques de arriba abajo (capas de la hoja)

| # | Capa | Pos (x,y) | Tamaño | Padding | Gap | Fondo | Radio |
|---|---|---|---|---|---|---|---|
| 1 | `atenuado · cerrar` (4019:3351) | 0,0 | 390×844 | — | — | `#17161C` al 55 % (rgba(23,22,28,0.55)) | 0 (cubre todo el marco; tocarlo cierra) |
| 2 | `hoja · Agregar evento` (4019:3353) | 0,408 | 390×436 | 12 sup / 28 inf / 20 h | 6 | `#FFFFFF` · sombra 0 4 12 rgba(23,22,28,0.18) | 24 arriba-izq y arriba-der; 0 abajo |
| 2.1 | `asa` (4019:3354) | 177,12 rel. | 36×4 | — | — | `#DAD8D2` | 2 |
| 2.2 | `Agregar evento` (4019:3355) | 20,22 rel. | 350×26 | — | — | — | — |
| 2.3 | `botón · Escanear el QR del evento` (4019:3356) | 20,54 rel. | 350×100 | 16 h / 12 v | 14 | `#FFC400` | 14 |
| 2.4 | `botón · Elegir pantallazo de la galería` (4019:3369) | 20,160 rel. | 350×100 | 16 h / 12 v | 14 | sin relleno · borde 1.5 `#DAD8D2` | 14 |
| 2.5 | `botón · Crear el evento a mano` (4019:3380) | 20,266 rel. | 350×100 | 16 h / 12 v | 14 | sin relleno · borde 1.5 `#DAD8D2` | 14 |
| 2.6 | aviso de pie (4019:3390) | 20,372 rel. | 350×36 | — | — | — | — |

Alturas: asa (12→16) + gap 6 → título (22→48) + gap 6 → fila 1 (54→154) + gap 6 → fila 2 (160→260) + gap 6 → fila 3 (266→366) + gap 6 → aviso (372→408) + padding inferior 28 = 436. Hijos centrados horizontalmente (`items-center`). La hoja arranca en y 408, es decir, tapa desde la mitad de la tarjeta «Reunión semillero» hacia abajo, incluido el FAB y la navegación.

### Fila de opción (anatomía)

| Parte | Medida |
|---|---|
| Contenedor | 350×100 · padding 16 h / 12 v · fila, gap 14, hijos centrados verticalmente · radio 14 |
| Contenedor de icono | 40×40 · radio 12 · en (16,30) rel. a fila · fila primaria: fondo `#FFFFFF`; filas secundarias: fondo `#F4F3EF` |
| Icono | 24×24 centrado en el contenedor (8,8) |
| Columna de texto | x 70 (16 + 40 + 14) · 242 de ancho · columna, gap 2 · alto 33 (2 líneas) o 47 (3 líneas en fila 3) |
| Título | Archivo Bold 16 · caja 17 · `#17161C` |
| Subtítulo | Archivo Regular 13 · caja 14 por línea · fila primaria `#17161C`; secundarias `#66636D` |
| Chevrón «›» | Archivo Bold 20 · caja 8×22 · en (326,39) rel. a fila · primaria `#17161C`; secundarias `#66636D` |
| Borde | primaria sin borde; secundarias 1.5 `#DAD8D2` |
| Sombra | ninguna |

### Textos literales de la hoja

| Texto | Fuente | Peso | Tamaño | Interlineado | Color |
|---|---|---|---|---|---|
| Agregar evento | Bricolage Grotesque | Bold | 22 | normal (26) | `#17161C` |
| Escanear el QR del evento | Archivo | Bold | 16 | normal (17) | `#17161C` |
| La alarma queda lista sin escribir nada | Archivo | Regular | 13 | normal (14) | `#17161C` |
| Elegir pantallazo de la galería | Archivo | Bold | 16 | normal (17) | `#17161C` |
| Leemos el QR que aparezca en la imagen | Archivo | Regular | 13 | normal (14) | `#66636D` |
| Crear el evento a mano | Archivo | Bold | 16 | normal (17) | `#17161C` |
| Escribe fecha, hora y lugar; te damos su QR | Archivo | Regular | 13 | normal (28 = 2 líneas) | `#66636D` |
| › (×3) | Archivo | Bold | 20 | normal (22) | `#17161C` / `#66636D` / `#66636D` |
| También puedes compartir un pantallazo desde WhatsApp o la galería con Alarmas QR. | Archivo | Regular | 13 | 1.35 (caja 36 = 2 líneas) | `#66636D`, centrado |

### Iconos vectoriales de la hoja

| Capa | Tamaño | Dónde |
|---|---|---|
| `icono · escanear` (4019:3358, base 4006:51) | 24×24 | fila 1, (8,8) rel. a contenedor de icono |
| `icono · galería` (4019:3371, base 4006:57) | 24×24 | fila 2 |
| `icono · más` (4019:3382, base 4006:61) | 24×24 | fila 3 |

Colores del trazo de los iconos: no visibles en la respuesta (assets SVG; el componente indica «recolorear el trazo según contexto»; en la captura se ven en `#17161C`).

---

## 3. M05 · Guardada + deshacer (4:223)

Base idéntica a M02 (barra, HOY con 2 tarjetas, MAÑANA con 2 tarjetas, navegación). Diferencias: agrupador «DOMINGO 30», tarjeta nueva, espaciador flexible, snackbar, y el FAB sube a (221,600).

### Bloques de arriba abajo

| # | Capa | Pos (x,y) | Tamaño | Padding | Gap | Fondo | Radio |
|---|---|---|---|---|---|---|---|
| 1 | `barra superior` (4:224) | 0,0 | 390×56 | 16 h | — | `#FFFFFF`, borde inf. 1.5 `#DAD8D2` | 0 |
| 2 | `contenido` (4:226) | 0,56 | 390×724 | 20 h / 16 v | 12 | `#FFFFFF` | 0 |
| 2.1 | `HOY · JUEVES 27` (4:227) | 20,16 rel. | 124×14 | — | — | — | — |
| 2.2 | `tarjeta · Reunión con el tutor` (4017:2898) | 20,42 rel. | 350×75 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 2.3 | `tarjeta · Gimnasio` (4:245) | 20,129 rel. | 350×53 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 2.4 | `MAÑANA · VIERNES 28` (4017:2907) | 20,194 rel. | 165×14 | — | — | — | — |
| 2.5 | `tarjeta · Vuelo BOG–MDE` (4017:2908) | 20,220 rel. | 350×75 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 2.6 | `tarjeta · Reunión semillero` (4017:2917) | 20,307 rel. | 350×75 | 14 h / 10 v | 10 | `#FFFFFF`, borde 1.5 `#DAD8D2` | 14 |
| 2.7 | `DOMINGO 30` (4:235) | 20,394 rel. | 94×14 | — | — | — | — |
| 2.8 | `tarjeta · Entrega de proyecto UX` (4:236) | 20,420 rel. | 350×75 | 14 h / 10 v | 10 | `#FFFFFF`, **borde 2 `#0B7048`** | 14 |
| 2.9 | `Frame` espaciador (4:252) | 20,507 rel. | 10×141 (flex 1) | — | — | — | — |
| 2.10 | `snackbar` (4:253) | 20,660 rel. (abs 20,716) | 350×48 | 14 h / 10 v | 10 | `#17161C` | 12 |
| 3 | `FAB · Escanear` (4:266) | 221,600 | 153×56 | 20 izq / 24 der | 10 | `#FFC400` | 16 |
| 4 | `navegación` (4:256) | 0,780 | 390×64 | 36 h | space-between | `#FFFFFF`, borde sup. 1.5 `#DAD8D2` | 0 |

### Tarjeta nueva (`tarjeta · Entrega de proyecto UX`, 4:236)

Qué la distingue de las demás: **borde 2 px `#0B7048`** (en vez de 1.5 `#DAD8D2`) y **chip «Nueva» relleno `#17161C` con texto `#FFFFFF`** (en vez de chip en contorno). Resto de anatomía igual a §0:

| Parte | Medida |
|---|---|
| Hora | «3:15» Spline Sans Mono Bold 26 + espacio 6 + «pm» Spline Sans Mono Medium 12 · `#17161C` · caja 81×31 en (14,22) |
| Título | «Entrega de proyecto UX» Archivo Bold 15 `#17161C` |
| Línea | «evento 4:00 pm · Aula SD-703» Archivo Regular 13 `#66636D` |
| Chip «Nueva» | 60×19 · fondo `#17161C` · sin borde · radio 999 · padding 12/3 · texto Archivo SemiBold 12 `#FFFFFF` (36×13) |
| Switch | 44×26 en (292,24.5), activo |
| Separación con «DOMINGO 30» | 12 (394+14 = 408 → 420); desde la tarjeta anterior 12 (382 → 394) |

### Snackbar (4:253)

| Parte | Medida |
|---|---|
| Contenedor | 350×48 · pos (20,660) rel. a contenido = abs (20,716) · fondo `#17161C` · radio 12 · padding 14 h / 10 v · fila, gap 10, centrado vertical · sin borde, sin sombra |
| Borde inferior | 764 abs → 16 por encima de la navegación (780) = padding inferior del contenido |
| Mensaje | «Alarma guardada · También en Google Calendar» · Archivo Regular 13 · interlineado normal (caja 202×28, 2 líneas) · `#FFFFFF` · flex 1 · en (14,10) rel. |
| Acción | «Deshacer · 5 s» · Archivo Bold 12 · **subrayado** · `#FFFFFF` · caja 110×26 · alineado a la derecha · en (226,11) rel. |
| Relación con el FAB | FAB termina en y 656; snackbar empieza en 716 → 60 de separación. El FAB sube de 708 (M02) a 600 (−108). |

### Textos propios de M05 (los demás, ver §1)

| Texto | Fuente | Peso | Tamaño | Interlineado | Color |
|---|---|---|---|---|---|
| DOMINGO 30 | Archivo | Bold | 13 (tracking 1.04) | normal (14) | `#66636D` |
| 3:15 / pm | Spline Sans Mono | Bold / Medium | 26 / 12 | normal (31) | `#17161C` |
| Entrega de proyecto UX | Archivo | Bold | 15 | normal (16) | `#17161C` |
| evento 4:00 pm · Aula SD-703 | Archivo | Regular | 13 | normal (14) | `#66636D` |
| Nueva | Archivo | SemiBold | 12 | normal (13) | `#FFFFFF` |
| Alarma guardada · También en Google Calendar | Archivo | Regular | 13 | normal (28, 2 líneas) | `#FFFFFF` |
| Deshacer · 5 s | Archivo | Bold | 12 | normal, subrayado | `#FFFFFF` |

### Iconos vectoriales

Igual que §1: `icono · alarma` (4007:170), `icono · calendario` (4007:175), `icono · ajustes` (4007:181) 20×20; `icono · escanear` (4019:3407) 26×26 en el FAB; `switch` ×5 (44×26).

---

## 4. M03b · Pantallazo recibido (4020:3295)

Pantalla sin barra de navegación inferior: `contenido` ocupa 390×788 (y 56 → 844). Hijos del contenido **centrados horizontalmente**.

### Bloques de arriba abajo

| # | Capa | Pos (x,y) | Tamaño | Padding | Gap | Fondo | Radio |
|---|---|---|---|---|---|---|---|
| 1 | `barra superior` (4020:3296) | 0,0 | 390×56 | 16 h | 12 | `#FFFFFF`, borde inf. 1.5 `#DAD8D2` | 0 |
| 1.1 | `‹` (4020:3297) | 16,6 | 44×44 | — | — | — | — |
| 1.2 | `Pantallazo recibido` (4020:3298) | 72,15 | 204×26 | — | — | — | — |
| 2 | `contenido` (4020:3300) | 0,56 | 390×788 | 20 h / 16 v | 12 | `#FFFFFF` | 0 |
| 2.1 | `chip` «✓ QR detectado» (4020:3447) | 138.5,16 rel. | 113×21 | 12 h / 4 v | — | sin relleno · borde 1.5 `#0B7048` | 999 |
| 2.2 | titular (4020:3449) | 20,49 rel. | 350×48 | — | — | — | — |
| 2.3 | `vista previa del pantallazo` (4020:3450) | 20,109 rel. | 350×300 | — | — (hijo centrado) | `#F4F3EF` | 16 |
| 2.3.1 | `mensaje` (4020:3451) | 50,36.5 rel. a vista previa | 250×227 | 14 h / 12 v | 10 | `#FFFFFF` · sombra 0 4 12 rgba(23,22,28,0.18) | 14 |
| 2.3.1.a | «Grupo MISO UX · hoy 8:12 am» (4020:3452) | 44.5,12 rel. a mensaje | 161×13 | — | — | — | — |
| 2.3.1.b | cuerpo del mensaje (4020:3453) | 14,35 rel. a mensaje | 222×34 | — | — | — | — |
| 2.3.1.c | `marco de lectura` (4020:3454) | 57,79 rel. a mensaje | 136×136 | 8 | — | sin relleno · borde 3 `#17161C` | 10 |
| 2.3.1.d | `código QR · evento` (4020:3455) | 8,8 rel. a marco | 120×120 | — | — | `#FFFFFF`, módulos `#17161C` | 4 |
| 2.4 | «Origen: …» (4020:3541) | 20,421 rel. | 350×14 | — | — | — | — |
| 2.5 | `espacio` (4020:3542) | 20,447 rel. | 350×177 (flex 1) | — | — | — | — |
| 2.6 | `botón · Continuar` (4020:3543) | 20,636 rel. (abs 20,692) | 350×52 | — | — | `#FFC400` | 999 |
| 2.7 | `botón · Elegir otra imagen` (4020:3546) | 20,700 rel. (abs 20,756) | 350×32 | — | — | sin relleno | 999 |
| 2.8 | pie (4020:3549) | 20,744 rel. | 350×28 | — | — | — | — |

Comprobación vertical del contenido: 16 + chip 21 + 12 + titular 48 + 12 + vista previa 300 + 12 + origen 14 + 12 + espacio 177 + 12 + Continuar 52 + 12 + Elegir otra 32 + 12 + pie 28 + 16 = 788.

### Controles

| Control | Medida |
|---|---|
| Botón atrás «‹» | área 44×44 en (16,6) · glifo Archivo Bold 26 `#17161C` centrado · sin fondo ni borde |
| Chip «✓ QR detectado» | 113×21 · borde 1.5 `#0B7048` · radio 999 · padding 12 h / 4 v · texto Archivo SemiBold 12 `#0B7048` (89×13) · centrado en la pantalla (x 138.5) |
| Vista previa | 350×300 · fondo `#F4F3EF` · radio 16 · sin borde · contenido centrado |
| Tarjeta «mensaje» (burbuja simulada) | 250×227 · fondo `#FFFFFF` · radio 14 · padding 14 h / 12 v · gap 10 · sombra 0 4 12 rgba(23,22,28,0.18) · hijos centrados |
| Marco de lectura | 136×136 · borde 3 `#17161C` · radio 10 · padding 8 · sin relleno |
| Código QR | 120×120 · fondo `#FFFFFF` · radio 4 · 21×21 módulos de 5.714 px en `#17161C` (componente 4010:2, «redimensionar 1:1») |
| Botón «Continuar» (primario) | 350×52 · fondo `#FFC400` · radio 999 · sin borde ni sombra · texto Archivo Bold 15 `#17161C` (71×16) centrado |
| Botón «Elegir otra imagen» (texto) | 350×32 · sin fondo ni borde · radio 999 · texto Archivo Bold 15 `#1A5BC4` **subrayado** (129×16) centrado |

### Textos literales

| Texto | Fuente | Peso | Tamaño | Interlineado | Color |
|---|---|---|---|---|---|
| ‹ | Archivo | Bold | 26 | normal | `#17161C` |
| Pantallazo recibido | Bricolage Grotesque | Bold | 22 | normal (26) | `#17161C` |
| ✓ QR detectado | Archivo | SemiBold | 12 | normal (13) | `#0B7048` |
| QR de evento detectado en tu pantallazo | Bricolage Grotesque | SemiBold | 20 | 1.2 (caja 48 = 2 líneas), centrado | `#17161C` |
| Grupo MISO UX · hoy 8:12 am | Archivo | Regular | 12 | normal (13) | `#66636D` |
| Nos vemos el domingo 30 en SD-703. Escanea para agendar 👇 | Archivo | Regular | 13 | 1.3 (caja 34 = 2 líneas), alineado a la izquierda | `#17161C` |
| Origen: WhatsApp · compartido con Alarmas QR | Archivo | Regular | 13 | normal (14), centrado | `#66636D` |
| Continuar | Archivo | Bold | 15 | normal (16) | `#17161C` |
| Elegir otra imagen | Archivo | Bold | 15 | normal (16), subrayado | `#1A5BC4` |
| Si el pantallazo no trae un QR, puedes crear el evento a mano. | Archivo | Regular | 13 | normal (caja 28 = 2 líneas), centrado | `#66636D` |

Nota: los **nombres de capa** en Figma de dos textos están desactualizados respecto al contenido real: capa «Encontramos un QR de event…» contiene «QR de evento detectado en tu pantallazo», y capa «Nos vemos el viernes 30 en…» contiene «…el domingo 30…». Manda el contenido.

### Iconos vectoriales

| Capa | Tamaño | Notas |
|---|---|---|
| `código QR · evento` (4020:3455, componente 4010:2) | 120×120 | no es SVG exportado: son 85 rectángulos «m» en `#17161C` sobre fondo blanco (retícula de 21×21, módulo 5.714 px) |

No hay otros iconos vectoriales en M03b (el «‹» y el «✓» son glifos de texto; el «👇» es emoji dentro del texto).

---

## 5. Lo que NO aparece en las respuestas

- Colores internos del `switch` (pista, perilla, borde): son assets SVG; la respuesta solo da 44×26. Descripción visual tomada de la captura.
- Color de trazo de los iconos de línea (alarma, calendario, ajustes, escanear, galería, más): assets SVG; el componente dice «recolorear según contexto».
- Estados presionado/foco de tarjetas, botones y filas de la hoja: no hay variantes en estos marcos.
- Interlineado numérico exacto de los textos con `leading normal`: se anota la altura de caja que reporta Figma.


---

# Hoja de medidas · Grupo 3 (M03, M04, M04d, M12, M13)

Fuente: Figma `4nHD4ygcnP33UH0gAhaii5`, página «02 · Móvil», marcos 390×844. Medidas en pt/px del marco; posiciones absolutas respecto a la esquina superior izquierda del marco salvo que se indique «local». Hex en mayúsculas. «no visible en la respuesta» = el dato no aparece ni en `get_metadata` ni en `get_design_context`.

Valores comunes a los cinco marcos:
- Marco raíz: relleno del marco con borde de 3 px `#17161C` y radio 26 (es el marco de dispositivo del mockup, no forma parte de la pantalla).
- Barra superior: 390×56, padding horizontal 16, gap 12 entre hijos, borde inferior 1.5 px.
- Botón «‹» (volver): 44×44 en (16, 6); Archivo Bold 26, centrado.
- Título de barra: Bricolage Grotesque Bold 22, `opsz 14`, en (72, 15), alto 26.
- Botones de acción: alto 52, píldora (radio 999), padding horizontal 20, rótulo Archivo Bold 15. Relleno primario `#FFC400` con texto `#17161C`; contorno 1.5 px `#17161C` sin relleno con texto `#17161C`.
- Margen lateral del contenido: 20 (contenido de 350 de ancho en marco de 390).
- Etiquetas de sección en mayúsculas (SONARÁ, ACTÍVALA EN 3 PASOS, QUÉ DETECTAMOS): Archivo Bold 13, tracking 1.04, `#66636D`.

---

## 1. M03 · Escáner dual — nodeId `4:135`

Fondo del marco: `#17161C` (Tinta). Columna vertical: barra superior (56) + visor (flex, 616) + hoja inferior (172).

### Bloques de arriba abajo

| # | Capa | Pos (x, y) | Tamaño | Padding | Gap | Fondo | Radio |
|---|---|---|---|---|---|---|---|
| 1 | barra superior `4:136` | (0, 0) | 390×56 | px 16 | 12 | transparente sobre Tinta; borde inferior 1.5 px `rgba(255,255,255,0.15)` | 0 |
| 2 | visor `4:142` | (0, 56) | 390×616 | 0 | 16 (columna centrada vertical y horizontal) | transparente (Tinta) | 0 |
| 3 | hoja inferior `4:181` | (0, 672) | 390×172 | pt 12, pb 32, px 20 | 10 | `#FFFFFF` | 24 (en la respuesta `rounded-[24px]` en las cuatro esquinas; solo se ven las superiores porque toca el borde inferior) |

### Barra superior (título, espaciador, chip)
- «‹» `4:137`: (16, 6) 44×44, Archivo Bold 26, blanco `#FFFFFF`, centrado.
- «Escanear QR» `4:138`: (72, 15) 135×26, Bricolage Grotesque Bold 22, interlineado normal, `#FFFFFF`.
- Espaciador `4:139` (Frame): (219, 23) 36×10, `flex 1 0 0` (empuja el chip a la derecha).
- Chip «Linterna · auto» `4:140`: (267, 12) 107×32, padding 14 horizontal / 3 vertical, radio 16, borde 1.5 px `#77747E`, sin relleno; texto `4:141` «Linterna · auto» Archivo SemiBold 12, `#FFFFFF`, interlineado normal (79×13). Sin icono.

### Visor
- Chip «● Cámara activa» `4020:3775`: absoluto local (142.5, 24) → absoluto marco (142.5, 80); 112×19; padding 10 horizontal / 3 vertical; radio 999; borde 1.5 px `#0B7048`; sin relleno; texto `4020:3776` «● Cámara activa» Archivo SemiBold 12, `#0B7048` (92×13).
- Marco de enfoque `4:143` (Frame): local (85, 167) → absoluto (85, 223); 220×220; contiene:
  - `Vector` `4:144`: local (10, 10) 200×200 (inset 4.55 %): las cuatro esquinas del visor; es un SVG exportado (`e87ac.svg`). Grosor y color del trazo: no visibles en la respuesta (en la captura se ve amarillo, esquinas redondeadas, trazo grueso; la longitud de cada esquina no es medible desde la respuesta).
  - `QR` `4:145`: local (60, 60) 100×100, opacidad 50 %; instancia `código QR` `4012:2246` 100×100, fondo `#FFFFFF`, radio 4, módulos `#17161C` de 4.762 px (retícula 21×21). Descripción del componente: «Código QR verosímil (21×21 módulos, patrones de posición y temporización). Redimensionar con proporción 1:1».
- «Apunta al código QR del evento» `4:179`: local (76, 403) → absoluto (76, 459); 238×17; Archivo Bold 16, `#FFFFFF`, interlineado normal.
- «vibra al detectar el código» `4:180`: local (127.5, 436) → absoluto (127.5, 492); 135×13; Archivo Regular 12, `#B9B7BF`, interlineado normal.

### Hoja inferior blanca
- Asa: contenedor `4:183` local (20, 12) 350×4 centrado; rectángulo `4:182` 36×4, `#DAD8D2`, radio 2, local (157, 0) → absoluto (177, 684).
- Botón «Elegir pantallazo de la galería» `4:184`: local (20, 26) → absoluto (20, 698); 350×52; contorno 1.5 px `#17161C`; radio 999; padding horizontal 20; texto `4:185` Archivo Bold 15 `#17161C` (207×16, centrado).
- Botón «Crear el evento a mano» `4:187` (dentro de `4:186` local (20, 88)): absoluto (20, 760); 350×52; contorno 1.5 px `#17161C`; radio 999; padding horizontal 20; texto `4:188` Archivo Bold 15 `#17161C` (163×16, centrado).
- Separación entre botones: 10 (gap de la hoja).
- Ningún elemento amarillo en la pantalla (los dos botones son de contorno).

### Iconos vectoriales
- `Vector` `4:144` (esquinas del visor): 200×200 dentro de un frame de 220.
- `código QR` `4012:2246`: 100×100 (instancia, no vector).

---

## 2. M04 · Alarma creada (hoja «Alarma programada» sobre M02) — nodeId `4:189`

Composición: fondo `fondo · M02 (atenuada)` `4316:1561` (390×844, réplica de M02 con barra «Mis alarmas», lista de 4 tarjetas, FAB «Escanear» y navegación) + velo `atenuado · cerrar` `4316:1633` (0, 0) 390×844 `rgba(23,22,28,0.55)` (Tinta 55 %) + hoja `4316:1635`.

### Hoja `hoja · Alarma programada` `4316:1635`
- Posición (0, 282), tamaño 390×562. Fondo `#FFFFFF`. Radio 24 solo en esquinas superiores. Padding: top 12, bottom 32, horizontal 20. Columna centrada, gap 12. Sombra: no visible en la respuesta.

Bloques (y local respecto a la hoja / y absoluto = local + 282):

| # | Capa | Pos local (x, y) | Abs. y | Tamaño | Notas |
|---|---|---|---|---|---|
| 1 | asa `4316:1636` | (177, 12) | 294 | 36×4 | `#DAD8D2`, radio 2 |
| 2 | fila título `4:194` | (20, 28) | 310 | 350×30 | gap 8, centrada |
| 3 | fila chip `4:198` | (20, 70) | 352 | 350×19 | centrada |
| 4 | tarjeta de evento `4:201` | (20, 101) | 383 | 350×144 | ver abajo |
| 5 | dato sugerido `4:215` (bloque Sonará) | (20, 257) | 539 | 350×140 | ver abajo |
| 6 | «También se agregó a Google Calendar.» `4:217` | (20, 409) | 691 | 350×13 | |
| 7 | botón · Listo `4:219` | (20, 434) | 716 | 350×52 | |
| 8 | botón · No puedo asistir · eliminar alarma `4:221` | (20, 498) | 780 | 350×32 | |

### Título
- Sello ✓ `4:195`: local (43, 0) dentro de la fila → 30×30, fondo `#129E63`, radio 999; texto «✓» `4:196` Archivo Bold 15 `#FFFFFF` (13×16).
- «¡Alarma programada!» `4:197`: Bricolage Grotesque Bold 22, `#17161C`, 226×26, interlineado normal. Gap con el sello: 8.

### Chip «Datos leídos del QR — verifícalos» `4:199`
- 207×19, local (71.5, 0) en la fila (centrado). Padding 12 horizontal / 3 vertical; radio 999; fondo `#FFF1BF` (Amarillo Suave); sin borde. Texto `4:200` Archivo SemiBold 12 `#17161C` (183×13).

### Tarjeta de evento `4:201`
- 350×144, fondo `#FFFFFF`, borde 1.5 px `#DAD8D2`, radio 14, padding 16 horizontal / 14 vertical, gap 8 (columna). Sombra `0 4 12 rgba(23,22,28,0.18)`.
- Título «Entrega de proyecto UX» `4:202`: local (16, 14), 228×24, Bricolage Grotesque SemiBold 20, `#17161C`.
- Filas de 318×15 con gap 10 entre etiqueta (ancho fijo 74) y valor (flex):
  - `4:203` local (16, 46): «FECHA» Archivo Bold 12, tracking 0.96, `#66636D` / «Dom 30 de agosto · 4:00 pm (GMT-5)» Archivo Regular 13.5 `#17161C`.
  - `4:206` local (16, 69): «LUGAR» / «Aula SD-703, Universidad» (mismos estilos).
  - `4:209` local (16, 92): «ORGANIZA» / «MISO · UniAndes  » Archivo Regular 13.5 `#17161C` + sello «✓ verificado» como run de texto Archivo Bold 13.5 `#0B7048` (Verde Texto) en la misma línea (dos espacios entre «UniAndes» y «✓»). No es un chip ni un icono: es texto.
  - `4:212` local (16, 115): «DETALLE» / «Sustentación final del prototipo.» (mismos estilos).

### Bloque «Sonará» `4:215` (dato sugerido)
- 350×140, fondo `#FFF1BF` (Amarillo Suave), radio 14, sin borde, padding 16 horizontal / 12 vertical, gap 2 (columna).
- «SONARÁ» `4016:2884`: local (16, 12), 63×14, Archivo Bold 13, tracking 1.04, `#66636D`.
- Hora `4016:2885`: local (16, 28), 219×62; texto «3:15 pm»; Spline Sans Mono Bold 52, `#17161C`, interlineado normal. **El sufijo «pm» NO aparece como run separado más pequeño en la respuesta**: el nodo de texto es un único estilo de 52 y en la captura «pm» se ve al mismo tamaño que la hora, separado por espacio ancho. Sufijo en Medium más pequeño: no visible en la respuesta (contradice la regla del DS; anotar como desviación del mockup).
- Texto de margen `4016:2886`: local (16, 92), 318×36; «30 min de margen + 15 min de trayecto desde tu ubicación habitual · » Archivo Regular 13, interlineado 1.35, `#17161C`, seguido del enlace «Editar» Archivo Bold 13 subrayado `#1A5BC4` (Azul Texto) en la misma línea.

### Textos y controles finales
- «También se agregó a Google Calendar.» `4:217`: 350×13, Archivo Regular 12, `#66636D`, centrado.
- Botón «Listo» `4:219`: 350×52, fondo `#FFC400`, radio 999, padding horizontal 20, texto `4:220` Archivo Bold 15 `#17161C` (36×16 centrado). Único elemento amarillo de acción de la hoja (el bloque Sonará usa Amarillo Suave).
- Enlace de descarte «No puedo asistir · eliminar alarma» `4:221`: 350×32, sin fondo ni borde, radio 999, padding horizontal 20; texto `4:222` Archivo Medium 14 subrayado, `#66636D` (209×15 centrado). Separación con «Listo»: 12 (gap).

### Iconos vectoriales
- Ninguno en la hoja (el ✓ del sello y el ✓ de «verificado» son glifos de texto). En el fondo M02 atenuado: `icono · alarma`, `icono · calendario`, `icono · ajustes` (20×20) y `icono · escanear` (26×26) del FAB, todos instancias.

---

## 3. M04d · ¿Eliminar alarma? — nodeId `4330:1432`

Composición: `fondo · M04 (atenuada)` `4330:1433` (390×844: réplica completa de M04 incluyendo su propio velo `4330:1490` y la hoja `4330:1491` con las mismas medidas de la sección 2) + segundo velo `atenuado · cerrar` `4330:1539` (0, 0) 390×844 `rgba(23,22,28,0.55)` + diálogo `4330:1541`.

### Velo
- `4330:1539`: 390×844, `#17161C` al 55 % (`rgba(23,22,28,0.55)`). Es un `<a>` (tocar el velo cierra = acción segura). Nota: por encima del velo de la hoja M04 (también 55 %), por lo que la lista M02 queda doblemente atenuada.

### Diálogo `diálogo · ¿Eliminar alarma?` `4330:1541`
- Posición (24, 272), tamaño 342×300 (margen lateral 24 respecto al marco de 390). Fondo `#FFFFFF`. Radio 20. Padding 24 en los cuatro lados. Columna, gap 16. Sombra: no visible en la respuesta.

| # | Capa | Pos local (x, y) | Abs. (x, y) | Tamaño |
|---|---|---|---|---|
| 1 | «¿Eliminar alarma?» `4330:1542` | (24, 24) | (48, 296) | 294×26 |
| 2 | cuerpo `4330:1543` | (24, 66) | (48, 338) | 294×80 |
| 3 | acciones `4330:1544` | (24, 162) | (48, 434) | 294×114 (gap 10) |
| 3a | botón · Conservar `4330:1545` | (0, 0) en acciones | (48, 434) | 294×52 |
| 3b | botón · Eliminar `4330:1548` | (0, 62) en acciones | (48, 496) | 294×52 |

- Título: Bricolage Grotesque SemiBold 22, `#17161C`, interlineado normal, ancho completo.
- Cuerpo (texto exacto): «Dejarás de recibir el aviso de “Entrega de proyecto UX” (dom 30 · 4:00 pm). Si cambias de opinión, puedes volver a escanear el QR del evento.» — Archivo Regular 14, interlineado 1.4, `#17161C`, ancho completo (294).
- Botón «Conservar» (acción segura, primario): 294×52, fondo `#FFC400`, radio 999, padding horizontal 20, texto `4330:1546` Archivo Bold 15 `#17161C` (75×16, centrado).
- Botón «Eliminar» (destructivo, contorno): 294×52, sin relleno, borde 1.5 px `#C4362E` (Coral Texto), radio 999, padding horizontal 20, texto `4330:1549` Archivo Bold 15 `#C4362E` (60×16, centrado).
- Separación entre botones: 10 (metadata: 52 + 10 = 62 de desplazamiento).
- Sin iconos.

---

## 4. M12 · Permiso de cámara — nodeId `6:87`

Fondo del marco `#FFFFFF`. Columna: barra superior (56) + contenido (788).

### Barra superior `6:88`
- (0, 0) 390×56, fondo `#FFFFFF`, borde inferior 1.5 px `#DAD8D2`, padding horizontal 16, gap 12.
- «‹» `6:89`: (16, 6) 44×44, Archivo Bold 26, `#17161C`.
- «Permiso de cámara» `6:90`: (72, 15) 206×26, Bricolage Grotesque Bold 22, `#17161C`.

### Contenido `6:91`
- (0, 56) 390×788, fondo `#FFFFFF`, padding top 14, bottom 32, horizontal 20; columna gap 11; el espaciador `4314:1561` es flex 1 y ancla las alternativas abajo.

Bloques (y local respecto a `contenido` / abs = local + 56):

| # | Capa | Pos local (x, y) | Abs. y | Tamaño | Fondo / borde | Radio |
|---|---|---|---|---|---|---|
| 1 | textura · módulos QR `4010:1878` | (0, 0) | 56 | 390×120 | módulos 8×8 radio 2 `rgba(23,22,28,0.08→0.02)` degradando hacia abajo; capa con opacidad 50 % | — |
| 2 | visor apagado `6:92` | (20, 14) | 70 | 350×110 | `#F4F3EF` (Gris Superficie); sin borde | 16 |
| 3 | «La cámara está apagada para la app» `6:94` | (20, 135) | 191 | 350×52 | — | — |
| 4 | «Solo la usamos para leer códigos QR de eventos. Nunca guardamos fotos ni videos.» `6:95` | (20, 198) | 254 | 350×30 | — | — |
| 5 | pasos `6:96` | (20, 239) | 295 | 350×125 | `#FFFFFF`, borde 1.5 px `#DAD8D2`, sombra `0 4 12 rgba(23,22,28,0.18)` | 14 |
| 6 | botón · Abrir ajustes `6:110` | (20, 375) | 431 | 350×52 | `#FFC400` | 999 |
| 7 | espaciador `4314:1561` | (20, 438) | 494 | 350×168 | flex 1 | — |
| 8 | divisor «mientras tanto» `6:112` | (20, 617) | 673 | 350×13 | — | — |
| 9 | botón · Elegir pantallazo de la galería `6:116` | (20, 641) | 697 | 350×52 | contorno 1.5 px `#17161C` | 999 |
| 10 | botón · Crear el evento a mano `6:120` | (20, 704) | 760 | 350×52 | contorno 1.5 px `#17161C` | 999 |

Detalles:
- Banda de textura: 120 de alto, pegada al borde superior del contenido (y absoluto 56–176), ancho 390 (sale de los márgenes de 20). Descripción del componente: «retícula de módulos QR que se desvanece hacia abajo. Opacidad máxima 8 %».
- Visor apagado `6:92`: 350×110, `#F4F3EF`, radio 16, contenido centrado; icono `icono · escanear (visor apagado)` `4378:1465` 48×48 en local (151, 31) → absoluto (171, 101); SVG `cd263.svg`; descripción: «Ícono de línea 24 px, trazo 2 px, terminales redondeadas. Recolorear el trazo según contexto» (aquí escalado a 48; color del trazo en la captura: Tinta; hex no visible en la respuesta). Texto «✕» `6:93` oculto (hidden).
- Título `6:94`: Bricolage Grotesque Bold 22, `#17161C`, centrado, dos líneas (52 de alto).
- Cuerpo `6:95`: Archivo Regular 14, `#17161C`, centrado, interlineado normal (30 de alto = 2 líneas).
- Tarjeta pasos `6:96`: padding 14 horizontal / 12 vertical, gap 7.
  - «ACTÍVALA EN 3 PASOS» `6:97`: local (14, 12), 165×14, Archivo Bold 13, tracking 1.04, `#66636D`.
  - Filas `6:98`/`6:102`/`6:106`: 322×22 en local y 33 / 62 / 91; gap 10 entre numeral y texto; numeral en círculo 22×22 fondo `#17161C` radio 999 con «1»/«2»/«3» Archivo Bold 12 `#FFFFFF`; textos Archivo Regular 13 `#17161C`: «Abrir los ajustes del teléfono», «Permisos › Cámara», «Elegir “Permitir con la app en uso”».
- Botón «Abrir ajustes» `6:110`: 350×52, `#FFC400`, radio 999, padding horizontal 20; texto `6:111` Archivo Bold 15 `#17161C` (91×16 centrado). Único elemento amarillo.
- Divisor `6:112`: fila 350×13, gap 8: línea `6:113` 128.5×1 `#DAD8D2` (flex 1) + «mientras tanto» `6:114` Archivo Regular 12 `#66636D` (77×13) + línea `6:115` 128.5×1 `#DAD8D2`.
- Alternativas ancladas abajo: botones de contorno 350×52 en y absolutos 697 y 760, separación 11; padding inferior del contenido 32 (el segundo botón termina en 812).

### Iconos vectoriales
- `icono · escanear (visor apagado)` `4378:1465`: 48×48.

---

## 5. M13 · QR inválido — nodeId `6:122`

Fondo del marco `#FFFFFF`. Columna: barra superior (56) + contenido (788).

### Barra superior `6:123`
- (0, 0) 390×56, `#FFFFFF`, borde inferior 1.5 px `#DAD8D2`, padding horizontal 16, gap 12.
- «‹» `6:124`: (16, 6) 44×44, Archivo Bold 26, `#17161C`.
- «QR sin evento» `6:125`: (72, 15) 146×26, Bricolage Grotesque Bold 22, `#17161C`.

### Contenido `6:126`
- (0, 56) 390×788, `#FFFFFF`, padding top 14, bottom 16, horizontal 20; columna gap 11. **No hay espaciador flexible**: los bloques quedan apilados desde arriba (a diferencia de M12); el último botón termina en y absoluto 625.

| # | Capa | Pos local (x, y) | Abs. y | Tamaño | Fondo / borde | Radio |
|---|---|---|---|---|---|---|
| 1 | textura · módulos QR `4009:1644` | (0, 0) | 56 | 390×120 | idéntica a M12 (módulos 8×8, radio 2, `rgba(23,22,28,0.08→0.02)`, capa al 50 %) | — |
| 2 | fila del sello `6:128` | (20, 14) | 70 | 350×64 | centrada | — |
| 2a | sello «!» `6:127` | (143, 0) local → abs (163, 70) | 70 | 64×64 | `#FDECEA` (Coral Suave), borde 3 px `#C4362E` | 999 |
| 3 | «Este QR no contiene un evento» `6:130` | (20, 89) | 145 | 350×26 | — | — |
| 4 | «Leímos el código, pero no trae fecha ni datos de evento para crear una alarma.» `6:131` | (20, 126) | 182 | 350×30 | — | — |
| 5 | diagnóstico `6:132` | (20, 167) | 223 | 350×122 | `#FFFFFF`, borde 1.5 px `#C4362E`; sin sombra en la respuesta | 14 |
| 6 | espaciador fijo `6:139` | (20, 300) | 356 | 10×32 | — | — |
| 7 | botón · Volver a escanear `6:140` | (20, 343) | 399 | 350×52 | `#FFC400` | 999 |
| 8 | botón · Crear el evento a mano `6:142` | (20, 406) | 462 | 350×52 | contorno 1.5 px `#17161C` | 999 |
| 9 | fila enlace `6:145` | (20, 469) | 525 | 350×100 | contiene relleno fijo `6:144` 10×100 + botón-enlace | — |
| 9a | botón · Abrir el enlace bajo mi responsabilidad `6:146` | (32, 0) local → abs (52, 525) | 525 | 296×32 | sin fondo ni borde | 999 |

Detalles:
- Banda de textura: 120 de alto en y absoluto 56–176, ancho 390.
- Sello «!» `6:127`: 64×64, `#FDECEA`, borde 3 px `#C4362E`, radio 999; glifo «!» `6:129` Bricolage Grotesque Bold 28, `#C4362E` (8×34, centrado). Es texto, no icono.
- Título `6:130`: Bricolage Grotesque Bold 22, `#17161C`, centrado, una línea.
- Cuerpo `6:131`: Archivo Regular 14, `#17161C`, centrado, dos líneas.
- Tarjeta de diagnóstico coral `6:132`: 350×122, padding 14 horizontal / 12 vertical, gap 7; borde 1.5 px `#C4362E`; fondo `#FFFFFF`; radio 14.
  - «QUÉ DETECTAMOS» `6:133`: local (14, 12), 138×14, Archivo Bold 13, tracking 1.04, `#66636D`.
  - Fila `6:134` local (14, 33) 322×19, gap 8: chip `6:135` «enlace externo» 106×19, padding 12 horizontal / 3 vertical, radio 999, fondo `#FDECEA`, borde 1.5 px `#C4362E`, texto Archivo SemiBold 12 `#C4362E` (82×13); a su derecha `6:137` «https://menu.resturl.co/…» Spline Sans Mono Medium 12, `#66636D` (180×14).
  - `6:138` local (14, 59) 322×51: «Parece el menú de un restaurante. Por tu seguridad no abrimos enlaces automáticamente (protección anti-quishing).» Archivo Regular 12, interlineado 1.45, `#66636D`.
- Separación entre diagnóstico y botón primario: gap 11 + espaciador fijo 32 + gap 11 = 54 (de 289 a 343 local).
- Botón «Volver a escanear» `6:140`: 350×52, `#FFC400`, radio 999, padding 20; texto `6:141` Archivo Bold 15 `#17161C` (127×16 centrado). Único elemento amarillo.
- Botón «Crear el evento a mano» `6:142`: 350×52, contorno 1.5 px `#17161C`, radio 999, padding 20; texto `6:143` Archivo Bold 15 `#17161C` (163×16).
- Enlace «Abrir el enlace bajo mi responsabilidad» `6:146`: 296×32, padding horizontal 20, radio 999, sin fondo/borde; texto `6:147` Archivo Bold 14 subrayado, `#1A5BC4` (Azul Texto) (256×15). En la fila `6:145` va precedido por un frame de relleno `6:144` de 10×100 (fondo blanco) que empuja la fila a 100 de alto; el enlace queda centrado horizontalmente.
- Divisor «mientras tanto»: **no existe en M13** (solo en M12).

### Iconos vectoriales
- Ninguno (el «!» es glifo de texto; la textura es una instancia de rectángulos).

---

## Resumen de colores usados (hex)
- Tinta `#17161C` · Blanco `#FFFFFF` · Amarillo `#FFC400` · Amarillo Suave `#FFF1BF`
- Gris Borde `#DAD8D2` · Gris Superficie `#F4F3EF` · Gris Texto `#66636D` · Gris sobre Tinta `#B9B7BF` · Gris borde sobre Tinta `#77747E`
- Verde `#129E63` · Verde Texto `#0B7048` · Coral Texto `#C4362E` · Coral Suave `#FDECEA` · Azul Texto `#1A5BC4`
- Velo Tinta 55 % `rgba(23,22,28,0.55)` · Borde barra sobre Tinta `rgba(255,255,255,0.15)`

## Datos no visibles en la respuesta
- M03: grosor y color hex del trazo de las esquinas del visor (`Vector` `4:144`, SVG); color del trazo del icono en M12 (SVG).
- M04: sombra de la hoja inferior; estilo del sufijo «pm» (aparece al mismo tamaño 52 que la hora, no como run Medium más pequeño).
- M04d: sombra del diálogo.
- Todos: nombres de estilos/variables de Figma (la respuesta trae hex crudos, no tokens).
