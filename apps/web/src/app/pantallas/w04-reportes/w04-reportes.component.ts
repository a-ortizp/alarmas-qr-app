import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { AqTarjetaComponent } from '../../componentes/tarjeta/aq-tarjeta.component';
import {
  AqSelectorSegmentadoComponent,
  OpcionSegmentada,
} from '../../componentes/selector-segmentado/aq-selector-segmentado.component';
import { AqCampoComponent } from '../../componentes/campo/aq-campo.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';
import { CortesService } from '../../navegacion/cortes.service';

/** W04 · Reportes (F-W04): tarjeta de formulario (rango + formato) y tarjeta lateral «Reportes generados». */
@Component({
  selector: 'aq-w04-reportes',
  imports: [
    RouterLink,
    FormField,
    AqTarjetaComponent,
    AqSelectorSegmentadoComponent,
    AqCampoComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W04" [attr.data-estado]="listo() ? 'listo' : null">
      <a aq-enlace routerLink="/alarmas">‹ Mis alarmas</a>
      <h1 class="titulo">Reportes</h1>
      <p class="subtitulo">Descarga informes con métricas agregadas de tus eventos.</p>

      @if (datos.reporte(); as reporte) {
        <div class="fila" [attr.data-apilada]="apilar() ? '' : null">
          <aq-tarjeta class="formulario">
            <h2 class="titulo-tarjeta">Exportar reporte consolidado</h2>
            @if (!listo()) {
              <aq-selector-segmentado
                etiqueta="Rango"
                [opciones]="opcionesRango"
                [formField]="formulario.rango"
              />
              @if (formulario.rango().value() === 'personalizado') {
                <div class="fechas">
                  <aq-campo etiqueta="DESDE" tipo="text" [formField]="formulario.desde" />
                  <aq-campo etiqueta="HASTA" tipo="text" [formField]="formulario.hasta" />
                </div>
              }
              <aq-selector-segmentado
                etiqueta="Formato"
                [opciones]="opcionesFormato"
                [formField]="formulario.formato"
              />
              <p class="nota">{{ reporte.nota }}</p>
              <button aq-boton type="button" (click)="generar()">Generar y descargar</button>
            } @else {
              <p class="resultado">
                {{ reporte.archivoGenerado }} generado y descargado exitosamente
              </p>
              <button aq-boton variante="secundario" type="button" (click)="listo.set(false)">
                Generar de nuevo
              </button>
            }
          </aq-tarjeta>
          <aq-tarjeta class="generados">
            <h2 class="titulo-tarjeta">Reportes generados</h2>
            @for (g of reporte.generados; track g.archivo) {
              <div class="fila-reporte">
                <div class="info">
                  <span class="archivo">{{ g.archivo }}</span>
                  <span class="metadatos"
                    >{{ g.fecha }} · {{ g.formato.toUpperCase() }} · {{ g.rango }}</span
                  >
                </div>
                <button type="button" aq-enlace (click)="descargarDeNuevo()">
                  Descargar de nuevo
                </button>
              </div>
            }
          </aq-tarjeta>
        </div>
      }
    </section>
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
    }
    .titulo {
      margin: 0;
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .subtitulo {
      margin: 0 0 var(--space-8);
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
    }
    .fila {
      display: flex;
      align-items: flex-start;
      gap: var(--space-web-bloques);
    }
    .fila[data-apilada] {
      flex-direction: column;
      align-items: stretch;
    }
    .formulario {
      flex: 0 0 var(--size-tarjeta-formulario-web);
      gap: var(--space-16);
    }
    .fila[data-apilada] .formulario {
      flex: none;
    }
    .generados {
      flex: 1;
      gap: var(--space-12);
      min-width: 0;
    }
    .titulo-tarjeta {
      margin: 0;
      font: var(--text-titulo-tarjeta-web);
      color: var(--color-texto);
    }
    .fechas {
      display: flex;
      gap: var(--space-12);
    }
    .fechas aq-campo {
      flex: 1;
    }
    .nota {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .resultado {
      margin: 0;
      font: var(--text-cuerpo-web);
      color: var(--color-exito);
    }
    .fila-reporte {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: var(--space-12);
      padding: var(--space-12) 0;
      border-top: var(--stroke-borde-fino) solid var(--color-borde);
    }
    .info {
      display: flex;
      flex-direction: column;
      gap: var(--space-2);
      min-width: 0;
    }
    .archivo {
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    .metadatos {
      font: var(--text-nota);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W04ReportesComponent {
  protected readonly datos = inject(DatosService);
  private readonly snackbar = inject(SnackbarService);
  protected readonly apilar = inject(CortesService).apilarColumnas;

  protected readonly opcionesRango: readonly OpcionSegmentada[] = [
    { valor: 'ultimo-mes', texto: 'Último mes' },
    { valor: 'semestre', texto: 'Semestre' },
    { valor: 'personalizado', texto: 'Rango personalizado' },
  ];
  protected readonly opcionesFormato: readonly OpcionSegmentada[] = [
    { valor: 'pdf', texto: 'PDF' },
    { valor: 'csv', texto: 'CSV' },
  ];

  protected readonly formulario = form(
    signal({ rango: 'ultimo-mes', formato: 'pdf', desde: '2026-08-01', hasta: '2026-08-31' }),
  );
  protected readonly listo = signal(false);

  protected generar(): void {
    this.listo.set(true);
  }

  protected descargarDeNuevo(): void {
    const mensajes = this.datos.mensajes();
    if (mensajes) this.snackbar.mostrar(mensajes.descargaCompletada);
  }
}
