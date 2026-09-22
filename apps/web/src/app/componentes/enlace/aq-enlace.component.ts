import { Component, booleanAttribute, input } from '@angular/core';

export type VarianteEnlace = 'enlace' | 'miga';

/**
 * Enlace de texto web: Azul Texto 14 subrayado dentro de un marco de 44 (área de puntero igual al
 * botón). La variante «miga» es la miga de pan («‹ Mis alarmas / …», W03/W04/W05): Gris Texto sin
 * subrayado, sin el marco de 44 ni el centrado del enlace normal — ocupa solo su contenido.
 */
@Component({
  selector: 'a[aq-enlace], button[aq-enlace]',
  template: `<ng-content />`,
  host: {
    '[attr.data-bloque]': "bloque() ? '' : null",
    '[attr.data-variante]': 'variante()',
  },
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
    :host([data-bloque]) {
      width: 100%;
    }
    :host([data-variante='miga']) {
      display: inline-flex;
      align-self: flex-start;
      justify-content: flex-start;
      height: auto;
      padding: var(--space-2) 0;
      color: var(--color-texto-secundario);
      text-decoration: none;
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
  readonly variante = input<VarianteEnlace>('enlace');
}
