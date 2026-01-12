package com.metasearch.android.feature.detail.photo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.component.MetaSearchToast
import com.metasearch.android.core.designsystem.theme.LightPink
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.feature.detail.photo.component.ImageDescriptionBottomSheetContent
import com.metasearch.android.feature.detail.photo.component.PhotoDetailBottomBar
import com.metasearch.android.feature.detail.photo.component.PhotoDetailBottomBarItem
import com.metasearch.android.feature.detail.photo.component.PhotoDetailHeader
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(PhotoDetailScreen::class, ActivityRetainedComponent::class)
@Composable
fun PhotoDetailUi(
    modifier: Modifier = Modifier,
    state: PhotoDetailUiState,
) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }

    LaunchedEffect(state.imageDescription) {
        if (state.imageDescription != null) {
            isSheetOpen = true
        }
    }

    PhotoDetailToastEffect(
        toastMessage = state.toastMessage,
        eventSink = state.eventSink,
    )

    MetaSearchScaffold(
        modifier = modifier,
        bottomBar = {
            PhotoDetailBottomBar(
                onTabClick = { tab ->
                    when (tab) {
                        PhotoDetailBottomBarItem.OPEN_AI -> {
                            state.eventSink(PhotoDetailUiEvent.OnCreateImageDescriptionButtonClick(state.imageUriString))
                        }
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

        if (state.isLoading) {
            MetaSearchLoadingIndicator()
        }

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState,
                containerColor = LightPink,
                dragHandle = { BottomSheetDefaults.DragHandle() },
            ) {
                ImageDescriptionBottomSheetContent(state.imageDescription ?: "")
            }
        }

        MetaSearchToast(
            isVisible = state.toastMessage != null,
            message = state.toastMessage,
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
