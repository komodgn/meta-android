package com.metasearch.android.core.webview.ui

import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MetaSearchWebViewContainer(
    url: String,
    modifier: Modifier = Modifier,
    onWebViewCreated: (MetaSearchWebView) -> Unit = {},
    webViewClient: WebViewClient? = null,
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            MetaSearchWebView(context).apply {
                webViewClient?.let { this.webViewClient = it }
                // Pass the instance for bridge configuration and additional setup
                onWebViewCreated(this)
            }
        },
        update = { webView ->
            if (url.isNotEmpty() && webView.url != url) {
                webView.loadUrl(url)
            }
        },
        onRelease = { webView ->
            webView.release()
        },
    )
}
