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
