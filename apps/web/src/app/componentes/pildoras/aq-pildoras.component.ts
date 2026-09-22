import { Component, input, output } from '@angular/core';

export interface OpcionPildora {
  valor: string;
  texto: string;
}

/** Grupo de píldoras de filtro o pestaña (DS §7): 34 de alto, activa Tinta con texto blanco, inactiva Gris Niebla. No es un control de formulario: la página decide qué hacer al elegir (normalmente navegar con un query param). */
@Component({
  selector: 'aq-pildoras',
  template: `
    <div class="grupo" role="group">
      @for (opcion of opciones(); track opcion.valor) {
        <button
          type="button"
          class="pildora"
          [class.activa]="opcion.valor === activo()"
          [attr.aria-pressed]="opcion.valor === activo()"
          (click)="elegir.emit(opcion.valor)"
        >
          {{ opcion.texto }}
        </button>
      }
    </div>
  `,
  styles: `
    .grupo {
      display: flex;
      gap: var(--space-10);
      flex-wrap: wrap;
    }
    .pildora {
      display: inline-flex;
      align-items: center;
      box-sizing: border-box;
      height: var(--size-pildora-filtro);
      padding: 0 var(--space-16);
      border: none;
      border-radius: var(--radius-pildora);
      background: var(--color-gris-niebla);
      color: var(--color-texto-secundario);
      font: var(--text-pildora-web);
      cursor: pointer;
      transition:
        background-color var(--motion-transicion),
        color var(--motion-transicion);
    }
    .pildora.activa {
      background: var(--color-tinta);
      color: var(--color-blanco);
    }
    .pildora:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
  `,
})
export class AqPildorasComponent {
  readonly opciones = input.required<readonly OpcionPildora[]>();
  readonly activo = input<string>('');
  readonly elegir = output<string>();
}
