import { Component, computed, input } from '@angular/core';

const MODULOS = 8;

interface Modulo {
  readonly fila: number;
  readonly columna: number;
}

/**
 * QR decorativo (Figma nodo «checkerboard» 4091:2, junto a cada evento de W05 y en la vista
 * previa del afiche): tablero de 8×8 a modo de marcador visual, no un QR real escaneable.
 */
@Component({
  selector: 'aq-qr-decorativo',
  template: `
    <svg
      [attr.viewBox]="'0 0 ' + modulos + ' ' + modulos"
      [attr.aria-label]="'Código QR de ' + etiqueta()"
      role="img"
    >
      @for (modulo of modulosRellenos(); track modulo.fila + '-' + modulo.columna) {
        <rect [attr.x]="modulo.columna" [attr.y]="modulo.fila" width="1" height="1" />
      }
    </svg>
  `,
  styles: `
    :host {
      display: inline-flex;
      flex: none;
    }
    svg {
      width: 100%;
      height: 100%;
      fill: currentColor;
    }
  `,
})
export class AqQrDecorativoComponent {
  readonly etiqueta = input('evento');
  protected readonly modulos = MODULOS;

  protected readonly modulosRellenos = computed<Modulo[]>(() => {
    const celdas: Modulo[] = [];
    for (let fila = 0; fila < MODULOS; fila++) {
      for (let columna = 0; columna < MODULOS; columna++) {
        if ((fila + columna) % 2 === 1) celdas.push({ fila, columna });
      }
    }
    return celdas;
  });
}
