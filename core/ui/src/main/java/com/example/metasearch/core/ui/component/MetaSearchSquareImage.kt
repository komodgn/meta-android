package com.example.metasearch.core.ui.component

import android.R.attr.onClick
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.metasearch.core.common.extensions.clickableIfNotNull
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchSquareImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    AsyncImage(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clickableIfNotNull(onClick),
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
