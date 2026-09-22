/** Formato de fecha/hora del tablero (DS): 12 h sin el espacio que Intl inserta antes de a. m./p. m. */

const DIAS_ABREV = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];
const DIAS_LARGO = ['domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado'];
const MESES_ABREV = [
  'ene',
  'feb',
  'mar',
  'abr',
  'may',
  'jun',
  'jul',
  'ago',
  'sep',
  'oct',
  'nov',
  'dic',
];
const MESES_LARGO = [
  'enero',
  'febrero',
  'marzo',
  'abril',
  'mayo',
  'junio',
  'julio',
  'agosto',
  'septiembre',
  'octubre',
  'noviembre',
  'diciembre',
];

function capitalizar(texto: string): string {
  return texto.charAt(0).toUpperCase() + texto.slice(1);
}

/** «2:00 p.m.» */
export function formatoHora12(iso: string): string {
  const fecha = new Date(iso);
  const horas24 = fecha.getHours();
  const minutos = fecha.getMinutes().toString().padStart(2, '0');
  const horas12 = horas24 % 12 === 0 ? 12 : horas24 % 12;
  const sufijo = horas24 < 12 ? 'a.m.' : 'p.m.';
  return `${horas12}:${minutos} ${sufijo}`;
}

/** «Vie 28 ago - 2:00 p.m.» — tablas de eventos y de asistentes. */
export function formatoFechaHoraCorta(iso: string): string {
  const fecha = new Date(iso);
  return `${DIAS_ABREV[fecha.getDay()]} ${fecha.getDate()} ${MESES_ABREV[fecha.getMonth()]} - ${formatoHora12(iso)}`;
}

/** «Jueves 27 de agosto de 2026» — subtítulo de W03. */
export function formatoFechaLarga(iso: string): string {
  const fecha = new Date(iso);
  return `${capitalizar(DIAS_LARGO[fecha.getDay()])} ${fecha.getDate()} de ${MESES_LARGO[fecha.getMonth()]} de ${fecha.getFullYear()}`;
}

/** «31 ago 2026» — lista de reportes generados (W04). Parte el string en vez de usar Date: una
 * fecha sin hora («2026-08-31») se interpreta como UTC y `getDate()` puede mostrar el día anterior
 * según la zona horaria del navegador. */
export function formatoFechaCorta(iso: string): string {
  const [anio, mes, dia] = iso.split('-').map(Number);
  return `${dia} ${MESES_ABREV[mes - 1]} ${anio}`;
}
