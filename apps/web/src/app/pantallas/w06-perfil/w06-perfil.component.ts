import { Component, inject, linkedSignal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaComponent } from '../../componentes/tarjeta/aq-tarjeta.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqSwitchComponent } from '../../componentes/switch/aq-switch.component';
import {
  AqSelectorSegmentadoComponent,
  OpcionSegmentada,
} from '../../componentes/selector-segmentado/aq-selector-segmentado.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';
import { CortesService } from '../../navegacion/cortes.service';

/** W06 · Ajustes de Perfil (F-W07): perfil, privacidad ante organizadores y acceso al modal «Eliminar cuenta» (hija /perfil/eliminar). */
@Component({
  selector: 'aq-w06-perfil',
  imports: [
    RouterLink,
    RouterOutlet,
    FormField,
    AqTarjetaComponent,
    AqCampoComponent,
    AqBotonComponent,
    AqSwitchComponent,
    AqSelectorSegmentadoComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W06">
      <h1 class="titulo">Ajustes de Perfil</h1>
      <div class="fila" [attr.data-apilada]="apilar() ? '' : null">
        <aq-tarjeta class="perfil">
          <h2 class="titulo-tarjeta">Perfil</h2>
          <aq-campo etiqueta="NOMBRES Y APELLIDOS" [formField]="formularioPerfil.nombre" />
          <aq-campo etiqueta="ALIAS PÚBLICO" [formField]="formularioPerfil.alias" />
          <aq-campo
            etiqueta="CORREO ELECTRÓNICO"
            tipo="email"
            [formField]="formularioPerfil.correo"
          />
          <div class="accion">
            <button aq-boton type="button" (click)="guardar()">Guardar cambios</button>
          </div>
        </aq-tarjeta>
        <div class="columna">
          <aq-tarjeta class="privacidad">
            <h2 class="titulo-tarjeta">Privacidad ante organizadores</h2>
            <p class="descripcion">Así apareces en "Quiénes escanearon" (Ley 1581).</p>
            <aq-selector-segmentado
              etiqueta="Cómo apareces ante los organizadores"
              [opciones]="opcionesAparicion"
              [formField]="formularioPrivacidad.aparicion"
            />
            <aq-switch
              etiqueta="Mostrar el estado de mi alarma"
              [formField]="formularioPrivacidad.mostrarEstado"
            />
            <aq-switch
              etiqueta='Contar mi "Ya voy" en las métricas'
              [formField]="formularioPrivacidad.contarYaVoy"
            />
          </aq-tarjeta>
          <aq-tarjeta class="eliminacion" variante="peligro">
            <h2 class="titulo-tarjeta">Eliminación de cuenta</h2>
            <p class="descripcion">
              Borra permanentemente tu perfil, eventos creados y el historial de escaneos.
            </p>
            <a aq-boton variante="destructivo" routerLink="eliminar">Eliminar mi cuenta</a>
          </aq-tarjeta>
        </div>
      </div>
      <p class="nota">
        Los cambios de perfil no afectan tus alarmas en el celular: en modo invitado siguen siendo
        locales.
      </p>
    </section>
    <router-outlet />
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-web-bloques);
    }
    .titulo,
    .titulo-tarjeta,
    .descripcion,
    .nota {
      margin: 0;
    }
    .titulo {
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .fila {
      display: flex;
      align-items: flex-start;
      gap: var(--space-web-bloques);
    }
    .perfil {
      flex: 0 0 var(--size-tarjeta-perfil-web);
      gap: var(--space-16);
    }
    /* Bajo el corte web-apilar-columnas (tokens v1.12) las dos columnas se apilan a todo el ancho. */
    .fila[data-apilada] {
      flex-direction: column;
      align-items: stretch;
    }
    .fila[data-apilada] .perfil {
      flex: none;
    }
    .columna {
      display: flex;
      flex: 1;
      flex-direction: column;
      gap: var(--space-web-bloques);
      min-width: 0;
    }
    .privacidad {
      gap: var(--space-12);
    }
    .eliminacion {
      gap: var(--space-10);
    }
    .eliminacion a {
      align-self: flex-start;
    }
    .titulo-tarjeta {
      font: var(--text-titulo-tarjeta-web);
      color: var(--color-texto);
    }
    .descripcion {
      font: var(--text-descripcion-web);
      color: var(--color-texto-secundario);
    }
    .accion {
      display: flex;
      justify-content: flex-end;
    }
    .nota {
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W06PerfilComponent {
  private readonly datos = inject(DatosService);
  private readonly snackbar = inject(SnackbarService);
  protected readonly apilar = inject(CortesService).apilarColumnas;

  protected readonly opcionesAparicion: readonly OpcionSegmentada[] = [
    { valor: 'nombre-completo', texto: 'Nombre completo' },
    { valor: 'solo-iniciales', texto: 'Solo iniciales' },
    { valor: 'alias', texto: 'Alias' },
  ];

  private readonly perfil = linkedSignal(() => {
    const u = this.datos.usuario();
    return { nombre: u?.nombre ?? '', alias: u?.aliasPublico ?? '', correo: u?.correo ?? '' };
  });
  private readonly privacidad = linkedSignal(() => {
    const p = this.datos.usuario()?.privacidad;
    return {
      aparicion: (p?.apariciónEnQuienesEscanearon ?? 'solo-iniciales') as string,
      mostrarEstado: p?.mostrarEstadoDeMiAlarma ?? true,
      contarYaVoy: p?.contarMiYaVoyEnMetricas ?? true,
    };
  });
  protected readonly formularioPerfil = form(this.perfil);
  protected readonly formularioPrivacidad = form(this.privacidad);

  /** Maquetación sin backend: guardar solo confirma con el snackbar de 3 s (W06 · Actualizado). */
  protected guardar(): void {
    const mensajes = this.datos.mensajes();
    if (mensajes) this.snackbar.mostrar(mensajes.perfilActualizado);
  }
}
