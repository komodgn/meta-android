package com.metasearch.android.core.webview.ui

import android.content.Context
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

open class MetaSearchWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : WebView(context, attrs) {
    init {
        setupDefaultSettings()
        webViewClient = MetaSearchWebViewClient()
    }

    private fun setupDefaultSettings() {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }
    }

    fun release() {
        stopLoading()
        loadUrl("about:blank")
        webChromeClient = null
        webViewClient = WebViewClient()
        settings.javaScriptEnabled = false
        clearHistory()
        clearCache(true)
        removeAllViews()
        destroy()
    }

    fun setPhotoSelectionInterface(onPhotoSelected: (String) -> Unit) {
        addJavascriptInterface(object {
            @JavascriptInterface
            fun receivePhotoName(photoName: String) {
                onPhotoSelected(photoName)
            }
        }, "Android")
    }
}
