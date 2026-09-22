import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

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

describe('W03 · Detalle Evento', () => {
  it('muestra la miga, el título, los indicadores del evento y los asistentes (página 1)', async () => {
    const { raiz } = await abrir('/eventos/w-partido');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    expect(pagina.textContent).toContain('‹ Mis alarmas');
    expect(pagina.textContent).toContain('Partido Sintética');
    expect(pagina.textContent).toContain('16'); // escaneos
    expect(pagina.textContent).toContain('11'); // alarmas activas
    expect(pagina.textContent).toContain('7'); // ya voy
    const filas = pagina.querySelectorAll('tbody tr');
    expect(filas.length).toBe(4);
    expect(pagina.textContent).toContain('Joale7');
    expect(pagina.textContent).toContain(dataset.web.asistentes.notaPrivacidad);
    expect(pagina.textContent).not.toContain('@'); // ningún correo
  });

  it('la miga «‹ Mis alarmas / Partido Sintética» vuelve a W01', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido');
    botonConTexto(raiz, '‹ Mis alarmas / Partido Sintética').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('el paginador navega a la página 2 con LauM, Nico_R, D.G, Vale22', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    (
      Array.from(pagina.querySelectorAll('button')).find(
        (b) => b.textContent?.trim() === '2',
      ) as HTMLElement
    ).click();
    await estable();
    expect(router.url).toBe('/eventos/w-partido?pagina=2');
    expect(pagina.textContent).toContain('LauM');
    expect(pagina.textContent).toContain('Vale22');
  });

  it('el paginador llega a 4 páginas (16 asistentes) y «Confirmó "Ya voy"» siempre muestra «—»', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    expect(pagina.textContent).toContain('Mostrando 1–4 de 16 asistentes · 4 por página');
    expect(
      Array.from(pagina.querySelectorAll('tbody td:last-child')).every(
        (td) => td.textContent?.trim() === '—',
      ),
    ).toBe(true);

    (
      Array.from(pagina.querySelectorAll('button')).find(
        (b) => b.textContent?.trim() === '4',
      ) as HTMLElement
    ).click();
    await estable();
    expect(router.url).toBe('/eventos/w-partido?pagina=4');
    expect(pagina.textContent).toContain('Mostrando 13–16 de 16 asistentes · 4 por página');
    const filas = pagina.querySelectorAll('tbody tr');
    expect(filas.length).toBe(4);
    expect(pagina.textContent).toContain('Sofi_B');
    expect(pagina.textContent).toContain('Dani21');
  });

  it('la búsqueda filtra por alias («Mi» → Mike1008)', async () => {
    const { raiz } = await abrir('/eventos/w-partido?q=Mi');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    const filas = pagina.querySelectorAll('tbody tr');
    expect(filas.length).toBe(1);
    expect(pagina.textContent).toContain('Mike1008');
  });

  it('un evento sin datos de asistentes (D4) muestra los indicadores y la tabla vacía', async () => {
    const { raiz } = await abrir('/eventos/w-seminario');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    expect(pagina.textContent).toContain('Seminario UX');
    expect(pagina.textContent).toContain('12'); // escaneos de w-seminario
    expect(pagina.querySelectorAll('tbody tr').length).toBe(0);
  });

  it('una búsqueda sin coincidencias (D4 no aplica: hay 16 asistentes) muestra «Limpiar», no el mensaje de evento sin datos', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido?q=zzz');
    const pagina = raiz.querySelector('[data-codigo="W03"]')!;
    expect(pagina.querySelectorAll('tbody tr').length).toBe(0);
    expect(pagina.textContent).not.toContain('Aún no hay asistentes registrados');
    expect(pagina.textContent).toContain('Sin resultados para "zzz"');
    const limpiar = botonConTexto(pagina as HTMLElement, 'Limpiar');
    expect(limpiar).toBeTruthy();
    limpiar.click();
    await estable();
    expect(router.url).toBe('/eventos/w-partido');
    expect(pagina.querySelectorAll('tbody tr').length).toBe(4);
    expect(pagina.textContent).toContain('Joale7');
  });

  it('«Exportar reporte» navega a W04', async () => {
    const { raiz, estable, router } = await abrir('/eventos/w-partido');
    botonConTexto(raiz, 'Exportar reporte').click();
    await estable();
    expect(router.url).toBe('/reportes');
  });
});
