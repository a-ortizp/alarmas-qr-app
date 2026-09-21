import { Component, effect, inject, input, signal, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaAccesoComponent } from '../../componentes/tarjeta-acceso/aq-tarjeta-acceso.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';
import { SesionService } from '../../datos/sesion.service';

/** W00 · Inicio de sesión (F-W00). Estados: error de credenciales (⏩ foco en contraseña, D8), ?estado=correo-enviado y ?estado=eliminada (snackbar 3 s). */
@Component({
  selector: 'aq-w00-login',
  imports: [
    RouterLink,
    FormField,
    AqTarjetaAccesoComponent,
    AqCampoComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <main class="pagina" data-codigo="W00" [attr.data-estado]="error() ? 'error' : null">
      <h1 class="solo-lector">Inicio de sesión</h1>
      <aq-tarjeta-acceso>
        <p class="subtitulo">Administración y consulta de tus eventos y alarmas</p>
        <form class="formulario" novalidate (submit)="iniciarSesion($event)">
          <div class="campos">
            <aq-campo
              etiqueta="CORREO ELECTRÓNICO"
              tipo="email"
              placeholder="nombre@correo.com"
              [formField]="formulario.correo"
            />
            <aq-campo
              etiqueta="CONTRASEÑA"
              tipo="password"
              placeholder="••••••••"
              [formField]="formulario.contrasena"
              [error]="error()"
              [mensajeError]="error() ? (acceso()?.errorCredenciales ?? '') : ''"
              (enfocado)="simularCredencialesInvalidas()"
            />
          </div>
          <button aq-boton bloque type="submit">Iniciar sesión</button>
        </form>
        <a aq-enlace bloque routerLink="/login/recuperar">¿Olvidaste tu contraseña?</a>
        <p class="nota">
          Entra cualquier usuario registrado en el sistema.<br />¿Aún no tienes cuenta? Créala desde
          la app móvil al registrarte.
        </p>
      </aq-tarjeta-acceso>
    </main>
  `,
  styles: `
    .pagina {
      display: grid;
      place-items: center;
      box-sizing: border-box;
      min-height: 100vh;
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
    }
    .subtitulo,
    .nota {
      margin: 0;
      color: var(--color-texto-secundario);
      text-align: center;
    }
    .subtitulo {
      font: var(--text-cuerpo-web);
    }
    .nota {
      font: var(--text-nota-acceso);
    }
    .formulario,
    .campos {
      display: flex;
      flex-direction: column;
    }
    .formulario {
      gap: var(--space-tarjeta-acceso-gap);
    }
    .campos {
      gap: var(--space-12);
    }
  `,
})
export class W00LoginComponent {
  private readonly datos = inject(DatosService);
  private readonly sesion = inject(SesionService);
  private readonly router = inject(Router);
  private readonly snackbar = inject(SnackbarService);

  /** Query param ?estado= (withComponentInputBinding). */
  readonly estado = input<string>();
  protected readonly acceso = this.datos.acceso;
  protected readonly formulario = form(signal({ correo: '', contrasena: '' }));
  protected readonly error = signal(false);

  constructor() {
    effect(() => {
      const mensajes = this.datos.mensajes();
      const estado = this.estado();
      if (!mensajes) return;
      const texto =
        estado === 'eliminada'
          ? mensajes.cuentaEliminada
          : estado === 'correo-enviado'
            ? mensajes.correoRecuperacionEnviado
            : null;
      if (texto) untracked(() => this.snackbar.mostrar(texto));
    });
  }

  /** ⏩ Disparador simulado del prototipo: el foco en la contraseña muestra el error de credenciales (NAVEGACION §6b). */
  protected simularCredencialesInvalidas(): void {
    this.error.set(true);
  }

  protected iniciarSesion(evento: Event): void {
    evento.preventDefault();
    this.sesion.iniciar();
    void this.router.navigateByUrl('/alarmas');
  }
}
