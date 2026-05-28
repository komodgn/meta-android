package com.metasearch.android.feature.search.focusing.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.feature.search.R

@Composable
fun MoreButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(MetaSearchTheme.radius.sm))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.focusing_search_more_button),
            style = MetaSearchTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@ComponentPreview
@Composable
private fun MoreButtonPreview() {
    MetaSearchTheme {
        MoreButton(
            onClick = {},
        )
    }
}
