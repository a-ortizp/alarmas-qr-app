import { TestBed } from '@angular/core/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { SesionService } from './sesion.service';

describe('SesionService', () => {
  it('empieza sin sesión y la conmuta', () => {
    TestBed.configureTestingModule({ providers: [provideZonelessChangeDetection()] });
    const s = TestBed.inject(SesionService);
    expect(s.iniciada()).toBe(false);
    s.iniciar();
    expect(s.iniciada()).toBe(true);
    s.cerrar();
    expect(s.iniciada()).toBe(false);
  });
});
