import { Injectable, signal } from '@angular/core';

/** «After delay 3 s» de los marcos W00 correo enviado / cuenta eliminada y W06 actualizado (motion.snackbar-web). */
export const DURACION_SNACKBAR_WEB_MS = 3000;

/** Snackbar único de la web (D7): cualquier página muestra un aviso breve con `mostrar()`. */
@Injectable({ providedIn: 'root' })
export class SnackbarService {
  private readonly _mensaje = signal<string | null>(null);
  readonly mensaje = this._mensaje.asReadonly();
  private temporizador: ReturnType<typeof setTimeout> | undefined;

  mostrar(texto: string, duracionMs = DURACION_SNACKBAR_WEB_MS): void {
    clearTimeout(this.temporizador);
    this._mensaje.set(texto);
    this.temporizador = setTimeout(() => this._mensaje.set(null), duracionMs);
  }

  ocultar(): void {
    clearTimeout(this.temporizador);
    this._mensaje.set(null);
  }
}
