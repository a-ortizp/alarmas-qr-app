import { DOCUMENT, DestroyRef, Injectable, Signal, inject, signal } from '@angular/core';

/**
 * Cortes de ancho web (tokens `breakpoint` v1.12–v1.13) como señales. Las variables CSS no funcionan dentro de
 * `@media`, así que el valor se lee de tokens.css en tiempo de ejecución y se consulta con matchMedia:
 * el corte vive en un solo lugar. Sin matchMedia (pruebas, SSR) todo queda como el marco 1280×820.
 */
@Injectable({ providedIn: 'root' })
export class CortesService {
  private readonly documento = inject(DOCUMENT);
  private readonly destruir = inject(DestroyRef);

  /** Ventana más angosta que `--breakpoint-web-colapsar-barra`: la barra lateral se colapsa sola. */
  readonly colapsarBarra = this.menorQue('--breakpoint-web-colapsar-barra');
  /** Ventana más angosta que `--breakpoint-web-apilar-columnas`: las páginas de dos columnas las apilan. */
  readonly apilarColumnas = this.menorQue('--breakpoint-web-apilar-columnas');
  /** Ventana más angosta que `--breakpoint-web-cajon`: la barra lateral deja el grid y se abre como cajón con ☰. */
  readonly cajon = this.menorQue('--breakpoint-web-cajon');
  /** Ventana más angosta que `--breakpoint-web-telefono`: rigen los tokens de toque (`data-ancho="telefono"`). */
  readonly telefono = this.menorQue('--breakpoint-web-telefono');

  private menorQue(token: string): Signal<boolean> {
    const ventana = this.documento.defaultView;
    const corte = ventana
      ?.getComputedStyle(this.documento.documentElement)
      .getPropertyValue(token)
      .trim();
    if (!ventana?.matchMedia || !corte) return signal(false).asReadonly();
    const consulta = ventana.matchMedia(`(width < ${corte})`);
    const estado = signal(consulta.matches);
    const alCambiar = (evento: MediaQueryListEvent) => estado.set(evento.matches);
    consulta.addEventListener('change', alCambiar);
    this.destruir.onDestroy(() => consulta.removeEventListener('change', alCambiar));
    return estado.asReadonly();
  }
}
