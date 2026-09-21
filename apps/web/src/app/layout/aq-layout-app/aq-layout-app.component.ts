import { Component, computed, inject, linkedSignal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router, RouterOutlet, UrlTree } from '@angular/router';
import { AqBarraSuperiorComponent } from '../../componentes/barra-superior/aq-barra-superior.component';
import { AqBarraLateralComponent } from '../../componentes/barra-lateral/aq-barra-lateral.component';
import { AqDialogoConfirmacionComponent } from '../../componentes/dialogo-confirmacion/aq-dialogo-confirmacion.component';
import { DatosService } from '../../datos/datos.service';
import { SesionService } from '../../datos/sesion.service';
import { CortesService } from '../../navegacion/cortes.service';

/** Layout de las páginas autenticadas: barra superior 64 + barra lateral 208/64 + contenido; «¿Cerrar sesión?» vive en ?dialogo=cerrar-sesion (TRAZABILIDAD §2). */
@Component({
  selector: 'aq-layout-app',
  imports: [
    RouterOutlet,
    AqBarraSuperiorComponent,
    AqBarraLateralComponent,
    AqDialogoConfirmacionComponent,
  ],
  template: `
    <div class="layout" [class.colapsada]="colapsada()">
      <aq-barra-superior class="superior" [nombre]="usuario()?.nombre ?? ''" />
      <aq-barra-lateral [(colapsada)]="colapsada" (cerrarSesion)="abrirDialogo()" />
      <main class="contenido"><router-outlet /></main>
    </div>
    @if (dialogoAbierto()) {
      @if (mensajes(); as m) {
        <aq-dialogo-confirmacion
          [titulo]="m.confirmarCerrarSesionTitulo"
          [cuerpo]="m.confirmarCerrarSesionCuerpoWeb"
          [rotuloSeguro]="m.confirmarCerrarSesionSeguro"
          [rotuloAccion]="m.confirmarCerrarSesionAccion"
          (seguro)="cerrarDialogo()"
          (confirmar)="cerrarSesion()"
        />
      }
    }
  `,
  styles: `
    .layout {
      display: grid;
      grid-template-columns: var(--size-barra-lateral-web) minmax(0, 1fr);
      grid-template-rows: var(--size-barra-superior-web) minmax(0, 1fr);
      height: 100vh;
      transition: grid-template-columns var(--motion-transicion);
    }
    .layout.colapsada {
      grid-template-columns: var(--size-barra-lateral-web-colapsada) minmax(0, 1fr);
    }
    .superior {
      grid-column: 1 / -1;
    }
    .contenido {
      min-width: 0;
      overflow: auto;
      padding: var(--space-web-contenido-y) var(--space-web-contenido-x);
    }
  `,
})
export class AqLayoutAppComponent {
  private readonly router = inject(Router);
  private readonly datos = inject(DatosService);
  private readonly sesion = inject(SesionService);
  private readonly consulta = toSignal(inject(ActivatedRoute).queryParamMap);

  /**
   * Colapso automático (token breakpoint web-colapsar-barra): se reinicia cada vez que la ventana cruza el corte;
   * entre cruces el control «Colapsar / Expandir menú» manda.
   */
  readonly colapsada = linkedSignal(inject(CortesService).colapsarBarra);
  readonly usuario = this.datos.usuario;
  readonly mensajes = this.datos.mensajes;
  readonly dialogoAbierto = computed(() => this.consulta()?.get('dialogo') === 'cerrar-sesion');

  abrirDialogo(): void {
    void this.router.navigateByUrl(this.urlConDialogo('cerrar-sesion'));
  }

  cerrarDialogo(): void {
    void this.router.navigateByUrl(this.urlConDialogo(null), { replaceUrl: true });
  }

  cerrarSesion(): void {
    this.sesion.cerrar();
    void this.router.navigateByUrl('/login');
  }

  /** La página actual con ?dialogo= puesto o quitado; conserva la ruta y los demás query params. */
  private urlConDialogo(valor: string | null): UrlTree {
    const arbol = this.router.parseUrl(this.router.url);
    const consulta = { ...arbol.queryParams };
    delete consulta['dialogo'];
    arbol.queryParams = valor ? { ...consulta, dialogo: valor } : consulta;
    return arbol;
  }
}
