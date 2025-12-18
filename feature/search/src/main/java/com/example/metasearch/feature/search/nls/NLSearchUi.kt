package com.example.metasearch.feature.search.nls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.feature.screens.NLSearchScreen
import com.example.metasearch.feature.screens.component.MetaSearchMainBottomBar
import com.example.metasearch.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(NLSearchScreen::class, ActivityRetainedComponent::class)
@Composable
fun NLSearchUi(
    modifier: Modifier = Modifier,
    state: NLSearchUiState,
) {
    MetaSearchScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MetaSearchMainBottomBar(
                modifier = modifier,
                currentTab = MetaSearchMainTabItem.SEARCH,
                onTabSelected = {
                    state.eventSink(NLSearchUiEvent.OnTabClick(it.screen))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            Text("NLSearch")
        }
    }
}

@DevicePreview
@Composable
private fun NLSearchUiPreview() {
    MetaSearchTheme {
        NLSearchUi(
            state = NLSearchUiState(
                eventSink = {},
            ),
        )
    }
}
