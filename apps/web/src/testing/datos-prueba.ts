import {
  ApplicationRef,
  EnvironmentProviders,
  Provider,
  provideZonelessChangeDetection,
} from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Routes, withComponentInputBinding } from '@angular/router';
import dataset from '../../public/dataset.json';

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
