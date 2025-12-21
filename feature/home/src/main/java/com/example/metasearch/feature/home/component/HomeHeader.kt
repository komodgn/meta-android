package com.example.metasearch.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.LightGrey
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral800
import com.example.metasearch.feature.home.R

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(MetaSearchTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.home_screen_header),
                style = MetaSearchTheme.typography.headlineSmall,
                color = Neutral800,
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
fun HomeHeaderPreview() {
    MetaSearchTheme {
        HomeHeader()
    }
}
