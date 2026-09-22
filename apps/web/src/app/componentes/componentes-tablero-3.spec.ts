import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqGraficaBarrasComponent } from './grafica-barras/aq-grafica-barras.component';
import { AqVistaPreviaAficheComponent } from './vista-previa-afiche/aq-vista-previa-afiche.component';
import dataset from '../../../public/dataset.json';

@Component({
  imports: [AqGraficaBarrasComponent, AqVistaPreviaAficheComponent],
  template: `
    <aq-grafica-barras id="grafica" [datos]="datos" />
    <aq-vista-previa-afiche id="afiche" nombreEvento="Seminario UX" />
  `,
})
class Anfitrion {
  readonly datos = dataset.web.escaneosPorSemana;
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes de tablero L09 (3/3)', () => {
  it('aq-grafica-barras dibuja una barra por semana con su etiqueta y la semana actual resaltada', async () => {
    const f = await montar();
    const grafica = (f.nativeElement as HTMLElement).querySelector('#grafica')!;
    expect(grafica.textContent).toContain('Escaneos por semana');
    expect(grafica.querySelectorAll('.columna').length).toBe(8);
    expect(grafica.textContent).toContain('24 ago');
    const actual = grafica.querySelector('.columna.actual')!;
    expect(actual.textContent).toContain('26');
    expect(grafica.textContent).toContain('Datos agregados y anónimos');
  });

  it('la barra más alta corresponde al valor máximo (24 ago, 26 escaneos)', async () => {
    const f = await montar();
    const barras = Array.from(
      (f.nativeElement as HTMLElement).querySelectorAll('#grafica .barra'),
    ) as SVGRectElement[];
    const alturas = barras.map((b) => Number(b.getAttribute('height')));
    expect(Math.max(...alturas)).toBe(alturas[alturas.length - 1]);
  });

  it('aq-vista-previa-afiche muestra la marca, el nombre del evento y la regla de tamaño', async () => {
    const f = await montar();
    const afiche = (f.nativeElement as HTMLElement).querySelector('#afiche')!;
    expect(afiche.textContent).toContain('Alarmas QR');
    expect(afiche.textContent).toContain('Seminario UX');
    expect(afiche.textContent).toContain(
      'Escaneálo y te avisamos'.length > 0 ? 'Escanéalo y te avisamos' : '',
    );
    expect(afiche.textContent).toContain('VISTA PREVIA DEL AFICHE');
    expect(afiche.textContent).toContain('QR mínimo 4 × 4 cm');
  });
});
