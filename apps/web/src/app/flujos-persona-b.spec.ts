import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from './app.routes';
import { SnackbarService } from './componentes/snackbar/snackbar.service';
import { SesionService } from './datos/sesion.service';
import { cargarDataset, proveedoresPrueba } from '../testing/datos-prueba';
import dataset from '../../public/dataset.json';

async function iniciar(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  const estable = () => harness.fixture.whenStable();
  const tocarTexto = async (texto: string) => {
    const el = Array.from(raiz.querySelectorAll('button, a')).find(
      (b) => b.textContent?.trim() === texto,
    );
    (el as HTMLElement).click();
    await estable();
  };
  return { raiz, estable, tocarTexto, router: TestBed.inject(Router) };
}

describe('Flujos web de la Persona B (TRAZABILIDAD §4 T5)', () => {
  it('T5 · W01 → W03 → W04 → volver, y W01 → W05 → descarga completada', async () => {
    const { raiz, estable, tocarTexto, router } = await iniciar('/alarmas');
    TestBed.inject(SesionService).iniciar();
    expect(raiz.querySelector('[data-codigo="W01"]')).not.toBeNull();

    await tocarTexto('Ver detalle ›');
    expect(router.url).toContain('/eventos/');
    expect(raiz.querySelector('[data-codigo="W03"]')).not.toBeNull();

    await tocarTexto('Exportar reporte');
    expect(router.url).toBe('/reportes');
    expect(raiz.querySelector('[data-codigo="W04"]')).not.toBeNull();
    await tocarTexto('Generar y descargar');
    expect(raiz.querySelector('[data-codigo="W04"]')!.getAttribute('data-estado')).toBe('listo');

    await tocarTexto('‹ Mis alarmas / Reportes');
    expect(router.url).toBe('/alarmas');

    await tocarTexto('Descargar QR en lote');
    expect(router.url).toBe('/qr');
    expect(raiz.querySelector('[data-codigo="W05"]')).not.toBeNull();
    await tocarTexto('Descargar');
    expect(router.url).toBe('/alarmas?estado=descarga-completada');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.descargaCompletada);
  });
});
