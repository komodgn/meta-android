package com.metasearch.android.feature.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral800
import com.metasearch.android.core.ui.component.MetaSearchDivider
import com.metasearch.android.feature.home.R

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onUploadClick: () -> Unit,
    isAnalyzing: Boolean,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(MetaSearchTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_menu_burger),
                        tint = Neutral800,
                        contentDescription = "Menu Icon",
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.home_screen_header),
                style = MetaSearchTheme.typography.headlineSmall,
                color = Neutral800,
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                if (isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    IconButton(onClick = onUploadClick) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_upload),
                            tint = Neutral800,
                            contentDescription = "Upload Icon",
                        )
                    }
                }
            }
        }
        MetaSearchDivider()
    }
}

@ComponentPreview
@Composable
fun HomeHeaderPreview() {
    MetaSearchTheme {
        HomeHeader(
            isAnalyzing = true,
            onMenuClick = {},
            onUploadClick = {},
        )
    }
}
