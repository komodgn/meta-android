package com.metasearch.android.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.metasearch.android.core.designsystem.R

val fontFamily =
    FontFamily(
        Font(R.font.gothic_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.pretendard_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    )

private val defaultLineHeightStyle =
    LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    )

private val baseTextStyle =
    TextStyle(
        fontFamily = fontFamily,
        lineHeightStyle = defaultLineHeightStyle,
        color = Neutral800,
    )

private fun style(
    fontSize: Int,
    lineHeight: Int,
    letterSpacing: Float,
    fontWeight: FontWeight,
) = baseTextStyle.copy(
    fontSize = fontSize.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing.sp,
    fontWeight = fontWeight,
)

/**
 * - Display: 앱 내 가장 큰 제목
 * - Headline: 섹션, 페이지 제목
 * - Title: 컴포넌트 내부 제목
 * - Body: 일반 텍스트
 * - Label: 버튼, 입력 필드 라벨
 */
@Immutable
data class MetaSearchTypography(
    val displaySmall: TextStyle = style(
        fontSize = 28,
        lineHeight = 38,
        letterSpacing = -0.66f,
        fontWeight = FontWeight.SemiBold,
    ),

    val headlineSmall: TextStyle = style(
        fontSize = 22,
        lineHeight = 30,
        letterSpacing = -0.26f,
        fontWeight = FontWeight.SemiBold,
    ),

    val titleLarge: TextStyle = style(
        fontSize = 18,
        lineHeight = 26,
        letterSpacing = -0.22f,
        fontWeight = FontWeight.SemiBold,
    ),

    val bodyLarge: TextStyle = style(
        fontSize = 16,
        lineHeight = 24,
        letterSpacing = -0.16f,
        fontWeight = FontWeight.Light,
    ),

    val bodyMedium: TextStyle = style(
        fontSize = 15,
        lineHeight = 24,
        letterSpacing = -0.15f,
        fontWeight = FontWeight.Normal,
    ),

    val labelLarge: TextStyle = style(
        fontSize = 14,
        lineHeight = 22,
        letterSpacing = -0.14f,
        fontWeight = FontWeight.Light,
    ),

    val labelMedium: TextStyle = style(
        fontSize = 13,
        lineHeight = 18,
        letterSpacing = -0.13f,
        fontWeight = FontWeight.Normal,
    ),

    val labelSmall: TextStyle = style(
        fontSize = 12,
        lineHeight = 16,
        letterSpacing = -0.12f,
        fontWeight = FontWeight.Normal,
    ),

    val captionSmall: TextStyle = style(
        fontSize = 11,
        lineHeight = 14,
        letterSpacing = -0.11f,
        fontWeight = FontWeight.Normal,
    ),
)
