import { Component, computed, input } from '@angular/core';

const MODULOS_QR = 21;
const TAMANO_FINDER = 7;
const MODULOS_TABLERO = 8;

export type VarianteQrDecorativo = 'qr' | 'tablero';

interface Modulo {
  readonly fila: number;
  readonly columna: number;
}

function esFinder(fila: number, columna: number, filaBase: number, columnaBase: number): boolean {
  const f = fila - filaBase;
  const c = columna - columnaBase;
  if (f < 0 || f >= TAMANO_FINDER || c < 0 || c >= TAMANO_FINDER) return false;
  if (f === 0 || f === TAMANO_FINDER - 1 || c === 0 || c === TAMANO_FINDER - 1) return true;
  return f >= 2 && f <= 4 && c >= 2 && c <= 4;
}

function enZonaFinder(fila: number, columna: number): boolean {
  const enEsquina = (f: number, c: number) => f < TAMANO_FINDER + 1 && c < TAMANO_FINDER + 1;
  return (
    enEsquina(fila, columna) ||
    enEsquina(fila, MODULOS_QR - 1 - columna) ||
    enEsquina(MODULOS_QR - 1 - fila, columna)
  );
}

function moduloQr(fila: number, columna: number): boolean {
  if (esFinder(fila, columna, 0, 0)) return true;
  if (esFinder(fila, columna, 0, MODULOS_QR - TAMANO_FINDER)) return true;
  if (esFinder(fila, columna, MODULOS_QR - TAMANO_FINDER, 0)) return true;
  if (enZonaFinder(fila, columna)) return false;
  if (fila === 6 || columna === 6) return (fila + columna) % 2 === 0;
  // Relleno pseudoaleatorio determinista (sin Math.random): el patrón es siempre el mismo, no
  // codifica ningún dato real — es maquetación, «verosímil» (Figma 4010:2), no un QR escaneable.
  const semilla = (fila * 13 + columna * 7 + fila * columna * 3) % 11;
  return semilla < 5;
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
    this.variante() === 'tablero' ? MODULOS_TABLERO : MODULOS_QR,
  );

  protected readonly modulosRellenos = computed<Modulo[]>(() => {
    const tablero = this.variante() === 'tablero';
    const lado = tablero ? MODULOS_TABLERO : MODULOS_QR;
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
