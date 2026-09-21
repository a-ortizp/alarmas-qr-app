import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqModalComponent } from '../../componentes/modal/aq-modal.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqIconoComponent } from '../../componentes/icono/aq-icono.component';
import { DatosService } from '../../datos/datos.service';
import { SesionService } from '../../datos/sesion.service';

/** W06 · Modal eliminar cuenta (F-W08, /perfil/eliminar): fricción de escribir ELIMINAR; la acción segura es la prominente. */
@Component({
  selector: 'aq-w06-modal-eliminar-cuenta',
  imports: [FormField, AqModalComponent, AqCampoComponent, AqBotonComponent, AqIconoComponent],
  template: `
    @if (cuenta(); as d) {
      <aq-modal
        titulo="¿Eliminar tu cuenta definitivamente?"
        data-codigo="W06"
        data-estado="eliminar"
        (cerrar)="conservar()"
      >
        <p class="texto">Esta acción <strong>no se puede deshacer</strong>. Al confirmar:</p>
        <ul class="consecuencias">
          <li>
            <aq-icono nombre="qr" tamano="vineta" />
            <span class="texto"
              >Tus <strong>{{ d.eventosPublicados }} eventos publicados</strong> se despublican y
              sus QR dejan de funcionar.</span
            >
          </li>
          <li>
            <aq-icono nombre="calendario" tamano="vineta" />
            <span class="texto"
              >Las <strong>{{ d.alarmasDeAsistentes }} alarmas de asistentes</strong> dejan de
              recibir actualizaciones (no se borran de sus celulares).</span
            >
          </li>
          <li>
            <aq-icono nombre="persona" tamano="vineta" />
            <span class="texto"
              >Tus datos personales se eliminan en máximo
              <strong>{{ d.diasParaBorrado }} días</strong> (Ley 1581 · habeas data).</span
            >
          </li>
          <li>
            <aq-icono nombre="descarga" tamano="vineta" />
            <span class="texto">Consejo: {{ consejo() }}</span>
          </li>
        </ul>
        <div class="confirmacion">
          <label class="etiqueta" for="confirmar-eliminacion"
            >Escribe {{ d.palabraDeConfirmacion }} para confirmar</label
          >
          <aq-campo
            idEntrada="confirmar-eliminacion"
            [placeholder]="d.palabraDeConfirmacion"
            [formField]="formulario.confirmacion"
          />
        </div>
        <div class="acciones">
          <button aq-boton type="button" (click)="conservar()">Conservar mi cuenta</button>
          <button
            aq-boton
            variante="destructivo"
            type="button"
            [disabled]="!confirmado()"
            (click)="eliminar()"
          >
            Eliminar definitivamente
          </button>
        </div>
      </aq-modal>
    }
  `,
  styles: `
    .texto {
      margin: 0;
      font: var(--text-rotulo-web);
      color: var(--color-texto);
    }
    .consecuencias {
      display: flex;
      flex-direction: column;
      gap: var(--space-8);
      margin: 0;
      padding: 0;
      list-style: none;
    }
    li {
      display: flex;
      align-items: flex-start;
      gap: var(--space-8);
    }
    .confirmacion {
      display: flex;
      flex-direction: column;
      gap: var(--space-4);
    }
    .etiqueta {
      font: var(--text-etiqueta-externa);
      color: var(--color-texto);
    }
    .acciones {
      display: flex;
      gap: var(--space-10);
    }
    .acciones button {
      flex: 1;
    }
  `,
})
export class W06ModalEliminarCuentaComponent {
  private readonly router = inject(Router);
  private readonly sesion = inject(SesionService);
  protected readonly cuenta = inject(DatosService).eliminarCuenta;
  protected readonly formulario = form(signal({ confirmacion: '' }));

  /** «Consejo: descarga antes…»: el dataset trae la frase con mayúscula inicial. */
  protected readonly consejo = computed(() => {
    const texto = this.cuenta()?.consejo ?? '';
    return texto.charAt(0).toLowerCase() + texto.slice(1);
  });

  /** Fricción de F-W08: solo la palabra exacta del dataset habilita la acción destructiva. */
  protected readonly confirmado = computed(
    () => this.formulario.confirmacion().value() === this.cuenta()?.palabraDeConfirmacion,
  );

  protected conservar(): void {
    void this.router.navigateByUrl('/perfil');
  }

  protected eliminar(): void {
    this.sesion.cerrar();
    void this.router.navigateByUrl('/login?estado=eliminada');
  }
}
