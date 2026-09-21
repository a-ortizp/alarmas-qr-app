import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqModalComponent } from './modal/aq-modal.component';
import { AqDialogoConfirmacionComponent } from './dialogo-confirmacion/aq-dialogo-confirmacion.component';

@Component({
  imports: [AqModalComponent, AqDialogoConfirmacionComponent],
  template: `
    <aq-modal
      id="modal"
      titulo="¿Eliminar tu cuenta definitivamente?"
      (cerrar)="cierres.set(cierres() + 1)"
    >
      <p class="proyectado">Esta acción no se puede deshacer.</p>
      <button type="button">Conservar mi cuenta</button>
    </aq-modal>
    <aq-dialogo-confirmacion
      id="dialogo"
      titulo="¿Cerrar sesión?"
      cuerpo="Tus alarmas siguen activas en el celular."
      rotuloSeguro="Cancelar"
      rotuloAccion="Cerrar sesión"
      [destruye]="destruye()"
      (seguro)="seguros.set(seguros() + 1)"
      (confirmar)="confirmaciones.set(confirmaciones() + 1)"
    />
  `,
})
class Anfitrion {
  readonly cierres = signal(0);
  readonly seguros = signal(0);
  readonly confirmaciones = signal(0);
  readonly destruye = signal(false);
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

const escape = () => new KeyboardEvent('keydown', { key: 'Escape', bubbles: true });

describe('aq-modal', () => {
  it('es un diálogo modal titulado con icono de advertencia y el contenido proyectado', async () => {
    const f = await montar();
    const modal = (f.nativeElement as HTMLElement).querySelector('#modal')!;
    const seccion = modal.querySelector('section')!;
    expect(seccion.getAttribute('role')).toBe('dialog');
    expect(seccion.getAttribute('aria-modal')).toBe('true');
    const titulo = modal.querySelector('h2')!;
    expect(titulo.textContent).toBe('¿Eliminar tu cuenta definitivamente?');
    expect(seccion.getAttribute('aria-labelledby')).toBe(titulo.id);
    expect(modal.querySelector('aq-icono')).not.toBeNull();
    expect(modal.querySelector('.proyectado')?.textContent).toContain('no se puede deshacer');
  });

  it('el velo y Escape cierran', async () => {
    const f = await montar();
    const modal = (f.nativeElement as HTMLElement).querySelector('#modal')!;
    (modal.querySelector('[data-velo]') as HTMLElement).click();
    modal.querySelector('section')!.dispatchEvent(escape());
    expect(f.componentInstance.cierres()).toBe(2);
  });
});

describe('aq-dialogo-confirmacion', () => {
  it('muestra título, cuerpo y la acción segura como primario', async () => {
    const f = await montar();
    const dialogo = (f.nativeElement as HTMLElement).querySelector('#dialogo')!;
    const seccion = dialogo.querySelector('section')!;
    expect(seccion.getAttribute('role')).toBe('alertdialog');
    expect(dialogo.querySelector('h2')?.textContent).toBe('¿Cerrar sesión?');
    expect(dialogo.querySelector('p')?.textContent).toBe(
      'Tus alarmas siguen activas en el celular.',
    );
    const seguro = dialogo.querySelector('[data-accion="seguro"]')!;
    const confirmar = dialogo.querySelector('[data-accion="confirmar"]')!;
    expect(seguro.textContent?.trim()).toBe('Cancelar');
    expect(seguro.getAttribute('data-variante')).toBe('primario');
    expect(confirmar.getAttribute('data-variante')).toBe('secundario');
    f.componentInstance.destruye.set(true);
    await f.whenStable();
    expect(confirmar.getAttribute('data-variante')).toBe('destructivo');
  });

  it('velo, Escape y «Cancelar» son la acción segura; «Cerrar sesión» confirma', async () => {
    const f = await montar();
    const dialogo = (f.nativeElement as HTMLElement).querySelector('#dialogo')!;
    (dialogo.querySelector('[data-velo]') as HTMLElement).click();
    dialogo.querySelector('section')!.dispatchEvent(escape());
    (dialogo.querySelector('[data-accion="seguro"]') as HTMLElement).click();
    expect(f.componentInstance.seguros()).toBe(3);
    (dialogo.querySelector('[data-accion="confirmar"]') as HTMLElement).click();
    expect(f.componentInstance.confirmaciones()).toBe(1);
  });
});
