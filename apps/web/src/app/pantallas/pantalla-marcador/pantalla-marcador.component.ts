import { Component, input } from '@angular/core';
import { PantallaWeb } from '../../navegacion/pantallas';

/** Marcador de la Fase 0: ocupa el lugar de una página hasta que su plan la construya. */
@Component({
  selector: 'aq-pantalla-marcador',
  template: `
    <section class="marcador" [attr.data-codigo]="pantalla().codigo">
      <h1>{{ pantalla().codigo }}</h1>
      <p>{{ pantalla().titulo }}</p>
      <small>Pantalla pendiente · {{ pantalla().funcionalidad }}</small>
    </section>
  `,
  styles: `
    .marcador {
      display: grid;
      gap: var(--space-web-bloques);
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
    }
    h1 {
      font: var(--text-h1-web);
      font-family: var(--font-titulares);
      margin: 0;
    }
    p {
      margin: 0;
      color: var(--color-texto-secundario);
    }
  `,
})
export class PantallaMarcadorComponent {
  pantalla = input.required<PantallaWeb>();
}
