package com.metasearch.android.core.ui.component

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.metasearch.android.core.common.extensions.previewPlaceholder
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchSquareImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    AsyncImage(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .previewPlaceholder()
            .combinedClickable(
                onClick = { onClick?.invoke() },
                onLongClick = { onLongClick?.invoke() },
            ),
        model = model,
        contentScale = ContentScale.Crop,
        contentDescription = contentDescription,
    )
}

@ComponentPreview
@Composable
private fun MetaSearchSquareImagePreview() {
    MetaSearchTheme {
        MetaSearchSquareImage(
            model = "",
        )
    }
}
