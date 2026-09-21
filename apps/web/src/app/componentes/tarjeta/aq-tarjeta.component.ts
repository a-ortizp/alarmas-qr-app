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
