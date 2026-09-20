import { Component } from '@angular/core';
import { PantallaMarcadorComponent } from '../pantalla-marcador/pantalla-marcador.component';
import { pantallaPorCodigo } from '../../navegacion/pantallas';

@Component({
  selector: 'aq-w01-mis-alarmas',
  imports: [PantallaMarcadorComponent],
  template: `<aq-pantalla-marcador [pantalla]="pantalla" />`,
})
export class W01MisAlarmasComponent {
  readonly pantalla = pantallaPorCodigo('W01');
}
