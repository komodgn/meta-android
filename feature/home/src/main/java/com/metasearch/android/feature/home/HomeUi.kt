package com.metasearch.android.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.PagingData.Companion.from
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.LightPink
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.model.GalleryImageModel
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.core.ui.component.MetaSearchSquareImage
import com.metasearch.android.feature.home.component.HomeHeader
import com.metasearch.android.feature.home.component.PersonCircleItem
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.component.MetaSearchMainBottomBar
import com.metasearch.android.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.flowOf

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    state: HomeUiState,
) {
    HomeSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        MetaSearchScaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(bottom = 0),
            bottomBar = {
                MetaSearchMainBottomBar(
                    modifier = modifier
                        .padding(bottom = MetaSearchTheme.spacing.spacing3),
                    currentTab = MetaSearchMainTabItem.HOME,
                    onTabSelected = {
                        state.eventSink(HomeUiEvent.OnTabClick(it.screen))
                    },
                )
            },
        ) { innerPadding ->
            HomeUiContent(
                state = state,
                innerPadding = innerPadding,
            )
        }

        AnimatedVisibility(
            visible = state.selectedLongClickImage != null,
            modifier = Modifier.zIndex(5f),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        state.eventSink(HomeUiEvent.OnLongClickCancel)
                    },
                contentAlignment = Alignment.Center,
            ) {
                state.selectedLongClickImage?.let { uri ->
                    Icon(
                        painter = painterResource(R.drawable.ic_image_share),
                        contentDescription = null,
                        tint = LightPink,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset {
                                IntOffset(
                                    x = (state.selectedOffset.x + 20).toInt(),
                                    y = (state.selectedOffset.y - 120).toInt()
                                )
                            }
                            .size(50.dp)
                            .clickable {
                                state.eventSink(HomeUiEvent.OnShareRelease(uri))
                            },
                    )
                    AsyncImage(
                        model = uri,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset {
                                IntOffset(
                                    x = state.selectedOffset.x.toInt(),
                                    y = state.selectedOffset.y.toInt(),
                                )
                            }
                            .size(80.dp)
                            .graphicsLayer {
                                scaleX = 1.2f
                                scaleY = 1.2f
                                rotationZ = -4f
                            },
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeUiContent(
    state: HomeUiState,
    innerPadding: PaddingValues,
) {
    val lazyPagingItems = state.images.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        HomeHeader(
            onUploadClick = {
                state.eventSink(HomeUiEvent.OnStartAnalysisClicked)
            },
            isAnalyzing = state.isAnalyzing,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    state.eventSink(HomeUiEvent.OnPersonSectionExpand)
                }
                .padding(MetaSearchTheme.spacing.spacing2),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(MetaSearchTheme.spacing.spacing2),
                text = stringResource(R.string.home_screen_person_scroll_view_title),
                color = Neutral500,
            )
            Icon(
                modifier = Modifier.size(20.dp),
                painter = if (state.isExpanded) painterResource(R.drawable.ic_up) else painterResource(R.drawable.ic_down),
                contentDescription = "Up And Down Arrow Icon",
                tint = Neutral500,
            )
        }

        AnimatedVisibility(state.isExpanded) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (state.persons.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(MetaSearchTheme.spacing.spacing4),
                        text = stringResource(R.string.home_screen_person_scroll_view_empty_content),
                        style = MaterialTheme.typography.bodySmall,
                        color = Neutral500,
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = MetaSearchTheme.spacing.spacing4),
                        horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing3),
                    ) {
                        items(state.persons) { person ->
                            val displayImage = person.representativeFace?.imageData
                                ?: person.faces.firstOrNull()?.imageData

                            PersonCircleItem(
                                name = person.inputName,
                                image = displayImage,
                                onClick = {
                                    state.eventSink(HomeUiEvent.OnPersonClick(person.id))
                                },
                            )
                        }
                    }
                }
            }
        }

        Text(
            modifier = Modifier.padding(MetaSearchTheme.spacing.spacing2),
            text = stringResource(
                R.string.home_screen_gallery_grid_view_title,
                lazyPagingItems.itemCount,
            ),
            color = Neutral500,
        )
        Box(
            modifier = Modifier.weight(1f),
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(5),
                userScrollEnabled = state.selectedLongClickImage == null,
                contentPadding = PaddingValues(
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,
                ),
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = lazyPagingItems.itemKey { it.id },
                ) { index ->
                    val item = lazyPagingItems[index]
                    if (item != null) {
                        var itemOffset by remember { mutableStateOf(Offset.Zero) }

                        MetaSearchSquareImage(
                            model = item.uriString,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                itemOffset = coordinates.positionInRoot()
                            },
                            onClick = {
                                state.eventSink(HomeUiEvent.OnImageClick(item.uriString))
                            },
                            onLongClick = {
                                state.eventSink(HomeUiEvent.OnImageLongClick(item.uriString, itemOffset))
                            },
                        )
                    }
                }
            }

            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                MetaSearchLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@DevicePreview
@Composable
private fun HomeUiPreview() {
    MetaSearchTheme {
        val fakeImages = List(20) { index ->
            GalleryImageModel(
                id = index.toLong(),
                uriString = "android.resource://com.example.metasearch/drawable/ic_launcher_foreground",
                dateAdded = System.currentTimeMillis(),
            )
        }

        HomeUi(
            state = HomeUiState(
                isExpanded = true,
                persons = emptyList(),
//                persons = listOf(
//                    PersonModel(
//                        id = 1L,
//                        name = "person1",
//                        inputName = "할미쬬",
//                        faces = listOf(
//                            FaceModel(
//                                id = 101L,
//                                personId = 1L,
//                                imageName = "face1.jpg",
//                                imageData = byteArrayOf(),
//                            ),
//                        ),
//                        isHomeDisplay = true,
//                    ),
//                    PersonModel(
//                        id = 2L,
//                        name = "person2",
//                        inputName = "춘식이",
//                        faces = listOf(
//                            FaceModel(
//                                id = 102L,
//                                personId = 2L,
//                                imageName = "face2.jpg",
//                                imageData = byteArrayOf(),
//                            ),
//                        ),
//                        isHomeDisplay = true,
//                    ),
//                    PersonModel(
//                        id = 3L,
//                        name = "person3",
//                        inputName = "춘구마",
//                        faces = listOf(
//                            FaceModel(
//                                id = 103L,
//                                personId = 3L,
//                                imageName = "face3.jpg",
//                                imageData = byteArrayOf(),
//                            ),
//                        ),
//                        isHomeDisplay = true,
//                    ),
//                ),
                images = flowOf(from(fakeImages)),
                eventSink = {},
            ),
        )
    }
}
