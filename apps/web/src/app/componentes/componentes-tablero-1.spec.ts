import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqIndicadorComponent } from './indicador/aq-indicador.component';
import { AqChipComponent } from './chip/aq-chip.component';
import { AqPildorasComponent } from './pildoras/aq-pildoras.component';
import { AqCampoBusquedaComponent } from './campo-busqueda/aq-campo-busqueda.component';

@Component({
  imports: [AqIndicadorComponent, AqChipComponent, AqPildorasComponent, AqCampoBusquedaComponent],
  template: `
    <aq-indicador
      id="ind"
      [valor]="128"
      etiqueta="Escaneos totales"
      detalle="De tus eventos propios"
    />
    <aq-chip id="chip-origen" variante="creada-por-mi">Creada por mí</aq-chip>
    <aq-chip id="chip-estado" variante="eliminada">Eliminada</aq-chip>
    <aq-pildoras
      id="pildoras"
      [opciones]="opciones"
      [activo]="activo()"
      (elegir)="activo.set($event)"
    />
    <aq-campo-busqueda
      id="busqueda"
      ancho="w03"
      placeholder="Buscar asistente"
      [(value)]="texto"
      (buscar)="buscados.push($event)"
    />
  `,
})
class Anfitrion {
  readonly opciones = [
    { valor: 'todos', texto: 'Todos' },
    { valor: 'creados', texto: 'Creados' },
    { valor: 'escaneados', texto: 'Escaneados' },
  ];
  readonly activo = signal('todos');
  readonly texto = signal('');
  readonly buscados: string[] = [];
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes de tablero L09 (1/2)', () => {
  it('aq-indicador muestra valor, etiqueta y detalle', async () => {
    const f = await montar();
    const ind = (f.nativeElement as HTMLElement).querySelector('#ind')!;
    expect(ind.textContent).toContain('128');
    expect(ind.textContent).toContain('Escaneos totales');
    expect(ind.textContent).toContain('De tus eventos propios');
  });

  it('aq-chip aplica la variante de origen y de estado', async () => {
    const f = await montar();
    const raiz = f.nativeElement as HTMLElement;
    expect(raiz.querySelector('#chip-origen')!.classList).toContain('creada-por-mi');
    expect(raiz.querySelector('#chip-estado')!.classList).toContain('eliminada');
    expect(raiz.querySelector('#chip-origen')!.textContent?.trim()).toBe('Creada por mí');
  });

  it('aq-pildoras marca la activa y avisa al elegir otra', async () => {
    const f = await montar();
    const raiz = f.nativeElement as HTMLElement;
    const botones = Array.from(raiz.querySelectorAll('#pildoras button'));
    expect(
      botones.find((b) => b.textContent?.trim() === 'Todos')!.getAttribute('aria-pressed'),
    ).toBe('true');
    (botones.find((b) => b.textContent?.trim() === 'Creados') as HTMLElement).click();
    await f.whenStable();
    expect(f.componentInstance.activo()).toBe('creados');
  });

  it('aq-campo-busqueda refleja el texto y solo avisa al presionar Enter (D6)', async () => {
    const f = await montar();
    const entrada = (f.nativeElement as HTMLElement).querySelector(
      '#busqueda input',
    ) as HTMLInputElement;
    expect(entrada.placeholder).toBe('Buscar asistente');
    entrada.value = 'Mi';
    entrada.dispatchEvent(new Event('input'));
    await f.whenStable();
    expect(f.componentInstance.texto()).toBe('Mi');
    expect(f.componentInstance.buscados).toEqual([]);
    entrada.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));
    expect(f.componentInstance.buscados).toEqual(['Mi']);
  });
});
