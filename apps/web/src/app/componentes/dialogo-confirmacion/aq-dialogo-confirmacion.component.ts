import { Component, booleanAttribute, input, output } from '@angular/core';
import { A11yModule } from '@angular/cdk/a11y';
import { AqBotonComponent } from '../boton/aq-boton.component';

let siguienteId = 0;

/** Diálogo de confirmación web (DS comp. 47 en web): velo Tinta 55 %, 420, acción segura primaria arriba y la que confirma en contorno abajo. */
@Component({
  selector: 'aq-dialogo-confirmacion',
  imports: [A11yModule, AqBotonComponent],
  template: `
    <div class="velo" data-velo (click)="seguro.emit()"></div>
    <section
      class="dialogo"
      role="alertdialog"
      aria-modal="true"
      [attr.aria-labelledby]="idTitulo"
      [attr.aria-describedby]="idCuerpo"
      cdkTrapFocus
      [cdkTrapFocusAutoCapture]="true"
      (keydown.escape)="seguro.emit()"
    >
      <h2 class="titulo" [id]="idTitulo">{{ titulo() }}</h2>
      <p class="cuerpo" [id]="idCuerpo">{{ cuerpo() }}</p>
      <div class="acciones">
        <button aq-boton bloque type="button" data-accion="seguro" (click)="seguro.emit()">
          {{ rotuloSeguro() }}
        </button>
        <button
          aq-boton
          bloque
          type="button"
          data-accion="confirmar"
          [variante]="destruye() ? 'destructivo' : 'secundario'"
          (click)="confirmar.emit()"
        >
          {{ rotuloAccion() }}
        </button>
      </div>
    </section>
  `,
  styles: `
    :host {
      position: fixed;
      inset: 0;
      z-index: 20;
      display: grid;
      place-items: center;
    }
    .velo {
      position: absolute;
      inset: 0;
      background: var(--color-velo-movil);
    }
    .dialogo {
      position: relative;
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
      box-sizing: border-box;
      width: var(--size-dialogo-web-ancho);
      max-width: calc(100% - 2 * var(--space-dialogo));
      padding: var(--space-dialogo);
      background: var(--color-blanco);
      border-radius: var(--radius-dialogo-web);
      box-shadow: var(--elevation-dialogo-web);
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-dialogo);
      color: var(--color-texto);
    }
    .cuerpo {
      margin: 0;
      font: var(--text-cuerpo-dialogo);
      color: var(--color-texto-secundario);
    }
    .acciones {
      display: flex;
      flex-direction: column;
      gap: var(--space-8);
      padding-top: var(--space-8);
    }
  `,
})
export class AqDialogoConfirmacionComponent {
  readonly titulo = input.required<string>();
  readonly cuerpo = input.required<string>();
  readonly rotuloSeguro = input.required<string>();
  readonly rotuloAccion = input.required<string>();
  readonly destruye = input(false, { transform: booleanAttribute });
  readonly seguro = output<void>();
  readonly confirmar = output<void>();
  protected readonly idTitulo = `aq-dialogo-titulo-${siguienteId++}`;
  protected readonly idCuerpo = `aq-dialogo-cuerpo-${siguienteId++}`;
}
