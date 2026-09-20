import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { routes } from './app.routes';
import { PANTALLAS } from './navegacion/pantallas';

describe('rutas de TRAZABILIDAD §2', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [provideZonelessChangeDetection(), provideRouter(routes)],
    }),
  );

  it('declara las 6 páginas con su código', () => {
    expect(PANTALLAS.map((p) => p.codigo)).toEqual(['W00', 'W01', 'W03', 'W04', 'W05', 'W06']);
    expect(PANTALLAS.find((p) => p.codigo === 'W06')?.ruta).toBe('perfil');
  });

  it('/perfil muestra el marcador W06 dentro del layout con barra lateral', async () => {
    const harness = await RouterTestingHarness.create('/perfil');
    const html = harness.routeNativeElement?.ownerDocument.body.innerHTML ?? '';
    expect(html).toContain('data-codigo="W06"');
    expect(html).toContain('aq-barra-lateral');
  });

  it('/login no lleva barra lateral y / redirige a /login', async () => {
    const harness = await RouterTestingHarness.create('/');
    const html = harness.routeNativeElement?.ownerDocument.body.innerHTML ?? '';
    expect(html).toContain('data-codigo="W00"');
    expect(html).not.toContain('aq-barra-lateral');
  });
});
