package com.example.metasearch.feature.search.nls.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral800
import com.example.metasearch.core.ui.component.MetaSearchDivider
import com.example.metasearch.feature.search.R

@Composable
fun NLSearchHeader(
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(MetaSearchTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.nl_search_screen_header),
                style = MetaSearchTheme.typography.headlineSmall,
                color = Neutral800,
            )
        }
        MetaSearchDivider()
    }
}

@ComponentPreview
@Composable
fun NLSearchHeaderPreview() {
    MetaSearchTheme {
        NLSearchHeader()
    }
}
