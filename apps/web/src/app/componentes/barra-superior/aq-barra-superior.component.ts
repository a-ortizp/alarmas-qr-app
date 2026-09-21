import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AqLogotipoComponent } from '../logotipo/aq-logotipo.component';

/** Barra superior web (DS comp. 14w): marca a la izquierda; nombre + avatar a la derecha (avatar → W06). */
@Component({
  selector: 'aq-barra-superior',
  imports: [RouterLink, AqLogotipoComponent],
  template: `
    <header class="barra">
      <div class="marca">
        <aq-logotipo tamano="barra" />
        <span class="nombre-app">Alarmas QR</span>
      </div>
      <a class="usuario" routerLink="/perfil" data-avatar>
        <span>{{ nombre() }}</span>
        <span class="avatar" aria-hidden="true"></span>
      </a>
    </header>
  `,
  styles: `
    .barra {
      display: flex;
      align-items: center;
      justify-content: space-between;
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
    }
    .nombre-app {
      font: var(--text-marca-web);
      color: var(--color-texto);
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
}
