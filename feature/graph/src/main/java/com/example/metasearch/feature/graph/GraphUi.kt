package com.example.metasearch.feature.graph

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.core.ui.component.MetaSearchDialog
import com.example.metasearch.feature.graph.component.GraphHeader
import com.example.metasearch.feature.screens.GraphScreen
import com.example.metasearch.feature.screens.component.MetaSearchMainBottomBar
import com.example.metasearch.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@SuppressLint("SetJavaScriptEnabled")
@CircuitInject(GraphScreen::class, ActivityRetainedComponent::class)
@Composable
fun GraphUi(
    modifier: Modifier = Modifier,
    state: GraphUiState,
) {
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
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            GraphHeader()

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
                                error: SslError?
                            ) {
                                handler?.proceed()
                            }
                        }

                        addJavascriptInterface(object {
                            @JavascriptInterface
                            fun receivePhotoName(photoName: String) {
                                state.eventSink(GraphUiEvent.OnPhotoSelected(photoName))
                            }
                        }, "Android")
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

        if (state.errorMessage.isNotBlank()) {
            MetaSearchDialog(
                title = stringResource(R.string.graph_screen_dialog_title),
                onDismissRequest = { state.eventSink(GraphUiEvent.OnErrorDialogDismiss) },
                content = { Text(state.errorMessage) },
                dismissButtonText = stringResource(R.string.graph_screen_dialog_close_button),
            )
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
