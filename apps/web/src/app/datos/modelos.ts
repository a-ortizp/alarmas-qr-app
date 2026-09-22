/** Modelos tipados que reflejan `public/dataset.json` (v1.2), único origen de datos simulados. */

export interface Dataset {
  meta: { version: string; hoy: string; zonaHoraria: string };
  usuario: Usuario;
  alarmas: Alarma[];
  mensajes: Mensajes;
  web: DatosWeb;
}

export interface Usuario {
  id: string;
  nombre: string;
  aliasPublico: string;
  correo: string;
  iniciales: string;
  privacidad: {
    apariciónEnQuienesEscanearon: 'nombre-completo' | 'solo-iniciales' | 'alias';
    mostrarEstadoDeMiAlarma: boolean;
    contarMiYaVoyEnMetricas: boolean;
  };
}

export interface Alarma {
  id: string;
  titulo: string;
  eventoInicio: string;
  suena: string;
  lugar: string | null;
  origen: string;
  estado: string;
  anticipacionMin: number;
  trayectoMin: number;
  chips: string[];
}

export interface Mensajes {
  perfilActualizado: string;
  descargaCompletada: string;
  cuentaEliminada: string;
  correoRecuperacionEnviado: string;
  confirmarCerrarSesionTitulo: string;
  confirmarCerrarSesionCuerpoWeb: string;
  confirmarCerrarSesionSeguro: string;
  confirmarCerrarSesionAccion: string;
  sinResultadosFiltros: string;
  sinBorradores: string;
}

export interface Indicador {
  valor: number;
  detalle: string;
}

export interface EventoWeb {
  id: string;
  titulo: string;
  fechaHora: string;
  origen: string;
  escaneos: number;
  alarmasActivas: number;
  estado: string;
  lugar: string;
}

export interface SemanaEscaneo {
  semana: string;
  etiqueta: string;
  escaneos: number;
  actual?: boolean;
}

export interface Reporte {
  rangos: string[];
  rangoPersonalizado: { desde: string; hasta: string };
  formatos: string[];
  archivoGenerado: string;
  nota: string;
  generados: { archivo: string; fecha: string; formato: string; rango: string }[];
  retencionDias: number;
}

export interface DescargaQR {
  seleccionados: string[];
  formatos: string[];
  afiche: { marca: string; lema: string; qrMinimoCm: number; resolucionPng: number };
}

export interface Asistente {
  alias: string;
  escaneo: string;
  alarma: string;
  confirmoYaVoy?: boolean;
  yaVoy?: boolean;
}

export interface Asistentes {
  eventoId: string;
  total: number;
  mostrados: Asistente[];
  notaPrivacidad: string;
  pagina2: Asistente[];
  porPagina: number;
  busquedaEjemplo: { consulta: string; resultados: string[] };
}

export interface EventoPasado {
  nombre: string;
  fechaHora: string;
  origen: string;
  escaneos: number;
  alarmasActivas: number;
  estado: string;
}

export interface Filtros {
  estado: string[];
  origen: string[];
  borradores: unknown[];
  busquedaEjemplo: { consulta: string; resultados: string[] };
}

export interface Acceso {
  correoEjemplo: string;
  errorCredenciales: string;
  recuperar: { titulo: string; texto: string; boton: string; volver: string };
}

export interface BarraLateralDatos {
  items: { id: string; icono: string }[];
  anchoExpandida: number;
  anchoColapsada: number;
}

export interface DatosWeb {
  indicadores: {
    eventosActivos: Indicador;
    escaneosTotales: Indicador;
    alarmasActivas: Indicador;
    confirmaronYaVoy: Indicador;
  };
  eventos: EventoWeb[];
  eliminarCuenta: {
    eventosPublicados: number;
    alarmasDeAsistentes: number;
    diasParaBorrado: number;
    palabraDeConfirmacion: string;
    consejo: string;
  };
  acceso: Acceso;
  barraLateral: BarraLateralDatos;
  escaneosPorSemana: SemanaEscaneo[];
  reporte: Reporte;
  descargaQR: DescargaQR;
  asistentes: Asistentes;
  eventosPasados: EventoPasado[];
  filtros: Filtros;
}
