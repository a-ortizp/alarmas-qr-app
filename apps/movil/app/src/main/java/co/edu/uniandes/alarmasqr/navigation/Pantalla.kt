package co.edu.uniandes.alarmasqr.navigation

/**
 * Las 19 pantallas de la app móvil con su código de mockup, ruta y funcionalidad (docs/TRAZABILIDAD.md).
 * La ruta se usa tal cual en Navigation Compose; {id} es el id de la alarma o del evento en dataset.json.
 */
enum class Pantalla(
    val codigo: String,
    val ruta: String,
    val titulo: String,
    val funcionalidad: String,
    val esRaiz: Boolean,
    val conNavegacionInferior: Boolean,
    val conFab: Boolean,
) {
    M01("M01", "bienvenida", "Bienvenida", "F-M01", esRaiz = true, conNavegacionInferior = false, conFab = false),
    M00a("M00a", "registro", "Crear cuenta", "F-M00a", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M00b("M00b", "entrar", "Iniciar sesión", "F-M00b", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M02v("M02v", "inicio/vacio", "Inicio · sin alarmas", "F-M02", esRaiz = true, conNavegacionInferior = true, conFab = false),
    M02("M02", "inicio", "Mis alarmas", "F-M02", esRaiz = true, conNavegacionInferior = true, conFab = true),
    M02h("M02h", "inicio/agregar", "Agregar evento", "F-M02", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M02b("M02b", "calendario", "Calendario", "F-M02", esRaiz = true, conNavegacionInferior = true, conFab = true),
    M03b("M03b", "pantallazo", "Pantallazo recibido", "F-M03", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M03("M03", "escanear", "Escanear QR", "F-M03", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M04("M04", "alarma/{id}/creada", "Alarma programada", "F-M04", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M05("M05", "inicio/guardada/{id}", "Guardada + deshacer", "F-M05", esRaiz = true, conNavegacionInferior = true, conFab = true),
    M06("M06", "alarma/{id}", "Editar alarma", "F-M06", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M07("M07", "evento/nuevo", "Crear evento a mano", "F-M07", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M08("M08", "evento/{id}/qr", "QR del evento", "F-M08", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M09("M09", "alarma/{id}/cambio", "Cambio en el evento", "F-M09", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M10("M10", "alarma/{id}/sonando", "La alarma suena", "F-M10", esRaiz = true, conNavegacionInferior = false, conFab = false),
    M11("M11", "ajustes", "Ajustes", "F-M11", esRaiz = true, conNavegacionInferior = true, conFab = false),
    M12("M12", "permiso-camara", "Permiso de cámara", "F-M12", esRaiz = false, conNavegacionInferior = false, conFab = false),
    M13("M13", "escanear/invalido", "QR sin evento", "F-M13", esRaiz = false, conNavegacionInferior = false, conFab = false);

    /** Ruta concreta reemplazando {id}. */
    fun con(id: String): String = ruta.replace("{id}", id)

    companion object {
        val inicio = M01
        fun porRuta(ruta: String?): Pantalla? = entries.firstOrNull { it.ruta == ruta }
    }
}
