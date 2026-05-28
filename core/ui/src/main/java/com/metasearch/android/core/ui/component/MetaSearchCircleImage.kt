package com.metasearch.android.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.metasearch.android.core.common.extensions.clickableIfNotNull
import com.metasearch.android.core.common.extensions.previewPlaceholder
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchCircleImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    size: Dp = 100.dp,
    borderWidth: Dp = 2.dp,
    borderColor: Color = Color.Unspecified,
    onClick: (() -> Unit)? = null,
) {
    val resolvedBorderColor = if (borderColor == Color.Unspecified) MetaSearchTheme.colors.outline else borderColor
    AsyncImage(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(borderWidth, resolvedBorderColor, CircleShape)
            .previewPlaceholder()
            .clickableIfNotNull(onClick),
        model = model,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
    )
}

@ComponentPreview
@Composable
private fun MetaSearchCircleImagePreview() {
    MetaSearchTheme {
        MetaSearchCircleImage(
            model = "",
            onClick = {},
        )
    }
}
