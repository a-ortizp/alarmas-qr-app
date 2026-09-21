import { Component, DOCUMENT, effect, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AqSnackbarComponent } from './componentes/snackbar/aq-snackbar.component';
import { CortesService } from './navegacion/cortes.service';

@Component({
  imports: [RouterOutlet, AqSnackbarComponent],
  selector: 'aq-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  constructor() {
    const raiz = inject(DOCUMENT).documentElement;
    const telefono = inject(CortesService).telefono;
    // Tokens v1.13: bajo --breakpoint-web-telefono tokens.css reemplaza medidas de puntero por las de toque.
    effect(() => {
      if (telefono()) raiz.setAttribute('data-ancho', 'telefono');
      else raiz.removeAttribute('data-ancho');
    });
  }
}
