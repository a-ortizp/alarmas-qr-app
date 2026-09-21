// Falla si src/app escribe a mano un color, un rgb()/rgba() o una medida en px: todo sale de tokens.css.
import { readdirSync, readFileSync, statSync } from 'node:fs';
import { join } from 'node:path';
import { fileURLToPath } from 'node:url';

const RAIZ = fileURLToPath(new URL('../src/app', import.meta.url));
const PROHIBIDOS = [
  { nombre: 'color hexadecimal', patron: /#[0-9a-fA-F]{3,8}\b/g },
  { nombre: 'rgb()/rgba()', patron: /\brgba?\(/g },
  { nombre: 'medida en px', patron: /\b\d+(\.\d+)?px\b/g },
];

function* archivos(dir) {
  for (const nombre of readdirSync(dir)) {
    const ruta = join(dir, nombre);
    if (statSync(ruta).isDirectory()) yield* archivos(ruta);
    else if (/\.(ts|css|html)$/.test(nombre) && !nombre.endsWith('.spec.ts')) yield ruta;
  }
}

let fallos = 0;
for (const archivo of archivos(RAIZ)) {
  readFileSync(archivo, 'utf8')
    .split('\n')
    .forEach((linea, i) => {
      for (const { nombre, patron } of PROHIBIDOS) {
        for (const m of linea.matchAll(patron)) {
          console.error(`${archivo}:${i + 1}: ${nombre} «${m[0]}» — usa una variable de tokens.css`);
          fallos++;
        }
      }
    });
}
if (fallos > 0) {
  console.error(`\n${fallos} valor(es) escritos a mano en src/app.`);
  process.exit(1);
}
console.log('Tokens: ningún color ni medida a mano en src/app.');
