package com.metasearch.android.feature.detail.graph

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.core.ui.component.WebViewErrorUi
import com.metasearch.android.core.webview.ui.MetaSearchWebViewClient
import com.metasearch.android.core.webview.ui.MetaSearchWebViewContainer
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.detail.graph.component.ExploreImageList
import com.metasearch.android.feature.detail.graph.mock.mock
import com.metasearch.android.feature.screens.GraphDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(GraphDetailScreen::class, AppScope::class)
@Composable
fun GraphDetailUi(
    modifier: Modifier = Modifier,
    state: GraphDetailUiState,
) {
    GraphDetailSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

    MetaSearchScaffold(
        modifier = modifier,
    ) { innerPadding ->
        GraphDetailUiContent(
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun GraphDetailUiContent(
    state: GraphDetailUiState,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MetaSearchTheme.colors.background)
                .zIndex(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            MetaSearchHeader(
                title = stringResource(R.string.graph_detail_screen_header),
                onBackClick = {
                    state.eventSink(GraphDetailUiEvent.OnBackClick)
                },
            )
        }
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            MetaSearchWebViewContainer(
                url = state.webViewUrl,
                onWebViewCreated = { webView ->
                    webView.addJavascriptInterface(
                        object {
                            @JavascriptInterface
                            fun onPageReady() {
                                state.eventSink(GraphDetailUiEvent.OnWebSuccess)
                            }

                            @JavascriptInterface
                            fun receivePhotoName(photoName: String) {
                                state.eventSink(GraphDetailUiEvent.OnPhotoSelected(photoName))
                            }
                        },
                        "Android",
                    )
                },
                webViewClient = object : MetaSearchWebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        if (url != null && !url.startsWith("about:")) {
                            state.eventSink(GraphDetailUiEvent.OnWebLoading)
                        }
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?,
                    ) {
                        if (request?.isForMainFrame == true) {
                            view?.stopLoading()
                            state.eventSink(GraphDetailUiEvent.OnWebError(error?.description?.toString() ?: "Network Error"))
                        }
                    }
                },
            )

            if (state.uiState is UiState.Loading) {
                Box(modifier = Modifier.fillMaxSize().background(MetaSearchTheme.colors.surface), contentAlignment = Alignment.Center) {
                    MetaSearchLoadingIndicator()
                }
            }

            if (state.uiState is UiState.Error) {
                WebViewErrorUi(onRetryClick = { state.eventSink(GraphDetailUiEvent.OnRetry) })
            }
        }

        if (state.uiState is UiState.Success && state.selectedImages.isNotEmpty()) {
            ExploreImageList(state)
        }
    }
}

@DevicePreview
@Composable
private fun GraphDetailUiPreview() {
    MetaSearchTheme {
        GraphDetailUi(
            state = GraphDetailUiState.mock(),
        )
    }
}
