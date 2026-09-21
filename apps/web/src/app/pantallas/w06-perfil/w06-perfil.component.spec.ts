import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SesionService } from '../../datos/sesion.service';
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

describe('W06 · Ajustes de Perfil', () => {
  it('muestra perfil, privacidad y eliminación con los datos del usuario', async () => {
    const { raiz } = await abrir('/perfil');
    const pagina = raiz.querySelector('[data-codigo="W06"]')!;
    for (const texto of [
      'Ajustes de Perfil',
      'Perfil',
      'NOMBRES Y APELLIDOS',
      'ALIAS PÚBLICO',
      'CORREO ELECTRÓNICO',
      'Guardar cambios',
      'Privacidad ante organizadores',
      'Así apareces en "Quiénes escanearon" (Ley 1581).',
      'Mostrar el estado de mi alarma',
      'Contar mi "Ya voy" en las métricas',
      'Eliminación de cuenta',
      'Borra permanentemente tu perfil, eventos creados y el historial de escaneos.',
      'Eliminar mi cuenta',
      'Los cambios de perfil no afectan tus alarmas en el celular: en modo invitado siguen siendo locales.',
    ]) {
      expect(pagina.textContent).toContain(texto);
    }
    const valores = Array.from(pagina.querySelectorAll('aq-campo input')).map(
      (e) => (e as HTMLInputElement).value,
    );
    expect(valores).toEqual([
      dataset.usuario.nombre,
      dataset.usuario.aliasPublico,
      dataset.usuario.correo,
    ]);
    const activo = pagina.querySelector('aq-selector-segmentado [aria-pressed="true"]');
    expect(activo?.textContent?.trim()).toBe('Solo iniciales');
    const switches = Array.from(pagina.querySelectorAll('[role="switch"]')).map((s) =>
      s.getAttribute('aria-checked'),
    );
    expect(switches).toEqual(['true', 'true']);
    expect(raiz.querySelector('[data-item="ajustes"]')!.classList).toContain('activo');
  });

  it('«Guardar cambios» muestra «Perfil actualizado»', async () => {
    const { raiz, estable } = await abrir('/perfil');
    botonConTexto(raiz, 'Guardar cambios').click();
    await estable();
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.perfilActualizado);
  });

  it('«Eliminar mi cuenta» abre el modal con las consecuencias del dataset', async () => {
    const { raiz, estable, router } = await abrir('/perfil');
    botonConTexto(raiz, 'Eliminar mi cuenta').click();
    await estable();
    expect(router.url).toBe('/perfil/eliminar');
    const modal = raiz.querySelector('[data-estado="eliminar"]')!;
    const d = dataset.web.eliminarCuenta;
    for (const texto of [
      '¿Eliminar tu cuenta definitivamente?',
      'Esta acción no se puede deshacer. Al confirmar:',
      `Tus ${d.eventosPublicados} eventos publicados se despublican y sus QR dejan de funcionar.`,
      `Las ${d.alarmasDeAsistentes} alarmas de asistentes dejan de recibir actualizaciones (no se borran de sus celulares).`,
      `Tus datos personales se eliminan en máximo ${d.diasParaBorrado} días (Ley 1581 · habeas data).`,
      'Consejo: descarga antes tus reportes (Reportes → PDF / CSV).',
      'Escribe ELIMINAR para confirmar',
      'Conservar mi cuenta',
      'Eliminar definitivamente',
    ]) {
      expect(modal.textContent?.replace(/\s+/g, ' ')).toContain(texto);
    }
    expect((modal.querySelector('input') as HTMLInputElement).placeholder).toBe('ELIMINAR');
  });

  it('«Eliminar definitivamente» solo se habilita al escribir ELIMINAR', async () => {
    const { raiz, estable } = await abrir('/perfil/eliminar');
    const eliminar = botonConTexto(raiz, 'Eliminar definitivamente') as HTMLButtonElement;
    expect(eliminar.disabled).toBe(true);
    const entrada = raiz.querySelector('[data-estado="eliminar"] input') as HTMLInputElement;
    entrada.value = 'eliminar';
    entrada.dispatchEvent(new Event('input'));
    await estable();
    expect(eliminar.disabled).toBe(true);
    entrada.value = 'ELIMINAR';
    entrada.dispatchEvent(new Event('input'));
    await estable();
    expect(eliminar.disabled).toBe(false);
  });

  it('«Conservar mi cuenta», el velo y Escape vuelven a /perfil sin cerrar la sesión', async () => {
    const { raiz, estable, router } = await abrir('/perfil/eliminar');
    const sesion = TestBed.inject(SesionService);
    sesion.iniciar();
    botonConTexto(raiz, 'Conservar mi cuenta').click();
    await estable();
    expect(router.url).toBe('/perfil');
    await router.navigateByUrl('/perfil/eliminar');
    await estable();
    (raiz.querySelector('[data-estado="eliminar"] [data-velo]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/perfil');
    await router.navigateByUrl('/perfil/eliminar');
    await estable();
    raiz
      .querySelector('[data-estado="eliminar"] section')!
      .dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape', bubbles: true }));
    await estable();
    expect(router.url).toBe('/perfil');
    expect(sesion.iniciada()).toBe(true);
  });
});
