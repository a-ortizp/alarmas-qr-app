# Plan 3 · Web de la Persona A — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Construir en `apps/web` los componentes L09 de la Persona A y sus páginas —W00 (inicio, error de credenciales, recuperar contraseña, correo enviado, cuenta eliminada), W06 (perfil, actualizado), el modal «Eliminar cuenta» y el diálogo transversal «¿Cerrar sesión?»—, junto con la barra superior y la barra lateral colapsable reales. Todo pixel-perfect contra Figma a 1280×820 y verificado con Vitest y capturas.

**Architecture:** Cada página es un componente independiente en `src/app/pantallas/<código-nombre>/` con estado en señales y formularios con Signal Forms (`@angular/forms/signals`). Los componentes L09 viven en `src/app/componentes/<nombre>/` con selector `aq-*` y solo usan variables de `tokens.css` (lo vigila un script en `npm run lint`). Los estados que la trazabilidad pone en la URL se leen del enrutador: el modal en `/perfil/eliminar`, el diálogo en `?dialogo=cerrar-sesion` y los snackbars de W00 en `?estado=…`. Se dibujan dentro del árbol del router con `cdkTrapFocus` (CDK a11y). Un único `SnackbarService` global muestra los avisos de 3 s. Los datos salen solo de `public/dataset.json` vía `DatosService`.

**Tech Stack:** Angular 22.1.7 (CLI/build 22.1.8, CDK 22.1.7) · TypeScript ~6.0.2 · Signal Forms · Vitest 4 (`@angular/build:unit-test`, jsdom) · Node 22.23.2 / npm 10.9.8.

**Spec:** `docs/superpowers/specs/2026-09-20-maquetacion-persona-a-design.md` (§3 web, §4 componentes, §5 método pixel-perfect, §6 pruebas). **Medidas:** `docs/superpowers/specs/2026-09-21-medidas-figma-web-persona-a.md` (anexo con los 9 marcos, la barra lateral, la barra superior y los iconos medidos en Figma; cada tarea cita su sección).

## Global Constraints

- **Node:** antes de cualquier `npm`/`npx` en `apps/web`, ejecutar `source ~/.nvm/nvm.sh && nvm use` (lee `.nvmrc` = 22.23.2). El Node del sistema (22.22.2) no arranca el CLI de Angular 22.
- **Versiones:** fijas las de `apps/web/package.json`. No se agregan dependencias: Signal Forms viene en `@angular/forms` y `cdkTrapFocus` en `@angular/cdk/a11y`.
- **Tokens:** nunca se escriben a mano colores, medidas en px, `rgb()`/`rgba()` ni radios en `src/app`. Solo se usan las variables `--color-*`, `--text-*`, `--size-*`, `--space-*`, `--radius-*`, `--stroke-*`, `--opacity-*`, `--elevation-*` y `--motion-*` de `tokens.css`. Lo verifica `node scripts/verificar-tokens.mjs`, que corre dentro de `npm run lint` (Tarea 1). Las coordenadas sin unidad dentro de un `<svg>` (`viewBox`, `d`, `rx`, `stroke-width`) no son medidas CSS y están permitidas.
- **Medidas nuevas:** una medida nueva se agrega primero a `packages/tokens/design-tokens.json` (v1.11) y luego a `packages/tokens/tokens.css` **y** `apps/web/src/tokens.css`. Los dos CSS deben quedar idénticos porque la CI los compara con `cmp`.
- **Dataset:** `dataset.json` no se modifica (móvil, web y `packages/tokens` son idénticos y la CI los compara).
- **Amarillo:** un solo elemento amarillo por pantalla, la acción principal. El logotipo es marca, no acción (D11). Estados activos en Tinta (píldora de la barra lateral, segmento activo, switch encendido).
- **Alturas:** botones web 44, campos 48, ítem de la barra lateral 38 (40×40 colapsada), control colapsar 36, barra superior 64, snackbar 36, selector segmentado 36.
- **Radios:** 14 en tarjetas, modal y diálogo web; 12 en campos; píldora en botones, snackbar, segmentos e ítems.
- **Texto:** ningún texto ≤ 15 px por debajo de 12. Los textos ≤ 15 px usan los tonos AA (`--color-texto-secundario` = Gris Texto, `--color-destructivo` = Coral Texto, `--color-enlace` = Azul Texto, `--color-exito` = Verde Texto). El placeholder va en Gris Medio (D3).
- **Prevención de errores:** «Eliminar mi cuenta» abre el modal con fricción: hay que escribir ELIMINAR y «Conservar mi cuenta» es el primario amarillo. «Cerrar Sesión» abre `aq-dialogo-confirmacion`: velo 55 %, 420 de ancho, «Cancelar» como primario y «Cerrar sesión» en contorno Tinta. Tocar el velo o pulsar Escape equivale a la acción segura.
- **Movimiento:** las transiciones usan `var(--motion-transicion)` (250 ms). `styles.css` ya respeta `prefers-reduced-motion`.
- **Textos visibles:** todos en español y **exactamente** los del anexo de medidas; el dataset manda en los textos que tiene (`web.acceso`, `mensajes`, `web.eliminarCuenta`). Los identificadores pueden ir en español o en inglés.
- **Nombres de archivo y raíz de página:** cada página vive en `src/app/pantallas/<código en minúsculas>-<nombre>/<código>-<nombre>.component.ts` con selector `aq-<código>-<nombre>`. Su elemento raíz lleva `data-codigo="<código>"`, y `data-estado="<estado>"` en los subestados. Así las pruebas navegan por código, igual que `testTag("pantalla-M04")` en el móvil.
- **Commits:** empiezan por el código de pantalla o por `L09` para los componentes (p. ej. «W06: modal Eliminar cuenta»), con al menos un commit por tarea. Todos terminan con la línea `Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>`. La rama es `feature/plan3-web-persona-a` (D1).
- **Verificación por tarea:** desde `apps/web`, ejecutar `npx prettier --write "src/**/*.{ts,html,css}"` y después `npx ng test --watch=false && npm run lint && npx ng build --configuration production`. Las tres cosas deben quedar en verde al cerrar cada tarea. El presupuesto de estilos por componente del build es de 4 kB de aviso y 8 kB de error.

## Decisiones tomadas al escribir el plan (revisar si se discrepa)

- **D1 · Rama:** todo el Plan 3 va en una sola rama, `feature/plan3-web-persona-a`, con commits prefijados por pantalla y un PR revisado por la Persona B. Es el mismo criterio que el D1 del Plan 2. La rama ya existe: se creó al guardar el anexo de medidas.
- **D2 · Modal «Eliminar cuenta» según el mockup** (elegido por el usuario el 2026-09-21): 481 de ancho, relleno 22, separación 16, borde Coral Texto de 1.5, icono de advertencia de 24 y título Bricolage 700 de 18, **sin** fila miga + ✕. Se cierra con «Conservar mi cuenta», con Escape o tocando el velo (45 %), y siempre vuelve a `/perfil`. Los tokens `modal-web` (540–600 → 481), `space.web.modal` (24 → 22) y `titulo-modal-web` (21 → 18) cambian. **Pendiente en el repo de UX:** actualizar la fila «Diálogo modal» de DS §7 y la regla «La fila de cabecera de un modal (miga + ✕)» de CLAUDE.md.
- **D3 · Campos según las reglas** (elegido por el usuario el 2026-09-21, igual que el D10 del Plan 2): campo de 48 (Figma 46; en el modal 40), etiqueta Archivo SemiBold 12 (Figma 11) y placeholder en Gris Medio (Figma `#DAD8D2` y `#BFBDBD`, que no son tonos AA).
- **D4 · Medidas no normadas según el mockup:** tarjeta de acceso de 520 (el token y el DS §7 decían 400; el token cambia), relleno 34 y separación 20. Tarjetas de W06 con relleno 22 (20 en «Eliminación de cuenta»). Tarjeta «Perfil» de ancho fijo 561 y columna derecha flexible. Barra lateral blanca con borde derecho Gris Borde de 1, sin marca (la marca está en la barra superior). Todo entra como tokens v1.11.
- **D5 · Botones uniformes:** todos usan `--text-boton-web` (600 14.5) y el relleno 13/24. Figma trae 15 en «Iniciar sesión» y «Enviar enlace», y 14 en los botones del diálogo. Los botones a ancho completo usan el atributo `bloque`.
- **D6 · Modal y diálogo en el árbol del router con `cdkTrapFocus`,** en vez de `CdkOverlay` como pedía la spec §3.2. El estado vive en la URL (`/perfil/eliminar`, `?dialogo=cerrar-sesion`), así que `RouterTestingHarness` los ve y el botón atrás del navegador los cierra.
- **D7 · Snackbar global:** hay uno solo (`SnackbarService` + `aq-snackbar` en `App`), dura 3 s y va centrado en la ventana a 24 del borde inferior. En Figma el snackbar de W06 está a 22 y el de «correo enviado» está descentrado (x = 509); se toma un único criterio.
- **D8 · Error de credenciales ⏩:** el primer foco en el campo contraseña enciende el estado de error (spec §3.2, NAVEGACION §6b). «Iniciar sesión» lleva siempre a `/alarmas`. Los marcos de Figma con snackbar no muestran «¿Olvidaste tu contraseña?»; la implementación lo mantiene por coherencia con W00 (anotar en UX).
- **D9 · Switch apagado:** Figma no lo dibuja. Se hace en contorno Tinta de 1.5 con la perilla Tinta a la izquierda, como el `Interruptor` móvil; así el estado se lee por posición además de por color. La perilla va centrada en vertical (en Figma está 1 px más arriba).
- **D10 · Sombra del diálogo en Tinta** (`0 12px 32px` a 18 %), como las demás elevaciones. Figma usa negro puro.
- **D11 · Logotipo amarillo:** convive con el primario amarillo en W00 y en la barra superior. Es la marca, no una acción, y queda como excepción documentada (anotar en UX).
- **D12 · Iconos como datos:** `iconos.ts` guarda cada icono como una lista de trazados `d`; los círculos y rectángulos de Figma se convierten a trazados. Se dibujan con `<path>` en la plantilla y trazo `currentColor`, sin `innerHTML` ni imágenes.
- **D13 · Signal Forms:** `aq-campo` y `aq-selector-segmentado` implementan `FormValueControl<string>` y `aq-switch` implementa `FormCheckboxControl`, así que funcionan con `[formField]`.
- **D14 · Comillas rectas:** «Así apareces en "Quiénes escanearon" (Ley 1581).» y «Contar mi "Ya voy" en las métricas» se copian tal cual del mockup.
- **D15 · Vitest, no Karma:** la spec §1/§6 dice Karma + Jasmine, pero el repo usa Vitest desde la Fase 0 y CLAUDE.md también lo pide.
- **D16 · Sin `aq-cabecera-modal` ni `aq-item-barra-lateral` como componentes:** el único modal no lleva cabecera de miga (D2), y el ítem de la barra lateral es marcado interno de `aq-barra-lateral`. La Persona B puede extraerlos si los necesita.

## Mapa de archivos

```
packages/tokens/design-tokens.json                        modificar · v1.11: tarjeta-acceso 520, modal-web 481, titulo-modal-web 18, size.web, space.web, font.scale web, elevation.dialogo-web, motion.snackbar-web
packages/tokens/tokens.css · apps/web/src/tokens.css      modificar · mismas adiciones (idénticos)
apps/web/package.json                                     modificar · lint = prettier + verificar-tokens
apps/web/scripts/verificar-tokens.mjs                     crear · guardia de valores a mano en src/app
apps/web/tsconfig.app.json                                modificar · excluir src/testing
apps/web/src/testing/datos-prueba.ts                      crear · proveedoresPrueba(), cargarDataset()
apps/web/src/styles.css                                   modificar · utilidad .solo-lector
apps/web/src/app/
  app.config.ts                                           modificar · withComponentInputBinding()
  app.ts · app.html                                       modificar · <aq-snackbar /> global
  app.routes.ts                                           modificar · /login/recuperar, /perfil/eliminar
  datos/modelos.ts · datos/datos.service.ts               modificar · acceso, barraLateral, eliminarCuenta
  navegacion/barra-lateral.ts                             crear · ITEMS_BARRA_LATERAL, ITEM_CERRAR_SESION
  componentes/icono/iconos.ts                             crear · ICONOS (trazados de 00 · Recursos gráficos)
  componentes/icono/aq-icono.component.ts                 crear
  componentes/logotipo/aq-logotipo.component.ts           crear
  componentes/boton/aq-boton.component.ts                 crear · button[aq-boton], a[aq-boton]
  componentes/enlace/aq-enlace.component.ts               crear · a[aq-enlace], button[aq-enlace]
  componentes/campo/aq-campo.component.ts                 crear · FormValueControl<string>
  componentes/tarjeta/aq-tarjeta.component.ts             crear · normal / peligro
  componentes/tarjeta-acceso/aq-tarjeta-acceso.component.ts crear
  componentes/snackbar/snackbar.service.ts                crear
  componentes/snackbar/aq-snackbar.component.ts           crear
  componentes/switch/aq-switch.component.ts               crear · FormCheckboxControl
  componentes/selector-segmentado/aq-selector-segmentado.component.ts crear · FormValueControl<string>
  componentes/modal/aq-modal.component.ts                 crear
  componentes/dialogo-confirmacion/aq-dialogo-confirmacion.component.ts crear
  componentes/barra-superior/aq-barra-superior.component.ts crear
  componentes/barra-lateral/aq-barra-lateral.component.ts crear
  layout/aq-layout-app/aq-layout-app.component.ts         modificar · barras reales, colapsar, diálogo ¿Cerrar sesión?
  pantallas/pantalla-marcador/pantalla-marcador.component.ts modificar · sin relleno propio (lo pone <main>)
  pantallas/w00-login/w00-login.component.ts              modificar · página real + estados
  pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component.ts crear
  pantallas/w06-perfil/w06-perfil.component.ts            modificar · página real + actualizado + <router-outlet>
  pantallas/w06-modal-eliminar-cuenta/w06-modal-eliminar-cuenta.component.ts crear
apps/web/src/app/**/*.spec.ts                             crear · una por componente/página + flujos-persona-a.spec.ts
docs/verificacion/W*.png · docs/verificacion/README.md    crear/modificar · parejas Figma / implementación (web)
README.md · CLAUDE.md                                     modificar · Plan 3, decisiones, tokens v1.11, cómo continuar (web)
```

---

### Task 1: Tokens v1.11, guardia de tokens, datos de acceso y ayudante de pruebas

**Files:**
- Modify: `packages/tokens/design-tokens.json`
- Modify: `packages/tokens/tokens.css`, `apps/web/src/tokens.css`
- Create: `apps/web/scripts/verificar-tokens.mjs`
- Modify: `apps/web/package.json` (script `lint`)
- Modify: `apps/web/tsconfig.app.json`
- Create: `apps/web/src/testing/datos-prueba.ts`
- Modify: `apps/web/src/app/app.config.ts`
- Modify: `apps/web/src/app/datos/modelos.ts`, `apps/web/src/app/datos/datos.service.ts`
- Test: `apps/web/src/app/datos/datos.service.spec.ts`

**Interfaces:**
- Consumes: `DatosService` (`dataset`, `usuario`, `mensajes`, `web`, `alarmas`), `Dataset`/`DatosWeb` de `modelos.ts` y `SesionService`, todos de la Fase 0.
- Produces:
  - Variables CSS nuevas (sección «web · Plan 3» de `tokens.css`): `--text-marca-acceso`, `--text-titulo-tarjeta-web`, `--text-cuerpo-web`, `--text-descripcion-web`, `--text-rotulo-web`, `--text-valor-campo`, `--text-etiqueta-campo`, `--text-etiqueta-externa`, `--text-nota-acceso`, `--text-snackbar-web`, `--size-logo-acceso`, `--size-logo-barra`, `--size-avatar-web`, `--size-icono-snackbar`, `--size-icono-advertencia`, `--size-icono-vineta`, `--size-pildora-colapsada`, `--size-switch-ancho`, `--size-switch-alto`, `--size-switch-perilla`, `--size-tarjeta-perfil-web`, `--size-modal-web`, `--space-tarjeta-acceso`, `--space-tarjeta-acceso-gap`, `--space-tarjeta-web`, `--space-tarjeta-peligro`, `--space-campo-x`, `--space-snackbar-web`, `--space-snackbar-web-borde`, `--space-barra-lateral-web`, `--space-barra-superior-web`, `--space-item-barra-lateral`, `--space-segmentado`, `--space-2`, `--space-4`, `--space-8`, `--space-10`, `--space-12`, `--space-16`, `--opacity-deshabilitado`, `--elevation-dialogo-web`. Cambian `--size-tarjeta-acceso` (520), `--text-titulo-modal-web` (18) y `--space-web-modal` (22). Desaparecen `--size-modal-web-min` y `--size-modal-web-max`.
  - `interface Acceso { correoEjemplo: string; errorCredenciales: string; recuperar: { titulo: string; texto: string; boton: string; volver: string } }`, `interface BarraLateralDatos { items: { id: string; icono: string }[]; anchoExpandida: number; anchoColapsada: number }`, y `DatosWeb.acceso` y `DatosWeb.barraLateral`.
  - `DatosService.acceso: Signal<Acceso | undefined>` y `DatosService.eliminarCuenta: Signal<DatosWeb['eliminarCuenta'] | undefined>`.
  - `proveedoresPrueba(rutas?: Routes): (Provider | EnvironmentProviders)[]` y `cargarDataset(): Promise<void>` en `src/testing/datos-prueba.ts`.
  - `app.config.ts` con `provideRouter(routes, withComponentInputBinding())`: los query params (`?estado=`) llegan como `input()` de la página.
  - `npm run lint` = prettier + `node scripts/verificar-tokens.mjs`.

- [ ] **Step 1: Confirmar la rama y el entorno**

```bash
cd /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app
git branch --show-current            # debe decir feature/plan3-web-persona-a
cd apps/web && source ~/.nvm/nvm.sh && nvm use && npx ng test --watch=false
```

Expected: rama `feature/plan3-web-persona-a`; `Tests 6 passed (6)`.

- [ ] **Step 2: Escribir la prueba de los datos nuevos (falla: `acceso` y `eliminarCuenta` no existen)**

Reemplazar `apps/web/src/app/datos/datos.service.spec.ts` completo:

```ts
import { TestBed } from '@angular/core/testing';
import { DatosService } from './datos.service';
import { cargarDataset, proveedoresPrueba } from '../../testing/datos-prueba';

describe('DatosService', () => {
  beforeEach(() => TestBed.configureTestingModule({ providers: proveedoresPrueba() }));

  it('carga dataset.json y expone usuario y mensajes como señales', async () => {
    const servicio = TestBed.inject(DatosService);
    await cargarDataset();
    expect(servicio.usuario()?.nombre).toBe('Andrés Rojas');
    expect(servicio.mensajes()?.cuentaEliminada).toBe('Cuenta eliminada exitosamente');
    expect(servicio.web()?.indicadores.escaneosTotales.valor).toBe(128);
  });

  it('expone los textos de acceso y los datos de «Eliminar cuenta»', async () => {
    const servicio = TestBed.inject(DatosService);
    await cargarDataset();
    expect(servicio.acceso()?.errorCredenciales).toBe(
      'Correo o contraseña incorrectos. Inténtalo de nuevo o recupera tu contraseña.',
    );
    expect(servicio.acceso()?.recuperar.boton).toBe('Enviar enlace');
    expect(servicio.eliminarCuenta()?.palabraDeConfirmacion).toBe('ELIMINAR');
    expect(servicio.web()?.barraLateral.items.map((i) => i.id)).toEqual([
      'mis-alarmas',
      'reportes',
      'descargar-qr',
      'ajustes',
      'cerrar-sesion',
    ]);
  });
});
```

- [ ] **Step 3: Crear el ayudante de pruebas**

`apps/web/src/testing/datos-prueba.ts`:

```ts
import { ApplicationRef, EnvironmentProviders, Provider, provideZonelessChangeDetection } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Routes, withComponentInputBinding } from '@angular/router';
import dataset from '../../public/dataset.json';

/** Proveedores comunes de las pruebas: sin zone.js, HTTP de prueba y enrutador con query params como inputs. */
export function proveedoresPrueba(rutas: Routes = []): (Provider | EnvironmentProviders)[] {
  return [
    provideZonelessChangeDetection(),
    provideHttpClient(),
    provideHttpClientTesting(),
    provideRouter(rutas, withComponentInputBinding()),
  ];
}

/** Responde la petición de dataset.json (única fuente de datos) y espera a que la app se estabilice. */
export async function cargarDataset(): Promise<void> {
  TestBed.tick();
  const http = TestBed.inject(HttpTestingController);
  for (const peticion of http.match('dataset.json')) peticion.flush(dataset);
  await TestBed.inject(ApplicationRef).whenStable();
}
```

En `apps/web/tsconfig.app.json`, cambiar el bloque `exclude` para que el build de la app no compile el ayudante:

```json
  "exclude": [
    "src/**/*.spec.ts",
    "src/testing/**"
  ]
```

- [ ] **Step 4: Ejecutar la prueba y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL en `datos.service.spec.ts` con un error de TypeScript: la propiedad `acceso` no existe en `DatosService` (y lo mismo con `eliminarCuenta` y `barraLateral`).

- [ ] **Step 5: Modelos y servicio**

En `apps/web/src/app/datos/modelos.ts`, reemplazar la interfaz `DatosWeb` completa por:

```ts
export interface Acceso {
  correoEjemplo: string;
  errorCredenciales: string;
  recuperar: { titulo: string; texto: string; boton: string; volver: string };
}

export interface BarraLateralDatos {
  items: { id: string; icono: string }[];
  anchoExpandida: number;
  anchoColapsada: number;
}

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
}
```

En `apps/web/src/app/datos/datos.service.ts`, agregar debajo de `readonly alarmas = …`:

```ts
  readonly acceso = computed(() => this.dataset.value()?.web.acceso);
  readonly eliminarCuenta = computed(() => this.dataset.value()?.web.eliminarCuenta);
```

En `apps/web/src/app/app.config.ts`, reemplazar el archivo completo:

```ts
import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(),
  ],
};
```

- [ ] **Step 6: Ejecutar las pruebas y verificar que pasan**

Run: `npx ng test --watch=false`
Expected: PASS, `Tests 7 passed (7)`.

- [ ] **Step 7: Tokens v1.11 en `design-tokens.json`**

Aplicar estas seis ediciones exactas en `packages/tokens/design-tokens.json`:

1. Cabecera: reemplazar

```json
    "version": "1.10",
    "date": "2026-09-20",
```

por

```json
    "version": "1.11",
    "date": "2026-09-21",
```

2. Notas: reemplazar el final de la nota v1.10 `medidas size.movil)."` + salto + `    ]` por:

```json
medidas size.movil).",
      "v1.11 (2026-09-21): medidas web de la Persona A tomadas de Figma para el Plan 3 (tarjeta de acceso 520, modal «Eliminar cuenta» 481 con relleno 22 y título 18, textos y medidas size.web/space.web, sombra del diálogo web, snackbar web 3 s). Campos 48 con etiqueta 12 por regla (Figma 46/11)."
    ]
```

3. Tipografía: reemplazar el bloque

```json
      "titulo-modal-web": {
        "family": "titulares",
        "weight": 700,
        "size": 21
      },
```

por

```json
      "titulo-modal-web": {
        "family": "titulares",
        "weight": 700,
        "size": 18,
        "$description": "Modal «Eliminar cuenta» (web v1.5, Figma 18; antes 21)"
      },
      "marca-acceso": { "family": "titulares", "weight": 700, "size": 24 },
      "titulo-tarjeta-web": { "family": "titulares", "weight": 700, "size": 17 },
      "cuerpo-web": { "family": "ui", "weight": 400, "size": 14 },
      "descripcion-web": { "family": "ui", "weight": 400, "size": 13.5 },
      "rotulo-web": { "family": "ui", "weight": 400, "size": 14.5 },
      "valor-campo": { "family": "ui", "weight": 400, "size": 15 },
      "etiqueta-campo": { "family": "ui", "weight": 600, "size": 12, "$description": "Figma 11; nada por debajo de 12 (Plan 3 D3)" },
      "etiqueta-externa": { "family": "ui", "weight": 600, "size": 13 },
      "nota-acceso": { "family": "ui", "weight": 400, "size": 13 },
      "snackbar-web": { "family": "ui", "weight": 600, "size": 14 },
```

4. Tamaños: reemplazar

```json
    "modal-web": {
      "min": 540,
      "max": 600
    },
    "tarjeta-acceso": 400,
```

por

```json
    "modal-web": {
      "width": 481,
      "$description": "Modal «Eliminar cuenta» (web v1.5, Figma 481; el DS §7 decía 540–600)"
    },
    "tarjeta-acceso": 520,
```

   y, justo después de la línea `    "tarjeta-formulario-web": 600,`, insertar:

```json
    "web": {
      "logo-acceso": 36, "logo-barra": 32, "avatar": 32, "icono-snackbar": 16, "icono-advertencia": 24,
      "icono-vineta": 16, "pildora-colapsada": 40, "perilla-switch": 18, "tarjeta-perfil": 561,
      "opacidad-deshabilitado": 0.45,
      "$description": "Medidas de los mockups web v1.5 medidas en Figma el 2026-09-21 (Plan 3)"
    },
```

5. Espacio: dentro de `space.web`, reemplazar

```json
      "modal": 24,
      "entre-indicadores": 14
    },
```

por

```json
      "modal": 22,
      "entre-indicadores": 14,
      "tarjeta-acceso": 34, "tarjeta-acceso-gap": 20, "tarjeta": 22, "tarjeta-peligro": 20, "campo-horizontal": 14,
      "snackbar": [10, 18], "snackbar-borde": 24, "item-barra-lateral": [9, 16], "segmentado": 3,
      "escala": [2, 4, 8, 10, 12, 16]
    },
```

6. Elevación y movimiento: reemplazar

```json
    "tarjeta": "0 4px 12px rgba(23, 22, 28, 0.18)"
```

por

```json
    "tarjeta": "0 4px 12px rgba(23, 22, 28, 0.18)",
    "dialogo-web": "0 12px 32px rgba(23, 22, 28, 0.18)"
```

   y agregar al objeto `motion`, después de la entrada `"ignorar-relectura": {…}`, una coma y:

```json
    "snackbar-web": {
      "duration": 3000,
      "$description": "Snackbar web (W00 correo enviado / cuenta eliminada, W06 actualizado): After delay 3 s"
    }
```

   Por último, reemplazar el `"version": "1.10"` final (el que va sin coma, antes de la llave de cierre) por `"version": "1.11"`.

Comprobar que el JSON sigue siendo válido:

```bash
cd /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app
python3 -c "import json;d=json.load(open('packages/tokens/design-tokens.json'));print(d['meta']['version'], d['size']['tarjeta-acceso'], d['size']['web']['pildora-colapsada'], d['motion']['snackbar-web']['duration'])"
```

Expected: `1.11 520 40 3000`.

- [ ] **Step 8: Tokens v1.11 en los dos `tokens.css`**

En `packages/tokens/tokens.css`:

1. Primera línea: `/* Alarmas QR · Energía puntual · tokens v1.9 (2026-09-19)` → `/* Alarmas QR · Energía puntual · tokens v1.11 (2026-09-21)`.
2. `  --text-titulo-modal-web: 700 21px/1.2 var(--font-titulares);` → `  --text-titulo-modal-web: 700 18px/1.2 var(--font-titulares);`
3. Reemplazar las dos líneas `  --size-modal-web-min: 540px;` y `  --size-modal-web-max: 600px;` por una sola: `  --size-modal-web: 481px;`
4. `  --size-tarjeta-acceso: 400px;` → `  --size-tarjeta-acceso: 520px;`
5. `  --space-web-modal: 24px;` → `  --space-web-modal: 22px;`
6. Insertar justo antes de la línea `  /* movimiento y elevación */`:

```css
  /* web · medidas de Figma para la Persona A (Plan 3, v1.11) */
  --text-marca-acceso: 700 24px/normal var(--font-titulares);
  --text-titulo-tarjeta-web: 700 17px/normal var(--font-titulares);
  --text-cuerpo-web: 400 14px/normal var(--font-ui);          /* subtítulos, enlaces, notas, nombre en la barra */
  --text-descripcion-web: 400 13.5px/normal var(--font-ui);   /* descripciones de tarjeta (W06) */
  --text-rotulo-web: 400 14.5px/normal var(--font-ui);        /* rótulos de switch, lista del modal */
  --text-valor-campo: 400 15px/normal var(--font-ui);
  --text-etiqueta-campo: 600 12px/normal var(--font-ui);      /* Figma 11; nada por debajo de 12 */
  --text-etiqueta-externa: 600 13px/normal var(--font-ui);    /* «Escribe ELIMINAR para confirmar» */
  --text-nota-acceso: 400 13px/normal var(--font-ui);
  --text-snackbar-web: 600 14px/normal var(--font-ui);
  --size-logo-acceso: 36px;
  --size-logo-barra: 32px;
  --size-avatar-web: 32px;
  --size-icono-snackbar: 16px;
  --size-icono-advertencia: 24px;
  --size-icono-vineta: 16px;
  --size-pildora-colapsada: 40px;
  --size-switch-ancho: 42px;
  --size-switch-alto: 24px;
  --size-switch-perilla: 18px;
  --size-tarjeta-perfil-web: 561px;
  --space-tarjeta-acceso: 34px;
  --space-tarjeta-acceso-gap: 20px;
  --space-tarjeta-web: 22px;
  --space-tarjeta-peligro: 20px;
  --space-campo-x: 14px;
  --space-snackbar-web: 10px 18px;
  --space-snackbar-web-borde: 24px;
  --space-barra-lateral-web: 20px 12px;
  --space-barra-superior-web: 28px;
  --space-item-barra-lateral: 9px 16px;
  --space-segmentado: 3px;
  --space-2: 2px;
  --space-4: 4px;
  --space-8: 8px;
  --space-10: 10px;
  --space-12: 12px;
  --space-16: 16px;
  --opacity-deshabilitado: 0.45;
  --elevation-dialogo-web: 0 12px 32px rgba(23, 22, 28, 0.18);

```

Copiar el resultado a la web y comprobar que son idénticos:

```bash
cp packages/tokens/tokens.css apps/web/src/tokens.css
cmp packages/tokens/tokens.css apps/web/src/tokens.css && echo IDENTICOS
grep -c "size-modal-web-min\|size-modal-web-max" apps/web/src/tokens.css
```

Expected: `IDENTICOS` y `0`.

- [ ] **Step 9: Guardia de valores a mano**

`apps/web/scripts/verificar-tokens.mjs`:

```js
// Falla si src/app escribe a mano un color, un rgb()/rgba() o una medida en px: todo sale de tokens.css.
import { readdirSync, readFileSync, statSync } from 'node:fs';
import { join } from 'node:path';
import { fileURLToPath } from 'node:url';

const RAIZ = fileURLToPath(new URL('../src/app', import.meta.url));
const PROHIBIDOS = [
  { nombre: 'color hexadecimal', patron: /#[0-9a-fA-F]{3,8}\b/g },
  { nombre: 'rgb()/rgba()', patron: /\brgba?\(/g },
  { nombre: 'medida en px', patron: /\b\d+(\.\d+)?px\b/g },
];

function* archivos(dir) {
  for (const nombre of readdirSync(dir)) {
    const ruta = join(dir, nombre);
    if (statSync(ruta).isDirectory()) yield* archivos(ruta);
    else if (/\.(ts|css|html)$/.test(nombre) && !nombre.endsWith('.spec.ts')) yield ruta;
  }
}

let fallos = 0;
for (const archivo of archivos(RAIZ)) {
  readFileSync(archivo, 'utf8')
    .split('\n')
    .forEach((linea, i) => {
      for (const { nombre, patron } of PROHIBIDOS) {
        for (const m of linea.matchAll(patron)) {
          console.error(`${archivo}:${i + 1}: ${nombre} «${m[0]}» — usa una variable de tokens.css`);
          fallos++;
        }
      }
    });
}
if (fallos > 0) {
  console.error(`\n${fallos} valor(es) escritos a mano en src/app.`);
  process.exit(1);
}
console.log('Tokens: ningún color ni medida a mano en src/app.');
```

En `apps/web/package.json`, cambiar el script `lint`:

```json
    "lint": "prettier --check \"src/**/*.{ts,html,css}\" && node scripts/verificar-tokens.mjs"
```

Comprobar que detecta un valor a mano y que el árbol actual pasa:

```bash
cd apps/web
printf '.x { color: #fff; margin: 3px; }\n' > src/app/prueba-guardia.css
node scripts/verificar-tokens.mjs; echo "salida=$?"
rm src/app/prueba-guardia.css
node scripts/verificar-tokens.mjs
```

Expected: la primera corrida lista `color hexadecimal «#fff»` y `medida en px «3px»` y termina con `salida=1`; la segunda imprime `Tokens: ningún color ni medida a mano en src/app.`

- [ ] **Step 10: Verificación completa y commit**

```bash
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../.. && git add packages/tokens apps/web docs/superpowers
git commit -m "$(cat <<'EOF'
L09: tokens v1.11 de la web, guardia de tokens y datos de acceso

Medidas de Figma de W00/W06 (anexo 2026-09-21), tarjeta de acceso 520,
modal 481, ayudante de pruebas y query params como inputs.

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

Expected: 7 pruebas en verde, lint y build sin errores y commit creado. El commit incluye el anexo de medidas y este plan si todavía no estaban versionados.

---

### Task 2: Componentes base L09 — icono, logotipo, botón, enlace, campo y tarjetas

**Files:**
- Create: `apps/web/src/app/componentes/icono/iconos.ts`
- Create: `apps/web/src/app/componentes/icono/aq-icono.component.ts`
- Create: `apps/web/src/app/componentes/logotipo/aq-logotipo.component.ts`
- Create: `apps/web/src/app/componentes/boton/aq-boton.component.ts`
- Create: `apps/web/src/app/componentes/enlace/aq-enlace.component.ts`
- Create: `apps/web/src/app/componentes/campo/aq-campo.component.ts`
- Create: `apps/web/src/app/componentes/tarjeta/aq-tarjeta.component.ts`
- Create: `apps/web/src/app/componentes/tarjeta-acceso/aq-tarjeta-acceso.component.ts`
- Test: `apps/web/src/app/componentes/componentes-base.spec.ts`

**Interfaces:**
- Consumes: los tokens de la Tarea 1 y `proveedoresPrueba()`.
- Produces:
  - `type NombreIcono = 'alarma' | 'reportes' | 'escanear' | 'ajustes' | 'cerrar-sesion' | 'colapsar' | 'advertencia' | 'qr' | 'calendario' | 'persona' | 'descarga'`, `interface DefinicionIcono { caja: 16 | 24; trazo: number; formas: readonly { d: string; relleno?: boolean }[] }` y `const ICONOS: Record<NombreIcono, DefinicionIcono>`.
  - `<aq-icono nombre="alarma" tamano="barra|vineta|advertencia" />`: toma el color de `color` (currentColor) y trae `aria-hidden`.
  - `<aq-logotipo tamano="acceso|barra" />`: 36 o 32.
  - `<button aq-boton variante="primario|secundario|destructivo" bloque>` y también `<a aq-boton …>`: `AqBotonComponent` con los inputs `variante` y `bloque`.
  - `<a aq-enlace bloque>` y `<button aq-enlace>`: `AqEnlaceComponent`.
  - `<aq-campo etiqueta tipo placeholder idEntrada error mensajeError [formField] (enfocado)>`: `AqCampoComponent implements FormValueControl<string>` con `value = model('')` y `touch`/`enfocado` como `output<void>()`.
  - `<aq-tarjeta variante="normal|peligro">`: contenedor en columna. El `gap` lo pone la página con una clase.
  - `<aq-tarjeta-acceso>`: tarjeta de 520 con el logotipo y «Alarmas QR» arriba y el contenido proyectado debajo, con separación 20.

- [ ] **Step 1: Escribir las pruebas (fallan: los componentes no existen)**

`apps/web/src/app/componentes/componentes-base.spec.ts`:

```ts
import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { form, FormField } from '@angular/forms/signals';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqBotonComponent } from './boton/aq-boton.component';
import { AqEnlaceComponent } from './enlace/aq-enlace.component';
import { AqIconoComponent } from './icono/aq-icono.component';
import { ICONOS } from './icono/iconos';
import { AqCampoComponent } from './campo/aq-campo.component';
import { AqTarjetaComponent } from './tarjeta/aq-tarjeta.component';
import { AqTarjetaAccesoComponent } from './tarjeta-acceso/aq-tarjeta-acceso.component';

@Component({
  imports: [
    AqBotonComponent,
    AqEnlaceComponent,
    AqIconoComponent,
    AqCampoComponent,
    AqTarjetaComponent,
    AqTarjetaAccesoComponent,
    FormField,
  ],
  template: `
    <button aq-boton id="primario" type="button">Guardar cambios</button>
    <button aq-boton id="destructivo" variante="destructivo" bloque type="button" disabled>Eliminar</button>
    <a aq-enlace id="enlace" href="/login/recuperar">¿Olvidaste tu contraseña?</a>
    <aq-icono id="icono" nombre="alarma" />
    <aq-campo
      id="campo"
      etiqueta="CORREO ELECTRÓNICO"
      placeholder="nombre@correo.com"
      [(value)]="correo"
      [error]="error()"
      [mensajeError]="error() ? 'Correo o contraseña incorrectos.' : ''"
      (enfocado)="enfoques = enfoques + 1"
    />
    <aq-campo id="con-formulario" etiqueta="ALIAS PÚBLICO" [formField]="formulario.alias" />
    <aq-tarjeta id="peligro" variante="peligro"><p>Eliminación de cuenta</p></aq-tarjeta>
    <aq-tarjeta-acceso id="acceso"><p class="proyectado">Administración</p></aq-tarjeta-acceso>
  `,
})
class Anfitrion {
  readonly correo = signal('andres@correo.com');
  readonly error = signal(false);
  readonly formulario = form(signal({ alias: 'Andrés R.' }));
  enfoques = 0;
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes base L09', () => {
  it('aq-boton aplica variante y ancho completo', async () => {
    const f = await montar();
    const raiz: HTMLElement = f.nativeElement;
    expect(raiz.querySelector('#primario')!.classList).toContain('primario');
    const destructivo = raiz.querySelector('#destructivo')!;
    expect(destructivo.classList).toContain('destructivo');
    expect(destructivo.classList).toContain('bloque');
  });

  it('aq-enlace es un enlace nativo con su texto', async () => {
    const f = await montar();
    const enlace = (f.nativeElement as HTMLElement).querySelector('#enlace') as HTMLAnchorElement;
    expect(enlace.tagName).toBe('A');
    expect(enlace.textContent?.trim()).toBe('¿Olvidaste tu contraseña?');
  });

  it('aq-icono dibuja los trazados del DS en currentColor y oculto al lector', async () => {
    const f = await montar();
    const svg = (f.nativeElement as HTMLElement).querySelector('#icono svg')!;
    expect(svg.getAttribute('aria-hidden')).toBe('true');
    expect(svg.getAttribute('viewBox')).toBe('0 0 24 24');
    expect(svg.querySelectorAll('path').length).toBe(ICONOS.alarma.formas.length);
  });

  it('cada icono tiene caja 16 o 24 y al menos un trazado', () => {
    for (const [nombre, def] of Object.entries(ICONOS)) {
      expect([16, 24], nombre).toContain(def.caja);
      expect(def.formas.length, nombre).toBeGreaterThan(0);
    }
  });

  it('aq-campo refleja el modelo, lo actualiza al escribir y avisa el foco', async () => {
    const f = await montar();
    const anfitrion = f.componentInstance;
    const campo = (f.nativeElement as HTMLElement).querySelector('#campo')!;
    const entrada = campo.querySelector('input')!;
    expect(campo.textContent).toContain('CORREO ELECTRÓNICO');
    expect(entrada.value).toBe('andres@correo.com');
    expect(entrada.placeholder).toBe('nombre@correo.com');
    entrada.value = 'ana@correo.com';
    entrada.dispatchEvent(new Event('input'));
    expect(anfitrion.correo()).toBe('ana@correo.com');
    entrada.dispatchEvent(new FocusEvent('focus'));
    expect(anfitrion.enfoques).toBe(1);
  });

  it('aq-campo en error: borde de error, aria-invalid y mensaje enlazado', async () => {
    const f = await montar();
    f.componentInstance.error.set(true);
    await f.whenStable();
    const campo = (f.nativeElement as HTMLElement).querySelector('#campo')!;
    const entrada = campo.querySelector('input')!;
    expect(campo.querySelector('.caja')!.classList).toContain('error');
    expect(entrada.getAttribute('aria-invalid')).toBe('true');
    const mensaje = campo.querySelector('.mensaje-error')!;
    expect(mensaje.textContent).toContain('Correo o contraseña incorrectos.');
    expect(entrada.getAttribute('aria-describedby')).toBe(mensaje.id);
  });

  it('aq-campo funciona con Signal Forms ([formField])', async () => {
    const f = await montar();
    const entrada = (f.nativeElement as HTMLElement).querySelector('#con-formulario input') as HTMLInputElement;
    expect(entrada.value).toBe('Andrés R.');
    entrada.value = 'Andrés';
    entrada.dispatchEvent(new Event('input'));
    expect(f.componentInstance.formulario.alias().value()).toBe('Andrés');
  });

  it('aq-tarjeta peligro y aq-tarjeta-acceso con marca y contenido proyectado', async () => {
    const f = await montar();
    const raiz: HTMLElement = f.nativeElement;
    expect(raiz.querySelector('#peligro')!.classList).toContain('peligro');
    const acceso = raiz.querySelector('#acceso')!;
    expect(acceso.querySelector('aq-logotipo')).not.toBeNull();
    expect(acceso.textContent).toContain('Alarmas QR');
    expect(acceso.querySelector('.proyectado')?.textContent).toBe('Administración');
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL. No se resuelven los imports `./boton/aq-boton.component` y los demás.

- [ ] **Step 3: Iconos**

`apps/web/src/app/componentes/icono/iconos.ts`. Los trazados son los de Figma, copiados del anexo («Iconos SVG» y los iconos del modal). Los círculos y rectángulos van convertidos a trazado (D12).

```ts
/** Iconos de línea del DS (Figma, página «00 · Recursos gráficos» y viñetas del modal W06). Trazo currentColor. */
export type NombreIcono =
  | 'alarma'
  | 'reportes'
  | 'escanear'
  | 'ajustes'
  | 'cerrar-sesion'
  | 'colapsar'
  | 'advertencia'
  | 'qr'
  | 'calendario'
  | 'persona'
  | 'descarga';

export interface DefinicionIcono {
  /** Lado del viewBox de Figma. */
  caja: 16 | 24;
  /** Grosor de trazo en unidades del viewBox. */
  trazo: number;
  /** Trazados; `relleno` pinta la forma en vez de contornearla. */
  formas: readonly { d: string; relleno?: boolean }[];
}

const T16 = 1.33333;

export const ICONOS: Record<NombreIcono, DefinicionIcono> = {
  alarma: {
    caja: 24,
    trazo: 2,
    formas: [
      { d: 'M4 13a8 8 0 1 0 16 0a8 8 0 1 0 -16 0' },
      { d: 'M12 9V13L15 15' },
      { d: 'M3 5L6 2' },
      { d: 'M21 5L18 2' },
    ],
  },
  reportes: {
    caja: 24,
    trazo: 2,
    formas: [{ d: 'M6 20V12' }, { d: 'M12 20V6' }, { d: 'M18 20V10' }, { d: 'M3 21H21' }],
  },
  escanear: {
    caja: 24,
    trazo: 2,
    formas: [
      { d: 'M3 8V5C3 3.66667 3.66667 3 5 3H8' },
      { d: 'M16 3H19C20.3333 3 21 3.66667 21 5V8' },
      { d: 'M21 16V19C21 20.3333 20.3333 21 19 21H16' },
      { d: 'M8 21H5C3.66667 21 3 20.3333 3 19V16' },
      {
        d: 'M9.5 8h5a1.5 1.5 0 0 1 1.5 1.5v5a1.5 1.5 0 0 1 -1.5 1.5h-5a1.5 1.5 0 0 1 -1.5 -1.5v-5a1.5 1.5 0 0 1 1.5 -1.5z',
        relleno: true,
      },
    ],
  },
  ajustes: {
    caja: 24,
    trazo: 2,
    formas: [
      {
        d: 'M11.6582 3.68945C11.8504 3.50952 12.1496 3.50952 12.3418 3.68945L13.8896 5.13867C14.332 5.55272 14.91 5.79149 15.5156 5.81152L17.6348 5.88184C17.8979 5.89054 18.1095 6.10207 18.1182 6.36523L18.1885 8.48438C18.2085 9.08996 18.4473 9.66797 18.8613 10.1104L20.3105 11.6582C20.4905 11.8504 20.4905 12.1496 20.3105 12.3418L18.8613 13.8896C18.4473 14.332 18.2085 14.91 18.1885 15.5156L18.1182 17.6348C18.1095 17.8979 17.8979 18.1095 17.6348 18.1182L15.5156 18.1885C14.91 18.2085 14.332 18.4473 13.8896 18.8613L12.3418 20.3105C12.1496 20.4905 11.8504 20.4905 11.6582 20.3105L10.1104 18.8613C9.66797 18.4473 9.08996 18.2085 8.48438 18.1885L6.36523 18.1182C6.10207 18.1095 5.89054 17.8979 5.88184 17.6348L5.81152 15.5156C5.79149 14.91 5.55272 14.332 5.13867 13.8896L3.68945 12.3418C3.50952 12.1496 3.50952 11.8504 3.68945 11.6582L5.13867 10.1104C5.55272 9.66797 5.79149 9.08996 5.81152 8.48438L5.88184 6.36523C5.89054 6.10206 6.10207 5.89054 6.36523 5.88184L8.48438 5.81152C9.08996 5.79149 9.66797 5.55272 10.1104 5.13867L11.6582 3.68945Z',
      },
      { d: 'M8.5 12a3.5 3.5 0 1 0 7 0a3.5 3.5 0 1 0 -7 0' },
    ],
  },
  'cerrar-sesion': {
    caja: 24,
    trazo: 2,
    formas: [
      { d: 'M9 3H5C3.9 3 3 3.9 3 5V19C3 20.1 3.9 21 5 21H9' },
      { d: 'M16 8L20 12L16 16' },
      { d: 'M10 12H20' },
    ],
  },
  colapsar: {
    caja: 24,
    trazo: 2,
    formas: [{ d: 'M11 7L6 12L11 17' }, { d: 'M18 7L13 12L18 17' }],
  },
  advertencia: {
    caja: 24,
    trazo: 2,
    formas: [{ d: 'M12 3L2 20H22L12 3Z' }, { d: 'M12 9V14M12 17.5V18' }],
  },
  qr: {
    caja: 16,
    trazo: T16,
    formas: [
      { d: 'M6.66667 2H2V6.66667H6.66667V2Z' },
      { d: 'M14 2H9.33333V6.66667H14V2Z' },
      { d: 'M6.66667 9.33333H2V14H6.66667V9.33333Z' },
      {
        d: 'M12.6667 9.33333H14M9.33333 12.6667H10.6667M12.6667 12.6667H14V14M9.33333 9.33333H11.3333V11.3333H9.33333V9.33333Z',
      },
    ],
  },
  calendario: {
    caja: 16,
    trazo: T16,
    formas: [
      {
        d: 'M12.6667 3.33333H3.33333C2.59695 3.33333 2 3.93029 2 4.66667V12.6667C2 13.403 2.59695 14 3.33333 14H12.6667C13.403 14 14 13.403 14 12.6667V4.66667C14 3.93029 13.403 3.33333 12.6667 3.33333Z',
      },
      { d: 'M2 6.66667H14M5.33333 2V4.66667M10.6667 2V4.66667' },
    ],
  },
  persona: {
    caja: 16,
    trazo: T16,
    formas: [
      {
        d: 'M8 8C9.47276 8 10.6667 6.80609 10.6667 5.33333C10.6667 3.86057 9.47276 2.66667 8 2.66667C6.52724 2.66667 5.33333 3.86057 5.33333 5.33333C5.33333 6.80609 6.52724 8 8 8Z',
      },
      {
        d: 'M2.66667 14C2.66667 12.5855 3.22857 11.229 4.22876 10.2288C5.22896 9.22857 6.58551 8.66667 8 8.66667C9.41449 8.66667 10.771 9.22857 11.7712 10.2288C12.7714 11.229 13.3333 12.5855 13.3333 14',
      },
    ],
  },
  descarga: {
    caja: 16,
    trazo: T16,
    formas: [{ d: 'M8 2.66667V10M11.3333 6.66667L8 10L4.66667 6.66667M2.66667 13.3333H13.3333' }],
  },
};
```

- [ ] **Step 4: `aq-icono` y `aq-logotipo`**

`apps/web/src/app/componentes/icono/aq-icono.component.ts`:

```ts
import { Component, computed, input } from '@angular/core';
import { ICONOS, NombreIcono } from './iconos';

export type TamanoIcono = 'barra' | 'vineta' | 'advertencia';

/** Icono de línea del DS dibujado con trazados (D12): toma el color del texto que lo rodea. */
@Component({
  selector: 'aq-icono',
  template: `
    <svg
      [attr.viewBox]="caja()"
      fill="none"
      stroke="currentColor"
      [attr.stroke-width]="definicion().trazo"
      stroke-linecap="round"
      stroke-linejoin="round"
      aria-hidden="true"
      focusable="false"
    >
      @for (forma of definicion().formas; track $index) {
        <path
          [attr.d]="forma.d"
          [attr.fill]="forma.relleno ? 'currentColor' : 'none'"
          [attr.stroke]="forma.relleno ? 'none' : 'currentColor'"
        />
      }
    </svg>
  `,
  host: {
    '[class.barra]': "tamano() === 'barra'",
    '[class.vineta]': "tamano() === 'vineta'",
    '[class.advertencia]': "tamano() === 'advertencia'",
  },
  styles: `
    :host {
      display: inline-flex;
      flex: none;
    }
    :host(.barra) {
      width: var(--size-icono-barra-lateral);
      height: var(--size-icono-barra-lateral);
    }
    :host(.vineta) {
      width: var(--size-icono-vineta);
      height: var(--size-icono-vineta);
    }
    :host(.advertencia) {
      width: var(--size-icono-advertencia);
      height: var(--size-icono-advertencia);
    }
    svg {
      width: 100%;
      height: 100%;
    }
  `,
})
export class AqIconoComponent {
  readonly nombre = input.required<NombreIcono>();
  readonly tamano = input<TamanoIcono>('barra');
  protected readonly definicion = computed(() => ICONOS[this.nombre()]);
  protected readonly caja = computed(() => `0 0 ${this.definicion().caja} ${this.definicion().caja}`);
}
```

`apps/web/src/app/componentes/logotipo/aq-logotipo.component.ts`. El logotipo de W00 (36) y el de la barra superior (32) tienen las mismas proporciones (anexo §1 «Logotipo» y barra superior 3a/3b), así que es un solo SVG escalado:

```ts
import { Component, input } from '@angular/core';

/** Logotipo «Alarmas QR»: cuadro Amarillo Energía con anillo y punto Tinta (Figma 4072:1864 / 4072:236). */
@Component({
  selector: 'aq-logotipo',
  template: `
    <svg viewBox="0 0 36 36" aria-hidden="true" focusable="false">
      <rect class="fondo" width="36" height="36" rx="9" />
      <rect class="anillo" x="10.2855" y="10.2855" width="15.429" height="15.429" rx="3.2145" stroke-width="2.571" />
      <rect class="punto" x="14.79" y="14.79" width="6.429" height="6.429" rx="1.607" />
    </svg>
  `,
  host: {
    '[class.acceso]': "tamano() === 'acceso'",
    '[class.barra]': "tamano() === 'barra'",
  },
  styles: `
    :host {
      display: inline-flex;
      flex: none;
    }
    :host(.acceso) {
      width: var(--size-logo-acceso);
      height: var(--size-logo-acceso);
    }
    :host(.barra) {
      width: var(--size-logo-barra);
      height: var(--size-logo-barra);
    }
    svg {
      width: 100%;
      height: 100%;
    }
    .fondo {
      fill: var(--color-amarillo-energia);
    }
    .anillo {
      fill: none;
      stroke: var(--color-tinta);
    }
    .punto {
      fill: var(--color-tinta);
    }
  `,
})
export class AqLogotipoComponent {
  readonly tamano = input<'acceso' | 'barra'>('acceso');
}
```

- [ ] **Step 5: `aq-boton` y `aq-enlace`**

`apps/web/src/app/componentes/boton/aq-boton.component.ts`:

```ts
import { Component, booleanAttribute, input } from '@angular/core';

export type VarianteBoton = 'primario' | 'secundario' | 'destructivo';

/** Botón web L09 (DS comp. 43): 44 de alto, píldora, relleno 13/24. Primario amarillo, secundario contorno Tinta, destructivo contorno Coral Texto. */
@Component({
  selector: 'button[aq-boton], a[aq-boton]',
  template: `<ng-content />`,
  host: {
    '[class.primario]': "variante() === 'primario'",
    '[class.secundario]': "variante() === 'secundario'",
    '[class.destructivo]': "variante() === 'destructivo'",
    '[class.bloque]': 'bloque()',
  },
  styles: `
    :host {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: var(--space-8);
      box-sizing: border-box;
      height: var(--size-boton-web);
      padding: var(--space-boton-web);
      border: var(--stroke-borde) solid transparent;
      border-radius: var(--radius-pildora);
      font: var(--text-boton-web);
      text-decoration: none;
      white-space: nowrap;
      cursor: pointer;
      transition:
        background-color var(--motion-transicion),
        opacity var(--motion-transicion);
    }
    :host(.primario) {
      background: var(--color-primario);
      color: var(--color-primario-texto);
    }
    :host(.secundario) {
      background: var(--color-blanco);
      color: var(--color-tinta);
      border-color: var(--color-tinta);
    }
    :host(.destructivo) {
      background: var(--color-blanco);
      color: var(--color-destructivo);
      border-color: var(--color-destructivo);
    }
    :host(.bloque) {
      width: 100%;
    }
    :host(:disabled) {
      opacity: var(--opacity-deshabilitado);
      cursor: not-allowed;
    }
    :host(:focus-visible) {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--stroke-foco);
    }
  `,
})
export class AqBotonComponent {
  readonly variante = input<VarianteBoton>('primario');
  readonly bloque = input(false, { transform: booleanAttribute });
}
```

`apps/web/src/app/componentes/enlace/aq-enlace.component.ts`:

```ts
import { Component, booleanAttribute, input } from '@angular/core';

/** Enlace de texto web: Azul Texto 14 subrayado dentro de un marco de 44 (área de puntero igual al botón). */
@Component({
  selector: 'a[aq-enlace], button[aq-enlace]',
  template: `<ng-content />`,
  host: { '[class.bloque]': 'bloque()' },
  styles: `
    :host {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      height: var(--size-boton-web);
      padding: 0;
      border: none;
      background: none;
      font: var(--text-cuerpo-web);
      color: var(--color-enlace);
      text-decoration: underline;
      cursor: pointer;
    }
    :host(.bloque) {
      width: 100%;
    }
    :host(:focus-visible) {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--stroke-foco);
      border-radius: var(--radius-barra);
    }
  `,
})
export class AqEnlaceComponent {
  readonly bloque = input(false, { transform: booleanAttribute });
}
```

- [ ] **Step 6: `aq-campo`**

`apps/web/src/app/componentes/campo/aq-campo.component.ts`. Etiqueta interna (DS comp. 06), 48 de alto (D3) y mensaje de error debajo con separación 12 (anexo §3, capa 3c):

```ts
import { Component, input, model, output } from '@angular/core';
import { FormValueControl } from '@angular/forms/signals';

let siguienteId = 0;

/** Campo web con etiqueta interna: 48, radio 12, borde Gris Borde (Coral Texto en error). Se usa con [formField] o [(value)]. */
@Component({
  selector: 'aq-campo',
  template: `
    <label class="caja" [class.error]="error()">
      @if (etiqueta()) {
        <span class="etiqueta">{{ etiqueta() }}</span>
      }
      <input
        [attr.id]="idEntrada() || null"
        [type]="tipo()"
        [value]="value()"
        [attr.placeholder]="placeholder() || null"
        [attr.aria-invalid]="error() ? 'true' : null"
        [attr.aria-describedby]="mensajeError() ? idMensaje : null"
        (input)="alEscribir($event)"
        (focus)="enfocado.emit()"
        (blur)="touch.emit()"
      />
    </label>
    @if (mensajeError()) {
      <p class="mensaje-error" [id]="idMensaje">{{ mensajeError() }}</p>
    }
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
    }
    .caja {
      display: flex;
      flex-direction: column;
      justify-content: center;
      gap: var(--space-2);
      box-sizing: border-box;
      height: var(--size-campo);
      padding: 0 var(--space-campo-x);
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-campo);
      cursor: text;
      transition: border-color var(--motion-transicion);
    }
    .caja:focus-within {
      border-color: var(--color-tinta);
    }
    .caja.error {
      border-color: var(--color-destructivo);
    }
    .etiqueta {
      font: var(--text-etiqueta-campo);
      color: var(--color-texto-secundario);
    }
    input {
      width: 100%;
      padding: 0;
      border: none;
      outline: none;
      background: transparent;
      font: var(--text-valor-campo);
      color: var(--color-texto);
    }
    input::placeholder {
      color: var(--color-gris-medio);
    }
    .mensaje-error {
      margin: 0;
      font: var(--text-nota);
      color: var(--color-destructivo);
    }
  `,
})
export class AqCampoComponent implements FormValueControl<string> {
  readonly value = model('');
  readonly touch = output<void>();
  readonly enfocado = output<void>();
  readonly etiqueta = input('');
  readonly tipo = input<'text' | 'email' | 'password'>('text');
  readonly placeholder = input('');
  readonly idEntrada = input('');
  readonly error = input(false);
  readonly mensajeError = input('');
  protected readonly idMensaje = `aq-campo-error-${siguienteId++}`;

  protected alEscribir(evento: Event): void {
    this.value.set((evento.target as HTMLInputElement).value);
  }
}
```

- [ ] **Step 7: `aq-tarjeta` y `aq-tarjeta-acceso`**

`apps/web/src/app/componentes/tarjeta/aq-tarjeta.component.ts`:

```ts
import { Component, input } from '@angular/core';

/** Tarjeta web (L07/L09): radio 14, borde Gris Borde 1.5, relleno 22; «peligro» con borde Coral Texto y relleno 20 (W06). */
@Component({
  selector: 'aq-tarjeta',
  template: `<ng-content />`,
  host: { '[class.peligro]': "variante() === 'peligro'" },
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      padding: var(--space-tarjeta-web);
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
    }
    :host(.peligro) {
      padding: var(--space-tarjeta-peligro);
      border-color: var(--color-destructivo);
    }
  `,
})
export class AqTarjetaComponent {
  readonly variante = input<'normal' | 'peligro'>('normal');
}
```

`apps/web/src/app/componentes/tarjeta-acceso/aq-tarjeta-acceso.component.ts`:

```ts
import { Component } from '@angular/core';
import { AqLogotipoComponent } from '../logotipo/aq-logotipo.component';

/** Tarjeta de acceso (DS comp. 45, W00): 520 de ancho, relleno 34, separación 20; marca centrada arriba. */
@Component({
  selector: 'aq-tarjeta-acceso',
  imports: [AqLogotipoComponent],
  template: `
    <div class="marca">
      <aq-logotipo tamano="acceso" />
      <span class="nombre">Alarmas QR</span>
    </div>
    <ng-content />
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-tarjeta-acceso-gap);
      box-sizing: border-box;
      width: var(--size-tarjeta-acceso);
      max-width: 100%;
      padding: var(--space-tarjeta-acceso);
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
    }
    .marca {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: var(--space-10);
    }
    .nombre {
      font: var(--text-marca-acceso);
      color: var(--color-texto);
    }
  `,
})
export class AqTarjetaAccesoComponent {}
```

- [ ] **Step 8: Ejecutar las pruebas y verificar que pasan**

Run: `npx ng test --watch=false`
Expected: PASS, `Tests 15 passed (15)`. Si la prueba de `[formField]` falla con un error de tipos del contrato `FormValueControl`, leer `node_modules/@angular/forms/types/signals.d.ts` (interfaces `FormUiControl` y `FormValueControl`) y ajustar solo los tipos de `aq-campo`: `value` debe seguir siendo `model('')`.

- [ ] **Step 9: Verificación completa y commit**

```bash
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../.. && git add apps/web/src/app/componentes
git commit -m "$(cat <<'EOF'
L09: icono, logotipo, botón, enlace, campo y tarjetas web

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

Expected: todo en verde; `verificar-tokens` no reporta nada (las coordenadas del SVG no llevan `px`).

---

### Task 3: Snackbar global, switch y selector segmentado

**Files:**
- Create: `apps/web/src/app/componentes/snackbar/snackbar.service.ts`
- Create: `apps/web/src/app/componentes/snackbar/aq-snackbar.component.ts`
- Create: `apps/web/src/app/componentes/switch/aq-switch.component.ts`
- Create: `apps/web/src/app/componentes/selector-segmentado/aq-selector-segmentado.component.ts`
- Modify: `apps/web/src/app/app.ts`, `apps/web/src/app/app.html`
- Test: `apps/web/src/app/componentes/componentes-estado.spec.ts`
- Test: `apps/web/src/app/app.spec.ts` (se agrega un caso)

**Interfaces:**
- Consumes: tokens v1.11 y `proveedoresPrueba()` (Tarea 1).
- Produces:
  - `SnackbarService` (`providedIn: 'root'`) con `mensaje: Signal<string | null>`, `mostrar(texto: string, duracionMs = DURACION_SNACKBAR_WEB_MS): void` y `ocultar(): void`. La constante es `export const DURACION_SNACKBAR_WEB_MS = 3000`. Mostrar un mensaje nuevo reinicia la cuenta.
  - `<aq-snackbar />`: se monta una sola vez, en `App`. La región `role="status"` siempre está presente; la píldora Tinta con el visto Verde Texto aparece mientras `mensaje()` no sea `null`.
  - `<aq-switch etiqueta="…" [formField] />` o `[(checked)]`: `AqSwitchComponent implements FormCheckboxControl`. Es un `button role="switch"` con `aria-checked` y toda la fila es clicable.
  - `<aq-selector-segmentado etiqueta="…" [opciones] [formField] />` o `[(value)]`: `AqSelectorSegmentadoComponent implements FormValueControl<string>`. Usa `interface OpcionSegmentada { valor: string; texto: string }` y botones con `aria-pressed`.

- [ ] **Step 1: Escribir las pruebas (fallan: no existen)**

`apps/web/src/app/componentes/componentes-estado.spec.ts`:

```ts
import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { form, FormField } from '@angular/forms/signals';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { DURACION_SNACKBAR_WEB_MS, SnackbarService } from './snackbar/snackbar.service';
import { AqSnackbarComponent } from './snackbar/aq-snackbar.component';
import { AqSwitchComponent } from './switch/aq-switch.component';
import { AqSelectorSegmentadoComponent, OpcionSegmentada } from './selector-segmentado/aq-selector-segmentado.component';

@Component({
  imports: [AqSnackbarComponent, AqSwitchComponent, AqSelectorSegmentadoComponent, FormField],
  template: `
    <aq-snackbar />
    <aq-switch id="switch" etiqueta="Mostrar el estado de mi alarma" [(checked)]="mostrar" />
    <aq-switch id="switch-formulario" etiqueta="Contar mi voto" [formField]="formulario.contar" />
    <aq-selector-segmentado id="selector" etiqueta="Cómo apareces" [opciones]="opciones" [(value)]="aparicion" />
  `,
})
class Anfitrion {
  readonly mostrar = signal(true);
  readonly aparicion = signal('solo-iniciales');
  readonly formulario = form(signal({ contar: false }));
  readonly opciones: readonly OpcionSegmentada[] = [
    { valor: 'nombre-completo', texto: 'Nombre completo' },
    { valor: 'solo-iniciales', texto: 'Solo iniciales' },
    { valor: 'alias', texto: 'Alias' },
  ];
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('SnackbarService', () => {
  afterEach(() => vi.useRealTimers());

  it('muestra un mensaje y lo oculta a los 3 s', () => {
    vi.useFakeTimers();
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const s = TestBed.inject(SnackbarService);
    s.mostrar('Perfil actualizado');
    expect(s.mensaje()).toBe('Perfil actualizado');
    vi.advanceTimersByTime(DURACION_SNACKBAR_WEB_MS - 1);
    expect(s.mensaje()).toBe('Perfil actualizado');
    vi.advanceTimersByTime(1);
    expect(s.mensaje()).toBeNull();
  });

  it('un mensaje nuevo reinicia la cuenta', () => {
    vi.useFakeTimers();
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const s = TestBed.inject(SnackbarService);
    s.mostrar('Uno');
    vi.advanceTimersByTime(2000);
    s.mostrar('Dos');
    vi.advanceTimersByTime(2000);
    expect(s.mensaje()).toBe('Dos');
  });
});

describe('Componentes con estado L09', () => {
  it('aq-snackbar dibuja el mensaje del servicio en una región de estado', async () => {
    const f = await montar();
    const raiz: HTMLElement = f.nativeElement;
    expect(raiz.querySelector('[role="status"]')).not.toBeNull();
    expect(raiz.querySelector('.snackbar')).toBeNull();
    TestBed.inject(SnackbarService).mostrar('Cuenta eliminada exitosamente');
    await f.whenStable();
    expect(raiz.querySelector('.snackbar')?.textContent).toContain('Cuenta eliminada exitosamente');
  });

  it('aq-switch alterna con un clic y expone aria-checked', async () => {
    const f = await montar();
    const boton = (f.nativeElement as HTMLElement).querySelector('#switch button') as HTMLButtonElement;
    expect(boton.getAttribute('role')).toBe('switch');
    expect(boton.getAttribute('aria-checked')).toBe('true');
    expect(boton.textContent).toContain('Mostrar el estado de mi alarma');
    boton.click();
    await f.whenStable();
    expect(f.componentInstance.mostrar()).toBe(false);
    expect(boton.getAttribute('aria-checked')).toBe('false');
  });

  it('aq-switch funciona con Signal Forms ([formField])', async () => {
    const f = await montar();
    const boton = (f.nativeElement as HTMLElement).querySelector('#switch-formulario button') as HTMLButtonElement;
    boton.click();
    await f.whenStable();
    expect(f.componentInstance.formulario.contar().value()).toBe(true);
  });

  it('aq-selector-segmentado marca la opción activa y cambia al tocar otra', async () => {
    const f = await montar();
    const botones = Array.from(
      (f.nativeElement as HTMLElement).querySelectorAll('#selector button'),
    ) as HTMLButtonElement[];
    expect(botones.map((b) => b.textContent?.trim())).toEqual(['Nombre completo', 'Solo iniciales', 'Alias']);
    expect(botones[1].getAttribute('aria-pressed')).toBe('true');
    expect(botones[1].classList).toContain('activo');
    botones[2].click();
    await f.whenStable();
    expect(f.componentInstance.aparicion()).toBe('alias');
    expect(botones[2].getAttribute('aria-pressed')).toBe('true');
  });
});
```

Agregar al final del `describe('App', …)` de `apps/web/src/app/app.spec.ts`:

```ts
  it('monta el snackbar global una sola vez', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    expect((fixture.nativeElement as HTMLElement).querySelectorAll('aq-snackbar').length).toBe(1);
  });
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL. Faltan los módulos de `snackbar`, `switch` y `selector-segmentado`, y `App` todavía no monta `aq-snackbar`.

- [ ] **Step 3: Snackbar**

`apps/web/src/app/componentes/snackbar/snackbar.service.ts`:

```ts
import { Injectable, signal } from '@angular/core';

/** «After delay 3 s» de los marcos W00 correo enviado / cuenta eliminada y W06 actualizado (motion.snackbar-web). */
export const DURACION_SNACKBAR_WEB_MS = 3000;

/** Snackbar único de la web (D7): cualquier página muestra un aviso breve con `mostrar()`. */
@Injectable({ providedIn: 'root' })
export class SnackbarService {
  private readonly _mensaje = signal<string | null>(null);
  readonly mensaje = this._mensaje.asReadonly();
  private temporizador: ReturnType<typeof setTimeout> | undefined;

  mostrar(texto: string, duracionMs = DURACION_SNACKBAR_WEB_MS): void {
    clearTimeout(this.temporizador);
    this._mensaje.set(texto);
    this.temporizador = setTimeout(() => this._mensaje.set(null), duracionMs);
  }

  ocultar(): void {
    clearTimeout(this.temporizador);
    this._mensaje.set(null);
  }
}
```

`apps/web/src/app/componentes/snackbar/aq-snackbar.component.ts`. Medidas del anexo §2 (snackbar 4072:1895): 36 de alto, relleno 10/18, separación 8, icono de 16 y a 24 del borde inferior.

```ts
import { Component, inject } from '@angular/core';
import { SnackbarService } from './snackbar.service';

/** Snackbar web (DS comp. 42): píldora Tinta centrada abajo, visto Verde Texto y texto blanco SemiBold 14. */
@Component({
  selector: 'aq-snackbar',
  template: `
    <div class="region" role="status" aria-live="polite">
      @if (servicio.mensaje(); as mensaje) {
        <div class="snackbar">
          <svg class="icono" viewBox="0 0 16 16" aria-hidden="true" focusable="false">
            <path
              class="circulo"
              d="M8 14C11.3137 14 14 11.3137 14 8C14 4.68629 11.3137 2 8 2C4.68629 2 2 4.68629 2 8C2 11.3137 4.68629 14 8 14Z"
            />
            <path
              class="visto"
              d="M5.33333 8L7.33333 10L10.6667 6"
              stroke-width="1.46667"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
          </svg>
          <span>{{ mensaje }}</span>
        </div>
      }
    </div>
  `,
  styles: `
    .region {
      position: fixed;
      left: 50%;
      bottom: var(--space-snackbar-web-borde);
      transform: translateX(-50%);
      z-index: 30;
    }
    .snackbar {
      display: flex;
      align-items: center;
      gap: var(--space-8);
      box-sizing: border-box;
      height: var(--size-snackbar-web);
      padding: var(--space-snackbar-web);
      border-radius: var(--radius-pildora);
      background: var(--color-tinta);
      color: var(--color-blanco);
      font: var(--text-snackbar-web);
      white-space: nowrap;
    }
    .icono {
      flex: none;
      width: var(--size-icono-snackbar);
      height: var(--size-icono-snackbar);
    }
    .circulo {
      fill: var(--color-blanco);
    }
    .visto {
      fill: none;
      stroke: var(--color-verde-texto);
    }
  `,
})
export class AqSnackbarComponent {
  protected readonly servicio = inject(SnackbarService);
}
```

`apps/web/src/app/app.html` queda:

```html
<router-outlet />
<aq-snackbar />
```

En `apps/web/src/app/app.ts`, agregar `AqSnackbarComponent` a `imports`:

```ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AqSnackbarComponent } from './componentes/snackbar/aq-snackbar.component';

@Component({
  imports: [RouterOutlet, AqSnackbarComponent],
  selector: 'aq-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {}
```

- [ ] **Step 4: Switch y selector segmentado**

`apps/web/src/app/componentes/switch/aq-switch.component.ts`. Medidas del anexo W06 §1 («Switch»): 42×24 y perilla de 18. El estado apagado sigue D9.

```ts
import { Component, input, model } from '@angular/core';
import { FormCheckboxControl } from '@angular/forms/signals';

/** Switch web (W06): fila rótulo + pista 42×24. Encendido: pista Tinta, perilla blanca a la derecha; apagado: contorno Tinta, perilla Tinta a la izquierda (D9). */
@Component({
  selector: 'aq-switch',
  template: `
    <button type="button" role="switch" class="fila" [attr.aria-checked]="checked()" (click)="alternar()">
      <span class="rotulo">{{ etiqueta() }}</span>
      <span class="pista" [class.encendido]="checked()" aria-hidden="true"><span class="perilla"></span></span>
    </button>
  `,
  styles: `
    .fila {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: var(--space-12);
      width: 100%;
      padding: 0;
      border: none;
      background: none;
      text-align: left;
      cursor: pointer;
    }
    .rotulo {
      font: var(--text-rotulo-web);
      color: var(--color-texto);
    }
    .pista {
      position: relative;
      flex: none;
      box-sizing: border-box;
      width: var(--size-switch-ancho);
      height: var(--size-switch-alto);
      border: var(--stroke-borde) solid var(--color-tinta);
      border-radius: var(--radius-pildora);
      background: var(--color-blanco);
      transition: background-color var(--motion-transicion);
    }
    .perilla {
      position: absolute;
      top: 50%;
      left: var(--space-2);
      width: var(--size-switch-perilla);
      height: var(--size-switch-perilla);
      border-radius: var(--radius-pildora);
      background: var(--color-tinta);
      transform: translateY(-50%);
      transition:
        left var(--motion-transicion),
        background-color var(--motion-transicion);
    }
    .encendido {
      background: var(--color-tinta);
    }
    .encendido .perilla {
      left: calc(100% - var(--size-switch-perilla) - var(--space-2));
      background: var(--color-blanco);
    }
    .fila:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-4);
      border-radius: var(--radius-barra);
    }
  `,
})
export class AqSwitchComponent implements FormCheckboxControl {
  readonly checked = model(false);
  readonly etiqueta = input.required<string>();

  protected alternar(): void {
    this.checked.update((valor) => !valor);
  }
}
```

`apps/web/src/app/componentes/selector-segmentado/aq-selector-segmentado.component.ts`. Medidas del anexo W06 §1 («Selector segmentado»): pista de 36, relleno 3 y separación 2; activo en Tinta con texto blanco e inactivo con texto Gris Texto.

```ts
import { Component, input, model } from '@angular/core';
import { FormValueControl } from '@angular/forms/signals';

export interface OpcionSegmentada {
  valor: string;
  texto: string;
}

/** Selector segmentado web (DS comp. 46): pista Gris Niebla de 36, segmento activo Tinta. */
@Component({
  selector: 'aq-selector-segmentado',
  template: `
    <div class="pista" role="group" [attr.aria-label]="etiqueta()">
      @for (opcion of opciones(); track opcion.valor) {
        <button
          type="button"
          class="segmento"
          [class.activo]="value() === opcion.valor"
          [attr.aria-pressed]="value() === opcion.valor"
          (click)="value.set(opcion.valor)"
        >
          {{ opcion.texto }}
        </button>
      }
    </div>
  `,
  styles: `
    .pista {
      display: flex;
      gap: var(--space-2);
      box-sizing: border-box;
      height: var(--size-segmentado-web);
      padding: var(--space-segmentado);
      border-radius: var(--radius-pildora);
      background: var(--color-gris-niebla);
    }
    .segmento {
      flex: 1;
      border: none;
      border-radius: var(--radius-pildora);
      background: transparent;
      color: var(--color-texto-secundario);
      font: var(--text-pildora-web);
      cursor: pointer;
      transition:
        background-color var(--motion-transicion),
        color var(--motion-transicion);
    }
    .segmento.activo {
      background: var(--color-tinta);
      color: var(--color-blanco);
    }
    .segmento:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
  `,
})
export class AqSelectorSegmentadoComponent implements FormValueControl<string> {
  readonly value = model('');
  readonly opciones = input.required<readonly OpcionSegmentada[]>();
  readonly etiqueta = input.required<string>();
}
```

- [ ] **Step 5: Ejecutar las pruebas y verificar que pasan**

Run: `npx ng test --watch=false`
Expected: PASS, `Tests 22 passed (22)`.

- [ ] **Step 6: Verificación completa y commit**

```bash
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../.. && git add apps/web/src/app
git commit -m "$(cat <<'EOF'
L09: snackbar global, switch y selector segmentado web

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

---

### Task 4: `aq-modal` (Eliminar cuenta) y `aq-dialogo-confirmacion` (web)

**Files:**
- Create: `apps/web/src/app/componentes/modal/aq-modal.component.ts`
- Create: `apps/web/src/app/componentes/dialogo-confirmacion/aq-dialogo-confirmacion.component.ts`
- Test: `apps/web/src/app/componentes/componentes-superpuestos.spec.ts`

**Interfaces:**
- Consumes: `AqIconoComponent` (`nombre="advertencia"`, `tamano="advertencia"`) y `AqBotonComponent` (Tarea 2), `A11yModule` de `@angular/cdk/a11y`, y los tokens v1.11.
- Produces:
  - `<aq-modal titulo="…" (cerrar)="…">contenido</aq-modal>`: `AqModalComponent` con `titulo = input.required<string>()` y `cerrar = output<void>()`. Es un velo de 45 % (`[data-velo]`) más una `section role="dialog" aria-modal="true"` de 481, relleno 22, separación 16 y borde Coral Texto, con cabecera de icono de advertencia y título `<h2>`. `cdkTrapFocus` usa autocaptura. El velo y Escape emiten `cerrar` (D2).
  - `<aq-dialogo-confirmacion titulo cuerpo rotuloSeguro rotuloAccion [destruye] (seguro) (confirmar) />`: `AqDialogoConfirmacionComponent` es un velo de 55 % más una `section role="alertdialog"` de 420 con dos botones apilados, `[data-accion="seguro"]` (primario amarillo) y `[data-accion="confirmar"]` (secundario Tinta, o destructivo si `destruye`). El velo, Escape y «Cancelar» emiten `seguro`.

- [ ] **Step 1: Escribir las pruebas (fallan: no existen)**

`apps/web/src/app/componentes/componentes-superpuestos.spec.ts`:

```ts
import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqModalComponent } from './modal/aq-modal.component';
import { AqDialogoConfirmacionComponent } from './dialogo-confirmacion/aq-dialogo-confirmacion.component';

@Component({
  imports: [AqModalComponent, AqDialogoConfirmacionComponent],
  template: `
    <aq-modal id="modal" titulo="¿Eliminar tu cuenta definitivamente?" (cerrar)="cierres.set(cierres() + 1)">
      <p class="proyectado">Esta acción no se puede deshacer.</p>
      <button type="button">Conservar mi cuenta</button>
    </aq-modal>
    <aq-dialogo-confirmacion
      id="dialogo"
      titulo="¿Cerrar sesión?"
      cuerpo="Tus alarmas siguen activas en el celular."
      rotuloSeguro="Cancelar"
      rotuloAccion="Cerrar sesión"
      [destruye]="destruye()"
      (seguro)="seguros.set(seguros() + 1)"
      (confirmar)="confirmaciones.set(confirmaciones() + 1)"
    />
  `,
})
class Anfitrion {
  readonly cierres = signal(0);
  readonly seguros = signal(0);
  readonly confirmaciones = signal(0);
  readonly destruye = signal(false);
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

const escape = () => new KeyboardEvent('keydown', { key: 'Escape', bubbles: true });

describe('aq-modal', () => {
  it('es un diálogo modal titulado con icono de advertencia y el contenido proyectado', async () => {
    const f = await montar();
    const modal = (f.nativeElement as HTMLElement).querySelector('#modal')!;
    const seccion = modal.querySelector('section')!;
    expect(seccion.getAttribute('role')).toBe('dialog');
    expect(seccion.getAttribute('aria-modal')).toBe('true');
    const titulo = modal.querySelector('h2')!;
    expect(titulo.textContent).toBe('¿Eliminar tu cuenta definitivamente?');
    expect(seccion.getAttribute('aria-labelledby')).toBe(titulo.id);
    expect(modal.querySelector('aq-icono')).not.toBeNull();
    expect(modal.querySelector('.proyectado')?.textContent).toContain('no se puede deshacer');
  });

  it('el velo y Escape cierran', async () => {
    const f = await montar();
    const modal = (f.nativeElement as HTMLElement).querySelector('#modal')!;
    (modal.querySelector('[data-velo]') as HTMLElement).click();
    modal.querySelector('section')!.dispatchEvent(escape());
    expect(f.componentInstance.cierres()).toBe(2);
  });
});

describe('aq-dialogo-confirmacion', () => {
  it('muestra título, cuerpo y la acción segura como primario', async () => {
    const f = await montar();
    const dialogo = (f.nativeElement as HTMLElement).querySelector('#dialogo')!;
    const seccion = dialogo.querySelector('section')!;
    expect(seccion.getAttribute('role')).toBe('alertdialog');
    expect(dialogo.querySelector('h2')?.textContent).toBe('¿Cerrar sesión?');
    expect(dialogo.querySelector('p')?.textContent).toBe('Tus alarmas siguen activas en el celular.');
    const seguro = dialogo.querySelector('[data-accion="seguro"]')!;
    const confirmar = dialogo.querySelector('[data-accion="confirmar"]')!;
    expect(seguro.textContent?.trim()).toBe('Cancelar');
    expect(seguro.classList).toContain('primario');
    expect(confirmar.classList).toContain('secundario');
    f.componentInstance.destruye.set(true);
    await f.whenStable();
    expect(confirmar.classList).toContain('destructivo');
  });

  it('velo, Escape y «Cancelar» son la acción segura; «Cerrar sesión» confirma', async () => {
    const f = await montar();
    const dialogo = (f.nativeElement as HTMLElement).querySelector('#dialogo')!;
    (dialogo.querySelector('[data-velo]') as HTMLElement).click();
    dialogo.querySelector('section')!.dispatchEvent(escape());
    (dialogo.querySelector('[data-accion="seguro"]') as HTMLElement).click();
    expect(f.componentInstance.seguros()).toBe(3);
    (dialogo.querySelector('[data-accion="confirmar"]') as HTMLElement).click();
    expect(f.componentInstance.confirmaciones()).toBe(1);
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL. No se resuelven `./modal/aq-modal.component` ni `./dialogo-confirmacion/aq-dialogo-confirmacion.component`.

- [ ] **Step 3: `aq-modal`**

`apps/web/src/app/componentes/modal/aq-modal.component.ts`. Medidas del anexo W06 §3 (modal 4075:1753), con D2:

```ts
import { Component, input, output } from '@angular/core';
import { A11yModule } from '@angular/cdk/a11y';
import { AqIconoComponent } from '../icono/aq-icono.component';

let siguienteId = 0;

/** Modal web destructivo (DS comp. 27, única variante desde la web v1.5): velo Tinta 45 %, 481, borde Coral Texto, icono de advertencia y título. */
@Component({
  selector: 'aq-modal',
  imports: [A11yModule, AqIconoComponent],
  template: `
    <div class="velo" data-velo (click)="cerrar.emit()"></div>
    <section
      class="modal"
      role="dialog"
      aria-modal="true"
      [attr.aria-labelledby]="idTitulo"
      cdkTrapFocus
      [cdkTrapFocusAutoCapture]="true"
      (keydown.escape)="cerrar.emit()"
    >
      <header class="cabecera">
        <aq-icono class="advertencia" nombre="advertencia" tamano="advertencia" />
        <h2 class="titulo" [id]="idTitulo">{{ titulo() }}</h2>
      </header>
      <ng-content />
    </section>
  `,
  styles: `
    :host {
      position: fixed;
      inset: 0;
      z-index: 20;
      display: grid;
      place-items: center;
    }
    .velo {
      position: absolute;
      inset: 0;
      background: var(--color-velo);
    }
    .modal {
      position: relative;
      display: flex;
      flex-direction: column;
      gap: var(--space-16);
      box-sizing: border-box;
      width: var(--size-modal-web);
      max-width: calc(100% - 2 * var(--space-web-modal));
      padding: var(--space-web-modal);
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-destructivo);
      border-radius: var(--radius-modal);
    }
    .cabecera {
      display: flex;
      align-items: center;
      gap: var(--space-10);
    }
    .advertencia {
      color: var(--color-destructivo);
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-modal-web);
      color: var(--color-texto);
    }
  `,
})
export class AqModalComponent {
  readonly titulo = input.required<string>();
  readonly cerrar = output<void>();
  protected readonly idTitulo = `aq-modal-titulo-${siguienteId++}`;
}
```

- [ ] **Step 4: `aq-dialogo-confirmacion`**

`apps/web/src/app/componentes/dialogo-confirmacion/aq-dialogo-confirmacion.component.ts`. Medidas del anexo W06 §4 (diálogo 4360:541):

```ts
import { Component, booleanAttribute, input, output } from '@angular/core';
import { A11yModule } from '@angular/cdk/a11y';
import { AqBotonComponent } from '../boton/aq-boton.component';

let siguienteId = 0;

/** Diálogo de confirmación web (DS comp. 47 en web): velo Tinta 55 %, 420, acción segura primaria arriba y la que confirma en contorno abajo. */
@Component({
  selector: 'aq-dialogo-confirmacion',
  imports: [A11yModule, AqBotonComponent],
  template: `
    <div class="velo" data-velo (click)="seguro.emit()"></div>
    <section
      class="dialogo"
      role="alertdialog"
      aria-modal="true"
      [attr.aria-labelledby]="idTitulo"
      [attr.aria-describedby]="idCuerpo"
      cdkTrapFocus
      [cdkTrapFocusAutoCapture]="true"
      (keydown.escape)="seguro.emit()"
    >
      <h2 class="titulo" [id]="idTitulo">{{ titulo() }}</h2>
      <p class="cuerpo" [id]="idCuerpo">{{ cuerpo() }}</p>
      <div class="acciones">
        <button aq-boton bloque type="button" data-accion="seguro" (click)="seguro.emit()">
          {{ rotuloSeguro() }}
        </button>
        <button
          aq-boton
          bloque
          type="button"
          data-accion="confirmar"
          [variante]="destruye() ? 'destructivo' : 'secundario'"
          (click)="confirmar.emit()"
        >
          {{ rotuloAccion() }}
        </button>
      </div>
    </section>
  `,
  styles: `
    :host {
      position: fixed;
      inset: 0;
      z-index: 20;
      display: grid;
      place-items: center;
    }
    .velo {
      position: absolute;
      inset: 0;
      background: var(--color-velo-movil);
    }
    .dialogo {
      position: relative;
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
      box-sizing: border-box;
      width: var(--size-dialogo-web-ancho);
      max-width: calc(100% - 2 * var(--space-dialogo));
      padding: var(--space-dialogo);
      background: var(--color-blanco);
      border-radius: var(--radius-dialogo-web);
      box-shadow: var(--elevation-dialogo-web);
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-dialogo);
      color: var(--color-texto);
    }
    .cuerpo {
      margin: 0;
      font: var(--text-cuerpo-dialogo);
      color: var(--color-texto-secundario);
    }
    .acciones {
      display: flex;
      flex-direction: column;
      gap: var(--space-8);
      padding-top: var(--space-8);
    }
  `,
})
export class AqDialogoConfirmacionComponent {
  readonly titulo = input.required<string>();
  readonly cuerpo = input.required<string>();
  readonly rotuloSeguro = input.required<string>();
  readonly rotuloAccion = input.required<string>();
  readonly destruye = input(false, { transform: booleanAttribute });
  readonly seguro = output<void>();
  readonly confirmar = output<void>();
  protected readonly idTitulo = `aq-dialogo-titulo-${siguienteId++}`;
  protected readonly idCuerpo = `aq-dialogo-cuerpo-${siguienteId++}`;
}
```

- [ ] **Step 5: Ejecutar las pruebas y verificar que pasan**

Run: `npx ng test --watch=false`
Expected: PASS, `Tests 26 passed (26)`.

- [ ] **Step 6: Verificación completa y commit**

```bash
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../.. && git add apps/web/src/app/componentes
git commit -m "$(cat <<'EOF'
L09: modal destructivo y diálogo de confirmación web

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

---

### Task 5: Barra superior, barra lateral colapsable y diálogo «¿Cerrar sesión?» en el layout

**Files:**
- Create: `apps/web/src/app/navegacion/barra-lateral.ts`
- Create: `apps/web/src/app/componentes/barra-superior/aq-barra-superior.component.ts`
- Create: `apps/web/src/app/componentes/barra-lateral/aq-barra-lateral.component.ts`
- Modify: `apps/web/src/app/layout/aq-layout-app/aq-layout-app.component.ts` (reescritura completa)
- Modify: `apps/web/src/app/pantallas/pantalla-marcador/pantalla-marcador.component.ts` (quita su relleno)
- Modify: `apps/web/src/app/app.routes.spec.ts` (proveedores de HTTP: el layout ya lee `DatosService`)
- Test: `apps/web/src/app/layout/aq-layout-app/aq-layout-app.component.spec.ts`

**Interfaces:**
- Consumes: `AqIconoComponent`/`NombreIcono`, `AqLogotipoComponent` (Tarea 2); `AqDialogoConfirmacionComponent` (Tarea 4); `DatosService.usuario`/`mensajes` y `SesionService.cerrar()`; `routes` de `app.routes.ts`; `proveedoresPrueba`/`cargarDataset`.
- Produces:
  - `interface ItemBarraLateral { id: string; texto: string; icono: NombreIcono; ruta: string }`, `const ITEMS_BARRA_LATERAL: readonly ItemBarraLateral[]` (Mis Alarmas → `/alarmas`, Reportes → `/reportes`, Descargar QR → `/qr`, Ajustes de Perfil → `/perfil`) y `const ITEM_CERRAR_SESION = { id: 'cerrar-sesion', texto: 'Cerrar Sesión', icono: 'cerrar-sesion' }`.
  - `<aq-barra-superior [nombre]="…" />`: marca a la izquierda; a la derecha nombre y avatar, que es un enlace a `/perfil` (`[data-avatar]`).
  - `<aq-barra-lateral [(colapsada)] (cerrarSesion) />`: `colapsada = model(false)`, `cerrarSesion = output<void>()`. Tiene el control `[data-control-menu]` y los ítems `[data-item="<id>"]`; el activo lleva la clase `activo` y `aria-current="page"`.
  - `AqLayoutAppComponent`: rejilla de barra superior de 64 más barra lateral de 208 (64 si está colapsada) más `<main>` con relleno 28/32. El estado `colapsada` es una señal del layout y sobrevive entre páginas. `?dialogo=cerrar-sesion` abre `aq-dialogo-confirmacion` con los textos `mensajes.confirmarCerrarSesion*` (cuerpo web). «Cancelar», el velo o Escape quitan el query param; «Cerrar sesión» cierra la sesión y navega a `/login`.

- [ ] **Step 1: Escribir las pruebas (fallan: no existen)**

`apps/web/src/app/layout/aq-layout-app/aq-layout-app.component.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SesionService } from '../../datos/sesion.service';
import { ITEM_CERRAR_SESION, ITEMS_BARRA_LATERAL } from '../../navegacion/barra-lateral';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  const estable = () => harness.fixture.whenStable();
  return { harness, raiz, estable, router: TestBed.inject(Router) };
}

describe('Layout con barra lateral (L09)', () => {
  it('los ítems de la barra coinciden con dataset.web.barraLateral', () => {
    const items = [...ITEMS_BARRA_LATERAL, ITEM_CERRAR_SESION].map((i) => ({ id: i.id, icono: i.icono }));
    expect(items).toEqual(dataset.web.barraLateral.items);
  });

  it('barra superior con el usuario y barra lateral con el ítem activo', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    expect(raiz.querySelector('aq-barra-superior')?.textContent).toContain('Alarmas QR');
    expect(raiz.querySelector('[data-avatar]')?.textContent).toContain('Andrés Rojas');
    const textos = Array.from(raiz.querySelectorAll('[data-item]')).map((e) => e.textContent?.trim());
    expect(textos).toEqual(['Mis Alarmas', 'Reportes', 'Descargar QR', 'Ajustes de Perfil', 'Cerrar Sesión']);
    const activo = raiz.querySelector('[data-item="mis-alarmas"]')!;
    expect(activo.classList).toContain('activo');
    expect(activo.getAttribute('aria-current')).toBe('page');
    (raiz.querySelector('[data-avatar]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/perfil');
  });

  it('colapsar deja solo los iconos, con nombre accesible, y se mantiene al navegar', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    const control = raiz.querySelector('[data-control-menu]') as HTMLButtonElement;
    expect(control.getAttribute('aria-label')).toBe('Colapsar menú');
    control.click();
    await estable();
    expect(raiz.querySelector('.layout')!.classList).toContain('colapsada');
    expect(control.getAttribute('aria-label')).toBe('Expandir menú');
    expect(raiz.querySelector('[data-item="reportes"]')!.getAttribute('aria-label')).toBe('Reportes');
    (raiz.querySelector('[data-item="reportes"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/reportes');
    expect(raiz.querySelector('.layout')!.classList).toContain('colapsada');
  });

  it('«Cerrar Sesión» abre el diálogo sobre la página actual y el velo lo cierra', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    (raiz.querySelector('[data-item="cerrar-sesion"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/alarmas?dialogo=cerrar-sesion');
    const dialogo = raiz.querySelector('aq-dialogo-confirmacion')!;
    expect(dialogo.textContent).toContain('¿Cerrar sesión?');
    expect(dialogo.textContent).toContain(dataset.mensajes.confirmarCerrarSesionCuerpoWeb);
    (dialogo.querySelector('[data-velo]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/alarmas');
    expect(raiz.querySelector('aq-dialogo-confirmacion')).toBeNull();
  });

  it('«Cerrar sesión» en el diálogo cierra la sesión y vuelve a /login', async () => {
    const { raiz, estable, router } = await abrir('/perfil?dialogo=cerrar-sesion');
    const sesion = TestBed.inject(SesionService);
    sesion.iniciar();
    (raiz.querySelector('aq-dialogo-confirmacion [data-accion="confirmar"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/login');
    expect(sesion.iniciada()).toBe(false);
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL. No se resuelve `../../navegacion/barra-lateral`.

- [ ] **Step 3: Ítems de la barra lateral**

`apps/web/src/app/navegacion/barra-lateral.ts`:

```ts
import { NombreIcono } from '../componentes/icono/iconos';

/** Ítem de la barra lateral (TRAZABILIDAD §2 «Barra lateral»): ids e iconos iguales a dataset.web.barraLateral. */
export interface ItemBarraLateral {
  id: string;
  texto: string;
  icono: NombreIcono;
  ruta: string;
}

export const ITEMS_BARRA_LATERAL: readonly ItemBarraLateral[] = [
  { id: 'mis-alarmas', texto: 'Mis Alarmas', icono: 'alarma', ruta: '/alarmas' },
  { id: 'reportes', texto: 'Reportes', icono: 'reportes', ruta: '/reportes' },
  { id: 'descargar-qr', texto: 'Descargar QR', icono: 'escanear', ruta: '/qr' },
  { id: 'ajustes', texto: 'Ajustes de Perfil', icono: 'ajustes', ruta: '/perfil' },
];

/** Al fondo de la barra: no navega, abre el diálogo «¿Cerrar sesión?» (?dialogo=cerrar-sesion). */
export const ITEM_CERRAR_SESION = { id: 'cerrar-sesion', texto: 'Cerrar Sesión', icono: 'cerrar-sesion' } as const;
```

- [ ] **Step 4: `aq-barra-superior`**

`apps/web/src/app/componentes/barra-superior/aq-barra-superior.component.ts`. Medidas del anexo «Barra superior web» (4072:234): 64 de alto, relleno lateral 28, borde inferior de 1, logo de 32, marca 700 17, nombre 14 en Gris Texto y avatar de 32 en Gris Niebla con borde Tinta de 1.5.

```ts
import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AqLogotipoComponent } from '../logotipo/aq-logotipo.component';

/** Barra superior web (DS comp. 14w): marca a la izquierda; nombre + avatar a la derecha (avatar → W06). */
@Component({
  selector: 'aq-barra-superior',
  imports: [RouterLink, AqLogotipoComponent],
  template: `
    <header class="barra">
      <div class="marca">
        <aq-logotipo tamano="barra" />
        <span class="nombre-app">Alarmas QR</span>
      </div>
      <a class="usuario" routerLink="/perfil" data-avatar>
        <span>{{ nombre() }}</span>
        <span class="avatar" aria-hidden="true"></span>
      </a>
    </header>
  `,
  styles: `
    .barra {
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-sizing: border-box;
      height: var(--size-barra-superior-web);
      padding: 0 var(--space-barra-superior-web);
      background: var(--color-blanco);
      border-bottom: var(--stroke-borde-fino) solid var(--color-borde);
    }
    .marca,
    .usuario {
      display: flex;
      align-items: center;
      gap: var(--space-10);
    }
    .nombre-app {
      font: var(--text-marca-web);
      color: var(--color-texto);
    }
    .usuario {
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
      text-decoration: none;
    }
    .usuario:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-4);
      border-radius: var(--radius-pildora);
    }
    .avatar {
      box-sizing: border-box;
      width: var(--size-avatar-web);
      height: var(--size-avatar-web);
      border: var(--stroke-borde) solid var(--color-tinta);
      border-radius: var(--radius-pildora);
      background: var(--color-gris-niebla);
    }
  `,
})
export class AqBarraSuperiorComponent {
  readonly nombre = input('');
}
```

- [ ] **Step 5: `aq-barra-lateral`**

`apps/web/src/app/componentes/barra-lateral/aq-barra-lateral.component.ts`. Medidas del anexo §2–§4 de la barra lateral:
- Expandida: 208, relleno 20/12, separación 4, fondo blanco y borde derecho de 1.
- Control: 36×36 con el icono colapsar (girado 180° si está colapsada).
- Ítem: 38 de alto, relleno 9/16, separación 10 e icono de 20; el activo en píldora Tinta con texto e icono blancos.
- Colapsada: píldoras de 40×40 sin texto.
- Espaciador flexible antes de «Cerrar Sesión».

```ts
import { Component, model, output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AqIconoComponent } from '../icono/aq-icono.component';
import { ITEM_CERRAR_SESION, ITEMS_BARRA_LATERAL } from '../../navegacion/barra-lateral';

/** Barra lateral web (DS comp. 14w, web v1.4): ítems con icono, píldora activa Tinta, colapsable a 64. */
@Component({
  selector: 'aq-barra-lateral',
  imports: [RouterLink, RouterLinkActive, AqIconoComponent],
  template: `
    <nav class="barra" [class.colapsada]="colapsada()" aria-label="Menú principal">
      <button
        type="button"
        class="control"
        data-control-menu
        [attr.aria-label]="colapsada() ? 'Expandir menú' : 'Colapsar menú'"
        [attr.aria-expanded]="!colapsada()"
        (click)="colapsada.set(!colapsada())"
      >
        <aq-icono nombre="colapsar" [class.girado]="colapsada()" />
      </button>
      @for (item of items; track item.id) {
        <a
          class="item"
          [routerLink]="item.ruta"
          routerLinkActive="activo"
          #activo="routerLinkActive"
          [attr.aria-current]="activo.isActive ? 'page' : null"
          [attr.aria-label]="colapsada() ? item.texto : null"
          [attr.title]="colapsada() ? item.texto : null"
          [attr.data-item]="item.id"
        >
          <aq-icono [nombre]="item.icono" />
          <span class="texto">{{ item.texto }}</span>
        </a>
      }
      <span class="espaciador"></span>
      <button
        type="button"
        class="item"
        [attr.data-item]="salida.id"
        [attr.aria-label]="colapsada() ? salida.texto : null"
        [attr.title]="colapsada() ? salida.texto : null"
        (click)="cerrarSesion.emit()"
      >
        <aq-icono [nombre]="salida.icono" />
        <span class="texto">{{ salida.texto }}</span>
      </button>
    </nav>
  `,
  styles: `
    :host {
      display: block;
    }
    .barra {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      gap: var(--space-4);
      box-sizing: border-box;
      height: 100%;
      padding: var(--space-barra-lateral-web);
      background: var(--color-blanco);
      border-right: var(--stroke-borde-fino) solid var(--color-borde);
      overflow: hidden;
    }
    .control {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      flex: none;
      width: var(--size-control-colapsar-menu);
      height: var(--size-control-colapsar-menu);
      padding: 0;
      border: none;
      border-radius: var(--radius-pildora);
      background: transparent;
      color: var(--color-tinta);
      cursor: pointer;
    }
    .girado {
      transform: rotate(180deg);
    }
    .item {
      display: flex;
      align-items: center;
      gap: var(--space-10);
      flex: none;
      box-sizing: border-box;
      width: 100%;
      height: var(--size-item-barra-lateral);
      padding: var(--space-item-barra-lateral);
      border: none;
      border-radius: var(--radius-pildora);
      background: transparent;
      color: var(--color-texto);
      font: var(--text-nav-lateral);
      text-align: left;
      text-decoration: none;
      white-space: nowrap;
      cursor: pointer;
      transition: background-color var(--motion-transicion);
    }
    .item.activo {
      background: var(--color-tinta);
      color: var(--color-blanco);
    }
    .espaciador {
      flex: 1;
    }
    .colapsada .item {
      justify-content: center;
      width: var(--size-pildora-colapsada);
      height: var(--size-pildora-colapsada);
      padding: 0;
    }
    .colapsada .texto {
      display: none;
    }
    .control:focus-visible,
    .item:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
  `,
})
export class AqBarraLateralComponent {
  readonly colapsada = model(false);
  readonly cerrarSesion = output<void>();
  protected readonly items = ITEMS_BARRA_LATERAL;
  protected readonly salida = ITEM_CERRAR_SESION;
}
```

- [ ] **Step 6: Layout y marcador**

Reemplazar `apps/web/src/app/layout/aq-layout-app/aq-layout-app.component.ts` completo:

```ts
import { Component, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router, RouterOutlet, UrlTree } from '@angular/router';
import { AqBarraSuperiorComponent } from '../../componentes/barra-superior/aq-barra-superior.component';
import { AqBarraLateralComponent } from '../../componentes/barra-lateral/aq-barra-lateral.component';
import { AqDialogoConfirmacionComponent } from '../../componentes/dialogo-confirmacion/aq-dialogo-confirmacion.component';
import { DatosService } from '../../datos/datos.service';
import { SesionService } from '../../datos/sesion.service';

/** Layout de las páginas autenticadas: barra superior 64 + barra lateral 208/64 + contenido; «¿Cerrar sesión?» vive en ?dialogo=cerrar-sesion (TRAZABILIDAD §2). */
@Component({
  selector: 'aq-layout-app',
  imports: [RouterOutlet, AqBarraSuperiorComponent, AqBarraLateralComponent, AqDialogoConfirmacionComponent],
  template: `
    <div class="layout" [class.colapsada]="colapsada()">
      <aq-barra-superior class="superior" [nombre]="usuario()?.nombre ?? ''" />
      <aq-barra-lateral [(colapsada)]="colapsada" (cerrarSesion)="abrirDialogo()" />
      <main class="contenido"><router-outlet /></main>
    </div>
    @if (dialogoAbierto()) {
      @if (mensajes(); as m) {
        <aq-dialogo-confirmacion
          [titulo]="m.confirmarCerrarSesionTitulo"
          [cuerpo]="m.confirmarCerrarSesionCuerpoWeb"
          [rotuloSeguro]="m.confirmarCerrarSesionSeguro"
          [rotuloAccion]="m.confirmarCerrarSesionAccion"
          (seguro)="cerrarDialogo()"
          (confirmar)="cerrarSesion()"
        />
      }
    }
  `,
  styles: `
    .layout {
      display: grid;
      grid-template-columns: var(--size-barra-lateral-web) minmax(0, 1fr);
      grid-template-rows: var(--size-barra-superior-web) minmax(0, 1fr);
      min-height: 100vh;
      transition: grid-template-columns var(--motion-transicion);
    }
    .layout.colapsada {
      grid-template-columns: var(--size-barra-lateral-web-colapsada) minmax(0, 1fr);
    }
    .superior {
      grid-column: 1 / -1;
    }
    .contenido {
      min-width: 0;
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
    }
  `,
})
export class AqLayoutAppComponent {
  private readonly router = inject(Router);
  private readonly datos = inject(DatosService);
  private readonly sesion = inject(SesionService);
  private readonly consulta = toSignal(inject(ActivatedRoute).queryParamMap);

  readonly colapsada = signal(false);
  readonly usuario = this.datos.usuario;
  readonly mensajes = this.datos.mensajes;
  readonly dialogoAbierto = computed(() => this.consulta()?.get('dialogo') === 'cerrar-sesion');

  abrirDialogo(): void {
    void this.router.navigateByUrl(this.urlConDialogo('cerrar-sesion'));
  }

  cerrarDialogo(): void {
    void this.router.navigateByUrl(this.urlConDialogo(null));
  }

  cerrarSesion(): void {
    this.sesion.cerrar();
    void this.router.navigateByUrl('/login');
  }

  /** La página actual con ?dialogo= puesto o quitado; conserva la ruta y los demás query params. */
  private urlConDialogo(valor: string | null): UrlTree {
    const arbol = this.router.parseUrl(this.router.url);
    const consulta = { ...arbol.queryParams };
    delete consulta['dialogo'];
    arbol.queryParams = valor ? { ...consulta, dialogo: valor } : consulta;
    return arbol;
  }
}
```

En `apps/web/src/app/pantallas/pantalla-marcador/pantalla-marcador.component.ts`, borrar la línea `padding: var(--space-web-contenido-y) var(--space-web-contenido-x);` del bloque `.marcador`: el relleno ahora lo pone `<main>`.

En `apps/web/src/app/app.routes.spec.ts`, el layout ahora inyecta `DatosService` (`httpResource`), así que sin HttpClient las rutas con barra lateral fallan con `No provider for HttpClient`. Cambiar los imports y el `beforeEach` para usar el ayudante:

```ts
import { TestBed } from '@angular/core/testing';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from './app.routes';
import { PANTALLAS } from './navegacion/pantallas';
import { proveedoresPrueba } from '../testing/datos-prueba';

describe('rutas de TRAZABILIDAD §2', () => {
  beforeEach(() => TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) }));
```

Los tres `it` quedan igual.

- [ ] **Step 7: Ejecutar las pruebas y verificar que pasan**

Run: `npx ng test --watch=false`
Expected: PASS, `Tests 31 passed (31)`. `app.routes.spec.ts` sigue en verde: `/perfil` todavía es el marcador W06 dentro del layout y la etiqueta `aq-barra-lateral` sigue en el HTML.

- [ ] **Step 8: Verificación completa y commit**

```bash
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../.. && git add apps/web/src/app
git commit -m "$(cat <<'EOF'
L09: barra superior, barra lateral colapsable y diálogo ¿Cerrar sesión?

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

---

### Task 6: W00 Inicio de sesión (error ⏩, correo enviado, cuenta eliminada) y Recuperar contraseña

**Files:**
- Modify: `apps/web/src/app/pantallas/w00-login/w00-login.component.ts` (reescritura completa)
- Create: `apps/web/src/app/pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component.ts`
- Modify: `apps/web/src/app/app.routes.ts` (ruta `login/recuperar`)
- Modify: `apps/web/src/styles.css` (utilidad `.solo-lector`)
- Test: `apps/web/src/app/pantallas/w00-login/w00-login.component.spec.ts`
- Test: `apps/web/src/app/pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component.spec.ts`

**Interfaces:**
- Consumes: `AqTarjetaAccesoComponent`, `AqCampoComponent`, `AqBotonComponent`, `AqEnlaceComponent` (Tarea 2); `SnackbarService` (Tarea 3); `DatosService.acceso` y `DatosService.mensajes`; `SesionService.iniciar()`; `withComponentInputBinding()` (Tarea 1).
- Produces:
  - `W00LoginComponent` (`aq-w00-login`) en `/login`. Tiene `estado = input<string>()`, que viene del query param: `eliminada` muestra `mensajes.cuentaEliminada` y `correo-enviado` muestra `mensajes.correoRecuperacionEnviado` en el snackbar. La raíz es `main[data-codigo="W00"]`, con `data-estado="error"` durante el estado de error. El primer foco en la contraseña enciende el error (D8). «Iniciar sesión» llama a `sesion.iniciar()` y navega a `/alarmas`.
  - `W00RecuperarContrasenaComponent` (`aq-w00-recuperar-contrasena`) en `/login/recuperar`, con `data: { codigo: 'W00' }` y raíz `main[data-codigo="W00"][data-estado="recuperar"]`. «Enviar enlace» navega a `/login?estado=correo-enviado`; «‹ Volver a iniciar sesión» navega a `/login`.
  - Clase global `.solo-lector` (texto solo para lectores de pantalla).

- [ ] **Step 1: Escribir las pruebas (fallan: la página todavía es el marcador)**

`apps/web/src/app/pantallas/w00-login/w00-login.component.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SesionService } from '../../datos/sesion.service';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { harness, raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

describe('W00 · Inicio de sesión', () => {
  it('muestra la tarjeta de acceso sin barra lateral', async () => {
    const { raiz } = await abrir('/login');
    const pagina = raiz.querySelector('[data-codigo="W00"]')!;
    expect(pagina).not.toBeNull();
    expect(raiz.querySelector('aq-barra-lateral')).toBeNull();
    for (const texto of [
      'Alarmas QR',
      'Administración y consulta de tus eventos y alarmas',
      'CORREO ELECTRÓNICO',
      'CONTRASEÑA',
      'Iniciar sesión',
      '¿Olvidaste tu contraseña?',
      'Entra cualquier usuario registrado en el sistema.',
      '¿Aún no tienes cuenta? Créala desde la app móvil al registrarte.',
    ]) {
      expect(pagina.textContent).toContain(texto);
    }
    const entradas = pagina.querySelectorAll('input');
    expect(entradas[0].placeholder).toBe('nombre@correo.com');
    expect(entradas[1].type).toBe('password');
  });

  it('⏩ el foco en la contraseña muestra el error de credenciales', async () => {
    const { raiz, estable } = await abrir('/login');
    const contrasena = raiz.querySelectorAll('input')[1] as HTMLInputElement;
    contrasena.dispatchEvent(new FocusEvent('focus'));
    await estable();
    expect(raiz.querySelector('[data-codigo="W00"]')!.getAttribute('data-estado')).toBe('error');
    expect(contrasena.getAttribute('aria-invalid')).toBe('true');
    expect(raiz.textContent).toContain(dataset.web.acceso.errorCredenciales);
  });

  it('T5 · «Iniciar sesión» inicia la sesión y lleva a Mis Alarmas', async () => {
    const { raiz, estable, router } = await abrir('/login');
    (raiz.querySelector('button[type="submit"]') as HTMLButtonElement).click();
    await estable();
    expect(router.url).toBe('/alarmas');
    expect(TestBed.inject(SesionService).iniciada()).toBe(true);
    expect(raiz.querySelector('[data-codigo="W01"]')).not.toBeNull();
    expect(raiz.querySelector('aq-barra-lateral')).not.toBeNull();
  });

  it('?estado=eliminada muestra «Cuenta eliminada exitosamente»', async () => {
    await abrir('/login?estado=eliminada');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.cuentaEliminada);
  });

  it('?estado=correo-enviado muestra el aviso del correo de recuperación', async () => {
    await abrir('/login?estado=correo-enviado');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.correoRecuperacionEnviado);
  });

  it('«¿Olvidaste tu contraseña?» abre Recuperar contraseña', async () => {
    const { raiz, estable, router } = await abrir('/login');
    const enlace = Array.from(raiz.querySelectorAll('a')).find((a) =>
      a.textContent?.includes('¿Olvidaste tu contraseña?'),
    )!;
    enlace.click();
    await estable();
    expect(router.url).toBe('/login/recuperar');
  });
});
```

`apps/web/src/app/pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir() {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create('/login/recuperar');
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

const r = dataset.web.acceso.recuperar;

describe('W00 · Recuperar contraseña', () => {
  it('muestra título, explicación, campo, «Enviar enlace» y «Volver»', async () => {
    const { raiz } = await abrir();
    const pagina = raiz.querySelector('[data-codigo="W00"][data-estado="recuperar"]')!;
    expect(pagina).not.toBeNull();
    for (const texto of [r.titulo, r.texto, 'CORREO ELECTRÓNICO', r.boton, r.volver]) {
      expect(pagina.textContent).toContain(texto);
    }
    expect(pagina.querySelectorAll('input').length).toBe(1);
  });

  it('«Enviar enlace» vuelve a W00 con el aviso de correo enviado', async () => {
    const { raiz, estable, router } = await abrir();
    (raiz.querySelector('button[type="submit"]') as HTMLButtonElement).click();
    await estable();
    expect(router.url).toBe('/login?estado=correo-enviado');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.correoRecuperacionEnviado);
  });

  it('«‹ Volver a iniciar sesión» vuelve a /login', async () => {
    const { raiz, estable, router } = await abrir();
    const volver = Array.from(raiz.querySelectorAll('a')).find((a) => a.textContent?.includes(r.volver))!;
    volver.click();
    await estable();
    expect(router.url).toBe('/login');
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL. En `w00-login` no aparecen los textos porque la página sigue siendo el marcador, y `w00-recuperar-contrasena` no resuelve la ruta (`/login/recuperar` redirige a `/login` por el comodín `**`).

- [ ] **Step 3: Utilidad `.solo-lector`**

Agregar al final de `apps/web/src/styles.css` (está fuera de `src/app`, así que la guardia no lo revisa; es la técnica estándar de texto oculto accesible):

```css
/* Texto solo para lectores de pantalla (títulos de página que el mockup no dibuja). */
.solo-lector {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  padding: 0;
  overflow: hidden;
  clip: rect(0 0 0 0);
  white-space: nowrap;
  border: 0;
}
```

- [ ] **Step 4: Página W00**

Reemplazar `apps/web/src/app/pantallas/w00-login/w00-login.component.ts` completo. Medidas y textos del anexo §1 (W00 4072:1861) y §3 (error 4362:474):

```ts
import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaAccesoComponent } from '../../componentes/tarjeta-acceso/aq-tarjeta-acceso.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';
import { SesionService } from '../../datos/sesion.service';

/** W00 · Inicio de sesión (F-W00). Estados: error de credenciales (⏩ foco en contraseña, D8), ?estado=correo-enviado y ?estado=eliminada (snackbar 3 s). */
@Component({
  selector: 'aq-w00-login',
  imports: [RouterLink, FormField, AqTarjetaAccesoComponent, AqCampoComponent, AqBotonComponent, AqEnlaceComponent],
  template: `
    <main class="pagina" data-codigo="W00" [attr.data-estado]="error() ? 'error' : null">
      <h1 class="solo-lector">Inicio de sesión</h1>
      <aq-tarjeta-acceso>
        <p class="subtitulo">Administración y consulta de tus eventos y alarmas</p>
        <form class="formulario" novalidate (submit)="iniciarSesion($event)">
          <div class="campos">
            <aq-campo
              etiqueta="CORREO ELECTRÓNICO"
              tipo="email"
              placeholder="nombre@correo.com"
              [formField]="formulario.correo"
            />
            <aq-campo
              etiqueta="CONTRASEÑA"
              tipo="password"
              placeholder="••••••••"
              [formField]="formulario.contrasena"
              [error]="error()"
              [mensajeError]="error() ? (acceso()?.errorCredenciales ?? '') : ''"
              (enfocado)="simularCredencialesInvalidas()"
            />
          </div>
          <button aq-boton bloque type="submit">Iniciar sesión</button>
        </form>
        <a aq-enlace bloque routerLink="/login/recuperar">¿Olvidaste tu contraseña?</a>
        <p class="nota">
          Entra cualquier usuario registrado en el sistema.<br />¿Aún no tienes cuenta? Créala desde la app móvil al
          registrarte.
        </p>
      </aq-tarjeta-acceso>
    </main>
  `,
  styles: `
    .pagina {
      display: grid;
      place-items: center;
      box-sizing: border-box;
      min-height: 100vh;
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
    }
    .subtitulo,
    .nota {
      margin: 0;
      color: var(--color-texto-secundario);
      text-align: center;
    }
    .subtitulo {
      font: var(--text-cuerpo-web);
    }
    .nota {
      font: var(--text-nota-acceso);
    }
    .formulario,
    .campos {
      display: flex;
      flex-direction: column;
    }
    .formulario {
      gap: var(--space-tarjeta-acceso-gap);
    }
    .campos {
      gap: var(--space-12);
    }
  `,
})
export class W00LoginComponent {
  private readonly datos = inject(DatosService);
  private readonly sesion = inject(SesionService);
  private readonly router = inject(Router);
  private readonly snackbar = inject(SnackbarService);

  /** Query param ?estado= (withComponentInputBinding). */
  readonly estado = input<string>();
  protected readonly acceso = this.datos.acceso;
  protected readonly formulario = form(signal({ correo: '', contrasena: '' }));
  protected readonly error = signal(false);

  constructor() {
    effect(() => {
      const mensajes = this.datos.mensajes();
      const estado = this.estado();
      if (!mensajes) return;
      const texto =
        estado === 'eliminada'
          ? mensajes.cuentaEliminada
          : estado === 'correo-enviado'
            ? mensajes.correoRecuperacionEnviado
            : null;
      if (texto) untracked(() => this.snackbar.mostrar(texto));
    });
  }

  /** ⏩ Disparador simulado del prototipo: el foco en la contraseña muestra el error de credenciales (NAVEGACION §6b). */
  protected simularCredencialesInvalidas(): void {
    this.error.set(true);
  }

  protected iniciarSesion(evento: Event): void {
    evento.preventDefault();
    this.sesion.iniciar();
    void this.router.navigateByUrl('/alarmas');
  }
}
```

- [ ] **Step 5: Página Recuperar contraseña y ruta**

`apps/web/src/app/pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component.ts` (anexo §4, 4362:427). Los textos salen de `dataset.web.acceso.recuperar`:

```ts
import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaAccesoComponent } from '../../componentes/tarjeta-acceso/aq-tarjeta-acceso.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { DatosService } from '../../datos/datos.service';

/** W00 · Recuperar contraseña (F-W00, /login/recuperar): «Enviar enlace» → W00 con snackbar de correo enviado. */
@Component({
  selector: 'aq-w00-recuperar-contrasena',
  imports: [RouterLink, FormField, AqTarjetaAccesoComponent, AqCampoComponent, AqBotonComponent, AqEnlaceComponent],
  template: `
    <main class="pagina" data-codigo="W00" data-estado="recuperar">
      @if (recuperar(); as r) {
        <aq-tarjeta-acceso>
          <h1 class="titulo">{{ r.titulo }}</h1>
          <p class="texto">{{ r.texto }}</p>
          <form class="formulario" novalidate (submit)="enviarEnlace($event)">
            <aq-campo
              etiqueta="CORREO ELECTRÓNICO"
              tipo="email"
              placeholder="nombre@correo.com"
              [formField]="formulario.correo"
            />
            <button aq-boton bloque type="submit">{{ r.boton }}</button>
          </form>
          <a aq-enlace bloque routerLink="/login">{{ r.volver }}</a>
        </aq-tarjeta-acceso>
      }
    </main>
  `,
  styles: `
    .pagina {
      display: grid;
      place-items: center;
      box-sizing: border-box;
      min-height: 100vh;
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-dialogo);
      color: var(--color-texto);
      text-align: center;
    }
    .texto {
      margin: 0;
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
      text-align: center;
    }
    .formulario {
      display: flex;
      flex-direction: column;
      gap: var(--space-tarjeta-acceso-gap);
    }
  `,
})
export class W00RecuperarContrasenaComponent {
  private readonly router = inject(Router);
  private readonly datos = inject(DatosService);
  protected readonly recuperar = computed(() => this.datos.acceso()?.recuperar);
  protected readonly formulario = form(signal({ correo: '' }));

  protected enviarEnlace(evento: Event): void {
    evento.preventDefault();
    void this.router.navigateByUrl('/login?estado=correo-enviado');
  }
}
```

En `apps/web/src/app/app.routes.ts`:
- agregar el import `import { W00RecuperarContrasenaComponent } from './pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component';`;
- dentro de `routes`, justo después de `...PANTALLAS.filter((p) => !p.conBarraLateral).map(ruta),`, agregar:

```ts
  {
    path: 'login/recuperar',
    component: W00RecuperarContrasenaComponent,
    title: 'Recuperar contraseña · Alarmas QR',
    data: { codigo: 'W00' },
  },
```

- [ ] **Step 6: Ejecutar las pruebas y verificar que pasan**

Run: `npx ng test --watch=false`
Expected: PASS, `Tests 40 passed (40)`, incluido `app.routes.spec.ts` (`/` → W00 sin barra lateral).

- [ ] **Step 7: Verificación completa y commit**

```bash
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../.. && git add apps/web/src
git commit -m "$(cat <<'EOF'
W00: inicio de sesión con error ⏩, avisos 3 s y recuperar contraseña

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

---

### Task 7: W06 Ajustes de Perfil (actualizado), modal «Eliminar cuenta» y flujos T5/T8

**Files:**
- Modify: `apps/web/src/app/pantallas/w06-perfil/w06-perfil.component.ts` (reescritura completa)
- Create: `apps/web/src/app/pantallas/w06-modal-eliminar-cuenta/w06-modal-eliminar-cuenta.component.ts`
- Modify: `apps/web/src/app/app.routes.ts` (reescritura completa: hija `perfil/eliminar`)
- Test: `apps/web/src/app/pantallas/w06-perfil/w06-perfil.component.spec.ts`
- Test: `apps/web/src/app/flujos-persona-a.spec.ts`

**Interfaces:**
- Consumes: `AqTarjetaComponent`, `AqCampoComponent`, `AqBotonComponent`, `AqIconoComponent` (Tarea 2); `AqSelectorSegmentadoComponent`/`OpcionSegmentada`, `AqSwitchComponent` y `SnackbarService` (Tarea 3); `AqModalComponent` (Tarea 4); el layout (Tarea 5); W00 (Tarea 6); `DatosService.usuario`, `mensajes` y `eliminarCuenta`; `SesionService.cerrar()`.
- Produces:
  - `W06PerfilComponent` (`aq-w06-perfil`) en `/perfil`, con raíz `section[data-codigo="W06"]` y un `<router-outlet />` para el modal. «Guardar cambios» muestra `mensajes.perfilActualizado` en el snackbar; «Eliminar mi cuenta» es un enlace a `eliminar`.
  - `W06ModalEliminarCuentaComponent` (`aq-w06-modal-eliminar-cuenta`) en `/perfil/eliminar` (`data: { codigo: 'W06' }`). Es un `aq-modal[data-codigo="W06"][data-estado="eliminar"]`. «Eliminar definitivamente» está deshabilitado hasta que el campo valga exactamente `palabraDeConfirmacion`; al confirmar llama a `sesion.cerrar()` y navega a `/login?estado=eliminada`. «Conservar mi cuenta», el velo y Escape navegan a `/perfil`.
  - `app.routes.ts` con `HIJAS: Partial<Record<CodigoPantalla, Routes>>` para anidar el modal en W06.

- [ ] **Step 1: Escribir las pruebas (fallan: W06 sigue siendo el marcador)**

`apps/web/src/app/pantallas/w06-perfil/w06-perfil.component.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SesionService } from '../../datos/sesion.service';
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
  Array.from(raiz.querySelectorAll('button, a')).find((b) => b.textContent?.trim() === texto) as HTMLElement;

describe('W06 · Ajustes de Perfil', () => {
  it('muestra perfil, privacidad y eliminación con los datos del usuario', async () => {
    const { raiz } = await abrir('/perfil');
    const pagina = raiz.querySelector('[data-codigo="W06"]')!;
    for (const texto of [
      'Ajustes de Perfil',
      'Perfil',
      'NOMBRES Y APELLIDOS',
      'ALIAS PÚBLICO',
      'CORREO ELECTRÓNICO',
      'Guardar cambios',
      'Privacidad ante organizadores',
      'Así apareces en "Quiénes escanearon" (Ley 1581).',
      'Mostrar el estado de mi alarma',
      'Contar mi "Ya voy" en las métricas',
      'Eliminación de cuenta',
      'Borra permanentemente tu perfil, eventos creados y el historial de escaneos.',
      'Eliminar mi cuenta',
      'Los cambios de perfil no afectan tus alarmas en el celular: en modo invitado siguen siendo locales.',
    ]) {
      expect(pagina.textContent).toContain(texto);
    }
    const valores = Array.from(pagina.querySelectorAll('aq-campo input')).map((e) => (e as HTMLInputElement).value);
    expect(valores).toEqual([dataset.usuario.nombre, dataset.usuario.aliasPublico, dataset.usuario.correo]);
    const activo = pagina.querySelector('aq-selector-segmentado [aria-pressed="true"]');
    expect(activo?.textContent?.trim()).toBe('Solo iniciales');
    const switches = Array.from(pagina.querySelectorAll('[role="switch"]')).map((s) => s.getAttribute('aria-checked'));
    expect(switches).toEqual(['true', 'true']);
    expect(raiz.querySelector('[data-item="ajustes"]')!.classList).toContain('activo');
  });

  it('«Guardar cambios» muestra «Perfil actualizado»', async () => {
    const { raiz, estable } = await abrir('/perfil');
    botonConTexto(raiz, 'Guardar cambios').click();
    await estable();
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.perfilActualizado);
  });

  it('«Eliminar mi cuenta» abre el modal con las consecuencias del dataset', async () => {
    const { raiz, estable, router } = await abrir('/perfil');
    botonConTexto(raiz, 'Eliminar mi cuenta').click();
    await estable();
    expect(router.url).toBe('/perfil/eliminar');
    const modal = raiz.querySelector('[data-estado="eliminar"]')!;
    const d = dataset.web.eliminarCuenta;
    for (const texto of [
      '¿Eliminar tu cuenta definitivamente?',
      'Esta acción no se puede deshacer. Al confirmar:',
      `Tus ${d.eventosPublicados} eventos publicados se despublican y sus QR dejan de funcionar.`,
      `Las ${d.alarmasDeAsistentes} alarmas de asistentes dejan de recibir actualizaciones (no se borran de sus celulares).`,
      `Tus datos personales se eliminan en máximo ${d.diasParaBorrado} días (Ley 1581 · habeas data).`,
      'Consejo: descarga antes tus reportes (Reportes → PDF / CSV).',
      'Escribe ELIMINAR para confirmar',
      'Conservar mi cuenta',
      'Eliminar definitivamente',
    ]) {
      expect(modal.textContent?.replace(/\s+/g, ' ')).toContain(texto);
    }
    expect((modal.querySelector('input') as HTMLInputElement).placeholder).toBe('ELIMINAR');
  });

  it('«Eliminar definitivamente» solo se habilita al escribir ELIMINAR', async () => {
    const { raiz, estable } = await abrir('/perfil/eliminar');
    const eliminar = botonConTexto(raiz, 'Eliminar definitivamente') as HTMLButtonElement;
    expect(eliminar.disabled).toBe(true);
    const entrada = raiz.querySelector('[data-estado="eliminar"] input') as HTMLInputElement;
    entrada.value = 'eliminar';
    entrada.dispatchEvent(new Event('input'));
    await estable();
    expect(eliminar.disabled).toBe(true);
    entrada.value = 'ELIMINAR';
    entrada.dispatchEvent(new Event('input'));
    await estable();
    expect(eliminar.disabled).toBe(false);
  });

  it('«Conservar mi cuenta», el velo y Escape vuelven a /perfil sin cerrar la sesión', async () => {
    const { raiz, estable, router } = await abrir('/perfil/eliminar');
    const sesion = TestBed.inject(SesionService);
    sesion.iniciar();
    botonConTexto(raiz, 'Conservar mi cuenta').click();
    await estable();
    expect(router.url).toBe('/perfil');
    await router.navigateByUrl('/perfil/eliminar');
    await estable();
    (raiz.querySelector('[data-estado="eliminar"] [data-velo]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/perfil');
    await router.navigateByUrl('/perfil/eliminar');
    await estable();
    raiz
      .querySelector('[data-estado="eliminar"] section')!
      .dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape', bubbles: true }));
    await estable();
    expect(router.url).toBe('/perfil');
    expect(sesion.iniciada()).toBe(true);
  });
});
```

`apps/web/src/app/flujos-persona-a.spec.ts`:

```ts
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from './app.routes';
import { SesionService } from './datos/sesion.service';
import { SnackbarService } from './componentes/snackbar/snackbar.service';
import { cargarDataset, proveedoresPrueba } from '../testing/datos-prueba';
import dataset from '../../public/dataset.json';

async function iniciar(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  const estable = () => harness.fixture.whenStable();
  const tocar = async (selector: string) => {
    (raiz.querySelector(selector) as HTMLElement).click();
    await estable();
  };
  const tocarTexto = async (texto: string) => {
    const el = Array.from(raiz.querySelectorAll('button, a')).find((b) => b.textContent?.trim() === texto);
    (el as HTMLElement).click();
    await estable();
  };
  return { raiz, estable, tocar, tocarTexto, router: TestBed.inject(Router) };
}

describe('Flujos web de la Persona A (TRAZABILIDAD §3)', () => {
  it('T5 · tramo W00: entrar, ir a Ajustes por la barra y salir con el diálogo', async () => {
    const { raiz, tocar, tocarTexto, router } = await iniciar('/login');
    const sesion = TestBed.inject(SesionService);
    await tocar('button[type="submit"]');
    expect(router.url).toBe('/alarmas');
    expect(sesion.iniciada()).toBe(true);
    await tocar('[data-item="ajustes"]');
    expect(router.url).toBe('/perfil');
    expect(raiz.querySelector('[data-codigo="W06"]')).not.toBeNull();
    await tocar('[data-item="cerrar-sesion"]');
    expect(router.url).toBe('/perfil?dialogo=cerrar-sesion');
    await tocarTexto('Cancelar');
    expect(router.url).toBe('/perfil');
    await tocar('[data-item="cerrar-sesion"]');
    await tocar('aq-dialogo-confirmacion [data-accion="confirmar"]');
    expect(router.url).toBe('/login');
    expect(sesion.iniciada()).toBe(false);
    expect(raiz.querySelector('[data-codigo="W00"]')).not.toBeNull();
  });

  it('T8 · perfil → modal → escribir ELIMINAR → W00 con «Cuenta eliminada exitosamente»', async () => {
    const { raiz, estable, tocarTexto, router } = await iniciar('/perfil');
    await tocarTexto('Eliminar mi cuenta');
    expect(router.url).toBe('/perfil/eliminar');
    const entrada = raiz.querySelector('[data-estado="eliminar"] input') as HTMLInputElement;
    entrada.value = dataset.web.eliminarCuenta.palabraDeConfirmacion;
    entrada.dispatchEvent(new Event('input'));
    await estable();
    await tocarTexto('Eliminar definitivamente');
    expect(router.url).toBe('/login?estado=eliminada');
    expect(raiz.querySelector('[data-codigo="W00"]')).not.toBeNull();
    expect(TestBed.inject(SesionService).iniciada()).toBe(false);
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.cuentaEliminada);
  });
});
```

- [ ] **Step 2: Ejecutar y verificar que falla**

Run: `npx ng test --watch=false`
Expected: FAIL. En `w06-perfil` no aparecen los textos (sigue el marcador) y `/perfil/eliminar` no existe (el comodín redirige a `/login`).

- [ ] **Step 3: Página W06**

Reemplazar `apps/web/src/app/pantallas/w06-perfil/w06-perfil.component.ts` completo. Medidas y textos del anexo W06 §1 (4072:233); el snackbar es el del §2 (4072:279).

```ts
import { Component, inject, linkedSignal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaComponent } from '../../componentes/tarjeta/aq-tarjeta.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqSwitchComponent } from '../../componentes/switch/aq-switch.component';
import {
  AqSelectorSegmentadoComponent,
  OpcionSegmentada,
} from '../../componentes/selector-segmentado/aq-selector-segmentado.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';

/** W06 · Ajustes de Perfil (F-W07): perfil, privacidad ante organizadores y acceso al modal «Eliminar cuenta» (hija /perfil/eliminar). */
@Component({
  selector: 'aq-w06-perfil',
  imports: [
    RouterLink,
    RouterOutlet,
    FormField,
    AqTarjetaComponent,
    AqCampoComponent,
    AqBotonComponent,
    AqSwitchComponent,
    AqSelectorSegmentadoComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W06">
      <h1 class="titulo">Ajustes de Perfil</h1>
      <div class="fila">
        <aq-tarjeta class="perfil">
          <h2 class="titulo-tarjeta">Perfil</h2>
          <aq-campo etiqueta="NOMBRES Y APELLIDOS" [formField]="formularioPerfil.nombre" />
          <aq-campo etiqueta="ALIAS PÚBLICO" [formField]="formularioPerfil.alias" />
          <aq-campo etiqueta="CORREO ELECTRÓNICO" tipo="email" [formField]="formularioPerfil.correo" />
          <div class="accion">
            <button aq-boton type="button" (click)="guardar()">Guardar cambios</button>
          </div>
        </aq-tarjeta>
        <div class="columna">
          <aq-tarjeta class="privacidad">
            <h2 class="titulo-tarjeta">Privacidad ante organizadores</h2>
            <p class="descripcion">Así apareces en "Quiénes escanearon" (Ley 1581).</p>
            <aq-selector-segmentado
              etiqueta="Cómo apareces ante los organizadores"
              [opciones]="opcionesAparicion"
              [formField]="formularioPrivacidad.aparicion"
            />
            <aq-switch etiqueta="Mostrar el estado de mi alarma" [formField]="formularioPrivacidad.mostrarEstado" />
            <aq-switch etiqueta='Contar mi "Ya voy" en las métricas' [formField]="formularioPrivacidad.contarYaVoy" />
          </aq-tarjeta>
          <aq-tarjeta class="eliminacion" variante="peligro">
            <h2 class="titulo-tarjeta">Eliminación de cuenta</h2>
            <p class="descripcion">Borra permanentemente tu perfil, eventos creados y el historial de escaneos.</p>
            <a aq-boton variante="destructivo" routerLink="eliminar">Eliminar mi cuenta</a>
          </aq-tarjeta>
        </div>
      </div>
      <p class="nota">
        Los cambios de perfil no afectan tus alarmas en el celular: en modo invitado siguen siendo locales.
      </p>
    </section>
    <router-outlet />
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-web-bloques);
    }
    .titulo,
    .titulo-tarjeta,
    .descripcion,
    .nota {
      margin: 0;
    }
    .titulo {
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .fila {
      display: flex;
      align-items: flex-start;
      gap: var(--space-web-bloques);
    }
    .perfil {
      flex: 0 0 var(--size-tarjeta-perfil-web);
      gap: var(--space-16);
    }
    .columna {
      display: flex;
      flex: 1;
      flex-direction: column;
      gap: var(--space-web-bloques);
      min-width: 0;
    }
    .privacidad {
      gap: var(--space-12);
    }
    .eliminacion {
      gap: var(--space-10);
    }
    .eliminacion a {
      align-self: flex-start;
    }
    .titulo-tarjeta {
      font: var(--text-titulo-tarjeta-web);
      color: var(--color-texto);
    }
    .descripcion {
      font: var(--text-descripcion-web);
      color: var(--color-texto-secundario);
    }
    .accion {
      display: flex;
      justify-content: flex-end;
    }
    .nota {
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W06PerfilComponent {
  private readonly datos = inject(DatosService);
  private readonly snackbar = inject(SnackbarService);

  protected readonly opcionesAparicion: readonly OpcionSegmentada[] = [
    { valor: 'nombre-completo', texto: 'Nombre completo' },
    { valor: 'solo-iniciales', texto: 'Solo iniciales' },
    { valor: 'alias', texto: 'Alias' },
  ];

  private readonly perfil = linkedSignal(() => {
    const u = this.datos.usuario();
    return { nombre: u?.nombre ?? '', alias: u?.aliasPublico ?? '', correo: u?.correo ?? '' };
  });
  private readonly privacidad = linkedSignal(() => {
    const p = this.datos.usuario()?.privacidad;
    return {
      aparicion: (p?.apariciónEnQuienesEscanearon ?? 'solo-iniciales') as string,
      mostrarEstado: p?.mostrarEstadoDeMiAlarma ?? true,
      contarYaVoy: p?.contarMiYaVoyEnMetricas ?? true,
    };
  });
  protected readonly formularioPerfil = form(this.perfil);
  protected readonly formularioPrivacidad = form(this.privacidad);

  /** Maquetación sin backend: guardar solo confirma con el snackbar de 3 s (W06 · Actualizado). */
  protected guardar(): void {
    const mensajes = this.datos.mensajes();
    if (mensajes) this.snackbar.mostrar(mensajes.perfilActualizado);
  }
}
```

- [ ] **Step 4: Modal «Eliminar cuenta»**

`apps/web/src/app/pantallas/w06-modal-eliminar-cuenta/w06-modal-eliminar-cuenta.component.ts`. Medidas y textos del anexo W06 §3 (4072:256), con D2 y D3; los números salen de `dataset.web.eliminarCuenta`.

```ts
import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqModalComponent } from '../../componentes/modal/aq-modal.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqIconoComponent } from '../../componentes/icono/aq-icono.component';
import { DatosService } from '../../datos/datos.service';
import { SesionService } from '../../datos/sesion.service';

/** W06 · Modal eliminar cuenta (F-W08, /perfil/eliminar): fricción de escribir ELIMINAR; la acción segura es la prominente. */
@Component({
  selector: 'aq-w06-modal-eliminar-cuenta',
  imports: [FormField, AqModalComponent, AqCampoComponent, AqBotonComponent, AqIconoComponent],
  template: `
    @if (cuenta(); as d) {
      <aq-modal
        titulo="¿Eliminar tu cuenta definitivamente?"
        data-codigo="W06"
        data-estado="eliminar"
        (cerrar)="conservar()"
      >
        <p class="texto">Esta acción <strong>no se puede deshacer</strong>. Al confirmar:</p>
        <ul class="consecuencias">
          <li>
            <aq-icono nombre="qr" tamano="vineta" />
            <span class="texto"
              >Tus <strong>{{ d.eventosPublicados }} eventos publicados</strong> se despublican y sus QR dejan de
              funcionar.</span
            >
          </li>
          <li>
            <aq-icono nombre="calendario" tamano="vineta" />
            <span class="texto"
              >Las <strong>{{ d.alarmasDeAsistentes }} alarmas de asistentes</strong> dejan de recibir actualizaciones
              (no se borran de sus celulares).</span
            >
          </li>
          <li>
            <aq-icono nombre="persona" tamano="vineta" />
            <span class="texto"
              >Tus datos personales se eliminan en máximo <strong>{{ d.diasParaBorrado }} días</strong> (Ley 1581 ·
              habeas data).</span
            >
          </li>
          <li>
            <aq-icono nombre="descarga" tamano="vineta" />
            <span class="texto">Consejo: {{ consejo() }}</span>
          </li>
        </ul>
        <div class="confirmacion">
          <label class="etiqueta" for="confirmar-eliminacion"
            >Escribe {{ d.palabraDeConfirmacion }} para confirmar</label
          >
          <aq-campo
            idEntrada="confirmar-eliminacion"
            [placeholder]="d.palabraDeConfirmacion"
            [formField]="formulario.confirmacion"
          />
        </div>
        <div class="acciones">
          <button aq-boton type="button" (click)="conservar()">Conservar mi cuenta</button>
          <button aq-boton variante="destructivo" type="button" [disabled]="!confirmado()" (click)="eliminar()">
            Eliminar definitivamente
          </button>
        </div>
      </aq-modal>
    }
  `,
  styles: `
    .texto {
      margin: 0;
      font: var(--text-rotulo-web);
      color: var(--color-texto);
    }
    .consecuencias {
      display: flex;
      flex-direction: column;
      gap: var(--space-8);
      margin: 0;
      padding: 0;
      list-style: none;
    }
    li {
      display: flex;
      align-items: flex-start;
      gap: var(--space-8);
    }
    .confirmacion {
      display: flex;
      flex-direction: column;
      gap: var(--space-4);
    }
    .etiqueta {
      font: var(--text-etiqueta-externa);
      color: var(--color-texto);
    }
    .acciones {
      display: flex;
      gap: var(--space-10);
    }
    .acciones button {
      flex: 1;
    }
  `,
})
export class W06ModalEliminarCuentaComponent {
  private readonly router = inject(Router);
  private readonly sesion = inject(SesionService);
  protected readonly cuenta = inject(DatosService).eliminarCuenta;
  protected readonly formulario = form(signal({ confirmacion: '' }));

  /** «Consejo: descarga antes…»: el dataset trae la frase con mayúscula inicial. */
  protected readonly consejo = computed(() => {
    const texto = this.cuenta()?.consejo ?? '';
    return texto.charAt(0).toLowerCase() + texto.slice(1);
  });

  /** Fricción de F-W08: solo la palabra exacta del dataset habilita la acción destructiva. */
  protected readonly confirmado = computed(
    () => this.formulario.confirmacion().value() === this.cuenta()?.palabraDeConfirmacion,
  );

  protected conservar(): void {
    void this.router.navigateByUrl('/perfil');
  }

  protected eliminar(): void {
    this.sesion.cerrar();
    void this.router.navigateByUrl('/login?estado=eliminada');
  }
}
```

- [ ] **Step 5: Rutas**

Reemplazar `apps/web/src/app/app.routes.ts` completo:

```ts
import { Routes } from '@angular/router';
import { CodigoPantalla, PANTALLAS, PantallaWeb } from './navegacion/pantallas';
import { AqLayoutAppComponent } from './layout/aq-layout-app/aq-layout-app.component';
import { W00LoginComponent } from './pantallas/w00-login/w00-login.component';
import { W00RecuperarContrasenaComponent } from './pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component';
import { W01MisAlarmasComponent } from './pantallas/w01-mis-alarmas/w01-mis-alarmas.component';
import { W03DetalleEventoComponent } from './pantallas/w03-detalle-evento/w03-detalle-evento.component';
import { W04ReportesComponent } from './pantallas/w04-reportes/w04-reportes.component';
import { W05DescargarQrComponent } from './pantallas/w05-descargar-qr/w05-descargar-qr.component';
import { W06PerfilComponent } from './pantallas/w06-perfil/w06-perfil.component';
import { W06ModalEliminarCuentaComponent } from './pantallas/w06-modal-eliminar-cuenta/w06-modal-eliminar-cuenta.component';

const componentes = {
  W00: W00LoginComponent,
  W01: W01MisAlarmasComponent,
  W03: W03DetalleEventoComponent,
  W04: W04ReportesComponent,
  W05: W05DescargarQrComponent,
  W06: W06PerfilComponent,
} as const;

/** Rutas hijas que se dibujan sobre su página (modal sobre W06: /perfil/eliminar). */
const HIJAS: Partial<Record<CodigoPantalla, Routes>> = {
  W06: [
    {
      path: 'eliminar',
      component: W06ModalEliminarCuentaComponent,
      title: 'Eliminar cuenta · Alarmas QR',
      data: { codigo: 'W06' },
    },
  ],
};

const ruta = (p: PantallaWeb) => ({
  path: p.ruta,
  component: componentes[p.codigo],
  title: `${p.titulo} · Alarmas QR`,
  data: { codigo: p.codigo },
  children: HIJAS[p.codigo],
});

/** Rutas de docs/TRAZABILIDAD.md §2, generadas desde PANTALLAS. Los estados (?estado=, ?dialogo=) se leen con query params. */
export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  ...PANTALLAS.filter((p) => !p.conBarraLateral).map(ruta),
  {
    path: 'login/recuperar',
    component: W00RecuperarContrasenaComponent,
    title: 'Recuperar contraseña · Alarmas QR',
    data: { codigo: 'W00' },
  },
  {
    path: '',
    component: AqLayoutAppComponent,
    children: PANTALLAS.filter((p) => p.conBarraLateral).map(ruta),
  },
  { path: '**', redirectTo: 'login' },
];
```

- [ ] **Step 6: Ejecutar las pruebas y verificar que pasan**

Run: `npx ng test --watch=false`
Expected: PASS, `Tests 47 passed (47)`, con `app.routes.spec.ts` en verde: `/perfil` ahora es la página real, que también lleva `data-codigo="W06"`.

- [ ] **Step 7: Verificación completa y commit**

```bash
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
cd ../.. && git add apps/web/src
git commit -m "$(cat <<'EOF'
W06: ajustes de perfil, modal Eliminar cuenta y flujos T5/T8

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

---

### Task 8: Verificación pixel-perfect contra Figma, documentación y cierre de la rama

**Files:**
- Create: `docs/verificacion/W00.png`, `W00-error.png`, `W00-recuperar.png`, `W00-correo-enviado.png`, `W00-eliminada.png`, `W06.png`, `W06-actualizado.png`, `W06-eliminar.png`, `dialogo-cerrar-sesion.png`, `barra-colapsada.png` y sus parejas `*-figma.png`
- Modify: `docs/verificacion/README.md` (sección «Web · Plan 3»)
- Modify: `README.md` (sección «Plan 3 · web de la Persona A» y viñeta web de «Cómo continuar (Persona B)»)
- Modify: `CLAUDE.md` (versión de tokens y regla de la fila de cabecera del modal)
- Modify: los componentes y páginas de las Tareas 2–7, solo si la comparación encuentra diferencias

**Interfaces:**
- Consumes: todas las páginas y componentes de las Tareas 1–7.
- Produces: diez parejas Figma / implementación a 1280×820, la tabla de estado por marco y la documentación para la Persona B.

| Nombre | Marco Figma | URL o interacción en la implementación |
|---|---|---|
| `W00` | `4072:1861` | `/login` |
| `W00-error` | `4362:474` | `/login`; escribir `andres@correo.com` en el correo, enfocar la contraseña y escribir `secret` (6 puntos) |
| `W00-recuperar` | `4362:427` | `/login/recuperar` |
| `W00-correo-enviado` | `4362:449` | `/login?estado=correo-enviado` (capturar antes de 3 s) |
| `W00-eliminada` | `4072:1878` | `/login?estado=eliminada` (capturar antes de 3 s) |
| `W06` | `4072:233` | `/perfil` |
| `W06-actualizado` | `4072:279` | `/perfil`, tocar «Guardar cambios» (capturar antes de 3 s) |
| `W06-eliminar` | `4072:256` | `/perfil/eliminar` |
| `dialogo-cerrar-sesion` | `4360:347` | `/alarmas?dialogo=cerrar-sesion` (el fondo es el marcador W01 de la Persona B; comparar solo el diálogo) |
| `barra-colapsada` | `4357:1868` | `/alarmas`, tocar «Colapsar menú» (comparar solo la barra lateral y la barra superior) |

- [ ] **Step 1: Levantar la app**

```bash
cd /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app/apps/web
source ~/.nvm/nvm.sh && nvm use
npx ng serve --port 4200
```

Ejecutarlo en segundo plano (`run_in_background`) y esperar a que imprima `Local: http://localhost:4200/`.

- [ ] **Step 2: Exportar los marcos de Figma**

Por cada fila de la tabla, llamar a `mcp__plugin_figma_figma__get_screenshot` con `fileKey: "4nHD4ygcnP33UH0gAhaii5"`, el `nodeId` de la columna «Marco Figma» y `maxDimension: 1280`. La respuesta trae una URL de corta duración con instrucciones de `curl`; guardar la imagen en `docs/verificacion/<Nombre>-figma.png`, por ejemplo:

```bash
curl -sSL "<url devuelta por get_screenshot>" -o /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app/docs/verificacion/W00-figma.png
file /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app/docs/verificacion/W00-figma.png
```

Expected: `PNG image data, 1280 x 820` (±1 px por el borde del marco del mockup).

- [ ] **Step 3: Capturar los estados que se abren por URL (Chrome sin interfaz)**

```bash
cd /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app
capturar() { google-chrome --headless=new --disable-gpu --hide-scrollbars --force-device-scale-factor=1 \
  --window-size=1280,820 --virtual-time-budget="$3" --screenshot="docs/verificacion/$1.png" "http://localhost:4200$2"; }
capturar W00 /login 3000
capturar W00-recuperar /login/recuperar 3000
capturar W00-correo-enviado '/login?estado=correo-enviado' 1500
capturar W00-eliminada '/login?estado=eliminada' 1500
capturar W06 /perfil 3000
capturar W06-eliminar /perfil/eliminar 3000
capturar dialogo-cerrar-sesion '/alarmas?dialogo=cerrar-sesion' 3000
file docs/verificacion/W00.png
```

Expected: siete PNG de 1280×820. `--virtual-time-budget=1500` deja cargar las fuentes y el dataset sin que pasen los 3 s del snackbar.

- [ ] **Step 4: Capturar los estados que necesitan interacción (Playwright MCP)**

Con las herramientas `mcp__plugin_playwright_playwright__*` (cargarlas con ToolSearch):
1. `browser_resize` a 1280×820.
2. **W00-error:** `browser_navigate` a `http://localhost:4200/login`. Con `browser_type`, escribir `andres@correo.com` en el campo «CORREO ELECTRÓNICO»; hacer `browser_click` en el campo «CONTRASEÑA» (el foco dispara el error ⏩) y escribir `secret`. Quitar el foco con `browser_press_key` `Tab` para que no se vea el borde de foco y guardar con `browser_take_screenshot` (`filename`) en `docs/verificacion/W00-error.png`.
3. **W06-actualizado:** `browser_navigate` a `/perfil`, `browser_click` en «Guardar cambios» y capturar de inmediato en `docs/verificacion/W06-actualizado.png`.
4. **barra-colapsada:** `browser_navigate` a `/alarmas`, `browser_click` en «Colapsar menú», esperar 300 ms (transición de 250 ms) con `browser_wait_for` y capturar en `docs/verificacion/barra-colapsada.png`.

Si Playwright guarda las capturas en su carpeta de salida, moverlas con `mv` a `docs/verificacion/`.

- [ ] **Step 5: Comparar cada pareja y corregir**

Abrir con `Read` cada `<Nombre>.png` junto a su `<Nombre>-figma.png` y revisar esta lista por marco:
- **Posiciones y tamaños:** posición y tamaño de la tarjeta, el modal o el diálogo (anexo, columnas «Pos.» y «Tamaño»).
- **Alturas:** botones 44, campos 48 (D3), snackbar 36, barra superior 64, ítems 38 o 40.
- **Radios y bordes:** 14, 12 y píldora; bordes de 1.5 Gris Borde y Coral Texto en error y en peligro.
- **Tipografías:** tamaño y peso según el anexo, salvo las decisiones D3 y D5.
- **Amarillo:** un solo amarillo de acción por pantalla (el logotipo es la excepción D11).
- **Textos:** literales idénticos al anexo.
- **Estados:** activo en Tinta y error con color más mensaje.

Las diferencias esperadas no se corrigen; hay que anotarlas: campos de 48 y etiqueta 12 (D3), botón de 14.5 (D5), snackbar centrado (D7), enlace de recuperar presente en los marcos con snackbar (D8), métricas de fuente del navegador y fondo de W01, que es el marcador de la Persona B. Cualquier otra diferencia se corrige en el componente o la página, siempre con tokens (si falta uno, se agrega a `design-tokens.json` y a los dos `tokens.css` como en la Tarea 1). Después se vuelve a capturar ese estado. Tras cada corrección, ejecutar `npx ng test --watch=false && npm run lint`.

- [ ] **Step 6: Tabla de verificación web**

Agregar al final de `docs/verificacion/README.md`:

```markdown
## Web · Plan 3 (Persona A)

Parejas a 1280×820: `<nombre>-figma.png` (exportada con `get_screenshot` del MCP de Figma, `maxDimension` 1280) y `<nombre>.png`. Esta última se captura con Chrome sin interfaz (`--window-size=1280,820 --virtual-time-budget`) sobre `ng serve`, o con Playwright MCP en los estados que requieren interacción (ver `docs/superpowers/plans/2026-09-21-plan3-web-persona-a.md`, Tarea 8). Diferencias aceptadas por decisión del plan: campos de 48 con etiqueta 12 y placeholder Gris Medio (D3), botones de 14.5 (D5), snackbar centrado en la ventana (D7) y «¿Olvidaste tu contraseña?» también en los estados con snackbar (D8).

| Nombre | Marco Figma | Estado |
|---|---|---|
| W00 | 4072:1861 | (resultado de la comparación) |
| W00-error | 4362:474 | (resultado de la comparación) |
| W00-recuperar | 4362:427 | (resultado de la comparación) |
| W00-correo-enviado | 4362:449 | (resultado de la comparación) |
| W00-eliminada | 4072:1878 | (resultado de la comparación) |
| W06 | 4072:233 | (resultado de la comparación) |
| W06-actualizado | 4072:279 | (resultado de la comparación) |
| W06-eliminar | 4072:256 | (resultado de la comparación) |
| dialogo-cerrar-sesion | 4360:347 | (resultado de la comparación) |
| barra-colapsada | 4357:1868 | (resultado de la comparación) |
```

En la columna «Estado», reemplazar cada «(resultado de la comparación)» por lo que se observó en el Step 5, en el mismo formato de la tabla móvil: «ok · pixel-perfect (…)» u «ok · diferencias aceptadas: …», nombrando cada diferencia y la decisión que la justifica.

- [ ] **Step 7: README y CLAUDE.md**

En `README.md`, insertar justo antes de `## Cómo continuar (Persona B)`:

```markdown
## Plan 3 · web de la Persona A

Páginas de `apps/web` pixel-perfect contra Figma a 1280×820 (`docs/superpowers/plans/2026-09-21-plan3-web-persona-a.md`; medidas en `docs/superpowers/specs/2026-09-21-medidas-figma-web-persona-a.md`).

**Páginas hechas:**
- W00 «Inicio de sesión» (`/login`), con el error de credenciales ⏩ (el primer foco en la contraseña lo simula) y los avisos de 3 s `?estado=correo-enviado` y `?estado=eliminada`.
- W00 «Recuperar contraseña» (`/login/recuperar`).
- W06 «Ajustes de Perfil» (`/perfil`), con el aviso «Perfil actualizado».
- Modal «Eliminar cuenta» (`/perfil/eliminar`): hay que escribir ELIMINAR y «Conservar mi cuenta» es el primario.
- Diálogo «¿Cerrar sesión?» (`?dialogo=cerrar-sesion` sobre cualquier página con barra lateral).
- Barra superior y barra lateral reales (iconos del DS, colapsable a 64).

W01, W03, W04 y W05 siguen como marcadores de la Persona B, ya dentro del layout real.

**Componentes L09** (`src/app/componentes/`, selector `aq-*`, solo tokens):
- `aq-icono` y `aq-logotipo`.
- `button|a[aq-boton]` (primario, secundario o destructivo, y `bloque`) y `a|button[aq-enlace]`.
- `aq-campo` (Signal Forms, `[formField]`), `aq-switch` y `aq-selector-segmentado`.
- `aq-tarjeta` (normal o peligro) y `aq-tarjeta-acceso`.
- `aq-snackbar` + `SnackbarService.mostrar(texto)`.
- `aq-modal` y `aq-dialogo-confirmacion`.
- `aq-barra-superior` y `aq-barra-lateral`.

**Guardia de tokens:** `npm run lint` ejecuta `scripts/verificar-tokens.mjs`, que falla si `src/app` escribe a mano un color, un `rgb()`/`rgba()` o una medida en `px`.

**Decisiones D1–D16** (sección «Decisiones» del plan). Las que se pueden querer revertir:
- El modal «Eliminar cuenta» sigue el mockup: 481, sin miga + ✕ y título 18 (D2).
- Campos de 48 con etiqueta 12 y placeholder Gris Medio, frente a los 46/11 del mockup (D3).
- Tarjeta de acceso de 520 (D4).
- Botones uniformes de 14.5 (D5).
- Modal y diálogo en la URL con `cdkTrapFocus` en vez de `CdkOverlay` (D6).
- Snackbar global centrado (D7).
- Error de credenciales por foco ⏩ (D8).
- Switch apagado en contorno Tinta (D9).

**Pendiente en el repo de UX:**
- DS §7, filas «Tarjeta de acceso» (400 → 520) y «Diálogo modal» (540–600 con miga + ✕ → 481 sin miga, título 18).
- La regla de CLAUDE.md sobre la fila de cabecera del modal.
- Los marcos de W00 con snackbar sin el enlace «¿Olvidaste tu contraseña?».
- El snackbar descentrado de «correo enviado».
- El estado apagado del switch y la excepción del logotipo amarillo.
```

En la sección `## Cómo continuar (Persona B)` de `README.md`, reemplazar la viñeta que empieza con `- Web: cada página está en` por:

```markdown
- Web: cada página está en `src/app/pantallas/<código>-<nombre>/`. Para construir W01, reemplazar el `<aq-pantalla-marcador>` de `w01-mis-alarmas.component.ts` por la página real, con raíz `data-codigo="W01"` (y `data-estado` en sus subestados). Las rutas salen de `navegacion/pantallas.ts` y de `app.routes.ts`: una ruta hija que se dibuje sobre su página va en `HIJAS`, como `/perfil/eliminar`, y los estados de filtro y búsqueda van como query params, que llegan como `input()` gracias a `withComponentInputBinding()`. Los datos salen de `DatosService` (señales `usuario`, `web`, `alarmas`, `mensajes`, `acceso`, `eliminarCuenta`). Los componentes compartidos están en `src/app/componentes/` (ver «Plan 3»); faltan los de tablero de L09: indicador, tabla, gráfica, píldora de filtro, chip web, afiche y paginador. Pruebas: `proveedoresPrueba(routes)` + `RouterTestingHarness.create(url)` + `cargarDataset()` de `src/testing/datos-prueba.ts`, igual que en `flujos-persona-a.spec.ts`. Tokens: solo variables de `tokens.css`; `npm run lint` lo verifica.
```

En `CLAUDE.md`:
- Reemplazar `Fuente de verdad: \`packages/tokens/design-tokens.json\` v1.10` por `Fuente de verdad: \`packages/tokens/design-tokens.json\` v1.11 (v1.11 del 2026-09-21: medidas web de la Persona A, Plan 3)`.
- Reemplazar la línea `- La fila de cabecera de un modal (miga + ✕) es un solo control que cierra.` por `- La fila de cabecera de un modal (miga + ✕) es un solo control que cierra. Excepción vigente: el modal «Eliminar cuenta» sigue el mockup web v1.5 (sin miga; se cierra con «Conservar mi cuenta», Escape o el velo; D2 del Plan 3, pendiente de reflejar en el repo de UX).`

- [ ] **Step 8: Verificación final completa**

```bash
cd /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app
cmp packages/tokens/dataset.json apps/web/public/dataset.json && cmp packages/tokens/dataset.json apps/movil/app/src/main/assets/dataset.json && cmp packages/tokens/tokens.css apps/web/src/tokens.css && echo DATOS-Y-TOKENS-OK
cd apps/web && source ~/.nvm/nvm.sh && nvm use
npx prettier --write "src/**/*.{ts,html,css}"
npx ng test --watch=false && npm run lint && npx ng build --configuration production
```

Expected: `DATOS-Y-TOKENS-OK`, 47 pruebas (o más, si el Step 5 agregó alguna) en verde, lint y build sin errores. Detener el `ng serve` del Step 1.

- [ ] **Step 9: Commit**

```bash
cd /home/alejo/proyectos/MISO_semestre3/alarmas-qr-app
git add docs/verificacion README.md CLAUDE.md apps/web packages/tokens
git commit -m "$(cat <<'EOF'
W00/W06: verificación pixel-perfect web y documentación del Plan 3

Co-Authored-By: Claude Opus 5 (1M context) <noreply@anthropic.com>
EOF
)"
```

- [ ] **Step 10: Cerrar la rama**

Usar la skill `superpowers:finishing-a-development-branch`. **Pedir confirmación al usuario antes de `git push` y de abrir el PR** (acción visible para la Persona B). El PR va de `feature/plan3-web-persona-a` a `main`. Su descripción resume las páginas, los componentes, las decisiones D1–D16 y los pendientes de UX, y termina con la línea `🤖 Generated with [Claude Code](https://claude.com/claude-code)`.

---

## Verificación de cobertura de la spec (Plan 3)

| Spec | Requisito | Tarea |
|---|---|---|
| §0.2 | Componentes propios sobre el CDK que replican L09; sin Angular Material | 2–5 (CDK a11y en 4) |
| §0.3 | Sin trampas: iconos y logotipo vectoriales, nada pintado con imágenes | 2 (D12), 3 (icono del snackbar) |
| §0.4 | Pixel-perfect a 1280×820 con anchos fijos del mockup (formulario, barra 208/64, diálogo 420) y el resto estirable | 2–7 (tokens), 8 (comparación) |
| §3.1 | `datos/` (`DatosService`, `SesionService`, modelos) | 1 |
| §3.1 | `componentes/<nombre>/` con selector `aq-*` y solo tokens | 2–5 + guardia (1) |
| §3.1 | `pantallas/<código-nombre>/`: `w00-login`, `w00-recuperar-contrasena`, `w06-perfil`, `w06-modal-eliminar-cuenta`; marcadores de W01/W03/W04/W05 intactos | 6, 7 |
| §3.1 | `layout/aq-layout-app` con barra lateral 208 colapsable a 64 (preferencia en señal); W00 sin barra | 5 |
| §3.2 | `/login` (+ `?estado=correo-enviado|eliminada`, snackbar 3 s), `/login/recuperar` | 6 |
| §3.2 | `/perfil` con hija `/perfil/eliminar` (modal, velo 45 %) | 7 (D6: en el árbol del router en vez de CdkOverlay) |
| §3.2 | `?dialogo=cerrar-sesion` global → diálogo 420, velo 55 % → «Cerrar sesión» a `/login` | 5 |
| §3.2 | `data: { codigo }` por ruta; error de W00 por el foco de la contraseña ⏩ | 6, 7 (D8) |
| §4 web | `aq-boton` (3 variantes, 44, relleno 24, píldora), `aq-campo` con error, `aq-enlace`, `aq-tarjeta-acceso`, `aq-tarjeta` | 2 |
| §4 web | `aq-snackbar` (3 s), `aq-switch`, `aq-selector-segmentado` | 3 |
| §4 web | `aq-modal`, `aq-dialogo-confirmacion` | 4 |
| §4 web | `aq-barra-lateral` (icono 20, píldora activa Tinta, colapsar 36×36), `aq-icono` | 5, 2 |
| §4 web | `aq-cabecera-modal`, `aq-item-barra-lateral` como componentes | No se construyen (D16): el único modal no lleva miga (D2) y el ítem es marcado interno de la barra |
| §5.1 | Medidas de Figma transcritas a tokens; medida nueva → `design-tokens.json` primero | 1, anexo 2026-09-21 |
| §5.2 | Capturas web a 1280×820 por ruta y estado; parejas en `docs/verificacion/` | 8 |
| §5.3 | Lista de comprobación (amarillo, 44, radios, AA, color + forma, diálogo en eliminar/salir) | 8 Step 5, Global Constraints |
| §6 | T5 tramo W00 (login → `/alarmas`) y T8 (`/perfil` → `/perfil/eliminar` → `/login?estado=eliminada`) con `RouterTestingHarness` | 6, 7 (`flujos-persona-a.spec.ts`) |
| §6 | Comandos `ng test`, `ng build --configuration production`; CI sin cambios de fondo | todas (Vitest por D15) |
| §7 | README con cómo continuar como Persona B; CLAUDE.md actualizado | 8 |
