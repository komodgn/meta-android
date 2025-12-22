package com.example.metasearch.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.LightGrey
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.designsystem.theme.Neutral800
import com.example.metasearch.feature.home.R

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    onUploadClick: () -> Unit,
    isAnalyzing: Boolean,
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
            Spacer(
                modifier = Modifier.weight(1f),
            )
            if (isAnalyzing) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                IconButton(
                    onClick = onUploadClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_upload),
                        tint = Neutral500,
                        contentDescription = "Upload Icon",
                    )
                }
            }
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
        HomeHeader(
            isAnalyzing = true,
            onUploadClick = {},
        )
    }
}
