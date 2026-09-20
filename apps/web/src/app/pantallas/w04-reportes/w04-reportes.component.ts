import { Component } from '@angular/core';
import { PantallaMarcadorComponent } from '../pantalla-marcador/pantalla-marcador.component';
import { pantallaPorCodigo } from '../../navegacion/pantallas';

@Component({
  selector: 'aq-w04-reportes',
  imports: [PantallaMarcadorComponent],
  template: `<aq-pantalla-marcador [pantalla]="pantalla" />`,
})
export class W04ReportesComponent {
  readonly pantalla = pantallaPorCodigo('W04');
}
