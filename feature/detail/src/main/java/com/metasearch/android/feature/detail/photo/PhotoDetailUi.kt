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
import androidx.compose.ui.res.stringResource
import com.metasearch.android.core.common.extensions.previewPlaceholder
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.component.NetworkImage
import com.metasearch.android.core.designsystem.theme.LightPink
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.detail.photo.component.ImageDescriptionBottomSheetContent
import com.metasearch.android.feature.detail.photo.component.PhotoDetailBottomBar
import com.metasearch.android.feature.detail.photo.component.PhotoDetailBottomBarItem
import com.metasearch.android.feature.detail.photo.mock.mock
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(PhotoDetailScreen::class, AppScope::class)
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

    PhotoDetailSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

    MetaSearchScaffold(
        modifier = modifier,
        bottomBar = {
            PhotoDetailBottomBar(
                onTabClick = { tab ->
                    when (tab) {
                        PhotoDetailBottomBarItem.OPEN_AI -> {
                            state.eventSink(PhotoDetailUiEvent.OnCreateImageDescriptionClick(state.imageUriString))
                        }
                        PhotoDetailBottomBarItem.GRAPH -> {
                            state.eventSink(PhotoDetailUiEvent.OnGraphClick)
                        }
                        PhotoDetailBottomBarItem.FOCUSING_SEARCH -> {
                            state.eventSink(PhotoDetailUiEvent.OnFocusingSearchClick(state.imageUriString))
                        }
                        PhotoDetailBottomBarItem.SHARE -> {
                            state.eventSink(PhotoDetailUiEvent.OnShareImageClick(state.imageUriString))
                        }
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
        MetaSearchHeader(
            title = stringResource(R.string.photo_detail_screen_header),
            onBackClick = {
                state.eventSink(PhotoDetailUiEvent.OnBackClick)
            },
        )
        NetworkImage(
            imageUrl = state.imageUriString,
            contentDescription = "Photo Detail Screen Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(1f)
                .previewPlaceholder(),
        )
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing6))
    }
}

@DevicePreview
@Composable
private fun PhotoDetailUiPreview() {
    MetaSearchTheme {
        PhotoDetailUi(
            state = PhotoDetailUiState.mock(),
        )
    }
}
