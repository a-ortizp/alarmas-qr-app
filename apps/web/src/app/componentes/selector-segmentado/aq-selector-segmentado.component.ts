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
