package com.example.metasearch.feature.detail.photo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.feature.detail.photo.component.PhotoDetailBottomBar
import com.example.metasearch.feature.detail.photo.component.PhotoDetailBottomBarItem
import com.example.metasearch.feature.detail.photo.component.PhotoDetailHeader
import com.example.metasearch.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(PhotoDetailScreen::class, ActivityRetainedComponent::class)
@Composable
fun PhotoDetailUi(
    modifier: Modifier = Modifier,
    state: PhotoDetailUiState,
) {
    MetaSearchScaffold(
        modifier = modifier,
        bottomBar = {
            PhotoDetailBottomBar(
                onTabClick = { tab ->
                    when (tab) {
                        PhotoDetailBottomBarItem.OPEN_AI -> TODO()
                        PhotoDetailBottomBarItem.GRAPH -> {
                            state.eventSink(PhotoDetailUiEvent.OnGraphButtonClick)
                        }
                        PhotoDetailBottomBarItem.FOCUSING_SEARCH -> {
                            state.eventSink(PhotoDetailUiEvent.OnFocusingSearchClick(state.imageUriString))
                        }
                        PhotoDetailBottomBarItem.SHARE -> TODO()
                    }
                },
            )
        },
    ) { innerPadding ->
        PhotoDetailUiContent(
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@Composable
private fun PhotoDetailUiContent(
    state: PhotoDetailUiState,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        PhotoDetailHeader(
            onBackClick = {
                state.eventSink(PhotoDetailUiEvent.OnBackClick)
            },
        )
        AsyncImage(
            model = state.imageUriString,
            contentDescription = "Photo Detail Screen Image",
            modifier = Modifier
                .weight(1f),
            contentScale = ContentScale.Crop,
        )
        Spacer(
            modifier = Modifier.height(MetaSearchTheme.spacing.spacing6),
        )
    }
}

@DevicePreview
@Composable
private fun PhotoDetailUiPreview() {
    MetaSearchTheme {
        PhotoDetailUi(
            state = PhotoDetailUiState(
                imageUriString = "",
                eventSink = {},
            ),
        )
    }
}
