package com.example.metasearch.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.core.ui.component.MetaSearchLoadingIndicator
import com.example.metasearch.feature.home.component.HomeHeader
import com.example.metasearch.feature.home.component.PersonCircleItem
import com.example.metasearch.feature.screens.HomeScreen
import com.example.metasearch.feature.screens.component.MetaSearchMainBottomBar
import com.example.metasearch.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    state: HomeUiState,
) {
    MetaSearchScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MetaSearchMainBottomBar(
                modifier = modifier,
                currentTab = MetaSearchMainTabItem.HOME,
                onTabSelected = {
                    state.eventSink(HomeUiEvent.OnTabClick(it.screen))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
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
                                PersonCircleItem(
                                    name = person.inputName,
                                    image = person.representativeFace?.imageData ?: person.faces.firstOrNull()?.imageData,
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
                text = stringResource(R.string.home_screen_gallery_grid_view_title),
                color = Neutral500,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(5),
                ) {
                    items(state.images) { uri ->
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(1.dp)
                                .clickable {
                                    state.eventSink(HomeUiEvent.OnImageClick(uri.toString()))
                                },
                            model = uri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }

                if (state.isGalleryLoading) {
                    MetaSearchLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun HomeUiPreview() {
    MetaSearchTheme {
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
                images = List(20) {
                    "android.resource://com.example.metasearch/feature/home/drawable/ic_launcher_foreground".toUri()
                },
                eventSink = {},
            ),
        )
    }
}
