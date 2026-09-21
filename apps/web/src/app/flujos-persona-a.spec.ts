import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from './app.routes';
import { SesionService } from './datos/sesion.service';
import { SnackbarService } from './componentes/snackbar/snackbar.service';
import { cargarDataset, proveedoresPrueba } from '../testing/datos-prueba';
import dataset from '../../public/dataset.json';

async function iniciar(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  const estable = () => harness.fixture.whenStable();
  const tocar = async (selector: string) => {
    (raiz.querySelector(selector) as HTMLElement).click();
    await estable();
  };
  const tocarTexto = async (texto: string) => {
    const el = Array.from(raiz.querySelectorAll('button, a')).find(
      (b) => b.textContent?.trim() === texto,
    );
    (el as HTMLElement).click();
    await estable();
  };
  return { raiz, estable, tocar, tocarTexto, router: TestBed.inject(Router) };
}

describe('Flujos web de la Persona A (TRAZABILIDAD §3)', () => {
  it('T5 · tramo W00: entrar, ir a Ajustes por la barra y salir con el diálogo', async () => {
    const { raiz, tocar, tocarTexto, router } = await iniciar('/login');
    const sesion = TestBed.inject(SesionService);
    await tocar('button[type="submit"]');
    expect(router.url).toBe('/alarmas');
    expect(sesion.iniciada()).toBe(true);
    await tocar('[data-item="ajustes"]');
    expect(router.url).toBe('/perfil');
    expect(raiz.querySelector('[data-codigo="W06"]')).not.toBeNull();
    await tocar('[data-item="cerrar-sesion"]');
    expect(router.url).toBe('/perfil?dialogo=cerrar-sesion');
    await tocarTexto('Cancelar');
    expect(router.url).toBe('/perfil');
    await tocar('[data-item="cerrar-sesion"]');
    await tocar('aq-dialogo-confirmacion [data-accion="confirmar"]');
    expect(router.url).toBe('/login');
    expect(sesion.iniciada()).toBe(false);
    expect(raiz.querySelector('[data-codigo="W00"]')).not.toBeNull();
  });

  it('T8 · perfil → modal → escribir ELIMINAR → W00 con «Cuenta eliminada exitosamente»', async () => {
    const { raiz, estable, tocarTexto, router } = await iniciar('/perfil');
    await tocarTexto('Eliminar mi cuenta');
    expect(router.url).toBe('/perfil/eliminar');
    const entrada = raiz.querySelector('[data-estado="eliminar"] input') as HTMLInputElement;
    entrada.value = dataset.web.eliminarCuenta.palabraDeConfirmacion;
    entrada.dispatchEvent(new Event('input'));
    await estable();
    await tocarTexto('Eliminar definitivamente');
    expect(router.url).toBe('/login?estado=eliminada');
    expect(raiz.querySelector('[data-codigo="W00"]')).not.toBeNull();
    expect(TestBed.inject(SesionService).iniciada()).toBe(false);
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.cuentaEliminada);
  });
});
