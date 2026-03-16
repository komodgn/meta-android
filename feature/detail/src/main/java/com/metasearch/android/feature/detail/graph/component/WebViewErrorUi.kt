package com.metasearch.android.feature.detail.graph.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.component.MetaSearchButton
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.White
import com.metasearch.android.feature.detail.R

@Composable
fun WebViewErrorUi(
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.graph_detail_screen_webview_error),
            style = MetaSearchTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing4))
        MetaSearchButton(
            text = stringResource(R.string.graph_detail_screen_reload_text_button),
            onClick = onRetryClick,
        )
    }
}

@ComponentPreview
@Composable
private fun WebViewErrorUiPreview() {
    MetaSearchTheme {
        WebViewErrorUi(
            onRetryClick = {},
        )
    }
}
