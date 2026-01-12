package com.metasearch.android.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.ui.component.MetaSearchCircleImage

@Composable
internal fun PersonCircleItem(
    modifier: Modifier = Modifier,
    name: String,
    image: ByteArray?,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MetaSearchCircleImage(
            model = image,
            size = 60.dp,
            contentDescription = name,
        )
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing1))
        Text(
            text = name,
            style = MetaSearchTheme.typography.labelSmall,
            color = Neutral500,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ComponentPreview
@Composable
private fun PersonCircleItemPreview() {
    MetaSearchTheme {
        PersonCircleItem(
            name = "춘식이",
            image = byteArrayOf(),
            onClick = {},
        )
    }
}
