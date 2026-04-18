package com.metasearch.android.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalBorder = staticCompositionLocalOf { MetaSearchBorder() }
private val LocalColorScheme = staticCompositionLocalOf { MetaSearchColorScheme() }
private val LocalRadius = staticCompositionLocalOf { MetaSearchRadius() }
private val LocalSpacing = staticCompositionLocalOf { MetaSearchSpacing() }
private val LocalTypography = staticCompositionLocalOf { MetaSearchTypography() }

@Composable
fun MetaSearchTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider {
        androidx.compose.material3.Surface(
            color = Neutral50,
            content = content,
        )
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
