package com.example.metasearch.feature.graph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.feature.screens.GraphScreen
import com.example.metasearch.feature.screens.component.MetaSearchMainBottomBar
import com.example.metasearch.feature.screens.component.MetaSearchMainTabItem
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(GraphScreen::class, ActivityRetainedComponent::class)
@Composable
fun GraphUi(
    modifier: Modifier = Modifier,
    state: GraphUiState,
) {
    MetaSearchScaffold(
        modifier = modifier,
        bottomBar = {
            MetaSearchMainBottomBar(
                modifier = modifier,
                currentTab = MetaSearchMainTabItem.GRAPH,
                onTabSelected = {
                    state.eventSink(GraphUiEvent.OnTabClick(it.screen))
                },
            )
        }
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Text("graph scree")
        }
    }
}

@DevicePreview
@Composable
private fun GraphUiPreview() {
    MetaSearchTheme {
        GraphUi(
            state = GraphUiState(
                eventSink = {},
            )
        )
    }
}
