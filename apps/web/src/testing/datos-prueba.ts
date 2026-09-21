import {
  ApplicationRef,
  EnvironmentProviders,
  Provider,
  provideZonelessChangeDetection,
  signal,
} from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Routes, withComponentInputBinding } from '@angular/router';
import dataset from '../../public/dataset.json';
import { CortesService } from '../app/navegacion/cortes.service';

/** Proveedores comunes de las pruebas: sin zone.js, HTTP de prueba y enrutador con query params como inputs. */
export function proveedoresPrueba(rutas: Routes = []): (Provider | EnvironmentProviders)[] {
  return [
    provideZonelessChangeDetection(),
    provideHttpClient(),
    provideHttpClientTesting(),
    provideRouter(rutas, withComponentInputBinding()),
  ];
}

/** Responde la petición de dataset.json (única fuente de datos) y espera a que la app se estabilice. */
export async function cargarDataset(): Promise<void> {
  TestBed.tick();
  const http = TestBed.inject(HttpTestingController);
  for (const peticion of http.match('dataset.json')) peticion.flush(dataset);
  await TestBed.inject(ApplicationRef).whenStable();
}

/** CortesService falso: señales que la prueba mueve para simular que la ventana cruza los cortes de ancho. */
export function cortesFalsos(colapsar = false, apilar = false, cajon = false, telefono = false) {
  const colapsarBarra = signal(colapsar);
  const apilarColumnas = signal(apilar);
  const enCajon = signal(cajon);
  const enTelefono = signal(telefono);
  const proveedor: Provider = {
    provide: CortesService,
    useValue: {
      colapsarBarra: colapsarBarra.asReadonly(),
      apilarColumnas: apilarColumnas.asReadonly(),
      cajon: enCajon.asReadonly(),
      telefono: enTelefono.asReadonly(),
    },
  };
  return { proveedor, colapsarBarra, apilarColumnas, cajon: enCajon, telefono: enTelefono };
}
