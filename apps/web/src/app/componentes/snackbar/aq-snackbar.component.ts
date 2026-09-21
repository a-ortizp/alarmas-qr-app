import { Component, inject } from '@angular/core';
import { SnackbarService } from './snackbar.service';

/** Snackbar web (DS comp. 42): píldora Tinta centrada abajo, visto Verde Texto y texto blanco SemiBold 14. */
@Component({
  selector: 'aq-snackbar',
  template: `
    <div class="region" role="status" aria-live="polite">
      @if (servicio.mensaje(); as mensaje) {
        <div class="snackbar">
          <svg class="icono" viewBox="0 0 16 16" aria-hidden="true" focusable="false">
            <path
              class="circulo"
              d="M8 14C11.3137 14 14 11.3137 14 8C14 4.68629 11.3137 2 8 2C4.68629 2 2 4.68629 2 8C2 11.3137 4.68629 14 8 14Z"
            />
            <path
              class="visto"
              d="M5.33333 8L7.33333 10L10.6667 6"
              stroke-width="1.46667"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
          </svg>
          <span>{{ mensaje }}</span>
        </div>
      }
    </div>
  `,
  styles: `
    .region {
      position: fixed;
      left: 50%;
      bottom: var(--space-snackbar-web-borde);
      transform: translateX(-50%);
      z-index: 30;
    }
    .snackbar {
      display: flex;
      align-items: center;
      gap: var(--space-8);
      box-sizing: border-box;
      height: var(--size-snackbar-web);
      padding: var(--space-snackbar-web);
      border-radius: var(--radius-pildora);
      background: var(--color-tinta);
      color: var(--color-blanco);
      font: var(--text-snackbar-web);
      white-space: nowrap;
    }
    .icono {
      flex: none;
      width: var(--size-icono-snackbar);
      height: var(--size-icono-snackbar);
    }
    .circulo {
      fill: var(--color-blanco);
    }
    .visto {
      fill: none;
      stroke: var(--color-verde-texto);
    }
  `,
})
export class AqSnackbarComponent {
  protected readonly servicio = inject(SnackbarService);
}
