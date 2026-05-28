package com.metasearch.android.feature.graph

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.metasearch.android.core.common.extensions.previewPlaceholder
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.core.ui.component.MetaSearchSquareImage
import com.metasearch.android.core.ui.component.WebViewErrorUi
import com.metasearch.android.core.webview.ui.MetaSearchWebViewClient
import com.metasearch.android.core.webview.ui.MetaSearchWebViewContainer
import com.metasearch.android.feature.graph.mock.graphUiStateMock
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.component.MetaSearchMainBottomBar
import com.metasearch.android.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(GraphScreen::class, AppScope::class)
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
                .background(MetaSearchTheme.colors.background)
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
            MetaSearchWebViewContainer(
                url = state.webViewUrl,
                onWebViewCreated = { webView ->
                    webView.addJavascriptInterface(
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
                },
                webViewClient = object : MetaSearchWebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        if (url != null && !url.startsWith("about:")) {
                            state.eventSink(GraphUiEvent.OnWebLoading)
                        }
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?,
                    ) {
                        if (request?.isForMainFrame == true) {
                            view?.stopLoading()
                            state.eventSink(GraphUiEvent.OnWebError(error?.description?.toString() ?: "Network Error"))
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
                WebViewErrorUi(onRetryClick = { state.eventSink(GraphUiEvent.OnRetry) })
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
            color = MetaSearchTheme.colors.contentSecondary,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            contentPadding = PaddingValues(horizontal = MetaSearchTheme.spacing.spacing4),
            horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing2),
        ) {
            items(state.selectedImages) { uriString ->
                MetaSearchSquareImage(
                    model = uriString,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { state.eventSink(GraphUiEvent.OnImageClick(uriString)) }
                        .previewPlaceholder(),
                    onClick = { state.eventSink(GraphUiEvent.OnImageClick(uriString)) },
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
