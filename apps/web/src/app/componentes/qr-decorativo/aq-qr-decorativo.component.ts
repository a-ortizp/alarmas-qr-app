import { Component, computed, input } from '@angular/core';

const MODULOS = 21;
const TAMANO_FINDER = 7;

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
    enEsquina(fila, MODULOS - 1 - columna) ||
    enEsquina(MODULOS - 1 - fila, columna)
  );
}

function moduloRelleno(fila: number, columna: number): boolean {
  if (esFinder(fila, columna, 0, 0)) return true;
  if (esFinder(fila, columna, 0, MODULOS - TAMANO_FINDER)) return true;
  if (esFinder(fila, columna, MODULOS - TAMANO_FINDER, 0)) return true;
  if (enZonaFinder(fila, columna)) return false;
  if (fila === 6 || columna === 6) return (fila + columna) % 2 === 0;
  // Relleno pseudoaleatorio determinista (sin Math.random): el patrón es siempre el mismo, no
  // codifica ningún dato real — es maquetación, «verosímil» (Figma 4010:2), no un QR escaneable.
  const semilla = (fila * 13 + columna * 7 + fila * columna * 3) % 11;
  return semilla < 5;
}

/**
 * QR decorativo (DS §7 «código QR · evento», Figma 4010:2): patrón de 21×21 módulos con los tres
 * patrones de posición y el de temporización, verosímil pero no escaneable (maquetación). Un solo
 * componente para el icono pequeño junto a cada evento y para la vista previa del afiche, así el
 * patrón es igual en los dos lugares.
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
        if (moduloRelleno(fila, columna)) celdas.push({ fila, columna });
      }
    }
    return celdas;
  });
}
