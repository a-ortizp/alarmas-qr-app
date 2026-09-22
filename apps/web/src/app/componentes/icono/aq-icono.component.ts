import { Component, computed, input } from '@angular/core';
import { ICONOS, NombreIcono } from './iconos';

export type TamanoIcono = 'barra' | 'vineta' | 'advertencia' | 'casilla';

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
  // El tamaño va en un atributo y no en una clase: una clase como «barra» chocaría con las
  // reglas encapsuladas del padre (aq-barra-lateral y aq-barra-superior usan .barra).
  host: { '[attr.data-tamano]': 'tamano()' },
  styles: `
    :host {
      display: inline-flex;
      flex: none;
    }
    :host([data-tamano='barra']) {
      width: var(--size-icono-barra-lateral);
      height: var(--size-icono-barra-lateral);
    }
    :host([data-tamano='vineta']) {
      width: var(--size-icono-vineta);
      height: var(--size-icono-vineta);
    }
    :host([data-tamano='advertencia']) {
      width: var(--size-icono-advertencia);
      height: var(--size-icono-advertencia);
    }
    :host([data-tamano='casilla']) {
      width: var(--size-icono-casilla);
      height: var(--size-icono-casilla);
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
