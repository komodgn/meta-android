package com.metasearch.android.core.network.request

import kotlinx.serialization.Serializable

@Serializable
data class FocusingSearchRequest(
    val circles: List<Circle>,
)

@Serializable
data class Circle(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
)
