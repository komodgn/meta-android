package com.metasearch.android.feature.graph

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.component.MetaSearchButton
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.designsystem.theme.White
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.feature.graph.mock.graphUiStateMock
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.component.MetaSearchMainBottomBar
import com.metasearch.android.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(GraphScreen::class, ActivityRetainedComponent::class)
@Composable
fun GraphUi(
    modifier: Modifier = Modifier,
    state: GraphUiState,
) {
    GraphSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

    MetaSearchScaffold(
        modifier = modifier
            .fillMaxSize(),
        bottomBar = {
            MetaSearchMainBottomBar(
                modifier = modifier,
                currentTab = MetaSearchMainTabItem.GRAPH,
                onTabSelected = {
                    state.eventSink(GraphUiEvent.OnTabClick(it.screen))
                },
            )
        },
    ) { innerPadding ->
        GraphUiContent(
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun GraphUiContent(
    state: GraphUiState,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            MetaSearchHeader(
                title = stringResource(R.string.graph_screen_header),
                textAlign = TextAlign.Start,
                textStyle = MetaSearchTheme.typography.headlineSmall,
            )
        }
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                if (url != null && !url.startsWith("about:")) {
                                    state.eventSink(GraphUiEvent.OnWebLoading)
                                }
                            }

                            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                                if (request?.isForMainFrame == true) {
                                    view?.stopLoading()
                                    state.eventSink(GraphUiEvent.OnWebError(error?.description?.toString() ?: "Network Error"))
                                }
                            }

                            @SuppressLint("WebViewClientOnReceivedSslError")
                            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                                handler?.proceed()
                            }
                        }

                        addJavascriptInterface(
                            object {
                                @JavascriptInterface
                                fun onPageReady() {
                                    state.eventSink(GraphUiEvent.OnWebSuccess)
                                }

                                @JavascriptInterface
                                fun receivePhotoName(photoName: String) {
                                    state.eventSink(GraphUiEvent.OnPhotoSelected(photoName))
                                }
                            },
                            "Android",
                        )
                    }
                },
                update = { webView ->
                    if (state.uiState is UiState.Error) return@AndroidView

                    if (state.webViewUrl.isNotEmpty() && webView.url != state.webViewUrl) {
                        webView.loadUrl(state.webViewUrl)
                    }
                },
            )

            if (state.uiState is UiState.Loading) {
                Box(modifier = Modifier.fillMaxSize().background(White), contentAlignment = Alignment.Center) {
                    MetaSearchLoadingIndicator()
                }
            }

            if (state.uiState is UiState.Error) {
                Column(
                    modifier = Modifier.fillMaxSize().background(White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.graph_screen_network_error),
                        style = MetaSearchTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing4))
                    MetaSearchButton(
                        text = stringResource(R.string.graph_screen_reload_text_button),
                        onClick = { state.eventSink(GraphUiEvent.OnRetry) },
                    )
                }
            }
        }

        if (state.uiState is UiState.Success && state.selectedImages.isNotEmpty()) {
            SelectedImagesList(state)
        }
    }
}

@Composable
private fun SelectedImagesList(state: GraphUiState) {
    Column {
        Text(
            modifier = Modifier.padding(MetaSearchTheme.spacing.spacing2),
            text = stringResource(R.string.graph_screen_bottom_selected_image_label),
            color = Neutral500,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            contentPadding = PaddingValues(horizontal = MetaSearchTheme.spacing.spacing4),
            horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing2),
        ) {
            items(state.selectedImages) { uriString ->
                AsyncImage(
                    model = uriString,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { state.eventSink(GraphUiEvent.OnImageClick(uriString)) },
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing4))
    }
}

@DevicePreview
@Composable
private fun GraphUiPreview() {
    MetaSearchTheme {
        GraphUiContent(
            state = graphUiStateMock,
            innerPadding = PaddingValues(0.dp),
        )
    }
}

@DevicePreview
@Composable
private fun GraphUiErrorPreview() {
    MetaSearchTheme {
        GraphUiContent(
            state = graphUiStateMock.copy(
                uiState = UiState.Error(message = "error"),
            ),
            innerPadding = PaddingValues(0.dp),
        )
    }
}

@DevicePreview
@Composable
private fun GraphUiWebViewPreview() {
    MetaSearchTheme {
        GraphUiContent(
            state = graphUiStateMock.copy(
                uiState = UiState.Success,
            ),
            innerPadding = PaddingValues(0.dp),
        )
    }
}
