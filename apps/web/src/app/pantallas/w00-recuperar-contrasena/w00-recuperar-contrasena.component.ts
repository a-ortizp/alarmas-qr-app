import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaAccesoComponent } from '../../componentes/tarjeta-acceso/aq-tarjeta-acceso.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { DatosService } from '../../datos/datos.service';

/** W00 · Recuperar contraseña (F-W00, /login/recuperar): «Enviar enlace» → W00 con snackbar de correo enviado. */
@Component({
  selector: 'aq-w00-recuperar-contrasena',
  imports: [
    RouterLink,
    FormField,
    AqTarjetaAccesoComponent,
    AqCampoComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <main class="pagina" data-codigo="W00" data-estado="recuperar">
      @if (recuperar(); as r) {
        <aq-tarjeta-acceso>
          <h1 class="titulo">{{ r.titulo }}</h1>
          <p class="texto">{{ r.texto }}</p>
          <form class="formulario" novalidate (submit)="enviarEnlace($event)">
            <aq-campo
              etiqueta="CORREO ELECTRÓNICO"
              tipo="email"
              placeholder="nombre@correo.com"
              [formField]="formulario.correo"
            />
            <button aq-boton bloque type="submit">{{ r.boton }}</button>
          </form>
          <a aq-enlace bloque routerLink="/login">{{ r.volver }}</a>
        </aq-tarjeta-acceso>
      }
    </main>
  `,
  styles: `
    .pagina {
      display: grid;
      place-items: center;
      /* Columna que puede encogerse: la tarjeta de 520 baja hasta el ancho de la ventana (tokens v1.13). */
      grid-template-columns: minmax(0, 1fr);
      box-sizing: border-box;
      min-height: 100vh;
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
    }
    .titulo {
      margin: 0;
      font: var(--text-titulo-dialogo);
      color: var(--color-texto);
      text-align: center;
    }
    .texto {
      margin: 0;
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
      text-align: center;
    }
    .formulario {
      display: flex;
      flex-direction: column;
      gap: var(--space-tarjeta-acceso-gap);
    }
  `,
})
export class W00RecuperarContrasenaComponent {
  private readonly router = inject(Router);
  private readonly datos = inject(DatosService);
  protected readonly recuperar = computed(() => this.datos.acceso()?.recuperar);
  protected readonly formulario = form(signal({ correo: '' }));

  protected enviarEnlace(evento: Event): void {
    evento.preventDefault();
    void this.router.navigateByUrl('/login?estado=correo-enviado');
  }
}
