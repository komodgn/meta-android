package com.example.metasearch.feature.search.focusing

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.component.MetaSearchToast
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.White
import com.example.metasearch.core.model.CircleModel
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.core.ui.component.MetaSearchLoadingIndicator
import com.example.metasearch.feature.screens.FocusingSearchScreen
import com.example.metasearch.feature.search.R
import com.example.metasearch.feature.search.focusing.component.FocusingSearchBottomBar
import com.example.metasearch.feature.search.focusing.component.FocusingSearchBottomBarItem
import com.example.metasearch.feature.search.focusing.component.FocusingSearchHeader
import com.example.metasearch.feature.search.focusing.component.SearchResultList
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
        isCirclesEmpty = state.circles?.isEmpty() == true,
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

    var size by remember { mutableStateOf(androidx.compose.ui.unit.IntSize.Zero) }

    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        FocusingSearchHeader(
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
            Canvas(modifier = Modifier.fillMaxSize()) {
                state.circles?.forEach { circle ->
                    drawCircle(
                        color = White,
                        radius = circle.radius * maxOf(size.width, size.height).toFloat(),
                        center = Offset(circle.centerX * size.width, circle.centerY * size.height),
                        style = Stroke(width = 4.dp.toPx()),
                    )
                }

                if (isDrawing) {
                    drawCircle(
                        color = White.copy(alpha = 0.5f),
                        radius = currentRadius,
                        center = currentCenter,
                        style = Stroke(width = 4.dp.toPx()),
                    )
                }
            }

            MetaSearchToast(
                isVisible = state.isToastVisible,
                message = stringResource(R.string.focusing_search_screen_toast_guide),
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
            )
        }
    }
}

@DevicePreview
@Composable
private fun FocusingSearchUiPreview() {
    MetaSearchTheme {
        FocusingSearchUi(
            state = FocusingSearchUiState(
                imageUriString = "",
                eventSink = {},
            ),
        )
    }
}
