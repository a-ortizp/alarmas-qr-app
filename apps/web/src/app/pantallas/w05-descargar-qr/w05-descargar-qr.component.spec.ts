import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

const botonConTexto = (raiz: HTMLElement, texto: string) =>
  Array.from(raiz.querySelectorAll('button, a')).find(
    (b) => b.textContent?.trim() === texto,
  ) as HTMLElement;

describe('W05 · Descargar QR', () => {
  it('muestra la miga, la lista de eventos preseleccionados (2 de 2) y la vista previa', async () => {
    const { raiz } = await abrir('/qr');
    const pagina = raiz.querySelector('[data-codigo="W05"]')!;
    expect(pagina.textContent).toContain('‹ Mis alarmas');
    expect(pagina.textContent).toContain('Seleccionar todos');
    expect(pagina.textContent).toContain('2 de 2');
    expect(pagina.textContent).toContain('Seminario UX');
    expect(pagina.textContent).toContain('Partido Sintética');
    expect(pagina.querySelector('aq-vista-previa-afiche')).not.toBeNull();
    const marcadas = pagina.querySelectorAll('input[type="checkbox"]:checked');
    expect(marcadas.length).toBe(3); // «Seleccionar todos» + 2 eventos
  });

  it('desmarcar un evento actualiza el contador y «Seleccionar todos»', async () => {
    const { raiz, estable } = await abrir('/qr');
    const pagina = raiz.querySelector('[data-codigo="W05"]')!;
    const casillas = Array.from(
      pagina.querySelectorAll('[data-evento] input'),
    ) as HTMLInputElement[];
    casillas[0].click();
    await estable();
    expect(pagina.textContent).toContain('1 de 2');
    expect((pagina.querySelector('[data-todos] input') as HTMLInputElement).checked).toBe(false);
  });

  it('«Cancelar» vuelve a W01 sin descargar', async () => {
    const { raiz, estable, router } = await abrir('/qr');
    botonConTexto(raiz, 'Cancelar').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('«Descargar» navega a W01 con ?estado=descarga-completada', async () => {
    const { raiz, estable, router } = await abrir('/qr');
    botonConTexto(raiz, 'Descargar').click();
    await estable();
    expect(router.url).toBe('/alarmas?estado=descarga-completada');
  });
});
