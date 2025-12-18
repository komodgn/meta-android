package com.example.metasearch.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

val Neutral50 = Color(0xFFFAFAFA)
val Neutral100 = Color(0xFFF5F5F5)
val Neutral200 = Color(0xFFE5E5E5)
val Neutral300 = Color(0xFFD4D4D4)
val Neutral400 = Color(0xFFA1A1A1)
val Neutral500 = Color(0xFF737373)
val Neutral600 = Color(0xFF525252)
val Neutral700 = Color(0xFF404040)
val Neutral800 = Color(0xFF262626)
val Neutral900 = Color(0xFF171717)
val Neutral950 = Color(0xFF0A0A0A)

val LightPink = Color(0xFFfde6eb)

@Immutable
data class MetaSearchColorScheme(
    val basePrimary: Color = White,
    val bgPrimary: Color = Neutral900,
    val bgPrimaryPressed: Color = Neutral700,
    val bgDisabled: Color = Neutral200,
    val contentPrimary: Color = Neutral800,
    val borderPrimary: Color = Neutral200,
    val divider: Color = Neutral200,
)
