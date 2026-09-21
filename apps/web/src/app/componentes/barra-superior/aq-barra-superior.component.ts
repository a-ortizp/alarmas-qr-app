import { Component, booleanAttribute, inject, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AqLogotipoComponent } from '../logotipo/aq-logotipo.component';
import { AqIconoComponent } from '../icono/aq-icono.component';
import { CortesService } from '../../navegacion/cortes.service';

/**
 * Barra superior web (DS comp. 14w): marca a la izquierda; nombre + avatar a la derecha (avatar → W06).
 * Tokens v1.13: con `menu` (bajo 900) lleva ☰ «Abrir menú» antes de la marca; bajo 600 el nombre queda solo para lectores.
 */
@Component({
  selector: 'aq-barra-superior',
  imports: [RouterLink, AqLogotipoComponent, AqIconoComponent],
  template: `
    <header class="barra">
      <div class="marca">
        @if (menu()) {
          <button
            type="button"
            class="abrir-menu"
            data-abrir-menu
            aria-label="Abrir menú"
            aria-controls="menu-cajon"
            [attr.aria-expanded]="menuAbierto()"
            (click)="abrirMenu.emit()"
          >
            <aq-icono nombre="menu" />
          </button>
        }
        <aq-logotipo tamano="barra" />
        <span class="nombre-app">Alarmas QR</span>
      </div>
      <a class="usuario" routerLink="/perfil" data-avatar>
        <span [class.solo-lector]="telefono()">{{ nombre() }}</span>
        <span class="avatar" aria-hidden="true"></span>
      </a>
    </header>
  `,
  styles: `
    .barra {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: var(--space-10);
      box-sizing: border-box;
      height: var(--size-barra-superior-web);
      padding: 0 var(--space-barra-superior-web);
      background: var(--color-blanco);
      border-bottom: var(--stroke-borde-fino) solid var(--color-borde);
    }
    .marca,
    .usuario {
      display: flex;
      align-items: center;
      gap: var(--space-10);
      min-width: 0;
    }
    .abrir-menu {
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
    .abrir-menu:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-2);
    }
    .nombre-app {
      font: var(--text-marca-web);
      color: var(--color-texto);
      white-space: nowrap;
    }
    .usuario {
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
      text-decoration: none;
    }
    .usuario:focus-visible {
      outline: var(--stroke-foco) solid var(--color-enlace);
      outline-offset: var(--space-4);
      border-radius: var(--radius-pildora);
    }
    .avatar {
      flex: none;
      box-sizing: border-box;
      width: var(--size-avatar-web);
      height: var(--size-avatar-web);
      border: var(--stroke-borde) solid var(--color-tinta);
      border-radius: var(--radius-pildora);
      background: var(--color-gris-niebla);
    }
  `,
})
export class AqBarraSuperiorComponent {
  readonly nombre = input('');
  readonly menu = input(false, { transform: booleanAttribute });
  readonly menuAbierto = input(false);
  readonly abrirMenu = output<void>();
  protected readonly telefono = inject(CortesService).telefono;
}
