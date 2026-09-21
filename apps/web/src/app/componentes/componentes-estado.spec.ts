import { Component, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { form, FormField } from '@angular/forms/signals';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { DURACION_SNACKBAR_WEB_MS, SnackbarService } from './snackbar/snackbar.service';
import { AqSnackbarComponent } from './snackbar/aq-snackbar.component';
import { AqSwitchComponent } from './switch/aq-switch.component';
import {
  AqSelectorSegmentadoComponent,
  OpcionSegmentada,
} from './selector-segmentado/aq-selector-segmentado.component';

@Component({
  imports: [AqSnackbarComponent, AqSwitchComponent, AqSelectorSegmentadoComponent, FormField],
  template: `
    <aq-snackbar />
    <aq-switch id="switch" etiqueta="Mostrar el estado de mi alarma" [(checked)]="mostrar" />
    <aq-switch id="switch-formulario" etiqueta="Contar mi voto" [formField]="formulario.contar" />
    <aq-selector-segmentado
      id="selector"
      etiqueta="Cómo apareces"
      [opciones]="opciones"
      [(value)]="aparicion"
    />
  `,
})
class Anfitrion {
  readonly mostrar = signal(true);
  readonly aparicion = signal('solo-iniciales');
  readonly formulario = form(signal({ contar: false }));
  readonly opciones: readonly OpcionSegmentada[] = [
    { valor: 'nombre-completo', texto: 'Nombre completo' },
    { valor: 'solo-iniciales', texto: 'Solo iniciales' },
    { valor: 'alias', texto: 'Alias' },
  ];
}

async function montar() {
  TestBed.configureTestingModule({ imports: [Anfitrion], providers: proveedoresPrueba() });
  const fixture = TestBed.createComponent(Anfitrion);
  await fixture.whenStable();
  return fixture;
}

describe('SnackbarService', () => {
  afterEach(() => vi.useRealTimers());

  it('muestra un mensaje y lo oculta a los 3 s', () => {
    vi.useFakeTimers();
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const s = TestBed.inject(SnackbarService);
    s.mostrar('Perfil actualizado');
    expect(s.mensaje()).toBe('Perfil actualizado');
    vi.advanceTimersByTime(DURACION_SNACKBAR_WEB_MS - 1);
    expect(s.mensaje()).toBe('Perfil actualizado');
    vi.advanceTimersByTime(1);
    expect(s.mensaje()).toBeNull();
  });

  it('un mensaje nuevo reinicia la cuenta', () => {
    vi.useFakeTimers();
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const s = TestBed.inject(SnackbarService);
    s.mostrar('Uno');
    vi.advanceTimersByTime(2000);
    s.mostrar('Dos');
    vi.advanceTimersByTime(2000);
    expect(s.mensaje()).toBe('Dos');
  });
});

describe('Componentes con estado L09', () => {
  it('aq-snackbar dibuja el mensaje del servicio en una región de estado', async () => {
    const f = await montar();
    const raiz: HTMLElement = f.nativeElement;
    expect(raiz.querySelector('[role="status"]')).not.toBeNull();
    expect(raiz.querySelector('.snackbar')).toBeNull();
    TestBed.inject(SnackbarService).mostrar('Cuenta eliminada exitosamente');
    await f.whenStable();
    expect(raiz.querySelector('.snackbar')?.textContent).toContain('Cuenta eliminada exitosamente');
  });

  it('aq-switch alterna con un clic y expone aria-checked', async () => {
    const f = await montar();
    const boton = (f.nativeElement as HTMLElement).querySelector(
      '#switch button',
    ) as HTMLButtonElement;
    expect(boton.getAttribute('role')).toBe('switch');
    expect(boton.getAttribute('aria-checked')).toBe('true');
    expect(boton.textContent).toContain('Mostrar el estado de mi alarma');
    boton.click();
    await f.whenStable();
    expect(f.componentInstance.mostrar()).toBe(false);
    expect(boton.getAttribute('aria-checked')).toBe('false');
  });

  it('aq-switch funciona con Signal Forms ([formField])', async () => {
    const f = await montar();
    const boton = (f.nativeElement as HTMLElement).querySelector(
      '#switch-formulario button',
    ) as HTMLButtonElement;
    boton.click();
    await f.whenStable();
    expect(f.componentInstance.formulario.contar().value()).toBe(true);
  });

  it('aq-selector-segmentado marca la opción activa y cambia al tocar otra', async () => {
    const f = await montar();
    const botones = Array.from(
      (f.nativeElement as HTMLElement).querySelectorAll('#selector button'),
    ) as HTMLButtonElement[];
    expect(botones.map((b) => b.textContent?.trim())).toEqual([
      'Nombre completo',
      'Solo iniciales',
      'Alias',
    ]);
    expect(botones[1].getAttribute('aria-pressed')).toBe('true');
    expect(botones[1].classList).toContain('activo');
    botones[2].click();
    await f.whenStable();
    expect(f.componentInstance.aparicion()).toBe('alias');
    expect(botones[2].getAttribute('aria-pressed')).toBe('true');
  });
});
