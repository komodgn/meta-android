package com.metasearch.android.core.ui.component

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
import com.metasearch.android.core.ui.R

@Composable
fun WebViewErrorUi(
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MetaSearchTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.webview_error_description),
            style = MetaSearchTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MetaSearchTheme.colors.contentSecondary,
        )
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing4))
        MetaSearchButton(
            text = stringResource(R.string.webview_reload_text_button),
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
