import { Injectable, computed } from '@angular/core';
import { httpResource } from '@angular/common/http';
import { Dataset } from './modelos';

/** Único origen de datos simulados de la web: public/dataset.json (idéntico al del móvil). */
@Injectable({ providedIn: 'root' })
export class DatosService {
  readonly dataset = httpResource<Dataset>(() => 'dataset.json');
  readonly usuario = computed(() => this.dataset.value()?.usuario);
  readonly mensajes = computed(() => this.dataset.value()?.mensajes);
  readonly web = computed(() => this.dataset.value()?.web);
  readonly alarmas = computed(() => this.dataset.value()?.alarmas ?? []);
  readonly acceso = computed(() => this.dataset.value()?.web.acceso);
  readonly eliminarCuenta = computed(() => this.dataset.value()?.web.eliminarCuenta);
}
