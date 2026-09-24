package co.edu.uniandes.alarmasqr.datos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Espejo tipado de dataset.json (v1.2). Solo se declaran los campos que usan las pantallas; el resto se ignora. */
@Serializable
data class Dataset(
    val meta: Meta,
    val usuario: Usuario,
    val alarmas: List<Alarma>,
    val calendario: Calendario,
    val eventosQR: List<EventoQR>,
    val qrInvalido: QRInvalido,
    val pantallazoRecibido: PantallazoRecibido,
    val mensajes: Mensajes,
)

@Serializable data class Meta(val version: String, val hoy: String, val zonaHoraria: String)

@Serializable
data class Usuario(
    val id: String,
    val nombre: String,
    val aliasPublico: String,
    val correo: String,
    val iniciales: String,
    val privacidad: Privacidad,
    val ajustes: Ajustes,
)

@Serializable
data class Privacidad(
    @SerialName("apariciónEnQuienesEscanearon") val aparicionEnQuienesEscanearon: String,
    val mostrarEstadoDeMiAlarma: Boolean,
    val contarMiYaVoyEnMetricas: Boolean,
)

@Serializable
data class Ajustes(
    val anticipacionPorDefectoMin: Int,
    val sumarTrayectoDesdeUbicacionHabitual: Boolean,
    val sonidoPorDefecto: String,
    val respetarNoMolestar: Boolean,
    val posponerPorDefectoMin: Int,
    val confirmarAntesDeAutoAjustar: Boolean,
    val calendariosVinculados: List<String>,
    val permisos: Permisos,
)

@Serializable
data class Permisos(val alarmasExactas: Boolean, val notificaciones: Boolean, val bateriaSinRestricciones: Boolean, val camara: Boolean)

@Serializable data class Organizador(val nombre: String, val verificado: Boolean = false)

@Serializable data class CambioDelOrganizador(
    val nuevoInicio: String,
    val nuevaHoraDeAlarma: String,
    val antesSonaba: String,
    val autor: String,
    val motivo: String,
)

@Serializable data class AlSonar(val salEnMin: Int, val traficoActual: String, val rutaDisponible: Boolean, val llegaA: String? = null)

@Serializable
data class Alarma(
    val id: String,
    val titulo: String,
    /** ISO-8601 con zona, p. ej. 2026-08-27T08:00:00-05:00 */
    val eventoInicio: String,
    val suena: String,
    val lugar: String? = null,
    val origen: String,
    val estado: String,
    val anticipacionMin: Int,
    val trayectoMin: Int,
    val chips: List<String> = emptyList(),
    /** Solo la alarma que el flujo T1 crea al escanear (M04 → M05); arranca fuera de la lista (Plan 2, D3). */
    val esNueva: Boolean = false,
    /** «vuelo» en «vuelo 8:15 am · Aeropuerto»; por defecto «evento». */
    val etiquetaEvento: String? = null,
    val organizador: Organizador? = null,
    val detalle: String? = null,
    val cambioDelOrganizador: CambioDelOrganizador? = null,
    val alSonar: AlSonar? = null,
    val notas: String? = null,
) {
    val pausada: Boolean get() = estado == "pausada"
}

@Serializable data class Calendario(val mes: String, val diasConAlarmas: Map<String, Int>, val diaSeleccionado: String)

@Serializable
data class EventoQR(
    val id: String,
    val alarmaId: String,
    val titulo: String,
    val codigoQR: String,
    val escaneos: Int? = null,
    val etiqueta: String? = null,
    val organizador: String? = null,
    val verificado: Boolean = false,
)

@Serializable data class QRInvalido(val contenido: String, val tipoDetectado: String, val diagnostico: String)

@Serializable data class PantallazoRecibido(val origen: String, val grupo: String, val mensaje: String, val eventoDetectado: String)

@Serializable
data class Mensajes(
    val alarmaGuardada: String,
    val deshacer: String,
    val descargaCompletada: String,
    val enlaceCopiado: String,
    val camaraActiva: String,
    val sinAlarmas: String,
    val sinAlarmasDetalle: String,
    val confirmarEliminarTitulo: String,
    val confirmarEliminarCuerpo: String,
    val confirmarEliminarSeguro: String,
    val confirmarEliminarAccion: String,
    val confirmarCerrarSesionTitulo: String,
    val confirmarCerrarSesionCuerpo: String,
    val confirmarCerrarSesionSeguro: String,
    val confirmarCerrarSesionAccion: String,
)
