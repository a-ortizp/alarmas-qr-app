package co.edu.uniandes.alarmasqr.alarma

import co.edu.uniandes.alarmasqr.datos.Alarma

/** Abstracción para que M04 se pruebe sin AlarmManager. */
interface Programador {
    /** Programa y devuelve el instante (epoch ms) elegido. */
    fun programar(alarma: Alarma): Long
    fun cancelar(id: String)
}
