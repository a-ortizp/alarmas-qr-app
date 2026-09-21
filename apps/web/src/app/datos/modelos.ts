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
}
