import { Component, computed, input } from '@angular/core';
import { SemanaEscaneo } from '../../datos/modelos';

interface ColumnaGrafica extends SemanaEscaneo {
  alturaPx: number;
}

/** Gráfica de barras «Escaneos por semana» (DS §7): 8 columnas en FILL, barra radio 4, altura máx. 96; semana actual en Tinta, el resto en Gris Texto. */
@Component({
  selector: 'aq-grafica-barras',
  template: `
    <header class="cabecera">
      <h3 class="titulo">{{ titulo() }}</h3>
      <span class="periodo"
        >{{ totalEscaneos() }} escaneos · últimas {{ datos().length }} semanas ·
        {{ calificador() }}</span
      >
    </header>
    <svg
      class="lienzo"
      [attr.viewBox]="'0 0 ' + columnas().length * 40 + ' 130'"
      preserveAspectRatio="none"
      role="img"
      aria-label="{{ titulo() }}"
    >
      @for (columna of columnas(); track columna.semana; let i = $index) {
        <g
          class="columna"
          [class.actual]="!!columna.actual"
          [attr.transform]="'translate(' + i * 40 + ',0)'"
        >
          <text class="valor" x="20" [attr.y]="94 - columna.alturaPx" text-anchor="middle">
            {{ columna.escaneos }}
          </text>
          <rect
            class="barra"
            x="6"
            [attr.y]="96 - columna.alturaPx"
            width="28"
            [attr.height]="columna.alturaPx"
            rx="4"
          />
          <text class="etiqueta" x="20" y="112" text-anchor="middle">{{ columna.etiqueta }}</text>
        </g>
      }
    </svg>
    <p class="pie">{{ nota() }}</p>
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-16);
      box-sizing: border-box;
      padding: var(--space-16);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
      background: var(--color-blanco);
    }
    .cabecera {
      display: flex;
      align-items: baseline;
      justify-content: space-between;
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-grafica-web);
      color: var(--color-texto);
    }
    .periodo {
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .lienzo {
      display: block;
      width: 100%;
      height: var(--size-grafica-barra-alto-lienzo);
    }
    .valor {
      font: var(--text-valor-grafica);
      fill: var(--color-texto);
    }
    .etiqueta {
      font: var(--text-etiqueta-semana);
      fill: var(--color-texto-secundario);
    }
    .columna .barra {
      fill: var(--color-texto-secundario);
    }
    .columna.actual .barra {
      fill: var(--color-tinta);
    }
    .columna.actual .etiqueta {
      fill: var(--color-tinta);
    }
    .pie {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class AqGraficaBarrasComponent {
  readonly datos = input.required<readonly SemanaEscaneo[]>();
  readonly titulo = input('Escaneos por semana');
  readonly nota = input('Datos agregados y anónimos');
  readonly calificador = input('eventos propios');

  protected readonly totalEscaneos = computed(() =>
    this.datos().reduce((suma, semana) => suma + semana.escaneos, 0),
  );

  protected readonly columnas = computed<ColumnaGrafica[]>(() => {
    const valores = this.datos();
    const maximo = Math.max(1, ...valores.map((v) => v.escaneos));
    return valores.map((v) => ({ ...v, alturaPx: (v.escaneos / maximo) * 96 }));
  });
}
