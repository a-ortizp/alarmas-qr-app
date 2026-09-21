import { Component } from '@angular/core';
import { AqLogotipoComponent } from '../logotipo/aq-logotipo.component';

/** Tarjeta de acceso (DS comp. 45, W00): 520 de ancho, relleno 34, separación 20; marca centrada arriba. */
@Component({
  selector: 'aq-tarjeta-acceso',
  imports: [AqLogotipoComponent],
  template: `
    <div class="marca">
      <aq-logotipo tamano="acceso" />
      <span class="nombre">Alarmas QR</span>
    </div>
    <ng-content />
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: column;
      gap: var(--space-tarjeta-acceso-gap);
      box-sizing: border-box;
      width: var(--size-tarjeta-acceso);
      max-width: 100%;
      /* Borde hacia dentro, como en Figma: el relleno 34 cuenta desde el borde exterior. */
      padding: calc(var(--space-tarjeta-acceso) - var(--stroke-borde));
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
    }
    .marca {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: var(--space-10);
    }
    .nombre {
      font: var(--text-marca-acceso);
      color: var(--color-texto);
    }
  `,
})
export class AqTarjetaAccesoComponent {}
