import { Component, computed, input, output } from '@angular/core';

/** Paginador (DS §7): pie «Mostrando…» + píldoras de 30×30 (‹ 1 2 3 4 ›); activa en Tinta, inactivas con contorno Gris Borde. */
@Component({
  selector: 'aq-paginador',
  template: `
    <p class="texto">{{ texto() }}</p>
    <nav class="paginas" aria-label="Paginación">
      <button
        type="button"
        class="pildora"
        data-anterior
        [disabled]="paginaActual() <= 1"
        (click)="cambiar.emit(paginaActual() - 1)"
      >
        ‹
      </button>
      @for (pagina of paginas(); track pagina) {
        <button
          type="button"
          class="pildora"
          [class.activa]="pagina === paginaActual()"
          [attr.aria-current]="pagina === paginaActual() ? 'true' : null"
          (click)="cambiar.emit(pagina)"
        >
          {{ pagina }}
        </button>
      }
      <button
        type="button"
        class="pildora"
        data-siguiente
        [disabled]="paginaActual() >= totalPaginas()"
        (click)="cambiar.emit(paginaActual() + 1)"
      >
        ›
      </button>
    </nav>
  `,
  styles: `
    :host {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
      padding-top: var(--space-12);
    }
    .texto {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .paginas {
      display: flex;
      gap: var(--space-4);
    }
    .pildora {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      width: var(--size-paginador-pildora);
      height: var(--size-paginador-pildora);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-pildora);
      background: var(--color-blanco);
      color: var(--color-texto);
      font: var(--text-pildora-web);
      cursor: pointer;
    }
    .pildora.activa {
      background: var(--color-tinta);
      border-color: var(--color-tinta);
      color: var(--color-blanco);
    }
    .pildora:disabled {
      opacity: var(--opacity-deshabilitado);
      cursor: not-allowed;
    }
    .pildora:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
  `,
})
export class AqPaginadorComponent {
  readonly texto = input.required<string>();
  readonly paginaActual = input.required<number>();
  readonly totalPaginas = input.required<number>();
  readonly cambiar = output<number>();
  protected readonly paginas = computed(() =>
    Array.from({ length: this.totalPaginas() }, (_, i) => i + 1),
  );
}
