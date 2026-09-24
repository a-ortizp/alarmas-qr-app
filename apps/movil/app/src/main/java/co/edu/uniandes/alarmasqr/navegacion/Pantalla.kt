package co.edu.uniandes.alarmasqr.navegacion

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Las 19 pantallas de la app móvil como claves de Navigation 3, con su código de mockup, ruta y funcionalidad
 * (docs/TRAZABILIDAD.md §1). La ruta sirve para deep links (notificación de la alarma, intent SEND) y para las
 * pruebas, que navegan por código. Las hojas M02h y M04 son claves (tienen ruta); los diálogos M04d/M06d/M11d no.
 */
@Serializable
sealed interface Pantalla : NavKey {
    val codigo: String
    val ruta: String
    val titulo: String
    val funcionalidad: String
    val esRaiz: Boolean get() = false
    val conNavegacionInferior: Boolean get() = false
    val conFab: Boolean get() = false
    /** Se dibuja como hoja inferior sobre la entrada anterior (HojaInferiorSceneStrategy). */
    val esHoja: Boolean get() = false

    /**
     * Ruta sintética: TRAZABILIDAD la registra como `/` (raíz de la app), no como literal `bienvenida`.
     * Necesita clave propia porque `porRuta` no puede resolver la cadena vacía sin ambigüedad.
     * Nota pendiente: `docs/TRAZABILIDAD.md` debe recibir la misma aclaración en el repo de UX.
     */
    @Serializable data object M01 : Pantalla {
        override val codigo = "M01"; override val ruta = "bienvenida"; override val titulo = "Bienvenida"; override val funcionalidad = "F-M01"
        override val esRaiz = true
    }
    @Serializable data object M00a : Pantalla {
        override val codigo = "M00a"; override val ruta = "registro"; override val titulo = "Crear cuenta"; override val funcionalidad = "F-M00a"
    }
    @Serializable data object M00b : Pantalla {
        override val codigo = "M00b"; override val ruta = "entrar"; override val titulo = "Iniciar sesión"; override val funcionalidad = "F-M00b"
    }
    /**
     * Ruta sintética: TRAZABILIDAD la registra como `/inicio` en estado vacío, no como literal `inicio/vacio`.
     * Necesita clave propia porque M02v y M02 comparten la misma ruta de TRAZABILIDAD según el estado.
     * Nota pendiente: `docs/TRAZABILIDAD.md` debe recibir la misma aclaración en el repo de UX.
     */
    @Serializable data object M02v : Pantalla {
        override val codigo = "M02v"; override val ruta = "inicio/vacio"; override val titulo = "Inicio · sin alarmas"; override val funcionalidad = "F-M02"
        override val esRaiz = true; override val conNavegacionInferior = true
    }
    @Serializable data object M02 : Pantalla {
        override val codigo = "M02"; override val ruta = "inicio"; override val titulo = "Mis alarmas"; override val funcionalidad = "F-M02"
        override val esRaiz = true; override val conNavegacionInferior = true; override val conFab = true
    }
    @Serializable data object M02h : Pantalla {
        override val codigo = "M02h"; override val ruta = "inicio/agregar"; override val titulo = "Agregar evento"; override val funcionalidad = "F-M02"
        override val esHoja = true
    }
    @Serializable data object M02b : Pantalla {
        override val codigo = "M02b"; override val ruta = "calendario"; override val titulo = "Calendario"; override val funcionalidad = "F-M02"
        override val esRaiz = true; override val conNavegacionInferior = true; override val conFab = true
    }
    @Serializable data object M03b : Pantalla {
        override val codigo = "M03b"; override val ruta = "pantallazo"; override val titulo = "Pantallazo recibido"; override val funcionalidad = "F-M03"
    }
    @Serializable data object M03 : Pantalla {
        override val codigo = "M03"; override val ruta = "escanear"; override val titulo = "Escanear QR"; override val funcionalidad = "F-M03"
    }
    @Serializable data class M04(val id: String) : Pantalla {
        override val codigo = "M04"; override val ruta = "alarma/$id/creada"; override val titulo = "Alarma programada"; override val funcionalidad = "F-M04"
        override val esHoja = true
    }
    /**
     * Ruta sintética: TRAZABILIDAD la registra como `/inicio` con snackbar, no como literal `inicio/guardada/{id}`.
     * Necesita clave propia porque M05 y M02 comparten la misma ruta de TRAZABILIDAD según el estado.
     * Nota pendiente: `docs/TRAZABILIDAD.md` debe recibir la misma aclaración en el repo de UX.
     */
    @Serializable data class M05(val id: String) : Pantalla {
        override val codigo = "M05"; override val ruta = "inicio/guardada/$id"; override val titulo = "Guardada + deshacer"; override val funcionalidad = "F-M05"
        override val esRaiz = true; override val conNavegacionInferior = true; override val conFab = true
    }
    @Serializable data class M06(val id: String) : Pantalla {
        override val codigo = "M06"; override val ruta = "alarma/$id"; override val titulo = "Editar alarma"; override val funcionalidad = "F-M06"
    }
    @Serializable data object M07 : Pantalla {
        override val codigo = "M07"; override val ruta = "evento/nuevo"; override val titulo = "Crear evento a mano"; override val funcionalidad = "F-M07"
    }
    /**
     * Solo se llega desde M07 («Guardar y crear QR»), no desde M06: el marco 5:2 de los mockups no dibuja ningún
     * control de compartir en M06 y `docs/TRAZABILIDAD.md` tampoco lo lista, así que manda el mockup. El mermaid de
     * `docs/NAVEGACION.md` §3 sí traza `M06 -->|"Compartir"| M08`, y F-M08 habla del QR de «cualquier alarma
     * guardada»: las dos frases contradicen al mockup y a TRAZABILIDAD.
     * Nota pendiente: quitar esa arista del mermaid (o dibujar la fila en el marco de M06) en el repo de UX.
     */
    @Serializable data class M08(val id: String) : Pantalla {
        override val codigo = "M08"; override val ruta = "evento/$id/qr"; override val titulo = "QR del evento"; override val funcionalidad = "F-M08"
    }
    @Serializable data class M09(val id: String) : Pantalla {
        override val codigo = "M09"; override val ruta = "alarma/$id/cambio"; override val titulo = "Cambio en el evento"; override val funcionalidad = "F-M09"
    }
    @Serializable data class M10(val id: String) : Pantalla {
        override val codigo = "M10"; override val ruta = "alarma/$id/sonando"; override val titulo = "La alarma suena"; override val funcionalidad = "F-M10"
        override val esRaiz = true
    }
    @Serializable data object M11 : Pantalla {
        override val codigo = "M11"; override val ruta = "ajustes"; override val titulo = "Ajustes"; override val funcionalidad = "F-M11"
        override val esRaiz = true; override val conNavegacionInferior = true
    }
    @Serializable data object M12 : Pantalla {
        override val codigo = "M12"; override val ruta = "permiso-camara"; override val titulo = "Permiso de cámara"; override val funcionalidad = "F-M12"
    }
    @Serializable data object M13 : Pantalla {
        override val codigo = "M13"; override val ruta = "escanear/invalido"; override val titulo = "QR sin evento"; override val funcionalidad = "F-M13"
    }

    companion object {
        private const val ID = "{id}"

        // perezosos: la interfaz tiene getters por defecto y la JVM la inicializa junto con el companion al
        // inicializar cualquier data object; sin lazy la lista se construiría con INSTANCE aún nulos.
        val inicio: Pantalla by lazy { M01 }

        /** Las 19 pantallas con un id de ejemplo para las parametrizadas (para tablas, pruebas y el marcador). */
        val todas: List<Pantalla> by lazy {
            listOf(
                M01, M00a, M00b, M02v, M02, M02h, M02b, M03b, M03, M04(ID), M05(ID), M06(ID), M07, M08(ID), M09(ID), M10(ID), M11, M12, M13,
            )
        }

        /**
         * Resuelve una ruta concreta («alarma/a-tutor/creada») a su clave; null si no existe.
         * Tolera la barra inicial (y final) porque los deep links llegan como `Uri.path`, que la incluye.
         */
        fun porRuta(ruta: String): Pantalla? {
            val ruta = ruta.trim('/')
            todas.firstOrNull { !it.ruta.contains(ID) && it.ruta == ruta }?.let { return it }
            val partes = ruta.split("/")
            return todas.filter { it.ruta.contains(ID) }.firstNotNullOfOrNull { plantilla ->
                val patron = plantilla.ruta.split("/")
                if (patron.size != partes.size) return@firstNotNullOfOrNull null
                var id: String? = null
                val coincide = patron.zip(partes).all { (p, r) -> if (p == ID) { id = r; true } else p == r }
                if (!coincide || id == null) null else when (plantilla) {
                    is M04 -> M04(id!!); is M05 -> M05(id!!); is M06 -> M06(id!!)
                    is M08 -> M08(id!!); is M09 -> M09(id!!); is M10 -> M10(id!!)
                    else -> null
                }
            }
        }
    }
}
