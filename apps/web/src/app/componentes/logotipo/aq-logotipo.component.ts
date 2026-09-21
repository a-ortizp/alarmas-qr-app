import { Component, input } from '@angular/core';

/** Logotipo «Alarmas QR»: cuadro Amarillo Energía con anillo y punto Tinta (Figma 4072:1864 / 4072:236). */
@Component({
  selector: 'aq-logotipo',
  template: `
    <svg viewBox="0 0 36 36" aria-hidden="true" focusable="false">
      <rect class="fondo" width="36" height="36" rx="9" />
      <rect
        class="anillo"
        x="10.2855"
        y="10.2855"
        width="15.429"
        height="15.429"
        rx="3.2145"
        stroke-width="2.571"
      />
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
