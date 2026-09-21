import { NombreIcono } from '../componentes/icono/iconos';

/** Ítem de la barra lateral (TRAZABILIDAD §2 «Barra lateral»): ids e iconos iguales a dataset.web.barraLateral. */
export interface ItemBarraLateral {
  id: string;
  texto: string;
  icono: NombreIcono;
  ruta: string;
}

export const ITEMS_BARRA_LATERAL: readonly ItemBarraLateral[] = [
  { id: 'mis-alarmas', texto: 'Mis Alarmas', icono: 'alarma', ruta: '/alarmas' },
  { id: 'reportes', texto: 'Reportes', icono: 'reportes', ruta: '/reportes' },
  { id: 'descargar-qr', texto: 'Descargar QR', icono: 'escanear', ruta: '/qr' },
  { id: 'ajustes', texto: 'Ajustes de Perfil', icono: 'ajustes', ruta: '/perfil' },
];

/** Al fondo de la barra: no navega, abre el diálogo «¿Cerrar sesión?» (?dialogo=cerrar-sesion). */
export const ITEM_CERRAR_SESION = {
  id: 'cerrar-sesion',
  texto: 'Cerrar Sesión',
  icono: 'cerrar-sesion',
} as const;
