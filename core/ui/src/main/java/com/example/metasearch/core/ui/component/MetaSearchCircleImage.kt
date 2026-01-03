package com.example.metasearch.core.ui.component

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
import com.example.metasearch.core.common.extensions.clickableIfNotNull
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.LightGrey
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchCircleImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    size: Dp = 100.dp,
    borderWidth: Dp = 2.dp,
    borderColor: Color = LightGrey,
    onClick: (() -> Unit)? = null,
) {
    AsyncImage(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
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
