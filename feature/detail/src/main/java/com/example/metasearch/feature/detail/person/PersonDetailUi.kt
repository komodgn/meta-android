package com.example.metasearch.feature.detail.person

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.LightGrey
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.model.PersonModel
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.core.ui.component.MetaSearchDialog
import com.example.metasearch.core.ui.component.MetaSearchLoadingIndicator
import com.example.metasearch.feature.detail.R
import com.example.metasearch.feature.detail.person.component.PersonDetailHeader
import com.example.metasearch.feature.detail.person.component.PersonEditDialogContent
import com.example.metasearch.feature.screens.PersonDetailScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(PersonDetailScreen::class, ActivityRetainedComponent::class)
@Composable
fun PersonDetailUi(
    modifier: Modifier = Modifier,
    state: PersonDetailUiState,
) {
    MetaSearchScaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        PersonDetailContent(
            state = state,
            innerPadding = innerPadding,
        )
    }

    if (state.isLoading) {
        MetaSearchLoadingIndicator()
    }

    if (state.showEditDialog) {
        MetaSearchDialog(
            title = stringResource(R.string.person_detail_screen_person_info_update_dialog_title),
            content = {
                PersonEditDialogContent(
                    editName = state.editName,
                    editPhone = state.editPhone,
                    editIsHomeDisplay = state.editIsHomeDisplay,
                    onEvent = { event -> state.eventSink(event) },
                )
            },
            onDismissRequest = { state.eventSink(PersonDetailUiEvent.OnEditCancel) },
            onConfirmRequest = { state.eventSink(PersonDetailUiEvent.OnEditSaveClick) },
            dismissButtonText = stringResource(R.string.person_detail_screen_cancel_button),
            confirmButtonText = stringResource(R.string.person_detail_screen_save_button),
        )
    }

    if (state.showMergeConfirmDialog) {
        MetaSearchDialog(
            title = stringResource(R.string.person_detail_merge_dialog_title),
            content = {
                Text(
                    text = stringResource(R.string.person_detail_merge_dialog_content),
                )
            },
            onDismissRequest = { state.eventSink(PersonDetailUiEvent.OnDismissMergeDialog) },
            onConfirmRequest = { state.eventSink(PersonDetailUiEvent.OnConfirmMergeSave) },
            dismissButtonText = stringResource(R.string.person_detail_merge_dialog_dismiss),
            confirmButtonText = stringResource(R.string.person_detail_merge_dialog_confirm),
        )
    }

    if (state.showPhotoSelectDialog) {
        Dialog(onDismissRequest = { state.eventSink(PersonDetailUiEvent.OnPhotoSelectCancel) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.person_detail_screen_profile_update_dialog_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MetaSearchTheme.spacing.spacing3),
                        horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing2),
                        contentPadding = PaddingValues(horizontal = MetaSearchTheme.spacing.spacing1),
                    ) {
                        items(state.person?.faces ?: emptyList()) { face ->
                            AsyncImage(
                                model = face.imageData,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = LightGrey,
                                        shape = CircleShape,
                                    )
                                    .clickable {
                                        state.eventSink(PersonDetailUiEvent.OnEditThumbnailClick(face.id))
                                    },
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonDetailContent(
    innerPadding: PaddingValues,
    state: PersonDetailUiState,
) {
    Column(
        modifier = Modifier.padding(innerPadding),
    ) {
        PersonDetailHeader(
            personName = state.person?.inputName ?: state.person?.name,
            onBackClick = { state.eventSink(PersonDetailUiEvent.OnHeaderBackClick) },
            onMenuClick = { state.eventSink(PersonDetailUiEvent.OnMenuClick) },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MetaSearchTheme.spacing.spacing4),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { state.eventSink(PersonDetailUiEvent.OnThumbnailClick) }
                    .border(2.dp, LightGrey, CircleShape),
                model = state.person?.representativeFace?.imageData,
                contentDescription = "Representative Image",
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing3))

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

@DevicePreview
@Composable
private fun PersonDetailUiPreview() {
    MetaSearchTheme {
        PersonDetailUi(
            state = PersonDetailUiState(
                isLoading = true,
                person = PersonModel(
                    id = 1L,
                    name = "춘식이",
                    inputName = "춘식이",
                ),
                showPhotoSelectDialog = false,
                eventSink = {},
            ),
        )
    }
}
