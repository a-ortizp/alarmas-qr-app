import { Component, input, model, output } from '@angular/core';
import { FormValueControl } from '@angular/forms/signals';

let siguienteId = 0;

/** Campo web con etiqueta interna: 48, radio 12, borde Gris Borde (Coral Texto en error). Se usa con [formField] o [(value)]. */
@Component({
  selector: 'aq-campo',
  template: `
    <label class="caja" [class.error]="error()">
      @if (etiqueta()) {
        <span class="etiqueta">{{ etiqueta() }}</span>
      }
      <input
        [attr.id]="idEntrada() || null"
        [type]="tipo()"
        [value]="value()"
        [attr.placeholder]="placeholder() || null"
        [attr.aria-invalid]="error() ? 'true' : null"
        [attr.aria-describedby]="mensajeError() ? idMensaje : null"
        (input)="alEscribir($event)"
        (focus)="enfocado.emit()"
        (blur)="touch.emit()"
      />
    </label>
    @if (mensajeError()) {
      <p class="mensaje-error" [id]="idMensaje">{{ mensajeError() }}</p>
    }
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
    }
    .caja {
      display: flex;
      flex-direction: column;
      justify-content: center;
      gap: var(--space-2);
      box-sizing: border-box;
      height: var(--size-campo);
      padding: 0 var(--space-campo-x);
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-campo);
      cursor: text;
      transition: border-color var(--motion-transicion);
    }
    .caja:focus-within {
      border-color: var(--color-tinta);
    }
    .caja.error {
      border-color: var(--color-destructivo);
    }
    .etiqueta {
      font: var(--text-etiqueta-campo);
      color: var(--color-texto-secundario);
    }
    input {
      width: 100%;
      padding: 0;
      border: none;
      outline: none;
      background: transparent;
      font: var(--text-valor-campo);
      color: var(--color-texto);
    }
    input::placeholder {
      color: var(--color-gris-medio);
    }
    .mensaje-error {
      margin: 0;
      font: var(--text-nota);
      color: var(--color-destructivo);
    }
  `,
})
export class AqCampoComponent implements FormValueControl<string> {
  readonly value = model('');
  readonly touch = output<void>();
  readonly enfocado = output<void>();
  readonly etiqueta = input('');
  readonly tipo = input<'text' | 'email' | 'password'>('text');
  readonly placeholder = input('');
  readonly idEntrada = input('');
  readonly error = input(false);
  readonly mensajeError = input('');
  protected readonly idMensaje = `aq-campo-error-${siguienteId++}`;

  protected alEscribir(evento: Event): void {
    this.value.set((evento.target as HTMLInputElement).value);
  }
}
