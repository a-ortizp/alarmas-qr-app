import { Component, input } from '@angular/core';

/** Estado vacío de tabla (DS §7): bloque Gris Niebla de ancho completo, radio 12, relleno 16/24, centrado. */
@Component({
  selector: 'aq-estado-vacio',
  template: `
    <h3 class="titulo">{{ titulo() }}</h3>
    <p class="texto">{{ texto() }}</p>
    <ng-content />
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: var(--space-12);
      box-sizing: border-box;
      width: 100%;
      padding: var(--space-web-estado-vacio);
      background: var(--color-gris-niebla);
      border-radius: var(--radius-campo);
      text-align: center;
    }
    .titulo,
    .texto {
      margin: 0;
    }
    .titulo {
      font: var(--text-destacado);
      color: var(--color-texto);
    }
    .texto {
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class AqEstadoVacioComponent {
  readonly titulo = input.required<string>();
  readonly texto = input.required<string>();
}
