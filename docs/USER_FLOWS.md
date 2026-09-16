# Alarmas QR · User Flows del MVP

Flujos de usuario de las funcionalidades priorizadas en la columna **MVP** de la Lista MVP.
Fecha: 2026-08-23 · Insumo: `LISTA_MVP.md` · Catálogo: `FUNCIONALIDADES.md` · Pantallas: mockups M/W.
Exportables: `User_Flows_Movil.pdf` y `User_Flows_Web.pdf` (raíz del proyecto).

**Convenciones de los diagramas:** rectángulo con borde grueso = pantalla · rombo = decisión del usuario · amarillo = retroalimentación del sistema · gris punteado = sistema externo u otro flujo · negro = fin del flujo.

## Resumen: funcionalidades MVP → flujos

| Funcionalidad (MVP) | Flujos | Ids |
|---|---|---|
| F-M02 · Consulta de alarmas | 1 | UF-M02.1 |
| F-M03 · Escaneo dual de QR | 2 | UF-M03.1 · UF-M03.2 |
| F-M04 · Confirmación automática y descarte | 1 | UF-M04.1 |
| F-M07 · Creación manual + QR | 1 | UF-M07.1 |
| F-M10 · Disparo contextual | 1 | UF-M10.1 |
| F-M12 · Permisos de cámara | 1 | UF-M12.1 |
| **Móvil** | **7** | |
| F-W00 · Inicio de sesión general | 1 | UF-W00.1 |
| F-W01 · Tablero de métricas | 1 | UF-W01.1 |
| F-W02 · Eventos propios y escaneados | 1 | UF-W02.1 |
| **Web** | **3** | |

Conexiones externas del MVP: solo la **web** depende del Cloud Backend (API REST); el móvil MVP funciona con APIs nativas del SO (cámara, almacenamiento, alarmas exactas) y persistencia local. Google Calendar es opcional (si el usuario lo conectó) y el tráfico en tiempo real queda en largo plazo (F-M10-Ext).

---

## Flujos de la aplicación móvil (7)

### UF-M02.1 · Consultar las próximas alarmas — funcionalidad F-M02

- **Pantallas:** M02 (Inicio · Mis alarmas) · salidas: M06, M03, M07
- **Retroalimentación del sistema:** Badges «Nueva» y «Mía · QR», anticipación visible por alarma, ícono de nube (alarma conectada)
- **Conexiones externas / cloud:** Ninguna en MVP (persistencia local)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A(["Abre la app"]) --> B["M02 · Mis alarmas<br>lista por día"]:::pantalla
B --> C{"¿Qué quiere hacer?"}
C -->|"Solo revisar"| Z(["Fin · sale informado"]):::fin
C -->|"Ver una alarma"| D["M06 · Detalle y ajuste"]:::pantalla
C -->|"Escanear un QR"| E(["→ UF-M03.1"]):::ext
C -->|"Usar un pantallazo"| F(["→ UF-M03.2"]):::ext
C -->|"Crear alarma propia"| G(["→ UF-M07.1"]):::ext
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-M03.1 · Escanear el QR con la cámara — funcionalidad F-M03

- **Pantallas:** M02 → M03 (Escáner) → M04 · desvíos: M12 (sin permiso), M13 (QR no válido)
- **Retroalimentación del sistema:** Vibración al detectar el código, linterna automática, marco guía de encuadre
- **Conexiones externas / cloud:** APIs nativas de cámara del SO; sin servicios cloud

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A["M02 · Mis alarmas"]:::pantalla -->|"Escanear QR del evento"| B{"¿Permiso de<br>cámara concedido?"}
B -->|"No"| C(["→ UF-M12.1<br>M12 · Permiso de cámara"]):::ext
B -->|"Sí"| D["M03 · Escáner<br>cámara lista"]:::pantalla
D --> E{"¿Detecta el QR?"}
E -->|"No"| F["Acercar / alejar ·<br>linterna automática"]:::fb
F --> D
E -->|"Sí"| G["Vibra al detectar"]:::fb
G --> H{"¿El QR contiene<br>un evento?"}
H -->|"Sí"| I(["→ UF-M04.1<br>M04 · Alarma programada"]):::ext
H -->|"No"| J["M13 · QR no válido"]:::pantalla
J --> K{"¿Qué decide?"}
K -->|"Escanear de nuevo"| D
K -->|"Usar pantallazo"| L(["→ UF-M03.2"]):::ext
K -->|"Crear a mano"| M(["→ UF-M07.1"]):::ext
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-M03.2 · Importar un pantallazo de la galería — funcionalidad F-M03

- **Pantallas:** M02 o M03 → selector de galería (SO) → M04 · desvío: M13
- **Retroalimentación del sistema:** Mensaje «No se pudo leer» si la imagen no trae un QR legible
- **Conexiones externas / cloud:** Lectura de almacenamiento del SO; sin servicios cloud

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A["M02 o M03"]:::pantalla -->|"Elegir pantallazo<br>de la galería"| B["Selector de imágenes<br>del sistema"]:::ext
B --> C{"¿La imagen trae<br>un QR legible?"}
C -->|"No"| D["«No se pudo leer,<br>prueba con otra imagen»"]:::fb
D --> E{"¿Reintenta?"}
E -->|"Sí"| B
E -->|"No"| F(["Fin · vuelve a M02"]):::fin
C -->|"Sí"| G{"¿El QR contiene<br>un evento?"}
G -->|"Sí"| H(["→ UF-M04.1<br>M04 · Alarma programada"]):::ext
G -->|"No"| I["M13 · QR no válido"]:::pantalla
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-M04.1 · Revisar la alarma programada (o descartarla) — funcionalidad F-M04

- **Pantallas:** M04 (Tarjeta del evento) → M05 (Inicio con confirmación)
- **Retroalimentación del sistema:** «Guardada automáticamente al escanear», snackbar «Alarma guardada · También en Google Calendar» (el botón Deshacer pertenece a F-M05, mediano plazo)
- **Conexiones externas / cloud:** Google Calendar API solo si el usuario conectó calendario (opcional)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A(["Llega de un escaneo"]) --> B["M04 · Tarjeta del evento"]:::pantalla
B --> C["«Alarma guardada<br>automáticamente»"]:::fb
C --> D{"¿Ajusta el aviso?"}
D -->|"Sí · Cambiar"| E["Elige anticipación<br>margen + trayecto"]:::pantalla
E --> F
D -->|"No"| F{"¿Agregar a<br>Google Calendar?"}
F -->|"Activa el toggle"| G["Evento creado<br>en el calendario"]:::fb
G --> H
F -->|"No"| H{"¿Puede asistir?"}
H -->|"No · Eliminar alarma"| I["«Alarma eliminada»"]:::fb
I --> J(["Fin · vuelve a M02"]):::fin
H -->|"Sí · Listo"| K["M05 · Inicio<br>alarma resaltada"]:::pantalla
K --> L["Snackbar «Alarma guardada»"]:::fb
L --> M(["Fin"]):::fin
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-M07.1 · Crear una alarma propia y generar su QR — funcionalidad F-M07

- **Pantallas:** M02 → M07 (Nueva alarma) → M05 · salida opcional: M08 (F-M08, corto plazo)
- **Retroalimentación del sistema:** Validación inline de campos obligatorios, confirmación «Alarma y QR creados»
- **Conexiones externas / cloud:** Ninguna en MVP (QR generado localmente)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A["M02 · Mis alarmas"]:::pantalla -->|"+ Nueva alarma"| B["M07 · Formulario<br>título · fecha · hora"]:::pantalla
B --> C["Guardar y crear QR"]
C --> D{"¿Datos completos<br>y válidos?"}
D -->|"No"| E["Error inline en el campo"]:::fb
E --> B
D -->|"Sí"| F["«Alarma y QR creados»"]:::fb
F --> G{"¿Compartir el QR ahora?"}
G -->|"Sí"| H(["M08 · Compartir QR<br>F-M08 · corto plazo"]):::ext
G -->|"No"| I["M05 · Inicio<br>alarma resaltada"]:::pantalla
I --> J(["Fin"]):::fin
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-M10.1 · Atender la alarma cuando suena — funcionalidad F-M10

- **Pantallas:** M10 (pantalla completa) · salidas: app de mapas (SO), M02
- **Retroalimentación del sistema:** Sonido o solo vibración según «No molestar», «Sal en X min», reprogramación al posponer, re-aviso a los 5 min sin respuesta
- **Conexiones externas / cloud:** AlarmManager / UserNotifications (alarmas exactas, sin internet); ruta con app de mapas del SO — el cálculo de tráfico en vivo es F-M10-Ext (largo plazo)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A(["Hora del aviso"]) --> B{"¿Modo «No molestar»<br>o en reunión?"}
B -->|"Sí"| C["Solo vibra"]:::fb
B -->|"No"| D["Suena la alarma"]:::fb
C --> E["M10 · Alarma en pantalla<br>«Sal en 10 min»"]:::pantalla
D --> E
E --> F{"¿Qué decide?"}
F -->|"Ya voy · ver ruta"| G["Abre la app de mapas"]:::ext
G --> H(["Fin · en camino"]):::fin
F -->|"Posponer 10 min"| I["«Reprogramada para<br>dentro de 10 min»"]:::fb
I --> E
F -->|"Silenciar esta vez"| J["«Silenciada solo hoy»"]:::fb
J --> K(["Fin"]):::fin
F -->|"Sin respuesta"| L{"¿Pasaron 5 min?"}
L -->|"Sí"| M["Re-aviso"]:::fb
M --> E
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-M12.1 · Conceder el permiso de cámara — funcionalidad F-M12

- **Pantallas:** M12 (guía de permiso) → ajustes del SO → M03 · alternativas: galería, M07
- **Retroalimentación del sistema:** Explicación de por qué se pide («no guardamos fotos»), estado del permiso al volver
- **Conexiones externas / cloud:** Ajustes del sistema operativo (Android)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A(["Toca «Escanear»"]) --> B{"¿Permiso de<br>cámara activo?"}
B -->|"Sí"| C(["→ UF-M03.1<br>M03 · Escáner"]):::ext
B -->|"No"| D["M12 · Permiso de cámara<br>por qué y cómo activarlo"]:::pantalla
D --> E{"¿Qué decide?"}
E -->|"Abrir ajustes"| F["Ajustes del celular<br>Permisos → Cámara"]:::ext
F --> G{"¿Lo concedió?"}
G -->|"Sí"| C
G -->|"No"| D
E -->|"Usar un pantallazo"| H(["→ UF-M03.2"]):::ext
E -->|"Crear a mano"| I(["→ UF-M07.1"]):::ext
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```

---

## Flujos de la aplicación web (3)

### UF-W00.1 · Iniciar sesión en la web — funcionalidad F-W00

- **Pantallas:** W00 (Login) → W01 (Tablero)
- **Retroalimentación del sistema:** Error de credenciales bajo el formulario, «Te enviamos un correo de recuperación»
- **Conexiones externas / cloud:** Cloud Backend / API REST (autenticación y sesión)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A["W00 · Inicio de sesión"]:::pantalla --> B{"¿Recuerda su<br>contraseña?"}
B -->|"No"| C["«¿Olvidaste tu contraseña?»"]
C --> D["«Te enviamos un correo<br>de recuperación»"]:::fb
D --> A
B -->|"Sí"| E["Ingresa correo y<br>contraseña · Iniciar sesión"]
E --> F{"¿Credenciales<br>válidas?"}
F -->|"No"| G["Error: «Revisa tu<br>correo o contraseña»"]:::fb
G --> A
F -->|"Sí"| H["W01 · Tablero de control"]:::pantalla
H --> I(["Fin · sesión iniciada"]):::fin
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-W01.1 · Consultar las métricas del tablero — funcionalidad F-W01

- **Pantallas:** W01 (Tablero) · salida: W02
- **Retroalimentación del sistema:** Métricas y gráfica recargadas al cambiar el rango de fechas; nota «datos agregados y anónimos»
- **Conexiones externas / cloud:** Cloud Backend / API REST (consulta de métricas agregadas)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A(["Sesión iniciada"]) --> B["W01 · Tablero<br>eventos · escaneos · alarmas · «Ya voy»"]:::pantalla
B --> C{"¿Qué quiere hacer?"}
C -->|"Solo revisar"| D(["Fin · sale informado"]):::fin
C -->|"Cambiar rango de fechas"| E["Métricas y gráfica<br>recargadas"]:::fb
E --> B
C -->|"Ver sus eventos"| F(["→ UF-W02.1<br>W02 · Mis eventos"]):::ext
C -->|"Reportes o afiches"| G(["W04 / W05<br>fuera del MVP"]):::ext
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```
### UF-W02.1 · Revisar mis eventos (propios y escaneados) — funcionalidad F-W02

- **Pantallas:** W02 (Mis eventos) → W03 (detalle básico) · el monitoreo detallado es F-W03 (corto plazo)
- **Retroalimentación del sistema:** Resultados al buscar, tags «Propio»/«Escaneado», estado vacío «Sin resultados con estos filtros»
- **Conexiones externas / cloud:** Cloud Backend / API REST (eventos de la cuenta)

```mermaid
%%{init: {'theme':'base','themeVariables':{'fontFamily':'sans-serif','fontSize':'15px','primaryColor':'#ffffff','primaryBorderColor':'#1d1d1b','primaryTextColor':'#1d1d1b','lineColor':'#1d1d1b','tertiaryColor':'#ffffff'}, 'flowchart':{'curve':'linear'}}}%%
flowchart TD
A["W02 · Mis eventos<br>propios y escaneados"]:::pantalla --> B{"¿Busca algo<br>específico?"}
B -->|"Sí"| C["Escribe o filtra<br>tipo · estado · fechas"]
C --> D{"¿Hay resultados?"}
D -->|"No"| E["«Sin resultados<br>con estos filtros»"]:::fb
E --> A
D -->|"Sí"| F["Lista filtrada con tags<br>«Propio» / «Escaneado»"]:::fb
F --> G
B -->|"No"| G{"¿Abre un evento?"}
G -->|"No"| H(["Fin"]):::fin
G -->|"Propio"| I["W03 · Detalle del evento<br>monitoreo detallado: F-W03"]:::pantalla
G -->|"Escaneado"| J["Estado de su alarma<br>para ese evento"]:::pantalla
I --> K(["Fin"]):::fin
J --> K
classDef pantalla fill:#ffffff,stroke:#1d1d1b,stroke-width:2.5px,rx:10,ry:10;
classDef fb fill:#ffe45c,stroke:#1d1d1b,stroke-width:2px;
classDef ext fill:#ececec,stroke:#1d1d1b,stroke-width:2px,stroke-dasharray:6 4;
classDef fin fill:#1d1d1b,stroke:#1d1d1b,color:#ffffff;
```

