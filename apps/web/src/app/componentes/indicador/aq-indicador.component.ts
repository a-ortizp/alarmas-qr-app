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
      flex: 1 0 0;
      min-width: 0;
      height: var(--size-indicador-web-h);
      padding: var(--space-web-indicadores) var(--space-16);
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
