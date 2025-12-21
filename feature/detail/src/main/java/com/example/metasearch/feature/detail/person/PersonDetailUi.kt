package com.example.metasearch.feature.detail.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.feature.detail.person.component.PersonDetailHeader
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
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            PersonDetailHeader(
                onBackClick = {
                    state.eventSink(PersonDetailUiEvent.OnHeaderBackClick)
                },
            )
        }
    }
}

@DevicePreview
@Composable
private fun PersonDetailUiPreview() {
    MetaSearchTheme {
        PersonDetailUi(
            state = PersonDetailUiState(
                eventSink = {},
            ),
        )
    }
}
