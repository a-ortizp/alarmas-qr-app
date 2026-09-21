import { Component, input, output } from '@angular/core';
import { A11yModule } from '@angular/cdk/a11y';
import { AqIconoComponent } from '../icono/aq-icono.component';

let siguienteId = 0;

/** Modal web destructivo (DS comp. 27, única variante desde la web v1.5): velo Tinta 45 %, 481, borde Coral Texto, icono de advertencia y título. */
@Component({
  selector: 'aq-modal',
  imports: [A11yModule, AqIconoComponent],
  template: `
    <div class="velo" data-velo (click)="cerrar.emit()"></div>
    <section
      class="modal"
      role="dialog"
      aria-modal="true"
      [attr.aria-labelledby]="idTitulo"
      cdkTrapFocus
      [cdkTrapFocusAutoCapture]="true"
      (keydown.escape)="cerrar.emit()"
    >
      <header class="cabecera">
        <aq-icono class="advertencia" nombre="advertencia" tamano="advertencia" />
        <h2 class="titulo" [id]="idTitulo">{{ titulo() }}</h2>
      </header>
      <ng-content />
    </section>
  `,
  styles: `
    :host {
      position: fixed;
      inset: 0;
      z-index: 20;
      display: grid;
      place-items: center;
      /* Columna que puede encogerse: el max-width de la caja manda bajo su ancho de puntero (tokens v1.13). */
      grid-template-columns: minmax(0, 1fr);
    }
    .velo {
      position: absolute;
      inset: 0;
      background: var(--color-velo);
    }
    .modal {
      position: relative;
      display: flex;
      flex-direction: column;
      gap: var(--space-16);
      box-sizing: border-box;
      width: var(--size-modal-web);
      max-width: calc(100% - 2 * var(--space-web-modal));
      max-height: calc(100% - 2 * var(--space-web-modal));
      overflow-y: auto;
      /* Borde hacia dentro, como en Figma: el relleno 22 cuenta desde el borde exterior. */
      padding: calc(var(--space-web-modal) - var(--stroke-borde));
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-destructivo);
      border-radius: var(--radius-modal);
    }
    .cabecera {
      display: flex;
      align-items: center;
      gap: var(--space-10);
    }
    .advertencia {
      color: var(--color-destructivo);
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-modal-web);
      color: var(--color-texto);
    }
  `,
})
export class AqModalComponent {
  readonly titulo = input.required<string>();
  readonly cerrar = output<void>();
  protected readonly idTitulo = `aq-modal-titulo-${siguienteId++}`;
}
