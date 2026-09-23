import { Component, input, output } from '@angular/core';

export interface OpcionPildora {
  valor: string;
  texto: string;
}

/** Grupo de píldoras de filtro o pestaña (DS §7, Figma 4073:33/4073:40): una sola pista Gris Niebla, segmento activo Tinta con texto blanco. No es un control de formulario: la página decide qué hacer al elegir (normalmente navegar con un query param). */
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
      box-sizing: border-box;
      gap: var(--space-2);
      padding: var(--space-segmentado);
      border-radius: var(--radius-pildora);
      background: var(--color-gris-niebla);
    }
    .pildora {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      flex: 1 0 0;
      height: var(--size-pildora-filtro);
      padding: 0 var(--space-16);
      border: none;
      border-radius: var(--radius-pildora);
      background: transparent;
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
