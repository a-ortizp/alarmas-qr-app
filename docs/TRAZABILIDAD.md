# Alarmas QR · Trazabilidad pantalla → funcionalidad → ruta → componente

Tabla de correspondencia entre los códigos de pantalla de los mockups (Mxx / Wxx), las funcionalidades de `FUNCIONALIDADES.md` (F-Mxx / F-Wxx), la ruta de la app y el nombre del componente raíz. Es independiente del stack: las rutas son las mismas en Flutter, React Native o React; solo cambia cómo se declaran.
Fecha: 2026-09-16 · v1.1 del 2026-09-17 (diálogos de confirmación, botones de 52) · Fuente: `FUNCIONALIDADES.md` v2.5.4, `NAVEGACION.md` §6 y §6b, `MOCKUPS.md` §7, `DESIGN_SYSTEM.md` v1.7.

Convenciones:

- **Ruta**: minúsculas, sin acentos, con el código de pantalla como nombre de ruta (`name: 'M06'`) para poder navegar por código desde las pruebas.
- **Componente**: PascalCase, nombre semántico con el código al final entre paréntesis en la documentación; en el código, el archivo lleva el código como prefijo (`m06_editar_alarma.dart`, `W04ExportModal.tsx`).
- **Estados** (pestañas, modales, confirmaciones) no son rutas: son estado del componente padre, salvo los modales web, que sí tienen ruta propia para poder enlazarlos desde la barra lateral.
- **Diálogos de confirmación** (M04d, M06d, M11d; revisión de tutores del 2026-09-17) tampoco son rutas: son un `DialogoConfirmacion` (DS comp. 47) que abre el componente padre sobre un velo Tinta 55 %. El código de mockup sirve para nombrar el estado en las pruebas (`estado = 'M06d'`).
- **⏩** marca los saltos que en la app real dispara un evento externo (permiso, push, hora de la alarma) y que en la maquetación se simulan con un control.

## 1 · Aplicación móvil (19 pantallas)

| Código | Pantalla | F | Ruta | Componente raíz | Navega a |
|---|---|---|---|---|---|
| M01 | Bienvenida | F-M01 | `/` | `PantallaBienvenida` | «Comenzar» → M00a · «Ahora no / invitado» → M02v |
| M00a | Crear cuenta | F-M00a | `/registro` | `PantallaRegistro` | «Crear cuenta» / «Continuar como invitado» → M02v · «Ya tengo cuenta» → M00b |
| M00b | Iniciar sesión | F-M00b | `/entrar` | `PantallaEntrar` | «Entrar» → M02 |
| M02v | Inicio · sin alarmas (primer uso) | F-M02 | `/inicio` (estado vacío) | `PantallaInicio` con `EstadoVacio` | «Escanear QR del evento» → M12 ⏩ · «Elegir pantallazo» → M03b · «Crear el evento a mano» → M07 |
| M02 | Inicio · lista | F-M02 | `/inicio` | `PantallaInicio` | FAB toque → M12 ⏩ (primer uso) o M03 · FAB mantener → M02h · tarjeta → M06 · Calendario → M02b · Ajustes → M11 |
| M02h | Agregar evento (hoja) | F-M02 | `/inicio/agregar` (hoja modal) | `HojaAgregarEvento` | «Escanear el QR» → M12 ⏩ / M03 · «Elegir pantallazo» → M03b · «Crear a mano» → M07 |
| M02b | Vista calendario | F-M02 | `/calendario` | `PantallaCalendario` | tarjeta → M06 · FAB igual que M02 · Alarmas → M02 |
| M03b | Pantallazo recibido | F-M03 | `/pantallazo` | `PantallaPantallazoRecibido` | «Continuar» → M04 · «Elegir otra imagen» → galería |
| M03 | Escáner dual | F-M03 | `/escanear` | `PantallaEscaner` | QR detectado → M04 · QR inválido → M13 · «Elegir pantallazo» → M03b · «Crear el evento a mano» → M07 |
| M04 | Alarma creada | F-M04 | `/alarma/:id/creada` | `PantallaAlarmaCreada` | «Listo» → M05 · «No puedo asistir · eliminar» → diálogo M04d («Conservar» / velo cierra · «Eliminar» → M02) |
| M05 | Guardada + deshacer | F-M05 | `/inicio` (con snackbar) | `PantallaInicio` + `SnackbarDeshacer` | «Deshacer · 5 s» revierte · tarjeta nueva → M06 |
| M06 | Detalle y edición | F-M06 | `/alarma/:id` | `PantallaEditarAlarma` | «Guardar cambios» → M02 · «Eliminar alarma» → diálogo M06d («Conservar» / velo cierra · «Eliminar» → M02) · fila «Cambios del organizador» → M09 ⏩ · «Gestionar en el calendario» → M02b |
| M07 | Crear evento a mano | F-M07 | `/evento/nuevo` | `PantallaCrearEvento` | «Guardar y crear QR» → M08 |
| M08 | QR del evento | F-M08 | `/evento/:id/qr` | `PantallaCompartirQR` | «Compartir por WhatsApp» → hoja del SO · «‹» → M02 |
| M09 | Cambio del organizador | F-M09 | `/alarma/:id/cambio` | `PantallaCambioEvento` | «Aceptar cambio» → M10 ⏩ · «Mantener alarma» / «×» → M02 · título → M06 |
| M10 | Alarma sonando | F-M10 | `/alarma/:id/sonando` (pantalla completa) | `PantallaAlarmaSonando` | «Ya voy» / «Posponer 10 min» → M02 · «Ver ruta ›» → mapas del SO |
| M11 | Ajustes | F-M11 | `/ajustes` | `PantallaAjustes` | Alarmas → M02 · fila «Cerrar sesión» → diálogo M11d («Cancelar» / velo cierra · «Cerrar sesión» → M01) |
| M12 | Permiso de cámara | F-M12 | `/permiso-camara` | `PantallaPermisoCamara` | «Abrir ajustes» → ajustes del SO → M03 · «Elegir pantallazo» → M03b · «Crear el evento a mano» → M07 · «‹» → M02 |
| M13 | QR inválido | F-M13 | `/escanear/invalido` | `PantallaQRInvalido` | «Volver a escanear» → M03 · «Crear el evento a mano» → M07 · «Abrir el enlace bajo mi responsabilidad» → navegador |

### 1b · Diálogos de confirmación (estados, no rutas)

| Código | Sobre | F | Componente | Título · cuerpo | Acción segura (primario amarillo) | Acción que confirma (contorno) |
|---|---|---|---|---|---|---|
| M04d | M04 (hoja) | F-M04 | `DialogoConfirmacion(variante = Eliminar)` | «¿Eliminar alarma?» · `mensajes.confirmarEliminarCuerpo` con {evento}, {fecha}, {hora} | «Conservar» → cierra (vuelve a M04) | «Eliminar» (Coral Texto) → M02 |
| M06d | M06 | F-M06 | `DialogoConfirmacion(variante = Eliminar)` | ídem | «Conservar» → cierra (vuelve a M06) | «Eliminar» (Coral Texto) → M02 |
| M11d | M11 | F-M11 | `DialogoConfirmacion(variante = CerrarSesion)` | «¿Cerrar sesión?» · `mensajes.confirmarCerrarSesionCuerpo` | «Cancelar» → cierra (vuelve a M11) | «Cerrar sesión» (Tinta, no destruye) → M01 |

Anatomía (tokens v1.7): ancho 342 (`size.dialogo.width`), relleno 24, radio 20, título `titulo-dialogo`, cuerpo `cuerpo-dialogo`, dos botones apilados de 52 separados 10; velo `color.velo-movil`; tocar el velo equivale a la acción segura; rótulos de máximo dos palabras. M09 no lleva diálogo en la app: en los mockups «Eliminar» salió de M09 desde la v1.1 (queda en M06); M09d existe solo en los wireframes.

Componentes compartidos del móvil (del Design System): `BotonPrimario`, `BotonSecundario`, `BotonDestructivo`, `DialogoConfirmacion`, `FabEscanear`, `TarjetaAlarma`, `ChipEstado`, `CampoTexto`, `SelectorSegmentado`, `Switch`, `BarraSuperior`, `NavegacionInferior`, `Snackbar`, `CodigoQR`, `SelloVerificado`, `BandaTextura`.

## 2 · Aplicación web (7 pantallas, 14 estados)

| Código | Pantalla / estado | F | Ruta | Componente raíz | Navega a |
|---|---|---|---|---|---|
| W00 | Inicio de sesión | F-W00 | `/login` | `PaginaLogin` | «Iniciar sesión» → W01 |
| W00 | · cuenta eliminada | F-W08 | `/login?estado=eliminada` | `PaginaLogin` + `Snackbar` | — |
| W01 | Mis Alarmas (hub) | F-W01 · F-W02 · F-W06 | `/alarmas` | `PaginaMisAlarmas` | «Ver detalle ›» → W03 · «Exportar reporte» → W04 · «Descargar QR en lote» → W05 · avatar → W06 |
| W01 | · pestaña Creados / Escaneados | F-W02 · F-W06 | `/alarmas?origen=creados` · `?origen=escaneados` | `PaginaMisAlarmas` (estado) | — |
| W03 | Detalle Evento | F-W03 | `/eventos/:id` | `PaginaDetalleEvento` | miga «‹ Mis alarmas» → W01 · «Ver todos ›» → lista completa |
| W04 | Modal Exportar reporte | F-W04 | `/alarmas/reporte` (modal sobre W01) | `ModalExportarReporte` | «Generar y descargar» → estado Listo · «Cancelar» / cabecera → W01 |
| W04 | · Rango personalizado · Listo | F-W04 | mismo (estado interno) | `ModalExportarReporte` | «Generar de nuevo» → estado inicial |
| W05 | Modal Descargar QR en lote | F-W05 | `/alarmas/qr` (modal sobre W01) | `ModalDescargarQR` | «Descargar» → estado Completado · «Cancelar» / cabecera → W01 |
| W05 | · Completado | F-W05 | mismo (estado interno) | `ModalDescargarQR` + `Snackbar` | — |
| W06 | Ajustes de Perfil | F-W07 | `/perfil` | `PaginaPerfil` | «Guardar cambios» → estado Actualizado · «Eliminar mi cuenta» → modal |
| W06 | · Modal eliminar cuenta | F-W08 | `/perfil/eliminar` (modal sobre W06) | `ModalEliminarCuenta` | «Conservar mi cuenta» → W06 · «Eliminar definitivamente» → W00 (cuenta eliminada) |
| W06 | · Actualizado | F-W07 | mismo (estado interno) | `PaginaPerfil` + `Snackbar` | — |
| — | Barra lateral (persistente) | — | — | `BarraLateral` | Mis Alarmas → W01 · Reportes → W04 · Descargar QR → W05 · Ajustes de Perfil → W06 · Cerrar Sesión → W00 |

Componentes compartidos de la web (Design System L09): `BarraSuperiorWeb`, `BarraLateral`, `ItemBarraLateral`, `Indicador`, `PildoraFiltro`, `ChipEstadoWeb`, `TablaEventos`, `CabeceraModal`, `Modal`, `GraficaEscaneosSemana`, `VistaPreviaAfiche`, `SnackbarWeb`, `BotonWeb` (primario · secundario · destructivo), `SelectorSegmentadoWeb`, `TarjetaAcceso`.

## 3 · Funcionalidades sin pantalla propia

| F | Dónde vive | Notas |
|---|---|---|
| F-M05 Deshacer | Snackbar sobre M02 durante 5 s | Un solo snackbar a la vez |
| F-M04 · F-M06 · F-M11 confirmación | `DialogoConfirmacion` sobre la pantalla de origen (§1b) | Prevención de errores: lo irreversible confirma, lo frecuente se deshace; la acción segura es la prominente |
| F-M09 push | Simulado desde M06 en la maquetación | En la app real llega como notificación |
| F-M10 hora del aviso | Alarma real del sistema en el APK | Debe sonar con la app cerrada (alarmas exactas, Android 12+) |
| F-W06 filtros | Buscador + píldoras dentro de W01 | Sin pantalla W02 separada |
| F-W08 eliminación | Modal sobre W06 | Fricción: escribir ELIMINAR; la acción segura es la prominente |

## 4 · Tareas de prueba y sus rutas

| Tarea | Recorrido | Rutas |
|---|---|---|
| T1 Escanear un QR | M02 → M12 → M03 → M04 → M05 | `/inicio` → `/permiso-camara` → `/escanear` → `/alarma/:id/creada` → `/inicio` |
| T2 Cambiar anticipación y sonido | M05 → M06 | `/inicio` → `/alarma/:id` |
| T3 Crear evento propio y compartir | M02 → M02h → M07 → M08 | `/inicio` → `/inicio/agregar` → `/evento/nuevo` → `/evento/:id/qr` |
| T4 Reaccionar a un cambio de hora | M06 → M09 → M10 → M02 | `/alarma/:id` → `/alarma/:id/cambio` → `/alarma/:id/sonando` → `/inicio` |
| T5 Web: entrar, filtrar, reporte y QR | W00 → W01 → W03 · W04 · W05 | `/login` → `/alarmas` → `/eventos/:id` · `/alarmas/reporte` · `/alarmas/qr` |
| T6 Errores | M02 → M12 → M03 → M13 → M03 | `/inicio` → `/permiso-camara` → `/escanear` → `/escanear/invalido` → `/escanear` |
| T7 Crear cuenta o invitado | M01 → M00a · M00b | `/` → `/registro` · `/entrar` |
| T8 Web: perfil y eliminar cuenta | W06 → modal → W00 | `/perfil` → `/perfil/eliminar` → `/login?estado=eliminada` |
