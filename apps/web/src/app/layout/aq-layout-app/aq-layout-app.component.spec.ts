import { TestBed } from '@angular/core/testing';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SesionService } from '../../datos/sesion.service';
import { ITEM_CERRAR_SESION, ITEMS_BARRA_LATERAL } from '../../navegacion/barra-lateral';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir(url: string) {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  const estable = () => harness.fixture.whenStable();
  return { harness, raiz, estable, router: TestBed.inject(Router) };
}

describe('Layout con barra lateral (L09)', () => {
  it('los ítems de la barra coinciden con dataset.web.barraLateral', () => {
    const items = [...ITEMS_BARRA_LATERAL, ITEM_CERRAR_SESION].map((i) => ({
      id: i.id,
      icono: i.icono,
    }));
    expect(items).toEqual(dataset.web.barraLateral.items);
  });

  it('barra superior con el usuario y barra lateral con el ítem activo', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    expect(raiz.querySelector('aq-barra-superior')?.textContent).toContain('Alarmas QR');
    expect(raiz.querySelector('[data-avatar]')?.textContent).toContain('Andrés Rojas');
    const textos = Array.from(raiz.querySelectorAll('[data-item]')).map((e) =>
      e.textContent?.trim(),
    );
    expect(textos).toEqual([
      'Mis Alarmas',
      'Reportes',
      'Descargar QR',
      'Ajustes de Perfil',
      'Cerrar Sesión',
    ]);
    const activo = raiz.querySelector('[data-item="mis-alarmas"]')!;
    expect(activo.classList).toContain('activo');
    expect(activo.getAttribute('aria-current')).toBe('page');
    (raiz.querySelector('[data-avatar]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/perfil');
  });

  it('colapsar deja solo los iconos, con nombre accesible, y se mantiene al navegar', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    const control = raiz.querySelector('[data-control-menu]') as HTMLButtonElement;
    expect(control.getAttribute('aria-label')).toBe('Colapsar menú');
    control.click();
    await estable();
    expect(raiz.querySelector('.layout')!.classList).toContain('colapsada');
    expect(control.getAttribute('aria-label')).toBe('Expandir menú');
    expect(raiz.querySelector('[data-item="reportes"]')!.getAttribute('aria-label')).toBe(
      'Reportes',
    );
    (raiz.querySelector('[data-item="reportes"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/reportes');
    expect(raiz.querySelector('.layout')!.classList).toContain('colapsada');
  });

  it('«Cerrar Sesión» abre el diálogo sobre la página actual y el velo lo cierra', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    (raiz.querySelector('[data-item="cerrar-sesion"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/alarmas?dialogo=cerrar-sesion');
    const dialogo = raiz.querySelector('aq-dialogo-confirmacion')!;
    expect(dialogo.textContent).toContain('¿Cerrar sesión?');
    expect(dialogo.textContent).toContain(dataset.mensajes.confirmarCerrarSesionCuerpoWeb);
    (dialogo.querySelector('[data-velo]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/alarmas');
    expect(raiz.querySelector('aq-dialogo-confirmacion')).toBeNull();
  });

  it('cerrar el diálogo no agrega entrada al historial: atrás no vuelve a abrirlo', async () => {
    const { raiz, estable, router } = await abrir('/alarmas');
    const location = TestBed.inject(Location);
    (raiz.querySelector('[data-item="cerrar-sesion"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/alarmas?dialogo=cerrar-sesion');
    (raiz.querySelector('[data-velo]') as HTMLElement).click();
    await estable();
    expect(location.path()).toBe('/alarmas');
    location.back();
    await estable();
    expect(location.path()).not.toContain('dialogo=cerrar-sesion');
  });

  it('«Cerrar sesión» en el diálogo cierra la sesión y vuelve a /login', async () => {
    const { raiz, estable, router } = await abrir('/perfil?dialogo=cerrar-sesion');
    const sesion = TestBed.inject(SesionService);
    sesion.iniciar();
    (
      raiz.querySelector('aq-dialogo-confirmacion [data-accion="confirmar"]') as HTMLElement
    ).click();
    await estable();
    expect(router.url).toBe('/login');
    expect(sesion.iniciada()).toBe(false);
  });
});
