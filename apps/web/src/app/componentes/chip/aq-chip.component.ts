import { Component, input } from '@angular/core';

export type VarianteChip = 'creada-por-mi' | 'escaneada' | 'publicado' | 'activa' | 'eliminada';

/** Chip web (DS §7 «Chips»): 19–20 de alto, píldora, Archivo Bold 12. Origen «Creada por mí»: contorno; «Escaneada» y estado: relleno + contorno (o solo contorno en «Eliminada»). */
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
    :host(.escaneada),
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
