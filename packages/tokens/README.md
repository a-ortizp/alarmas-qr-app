# packages/tokens

Tokens del Design System «Energía puntual» (fuente de verdad: `docs/DESIGN_SYSTEM.md`).

- `design-tokens.json` · formato Design Tokens, v1.5
- `Tokens.kt` · derivado para Jetpack Compose → copiar a `apps/movil/app/src/main/java/<paquete>/ui/theme/` y ajustar el `package`
- `tokens.css` · derivado para Angular → importar desde `apps/web/src/styles.css`
- `dataset.json` · dataset único de los mockups, compartido por las dos apps
- `fonts/` · pendiente: `.ttf` de Bricolage Grotesque, Archivo y Spline Sans Mono (Google Fonts, licencia OFL) con su `OFL.txt`

Si el sistema cambia, se regeneran en el repositorio de UX (`handoff/`) y se vuelven a copiar aquí.
