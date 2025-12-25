package com.example.metasearch.feature.detail.person

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.model.PersonModel
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.feature.detail.person.component.PersonDetailHeader
import com.example.metasearch.feature.screens.PersonDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent
import com.example.metasearch.feature.detail.R

@CircuitInject(PersonDetailScreen::class, ActivityRetainedComponent::class)
@Composable
fun PersonDetailUi(
    modifier: Modifier = Modifier,
    state: PersonDetailUiState,
) {
    MetaSearchScaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            PersonDetailHeader(
                personName = state.person?.name,
                onBackClick = {
                    state.eventSink(PersonDetailUiEvent.OnHeaderBackClick)
                },
            )



            Text(
                modifier = Modifier.padding(MetaSearchTheme.spacing.spacing2),
                text = stringResource(R.string.person_detail_screen_grid_label),
                color = Neutral500,
            )
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(5),
            ) {
                items(state.photoUris) { uri ->
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(1.dp)
                            .clickable {
                                state.eventSink(PersonDetailUiEvent.OnGridImageClick(uri))
                            },
                        model = uri,
                        contentDescription = "Person Image",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun PersonDetailUiPreview() {
    MetaSearchTheme {
        PersonDetailUi(
            state = PersonDetailUiState(
                person = PersonModel(
                    id = 1L,
                    name = "춘식이",
                    inputName = "춘식이",
                ),
                photoUris = listOf(

                ),
                eventSink = {},
            ),
        )
    }
}
