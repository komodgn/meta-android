package com.metasearch.android.feature.search.focusing.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.theme.White
import com.metasearch.android.data.domain.Circle
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawingCanvas(
    circles: ImmutableList<Circle>?,
    isDrawing: Boolean,
    currentCenter: Offset,
    currentRadius: Float,
    canvasSize: IntSize,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (canvasSize.width <= 0) return@Canvas

        val maxDimension = maxOf(canvasSize.width, canvasSize.height).toFloat()

        circles?.forEach { circle ->
            drawCircle(
                color = White,
                radius = circle.radius * maxDimension,
                center = Offset(circle.centerX * canvasSize.width, circle.centerY * canvasSize.height),
                style = Stroke(width = 4.dp.toPx()),
            )
        }

        if (isDrawing) {
            drawCircle(
                color = White.copy(alpha = 0.5f),
                radius = currentRadius,
                center = currentCenter,
                style = Stroke(width = 4.dp.toPx()),
            )
        }
    }
}
