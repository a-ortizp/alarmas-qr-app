import { Component, computed, input } from '@angular/core';

const MODULOS_TABLERO = 8;

export type VarianteQrDecorativo = 'qr' | 'tablero';

interface Modulo {
  readonly fila: number;
  readonly columna: number;
}

// Patrón exacto del componente «código QR · evento» (Figma 4010:2), extraído módulo a módulo del
// diseño: 21×21, marcadores de posición y temporización. No codifica datos reales ni es
// escaneable — es maquetación, «verosímil».
const FILAS_QR: readonly string[] = [
  '111111100010001111111',
  '100000101110001000001',
  '101110100010001011101',
  '101110100100001011101',
  '101110101000001011101',
  '100000100100101000001',
  '111111101010101111111',
  '000000001010000000000',
  '111100100100111001000',
  '110001000001111001001',
  '011101110110101111000',
  '001010000110111001101',
  '111010110110111101001',
  '000000001000100101110',
  '111111100101101000011',
  '100000101100111000110',
  '101110101000011000110',
  '101110101010000111111',
  '101110101000111000101',
  '100000100100111111001',
  '111111100000010100111',
];

function moduloQr(fila: number, columna: number): boolean {
  return FILAS_QR[fila][columna] === '1';
}

function moduloTablero(fila: number, columna: number): boolean {
  return (fila + columna) % 2 === 1;
}

/**
 * Gráfico QR decorativo: patrón «verosímil» de 21×21 con marcadores de posición y temporización
 * (variante por defecto `qr`, Figma «código QR · evento» 4010:2 — vista previa del afiche), o un
 * tablero de 8×8 alterno (variante `tablero`, Figma «checkerboard» 4091:2 — junto a cada evento de
 * W05). Ninguna de las dos codifica datos reales ni es escaneable: es maquetación.
 */
@Component({
  selector: 'aq-qr-decorativo',
  template: `
    <svg
      [attr.viewBox]="'0 0 ' + modulos() + ' ' + modulos()"
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
  readonly variante = input<VarianteQrDecorativo>('qr');
  protected readonly modulos = computed(() =>
    this.variante() === 'tablero' ? MODULOS_TABLERO : FILAS_QR.length,
  );

  protected readonly modulosRellenos = computed<Modulo[]>(() => {
    const tablero = this.variante() === 'tablero';
    const lado = tablero ? MODULOS_TABLERO : FILAS_QR.length;
    const relleno = tablero ? moduloTablero : moduloQr;
    const celdas: Modulo[] = [];
    for (let fila = 0; fila < lado; fila++) {
      for (let columna = 0; columna < lado; columna++) {
        if (relleno(fila, columna)) celdas.push({ fila, columna });
      }
    }
    return celdas;
  });
}
