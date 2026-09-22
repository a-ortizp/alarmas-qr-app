import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqTablaComponent } from './tabla/aq-tabla.component';
import { AqEstadoVacioComponent } from './estado-vacio/aq-estado-vacio.component';
import { AqPaginadorComponent } from './paginador/aq-paginador.component';

@Component({
  imports: [AqTablaComponent, AqEstadoVacioComponent, AqPaginadorComponent],
  template: `
    <table aq-tabla id="tabla">
      <thead>
        <tr>
          <th>Evento</th>
          <th>Escaneos</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Seminario UX</td>
          <td>12</td>
        </tr>
      </tbody>
    </table>
    <aq-estado-vacio
      id="vacio"
      titulo="Sin resultados con estos filtros"
      texto="No tienes borradores."
    >
      <button type="button" class="accion">Ver todos</button>
    </aq-estado-vacio>
    <aq-paginador
      id="pag"
      texto="Mostrando 1–4 de 16 asistentes · 4 por página"
      [paginaActual]="1"
      [totalPaginas]="2"
      (cambiar)="paginas.push($event)"
    />
  `,
})
class Anfitrion {
  readonly paginas: number[] = [];
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes de tablero L09 (2/2)', () => {
  it('aq-tabla no altera el contenido proyectado (solo estilo)', async () => {
    const f = await montar();
    const tabla = (f.nativeElement as HTMLElement).querySelector('#tabla')!;
    expect(tabla.querySelectorAll('th').length).toBe(2);
    expect(tabla.querySelector('td')!.textContent).toBe('Seminario UX');
  });

  it('aq-estado-vacio muestra título, texto y proyecta la acción', async () => {
    const f = await montar();
    const vacio = (f.nativeElement as HTMLElement).querySelector('#vacio')!;
    expect(vacio.textContent).toContain('Sin resultados con estos filtros');
    expect(vacio.textContent).toContain('No tienes borradores.');
    expect(vacio.querySelector('.accion')).not.toBeNull();
  });

  it('aq-paginador muestra el texto, las páginas y avisa al cambiar', async () => {
    const f = await montar();
    const raiz = f.nativeElement as HTMLElement;
    const pag = raiz.querySelector('#pag')!;
    expect(pag.textContent).toContain('Mostrando 1–4 de 16 asistentes · 4 por página');
    const activa = pag.querySelector('[aria-current="true"]')!;
    expect(activa.textContent?.trim()).toBe('1');
    (
      Array.from(pag.querySelectorAll('button')).find(
        (b) => b.textContent?.trim() === '2',
      ) as HTMLElement
    ).click();
    await f.whenStable();
    expect(f.componentInstance.paginas).toEqual([2]);
    (pag.querySelector('[data-siguiente]') as HTMLElement).click();
    expect(f.componentInstance.paginas).toEqual([2, 2]);
  });
});
