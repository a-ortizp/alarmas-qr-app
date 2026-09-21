import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { form, FormField } from '@angular/forms/signals';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { AqBotonComponent } from './boton/aq-boton.component';
import { AqEnlaceComponent } from './enlace/aq-enlace.component';
import { AqIconoComponent } from './icono/aq-icono.component';
import { ICONOS } from './icono/iconos';
import { AqCampoComponent } from './campo/aq-campo.component';
import { AqTarjetaComponent } from './tarjeta/aq-tarjeta.component';
import { AqTarjetaAccesoComponent } from './tarjeta-acceso/aq-tarjeta-acceso.component';

@Component({
  imports: [
    AqBotonComponent,
    AqEnlaceComponent,
    AqIconoComponent,
    AqCampoComponent,
    AqTarjetaComponent,
    AqTarjetaAccesoComponent,
    FormField,
  ],
  template: `
    <button aq-boton id="primario" type="button">Guardar cambios</button>
    <button aq-boton id="destructivo" variante="destructivo" bloque type="button" disabled>
      Eliminar
    </button>
    <a aq-enlace id="enlace" href="/login/recuperar">¿Olvidaste tu contraseña?</a>
    <aq-icono id="icono" nombre="alarma" />
    <aq-campo
      id="campo"
      etiqueta="CORREO ELECTRÓNICO"
      placeholder="nombre@correo.com"
      [(value)]="correo"
      [error]="error()"
      [mensajeError]="error() ? 'Correo o contraseña incorrectos.' : ''"
      (enfocado)="enfoques = enfoques + 1"
    />
    <aq-campo id="con-formulario" etiqueta="ALIAS PÚBLICO" [formField]="formulario.alias" />
    <aq-tarjeta id="peligro" variante="peligro"><p>Eliminación de cuenta</p></aq-tarjeta>
    <aq-tarjeta-acceso id="acceso"><p class="proyectado">Administración</p></aq-tarjeta-acceso>
  `,
})
class Anfitrion {
  readonly correo = signal('andres@correo.com');
  readonly error = signal(false);
  readonly formulario = form(signal({ alias: 'Andrés R.' }));
  enfoques = 0;
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('Componentes base L09', () => {
  it('aq-boton aplica variante y ancho completo', async () => {
    const f = await montar();
    const raiz: HTMLElement = f.nativeElement;
    expect(raiz.querySelector('#primario')!.classList).toContain('primario');
    const destructivo = raiz.querySelector('#destructivo')!;
    expect(destructivo.classList).toContain('destructivo');
    expect(destructivo.classList).toContain('bloque');
  });

  it('aq-enlace es un enlace nativo con su texto', async () => {
    const f = await montar();
    const enlace = (f.nativeElement as HTMLElement).querySelector('#enlace') as HTMLAnchorElement;
    expect(enlace.tagName).toBe('A');
    expect(enlace.textContent?.trim()).toBe('¿Olvidaste tu contraseña?');
  });

  it('aq-icono dibuja los trazados del DS en currentColor y oculto al lector', async () => {
    const f = await montar();
    const svg = (f.nativeElement as HTMLElement).querySelector('#icono svg')!;
    expect(svg.getAttribute('aria-hidden')).toBe('true');
    expect(svg.getAttribute('viewBox')).toBe('0 0 24 24');
    expect(svg.querySelectorAll('path').length).toBe(ICONOS.alarma.formas.length);
  });

  it('aq-icono y aq-logotipo marcan el tamaño en data-tamano, no en una clase que choque con .barra del padre', async () => {
    const f = await montar();
    const icono = (f.nativeElement as HTMLElement).querySelector('#icono')!;
    expect(icono.getAttribute('data-tamano')).toBe('barra');
    expect(icono.classList).not.toContain('barra');
    const logotipo = (f.nativeElement as HTMLElement).querySelector('#acceso aq-logotipo')!;
    expect(logotipo.getAttribute('data-tamano')).toBe('acceso');
    expect(logotipo.classList).not.toContain('acceso');
  });

  it('cada icono tiene caja 16 o 24 y al menos un trazado', () => {
    for (const [nombre, def] of Object.entries(ICONOS)) {
      expect([16, 24], nombre).toContain(def.caja);
      expect(def.formas.length, nombre).toBeGreaterThan(0);
    }
  });

  it('aq-campo refleja el modelo, lo actualiza al escribir y avisa el foco', async () => {
    const f = await montar();
    const anfitrion = f.componentInstance;
    const campo = (f.nativeElement as HTMLElement).querySelector('#campo')!;
    const entrada = campo.querySelector('input')!;
    expect(campo.textContent).toContain('CORREO ELECTRÓNICO');
    expect(entrada.value).toBe('andres@correo.com');
    expect(entrada.placeholder).toBe('nombre@correo.com');
    entrada.value = 'ana@correo.com';
    entrada.dispatchEvent(new Event('input'));
    expect(anfitrion.correo()).toBe('ana@correo.com');
    entrada.dispatchEvent(new FocusEvent('focus'));
    expect(anfitrion.enfoques).toBe(1);
  });

  it('aq-campo en error: borde de error, aria-invalid y mensaje enlazado', async () => {
    const f = await montar();
    f.componentInstance.error.set(true);
    await f.whenStable();
    const campo = (f.nativeElement as HTMLElement).querySelector('#campo')!;
    const entrada = campo.querySelector('input')!;
    expect(campo.querySelector('.caja')!.classList).toContain('error');
    expect(entrada.getAttribute('aria-invalid')).toBe('true');
    const mensaje = campo.querySelector('.mensaje-error')!;
    expect(mensaje.textContent).toContain('Correo o contraseña incorrectos.');
    expect(entrada.getAttribute('aria-describedby')).toBe(mensaje.id);
  });

  it('aq-campo funciona con Signal Forms ([formField])', async () => {
    const f = await montar();
    const entrada = (f.nativeElement as HTMLElement).querySelector(
      '#con-formulario input',
    ) as HTMLInputElement;
    expect(entrada.value).toBe('Andrés R.');
    entrada.value = 'Andrés';
    entrada.dispatchEvent(new Event('input'));
    expect(f.componentInstance.formulario.alias().value()).toBe('Andrés');
  });

  it('aq-tarjeta peligro y aq-tarjeta-acceso con marca y contenido proyectado', async () => {
    const f = await montar();
    const raiz: HTMLElement = f.nativeElement;
    expect(raiz.querySelector('#peligro')!.classList).toContain('peligro');
    const acceso = raiz.querySelector('#acceso')!;
    expect(acceso.querySelector('aq-logotipo')).not.toBeNull();
    expect(acceso.textContent).toContain('Alarmas QR');
    expect(acceso.querySelector('.proyectado')?.textContent).toBe('Administración');
  });
});
