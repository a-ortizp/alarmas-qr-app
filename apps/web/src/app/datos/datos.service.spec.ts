import { TestBed } from '@angular/core/testing';
import { DatosService } from './datos.service';
import { cargarDataset, proveedoresPrueba } from '../../testing/datos-prueba';

describe('DatosService', () => {
  beforeEach(() => TestBed.configureTestingModule({ providers: proveedoresPrueba() }));

  it('carga dataset.json y expone usuario y mensajes como señales', async () => {
    const servicio = TestBed.inject(DatosService);
    await cargarDataset();
    expect(servicio.usuario()?.nombre).toBe('Andrés Rojas');
    expect(servicio.mensajes()?.cuentaEliminada).toBe('Cuenta eliminada exitosamente');
    expect(servicio.web()?.indicadores.escaneosTotales.valor).toBe(128);
  });

  it('expone los textos de acceso y los datos de «Eliminar cuenta»', async () => {
    const servicio = TestBed.inject(DatosService);
    await cargarDataset();
    expect(servicio.acceso()?.errorCredenciales).toBe(
      'Correo o contraseña incorrectos. Inténtalo de nuevo o recupera tu contraseña.',
    );
    expect(servicio.acceso()?.recuperar.boton).toBe('Enviar enlace');
    expect(servicio.eliminarCuenta()?.palabraDeConfirmacion).toBe('ELIMINAR');
    expect(servicio.web()?.barraLateral.items.map((i) => i.id)).toEqual([
      'mis-alarmas',
      'reportes',
      'descargar-qr',
      'ajustes',
      'cerrar-sesion',
    ]);
  });

  it('expone los datos de tablero de la Persona B (gráfica, reportes, QR, asistentes)', async () => {
    const servicio = TestBed.inject(DatosService);
    await cargarDataset();
    expect(servicio.escaneosPorSemana()?.length).toBe(8);
    expect(servicio.escaneosPorSemana()?.at(-1)?.actual).toBe(true);
    expect(servicio.reporte()?.archivoGenerado).toBe('reporte-alarmasqr-ago2026.pdf');
    expect(servicio.reporte()?.generados.length).toBe(3);
    expect(servicio.descargaQR()?.seleccionados).toEqual(['w-seminario', 'w-partido']);
    expect(servicio.asistentes()?.eventoId).toBe('w-partido');
    expect(servicio.asistentes()?.mostrados.length).toBe(4);
    expect(servicio.asistentes()?.pagina2.length).toBe(4);
    expect(servicio.eventosPasados()?.length).toBe(2);
    expect(servicio.filtros()?.borradores.length).toBe(0);
  });
});
