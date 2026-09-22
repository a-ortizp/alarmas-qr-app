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
  readonly escaneosPorSemana = computed(() => this.dataset.value()?.web.escaneosPorSemana);
  readonly reporte = computed(() => this.dataset.value()?.web.reporte);
  readonly descargaQR = computed(() => this.dataset.value()?.web.descargaQR);
  readonly asistentes = computed(() => this.dataset.value()?.web.asistentes);
  readonly eventosPasados = computed(() => this.dataset.value()?.web.eventosPasados);
  readonly filtros = computed(() => this.dataset.value()?.web.filtros);
}
