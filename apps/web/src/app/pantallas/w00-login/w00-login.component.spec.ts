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
  return {
    harness,
    raiz,
    estable: () => harness.fixture.whenStable(),
    router: TestBed.inject(Router),
  };
}

describe('W00 · Inicio de sesión', () => {
  it('muestra la tarjeta de acceso sin barra lateral', async () => {
    const { raiz } = await abrir('/login');
    const pagina = raiz.querySelector('[data-codigo="W00"]')!;
    expect(pagina).not.toBeNull();
    expect(raiz.querySelector('aq-barra-lateral')).toBeNull();
    for (const texto of [
      'Alarmas QR',
      'Administración y consulta de tus eventos y alarmas',
      'CORREO ELECTRÓNICO',
      'CONTRASEÑA',
      'Iniciar sesión',
      '¿Olvidaste tu contraseña?',
      'Entra cualquier usuario registrado en el sistema.',
      '¿Aún no tienes cuenta? Créala desde la app móvil al registrarte.',
    ]) {
      expect(pagina.textContent).toContain(texto);
    }
    const entradas = pagina.querySelectorAll('input');
    expect(entradas[0].placeholder).toBe('nombre@correo.com');
    expect(entradas[1].type).toBe('password');
  });

  it('⏩ el foco en la contraseña muestra el error de credenciales', async () => {
    const { raiz, estable } = await abrir('/login');
    const contrasena = raiz.querySelectorAll('input')[1] as HTMLInputElement;
    contrasena.dispatchEvent(new FocusEvent('focus'));
    await estable();
    expect(raiz.querySelector('[data-codigo="W00"]')!.getAttribute('data-estado')).toBe('error');
    expect(contrasena.getAttribute('aria-invalid')).toBe('true');
    expect(raiz.textContent).toContain(dataset.web.acceso.errorCredenciales);
  });

  it('T5 · «Iniciar sesión» inicia la sesión y lleva a Mis Alarmas', async () => {
    const { raiz, estable, router } = await abrir('/login');
    (raiz.querySelector('button[type="submit"]') as HTMLButtonElement).click();
    await estable();
    expect(router.url).toBe('/alarmas');
    expect(TestBed.inject(SesionService).iniciada()).toBe(true);
    expect(raiz.querySelector('[data-codigo="W01"]')).not.toBeNull();
    expect(raiz.querySelector('aq-barra-lateral')).not.toBeNull();
  });

  it('?estado=eliminada muestra «Cuenta eliminada exitosamente»', async () => {
    await abrir('/login?estado=eliminada');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(dataset.mensajes.cuentaEliminada);
  });

  it('?estado=correo-enviado muestra el aviso del correo de recuperación', async () => {
    await abrir('/login?estado=correo-enviado');
    expect(TestBed.inject(SnackbarService).mensaje()).toBe(
      dataset.mensajes.correoRecuperacionEnviado,
    );
  });

  it('«¿Olvidaste tu contraseña?» abre Recuperar contraseña', async () => {
    const { raiz, estable, router } = await abrir('/login');
    const enlace = Array.from(raiz.querySelectorAll('a')).find((a) =>
      a.textContent?.includes('¿Olvidaste tu contraseña?'),
    )!;
    enlace.click();
    await estable();
    expect(router.url).toBe('/login/recuperar');
  });
});
