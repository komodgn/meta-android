package com.metasearch.android.feature.webview

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.feature.screens.WebViewScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(WebViewScreen::class, ActivityRetainedComponent::class)
@Composable
fun WebViewUi(
    modifier: Modifier = Modifier,
    state: WebViewUiState,
) {
    MetaSearchScaffold(
        modifier = modifier,
    ) { innerPadding ->
        WebViewUiContent(
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@Composable
private fun WebViewUiContent(
    state: WebViewUiState,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )

                    webViewClient = WebViewClient()
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                    }
                    loadUrl(state.url)
                }
            },
        )
    }
}

@DevicePreview
@Composable
private fun WebViewUiPreview() {
    MetaSearchTheme {
        WebViewUi(
            state = WebViewUiState(
                eventSink = {},
            ),
        )
    }
}
