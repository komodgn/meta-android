package com.example.metasearch.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.model.PersonModel
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.core.ui.component.MetaSearchLoadingIndicator
import com.example.metasearch.feature.home.component.HomeHeader
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
                    painter = if (state.isExpanded) painterResource(R.drawable.ic_up) else painterResource(R.drawable.ic_down),
                    contentDescription = "Up And Down Arrow Icon",
                    tint = Neutral500,
                )
            }

            AnimatedVisibility(state.isExpanded) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = MetaSearchTheme.spacing.spacing4),
                    horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing3),
                ) {
                    items(state.persons) { person ->
                        PersonCircleItem(
                            name = person.inputName,
                            image = person.image,
                            onClick = {
                                state.eventSink(HomeUiEvent.OnPersonClick(person.id))
                            },
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(MetaSearchTheme.spacing.spacing2),
                text = stringResource(R.string.home_screen_gallery_grid_view_title),
                color = Neutral500,
            )
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

            if (state.isLoading) {
                MetaSearchLoadingIndicator()
            }
        }
    }
}

@Composable
private fun PersonCircleItem(
    modifier: Modifier = Modifier,
    name: String,
    image: ByteArray?,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = image,
            contentDescription = name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_empty_person),
        )
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing1))
        Text(
            text = name,
            style = MetaSearchTheme.typography.labelSmall,
            color = Neutral500,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@DevicePreview
@Composable
private fun HomeUiPreview() {
    MetaSearchTheme {
        HomeUi(
            state = HomeUiState(
                persons = listOf(
                    PersonModel(
                        id = 1,
                        inputName = "할미쬬",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                    PersonModel(
                        id = 2,
                        inputName = "춘식이",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                    PersonModel(
                        id = 3,
                        inputName = "좀비쬬",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                    PersonModel(
                        id = 4,
                        inputName = "고구마",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                    PersonModel(
                        id = 5,
                        inputName = "할미쬬",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                    PersonModel(
                        id = 6,
                        inputName = "춘식이",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                    PersonModel(
                        id = 7,
                        inputName = "좀비쬬",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                    PersonModel(
                        id = 8,
                        inputName = "고구마",
                        image = byteArrayOf(),
                        imageName = "",
                        homeDisplay = true,
                    ),
                ),
                images = List(20) {
                    "android.resource://com.example.metasearch/feature/home/drawable/ic_launcher_foreground".toUri()
                },
                eventSink = {},
            ),
        )
    }
}
