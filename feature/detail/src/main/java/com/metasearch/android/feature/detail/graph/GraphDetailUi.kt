package com.metasearch.android.feature.detail.graph

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
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
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.detail.graph.mock.graphDetailUiStateMock
import com.metasearch.android.feature.screens.GraphDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(GraphDetailScreen::class, ActivityRetainedComponent::class)
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
        MetaSearchHeader(
            title = stringResource(R.string.graph_detail_screen_header),
            onBackClick = {
                state.eventSink(GraphDetailUiEvent.OnBackClick)
            },
        )
        AndroidView(
            modifier = Modifier
                .weight(1f).fillMaxWidth(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()

                    addJavascriptInterface(
                        object {
                            @JavascriptInterface
                            fun receivePhotoName(photoName: String) {
                                state.eventSink(GraphDetailUiEvent.OnPhotoSelected(photoName))
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
                text = stringResource(R.string.graph_detail_screen_bottom_selected_image_label),
                color = Neutral500,
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentPadding = PaddingValues(MetaSearchTheme.spacing.spacing4),
                horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing2),
            ) {
                items(state.selectedImages) { uriString ->
                    AsyncImage(
                        model = uriString,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { state.eventSink(GraphDetailUiEvent.OnImageClick(uriString)) },
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
private fun GraphDetailUiPreview() {
    MetaSearchTheme {
        GraphDetailUi(
            state = graphDetailUiStateMock,
        )
    }
}
