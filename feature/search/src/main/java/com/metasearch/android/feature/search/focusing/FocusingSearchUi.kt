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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.component.NetworkImage
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.data.domain.Circle
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.feature.screens.FocusingSearchScreen
import com.metasearch.android.feature.search.R
import com.metasearch.android.feature.search.focusing.component.DrawingCanvas
import com.metasearch.android.feature.search.focusing.component.FocusingSearchBottomBar
import com.metasearch.android.feature.search.focusing.component.FocusingSearchBottomBarItem
import com.metasearch.android.feature.search.focusing.component.SearchResultList
import com.metasearch.android.feature.search.focusing.mock.mock
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlin.math.sqrt

@CircuitInject(FocusingSearchScreen::class, AppScope::class)
@Composable
fun FocusingSearchUi(
    modifier: Modifier = Modifier,
    state: FocusingSearchUiState,
) {
    FocusingSearchSideEffect(
        state = state,
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
                                        Circle(
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
            NetworkImage(
                imageUrl = state.imageUriString,
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
    MetaSearchTheme {
        FocusingSearchUi(
            state = FocusingSearchUiState.mock(),
        )
    }
}

@DevicePreview
@Composable
private fun FocusingSearchUiLoadingPreview() {
    MetaSearchTheme {
        FocusingSearchUi(
            state = FocusingSearchUiState.mock().copy(
                isLoading = true,
            ),
        )
    }
}
