package com.example.metasearch.feature.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.LightGrey
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.feature.home.R

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
        AsyncImage(
            model = image,
            contentDescription = name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = LightGrey,
                    shape = CircleShape,
                ),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_empty_person),
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
