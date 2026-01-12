package com.metasearch.android.feature.detail.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchCircleImage
import com.metasearch.android.core.ui.component.MetaSearchDialog
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.core.ui.component.MetaSearchSquareImage
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.detail.person.component.PersonDetailHeader
import com.metasearch.android.feature.detail.person.component.PersonEditDialogContent
import com.metasearch.android.feature.screens.PersonDetailScreen
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
                            MetaSearchCircleImage(
                                model = face.imageData,
                                onClick = {
                                    state.eventSink(PersonDetailUiEvent.OnEditThumbnailClick(face.id))
                                },
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
            MetaSearchCircleImage(
                model = state.person?.representativeFace?.imageData,
                onClick = {
                    state.eventSink(PersonDetailUiEvent.OnThumbnailClick)
                },
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
                MetaSearchSquareImage(
                    model = uri,
                    onClick = {
                        state.eventSink(PersonDetailUiEvent.OnGridImageClick(uri))
                    },
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
                isLoading = false,
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
