import { Component, input, model } from '@angular/core';
import { FormCheckboxControl } from '@angular/forms/signals';

/** Switch web (W06): fila rótulo + pista 42×24. Encendido: pista Tinta, perilla blanca a la derecha; apagado: contorno Tinta, perilla Tinta a la izquierda (D9). */
@Component({
  selector: 'aq-switch',
  template: `
    <button
      type="button"
      role="switch"
      class="fila"
      [attr.aria-checked]="checked()"
      (click)="alternar()"
    >
      <span class="rotulo">{{ etiqueta() }}</span>
      <span class="pista" [class.encendido]="checked()" aria-hidden="true"
        ><span class="perilla"></span
      ></span>
    </button>
  `,
  styles: `
    .fila {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: var(--space-12);
      width: 100%;
      padding: 0;
      border: none;
      background: none;
      text-align: left;
      cursor: pointer;
    }
    .rotulo {
      font: var(--text-rotulo-web);
      color: var(--color-texto);
    }
    .pista {
      position: relative;
      flex: none;
      box-sizing: border-box;
      width: var(--size-switch-ancho);
      height: var(--size-switch-alto);
      border: var(--stroke-borde) solid var(--color-tinta);
      border-radius: var(--radius-pildora);
      background: var(--color-blanco);
      transition: background-color var(--motion-transicion);
    }
    .perilla {
      position: absolute;
      top: 50%;
      left: var(--space-2);
      width: var(--size-switch-perilla);
      height: var(--size-switch-perilla);
      border-radius: var(--radius-pildora);
      background: var(--color-tinta);
      transform: translateY(-50%);
      transition:
        left var(--motion-transicion),
        background-color var(--motion-transicion);
    }
    .encendido {
      background: var(--color-tinta);
    }
    .encendido .perilla {
      left: calc(100% - var(--size-switch-perilla) - var(--space-2));
      background: var(--color-blanco);
    }
    .fila:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-4);
      border-radius: var(--radius-barra);
    }
  `,
})
export class AqSwitchComponent implements FormCheckboxControl {
  readonly checked = model(false);
  readonly etiqueta = input.required<string>();

  protected alternar(): void {
    this.checked.update((valor) => !valor);
  }
}
