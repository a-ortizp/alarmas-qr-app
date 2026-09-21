import { Routes } from '@angular/router';
import { CodigoPantalla, PANTALLAS, PantallaWeb } from './navegacion/pantallas';
import { AqLayoutAppComponent } from './layout/aq-layout-app/aq-layout-app.component';
import { W00LoginComponent } from './pantallas/w00-login/w00-login.component';
import { W00RecuperarContrasenaComponent } from './pantallas/w00-recuperar-contrasena/w00-recuperar-contrasena.component';
import { W01MisAlarmasComponent } from './pantallas/w01-mis-alarmas/w01-mis-alarmas.component';
import { W03DetalleEventoComponent } from './pantallas/w03-detalle-evento/w03-detalle-evento.component';
import { W04ReportesComponent } from './pantallas/w04-reportes/w04-reportes.component';
import { W05DescargarQrComponent } from './pantallas/w05-descargar-qr/w05-descargar-qr.component';
import { W06PerfilComponent } from './pantallas/w06-perfil/w06-perfil.component';
import { W06ModalEliminarCuentaComponent } from './pantallas/w06-modal-eliminar-cuenta/w06-modal-eliminar-cuenta.component';

const componentes = {
  W00: W00LoginComponent,
  W01: W01MisAlarmasComponent,
  W03: W03DetalleEventoComponent,
  W04: W04ReportesComponent,
  W05: W05DescargarQrComponent,
  W06: W06PerfilComponent,
} as const;

/** Rutas hijas que se dibujan sobre su página (modal sobre W06: /perfil/eliminar). */
const HIJAS: Partial<Record<CodigoPantalla, Routes>> = {
  W06: [
    {
      path: 'eliminar',
      component: W06ModalEliminarCuentaComponent,
      title: 'Eliminar cuenta · Alarmas QR',
      data: { codigo: 'W06' },
    },
  ],
};

const ruta = (p: PantallaWeb) => ({
  path: p.ruta,
  component: componentes[p.codigo],
  title: `${p.titulo} · Alarmas QR`,
  data: { codigo: p.codigo },
  children: HIJAS[p.codigo],
});

/** Rutas de docs/TRAZABILIDAD.md §2, generadas desde PANTALLAS. Los estados (?estado=, ?dialogo=) se leen con query params. */
export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  ...PANTALLAS.filter((p) => !p.conBarraLateral).map(ruta),
  {
    path: 'login/recuperar',
    component: W00RecuperarContrasenaComponent,
    title: 'Recuperar contraseña · Alarmas QR',
    data: { codigo: 'W00' },
  },
  {
    path: '',
    component: AqLayoutAppComponent,
    children: PANTALLAS.filter((p) => p.conBarraLateral).map(ruta),
  },
  { path: '**', redirectTo: 'login' },
];
