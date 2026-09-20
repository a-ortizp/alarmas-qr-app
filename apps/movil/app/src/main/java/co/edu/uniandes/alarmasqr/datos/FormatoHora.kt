package co.edu.uniandes.alarmasqr.datos

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.Locale

/**
 * Formatos de fecha y hora de los mockups (MOCKUPS.md §4): 12 h con am/pm en minúsculas, agrupadores de día en
 * mayúsculas («HOY · JUEVES 27») y fechas largas de M04. Todo se calcula en la zona del dataset (GMT-5).
 */
object FormatoHora {
    private val locale = Locale.forLanguageTag("es-CO")

    private fun parsear(iso: String): OffsetDateTime = OffsetDateTime.parse(iso)

    fun dia(iso: String): LocalDate = parsear(iso).toLocalDate()

    /** «7:30» — sin cero inicial, minutos con dos cifras. */
    fun hora(iso: String): String {
        val t = parsear(iso)
        val h12 = when (val h = t.hour % 12) { 0 -> 12; else -> h }
        return "%d:%02d".format(h12, t.minute)
    }

    fun sufijo(iso: String): String = if (parsear(iso).hour < 12) "am" else "pm"

    fun horaConSufijo(iso: String): String = "${hora(iso)} ${sufijo(iso)}"

    private fun nombreDia(fecha: LocalDate): String =
        fecha.dayOfWeek.getDisplayName(TextStyle.FULL, locale).uppercase(locale)

    /** «HOY · JUEVES 27», «MAÑANA · VIERNES 28», «DOMINGO 30». */
    fun etiquetaDia(iso: String, hoy: LocalDate): String {
        val fecha = dia(iso)
        val base = "${nombreDia(fecha)} ${fecha.dayOfMonth}"
        return when (fecha) {
            hoy -> "HOY · $base"
            hoy.plusDays(1) -> "MAÑANA · $base"
            else -> base
        }
    }

    /** «Dom 30 de agosto · 4:00 pm (GMT-5)» (tarjeta del evento, M04). */
    fun fechaLarga(iso: String): String {
        val t = parsear(iso)
        val diaCorto = t.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).trimEnd('.').replaceFirstChar { it.uppercase(locale) }
        val mes = t.month.getDisplayName(TextStyle.FULL, locale).lowercase(locale)
        val zona = "GMT" + t.offset.id.substringBefore(':').replace("-0", "-").replace("+0", "+")
        return "$diaCorto ${t.dayOfMonth} de $mes · ${horaConSufijo(iso)} ($zona)"
    }

    /** «dom 30» (cuerpo del diálogo M04d: «{fecha} · {hora}»). */
    fun fechaCorta(iso: String): String {
        val t = parsear(iso)
        return "${t.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).trimEnd('.').lowercase(locale)} ${t.dayOfMonth}"
    }

    /** «evento 8:00 am · Aula SD-703» · «pausada · evento 9:30 am» · «vuelo 8:15 am · Aeropuerto». */
    fun lineaEvento(alarma: Alarma): String {
        val etiqueta = alarma.etiquetaEvento ?: "evento"
        val base = "$etiqueta ${horaConSufijo(alarma.eventoInicio)}"
        val conLugar = alarma.lugar?.let { "$base · $it" } ?: base
        return if (alarma.pausada) "pausada · $conLugar" else conLugar
    }
}
