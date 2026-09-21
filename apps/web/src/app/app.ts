import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AqSnackbarComponent } from './componentes/snackbar/aq-snackbar.component';

@Component({
  imports: [RouterOutlet, AqSnackbarComponent],
  selector: 'aq-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {}
