package com.metasearch.android.core.webview.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

open class MetaSearchWebViewClient : WebViewClient() {

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?,
    ) {
        handler?.proceed()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url ?: return false

        return when {
            url.scheme == "tel" || url.scheme == "mailto" || url.scheme == "market" -> {
                handleExternalIntent(view?.context, url)
                true
            }
            url.toString().startsWith("intent:") -> {
                handleExternalIntent(view?.context, url)
                true
            }
            else -> false
        }
    }

    private fun handleExternalIntent(context: Context?, url: Uri) {
        runCatching {
            val intent = Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME)
            context?.startActivity(intent)
        }
    }
}
