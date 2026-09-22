import { Component, computed, effect, inject, signal, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AqTarjetaComponent } from '../../componentes/tarjeta/aq-tarjeta.component';
import {
  AqSelectorSegmentadoComponent,
  OpcionSegmentada,
} from '../../componentes/selector-segmentado/aq-selector-segmentado.component';
import { AqChipComponent } from '../../componentes/chip/aq-chip.component';
import { AqVistaPreviaAficheComponent } from '../../componentes/vista-previa-afiche/aq-vista-previa-afiche.component';
import { AqQrDecorativoComponent } from '../../componentes/qr-decorativo/aq-qr-decorativo.component';
import { AqIconoComponent } from '../../componentes/icono/aq-icono.component';
import { AqBotonComponent } from '../../componentes/boton/aq-boton.component';
import { AqEnlaceComponent } from '../../componentes/enlace/aq-enlace.component';
import { DatosService } from '../../datos/datos.service';
import { formatoFechaHoraCorta } from '../../datos/formato-fecha';
import { CortesService } from '../../navegacion/cortes.service';

/** W05 · Descargar QR (F-W05): selección de eventos activos, formato PNG/PDF y vista previa del afiche. */
@Component({
  selector: 'aq-w05-descargar-qr',
  imports: [
    RouterLink,
    AqTarjetaComponent,
    AqSelectorSegmentadoComponent,
    AqChipComponent,
    AqVistaPreviaAficheComponent,
    AqQrDecorativoComponent,
    AqIconoComponent,
    AqBotonComponent,
    AqEnlaceComponent,
  ],
  template: `
    <section class="pagina" data-codigo="W05">
      <a aq-enlace variante="miga" routerLink="/alarmas">‹ Mis alarmas</a>
      <div class="encabezado">
        <h1 class="titulo">Descargar QR en lote</h1>
        <p class="subtitulo">
          Elige los eventos y descarga sus afiches con el QR listo para imprimir.
        </p>
      </div>

      @if (datos.web(); as web) {
        <div class="fila" [attr.data-apilada]="apilar() ? '' : null">
          <aq-tarjeta class="seleccion">
            <label class="fila-todos" data-todos>
              <span class="casilla" [attr.data-marcada]="todosSeleccionados() ? '' : null">
                <input
                  type="checkbox"
                  class="entrada"
                  [checked]="todosSeleccionados()"
                  (change)="alternarTodos()"
                />
                @if (todosSeleccionados()) {
                  <aq-icono nombre="check" tamano="casilla" />
                }
              </span>
              <span>Seleccionar todos</span>
              <span class="contador">{{ seleccionados().size }} de {{ web.eventos.length }}</span>
            </label>
            @for (evento of web.eventos; track evento.id) {
              <label class="fila-evento" [attr.data-evento]="evento.id">
                <span
                  class="casilla"
                  [attr.data-marcada]="seleccionados().has(evento.id) ? '' : null"
                >
                  <input
                    type="checkbox"
                    class="entrada"
                    [checked]="seleccionados().has(evento.id)"
                    (change)="alternar(evento.id)"
                  />
                  @if (seleccionados().has(evento.id)) {
                    <aq-icono nombre="check" tamano="casilla" />
                  }
                </span>
                <aq-qr-decorativo class="qr-fila" [etiqueta]="evento.titulo" />
                <span class="datos-evento">
                  <span class="nombre">{{ evento.titulo }}</span>
                  <span class="fecha">{{ formatoFecha(evento.fechaHora) }}</span>
                </span>
                <aq-chip variante="publicado">Publicado</aq-chip>
              </label>
            }
            <aq-selector-segmentado
              etiqueta="Formato"
              [opciones]="opcionesFormato"
              [(value)]="formato"
            />
            <div class="acciones">
              <a aq-boton variante="secundario" routerLink="/alarmas">Cancelar</a>
              <button
                aq-boton
                type="button"
                [disabled]="seleccionados().size === 0"
                (click)="descargar()"
              >
                Descargar
              </button>
            </div>
          </aq-tarjeta>
          <aq-vista-previa-afiche
            class="vista-previa"
            [nombreEvento]="nombrePrimerSeleccionado()"
          />
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
    .encabezado {
      display: flex;
      flex-direction: column;
      gap: var(--space-4);
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
    .fila {
      display: flex;
      align-items: flex-start;
      gap: var(--space-web-bloques);
    }
    .fila[data-apilada] {
      flex-direction: column;
      align-items: stretch;
    }
    .seleccion {
      flex: 0 0 var(--size-tarjeta-formulario-web);
      gap: var(--space-12);
    }
    .fila[data-apilada] .seleccion {
      flex: none;
    }
    .vista-previa {
      flex: 1;
      min-width: 0;
    }
    .fila-todos,
    .fila-evento {
      display: flex;
      align-items: center;
      gap: var(--space-10);
      padding: var(--space-8) 0;
      border-bottom: var(--stroke-borde-fino) solid var(--color-borde);
      cursor: pointer;
    }
    .casilla {
      position: relative;
      display: inline-flex;
      flex: none;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      width: var(--size-checkbox);
      height: var(--size-checkbox);
      border: var(--stroke-borde-fino) solid var(--color-borde);
      border-radius: var(--radius-casilla-web);
      background: var(--color-blanco);
      color: var(--color-blanco);
    }
    .casilla[data-marcada] {
      border-color: var(--color-tinta);
      background: var(--color-tinta);
    }
    .entrada {
      position: absolute;
      inset: 0;
      margin: 0;
      opacity: 0;
      cursor: pointer;
    }
    .qr-fila {
      box-sizing: border-box;
      width: var(--size-qr-miniatura-lista);
      height: var(--size-qr-miniatura-lista);
      padding: var(--space-4);
      border: var(--stroke-borde-fino) solid var(--color-tinta);
      border-radius: var(--radius-chip-web);
      background: var(--color-blanco);
      color: var(--color-tinta);
    }
    .datos-evento {
      display: flex;
      flex-direction: column;
      flex: 1;
      min-width: 0;
      gap: var(--space-2);
    }
    .nombre {
      font: var(--text-cuerpo-web);
      color: var(--color-texto);
    }
    .fecha {
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .contador {
      font: var(--text-nota-web);
      color: var(--color-texto-secundario);
    }
    .acciones {
      display: flex;
      justify-content: flex-end;
      gap: var(--space-12);
      padding-top: var(--space-8);
    }
  `,
})
export class W05DescargarQrComponent {
  protected readonly datos = inject(DatosService);
  private readonly router = inject(Router);
  protected readonly apilar = inject(CortesService).apilarColumnas;

  protected readonly opcionesFormato: readonly OpcionSegmentada[] = [
    { valor: 'png', texto: 'PNG' },
    { valor: 'pdf', texto: 'PDF' },
  ];
  protected readonly formato = signal('png');
  protected readonly seleccionados = signal(new Set<string>());
  protected readonly formatoFecha = formatoFechaHoraCorta;

  constructor() {
    effect(() => {
      const inicial = this.datos.descargaQR()?.seleccionados;
      if (inicial) untracked(() => this.seleccionados.set(new Set(inicial)));
    });
  }

  protected readonly todosSeleccionados = computed(() => {
    const total = this.datos.web()?.eventos.length ?? 0;
    return total > 0 && this.seleccionados().size === total;
  });

  protected readonly nombrePrimerSeleccionado = computed(() => {
    const eventos = this.datos.web()?.eventos ?? [];
    const primero = eventos.find((e) => this.seleccionados().has(e.id));
    return primero?.titulo ?? eventos[0]?.titulo ?? '';
  });

  protected alternar(id: string): void {
    const actuales = new Set(this.seleccionados());
    if (actuales.has(id)) actuales.delete(id);
    else actuales.add(id);
    this.seleccionados.set(actuales);
  }

  protected alternarTodos(): void {
    const eventos = this.datos.web()?.eventos ?? [];
    this.seleccionados.set(
      this.todosSeleccionados() ? new Set() : new Set(eventos.map((e) => e.id)),
    );
  }

  protected descargar(): void {
    void this.router.navigate(['/alarmas'], { queryParams: { estado: 'descarga-completada' } });
  }
}
