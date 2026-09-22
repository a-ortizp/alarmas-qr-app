import { Component, computed, inject, input } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AqIndicadorComponent } from '../../componentes/indicador/aq-indicador.component';
import { AqCampoBusquedaComponent } from '../../componentes/campo-busqueda/aq-campo-busqueda.component';
import { AqTablaComponent } from '../../componentes/tabla/aq-tabla.component';
import { AqChipComponent } from '../../componentes/chip/aq-chip.component';
import { AqPaginadorComponent } from '../../componentes/paginador/aq-paginador.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { DatosService } from '../../datos/datos.service';
import { Asistente } from '../../datos/modelos';

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
  ],
  template: `
    <section class="pagina" data-codigo="W03">
      <a aq-enlace routerLink="/alarmas">‹ Mis alarmas</a>

      @if (evento(); as evento) {
        <header class="cabecera">
          <h1 class="titulo">{{ evento.titulo }}</h1>
          <a aq-boton variante="secundario" routerLink="/reportes">Exportar reporte</a>
        </header>

        <div class="indicadores">
          <aq-indicador [valor]="evento.escaneos" etiqueta="Escaneos" detalle="Total del evento" />
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
            etiqueta="«Ya voy»"
            detalle="Personas que avisaron que van en camino"
          />
        </div>

        <section class="tabla-asistentes">
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
              <table aq-tabla>
                <thead>
                  <tr>
                    <th>Alias</th>
                    <th>Escaneo</th>
                    <th>Alarma</th>
                    <th>«Ya voy»</th>
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
                      <td>{{ (fila.confirmoYaVoy ?? fila.yaVoy) ? 'Sí' : 'No' }}</td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
            <p class="privacidad">{{ asistentesEvento()?.notaPrivacidad }}</p>
            @if (!qActual()) {
              <aq-paginador
                [texto]="textoPaginador()"
                [paginaActual]="paginaActual()"
                [totalPaginas]="2"
                (cambiar)="irAPagina($event)"
              />
            }
          } @else {
            <p class="sin-asistentes">Aún no hay asistentes registrados para este evento.</p>
          }
        </section>
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
    .indicadores {
      display: flex;
      flex-wrap: wrap;
      gap: var(--space-web-indicadores);
    }
    .tabla-asistentes {
      display: flex;
      flex-direction: column;
      gap: var(--space-16);
      box-sizing: border-box;
      padding: var(--space-16);
      border: var(--stroke-borde) solid var(--color-borde);
      border-radius: var(--radius-tarjeta);
      background: var(--color-blanco);
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
    .privacidad,
    .sin-asistentes {
      margin: 0;
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
  protected readonly asistentesEvento = computed(() => {
    const asistentes = this.datos.asistentes();
    return asistentes && asistentes.eventoId === this.id() ? asistentes : undefined;
  });
  protected readonly paginaActual = computed(() => (this.paginaValor() === '2' ? 2 : 1));

  protected readonly filas = computed<Asistente[]>(() => {
    const asistentes = this.asistentesEvento();
    if (!asistentes) return [];
    const consulta = this.qActual().trim().toLowerCase();
    if (consulta) {
      return [...asistentes.mostrados, ...asistentes.pagina2].filter((a) =>
        a.alias.toLowerCase().includes(consulta),
      );
    }
    return this.paginaActual() === 2 ? asistentes.pagina2 : asistentes.mostrados;
  });

  protected readonly textoPaginador = computed(() => {
    const asistentes = this.asistentesEvento();
    if (!asistentes) return '';
    const desde = this.paginaActual() === 2 ? asistentes.porPagina + 1 : 1;
    const hasta = this.paginaActual() === 2 ? asistentes.porPagina * 2 : asistentes.porPagina;
    return `Mostrando ${desde}–${hasta} de ${asistentes.total} asistentes · ${asistentes.porPagina} por página`;
  });

  protected formatoFecha(iso: string): string {
    return new Intl.DateTimeFormat('es-CO', { day: 'numeric', month: 'short' }).format(
      new Date(iso),
    );
  }

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
