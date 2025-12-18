package com.example.metasearch.feature.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.ui.MetaSearchScaffold
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
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            Text("person screen")
        }
    }
}

@DevicePreview
@Composable
private fun PersonUiPreview() {
    MetaSearchTheme {
        PersonUi(
            state = PersonUiState(
                eventSink = {},
            ),
        )
    }
}
