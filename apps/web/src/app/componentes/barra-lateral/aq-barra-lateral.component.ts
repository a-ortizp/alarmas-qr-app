import { Component, booleanAttribute, computed, input, model, output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AqIconoComponent } from '../icono/aq-icono.component';
import { ITEM_CERRAR_SESION, ITEMS_BARRA_LATERAL } from '../../navegacion/barra-lateral';

/**
 * Barra lateral web (DS comp. 14w, web v1.4): ítems con icono, píldora activa Tinta, colapsable a 64.
 * Con `cajon` (tokens v1.13, bajo 900) va siempre expandida y el control pasa a «Cerrar menú».
 */
@Component({
  selector: 'aq-barra-lateral',
  imports: [RouterLink, RouterLinkActive, AqIconoComponent],
  template: `
    <nav class="barra" [class.colapsada]="plegada()" aria-label="Menú principal">
      @if (cajon()) {
        <button
          type="button"
          class="control"
          data-control-menu
          aria-label="Cerrar menú"
          (click)="cerrarCajon.emit()"
        >
          <aq-icono nombre="colapsar" />
        </button>
      } @else {
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
      }
      @for (item of items; track item.id) {
        <a
          class="item"
          [routerLink]="item.ruta"
          routerLinkActive="activo"
          #activo="routerLinkActive"
          [attr.aria-current]="activo.isActive ? 'page' : null"
          [attr.aria-label]="plegada() ? item.texto : null"
          [attr.title]="plegada() ? item.texto : null"
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
        [attr.aria-label]="plegada() ? salida.texto : null"
        [attr.title]="plegada() ? salida.texto : null"
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
  readonly colapsada = model(false);
  readonly cajon = input(false, { transform: booleanAttribute });
  readonly cerrarSesion = output<void>();
  readonly cerrarCajon = output<void>();
  /** El cajón nunca se pliega a 64: ocupa 208 sobre el velo. */
  protected readonly plegada = computed(() => this.colapsada() && !this.cajon());
  protected readonly items = ITEMS_BARRA_LATERAL;
  protected readonly salida = ITEM_CERRAR_SESION;
}
