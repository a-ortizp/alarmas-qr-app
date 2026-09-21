import { Component, computed, inject, linkedSignal } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { A11yModule } from '@angular/cdk/a11y';
import { filter } from 'rxjs';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet, UrlTree } from '@angular/router';
import { AqBarraSuperiorComponent } from '../../componentes/barra-superior/aq-barra-superior.component';
import { AqBarraLateralComponent } from '../../componentes/barra-lateral/aq-barra-lateral.component';
import { AqDialogoConfirmacionComponent } from '../../componentes/dialogo-confirmacion/aq-dialogo-confirmacion.component';
import { DatosService } from '../../datos/datos.service';
import { SesionService } from '../../datos/sesion.service';
import { CortesService } from '../../navegacion/cortes.service';

/**
 * Layout de las páginas autenticadas: barra superior 64 + barra lateral 208/64 + contenido; «¿Cerrar sesión?» vive en ?dialogo=cerrar-sesion (TRAZABILIDAD §2).
 * Tokens v1.13: bajo --breakpoint-web-cajon la barra lateral sale del grid y se abre como cajón (☰) sobre velo Tinta 55 %.
 */
@Component({
  selector: 'aq-layout-app',
  imports: [
    A11yModule,
    RouterOutlet,
    AqBarraSuperiorComponent,
    AqBarraLateralComponent,
    AqDialogoConfirmacionComponent,
  ],
  template: `
    <div class="layout" [class.colapsada]="colapsada() && !cajon()" [class.cajon]="cajon()">
      <aq-barra-superior
        class="superior"
        [nombre]="usuario()?.nombre ?? ''"
        [menu]="cajon()"
        [menuAbierto]="menuAbierto()"
        (abrirMenu)="menuAbierto.set(true)"
      />
      @if (!cajon()) {
        <aq-barra-lateral [(colapsada)]="colapsada" (cerrarSesion)="abrirDialogo()" />
      }
      <main class="contenido"><router-outlet /></main>
    </div>
    @if (cajon() && menuAbierto()) {
      <div class="cajon-menu">
        <div class="velo" data-velo-menu (click)="menuAbierto.set(false)"></div>
        <div
          id="menu-cajon"
          class="panel"
          role="dialog"
          aria-modal="true"
          aria-label="Menú"
          cdkTrapFocus
          [cdkTrapFocusAutoCapture]="true"
          (keydown.escape)="menuAbierto.set(false)"
        >
          <aq-barra-lateral
            cajon
            (cerrarCajon)="menuAbierto.set(false)"
            (cerrarSesion)="menuAbierto.set(false); abrirDialogo()"
          />
        </div>
      </div>
    }
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
      height: 100dvh;
      transition: grid-template-columns var(--motion-transicion);
    }
    .layout.colapsada {
      grid-template-columns: var(--size-barra-lateral-web-colapsada) minmax(0, 1fr);
    }
    .layout.cajon {
      grid-template-columns: minmax(0, 1fr);
    }
    .cajon-menu {
      position: fixed;
      inset: 0;
      z-index: 20;
    }
    .velo {
      position: absolute;
      inset: 0;
      background: var(--color-velo-movil);
    }
    .panel {
      position: absolute;
      inset: 0 auto 0 0;
      width: var(--size-barra-lateral-web);
      max-width: 100%;
      animation: var(--motion-transicion) entrar-cajon;
    }
    .panel aq-barra-lateral {
      height: 100%;
    }
    @keyframes entrar-cajon {
      from {
        transform: translateX(-100%);
      }
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
  private readonly cortes = inject(CortesService);
  private readonly consulta = toSignal(inject(ActivatedRoute).queryParamMap);

  /**
   * Colapso automático (token breakpoint web-colapsar-barra): se reinicia cada vez que la ventana cruza el corte;
   * entre cruces el control «Colapsar / Expandir menú» manda.
   */
  readonly colapsada = linkedSignal(this.cortes.colapsarBarra);
  /** Bajo --breakpoint-web-cajon la barra lateral es un cajón. */
  readonly cajon = this.cortes.cajon;
  /** El cajón arranca cerrado y se cierra solo al cruzar el corte o al navegar. */
  readonly menuAbierto = linkedSignal({ source: this.cajon, computation: () => false });
  readonly usuario = this.datos.usuario;
  readonly mensajes = this.datos.mensajes;
  readonly dialogoAbierto = computed(() => this.consulta()?.get('dialogo') === 'cerrar-sesion');

  constructor() {
    this.router.events
      .pipe(
        filter((e) => e instanceof NavigationEnd),
        takeUntilDestroyed(),
      )
      .subscribe(() => this.menuAbierto.set(false));
  }

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
