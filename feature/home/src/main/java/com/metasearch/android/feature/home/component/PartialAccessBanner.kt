package com.metasearch.android.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.feature.home.R

@Composable
fun PartialAccessBanner(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MetaSearchTheme.colors.brandSurface)
            .clickable(onClick = onClick)
            .padding(
                horizontal = MetaSearchTheme.spacing.spacing4,
                vertical = MetaSearchTheme.spacing.spacing3,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_landscape),
                contentDescription = null,
                tint = MetaSearchTheme.colors.brandContent,
                modifier = Modifier.size(MetaSearchTheme.spacing.spacing5),
            )
            Spacer(modifier = Modifier.width(MetaSearchTheme.spacing.spacing3))
            Text(
                text = stringResource(R.string.home_screen_partial_access_banner_title),
                style = MaterialTheme.typography.bodyMedium,
                color = MetaSearchTheme.colors.brandContent,
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_angle_right),
            contentDescription = null,
            tint = MetaSearchTheme.colors.brandContent,
            modifier = Modifier.size(MetaSearchTheme.spacing.spacing4),
        )
    }
}

@ComponentPreview
@Composable
private fun PartialAccessBannerPreview() {
    MetaSearchTheme {
        PartialAccessBanner(
            onClick = {},
        )
    }
}
