import { Component, inject, model, output } from '@angular/core';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';
import { AqIconoComponent } from '../icono/aq-icono.component';
import {
  ITEM_CERRAR_SESION,
  ITEMS_BARRA_LATERAL,
  ItemBarraLateral,
} from '../../navegacion/barra-lateral';

/** Barra lateral web (DS comp. 14w, web v1.4): ítems con icono, píldora activa Tinta, colapsable a 64. */
@Component({
  selector: 'aq-barra-lateral',
  imports: [RouterLink, AqIconoComponent],
  template: `
    <nav class="barra" [class.colapsada]="colapsada()" aria-label="Menú principal">
      <button
        type="button"
        class="control"
        data-control-menu
        [attr.aria-label]="colapsada() ? 'Expandir menú' : 'Colapsar menú'"
        [attr.aria-expanded]="!colapsada()"
        (click)="colapsada.set(!colapsada())"
      >
        <aq-icono nombre="colapsar" [class.girado]="colapsada()" />
      </button>
      @for (item of items; track item.id) {
        <a
          class="item"
          [class.activo]="esActivo(item)"
          [routerLink]="item.ruta"
          [attr.aria-current]="esActivo(item) ? 'page' : null"
          [attr.aria-label]="colapsada() ? item.texto : null"
          [attr.title]="colapsada() ? item.texto : null"
          [attr.data-item]="item.id"
        >
          <aq-icono [nombre]="item.icono" />
          <span class="texto">{{ item.texto }}</span>
        </a>
      }
      <span class="espaciador"></span>
      <button
        type="button"
        class="item"
        [attr.data-item]="salida.id"
        [attr.aria-label]="colapsada() ? salida.texto : null"
        [attr.title]="colapsada() ? salida.texto : null"
        (click)="cerrarSesion.emit()"
      >
        <aq-icono [nombre]="salida.icono" />
        <span class="texto">{{ salida.texto }}</span>
      </button>
    </nav>
  `,
  styles: `
    :host {
      display: block;
    }
    .barra {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      gap: var(--space-4);
      box-sizing: border-box;
      height: 100%;
      padding: var(--space-barra-lateral-web);
      background: var(--color-blanco);
      border-right: var(--stroke-borde-fino) solid var(--color-borde);
      overflow: hidden;
    }
    .control {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      flex: none;
      width: var(--size-control-colapsar-menu);
      height: var(--size-control-colapsar-menu);
      padding: 0;
      border: none;
      border-radius: var(--radius-pildora);
      background: transparent;
      color: var(--color-tinta);
      cursor: pointer;
    }
    .girado {
      transform: rotate(180deg);
    }
    .item {
      display: flex;
      align-items: center;
      gap: var(--space-10);
      flex: none;
      box-sizing: border-box;
      width: 100%;
      height: var(--size-item-barra-lateral);
      padding: var(--space-item-barra-lateral);
      border: none;
      border-radius: var(--radius-pildora);
      background: transparent;
      color: var(--color-texto);
      font: var(--text-nav-lateral);
      text-align: left;
      text-decoration: none;
      white-space: nowrap;
      cursor: pointer;
      transition: background-color var(--motion-transicion);
    }
    .item.activo {
      background: var(--color-tinta);
      color: var(--color-blanco);
    }
    .espaciador {
      flex: 1;
    }
    .colapsada .item {
      justify-content: center;
      width: var(--size-pildora-colapsada);
      height: var(--size-pildora-colapsada);
      padding: 0;
    }
    .colapsada .texto {
      display: none;
    }
    .control:focus-visible,
    .item:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
  `,
})
export class AqBarraLateralComponent {
  private readonly router = inject(Router);

  readonly colapsada = model(false);
  readonly cerrarSesion = output<void>();
  protected readonly items = ITEMS_BARRA_LATERAL;
  protected readonly salida = ITEM_CERRAR_SESION;

  // routerLinkActive por sí solo no alcanza: W03 vive en /eventos/:id, fuera del árbol de /alarmas,
  // pero sigue siendo parte de la sección «Mis Alarmas» (rutasActivas lo declara explícitamente).
  private readonly urlActual = toSignal(
    this.router.events.pipe(
      filter((evento): evento is NavigationEnd => evento instanceof NavigationEnd),
      map(() => this.router.url),
    ),
    { initialValue: this.router.url },
  );

  protected esActivo(item: ItemBarraLateral): boolean {
    const ruta = this.urlActual().split('?')[0];
    return (item.rutasActivas ?? [item.ruta]).some(
      (prefijo) => ruta === prefijo || ruta.startsWith(`${prefijo}/`),
    );
  }
}
