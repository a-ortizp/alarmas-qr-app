import { Component } from '@angular/core';
import { PantallaMarcadorComponent } from '../pantalla-marcador/pantalla-marcador.component';
import { pantallaPorCodigo } from '../../navegacion/pantallas';

@Component({
  selector: 'aq-w05-descargar-qr',
  imports: [PantallaMarcadorComponent],
  template: `<aq-pantalla-marcador [pantalla]="pantalla" />`,
})
export class W05DescargarQrComponent {
  readonly pantalla = pantallaPorCodigo('W05');
}
