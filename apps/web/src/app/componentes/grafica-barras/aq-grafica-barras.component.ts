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
    <div class="lienzo" role="img" [attr.aria-label]="titulo()">
      @for (columna of columnas(); track columna.semana) {
        <div class="columna" [class.actual]="!!columna.actual">
          <span class="valor">{{ columna.escaneos }}</span>
          <div class="barra" [style.height.px]="columna.alturaPx"></div>
          <span class="etiqueta">{{ columna.etiqueta }}</span>
        </div>
      }
    </div>
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
      display: flex;
      align-items: flex-end;
      gap: var(--space-12);
    }
    .columna {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: flex-end;
      flex: 1 0 0;
      min-width: 0;
      gap: var(--space-4);
    }
    .valor {
      font: var(--text-valor-grafica);
      color: var(--color-texto);
    }
    .etiqueta {
      font: var(--text-etiqueta-semana);
      color: var(--color-texto-secundario);
    }
    .barra {
      box-sizing: border-box;
      width: 100%;
      max-height: var(--size-grafica-barra-alto-max);
      border-radius: var(--radius-barra);
      background: var(--color-texto-secundario);
    }
    .columna.actual .barra {
      background: var(--color-tinta);
    }
    .columna.actual .etiqueta {
      color: var(--color-tinta);
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
