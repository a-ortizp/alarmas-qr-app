import { TestBed } from '@angular/core/testing';
import { proveedoresPrueba } from '../../testing/datos-prueba';
import { CortesService } from './cortes.service';

/** matchMedia falso: cada consulta guarda sus oyentes para simular que la ventana cruza el corte. */
function simularVentana(anchoInicial: number) {
  const consultas: { corte: number; oyentes: ((e: { matches: boolean }) => void)[] }[] = [];
  vi.spyOn(window, 'getComputedStyle').mockReturnValue({
    getPropertyValue: (token: string) =>
      ({
        '--breakpoint-web-colapsar-barra': ' 1200px',
        '--breakpoint-web-apilar-columnas': '1100px',
        '--breakpoint-web-cajon': '900px',
        '--breakpoint-web-telefono': '600px',
      })[token] ?? '',
  } as CSSStyleDeclaration);
  let ancho = anchoInicial;
  window.matchMedia = vi.fn((consulta: string) => {
    const corte = Number(/< (\d+)px/.exec(consulta)?.[1]);
    const registro = { corte, oyentes: [] as ((e: { matches: boolean }) => void)[] };
    consultas.push(registro);
    return {
      matches: ancho < corte,
      addEventListener: (_: string, oyente: (e: { matches: boolean }) => void) =>
        registro.oyentes.push(oyente),
      removeEventListener: () => undefined,
    } as unknown as MediaQueryList;
  });
  return (nuevoAncho: number) => {
    ancho = nuevoAncho;
    for (const c of consultas) c.oyentes.forEach((o) => o({ matches: ancho < c.corte }));
  };
}

describe('CortesService (tokens breakpoint v1.12–v1.13)', () => {
  const matchMediaOriginal = window.matchMedia;
  afterEach(() => {
    vi.restoreAllMocks();
    window.matchMedia = matchMediaOriginal;
  });

  it('a 1280 (marco de Figma) no colapsa ni apila', () => {
    simularVentana(1280);
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const cortes = TestBed.inject(CortesService);
    expect(cortes.colapsarBarra()).toBe(false);
    expect(cortes.apilarColumnas()).toBe(false);
  });

  it('lee los cortes de tokens.css y sigue a la ventana al cruzarlos', () => {
    const cambiarAncho = simularVentana(1150);
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const cortes = TestBed.inject(CortesService);
    expect(window.matchMedia).toHaveBeenCalledWith('(width < 1200px)');
    expect(window.matchMedia).toHaveBeenCalledWith('(width < 1100px)');
    expect(cortes.colapsarBarra()).toBe(true);
    expect(cortes.apilarColumnas()).toBe(false);
    cambiarAncho(1000);
    expect(cortes.apilarColumnas()).toBe(true);
    cambiarAncho(1300);
    expect(cortes.colapsarBarra()).toBe(false);
    expect(cortes.apilarColumnas()).toBe(false);
  });

  it('bajo 900 abre el modo cajón y bajo 600 el de teléfono (v1.13)', () => {
    const cambiarAncho = simularVentana(1280);
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const cortes = TestBed.inject(CortesService);
    expect(cortes.cajon()).toBe(false);
    expect(cortes.telefono()).toBe(false);
    cambiarAncho(800);
    expect(cortes.cajon()).toBe(true);
    expect(cortes.telefono()).toBe(false);
    cambiarAncho(360);
    expect(cortes.cajon()).toBe(true);
    expect(cortes.telefono()).toBe(true);
  });

  it('sin matchMedia (pruebas, SSR) se comporta como el marco de referencia', () => {
    window.matchMedia = undefined as unknown as typeof window.matchMedia;
    TestBed.configureTestingModule({ providers: proveedoresPrueba() });
    const cortes = TestBed.inject(CortesService);
    expect(cortes.colapsarBarra()).toBe(false);
    expect(cortes.apilarColumnas()).toBe(false);
  });
});
