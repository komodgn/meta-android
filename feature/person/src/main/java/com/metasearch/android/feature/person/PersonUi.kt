package com.metasearch.android.feature.person

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchDialog
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.feature.person.component.PersonItem
import com.metasearch.android.feature.person.component.PersonSearchTextField
import com.metasearch.android.feature.person.mock.personUiStateMock
import com.metasearch.android.feature.screens.PersonScreen
import com.metasearch.android.feature.screens.component.MetaSearchMainBottomBar
import com.metasearch.android.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(PersonScreen::class, ActivityRetainedComponent::class)
@Composable
fun PersonUi(
    modifier: Modifier = Modifier,
    state: PersonUiState,
) {
    PersonSideEffect(
        state = state,
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
            MetaSearchHeader(
                title = stringResource(R.string.person_screen_header),
                textAlign = TextAlign.Start,
                textStyle = MetaSearchTheme.typography.headlineSmall,
            )

            PersonSearchTextField(
                inputString = state.inputPersonNameString,
                onInputChange = { state.eventSink(PersonUiEvent.OnInputChange(it)) },
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
    }
}

@DevicePreview
@Composable
private fun PersonUiPreview() {
    MetaSearchTheme {
        PersonUi(
            state = personUiStateMock,
        )
    }
}

@DevicePreview
@Composable
private fun PersonUiDeleteDialogPreview() {
    MetaSearchTheme {
        PersonUi(
            state = personUiStateMock.copy(showDeleteDialog = true),
        )
    }
}
