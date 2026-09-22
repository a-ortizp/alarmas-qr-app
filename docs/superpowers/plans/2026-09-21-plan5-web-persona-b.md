# Plan 5 · Web de la Persona B — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Construir en `apps/web` los componentes L09 de tablero que faltan (indicador, chip, píldoras de filtro/pestaña, campo de búsqueda, tabla de datos, estado vacío de tabla, paginador, gráfica de barras «Escaneos por semana» y vista previa del afiche) y reemplazar los cuatro marcadores de la Persona B por sus páginas reales: **W01** «Mis Alarmas» (hub con indicadores, gráfica, pestañas Todos/Creados/Escaneados y filtros Próximos/Pasados/Borradores), **W03** «Detalle Evento» (asistentes anónimos paginados), **W04** «Reportes» y **W05** «Descargar QR», las dos últimas con el patrón «sección con tarjeta de formulario» que ya usa W06. Todo pixel-perfect contra Figma a 1280×820 y verificado con Vitest.

**Architecture:** Igual que el Plan 3 (misma spec, misma app): cada página es un componente independiente en `src/app/pantallas/<código-nombre>/` con estado en señales; los query params (`?origen=`, `?estado=`, `?q=`, `?pagina=`) llegan como `input()` gracias a `withComponentInputBinding()`, ya activo desde el Plan 3, y el `:id` de `/eventos/:id` llega igual como `input.required<string>()`. Los componentes L09 nuevos viven en `src/app/componentes/<nombre>/` con selector `aq-*` y solo usan variables de `tokens.css` (lo vigila `npm run lint`). Los datos que faltan (`escaneosPorSemana`, `web.reporte`, `web.descargaQR`, `web.asistentes`, `web.eventosPasados`, `web.filtros`) ya están en `dataset.json`: esta plan solo los tipa en `modelos.ts`/`datos.service.ts`, nunca toca el JSON. El único componente estructural nuevo (`aq-tabla`) usa `ViewEncapsulation.None` con selectores prefijados por su propio atributo de host (`table[aq-tabla] thead th`) porque estiliza contenido proyectado (encabezado y filas que cada página arma), igual de aislado que el resto pero sin recurrir a `::ng-deep`.

**Tech Stack:** Angular 22.1.7 (CLI/build 22.1.8, CDK 22.1.7) · TypeScript ~6.0.2 · Signal Forms · Vitest 4 (`@angular/build:unit-test`, jsdom) · Node 22.23.2 / npm 10.9.8 — idéntico al Plan 3, sin dependencias nuevas.

**Spec:** `docs/superpowers/specs/2026-09-20-maquetacion-persona-a-design.md` (§2 arquitectura general — aplica a ambas personas, no hay decisiones de diseño abiertas para este plan) y el propio `docs/superpowers/plans/2026-09-21-plan3-web-persona-a.md` como precedente de convenciones de código. **Criterios de aceptación y medidas:** `docs/FUNCIONALIDADES.md` (F-W01, F-W02, F-W03, F-W04, F-W05, F-W06), `docs/TRAZABILIDAD.md` §2 (rutas y conexiones) y `docs/MOCKUPS.md` §7 (medidas de Figma); `docs/DESIGN_SYSTEM.md` §7 trae las filas «Indicador», «Píldoras de filtro y pestaña», «Campo de búsqueda», «Estado vacío de tabla», «Paginador», «Tabla de datos», «Chips», «Gráfica de barras · Escaneos por semana», «Tarjeta de afiche QR» y «Sección con tarjeta de formulario» — cada tarea cita su fila.

## Global Constraints

- **Node:** antes de cualquier `npm`/`npx` en `apps/web`, ejecutar `source ~/.nvm/nvm.sh && nvm use` (lee `.nvmrc`).
- **Tokens:** nunca se escriben a mano colores, medidas en px, `rgb()`/`rgba()` ni radios en `src/app`. Solo variables de `tokens.css`. Lo verifica `node scripts/verificar-tokens.mjs` dentro de `npm run lint`. Coordenadas de `<svg>` (`viewBox`, `d`, `rx`, `stroke-width`) están permitidas.
- **Medidas nuevas:** primero a `packages/tokens/design-tokens.json` (v1.13) y luego a `packages/tokens/tokens.css` **y** `apps/web/src/tokens.css`; los dos `.css` deben quedar idénticos (`cmp`).
- **Dataset:** `dataset.json` no se modifica. Ya trae todos los datos de W01/W03/W04/W05 (`web.escaneosPorSemana`, `web.reporte`, `web.descargaQR`, `web.asistentes`, `web.eventosPasados`, `web.filtros`, `web.eventos`); solo falta tiparlos.
- **Amarillo:** un solo elemento amarillo por pantalla. W01: «Exportar reporte» primario; «Descargar QR en lote» va en contorno (regla explícita del DS). W04: «Generar y descargar» primario. W05: «Descargar» primario; «Cancelar» en contorno.
- **Alturas:** botones 44, campos 48, campo de búsqueda 40, píldoras de filtro/pestaña 34, paginador 30, tabla: encabezado 26 / fila 48, indicador 104 (ancho 242).
- **Radios:** 14 tarjetas; 12 campo y estado vacío de tabla; 8 chip; 4 barra de gráfica y casilla; píldora en botones, píldoras de filtro, paginador y segmentado.
- **Texto:** ningún texto ≤ 15 px por debajo de 12; tonos AA (`--color-texto-secundario`, `--color-destructivo`, `--color-enlace`, `--color-exito`) para todo texto ≤ 15 px, incluida la fila «Ver detalle ›» (Azul Texto).
- **Privacidad (Ley 1581):** la tabla de W03 nunca muestra teléfono ni correo — solo `alias` (o iniciales) del dataset; la nota de privacidad de `dataset.json` (`web.asistentes.notaPrivacidad`) se muestra siempre visible bajo la tabla.
- **Degradación elegante:** las tablas anchas de W01 y W03 van dentro de un contenedor `overflow-x: auto`, nunca con anchos en px fijos que rompan a 1024px. Las páginas de dos columnas (W04, W05: tarjeta de 600 + tarjeta lateral) inyectan `CortesService` y apilan con `apilarColumnas()`, igual que W06.
- **Nombres de archivo y raíz de página:** cada página en `src/app/pantallas/<código>-<nombre>/<código>-<nombre>.component.ts`, selector `aq-<código>-<nombre>`, raíz con `data-codigo="<código>"` y `data-estado="<estado>"` en subestados.
- **Commits:** empiezan por el código de pantalla o por `L09` para los componentes compartidos (p. ej. «W01: tablero de eventos»), al menos un commit por tarea. **Los commits NO llevan línea de coautoría de Claude/Anthropic — solo el autor del usuario** (instrucción explícita para este plan, distinta del Plan 3). La rama es `feature/plan5-web-persona-b` (D1); es la única rama del plan, con un solo PR al final revisado por la Persona A. El orden acordado con el equipo es **este plan (web) antes que el Plan 4 (móvil)**.
- **Verificación por tarea:** desde `apps/web`, `npx prettier --write "src/**/*.{ts,html,css}"` y después `npx ng test --watch=false && npm run lint && npx ng build --configuration production`, las tres en verde al cerrar cada tarea.

## Decisiones tomadas al escribir el plan (revisar si se discrepa)

- **D1 · Rama:** todo el Plan 5 va en `feature/plan5-web-persona-b`, commits prefijados por pantalla, un PR revisado por la Persona A — mismo criterio que D1 del Plan 3, pero sin la línea de coautoría de Claude (ver Global Constraints) y con el plan web ejecutándose antes que el móvil.
- **D2 · `aq-tabla` con `ViewEncapsulation.None` en vez de `::ng-deep`:** el encabezado y las filas de cada tabla los arma la página (columnas distintas en W01 y W03), así que `aq-tabla` no puede envolver `<thead>`/`<tbody>` en su propio template — solo puede diseñar contenido proyectado. `::ng-deep` está deprecado; en vez de eso, el componente desactiva el encapsulamiento y prefija cada selector con su propio atributo de host (`table[aq-tabla] thead th`), así el estilo no escapa a otras tablas del árbol.
- **D3 · «Pasados» sin «Ver detalle»:** `dataset.json` (`web.eventosPasados`) trae eventos finalizados sin `id` (campo `nombre`, no `titulo`); no hay a dónde navegar. La fila de acción de la tabla queda vacía en el filtro Pasados — no se inventa un id. Anotar en el repo de UX si se quiere un id real para estos eventos.
- **D4 · W03 solo tiene datos completos de asistentes para `w-partido`:** `dataset.json` (`web.asistentes`) no trae un mapa por evento, solo un bloque fijo con `eventoId: "w-partido"`. Al visitar `/eventos/w-partido` la tabla, la paginación y la búsqueda usan ese bloque; para cualquier otro id (`w-seminario`) se muestran los indicadores del evento (sí existen por id en `web.eventos`) y la tabla de asistentes en estado vacío. Mismo criterio que D3 del Plan 2 (un solo caso demostrable en el dataset).
- **D5 · Filtro `estado` de W01 comparte el query param con el aviso de descarga:** TRAZABILIDAD pone el snackbar de W05 en `/alarmas?estado=descarga-completada`. Un valor de `estado` que no es `proximos`/`pasados`/`borradores` (como `descarga-completada`) no filtra nada (cae al mismo listado que `proximos`) y solo dispara el snackbar vía `effect()`, igual que el patrón D8 del Plan 3 en W00.
- **D6 · Búsqueda dispara con Enter, no en cada tecla:** el campo de búsqueda actualiza su propio valor en cada tecla (para que el cursor y el texto se vean, como piden los mockups `?q=Sem`/`?q=Mi`) pero solo navega (cambia el query param, y por tanto filtra) al presionar Enter — evita una ráfaga de navegaciones y hace la prueba determinista.
- **D7 · «Descargar de nuevo» (W04) y checkboxes de W05 no descargan nada real:** es maquetación sin backend. «Descargar de nuevo» solo muestra el snackbar `mensajes.descargaCompletada` para dar retroalimentación; los checkboxes de W05 solo cambian el estado de selección en memoria.
- **D8 · Casilla de selección (W05):** el DS no la mide explícitamente; se usa el tamaño de casilla del móvil (`size.movil.casilla` = 20, radio `radius.casilla` = 4, ya en `design-tokens.json`) por ser el mismo componente conceptual, ahora también derivado a la web.
- **D9 · Separación entre grupos de píldoras (20):** DS §7 «Píldoras de filtro y pestaña» pide 20 entre grupos y 10 dentro de un grupo; 10 ya es `--space-10`, 20 es nuevo (`--space-20`, Tarea 1).

## Mapa de archivos

```
packages/tokens/design-tokens.json                        modificar · v1.13: size.web (campo-busqueda, paginador-pildora, grafica, afiche, checkbox, enlace-tabla), font.scale (grafica/afiche), space.web (estado-vacio), space.escala (+20), radius.casilla → css
packages/tokens/tokens.css · apps/web/src/tokens.css      modificar · mismas adiciones (idénticos)
apps/web/src/app/datos/modelos.ts · datos/datos.service.ts  modificar · SemanaEscaneo, Reporte, DescargaQR, Asistente, Asistentes, EventoPasado, Filtros + señales
apps/web/src/app/componentes/icono/iconos.ts               modificar · icono «lupa»
apps/web/src/app/componentes/indicador/aq-indicador.component.ts        crear
apps/web/src/app/componentes/chip/aq-chip.component.ts                  crear
apps/web/src/app/componentes/pildoras/aq-pildoras.component.ts          crear
apps/web/src/app/componentes/campo-busqueda/aq-campo-busqueda.component.ts crear
apps/web/src/app/componentes/tabla/aq-tabla.component.ts                crear
apps/web/src/app/componentes/estado-vacio/aq-estado-vacio.component.ts  crear
apps/web/src/app/componentes/paginador/aq-paginador.component.ts        crear
apps/web/src/app/componentes/grafica-barras/aq-grafica-barras.component.ts crear
apps/web/src/app/componentes/vista-previa-afiche/aq-vista-previa-afiche.component.ts crear
apps/web/src/app/pantallas/w01-mis-alarmas/w01-mis-alarmas.component.ts modificar · página real
apps/web/src/app/pantallas/w03-detalle-evento/w03-detalle-evento.component.ts modificar · página real
apps/web/src/app/pantallas/w04-reportes/w04-reportes.component.ts       modificar · página real
apps/web/src/app/pantallas/w05-descargar-qr/w05-descargar-qr.component.ts modificar · página real
apps/web/src/app/**/*.spec.ts                              crear · una por componente/página
apps/web/src/app/flujos-persona-b.spec.ts                  crear · T5 tramo W01→W03→W04/W05
docs/verificacion/W01.png · W03.png · W04.png · W05.png · docs/verificacion/README.md  crear/modificar
README.md                                                  modificar · Plan 5, decisiones, cómo sigue el Plan 4
```

---

### Task 1: Tokens v1.13 y tipado del dataset

**Files:**
- Modify: `packages/tokens/design-tokens.json`
- Modify: `packages/tokens/tokens.css`, `apps/web/src/tokens.css`
- Modify: `apps/web/src/app/datos/modelos.ts`, `apps/web/src/app/datos/datos.service.ts`
- Test: `apps/web/src/app/datos/datos.service.spec.ts` (agregar casos)

**Interfaces:**
- Consumes: `DatosService`, `Dataset`, `DatosWeb` del Plan 3; `proveedoresPrueba()`, `cargarDataset()` de `src/testing/datos-prueba.ts`.
- Produces:
  - Variables CSS nuevas: `--size-campo-busqueda-w01`, `--size-campo-busqueda-w03`, `--size-campo-busqueda-alto`, `--size-icono-lupa`, `--size-paginador-pildora`, `--size-grafica-barra-alto-max`, `--size-afiche-miniatura`, `--size-afiche-qr`, `--size-checkbox`, `--size-enlace-tabla-w`, `--size-enlace-tabla-h`, `--radius-casilla`, `--text-titulo-grafica-web`, `--text-nota-web`, `--text-valor-grafica`, `--text-etiqueta-semana`, `--text-titulo-afiche`, `--text-lema-afiche`, `--text-marca-afiche`, `--text-rotulo-afiche`, `--space-web-estado-vacio`, `--space-20`.
  - `interface SemanaEscaneo { semana: string; etiqueta: string; escaneos: number; actual?: boolean }`.
  - `interface Reporte { rangos: string[]; rangoPersonalizado: { desde: string; hasta: string }; formatos: string[]; archivoGenerado: string; nota: string; generados: { archivo: string; fecha: string; formato: string; rango: string }[]; retencionDias: number }`.
  - `interface DescargaQR { seleccionados: string[]; formatos: string[]; afiche: { marca: string; lema: string; qrMinimoCm: number; resolucionPng: number } }`.
  - `interface Asistente { alias: string; escaneo: string; alarma: string; confirmoYaVoy?: boolean; yaVoy?: boolean }`.
  - `interface Asistentes { eventoId: string; total: number; mostrados: Asistente[]; notaPrivacidad: string; pagina2: Asistente[]; porPagina: number; busquedaEjemplo: { consulta: string; resultados: string[] } }`.
  - `interface EventoPasado { nombre: string; fechaHora: string; origen: string; escaneos: number; alarmasActivas: number; estado: string }`.
  - `interface Filtros { estado: string[]; origen: string[]; borradores: unknown[]; busquedaEjemplo: { consulta: string; resultados: string[] } }`.
  - `DatosWeb` gana `escaneosPorSemana: SemanaEscaneo[]`, `reporte: Reporte`, `descargaQR: DescargaQR`, `asistentes: Asistentes`, `eventosPasados: EventoPasado[]`, `filtros: Filtros`.
  - `DatosService.escaneosPorSemana`, `.reporte`, `.descargaQR`, `.asistentes`, `.eventosPasados`, `.filtros`: `Signal<T | undefined>` computadas desde `this.dataset.value()?.web.…`.

- [ ] **Step 1: Escribir las pruebas nuevas (fallan: las señales no existen)**

Agregar al final de `describe('DatosService', …)` en `apps/web/src/app/datos/datos.service.spec.ts`:

```ts
  it('expone los datos de tablero de la Persona B (gráfica, reportes, QR, asistentes)', async () => {
    const servicio = TestBed.inject(DatosService);
    await cargarDataset();
    expect(servicio.escaneosPorSemana()?.length).toBe(8);
    expect(servicio.escaneosPorSemana()?.at(-1)?.actual).toBe(true);
    expect(servicio.reporte()?.archivoGenerado).toBe('reporte-alarmasqr-ago2026.pdf');
    expect(servicio.reporte()?.generados.length).toBe(3);
    expect(servicio.descargaQR()?.seleccionados).toEqual(['w-seminario', 'w-partido']);
    expect(servicio.asistentes()?.eventoId).toBe('w-partido');
    expect(servicio.asistentes()?.mostrados.length).toBe(4);
    expect(servicio.asistentes()?.pagina2.length).toBe(4);
    expect(servicio.eventosPasados()?.length).toBe(2);
    expect(servicio.filtros()?.borradores.length).toBe(0);
  });
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `cd apps/web && npx ng test --watch=false`
Expected: FAIL — `Property 'escaneosPorSemana' does not exist on type 'DatosService'` (y lo mismo con `reporte`, `descargaQR`, `asistentes`, `eventosPasados`, `filtros`).

- [ ] **Step 3: Tipar el dataset**

En `apps/web/src/app/datos/modelos.ts`, agregar debajo de `export interface EventoWeb { … }`:

```ts
export interface SemanaEscaneo {
  semana: string;
  etiqueta: string;
  escaneos: number;
  actual?: boolean;
}

export interface Reporte {
  rangos: string[];
  rangoPersonalizado: { desde: string; hasta: string };
  formatos: string[];
  archivoGenerado: string;
  nota: string;
  generados: { archivo: string; fecha: string; formato: string; rango: string }[];
  retencionDias: number;
}

export interface DescargaQR {
  seleccionados: string[];
  formatos: string[];
  afiche: { marca: string; lema: string; qrMinimoCm: number; resolucionPng: number };
}

export interface Asistente {
  alias: string;
  escaneo: string;
  alarma: string;
  confirmoYaVoy?: boolean;
  yaVoy?: boolean;
}

export interface Asistentes {
  eventoId: string;
  total: number;
  mostrados: Asistente[];
  notaPrivacidad: string;
  pagina2: Asistente[];
  porPagina: number;
  busquedaEjemplo: { consulta: string; resultados: string[] };
}

export interface EventoPasado {
  nombre: string;
  fechaHora: string;
  origen: string;
  escaneos: number;
  alarmasActivas: number;
  estado: string;
}

export interface Filtros {
  estado: string[];
  origen: string[];
  borradores: unknown[];
  busquedaEjemplo: { consulta: string; resultados: string[] };
}
```

Reemplazar la interfaz `DatosWeb` completa por:

```ts
export interface DatosWeb {
  indicadores: {
    eventosActivos: Indicador;
    escaneosTotales: Indicador;
    alarmasActivas: Indicador;
    confirmaronYaVoy: Indicador;
  };
  eventos: EventoWeb[];
  eliminarCuenta: {
    eventosPublicados: number;
    alarmasDeAsistentes: number;
    diasParaBorrado: number;
    palabraDeConfirmacion: string;
    consejo: string;
  };
  acceso: Acceso;
  barraLateral: BarraLateralDatos;
  escaneosPorSemana: SemanaEscaneo[];
  reporte: Reporte;
  descargaQR: DescargaQR;
  asistentes: Asistentes;
  eventosPasados: EventoPasado[];
  filtros: Filtros;
}
```

En `apps/web/src/app/datos/datos.service.ts`, agregar debajo de `readonly eliminarCuenta = …`:

```ts
  readonly escaneosPorSemana = computed(() => this.dataset.value()?.web.escaneosPorSemana);
  readonly reporte = computed(() => this.dataset.value()?.web.reporte);
  readonly descargaQR = computed(() => this.dataset.value()?.web.descargaQR);
  readonly asistentes = computed(() => this.dataset.value()?.web.asistentes);
  readonly eventosPasados = computed(() => this.dataset.value()?.web.eventosPasados);
  readonly filtros = computed(() => this.dataset.value()?.web.filtros);
```

- [ ] **Step 4: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 5: Tokens v1.13 en `design-tokens.json`**

En `packages/tokens/design-tokens.json`:

1. Cabecera: `"version": "1.12"` → `"version": "1.13"` (las dos apariciones: la de `meta` y la del cierre del archivo), `"date": "2026-09-21"` se mantiene.
2. Al final del arreglo `meta.notes`, agregar tras la nota v1.12:

```json
      "v1.13 (2026-09-21): medidas de tablero web de la Persona B (Plan 5) — campo de búsqueda (240/260×40), paginador (píldora 30), gráfica de barras (alto máximo 96) y vista previa del afiche (miniatura 132, QR 72); radius.casilla se deriva ahora también a la web (checkbox de W05)."
```

3. Dentro de `size.web` (objeto que ya tiene `logo-acceso`, `avatar`, …), agregar antes de `"$description"`:

```json
      "campo-busqueda-w01": 240,
      "campo-busqueda-w03": 260,
      "campo-busqueda-alto": 40,
      "icono-lupa": 16,
      "paginador-pildora": 30,
      "grafica-barra-alto-max": 96,
      "afiche-miniatura": 132,
      "afiche-qr": 72,
      "checkbox": 20,
      "enlace-tabla": [100, 20],
```

4. Dentro de `font.scale`, agregar (junto a las demás entradas `*-web`):

```json
      "titulo-grafica-web": { "family": "ui", "weight": 600, "size": 14.5 },
      "nota-web": { "family": "ui", "weight": 400, "size": 13 },
      "valor-grafica": { "family": "datos", "weight": 700, "size": 12 },
      "etiqueta-semana": { "family": "datos", "weight": 500, "size": 11 },
      "titulo-afiche": { "family": "titulares", "weight": 700, "size": 10.5 },
      "lema-afiche": { "family": "ui", "weight": 400, "size": 8.5 },
      "marca-afiche": { "family": "ui", "weight": 700, "size": 9.5 },
      "rotulo-afiche": { "family": "ui", "weight": 700, "size": 11 },
```

5. Dentro de `space.web`, agregar junto a `"tarjeta-peligro": 20,`:

```json
      "estado-vacio": [16, 24],
```

   Y en `space.escala` (arreglo `[2, 4, 8, 10, 12, 16]`), agregar `20` al final: `[2, 4, 8, 10, 12, 16, 20]`.

Comprobar que el JSON sigue siendo válido:

```bash
python3 -c "import json;d=json.load(open('packages/tokens/design-tokens.json'));print(d['meta']['version'], d['size']['web']['paginador-pildora'], d['font']['scale']['valor-grafica'], d['space']['escala'])"
```

Expected: `1.13 30 {'family': 'datos', 'weight': 700, 'size': 12} [2, 4, 8, 10, 12, 16, 20]`.

- [ ] **Step 6: Tokens v1.13 en los dos `tokens.css`**

En `packages/tokens/tokens.css`:

1. Primera línea: `tokens v1.12 (2026-09-21)` → `tokens v1.13 (2026-09-21)`.
2. Insertar, justo antes de la sección `/* cortes de ancho web (v1.12): …`:

```css
  /* web · tablero de la Persona B (Plan 5, v1.13) */
  --size-campo-busqueda-w01: 240px;
  --size-campo-busqueda-w03: 260px;
  --size-campo-busqueda-alto: 40px;
  --size-icono-lupa: 16px;
  --size-paginador-pildora: 30px;
  --size-grafica-barra-alto-max: 96px;
  --size-afiche-miniatura: 132px;
  --size-afiche-qr: 72px;
  --size-checkbox: 20px;
  --size-enlace-tabla-w: 100px;
  --size-enlace-tabla-h: 20px;
  --radius-casilla: 4px;
  --text-titulo-grafica-web: 600 14.5px/normal var(--font-ui);
  --text-nota-web: 400 13px/normal var(--font-ui);
  --text-valor-grafica: 700 12px/1 var(--font-datos);
  --text-etiqueta-semana: 500 11px/1 var(--font-datos);
  --text-titulo-afiche: 700 10.5px/normal var(--font-titulares);
  --text-lema-afiche: 400 8.5px/normal var(--font-ui);
  --text-marca-afiche: 700 9.5px/normal var(--font-ui);
  --text-rotulo-afiche: 700 11px/normal var(--font-ui);
  --space-web-estado-vacio: 16px 24px;
  --space-20: 20px;

```

Copiar a la web y comprobar:

```bash
cp packages/tokens/tokens.css apps/web/src/tokens.css
cmp packages/tokens/tokens.css apps/web/src/tokens.css && echo IDENTICOS
```

Expected: `IDENTICOS`.

- [ ] **Step 7: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add packages/tokens apps/web/src/app/datos apps/web/src/tokens.css apps/web/src/app/datos/datos.service.spec.ts
git commit -m "L09: tokens v1.13 del tablero y tipado del dataset de la Persona B"
```

Expected: pruebas, lint y build en verde; commit creado sin línea de coautoría.

---

### Task 2: Componentes L09 — Indicador, Chip, Píldoras y Campo de búsqueda

**Files:**
- Create: `apps/web/src/app/componentes/indicador/aq-indicador.component.ts`
- Create: `apps/web/src/app/componentes/chip/aq-chip.component.ts`
- Create: `apps/web/src/app/componentes/pildoras/aq-pildoras.component.ts`
- Create: `apps/web/src/app/componentes/campo-busqueda/aq-campo-busqueda.component.ts`
- Modify: `apps/web/src/app/componentes/icono/iconos.ts` (icono `lupa`)
- Test: `apps/web/src/app/componentes/componentes-tablero-1.spec.ts`

**Interfaces:**
- Consumes: tokens de la Tarea 1, `proveedoresPrueba()`.
- Produces:
  - `<aq-indicador [valor]="128" etiqueta="Escaneos totales" detalle="De tus eventos propios" />`: `AqIndicadorComponent` con `valor`, `etiqueta`, `detalle` como `input.required`.
  - `<aq-chip variante="creada-por-mi">Creada por mí</aq-chip>`: `AqChipComponent` con `variante: input.required<VarianteChip>()`, `VarianteChip = 'creada-por-mi' | 'escaneada' | 'publicado' | 'activa' | 'eliminada'`, contenido proyectado.
  - `<aq-pildoras [opciones]="opts" [activo]="valorActual" (elegir)="cambiar($event)" />`: `AqPildorasComponent`, `opciones: input.required<{ valor: string; texto: string }[]>()`, `activo: input<string>('')`, `elegir: output<string>()`.
  - `<aq-campo-busqueda ancho="w01" placeholder="Buscar por nombre" [value]="q()" (buscar)="onBuscar($event)" />`: `AqCampoBusquedaComponent`, `ancho: input<'w01' | 'w03'>('w01')`, `placeholder: input('Buscar por nombre')`, `value = model('')`, `buscar: output<string>()` (emite al presionar Enter, D6).
  - `NombreIcono` gana `'lupa'`.

- [ ] **Step 1: Escribir las pruebas (fallan: los componentes no existen)**

`apps/web/src/app/componentes/componentes-tablero-1.spec.ts`:

```ts
import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqIndicadorComponent } from './indicador/aq-indicador.component';
import { AqChipComponent } from './chip/aq-chip.component';
import { AqPildorasComponent } from './pildoras/aq-pildoras.component';
import { AqCampoBusquedaComponent } from './campo-busqueda/aq-campo-busqueda.component';

@Component({
  imports: [AqIndicadorComponent, AqChipComponent, AqPildorasComponent, AqCampoBusquedaComponent],
  template: `
    <aq-indicador id="ind" [valor]="128" etiqueta="Escaneos totales" detalle="De tus eventos propios" />
    <aq-chip id="chip-origen" variante="creada-por-mi">Creada por mí</aq-chip>
    <aq-chip id="chip-estado" variante="eliminada">Eliminada</aq-chip>
    <aq-pildoras
      id="pildoras"
      [opciones]="opciones"
      [activo]="activo()"
      (elegir)="activo.set($event)"
    />
    <aq-campo-busqueda
      id="busqueda"
      ancho="w03"
      placeholder="Buscar asistente"
      [(value)]="texto"
      (buscar)="buscados.push($event)"
    />
  `,
})
class Anfitrion {
  readonly opciones = [
    { valor: 'todos', texto: 'Todos' },
    { valor: 'creados', texto: 'Creados' },
    { valor: 'escaneados', texto: 'Escaneados' },
  ];
  readonly activo = signal('todos');
  readonly texto = signal('');
  readonly buscados: string[] = [];
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes de tablero L09 (1/2)', () => {
  it('aq-indicador muestra valor, etiqueta y detalle', async () => {
    const f = await montar();
    const ind = (f.nativeElement as HTMLElement).querySelector('#ind')!;
    expect(ind.textContent).toContain('128');
    expect(ind.textContent).toContain('Escaneos totales');
    expect(ind.textContent).toContain('De tus eventos propios');
  });

  it('aq-chip aplica la variante de origen y de estado', async () => {
    const f = await montar();
    const raiz = f.nativeElement as HTMLElement;
    expect(raiz.querySelector('#chip-origen')!.classList).toContain('creada-por-mi');
    expect(raiz.querySelector('#chip-estado')!.classList).toContain('eliminada');
    expect(raiz.querySelector('#chip-origen')!.textContent?.trim()).toBe('Creada por mí');
  });

  it('aq-pildoras marca la activa y avisa al elegir otra', async () => {
    const f = await montar();
    const raiz = f.nativeElement as HTMLElement;
    const botones = Array.from(raiz.querySelectorAll('#pildoras button'));
    expect(botones.find((b) => b.textContent?.trim() === 'Todos')!.getAttribute('aria-pressed')).toBe(
      'true',
    );
    (botones.find((b) => b.textContent?.trim() === 'Creados') as HTMLElement).click();
    await f.whenStable();
    expect(f.componentInstance.activo()).toBe('creados');
  });

  it('aq-campo-busqueda refleja el texto y solo avisa al presionar Enter (D6)', async () => {
    const f = await montar();
    const entrada = (f.nativeElement as HTMLElement).querySelector('#busqueda input') as HTMLInputElement;
    expect(entrada.placeholder).toBe('Buscar asistente');
    entrada.value = 'Mi';
    entrada.dispatchEvent(new Event('input'));
    await f.whenStable();
    expect(f.componentInstance.texto()).toBe('Mi');
    expect(f.componentInstance.buscados).toEqual([]);
    entrada.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));
    expect(f.componentInstance.buscados).toEqual(['Mi']);
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL — no se resuelven los imports de los cuatro componentes.

- [ ] **Step 3: Icono «lupa»**

En `apps/web/src/app/componentes/icono/iconos.ts`, agregar `'lupa'` al tipo `NombreIcono` y al `Record`:

```ts
  | 'lupa';
```

(al final de la unión, antes del `;`) y, dentro de `ICONOS`, antes del cierre `};`:

```ts
  lupa: {
    caja: 16,
    trazo: T16,
    formas: [
      {
        d: 'M7.33333 12.6667C10.2789 12.6667 12.6667 10.2789 12.6667 7.33333C12.6667 4.38781 10.2789 2 7.33333 2C4.38781 2 2 4.38781 2 7.33333C2 10.2789 4.38781 12.6667 7.33333 12.6667Z',
      },
      { d: 'M14 14L11.1 11.1' },
    ],
  },
```

- [ ] **Step 4: `aq-indicador`**

`apps/web/src/app/componentes/indicador/aq-indicador.component.ts`:

```ts
import { Component, input } from '@angular/core';

/** Indicador de tablero (DS §7 «Indicador»): 242×104, borde Gris Borde, radio 14, relleno 14/18. */
@Component({
  selector: 'aq-indicador',
  template: `
    <span class="valor">{{ valor() }}</span>
    <span class="etiqueta">{{ etiqueta() }}</span>
    <span class="detalle">{{ detalle() }}</span>
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-4);
      box-sizing: border-box;
      width: var(--size-indicador-web-w);
      height: var(--size-indicador-web-h);
      padding: var(--space-web-indicadores) 18px;
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
      background: var(--color-blanco);
    }
    .valor {
      font: var(--text-indicador-web);
      color: var(--color-texto);
    }
    .etiqueta {
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    .detalle {
      font: var(--text-nota);
      color: var(--color-texto-secundario);
    }
  `,
})
export class AqIndicadorComponent {
  readonly valor = input.required<number | string>();
  readonly etiqueta = input.required<string>();
  readonly detalle = input.required<string>();
}
```

Nota: `18px` de relleno horizontal reutiliza la medida ya usada por otras tarjetas; si se prefiere un token dedicado, agregar `--space-web-indicador-x` en una vuelta futura — no es una medida en px suelta prohibida porque no hay guardia sobre valores dentro de `padding` compuesto con variables... **corrección:** `verificar-tokens.mjs` sí detecta cualquier `\d+px` literal en cualquier posición. Reemplazar `18px` por una variable: usar `var(--space-web-indicadores)` en ambos ejes (14/18 se redondea a 14/14, aceptable porque no hay token de 18 dedicado) — ver Step 4b.

- [ ] **Step 4b: Ajustar el relleno de `aq-indicador` para no violar la guardia de tokens**

Reemplazar en el `styles` de `aq-indicador.component.ts`:

```css
      padding: var(--space-web-indicadores) 18px;
```

por

```css
      padding: var(--space-web-indicadores) var(--space-16);
```

(14 vertical / 16 horizontal, la medida existente más cercana a los 18 del DS; anotar como divergencia menor en «Pendiente en el repo de UX» si se quiere el 18 exacto con un token nuevo).

- [ ] **Step 5: `aq-chip`**

`apps/web/src/app/componentes/chip/aq-chip.component.ts`:

```ts
import { Component, input } from '@angular/core';

export type VarianteChip = 'creada-por-mi' | 'escaneada' | 'publicado' | 'activa' | 'eliminada';

/** Chip web (DS §7 «Chips»): 19–20 de alto, píldora, Archivo Bold 12. Origen: contorno; estado: relleno + contorno (o solo contorno en «Eliminada»). */
@Component({
  selector: 'aq-chip',
  template: `<ng-content />`,
  host: {
    '[class.creada-por-mi]': "variante() === 'creada-por-mi'",
    '[class.escaneada]': "variante() === 'escaneada'",
    '[class.publicado]': "variante() === 'publicado'",
    '[class.activa]': "variante() === 'activa'",
    '[class.eliminada]': "variante() === 'eliminada'",
  },
  styles: `
    :host {
      display: inline-flex;
      align-items: center;
      box-sizing: border-box;
      height: var(--size-chip);
      padding: var(--space-2) var(--space-12);
      border: var(--stroke-borde) solid transparent;
      border-radius: var(--radius-pildora);
      font: var(--text-chip);
      white-space: nowrap;
    }
    :host(.creada-por-mi) {
      background: var(--color-blanco);
      border-color: var(--color-tinta);
      color: var(--color-tinta);
    }
    :host(.escaneada) {
      background: var(--color-blanco);
      border-color: var(--color-verde-texto);
      color: var(--color-verde-texto);
    }
    :host(.publicado),
    :host(.activa) {
      background: var(--color-verde-fondo);
      border-color: var(--color-verde-texto);
      color: var(--color-verde-texto);
    }
    :host(.eliminada) {
      background: var(--color-blanco);
      border-color: var(--color-destructivo);
      color: var(--color-destructivo);
    }
  `,
})
export class AqChipComponent {
  readonly variante = input.required<VarianteChip>();
}
```

- [ ] **Step 6: `aq-pildoras`**

`apps/web/src/app/componentes/pildoras/aq-pildoras.component.ts`:

```ts
import { Component, input, output } from '@angular/core';

export interface OpcionPildora {
  valor: string;
  texto: string;
}

/** Grupo de píldoras de filtro o pestaña (DS §7): 34 de alto, activa Tinta con texto blanco, inactiva Gris Niebla. No es un control de formulario: la página decide qué hacer al elegir (normalmente navegar con un query param). */
@Component({
  selector: 'aq-pildoras',
  template: `
    <div class="grupo" role="group">
      @for (opcion of opciones(); track opcion.valor) {
        <button
          type="button"
          class="pildora"
          [class.activa]="opcion.valor === activo()"
          [attr.aria-pressed]="opcion.valor === activo()"
          (click)="elegir.emit(opcion.valor)"
        >
          {{ opcion.texto }}
        </button>
      }
    </div>
  `,
  styles: `
    .grupo {
      display: flex;
      gap: var(--space-10);
      flex-wrap: wrap;
    }
    .pildora {
      display: inline-flex;
      align-items: center;
      box-sizing: border-box;
      height: var(--size-pildora-filtro);
      padding: 0 var(--space-14, var(--space-16));
      border: none;
      border-radius: var(--radius-pildora);
      background: var(--color-gris-niebla);
      color: var(--color-texto-secundario);
      font: var(--text-pildora-web);
      cursor: pointer;
      transition: background-color var(--motion-transicion), color var(--motion-transicion);
    }
    .pildora.activa {
      background: var(--color-tinta);
      color: var(--color-blanco);
    }
    .pildora:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
  `,
})
export class AqPildorasComponent {
  readonly opciones = input.required<readonly OpcionPildora[]>();
  readonly activo = input<string>('');
  readonly elegir = output<string>();
}
```

`--space-14` no existe como token (la escala salta de 12 a 16); usar directamente `var(--space-16)` en vez del `var(--space-14, var(--space-16))` con fallback — **corrección aplicada en el Step 6b**.

- [ ] **Step 6b: Corregir el relleno horizontal de la píldora**

Reemplazar en `aq-pildoras.component.ts`:

```css
      padding: 0 var(--space-14, var(--space-16));
```

por

```css
      padding: 0 var(--space-16);
```

(el DS pide 14; se usa el token existente más cercano, 16 — mismo criterio que el Step 4b. Anotar en «Pendiente en el repo de UX» si se quiere el 14 exacto.)

- [ ] **Step 7: `aq-campo-busqueda`**

`apps/web/src/app/componentes/campo-busqueda/aq-campo-busqueda.component.ts`:

```ts
import { Component, computed, input, model, output } from '@angular/core';
import { AqIconoComponent } from '../icono/aq-icono.component';

/** Campo de búsqueda (DS §7): 240×40 (W01) o 260×40 (W03), lupa a la izquierda, un solo marcador. Navega/filtra solo al presionar Enter (Plan 5 D6). */
@Component({
  selector: 'aq-campo-busqueda',
  imports: [AqIconoComponent],
  template: `
    <label class="caja">
      <aq-icono nombre="lupa" tamano="vineta" />
      <input
        type="search"
        [value]="value()"
        [attr.placeholder]="placeholder()"
        (input)="value.set($any($event.target).value)"
        (keydown.enter)="buscar.emit(value())"
      />
    </label>
  `,
  host: { '[class.w03]': "ancho() === 'w03'" },
  styles: `
    .caja {
      display: inline-flex;
      align-items: center;
      gap: var(--space-8);
      box-sizing: border-box;
      width: var(--size-campo-busqueda-w01);
      height: var(--size-campo-busqueda-alto);
      padding: 0 var(--space-12);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-pildora);
      background: var(--color-blanco);
      color: var(--color-texto-secundario);
      cursor: text;
      transition: border-color var(--motion-transicion);
    }
    :host(.w03) .caja {
      width: var(--size-campo-busqueda-w03);
    }
    .caja:focus-within {
      border-color: var(--color-tinta);
      color: var(--color-tinta);
    }
    input {
      width: 100%;
      border: none;
      outline: none;
      background: transparent;
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    input::placeholder {
      color: var(--color-gris-medio);
    }
    input[type='search']::-webkit-search-cancel-button {
      display: none;
    }
  `,
})
export class AqCampoBusquedaComponent {
  readonly value = model('');
  readonly ancho = input<'w01' | 'w03'>('w01');
  readonly placeholder = input('Buscar por nombre');
  readonly buscar = output<string>();
  protected readonly icono = computed(() => 'lupa' as const);
}
```

(El `computed` `icono` no se usa en la plantilla — quitarlo: ver Step 7b.)

- [ ] **Step 7b: Quitar el `computed` sin uso**

En `aq-campo-busqueda.component.ts`, borrar la línea `protected readonly icono = computed(() => 'lupa' as const);` y el import de `computed` si queda sin uso (sigue haciendo falta `input`, `model`, `output`, `Component`).

- [ ] **Step 8: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 9: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/componentes
git commit -m "L09: indicador, chip, píldoras y campo de búsqueda"
```

---

### Task 3: Componentes L09 — Tabla, Estado vacío y Paginador

**Files:**
- Create: `apps/web/src/app/componentes/tabla/aq-tabla.component.ts`
- Create: `apps/web/src/app/componentes/estado-vacio/aq-estado-vacio.component.ts`
- Create: `apps/web/src/app/componentes/paginador/aq-paginador.component.ts`
- Test: `apps/web/src/app/componentes/componentes-tablero-2.spec.ts`

**Interfaces:**
- Consumes: tokens de la Tarea 1.
- Produces:
  - `<table aq-tabla>…</table>` (D2): estiliza `thead th` (26, borde inferior Tinta, mayúsculas 11 Bold Gris Texto) y `tbody tr` (48, borde Gris Borde) del contenido proyectado por la página.
  - `<aq-estado-vacio titulo="Sin resultados con estos filtros" [texto]="explicacion"><a aq-boton variante="secundario" …>Ver todos</a></aq-estado-vacio>`: `AqEstadoVacioComponent` con `titulo`, `texto` como `input.required<string>()`, contenido proyectado para la acción.
  - `<aq-paginador texto="Mostrando 1–4 de 16 asistentes · 4 por página" [paginaActual]="1" [totalPaginas]="2" (cambiar)="irA($event)" />`: `AqPaginadorComponent`, `texto: input.required<string>()`, `paginaActual: input.required<number>()`, `totalPaginas: input.required<number>()`, `cambiar: output<number>()`.

- [ ] **Step 1: Escribir las pruebas (fallan: los componentes no existen)**

`apps/web/src/app/componentes/componentes-tablero-2.spec.ts`:

```ts
import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqTablaComponent } from './tabla/aq-tabla.component';
import { AqEstadoVacioComponent } from './estado-vacio/aq-estado-vacio.component';
import { AqPaginadorComponent } from './paginador/aq-paginador.component';

@Component({
  imports: [AqTablaComponent, AqEstadoVacioComponent, AqPaginadorComponent],
  template: `
    <table aq-tabla id="tabla">
      <thead>
        <tr><th>Evento</th><th>Escaneos</th></tr>
      </thead>
      <tbody>
        <tr><td>Seminario UX</td><td>12</td></tr>
      </tbody>
    </table>
    <aq-estado-vacio id="vacio" titulo="Sin resultados con estos filtros" texto="No tienes borradores.">
      <button type="button" class="accion">Ver todos</button>
    </aq-estado-vacio>
    <aq-paginador
      id="pag"
      texto="Mostrando 1–4 de 16 asistentes · 4 por página"
      [paginaActual]="1"
      [totalPaginas]="2"
      (cambiar)="paginas.push($event)"
    />
  `,
})
class Anfitrion {
  readonly paginas: number[] = [];
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes de tablero L09 (2/2)', () => {
  it('aq-tabla no altera el contenido proyectado (solo estilo)', async () => {
    const f = await montar();
    const tabla = (f.nativeElement as HTMLElement).querySelector('#tabla')!;
    expect(tabla.querySelectorAll('th').length).toBe(2);
    expect(tabla.querySelector('td')!.textContent).toBe('Seminario UX');
  });

  it('aq-estado-vacio muestra título, texto y proyecta la acción', async () => {
    const f = await montar();
    const vacio = (f.nativeElement as HTMLElement).querySelector('#vacio')!;
    expect(vacio.textContent).toContain('Sin resultados con estos filtros');
    expect(vacio.textContent).toContain('No tienes borradores.');
    expect(vacio.querySelector('.accion')).not.toBeNull();
  });

  it('aq-paginador muestra el texto, las páginas y avisa al cambiar', async () => {
    const f = await montar();
    const raiz = f.nativeElement as HTMLElement;
    const pag = raiz.querySelector('#pag')!;
    expect(pag.textContent).toContain('Mostrando 1–4 de 16 asistentes · 4 por página');
    const activa = pag.querySelector('[aria-current="true"]')!;
    expect(activa.textContent?.trim()).toBe('1');
    (Array.from(pag.querySelectorAll('button')).find((b) => b.textContent?.trim() === '2') as HTMLElement).click();
    await f.whenStable();
    expect(f.componentInstance.paginas).toEqual([2]);
    (pag.querySelector('[data-siguiente]') as HTMLElement).click();
    expect(f.componentInstance.paginas).toEqual([2, 2]);
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL — no se resuelven los tres imports.

- [ ] **Step 3: `aq-tabla` (D2)**

`apps/web/src/app/componentes/tabla/aq-tabla.component.ts`:

```ts
import { Component, ViewEncapsulation } from '@angular/core';

/**
 * Tabla de datos (DS §7 «Tabla de datos»): encabezado 26 con borde inferior Tinta y rótulos mayúsculas
 * 11 Bold Gris Texto; filas de 48 separadas por líneas Gris Borde. Cada página arma su propio
 * `<thead>`/`<tbody>` (las columnas difieren entre W01 y W03), así que este componente no envuelve nada:
 * solo aporta estilo. `ViewEncapsulation.None` + selectores prefijados por `table[aq-tabla]` (D2) en vez
 * de `::ng-deep` (deprecado) para no perder el aislamiento del resto de la app.
 */
@Component({
  selector: 'table[aq-tabla]',
  template: `<ng-content />`,
  encapsulation: ViewEncapsulation.None,
  styles: `
    table[aq-tabla] {
      width: 100%;
      border-collapse: collapse;
    }
    table[aq-tabla] thead th {
      box-sizing: border-box;
      height: var(--size-tabla-encabezado);
      padding: 0 var(--space-12);
      border-bottom: var(--stroke-borde) solid var(--color-tinta);
      font: var(--text-rotulo-tabla);
      letter-spacing: 0.06em;
      text-transform: uppercase;
      color: var(--color-texto-secundario);
      text-align: left;
    }
    table[aq-tabla] tbody tr {
      height: var(--size-tabla-fila);
      border-bottom: var(--stroke-borde-fino) solid var(--color-borde);
    }
    table[aq-tabla] tbody td {
      box-sizing: border-box;
      padding: var(--space-12) var(--space-12) var(--space-12) 0;
      font: var(--text-tabla-celda);
      color: var(--color-texto);
    }
    table[aq-tabla] tbody td:first-child {
      padding-left: var(--space-12);
    }
  `,
})
export class AqTablaComponent {}
```

- [ ] **Step 4: `aq-estado-vacio`**

`apps/web/src/app/componentes/estado-vacio/aq-estado-vacio.component.ts`:

```ts
import { Component, input } from '@angular/core';

/** Estado vacío de tabla (DS §7): bloque Gris Niebla de ancho completo, radio 12, relleno 16/24, centrado. */
@Component({
  selector: 'aq-estado-vacio',
  template: `
    <h3 class="titulo">{{ titulo() }}</h3>
    <p class="texto">{{ texto() }}</p>
    <ng-content />
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: var(--space-12);
      box-sizing: border-box;
      width: 100%;
      padding: var(--space-web-estado-vacio);
      background: var(--color-gris-niebla);
      border-radius: var(--radius-campo);
      text-align: center;
    }
    .titulo,
    .texto {
      margin: 0;
    }
    .titulo {
      font: var(--text-titulo-tarjeta-web);
      font-size: 16px;
      color: var(--color-texto);
    }
    .texto {
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class AqEstadoVacioComponent {
  readonly titulo = input.required<string>();
  readonly texto = input.required<string>();
}
```

`font-size: 16px` en `.titulo` es una medida a mano prohibida por la guardia — corregido en el Step 4b.

- [ ] **Step 4b: Quitar la medida a mano del título**

`--text-titulo-tarjeta-web` ya es `700 17px` (Bricolage), no `16` (Archivo SemiBold, DS pide «Archivo SemiBold 16»). En vez de forzar un tamaño suelto, usar `--text-destacado` (`700 16px/1.4 var(--font-ui)`, ya existe en tokens.css y coincide en tamaño y familia). Reemplazar en `aq-estado-vacio.component.ts`:

```css
    .titulo {
      font: var(--text-titulo-tarjeta-web);
      font-size: 16px;
      color: var(--color-texto);
    }
```

por

```css
    .titulo {
      font: var(--text-destacado);
      color: var(--color-texto);
    }
```

- [ ] **Step 5: `aq-paginador`**

`apps/web/src/app/componentes/paginador/aq-paginador.component.ts`:

```ts
import { Component, computed, input, output } from '@angular/core';

/** Paginador (DS §7): pie «Mostrando…» + píldoras de 30×30 (‹ 1 2 3 4 ›); activa en Tinta, inactivas con contorno Gris Borde. */
@Component({
  selector: 'aq-paginador',
  template: `
    <p class="texto">{{ texto() }}</p>
    <nav class="paginas" aria-label="Paginación">
      <button
        type="button"
        class="pildora"
        data-anterior
        [disabled]="paginaActual() <= 1"
        (click)="cambiar.emit(paginaActual() - 1)"
      >
        ‹
      </button>
      @for (pagina of paginas(); track pagina) {
        <button
          type="button"
          class="pildora"
          [class.activa]="pagina === paginaActual()"
          [attr.aria-current]="pagina === paginaActual() ? 'true' : null"
          (click)="cambiar.emit(pagina)"
        >
          {{ pagina }}
        </button>
      }
      <button
        type="button"
        class="pildora"
        data-siguiente
        [disabled]="paginaActual() >= totalPaginas()"
        (click)="cambiar.emit(paginaActual() + 1)"
      >
        ›
      </button>
    </nav>
  `,
  styles: `
    :host {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
      padding-top: var(--space-12);
    }
    .texto {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .paginas {
      display: flex;
      gap: var(--space-4);
    }
    .pildora {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      width: var(--size-paginador-pildora);
      height: var(--size-paginador-pildora);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-pildora);
      background: var(--color-blanco);
      color: var(--color-texto);
      font: var(--text-pildora-web);
      cursor: pointer;
    }
    .pildora.activa {
      background: var(--color-tinta);
      border-color: var(--color-tinta);
      color: var(--color-blanco);
    }
    .pildora:disabled {
      opacity: var(--opacity-deshabilitado);
      cursor: not-allowed;
    }
    .pildora:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
  `,
})
export class AqPaginadorComponent {
  readonly texto = input.required<string>();
  readonly paginaActual = input.required<number>();
  readonly totalPaginas = input.required<number>();
  readonly cambiar = output<number>();
  protected readonly paginas = computed(() =>
    Array.from({ length: this.totalPaginas() }, (_, i) => i + 1),
  );
}
```

- [ ] **Step 6: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 7: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/componentes
git commit -m "L09: tabla de datos, estado vacío y paginador"
```

---

### Task 4: Componentes L09 — Gráfica de barras y Vista previa del afiche

**Files:**
- Create: `apps/web/src/app/componentes/grafica-barras/aq-grafica-barras.component.ts`
- Create: `apps/web/src/app/componentes/vista-previa-afiche/aq-vista-previa-afiche.component.ts`
- Test: `apps/web/src/app/componentes/componentes-tablero-3.spec.ts`

**Interfaces:**
- Consumes: `SemanaEscaneo` de la Tarea 1, `AqIconoComponent` (icono `qr`).
- Produces:
  - `<aq-grafica-barras [datos]="escaneosPorSemana()!" />`: `AqGraficaBarrasComponent`, `datos: input.required<readonly SemanaEscaneo[]>()`, `titulo: input('Escaneos por semana')`, `nota: input('Datos agregados y anónimos')`.
  - `<aq-vista-previa-afiche [nombreEvento]="'Seminario UX'" />`: `AqVistaPreviaAficheComponent`, `nombreEvento: input.required<string>()`.

- [ ] **Step 1: Escribir las pruebas (fallan: los componentes no existen)**

`apps/web/src/app/componentes/componentes-tablero-3.spec.ts`:

```ts
import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqGraficaBarrasComponent } from './grafica-barras/aq-grafica-barras.component';
import { AqVistaPreviaAficheComponent } from './vista-previa-afiche/aq-vista-previa-afiche.component';
import dataset from '../../../public/dataset.json';

@Component({
  imports: [AqGraficaBarrasComponent, AqVistaPreviaAficheComponent],
  template: `
    <aq-grafica-barras id="grafica" [datos]="datos" />
    <aq-vista-previa-afiche id="afiche" nombreEvento="Seminario UX" />
  `,
})
class Anfitrion {
  readonly datos = dataset.web.escaneosPorSemana;
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes de tablero L09 (3/3)', () => {
  it('aq-grafica-barras dibuja una barra por semana con su etiqueta y la semana actual resaltada', async () => {
    const f = await montar();
    const grafica = (f.nativeElement as HTMLElement).querySelector('#grafica')!;
    expect(grafica.textContent).toContain('Escaneos por semana');
    expect(grafica.querySelectorAll('.columna').length).toBe(8);
    expect(grafica.textContent).toContain('24 ago');
    const actual = grafica.querySelector('.columna.actual')!;
    expect(actual.textContent).toContain('23');
    expect(grafica.textContent).toContain('Datos agregados y anónimos');
  });

  it('la barra más alta corresponde al valor máximo (24 ago, 23 escaneos)', async () => {
    const f = await montar();
    const barras = Array.from(
      (f.nativeElement as HTMLElement).querySelectorAll('#grafica .barra'),
    ) as SVGRectElement[];
    const alturas = barras.map((b) => Number(b.getAttribute('height')));
    expect(Math.max(...alturas)).toBe(alturas[alturas.length - 1]);
  });

  it('aq-vista-previa-afiche muestra la marca, el nombre del evento y la regla de tamaño', async () => {
    const f = await montar();
    const afiche = (f.nativeElement as HTMLElement).querySelector('#afiche')!;
    expect(afiche.textContent).toContain('Alarmas QR');
    expect(afiche.textContent).toContain('Seminario UX');
    expect(afiche.textContent).toContain('Escaneálo y te avisamos'.length > 0 ? 'Escanéalo y te avisamos' : '');
    expect(afiche.textContent).toContain('VISTA PREVIA DEL AFICHE');
    expect(afiche.textContent).toContain('QR mínimo 4 × 4 cm');
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL — no se resuelven los dos imports.

- [ ] **Step 3: `aq-grafica-barras`**

`apps/web/src/app/componentes/grafica-barras/aq-grafica-barras.component.ts`:

```ts
import { Component, computed, input } from '@angular/core';
import { SemanaEscaneo } from '../../datos/modelos';

interface ColumnaGrafica extends SemanaEscaneo {
  alturaPx: number;
}

/** Gráfica de barras «Escaneos por semana» (DS §7): 8 columnas en FILL, barra radio 4, altura máx. 96; semana actual en Tinta, el resto en Gris Texto. */
@Component({
  selector: 'aq-grafica-barras',
  template: `
    <header class="cabecera">
      <h3 class="titulo">{{ titulo() }}</h3>
      <span class="periodo">Últimas {{ datos().length }} semanas</span>
    </header>
    <svg class="lienzo" [attr.viewBox]="'0 0 ' + (columnas().length * 40) + ' 130'" role="img" aria-label="{{ titulo() }}">
      @for (columna of columnas(); track columna.semana; let i = $index) {
        <g
          class="columna"
          [class.actual]="!!columna.actual"
          [attr.transform]="'translate(' + i * 40 + ',0)'"
        >
          <text class="valor" x="20" [attr.y]="94 - columna.alturaPx" text-anchor="middle">
            {{ columna.escaneos }}
          </text>
          <rect
            class="barra"
            x="6"
            [attr.y]="96 - columna.alturaPx"
            width="28"
            [attr.height]="columna.alturaPx"
            rx="4"
          />
          <text class="etiqueta" x="20" y="112" text-anchor="middle">{{ columna.etiqueta }}</text>
        </g>
      }
    </svg>
    <p class="pie">{{ nota() }}</p>
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-16);
      box-sizing: border-box;
      padding: 16px var(--space-16);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
      background: var(--color-blanco);
    }
    .cabecera {
      display: flex;
      align-items: baseline;
      justify-content: space-between;
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-grafica-web);
      color: var(--color-texto);
    }
    .periodo {
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .lienzo {
      width: 100%;
      height: auto;
    }
    .valor {
      font: var(--text-valor-grafica);
      fill: var(--color-texto);
    }
    .etiqueta {
      font: var(--text-etiqueta-semana);
      fill: var(--color-texto-secundario);
    }
    .columna .barra {
      fill: var(--color-texto-secundario);
    }
    .columna.actual .barra {
      fill: var(--color-tinta);
    }
    .columna.actual .etiqueta {
      fill: var(--color-tinta);
    }
    .pie {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class AqGraficaBarrasComponent {
  readonly datos = input.required<readonly SemanaEscaneo[]>();
  readonly titulo = input('Escaneos por semana');
  readonly nota = input('Datos agregados y anónimos');

  protected readonly columnas = computed<ColumnaGrafica[]>(() => {
    const valores = this.datos();
    const maximo = Math.max(1, ...valores.map((v) => v.escaneos));
    return valores.map((v) => ({ ...v, alturaPx: (v.escaneos / maximo) * 96 }));
  });
}
```

`padding: 16px var(--space-16);` mezcla un valor a mano con uno con token — corregido en el Step 3b (la guardia detecta el `16px` literal).

- [ ] **Step 3b: Quitar la medida a mano del relleno**

Reemplazar en `aq-grafica-barras.component.ts`:

```css
      padding: 16px var(--space-16);
```

por

```css
      padding: var(--space-16);
```

- [ ] **Step 4: `aq-vista-previa-afiche`**

`apps/web/src/app/componentes/vista-previa-afiche/aq-vista-previa-afiche.component.ts`:

```ts
import { Component, input } from '@angular/core';
import { AqIconoComponent } from '../icono/aq-icono.component';

/** Vista previa del afiche (DS §7 «Tarjeta de afiche QR»): miniatura 132 (marca Tinta + QR 72 + nombre + lema) y bloque de texto con la regla de tamaño mínimo. */
@Component({
  selector: 'aq-vista-previa-afiche',
  imports: [AqIconoComponent],
  template: `
    <div class="miniatura">
      <div class="marca">
        <span class="punto" aria-hidden="true"></span>
        <span>Alarmas QR</span>
      </div>
      <aq-icono nombre="qr" class="qr" />
      <span class="nombre-evento">{{ nombreEvento() }}</span>
      <span class="lema">Escanéalo y te avisamos</span>
    </div>
    <div class="texto">
      <span class="rotulo">VISTA PREVIA DEL AFICHE</span>
      <p class="explicacion">
        Pieza lista para imprimir con el código QR del evento y la marca de Alarmas QR.
      </p>
      <p class="regla">QR mínimo 4 × 4 cm · PNG a 300 ppp o PDF vectorial</p>
    </div>
  `,
  styles: `
    :host {
      display: flex;
      gap: var(--space-12);
      box-sizing: border-box;
      padding: var(--space-12);
      background: var(--color-gris-niebla);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-campo);
    }
    .miniatura {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: var(--space-8);
      flex: none;
      box-sizing: border-box;
      width: var(--size-afiche-miniatura);
      padding: var(--space-12) var(--space-8);
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-chip-web);
    }
    .marca {
      display: flex;
      align-items: center;
      gap: var(--space-4);
      box-sizing: border-box;
      width: 100%;
      padding: var(--space-4) var(--space-8);
      background: var(--color-tinta);
      border-radius: var(--radius-barra);
      font: var(--text-marca-afiche);
      color: var(--color-blanco);
    }
    .punto {
      width: var(--space-8);
      height: var(--space-8);
      border-radius: var(--radius-pildora);
      background: var(--color-amarillo-energia);
    }
    .qr {
      width: var(--size-afiche-qr);
      height: var(--size-afiche-qr);
      color: var(--color-tinta);
    }
    .nombre-evento {
      font: var(--text-titulo-afiche);
      color: var(--color-texto);
      text-align: center;
    }
    .lema {
      font: var(--text-lema-afiche);
      color: var(--color-texto-secundario);
      text-align: center;
    }
    .texto {
      display: flex;
      flex-direction: column;
      gap: var(--space-8);
      min-width: 0;
    }
    .rotulo {
      font: var(--text-rotulo-afiche);
      letter-spacing: 0.06em;
      color: var(--color-texto-secundario);
    }
    .explicacion {
      margin: 0;
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    .regla {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class AqVistaPreviaAficheComponent {
  readonly nombreEvento = input.required<string>();
}
```

- [ ] **Step 5: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 6: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/componentes
git commit -m "L09: gráfica de barras y vista previa del afiche"
```

---

### Task 5: W01 · Mis Alarmas (hub)

**Files:**
- Modify: `apps/web/src/app/pantallas/w01-mis-alarmas/w01-mis-alarmas.component.ts`
- Test: `apps/web/src/app/pantallas/w01-mis-alarmas/w01-mis-alarmas.component.spec.ts`

**Interfaces:**
- Consumes: `AqIndicadorComponent`, `AqPildorasComponent`, `AqCampoBusquedaComponent`, `AqTablaComponent`, `AqChipComponent`, `AqEstadoVacioComponent`, `AqGraficaBarrasComponent`, `AqBotonComponent`, `AqEnlaceComponent`, `DatosService` (`web`, `eventosPasados`, `filtros`, `escaneosPorSemana`, `mensajes`), `SnackbarService` (D5).
- Produces: `W01MisAlarmasComponent` con `origen = input<string>('todos')`, `estado = input<string>('proximos')`, `q = input<string>('')` (query params, `withComponentInputBinding()`), raíz `data-codigo="W01"`.

- [ ] **Step 1: Escribir la prueba (falla: sigue siendo el marcador)**

Reemplazar `apps/web/src/app/pantallas/w01-mis-alarmas/w01-mis-alarmas.component.spec.ts` (crear si no existe) completo:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

const botonConTexto = (raiz: HTMLElement, texto: string) =>
  Array.from(raiz.querySelectorAll('button, a')).find(
    (b) => b.textContent?.trim() === texto,
  ) as HTMLElement;

describe('W01 · Mis Alarmas', () => {
  it('muestra los cuatro indicadores, la gráfica y los eventos propios y escaneados (pestaña Todos)', async () => {
    const { raiz } = await abrir('/alarmas');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.textContent).toContain(String(dataset.web.indicadores.eventosActivos.valor));
    expect(pagina.textContent).toContain(String(dataset.web.indicadores.escaneosTotales.valor));
    expect(pagina.querySelector('aq-grafica-barras')).not.toBeNull();
    const filas = pagina.querySelectorAll('tbody tr');
    expect(filas.length).toBe(dataset.web.eventos.length);
    expect(pagina.textContent).toContain('Seminario UX');
    expect(pagina.textContent).toContain('Partido Sintética');
  });

  it('pestaña Creados solo muestra eventos creados por mí', async () => {
    const { raiz } = await abrir('/alarmas?origen=creados');
    const filas = raiz.querySelectorAll('[data-codigo="W01"] tbody tr');
    expect(filas.length).toBe(1);
    expect(raiz.querySelector('[data-codigo="W01"]')!.textContent).toContain('Seminario UX');
  });

  it('filtro Pasados muestra los eventos finalizados sin acción «Ver detalle» (D3)', async () => {
    const { raiz } = await abrir('/alarmas?estado=pasados');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.textContent).toContain('Feria de empleo');
    expect(pagina.textContent).toContain('Taller de Figma');
    expect(pagina.querySelectorAll('tbody a[href*="/eventos/"]').length).toBe(0);
  });

  it('filtro Borradores muestra el estado vacío con «Ver todos»', async () => {
    const { raiz, estable, router } = await abrir('/alarmas?estado=borradores');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.querySelector('aq-estado-vacio')).not.toBeNull();
    expect(pagina.textContent).toContain('Sin resultados con estos filtros');
    botonConTexto(raiz, 'Ver todos').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('la búsqueda filtra por título y «Limpiar» vuelve al listado completo', async () => {
    const { raiz, estable, router } = await abrir('/alarmas?q=Sem');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.querySelectorAll('tbody tr').length).toBe(1);
    expect(pagina.textContent).toContain('Seminario UX');
    botonConTexto(raiz, 'Limpiar').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('«Exportar reporte» y «Descargar QR en lote» navegan a W04 y W05', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    botonConTexto(raiz, 'Exportar reporte').click();
    await estable();
    expect(router.url).toBe('/reportes');
    await router.navigateByUrl('/alarmas');
    await estable();
    botonConTexto(raiz, 'Descargar QR en lote').click();
    await estable();
    expect(router.url).toBe('/qr');
  });

  it('«Ver detalle ›» navega a W03 con el id del evento', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    botonConTexto(raiz, 'Ver detalle ›').click();
    await estable();
    expect(router.url).toContain('/eventos/');
  });

  it('?estado=descarga-completada muestra el snackbar de descarga sin filtrar nada (D5)', async () => {
    const { raiz } = await abrir('/alarmas?estado=descarga-completada');
    expect(raiz.querySelectorAll('[data-codigo="W01"] tbody tr').length).toBe(
      dataset.web.eventos.length,
    );
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.descargaCompletada);
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL — el marcador no tiene indicadores, tabla ni botones.

- [ ] **Step 3: Página real**

Reemplazar `apps/web/src/app/pantallas/w01-mis-alarmas/w01-mis-alarmas.component.ts` completo:

```ts
import { Component, computed, effect, inject, input, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AqIndicadorComponent } from '../../componentes/indicador/aq-indicador.component';
import { AqGraficaBarrasComponent } from '../../componentes/grafica-barras/aq-grafica-barras.component';
import { AqPildorasComponent } from '../../componentes/pildoras/aq-pildoras.component';
import { AqCampoBusquedaComponent } from '../../componentes/campo-busqueda/aq-campo-busqueda.component';
import { AqTablaComponent } from '../../componentes/tabla/aq-tabla.component';
import { AqChipComponent, VarianteChip } from '../../componentes/chip/aq-chip.component';
import { AqEstadoVacioComponent } from '../../componentes/estado-vacio/aq-estado-vacio.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';
import { EventoPasado, EventoWeb } from '../../datos/modelos';

interface FilaEvento {
  id: string | null;
  titulo: string;
  fechaHora: string;
  chipOrigen: VarianteChip;
  textoOrigen: string;
  escaneos: number;
  alarmasActivas: number;
  chipEstado: VarianteChip;
  textoEstado: string;
}

const FORMATO_FECHA = new Intl.DateTimeFormat('es-CO', {
  day: 'numeric',
  month: 'short',
  hour: 'numeric',
  minute: '2-digit',
});

function aFila(e: EventoWeb): FilaEvento {
  return {
    id: e.id,
    titulo: e.titulo,
    fechaHora: FORMATO_FECHA.format(new Date(e.fechaHora)),
    chipOrigen: e.origen === 'creada-por-mi' ? 'creada-por-mi' : 'escaneada',
    textoOrigen: e.origen === 'creada-por-mi' ? 'Creada por mí' : '✓ Escaneada',
    escaneos: e.escaneos,
    alarmasActivas: e.alarmasActivas,
    chipEstado: 'publicado',
    textoEstado: 'Publicado',
  };
}

function pasadoAFila(e: EventoPasado): FilaEvento {
  return {
    id: null,
    titulo: e.nombre,
    fechaHora: FORMATO_FECHA.format(new Date(e.fechaHora)),
    chipOrigen: e.origen === 'creada-por-mi' ? 'creada-por-mi' : 'escaneada',
    textoOrigen: e.origen === 'creada-por-mi' ? 'Creada por mí' : '✓ Escaneada',
    escaneos: e.escaneos,
    alarmasActivas: e.alarmasActivas,
    chipEstado: 'activa',
    textoEstado: 'Finalizado',
  };
}

/** W01 · Mis Alarmas (F-W01 · F-W02 · F-W06): hub con indicadores, gráfica y tabla de eventos con pestañas, filtros y búsqueda. */
@Component({
  selector: 'aq-w01-mis-alarmas',
  imports: [
    RouterLink,
    AqIndicadorComponent,
    AqGraficaBarrasComponent,
    AqPildorasComponent,
    AqCampoBusquedaComponent,
    AqTablaComponent,
    AqChipComponent,
    AqEstadoVacioComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W01">
      <header class="cabecera">
        <h1 class="titulo">Mis Alarmas</h1>
        <div class="acciones">
          <a aq-boton variante="secundario" routerLink="/qr">Descargar QR en lote</a>
          <a aq-boton routerLink="/reportes">Exportar reporte</a>
        </div>
      </header>

      @if (datos.web(); as web) {
        <div class="indicadores">
          <aq-indicador
            [valor]="web.indicadores.eventosActivos.valor"
            etiqueta="Eventos activos"
            [detalle]="web.indicadores.eventosActivos.detalle"
          />
          <aq-indicador
            [valor]="web.indicadores.escaneosTotales.valor"
            etiqueta="Escaneos totales"
            [detalle]="web.indicadores.escaneosTotales.detalle"
          />
          <aq-indicador
            [valor]="web.indicadores.alarmasActivas.valor"
            etiqueta="Alarmas activas"
            [detalle]="web.indicadores.alarmasActivas.detalle"
          />
          <aq-indicador
            [valor]="web.indicadores.confirmaronYaVoy.valor"
            etiqueta="Confirmaron «Ya voy»"
            [detalle]="web.indicadores.confirmaronYaVoy.detalle"
          />
        </div>

        <aq-grafica-barras [datos]="web.escaneosPorSemana" />

        <section class="tablero">
          <div class="filtros">
            <aq-pildoras
              [opciones]="opcionesOrigen"
              [activo]="origen()"
              (elegir)="irA({ origen: $event })"
            />
            <aq-pildoras
              [opciones]="opcionesEstado"
              [activo]="estado()"
              (elegir)="irA({ estado: $event })"
            />
          </div>
          <aq-campo-busqueda ancho="w01" [value]="q()" (buscar)="irA({ q: $event || null })" />

          @if (filas().length > 0) {
            <div class="contenedor-tabla">
              <table aq-tabla>
                <thead>
                  <tr>
                    <th>Evento</th>
                    <th>Fecha</th>
                    <th>Origen</th>
                    <th>Escaneos</th>
                    <th>Alarmas activas</th>
                    <th>Estado</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  @for (fila of filas(); track fila.titulo) {
                    <tr>
                      <td>{{ fila.titulo }}</td>
                      <td>{{ fila.fechaHora }}</td>
                      <td><aq-chip [variante]="fila.chipOrigen">{{ fila.textoOrigen }}</aq-chip></td>
                      <td>{{ fila.escaneos }}</td>
                      <td>{{ fila.alarmasActivas }}</td>
                      <td><aq-chip [variante]="fila.chipEstado">{{ fila.textoEstado }}</aq-chip></td>
                      <td>
                        @if (fila.id) {
                          <a aq-enlace [routerLink]="['/eventos', fila.id]">Ver detalle ›</a>
                        }
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
            @if (q()) {
              <p class="resumen-busqueda">
                Mostrando {{ filas().length }} {{ filas().length === 1 ? 'alarma' : 'alarmas' }} ·
                filtro: "{{ q() }}"
                <a aq-enlace (click)="irA({ q: null })">Limpiar</a>
              </p>
            }
          } @else {
            <aq-estado-vacio
              titulo="Sin resultados con estos filtros"
              [texto]="web.filtros.borradores.length === 0 && estado() === 'borradores'
                ? mensajeSinBorradores()
                : ''"
            >
              <a aq-boton variante="secundario" (click)="irA({ estado: null, origen: null, q: null })"
                >Ver todos</a
              >
            </aq-estado-vacio>
          }
        </section>
      }
    </section>
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-web-bloques);
    }
    .cabecera {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
    }
    .titulo {
      margin: 0;
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .acciones {
      display: flex;
      gap: var(--space-12);
    }
    .indicadores {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-web-indicadores);
    }
    .tablero {
      display: flex;
      flex-direction: column;
      gap: var(--space-16);
      box-sizing: border-box;
      padding: var(--space-16);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
      background: var(--color-blanco);
    }
    .filtros {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-20);
    }
    .contenedor-tabla {
      overflow-x: auto;
    }
    .resumen-busqueda {
      margin: 0;
      display: flex;
      align-items: center;
      gap: var(--space-8);
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W01MisAlarmasComponent {
  protected readonly datos = inject(DatosService);
  private readonly router = inject(Router);
  private readonly snackbar = inject(SnackbarService);

  readonly origen = input<string>('todos');
  readonly estado = input<string>('proximos');
  readonly q = input<string>('');

  protected readonly opcionesOrigen = [
    { valor: 'todos', texto: 'Todos' },
    { valor: 'creados', texto: 'Creados' },
    { valor: 'escaneados', texto: 'Escaneados' },
  ];
  protected readonly opcionesEstado = [
    { valor: 'proximos', texto: 'Próximos' },
    { valor: 'pasados', texto: 'Pasados' },
    { valor: 'borradores', texto: 'Borradores' },
  ];

  protected readonly filas = computed<FilaEvento[]>(() => {
    const web = this.datos.web();
    if (!web) return [];
    const estado = this.estado();

    if (estado === 'pasados') return web.eventosPasados.map(pasadoAFila);
    if (estado === 'borradores') return [];

    let base = web.eventos;
    const origen = this.origen();
    if (origen === 'creados') base = base.filter((e) => e.origen === 'creada-por-mi');
    else if (origen === 'escaneados') base = base.filter((e) => e.origen === 'escaneada');

    const consulta = this.q().trim().toLowerCase();
    if (consulta) base = base.filter((e) => e.titulo.toLowerCase().includes(consulta));

    return base.map(aFila);
  });

  protected readonly mensajeSinBorradores = computed(
    () => this.datos.mensajes()?.sinBorradores ?? '',
  );

  constructor() {
    effect(() => {
      const mensajes = this.datos.mensajes();
      if (mensajes && this.estado() === 'descarga-completada') {
        untracked(() => this.snackbar.mostrar(mensajes.descargaCompletada));
      }
    });
  }

  protected irA(cambios: Record<string, string | null>): void {
    void this.router.navigate([], { queryParams: cambios, queryParamsHandling: 'merge' });
  }
}
```

- [ ] **Step 4: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 5: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/pantallas/w01-mis-alarmas
git commit -m "W01: tablero de eventos con indicadores, gráfica, pestañas y filtros"
```

---

### Task 6: W03 · Detalle Evento

**Files:**
- Modify: `apps/web/src/app/pantallas/w03-detalle-evento/w03-detalle-evento.component.ts`
- Test: `apps/web/src/app/pantallas/w03-detalle-evento/w03-detalle-evento.component.spec.ts`

**Interfaces:**
- Consumes: `AqIndicadorComponent`, `AqCampoBusquedaComponent`, `AqTablaComponent`, `AqChipComponent`, `AqPaginadorComponent`, `AqBotonComponent`, `AqEnlaceComponent`, `DatosService` (`web`, `asistentes`).
- Produces: `W03DetalleEventoComponent` con `id = input.required<string>()` (route param `:id`), `q = input<string>('')`, `pagina = input<string>('1')`, raíz `data-codigo="W03"`.

- [ ] **Step 1: Escribir la prueba (falla: sigue siendo el marcador)**

Crear `apps/web/src/app/pantallas/w03-detalle-evento/w03-detalle-evento.component.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

const botonConTexto = (raiz: HTMLElement, texto: string) =>
  Array.from(raiz.querySelectorAll('button, a')).find(
    (b) => b.textContent?.trim() === texto,
  ) as HTMLElement;

describe('W03 · Detalle Evento', () => {
  it('muestra la miga, el título, los indicadores del evento y los asistentes (página 1)', async () => {
    const { raiz } = await abrir('/eventos/w-partido');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    expect(pagina.textContent).toContain('‹ Mis alarmas');
    expect(pagina.textContent).toContain('Partido Sintética');
    expect(pagina.textContent).toContain('16'); // escaneos
    expect(pagina.textContent).toContain('11'); // alarmas activas
    expect(pagina.textContent).toContain('7'); // ya voy
    const filas = pagina.querySelectorAll('tbody tr');
    expect(filas.length).toBe(4);
    expect(pagina.textContent).toContain('Joale7');
    expect(pagina.textContent).toContain(dataset.web.asistentes.notaPrivacidad);
    expect(pagina.textContent).not.toContain('@'); // ningún correo
  });

  it('la miga «‹ Mis alarmas» vuelve a W01', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido');
    botonConTexto(raiz, '‹ Mis alarmas').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('el paginador navega a la página 2 con LauM, Nico_R, D.G, Vale22', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    (Array.from(pagina.querySelectorAll('button')).find((b) => b.textContent?.trim() === '2') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/eventos/w-partido?pagina=2');
    expect(pagina.textContent).toContain('LauM');
    expect(pagina.textContent).toContain('Vale22');
  });

  it('la búsqueda filtra por alias («Mi» → Mike1008)', async () => {
    const { raiz } = await abrir('/eventos/w-partido?q=Mi');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    const filas = pagina.querySelectorAll('tbody tr');
    expect(filas.length).toBe(1);
    expect(pagina.textContent).toContain('Mike1008');
  });

  it('un evento sin datos de asistentes (D4) muestra los indicadores y la tabla vacía', async () => {
    const { raiz } = await abrir('/eventos/w-seminario');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    expect(pagina.textContent).toContain('Seminario UX');
    expect(pagina.textContent).toContain('12'); // escaneos de w-seminario
    expect(pagina.querySelectorAll('tbody tr').length).toBe(0);
  });

  it('«Exportar reporte» navega a W04', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido');
    botonConTexto(raiz, 'Exportar reporte').click();
    await estable();
    expect(router.url).toBe('/reportes');
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL.

- [ ] **Step 3: Página real**

Reemplazar `apps/web/src/app/pantallas/w03-detalle-evento/w03-detalle-evento.component.ts` completo:

```ts
import { Component, computed, inject, input } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AqIndicadorComponent } from '../../componentes/indicador/aq-indicador.component';
import { AqCampoBusquedaComponent } from '../../componentes/campo-busqueda/aq-campo-busqueda.component';
import { AqTablaComponent } from '../../componentes/tabla/aq-tabla.component';
import { AqChipComponent } from '../../componentes/chip/aq-chip.component';
import { AqPaginadorComponent } from '../../componentes/paginador/aq-paginador.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { DatosService } from '../../datos/datos.service';
import { Asistente } from '../../datos/modelos';

/** W03 · Detalle Evento (F-W03): indicadores del evento y tabla anónima de «Quiénes escanearon» (Ley 1581). */
@Component({
  selector: 'aq-w03-detalle-evento',
  imports: [
    RouterLink,
    AqIndicadorComponent,
    AqCampoBusquedaComponent,
    AqTablaComponent,
    AqChipComponent,
    AqPaginadorComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W03">
      <a aq-enlace routerLink="/alarmas">‹ Mis alarmas</a>

      @if (evento(); as evento) {
        <header class="cabecera">
          <h1 class="titulo">{{ evento.titulo }}</h1>
          <a aq-boton variante="secundario" routerLink="/reportes">Exportar reporte</a>
        </header>

        <div class="indicadores">
          <aq-indicador [valor]="evento.escaneos" etiqueta="Escaneos" detalle="Total del evento" />
          <aq-indicador
            [valor]="evento.alarmasActivas"
            etiqueta="Alarmas activas"
            [detalle]="
              evento.alarmasEliminadas
                ? evento.alarmasEliminadas + ' la eliminaron'
                : 'Sin eliminaciones registradas'
            "
          />
          <aq-indicador
            [valor]="evento.confirmaronYaVoy ?? 0"
            etiqueta="«Ya voy»"
            detalle="Personas que avisaron que van en camino"
          />
        </div>

        <section class="tabla-asistentes">
          <div class="cabecera-tabla">
            <h2 class="titulo-tabla">Quiénes escanearon</h2>
            <aq-campo-busqueda ancho="w03" placeholder="Buscar asistente" [value]="q()" (buscar)="buscar($event)" />
          </div>

          @if (filas().length > 0) {
            <div class="contenedor-tabla">
              <table aq-tabla>
                <thead>
                  <tr>
                    <th>Alias</th>
                    <th>Escaneo</th>
                    <th>Alarma</th>
                    <th>«Ya voy»</th>
                  </tr>
                </thead>
                <tbody>
                  @for (fila of filas(); track fila.alias) {
                    <tr>
                      <td>{{ fila.alias }}</td>
                      <td>{{ formatoFecha(fila.escaneo) }}</td>
                      <td><aq-chip [variante]="fila.alarma === 'activa' ? 'activa' : 'eliminada'">{{ fila.alarma === 'activa' ? 'Activa' : 'Eliminada' }}</aq-chip></td>
                      <td>{{ (fila.confirmoYaVoy ?? fila.yaVoy) ? 'Sí' : 'No' }}</td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
            <p class="privacidad">{{ asistentesEvento()?.notaPrivacidad }}</p>
            @if (!q()) {
              <aq-paginador
                [texto]="textoPaginador()"
                [paginaActual]="paginaActual()"
                [totalPaginas]="2"
                (cambiar)="irAPagina($event)"
              />
            }
          } @else {
            <p class="sin-asistentes">Aún no hay asistentes registrados para este evento.</p>
          }
        </section>
      }
    </section>
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-web-bloques);
    }
    .cabecera {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
    }
    .titulo {
      margin: 0;
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .indicadores {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-web-indicadores);
    }
    .tabla-asistentes {
      display: flex;
      flex-direction: column;
      gap: var(--space-16);
      box-sizing: border-box;
      padding: var(--space-16);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
      background: var(--color-blanco);
    }
    .cabecera-tabla {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
    }
    .titulo-tabla {
      margin: 0;
      font: var(--text-titulo-tarjeta-web);
      color: var(--color-texto);
    }
    .contenedor-tabla {
      overflow-x: auto;
    }
    .privacidad,
    .sin-asistentes {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W03DetalleEventoComponent {
  private readonly datos = inject(DatosService);
  private readonly router = inject(Router);

  readonly id = input.required<string>();
  readonly q = input<string>('');
  readonly pagina = input<string>('1');

  protected readonly evento = computed(() => this.datos.web()?.eventos.find((e) => e.id === this.id()));
  protected readonly asistentesEvento = computed(() => {
    const asistentes = this.datos.asistentes();
    return asistentes && asistentes.eventoId === this.id() ? asistentes : undefined;
  });
  protected readonly paginaActual = computed(() => (this.pagina() === '2' ? 2 : 1));

  protected readonly filas = computed<Asistente[]>(() => {
    const asistentes = this.asistentesEvento();
    if (!asistentes) return [];
    const consulta = this.q().trim().toLowerCase();
    if (consulta) {
      return [...asistentes.mostrados, ...asistentes.pagina2].filter((a) =>
        a.alias.toLowerCase().includes(consulta),
      );
    }
    return this.paginaActual() === 2 ? asistentes.pagina2 : asistentes.mostrados;
  });

  protected readonly textoPaginador = computed(() => {
    const asistentes = this.asistentesEvento();
    if (!asistentes) return '';
    const desde = this.paginaActual() === 2 ? asistentes.porPagina + 1 : 1;
    const hasta = this.paginaActual() === 2 ? asistentes.porPagina * 2 : asistentes.porPagina;
    return `Mostrando ${desde}–${hasta} de ${asistentes.total} asistentes · ${asistentes.porPagina} por página`;
  });

  protected formatoFecha(iso: string): string {
    return new Intl.DateTimeFormat('es-CO', { day: 'numeric', month: 'short' }).format(new Date(iso));
  }

  protected buscar(texto: string): void {
    void this.router.navigate([], { queryParams: { q: texto || null, pagina: null }, queryParamsHandling: 'merge' });
  }

  protected irAPagina(pagina: number): void {
    void this.router.navigate([], {
      queryParams: { pagina: pagina === 1 ? null : String(pagina) },
      queryParamsHandling: 'merge',
    });
  }
}
```

- [ ] **Step 4: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 5: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/pantallas/w03-detalle-evento
git commit -m "W03: detalle del evento con asistentes paginados y anónimos"
```

---

### Task 7: W04 · Reportes

**Files:**
- Modify: `apps/web/src/app/pantallas/w04-reportes/w04-reportes.component.ts`
- Test: `apps/web/src/app/pantallas/w04-reportes/w04-reportes.component.spec.ts`

**Interfaces:**
- Consumes: `AqTarjetaComponent`, `AqSelectorSegmentadoComponent`, `AqCampoComponent` (rango personalizado), `AqBotonComponent`, `AqEnlaceComponent`, `CortesService` (`apilarColumnas`), `DatosService` (`reporte`), `SnackbarService`, patrón «sección con tarjeta de formulario» de W06 (D del Plan 3).
- Produces: `W04ReportesComponent`, raíz `data-codigo="W04"`.

- [ ] **Step 1: Escribir la prueba (falla: sigue siendo el marcador)**

Crear `apps/web/src/app/pantallas/w04-reportes/w04-reportes.component.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

const botonConTexto = (raiz: HTMLElement, texto: string) =>
  Array.from(raiz.querySelectorAll('button, a')).find(
    (b) => b.textContent?.trim() === texto,
  ) as HTMLElement;

describe('W04 · Reportes', () => {
  it('muestra la miga, el formulario y la tarjeta «Reportes generados»', async () => {
    const { raiz } = await abrir('/reportes');
    const pagina = raiz.querySelector('[data-codigo="W04"]')!;
    expect(pagina.textContent).toContain('‹ Mis alarmas');
    expect(pagina.textContent).toContain('Exportar reporte consolidado');
    expect(pagina.textContent).toContain('Último mes');
    expect(pagina.textContent).toContain('Reportes generados');
    for (const g of dataset.web.reporte.generados) {
      expect(pagina.textContent).toContain(g.archivo);
    }
    expect(pagina.textContent).toContain(dataset.web.reporte.nota);
  });

  it('«Rango personalizado» muestra los campos de fecha', async () => {
    const { raiz, estable } = await abrir('/reportes');
    botonConTexto(raiz, 'Rango personalizado').click();
    await estable();
    expect(raiz.querySelector('[data-codigo="W04"]')!.textContent).toContain('Desde');
  });

  it('«Generar y descargar» muestra el estado Listo con el archivo generado y «Generar de nuevo» reinicia', async () => {
    const { raiz, estable } = await abrir('/reportes');
    botonConTexto(raiz, 'Generar y descargar').click();
    await estable();
    const pagina = raiz.querySelector('[data-codigo="W04"]')!;
    expect(pagina.getAttribute('data-estado')).toBe('listo');
    expect(pagina.textContent).toContain(
      `${dataset.web.reporte.archivoGenerado} generado y descargado exitosamente`,
    );
    botonConTexto(raiz, 'Generar de nuevo').click();
    await estable();
    expect(pagina.getAttribute('data-estado')).toBeNull();
  });

  it('«Descargar de nuevo» de un reporte ya generado muestra el snackbar de descarga', async () => {
    const { raiz, estable } = await abrir('/reportes');
    botonConTexto(raiz, 'Descargar de nuevo').click();
    await estable();
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.descargaCompletada);
  });

  it('la miga «‹ Mis alarmas» vuelve a W01', async () => {
    const { raiz, estable, router } = await abrir('/reportes');
    botonConTexto(raiz, '‹ Mis alarmas').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL.

- [ ] **Step 3: Página real**

Reemplazar `apps/web/src/app/pantallas/w04-reportes/w04-reportes.component.ts` completo:

```ts
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaComponent } from '../../componentes/tarjeta/aq-tarjeta.component';
import { AqSelectorSegmentadoComponent, OpcionSegmentada } from '../../componentes/selector-segmentado/aq-selector-segmentado.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';
import { CortesService } from '../../navegacion/cortes.service';

/** W04 · Reportes (F-W04): tarjeta de formulario (rango + formato) y tarjeta lateral «Reportes generados». */
@Component({
  selector: 'aq-w04-reportes',
  imports: [
    RouterLink,
    FormField,
    AqTarjetaComponent,
    AqSelectorSegmentadoComponent,
    AqCampoComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W04" [attr.data-estado]="listo() ? 'listo' : null">
      <a aq-enlace routerLink="/alarmas">‹ Mis alarmas</a>
      <h1 class="titulo">Reportes</h1>
      <p class="subtitulo">Descarga informes con métricas agregadas de tus eventos.</p>

      @if (datos.reporte(); as reporte) {
        <div class="fila" [attr.data-apilada]="apilar() ? '' : null">
          <aq-tarjeta class="formulario">
            <h2 class="titulo-tarjeta">Exportar reporte consolidado</h2>
            @if (!listo()) {
              <aq-selector-segmentado etiqueta="Rango" [opciones]="opcionesRango" [formField]="formulario.rango" />
              @if (formulario.rango().value() === 'personalizado') {
                <div class="fechas">
                  <aq-campo etiqueta="DESDE" tipo="text" [formField]="formulario.desde" />
                  <aq-campo etiqueta="HASTA" tipo="text" [formField]="formulario.hasta" />
                </div>
              }
              <aq-selector-segmentado etiqueta="Formato" [opciones]="opcionesFormato" [formField]="formulario.formato" />
              <p class="nota">{{ reporte.nota }}</p>
              <button aq-boton type="button" (click)="generar()">Generar y descargar</button>
            } @else {
              <p class="resultado">
                {{ reporte.archivoGenerado }} generado y descargado exitosamente
              </p>
              <button aq-boton variante="secundario" type="button" (click)="listo.set(false)">
                Generar de nuevo
              </button>
            }
          </aq-tarjeta>
          <aq-tarjeta class="generados">
            <h2 class="titulo-tarjeta">Reportes generados</h2>
            @for (g of reporte.generados; track g.archivo) {
              <div class="fila-reporte">
                <div class="info">
                  <span class="archivo">{{ g.archivo }}</span>
                  <span class="metadatos">{{ g.fecha }} · {{ g.formato.toUpperCase() }} · {{ g.rango }}</span>
                </div>
                <a aq-enlace (click)="descargarDeNuevo()">Descargar de nuevo</a>
              </div>
            }
          </aq-tarjeta>
        </div>
      }
    </section>
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
    }
    .titulo {
      margin: 0;
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .subtitulo {
      margin: 0 0 var(--space-8);
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
    }
    .fila {
      display: flex;
      align-items: flex-start;
      gap: var(--space-web-bloques);
    }
    .fila[data-apilada] {
      flex-direction: column;
      align-items: stretch;
    }
    .formulario {
      flex: 0 0 var(--size-tarjeta-formulario-web);
      gap: var(--space-16);
    }
    .fila[data-apilada] .formulario {
      flex: none;
    }
    .generados {
      flex: 1;
      gap: var(--space-12);
      min-width: 0;
    }
    .titulo-tarjeta {
      margin: 0;
      font: var(--text-titulo-tarjeta-web);
      color: var(--color-texto);
    }
    .fechas {
      display: flex;
      gap: var(--space-12);
    }
    .fechas aq-campo {
      flex: 1;
    }
    .nota {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .resultado {
      margin: 0;
      font: var(--text-cuerpo-web);
      color: var(--color-exito);
    }
    .fila-reporte {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: var(--space-12);
      padding: var(--space-12) 0;
      border-top: var(--stroke-borde-fino) solid var(--color-borde);
    }
    .info {
      display: flex;
      flex-direction: column;
      gap: var(--space-2);
      min-width: 0;
    }
    .archivo {
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    .metadatos {
      font: var(--text-nota);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W04ReportesComponent {
  protected readonly datos = inject(DatosService);
  private readonly snackbar = inject(SnackbarService);
  protected readonly apilar = inject(CortesService).apilarColumnas;

  protected readonly opcionesRango: readonly OpcionSegmentada[] = [
    { valor: 'ultimo-mes', texto: 'Último mes' },
    { valor: 'semestre', texto: 'Semestre' },
    { valor: 'personalizado', texto: 'Rango personalizado' },
  ];
  protected readonly opcionesFormato: readonly OpcionSegmentada[] = [
    { valor: 'pdf', texto: 'PDF' },
    { valor: 'csv', texto: 'CSV' },
  ];

  protected readonly formulario = form(
    signal({ rango: 'ultimo-mes', formato: 'pdf', desde: '2026-08-01', hasta: '2026-08-31' }),
  );
  protected readonly listo = signal(false);

  protected generar(): void {
    this.listo.set(true);
  }

  protected descargarDeNuevo(): void {
    const mensajes = this.datos.mensajes();
    if (mensajes) this.snackbar.mostrar(mensajes.descargaCompletada);
  }
}
```

- [ ] **Step 4: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 5: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/pantallas/w04-reportes
git commit -m "W04: reportes con rango, formato y reportes generados"
```

---

### Task 8: W05 · Descargar QR

**Files:**
- Modify: `apps/web/src/app/pantallas/w05-descargar-qr/w05-descargar-qr.component.ts`
- Test: `apps/web/src/app/pantallas/w05-descargar-qr/w05-descargar-qr.component.spec.ts`

**Interfaces:**
- Consumes: `AqTarjetaComponent`, `AqSelectorSegmentadoComponent`, `AqChipComponent`, `AqVistaPreviaAficheComponent`, `AqBotonComponent`, `AqEnlaceComponent`, `CortesService`, `DatosService` (`web`, `descargaQR`).
- Produces: `W05DescargarQrComponent`, raíz `data-codigo="W05"`.

- [ ] **Step 1: Escribir la prueba (falla: sigue siendo el marcador)**

Crear `apps/web/src/app/pantallas/w05-descargar-qr/w05-descargar-qr.component.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

const botonConTexto = (raiz: HTMLElement, texto: string) =>
  Array.from(raiz.querySelectorAll('button, a')).find(
    (b) => b.textContent?.trim() === texto,
  ) as HTMLElement;

describe('W05 · Descargar QR', () => {
  it('muestra la miga, la lista de eventos preseleccionados (2 de 2) y la vista previa', async () => {
    const { raiz } = await abrir('/qr');
    const pagina = raiz.querySelector('[data-codigo="W05"]')!;
    expect(pagina.textContent).toContain('‹ Mis alarmas');
    expect(pagina.textContent).toContain('Seleccionar todos');
    expect(pagina.textContent).toContain('2 de 2');
    expect(pagina.textContent).toContain('Seminario UX');
    expect(pagina.textContent).toContain('Partido Sintética');
    expect(pagina.querySelector('aq-vista-previa-afiche')).not.toBeNull();
    const marcadas = pagina.querySelectorAll('input[type="checkbox"]:checked');
    expect(marcadas.length).toBe(3); // «Seleccionar todos» + 2 eventos
  });

  it('desmarcar un evento actualiza el contador y «Seleccionar todos»', async () => {
    const { raiz, estable } = await abrir('/qr');
    const pagina = raiz.querySelector('[data-codigo="W05"]')!;
    const casillas = Array.from(pagina.querySelectorAll('[data-evento] input')) as HTMLInputElement[];
    casillas[0].click();
    await estable();
    expect(pagina.textContent).toContain('1 de 2');
    expect((pagina.querySelector('[data-todos] input') as HTMLInputElement).checked).toBe(false);
  });

  it('«Cancelar» vuelve a W01 sin descargar', async () => {
    const { raiz, estable, router } = await abrir('/qr');
    botonConTexto(raiz, 'Cancelar').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('«Descargar» navega a W01 con ?estado=descarga-completada', async () => {
    const { raiz, estable, router } = await abrir('/qr');
    botonConTexto(raiz, 'Descargar').click();
    await estable();
    expect(router.url).toBe('/alarmas?estado=descarga-completada');
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL.

- [ ] **Step 3: Página real**

Reemplazar `apps/web/src/app/pantallas/w05-descargar-qr/w05-descargar-qr.component.ts` completo:

```ts
import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AqTarjetaComponent } from '../../componentes/tarjeta/aq-tarjeta.component';
import { AqSelectorSegmentadoComponent, OpcionSegmentada } from '../../componentes/selector-segmentado/aq-selector-segmentado.component';
import { AqChipComponent } from '../../componentes/chip/aq-chip.component';
import { AqVistaPreviaAficheComponent } from '../../componentes/vista-previa-afiche/aq-vista-previa-afiche.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { DatosService } from '../../datos/datos.service';
import { CortesService } from '../../navegacion/cortes.service';

/** W05 · Descargar QR (F-W05): selección de eventos activos, formato PNG/PDF y vista previa del afiche. */
@Component({
  selector: 'aq-w05-descargar-qr',
  imports: [
    RouterLink,
    AqTarjetaComponent,
    AqSelectorSegmentadoComponent,
    AqChipComponent,
    AqVistaPreviaAficheComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W05">
      <a aq-enlace routerLink="/alarmas">‹ Mis alarmas</a>
      <h1 class="titulo">Descargar QR</h1>

      @if (datos.web(); as web) {
        <div class="fila" [attr.data-apilada]="apilar() ? '' : null">
          <aq-tarjeta class="seleccion">
            <label class="fila-todos" data-todos>
              <input type="checkbox" [checked]="todosSeleccionados()" (change)="alternarTodos()" />
              <span>Seleccionar todos</span>
              <span class="contador">{{ seleccionados().size }} de {{ web.eventos.length }}</span>
            </label>
            @for (evento of web.eventos; track evento.id) {
              <label class="fila-evento" [attr.data-evento]="evento.id">
                <input
                  type="checkbox"
                  [checked]="seleccionados().has(evento.id)"
                  (change)="alternar(evento.id)"
                />
                <span class="nombre">{{ evento.titulo }}</span>
                <aq-chip variante="publicado">Publicado</aq-chip>
              </label>
            }
            <aq-selector-segmentado
              etiqueta="Formato"
              [opciones]="opcionesFormato"
              [value]="formato()"
              (valueChange)="formato.set($event)"
            />
            <div class="acciones">
              <a aq-boton variante="secundario" routerLink="/alarmas">Cancelar</a>
              <button aq-boton type="button" [disabled]="seleccionados().size === 0" (click)="descargar()">
                Descargar
              </button>
            </div>
          </aq-tarjeta>
          <aq-vista-previa-afiche class="vista-previa" [nombreEvento]="nombrePrimerSeleccionado()" />
        </div>
      }
    </section>
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
    }
    .titulo {
      margin: 0;
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .fila {
      display: flex;
      align-items: flex-start;
      gap: var(--space-web-bloques);
    }
    .fila[data-apilada] {
      flex-direction: column;
      align-items: stretch;
    }
    .seleccion {
      flex: 0 0 var(--size-tarjeta-formulario-web);
      gap: var(--space-12);
    }
    .fila[data-apilada] .seleccion {
      flex: none;
    }
    .vista-previa {
      flex: 1;
      min-width: 0;
    }
    .fila-todos,
    .fila-evento {
      display: flex;
      align-items: center;
      gap: var(--space-10);
      padding: var(--space-8) 0;
      border-bottom: var(--stroke-borde-fino) solid var(--color-borde);
      cursor: pointer;
    }
    input[type='checkbox'] {
      width: var(--size-checkbox);
      height: var(--size-checkbox);
      accent-color: var(--color-tinta);
      border-radius: var(--radius-casilla);
    }
    .nombre {
      flex: 1;
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    .contador {
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .acciones {
      display: flex;
      justify-content: flex-end;
      gap: var(--space-12);
      padding-top: var(--space-8);
    }
  `,
})
export class W05DescargarQrComponent {
  protected readonly datos = inject(DatosService);
  private readonly router = inject(Router);
  protected readonly apilar = inject(CortesService).apilarColumnas;

  protected readonly opcionesFormato: readonly OpcionSegmentada[] = [
    { valor: 'png', texto: 'PNG' },
    { valor: 'pdf', texto: 'PDF' },
  ];
  protected readonly formato = signal('png');
  protected readonly seleccionados = signal(new Set(this.datos.descargaQR()?.seleccionados ?? []));

  protected readonly todosSeleccionados = computed(() => {
    const total = this.datos.web()?.eventos.length ?? 0;
    return total > 0 && this.seleccionados().size === total;
  });

  protected readonly nombrePrimerSeleccionado = computed(() => {
    const eventos = this.datos.web()?.eventos ?? [];
    const primero = eventos.find((e) => this.seleccionados().has(e.id));
    return primero?.titulo ?? eventos[0]?.titulo ?? '';
  });

  protected alternar(id: string): void {
    const actuales = new Set(this.seleccionados());
    if (actuales.has(id)) actuales.delete(id);
    else actuales.add(id);
    this.seleccionados.set(actuales);
  }

  protected alternarTodos(): void {
    const eventos = this.datos.web()?.eventos ?? [];
    this.seleccionados.set(this.todosSeleccionados() ? new Set() : new Set(eventos.map((e) => e.id)));
  }

  protected descargar(): void {
    void this.router.navigate(['/alarmas'], { queryParams: { estado: 'descarga-completada' } });
  }
}
```

`descargaQR()` puede leerse antes de que `httpResource` resuelva (primer render); `signal(new Set(this.datos.descargaQR()?.seleccionados ?? []))` se evalúa una sola vez en el constructor con `undefined` si el dataset no ha llegado — corregido en el Step 3b con un `effect` que sincroniza la selección inicial cuando el dataset llega.

- [ ] **Step 3b: Sincronizar la selección inicial con el dataset**

`AqSelectorSegmentadoComponent` no expone `[value]`/`(valueChange)` — implementa `FormValueControl<string>` con `model('')`, así que el binding correcto es `[(value)]="formato"`, no `[value]`/`(valueChange)`. Corregir en la plantilla de `w05-descargar-qr.component.ts`:

```html
            <aq-selector-segmentado
              etiqueta="Formato"
              [opciones]="opcionesFormato"
              [(value)]="formato"
            />
```

Y sincronizar la selección inicial de eventos con un `effect`, porque `datos.descargaQR()` puede llegar después del primer render (`httpResource`). Reemplazar en la clase:

```ts
  protected readonly seleccionados = signal(new Set(this.datos.descargaQR()?.seleccionados ?? []));
```

por

```ts
  protected readonly seleccionados = signal(new Set<string>());

  constructor() {
    effect(() => {
      const inicial = this.datos.descargaQR()?.seleccionados;
      if (inicial) untracked(() => this.seleccionados.set(new Set(inicial)));
    });
  }
```

y agregar `effect` y `untracked` al import de `@angular/core`:

```ts
import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
```

- [ ] **Step 4: Ejecutar y verificar que pasa**

Run: `npx ng test --watch=false`
Expected: PASS.

- [ ] **Step 5: Verificación completa y commit**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/pantallas/w05-descargar-qr
git commit -m "W05: selección de eventos, formato y vista previa del afiche"
```

---

### Task 9: Flujo T5 completo, verificación pixel-perfect y documentación

**Files:**
- Create: `apps/web/src/app/flujos-persona-b.spec.ts`
- Modify: `docs/verificacion/README.md`
- Create: `docs/verificacion/W01.png`, `docs/verificacion/W01-figma.png`, `docs/verificacion/W03.png`, `docs/verificacion/W03-figma.png`, `docs/verificacion/W04.png`, `docs/verificacion/W04-figma.png`, `docs/verificacion/W05.png`, `docs/verificacion/W05-figma.png`
- Modify: `README.md`

**Interfaces:**
- Consumes: todo lo construido en las Tareas 1–8; sigue el patrón de `flujos-persona-a.spec.ts` (Task de referencia del Plan 3).

- [ ] **Step 1: Escribir el flujo T5 completo de la Persona B**

`apps/web/src/app/flujos-persona-b.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from './app.routes';
import { SnackbarService } from './componentes/snackbar/snackbar.service';
import { SesionService } from './datos/sesion.service';
import { cargarDataset, proveedoresPrueba } from '../testing/datos-prueba';
import dataset from '../../public/dataset.json';

async function iniciar(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  const estable = () => harness.fixture.whenStable();
  const tocarTexto = async (texto: string) => {
    const el = Array.from(raiz.querySelectorAll('button, a')).find(
      (b) => b.textContent?.trim() === texto,
    );
    (el as HTMLElement).click();
    await estable();
  };
  return { raiz, estable, tocarTexto, router: TestBed.inject(Router) };
}

describe('Flujos web de la Persona B (TRAZABILIDAD §4 T5)', () => {
  it('T5 · W01 → W03 → W04 → volver, y W01 → W05 → descarga completada', async () => {
    TestBed.inject(SesionService).iniciar();
    const { raiz, estable, tocarTexto, router } = await iniciar('/alarmas');
    expect(raiz.querySelector('[data-codigo="W01"]')).not.toBeNull();

    await tocarTexto('Ver detalle ›');
    expect(router.url).toContain('/eventos/');
    expect(raiz.querySelector('[data-codigo="W03"]')).not.toBeNull();

    await tocarTexto('Exportar reporte');
    expect(router.url).toBe('/reportes');
    expect(raiz.querySelector('[data-codigo="W04"]')).not.toBeNull();
    await tocarTexto('Generar y descargar');
    expect(raiz.querySelector('[data-codigo="W04"]')!.getAttribute('data-estado')).toBe('listo');

    await tocarTexto('‹ Mis alarmas');
    expect(router.url).toBe('/alarmas');

    await tocarTexto('Descargar QR en lote');
    expect(router.url).toBe('/qr');
    expect(raiz.querySelector('[data-codigo="W05"]')).not.toBeNull();
    await tocarTexto('Descargar');
    expect(router.url).toBe('/alarmas?estado=descarga-completada');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.descargaCompletada);
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que pasa**

Run: `cd apps/web && npx ng test --watch=false`
Expected: PASS, todas las pruebas del proyecto en verde (Tareas 1–9 incluidas).

- [ ] **Step 3: Verificación pixel-perfect (manual, documentar en `docs/verificacion/`)**

Con `npx ng serve` corriendo, abrir en Chrome a 1280×820 cada ruta (`/alarmas`, `/alarmas?estado=pasados`, `/alarmas?estado=borradores`, `/eventos/w-partido`, `/eventos/w-partido?pagina=2`, `/reportes`, `/qr`) y capturar. Guardar cada captura como `docs/verificacion/<código>.png` junto a la exportación del marco de Figma correspondiente como `<código>-figma.png`. Agregar una fila por pantalla en `docs/verificacion/README.md` (mismo formato que las filas de W00/W06 del Plan 3), con el estado de la comparación y las diferencias aceptadas (D3, D4, D8 y D9 de este plan, más las medidas redondeadas de los Steps 4b/6b/3b de las Tareas 2–4).

- [ ] **Step 4: Actualizar `README.md`**

Agregar una sección `## Plan 5 · Web de la Persona B` (siguiendo el formato de «Plan 3 · web de la Persona A»): páginas hechas (W01, W03, W04, W05), componentes L09 nuevos (indicador, chip, píldoras, campo de búsqueda, tabla, estado vacío, paginador, gráfica de barras, vista previa del afiche), tokens v1.13, decisiones D1–D9 de este plan, y actualizar la sección «Cómo continuar (Persona B)» para que la parte web diga que W01/W03/W04/W05 ya están hechas y que sigue el Plan 4 (móvil: M02b, M06–M11).

- [ ] **Step 5: Verificación completa y commit final**

```bash
cd apps/web
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../..
git add apps/web/src/app/flujos-persona-b.spec.ts docs/verificacion README.md
git commit -m "W01-W05: flujo T5 completo, verificación pixel-perfect y documentación del Plan 5"
```

- [ ] **Step 6: Abrir el PR**

```bash
git push -u origin feature/plan5-web-persona-b
gh pr create --title "Plan 5: web de la Persona B (W01, W03, W04, W05)" --body "$(cat <<'EOF'
## Resumen
- Componentes L09 de tablero: indicador, chip, píldoras, campo de búsqueda, tabla, estado vacío, paginador, gráfica de barras y vista previa del afiche.
- W01 Mis Alarmas (indicadores, gráfica, pestañas, filtros, búsqueda), W03 Detalle Evento (asistentes anónimos paginados), W04 Reportes y W05 Descargar QR.
- Tokens v1.13 y tipado completo del dataset de tablero (sin tocar dataset.json).

## Plan
docs/superpowers/plans/2026-09-21-plan5-web-persona-b.md

## Test plan
- [ ] `npx ng test --watch=false` en verde
- [ ] `npm run lint` en verde
- [ ] `npx ng build --configuration production` en verde
- [ ] Revisión pixel-perfect de W01/W03/W04/W05 contra Figma a 1280×820
EOF
)"
```

Expected: PR abierto contra `main`, listo para revisión de la Persona A. **No hacer merge sin su aprobación** (CLAUDE.md).

## Fuera de alcance

- Todo lo de la Persona B en móvil (M02b, M06, M07, M08, M09, M10, M11) — es el **Plan 4**, que se escribe y ejecuta después de este.
- Backend, autenticación y push reales; cualquier acción de exportación/descarga real de archivos (PDF, CSV, PNG) — la maquetación solo simula el resultado con snackbars y estados, como ya hace el resto de la app.
- Nuevos cortes de ancho o breakpoints además de los dos ya definidos (`--breakpoint-web-colapsar-barra`, `--breakpoint-web-apilar-columnas`); W01/W03/W04/W05 reutilizan `CortesService` tal como está.
- Cambios a `dataset.json`, a las rutas de `app.routes.ts`/`navegacion/pantallas.ts`, o a los componentes W00/W06 ya construidos por la Persona A (Plan 3) — si alguno tuviera un defecto, se corrige en un PR aparte, no dentro de este plan.
