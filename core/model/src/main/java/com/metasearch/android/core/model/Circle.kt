package com.metasearch.android.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Circle(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
) {
    companion object
}
