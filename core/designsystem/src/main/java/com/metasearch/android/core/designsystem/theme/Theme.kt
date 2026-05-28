package com.metasearch.android.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DefaultBorder = MetaSearchBorder()
private val DefaultColorScheme = MetaSearchColorScheme()
private val DefaultRadius = MetaSearchRadius()
private val DefaultSpacing = MetaSearchSpacing()
private val DefaultTypography = MetaSearchTypography()

private val LocalBorder = staticCompositionLocalOf { DefaultBorder }
private val LocalColorScheme = staticCompositionLocalOf { DefaultColorScheme }
private val LocalRadius = staticCompositionLocalOf { DefaultRadius }
private val LocalSpacing = staticCompositionLocalOf { DefaultSpacing }
private val LocalTypography = staticCompositionLocalOf { DefaultTypography }

private val DarkColors = darkColorScheme(
    primary = Neutral900,
    onPrimary = LightPink,
    primaryContainer = LightPink.copy(alpha = 0.2f),
    onPrimaryContainer = LightPink,

    secondary = Neutral900,
    onSecondary = Color.White,

    tertiary = Rose,
    onTertiary = Neutral50,
    tertiaryContainer = Rose.copy(alpha = 0.2f),
    onTertiaryContainer = Rose,

    background = Neutral950,
    onBackground = Neutral50,

    surface = LightPink,
    onSurface = Neutral900,

    surfaceVariant = Neutral800,
    onSurfaceVariant = Neutral300,

    outline = Neutral700,
    error = HotPink,
    onError = Color.White,
)

private val LightColors = lightColorScheme(
    primary = Black,
    onPrimary = LightPink,
    primaryContainer = Pink.copy(alpha = 0.2f),
    onPrimaryContainer = Pink,

    secondary = Neutral900,
    onSecondary = Color.White,

    tertiary = Rose,
    onTertiary = Neutral50,
    tertiaryContainer = Rose.copy(alpha = 0.2f),
    onTertiaryContainer = Rose,

    background = Color.White,
    onBackground = Neutral900,

    surface = Neutral50,
    onSurface = Neutral900,

    surfaceVariant = Neutral200,
    onSurfaceVariant = Neutral700,

    outline = Neutral400,
    error = HotPink,
    onError = Color.White,
)

private val LightMetaSearchColors = MetaSearchColorScheme(
    background = Color.White,
    surface = Neutral50,
    surfaceVariant = Neutral200,
    actionPrimary = Pink,
    actionContent = Color.White,
    contentPrimary = Neutral900,
    contentSecondary = Neutral700,
    outline = Neutral400,
    divider = Neutral50,
    brandSurface = Neutral800,
    brandContent = LightPink,
)

private val DarkMetaSearchColors = MetaSearchColorScheme(
    background = Neutral950,
    surface = Neutral900,
    surfaceVariant = Neutral800,
    actionPrimary = LightPink,
    actionContent = Neutral900,
    contentPrimary = Neutral50,
    contentSecondary = Neutral300,
    outline = Neutral700,
    divider = Neutral700,
    brandSurface = Neutral800,
    brandContent = LightPink,
)

@Composable
fun MetaSearchTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (isDarkMode) DarkColors else LightColors
    val customColors = if (isDarkMode) DarkMetaSearchColors else LightMetaSearchColors

    CompositionLocalProvider(
        LocalColorScheme provides customColors,
        LocalBorder provides DefaultBorder,
        LocalRadius provides DefaultRadius,
        LocalSpacing provides DefaultSpacing,
        LocalTypography provides DefaultTypography,
    ) {
        MaterialTheme(
            colorScheme = colors,
        ) {
            Surface(color = MetaSearchTheme.colors.background) {
                content()
            }
        }
    }
}

object MetaSearchTheme {
    val border: MetaSearchBorder
        @Composable
        @ReadOnlyComposable
        get() = LocalBorder.current

    val colors: MetaSearchColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val radius: MetaSearchRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalRadius.current

    val spacing: MetaSearchSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val typography: MetaSearchTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
