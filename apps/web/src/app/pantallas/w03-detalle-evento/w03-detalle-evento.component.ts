import { Component, computed, inject, input } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AqIndicadorComponent } from '../../componentes/indicador/aq-indicador.component';
import { AqCampoBusquedaComponent } from '../../componentes/campo-busqueda/aq-campo-busqueda.component';
import { AqTablaComponent } from '../../componentes/tabla/aq-tabla.component';
import { AqChipComponent } from '../../componentes/chip/aq-chip.component';
import { AqPaginadorComponent } from '../../componentes/paginador/aq-paginador.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { AqIconoComponent } from '../../componentes/icono/aq-icono.component';
import { DatosService } from '../../datos/datos.service';
import { formatoFechaHoraCorta, formatoFechaLarga, formatoHora12 } from '../../datos/formato-fecha';
import { Asistente } from '../../datos/modelos';

/**
 * `dataset.json` (asistentes de "w-partido") solo trae 2 páginas (8 de 16 asistentes); las páginas
 * 3 y 4 se completan aquí con datos inventados, nunca en el dataset (D4 del Plan 5: `dataset.json`
 * nunca se modifica). La columna «Confirmó "Ya voy"» no se calcula desde estos datos — el mockup la
 * muestra siempre como «—», así que estas filas no necesitan `confirmoYaVoy`/`yaVoy`.
 */
const ASISTENTES_INVENTADOS: readonly Asistente[] = [
  { alias: 'Fer_22', escaneo: '2026-08-14T09:10:00-05:00', alarma: 'activa' },
  { alias: 'Caro_R', escaneo: '2026-08-13T20:45:00-05:00', alarma: 'activa' },
  { alias: 'Tavo9', escaneo: '2026-08-12T16:30:00-05:00', alarma: 'eliminada' },
  { alias: 'Pao_M', escaneo: '2026-08-11T11:15:00-05:00', alarma: 'activa' },
  { alias: 'Kata07', escaneo: '2026-08-10T19:00:00-05:00', alarma: 'activa' },
  { alias: 'Julian.C', escaneo: '2026-08-09T14:20:00-05:00', alarma: 'activa' },
  { alias: 'Sofi_B', escaneo: '2026-08-08T10:05:00-05:00', alarma: 'eliminada' },
  { alias: 'Dani21', escaneo: '2026-08-07T21:40:00-05:00', alarma: 'activa' },
];

/** W03 · Detalle Evento (F-W03): indicadores del evento y tabla anónima de «Quiénes escanearon» (Ley 1581). */
@Component({
  selector: 'aq-w03-detalle-evento',
  imports: [
    RouterLink,
    AqIndicadorComponent,
    AqCampoBusquedaComponent,
    AqTablaComponent,
    AqChipComponent,
    AqPaginadorComponent,
    AqBotonComponent,
    AqEnlaceComponent,
    AqIconoComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W03">
      <a aq-enlace variante="miga" routerLink="/alarmas">‹ Mis alarmas{{ migaEvento() }}</a>

      @if (evento(); as evento) {
        <header class="cabecera">
          <div class="grupo-titulo">
            <h1 class="titulo">{{ evento.titulo }}</h1>
            <p class="subtitulo">{{ subtituloEvento() }}</p>
          </div>
          @if (evento.estado === 'publicado') {
            <aq-chip variante="publicado">Publicado</aq-chip>
          }
          <a aq-boton variante="secundario" routerLink="/reportes">Exportar reporte</a>
        </header>

        <div class="indicadores">
          <aq-indicador [valor]="evento.escaneos" etiqueta="Escaneos de QR" detalle="" />
          <aq-indicador
            [valor]="evento.alarmasActivas"
            etiqueta="Alarmas activas"
            [detalle]="
              evento.alarmasEliminadas
                ? evento.alarmasEliminadas + ' la eliminaron'
                : 'Sin eliminaciones registradas'
            "
          />
          <aq-indicador
            [valor]="evento.confirmaronYaVoy ?? 0"
            etiqueta='Confirmaron "Ya voy"'
            detalle=""
          />
        </div>

        <div class="cabecera-tabla">
          <h2 class="titulo-tabla">Quiénes escanearon</h2>
          <aq-campo-busqueda
            ancho="w03"
            placeholder="Buscar asistente"
            [value]="qActual()"
            (buscar)="buscar($event)"
          />
        </div>

        @if (filas().length > 0) {
          <div class="contenedor-tabla">
            <table aq-tabla class="fija">
              <thead>
                <tr>
                  <th class="col">Asistente</th>
                  <th class="col">Fecha de escaneo</th>
                  <th class="col">Alarma</th>
                  <th class="col">Confirmó &quot;Ya voy&quot;</th>
                </tr>
              </thead>
              <tbody>
                @for (fila of filas(); track fila.alias) {
                  <tr>
                    <td>{{ fila.alias }}</td>
                    <td>{{ formatoFecha(fila.escaneo) }}</td>
                    <td>
                      <aq-chip [variante]="fila.alarma === 'activa' ? 'activa' : 'eliminada'">{{
                        fila.alarma === 'activa' ? 'Activa' : 'Eliminada'
                      }}</aq-chip>
                    </td>
                    <td class="centrado">—</td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
          @if (!qActual()) {
            <aq-paginador
              [texto]="textoPaginador()"
              [paginaActual]="paginaActual()"
              [totalPaginas]="totalPaginas()"
              (cambiar)="irAPagina($event)"
            />
          }
        } @else if (qActual()) {
          <p class="sin-asistentes">
            Sin resultados para "{{ qActual() }}"
            <button type="button" aq-enlace (click)="buscar('')">Limpiar</button>
          </p>
        } @else {
          <p class="sin-asistentes">Aún no hay asistentes registrados para este evento.</p>
        }

        <p class="privacidad">
          <aq-icono nombre="info" tamano="vineta" />
          {{ notaPrivacidad() }}
        </p>
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
    .grupo-titulo {
      display: flex;
      flex-direction: column;
      gap: var(--space-2);
    }
    .titulo {
      margin: 0;
      font: var(--text-h1-web);
      color: var(--color-texto);
    }
    .subtitulo {
      margin: 0;
      font: var(--text-cuerpo-web);
      color: var(--color-texto-secundario);
    }
    .indicadores {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-web-indicadores);
    }
    .cabecera-tabla {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: var(--space-12);
    }
    .titulo-tabla {
      margin: 0;
      font: var(--text-titulo-tarjeta-web);
      color: var(--color-texto);
    }
    .contenedor-tabla {
      overflow-x: auto;
    }
    .fija {
      table-layout: fixed;
    }
    .col {
      width: 25%;
    }
    .centrado {
      text-align: center;
    }
    .privacidad,
    .sin-asistentes {
      margin: 0;
      display: flex;
      align-items: center;
      gap: var(--space-8);
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
  `,
})
export class W03DetalleEventoComponent {
  private readonly datos = inject(DatosService);
  private readonly router = inject(Router);

  readonly id = input.required<string>();
  readonly q = input<string>('');
  readonly pagina = input<string>('1');

  // El enrutador (withComponentInputBinding) fuerza estos inputs a `undefined` cuando el
  // query param no está en la URL, sin respetar el valor por defecto del input: estas señales
  // derivadas restauran ese valor por defecto para el resto del componente.
  protected readonly qActual = computed(() => this.q() ?? '');
  protected readonly paginaValor = computed(() => this.pagina() ?? '1');

  protected readonly evento = computed(() =>
    this.datos.web()?.eventos.find((e) => e.id === this.id()),
  );
  protected readonly migaEvento = computed(() => {
    const evento = this.evento();
    return evento ? ` / ${evento.titulo}` : '';
  });
  protected readonly subtituloEvento = computed(() => {
    const evento = this.evento();
    if (!evento) return '';
    return [formatoFechaLarga(evento.fechaHora), formatoHora12(evento.fechaHora), evento.lugar]
      .filter(Boolean)
      .join(' · ');
  });
  protected readonly asistentesEvento = computed(() => {
    const asistentes = this.datos.asistentes();
    return asistentes && asistentes.eventoId === this.id() ? asistentes : undefined;
  });
  protected readonly notaPrivacidad = computed(() => this.datos.asistentes()?.notaPrivacidad ?? '');

  // 16 asistentes · 4 por página = 4 páginas (D4: las páginas 3 y 4 son ASISTENTES_INVENTADOS).
  protected readonly totalPaginas = computed(() => (this.asistentesEvento() ? 4 : 1));
  protected readonly paginaActual = computed(() => {
    const numero = Number(this.paginaValor());
    return numero >= 1 && numero <= this.totalPaginas() ? numero : 1;
  });

  private paginaDe(numero: number): readonly Asistente[] {
    const asistentes = this.asistentesEvento();
    if (!asistentes) return [];
    switch (numero) {
      case 1:
        return asistentes.mostrados;
      case 2:
        return asistentes.pagina2;
      case 3:
        return ASISTENTES_INVENTADOS.slice(0, asistentes.porPagina);
      case 4:
        return ASISTENTES_INVENTADOS.slice(asistentes.porPagina, asistentes.porPagina * 2);
      default:
        return [];
    }
  }

  protected readonly filas = computed<Asistente[]>(() => {
    const asistentes = this.asistentesEvento();
    if (!asistentes) return [];
    const consulta = this.qActual().trim().toLowerCase();
    if (consulta) {
      const todos = [1, 2, 3, 4].flatMap((numero) => this.paginaDe(numero));
      return todos.filter((a) => a.alias.toLowerCase().includes(consulta));
    }
    return [...this.paginaDe(this.paginaActual())];
  });

  protected readonly textoPaginador = computed(() => {
    const asistentes = this.asistentesEvento();
    if (!asistentes) return '';
    const desde = (this.paginaActual() - 1) * asistentes.porPagina + 1;
    const hasta = Math.min(this.paginaActual() * asistentes.porPagina, asistentes.total);
    return `Mostrando ${desde}–${hasta} de ${asistentes.total} asistentes · ${asistentes.porPagina} por página`;
  });

  protected readonly formatoFecha = formatoFechaHoraCorta;

  protected buscar(texto: string): void {
    void this.router.navigate([], {
      queryParams: { q: texto || null, pagina: null },
      queryParamsHandling: 'merge',
    });
  }

  protected irAPagina(pagina: number): void {
    void this.router.navigate([], {
      queryParams: { pagina: pagina === 1 ? null : String(pagina) },
      queryParamsHandling: 'merge',
    });
  }
}
