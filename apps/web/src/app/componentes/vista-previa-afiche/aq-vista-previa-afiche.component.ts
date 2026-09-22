import { Component, input } from '@angular/core';
import { AqQrDecorativoComponent } from '../qr-decorativo/aq-qr-decorativo.component';

/** Vista previa del afiche (DS §7 «Tarjeta de afiche QR»): miniatura 132 (marca Tinta + QR 72 + nombre + lema) y bloque de texto con la regla de tamaño mínimo. */
@Component({
  selector: 'aq-vista-previa-afiche',
  imports: [AqQrDecorativoComponent],
  template: `
    <div class="miniatura">
      <div class="marca">
        <span class="punto" aria-hidden="true"></span>
        <span>Alarmas QR</span>
      </div>
      <aq-qr-decorativo class="qr" [etiqueta]="nombreEvento()" />
      <span class="nombre-evento">{{ nombreEvento() }}</span>
      <span class="lema">Escanéalo y te avisamos</span>
    </div>
    <div class="texto">
      <span class="rotulo">VISTA PREVIA DEL AFICHE</span>
      <p class="explicacion">
        Pieza lista para imprimir con el código QR del evento y la marca de Alarmas QR.
      </p>
      <p class="regla">QR mínimo 4 × 4 cm · PNG a 300 ppp o PDF vectorial</p>
    </div>
  `,
  styles: `
    :host {
      display: flex;
      gap: var(--space-12);
      box-sizing: border-box;
      padding: var(--space-12);
      background: var(--color-gris-niebla);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-campo);
    }
    .miniatura {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: var(--space-8);
      flex: none;
      box-sizing: border-box;
      width: var(--size-afiche-miniatura);
      padding: var(--space-12) var(--space-8);
      background: var(--color-blanco);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-chip-web);
    }
    .marca {
      display: flex;
      align-items: center;
      gap: var(--space-4);
      box-sizing: border-box;
      width: 100%;
      padding: var(--space-4) var(--space-8);
      background: var(--color-tinta);
      border-radius: var(--radius-barra);
      font: var(--text-marca-afiche);
      color: var(--color-blanco);
    }
    .punto {
      width: var(--space-8);
      height: var(--space-8);
      border-radius: var(--radius-pildora);
      background: var(--color-amarillo-energia);
    }
    .qr {
      width: var(--size-afiche-qr);
      height: var(--size-afiche-qr);
      color: var(--color-tinta);
    }
    .nombre-evento {
      font: var(--text-titulo-afiche);
      color: var(--color-texto);
      text-align: center;
    }
    .lema {
      font: var(--text-lema-afiche);
      color: var(--color-texto-secundario);
      text-align: center;
    }
    .texto {
      display: flex;
      flex-direction: column;
      gap: var(--space-8);
      min-width: 0;
    }
    .rotulo {
      font: var(--text-rotulo-afiche);
      letter-spacing: 0.06em;
      color: var(--color-texto-secundario);
    }
    .explicacion {
      margin: 0;
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    .regla {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class AqVistaPreviaAficheComponent {
  readonly nombreEvento = input.required<string>();
}
