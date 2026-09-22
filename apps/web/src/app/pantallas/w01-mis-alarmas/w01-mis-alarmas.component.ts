import { Component, computed, effect, inject, input, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AqIndicadorComponent } from '../../componentes/indicador/aq-indicador.component';
import { AqGraficaBarrasComponent } from '../../componentes/grafica-barras/aq-grafica-barras.component';
import { AqPildorasComponent } from '../../componentes/pildoras/aq-pildoras.component';
import { AqCampoBusquedaComponent } from '../../componentes/campo-busqueda/aq-campo-busqueda.component';
import { AqTablaComponent } from '../../componentes/tabla/aq-tabla.component';
import { AqChipComponent, VarianteChip } from '../../componentes/chip/aq-chip.component';
import { AqEstadoVacioComponent } from '../../componentes/estado-vacio/aq-estado-vacio.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { AqIconoComponent } from '../../componentes/icono/aq-icono.component';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { DatosService } from '../../datos/datos.service';
import { formatoFechaHoraCorta } from '../../datos/formato-fecha';
import { EventoPasado, EventoWeb } from '../../datos/modelos';

interface FilaEvento {
  id: string | null;
  titulo: string;
  fechaHora: string;
  chipOrigen: VarianteChip;
  textoOrigen: string;
  escaneos: number;
  alarmasActivas: number;
  chipEstado: VarianteChip;
  textoEstado: string;
}

function aFila(e: EventoWeb): FilaEvento {
  return {
    id: e.id,
    titulo: e.titulo,
    fechaHora: formatoFechaHoraCorta(e.fechaHora),
    chipOrigen: e.origen === 'creada-por-mi' ? 'creada-por-mi' : 'escaneada',
    textoOrigen: e.origen === 'creada-por-mi' ? 'Creada por mí' : '✓ Escaneada',
    escaneos: e.escaneos,
    alarmasActivas: e.alarmasActivas,
    chipEstado: 'publicado',
    textoEstado: 'Publicado',
  };
}

function pasadoAFila(e: EventoPasado): FilaEvento {
  return {
    id: null,
    titulo: e.nombre,
    fechaHora: formatoFechaHoraCorta(e.fechaHora),
    chipOrigen: e.origen === 'creada-por-mi' ? 'creada-por-mi' : 'escaneada',
    textoOrigen: e.origen === 'creada-por-mi' ? 'Creada por mí' : '✓ Escaneada',
    escaneos: e.escaneos,
    alarmasActivas: e.alarmasActivas,
    chipEstado: 'activa',
    textoEstado: 'Finalizado',
  };
}

/** W01 · Mis Alarmas (F-W01 · F-W02 · F-W06): hub con indicadores, gráfica y tabla de eventos con pestañas, filtros y búsqueda. */
@Component({
  selector: 'aq-w01-mis-alarmas',
  imports: [
    RouterLink,
    AqIndicadorComponent,
    AqGraficaBarrasComponent,
    AqPildorasComponent,
    AqCampoBusquedaComponent,
    AqTablaComponent,
    AqChipComponent,
    AqEstadoVacioComponent,
    AqBotonComponent,
    AqEnlaceComponent,
    AqIconoComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W01">
      <header class="cabecera">
        <h1 class="titulo">Mis Alarmas</h1>
        <div class="acciones">
          <a aq-boton routerLink="/reportes">Exportar reporte</a>
          <a aq-boton variante="secundario" routerLink="/qr">Descargar QR en lote</a>
        </div>
      </header>

      @if (datos.web(); as web) {
        <div class="indicadores">
          <aq-indicador
            [valor]="web.indicadores.eventosActivos.valor"
            etiqueta="Eventos activos"
            [detalle]="web.indicadores.eventosActivos.detalle"
          />
          <aq-indicador
            [valor]="web.indicadores.escaneosTotales.valor"
            etiqueta="Escaneos totales"
            [detalle]="web.indicadores.escaneosTotales.detalle"
          />
          <aq-indicador
            [valor]="web.indicadores.alarmasActivas.valor"
            etiqueta="Alarmas activas"
            [detalle]="web.indicadores.alarmasActivas.detalle"
          />
          <aq-indicador
            [valor]="web.indicadores.confirmaronYaVoy.valor"
            etiqueta='Confirmaron "Ya voy"'
            [detalle]="web.indicadores.confirmaronYaVoy.detalle"
          />
        </div>

        <div class="filtros-busqueda">
          <aq-campo-busqueda
            ancho="w01"
            [value]="qActual()"
            (buscar)="irA({ q: $event || null })"
          />
          <div class="filtros">
            <aq-pildoras
              [opciones]="opcionesEstado"
              [activo]="estadoActual()"
              (elegir)="irA({ estado: $event })"
            />
            <aq-pildoras
              [opciones]="opcionesOrigen"
              [activo]="origenActual()"
              (elegir)="irA({ origen: $event })"
            />
          </div>
        </div>

        @if (filas().length > 0) {
          <div class="contenedor-tabla">
            <table aq-tabla>
              <thead>
                <tr>
                  <th>Nombre del evento</th>
                  <th>Fecha y hora</th>
                  <th>Origen</th>
                  <th>Escaneos</th>
                  <th>Alarmas activas</th>
                  <th>Estado</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                @for (fila of filas(); track fila.titulo) {
                  <tr>
                    <td>{{ fila.titulo }}</td>
                    <td>{{ fila.fechaHora }}</td>
                    <td>
                      <aq-chip [variante]="fila.chipOrigen">{{ fila.textoOrigen }}</aq-chip>
                    </td>
                    <td>{{ fila.escaneos }}</td>
                    <td>{{ fila.alarmasActivas }}</td>
                    <td>
                      <aq-chip [variante]="fila.chipEstado">{{ fila.textoEstado }}</aq-chip>
                    </td>
                    <td>
                      @if (fila.id) {
                        <a aq-enlace [routerLink]="['/eventos', fila.id]">Ver detalle ›</a>
                      }
                    </td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
          <div class="pie-tabla">
            <p class="conteo">
              Mostrando {{ filas().length }} {{ filas().length === 1 ? 'alarma' : 'alarmas' }}
              @if (qActual()) {
                · filtro: "{{ qActual() }}"
                <button type="button" aq-enlace (click)="irA({ q: null })">Limpiar</button>
              }
            </p>
            <p class="nota-metricas">
              <aq-icono nombre="info" tamano="vineta" />
              Las métricas de eventos escaneados pertenecen a su organizador.
            </p>
          </div>
        } @else {
          <aq-estado-vacio
            titulo="Sin resultados con estos filtros"
            [texto]="
              web.filtros.borradores.length === 0 && estadoActual() === 'borradores'
                ? mensajeSinBorradores()
                : ''
            "
          >
            <button
              type="button"
              aq-boton
              variante="secundario"
              (click)="irA({ estado: null, origen: null, q: null })"
            >
              Ver todos
            </button>
          </aq-estado-vacio>
        }

        <aq-grafica-barras [datos]="web.escaneosPorSemana" />
      }
    </section>
  `,
  styles: `
    .pagina {
      display: flex;
      flex-direction: column;
      gap: var(--space-web-bloques);
    }
    .cabecera {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
    }
    .titulo {
      margin: 0;
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .acciones {
      display: flex;
      gap: var(--space-12);
    }
    .indicadores {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-web-indicadores);
    }
    .filtros-busqueda {
      display: flex;
      flex-direction: column;
      gap: var(--space-12);
    }
    .filtros {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-20);
    }
    .contenedor-tabla {
      overflow-x: auto;
    }
    .pie-tabla {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
    }
    .conteo,
    .nota-metricas {
      margin: 0;
      display: flex;
      align-items: center;
      gap: var(--space-8);
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W01MisAlarmasComponent {
  protected readonly datos = inject(DatosService);
  private readonly router = inject(Router);
  private readonly snackbar = inject(SnackbarService);

  readonly origen = input<string>('todos');
  readonly estado = input<string>('proximos');
  readonly q = input<string>('');

  // El enrutador (withComponentInputBinding) fuerza estos inputs a `undefined` cuando el
  // query param no está en la URL, sin respetar el valor por defecto del input: estas señales
  // derivadas restauran ese valor por defecto para el resto del componente.
  protected readonly origenActual = computed(() => this.origen() ?? 'todos');
  protected readonly estadoActual = computed(() => this.estado() ?? 'proximos');
  protected readonly qActual = computed(() => this.q() ?? '');

  protected readonly opcionesOrigen = [
    { valor: 'todos', texto: 'Todos' },
    { valor: 'creados', texto: 'Creados' },
    { valor: 'escaneados', texto: 'Escaneados' },
  ];
  protected readonly opcionesEstado = [
    { valor: 'proximos', texto: 'Próximos' },
    { valor: 'pasados', texto: 'Pasados' },
    { valor: 'borradores', texto: 'Borradores' },
  ];

  protected readonly filas = computed<FilaEvento[]>(() => {
    const web = this.datos.web();
    if (!web) return [];
    const estado = this.estadoActual();

    if (estado === 'pasados') return web.eventosPasados.map(pasadoAFila);
    if (estado === 'borradores') return [];

    let base = web.eventos;
    const origen = this.origenActual();
    if (origen === 'creados') base = base.filter((e) => e.origen === 'creada-por-mi');
    else if (origen === 'escaneados') base = base.filter((e) => e.origen === 'escaneada');

    const consulta = this.qActual().trim().toLowerCase();
    if (consulta) base = base.filter((e) => e.titulo.toLowerCase().includes(consulta));

    return base.map(aFila);
  });

  protected readonly mensajeSinBorradores = computed(
    () => this.datos.mensajes()?.sinBorradores ?? '',
  );

  constructor() {
    effect(() => {
      const mensajes = this.datos.mensajes();
      if (mensajes && this.estadoActual() === 'descarga-completada') {
        untracked(() => this.snackbar.mostrar(mensajes.descargaCompletada));
      }
    });
  }

  protected irA(cambios: Record<string, string | null>): void {
    void this.router.navigate([], { queryParams: cambios, queryParamsHandling: 'merge' });
  }
}
