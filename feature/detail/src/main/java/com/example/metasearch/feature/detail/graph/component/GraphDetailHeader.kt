package com.example.metasearch.feature.detail.graph.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.LightGrey
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral800
import com.example.metasearch.feature.detail.R
import com.example.metasearch.feature.detail.photo.component.PhotoDetailHeader

@Composable
fun GraphDetailHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(MetaSearchTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Arrow Back Icon",
                    tint = Neutral800,
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.graph_detail_screen_header),
                style = MetaSearchTheme.typography.captionSmall,
                color = Neutral800,
                textAlign = TextAlign.Center,
            )
            Spacer(
                modifier = Modifier.width(MetaSearchTheme.spacing.spacing10),
            )
        }
        Spacer(
            modifier = modifier.fillMaxWidth()
                .height(MetaSearchTheme.spacing.spacing05)
                .background(LightGrey),
        )
    }
}

@ComponentPreview
@Composable
private fun GraphDetailHeaderPreview() {
    MetaSearchTheme {
        GraphDetailHeader(
            onBackClick = {},
        )
    }
}
