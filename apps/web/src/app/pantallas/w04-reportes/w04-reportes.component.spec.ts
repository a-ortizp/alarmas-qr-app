import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
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

describe('W04 · Reportes', () => {
  it('muestra la miga, el formulario y la tarjeta «Reportes generados»', async () => {
    const { raiz } = await abrir('/reportes');
    const pagina = raiz.querySelector('[data-codigo="W04"]')!;
    expect(pagina.textContent).toContain('‹ Mis alarmas');
    expect(pagina.textContent).toContain('Exportar reporte consolidado');
    expect(pagina.textContent).toContain('Último mes');
    expect(pagina.textContent).toContain('Reportes generados');
    for (const g of dataset.web.reporte.generados) {
      expect(pagina.textContent).toContain(g.archivo);
    }
    expect(pagina.textContent).toContain(dataset.web.reporte.nota);
  });

  it('«Rango personalizado» muestra los campos de fecha', async () => {
    const { raiz, estable } = await abrir('/reportes');
    botonConTexto(raiz, 'Rango personalizado').click();
    await estable();
    expect(raiz.querySelector('[data-codigo="W04"]')!.textContent).toContain('DESDE');
  });

  it('«Generar y descargar» muestra el estado Listo con el archivo generado, campos visibles y «Generar de nuevo» lo mantiene', async () => {
    const { raiz, estable } = await abrir('/reportes');
    botonConTexto(raiz, 'Generar y descargar').click();
    await estable();
    const pagina = raiz.querySelector('[data-codigo="W04"]')!;
    expect(pagina.getAttribute('data-estado')).toBe('listo');
    expect(pagina.textContent).toContain(
      `${dataset.web.reporte.archivoGenerado} generado y descargado exitosamente`,
    );
    expect(pagina.textContent).toContain('RANGO DE FECHAS');
    expect(pagina.textContent).toContain('FORMATO DE SALIDA');
    botonConTexto(raiz, 'Generar de nuevo').click();
    await estable();
    expect(pagina.getAttribute('data-estado')).toBe('listo');
  });

  it('«Descargar de nuevo» de un reporte ya generado muestra el snackbar de descarga', async () => {
    const { raiz, estable } = await abrir('/reportes');
    botonConTexto(raiz, 'Descargar de nuevo').click();
    await estable();
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.descargaCompletada);
  });

  it('la miga «‹ Mis alarmas» vuelve a W01', async () => {
    const { raiz, estable, router } = await abrir('/reportes');
    botonConTexto(raiz, '‹ Mis alarmas / Reportes').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });
});
