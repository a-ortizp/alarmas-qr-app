import { DOCUMENT } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { RouterTestingHarness } from '@angular/router/testing';
import { Router } from '@angular/router';
import { routes } from './app.routes';
import { App } from './app';
import { cargarDataset, cortesFalsos, proveedoresPrueba } from '../testing/datos-prueba';

/** Web responsive (tokens breakpoint v1.13): cajón bajo 900 y tokens de toque bajo 600. */
async function abrir(url: string, cajon: boolean, telefono = false) {
  const cortes = cortesFalsos(true, true, cajon, telefono);
  TestBed.configureTestingModule({ providers: [...proveedoresPrueba(routes), cortes.proveedor] });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return {
    raiz,
    cortes,
    router: TestBed.inject(Router),
    estable: () => harness.fixture.whenStable(),
  };
}

const cajonAbierto = (raiz: HTMLElement) => raiz.querySelector('#menu-cajon');

describe('Web responsive · cajón (bajo 900)', () => {
  it('sobre el corte no hay ☰ y la barra lateral está en el grid', async () => {
    const { raiz } = await abrir('/alarmas', false);
    expect(raiz.querySelector('[data-abrir-menu]')).toBeNull();
    expect(raiz.querySelector('.layout aq-barra-lateral')).not.toBeNull();
  });

  it('bajo el corte la barra lateral sale del grid y ☰ la abre como cajón expandido', async () => {
    const { raiz, estable } = await abrir('/alarmas', true);
    expect(raiz.querySelector('.layout')!.classList).toContain('cajon');
    expect(raiz.querySelector('.layout aq-barra-lateral')).toBeNull();
    const abrirMenu = raiz.querySelector('[data-abrir-menu]') as HTMLButtonElement;
    expect(abrirMenu.getAttribute('aria-expanded')).toBe('false');
    abrirMenu.click();
    await estable();
    const cajon = cajonAbierto(raiz)!;
    expect(cajon.getAttribute('role')).toBe('dialog');
    expect(abrirMenu.getAttribute('aria-expanded')).toBe('true');
    // Siempre expandido aunque la ventana esté bajo el corte de colapso: textos visibles, sin aria-label.
    expect(cajon.querySelector('.barra')!.classList).not.toContain('colapsada');
    expect(cajon.querySelector('[data-control-menu]')!.getAttribute('aria-label')).toBe(
      'Cerrar menú',
    );
  });

  it('el velo, Escape y «Cerrar menú» cierran el cajón', async () => {
    const { raiz, estable } = await abrir('/alarmas', true);
    const abrirMenu = raiz.querySelector('[data-abrir-menu]') as HTMLElement;

    abrirMenu.click();
    await estable();
    (raiz.querySelector('[data-velo-menu]') as HTMLElement).click();
    await estable();
    expect(cajonAbierto(raiz)).toBeNull();

    abrirMenu.click();
    await estable();
    cajonAbierto(raiz)!.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
    await estable();
    expect(cajonAbierto(raiz)).toBeNull();

    abrirMenu.click();
    await estable();
    (cajonAbierto(raiz)!.querySelector('[data-control-menu]') as HTMLElement).click();
    await estable();
    expect(cajonAbierto(raiz)).toBeNull();
  });

  it('elegir un ítem navega y cierra el cajón', async () => {
    const { raiz, estable, router } = await abrir('/alarmas', true);
    (raiz.querySelector('[data-abrir-menu]') as HTMLElement).click();
    await estable();
    (cajonAbierto(raiz)!.querySelector('[data-item="ajustes"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/perfil');
    expect(cajonAbierto(raiz)).toBeNull();
  });

  it('«Cerrar Sesión» desde el cajón lo cierra y abre el diálogo de confirmación', async () => {
    const { raiz, estable, router } = await abrir('/alarmas', true);
    (raiz.querySelector('[data-abrir-menu]') as HTMLElement).click();
    await estable();
    (cajonAbierto(raiz)!.querySelector('[data-item="cerrar-sesion"]') as HTMLElement).click();
    await estable();
    expect(router.url).toBe('/alarmas?dialogo=cerrar-sesion');
    expect(cajonAbierto(raiz)).toBeNull();
    expect(raiz.querySelector('aq-dialogo-confirmacion')).not.toBeNull();
  });

  it('al superar el corte con el cajón abierto vuelve la barra lateral fija', async () => {
    const { raiz, estable, cortes } = await abrir('/alarmas', true);
    (raiz.querySelector('[data-abrir-menu]') as HTMLElement).click();
    await estable();
    cortes.cajon.set(false);
    await estable();
    expect(cajonAbierto(raiz)).toBeNull();
    expect(raiz.querySelector('.layout aq-barra-lateral')).not.toBeNull();
  });
});

describe('Web responsive · teléfono (bajo 600)', () => {
  it('la raíz lleva data-ancho="telefono" solo bajo el corte (tokens de toque)', async () => {
    const cortes = cortesFalsos(true, true, true, true);
    TestBed.configureTestingModule({
      imports: [App],
      providers: [...proveedoresPrueba(routes), cortes.proveedor],
    });
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const html = TestBed.inject(DOCUMENT).documentElement;
    expect(html.getAttribute('data-ancho')).toBe('telefono');
    cortes.telefono.set(false);
    await fixture.whenStable();
    expect(html.hasAttribute('data-ancho')).toBe(false);
  });

  it('el nombre del usuario queda solo para lectores de pantalla', async () => {
    const { raiz } = await abrir('/alarmas', true, true);
    const nombre = raiz.querySelector('[data-avatar] span')!;
    expect(nombre.classList).toContain('solo-lector');
    expect(nombre.textContent).toContain('Andrés Rojas');
  });

  it('el modal «Eliminar cuenta» apila sus dos acciones', async () => {
    const { raiz, cortes, estable } = await abrir('/perfil/eliminar', true, true);
    const acciones = raiz.querySelector('[data-estado="eliminar"] .acciones')!;
    expect(acciones.hasAttribute('data-apiladas')).toBe(true);
    cortes.telefono.set(false);
    await estable();
    expect(acciones.hasAttribute('data-apiladas')).toBe(false);
  });
});
