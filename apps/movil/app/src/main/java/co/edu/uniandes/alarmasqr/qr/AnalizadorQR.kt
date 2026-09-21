package co.edu.uniandes.alarmasqr.qr

import co.edu.uniandes.alarmasqr.datos.RepositorioDataset

sealed interface ResultadoQR {
    data class EventoDetectado(val eventoId: String) : ResultadoQR
    data class QRInvalido(val contenido: String) : ResultadoQR
}

/** Interpreta lo leído por la cámara (F-M03 / F-M13): `alarmasqr://evento/{id}` de un evento del dataset → M04; lo demás → M13. */
object AnalizadorQR {
    private val patron = Regex("^alarmasqr://evento/([A-Za-z0-9_-]+)$")

    fun interpretar(contenido: String, repositorio: RepositorioDataset): ResultadoQR {
        val id = patron.find(contenido.trim())?.groupValues?.get(1) ?: return ResultadoQR.QRInvalido(contenido)
        return if (repositorio.evento(id) != null) ResultadoQR.EventoDetectado(id) else ResultadoQR.QRInvalido(contenido)
    }
}
