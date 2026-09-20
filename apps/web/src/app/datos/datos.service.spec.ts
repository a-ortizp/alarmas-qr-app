import { TestBed } from '@angular/core/testing';
import { ApplicationRef, provideZonelessChangeDetection } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { DatosService } from './datos.service';
import dataset from '../../../public/dataset.json';

describe('DatosService', () => {
  it('carga dataset.json y expone usuario y mensajes como señales', async () => {
    TestBed.configureTestingModule({ providers: [provideZonelessChangeDetection(), provideHttpClient(), provideHttpClientTesting()] });
    const servicio = TestBed.inject(DatosService);
    const http = TestBed.inject(HttpTestingController);
    TestBed.tick();
    http.expectOne('dataset.json').flush(dataset);
    await TestBed.inject(ApplicationRef).whenStable();
    expect(servicio.usuario()?.nombre).toBe('Andrés Rojas');
    expect(servicio.mensajes()?.cuentaEliminada).toBe('Cuenta eliminada exitosamente');
    expect(servicio.web()?.indicadores.escaneosTotales.valor).toBe(128);
    http.verify();
  });
});
