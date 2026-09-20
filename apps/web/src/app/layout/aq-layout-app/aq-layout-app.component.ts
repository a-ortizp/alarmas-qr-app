import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { PANTALLAS } from '../../navegacion/pantallas';

/** Layout de las páginas autenticadas: barra lateral (marcadora en la Fase 0; el componente aq-barra-lateral real llega en el Plan 3) + contenido. */
@Component({
  selector: 'aq-layout-app',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="layout">
      <nav class="aq-barra-lateral" aria-label="Menú principal">
        <span class="marca">Alarmas QR</span>
        @for (p of paginas; track p.codigo) {
          <a [routerLink]="'/' + p.ruta" routerLinkActive="activa" [attr.data-codigo]="p.codigo">{{
            p.titulo
          }}</a>
        }
      </nav>
      <main class="contenido"><router-outlet /></main>
    </div>
  `,
  styles: `
    .layout {
      display: grid;
      grid-template-columns: var(--size-barra-lateral-web) 1fr;
      min-height: 100vh;
    }
    .aq-barra-lateral {
      display: flex;
      flex-direction: column;
      gap: var(--space-web-bloques);
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
      background: var(--color-gris-niebla);
    }
    .marca {
      font-family: var(--font-titulares);
      font-weight: 700;
    }
    a {
      color: var(--color-texto);
      text-decoration: none;
      padding: var(--space-boton-web);
      display: flex;
      align-items: center;
      border-radius: var(--radius-pildora);
    }
    a.activa {
      background: var(--color-tinta);
      color: var(--color-blanco);
    }
  `,
})
export class AqLayoutAppComponent {
  readonly paginas = PANTALLAS.filter((p) => p.conBarraLateral && !p.ruta.includes(':'));
}
