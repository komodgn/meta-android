package com.metasearch.android.feature.graph

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchDialog
import com.metasearch.android.core.ui.component.MetaSearchHeader
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
        modifier = modifier,
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
        modifier = Modifier.padding(innerPadding),
    ) {
        MetaSearchHeader(
            title = stringResource(R.string.graph_screen_header),
            textAlign = TextAlign.Start,
            textStyle = MetaSearchTheme.typography.headlineSmall,
        )
        AndroidView(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }

                    webViewClient = object : WebViewClient() {
                        @SuppressLint("WebViewClientOnReceivedSslError")
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler?,
                            error: SslError?,
                        ) {
                            handler?.proceed()
                        }
                    }

                    addJavascriptInterface(
                        object {
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
                if (state.webViewUrl.isNotEmpty() && webView.url != state.webViewUrl) {
                    webView.loadUrl(state.webViewUrl)
                }
            },
        )

        if (state.selectedImages.isNotEmpty()) {
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
}

@DevicePreview
@Composable
private fun GraphUiPreview() {
    MetaSearchTheme {
        GraphUi(
            state = GraphUiState(
                webViewUrl = "https://www.google.com",
                eventSink = {},
            ),
        )
    }
}
