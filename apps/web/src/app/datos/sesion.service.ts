import { Injectable, signal } from '@angular/core';

/** Sesión simulada (sin autenticación real): W00 la inicia, el diálogo «¿Cerrar sesión?» la cierra. */
@Injectable({ providedIn: 'root' })
export class SesionService {
  private readonly _iniciada = signal(false);
  readonly iniciada = this._iniciada.asReadonly();
  iniciar(): void {
    this._iniciada.set(true);
  }
  cerrar(): void {
    this._iniciada.set(false);
  }
}
