import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { routes } from '../../app.routes';
import { SnackbarService } from '../../componentes/snackbar/snackbar.service';
import { cargarDataset, proveedoresPrueba } from '../../../testing/datos-prueba';
import dataset from '../../../../public/dataset.json';

async function abrir() {
  TestBed.configureTestingModule({ providers: proveedoresPrueba(routes) });
  const harness = await RouterTestingHarness.create('/login/recuperar');
  await cargarDataset();
  await harness.fixture.whenStable();
  const raiz = harness.fixture.nativeElement as HTMLElement;
  return { raiz, estable: () => harness.fixture.whenStable(), router: TestBed.inject(Router) };
}

const r = dataset.web.acceso.recuperar;

describe('W00 · Recuperar contraseña', () => {
  it('muestra título, explicación, campo, «Enviar enlace» y «Volver»', async () => {
    const { raiz } = await abrir();
    const pagina = raiz.querySelector('[data-codigo="W00"][data-estado="recuperar"]')!;
    expect(pagina).not.toBeNull();
    for (const texto of [r.titulo, r.texto, 'CORREO ELECTRÓNICO', r.boton, r.volver]) {
      expect(pagina.textContent).toContain(texto);
    }
    expect(pagina.querySelectorAll('input').length).toBe(1);
  });

  it('«Enviar enlace» vuelve a W00 con el aviso de correo enviado', async () => {
    const { raiz, estable, router } = await abrir();
    (raiz.querySelector('button[type="submit"]') as HTMLButtonElement).click();
    await estable();
    expect(router.url).toBe('/login?estado=correo-enviado');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(
      dataset.mensajes.correoRecuperacionEnviado,
    );
  });

  it('«‹ Volver a iniciar sesión» vuelve a /login', async () => {
    const { raiz, estable, router } = await abrir();
    const volver = Array.from(raiz.querySelectorAll('a')).find((a) =>
      a.textContent?.includes(r.volver),
    )!;
    volver.click();
    await estable();
    expect(router.url).toBe('/login');
  });
});
