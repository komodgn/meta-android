package com.example.metasearch.feature.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.component.MetaSearchToast
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.model.PersonModel
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.core.ui.component.MetaSearchDialog
import com.example.metasearch.feature.person.component.PersonHeader
import com.example.metasearch.feature.person.component.PersonItem
import com.example.metasearch.feature.person.component.PersonSearchTextField
import com.example.metasearch.feature.screens.PersonScreen
import com.example.metasearch.feature.screens.component.MetaSearchMainBottomBar
import com.example.metasearch.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(PersonScreen::class, ActivityRetainedComponent::class)
@Composable
fun PersonUi(
    modifier: Modifier = Modifier,
    state: PersonUiState,
) {
    PersonToastEffect(
        showToast = state.showToast,
        eventSink = state.eventSink,
    )

    MetaSearchScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MetaSearchMainBottomBar(
                modifier = modifier,
                onTabSelected = {
                    state.eventSink(PersonUiEvent.OnTabClick(it.screen))
                },
                currentTab = MetaSearchMainTabItem.PERSON,
            )
        },
    ) { innerPadding ->
        PersonUiContent(
            state = state,
            innerPadding = innerPadding,
        )
    }

    if (state.showDeleteDialog) {
        MetaSearchDialog(
            title = stringResource(R.string.person_delete_dialog_title),
            content = {
                Text(
                    text = stringResource(
                        R.string.person_delete_dialog_content,
                        state.pendingDeletePersonName,
                    ),
                )
            },
            onConfirmRequest = {
                state.eventSink(PersonUiEvent.OnPersonDeleteConfirm)
            },
            onDismissRequest = {
                state.eventSink(PersonUiEvent.OnPersonDeleteCancel)
            },
            confirmButtonText = stringResource(R.string.person_delete_dialog_confirm_button),
            dismissButtonText = stringResource(R.string.person_delete_dialog_cancel_button),
        )
    }
}

@Composable
private fun PersonUiContent(
    state: PersonUiState,
    innerPadding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            PersonHeader()

            PersonSearchTextField(
                inputString = state.inputPersonNameString,
                onInputChange = { state.eventSink(PersonUiEvent.OnInputChange(it)) },
                onSearchClick = { state.eventSink(PersonUiEvent.OnPersonSearchClick(state.inputPersonNameString)) },
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    state.people,
                    key = { it.id },
                ) { person ->
                    PersonItem(
                        person = person,
                        onClick = { state.eventSink(PersonUiEvent.OnPersonClick(person.id)) },
                        onDeleteClick = { state.eventSink(PersonUiEvent.OnPersonDeleteClick(person.id)) },
                    )
                }
            }
        }

        MetaSearchToast(
            modifier = Modifier.align(Alignment.Center),
            isVisible = state.showToast,
            message = stringResource(R.string.person_delete_failed_toast_message),
        )
    }
}

@DevicePreview
@Composable
private fun PersonUiPreview() {
    MetaSearchTheme {
        PersonUi(
            state = PersonUiState(
                showDeleteDialog = true,
                people = listOf(
                    PersonModel(
                        id = 1L,
                        name = "춘식이",
                        inputName = "춘식이",
                        isHomeDisplay = true,
                    ),
                    PersonModel(
                        id = 2L,
                        name = "춘식이2",
                        inputName = "춘식이2",
                        isHomeDisplay = true,
                    ),
                    PersonModel(
                        id = 3L,
                        name = "춘식이3",
                        inputName = "춘식이3",
                        isHomeDisplay = true,
                    ),
                    PersonModel(
                        id = 4L,
                        name = "춘식이4",
                        inputName = "춘식이4",
                        isHomeDisplay = true,
                    ),
                ),
                eventSink = {},
            ),
        )
    }
}
