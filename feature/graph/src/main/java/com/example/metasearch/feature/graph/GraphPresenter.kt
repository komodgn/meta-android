package com.example.metasearch.feature.graph

import androidx.compose.runtime.Composable
import com.example.metasearch.feature.screens.GraphScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class GraphPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<GraphUiState> {

    @Composable
    override fun present(): GraphUiState {
        fun handleEvent(event: GraphUiEvent) {
            when (event) {
                is GraphUiEvent.OnTabClick -> {
                    navigator.resetRoot(event.screen)
                }
            }
        }

        return GraphUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(GraphScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator) : GraphPresenter
    }
}
