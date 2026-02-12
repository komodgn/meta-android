package com.metasearch.android.feature.search.focusing

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import coil3.compose.AsyncImage
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.component.MetaSearchToast
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.model.CircleModel
import com.metasearch.android.core.model.PhotoGroup
import com.metasearch.android.core.model.SearchResult
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.feature.screens.FocusingSearchScreen
import com.metasearch.android.feature.search.R
import com.metasearch.android.feature.search.focusing.component.DrawingCanvas
import com.metasearch.android.feature.search.focusing.component.FocusingSearchBottomBar
import com.metasearch.android.feature.search.focusing.component.FocusingSearchBottomBarItem
import com.metasearch.android.feature.search.focusing.component.SearchResultList
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlin.math.sqrt

@CircuitInject(FocusingSearchScreen::class, ActivityRetainedComponent::class)
@Composable
fun FocusingSearchUi(
    modifier: Modifier = Modifier,
    state: FocusingSearchUiState,
) {
    FocusingSearchToastEffect(
        toastMessage = state.toastMessage,
        eventSink = state.eventSink,
    )

    MetaSearchScaffold(
        bottomBar = {
            FocusingSearchBottomBar(
                modifier = modifier,
                onTabClick = { tab ->
                    when (tab) {
                        FocusingSearchBottomBarItem.SEARCH -> state.eventSink(FocusingSearchUiEvent.OnSearchClick)
                        FocusingSearchBottomBarItem.COLOR -> state.eventSink(FocusingSearchUiEvent.OnColorClick)
                        FocusingSearchBottomBarItem.RESET -> state.eventSink(FocusingSearchUiEvent.OnCircleResetClick)
                    }
                },
            )
        },
    ) { innerPadding ->
        FocusingSearchUiContent(
            state = state,
            innerPadding = innerPadding,
        )

        if (state.isLoading) {
            MetaSearchLoadingIndicator()
        }
    }
}

@Composable
private fun FocusingSearchUiContent(
    state: FocusingSearchUiState,
    innerPadding: PaddingValues,
) {
    var currentCenter by remember { mutableStateOf(Offset.Zero) }
    var currentRadius by remember { mutableFloatStateOf(0f) }
    var isDrawing by remember { mutableStateOf(false) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        MetaSearchHeader(
            title = stringResource(R.string.focusing_search_screen_header),
            onBackClick = {
                state.eventSink(FocusingSearchUiEvent.OnBackClick)
            },
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentCenter = offset
                            isDrawing = true
                        },
                        onDrag = { change, _ ->
                            change.consume()

                            val xDiff = change.position.x - currentCenter.x
                            val yDiff = change.position.y - currentCenter.y
                            currentRadius = sqrt(xDiff * xDiff + yDiff * yDiff)
                        },
                        onDragEnd = {
                            if (size.width > 0 && size.height > 0) {
                                val normalizedX = currentCenter.x / size.width
                                val normalizedY = currentCenter.y / size.height
                                val normalizedRadius = currentRadius / maxOf(size.width, size.height)

                                state.eventSink(
                                    FocusingSearchUiEvent.OnCircleAdded(
                                        CircleModel(
                                            centerX = normalizedX,
                                            centerY = normalizedY,
                                            radius = normalizedRadius,
                                        ),
                                    ),
                                )
                            }
                            isDrawing = false
                            currentRadius = 0f
                        },
                    )
                },
        ) {
            AsyncImage(
                model = state.imageUriString,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )

            DrawingCanvas(
                circles = state.circles,
                isDrawing = isDrawing,
                currentCenter = currentCenter,
                currentRadius = currentRadius,
                canvasSize = size,
            )

            MetaSearchToast(
                isVisible = state.toastMessage != null,
                message = state.toastMessage ?: "",
                modifier = Modifier.align(Alignment.Center),
            )
        }

        state.searchResult?.let { result ->
            SearchResultList(
                modifier = Modifier.weight(1f),
                result = result,
                onImageClick = { uri ->
                    state.eventSink(FocusingSearchUiEvent.OnImageClick(uri))
                },
                onMoreClick = { categoryName ->
                    state.eventSink(FocusingSearchUiEvent.OnMoreClick(categoryName))
                },
            )
        }
    }
}

@DevicePreview
@Composable
private fun FocusingSearchUiPreview() {
    val fakeGroups = listOf(
        PhotoGroup(
            categoryName = "# 고양이 # 노트북",
            photoNames = listOf("https://picsum.photos/200", "https://picsum.photos/201", "https://picsum.photos/202", "https://picsum.photos/203"),
        ),
        PhotoGroup(
            categoryName = "# 고양이",
            photoNames = listOf("https://picsum.photos/200", "https://picsum.photos/201", "https://picsum.photos/202", "https://picsum.photos/203"),
        ),
        PhotoGroup(
            categoryName = "# 노트북",
            photoNames = listOf("https://picsum.photos/204", "https://picsum.photos/205"),
        ),
    )
    val fakeSearchResult = SearchResult(groups = fakeGroups)

    MetaSearchTheme {
        FocusingSearchUi(
            state = FocusingSearchUiState(
                imageUriString = "",
                searchResult = fakeSearchResult,
                eventSink = {},
            ),
        )
    }
}
