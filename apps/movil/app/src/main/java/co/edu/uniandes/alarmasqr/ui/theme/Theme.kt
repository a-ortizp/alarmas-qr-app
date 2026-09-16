package co.edu.uniandes.alarmasqr.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Material 3 re-tematizado con «Energía puntual». Solo esquema claro (la app no tiene modo oscuro en los mockups;
 * las superficies Tinta de M03 y M10 se pintan explícitamente con Colores.Tinta).
 * Regla: el primario (amarillo) se usa en un solo elemento por pantalla; los estados activos usan `secondary` (Tinta).
 */
private val EsquemaClaro = lightColorScheme(
    primary = Colores.AmarilloEnergia,
    onPrimary = Colores.Tinta,
    primaryContainer = Colores.AmarilloSuave,
    onPrimaryContainer = Colores.Tinta,
    secondary = Colores.Tinta,
    onSecondary = Colores.Blanco,
    secondaryContainer = Colores.GrisNiebla,
    onSecondaryContainer = Colores.Tinta,
    tertiary = Colores.VerdeConfirmado,
    onTertiary = Colores.Blanco,
    tertiaryContainer = Colores.VerdeFondo,
    onTertiaryContainer = Colores.VerdeTexto,
    error = Colores.CoralAlarma,
    onError = Colores.Blanco,
    errorContainer = Colores.Blanco,
    onErrorContainer = Colores.CoralTexto,
    background = Colores.Blanco,
    onBackground = Colores.Tinta,
    surface = Colores.Blanco,
    onSurface = Colores.Tinta,
    surfaceVariant = Colores.GrisNiebla,
    onSurfaceVariant = Colores.GrisTexto,
    outline = Colores.GrisBorde,
    outlineVariant = Colores.GrisBorde,
    scrim = Colores.Tinta,
    inverseSurface = Colores.Tinta,
    inverseOnSurface = Colores.Blanco,
    inversePrimary = Colores.Blanco,
)

val TipografiaM3 = Typography(
    displayLarge = Tipografia.HoraProtagonista,
    displayMedium = Tipografia.HoraSonara,
    displaySmall = Tipografia.HoraTarjeta,
    headlineLarge = Tipografia.H1,
    headlineMedium = Tipografia.BarraSuperior,
    headlineSmall = Tipografia.H2,
    titleLarge = Tipografia.H2,
    titleMedium = Tipografia.TituloTarjeta,
    titleSmall = Tipografia.H3,
    bodyLarge = Tipografia.Cuerpo,
    bodyMedium = Tipografia.Etiqueta,
    bodySmall = Tipografia.Nota,
    labelLarge = Tipografia.Boton,
    labelMedium = Tipografia.Chip,
    labelSmall = Tipografia.NavegacionInferior,
)

val Formas = Shapes(
    extraSmall = Radios.Barra,
    small = Radios.Campo,
    medium = Radios.Tarjeta,
    large = Radios.Fab,
    extraLarge = Radios.Hoja,
)

@Composable
fun AlarmasQRTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro, typography = TipografiaM3, shapes = Formas, content = content)
}
