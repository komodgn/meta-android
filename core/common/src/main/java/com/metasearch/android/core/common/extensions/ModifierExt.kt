package com.metasearch.android.core.common.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalInspectionMode

fun Modifier.clickableIfNotNull(onClick: (() -> Unit)?): Modifier =
    if (onClick != null) this.clickable(onClick = onClick) else this

@Composable
fun Modifier.previewPlaceholder(
    color: Color = LightGray,
    shape: Shape? = null
): Modifier = if (LocalInspectionMode.current) {
    if (shape != null) background(color, shape) else background(color)
} else {
    this
}
