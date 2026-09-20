/** Páginas de la web (docs/TRAZABILIDAD.md §2, web v1.5). Los estados (pestañas, filtros, snackbars) no son rutas. */
export type CodigoPantalla = 'W00' | 'W01' | 'W03' | 'W04' | 'W05' | 'W06';

export interface PantallaWeb {
  codigo: CodigoPantalla;
  ruta: string;
  titulo: string;
  funcionalidad: string;
  /** Las páginas autenticadas se muestran dentro del layout con barra lateral. */
  conBarraLateral: boolean;
}

export const PANTALLAS: readonly PantallaWeb[] = [
  {
    codigo: 'W00',
    ruta: 'login',
    titulo: 'Inicio de sesión',
    funcionalidad: 'F-W00',
    conBarraLateral: false,
  },
  {
    codigo: 'W01',
    ruta: 'alarmas',
    titulo: 'Mis alarmas',
    funcionalidad: 'F-W01 · F-W02 · F-W06',
    conBarraLateral: true,
  },
  {
    codigo: 'W03',
    ruta: 'eventos/:id',
    titulo: 'Detalle del evento',
    funcionalidad: 'F-W03',
    conBarraLateral: true,
  },
  {
    codigo: 'W04',
    ruta: 'reportes',
    titulo: 'Reportes',
    funcionalidad: 'F-W04',
    conBarraLateral: true,
  },
  {
    codigo: 'W05',
    ruta: 'qr',
    titulo: 'Descargar QR',
    funcionalidad: 'F-W05',
    conBarraLateral: true,
  },
  {
    codigo: 'W06',
    ruta: 'perfil',
    titulo: 'Ajustes de perfil',
    funcionalidad: 'F-W07 · F-W08',
    conBarraLateral: true,
  },
] as const;

export function pantallaPorCodigo(codigo: CodigoPantalla): PantallaWeb {
  const p = PANTALLAS.find((x) => x.codigo === codigo);
  if (!p) throw new Error(`Pantalla ${codigo} no declarada`);
  return p;
}
