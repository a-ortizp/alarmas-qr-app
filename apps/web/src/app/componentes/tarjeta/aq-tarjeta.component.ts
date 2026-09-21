import { Component, input } from '@angular/core';

/** Tarjeta web (L07/L09): radio 14, borde Gris Borde 1.5, relleno 22; «peligro» con borde Coral Texto y relleno 20 (W06). */
@Component({
  selector: 'aq-tarjeta',
  template: `<ng-content />`,
  host: { '[attr.data-variante]': 'variante()' },
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      /* Figma dibuja el borde hacia dentro: el relleno se mide desde el borde exterior. */
      padding: calc(var(--space-tarjeta-web) - var(--stroke-borde));
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
    }
    :host([data-variante='peligro']) {
      padding: calc(var(--space-tarjeta-peligro) - var(--stroke-borde));
      border-color: var(--color-destructivo);
    }
  `,
})
export class AqTarjetaComponent {
  readonly variante = input<'normal' | 'peligro'>('normal');
}
