import { Component, booleanAttribute, input } from '@angular/core';

/** Enlace de texto web: Azul Texto 14 subrayado dentro de un marco de 44 (área de puntero igual al botón). */
@Component({
  selector: 'a[aq-enlace], button[aq-enlace]',
  template: `<ng-content />`,
  host: { '[attr.data-bloque]': "bloque() ? '' : null" },
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
