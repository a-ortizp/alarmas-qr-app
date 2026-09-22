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
import { formatoFechaCorta } from '../../datos/formato-fecha';

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
      <a aq-enlace variante="miga" routerLink="/alarmas">‹ Mis alarmas / Reportes</a>
      <h1 class="titulo">Reportes</h1>
      <p class="subtitulo">
        Genera y descarga reportes consolidados de tus eventos. Solo datos agregados y anónimos (Ley
        1581).
      </p>

      @if (datos.reporte(); as reporte) {
        <div class="fila" [attr.data-apilada]="apilar() ? '' : null">
          <aq-tarjeta class="formulario">
            <div class="grupo-titulo-tarjeta">
              <h2 class="titulo-tarjeta-formulario">Exportar reporte consolidado</h2>
              <p class="subtitulo-tarjeta">Genera un reporte ejecutivo de tus alarmas</p>
            </div>
            @if (!listo()) {
              <div class="grupo-campo">
                <span class="etiqueta-campo">RANGO DE FECHAS</span>
                <aq-selector-segmentado
                  etiqueta="Rango"
                  [opciones]="opcionesRango"
                  [formField]="formulario.rango"
                />
              </div>
              @if (formulario.rango().value() === 'personalizado') {
                <div class="fechas">
                  <aq-campo etiqueta="DESDE" tipo="text" [formField]="formulario.desde" />
                  <aq-campo etiqueta="HASTA" tipo="text" [formField]="formulario.hasta" />
                </div>
              }
              <div class="grupo-campo">
                <span class="etiqueta-campo">FORMATO DE SALIDA</span>
                <aq-selector-segmentado
                  etiqueta="Formato"
                  [opciones]="opcionesFormato"
                  [formField]="formulario.formato"
                />
              </div>
              <p class="nota">{{ reporte.nota }}</p>
              <div class="acciones">
                <a aq-boton variante="secundario" routerLink="/alarmas">Cancelar</a>
                <button aq-boton type="button" (click)="generar()">Generar y descargar</button>
              </div>
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
            <div class="grupo-titulo-tarjeta">
              <h2 class="titulo-tarjeta">Reportes generados</h2>
              <p class="subtitulo-tarjeta">
                Los últimos reportes quedan disponibles {{ reporte.retencionDias }} días.
              </p>
            </div>
            @for (g of reporte.generados; track g.archivo) {
              <div class="fila-reporte">
                <div class="info">
                  <span class="archivo">{{ g.archivo }}</span>
                  <span class="metadatos"
                    >{{ formatoFecha(g.fecha) }} · {{ g.formato.toUpperCase() }} ·
                    {{ textoRango(g.rango) }}</span
                  >
                </div>
                <button type="button" aq-enlace variante="fila" (click)="descargarDeNuevo()">
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
    .grupo-titulo-tarjeta {
      display: flex;
      flex-direction: column;
      gap: var(--space-4);
    }
    .titulo-tarjeta-formulario {
      margin: 0;
      font: var(--text-titulo-tarjeta-formulario-web);
      color: var(--color-texto);
    }
    .titulo-tarjeta {
      margin: 0;
      font: var(--text-titulo-tarjeta);
      color: var(--color-texto);
    }
    .subtitulo-tarjeta {
      margin: 0;
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .grupo-campo {
      display: flex;
      flex-direction: column;
      gap: var(--space-8);
    }
    .etiqueta-campo {
      font: var(--text-rotulo-tabla);
      color: var(--color-gris-texto);
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
    .acciones {
      display: flex;
      justify-content: flex-end;
      gap: var(--space-12);
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
  protected readonly formatoFecha = formatoFechaCorta;

  protected textoRango(valor: string): string {
    return this.opcionesRango.find((o) => o.valor === valor)?.texto ?? valor;
  }

  protected generar(): void {
    this.listo.set(true);
  }

  protected descargarDeNuevo(): void {
    const mensajes = this.datos.mensajes();
    if (mensajes) this.snackbar.mostrar(mensajes.descargaCompletada);
  }
}
