import { Component, computed, input } from '@angular/core';
import { ICONOS, NombreIcono } from './iconos';

export type TamanoIcono = 'barra' | 'vineta' | 'advertencia';

/** Icono de línea del DS dibujado con trazados (D12): toma el color del texto que lo rodea. */
@Component({
  selector: 'aq-icono',
  template: `
    <svg
      [attr.viewBox]="caja()"
      fill="none"
      stroke="currentColor"
      [attr.stroke-width]="definicion().trazo"
      stroke-linecap="round"
      stroke-linejoin="round"
      aria-hidden="true"
      focusable="false"
    >
      @for (forma of definicion().formas; track $index) {
        <path
          [attr.d]="forma.d"
          [attr.fill]="forma.relleno ? 'currentColor' : 'none'"
          [attr.stroke]="forma.relleno ? 'none' : 'currentColor'"
        />
      }
    </svg>
  `,
  host: {
    '[class.barra]': "tamano() === 'barra'",
    '[class.vineta]': "tamano() === 'vineta'",
    '[class.advertencia]': "tamano() === 'advertencia'",
  },
  styles: `
    :host {
      display: inline-flex;
      flex: none;
    }
    :host(.barra) {
      width: var(--size-icono-barra-lateral);
      height: var(--size-icono-barra-lateral);
    }
    :host(.vineta) {
      width: var(--size-icono-vineta);
      height: var(--size-icono-vineta);
    }
    :host(.advertencia) {
      width: var(--size-icono-advertencia);
      height: var(--size-icono-advertencia);
    }
    svg {
      width: 100%;
      height: 100%;
    }
  `,
})
export class AqIconoComponent {
  readonly nombre = input.required<NombreIcono>();
  readonly tamano = input<TamanoIcono>('barra');
  protected readonly definicion = computed(() => ICONOS[this.nombre()]);
  protected readonly caja = computed(
    () => `0 0 ${this.definicion().caja} ${this.definicion().caja}`,
  );
}
