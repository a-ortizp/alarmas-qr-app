import { Component, booleanAttribute, input } from '@angular/core';

export type VarianteBoton = 'primario' | 'secundario' | 'destructivo';

/** Botón web L09 (DS comp. 43): 44 de alto, píldora, relleno 13/24. Primario amarillo, secundario contorno Tinta, destructivo contorno Coral Texto. */
@Component({
  selector: 'button[aq-boton], a[aq-boton]',
  template: `<ng-content />`,
  host: {
    '[attr.data-variante]': 'variante()',
    '[attr.data-bloque]': "bloque() ? '' : null",
  },
  styles: `
    :host {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: var(--space-8);
      box-sizing: border-box;
      appearance: none;
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
    :host([data-variante='primario']) {
      background: var(--color-primario);
      color: var(--color-primario-texto);
    }
    :host([data-variante='secundario']) {
      background: var(--color-blanco);
      color: var(--color-tinta);
      border-color: var(--color-tinta);
    }
    :host([data-variante='destructivo']) {
      background: var(--color-blanco);
      color: var(--color-destructivo);
      border-color: var(--color-destructivo);
    }
    :host([data-bloque]) {
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
