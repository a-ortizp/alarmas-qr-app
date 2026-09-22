import { Component, input, model, output } from '@angular/core';
import { AqIconoComponent } from '../icono/aq-icono.component';

/** Campo de búsqueda (DS §7): 240×40 (W01) o 260×40 (W03), lupa a la izquierda, un solo marcador. Navega/filtra solo al presionar Enter (Plan 5 D6). */
@Component({
  selector: 'aq-campo-busqueda',
  imports: [AqIconoComponent],
  template: `
    <label class="caja">
      <aq-icono nombre="lupa" tamano="vineta" />
      <input
        type="search"
        [value]="value()"
        [attr.placeholder]="placeholder()"
        (input)="value.set($any($event.target).value)"
        (keydown.enter)="buscar.emit(value())"
      />
    </label>
  `,
  host: { '[class.w03]': "ancho() === 'w03'" },
  styles: `
    .caja {
      display: inline-flex;
      align-items: center;
      gap: var(--space-8);
      box-sizing: border-box;
      width: var(--size-campo-busqueda-w01);
      height: var(--size-campo-busqueda-alto);
      padding: 0 var(--space-12);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-pildora);
      background: var(--color-blanco);
      color: var(--color-texto-secundario);
      cursor: text;
      transition: border-color var(--motion-transicion);
    }
    :host(.w03) .caja {
      width: var(--size-campo-busqueda-w03);
    }
    .caja:focus-within {
      border-color: var(--color-tinta);
      color: var(--color-tinta);
    }
    input {
      width: 100%;
      border: none;
      outline: none;
      background: transparent;
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    input::placeholder {
      color: var(--color-gris-medio);
    }
    input[type='search']::-webkit-search-cancel-button {
      display: none;
    }
  `,
})
export class AqCampoBusquedaComponent {
  readonly value = model('');
  readonly ancho = input<'w01' | 'w03'>('w01');
  readonly placeholder = input('Buscar por nombre');
  readonly buscar = output<string>();
}
