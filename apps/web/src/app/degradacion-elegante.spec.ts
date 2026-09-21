import { TestBed } from '@angular/core/testing';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from './app.routes';
import { cargarDataset, cortesFalsos, proveedoresPrueba } from '../testing/datos-prueba';

/** Degradación elegante fuera del marco 1280×820 (tokens breakpoint v1.12). */
async function abrir(url: string, colapsar: boolean, apilar: boolean) {
  const cortes = cortesFalsos(colapsar, apilar);
  TestBed.configureTestingModule({ providers: [...proveedoresPrueba(routes), cortes.proveedor] });
  const harness = await RouterTestingHarness.create(url);
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, cortes, estable: () => harness.fixture.whenStable() };
}

describe('Degradación elegante (cortes de ancho)', () => {
  it('bajo el corte la barra lateral arranca colapsada y se expande al superarlo', async () => {
    const { raiz, cortes, estable } = await abrir('/alarmas', true, false);
    const layout = raiz.querySelector('.layout')!;
    expect(layout.classList).toContain('colapsada');
    cortes.colapsarBarra.set(false);
    await estable();
    expect(layout.classList).not.toContain('colapsada');
  });

  it('entre cruces el control manual manda; el siguiente cruce vuelve a colapsar', async () => {
    const { raiz, cortes, estable } = await abrir('/alarmas', true, false);
    const layout = raiz.querySelector('.layout')!;
    (raiz.querySelector('[data-control-menu]') as HTMLElement).click();
    await estable();
    expect(layout.classList).not.toContain('colapsada');
    cortes.colapsarBarra.set(false);
    await estable();
    cortes.colapsarBarra.set(true);
    await estable();
    expect(layout.classList).toContain('colapsada');
  });

  it('a 1280 (sobre ambos cortes) W06 conserva sus dos columnas', async () => {
    const { raiz } = await abrir('/perfil', false, false);
    expect(raiz.querySelector('[data-codigo="W06"] .fila')!.hasAttribute('data-apilada')).toBe(
      false,
    );
  });

  it('bajo el corte de columnas W06 apila Perfil y la columna de Privacidad/Eliminación', async () => {
    const { raiz, cortes, estable } = await abrir('/perfil', true, true);
    const fila = raiz.querySelector('[data-codigo="W06"] .fila')!;
    expect(fila.hasAttribute('data-apilada')).toBe(true);
    cortes.apilarColumnas.set(false);
    await estable();
    expect(fila.hasAttribute('data-apilada')).toBe(false);
  });
});
