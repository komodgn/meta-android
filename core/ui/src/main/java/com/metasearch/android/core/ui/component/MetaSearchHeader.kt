package com.metasearch.android.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral800
import com.metasearch.android.core.ui.R

@Composable
fun MetaSearchHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    textStyle: TextStyle = MetaSearchTheme.typography.labelSmall,
    textAlign: TextAlign = TextAlign.Center,
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(MetaSearchTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            onBackClick?.let {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Arrow Back Icon",
                        tint = Neutral800,
                    )
                }
            }
            title?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                    style = textStyle,
                    color = Neutral800,
                    textAlign = textAlign,
                )
            }
            onBackClick?.let {
                Spacer(modifier = Modifier.width(MetaSearchTheme.spacing.spacing10))
            }
        }
        MetaSearchDivider()
    }
}

@ComponentPreview
@Composable
private fun FocusingSearchHeaderPreview() {
    MetaSearchTheme {
        MetaSearchHeader(
            title = "드래그 검색",
            onBackClick = {},
        )
    }
}
