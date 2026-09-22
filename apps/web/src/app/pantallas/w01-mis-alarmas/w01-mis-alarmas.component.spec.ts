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

describe('W01 · Mis Alarmas', () => {
  it('muestra los cuatro indicadores, la gráfica y los eventos propios y escaneados (pestaña Todos)', async () => {
    const { raiz } = await abrir('/alarmas');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.textContent).toContain(String(dataset.web.indicadores.eventosActivos.valor));
    expect(pagina.textContent).toContain(String(dataset.web.indicadores.escaneosTotales.valor));
    expect(pagina.querySelector('aq-grafica-barras')).not.toBeNull();
    const filas = pagina.querySelectorAll('tbody tr');
    expect(filas.length).toBe(dataset.web.eventos.length);
    expect(pagina.textContent).toContain('Seminario UX');
    expect(pagina.textContent).toContain('Partido Sintética');
  });

  it('pestaña Creados solo muestra eventos creados por mí', async () => {
    const { raiz } = await abrir('/alarmas?origen=creados');
    const filas = raiz.querySelectorAll('[data-codigo="W01"] tbody tr');
    expect(filas.length).toBe(1);
    expect(raiz.querySelector('[data-codigo="W01"]')!.textContent).toContain('Seminario UX');
  });

  it('filtro Pasados muestra los eventos finalizados sin acción «Ver detalle» (D3)', async () => {
    const { raiz } = await abrir('/alarmas?estado=pasados');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.textContent).toContain('Feria de empleo');
    expect(pagina.textContent).toContain('Taller de Figma');
    expect(pagina.querySelectorAll('tbody a[href*="/eventos/"]').length).toBe(0);
  });

  it('filtro Borradores muestra el estado vacío con «Ver todos»', async () => {
    const { raiz, estable, router } = await abrir('/alarmas?estado=borradores');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.querySelector('aq-estado-vacio')).not.toBeNull();
    expect(pagina.textContent).toContain('Sin resultados con estos filtros');
    botonConTexto(raiz, 'Ver todos').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('la búsqueda filtra por título y «Limpiar» vuelve al listado completo', async () => {
    const { raiz, estable, router } = await abrir('/alarmas?q=Sem');
    const pagina = raiz.querySelector('[data-codigo="W01"]')!;
    expect(pagina.querySelectorAll('tbody tr').length).toBe(1);
    expect(pagina.textContent).toContain('Seminario UX');
    botonConTexto(raiz, 'Limpiar').click();
    await estable();
    expect(router.url).toBe('/alarmas');
  });

  it('«Exportar reporte» y «Descargar QR en lote» navegan a W04 y W05', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    botonConTexto(raiz, 'Exportar reporte').click();
    await estable();
    expect(router.url).toBe('/reportes');
    await router.navigateByUrl('/alarmas');
    await estable();
    botonConTexto(raiz, 'Descargar QR en lote').click();
    await estable();
    expect(router.url).toBe('/qr');
  });

  it('«Ver detalle ›» navega a W03 con el id del evento', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    botonConTexto(raiz, 'Ver detalle ›').click();
    await estable();
    expect(router.url).toContain('/eventos/');
  });

  it('?estado=descarga-completada muestra el snackbar de descarga sin filtrar nada (D5)', async () => {
    const { raiz } = await abrir('/alarmas?estado=descarga-completada');
    expect(raiz.querySelectorAll('[data-codigo="W01"] tbody tr').length).toBe(
      dataset.web.eventos.length,
    );
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.descargaCompletada);
  });
});
