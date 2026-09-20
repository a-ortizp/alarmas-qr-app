import { Component } from '@angular/core';
import { PantallaMarcadorComponent } from '../pantalla-marcador/pantalla-marcador.component';
import { pantallaPorCodigo } from '../../navegacion/pantallas';

@Component({
  selector: 'aq-w03-detalle-evento',
  imports: [PantallaMarcadorComponent],
  template: `<aq-pantalla-marcador [pantalla]="pantalla" />`,
})
export class W03DetalleEventoComponent {
  readonly pantalla = pantallaPorCodigo('W03');
}
